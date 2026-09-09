package com.yennie.restapi;

import com.yennie.restapi.school.SchoolRequestDto;
import com.yennie.restapi.school.SchoolResponseDto;
import com.yennie.restapi.student.StudentDto;
import com.yennie.restapi.student.StudentResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** 使用随机端口发送真实 HTTP 请求；测试配置只连接 H2。 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentApiTests {
    @Autowired
    private Environment environment;
    private RestClient client;

    @BeforeEach
    void setUpClient() {
        client = RestClient.create("http://localhost:" + environment.getProperty("local.server.port"));
    }

    @Test
    void createsListsSearchesAndDeletesStudentThroughHttp() {
        var school = client.post().uri("/schools").contentType(MediaType.APPLICATION_JSON)
                .body(new SchoolRequestDto("API School")).retrieve().body(SchoolResponseDto.class);
        assertThat(school).isNotNull();
        assertThat(school.id()).isPositive();
        assertThat(school.name()).isEqualTo("API School");
        var schools = client.get().uri("/schools").retrieve().body(SchoolResponseDto[].class);
        assertThat(schools).contains(school);

        var request = new StudentDto("Yennie", "Peng", UUID.randomUUID() + "@example.com", 20, school.id());
        var student = client.post().uri("/students").contentType(MediaType.APPLICATION_JSON)
                .body(request).retrieve().body(StudentResponseDto.class);
        assertThat(student).isNotNull();
        assertThat(student.firstName()).isEqualTo("Yennie");
        assertThat(student.lastName()).isEqualTo("Peng");
        assertThat(student.age()).isEqualTo(20);
        assertThat(student.email()).isEqualTo(request.email());
        assertThat(student.schoolId()).isEqualTo(school.id());
        assertThat(student.schoolName()).isEqualTo("API School");

        var fetched = client.get().uri("/students/" + student.id())
                .retrieve().body(StudentResponseDto.class);
        assertThat(fetched).isEqualTo(student);
        assertThat(client.get().uri("/students").retrieve().body(StudentResponseDto[].class))
                .contains(student);
        assertThat(client.get().uri("/students/search/YEN").retrieve().body(StudentResponseDto[].class))
                .contains(student);

        var deleted = client.delete().uri("/students/" + student.id()).retrieve().toBodilessEntity();
        assertThat(deleted.getStatusCode().value()).isEqualTo(204);
        assertStatus(404, () -> client.get().uri("/students/" + student.id())
                .retrieve().toBodilessEntity());
    }

    @Test
    void rejectsMissingSchoolId() {
        var dto = new StudentDto("Test", "Student", "missing@example.com", 20, null);
        assertStatus(400, () -> client.post().uri("/students").contentType(MediaType.APPLICATION_JSON)
                .body(dto).retrieve().toBodilessEntity());
    }

    @Test
    void rejectsUnknownSchoolAndUnknownStudent() {
        var dto = new StudentDto("Test", "Student", "unknown@example.com", 20, -1);
        assertStatus(404, () -> client.post().uri("/students").contentType(MediaType.APPLICATION_JSON)
                .body(dto).retrieve().toBodilessEntity());
        assertStatus(404, () -> client.get().uri("/students/-1").retrieve().toBodilessEntity());
    }

    private void assertStatus(int expected, Runnable request) {
        assertThatThrownBy(request::run)
                .isInstanceOfSatisfying(RestClientResponseException.class,
                        error -> assertThat(error.getStatusCode().value()).isEqualTo(expected));
    }
}

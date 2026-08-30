package com.yennie.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示常见 REST 请求参数如何映射到 Java 方法参数。
 */
@RestController
public class FirstController {

    @GetMapping("/hello-2")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String sayHello() {
        return "Hello from my first controller";
    }

    /** 接收整个纯文本请求体。 */
    @PostMapping("/post")
    public String postMessage(@RequestBody String message) {
        return "Request accepted and message is: " + message;
    }

    /** Jackson 通过 getter/setter 在 JSON 与普通 JavaBean 之间转换。 */
    @PostMapping("/post-order")
    public Order postOrder(@RequestBody Order order) {
        return order;
    }

    /** Record 自带构造器和访问方法，也可以直接用于 JSON 映射。 */
    @PostMapping("/post-order-record")
    public OrderRecord postOrderRecord(@RequestBody OrderRecord orderRecord) {
        return orderRecord;
    }

    // 示例：http://localhost:8080/hello/Yennie
    @GetMapping("/hello/{userName}")
    public String pathVariable(@PathVariable String userName) {
        return "My value = " + userName;
    }

    // 示例：http://localhost:8080/hello?user-name=Yennie&user-lastname=Peng
    @GetMapping("/hello")
    public String requestParameters(
            @RequestParam("user-name") String userName,
            @RequestParam("user-lastname") String lastName
    ) {
        return "My value = " + userName + " " + lastName;
    }
}

package com.yennie.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
// 命名约定：实体名.仓库方法名。JPQL 中用实体及 Java 属性，不是表/列名。
@NamedQueries(
        {
                @NamedQuery(
                        name = "Author.findByNamedQuery",
                        // = 表示恰好等于；>= 则会包含所有更大的年龄。
                        query = "select a from Author a where a.age = :age"
                ),

                @NamedQuery(
                        name="Author.updateByNamedQuery",
                        // 无 WHERE：会更新全部作者，调用前务必确认目标数据库。
                        query = "update Author a set a.age = :age"
                )
        }
)



//@Table(name = "AUTHOR_TBL")
public class Author extends BaseEntity {



//    @Column(name = "f_name",length = 50)
    private String firstName;
    private String lastName;
//    @Column(unique = true, nullable = false)
    private String email;
    private int age;
//    @Column(updatable = false, nullable = false)
//    private LocalDateTime createdAt;
//    @Column(insertable = false)
//    private LocalDateTime lastModified;
    // 排除关联集合，避免打印触发懒加载，以及双向关系递归。
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "authors")
    private List<Course>  courses;
}

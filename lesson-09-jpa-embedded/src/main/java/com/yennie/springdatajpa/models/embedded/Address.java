package com.yennie.springdatajpa.models.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可嵌入的地址值对象：把相关属性组合成一个 Java 对象，便于复用。
 * 本例没有独立的 address 表，也没有地址 ID；数据随所属 Order 一起保存。
 * 与 @OneToOne 不同，这里不需要 address_id 外键，也不需要单独保存 Address。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // 声明这个类可以被其他持久化类型嵌入；使用位置标记 @Embedded。
public class Address {
    private String streetName;
    private String houseNumber;
    // 在当前 Spring Boot 默认命名规则下，字段对应 _order.zip_code。
    private String zipCode;
}

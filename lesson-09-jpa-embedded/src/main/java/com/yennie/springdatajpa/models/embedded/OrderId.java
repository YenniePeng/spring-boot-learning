package com.yennie.springdatajpa.models.embedded;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 订单的复合主键：username 和 orderDate 两列共同确定一条订单记录。
 * 同一用户可以有不同时间的订单，但不能重复使用完全相同的两项主键值。
 * 持久化前应设置两项非空值；本例不使用 @GeneratedValue，保存后不要修改主键。
 * 时间应使用数据库能精确存储的精度，避免纳秒截断导致按主键查询不匹配。
 */
@Data // 生成基于两项字段的 equals/hashCode，使相同主键值的对象可正确比较。
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderId implements Serializable {
    // 保留可序列化能力；static 字段不会成为数据库列或构造方法参数。
    private static final long serialVersionUID = 1L;

    private String username;
    private LocalDateTime orderDate;
}

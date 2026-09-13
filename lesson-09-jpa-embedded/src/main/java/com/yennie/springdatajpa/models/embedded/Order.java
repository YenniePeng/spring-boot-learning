package com.yennie.springdatajpa.models.embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 嵌入对象在 Java 中有层次，在本例数据库中展开成同一张 _order 表的列：
 * orderId.username  -> username   ┐ 共同组成主键
 * orderId.orderDate -> order_date ┘
 * address.streetName/houseNumber/zipCode -> street_name/house_number/zip_code
 * 不会生成 order_id 对象列，也不会为 Address 或 OrderId 单独建表。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_order") // 避免使用 SQL 关键字 ORDER 作为未转义表名。
public class Order {
    @EmbeddedId // 嵌入的对象承担主键职责；不同于下面的普通 @Embedded。
    private OrderId orderId;

    // Address 没有独立生命周期，保存 Order 时会同时保存这些地址列。
    // 若同一个实体需要两个 Address（如收货/账单地址），可在使用处添加
    // @AttributeOverrides + @AttributeOverride，为两组属性指定不同列名。
    // 修改托管 Order 的地址属性，可在事务 flush 时同步到订单表。
    @Embedded
    private Address address;

    private String orderInfo;
    private String anotherField;
}

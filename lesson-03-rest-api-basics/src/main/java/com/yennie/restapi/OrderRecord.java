package com.yennie.restapi;

/**
 * Record 是只承载数据时更简洁的写法，自动提供构造器、访问方法和 toString。
 */
public record OrderRecord(
        String customerName,
        String productName,
        int quantity
) {
}

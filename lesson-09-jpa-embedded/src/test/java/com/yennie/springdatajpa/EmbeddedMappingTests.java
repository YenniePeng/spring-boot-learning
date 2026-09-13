package com.yennie.springdatajpa;

import com.yennie.springdatajpa.models.embedded.Address;
import com.yennie.springdatajpa.models.embedded.Order;
import com.yennie.springdatajpa.models.embedded.OrderId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EmbeddedMappingTests {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void embeddedAddressUsesOrderColumnsAndCompositeIdFindsExactOrder() {
        var date = LocalDateTime.of(2026, 9, 13, 16, 0);
        var order = new Order(new OrderId("yennie", date),
                new Address("Spring Road", "8", "200000"), "JPA course", "notes");
        var second = new Order(new OrderId("yennie", date.plusMinutes(1)),
                new Address("Other Road", "9", "100000"), "Another order", null);
        entityManager.persist(order);
        entityManager.persist(second);
        // 清空一级缓存，确保下面是从数据库重新读取，而不是拿到原 Java 对象。
        entityManager.flush();
        entityManager.clear();

        var loaded = entityManager.find(Order.class, new OrderId("yennie", date));
        assertNotNull(loaded);
        assertEquals("JPA course", loaded.getOrderInfo());
        assertEquals("200000", loaded.getAddress().getZipCode());
        assertEquals("Another order", entityManager.find(Order.class,
                new OrderId("yennie", date.plusMinutes(1))).getOrderInfo());
        // 原生 SQL 验证地址确实展开在 _order 表中。
        Object[] row = (Object[]) entityManager.createNativeQuery(
                "select street_name, house_number, zip_code from _order "
                        + "where username = :user and order_date = :date")
                .setParameter("user", "yennie").setParameter("date", date).getSingleResult();
        assertArrayEquals(new Object[]{"Spring Road", "8", "200000"}, row);
    }

    @Test
    void changingEmbeddedAddressUpdatesOwningOrder() {
        var id = new OrderId("address-update", LocalDateTime.of(2026, 9, 13, 17, 0));
        entityManager.persist(new Order(id, new Address("Old Road", "1", "200000"), null, null));
        entityManager.flush();
        entityManager.clear();

        var managed = entityManager.find(Order.class, id);
        managed.getAddress().setStreetName("New Road");
        // 托管对象的嵌入属性参与脏检查，无须单独 save(address)。
        entityManager.flush();
        entityManager.clear();
        assertEquals("New Road", entityManager.find(Order.class, id).getAddress().getStreetName());
    }
}

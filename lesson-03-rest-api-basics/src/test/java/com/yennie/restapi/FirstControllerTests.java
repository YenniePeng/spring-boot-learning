package com.yennie.restapi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FirstControllerTests {

    private final FirstController controller = new FirstController();

    @Test
    void readsPathAndQueryParameters() {
        assertThat(controller.pathVariable("Yennie")).isEqualTo("My value = Yennie");
        assertThat(controller.requestParameters("Yennie", "Peng"))
                .isEqualTo("My value = Yennie Peng");
    }

    @Test
    void returnsRequestBodyObjects() {
        var order = new Order();
        order.setCustomerName("Lucy");
        order.setProductName("iPhone");
        order.setQuantity(1);

        assertThat(controller.postOrder(order)).isSameAs(order);

        var record = new OrderRecord("Lucy", "iPhone", 1);
        assertThat(controller.postOrderRecord(record)).isSameAs(record);
    }
}

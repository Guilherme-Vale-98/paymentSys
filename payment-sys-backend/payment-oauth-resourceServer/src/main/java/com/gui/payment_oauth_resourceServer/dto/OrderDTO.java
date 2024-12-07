package com.gui.payment_oauth_resourceServer.dto;

import java.util.List;

public class OrderDTO {

        private Long userId;
        private List<OrderItemDTO> items;
        private String status;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public List<OrderItemDTO> getItems() {
            return items;
        }

        public void setItems(List<OrderItemDTO> items) {
            this.items = items;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    public OrderDTO(Long userId, List<OrderItemDTO> items, String status) {
        this.userId = userId;
        this.items = items;
        this.status = status;
    }
}

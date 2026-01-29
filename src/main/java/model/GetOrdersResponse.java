package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersResponse {
    private boolean success;
    private List<Orders> orders;
    private Integer total;
    private Integer totalToday;
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Orders {
        private String _id;
        private List<String> ingredients;
        private String status;
        private String name;
        private String createdAt;
        private String updatedAt;
        private Integer number;
    }
}

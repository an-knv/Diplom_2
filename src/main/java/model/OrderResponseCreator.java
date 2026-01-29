package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseCreator {
    private boolean success;
    private String name;
    private OwnerDetails owner;
    private OrderDetails order;
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetails {
        private List<Ingredients> ingredients;
        private String status;
        private String name;
        private String createdAt;
        private String updatedAt;
        private Integer number;
        private Integer price;
        private String _id;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Ingredients {
            private String _id;
            private String name;
            private String type;
            private int proteins;
            private int fat;
            private int carbohydrates;
            private int calories;
            private int price;
            private String image;
            private String image_mobile;
            private String image_large;
            private int __v;
        }
    }
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OwnerDetails {
            private String name;
            private String email;
            private String createdAt;
            private String updatedAt;
        }

}



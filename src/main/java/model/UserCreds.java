package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreds {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String email;
        private String name;
    }

}

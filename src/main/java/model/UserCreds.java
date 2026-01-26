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
    public static UserCreds fromUserRegisterTrue(UserRegister userRegister,
                                             String accessToken,
                                             String refreshToken) {
        User user = new User(userRegister.getEmail(), userRegister.getName());
        return new UserCreds(true, user, accessToken, refreshToken,null);
    }
    public static UserCreds fromUserRegisterFalse(UserRegister userRegister, String errorMessage) {
        User user = new User(userRegister.getEmail(), userRegister.getName());
        return new UserCreds(false, null, null, null, errorMessage);
    }
}

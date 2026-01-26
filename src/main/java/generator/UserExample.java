package generator;

import com.github.javafaker.Faker;
import model.UserRegister;

public class UserExample {
    private static final Faker faker = new Faker();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String password;
        private String name;

        public Builder() {
            this.email = faker.internet().emailAddress();
            this.password = faker.internet().password(6, 10);
            this.name = faker.name().firstName();
        }
        public UserRegister build() {
            return new UserRegister(email, password, name);
        }
    }

    public static UserRegister getRandomUser() {
        return builder().build();
    }

}

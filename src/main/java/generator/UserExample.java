package generator;

import com.github.javafaker.Faker;

public class UserExample {
    public static final Faker faker = new Faker();

    private String email;
    private String password;
    private String name;

    private UserExample(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserExample randomUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(6, 10);
        String name = faker.name().firstName();

        return new UserExample(email, password, name);
    }
    public static UserExample userWrongPassword() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(5,5);
        String name = faker.name().firstName();

        return new UserExample(email, password, name);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}

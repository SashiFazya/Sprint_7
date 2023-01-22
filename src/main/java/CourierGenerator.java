import com.github.javafaker.Faker;

public class CourierGenerator {
    private static String login;
    private static String password;
    private static String firstName;
    private static Faker faker = new Faker();

    public static Courier randomCourier(){
        password = faker.internet().password(true);
        firstName = faker.name().firstName();
        login = firstName + (int) (Math.random() * 100);

        return new Courier(login, password, firstName);
    }
}

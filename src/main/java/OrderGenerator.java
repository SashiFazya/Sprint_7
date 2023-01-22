import com.github.javafaker.Faker;

import java.util.List;

public class OrderGenerator {
    private static String firstName;
    private static  String lastName;
    private static  String address;
    private static  String metroStation;
    private static  String phone;
    private static  int rentTime;
    private static  String deliveryDate;
    private static  String comment;
    private static  List<Color> colors;
    private static Faker faker = new Faker();

    public Order randomOrderNoColor(){
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        address = faker.address().fullAddress();
        metroStation = faker.address().state();
        phone = faker.phoneNumber().phoneNumber();
        rentTime = (int) (Math.random() * 100);
        deliveryDate = String.format("%s", faker.date().birthday());
        comment = faker.witcher().quote();
        return new Order(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment
        );
    }
}

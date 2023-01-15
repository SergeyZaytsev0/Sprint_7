import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.dto.OrderRequest;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrdersTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    OrderRequest orderRequest;
    OrderClient orderClient;
    int track;

    public CreateOrdersTest(String firstName, String lastName, String address, String metroStation, String phone,
                            int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные {0},{1},{2},{3}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"Дмитрий", "Гулов", "Гоголя, 2, 22", "90", "+7 900 000 00 44", 6, "2024-03-06", "Привет Толику", new String[]{"BLACK"}},
                {"Антон", "Киров", "Чехова, 123", "56", "+7 900 555 55 55", 5, "2022-12-31", "SALAM", new String[]{"GREY", "BLACK"}},
                {"Павел", "Жмыхов", "Таганрог, Чукоткина, 15, 27", "4", "+7 900 222 22 22", 7, "2023-02-02", "Кот", new String[]{"GREY"}},
                {"Ахмед", "Ахмедов", "Кузьмич", "93", "+7 900 333 33 33", 1, "2023-05-11", "Утки ууу", new String[]{""}},
        };
    }

    @Test
    @DisplayName("API POST /api/v1/orders testing creating parameterized orders")
    public void createOrderTest() {
        orderRequest = new OrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate,
                comment, color);
        orderClient = new OrderClient();
        track = orderClient.createOrder(orderRequest)
                .assertThat().statusCode(SC_CREATED)
                .and().body("track", notNullValue())
                .extract().path("track");
        System.out.println(track);
    }
}

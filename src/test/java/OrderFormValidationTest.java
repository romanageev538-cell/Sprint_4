import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.openqa.selenium.WebDriver;
import ru.praktikum.scooter.pageobject.MainPage;
import ru.praktikum.scooter.pageobject.OrderPersonalInfoPage;
import ru.praktikum.scooter.pageobject.OrderDeliveryDetailsPage;
import ru.praktikum.scooter.pageobject.OrderConfirmationPage;

import java.util.Arrays;
import java.util.Collection;



@RunWith(Parameterized.class)
public class OrderFormValidationTest {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();

    private WebDriver driver;
    private MainPage mainPage;
    private OrderPersonalInfoPage stepOne;
    private OrderDeliveryDetailsPage stepSecond;
    private OrderConfirmationPage stepThree;

    // Параметры для параметризованного теста
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String deliveryDateClickText;
    private final String rentalDurationText;

    public OrderFormValidationTest(
            String firstName,
            String lastName,
            String address,
            String metroStation,
            String phone,
            String deliveryDateClickText,
            String rentalDurationText
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.deliveryDateClickText = deliveryDateClickText;
        this.rentalDurationText = rentalDurationText;
    }

    @Before
    public void setUp() {
        driver = driverFactory.getDriver();
        driver.manage().window().maximize();

        mainPage = new MainPage(driver);
        mainPage.openYandexSamokat();

        stepOne = new OrderPersonalInfoPage(driver);
        stepSecond = new OrderDeliveryDetailsPage(driver);
        stepThree = new OrderConfirmationPage(driver);
    }

    @After
    public void tearDown() {
        // Гарантированно закрываем драйвер после каждого теста
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    public void testOrderFullFlow() {
        // Шаг 0: переход к оформлению
        mainPage.clickOrderButton("top");

        // Шаг 1: заполнение персональных данных
        stepOne.fillFirstStep(firstName, lastName, address, metroStation, phone);
        stepOne.clickNext();

        // Шаг 2: выбор даты и длительности аренды
        stepSecond.fillSecondStepAndSubmit(deliveryDateClickText, rentalDurationText);

        // Шаг 3: подтверждение заказа (в методе уже есть ожидание появления «Номер заказа»)
        stepThree.confirmOrder();

    }

    @Parameterized.Parameters(name = "Тест заказа: {0} {1}, адрес: {2}, метро: {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Иван", "Иванов", "ул. Ленина, д. 1", "Фрунзенская", "+79990000001", "05.08.2026", "двое суток"},
                {"Пётр", "Петров", "пр. Мира, д. 5", "Сокольники", "+79991112233", "08.12.2027", "трое суток"},
        });
    }
}
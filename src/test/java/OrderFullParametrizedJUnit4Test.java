import PageObject.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderFullParametrizedJUnit4Test {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();

    private WebDriver driver;
    private MainPage mainPage;
    private OrderStep1Page stepOne;
    private OrderStep2Page stepSecond;
    private OrderStep3Page stepThree;

    // Параметры для параметризованного теста
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String deliveryDateClickText;
    private final String rentalDurationText;

    public OrderFullParametrizedJUnit4Test(
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

        stepOne = new OrderStep1Page(driver);
        stepSecond = new OrderStep2Page(driver);
        stepThree = new OrderStep3Page(driver);
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
        stepOne.fillStep1(firstName, lastName, address, metroStation, phone);
        stepOne.clickNext();

        // Шаг 2: выбор даты и длительности аренды
        stepSecond.fillStep2AndSubmit(deliveryDateClickText, rentalDurationText);

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
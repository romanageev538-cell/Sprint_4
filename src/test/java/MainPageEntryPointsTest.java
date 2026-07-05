import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ru.praktikum.scooter.pageobject.MainPage;
import ru.praktikum.scooter.pageobject.OrderPersonalInfoPage;

import static org.junit.Assert.assertTrue;

public class MainPageEntryPointsTest {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();

    private MainPage mainPage;
    private OrderPersonalInfoPage personalInfoPage; // экземпляр страницы, а не класс

    @Before
    public void setUp() {
        var driver = driverFactory.getDriver();
        mainPage = new MainPage(driver);
        personalInfoPage = new OrderPersonalInfoPage(driver); // создаём экземпляр
        mainPage.openYandexSamokat();
    }

    @After
    public void tearDown() {
        // Закрытие драйвера берёт на себя DriverFactory
    }

    @Test
    public void testTopOrderButtonOpensPersonalInfoPage() {
        // Клик по кнопке «Заказать» вверху
        mainPage.clickOrderButton("top");

            // Ждём и проверяем загрузку страницы
            personalInfoPage.waitForPageToLoad();

            assertTrue(
                    "Страница ввода персональных данных не загрузилась после клика по кнопке «Заказать» (верх)",
                    personalInfoPage.isPageLoaded()
            );
        }

    @Test
    public void testBottomOrderButtonOpensPersonalInfoPage() {
        // Возвращаемся на главную, чтобы начать с чистого состояния
        mainPage.openYandexSamokat();

        // Клик по кнопке «Заказать» внизу
        mainPage.clickOrderButton("bottom");

        // Ждём и проверяем загрузку страницы
        personalInfoPage.waitForPageToLoad();
        assertTrue(
                "Страница ввода персональных данных не загрузилась после клика по кнопке «Заказать» (низ)",
                personalInfoPage.isPageLoaded()
        );
    }
}
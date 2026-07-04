import PageObject.MainPage;
import PageObject.MainPage;
import PageObject.OrderStep1Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class MainPageEntryPointsTest {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();

    private MainPage mainPage;
    private OrderStep1Page orderStep1Page;
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = driverFactory.getDriver();
        mainPage = new MainPage(driver);
        orderStep1Page = new OrderStep1Page(driver);
        mainPage.openYandexSamokat();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    public void testOrderButtonTopLeadsToStep1() {
        // Клик по кнопке «Заказать» вверху
        mainPage.clickOrderButton("top");

        // Ждём появления контейнера приложения (шаг 1 формы)
        orderStep1Page.waitForAppContainer();

        // Если дошли сюда — значит, элемент появился за время ожидания.
        // Можно добавить дополнительную проверку, если нужно:
        assertTrue("Контейнер App_App__15LM- не появился после клика 'top'",
                driver.findElements(By.className("App_App__15LM-")).size() > 0);
    }

    @Test
    public void testOrderButtonBottomLeadsToStep1() {
        // Перезагружаем страницу, чтобы снова быть на главной
        mainPage.openYandexSamokat();

        // Клик по кнопке «Заказать» внизу
        mainPage.clickOrderButton("bottom");

        // Ждём появления контейнера приложения
        orderStep1Page.waitForAppContainer();

        assertTrue("Контейнер App_App__15LM- не появился после клика 'bottom'",
                driver.findElements(By.className("App_App__15LM-")).size() > 0);
    }
}
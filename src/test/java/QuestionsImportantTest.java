import PageObject.MainPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QuestionsImportantTest {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();

    private WebDriver driver;
    private MainPage mainPage;

    // Выносим ожидаемые тексты в константу: это чище, чем инициализировать в поле экземпляра
    private static final List<String> EXPECTED_TEXTS = Arrays.asList(
            "Сутки — 400 рублей. Оплата курьеру — наличными или картой.",
            "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.",
            "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.",
            "Только начиная с завтрашнего дня. Но скоро станем расторопнее.",
            "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.",
            "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.",
            "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.",
            "Да, обязательно. Всем самокатов! И Москве, и Московской области."
    );

    @Before
    public void setUp() {
        driver = driverFactory.getDriver();
        driver.manage().window().maximize();

        mainPage = new MainPage(driver);
        mainPage.openYandexSamokat();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Ошибка при закрытии WebDriver: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }

    @Test
    public void testAllAccordionItems() {
        int count = mainPage.getQuestionsCount();

        assertFalse(
                "Не найдено ни одного вопроса в аккордеоне (count = 0)",
                count == 0
        );

        assertEquals(
                "Количество вопросов (" + count + ") не совпадает с количеством ожидаемых текстов (" + EXPECTED_TEXTS.size() + ")",
                EXPECTED_TEXTS.size(),
                count
        );

        for (int i = 0; i < count; i++) {
            // 1. Кликаем на заголовок вопроса (стрелочку)
            mainPage.clickQuestionButton(i);

            // 2. Проверяем, что панель раскрылась
            assertTrue(
                    "Панель вопроса №" + i + " не открылась после клика",
                    mainPage.isQuestionPanelVisible(i)
            );

            // 3. Получаем текст и сверяем с эталоном
            String actualText = mainPage.getQuestionText(i);
            String expectedText = EXPECTED_TEXTS.get(i);

            assertEquals(
                    "Текст для вопроса №" + i + " не совпадает.\n" +
                            "Ожидалось: [" + expectedText + "]\n" +
                            "Фактически:  [" + actualText + "]",
                    expectedText,
                    actualText
            );
        }
    }
}
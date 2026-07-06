
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import ru.praktikum.scooter.pageobject.MainPage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

    @RunWith(Parameterized.class)
    public class QuestionsImportantParametrizedTest {

        @Rule
        public DriverFactory driverFactory = new DriverFactory();

        private WebDriver driver;
        private MainPage mainPage;

        // Параметры для каждого прогона: индекс вопроса и ожидаемый текст
        private final int index;
        private final String expectedText;

        public QuestionsImportantParametrizedTest(int index, String expectedText) {
            this.index = index;
            this.expectedText = expectedText;
        }

        @Parameterized.Parameters(name = "Вопрос #{0}: проверка текста")
        public static Collection<Object[]> data() {
            List<String> texts = Arrays.asList(
                    "Сутки — 400 рублей. Оплата курьеру — наличными или картой.",
                    "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.",
                    "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.",
                    "Только начиная с завтрашнего дня. Но скоро станем расторопнее.",
                    "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.",
                    "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.",
                    "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.",
                    "Да, обязательно. Всем самокатов! И Москве, и Московской области."
            );

            Object[][] result = new Object[texts.size()][2];
            for (int i = 0; i < texts.size(); i++) {
                result[i][0] = i;
                result[i][1] = texts.get(i);
            }
            return Arrays.asList(result);
        }

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
        public void checkAccordionItem() {
            // 1. Клик на заголовок вопроса
            mainPage.clickQuestionButton(index);

            // 2. Проверка, что панель раскрылась
            assertTrue(
                    "Панель вопроса №" + index + " не открылась после клика",
                    mainPage.isQuestionPanelVisible(index)
            );

            // 3. Получение и сравнение текста
            String actualText = mainPage.getQuestionText(index);
            assertEquals(
                    "Текст для вопроса №" + index + " не совпадает.\n" +
                            "Ожидалось: [" + expectedText + "]\n" +
                            "Фактически:  [" + actualText + "]",
                    expectedText,
                    actualText
            );
        }
    }



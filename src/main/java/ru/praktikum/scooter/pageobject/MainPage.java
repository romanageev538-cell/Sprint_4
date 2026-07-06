package ru.praktikum.scooter.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {

    //Кнопка "Заказать" вверху страницы
    private static final By ORDER_BUTTON_TOP = By.cssSelector(".Header_Nav__AGCXC button.Button_Button__ra12g");
    //Кнопка "Заказать" внизу страницы
    private static final By ORDER_BUTTON_BOTTOM = By.cssSelector("button.Button_Button__ra12g.Button_Middle__1CSJM");
    /**
    // * Локатор для всех заголовков аккордеона (вопросов FAQ).
    // * Ищет элементы по атрибуту id, который начинается с 'accordion__heading-'.
    // * Такой подход позволяет одним селектором находить все заголовки,
    // * не привязываясь к конкретным индексам.
    */
    private static final By ACCORDION_HEADING_PREFIX = By.cssSelector("[id^='accordion__heading-']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openYandexSamokat() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    /**
     * Клик по кнопке «Заказать»: top/bottom.
     */
    public void clickOrderButton(String entryPoint) {
        String normalized = entryPoint != null ? entryPoint.toLowerCase() : "";

        By locator;
        if ("top".equals(normalized)) {
            locator = ORDER_BUTTON_TOP;
        } else if ("bottom".equals(normalized)) {
            locator = ORDER_BUTTON_BOTTOM;
        } else {
            throw new IllegalArgumentException(
                    "Неизвестная точка входа: '" + entryPoint + "'. Допустимые: 'top', 'bottom'."
            );
        }

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        clickWithJs(element);
    }

    /**
     * Клик на заголовок вопроса аккордеона по индексу.
     */
    public void clickQuestionButton(int index) {
        String id = "accordion__heading-" + index;
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        clickWithJs(element);
    }

    /**
     * Получение текста панели вопроса по индексу.
     */
    public String getQuestionText(int index) {
        String id = "accordion__panel-" + index;
        WebElement panel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        return panel.getText();
    }

    /**
     * Проверка видимости панели вопроса (true/false без падения теста).
     */
    public boolean isQuestionPanelVisible(int index) {
        String id = "accordion__panel-" + index;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Подсчёт вопросов через заранее объявленный локатор.
     */
    public int getQuestionsCount() {
        List<WebElement> elements = driver.findElements(ACCORDION_HEADING_PREFIX);
        return elements.size();
    }

    /**
     * JS‑клик: обходит оверлеи и ElementClickInterceptedException.
     */
    private void clickWithJs(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}

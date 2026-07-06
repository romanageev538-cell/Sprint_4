package ru.praktikum.scooter.pageobject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPersonalInfoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы полей
    //Поле "Имя"
    private static final By FIELD_FIRST_NAME = By.cssSelector("input[placeholder*='Имя']");
    //Поле "Фамилия"
    private static final By FIELD_LAST_NAME = By.cssSelector("input[placeholder*='Фамилия']");
    //Поле "Адреса"
    private static final By FIELD_ADDRESS = By.cssSelector("input[placeholder*='Адрес: куда привезти заказ']");
    //Поле "Станция метро"
    private static final By FIELD_METROSTATION = By.cssSelector("input[placeholder*='Станция метро']");
    //Поле "Номера телефона"
    private static final By FIELD_PHONE = By.cssSelector("input[placeholder*='Телефон: на него позвонит курьер']");
    // Конпка "Далее"
    private static final By BUTTON_NEXT = By.cssSelector(".Button_Button__ra12g.Button_Middle__1CSJM");

    // Локатор контейнера приложения (для проверки перехода на шаг 1)
    private static final By APP_CONTAINER = By.className("App_App__15LM-");

    public OrderPersonalInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Ждёт, пока ключевой контейнер страницы станет видимым.
     * Если не появится за 10 секунд — выбросит TimeoutException.
     */
    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(APP_CONTAINER));
    }

    /**
     * Проверяет, загружена ли страница.
     * Возвращает true, если контейнер виден, иначе false.
     * Не ломает тест, удобен для assert в JUnit.
     */
    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(APP_CONTAINER));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Заполняет все поля первого шага формы.
     */
    public void fillFirstStep(String firstName, String lastName, String address, String metroStation, String phone) {
        fillField(FIELD_FIRST_NAME, firstName);
        fillField(FIELD_LAST_NAME, lastName);
        fillField(FIELD_ADDRESS, address);

        // Метро требует отдельной логики из-за автокомплита
        selectMetroStationWithAutocomplete(metroStation);

        fillField(FIELD_PHONE, phone);
    }

    /**
     * Универсальный метод для заполнения поля: ждёт кликабельности, очищает, вводит текст.
     */
    private void fillField(By locator, String value) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locator));
        field.clear();
        field.sendKeys(value);
    }

    /**
     * Выбор станции метро через автокомплит.
     * Логика: клик → ввод текста → пауза для подгрузки списка → стрелка вниз → Enter.
     */
    public void selectMetroStationWithAutocomplete(String metroStation) {
        WebElement metroField = wait.until(ExpectedConditions.elementToBeClickable(FIELD_METROSTATION));
        metroField.click();
        metroField.clear();
        metroField.sendKeys(metroStation);

        metroField.sendKeys(Keys.DOWN, Keys.ENTER);
    }

    /**
     * Нажимает кнопку «Далее» через JavaScript.
     * JS‑клик нужен, чтобы обойти полупрозрачные оверлеи, которые блокируют обычный клик.
     */
    public void clickNext() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(BUTTON_NEXT));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }
}
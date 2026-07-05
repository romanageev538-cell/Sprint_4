package ru.praktikum.scooter.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderConfirmationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Кнопка «Да» в модалке
    private static final By BTN_CONFIRM_YES = By.xpath(
            ".//button[contains(@class, 'Button_Button') and normalize-space() = 'Да']"
    );

    // Элемент с текстом «Номер заказа» — наш индикатор успешного оформления
    // Используем XPath по тексту: ищем любой элемент, содержащий эту фразу
    private static final By SUCCESS_INDICATOR = By.xpath(
            "//*[contains(text(), 'Номер заказа')]"
    );

    public OrderConfirmationPage(WebDriver driver) {
        this.driver = driver;
        // 20 секунд — хороший запас для Firefox с анимациями и рендером
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Подтверждение заказа:
     * 1. Ждём кнопку «Да».
     * 2. Кликаем через JS (надёжнее в Firefox, обходит перекрывающие слои).
     * 3. Ждём появления элемента с текстом «Номер заказа».
     */
    public void confirmOrder() {
        // 1. Находим кнопку «Да»
        WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(BTN_CONFIRM_YES));

        // 2. Клик через JS — это спасает от ElementNotInteractableException в Firefox
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesBtn);

        // 3. Ждём появления индикатора успеха — элемента с текстом «Номер заказа»
        wait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_INDICATOR));
    }
}
package PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderStep2Page {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Поле «Когда привести самокат»
    private static final By FIELD_DELIVERY_DATE = By.cssSelector("input[placeholder*='Когда привезти самокат']");

    // Поле «Срок аренды» (выпадающий список)
    private static final By FIELD_RENTAL_DURATION = By.className("Dropdown-placeholder");

    // Кнопка «Заказать»
    private static final By BUTTON_ORDER = By.xpath(".//button[contains(@class, 'Button_Button') and contains(@class, 'Button_Middle') and normalize-space()='Заказать']");

    // Выпадающий список с опциями сроков аренды
    private static final By DROPDOWN_MENU = By.cssSelector(".Dropdown-menu");
    private static final By DROPDOWN_OPTIONS = By.cssSelector(".Dropdown-option");

    public OrderStep2Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Заполняет шаг 2 и отправляет форму.
     */
    public void fillStep2AndSubmit(String deliveryDateText, String rentalDurationText) {
        selectDeliveryDate(deliveryDateText);
        selectFromDropdown(FIELD_RENTAL_DURATION, rentalDurationText);

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(BUTTON_ORDER));
        clickWithJs(btn);
    }

    /**
     * Вводит дату доставки, подтверждает Enter, проверяет, что значение установилось.
     */
    private void selectDeliveryDate(String dateText) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(FIELD_DELIVERY_DATE));
        field.clear();
        field.sendKeys(dateText);
        field.sendKeys(Keys.ENTER);

        // Ждём, пока поле реально примет значение
        wait.until(driver -> !field.getAttribute("value").isEmpty());

        String actualValue = field.getAttribute("value");
        if (!actualValue.contains(dateText)) {
            throw new AssertionError(
                    "В поле даты не появилось ожидаемое значение: было '" + actualValue + "', ожидалось '" + dateText + "'"
            );
        }
    }

    /**
     * Открывает дропдаун, ищет опцию по тексту и кликает.
     * Если опция не найдена — падает с понятным сообщением.
     */
    private void selectFromDropdown(By fieldLocator, String optionText) {
        WebElement dropdownField = wait.until(ExpectedConditions.elementToBeClickable(fieldLocator));
        dropdownField.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(DROPDOWN_MENU));
        List<WebElement> options = driver.findElements(DROPDOWN_OPTIONS);

        for (WebElement option : options) {
            if (option.getText().equals(optionText)) {
                option.click();
                return; // Успешно выбрали — выходим
            }
        }

        throw new AssertionError("Опция '" + optionText + "' не найдена в выпадающем списке!");
    }

    /**
     * Универсальный JS-клик — страхует от ElementClickInterceptedException.
     */
    private void clickWithJs(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
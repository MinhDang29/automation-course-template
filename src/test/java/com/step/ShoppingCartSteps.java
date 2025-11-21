package com.step;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.utils.BasicTest; // BasicTest provides WebDriver `driver` and WebDriverWait `wait`

public class ShoppingCartSteps extends BasicTest {
    private int expectedQuantity = 0;
    private String productTitleExpected; // store product title for later assertions

    @Given("người dùng đăng nhập thành công với {string} và {string}")
    public void user_login_successfully(String username, String password) throws Exception {
        String url = "https://bantheme.xyz/hathanhauto/tai-khoan/";
        driver.get(url);
        Assert.assertEquals(driver.getCurrentUrl(), url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='username']"))).sendKeys(username);
        WebElement passwordInp = driver.findElement(By.xpath("//*[@id='password']"));
        passwordInp.sendKeys(password);
            // Click login button (try normal click, fall back to JS click if intercepted)
            WebElement loginBtn = driver.findElement(By.xpath("//button[text()='Đăng nhập']"));
            try {
                loginBtn.click();
            } catch (org.openqa.selenium.ElementClickInterceptedException ex) {
                // If some floating widget blocks the button, perform JS click as fallback
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);
            }
        By loginLocator = By.xpath("//button[text()='Đăng nhập']");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loginLocator));
        Assert.assertFalse(isElementDisplayed(loginLocator), "Đăng nhập thất bại, nút Đăng nhập vẫn hiển thị.");
    }

    @Given("số lượng sản phẩm trong giỏ hàng ban đầu được ghi nhận")
    public void initial_cart_quantity_is_recorded() {
        expectedQuantity = getCurrentCartQuantity();
    }

    @When("người dùng tìm kiếm sản phẩm {string}")
    public void user_searches_for_product(String searchTerm) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@name='s'])[1]"))).sendKeys(searchTerm);
    }

    @When("người dùng tìm kiếm sản phẩm {string} và chọn sản phẩm {string}")
    public void user_searches_and_selects_product(String keyword, String productName) {
        // Enter search term and submit (press ENTER)
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@name='s'])[1]")));
        searchInput.clear();
        searchInput.sendKeys(keyword + Keys.ENTER);

        // Wait for results and click first matching product by productName
        By productLinkLocator = By.xpath("(//a[contains(text(),'" + productName + "')])[1]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(productLinkLocator));
        productTitleExpected = option.getText();
        option.click();
    }

    @When("chọn sản phẩm {string} đầu tiên")
    public void user_selects_first_product(String productPartialName) {
        By productLinkLocator = By.xpath("(//a[contains(text(),'" + productPartialName + "')])[1]");
        WebElement option1 = wait.until(ExpectedConditions.visibilityOfElementLocated(productLinkLocator));
        productTitleExpected = option1.getText();
        option1.click();
    }

    @When("chọn thuộc tính xuất xứ {string}")
    public void user_selects_origin_attribute(String origin) {
        WebElement option_Element = driver.findElement(By.xpath("//select[@id='pa_xuat-xu']"));
        option_Element.click();
        WebElement result_Engl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='pa_xuat-xu']/option[text()='" + origin + "']")));
        result_Engl.click();
    }

    @When("nhấn nút {string}")
    public void user_clicks_button(String buttonText) {
        if (buttonText.equals("THÊM VÀO GIỎ HÀNG")) {
            By locator = By.xpath("//button[@class='single_add_to_cart_button button alt']");
            WebElement addToCartBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            addToCartBtn.click();
            expectedQuantity++;
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return;
        }
        if (buttonText.equals("CẬP NHẬT GIỎ HÀNG")) {
            By updateCartLocator = By.xpath("//button[text()='Cập nhật giỏ hàng']");
            WebElement updateCartBtn = driver.findElement(updateCartLocator);
            updateCartBtn.click();
            wait.until(ExpectedConditions.attributeToBe(updateCartLocator, "disabled", "true"));
        }
    }

    @When("nhấn nút {string} để tăng số lượng lên 1")
    public void user_clicks_plus_button(String buttonSign) {
        WebElement plusitem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='plus']")));
        plusitem.click();
        expectedQuantity++;
    }

    @Then("số lượng giỏ hàng trên biểu tượng phải tăng thêm 2 đơn vị")
    public void cart_quantity_should_be_updated() {
        By cartQuantityLocator = By.xpath("//a[@title='Giỏ hàng của bạn']/b");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(cartQuantityLocator, String.valueOf(expectedQuantity)));
        int actualQuantity = getCurrentCartQuantity();
        Assert.assertEquals(expectedQuantity, actualQuantity, "Lỗi: Số lượng giỏ hàng không khớp.");
    }

    @Then("tổng tiền tạm tính phải bằng đơn giá nhân với số lượng mới")
    public void total_price_should_match_quantity() {
        Assert.assertNotNull(productTitleExpected, "productTitleExpected chưa được thiết lập.");
        int safeLen = Math.min(10, productTitleExpected.length());
        WebElement addnewitem = driver.findElement(By.xpath("//a[contains(text(),'" + productTitleExpected.substring(0, safeLen) + "')]") );
        Assert.assertTrue(addnewitem.getText().contains(productTitleExpected), "Lỗi: Tên sản phẩm trong giỏ hàng không khớp.");
        WebElement unitPriceBaseElement = driver.findElement(By.xpath("(//span[@class='woocommerce-Price-amount amount'])[1]"));
        double unitPrice = parsePrice(unitPriceBaseElement.getText());
        WebElement tempPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td[@data-title='Tạm tính'])[1]")));
        double actualTempPrice = parsePrice(tempPriceElement.getText());
        double expectedTotalPrice = unitPrice * expectedQuantity;
        Assert.assertEquals(expectedTotalPrice, actualTempPrice, 0.001, "Lỗi: Giá Tạm tính không khớp.");
    }

    // ---------------------- Helper methods ----------------------
    private boolean isElementDisplayed(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private int getCurrentCartQuantity() {
        try {
            By cartQuantityLocator = By.xpath("//a[@title='Giỏ hàng của bạn']/b");
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(cartQuantityLocator));
            String txt = el.getText().trim();
            if (txt.isEmpty()) return 0;
            return Integer.parseInt(txt);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parsePrice(String priceString) {
        if (priceString == null || priceString.trim().isEmpty()) {
            return 0.0;
        }
        String cleanedString = priceString.replaceAll("[^0-9\\.]", "");
        if (cleanedString.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(cleanedString);
        } catch (NumberFormatException e) {
            System.err.println("Lỗi phân tích giá: " + priceString);
            return 0.0; 
        }
    }
}
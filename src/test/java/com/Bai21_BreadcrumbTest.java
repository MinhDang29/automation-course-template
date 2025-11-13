package com;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement; 
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.utils.BasicTest;
import com.utils.Utils;

public class Bai21_BreadcrumbTest extends BasicTest {
    @Test()
    public void breadcrumbTest() throws Exception {
        // Launch website
        String url = "https://bantheme.xyz/hathanhauto/tai-khoan/";
        driver.get(url);
        Assert.assertEquals(driver.getCurrentUrl(), url);

        // Input username
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='username']"))).sendKeys("mdangdn29@gmail.com");

        // Input password
        WebElement passwordInp = driver.findElement(By.xpath("//*[@id='password']"));
        passwordInp.sendKeys("D@ng291199");

        // Click login button
        WebElement loginBtn = driver.findElement(By.xpath("//button[text()='Đăng nhập']"));
        loginBtn.click();

        // Add a small wait to ensure the element is not displayed anymore
        boolean isLoginDisplay = isElementDisplayed(By.xpath("//button[text()='Đăng nhập']"));
        Assert.assertFalse(isLoginDisplay);
        
        // --- Cải thiện độ ổn định cho MENU HOVER ---
        
        // 1. MenuItem: Chờ clickable
        By menuItemLocator = By.xpath("//a[text()='Hệ thống truyền động, Khung gầm']");
        WebElement MenuItem = wait.until(ExpectedConditions.elementToBeClickable(menuItemLocator));

        action.moveToElement(MenuItem).perform(); 
        String MenuItemText = MenuItem.getText().trim();

        // 2. SupItem: Chờ clickable
        By supItemLocator = By.xpath("//a[text()='Hệ thống phanh']");
        WebElement SupItem = wait.until(ExpectedConditions.elementToBeClickable(supItemLocator));
        
        action.moveToElement(SupItem).perform(); //hover chuột vào supitem hệ thống phanh
        String MenuSupItem = SupItem.getText().trim();

        // 3. douSupItem: Chờ clickable
        By douSupItemLocator = By.xpath("(//a[text()=\"Phanh tay ô tô\"])[1]");
        WebElement douSupItem = wait.until(ExpectedConditions.elementToBeClickable(douSupItemLocator));
        
        action.moveToElement(douSupItem).perform(); //hover chuột vào phanh tay oto
        String douSupItemText = douSupItem.getText().trim();
        douSupItem.click(); // Click để chuyển trang

        String Expectedtext = ("Trang chủ / Sản phẩm / "+ MenuItemText+" / "+MenuSupItem +" / "+douSupItemText);
        System.out.println("Đường dẫn mong đợi: " + Expectedtext);
        
        
        // --- Xử lý Breadcrumb sau khi chuyển trang ---
        By breadcrumbLocator = By.xpath("//section[@class='flw section section-breacrumb']");
        
        
        // Tìm phần tử breadcrumb (Sau khi chuyển trang, có thể cần tìm lại hoặc dùng wait.until)
        WebElement BreadcrumbNavim = wait.until(ExpectedConditions.presenceOfElementLocated(breadcrumbLocator));
        
        // 2. Thực hiện cuộn (scrollIntoView) đến phần tử breadcrumb
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", BreadcrumbNavim);
        
        // 3. Kiểm tra tính hiển thị bằng phương thức isElementDisplayed
        boolean BreadcrumbNav = isElementDisplayed(breadcrumbLocator); 
        Assert.assertTrue(BreadcrumbNav, "Breadcrumb navigation không hiển thị sau khi cuộn.");

        
        // 4. Lấy văn bản và so sánh
        String BreadcrumbNavText = BreadcrumbNavim.getText().trim();
        Assert.assertEquals(BreadcrumbNavText, Expectedtext,"Sai đường dẫn ");

    }
    

     public boolean isElementDisplayed(By by) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
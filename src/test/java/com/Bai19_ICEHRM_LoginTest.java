package com;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.utils.BasicTest;
import com.utils.Utils;
// import org.openqa.selenium.we;


public class Bai19_ICEHRM_LoginTest extends BasicTest {


    @Test(dataProvider = "loginDataFeed")
    public void loginTest(String email, String password , String expectedMessage) throws Exception {
        // Mở trang đăng nhập
        String url = "https://icehrm-open.gamonoid.com/login.php";
        driver.get(url);
        Assert.assertEquals(driver.getCurrentUrl(), url);

        //Nhập email
        WebElement emailInput = driver.findElement(By.xpath("//*[@id='username']"));
        emailInput.sendKeys(email);

        //Nhập password
         WebElement passwordInput = driver.findElement(By.xpath("//*[@id='password']"));
         passwordInput.sendKeys(password);

         //Click nút đăng nhập
        WebElement loginBtn = driver.findElement(By.xpath("(//*[@type='button'])[1]"));
        loginBtn.click();
        Utils.hardWait(2000);

        // Kiểm tra đăng nhập thành công
        boolean isLoginDisplay  = isElementDisplayed(loginBtn);
        Assert.assertFalse(isLoginDisplay);
        Utils.hardWait(1000);

        // kiểm tra message hiện ra 
        By byElementlocator = By.xpath("//*[@class='alert alert-danger']");
        String errorMessageText = getErrorMessage(byElementlocator);
        System.out.println("Error Message: " + errorMessageText);
        Assert.assertEquals(errorMessageText, expectedMessage); 
    }
    @DataProvider(name = "loginDataFeed")
    public Object[][] testDataFeed(){
        return new Object[][] {
            // Test Case 1: Valid Login (Expected to succeed)
            {"admin", "admin", ""}, 
            // Test Case 2: Invalid Password (Expected to fail)
            {"admin", "admin123", "Login failed"}
        };  
    }
    // @DataProvider(name = "loginData2")
    // public Object[][] loginData2(){
    //     return new Object[][] {
    //         {"mdangdn29@gmail.com","D@ng291199" ,"error message"},
    //         {"","D@ng291199"},
    //         {"mdangdn29","123456"}
    //     };  
    // }

    public boolean isElementDisplayed(WebElement element){

        try {
            return element.isDisplayed();
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }
        public String getErrorMessage(By byElementlocator){
        
            try {
                WebElement element = driver.findElement(byElementlocator);
                return element.getText();
            } catch (Exception e) {
                // TODO: handle exception
                return "";
            }

        }

}

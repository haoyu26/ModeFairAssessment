package com.mycompany.modefairassessment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

public class ModeFairAssessment {
    

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

         ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Users\\User\\Downloads\\chrome-win64\\chrome-win64\\chrome.exe"); 
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--profile-directory=Default");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-plugins-discovery");
        options.addArguments("--incognito");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

         try {
            //test case 1
            System.out.println("Executing Test Case 1: Convert MYR to USD");
            convertCurrency(driver, "1000", "MYR", "USD");

            //test case 2
            System.out.println("Executing Test Case 2: Reverse conversion using swap button");
            reverseConversion(driver, "1500", "MYR", "EUR");

            //test case 3
            System.out.println("Executing Test Case 3: Convert currency with negative amount");
            convertCurrency(driver, "-100", "MYR", "USD");

            //test case 4
            System.out.println("Executing Test Case 4: Convert currency with decimal");
            convertCurrency(driver, "1000.226", "USD", "GBP");
            convertCurrency(driver, "1000.224", "USD", "GBP");

            //test case 5
            System.out.println("Executing Test Case 5: Entered non-integer in the amount field");
            convertCurrency(driver, "2b(d8js4#rqe!", "MYR", "USD");
            convertCurrency(driver, "p5g(&Yv^4n01B", "MYR", "USD");

        } catch (InterruptedException ex) {
            System.out.println("System error" + ex);
        } finally {
            driver.quit();
        }
    }

    public static void convertCurrency(WebDriver driver, String amount, String fromCurrency, String toCurrency) throws InterruptedException {
        try{
            driver.get("https://www.xe.com/");

            WebElement amountField = driver.findElement(By.id("amount"));
            amountField.sendKeys(Keys.CONTROL + "a");
            amountField.sendKeys(Keys.DELETE);
            amountField.sendKeys(amount);

            //from currency
            WebElement fromDropdown = driver.findElement(By.xpath("//div[@id='midmarketFromCurrency']"));
            fromDropdown.click();
            WebElement fromInputField = fromDropdown.findElement(By.cssSelector("input[type='text']"));
            fromInputField.sendKeys(fromCurrency);
            Thread.sleep(1000);
            fromInputField.sendKeys(Keys.RETURN);

            //to currency
            WebElement toDropdown = driver.findElement(By.xpath("//div[@id='midmarketToCurrency']"));
            toDropdown.click();
            WebElement toInputField = toDropdown.findElement(By.cssSelector("input[type='text']"));
            toInputField.sendKeys(toCurrency);
            Thread.sleep(1000); 
            toInputField.sendKeys(Keys.RETURN);


            WebElement convertButton = driver.findElement(By.xpath("//button[text()='Convert']"));
            convertButton.click();
            Thread.sleep(3000);
            WebDriverWait wait = new WebDriverWait(driver, 20);
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class, 'sc-295edd9f-1') and contains(@class, 'jqMUXt')]")));
            String convertedAmount = result.getText();
            System.out.println("Original amount (" + fromCurrency + "): " + amount);
            System.out.println("Converted amount (" + toCurrency + "): " + convertedAmount);
            
        } catch (InterruptedException | TimeoutException | NoSuchElementException e) {
            System.out.println("System Timeout");
        } catch (Exception e) {
            System.out.println("false");
        }
    }

    public static void reverseConversion(WebDriver driver, String amount, String fromCurrency, String toCurrency) {
        try {
            driver.get("https://www.xe.com/");

            WebElement amountField = driver.findElement(By.id("amount"));
            amountField.sendKeys(Keys.CONTROL + "a");
            amountField.sendKeys(Keys.DELETE);
            amountField.sendKeys(amount);

            //from currency
            WebElement fromDropdown = driver.findElement(By.xpath("//div[@id='midmarketFromCurrency']"));
            fromDropdown.click();
            WebElement fromInputField = fromDropdown.findElement(By.cssSelector("input[type='text']"));
            fromInputField.sendKeys(fromCurrency);
            Thread.sleep(1000); 
            fromInputField.sendKeys(Keys.RETURN);

            //to currency
            WebElement toDropdown = driver.findElement(By.xpath("//div[@id='midmarketToCurrency']"));
            toDropdown.click();
            WebElement toInputField = toDropdown.findElement(By.cssSelector("input[type='text']"));
            toInputField.sendKeys(toCurrency);
            Thread.sleep(1000); 
            toInputField.sendKeys(Keys.RETURN);
            
            WebElement convertButton = driver.findElement(By.xpath("//button[text()='Convert']"));
            convertButton.click();

            WebDriverWait wait = new WebDriverWait(driver, 20);
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class, 'sc-295edd9f-1') and contains(@class, 'jqMUXt')]")));
            String convertedAmount = result.getText();
            System.out.println("Original amount (" + fromCurrency + "): " + amount);
            System.out.println("Converted amount (" + toCurrency + "): " + convertedAmount);

            WebElement swapButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Swap currencies']")));
            swapButton.click();
            Thread.sleep(3000); 
            result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(@class, 'sc-295edd9f-1') and contains(@class, 'jqMUXt')]")));
            convertedAmount = result.getText();
            System.out.println("After swap: ");
            System.out.println("Original amount (" + fromCurrency + "): " + amount);
            System.out.println("Converted amount (" + toCurrency + "): " + convertedAmount);

        } catch (InterruptedException | TimeoutException | NoSuchElementException e) {
            System.out.println("System Timeout");
        } catch (Exception e) {
            System.out.println("false");
        }
    }
}


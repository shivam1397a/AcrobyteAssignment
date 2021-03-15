package Steps;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.vimalselvam.cucumber.listener.Reporter;
import cucumber.api.PendingException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.*;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import javax.imageio.ImageIO;

public class TheSouledStore {
    WebDriver driver;
    public static String productname;

    @Given("^I navigate to TheSouledStore home page in \"([^\"]*)\" browser$")
    public void iNavigateToTheSouledStoreHomePageInBrowser(String browser) throws Throwable {
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.chrome.driver", ".\\drivers\\geckodriver.exe");
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.thesouledstore.com/");
        Reporter.addStepLog("Successfully navigated to TheSouledStore");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(TheSouledStore.destinationPath.toString());
    }


    @And("^I close the automation browser$")
    public void iCloseTheAutomationBrowser() throws Exception{
        driver.close();
    }

    public static File destinationPath;

    public void getscreenshot() throws IOException, InterruptedException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        destinationPath = new File(System.getProperty("user.dir") + "/target/cucumber-reports/screenshot" + System.currentTimeMillis() + ".jpg");
        FileUtils.copyFile(scrFile, destinationPath);
        Thread.sleep(2000L);
    }

    @And("^I click on \"([^\"]*)\" button$")
    public void iClickOnButton(String btn) throws Throwable {
        Thread.sleep(3000L);
        driver.findElement(By.xpath("//button[contains(text(),'"+btn+"')]")).click();
        Reporter.addStepLog(btn + " button click successful");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(TheSouledStore.destinationPath.toString());
    }

    @And("^I click on \"([^\"]*)\" link$")
    public void iClickOnLink(String link) throws Throwable {
        Thread.sleep(3000L);
        driver.findElement(By.xpath("//a[contains(text(),'"+link+"')]")).click();
        Reporter.addStepLog(link + " link click successful");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(TheSouledStore.destinationPath.toString());
    }

    @And("^I select \"([^\"]*)\" tile under \"([^\"]*)\"$")
    public void iSelectTileUnder(String tile, String headers) throws Throwable {
        driver.findElement(By.xpath("//div[div[div[span[text()='"+headers+"']]]]//div[text()='"+tile+"']")).click();
        Reporter.addStepLog("Navigated to "+tile);
        getscreenshot();
        Reporter.addScreenCaptureFromPath(TheSouledStore.destinationPath.toString());
    }

    @And("^I select last available product$")
    public void iSelectLastAvailableProduct() throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//ul[li[contains(text(),'Pages')]])[1]")));
        int pages=driver.findElements(By.xpath("(//ul[li[contains(text(),'Pages')]])[1]/li")).size();
        driver.findElement(By.xpath("(//ul[li[contains(text(),'Pages')]])[1]/li["+pages+"]/span")).click();
        Reporter.addStepLog("Navigated to last page");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='productlist']//img)[1]")));
        int productcount=driver.findElements(By.xpath("//div[@class='productlist']//img")).size();
        int soldoutcount=driver.findElements(By.xpath("//div[@class='productlist']//img/following-sibling::div[@class='souledout']")).size();
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("(//div[@class='productlist']//img)["+(productcount-soldoutcount)+"]")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='productinfo']//h1")));
        productname=driver.findElement(By.xpath("//div[@class='productinfo']//h1")).getText();
        Reporter.addStepLog("Navigated to last available product");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(TheSouledStore.destinationPath.toString());
    }

    @And("^I select \"([^\"]*)\" size and \"([^\"]*)\" quantity$")
    public void iSelectSizeAndQuantity(String size, String quantity) throws Throwable {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[label[span[text()='"+size+"']]]//input")));
        driver.findElement(By.xpath("//li[label[span[text()='"+size+"']]]//input")).click();
        Select quants = new Select(driver.findElement(By.className("qtyOption")));
        quants.selectByVisibleText(quantity);
        Reporter.addStepLog("Size and Quantity selected");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(TheSouledStore.destinationPath.toString());
    }

    @Then("^I verify that product is added successfully$")
    public void iVerifyThatProductIsAddedSuccessfully() throws Throwable {
        Assert.assertEquals(driver.findElement(By.xpath("//a[text()='Solids: Salmon']")).isDisplayed(),true);
        Assert.assertEquals(driver.findElement(By.xpath("//div[text()='Size: ']/span")).getText(),"S");
        Reporter.addStepLog("Selected object is present");
        getscreenshot();
        Reporter.addScreenCaptureFromPath(TheSouledStore.destinationPath.toString());
    }
}

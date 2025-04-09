package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class basketSteps {

    WebDriver driver = null;
    Random random = new Random();
    private static final Map<String, String> fieldNames = new LinkedHashMap<>();
    private static final Map<String, String> boxNames = new LinkedHashMap<>();
    private static final Map<String, String> faultyFields = new LinkedHashMap<>();
    private String name = "";
    private String email = "";
    private String password = "";


    static {
        fieldNames.put("date of birth", "DateOfBirth");
        fieldNames.put("first name", "Forename");
        fieldNames.put("last name", "Surname");
        fieldNames.put("email address", "EmailAddress");
        fieldNames.put("confirm email address", "ConfirmEmailAddress");
        fieldNames.put("password", "Password");
        fieldNames.put("retype your password", "ConfirmPassword");

        boxNames.put("terms and conditions", "sign_up_25");
        boxNames.put("aged over 18", "sign_up_26");
        boxNames.put("ethics and conduct", "fanmembersignup_agreetocodeofethicsandconduct");

        faultyFields.put("first name", "member_firstname");
        faultyFields.put("last name", "member_lastname");
        faultyFields.put("email address", "member_emailaddress");
        faultyFields.put("confirm email address", "member_confirmemailaddress");
        faultyFields.put("password", "member_password");
        faultyFields.put("retype your password", "member_confirmpassword");
        faultyFields.put("terms and conditions", "TermsAccept");
        faultyFields.put("aged over 18", "AgeAccept");
        faultyFields.put("ethics and conduct", "AgreeToCodeOfEthicsAndConduct");
    }

    @Given("I use the {string}")
    public void iUseThe(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome" :
                driver = new ChromeDriver();
                break;
            case "edge" :
                driver = new EdgeDriver();
                break;
            case "firefox" :
                driver = new FirefoxDriver();
                break;
            default:
                System.out.println("Browser not supported! Test is run in chrome");
                driver = new ChromeDriver();
        }
    }

    @Given("I go to the webpage {string}")
    public void iGoToTheWebpage(String url) {
        if (driver == null) {
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        driver.get(url);
    }

    @When("I create an account with correct data")
    public void iTryToCreateAnAccountWithCorrectData() {
        for (String key : fieldNames.keySet()) {
            driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys(generate(key));
        }
        for (String key : boxNames.keySet()) {
            WebElement label = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='" + boxNames.get(key) + "']")));
            label.click();
        } clickOnButton();
    }

    @When("I create an account without filling out {string}")
    public void iTryToCreateAnAccountWithoutFillingOut(String field) {
        for (String key : fieldNames.keySet()) {
            if (key.equalsIgnoreCase(field)) {
                continue;
            }
            driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys(generate(key));
        }
        for (String key : boxNames.keySet()) {
            if (key.equalsIgnoreCase(field)) {
                continue;
            }
            waitAndClick(driver, By.cssSelector("label[for='" + boxNames.get(key) + "']"));
        } clickOnButton();
    }


    @When("I create an account with unmatching data for {string}")
    public void iTryToCreateAnAccountWithUnmatchingDataFor(String field) {
        for (String key : fieldNames.keySet()) {
            if (key.equalsIgnoreCase(field)) {
                driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys(generateUnmatching(key));
                continue;
            }
            driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys(generate(key));
        }
        for (String key : boxNames.keySet()) {
            waitAndClick(driver, By.cssSelector("label[for='" + boxNames.get(key) + "']"));
        } clickOnButton();
    }


    @When("I create an account with the incorrect {string} for {string}")
    public void iCreateAnAccountWithTheIncorrectFor(String value, String field) {
        if (value.equals("empty")) {
            for (String key : fieldNames.keySet()) {
                if (key.equalsIgnoreCase(field)) {
                    driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys("");
                } else driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys(generate(key));
            }
        } else {
            for (String key : fieldNames.keySet()) {
                if (key.equalsIgnoreCase(field)) {
                    driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys(value);
                } else driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']")).sendKeys(generate(key));
            }
        }
        for (String key : boxNames.keySet()) {
            if (key.equalsIgnoreCase(field)) {
                continue;
            }
            waitAndClick(driver, By.cssSelector("label[for='" + boxNames.get(key) + "']"));
        } clickOnButton();
    }


    @Then("I will receive the message {string}")
    public void iWillReceiveTheMessage(String expectedMessage) {
        String actualMessage = driver.findElement(By.cssSelector("h2[class=bold]")).getText();
        assertEquals(expectedMessage, actualMessage);
    }


    @Then("I will receive the message {string} for the field {string}")
    public void iWillReceiveAWarningMessageForTheField(String expectedMessage, String field) {
        String actualMessage = driver.findElement(By.cssSelector("span[for='" + faultyFields.get(field.toLowerCase()) + "']")).getText();
        assertEquals(expectedMessage, actualMessage);
    }


    @Then("I will receive the {string} for {string}")
    public void iWillReceiveThe(String expectedMessage, String field) {
        String actualMessage = driver.findElement(By.cssSelector("span[for='" + faultyFields.get(field.toLowerCase())+ "']")).getText();
        assertEquals(expectedMessage, actualMessage);
    }


    private String generate(String field) {
        switch (field.toLowerCase()) {
            case "date of birth":
                int year = random.nextInt(20) + 1980;
                int month = random.nextInt(12) + 1;
                int day = random.nextInt(28) + 1;
                return day + "/" + month + "/" + year;
            case "first name":
                name = "Test"; //faker.name().firstName();
                return name;
            case "last name":
                return "Testsson"; //faker.name().lastName();
            case "email address":
                email = name + (random.nextInt(10000) + 1) + "@mailnesia.com";
                return email;
            case "confirm email address":
                return email;
            case "password":
                password = "Nosstset" + (random.nextInt(10000) + 1);//faker.internet().password();
                return password;
            case "retype your password":
                return password;
        } return "Invalid field name!";
    }

    private String generateUnmatching(String field) {
        return switch (field) {
            case "confirm email address" -> "random@mail.com";
            case "retype your password" -> "faultyRepeatPassword";
            default -> "Invalid field name";
        };
    }

    private void clickOnButton() {
        driver.findElement(By.cssSelector("input[type=submit]")).click();
    }

    public void waitAndClick(WebDriver driver, By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5)).
                until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

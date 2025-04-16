package stepDefinitions;

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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasketSteps {

    WebDriver driver = null;
    Random random = new Random();
    private static final HashMap<String, String> fieldNames = new LinkedHashMap<>();
    private static final HashMap<String, String> boxNames = new LinkedHashMap<>();
    private static final HashMap<String, String> faultyFields = new HashMap<>();
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
                System.out.println("Browser :" + browser + " is not supported!");
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
    public void iCreateAnAccountWithCorrectData() {
        for (String key : fieldNames.keySet()) {
            //Pga väldigt många repeterade kodrader för driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']"));
            //skapade jag en metod som sköter detta.
            findFieldElement(key).sendKeys(generate(key));
        }
        for (String key : boxNames.keySet()) {
            waitAndClick(driver, By.cssSelector("label[for='" + boxNames.get(key) + "']"));
        } clickOnButton();
    }


    @When("I create an account with the incorrect or empty {string} for {string}")
    public void iCreateAnAccountWithTheIncorrectOrEmptyFor(String value, String field) {

        if (!isFieldValid(field)) {
            return;
        }

        for (String key : fieldNames.keySet()) {
            if (key.equalsIgnoreCase(field)) {
                findFieldElement(key).sendKeys(value.equals("empty") ? "" : value);
            } else {
                findFieldElement(key).sendKeys(generate(key));
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
        String actualMessage = waitAndLocate(driver, By.cssSelector("h2.bold.gray")).getText();
        assertEquals(expectedMessage, actualMessage);

        if (expectedMessage.equals(actualMessage)) {
            driver.quit();
        }
    }



    @Then("I will receive the {string} for {string}")
    public void iWillReceiveTheFor(String expectedMessage, String field) {

        if (!isFieldValid(field)) {
            return;
        }

        String actualMessage = driver.findElement(By.cssSelector("span[for='" + faultyFields.get(field.toLowerCase())+ "']")).getText();
        assertEquals(expectedMessage, actualMessage);

        if (expectedMessage.equals(actualMessage)) {
            driver.quit();
        }
    }


    private WebElement findFieldElement(String key) {
        return driver.findElement(By.cssSelector("input[name='" + fieldNames.get(key) + "']"));
    }

    private String generate(String field) {
        switch (field.toLowerCase()) {
            case "date of birth":
                //Här sätts de randomiserade åren begränsat till årtal där användaren är över 18 garanterat.
                //Jag valde 1980-2000.
                int year = random.nextInt(20) + 1980;
                int month = random.nextInt(12) + 1;
                int day = random.nextInt(28) + 1;
                return day + "/" + month + "/" + year;
            case "first name":
                //Namn och efternamn behöver inte varieras egentligen, så jag valde att behålla samma för enkelhets skull.
                //I och med att jag återanvänder name, så sparar jag värdet i en private-variabel. Anledningen till att jag inte
                //hårdkodar in namnet från början är för att behålla möjligheten att enkelt ändra till en faker-metod som
                //randomiserar även namnen.
                name = "Test"; //faker.name().firstName();
                return name;
            case "last name":
                return "Testsson"; //faker.name().lastName();
            case "email address":
                //Jag valde att använda mig av namn och en random siffra mellan 0 och 10000.
                //Har man riktig otur kommer man få samma siffra, men sannolikheten är väldigt låg.
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

    //I och med min Scenario Outline så är denna metod egentligen onödig. Jag ville behålla den för att visa min
    //process bättre. För geterateIncorrect, tyckte jag att det faktiskt blev bättre med en Scenario Outline, eftersom
    //då får man själv skriva in exakt vad man vill testa för felaktig data.
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

    private void waitAndClick(WebDriver driver, By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5)).
                until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    private WebElement waitAndLocate(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(5)).
                until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private boolean isFieldValid(String key) {
        if (key.trim().isEmpty()) {
            System.out.println("Field cannot be empty, please specify field!");
            return false;
        }
        key = key.toLowerCase();

        if (!fieldNames.containsKey(key) && !boxNames.containsKey(key) && !faultyFields.containsKey(key)) {
            System.out.println("Cannot find field name: " + key + ". Please check spelling!");
            return false;
        }
        return true;
    }
}

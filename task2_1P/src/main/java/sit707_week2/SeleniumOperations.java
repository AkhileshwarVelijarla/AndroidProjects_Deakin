package sit707_week2;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.io.Files;

/**
 * This class demonstrates Selenium locator APIs to identify HTML elements.
 * 
 * Details in Selenium documentation https://www.selenium.dev/documentation/webdriver/elements/locators/
 * 
 * @author Ahsan Habib
 */
public class SeleniumOperations {

	public static void sleep(int sec) {
		try {
			Thread.sleep(sec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void officeworks_registration_page(String url) {
		// Step 1: Locate chrome driver folder in the local drive.
		System.setProperty("webdriver.chrome.driver", "/Users/akhilesh/Drivers/chromedriver-mac-arm64/chromedriver");
		
		// Step 2: Use above chrome driver to open up a chromium browser.
		System.out.println("Fire up chrome browser.");
		WebDriver driver = new ChromeDriver();
		
		System.out.println("Driver info: " + driver);
		
		sleep(2);
	
		// Load a webpage in chromium browser.
		driver.get(url);
		
		/*
		 * How to identify a HTML input field -
		 * Step 1: Inspect the webpage, 
		 * Step 2: locate the input field, 
		 * Step 3: Find out how to identify it, by id/name/...
		 */
		
		// Find first input field which is firstname
		WebElement element = driver.findElement(By.id("firstname"));
		System.out.println("Found element: " + element);
		// Send first name
		element.sendKeys("Ahsan");
		
		/*
		 * Find following input fields and populate with values
		 */
		// Write code
		// Last name
				element = driver.findElement(By.id("lastname"));
				System.out.println("Found element: " + element);
				element.sendKeys("Velijarla");

				// Email
				element = driver.findElement(By.id("email"));
				System.out.println("Found element: " + element);
				element.sendKeys("akhilesh.testing707@gmail.com");

				// Phone number
				element = driver.findElement(By.name("phoneNumber"));
				element.sendKeys("0412345678");

				// Password - intentionally weak so account is not created
				element = driver.findElement(By.id("password"));
				System.out.println("Found element: " + element);
				element.sendKeys("123");

				// Confirm password
				element = driver.findElement(By.id("confirmPassword"));
				System.out.println("Found element: " + element);
				element.sendKeys("123");

				sleep(2);
		
		
		/*
		 * Identify button 'Create account' and click to submit using Selenium API.
		 */
		// Write code
				WebElement createButton = driver.findElement(By.xpath("//button[contains(text(),'Create account')]"));
				System.out.println("Found button: " + createButton);
				createButton.click();

				sleep(3);

		
		/*
		 * Take screenshot using selenium API.
		 */
		// Write code
				File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				File destination = new File("officeworks_registration.png");
				try {
					Files.copy(screenshot, destination);
					System.out.println("Screenshot saved at: " + destination.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}

		
		
		// Sleep a while
		sleep(2);
		
		// close chrome driver
		driver.close();	
	}
	
	public static void parabank_registration_page(String url) {

		System.setProperty("webdriver.chrome.driver",
				"/Users/akhilesh/Drivers/chromedriver-mac-arm64/chromedriver");

		System.out.println("Opening Parabank...");
		WebDriver driver = new ChromeDriver();

		try {
			driver.get(url);
			driver.manage().window().maximize();
			sleep(3);

			// First Name
			WebElement element = driver.findElement(By.id("customer.firstName"));
			element.sendKeys("Akhilesh");

			// Last Name
			element = driver.findElement(By.id("customer.lastName"));
			element.sendKeys("Velijarla");

			// Address
			element = driver.findElement(By.id("customer.address.street"));
			element.sendKeys("123 Main Street");

			// City
			element = driver.findElement(By.id("customer.address.city"));
			element.sendKeys("Melbourne");

			// State
			element = driver.findElement(By.id("customer.address.state"));
			element.sendKeys("VIC");

			// Zip Code
			element = driver.findElement(By.id("customer.address.zipCode"));
			element.sendKeys("3000");

			// Phone
			element = driver.findElement(By.id("customer.phoneNumber"));
			element.sendKeys("0412345678");

			// SSN
			element = driver.findElement(By.id("customer.ssn"));
			element.sendKeys("123456");

			// Username (unique)
			element = driver.findElement(By.id("customer.username"));
			element.sendKeys("akhilesh" + System.currentTimeMillis());

			// Password
			element = driver.findElement(By.id("customer.password"));
			element.sendKeys("Test123");

			// Confirm Password (intentional mismatch)
			element = driver.findElement(By.id("repeatedPassword"));
			element.sendKeys("Wrong123");

			sleep(2);

			// Register
			WebElement registerBtn = driver.findElement(
					By.xpath("//input[@value='Register']"));
			registerBtn.click();

			sleep(3);

			// Screenshot
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File destination = new File("parabank_registration_failed.png");
			Files.copy(screenshot, destination);

			System.out.println("Parabank screenshot saved.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sleep(2);
			driver.quit();
		}
	}
}
	
	


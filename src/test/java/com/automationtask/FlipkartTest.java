package com.automationtask;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.google.common.io.Files;

public class FlipkartTest {

	public static void main(String[] args) throws InterruptedException, IOException {

		//Launching Browser 
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));


		driver.get("https://www.flipkart.com/");


		//Searching for "Bluetooth Speakers" in search field
		driver.findElement(By.xpath("//input[@class = 'Pke_EE']")).sendKeys("Bluetooth Speakers" , Keys.ENTER);

		//Scrolling the webpage to configuration filter option to click on Brand and rating
		WebElement configurationEle = driver.findElement(By.xpath("//div[text() = 'Configuration']"));
		Actions act = new Actions(driver);
		act.scrollToElement(configurationEle).perform();

		//Clicking on Brand filter option
		driver.findElement(By.xpath("//div[text() = 'Brand']")).click();
		Thread.sleep(1000);

		//Clicking on brand filter "boAt"
		driver.findElement(By.xpath("//div[text() = 'boAt']")).click();
		Thread.sleep(1000);


		//Clicking on customer rating filter "4★ & above"
		WebElement ele = driver.findElement(By.xpath("//div[text() ='4★ & above']/preceding-sibling::input"));
		act.moveToElement(ele).perform();
		act.click().perform();

		Thread.sleep(1000);

		//Clicking on price filter "Low to High"
		driver.findElement(By.xpath("//div[text() ='Price -- Low to High']")).click();

		Thread.sleep(3000);

		//Clicking on first product from all the products displayed
		List<WebElement> products = driver.findElements(By.xpath("//a[@class = 'pIpigb']"));
		products.get(0).click();

		String currentTabId = driver.getWindowHandle();
		Set<String> allIds = driver.getWindowHandles();

		//Switching driver control to product description page
		for(String id: allIds) {
			if(!id.equals(currentTabId))
				driver.switchTo().window(id);
		}

		Thread.sleep(2000);	

		//Checking if "Available offers" is displayed and printing total number of offers
		WebElement availableOfferEle = driver.findElement(By.xpath("//div[text() ='Available offers']"));
		  if(availableOfferEle.isDisplayed()) {
			
			WebElement showAllOffers = driver.findElement(By.xpath("//div[@class='pJPLqn']"));
			if(showAllOffers.isDisplayed()){
				showAllOffers.click();
			}
			List<WebElement> availableoffers =	driver.findElements(By.xpath("//div[@class='kXRMKo']/span"));
			System.out.println("Available offers are : "+availableoffers.size());
		}

		TakesScreenshot ts = (TakesScreenshot)driver;
		WebElement addToCartButton = driver.findElement(By.xpath("//button[text()='Add to cart']"));

		//Checking if the "Add to cart" button is displayed and enabled and capturing screenshot of webpage
		if(addToCartButton.isDisplayed()  &&  addToCartButton.isEnabled()) {
			driver.findElement(By.xpath("//button[text() ='Add to cart']")).click();
			Thread.sleep(1000);
			File src = ts.getScreenshotAs(OutputType.FILE);
			File des = new File(".\\Screenshots\\cart_result.png");
			Files.copy(src, des);
		}

		//Capturing screenshot of webpage when "Add to cart" button is disabled or missing
		else {
			System.out.println("Product unavailable — could not be added to cart");
			File src = ts.getScreenshotAs(OutputType.FILE);
			File des = new File(".\\Screenshots\\result.png");
			Files.copy(src, des);
		}
		driver.quit();
	}

}

package com.example;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.example.Actionwords;

public class ProjectTest extends Actionwords {
	private final Logger oLog = Logger.getLogger(ProjectTest.class);

	
	 Actionwords actionwords = new Actionwords();
	

	
	@BeforeMethod
	public void setUp(){
		driver = actionwords.getBrowser("chrome");
//		oLog.info("Driver is initialized");
	}
    
    
	@Test
    public void testLoginIntoFreeCRM() {
        // TODO: Implement action: "Given Browser is opened"
        // TODO: Implement action: "then Enter the username and password"
        // TODO: Implement action: "then click on login button"
        // TODO: Implement result: "and User should be able to login"
		
    	actionwords.login("http:\\www.freecrm.com");
    	actionwords.generalSettings();
    	
		driver.findElement(By.name("username")).sendKeys("pankgupta20");
		driver.findElement(By.name("password")).sendKeys("Password@123");
		driver.findElement(By.xpath("//input[@type='submit']")).click();       
    }
	
    // HomePage title should display as expected
    // 
    // Tags: id:2 Priority:1
   @Test
    public void testVerifyHomePageTitle() {
        // TODO: Implement action: "Given User is logged-in"
        // TODO: Implement action: "then get the title of the page"
        // TODO: Implement result: "and HomePage title should be correct as expected"
		   	actionwords.login("http:\\www.freecrm.com");
		   	actionwords.generalSettings();
		   	
			driver.findElement(By.name("username")).sendKeys("pankgupta20");
			driver.findElement(By.name("password")).sendKeys("Password@123");
			driver.findElement(By.xpath("//input[@type='submit']")).click();
    	
			String ActTitle = driver.getTitle();
			String ExpTitle = "FreeCRM";
			Assert.assertEquals(ActTitle, ExpTitle);
    	}
   
   
 
   
}
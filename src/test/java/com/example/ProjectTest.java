package com.example;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.getLocator.GetLocator;
import com.testBase.Actionwords;
import com.testBase.Config;

public class ProjectTest extends Actionwords {
	private final Logger oLog = Logger.getLogger(ProjectTest.class);

	private GetLocator getlocator;
	
/*	 public ProjectTest(WebDriver driver){
		 this.driver = driver;		 
	 }*/
	 
	
	@BeforeMethod
	public void setUp(){
		new Actionwords();
		Config config = new Config(driver,OR);
		//Actionwords instaceDriver = Actionwords.getInstance();
		//driver = instaceDriver.getBrowser(config.getBrowser());
	}
    
    
	@Test
    public void testLoginIntoFreeCRM() throws Exception {
		getlocator = new GetLocator(driver,OR);
    	login("http:\\www.freecrm.com");
    	browserGeneralSettings();
    	getlocator.getWebElement("username").sendKeys("pankgupta20");
    	getlocator.getWebElement("password").sendKeys("Password@123");
    	getlocator.getWebElement("submitbtn").click();   
    	//driver.findElement(By.)
    }
	

   @Test
    public void testVerifyHomePageTitle() throws Exception {
		   	login("http:\\www.freecrm.com");
		   	browserGeneralSettings();
	    	getlocator.getWebElement("username").sendKeys("pankgupta20");
	    	getlocator.getWebElement("password").sendKeys("Password@123");
	    	getlocator.getWebElement("submitbtn").click(); 
    	
			String ActTitle = driver.getTitle();
			String ExpTitle = "FreeCRM";
			Assert.assertEquals(ActTitle, ExpTitle);
    	}
   
   
 
   
}
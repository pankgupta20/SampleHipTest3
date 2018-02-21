package com.example;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Actionwords {
	public WebDriver driver;
	public static final Logger logger = Logger.getLogger(Actionwords.class.getName());
	public static ExtentReports extent;
	public static ExtentTest test;
	public ITestResult result;
	public static String imagelocation = System.getProperty("user.dir")+"/ScreenShots/";
	
	public Actionwords(){
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);	
	  }
	
	
	static{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		extent = new ExtentReports(System.getProperty("user.dir")+"/TestRunReport/"+formater.format(calendar.getTime())+".html", false);		
	}
	
	
	public WebDriver getBrowser(String browser){
		if(System.getProperty("os.name").contains("Window")){
			if(browser.equalsIgnoreCase("firefox")){
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/Utilities/geckodriver.exe");
				driver = new FirefoxDriver();
			}
			else if(browser.equalsIgnoreCase("chrome")){
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/Utilities/chromedriver.exe");
				driver = new ChromeDriver();
			}
			else if(browser.equalsIgnoreCase("IE")){
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/Utilities/IEDriverServer.exe");
				driver = new ChromeDriver();
			}			
		}
		return driver;
	}
	
	
	
	public void login(String url){
		driver.get(url);		
	}
	
	
	public  void generalSettings(){
    	driver.manage().window().maximize();
    	driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	}
	
	public  String takeScreenshot(String imagename) throws IOException{
		if(imagename.equals("")){
			imagename="Blank";
		}
		//File image = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File image = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		
		String dirpath = imagelocation+formater.format(calendar.getTime());
		File newDirectory = new File(dirpath);
		boolean isCreated = newDirectory.mkdirs();
		
		if(isCreated) {
             newDirectory.getCanonicalPath();
		}
		
		String actualimage = imagename+"_"+formater.format(calendar.getTime())+".png";
		File destFile = new File(dirpath+File.separator +actualimage);
		FileUtils.copyFile(image, destFile);	
		return actualimage;
	}

	

	
	
	
	public void getresult(ITestResult result) throws IOException{
		if(result.getStatus() == ITestResult.SUCCESS){
			test.log(LogStatus.PASS, result.getName()+ " : Test is passed");
			logger.info("Test Passed: " + result.getName());
		}
		else if(result.getStatus() == ITestResult.SKIP){
			test.log(LogStatus.SKIP, result.getName()+ " : Test is skipped and skip reason is: " + result.getThrowable());
			logger.info("Test Skipped: " + result.getName());
		}
		else if(result.getStatus() == ITestResult.FAILURE){
			test.log(LogStatus.FAIL, result.getName()+ " : Test is failed and failure reason is: " + result.getThrowable());
			String screen = takeScreenshot("");
			test.log(LogStatus.FAIL, test.addScreenCapture(screen));
			logger.info("Test Failed: " + result.getName());
		}
		else if(result.getStatus() == ITestResult.STARTED){
			test.log(LogStatus.INFO, result.getName()+ " : Test is started ");
		}	
	}
	
	
	@BeforeMethod
		public void reportSetUp(Method result){
			test = extent.startTest(result.getName());
			test.log(LogStatus.INFO, result.getName()+" : Test started");
			logger.info("****************************************************");
			logger.info("Test Started: " + result.getName());
		}

	@AfterMethod   
		public void afterMethod(ITestResult result) throws IOException{
				getresult(result);	
				logger.info("Test Completed: " + result.getName());
//				logger.info("****************************************************");
			}
	
	  @AfterMethod
	   public void tearDown(){
		   driver.quit();
	 }
	  
	  
	@AfterClass(alwaysRun=true)
		public void endTest(){
			extent.endTest(test);
			extent.flush();
		}
	

}
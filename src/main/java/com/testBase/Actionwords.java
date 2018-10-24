package com.testBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.excelReader.Excel_reader;
import com.helper.Wait.WaitHelper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Actionwords {
	//private static Actionwords instanceDriver = null;
	public WebDriver driver;
	public static final Logger logger = Logger.getLogger(Actionwords.class.getName());
	public static ExtentReports extent;
	public static ExtentTest test;
	public ITestResult result;
	public static String imagelocation = System.getProperty("user.dir")+"/ScreenShots/";
	public Properties OR;
	public File f1;
	public FileInputStream file;
	public final String configpath = System.getProperty("user.dir")+"/src/main/java/com/config/config.properties";
	public final String orPath = System.getProperty("user.dir")+"/src/main/java/com/config/or.properties";
	public Excel_reader excelreader;
	public String browsername;
	private Config config;
	
	public Actionwords(){	
	
	  }
	
	
/*	public static Actionwords getInstance(){
		if(instanceDriver == null)
			instanceDriver = new Actionwords();
		return instanceDriver;
	}*/
	
	@BeforeMethod
	public void launchBrowser(){
		try {
			loadPropertiesFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		config = new Config(driver,OR);
		//getInstance();
		//driver = getBrowser(config.getBrowser());
		
/*		WaitHelper waitHelper = new WaitHelper(driver);
		waitHelper.setImplicitWait(config.getImplicitWait(), TimeUnit.SECONDS);
		waitHelper.setPageLoadTimeout(config.getPageLoadTimeOut(), TimeUnit.SECONDS);*/
	}
	
	
	
	static{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		extent = new ExtentReports(System.getProperty("user.dir")+"/TestRunReport/"+formater.format(calendar.getTime())+".html", false);		
	}
	
	
	public void getBrowser(String browser){
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
		//return driver;
	}
	
	
	
	public void login(String url){
		driver.get(url);	
		driver.manage().window().maximize();
	}
	
	
	public  String takeScreenshot(String imagename) throws IOException{
		if(imagename.equals("")){
			imagename="Blank";
		}
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


	
	public void browserGeneralSettings(){
		driver.manage().timeouts().implicitlyWait(config.getImplicitWait(),TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(config.getPageLoadTimeOut(),TimeUnit.SECONDS);
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
	
	
public void loadPropertiesFile() throws IOException{
		
		/*String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);*/
		OR = new Properties();
		f1 = new File(configpath);
		file = new FileInputStream(f1);
		OR.load(file);
		logger.info("loading config.properties");
		
		f1 = new File(orPath);
		file = new FileInputStream(f1);
		OR.load(file);
		logger.info("loading or.properties");
	}

	public String getProperty(String elemname) throws Exception{
		loadPropertiesFile();
		return (OR.getProperty(elemname));
	}
	

	
	
	public String[][] getData(String excelName, String sheetName){
		System.out.println(System.getProperty("user.dir"));
		String excellocation = System.getProperty("user.dir")+"/src/main/java/com/hybridFramework/data/"+excelName;
		System.out.println(excellocation);
		excelreader = new Excel_reader();
		return excelreader.getExcelData(excellocation, sheetName);
	}
	
	public static void updateResultupdateResult(int indexSI,  String screenShotLocation,String response) throws IOException {
		String startDateTime = new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime());
	    System.out.println("startDateTime---"+startDateTime);
		String userDirector = System.getProperty("user.dir");

		String resultFile = userDirector + "/src/main/java/com/SampleHipTest3/Screenshots/TestReport.html";
		
		File file = new File(resultFile);
		System.out.println(file.exists());

		if (!file.exists()) {
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<html>" + "\n");
			bw.write("<head><title>" + "Test execution report" + "</title>" + "\n");
			bw.write("</head>" + "\n");
			bw.write("<body>");
			bw.write("<font face='Tahoma'size='2'>" + "\n");
			bw.write("<u><h1 align='center'>" + "Test execution report" + "</h1></u>" + "\n");
			bw.flush();
			bw.close();
		}
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(file, true));
		if (indexSI == 1) {

			bw1.write("<table align='center' border='0' width='70%' height='10'>");
			bw1.write("<tr><td width='70%' </td></tr>");
			bw1.write("<table align='center' border='1' width='70%' height='47'>");
			bw1.write("<tr>");
			bw1.write("<td colspan='1' align='center'><b><font color='#000000' face='Tahoma' size='2'>ScriptName :&nbsp;&nbsp;&nbsp;</font><font color='#0000FF'' face='Tahoma' size='2'>Resiliency Test </font></b></td>");
			bw1.write("<td colspan='2' align='left'><b><font color='#000000' face='Tahoma' size='2'>Start Time :&nbsp;</font></b><font color='#0000FF'' face='Tahoma' size='1'>" + startDateTime + " </font></td>");
			bw1.write("</tr>");
			bw1.write("</tr>");
			bw1.write("<td  bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>S.No</font></b></td>");
			bw1.write("<td  bgcolor='#CCCCFF' align='left'><b><font color='#000000' face='Tahoma' size='2'>Screen Shot</font></b></td>");
			bw1.write("<td  bgcolor='#CCCCFF' align='center'><b><font color='#000000' face='Tahoma' size='2'>Response </font></b></td>");
			bw1.write("</tr>");
		}

		bw1.write("<tr>" + "\n");
		bw1.write("<td bgcolor='#FFFFDC'align='Center'><font color='#000000' face='Tahoma' size='2'>" + indexSI + "</font></td>" + "\n");
		bw1.write("<td  bgcolor='#FFFFDC' valign='middle' align='left'><b><font color='#000000' face='Tahoma' size='2'>" + "<img src="+screenShotLocation+" alt='Smiley face' height='500' width='750'>" + "</font></b></td>" + "\n");
		bw1.write("<td  bgcolor='#FFFFDC' valign='middle' align='left'><b><font color='#000000' face='Tahoma' size='2'>" + response + "</font></b></td>" + "\n");
		bw1.write("</tr>" + "\n");
		bw1.write("</body>" + "\n");
		bw1.write("</html>");
		bw1.flush();
		bw1.close();
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
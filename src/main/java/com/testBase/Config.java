package com.testBase;

import java.util.Properties;

import org.openqa.selenium.WebDriver;

public class Config {
	private WebDriver driver;
	private Properties OR;
	
	public Config(WebDriver driver, Properties OR){
		this.driver=driver;
		this.OR = OR;
	}
	public String getUserName() {
		return OR.getProperty("Username");
	}

	public String getPassword() {
		return OR.getProperty("Password");
	}

	public String getWebsite(String appUrl) {
		return OR.getProperty(appUrl);
	}

	public int getPageLoadTimeOut() {
		return Integer.parseInt(OR.getProperty("PageLoadTimeOut"));
	}

	public int getImplicitWait() {
		return Integer.parseInt(OR.getProperty("ImplcitWait"));
	}

	public int getExplicitWait() {
		return Integer.parseInt(OR.getProperty("ExplicitWait"));
	}

	public String getDbType() {
		return OR.getProperty("DataBase.Type");
	}

	public String getDbConnStr() {
		return OR.getProperty("DtaBase.ConnectionStr");
	}
	public String getBrowser() {
		return OR.getProperty("Browser");
	}

}

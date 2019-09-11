package com.tvsm2.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tvsm2.pages.StartPage;
import com.tvsm2.pages.LoginPage;
import com.tvsm2.pages.OTPVerificationPage;
import com.tvsm2.pages.UpdatePage;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class Utils {
	 public AndroidDriver<MobileElement> driver;
	 UpdatePage update;
		StartPage homePage;
		LoginPage loginPage;
		OTPVerificationPage otpPage; 
		WebDriverWait wait;
	 
	public Utils(AndroidDriver<MobileElement> driver) {
		 this.driver=driver;
		 wait = new WebDriverWait(driver,15);
	 }
	 
	public String getOTP() throws InterruptedException{
		//Thread.sleep(10000);
		//driver.openNotifications();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'This is your one-time password- ')]")));
		String otp = driver.findElement(By.xpath("//*[contains(text(),'This is your one-time password- ')]")).getText().split("password- ")[1];
		
		//driver.hideKeyboard();
		return otp;
	}
	public static String getScreenshot(WebDriver driver)
	{
		TakesScreenshot ts=(TakesScreenshot) driver;
		
		File src=ts.getScreenshotAs(OutputType.FILE);
		
		String path=System.getProperty("user.dir")+"/Screenshot/"+System.currentTimeMillis()+".png";
		
		File destination=new File(path);
		
		try 
		{
			FileUtils.copyFile(src, destination);
		} catch (IOException e) 
		{
			System.out.println("Capture Failed "+e.getMessage());
		}
		
		return path;
	}
}

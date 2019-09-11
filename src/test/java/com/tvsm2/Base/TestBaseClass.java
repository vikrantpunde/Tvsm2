package com.tvsm2.Base;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.tvsm2.pages.StartPage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.tvsm2.pages.HomePage;
import com.tvsm2.pages.LoginPage;
import com.tvsm2.pages.OTPVerificationPage;
import com.tvsm2.pages.UpdatePage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import com.tvsm2.utils.Utils;

public class TestBaseClass {
	public AndroidDriver<MobileElement> driver;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	DesiredCapabilities capabilities;
	HomePage hp;
	UpdatePage update;
	StartPage startPage;
	LoginPage loginPage;
	OTPVerificationPage otpPage;
	WebDriverWait wait;

	@BeforeMethod
	public void run() throws MalformedURLException {

		capabilities = new DesiredCapabilities();
		capabilities.setCapability("BROWSER_NAME", "Android");
		capabilities.setCapability("VERSION", "8.1.0");
		capabilities.setCapability("deviceName", "Mi A2");
		capabilities.setCapability("platformName", "Android");

		capabilities.setCapability("appPackage", "com.tvsm.connect");
		capabilities.setCapability("appActivity", "com.tvsm.connect.SplashActivity");
		capabilities.setCapability("autoGrantPermissions", "true");
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

	}
	@BeforeSuite
	public void initExtent() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/MyOwnReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("OS", "Mac Sierra");
        extent.setSystemInfo("Host Name", "Krishna");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User Name", "Krishna Sakinala");
        htmlReporter.config().setDocumentTitle("Guru99 Bank Report");
        htmlReporter.config().setReportName("My Own Report");

	}
	@AfterMethod(alwaysRun = true)
	public void getResult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenShotPath = Utils.getScreenshot(driver);
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" Test case FAILED due to below issues:", ExtentColor.RED));
            test.fail(result.getThrowable());
			test.fail("Snapshot below: " + test.addScreenCaptureFromPath(screenShotPath));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
		} else {
			test.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.ORANGE));
			test.skip(result.getThrowable());
		}
		extent.flush();
		driver.quit();
	}

	public AndroidDriver<MobileElement> getDriver() {
		return this.driver;
	}

	public void Login() throws InterruptedException {
		update = new UpdatePage(driver);
		startPage = new StartPage(driver);
		loginPage = new LoginPage(driver);
		otpPage = new OTPVerificationPage(driver);
		hp = new HomePage(driver);
		Thread.sleep(10000);

		update.GoToHomePage();
		startPage.clickLogin();
		loginPage.enterMobileNumber("8805992841");
		loginPage.clickLoginBtn();
		otpPage.enterOTP();
		otpPage.clickSubmit();
		hp.verifyHomePageDisplayedElseAddVehicle();
		Thread.sleep(10000);
	}

	public void Logout() throws InterruptedException {

		driver.findElement(By.xpath("//*[@id='imgBack']")).click();
		new WebDriverWait(driver, 10)
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Logout']")));
		driver.findElement(By.xpath("//*[@id='imgBack']")).click();
		driver.findElement(By.xpath("//*[@text='Logout']")).click();
		driver.findElement(By.xpath("//*[@text='YES']")).click();
	}

}

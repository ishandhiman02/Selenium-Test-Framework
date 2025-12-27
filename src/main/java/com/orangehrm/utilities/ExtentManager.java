package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.units.qual.s;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize the Extent Report
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath); // to cretae html reports
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding system information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));

		}
		return extent;
	}

	// Startt the test- isme kya ho rha hai harr thread ke liye naya block ban raha
	// hai
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// get Current Threads Test Name -ki uska log fail huva pass ya skip ?
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	// End the Test
	public synchronized static void endTest() {
		getReporter().flush();
	}

	// Method to get Name of current test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No test is currently active for this Thread";
		}
	}

	// Log a step - informational message
//	public static void  logStep(String logMessage) {
//		getTest().info(logMessage);
//	}
	public static void logStep(String logMessage) {
		if (getTest() != null) {
			getTest().info(logMessage);
		} else {
			System.out.println("ExtentTest is NULL → " + logMessage);
		}
	}

	// log a step validation with ScreenSHot
	public static void LogStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
		getTest().pass(logMessage);
		// screenShot method
		attachScreenshot(driver, screenShotMessage);
	}

	// log a step validation for API
	public static void LogStepValidationForAPI(String logMessage) {
		getTest().pass(logMessage);
	}

	// log a failure
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
		String colorMessage = String.format("<span style='color:red;'>", logMessage + "</span>");
		getTest().fail(colorMessage);
		// screeen shot method
		attachScreenshot(driver, screenShotMessage);
	}

	// log a failure for API
	public static void logFailureAPI(String logMessage) {
		String colorMessage = String.format("<span style='color:red;'>", logMessage + "</span>");
		getTest().fail(colorMessage);
	}

	// log a skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style ='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}

	// take screenshot with date and tame in the file
//	1️⃣ Screenshot lo
//	2️⃣ PNG save karo
//	3️⃣ base64 convert karo
//	4️⃣ Report me attach karo

	public synchronized static String takeScreenShot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		// Format date and time for file name
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		// Saving the screenShot to a file
		String destPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenshotName + "_"
				+ timeStamp + ".png";

		File finalPath = new File(destPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// convert screenshot to base64 for embedding in the report
		String base64Format = convertTOBase64(src);
		return base64Format;
	}

	// convert screenShot to Base64Format
	public static String convertTOBase64(File screenShotFile) { // file ka path pass kraya h yha (FIle ScreenShotFile)
		String base64Format = "";
		// Read the file content into a byte array
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
			// convert the byte array into base64 String
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Format;
	}

	// Attach screenshot to report using base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
//		if (driver == null || getTest() == null) {
//	        return; 
//	    } 
		try {
			String screenShotBase64 = takeScreenShot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach Screenshot:" + message);
			e.printStackTrace();
		}
	}

	// Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}

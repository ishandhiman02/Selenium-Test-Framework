  package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.IllegalSelectorException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	// instance Variables -singleton desgin pattern
	protected static Properties prop;
	// using threadLocal for parallel execution /testing (har class ka apna ek thread )
	//threadlocal =Har thread ka apna WebDriver + ActionDriver
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	
	
	//Getter method for getSoftAssert 
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;
	//driver = driver.get() = getDriver()

	@BeforeSuite
	public void loadConfig() throws IOException {
		// load configuration file
		prop = new Properties();
//		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"src/main/resources/config.properties");
		FileInputStream fis = new FileInputStream("src\\main\\resources\\config.properties");
		prop.load(fis);
		logger.info("config.propertiez file loaded");
		
		//Start the Extent Report
//		ExtentManager.getReporter(); --> This has been used in TestListener
	}

	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		System.out.println("Setting up driver for: " + this.getClass().getSimpleName());
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		logger.trace("This is Trace message");
//		logger.error("This is Error message");
//		logger.debug("This is debug message");
//		logger.fatal("This is fatal message");
//		logger.warn("This is warn message");
		
/*		//Initial the Action Driver only once
		if(actionDriver==null) {
			actionDriver = new ActionDriver(driver);
//			System.out.println("ActionDriver instance is created");
			logger.info("ActionDriver instance is created" +Thread.currentThread().getId());
		}
	}
*/
		//Intialize ActionDriver for the current  Thread
		
		driver.set(getDriver());
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread:" +Thread.currentThread().getId());
	}
	 
//	private synchronized  void lauchBrowser() {
//		// initialize the webdriver based on browser define in config.properties
//		String browser = prop.getProperty("browser");
//		if (browser.equalsIgnoreCase("chrome")) {
////			System.setProperty("webdriver.chrome.driver", 
////				    "C:\\Users\\dell\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
////			driver = new ChromeDriver();
//			driver.set(new ChromeDriver()); 
//			ExtentManager.registerDriver(getDriver());
//			logger.info("ChromeDriver instance is created");
//		} else if (browser.equals("firefox")) {
////			driver = new FirefoxDriver();
//			driver.set(new FirefoxDriver());
//			ExtentManager.registerDriver(getDriver());
//			logger.info("FireFoxDriver instance is created");
//		} else if (browser.equalsIgnoreCase("edge")) {
////			driver = new EdgeDriver();
//			driver.set(new EdgeDriver());
//			ExtentManager.registerDriver(getDriver());
//			logger.info("EdgeDriver instance is created");
//		} else {
//			throw new IllegalArgumentException("browser not supported:  " + browser);
//		}
//	}
	
//	private synchronized  void lauchBrowser() {
//		// initialize the webdriver based on browser define in config.properties
//		String browser = prop.getProperty("browser");
//		if (browser.equalsIgnoreCase("chrome")) {
//			
//			//create ChromeOption
//			ChromeOptions options = new ChromeOptions();
//			options.addArguments("--headless");// Run Chrome in Headless mode
//			options.addArguments("--disable-gpu");// Disable GPU  for headless mode
////			options.addArguments("--window-size=1920,1080");// Set window size
//			options.addArguments("--disable-notifications");// Disable browser notifications
//			options.addArguments("--no-sandbox");// Required for some CI environments like
//			options.addArguments("--disable-dev-shm-usage");// Resolve issues in resources
//			
////			driver = new ChromeDriver();
//			driver.set(new ChromeDriver(options));  // New Changes As Per Thread 
//			ExtentManager.registerDriver(getDriver());
//			logger.info("ChromeDriver instance is created");
//		} else if (browser.equalsIgnoreCase("firefox")) {
//			
//			//create FireFoxOption
//			FirefoxOptions options = new FirefoxOptions();
//			options.addArguments("--headless");// Run Chrome in Headless mode
//			options.addArguments("--disable-gpu");// Disable GPU rendering (usefull for headless mode)
//			options.addArguments("--width=1920");// Set browser width
//			options.addArguments("--height=1080");// Set browser height
//			options.addArguments("--disable-notifications");// Disable browser notifications
//			options.addArguments("--no-sandbox");// Required for some CI environments like
//			options.addArguments("--disable-dev-shm-usage");// Prevent crashes in low-resources
//			
////			driver = new FirefoxDriver();
//			driver.set(new FirefoxDriver(options)); // New Changes As Per Thread
//			ExtentManager.registerDriver(getDriver());
//			logger.info("FireFoxDriver instance is created");
//		} else if (browser.equalsIgnoreCase("edge")) {
//			
//			//create EdgeOption
//			EdgeOptions options = new EdgeOptions();
//			options.addArguments("--headless");// Run Chrome in Headless mode
//			options.addArguments("--disable-gpu");// Disable GPU  acceleration
//			options.addArguments("--window-size=1920,1080");// Set window size
//			options.addArguments("--disable-notifications");// Disable pop-up notifications
//			options.addArguments("--no-sandbox");// Required for some CI environments like
//			options.addArguments("--disable-dev-shm-usage");// prevent resource-limited 
//			
////			driver = new EdgeDriver();
//			driver.set(new EdgeDriver(options)); // New Changes As Per Thread
//			ExtentManager.registerDriver(getDriver());
//			logger.info("EdgeDriver instance is created");
//		} else {
//			throw new IllegalArgumentException("browser not supported:  " + browser);
//		}
//	}
	
	
	private synchronized void launchBrowser(String browser) {

		//String browser = prop.getProperty("browser");
		
		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridURL = prop.getProperty("gridURL");
		
		if (seleniumGrid) {
		    try {
		        if (browser.equalsIgnoreCase("chrome")) {
		            ChromeOptions options = new ChromeOptions();
		            options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
		            driver.set(new RemoteWebDriver(new URL(gridURL), options));
		        } else if (browser.equalsIgnoreCase("firefox")) {
		            FirefoxOptions options = new FirefoxOptions();
		            options.addArguments("-headless");
		            driver.set(new RemoteWebDriver(new URL(gridURL), options));
		        } else if (browser.equalsIgnoreCase("edge")) {
		            EdgeOptions options = new EdgeOptions();
		            options.addArguments("--headless=new", "--disable-gpu","--no-sandbox","--disable-dev-shm-usage");
		            driver.set(new RemoteWebDriver(new URL(gridURL), options));
		        } else {
		            throw new IllegalArgumentException("Browser Not Supported: " + browser);
		        }
		        logger.info("RemoteWebDriver instance created for Grid in headless mode");
		    } catch (MalformedURLException e) {
		        throw new RuntimeException("Invalid Grid URL", e);
		    }
		} else {

		if (browser.equalsIgnoreCase("chrome")) {
			
			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); // Run Chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU for headless mode
			//options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

			// driver = new ChromeDriver();
			driver.set(new ChromeDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created.");
		} else if (browser.equalsIgnoreCase("firefox")) {
			
			// Create FirefoxOptions
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created.");
		} else if (browser.equalsIgnoreCase("edge")) {
			
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created.");
		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}
		}
	}

	private void configureBrowser() {
		// ConfigureBrowser settings such as implicit wait and maximise browser and navigate to URl
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		//driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait)); // selenium 4
		// driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS); // selenium 3
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		//getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		
		// Maximize the Browser
		driver.get().manage().window().maximize();

		// Navigate to URL
		try {
			//driver.get(prop.getProperty("url"));
			driver.get() .get(prop.getProperty("url")); 
			//getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("failed to navigate to the url:" + e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) { 
			try {
				//driver.quit();
				//driver.get().quit();
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver:" + e.getMessage());
			}
		}
//		System.out.println("WebDriver instance is closed");
		logger.info("WebDriver instance is closed");
		driver.remove();
		actionDriver.remove();
//		driver = null;
//		actionDriver = null;
//		ExtentManager.endTest(); --> this has been used in TestListener 
	}
/*	
	//Driver getter method
	public WebDriver getDriver(){
		return driver;
	}
*/	 
	
	//Getter Method for WebDriver
	public static WebDriver getDriver() {
		if(driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}
	
	//Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {
		if(actionDriver.get() == null) {
		System.out.println("ActionDriver is not initialized");
		throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();	
	}
	
	//Getter method for Prop
	public static Properties getProp() {
		return prop;
	}
	
	//Driver setter metod
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver =  driver;
	}
	
	//Static wait for PAuse
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
	
	
}

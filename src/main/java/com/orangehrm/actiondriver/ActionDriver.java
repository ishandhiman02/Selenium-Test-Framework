package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class ActionDriver extends BaseClass {

	//Test checkin
	//Testing ishan
	// Variables	
	private WebDriver driver;
	private WebDriverWait wait; // ExplicitWait- Dynamic wait
//	protected static Properties prop;
	public static final Logger logger = BaseClass.logger;
//	public static final Logger logger = LoggerManager.getLogger(ActionDriver.class);

	// Create constructor for this class- same name as class name
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
//		int implicitWait = Integer.parseInt(prop.getProperty(30));
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait)); 
//		int implicitWait = Integer.parseInt(BaseClass.prop.getProperty("implicitWait")); //chatgpt 
//		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, (Duration.ofSeconds(explicitWait)));
//		System.out.println("WebDriver instance is created");
		logger.info("WebDriver instance is created");
	}

	// USER'S PAGE ACTION METHODS

	// Method to click an ELement
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by); // sbse phle "waitForElementToBeClickable" yeh method call hoga-
			// then phle try kro element per click karne ki agr nahi hota hai toh exception
			// throw krdo
			driver.findElement(by).click();
			ExtentManager.logStep("cliaked an element:" + elementDescription);
			logger.info("clicked an element-->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("unable to click element:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to click element:",
					elementDescription + "_unable to click");
			logger.error("unable to click element");
		}
	}

	// Method to Enter an text into an input field - Avoid Code Duplication
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
//			driver.findElement(by).clear();
//			driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on" + getElementDescription(by) + "-->" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
//			System.out.println("Unable to enter the value: " + e.getMessage());
			logger.error("unable to enter the value: " + e.getMessage());
		}
	}

	// Method to get text from an input field
	// Yeh method element ka text nikaal kar tumhare test ko wapas deta hai.
	// Agar element nahi mila to empty string return karta hai

//	Step 1: Tum element ka locator pass karte ho
//  Step 2: Method us element ka wait karta hai
//	Step 3: Element milta hai → uska text return karta hai
//	Step 4: Nahi milta → error print + empty string return

	public String getText(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
//			System.out.println("Unable to get the Text:" + e.getMessage());
			logger.error("unable to get the text: " + e.getMessage());
			return "";
		}
	}

	// Method to compare Two Text
	public boolean compareTwoText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
//				System.out.println("Text is matching: " + actualText + " equals " + expectedText);
				logger.info("Text is matching:" + actualText + " equals " + expectedText);
				ExtentManager.LogStepWithScreenshot(BaseClass.getDriver(), "compare Text",
						"Text verified Successfully!" + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
//				System.out.println("Text is not matching:" + actualText + " not equals " + expectedText);
				logger.error("Text is not matching:" + actualText + " not equals to " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text comparison failed!" + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
//			System.out.println("Unable to compare the Texts:" + e.getMessage());
			logger.error("Unable to compare the Texts:" + e.getMessage());
		}
		return false;
	}

	// Method to check if element is displayed
//	public boolean isDisplayed(By by) {
//		try {
//			waitForElementToBeVisible(by);
//			boolean isDisplayed = driver.findElement(by).isDisplayed();
//			if (isDisplayed) {
//				System.out.println("Text is displayed");
//				return isDisplayed; // isdisplaed -true value
//			} else {
//				return isDisplayed; // isdisplaed -false value
//			}
//		} catch (Exception e) {
//			System.out.println("Element/Text is not Displayed" + e.getMessage());
//			return false;
//		}
//	}
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is Displayed " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed:" + getElementDescription(by));
			ExtentManager.LogStepWithScreenshot(BaseClass.getDriver(), "Element is displayed:",
					"Element is displayed:" + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
//			System.out.println("Element is not displayed" + e.getMessage());
			logger.error("Element is not displayed" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed:",
					"Element is not displayed" + getElementDescription(by));
			return false;
		}
	}

	// WAIT FOR THE PAGE TO LOAD
	// Yeh method browser ko boltta hai-
	// Bhai ruk ja… poora page load hone do…
	// jab document.readyState == complete ho jaye tab hi aage ka code chala
	// document.readyState - ye 3 possible value per kaam karta hai-
	// 1. page loading, 2.page interactive- html load ho gyi but image/script
	// pending hai, 3. sav kuj cmplte ho chuka hai
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
//			System.out.println("Page Load Successfully");
			logger.info("Page Load Successfully");
		} catch (Exception e) {
//			System.out.println("Page didn't Load within " + timeOutInSec + "seconds.Exeption: " + e.getMessage());
			logger.error("Page didn't Load within " + timeOutInSec + "seconds.Exeption: " + e.getMessage());
		}

	}

	// Scroll to an ELement
	// first we get call the executescript method from javascript ->
	// this is the commnd ,it will go to particular elemnt what we're passing over
	// here
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
//			System.out.println("Unable to locate Element:" + e.getMessage());
			logger.error("Unable to locate Element:" + e.getMessage());
		}
	}

// Wait for Element to be clickable - its wait for elemnt to click otherwose its throws exception

	// BY = public WebElement findElement(WebDriver driver) {
	// WebElement element = driver.findElement(By.id(getSelector()));
	// if (element == null)
	// element = driver.findElement(By.name(getSelector());
	// return element; }

	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
//			System.out.println("element is not clickabele: " + e.getMessage());
			logger.error("element is not clickabele: " + e.getMessage());
		}
	}

//Wait for Element to be Visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
//			System.out.println("Element is not VIsible: " + e.getMessage());
			logger.error("Element is not VIsible: " + e.getMessage());
		}
	}

//Method to get description of an element using By locator
	public String getElementDescription(By locator) {

		// Check for null driver and null locator to avoid NullPointerException
		if (driver == null) {
			return "driver is null";
		} else if (locator == null) {
			return "locator is null";
		}

		try {
			// Find Element using locator
			WebElement element = driver.findElement(locator);

			// Get element Attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			// return the description based on the element attribute
			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with id: " + id;
			} else if (isNotEmpty(placeHolder)) {
				return "Element with placeHolder" + placeHolder;
			} else if (isNotEmpty(text)) {
				return "ELement with text:" + text;
			} else if (isNotEmpty(className)) {
				return "element with className: " + className;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the Element: " + e.getMessage());
		}
		return "Unable to describe the element";
	}

	// Utility Method to check a string is not Null or Empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long String
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		} else {
			return value.substring(0, maxLength) + "...";
		}

	}

	// utiliy mthod to border an element
	public void applyBorder(By by, String color) {
		// Locate teh element
		try {
			WebElement element = driver.findElement(by);
			// Apply the border
			String script = "arguments[0].style.border='3px solid'" + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border width color" + color + "to element " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the bordr of a element:" + getElementDescription(by), e);
		}
	}

	// ===================SELECT MODE======================
	// Method to select dropdown by visible text
	public void selectByVisibileText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected dropDown value: " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown value: " + value, e);
		}
	}

	// Method to select dropdown by value
	public void selectByValue(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by, "green");
			logger.info("Selected dropDown by actual value: " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown by value: " + value, e);
		}
	}

	// Method to select dropdown by index
	public void selectByIndex(By by, int index) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByIndex(index);
			applyBorder(by, "green");
			logger.info("Selected dropDown by index: " + index);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown by index: " + index, e);
		}
	}

	// Method to Get All option from dropdown
	public List<String> getDropDownOptions(By by) {
		List<String> optionsList = new ArrayList<>();

		try {
			WebElement dropDownElement = driver.findElement(by);
			Select select = new Select(dropDownElement);
			for (WebElement option : select.getOptions()) {
				optionsList.add(option.getText());
			}
			applyBorder(by, "green");
			logger.info("Retrieved dropdown options for" + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get dropdown options: ", e.getMessage());
		}
		return optionsList;
	}

	// ======================JAVASCRIPT UTILITY METHOD===================

	// Method to click using JavaScript
	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			applyBorder(by, "green");
			logger.info("Clicked element using JavaScript: " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to CLick usong JavaScript:" + e);
		}
	}

	// Method to scroll to the bottom of the page
	public void scrollToBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		logger.info("Scrolled to the bottom of the page.");
	}

	// Method to highlight an element using JavaScript
	public void highlightElementJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
			logger.info("Highlighted element using JavaScript: " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to highlight element using JavaScript", e);
		}
	}

	// ===================== Window and Frame Handling =====================

	// Method to switch between browser windows
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switched to window: " + windowTitle);
					return;
				}
			}
			logger.warn("Window with title " + windowTitle + " not found.");
		} catch (Exception e) {
			logger.error("Unable to switch window", e);
		}
	}

	// Method to switch to an iframe
	public void switchToFrame(By by) {
		try {
			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switched to iframe: " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to switch to iframe", e);
		}
	}

	// Method to switch back to the default content
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logger.info("Switched back to default content.");
	}

	// ===================== Alert Handling =====================

	// Method to accept an alert popup
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert accepted.");
		} catch (Exception e) {
			logger.error("No alert found to accept", e);
		}
	}

	// Method to dismiss an alert popup
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert dismissed.");
		} catch (Exception e) {
			logger.error("No alert found to dismiss", e);
		}
	}

	// Method to get alert text
	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			logger.error("No alert text found", e);
			return "";
		}
	}

	// ===================== Browser Actions =====================

	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentManager.logStep("Page refreshed successfully.");
			logger.info("Page refreshed successfully.");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", "refresh_page_failed");
			logger.error("Unable to refresh page: " + e.getMessage());
		}
	}

	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			ExtentManager.logStep("Current URL fetched: " + url);
			logger.info("Current URL fetched: " + url);
			return url;
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL", "get_current_url_failed");
			logger.error("Unable to fetch current URL: " + e.getMessage());
			return null;
		}
	}

	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtentManager.logStep("Browser window maximized.");
			logger.info("Browser window maximized.");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to maximize window", "maximize_window_failed");
			logger.error("Unable to maximize window: " + e.getMessage());
		}
	}

	// ===================== Advanced WebElement Actions =====================

	public void moveToElement(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(by)).perform();
			ExtentManager.logStep("Moved to element: " + elementDescription);
			logger.info("Moved to element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element",
					elementDescription + "_move_failed");
			logger.error("Unable to move to element: " + e.getMessage());
		}
	}

	public void dragAndDrop(By source, By target) {
		String sourceDescription = getElementDescription(source);
		String targetDescription = getElementDescription(target);
		try {
			Actions actions = new Actions(driver);
			actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
			ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
			logger.info("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to drag and drop",
					sourceDescription + "_drag_failed");
			logger.error("Unable to drag and drop: " + e.getMessage());
		}
	}

	public void doubleClick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.doubleClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Double-clicked on element: " + elementDescription);
			logger.info("Double-clicked on element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to double-click element",
					elementDescription + "_doubleclick_failed");
			logger.error("Unable to double-click element: " + e.getMessage());
		}
	}

	public void rightClick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.contextClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Right-clicked on element: " + elementDescription);
			logger.info("Right-clicked on element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to right-click element",
					elementDescription + "_rightclick_failed");
			logger.error("Unable to right-click element: " + e.getMessage());
		}
	}

	public void sendKeysWithActions(By by, String value) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.sendKeys(driver.findElement(by), value).perform();
			ExtentManager.logStep("Sent keys to element: " + elementDescription + " | Value: " + value);
			logger.info("Sent keys to element --> " + elementDescription + " | Value: " + value);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to send keys",
					elementDescription + "_sendkeys_failed");
			logger.error("Unable to send keys to element: " + e.getMessage());
		}
	}

	public void clearText(By by) {
		String elementDescription = getElementDescription(by);
		try {
			driver.findElement(by).clear();
			ExtentManager.logStep("Cleared text in element: " + elementDescription);
			logger.info("Cleared text in element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to clear text",
					elementDescription + "_clear_failed");
			logger.error("Unable to clear text in element: " + e.getMessage());
		}
	}

	// Method to upload a file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			logger.info("Uploaded file: " + filePath);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to upload file: " + e.getMessage());
		}
	}
}

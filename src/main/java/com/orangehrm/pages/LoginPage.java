package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actiondriver;

	// Define Locators using By class
	private By userNameField = By.name("username");
	private By passwordField = By.xpath("//input[@type='password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

/*	// Initialize the ActionDriver Object by passing webdriver instance
	public LoginPage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);
	}
*/
	//Constructor - Initialize the ActionDriver Object by passing webdriver instance
	public LoginPage(WebDriver driver) {
		this.actiondriver = BaseClass.getActionDriver();
	}
	
	// Method to Perform Login
	public void login(String userName, String password) {
		actiondriver.enterText(userNameField, userName);
		actiondriver.enterText(passwordField, password);
		actiondriver.click(loginButton);
	}

	// Method to Check if Error message isDisplayed
	public boolean isErrorMessageDisplayed() {
		return actiondriver.isDisplayed(errorMessage);
	}

	// Method to get the text from Error Message
	public String getErrorMessage() {
		return actiondriver.getText(errorMessage);
	}

	// Verify if error message correct or not
	public boolean verifyErrorMessage(String expectedError) {
		return actiondriver.compareTwoText(errorMessage, expectedError);
	}

}

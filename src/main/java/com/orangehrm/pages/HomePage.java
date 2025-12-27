package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {

	private ActionDriver actiondriver;
	
	//Define locators using By class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-img");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");
	
	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchButton = By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By empLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");
	
/*	//Initialize  the ActionDriver Object by passing webdriver instance
	public HomePage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);
	} 
*/
	//Constructor -Initialize  the ActionDriver Object by passing webdriver instance
	public HomePage(WebDriver driver) {
		this.actiondriver = BaseClass.getActionDriver();
	}
	
	//Method to verify if Admin Tab is visible
	public boolean isAdminTabVisble() {
		return actiondriver.isDisplayed(adminTab);
	}
	
	//Method to verify OrangeHRMLogo is displayed
	public boolean verifyOrangeHRMLogo() {
		return actiondriver.isDisplayed(orangeHRMLogo);
	}
	
	//Method to Navigate to PIM Tab
	public void clickOnPIMTab() {
		actiondriver.click(pimTab);
	}
	
	//Method for Employee Search
	public void  employeeSearch(String value) {
		 actiondriver.enterText(employeeSearch,value);
		 actiondriver.click(searchButton);
		 actiondriver.scrollToElement(emplFirstAndMiddleName);	 
	}
	
	//Verify Employee First and Middle Name
	public boolean verifyEmployeeFirstNameAndMiddleName(String emplFirstAndMiddleNameFromDB ) {
		return actiondriver.compareTwoText(emplFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
	}
	
	//Verify Employee Last Name
	public boolean verifyEmployeeLastName(String emplLastFromDB) {
		return actiondriver.compareTwoText(empLastName, emplLastFromDB);
	}
	
	
	//Method to perform logout Operation
	public void Logout() {
		actiondriver.click(userIDButton);
		actiondriver.click(logoutButton);
	}
	
}

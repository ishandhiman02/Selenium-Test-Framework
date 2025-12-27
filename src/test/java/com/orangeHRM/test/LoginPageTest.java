package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setUpPages() { // in setup methods we have creating object of login and home page
		loginPage = new LoginPage(getDriver());
		 homePage = new HomePage(getDriver());
	}
	
//	@Test
//	public void validLoginTest() {

	@Test (dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
//		ExtentManager.startTest("Valid login Test");
		System.out.println("RUnnning testmethod1 on thread:" +Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering UserName and Password");
 //		loginPage.login("admin", "admin123");
		ExtentManager.logStep("Verifying Admin TAb is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisble(),"Admin tab should be visible after successful login");
 		ExtentManager.logStep("Validation successfull");
		homePage.Logout();
		ExtentManager.logStep("Logged out sucessfulluy!");
		staticWait(2);
	}
	
//	@Test
//	public void invalidLoginTest() {
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public void invalidLoginTest(String username, String password) {
//		ExtentManager.startTest("invalid login test");-->This has been used in TestListener
		System.out.println("RUnnning testmethod2 on thread:" +Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page entering userName and Password");
		loginPage.login(username, password);
//		loginPage.login("admin", "admin123");
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid error message");
        ExtentManager.logStep("Validation successfull");
        ExtentManager.logStep("Logged out successfull!");
	    
	}
}

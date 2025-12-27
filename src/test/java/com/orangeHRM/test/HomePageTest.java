package com.orangeHRM.test;

	import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setUpPages() { // in setup methods we have creating object of login and home page
		loginPage = new LoginPage(getDriver());
		 homePage = new HomePage(getDriver());
	}
	
//	@Test
//	public void verifyOrangeHRMLogo() {

	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo(String username, String password) {
//		ExtentManager.startTest("HOme page verify logo test");-->This has been used in TestListener
		ExtentManager.logStep("Navigating to Login Page entering UserName and Password");
		loginPage.login(username, password);
//		loginPage.login("admin", "admin123");

		ExtentManager.logStep("Verifying logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"Logo is not visble");
 		ExtentManager.logStep("Validation successfull");
 		homePage.Logout();
		ExtentManager.logStep("Logged out sucessfulluy!");

	}
}

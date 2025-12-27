package com.orangeHRM.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
	public void VerifyEmployeeNameVerificationFromDB(String emplID, String empName) {
		ExtentManager.logStep("Logging with Admin Credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

		SoftAssert softAssert = getSoftAssert();

		ExtentManager.logStep("click on PIM Tab");
		homePage.clickOnPIMTab();

		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch(empName);

		ExtentManager.logStep("Get the Employee Name from DB");
		String employee_id = emplID;

		// Fetch the data into a map

		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);

		String emplFirstName = employeeDetails.get("firstName");
		String emplMiddleName = employeeDetails.get("middleName");
		String emplLastName = employeeDetails.get("lastName");

		String emplFirstAndMiddleName = (emplFirstName + "" + emplMiddleName).trim();

		// Verification for first and middle name
		ExtentManager.logStep("Verify the employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstNameAndMiddleName(emplFirstAndMiddleName));

		// Verification for last name
		ExtentManager.logStep("Verify the employee last name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName));

		ExtentManager.logStep("DB validation Completed");

		// it will collect all the failures and passes
		softAssert.assertAll(); // it will collect all the failures and passes
	}
}

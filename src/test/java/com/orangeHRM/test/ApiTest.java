package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {

//	@Test(retryAnalyzer = RetryAnalyzer.class)
	@Test
	public void verifyGetUserAPI() {

		SoftAssert softAssert = new SoftAssert();

		// Step 1: Define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " + endPoint);

		// Step 2: Send GET Request
		ExtentManager.logStep("sending GET Request  to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step 3: validate status code
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);

		softAssert.assertTrue(isStatusCodeValid, "Status Code Is Not As Expected");

		if (isStatusCodeValid) {
			ExtentManager.LogStepValidationForAPI("Status Code Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Status Code Validation Failed!");
		}

		// Step 4: validate user name
		ExtentManager.logStep("Validating response body for username");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "UserName is not Valid");
		if (isUserNameValid) {
			ExtentManager.LogStepValidationForAPI("UserName Validation PAssed!");
		} else {
			ExtentManager.logFailureAPI("UserName Vlaidation Failed!");
		}

		// validate user email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "Email is not Valid");
		if (isEmailValid) {
			ExtentManager.LogStepValidationForAPI("Email Validation PAssed!");
		} else {
			ExtentManager.logFailureAPI("Email Vlaidation Failed!");
		}

		softAssert.assertAll();
	}

}

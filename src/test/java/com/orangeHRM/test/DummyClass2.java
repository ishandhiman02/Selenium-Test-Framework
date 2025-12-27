package com.orangeHRM.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {

	@Test
	public void dummyTest2() {
//		String title = driver.getTitle();
//		ExtentManager.startTest("DummyTest2 test");-->This has been used in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep	("Verifying the title");
		  
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching";
		System.out.println("Test Passed - Title is matching");
		ExtentManager.logStep("Validation successful");
	}
}

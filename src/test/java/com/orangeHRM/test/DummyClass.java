 package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass{

	@Test
	public void dummyTest(){
//	 String title =driver.getTitle();
//		ExtentManager.startTest("DummyTest1 Test"); --> This has been used in TestListener
		String title = getDriver().getTitle();
	 ExtentManager.logStep("verifying the title");
//		assert title.equals("OrangeHRM"):"Test Failed - Title is not matching";
	   if (!title.equals("OrangeHRM")) {
	        Assert.fail("Test Failed - Title not matching");
	    }
		System.out.println("Test Passed - Title is matching");
//		ExtentManager.logSkip("this case is skipped");
//	 throw new SkipException("SKipping the test as part of testing");
	}
}

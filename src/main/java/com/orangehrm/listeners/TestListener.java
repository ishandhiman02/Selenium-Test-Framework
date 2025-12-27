  package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer{

//	@Override
//	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
//		annotation.setRetryAnalyzer(RetryAnalyzer.class);
//	}

	//Trigerred when Test Starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		//Start logging in Extent Report
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Log started:"+testName);
		}
    
	//Trigeered when the test succeed  
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
		ExtentManager.LogStepWithScreenshot(BaseClass.getDriver(),"Test Passed successfully!", "Test ENd:" +testName + " - Test Passed");
		}else {
			ExtentManager.LogStepValidationForAPI("Test ENd: "+testName+" - Test Passed");
		}
		
	}

	//Triggereed when a test failed
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
		ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test ENd:" +testName + "- Test Failed");
		}else {
			ExtentManager.logFailureAPI("Test ENd: "+testName+" - Test Failed");
		}
//        EmailUtil.sendEmailAlert(testName, failureMessage);

	}

	//Triggered when a Test Skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped :" +testName);
	}

	//Triggered when suite start
	@Override
	public void onStart(ITestContext context) {
		//Initialize The Extent Reports
		ExtentManager.getReporter();
	}

	//Triggered when suite ends
	@Override
	public void onFinish(ITestContext context) {
        //Flush the Extent Reports
		ExtentManager.endTest();
	}

}

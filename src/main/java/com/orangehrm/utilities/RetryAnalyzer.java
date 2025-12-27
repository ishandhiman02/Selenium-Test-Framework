package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

	private int retryCount =0;// number of retries count
	private static final int maxRetryCount=2;// Max noumber of retries count
	
	@Override
	public boolean retry(ITestResult result) {
		 if(retryCount<maxRetryCount) {
			 retryCount++;
			 return true; //Retry the Test 
		 }else {
			 return false; 
		 }
	}

}

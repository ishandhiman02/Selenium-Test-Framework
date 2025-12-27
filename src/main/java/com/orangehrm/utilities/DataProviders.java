package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {

	private static String FILE_PATH = System.getProperty("user.dir")+ "/src/test/resources/testdata/TestData.xlsx";
	
	@DataProvider(name="validLoginData")
	public static Object[][] validLoginData(){
		return  getSheetData("validLoginData"); // this is sheet name 
	}
	
	@DataProvider(name="inValidLoginData") 
	public static Object[][] inValidLoginData(){
		return getSheetData("inValidLoginData");
	}
	
	@DataProvider(name="emplVerification") 
	public static Object[][] emplVerifcation(){
		return getSheetData("emplVerifcation");
	}
	
	private static Object[][] getSheetData(String sheetName) {
		List<String[]> sheetData= ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

		 if (sheetData.isEmpty()) {
		        return new Object[0][0];
		    }
	     
		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];
		
		for(int i=0;i<sheetData.size();i++) {
			data[i]=sheetData.get(i);
		}
		return data;
	}
}

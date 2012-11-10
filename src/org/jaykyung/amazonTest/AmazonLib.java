package org.jaykyung.amazonTest;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.html5.AppCacheStatus;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.selenesedriver.TakeScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IExtraOutput;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.support.FindBy;

import com.opera.core.systems.OperaDriver;
import com.opera.core.systems.scope.protos.SelftestProtos.RunModulesArg.OutputType;

//import com.sun.java.util.jar.pack.Instruction.Switch;

import jxl.*;  //excel import

import java.io.*;  //to open excel file

public class AmazonLib {

	public String user;
	public String password;
	public static final String loginStatusWelcomeMessage = "Welcome to NVC Press web.";
	public String loginStatus = "You are logged in as ";
	public String loginStatusUserId;  //user id needs to be attached at the end of loginStatus
	//public String db_url = "jdbc:mysql://newvisionpress.org:3306/newvis8_newvisionpress"; 
	
	//public String url;
	public String byId = "id";
	public String byXpath = "xpath";
	public String byText = "text";
	public String byCss = "css";
	public String byClass = "class";
	
	public static WebDriver driver;
	
	//constructor ------------------------------------------------------------
	public AmazonLib(String browserType, String url){
		try{
			switch (browserType){
			case "chrome":
				setupChrome(url);
				break;
			case "firefox":
				setupFirefox(url);
				break;
			case "htmlunit":
				break;
			case "ie":
				break;
			case "safari":
				break;
			case "opera":
				break;
			default:
				Assert.fail("The browser type " + browserType + " is invalid choise");
				break;
			}
		} catch (Exception e){
			Assert.fail("Could not start the browser with this url: " + url);
		}
	}
	public AmazonLib(){
		
	}	
	
	//setup and tear down methods ----------------------------------------------------------------
	public static void setupChrome(String url) {
		try{
			//I need to set this properties for Chrome. don't need for Firefox
			System.setProperty("webdriver.chrome.driver", "C:\\data\\programming\\selenium_chrome_driver\\chromedriver.exe");
			//** alternate code ***********************  USE System.setProperty instead.
				//ChromeOptions options = new ChromeOptions();
				//options.setBinary("C:\\data\\programming\\selenium_chrome_driver\\chromedriver.exe");
				//driver = new ChromeDriver(options);			
			//** end alternate code *******************
			driver = new ChromeDriver();
			//*** find a way to maximize browser window
			//System.out.println("*** Chrome driver started"); 
			driver.get(url);
			closeBrowserTabWithPartOfURL("goodguide");
			
		} catch (Exception e) {
			Assert.fail("*** Error setting up Chrome driver: " + e.getMessage());
		}
	}
	public void setupFirefox(String url) {
		driver = new FirefoxDriver();
		driver.get(url);
		//*** find a way to maximize browser window
	}
	//what are the preference types? where can I find the list?  not working now.
	public void setFirefoxProfile(String key, String value){
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference(key, value);
		driver = new FirefoxDriver();
		
	}
	public void setOpera(String url){
		driver = new OperaDriver();
		driver.get(url);
	}
	public void setupIE(String url) {
		try{
			//I need to set this properties for Chrome. don't need for Firefox
			System.setProperty("webdriver.ie.driver", "C:\\data\\programming\\selenium_ie_driver\\IEDriverServer.exe");
			
			
			driver = new InternetExplorerDriver();
			
		
			
			driver.get(url);
			//*** find a way to maximize browser window
		} catch (Exception e) {
			Assert.fail("*** Error setting up Internet Explorer driver: " + e.getMessage());
		}
	}
	public void setCredential(String user, String password){
		this.user = user;
		this.password = password;
		this.loginStatusUserId = loginStatus + user;
	} 
	public static void tearDown(){
		//waitForPage(40, 1, driver.getCurrentUrl());
		driver.quit();
		System.out.println("Chrome driver stopped");
		//driver.close();  --> only closed the window, the driver instance still in the memory space
	}
	
	//Top navigation links and login status message ---------------------------------------------
	public void clickHomeLink(){
		try{
			//driver.findElement(By.xpath(".//*[@id='topnav']/ul/li[1]/a")).click();
			driver.findElement(By.id("home")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'HOME' link from top navigation menu");
		}

	}
	
	//Common: modify this so I don't have to pass method of finding element.  Use if statement to test each method instead
	public void clickElement(String locator, String locatorType){
		System.out.println("*** trying to find element: " + locator + " type is: " + locatorType);
		try{
			switch (locatorType.toLowerCase()){
			case "id":
				driver.findElement(By.id(locator)).click();
				System.out.println("*** found element by id");
				break;
			case "xpath":
				driver.findElement(By.xpath(locator)).click();
				System.out.println("*** found element by xpath");
				break;
			case "text":
				driver.findElement(By.linkText(locator)).click();
				break;
			case "css":
				driver.findElement(By.cssSelector(locator)).click();
				System.out.println("*** found element by css");
				break;
			case "class":
				break;
			default:
				Assert.fail("*** no valid locator type found");
				break;
			}
		} catch (NoSuchElementException e){
			System.out.println("Can't find the element " + locator + " on this page: " + driver.getTitle());
		} catch (Exception e){
			System.out.println("*** Element not found!");
		}
	}
	

	public void clickGalleyLink(){
		try{
			//driver.findElement(By.xpath(".//*[@id='topnav']/ul/li[3]/a")).click();
			driver.findElement(By.id("gallery")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'GALLERY' link from top navigation menu");
		}

	}
	public void clickForumLink(){
		try{
			//driver.findElement(By.xpath(".//*[@id='topnav_forum']")).click();
			driver.findElement(By.id("forum")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'FORUM' link from top navigation menu");
		}

	}
	public void communityLink(){
		try{
			//driver.findElement(By.xpath(".//*[@id='topnav']/ul/li[7]/a")).click();
			driver.findElement(By.id("community")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'COMMUNITY' link from top navigation menu");
		}

	}
	public void helpLink(){
		try{
			//driver.findElement(By.xpath(".//*[@id='topnav']/ul/li[9]/a")).click();
			driver.findElement(By.id("help")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'HELP' link from top navigation menu");
		}

	}
	public void contactLink(){

		try{
			//driver.findElement(By.xpath(".//*[@id='topnav']/ul/li[11]/a")).click();
			driver.findElement(By.id("contact")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'CONTACT' link from top navigation menu");
		}

	}
	public void loginLink(){
		try{
			//driver.findElement(By.xpath(".//*[@id='topnav']/ul/li[15]/a")).click();
			driver.findElement(By.id("login")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'LOGIN' link from top navigation menu");
		}

	}
	public void login(String userID, String password){
		clickElement("login", null);
		//loginLink();
		setCredential(userID, password);
		driver.findElement(By.xpath(".//*[@id='username']")).sendKeys(user);
		driver.findElement(By.xpath(".//*[@id='password']")).sendKeys(password);
		driver.findElement(By.xpath(".//*[@id='submit']")).click();
	}
	//common method --------------------------------------------------------------------	
	public void sendKeysToElement(String locator, String locatorType, String inputText){
		try{
			switch (locator){
			case "id":
				driver.findElement(By.id(locator)).sendKeys(inputText);
				break;
			case "xpath":
				driver.findElement(By.xpath(locator)).sendKeys(inputText);
				break;
			case "text":
				driver.findElement(By.linkText(locator)).sendKeys(inputText);
				break;
			case "css":
				break;
			case "class":
				break;
			default:
				Assert.fail("locator type " + locatorType + "is not valid locator type, valid types are id, xpath, text, css, or class");
			}
		} catch (Exception e){
			System.out.println("Can't find the element " + locator + " on this page: " + driver.getTitle());
		}
		

	}
	

	public void logoutLink(){
		try{
			//driver.findElement(By.xpath(".//*[@id='topnav']/ul/li[17]/a")).click();
			driver.findElement(By.id("logout")).click();
		} catch (Exception e){
			Assert.fail("Can't find 'LOGOUT' link from top navigation menu");
		}

	}
	public void loginStatusMessage(){
		//this link will take to the user profile page
		try{
			driver.findElement(By.xpath(".//*[@id='logoxx']/span/a")).click();
		} catch (Exception e){
			Assert.fail("Login status message is not there.");
		}
	}
/*	public Boolean verifyLoggedInStatusMessage(){
		verifyText(".//*[@id='logoxx']/span/a", "xpath", loginStatusWelcomeMessage);
	}*/
	//common method ---------------------------------------------------------------------	
	public void verifyText(String locator, String locatorType, String expectedText){
		String actualText;
		try{
			switch (locator){
			case "id":
				Assert.assertEquals(expectedText, driver.findElement(By.id(locator)).getText());				
				break;
			case "name":
				Assert.assertEquals(expectedText, driver.findElement(By.name(locator)).getText());
				break;
			case "text":
				Assert.assertEquals(expectedText, driver.findElement(By.linkText(locator)).getText());
				break;
			case "xpath":
				Assert.assertEquals(expectedText, driver.findElement(By.xpath(locator)).getText());
				break;
			case "class":
				break;
			default:
				Assert.fail("locator type " + locatorType + "is not valid locator type, valid types are id, xpath, text, css, or class");
			}
		} catch (Exception e){
			System.out.println("Can't find the element " + locator + " on this page: " + driver.getTitle());
		}
	}
	public Boolean welcomeMessageVerify(){
		String actualStatusMessage = driver.findElement(By.xpath(".//*[@id='logoxx']/span/a")).getText();
		if(!(actualStatusMessage.equals(loginStatusWelcomeMessage))){
			Assert.fail("You are expecting \'" + loginStatusWelcomeMessage + "\' but got " + actualStatusMessage);
			return false;
		}
		return true;
	}
	
	//left accordion menu ---------------------------------------------------------------------------
	public int accordionMenuOpen(String accordionMenu) {
		String xpath = null;
		int i = 0; //for xpath index
		
		if (accordionMenu.equalsIgnoreCase("press")){
			i = 1;
			System.out.println("\'press\' was passed");
		}
		if (accordionMenu.equalsIgnoreCase("photography")){
			i = 2;
		}
		if (accordionMenu.equalsIgnoreCase("computer")){
			i = 3;
		}
		if (accordionMenu.equalsIgnoreCase("display")){
			i = 4;
		}
		if (accordionMenu.equalsIgnoreCase("design")){
			i = 5;
		}
		if (accordionMenu.equalsIgnoreCase("edit")){
			i = 6;
		}
		if (accordionMenu.equalsIgnoreCase("public")){
			i = 7;
		}
		if (accordionMenu.equalsIgnoreCase("admin")){
			i = 8;
		}
		if (accordionMenu.equalsIgnoreCase("other")){
			i = 9;
		}
		System.out.println("i is: " + i);
		xpath = ".//*[@id='content_left_navigation']/li[" + i + "]/a";
		System.out.println("xpath is " + xpath);
		
		driver.findElement(By.xpath(xpath)).click();
		return i;
	}
	public void accordionSubMenuClick(int accordionMenuNumber, int subMenuNumber){
		String xpath = ".//*[@id='content_left_navigation']/li[" + accordionMenuNumber + "]/ul/li[" + subMenuNumber + "]/a";
		driver.findElement(By.xpath(xpath)).click();
	}
	public void modalPopupVerify(){
		//*** I can't switch to the popup.  driver.getWindowHandles does not provide any information
		//and java script alert: driver.switchTo().alert() doesn't work either
		try{
			driver.findElement(By.xpath("//*[@id='press_dept_ministry']")).click();
			//driver.findElement(By.className("close-reveal-modal")).click();
		} catch (Exception e){
			Assert.fail("Faile to close modal pop up.");
		}
		//*[@id="press_dept_ministry"]
	}
	
	//common method ---------------------------------------------------------------------
	public void verifyExternalLink(String locator, String locatorType, String url) throws InterruptedException{
		String parentWindowHandle = driver.getWindowHandle(); //getting original browser window handle
		driver.findElement(By.id(locator)).click();
		Set<String> windowHandle =driver.getWindowHandles(); //getting all window handles
		
		for(String extHandle: windowHandle ){  //for each handle...
			if(!(extHandle.equalsIgnoreCase(parentWindowHandle))){  
				String externalURL = driver.switchTo().window(extHandle).getCurrentUrl();
				System.out.println("the title of this page is: " + externalURL);
				
				if(!(externalURL.equalsIgnoreCase(url))){
					Assert.fail("External site URL is not matching, please confirm");
				}
				driver.switchTo().window(extHandle).close(); //close external site window
				driver.switchTo().window(parentWindowHandle);  //switch back to original window
			}
		}
	}
	
	
	
	//common method ---------------------------------------------------------------------
	public static void closeBrowserTabWithPartOfURL(String partOfUrl) throws InterruptedException{
		
		//System.out.println("***try to close tab: " + partOfUrl);
		//System.out.println("waiting for Amazon page to load");
		waitForElement(30, 1, WebElements.homeLogoId);
		String parentWindowHandle = driver.getWindowHandle(); //getting original browser window handle
		System.out.println("parentWindowHandle is: " + parentWindowHandle);
		Set<String> windowHandle =driver.getWindowHandles(); //getting all window handles

		//System.out.println("***found number of windows: " + windowHandle.size() + " handles");
		for(String extHandle: windowHandle ){  //for each handle...
			System.out.println("*** URL of each window is: " + driver.switchTo().window(extHandle).getCurrentUrl());
			if(!(extHandle.equalsIgnoreCase(parentWindowHandle))){  
				String externalURL = driver.switchTo().window(extHandle).getCurrentUrl();
				System.out.println("the title of this page is: " + externalURL);
				driver.switchTo().window(extHandle).close(); //close external site window
				driver.switchTo().window(parentWindowHandle);  //switch back to original window
			}
		}
	}
	
	
	//content section
	
	//footer ----------------------------------------------------------------------------------------
	/**
	 * Footer text verification, footer should be same for all pages
	 * 
	 */
	public void verifyFooterText(){
		try{
			driver.findElement(By.xpath(".//*[@id='footer']/p")).isDisplayed();
		} catch (Exception e){
			Assert.fail("Can't find the footer on this page");
		}

	}
	
	//Common: utility methods -------------------------------------------------------------------------------
	public void verifyCurrentURL(String pageURL){
		String currentURL = driver.getCurrentUrl();
		if(!(pageURL.equalsIgnoreCase(currentURL))){
			Assert.fail("You are expecting " + pageURL + " page but actual page is " + currentURL);
		}
	}
	public void submitContactForm(String name, String email, String phoneNum, String message){
		//*** deal with pop-ups for negative test
		try{
			driver.findElement(By.xpath(".//*[@id='contactName']")).sendKeys(name);
			driver.findElement(By.xpath(".//*[@id='contactEmail']")).sendKeys(email);
			driver.findElement(By.xpath(".//*[@id='contactPhoneNumber']")).sendKeys(phoneNum);
			driver.findElement(By.xpath(".//*[@id='contactComments']")).sendKeys(message); 
			driver.findElement(By.xpath(".//*[@id='contactSubmit']")).click();
		} catch (Exception e){
			Assert.fail("The contact form could not submitted, please check again, current page is " + driver.getCurrentUrl());
		}
	}
	public void getTeamPermissionForUser(String userid){
		//*** TBD, get team permission from mySQL database
	}
	public void db_connect() throws SQLException{
		//*** db connection not working, workaround: use known set of users
//		try{
//		 //Connection conn = DriverManager.getConnection(db_url, "newvis8", "pub4ever");
//		} catch (SQLException e){
//			//Assert.fail("database connection error");
//			//e.printStackTrace();
//			//e.getErrorCode();
//		}
	}
	public void runQuery(){
//		try{
//			db_connect();
//		} catch (Exception e){
//			
//		}
	}
	public Boolean isLoggedIn(){
		if((driver.findElement(By.xpath(".//*[@id='logoxx']/span/a")).getText()).equalsIgnoreCase(loginStatusWelcomeMessage)){
			System.out.println("you need to login first");
			return false;
		} else {
			return true;
		}
	}
	//Common: --------------------------------------------------------------------------------------------------
	 public String[][] getTableArray(String xlFilePath, String sheetName, String tableName) throws Exception{
		 //this method reads Excel file and retrieve relevant data and return String array
	        
		 String[][] tabArray=null;
         Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
         Sheet sheet = workbook.getSheet(sheetName); 
         int startRow,startCol, endRow, endCol,ci,cj;
         Cell tableStart=sheet.findCell(tableName);
         startRow=tableStart.getRow();
         startCol=tableStart.getColumn();

         //System.out.println("startRow is: " + startRow);
         //System.out.println("startCol is: " + startCol);
         
         Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);                

         endRow=tableEnd.getRow();
         endCol=tableEnd.getColumn();
         System.out.println("startRow="+startRow+", endRow="+endRow+", " +
                 "startCol="+startCol+", endCol="+endCol);
         tabArray=new String[endRow-startRow-1][endCol-startCol-1];
         ci=0;

         for (int i=startRow+1;i<endRow;i++,ci++){
             cj=0;
             for (int j=startCol+1;j<endCol;j++,cj++){
                 tabArray[ci][cj]=sheet.getCell(j,i).getContents();
             }
         }
	        return(tabArray);
	    }

	//form input field

	public void invalidContactFormInput(){
		//*** deal with pop up error message
		try{
			
		} catch (Exception e){
			
		}
	}
	
	public void checkPageTitle(String pageName){
		if(!(driver.getTitle().toLowerCase().endsWith(pageName)))
		{
			Assert.fail("Page title doesn't match, please check");
		}
	}
	
	public void verifyTextExits(String text){
		try{
			//driver.findElement(By.xpath("//*[contains(.,'Thank you for contacting')]"));
			driver.getPageSource().contains(text);
		} catch (Exception e){ 
			Assert.fail("Message-sent message should be displayed. Check contact form");
		}
	}
	
	public void verifyURL(String url){
		System.out.println("**** running NVCwebLib.verifyURL method");
		Assert.assertEquals("**** URL does not match!", url, driver.getCurrentUrl());
	}
	//Common: --------------------------------------------------------------------------------
	public static void waitForElement(int duration, int poll, String locator){
		//I should call parseLocatorType method and make this method more generic
		WebDriverWait wait = new WebDriverWait(driver, duration, poll);
		try{
			wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));	
		} catch (Exception e) {
			Assert.fail("Element " + locator + "can not be found!");
		}
		
/*		//***alternate code
		 Wait wait = new FluentWait(driver)  
         .withTimeout(30, SECONDS)  
         .pollingEvery(2, SECONDS);  

		 wait.until(ExpectedConditions.titleIs("Login"));*/
	}
	
	
	
	
	//Common: --------------------------------------------------------------------------------
	public void waitForPage(int duration, int poll, String url){
		WebDriverWait wait = new WebDriverWait(driver, duration, poll);
		try{
			wait.until(ExpectedConditions.titleIs(driver.getTitle()));	
		} catch (Exception e) {
			Assert.fail("The page " + url  + "can not be found!");
		}
	}
	//Common: ------------------------------------------------------------------------------------
	public void findElementFromAllWindows(String xpathString, String textToFind){
		for(String handle: driver.getWindowHandles()){
			driver.switchTo().window(handle);
			this.verifyTextExits(textToFind);
		}
	}

	//Common ------------------------------------------------------------------------------------
	public String getAttributeValue(String locator, String attributeKey){
		WebElement element = null;
		String locatorType = parseLocatorType(locator);
		try{
			switch (locatorType){
			case "id":
				element = driver.findElement(By.id(locator));
				break;
			case "xpath":
				element = driver.findElement(By.xpath(locator));
				break;
			case "text":
				element = driver.findElement(By.linkText(locator));
				break;
			case "css":
				break;
			case "class":
				break;
			default:
				break;
			}
		} catch (Exception e){
			System.out.println("Can't find the element " + locator + " on this page: " + driver.getTitle());
		}
		
		String attribute = element.getAttribute(attributeKey);
		//System.out.println("the attribute value for href is : " + attribute);
		
		return attribute;
	}
	//***Actions
	public void dragAndDropAction(String fromLocator, String toLocator){
		Actions builder = new Actions(driver);
		Action dragAndDrop = builder.clickAndHold(driver.findElement(By.id(fromLocator)))
				.moveToElement(driver.findElement(By.id(toLocator)))
				.release(null)
				.build();
		dragAndDrop.perform();
	}
	//*** Actions 
	public void contextMenuAction(String locator){
		
	}
	//*** Actions
	public void selectMultipleItems(String locator){
		WebElement select = driver.findElement(By.id(locator));
		List<WebElement> options = select.findElements(By.tagName("options"));
		Actions builder = new Actions(driver);
		Action action = builder.keyDown(Keys.SHIFT)
						.click(options.get(0))
						.click(options.get(2))
						.build();
		action.perform();		
		
	}
	//*** Application Cache
	public void applicationCacheTest(){
		AppCacheStatus status = ((ApplicationCache)driver).getStatus();
		Assert.assertEquals (AppCacheStatus.UNCACHED, status);
	}
	
	public void isBrowserOnline(){
		//Assert.assertTrue((BrowserConnection)driver).
	}
	
	public void localStorgaeTest(){
		LocalStorage ls = (LocalStorage)driver;
		ls.size();  //return 0 if nothing there
	}
	public void sessionStorageTest(){
		SessionStorage ss = (SessionStorage)driver;
		ss.size();
	}
	public void takeScreenShotTest(){
		//String screenShotBase64 = ((TakesScreenshot)driver).getScreenshotAs(base64);
		 
	}
	//** utility - copy file
	public void copyFile(String originFile, String newFile){
		File in = new File(originFile);
		File out = new File(newFile);
	
		try {
			FileUtils.copyFile(in, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//** utility - parse WebElement name to get locator type
	public String parseLocatorType(String locator){
		if(locator.toLowerCase().contains("id")){
			return "id";
		} else if (locator.toLowerCase().contentEquals("name")){
			return "name";
		} else if (locator.toLowerCase().contentEquals("class")){
			return "class";
		} else if (locator.toLowerCase().contentEquals("css")){
			return "css";
		} else if (locator.toLowerCase().contentEquals("xpath")){
			return "xpath";
		} else {
			return null;
		}
	}
	
	
}

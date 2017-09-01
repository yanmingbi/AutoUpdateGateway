package com.example;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UpdateGuangzhouInternalGateway {
	// the xpath of internal gateway, i.e. the first search result.
	public static final String GATEWAY_INTERNAL_XPATH = "//*[@id=\"applisttable\"]/tbody/tr[1]/td[2]/a";
	// to-update platform
	public static final String GUANGZHOU = "http://101.200.52.215:5085/cloudui/app/pages/login.html";
	// the xpath of application status
	public static final String STATE_XPATH = "/html/body/div[1]/section/div/div[2]/div/div/div[1]/div[2]/p[1]";
	// outer gateway name
	public static final String GATEWAY_HOST_NAME = "gateway_host";
	// internal gateway name
	public static final String GATEWAY_INTERNAL_NAME = "20170103gateway";

	public static void main(String[] args) throws Exception {
		updateInternalGateway();
	}

	/**
	 * updateInternal
	 * 
	 * @Title: updateInternal
	 * @param @throws
	 *            InterruptedException
	 * @return void
	 */
	public static void updateInternalGateway() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "F:/chromedriver_new.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		//////////// log in index page
		driver.get(GUANGZHOU);
		WebElement userNameElement = driver.findElement(By.id("exampleInputEmail1"));
		userNameElement.sendKeys("admin");
		WebElement passwordElement = driver.findElement(By.id("exampleInputPassword1"));
		passwordElement.sendKeys("111111");
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/section/form/button"));
		button.click();
		//////////// --log in index page

		/////////// wait logging in, when success, to find target app
		WebDriverWait secondWait = new WebDriverWait(driver, 60);
		secondWait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a")));
		WebElement appManager = driver.findElement(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a"));
		appManager.click();
		Thread.sleep(1000);
		WebElement inputElement = driver.findElement(By.xpath("/html/body/div/section/div/div[2]/input"));
		inputElement.sendKeys(GATEWAY_INTERNAL_NAME);
		// press keyboard
		inputElement.sendKeys(Keys.ENTER);
		Thread.sleep(500);
		/////////// --wait logging in, when success, to find target app

		/////////// enter to-update gateway
		WebElement gatewayElement = driver.findElement(By.xpath(GATEWAY_INTERNAL_XPATH));
		gatewayElement.click();
		// waiting for load complete of internal gateway
		Thread.sleep(2000);
		WebElement appState = driver.findElement(By.xpath(STATE_XPATH));
		////////// --enter to-update gateway

		// get instance amount
		List<WebElement> list = driver
				.findElements(By.cssSelector("tr > td:nth-child(4) > button.btn.btn-info.btn-sm"));
		int oCount = list.size();
		System.out.println("the instance amount of this page" + oCount);

		int tempoCount = oCount + 1;
		// number input box
		WebElement objectCount = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/table/tbody/tr[" + tempoCount
						+ "]/td/page/div/div[1]/input"));
		if (!objectCount.getAttribute("max").equals("1")) {
			System.out.println("amount of instance greater than 1,please clean");
			return;
		}

		if (appState.getText().equals("应用状态：RUNNING")) {
			// stop button
			WebElement stopElement = driver
					.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/button[4]"));
			stopElement.click();
			// waiting for floating frame in this endless loop
			while (true) {
				if (appState.getText().equals("应用状态：DEPLOYED")) {
					// wait two seconds for floating frame termination
					Thread.sleep(2000);
					break;
				}
				Thread.sleep(500);
			}
		} else {
			System.out.println("the app status not \"RUNNING\", could not stop.");
			return;
		}

		if (appState.getText().equals("应用状态：DEPLOYED")) {
			// uninstall
			WebElement downElement = driver
					.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/button[1]"));
			downElement.click();
			while (true) {
				if (appState.getText().equals("应用状态：FREE")) {
					// wait two seconds for floating frame termination
					Thread.sleep(2000);
					break;
				}
				Thread.sleep(500);
			}
		} else {
			System.out.println("the app status not \"DEPLOYED\", could not uninstall.");
			return;
		}

		// enter "update page"
		WebElement updateElement = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/a"));
		updateElement.click();
		Thread.sleep(1000);
		WebElement typeList = driver
				.findElement(By.xpath("//*[@id=\"formValidate\"]/div/div[1]/fieldset[6]/div/div/select"));
		// convert element to Select
		Select gtl = new Select(typeList);
		// select the last one
		gtl.getOptions().get(gtl.getOptions().size() - 1).click();
		// pause selenium, I can inspect whether it selected the right image
		// version
		Thread.sleep(3000);
		// update app
		WebElement updateClick = driver.findElement(By.xpath("//*[@id=\"formValidate\"]/div/div[5]/button"));
		updateClick.click();
		Thread.sleep(1000);
		// deploy app
		int tempCount = 0;
		while (true) {
			try {
				// use Xpath method could not find target element, so use css
				// selector method
				// WebElement putClick = driver
				// .findElement(By.xpath("/html/body/div/section/div/div[3]/div[2]/table/tbody/tr["
				// + tempListSize
				// + "]/td[4]/button[1])"));
				//
				List<WebElement> putList = driver.findElements(By.cssSelector(".btn.btn-info.btn-sm"));
				System.out.println(putList.size());
				// select the last one
				putList.get(putList.size() - 1).click();
				break;
			} catch (NoSuchElementException e) {
				System.out.println("the " + tempCount + "count, find fail");
				tempCount++;
			}
		} // while(true)

		Thread.sleep(1000);
		List<WebElement> alertList = driver.findElements(By.cssSelector(".btn.btn-primary"));
		for (int i = 0; i < alertList.size(); i++) {
			if (alertList.get(i).getText().equals("确定")) {
				alertList.get(i).click();
			}
		}

		while (true) {
			try {
				if (appState.getText().equals("应用状态：DEPLOYED")) {
					// wait two seconds for floating frame termination
					Thread.sleep(2000);
					break;
				}
			} catch (Exception e) {
				// org.openqa.selenium.StaleElementReferenceException: stale
				// element reference: element is not attached to the page
				// document
				//if selenium catch exception above, it need find again
				appState = driver.findElement(By.xpath(STATE_XPATH));
			}
			Thread.sleep(500);
		}
		Thread.sleep(1000);

		// start app
		WebElement startElement = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/button[5]"));
		startElement.click();
		while (true) {
			if (appState.getText().equals("应用状态：RUNNING")) {
				// wait two seconds for floating frame termination
				Thread.sleep(2000);
				break;
			}
			Thread.sleep(500);
		}
		Thread.sleep(1000);

		// create loadbalance
		WebElement lbElement = driver.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[1]/a[6]"));
		lbElement.click();
		Thread.sleep(2000);
		WebElement createElement = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/div/div/div/a"));
		createElement.click();
		Thread.sleep(3000);
		// WebElement createElement2 = findElement(driver,
		// By.xpath("/html/body/div[2]/div[2]/div[2]/div/div[4]/div/form/button"));

		WebElement createElement2 = findElementByValue(driver, By.cssSelector(".btn.btn-info"), "添加端口映射");
		createElement2.click();
		Thread.sleep(500);
		createElement2.click();
		Thread.sleep(1000);

		////////////////// add specific lb context
		List<WebElement> webElements = findAllSameElement(driver,
				By.cssSelector(".form-control.ng-pristine.ng-untouched.ng-valid"), 6);
		webElements.get(2).sendKeys("mscx-gateway.hanlnk.com");
		webElements.get(4).sendKeys("");
		Select temp3 = new Select(webElements.get(3));
		temp3.getOptions().get(2).click();
		Select temp4 = new Select(webElements.get(5));
		temp4.getOptions().get(2).click();
		List<WebElement> webElements2 = findAllSameElement(driver,
				By.cssSelector(".form-control.ng-untouched.ng-invalid.ng-invalid-required.ng-dirty.ng-valid-number"),
				4);
		webElements2.get(0).sendKeys("58080");
		webElements2.get(1).sendKeys("48081");
		webElements2.get(2).sendKeys("58888");
		webElements2.get(3).sendKeys("58088");
		List<WebElement> webElements3 = findAllSameElement(driver,
				By.cssSelector(".form-control.ng-pristine.ng-untouched.ng-invalid.ng-invalid-required"), 2);
		webElements3.get(0).sendKeys("gateway-web-1.8.0/");
		webElements3.get(1).sendKeys("/");
		// "create" button
		WebElement overTemp = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[3]/button[1]"));
		Thread.sleep(2000);
		overTemp.click();
		// ////////////////// --add specific lb context
		// waiting for the finish of lb update
		Thread.sleep(5000);

		///////////////////// update outer network
		WebElement outerNetwork = driver.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[1]/a[8]"));
		outerNetwork.click();
		// waiting for the finish of outer network panel load
		Thread.sleep(2000);
		WebElement outerNetworkAdd = driver.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/form/a"));
		outerNetworkAdd.click();
		Thread.sleep(1000);
		WebElement network1 = driver.findElement(
				By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/form/table/tbody/tr[8]/td[1]/input"));
		network1.sendKeys("219.143.213.112");
		WebElement network2 = driver.findElement(
				By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/form/table/tbody/tr[8]/td[2]/input"));
		network2.sendKeys("80");
		Thread.sleep(500);
		WebElement createButton = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/form/button"));
		createButton.click();
		/////////////////// --update outer network
		System.exit(0);

	}

	/**
	 * outdate
	 */
	public static WebElement findElement(WebDriver driver, By by) {

		int tempCount = 0;
		while (tempCount <= 1000) {
			try {
				WebElement webElement = driver.findElement((by));
				return webElement;
			} catch (Exception e) {
				System.out.println("the " + tempCount + "count find fail");
				tempCount++;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // while
		return null;
	}

	/**
	 * according to a series of tag, this function return the tag which text equals param value.
	 */
	public static WebElement findElementByValue(WebDriver driver, By by, String value) {
		int tempCount = 0;
		while (tempCount <= 1000) {
			try {
				List<WebElement> list = driver.findElements((by));
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getText().equals(value)) {
						return list.get(i);
					}
				}
			} catch (Exception e) {
				System.out.println("the " + tempCount + "count find fail");
				tempCount++;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // while
		return null;
	}

	/**
	 * findElement
	 * 
	 * @Title: findElement
	 * @Description: according to order to get element value
	 * @param driver
	 * @param by
	 * @param index
	 *           the target index. first element's index is 0
	 * @param targetCount
	 *            the expect amount which findElements() final return. Why do I set this parameter? Because sometime
	 *            selenium could not find out all Elements by using css selector.  
	 * @param @return
	 * @return WebElement
	 */
	public static WebElement findElementByIndex(WebDriver driver, By by, int index, int targetCount) {
		// final search count
		int totalCount = 0;
		// search 1000 times
		while (totalCount <= 1000) {
			try {
				List<WebElement> list = driver.findElements((by));
				int tempSize = list.size();
				if (tempSize != targetCount) {
					System.out.println("the amount wrong. " + "finalCount: " + targetCount + " queryCount: " + tempSize);
					continue;
				}
				for (int i = 0; i < tempSize; i++) {
					if (i == index) {
						return list.get(i);
					}
				}
			} catch (Exception e) {
				System.out.println("the " + totalCount + " count fail");
				totalCount++;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // while
		return null;
	}

	/**
	 * find out all tag which could be find out byBy.
	 * @Title: identicalConditionfindElement
	 * @param driver
	 * @param by
	 * 		mechanism used to locate elements within a document. 
	 * @param targetCount
	 *            if the amount of driver.findElements() does not equal targetCount, find again.
	 */
	public static List<WebElement> findAllSameElement(WebDriver driver, By by, int targetCount) {
		// final search count
		int totalCount = 0;
		// search 1000 times
		while (totalCount <= 1000) {
			try {
				List<WebElement> list = driver.findElements((by));
				int tempSize = list.size();
				if (tempSize != targetCount) {
					System.out.println("the count wrong. " + "finalCount: " + targetCount + " queryCount: " + tempSize);
					continue;
				}
				for (int j = 0; j < targetCount; j++) {
					System.out.println(list.get(j).getTagName());
				}
				return list;
			} catch (Exception e) {
				System.out.println("the " + totalCount + " fail");
				totalCount++;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // while
		return null;
	}

}

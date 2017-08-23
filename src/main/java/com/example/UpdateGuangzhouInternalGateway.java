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

	// 内网网关的xpath，也就是搜索结果的第一个
	public static final String GATEWAY_INTERNAL_XPATH = "//*[@id=\"applisttable\"]/tbody/tr[1]/td[2]/a";
	// 待更新平台
	public static final String GUANGZHOU = "http://101.200.52.215:5085/cloudui/app/pages/login.html";
	// 应用状态的XPATH值
	public static final String STATE_XPATH = "/html/body/div[1]/section/div/div[2]/div/div/div[1]/div[2]/p[1]";
	// 外网网关名称
	public static final String GATEWAY_HOST_NAME = "gateway_host";
	// 内网网关名称
	public static final String GATEWAY_INTERNAL_NAME = "20170103gateway";

	public static void main(String[] args) throws Exception {
		updateInternalGateway();
	}

	/**
	 * updateInternal
	 * @Title: updateInternal
	 * @Description: TODO
	 * @param @throws
	 *            InterruptedException
	 * @return void
	 */
	public static void updateInternalGateway() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "F:/chromedriver_new.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		//////////// 首页登陆
		driver.get(GUANGZHOU);
		WebElement userNameElement = driver.findElement(By.id("exampleInputEmail1"));
		userNameElement.sendKeys("admin");
		WebElement passwordElement = driver.findElement(By.id("exampleInputPassword1"));
		passwordElement.sendKeys("111111");
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/section/form/button"));
		button.click();
		//////////// --首页登陆

		///////////等待登录成功，寻找目标应用
		WebDriverWait secondWait = new WebDriverWait(driver, 20);
		secondWait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a")));
		WebElement appManager = driver.findElement(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a"));
		appManager.click();
		Thread.sleep(1000);
		WebElement inputElement = driver.findElement(By.xpath("/html/body/div/section/div/div[2]/input"));
		inputElement.sendKeys(GATEWAY_INTERNAL_NAME);
		//按键盘键
		inputElement.sendKeys(Keys.ENTER);
		Thread.sleep(500);
		///////////--等待登录成功，寻找目标应用
		
		/////////// 进入待更新网关
		WebElement gatewayElement = driver.findElement(By.xpath(GATEWAY_INTERNAL_XPATH));
		gatewayElement.click();
		//等待内网网关应用完全加载
		Thread.sleep(2000);
		WebElement appState = driver.findElement(By.xpath(STATE_XPATH));
		//////////--进入待更新网关
		
		
		
		// 获取实例数量
		List<WebElement> list = driver
				.findElements(By.cssSelector("tr > td:nth-child(4) > button.btn.btn-info.btn-sm"));
		int oCount = list.size();
		System.out.println("当前页面实例数量为" + oCount);

		int tempoCount = oCount + 1;
		//页面编号框
		WebElement objectCount = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/table/tbody/tr[" + tempoCount
						+ "]/td/page/div/div[1]/input"));
		if (!objectCount.getAttribute("max").equals("1")) {
			System.out.println("实例页数不为1，请清理");
			return;
		}

		if (appState.getText().equals("应用状态：RUNNING")) {
			// 停止键
			WebElement stopElement = driver
					.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/button[4]"));
			stopElement.click();
			//死循环等待浮动框消失
			while (true) {
				if (appState.getText().equals("应用状态：DEPLOYED")) {
					// 等待两秒，等待浮动框消失
					Thread.sleep(2000);
					break;
				}
				Thread.sleep(500);
			}
		} else {
			System.out.println("应用非RUNNING状态，无法停止");
			return;
		}

		if (appState.getText().equals("应用状态：DEPLOYED")) {
			// 卸载
			WebElement downElement = driver
					.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/button[1]"));
			downElement.click();
			while (true) {
				if (appState.getText().equals("应用状态：FREE")) {
					// 等待两秒，等待浮动框消失
					Thread.sleep(2000);
					break;
				}
				Thread.sleep(500);
			}
		} else {
			System.out.println("应用非DEPLOYED状态，无法卸载");
			return;
		}

		// 进入更新页面
		WebElement updateElement = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/a"));
		updateElement.click();
		Thread.sleep(1000);
		WebElement typeList = driver
				.findElement(By.xpath("//*[@id=\"formValidate\"]/div/div[1]/fieldset[6]/div/div/select"));
		// 将元素转换为 Select
		Select gtl = new Select(typeList);
		// 选择最后一项
		gtl.getOptions().get(gtl.getOptions().size() - 1).click();
		// 暂停一下，观察是否选择了正确版本
		Thread.sleep(3000);
		// 更新应用
		WebElement updateClick = driver.findElement(By.xpath("//*[@id=\"formValidate\"]/div/div[5]/button"));
		updateClick.click();
		Thread.sleep(1000);
		// 部署
		int tempCount = 0;
		while (true) {
			try {
				// XPath取不到，因此使用css选择器找元素
				// WebElement putClick = driver
				// .findElement(By.xpath("/html/body/div/section/div/div[3]/div[2]/table/tbody/tr["
				// + tempListSize
				// + "]/td[4]/button[1])"));
				//
				List<WebElement> putList = driver.findElements(By.cssSelector(".btn.btn-info.btn-sm"));
				System.out.println(putList.size());
				//点击最后一项
				putList.get(putList.size() - 1).click();
				break;
			} catch (NoSuchElementException e) {
				System.out.println("第" + tempCount + "次查找失败");
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
					// 等待两秒，等待浮动框完全消失
					Thread.sleep(2000);
					break;
				}
			} catch (Exception e) {
				// org.openqa.selenium.StaleElementReferenceException: stale
				// element reference: element is not attached to the page
				// document
				// 可能报如上异常，需重新获取
				appState = driver.findElement(By.xpath(STATE_XPATH));
			}
			Thread.sleep(500);
		}
		Thread.sleep(1000);
		
		
		// 启动应用
		WebElement startElement = driver
				.findElement(By.xpath("/html/body/div[1]/section/div/div[2]/div/div/div[2]/div[1]/button[5]"));
		startElement.click();
		while (true) {
			if (appState.getText().equals("应用状态：RUNNING")) {
				// 等待两秒，等待浮动框消失
				Thread.sleep(2000);
				break;
			}
			Thread.sleep(500);
		}
		Thread.sleep(1000);

		// 创建负载
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

		////////////////// 添加具体负载内容
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
		// 创建按钮
		WebElement overTemp = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[3]/button[1]"));
		Thread.sleep(2000);
		overTemp.click();
		// ////////////////// --添加具体负载内容
		Thread.sleep(5000);

		///////////////////// 更新外部网络
		WebElement outerNetwork = driver.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[1]/a[8]"));
		outerNetwork.click();
		// 等待外部网络面板加载完毕
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
		/////////////////// --更新外部网络
		System.exit(0);

	}

	/**
	 * 已废弃
	 */
	public static WebElement findElement(WebDriver driver, By by) {

		int tempCount = 0;
		while (tempCount <= 1000) {
			try {
				WebElement webElement = driver.findElement((by));
				return webElement;
			} catch (Exception e) {
				System.out.println("第" + tempCount + "次查找失败");
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
	 * 根据by获取一系列标签，返回value与标签的text相同的元素
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
				System.out.println("第" + tempCount + "次查找失败");
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
	 * @Description: 根据顺序获取元素值
	 * @param driver
	 * @param by
	 * @param index
	 *            待查找元素次序，次序从0开始
	 * @param targetCount
	 *            最终希望通过findElements()方法查询出的标签的总数量。因有时使用css查询标签可能查不全
	 * @param @return
	 * @return WebElement
	 */
	public static WebElement findElementByIndex(WebDriver driver, By by, int index, int targetCount) {
		// 总查询次数
		int totalCount = 0;
		// 查询1000次
		while (totalCount <= 1000) {
			try {
				List<WebElement> list = driver.findElements((by));
				int tempSize = list.size();
				if (tempSize != targetCount) {
					System.out.println("查找元素个数不对." + "finalCount: " + targetCount + " queryCount: " + tempSize);
					continue;
				}
				for (int i = 0; i < tempSize; i++) {
					if (i == index) {
						return list.get(i);
					}
				}
			} catch (Exception e) {
				System.out.println("第" + totalCount + "次查找失败");
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
	 * 查询符合by条件的所有标签
	 * @Title: identicalConditionfindElement
	 * @Description: TODO
	 * @param driver
	 * @param by 查询条件
	 * @param targetCount 符合by条件的标签目标数量，如查询得到的数量与目标数量不同，则再次获取
	 */
	public static List<WebElement> findAllSameElement(WebDriver driver, By by, int targetCount) {
		// 总查询次数
		int totalCount = 0;
		// 查询1000次
		while (totalCount <= 1000) {
			try {
				List<WebElement> list = driver.findElements((by));
				int tempSize = list.size();
				if (tempSize != targetCount) {
					System.out.println("查找元素个数不对." + "finalCount: " + targetCount + " queryCount: " + tempSize);
					continue;
				}
				for (int j = 0; j < targetCount; j++) {
					System.out.println(list.get(j).getTagName());
				}
				return list;
			} catch (Exception e) {
				System.out.println("第" + totalCount + "次查找失败");
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

package com.example;

import java.awt.RenderingHints.Key;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 这个是自动化持续集成过程的代码
  */
public class TestCIGuangzhou {

	// 内网网关的xpath，也就是搜索结果的第一个
	public static final String GATEWAY_INTERNAL_XPATH = "//*[@id=\"applisttable\"]/tbody/tr[1]/td[2]/a";
	// 外网网关的xpath，也就是搜索结果的第二个
	public static final String GATEWAY_HOST_XPATH = "//*[@id=\"applisttable\"]/tbody/tr[2]/td[2]/a";
	// 待更新平台
	public static final String GUANGZHOU = "http://101.200.52.215:5085/cloudui/app/pages/login.html";

	public static final String CINAME = "20170103platformgateway";

	public static void main(String[] args) throws Exception {
		//////////// 首页登陆
		System.setProperty("webdriver.chrome.driver", "I:/chromedriver_for_chrome57.exe");
		WebDriver driver = new ChromeDriver();
		driver.get(GUANGZHOU);
		WebElement userNameElement = driver.findElement(By.id("exampleInputEmail1"));
		userNameElement.sendKeys("admin");
		WebElement passwordElement = driver.findElement(By.id("exampleInputPassword1"));
		passwordElement.sendKeys("111111");
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/section/form/button"));
		button.click();
		//////////// --首页登陆

		//////////// 持续集成
		WebDriverWait zeroWait = new WebDriverWait(driver, 20);
		zeroWait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/aside/div/nav/ul/li[9]/a")));
		WebElement continuousIntegration = driver.findElement(By.xpath("/html/body/div/aside/div/nav/ul/li[9]/a"));
		continuousIntegration.click();
		Thread.sleep(1000);
		outer: for (int i = 1; i <= 10; i++) {
			WebElement tempElement1 = driver
					.findElement(By.xpath("//*[@id=\"tasklisttable\"]/tbody/tr[" + i + "]/td[2]"));
			if (tempElement1.getText().equals(CINAME)) {
				WebElement tempElement2 = driver
						.findElement(By.xpath("//*[@id=\"tasklisttable\"]/tbody/tr[" + i + "]/td[3]/button[2]"));
				tempElement2.click();
				Thread.sleep(500);
				WebElement tempElement3 = driver
						.findElement(By.xpath("//*[@id=\"tasklisttable\"]/tbody/tr[" + i + "]/td[3]/a"));
				tempElement3.click();
				Thread.sleep(5000);
				// 循环查看是否完成
				int count = 0;
				while (true) {
					System.out.println("第" + count + "次查看");
					WebElement tempElement4 = driver
							.findElement(By.xpath("//*[@id=\"buildlisttable\"]/tbody/tr[1]/td[3]"));
					if (!tempElement4.getText().equals("0")) {
						System.out.println("持续集成完成");
						break outer;
					}
					Thread.sleep(5000);
					driver.navigate().refresh();
					count++;
				}
			} // if (tempElement1.getText().equals(CINAME))
		} // outer: for (int i = 1; i <= 10; i++)
			/////////// --持续集成

		WebDriverWait secondWait = new WebDriverWait(driver, 20);
		secondWait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a")));
		WebElement appManager = driver.findElement(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a"));
		appManager.click();

		// TODO 这个的用法值得记一记
		Thread.sleep(1000);
		WebElement inputElement = driver.findElement(By.xpath("/html/body/div/section/div/div[2]/input"));
		inputElement.sendKeys("gateway");
		inputElement.sendKeys(Keys.ENTER);
		Thread.sleep(500);
		// 进入待更新网关
		WebElement gatewayElement = driver.findElement(By.xpath(GATEWAY_INTERNAL_XPATH));
		gatewayElement.click();
		Thread.sleep(500);
		
		
		
		
		// WebElement outNetworkElement =
		// driver.findElement(By.xpath("/html/body/div/section/div/div[3]/div[1]/a[8]"));
		// outNetworkElement.click();
		// Thread.sleep(500);
		// WebElement addIP =
		// driver.findElement(By.xpath("//*[@id=\"ipForm\"]/a"));
		// addIP.click();
		// Thread.sleep(500);
		// WebElement ipInput =
		// driver.findElement(By.xpath("//*[@id=\"ipForm\"]/table/tbody/tr[8]/td[1]/input"));
		// ipInput.sendKeys("127.0.0.1");
		// WebElement portInput =
		// driver.findElement(By.xpath("//*[@id=\"ipForm\"]/table/tbody/tr[8]/td[2]/input"));
		// portInput.sendKeys("80");
		// WebElement addButton =
		// driver.findElement(By.xpath("//*[@id=\"ipForm\"]/button"));
		// addButton.click();
	}// main(String[] args)

}

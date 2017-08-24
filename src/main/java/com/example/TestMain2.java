
package com.example;

import java.util.Collection;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestMain2 {

	// �������ص�xpath��Ҳ������������ĵ�һ��
	public static final String GATEWAY_INTERNAL_XPATH = "//*[@id=\"applisttable\"]/tbody/tr[1]/td[2]/a";
	// ������ƽ̨
	public static final String GUANGZHOU = "http://101.200.52.215:5085/cloudui/app/pages/login.html";
	// Ӧ��״̬��XPATHֵ
	public static final String STATE_XPATH = "/html/body/div[1]/section/div/div[2]/div/div/div[1]/div[2]/p[1]";
	// ������������
	public static final String GATEWAY_HOST_NAME = "gateway_host";
	// ������������
	public static final String GATEWAY_INTERNAL_NAME = "20170103gateway";
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		System.setProperty("webdriver.chrome.driver", "F:/chromedriver_new.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		//////////// ��ҳ��½
		driver.get(GUANGZHOU);
		WebElement userNameElement = driver.findElement(By.id("exampleInputEmail1"));
		userNameElement.sendKeys("admin");
		WebElement passwordElement = driver.findElement(By.id("exampleInputPassword1"));
		passwordElement.sendKeys("111111");
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/section/form/button"));
		button.click();
		//////////// --��ҳ��½

		///////////�ȴ���¼�ɹ���Ѱ��Ŀ��Ӧ��
		WebDriverWait secondWait = new WebDriverWait(driver, 20);
		secondWait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a")));
		WebElement appManager = driver.findElement(By.xpath("/html/body/div/aside/div/nav/ul/li[5]/a"));
		appManager.click();
		Thread.sleep(1000);
		WebElement inputElement = driver.findElement(By.xpath("/html/body/div/section/div/div[2]/input"));
		inputElement.sendKeys(GATEWAY_INTERNAL_NAME);
		//�����̼�
		inputElement.sendKeys(Keys.ENTER);
		Thread.sleep(500);
		///////////--�ȴ���¼�ɹ���Ѱ��Ŀ��Ӧ��
		
		/////////// �������������
		WebElement gatewayElement = driver.findElement(By.xpath(GATEWAY_INTERNAL_XPATH));
		gatewayElement.click();
		//�ȴ���������Ӧ����ȫ����
		Thread.sleep(2000);
		WebElement appState = driver.findElement(By.xpath(STATE_XPATH));
		//////////--�������������

		
		WebElement outerNetwork = driver.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[1]/a[8]"));
		outerNetwork.click();
		
		Thread.sleep(2000);
		WebElement outerNetworkAdd = driver.findElement(By.xpath("/html/body/div[1]/section/div/div[3]/div[2]/form/a"));
		outerNetworkAdd.click();
		outerNetworkAdd.click();
		outerNetworkAdd.click();
		
		
		
		
		
		
		
	}

}

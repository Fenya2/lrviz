package ru.urfu.lrviz.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
public class SeleniumConfig {
    /**
     * Свойство, отвечающее за запуск selenium тестов с отрисовкой GUI
     */
    public static final String SELENIUM_HEADLESS_PROPERTY = "selenium.headless";

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public WebDriver webDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty(SELENIUM_HEADLESS_PROPERTY, Boolean.TRUE.toString()))) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }
}
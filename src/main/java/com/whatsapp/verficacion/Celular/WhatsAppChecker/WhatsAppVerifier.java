package com.whatsapp.verficacion.Celular.WhatsAppChecker;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.File;
import java.nio.file.Paths;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class WhatsAppVerifier {
    
    private WebDriver driver;

    @PostConstruct
    public void init(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        String userData = Paths.get("./src/main/resources/static/chrome-profile").toAbsolutePath().toString();
        File profile = new File(userData);

        if (!profile.exists()){
            profile.mkdirs();
        }
        
        options.addArguments("--user-data-dir="+userData);
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1200");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    public String getQR() {
        try {
            driver.get("https://web.whatsapp.com");
            Thread.sleep(4000);
            WebElement qrElement = driver.findElement(By.xpath(("//*[@id=\'app\']/div/div[2]/div[2]/div[1]/div/div/div[2]/div[2]/div[1]")));
            String qrData = qrElement.getAttribute("data-ref");
            return qrData;
        } catch (Exception e) {
            System.out.println("Error al obtener el QR:"+e.getMessage());
            return "Error";
        }
    }

    public boolean isLoggin(){
        try{
            Thread.sleep(5000);
            WebElement inicioElement = driver.findElement(By.xpath("//*[@id='side']/div[1]/div/div[2]/div[1]"));
            if (inicioElement.getText().equals("Search") || inicioElement.getText().equals("Buscar")){
                return true;
            }else{
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }

    public String numberCheck(String phoneNumber) {
        try {
            String url = "https://web.whatsapp.com/send/?phone=%2B"+phoneNumber+"&text&type=phone_number&app_absent=0";
            driver.get(url);
            Thread.sleep(6000);
            /*
             * try {
    WebElement elemento = driver.findElement(By.xpath("//*[text()='Texto que buscas']"));
    System.out.println("El texto fue encontrado.");
} catch (NoSuchElementException e) {
    System.out.println("El texto no fue encontrado.");
}

             */
            WebElement element;
            try{
                element = driver.findElement(By.xpath("//*[@id='app']/div/span[2]/div/span/div/div/div/div/div/div[2]/div/button/div/div"));
            } catch (Exception e) {
                element = driver.findElement(By.xpath("//*[@id='main']/footer/div[1]/div/span/div/div[2]/div[1]/div/div[2]/div"));
            }
            WebElement element2 = driver.findElement(By.xpath("//*[@id='app']/div/span[2]/div/span/div/div/div/div/div/div[1]"));
            String existenceText2 = element2.getText();
            String existenceText = element.getText();
            if (existenceText.contains("OK") && (existenceText2.contains("El número de teléfono compartido a través de la dirección URL no es válido.") || existenceText2.contains("The phone number shared via the URL is invalid."))){
                return "No";
            }else if (existenceText.contains("Cancelar") || existenceText.contains("Cancel") || existenceText.contains("Escribe un mensaje") || existenceText.contains("Write a message")) {
                return "Si";
            }else {
                return "Repetir";
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "Error al buscar el número.";
        }
    }

    public String openMenu() {
        try {
            driver.get("https://web.whatsapp.com");
            Thread.sleep(5000);
            WebElement menuButton = driver.findElement(By.xpath("//*[@id='app']/div/div[3]/div[3]/header/header/div/span/div/span/div[2]/div"));
            menuButton.click();
            Thread.sleep(1000);
            return "Menú abierto";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "Error al abrir el menú."; 
        }
    }

    public String logout() {
        try {
            WebElement logoutButton = driver.findElement(By.xpath("//*[@id='app']/div/div[3]/div[3]/header/header/div/span/div/span/div[2]/span/div/ul/li[5]"));
            logoutButton.click();
            Thread.sleep(1000);
            logoutButton = driver.findElement(By.xpath("//*[@id='app']/div/span[2]/div/div/div/div/div/div/div[2]/div/button[2]"));
            Thread.sleep(7000);
            return "Sesion cerrada con éxito.";
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "Error al cerrar la sesión, cierrela desde su dispositivo.";
        }
    }

    @PreDestroy
    public void cleanup() {
        driver.quit();
    }
}
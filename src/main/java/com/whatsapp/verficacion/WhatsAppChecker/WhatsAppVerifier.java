package com.whatsapp.verficacion.WhatsAppChecker;

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
    private String text1 = "(text(), 'El número de teléfono compartido a través de la dirección URL no es válido.')";
    private String text2 = "(text(), 'The phone number shared via the URL is invalid.')";
    private String text3 = "(text(), 'Write a message')";
    private String text4 = "(text(), 'Escribe un mensaje')";
    private String text5 = "(text(), 'Buscar')";
    private String text6 = "(text(), 'Search')";

    @PostConstruct
    public void init() {
        /*
                    * // Guardar cookies
            FileOutputStream fileOut = new FileOutputStream("cookies.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(driver.manage().getCookies());
            out.close();

            // Cargar cookies
            FileInputStream fileIn = new FileInputStream("cookies.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Set<Cookie> cookies = (Set<Cookie>) in.readObject();
            in.close();

            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }

        */
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        String userData = Paths.get("./src/main/resources/static/chrome-profile").toAbsolutePath().toString();
        File profile = new File(userData);

        if (!profile.exists()) {
            profile.mkdirs();
        }

        options.addArguments("--user-data-dir=" + userData);
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
            WebElement qrElement = driver
                    .findElement(By.xpath(("//*[@id=\'app\']/div/div[2]/div[2]/div[1]/div/div/div[2]/div[2]/div[1]")));
            String qrData = qrElement.getAttribute("data-ref");
            return qrData;
        } catch (Exception e) {
            System.out.println("Error al obtener el QR:" + e.getMessage());
            return "Error";
        }
    }

    public boolean isLoggin() {
        try {
            Thread.sleep(5000);
            WebElement inicioElement = driver.findElement(By.xpath("//*[@id='side']/div[1]/div/div[2]/div[1]"));
            if (inicioElement.getText().equals("Search") || inicioElement.getText().equals("Buscar")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String numberCheck(String phoneNumber) {
        try {
            String url = "https://web.whatsapp.com/send/?phone=%2B" + phoneNumber + "&text&type=phone_number&app_absent=0";
            driver.get(url);
            Thread.sleep(5500);
            WebElement element = null, element2 = null;
            try {
                element = driver.findElement(By.xpath("//*[contains" + text1 + "or contains" + text2 + "]"));
            } catch (Exception e) {
                element2 = driver.findElement(By.xpath("//*[contains" + text3 + "or contains" + text4 + "or contains"
                        + text5 + "or contains" + text6 + "or contains" + "]"));
            }
            if(element != null){
                return "No";
            }else if (element2 != null){
                return "Si";
            }else{
                return "Repetir";
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "Error al buscar el número.";
        }
    }

    public String openMenu() {
        try {
            driver.get("https://web.whatsapp.com");
            Thread.sleep(5000);
            WebElement menuButton = driver.findElement(
                    By.xpath("//*[@id='app']/div/div[3]/div[3]/header/header/div/span/div/span/div[2]/div"));
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
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "Error al cerrar la sesión, cierrela desde su dispositivo.";
        }
    }

    @PreDestroy
    public void cleanup() {
        driver.quit();
        System.out.println("Se cerro el navegador.");
    }
}
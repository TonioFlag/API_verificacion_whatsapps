package com.whatsapp.verficacion.Celular;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CelularController {

    private final CelularService celularService;

    @GetMapping
    public String getMethodName() {
        return "Hola Mundo...";
    }
    
    @GetMapping("/whatsapp/{phone}")
    public ResponseEntity<?> whatsappVerify(@PathVariable String phone) {
        if (phone.contains("+")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: El número de teléfono no debe contener el carácter '+'");
        }
        if (phone.contains(" ")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: El número de teléfono no debe contener espacios.");
        }

        Celular response = celularService.verificarCelular(phone);
        if (response.getWhatsapp().equals("Sesión no iniciada. Genera un QR para iniciar sesión.")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }
    @GetMapping("/getqr")
    public String getQR() {
        return celularService.extractQR();
    }
    @GetMapping("/isloggin")
    public boolean isLoggin() {
        return celularService.sesionCheck();
    }
    
    @GetMapping("/logout")
    public String logoutWhatsapp() {
        return celularService.proccesLogout();
    }
}

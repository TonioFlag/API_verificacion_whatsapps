package com.whatsapp.verficacion.Celular;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CelularController {

    private final CelularService celularService;

    @GetMapping("/whatsapp")
    public List<Celular> getAllCelular() {
        return celularService.getAll();
    }
    
    @PostMapping("/whatsapp")
    public ResponseEntity<?> whatsappVerify(@RequestBody Celular celular) {
        if (!celular.getCelular().matches("^\\d{5,20}$")) {
            Map<String, Object> response = new HashMap<>();
            response.put("data", celular);
            response.put("message", "El número de teléfono debe contener entre 5 y 20 dígitos. No debe tener caracteres especiales.");
            response.put("status","Error" );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        Celular response = celularService.verificarCelular(celular.getCelular());

        if (response.getWhatsapp() == null) {
            Map<String,Object> response_error = new HashMap<>();
            response_error.put("data", response);
            response_error.put("message", "Error al buscar el número.");
            response_error.put("status", "Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_error);
        } else {
            return ResponseEntity.ok(response);
        }
    }
    @GetMapping("/getqr")
    public ResponseEntity<Object> getQR() {
        Object response = celularService.extractQR();
        if(response == null){
            Map<String,Object> response_error = new HashMap<>();
            response_error.put("data", response);
            response_error.put("message", "Error al generar el código QR.");
            response_error.put("status", "Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_error);
        }else{
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/islogin")
    public ResponseEntity<Map<String, Boolean>> isLoggin() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("islogin", celularService.sesionCheck());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutWhatsapp() {
        String response = celularService.proccesLogout();
        if (response == null){
            Map<String,String> response_error = new HashMap<>();
            response_error.put("data", response);
            response_error.put("recomend", "Cierre la sesión manualmente desde su celular.");
            response_error.put("message", "Error al cerrar la sesión.");
            response_error.put("status", "Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_error);
        }else{
            Map<String,String> response_succes = new HashMap<>();
            response_succes.put("data", response);
            response_succes.put("recomend", "Asegúrese de que en su dispositivo no salga conectado.");
            return ResponseEntity.ok(response_succes);
        }
    }
}

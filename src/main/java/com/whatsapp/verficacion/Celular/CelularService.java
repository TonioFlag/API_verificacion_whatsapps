package com.whatsapp.verficacion.Celular;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.whatsapp.verficacion.Celular.WhatsAppChecker.WhatsAppVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CelularService {

    private final CelularRepository celularRepository;
    private final WhatsAppVerifier verifier;

    public boolean sesionCheck() {
        return verifier.isLoggin();
    }

    public Celular verificarCelular(String celular) {
        Optional<Celular> optionalCelular  = celularRepository.findByCelular(celular);
        LocalDate now = LocalDate.now();

        if (optionalCelular.isPresent()) {
            Celular exist = optionalCelular.get();
            LocalDate dateConsul = exist.getFecha();
            Long monthBetween = ChronoUnit.MONTHS.between(dateConsul, now);

            if (monthBetween >= 6) {
                if (verifier.isLoggin()) {
                    String result = verifier.numberCheck(celular);
                    exist.setWhatsapp(result);
                    exist.setFecha(now);
                    celularRepository.save(exist);
                } else {
                    exist.setWhatsapp("Sesión no iniciada. Genera un QR para iniciar sesión.");
                }
            }
            return exist;
        } else {
            Celular nuevo = new Celular();
            nuevo.setCelular(celular);
            nuevo.setFecha(now);

            if (verifier.isLoggin()) {
                String result = verifier.numberCheck(celular);
                nuevo.setWhatsapp(result);
            } else {
                nuevo.setWhatsapp("Sesión no iniciada. Genera un QR para iniciar sesión.");
            }

            celularRepository.save(nuevo);
            return nuevo;
        }
    }

    public String extractQR(){
        return verifier.getQR();
    }

    public String proccesLogout() {
        String response = verifier.openMenu();
        if (response.equals("Menú abierto")){
            return verifier.logout();
        }else{
            return response;
        }
    }
}
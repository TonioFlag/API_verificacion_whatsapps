package com.whatsapp.verficacion.Celular;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.whatsapp.verficacion.WhatsAppChecker.WhatsAppVerifier;

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
        Optional<Celular> optionalCelular  = celularRepository.findById(celular);
        LocalDate now = LocalDate.now();

        if (optionalCelular.isPresent()) {
            Celular exist = optionalCelular.get();
            LocalDate dateConsul = exist.getFecha();
            Long monthBetween = ChronoUnit.MONTHS.between(dateConsul, now);
            String verfiquier = exist.getWhatsapp();

            if (monthBetween >= 6 || verfiquier == null) {
                String result = verifier.numberCheck(celular);
                exist.setWhatsapp(result);
                exist.setFecha(now);
                celularRepository.save(exist);
            }
            return exist;
        } else {
            Celular nuevo = new Celular();
            nuevo.setCelular(celular);
            nuevo.setFecha(now);

            String result = verifier.numberCheck(celular);
            nuevo.setWhatsapp(result);

            celularRepository.save(nuevo);
            return nuevo;
        }
    }

    public Object extractQR() {
        return verifier.getQR();
    }

    public String proccesLogout() {
        String response = verifier.openMenu();
        if (response.equals("Menú abierto")){
            return verifier.logout();
        }else{
            return null;
        }
    }

    public List<Celular> getAll() {
        return celularRepository.findAll();
    }
}
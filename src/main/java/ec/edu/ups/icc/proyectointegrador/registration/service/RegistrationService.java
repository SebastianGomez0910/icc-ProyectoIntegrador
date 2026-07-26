// 📁 registration/service/RegistrationService.java
package ec.edu.ups.icc.proyectointegrador.registration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ec.edu.ups.icc.proyectointegrador.registration.dto.RegistrationResponseDto;

public interface RegistrationService {

    RegistrationResponseDto register(Long eventId, String participantEmail);

    void cancel(Long registrationId, String participantEmail);

    Page<RegistrationResponseDto> getMyRegistrations(String participantEmail, Pageable pageable);
}
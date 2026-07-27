package ec.edu.ups.icc.proyectointegrador.registration.controller;

@RestController
@RequestMapping("/events/{eventId}/registrations")

public class EventRegistrationController {

    private final RegistrationService registrationService;

    public EventRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<RegistrationResponseDto> register(
            @PathVariable Long eventId, Authentication authentication) {
        RegistrationResponseDto registration = registrationService.register(eventId, authentication.getName());
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }
    
}

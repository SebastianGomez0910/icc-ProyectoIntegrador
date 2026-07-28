package ec.edu.ups.icc.proyectointegrador.report.service;

public class ReportServiceImpl {
    
    
    private static final List<String> REPORTABLE_STATUSES = List.of("PENDING", "CONFIRMED");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ReportServiceImpl(RegistrationRepository registrationRepository,
            EventRepository eventRepository,
            UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }
    
}

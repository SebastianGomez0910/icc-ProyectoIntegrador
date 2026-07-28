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

    @Override
    @Transactional(readOnly = true)
    public byte[] generateEventRegistrationsPdf(Long eventId, String requesterEmail) {
        Event event = getEventChecked(eventId, requesterEmail);
        List<Registration> registrations = registrationRepository
                .findByEventIdAndStatusIn(eventId, REPORTABLE_STATUSES);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

            document.add(new Paragraph("Listado de inscritos", titleFont));
            document.add(new Paragraph("Evento: " + event.getTitle(), subtitleFont));
            document.add(new Paragraph("Total de inscritos: " + registrations.size(), subtitleFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            addHeaderCell(table, "Participante", headerFont);
            addHeaderCell(table, "Correo", headerFont);
            addHeaderCell(table, "Estado", headerFont);
            addHeaderCell(table, "Fecha de inscripción", headerFont);

            for (Registration r : registrations) {
                User participant = r.getParticipant();
                table.addCell(new PdfPCell(new com.lowagie.text.Phrase(
                        participant.getFirstName() + " " + participant.getLastName(), cellFont)));
                table.addCell(new PdfPCell(new com.lowagie.text.Phrase(participant.getEmail(), cellFont)));
                table.addCell(new PdfPCell(new com.lowagie.text.Phrase(r.getStatus(), cellFont)));
                table.addCell(new PdfPCell(new com.lowagie.text.Phrase(formatDate(r), cellFont)));
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el reporte PDF de inscritos.", e);
        }
    }

}

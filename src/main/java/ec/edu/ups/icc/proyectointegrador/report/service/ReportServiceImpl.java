package ec.edu.ups.icc.proyectointegrador.report.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import ec.edu.ups.icc.proyectointegrador.common.exception.domain.BusinessRuleException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ForbiddenOperationException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ResourceNotFoundException;
import ec.edu.ups.icc.proyectointegrador.event.entity.Event;
import ec.edu.ups.icc.proyectointegrador.event.repositories.EventRepository;
import ec.edu.ups.icc.proyectointegrador.registration.entity.Registration;
import ec.edu.ups.icc.proyectointegrador.registration.repositories.RegistrationRepository;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;

@Service
public class ReportServiceImpl implements ReportService {

    private static final List<String> REPORTABLE_STATUSES = List.of("PENDING", "CONFIRMED");
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Guayaquil");
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
    public byte[] generateEventRegistrationsPdf(Long eventId, String requesterEmail, LocalDate startDate, LocalDate endDate) {
        Event event = getEventChecked(eventId, requesterEmail);
        List<Registration> registrations = getFilteredRegistrations(eventId, startDate, endDate);

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
            document.add(new Paragraph("Total de inscritos (filtrados): " + registrations.size(), subtitleFont));
            if (startDate != null || endDate != null) {
                document.add(new Paragraph("Rango de fechas: " + (startDate != null ? startDate : "Inicio") + " al " + (endDate != null ? endDate : "Fin"), subtitleFont));
            }
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

    @Override
    @Transactional(readOnly = true)
    public byte[] generateEventRegistrationsExcel(Long eventId, String requesterEmail, LocalDate startDate, LocalDate endDate) {
        Event event = getEventChecked(eventId, requesterEmail);
        List<Registration> registrations = getFilteredRegistrations(eventId, startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inscritos");

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("Evento: " + event.getTitle());

            Row header = sheet.createRow(2);
            String[] columns = { "Participante", "Correo", "Estado", "Fecha de inscripción" };
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIndex = 3;
            for (Registration r : registrations) {
                User participant = r.getParticipant();
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(participant.getFirstName() + " " + participant.getLastName());
                row.createCell(1).setCellValue(participant.getEmail());
                row.createCell(2).setCellValue(r.getStatus());
                Cell dateCell = row.createCell(3);
                dateCell.setCellValue(formatDate(r));
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el reporte Excel de inscritos.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateRegistrationCertificatePdf(Long registrationId, String requesterEmail) {
        User requester = getUserByEmail(requesterEmail);

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inscripción no encontrada con ID: " + registrationId));

        if (!registration.getParticipant().getId().equals(requester.getId())) {
            throw new ForbiddenOperationException("No puede descargar el comprobante de otra persona.");
        }

        if (!"CONFIRMED".equalsIgnoreCase(registration.getStatus())) {
            throw new BusinessRuleException("Solo se puede emitir comprobante de una inscripción confirmada.");
        }

        Event event = registration.getEvent();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph title = new Paragraph("Comprobante de inscripción", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Participante: " + registration.getParticipant().getFirstName()
                    + " " + registration.getParticipant().getLastName(), bodyFont));
            document.add(new Paragraph("Evento: " + event.getTitle(), bodyFont));
            document.add(new Paragraph("Código de inscripción: " + registration.getRegistrationCode(), bodyFont));
            document.add(new Paragraph("Estado: " + registration.getStatus(), bodyFont));
            document.add(new Paragraph("Fecha de inscripción: " + formatDate(registration), bodyFont));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el comprobante en PDF.", e);
        }
    }

    // Método auxiliar para obtener y filtrar las inscripciones por rango de fechas de forma limpia
    private List<Registration> getFilteredRegistrations(Long eventId, LocalDate startDate, LocalDate endDate) {
        List<Registration> registrations = registrationRepository
                .findByEventIdAndStatusIn(eventId, REPORTABLE_STATUSES);

        if (startDate == null && endDate == null) {
            return registrations;
        }

        return registrations.stream().filter(r -> {
            if (r.getRegisteredAt() == null) return false;
            LocalDate regDate = r.getRegisteredAt().toLocalDate();

            if (startDate != null && regDate.isBefore(startDate)) {
                return false;
            }
            if (endDate != null && regDate.isAfter(endDate)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    private Event getEventChecked(Long eventId, String requesterEmail) {
        User requester = getUserByEmail(requesterEmail);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + eventId));

        boolean isOwner = event.getOrganizer() != null
                && event.getOrganizer().getId().equals(requester.getId());

        if (!isOwner && !isAdmin(requester)) {
            throw new ForbiddenOperationException(
                    "Solo el organizador propietario del evento o un ADMIN pueden generar este reporte.");
        }
        return event;
    }

    private boolean isAdmin(User user) {
        if (user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream().anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado."));
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new com.lowagie.text.Phrase(text, font));
        table.addCell(cell);
    }

    private String formatDate(Registration r) {
        if (r.getRegisteredAt() == null) {
            return "-";
        }
        ZonedDateTime guayaquilTime = r.getRegisteredAt()
                .atZone(ZoneOffset.UTC) // Le decimos que el dato original está en UTC
                .withZoneSameInstant(BUSINESS_ZONE); // Lo convertimos a la zona horaria de Guayaquil
        return DATE_FORMAT.format(guayaquilTime);
    }
}
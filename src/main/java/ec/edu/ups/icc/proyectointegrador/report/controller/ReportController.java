package ec.edu.ups.icc.proyectointegrador.report.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.proyectointegrador.report.service.ReportService;

@RestController
public class ReportController {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports/events/{eventId}/registrations.pdf")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public ResponseEntity<byte[]> eventRegistrationsPdf(@PathVariable Long eventId, Authentication authentication) {
        byte[] pdf = reportService.generateEventRegistrationsPdf(eventId, authentication.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"registrations-event-" + eventId + ".pdf\"")
                .body(pdf);
    }

    @GetMapping("/reports/events/{eventId}/registrations.xlsx")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public ResponseEntity<byte[]> eventRegistrationsExcel(@PathVariable Long eventId, Authentication authentication) {
        byte[] xlsx = reportService.generateEventRegistrationsExcel(eventId, authentication.getName());
        return ResponseEntity.ok()
                .contentType(XLSX_MEDIA_TYPE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"registrations-event-" + eventId + ".xlsx\"")
                .body(xlsx);
    }

    @GetMapping("/registrations/{id}/certificate.pdf")
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<byte[]> registrationCertificate(@PathVariable Long id, Authentication authentication) {
        byte[] pdf = reportService.generateRegistrationCertificatePdf(id, authentication.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"certificate-registration-" + id + ".pdf\"")
                .body(pdf);
    }
}


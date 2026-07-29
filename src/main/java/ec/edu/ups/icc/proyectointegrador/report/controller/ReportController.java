package ec.edu.ups.icc.proyectointegrador.report.controller;

import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.proyectointegrador.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reportes y Comprobantes", description = "Endpoints para la generación de reportes de inscritos en PDF/Excel y descarga de certificados")
@RestController
public class ReportController {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Generar reporte de inscritos en PDF", description = "Permite al organizador o administrador descargar un reporte en PDF con los inscritos al evento, filtrando opcionalmente por un rango de fechas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte PDF generado exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver reportes de este evento")
    })
    @GetMapping("/reports/events/{eventId}/registrations.pdf")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public ResponseEntity<byte[]> eventRegistrationsPdf(@PathVariable Long eventId, @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate, Authentication authentication) {
        byte[] pdf = reportService.generateEventRegistrationsPdf(eventId, authentication.getName(), startDate, endDate);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"registrations-event-" + eventId + ".pdf\"")
                .body(pdf);
    }

    @Operation(summary = "Generar reporte de inscritos en Excel", description = "Permite al organizador o administrador descargar una hoja de cálculo en Excel con los inscritos al evento, filtrando opcionalmente por un rango de fechas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte Excel generado exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver reportes de este evento")
    })
    @GetMapping("/reports/events/{eventId}/registrations.xlsx")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public ResponseEntity<byte[]> eventRegistrationsExcel(@PathVariable Long eventId, @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate, Authentication authentication) {
        byte[] xlsx = reportService.generateEventRegistrationsExcel(eventId, authentication.getName(), startDate, endDate);
        return ResponseEntity.ok()
                .contentType(XLSX_MEDIA_TYPE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"registrations-event-" + eventId + ".xlsx\"")
                .body(xlsx);
    }

    @Operation(summary = "Descargar certificado de inscripción", description = "Permite al participante descargar el comprobante o certificado en PDF de su inscripción confirmada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Certificado generado exitosamente"),
        @ApiResponse(responseCode = "400", description = "La inscripción no está confirmada"),
        @ApiResponse(responseCode = "403", description = "No puede descargar el comprobante de otra persona")
    })
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
package ec.edu.ups.icc.proyectointegrador.report.service;

import java.time.LocalDate;

public interface ReportService {

    byte[] generateEventRegistrationsPdf(Long eventId, String requesterEmail, LocalDate startDate, LocalDate endDate);

    byte[] generateEventRegistrationsExcel(Long eventId, String requesterEmail, LocalDate startDate, LocalDate endDate);

    byte[] generateRegistrationCertificatePdf(Long registrationId, String requesterEmail);

}

package ec.edu.ups.icc.proyectointegrador.report.service;

public interface ReportService {

    byte[] generateEventRegistrationsPdf(Long eventId, String requesterEmail);

    byte[] generateEventRegistrationsExcel(Long eventId, String requesterEmail);

    byte[] generateRegistrationCertificatePdf(Long registrationId, String requesterEmail);

}

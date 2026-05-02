package net.mkubik.gmms.controller.api.v1;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.api.ReportApi;
import net.mkubik.gmms.api.model.RevenueReportResponse;
import net.mkubik.gmms.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController implements ReportApi {

    private final ReportService reportService;

    @Override
    public ResponseEntity<RevenueReportResponse> getRevenueReport() {
        RevenueReportResponse response = reportService.getRevenueReport();
        return ResponseEntity.ok(response);
    }
}
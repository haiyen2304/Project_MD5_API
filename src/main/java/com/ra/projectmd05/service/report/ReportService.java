package com.ra.projectmd05.service.report;

import com.ra.projectmd05.model.dto.request.ReportRequestDTO;
import com.ra.projectmd05.model.entity.Report;

public interface ReportService {

   Report createReport(ReportRequestDTO reportRequestDTO);
}

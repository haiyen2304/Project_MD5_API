package com.ra.projectmd05.controller.user.report;


import com.ra.projectmd05.model.dto.request.ReportRequestDTO;
import com.ra.projectmd05.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/reportPost")
public class RepostPost {

    private final ReportService reportService;


    @PostMapping()
    public ResponseEntity<?> reactionPost(@RequestBody ReportRequestDTO reportRequestDTO) throws IOException {
        reportService.createReport(reportRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package com.ra.projectmd05.service.report;

import com.ra.projectmd05.model.dto.request.ReportRequestDTO;
import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.model.entity.Report;
import com.ra.projectmd05.model.entity.ReportReason;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.repository.ReportReasonRepository;
import com.ra.projectmd05.repository.ReportRepository;
import com.ra.projectmd05.service.post.PostService;
import com.ra.projectmd05.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final PostService postServiceImpl;
    private final UserService userServiceImpl;
    private final ReportReasonRepository reportReasonRepository;
    @Override
    public Report createReport(ReportRequestDTO reportRequestDTO) {

        User user = userServiceImpl.getCurrentUserInfo();
        Post post = postServiceImpl.getPostById(reportRequestDTO.getPostId());

        ReportReason reportReason = new ReportReason();
        reportReason.setReason(reportRequestDTO.getReason());

        if (user != null && post != null) {
            Report report = Report.builder().
                    user(user).
                    post(post).
                    reason(reportReason).
                    build();

            reportReasonRepository.save(reportReason);

            reportRepository.save(report);
            return report;
        }

        return null;
    }
}

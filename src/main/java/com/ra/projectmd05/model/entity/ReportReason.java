package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "report_reason")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReportReason extends BaseObject
{
    private String reason;
}

package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Room extends BaseObject
{
    @Column(name = "name")
    private String name;

    @Column(name = "avatar")
    private String avatar;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();
}

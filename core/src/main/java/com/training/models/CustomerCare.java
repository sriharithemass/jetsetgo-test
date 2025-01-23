package com.training.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "customer_care_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerCare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @NotBlank
    private String subject;
    @NotBlank
    private String message;
    @CreationTimestamp
    private LocalDate timeStamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

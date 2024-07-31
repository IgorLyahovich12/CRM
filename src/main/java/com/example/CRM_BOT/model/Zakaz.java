package com.example.CRM_BOT.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zakaz")
public class Zakaz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "zakaz_id", updatable = false, nullable = false)
    private UUID zakazId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "office_number")
    private int officeNumber;

    @Column(name = "utm_source")
    private String utmSource;

    @Column(name = "utm_medium")
    private String utmMedium;

    @Column(name = "utm_term")
    private String utmTerm;

    @Column(name = "utm_content")
    private String utmContent;

    @Column(name = "utm_campaign")
    private String utmCampaign;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;
}

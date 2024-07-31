package com.example.CRM_BOT.web;


import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record ZakazDTO (
        UUID zakaz_id,
        String user_name,
        String phone_number,
        String product_name,
        int product_id,
        double price,
        int quantity,
        int office_number,
        String utm_source,
        String utm_medium,
        String utm_term,
        String utm_content,
        String utm_campaign,
        LocalDateTime order_date_time
)
{
}
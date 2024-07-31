package com.example.CRM_BOT.dao;


import com.example.CRM_BOT.model.Zakaz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ZakazRepository extends JpaRepository<Zakaz, UUID> {
    List<Zakaz> findByOfficeNumber(int officeNumber);
    List<Zakaz> findByUtmSource(String utmSource);
    List<Zakaz> findByUtmMedium(String utmMedium);
    List<Zakaz> findByUtmTerm(String utmTerm);
    List<Zakaz> findByUtmContent(String utmContent);
    List<Zakaz> findByUtmCampaign(String utmCampaign);
    List<Zakaz> findByProductName(String productName);
    List<Zakaz> findByProductId(int productId);
    List<Zakaz> findByOrderDateTimeBetween(LocalDateTime start, LocalDateTime end);


}


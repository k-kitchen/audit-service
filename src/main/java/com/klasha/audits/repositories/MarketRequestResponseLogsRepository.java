package com.klasha.audits.repositories;

import com.klasha.audits.model.MarketRequestResponseLogs;
import com.klasha.audits.model.PaymentRequestResponseLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRequestResponseLogsRepository extends JpaRepository<MarketRequestResponseLogs, Long> {
}

package com.klasha.audits.repositories;

import com.klasha.audits.model.AuthRequestResponseLogs;
import com.klasha.audits.model.PaymentRequestResponseLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRequestResponseLogsRepository extends JpaRepository<AuthRequestResponseLogs, Long> {
}

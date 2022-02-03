package com.klasha.audits.repositories;

import com.klasha.audits.model.NotificationRequestResponseLogs;
import com.klasha.audits.model.PaymentRequestResponseLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRequestResponseLogsRepository extends JpaRepository<NotificationRequestResponseLogs, Long> {
}

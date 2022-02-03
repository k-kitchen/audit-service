package com.klasha.audits.repositories;

import com.klasha.audits.model.PaymentRequestResponseLogs;
import com.klasha.audits.model.WalletRequestResponseLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRequestResponseLogsRepository extends JpaRepository<WalletRequestResponseLogs, Long> {
}

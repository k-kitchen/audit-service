package com.klasha.audits.repositories;

import com.klasha.audits.model.ObjectHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<ObjectHistory, Long> {
}

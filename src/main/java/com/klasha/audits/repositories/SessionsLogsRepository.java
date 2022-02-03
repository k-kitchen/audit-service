package com.klasha.audits.repositories;

import com.klasha.audits.model.SessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionsLogsRepository extends JpaRepository<SessionLog, Long> {

}

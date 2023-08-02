package com.example.core.repository.serverLog;

import com.example.core.model.serverLog.ServerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerLogRepository extends JpaRepository<ServerLog, Long> {}

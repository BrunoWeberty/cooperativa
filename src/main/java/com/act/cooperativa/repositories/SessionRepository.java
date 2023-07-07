package com.act.cooperativa.repositories;

import com.act.cooperativa.model.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<SessionModel, UUID> {
}

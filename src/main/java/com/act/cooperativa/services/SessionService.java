package com.act.cooperativa.services;

import com.act.cooperativa.model.SessionModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionService {

    SessionModel save(SessionModel sessionModel);

    Optional<SessionModel> findById(UUID sessionId);

    List<SessionModel> findAll();
}

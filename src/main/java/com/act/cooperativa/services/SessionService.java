package com.act.cooperativa.services;

import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.SaveException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionService {

    SessionModel save(SessionModel sessionModel) throws SaveException;

    Optional<SessionModel> findById(UUID sessionId) throws GetException;

    List<SessionModel> findAll() throws GetException;
}

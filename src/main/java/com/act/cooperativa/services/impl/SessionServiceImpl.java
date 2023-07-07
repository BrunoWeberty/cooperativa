package com.act.cooperativa.services.impl;

import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.repositories.SessionRepository;
import com.act.cooperativa.services.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class SessionServiceImpl implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public SessionModel save(SessionModel sessionModel) {
        return sessionRepository.save(sessionModel);
    }

    @Override
    public Optional<SessionModel> findById(UUID sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Override
    public List<SessionModel> findAll() {
        return sessionRepository.findAll();
    }
}

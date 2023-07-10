package com.act.cooperativa.services.impl;

import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.repositories.SessionRepository;
import com.act.cooperativa.services.SessionService;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.SaveException;
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
    public SessionModel save(SessionModel sessionModel) throws SaveException {
        try {
            return sessionRepository.save(sessionModel);

        } catch (Exception e) {
            throw new SaveException("Error saving session.", e);
        }
    }

    @Override
    public Optional<SessionModel> findById(UUID sessionId) throws GetException {
        try {
            return sessionRepository.findById(sessionId);

        } catch (Exception e) {
            throw new GetException("Associated fetching error.", e);
        }
    }

    public List<SessionModel> findAll() throws GetException {
        try {
            return sessionRepository.findAll();
        } catch (Exception e) {
            throw new GetException("Error fetching sessions.", e);
        }
    }
}

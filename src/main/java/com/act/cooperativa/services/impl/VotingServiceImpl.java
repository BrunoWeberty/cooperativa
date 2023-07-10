package com.act.cooperativa.services.impl;

import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.model.VotingModel;
import com.act.cooperativa.repositories.SessionRepository;
import com.act.cooperativa.repositories.VotingRepository;
import com.act.cooperativa.services.VotingService;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.SaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class VotingServiceImpl implements VotingService {

    @Autowired
    VotingRepository votingRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Override
    @Transactional
    public VotingModel save(VotingModel votingModel, SessionModel sessionModel) throws SaveException {
        try {
            var votingModelSaved = votingRepository.save(votingModel);

            sessionModel.setVotingId(votingModelSaved.getVotingId());
            sessionRepository.save(sessionModel);

            return votingModelSaved;
        } catch (Exception e) {
            throw new SaveException("Error saving voting.", e);
        }
    }

    @Override
    public Optional<VotingModel> findById(UUID votingId) throws GetException {
        try {
            return votingRepository.findById(votingId);
        } catch (Exception e) {
            throw new GetException("Voting fetching error.", e);
        }
    }
}

package com.act.cooperativa.services.impl;

import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.model.VotingModel;
import com.act.cooperativa.repositories.SessionRepository;
import com.act.cooperativa.repositories.VotingRepository;
import com.act.cooperativa.services.VotingService;
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
    public VotingModel save(VotingModel votingModel, SessionModel sessionModel) {
        var votingModelSaved = votingRepository.save(votingModel);

        sessionModel.setVotingId(votingModelSaved.getVotingId());
        sessionRepository.save(sessionModel);

        return votingModelSaved;
    }

    @Override
    public Optional<VotingModel> findById(UUID votingId) {
        return votingRepository.findById(votingId);
    }
}

package com.act.cooperativa.services;

import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.model.VotingModel;

import java.util.Optional;
import java.util.UUID;

public interface VotingService {

    VotingModel save(VotingModel votingModel, SessionModel sessionModel);

    Optional<VotingModel> findById(UUID votingId);
}

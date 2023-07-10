package com.act.cooperativa.services;

import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.model.VotingModel;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.SaveException;

import java.util.Optional;
import java.util.UUID;

public interface VotingService {

    VotingModel save(VotingModel votingModel, SessionModel sessionModel) throws SaveException;

    Optional<VotingModel> findById(UUID votingId) throws GetException;
}

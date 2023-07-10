package com.act.cooperativa.services;

import com.act.cooperativa.model.VoteModel;
import com.act.cooperativa.services.exception.SaveException;

public interface VoteService {

    VoteModel save(VoteModel voteModel) throws SaveException;
}

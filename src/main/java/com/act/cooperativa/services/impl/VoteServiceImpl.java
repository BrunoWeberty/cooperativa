package com.act.cooperativa.services.impl;

import com.act.cooperativa.model.VoteModel;
import com.act.cooperativa.repositories.VoteRepository;
import com.act.cooperativa.services.VoteService;
import com.act.cooperativa.services.exception.SaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Override
    public VoteModel save(VoteModel voteModel) throws SaveException {
        try {
            return voteRepository.save(voteModel);

        } catch (Exception e) {
            throw new SaveException("Error saving vote.", e);
        }
    }
}

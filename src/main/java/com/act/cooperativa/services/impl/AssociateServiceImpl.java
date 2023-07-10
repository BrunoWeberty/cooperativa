package com.act.cooperativa.services.impl;

import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.repositories.AssociateRepository;
import com.act.cooperativa.services.AssociateService;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.SaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssociateServiceImpl implements AssociateService {

    @Autowired
    AssociateRepository associateRepository;

    @Override
    public AssociateModel save(AssociateModel associateModel) throws SaveException {
        try {
            return associateRepository.save(associateModel);
        } catch (Exception e) {
            throw new SaveException("Error saving associated.", e);
        }

    }

    @Override
    public Optional<AssociateModel> findById(UUID associateId) throws GetException {
        try {
            return associateRepository.findById(associateId);
        } catch (Exception e) {
            throw new GetException("Associated fetching error.", e);
        }
    }

    @Override
    public List<AssociateModel> findAll() throws GetException {
        try {
            return associateRepository.findAll();
        } catch (Exception e) {
            throw new GetException("Error fetching associates.", e);
        }
    }
}

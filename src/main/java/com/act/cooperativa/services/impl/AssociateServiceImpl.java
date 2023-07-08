package com.act.cooperativa.services.impl;

import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.repositories.AssociateRepository;
import com.act.cooperativa.services.AssociateService;
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
    public AssociateModel save(AssociateModel associateModel) {
        return associateRepository.save(associateModel);
    }

    @Override
    public Optional<AssociateModel> findById(UUID associateId) {
        return associateRepository.findById(associateId);
    }

    @Override
    public List<AssociateModel> findAll() {
        return associateRepository.findAll();
    }


}

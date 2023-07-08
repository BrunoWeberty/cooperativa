package com.act.cooperativa.services;

import com.act.cooperativa.model.AssociateModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssociateService {
    AssociateModel save(AssociateModel associateModel);

    Optional<AssociateModel> findById(UUID associateId);

    List<AssociateModel> findAll();
}

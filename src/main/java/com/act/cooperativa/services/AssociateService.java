package com.act.cooperativa.services;

import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssociateService {
    AssociateModel save(AssociateModel associateModel) throws ServiceException;

    Optional<AssociateModel> findById(UUID associateId) throws GetException;

    List<AssociateModel> findAll() throws GetException;
}

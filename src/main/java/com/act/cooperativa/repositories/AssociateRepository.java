package com.act.cooperativa.repositories;

import com.act.cooperativa.model.AssociateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssociateRepository extends JpaRepository<AssociateModel, UUID> {
}

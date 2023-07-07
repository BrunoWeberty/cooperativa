package com.act.cooperativa.repositories;

import com.act.cooperativa.model.VotingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VotingRepository extends JpaRepository<VotingModel, UUID> {
}

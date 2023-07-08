package com.act.cooperativa.repositories;

import com.act.cooperativa.model.VoteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<VoteModel, UUID> {
}

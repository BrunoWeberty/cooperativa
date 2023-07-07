package com.act.cooperativa.controllers;

import com.act.cooperativa.dtos.VotingDto;
import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.model.VotingModel;
import com.act.cooperativa.services.SessionService;
import com.act.cooperativa.services.VotingService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class VotingController {

    @Autowired
    VotingService votingService;

    @Autowired
    SessionService sessionService;

    @PostMapping("/sessions/{sessionId}/voting")
    public ResponseEntity<Object> saveVoting(@PathVariable(value = "sessionId") UUID sessionId,
                                             @RequestBody @Valid VotingDto votingDto) {
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        if (sessionModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found");
        }
        var votingModel = new VotingModel();
        BeanUtils.copyProperties(votingDto, votingModel);
        votingModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        if (votingDto.getDuration() != null) {
            votingModel.setInitPeriod(votingModel.getCreationDate().plusMinutes(votingDto.getDuration()));
        } else {
            votingModel.setInitPeriod((votingModel.getCreationDate().plusMinutes(1)));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(votingService.save(votingModel, sessionModelOptional.get()));
    }

    @GetMapping("/sessions/voting/{sessionId}")
    public ResponseEntity<Object> getOneVoting(@PathVariable(value = "sessionId") UUID sessionId) {
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        if(sessionModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found");
        }

        Optional<VotingModel> votingModelOptional = votingService.findById(sessionModelOptional.get().getVotingId());
        return votingModelOptional.<ResponseEntity<Object>>map(
                votingModel -> ResponseEntity.status(HttpStatus.OK).body(votingModel)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("This Session Don't Has Voting"));
    }
}

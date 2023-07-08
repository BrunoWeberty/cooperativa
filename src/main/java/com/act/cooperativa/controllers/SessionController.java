package com.act.cooperativa.controllers;

import com.act.cooperativa.dtos.SessionDto;
import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.services.AssociateService;
import com.act.cooperativa.services.SessionService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SessionController {

    @Autowired
    SessionService sessionService;

    @Autowired
    AssociateService associateService;

    @PostMapping("/{sessionId}/associate/{associateId}")
    public ResponseEntity<Object> saveAssociateIntoSession(@PathVariable(value = "sessionId") UUID sessionId,
                                                           @PathVariable(value = "associateId") UUID associateId) {
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        if (sessionModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found");
        }

        var sessionModel = sessionModelOptional.get();

        Optional<AssociateModel> associateModelOptional = associateService.findById(associateId);
        if (associateModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associate Not Found");
        }

        var associates = sessionModelOptional.get().getAssociates();
        var associate = associates.stream().filter(a -> a.getAssociateId().equals(associateId)).findAny();

        if (associate.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This member has already registered for this session.");
        }

        associates.add(associateModelOptional.get());
        sessionModel.setAssociates(associates);
        sessionService.save(sessionModel);

        log.debug("Post saveAssociateIntoSession sessionModel updated {}", sessionModelOptional.get().toString());
        log.info("Session updated successfully {}", sessionModelOptional.get().getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionModelOptional.get());

    }

    @PostMapping
    public ResponseEntity<Object> saveSession(@RequestBody @Valid SessionDto sessionDto) {
        log.debug("POST saveSession sessionDto received {}", sessionDto.toString());
        var sessionModel = new SessionModel();
        BeanUtils.copyProperties(sessionDto, sessionModel);
        sessionModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        sessionService.save(sessionModel);
        log.debug("Post saveSession sessionModel saved {}", sessionModel.toString());
        log.info("Session saved successfully {}", sessionModel.getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionModel);
    }

    @GetMapping
    public ResponseEntity<Object> getAllSessions() {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.findAll());
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<Object> getOneSession(@PathVariable(value = "sessionId") UUID sessionId) {
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        return sessionModelOptional.<ResponseEntity<Object>>map(
                sessionModel -> ResponseEntity.status(HttpStatus.OK).body(sessionModel)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found"));
    }


}

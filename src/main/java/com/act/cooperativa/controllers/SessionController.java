package com.act.cooperativa.controllers;

import com.act.cooperativa.dtos.SessionDto;
import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.services.AssociateService;
import com.act.cooperativa.services.SessionService;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.SaveException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Session Controller")
public class SessionController {

    @Autowired
    SessionService sessionService;

    @Autowired
    AssociateService associateService;

    @PostMapping(value = "/{sessionId}/associate/{associateId}")
    @Operation(summary = "Cadastra um associado dentro da pauta que será votada", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associada cadastrado na pauta"),
            @ApiResponse(responseCode = "404", description = "Associado informado não encontrado"),
            @ApiResponse(responseCode = "409", description = "Associada já registrado para essa pauta")
    })
    public ResponseEntity<Object> saveAssociateIntoSession(@PathVariable(value = "sessionId") UUID sessionId,
                                                           @PathVariable(value = "associateId") UUID associateId) throws GetException, SaveException {
        log.debug("POST saveAssociateIntoSession sessionId received {} and associateId {}", sessionId, associateId);
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
        if(Objects.nonNull(associates)) {
            var associate = associates.stream().filter(a -> a.getAssociateId().equals(associateId)).findAny();

            if (associate.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This member has already registered for this session.");
            }
        } else {
            associates = new ArrayList<>();
        }

        associates.add(associateModelOptional.get());
        sessionModel.setAssociates(associates);
        sessionService.save(sessionModel);

        log.debug("Post saveAssociateIntoSession sessionModel updated {}", sessionModelOptional.get().toString());
        log.info("Session updated successfully {}", sessionModelOptional.get().getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionModelOptional.get());

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra uma pauta no banco de dados", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada")
    })
    public ResponseEntity<Object> saveSession(@RequestBody @Valid SessionDto sessionDto) throws SaveException {
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
    @Operation(summary = "Busca todas as pautas do banco de dados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta retornada")
    })
    public ResponseEntity<Object> getAllSessions() throws GetException {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.findAll());
    }

    @GetMapping("/{sessionId}")
    @Operation(summary = "Busca uma pauta do banco de dados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta retornada"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    })
    public ResponseEntity<Object> getOneSession(@PathVariable(value = "sessionId") UUID sessionId) throws GetException {
        Optional<SessionModel> sessionModelOptional = sessionService.findById(sessionId);
        return sessionModelOptional.<ResponseEntity<Object>>map(
                sessionModel -> ResponseEntity.status(HttpStatus.OK).body(sessionModel)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not Found"));
    }
}

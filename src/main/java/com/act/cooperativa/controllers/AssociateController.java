package com.act.cooperativa.controllers;

import com.act.cooperativa.dtos.AssociateDto;
import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.services.AssociateService;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.ServiceException;
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
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/associates")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Associate Controller")
public class AssociateController {

    @Autowired
    AssociateService associateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Realiza o registro de associados no banco de dados", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associado criado")
    })
    public ResponseEntity<Object> saveAssociate(@RequestBody @Valid AssociateDto associateDto) throws ServiceException {
        log.debug("POST saveAssociate associateDto received {}", associateDto.toString());
        var associateModel = new AssociateModel();
        BeanUtils.copyProperties(associateDto, associateModel);
        associateModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        associateService.save(associateModel);
        log.debug("Post saveAssociate associateModel saved {}", associateModel.toString());
        log.info("Associate saved successfully {}", associateModel.getAssociateId());
        return ResponseEntity.status(HttpStatus.CREATED).body(associateModel);
    }

    @GetMapping
    @Operation(summary = "Busca todos os associados do banco de dados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornado todos os associados")
    })
    public ResponseEntity<Object> getAllAssociates() throws GetException {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.findAll());
    }

    @GetMapping("/{associateId}")
    @Operation(summary = "Busca um associado no banco de dados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornado associado informado"),
            @ApiResponse(responseCode = "200", description = "Associado informado não encontrado")
    })
    public ResponseEntity<Object> getOneAssociate(@PathVariable(value = "associateId") UUID associateId) throws GetException {
        Optional<AssociateModel> associateModelOptional = associateService.findById(associateId);
        return associateModelOptional.<ResponseEntity<Object>>map(
                associateModel -> ResponseEntity.status(HttpStatus.OK).body(associateModel)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associate Not Found"));
    }
}

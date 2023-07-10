package com.act.cooperativa.controllers;

import com.act.cooperativa.dtos.AssociateDto;
import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.services.AssociateService;
import com.act.cooperativa.services.exception.GetException;
import com.act.cooperativa.services.exception.ServiceException;
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
@RequestMapping("/associates")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AssociateController {

    @Autowired
    AssociateService associateService;

    @PostMapping
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
    public ResponseEntity<Object> getAllAssociates() throws GetException {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.findAll());
    }

    @GetMapping("/{associateId}")
    public ResponseEntity<Object> getOneAssociate(@PathVariable(value = "associateId") UUID associateId) throws GetException {
        Optional<AssociateModel> associateModelOptional = associateService.findById(associateId);
        return associateModelOptional.<ResponseEntity<Object>>map(
                associateModel -> ResponseEntity.status(HttpStatus.OK).body(associateModel)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associate Not Found"));
    }
}

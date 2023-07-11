package com.act.cooperativa.controller;

import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.repositories.AssociateRepository;
import com.act.cooperativa.repositories.SessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    AssociateRepository associateRepository;

    @Test
    void getSessionWithSuccess() throws Exception {
        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));

        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(sessionModel));

        this.mockMvc.perform(get("/sessions/{sessionId}", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"sessionId\":  \"fd82c6f2-8032-4800-b2d4-3a8fc26c3721\",\"name\":  \"É a favor do porte de armas?\"}"));
    }

    @Test
    void getSessionsWithSuccess() throws Exception {
        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));

        Mockito.when(sessionRepository.findAll()).thenReturn(List.of(sessionModel));

        this.mockMvc.perform(get("/sessions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{ \"sessionId\":  \"fd82c6f2-8032-4800-b2d4-3a8fc26c3721\",\"name\":  \"É a favor do porte de armas?\"}]"));
    }

    @Test
    void saveSessionWithSuccess() throws Exception {
        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");

        Mockito.when(sessionRepository.save(Mockito.any())).thenReturn(sessionModel);

        this.mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":  \"É a favor do porte de armas?\"}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":  \"É a favor do porte de armas?\"}"));
    }

    @Test
    void saveAssociateIntoSessionWithSuccess() throws Exception {
        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));

        var associate = new AssociateModel();
        associate.setAssociateId(UUID.fromString("e2f5a3bf-eb40-4bc9-bd22-4184ef763e97"));
        associate.setCreationDate(LocalDateTime.of(2023, 2, 20, 12, 0));
        associate.setName("José");
        associate.setCpf("02069049086");
        ;

        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(sessionModel));
        Mockito.when(associateRepository.findById(Mockito.any())).thenReturn(Optional.of(associate));

        Mockito.when(sessionRepository.save(Mockito.any())).thenReturn(sessionModel);

        this.mockMvc.perform(post("/sessions/{sessionId}/associate/{associateId}",
                        UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"),
                        UUID.fromString("e2f5a3bf-eb40-4bc9-bd22-4184ef763e97")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"sessionId\":  \"fd82c6f2-8032-4800-b2d4-3a8fc26c3721\",\"name\":  \"É a favor do porte de armas?\"}"));
    }
}

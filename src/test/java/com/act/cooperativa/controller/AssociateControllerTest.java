package com.act.cooperativa.controller;

import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.repositories.AssociateRepository;
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
public class AssociateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AssociateRepository associateRepository;

    @Test
    void getAllAssociatesWithSuccess() throws Exception {
        var associate = new AssociateModel();
        associate.setCreationDate(LocalDateTime.of(2023, 02, 20, 12, 00));
        associate.setName("José");
        associate.setCpf("02069049086");
        Mockito.when(associateRepository.findAll()).thenReturn(List.of(associate));
        this.mockMvc.perform(get("/associates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":  \"José\", \"cpf\":  \"02069049086\", \"creationDate\": \"2023-02-20T12:00:00Z\"}]"));
    }

    @Test
    void getAssociateWithSuccess() throws Exception {
        var associate = new AssociateModel();
        associate.setAssociateId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));
        associate.setCreationDate(LocalDateTime.of(2023, 02, 20, 12, 00));
        associate.setName("José");
        associate.setCpf("02069049086");
        Mockito.when(associateRepository.findById(Mockito.any())).thenReturn(Optional.of(associate));
        this.mockMvc.perform(get("/associates/{associateId}", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"associateId\":  \"fd82c6f2-8032-4800-b2d4-3a8fc26c3721\",\"name\":  \"José\", \"cpf\":  \"02069049086\", \"creationDate\": \"2023-02-20T12:00:00Z\"}"));
    }

    @Test
    void saveAssociateWithSuccess() throws Exception {
        var associate = new AssociateModel();
        associate.setCreationDate(LocalDateTime.of(2023, 02, 20, 12, 00));
        associate.setName("José");
        associate.setCpf("02069049086");
        Mockito.when(associateRepository.save(Mockito.any())).thenReturn(associate);
        this.mockMvc.perform(post("/associates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":  \"José\", \"cpf\":  \"02069049086\"}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":  \"José\", \"cpf\":  \"02069049086\"}"));
    }
}

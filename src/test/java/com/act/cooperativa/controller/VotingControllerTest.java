package com.act.cooperativa.controller;

import com.act.cooperativa.enuns.Vote;
import com.act.cooperativa.model.AssociateModel;
import com.act.cooperativa.model.SessionModel;
import com.act.cooperativa.model.VoteModel;
import com.act.cooperativa.model.VotingModel;
import com.act.cooperativa.repositories.SessionRepository;
import com.act.cooperativa.repositories.VoteRepository;
import com.act.cooperativa.repositories.VotingRepository;
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
public class VotingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    VotingRepository votingRepository;

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    VoteRepository voteRepository;

    @Test
    void getVotingWithSuccess() throws Exception {
        var voting = new VotingModel();
        voting.setVotingId(UUID.fromString("e2f5a3bf-eb40-4bc9-bd22-4184ef763e97"));
        voting.setInitPeriod(LocalDateTime.of(2023, 2, 20, 12, 0));
        voting.setEndPeriod(LocalDateTime.of(2023, 2, 20, 12, 30));
        voting.setCreationDate(LocalDateTime.of(2023, 2, 20, 12, 0));

        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));

        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(sessionModel));
        Mockito.when(votingRepository.findById(Mockito.any())).thenReturn(Optional.of(voting));
        this.mockMvc.perform(get("/sessions/voting/{sessionId}", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"votingId\":  \"e2f5a3bf-eb40-4bc9-bd22-4184ef763e97\"," +
                        "\"initPeriod\":  \"2023-02-20T12:00:00Z\", \"endPeriod\":  \"2023-02-20T12:30:00Z\", \"creationDate\": \"2023-02-20T12:00:00Z\"}"));
    }

    @Test
    void getVotingWithFailGetSession() throws Exception {

        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/sessions/voting/{sessionId}", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getVotingWithFailGetVoting() throws Exception {

        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(sessionModel));

        Mockito.when(votingRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/sessions/voting/{sessionId}", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void saveVotingWithSuccess() throws Exception {
        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));

        var voting = new VotingModel();
        voting.setVotingId(UUID.fromString("e2f5a3bf-eb40-4bc9-bd22-4184ef763e97"));
        voting.setInitPeriod(LocalDateTime.of(2023, 2, 20, 12, 0));
        voting.setEndPeriod(LocalDateTime.of(2023, 2, 20, 12, 30));
        voting.setCreationDate(LocalDateTime.of(2023, 2, 20, 12, 0));

        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(sessionModel));
        Mockito.when(votingRepository.save(Mockito.any())).thenReturn(voting);

        this.mockMvc.perform(post("/sessions/{sessionId}/voting", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCampoNumerico\":  30, \"idCampoData\":  \"2023-02-20T12:00:00Z\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{ \"votingId\":  \"e2f5a3bf-eb40-4bc9-bd22-4184ef763e97\"," +
                        "\"initPeriod\":  \"2023-02-20T12:00:00Z\", \"endPeriod\":  \"2023-02-20T12:30:00Z\"}"));
    }

    @Test
    void saveVoteWithSuccess() throws Exception {
        var associate = new AssociateModel();
        associate.setAssociateId(UUID.fromString("f7fbfb14-9b5e-4bca-b80a-d4ebd4cedb7f"));
        associate.setCreationDate(LocalDateTime.of(2023, 2, 20, 12, 0));
        associate.setName("José");
        associate.setCpf("02069049086");

        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));
        sessionModel.setAssociates(List.of(associate));

        var voting = new VotingModel();
        voting.setVotingId(UUID.fromString("e2f5a3bf-eb40-4bc9-bd22-4184ef763e97"));
        voting.setInitPeriod(LocalDateTime.now().minusMinutes(40));
        voting.setEndPeriod(LocalDateTime.now().plusMinutes(10));
        voting.setCreationDate(LocalDateTime.now());

        var vote = new VoteModel();
        vote.setAssociateId(UUID.fromString("f7fbfb14-9b5e-4bca-b80a-d4ebd4cedb7f"));
        vote.setVote(Vote.YES);
        vote.setCreationDate(LocalDateTime.now());

        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(sessionModel));
        Mockito.when(votingRepository.findById(Mockito.any())).thenReturn(Optional.of(voting));
        Mockito.when(voteRepository.save(Mockito.any())).thenReturn(vote);
        Mockito.when(votingRepository.save(Mockito.any())).thenReturn(voting);
        Mockito.when(sessionRepository.save(Mockito.any())).thenReturn(sessionModel);

        this.mockMvc.perform(post("/sessions/{sessionId}/vote/{associateId}", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"), UUID.fromString("f7fbfb14-9b5e-4bca-b80a-d4ebd4cedb7f"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vote\":  \"YES\"}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getCountVotesInVotingOfTheSessionWithSuccess() throws Exception {
        var associate = new AssociateModel();
        associate.setAssociateId(UUID.fromString("f7fbfb14-9b5e-4bca-b80a-d4ebd4cedb7f"));
        associate.setCreationDate(LocalDateTime.of(2023, 2, 20, 12, 0));
        associate.setName("José");
        associate.setCpf("02069049086");

        var sessionModel = new SessionModel();
        sessionModel.setName("É a favor do porte de armas?");
        sessionModel.setSessionId(UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721"));
        sessionModel.setAssociates(List.of(associate));

        var voting = new VotingModel();
        voting.setVotingId(UUID.fromString("e2f5a3bf-eb40-4bc9-bd22-4184ef763e97"));
        voting.setInitPeriod(LocalDateTime.now().minusMinutes(40));
        voting.setEndPeriod(LocalDateTime.now().plusMinutes(10));
        voting.setCreationDate(LocalDateTime.now());

        var vote = new VoteModel();
        vote.setAssociateId(UUID.fromString("f7fbfb14-9b5e-4bca-b80a-d4ebd4cedb7f"));
        vote.setVote(Vote.YES);
        vote.setCreationDate(LocalDateTime.now());

        voting.setVotes(List.of(vote));

        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(sessionModel));
        Mockito.when(votingRepository.findById(Mockito.any())).thenReturn(Optional.of(voting));
        Mockito.when(voteRepository.save(Mockito.any())).thenReturn(vote);
        Mockito.when(votingRepository.save(Mockito.any())).thenReturn(voting);
        Mockito.when(sessionRepository.save(Mockito.any())).thenReturn(sessionModel);

        this.mockMvc.perform(get("/sessions/{sessionId}/voting/countVotes", UUID.fromString("fd82c6f2-8032-4800-b2d4-3a8fc26c3721")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"yes\":  1, \"no\": 0}"));
    }
}

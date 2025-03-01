package com.example.crash.service;

import com.example.crash.exception.sessionspeaker.SessionSpeakerNotFoundException;
import com.example.crash.model.entity.SessionSpeakerEntity;
import com.example.crash.model.sessionspeaker.SessionSpeaker;
import com.example.crash.repository.SessionSpeakerEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionSpeakerService {

    @Autowired
    private SessionSpeakerEntityRepository sessionSpeakerEntityRepository;

    //복수목록조회
    public List<SessionSpeaker> getSessionSpeakers() {
        var sessionSpeakerEntities = sessionSpeakerEntityRepository.findAll();
        return sessionSpeakerEntities.stream().map(SessionSpeaker::from).toList();
    }

    // 자주쓰이는 exception 이기 때문에 이렇게 구현
    public SessionSpeakerEntity getSessionSpeakerEntityBySpeakerId(Long speakerId) {
        return sessionSpeakerEntityRepository
                .findById(speakerId)
                .orElseThrow(() -> new SessionSpeakerNotFoundException(speakerId));
    }

    //단건목록조회
    public SessionSpeaker getSessionSpeakerBySpeakerId(Long speakerId) {
        var sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);
        return SessionSpeaker.from(sessionSpeakerEntity);
    }
}

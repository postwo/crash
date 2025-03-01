package com.example.crash.service;

import com.example.crash.exception.sessionspeaker.SessionSpeakerNotFoundException;
import com.example.crash.model.entity.SessionSpeakerEntity;
import com.example.crash.model.sessionspeaker.SessionSpeaker;
import com.example.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.example.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.example.crash.repository.SessionSpeakerEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

    //같은 회사의 같은 이름을 가지는 동명이인이 있을수 도있어서 중복체크는 안한다
    public SessionSpeaker createSessionSpeaker(
            SessionSpeakerPostRequestBody sessionSpeakerPostRequestBody) {
        var sessionSpeakerEntity =
                SessionSpeakerEntity.of(
                        sessionSpeakerPostRequestBody.company(),
                        sessionSpeakerPostRequestBody.name(),
                        sessionSpeakerPostRequestBody.description());

        return SessionSpeaker.from(sessionSpeakerEntityRepository.save(sessionSpeakerEntity));
    }

    //수정
    public SessionSpeaker updateSessionSpeaker(
            Long speakerId, SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody) {
        var sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);

        //ObjectUtils.isEmpty = 객체가 비어 있는지 확인하는 데 사용됩니다. 여기서 "비어 있다"는 것은 null이거나 빈 문자열인 경우를 말한다
        if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.company())) {
            sessionSpeakerEntity.setCompany(sessionSpeakerPatchRequestBody.company());
        }

        if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.name())) {
            sessionSpeakerEntity.setName(sessionSpeakerPatchRequestBody.name());
        }

        if (!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.description())) {
            sessionSpeakerEntity.setDescription(sessionSpeakerPatchRequestBody.description());
        }

        return SessionSpeaker.from(sessionSpeakerEntityRepository.save(sessionSpeakerEntity));
    }

    //삭제
    public void deleteSessionSpeaker(Long speakerId) {
        var sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);
        sessionSpeakerEntityRepository.delete(sessionSpeakerEntity);
    }
}

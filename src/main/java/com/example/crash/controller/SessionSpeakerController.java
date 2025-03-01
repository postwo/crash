package com.example.crash.controller;

import com.example.crash.model.sessionspeaker.SessionSpeaker;
import com.example.crash.service.SessionSpeakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session-speakers")
public class SessionSpeakerController {

    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    //복수목록조회
    @GetMapping
    public ResponseEntity<List<SessionSpeaker>> getSessionSpeakers() {
        var sessionSpeakers = sessionSpeakerService.getSessionSpeakers();
        return ResponseEntity.ok(sessionSpeakers);
    }

    //단건조회
    @GetMapping("/{speakerId}")
    public ResponseEntity<SessionSpeaker> getSessionSpeakerBySpeakerId(@PathVariable Long speakerId) {
        var sessionSpeaker = sessionSpeakerService.getSessionSpeakerBySpeakerId(speakerId);
        return ResponseEntity.ok(sessionSpeaker);
    }
}

package com.example.crash.model.sessionspeaker;

//compan 하나만 수정한다든가 name을 하나만 수정한다든가등 이렇게 부분수정을 허용할거기 때문에 NotEmpty를 사용하지 않는다
public record SessionSpeakerPatchRequestBody(String company, String name, String description) {}
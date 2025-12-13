package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.service.AudioStreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audio")
public class AudioStreamingController {

    private final AudioStreamingService audioStreamingService;

    @Autowired
    public AudioStreamingController(AudioStreamingService audioStreamingService) {
        this.audioStreamingService = audioStreamingService;
    }

    @GetMapping("/{songId}")
    public ResponseEntity<Resource> streamAudio(@PathVariable Integer songId) {
        Resource audioResource = audioStreamingService.loadAudioFile(songId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(audioResource);
    }
}
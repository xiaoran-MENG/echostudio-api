package com.echostudio.controller;

import com.echostudio.dto.CreateSongRequest;
import com.echostudio.dto.SongsResponse;
import com.echostudio.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @GetMapping
    public ResponseEntity<?> all() {
        try {
            return ResponseEntity.ok(this.songService.all());
        } catch (Exception e) {
            return ResponseEntity.ok(new SongsResponse(false, null));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestPart("request") String requestBody,
            @RequestPart("audio") MultipartFile audio,
            @RequestPart("image") MultipartFile image
    ) {
        try {
            CreateSongRequest request = new ObjectMapper().readValue(
                    requestBody,
                    CreateSongRequest.class);
            request.setAudio(audio);
            request.setImage(image);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(this.songService.create(request));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable String id) {
        try {
            return this.songService.remove(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

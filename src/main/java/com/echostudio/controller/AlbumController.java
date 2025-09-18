package com.echostudio.controller;

import com.echostudio.document.Album;
import com.echostudio.dto.AlbumsResponse;
import com.echostudio.dto.CreateAlbumRequest;
import com.echostudio.service.AlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestPart("request") String request, @RequestPart("image") MultipartFile image) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            var createRequest = mapper.readValue(request, CreateAlbumRequest.class);
            createRequest.setImage(image);
            Album album = this.service.create(createRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(album);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> all() {
        try {
            return ResponseEntity.ok(this.service.all());
        } catch (Exception e) {
            return ResponseEntity.ok(new AlbumsResponse(false, null));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable String id) {
        try {
            return this.service.remove(id)
                    ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                    : ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

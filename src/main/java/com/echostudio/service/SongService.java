package com.echostudio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.echostudio.document.Song;
import com.echostudio.dto.CreateSongRequest;
import com.echostudio.dto.SongsResponse;
import com.echostudio.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final Cloudinary cloudinary;

    public Song create(CreateSongRequest request) throws IOException {
        Map<String, Object> audioUploadResult = this.cloudinary.uploader().upload(
                request.getAudio().getBytes(),
                ObjectUtils.asMap("resource_type", "video"));
        Map<String, Object> imageUploadResult = this.cloudinary.uploader().upload(
                request.getImage().getBytes(),
                ObjectUtils.asMap("resource_type", "image"));
        String duration = this.formatDuration((Double) audioUploadResult.get("duration"));
        Song song = Song.builder()
                .name(request.getName())
                .description(request.getDescription())
                .album(request.getAlbum())
                .imageUrl(imageUploadResult.get("secure_url").toString())
                .audioUrl(audioUploadResult.get("secure_url").toString())
                .duration(duration)
                .build();
        return this.songRepository.save(song);
    }

    public SongsResponse all() {
        return new SongsResponse(true, this.songRepository.findAll());
    }

    public Boolean remove(String id) {
        Song song = this.songRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Song " + id + " is not found"));
        this.songRepository.delete(song);
        return true;
    }

    private String formatDuration(Double seconds) {
        return seconds == null ? "0:00" : String.format("%d:%02d", (int)(seconds / 60), (int)(seconds % 60));
    }
}

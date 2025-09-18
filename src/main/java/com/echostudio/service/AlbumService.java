package com.echostudio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.echostudio.document.Album;
import com.echostudio.dto.AlbumsResponse;
import com.echostudio.dto.CreateAlbumRequest;
import com.echostudio.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository repo;
    private final Cloudinary cloudinary;

    public Album create(CreateAlbumRequest request) throws IOException {
        Map<String, Object> upload = this.cloudinary.uploader().upload(
                request.getImage().getBytes(),
                ObjectUtils.asMap("resource_type", "image"));
        return this.repo.save(
                Album.fromCreateRequest(request, upload.get("secure_url").toString()));
    }

    public AlbumsResponse all() {
        return new AlbumsResponse(true, this.repo.findAll());
    }

    public Boolean remove(String id) {
        Album album = this.repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Album " + id + " is not found"));
        this.repo.delete(album);
        return true;
    }
}

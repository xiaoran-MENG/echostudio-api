package com.echostudio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAlbumRequest {
    private String id, name, description, bgColor;
    private MultipartFile image;
}

package com.echostudio.document;

import com.echostudio.dto.CreateAlbumRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "albums")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Album {
    @Id
    @JsonProperty("_id")
    private String id;
    private String name, description, bgColor, imageUrl;

    public static Album fromCreateRequest(CreateAlbumRequest request, String imageUrl) {
        return Album.builder()
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
                .imageUrl(imageUrl)
                .build();
    }
}

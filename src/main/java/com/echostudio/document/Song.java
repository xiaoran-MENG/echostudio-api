package com.echostudio.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "songs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Song {
    @Id
    @JsonProperty("_id")
    private String id;
    private String name, description, album, imageUrl, fileUrl, duration;
}

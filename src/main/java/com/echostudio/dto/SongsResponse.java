package com.echostudio.dto;

import com.echostudio.document.Song;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongsResponse {
    private Boolean success;
    private List<Song> songs;
}

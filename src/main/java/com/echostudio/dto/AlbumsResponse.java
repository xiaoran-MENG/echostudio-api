package com.echostudio.dto;

import com.echostudio.document.Album;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumsResponse {
    private boolean success;
    private List<Album> albums;
}

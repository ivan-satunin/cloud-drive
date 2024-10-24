package com.me.clouddrive.dto.view;

import com.me.clouddrive.dto.Size;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record FileView(
        String name,
        Size size,
        ZonedDateTime lastModified,
        String location
) implements StorageObject {
}

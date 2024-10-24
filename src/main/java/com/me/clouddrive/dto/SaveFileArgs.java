package com.me.clouddrive.dto;

import lombok.Builder;

@Builder
public record SaveFileArgs(
        byte[] fileData,
        long size,
        long partSize,
        String contentType
) {
}

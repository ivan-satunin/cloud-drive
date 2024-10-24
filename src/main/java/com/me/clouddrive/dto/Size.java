package com.me.clouddrive.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class Size {
    private static final BigDecimal KB = BigDecimal.valueOf(1024);
    private static final BigDecimal MB = BigDecimal.valueOf(1024 * 1024);
    private static final BigDecimal GB = BigDecimal.valueOf(1024 * 1024 * 1024);

    private final BigDecimal size;

    public Size(Long size) {
        this.size = new BigDecimal(size);
    }

    @Override
    public String toString() {
        if (size.compareTo(KB) < 0) {
            return size.setScale(2, RoundingMode.HALF_UP) + " B";
        } else if (size.compareTo(MB) < 0) {
            return size.divide(KB, 2, RoundingMode.HALF_UP) + " KiB";
        } else if (size.compareTo(GB) < 0) {
            return size.divide(MB, 2, RoundingMode.HALF_UP) + " MB";
        } else {
            return size.divide(GB, 2, RoundingMode.HALF_UP) + " GB";
        }
    }
}

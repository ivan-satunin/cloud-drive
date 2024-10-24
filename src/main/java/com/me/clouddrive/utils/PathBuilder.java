package com.me.clouddrive.utils;

import java.util.ArrayList;
import java.util.List;

public class PathBuilder {
    private final List<String> segments = new ArrayList<>();

    private void addSegment(String fragment) {
        if (!fragment.isEmpty())
            this.segments.add(fragment);
    }

    public PathBuilder userFolder(long userId) {
        addSegment("user-%d-files".formatted(userId));
        return this;
    }

    public PathBuilder segment(Iterable<String> segments) {
        segments.forEach(this.segments::add);
        return this;
    }

    public PathBuilder segment(String... segments) {
        for (var segment : segments) {
            addSegment(segment);
        }
        return this;
    }

    public String build() {
        return String.join("/", this.segments);
    }

    public String buildFolder() {
        this.segments.add("");
        return build();
    }
}

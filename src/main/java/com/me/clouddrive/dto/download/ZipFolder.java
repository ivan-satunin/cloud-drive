package com.me.clouddrive.dto.download;

import com.me.clouddrive.exception.file.DownloadFileException;
import com.me.clouddrive.exception.folder.DownloadFolderException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
public class ZipFolder {
    private final String originalName;
    private final List<DownloadFile> files;

    public String name() {
        return UriUtils.encode(originalName, StandardCharsets.UTF_8) + ".zip";
    }

    public void writeToZip(OutputStream out) {
        try (var zipOut = new ZipOutputStream(out)) {
            files.forEach(file -> {
                try {
                    zipOut.putNextEntry(new ZipEntry(file.name()));
                    StreamUtils.copy(file.fileData(), zipOut);
                } catch (IOException ioEx) {
                    throw new DownloadFileException(file.name(), ioEx);
                }
            });
        } catch (IOException ex) {
            throw new DownloadFolderException(this.originalName, ex);
        }
    }
}

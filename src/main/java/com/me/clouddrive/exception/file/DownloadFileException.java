package com.me.clouddrive.exception.file;

public class DownloadFileException extends FileException {

  public DownloadFileException(String fileName) {
    super("An error occurred while downloading a file " + fileName);
  }

  public DownloadFileException(String fileName, Throwable cause) {
    super("An error occurred while downloading a file " + fileName, cause);
  }
}

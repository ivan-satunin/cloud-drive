package com.me.clouddrive.service;

import com.me.clouddrive.dto.SaveFileArgs;
import com.me.clouddrive.exception.file.DeleteFileException;
import com.me.clouddrive.utils.PathBuilder;
import com.me.clouddrive.utils.PathUtils;
import io.minio.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class MinioUserObjectService {
    public static final String ROOT_FOLDER = "";

    private final MinioClient minioClient;
    
    @Value("${minio.user.bucket}")
    private String userFilesBucket;

    @PostConstruct
    @SneakyThrows
    public void createUserFilesBucket() {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(userFilesBucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(userFilesBucket).build());
        }
    }

    public Stream<Item> listObjects(boolean isRecursive, Function<PathBuilder, String> pathFunction) {
        var objectsArgs = ListObjectsArgs.builder()
                .bucket(userFilesBucket)
                .prefix(pathFunction.apply(new PathBuilder()))
                .recursive(isRecursive)
                .build();
        var objects = minioClient.listObjects(objectsArgs);
        List<Item> items = new ArrayList<>();
        objects.forEach(itemResult -> {
            var item = get(itemResult);
            items.add(item);
        });
        return items.stream();
    }

    public Stream<Item> listObjects(Function<PathBuilder, String> pathFunction) {
        return listObjects(false, pathFunction);
    }

    @SneakyThrows
    public void uploadObject(SaveFileArgs args, Function<PathBuilder, String> pathFunction) {
        uploadObject(args, pathFunction.apply(new PathBuilder()));
    }

    @SneakyThrows
    public void uploadObject(SaveFileArgs args, String objectName) {
        var putObjectArgs = PutObjectArgs.builder()
                .bucket(userFilesBucket)
                .object(objectName)
                .stream(new ByteArrayInputStream(args.fileData()), args.size(), args.partSize())
                .build();
        minioClient.putObject(putObjectArgs);
    }

    @SneakyThrows
    public void copyObject(Function<PathBuilder, String> fromFunction, Function<PathBuilder, String> toFunction) {
        var copyArgs = CopyObjectArgs.builder()
                .bucket(userFilesBucket)
                .object(toFunction.apply(new PathBuilder()))
                .source(CopySource.builder()
                        .bucket(userFilesBucket)
                        .object(fromFunction.apply(new PathBuilder()))
                        .build())
                .build();
        minioClient.copyObject(copyArgs);
    }

    @SneakyThrows
    public GetObjectResponse getObject(Function<PathBuilder, String> pathFunction) {
        var getObjectArgs = GetObjectArgs.builder()
                .bucket(userFilesBucket)
                .object(pathFunction.apply(new PathBuilder()))
                .build();
        return minioClient.getObject(getObjectArgs);
    }

    @SneakyThrows
    public GetObjectResponse getObject(String objectName) {
        return getObject(path -> path.segment(objectName).build());
    }

    @SneakyThrows
    public void deleteObject(Function<PathBuilder, String> pathFunction) {
        var removeArgs = RemoveObjectArgs.builder()
                .bucket(userFilesBucket)
                .object(pathFunction.apply(new PathBuilder()))
                .build();
        minioClient.removeObject(removeArgs);
    }

    public void deleteObjects(Iterable<DeleteObject> deleteObjects) {
        var removeObjects = RemoveObjectsArgs.builder()
                .bucket(userFilesBucket)
                .objects(deleteObjects)
                .build();
        var results = minioClient.removeObjects(removeObjects);
        results.forEach(result -> {
            var error = get(result);
            throw new DeleteFileException(PathUtils.cutItemName(error.objectName()));
        });
    }

    @SneakyThrows
    private <T> T get(Result<T> result) {
        return result.get();
    }
}

package com.me.clouddrive.utils;

import com.me.clouddrive.service.MinioUserObjectService;
import io.minio.messages.Item;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class PathUtils {

    public static String cutItemName(String objectPath) {
        var breadCrumbs = objectPath.split("/");
        if (breadCrumbs.length == 1)
            return objectPath;
        return breadCrumbs[breadCrumbs.length - 1];
    }

    public static String cutItemName(Item item) {
        return cutItemName(item.objectName());
    }

    public static String cutLocation(String objectPath) {
        var breadCrumbs = objectPath.split("/");
        return switch (breadCrumbs.length) {
            case 1, 2 -> MinioUserObjectService.ROOT_FOLDER;
            default -> {
                var relativeUserBreadCrumbs = Arrays.stream(breadCrumbs, 1, breadCrumbs.length - 1).toList();
                yield new PathBuilder().segment(relativeUserBreadCrumbs).build();
            }
        };
    }

    public static String cutLocation(Item item) {
        return cutLocation(item.objectName());
    }

}

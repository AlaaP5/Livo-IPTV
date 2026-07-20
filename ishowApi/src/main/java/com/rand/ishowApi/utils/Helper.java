package com.rand.ishowApi.utils;

import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Helper {

    public static  <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) setter.accept(value);
    }


}

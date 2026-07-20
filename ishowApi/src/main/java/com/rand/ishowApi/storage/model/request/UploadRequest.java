package com.rand.ishowApi.storage.model.request;

import com.rand.ishowApi.storage.model.contentType.StorageType;
import com.rand.ishowApi.storage.model.contentType.ContentTypeIds;
import com.rand.ishowApi.storage.model.contentType.FileCategory;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UploadRequest {
    private MultipartFile file;
    private StorageType contentType;
    ContentTypeIds contentTypeIds;
    FileCategory fileCategory;

}

package rand.ishowStream.stream.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import rand.ishowStream.exception.model.CustomException;
import rand.ishowStream.utils.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


@Service
public class StreamService {

    @Value("${storage.working-dir}")
    private String workingDir;

    public Resource servePoster(String dbPath, HttpHeaders headers) {
        try {
            Path filePath = FileUtils.resolveDbPath(dbPath, workingDir).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                return null;  // signal "not found" to caller
            }

            Resource resource = new FileSystemResource(file);
            String contentType = Files.probeContentType(filePath);

            headers.setContentType(
                    contentType != null
                            ? MediaType.parseMediaType(contentType)
                            : MediaType.APPLICATION_OCTET_STREAM
            );
            headers.setContentLength(file.length());

            return resource;

        } catch (Exception e) {
            throw new CustomException("Failed to read file", null);
        }
    }
}

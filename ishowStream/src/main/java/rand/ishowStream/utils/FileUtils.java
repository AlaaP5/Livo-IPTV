package rand.ishowStream.utils;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    @Value("${storage.working-dir}")
    private String workingDir;

    public static String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex != -1) ? filename.substring(dotIndex) : "";
    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    public static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    public static String toRelativePath(String path){
        return "/"+path.toString() .replace("\\", "/")          // Replace backslashes with forward slashes
                .replaceFirst("^\\.?/", "");
    }

    // To And From DB
    public static String toDbPath(Path absolutePath, String workingDir) {
        Path workingDirPath = Paths.get(workingDir).toAbsolutePath().normalize();
        Path targetPath = absolutePath.toAbsolutePath().normalize();

        String relative = workingDirPath.relativize(targetPath).toString();
        return "/" + relative.replace("\\", "/");
    }


    public static Path resolveDbPath(String dbPath, String workingDir) {
        if (dbPath == null || dbPath.isBlank()) return null;

        String cleanPath = dbPath.startsWith("/") ? dbPath.substring(1) : dbPath;
        return Paths.get(workingDir).resolve(cleanPath).toAbsolutePath();
    }
}

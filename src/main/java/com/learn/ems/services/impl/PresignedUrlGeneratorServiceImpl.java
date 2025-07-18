package com.learn.ems.services.impl;

import com.cloudinary.Cloudinary;
import com.learn.ems.services.PresignedUrlGeneratorService;
import com.learn.ems.utils.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class PresignedUrlGeneratorServiceImpl implements PresignedUrlGeneratorService {

    private final Cloudinary cloudinary;

    @Override
    public Map<String, String> generate(MultipartFile image) {
        try {
            final String fileName = ImageUtils.getFileName(ImageUtils.getFileName(image.getOriginalFilename()));
            final File uploadedFile = convertMultiPartToFile(image);
            Map<String, Object> uploadOptions = Map.of("public_id", "ems/banner/" + fileName);
            Map result = cloudinary.uploader().upload(uploadedFile, uploadOptions);
            deleteTempFile(uploadedFile);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed due to IO error: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during file upload: " + e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        String prefix = "upload_";
        String suffix = "." + FilenameUtils.getExtension(file.getOriginalFilename());
        File tempFile = Files.createTempFile(prefix, suffix).toFile();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;
    }

    private void deleteTempFile(File uploadedFile) {
        if (uploadedFile.delete()) {
            log.info("Temp file deleted successfully.");
        } else {
            log.warn("Failed to delete temp file: {}", uploadedFile.getAbsolutePath());
        }
    }
}

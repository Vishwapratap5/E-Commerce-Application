package com.guru.ecommerce.Service.FileService.Implementations;

import com.guru.ecommerce.Service.FileService.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {

        // 1. Ensure directory exists
        Path uploadPath = Paths.get(path);
        Files.createDirectories(uploadPath);   // safe even if exists

        // 2. Extract extension safely
        String originalFilename = image.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 3. Generate filename
        String filename = UUID.randomUUID().toString() + extension;

        // 4. Resolve final path
        Path filePath = uploadPath.resolve(filename);

        // 5. Copy file
        Files.copy(image.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }


}

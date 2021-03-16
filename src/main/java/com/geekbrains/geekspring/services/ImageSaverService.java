package com.geekbrains.geekspring.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageSaverService {
    private static final String UPLOADED_FOLDER = "src.main.resources.assets.images";

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "";
        }
        String fileName = file.getOriginalFilename();
        try {
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            file.transferTo(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}

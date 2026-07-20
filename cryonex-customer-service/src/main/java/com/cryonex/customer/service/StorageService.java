package com.cryonex.customer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private final String baseStoragePath = "uploaded-documents";

    public String saveFile(String customerId, MultipartFile file) {
        try {
            Path customerFolder = Paths.get(baseStoragePath, customerId).toAbsolutePath();
            Files.createDirectories(customerFolder);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = customerFolder.resolve(fileName);

            file.transferTo(filePath);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    public byte[] readFile(String filePath){

        try {
            return Files.readAllBytes(Paths.get(filePath));
        }catch (IOException e){
            throw new RuntimeException("Failed to read file: "+ e.getMessage());
        }
    }

    public void deleteFile(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }
}

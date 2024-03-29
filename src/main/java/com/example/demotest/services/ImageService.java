package com.example.demotest.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageService implements IStorageService{
    public final Path storageFolder = Paths.get("uploads");

    public ImageService () {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            throw new RuntimeException("can not initialize storage", e);
        }
    }
    /*
    *  Check extension of images
    *
    * */
    private boolean isImageFile( MultipartFile file) {
        String fileExtention = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg", "jpeg", "bmp"})
                .contains(fileExtention.trim().toLowerCase());
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("Hi");
            if(file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }

            if(!isImageFile(file)) {
                throw new RuntimeException("you can only upload image file");
            }

            float fileSizeMegabytes = file.getSize() / 1_000_000.0f;
            if (fileSizeMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5MB");
            }
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String geneatedFileName = UUID.randomUUID().toString().replace("-","");
            geneatedFileName = geneatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                    Paths.get(geneatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals((this.storageFolder.toAbsolutePath()))) {
                throw new RuntimeException(
                        "Can not store file outside current directory");

            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return geneatedFileName;
        }
        catch( IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load stored files", e);
        }

    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public void deleteAllFile() {

    }
}

package com.tobioxd.BE.services.impl;

import com.tobioxd.BE.entities.User;
import com.tobioxd.BE.payload.responses.UserResponse;
import com.tobioxd.BE.repositories.UserRepository;
import com.tobioxd.BE.services.base.IFileService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Override
    public UserResponse uploadFile(MultipartFile file, String token) throws Exception {
        String extractedToken = token.substring(7); // Clear "Bearer" from token
        User user = userServiceImpl.getUserDetailsFromToken(extractedToken);

        String oldPhotoUrl = !user.getPhotoUrl().equals("default.jpg") ? user.getPhotoUrl() : null;

        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new Exception("Invalid image format");
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Add UUID to front to make sure it is unique
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;

        // Convert MultipartFile to File
        File convertedFile = convertMultipartFileToFile(file);

        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFilename)
                .build(),
                RequestBody.fromFile(convertedFile));

        // Clean up the temporary file
        convertedFile.delete();

        String photoUrl = "https://tobioxd.s3.ap-southeast-1.amazonaws.com/" + uniqueFilename;

        if (oldPhotoUrl != null) {
            deleteFile(oldPhotoUrl.substring(oldPhotoUrl.lastIndexOf("/") + 1));
        }
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);

        return UserResponse.fromUser(user);
    }

    private void deleteFile(String file) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(file)
                .build());
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private File convertMultipartFileToFile(MultipartFile file) throws Exception {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

}

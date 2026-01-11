package com.example.edms.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Uploads an image file to Cloudinary.
     * @param file The image file received from the frontend.
     * @return The secure URL of the uploaded image.
     * @throws IOException If the upload fails.
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // Upload the file's bytes to Cloudinary and get the result map
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        // Extract the "secure_url" from the result map and return it as a String
        return (String) uploadResult.get("secure_url");
    }
}

package com.danko.danko_handmade.product.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadPhoto(MultipartFile file, String s) throws IOException {
        Map uploadResult = cloudinary
                .uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        return (String) uploadResult.get("secure_url");
    }

    public void deletePhoto(String mainPhotoUrl) {
        try {
            Map result = cloudinary.uploader().destroy(mainPhotoUrl, ObjectUtils.emptyMap());
            System.out.println("Cloudinary response: " + result);
        } catch (IOException e) {
            System.err.println("Error while deleting photo from Cloudinary: " + e.getMessage());
        }
    }
}

package com.danko.danko_handmade.product.service;
import com.danko.danko_handmade.config.CloudflareR2Config;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class CloudflareR2Service {

    private final S3Client s3Client;
    private final String bucketName;
    private final String endpoint;

    public CloudflareR2Service(CloudflareR2Config cloudflareR2Config) {
        this.endpoint = cloudflareR2Config.getEndpoint();
        this.bucketName = cloudflareR2Config.getBucketName();

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                cloudflareR2Config.getAccessKey(),
                cloudflareR2Config.getSecretKey()
        );

        this.s3Client = S3Client.builder()
                .region(Region.of("auto"))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String uploadFile(String filePath, String fileName) {
        Path path = Paths.get(filePath);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));

        if (response.sdkHttpResponse().isSuccessful()) {
            return String.format("%s/%s/%s", endpoint, bucketName, fileName);
        }

        return null;
    }

    public List<String> uploadMultipleFiles(List<String> filePaths, List<String> fileNames) {
        List<String> additionalPhotosUrls = new ArrayList<>();

        for (int i = 0; i < filePaths.size(); i++) {
            String filePath = filePaths.get(i);
            String fileName = fileNames.get(i);
            String fileUrl = uploadFile(filePath, fileName);

            if (fileUrl != null) {
                additionalPhotosUrls.add(fileUrl);
            }
        }

        return additionalPhotosUrls;
    }
}
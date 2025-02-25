package com.danko.danko_handmade.product.service;
import com.danko.danko_handmade.config.CloudflareR2Config;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;

@Service
public class CloudflareR2Service {

    private final CloudflareR2Config cloudflareR2Config;

    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;

    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;

    public CloudflareR2Service(CloudflareR2Config cloudflareR2Config) {
        this.cloudflareR2Config = cloudflareR2Config;

        String endpoint = cloudflareR2Config.getEndpoint();
        String accessKey = cloudflareR2Config.getAccessKey();
        String secretKey = cloudflareR2Config.getSecretKey();
        String bucketName = cloudflareR2Config.getBucketName();

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        s3Client = S3Client.builder()
                .region(Region.of("auto"))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(() -> credentials)
                .build();
    }

    public String uploadFile(String filePath, String fileName) {
        Path path = Paths.get(filePath);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, path);

        if (response != null) {
            return String.format("%s/%s/%s", endpoint, bucketName, fileName);
        }

        return null;
    }
}

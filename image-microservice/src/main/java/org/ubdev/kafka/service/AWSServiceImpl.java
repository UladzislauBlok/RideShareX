package org.ubdev.kafka.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.ubdev.kafka.exception.FileUploadException;
import org.ubdev.kafka.model.UploadImageMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AWSServiceImpl implements AWSService {
    @Value("${aws.access}")
    private String accessKey;
    @Value("${aws.secret}")
    private String secretKey;
    @Value("${aws.content.bucket.name}")
    private String bucketName;

    @Override
    public void saveImage(UploadImageMessage imageMessage) {
        byte[] imageBytes = imageMessage.imageBytes();
        try (ByteArrayInputStream imageInputStream = new ByteArrayInputStream(imageBytes)) {
            Regions region = Regions.EU_CENTRAL_1;
            AmazonS3 s3Client = createS3Client(region);
            ObjectMetadata metadata = createS3ObjectMetadata(imageBytes);
            String objectKey = imageMessage.id().toString();
            s3Client.putObject(new PutObjectRequest(bucketName, objectKey, imageInputStream, metadata));
        } catch (AmazonS3Exception e) {
            throw new FileUploadException(e.getMessage(), e.getStatusCode());
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private AmazonS3 createS3Client(Regions region) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private ObjectMetadata createS3ObjectMetadata(byte[] imageBytes) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        return metadata;
    }
}


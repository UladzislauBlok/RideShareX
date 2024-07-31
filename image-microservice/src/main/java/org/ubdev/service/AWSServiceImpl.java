package org.ubdev.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.ubdev.exception.FileDownloadException;
import org.ubdev.exception.FileUploadException;
import org.ubdev.kafka.model.UploadImageMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class AWSServiceImpl implements AWSService {
    private final AmazonS3 s3;
    private final String accessKey;
    private final String secretKey;
    private final String bucketName;

    @Autowired
    public AWSServiceImpl(@Value("${aws.access}") String accessKey,
                          @Value("${aws.secret}") String secretKey,
                          @Value("${aws.content.bucket.name}") String bucketName) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.s3 = createS3Client();
    }

    @Override
    public void saveImage(UploadImageMessage imageMessage) {
        byte[] imageBytes = imageMessage.imageBytes();
        try (ByteArrayInputStream imageInputStream = new ByteArrayInputStream(imageBytes)) {
            ObjectMetadata metadata = createS3ObjectMetadata(imageBytes);
            String objectKey = imageMessage.id().toString();
            s3.putObject(new PutObjectRequest(bucketName, objectKey, imageInputStream, metadata));
        } catch (AmazonS3Exception e) {
            throw new FileUploadException(e.getMessage(), e.getStatusCode());
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public byte[] getImage(UUID id) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, id.toString());
        S3Object object = s3.getObject(getObjectRequest);
        try (S3ObjectInputStream stream = object.getObjectContent()) {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new FileDownloadException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private AmazonS3 createS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private ObjectMetadata createS3ObjectMetadata(byte[] imageBytes) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        return metadata;
    }
}


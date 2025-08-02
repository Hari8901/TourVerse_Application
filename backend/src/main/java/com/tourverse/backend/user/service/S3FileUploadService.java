package com.tourverse.backend.user.service;

import com.tourverse.backend.common.config.AwsS3Config.S3Properties;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class S3FileUploadService {
	private final S3Client s3Client;
	private final S3Properties s3Properties;

	public S3FileUploadService(S3Client s3Client, S3Properties s3Properties) {
		this.s3Client = s3Client;
		this.s3Properties = s3Properties;
	}

	public String uploadFile(MultipartFile file) {

		String extension = getFileExtension(file.getOriginalFilename());
		String uniqueFilename = UUID.randomUUID() + extension;
		
		
		String bucketName = s3Properties.getBucketName();

		try {
			PutObjectRequest putRequest = PutObjectRequest
					.builder()
					.bucket(bucketName)
					.key(uniqueFilename)
					.contentType(file.getContentType())
					.acl(ObjectCannedACL.PUBLIC_READ)
					.build();

			s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

			System.out.println("File uploaded to S3: " + bucketName + "/" + uniqueFilename);

			return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, s3Properties.getRegion(), uniqueFilename);

		} catch (IOException e) {
			System.err.println("Failed to upload file to S3: " + e.getMessage());
			throw new RuntimeException("Failed to upload file to S3", e);
		}
	}

	private String getFileExtension(String filename) {
		// extract extension of file
		if (filename != null && filename.contains(".")) {
			return filename.substring(filename.lastIndexOf("."));
		}
		return "";
	}
}
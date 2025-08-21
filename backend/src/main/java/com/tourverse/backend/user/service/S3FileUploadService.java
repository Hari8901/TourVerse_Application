package com.tourverse.backend.user.service;

import com.tourverse.backend.common.config.AwsS3Config.S3Properties;
import com.tourverse.backend.common.exceptions.FileUploadException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileUploadService {
	private final S3Client s3Client;
	private final S3Properties s3Properties;

	@Value("${file.upload.max-size}")
	private long maxFileSize;

	private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
			"image/jpeg", "image/png", "application/pdf"
	);

	public String uploadFile(MultipartFile file) {
		validateFile(file);

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

			log.info("File uploaded to S3: {}/{}", bucketName, uniqueFilename);

			return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucketName, s3Properties.getRegion(), uniqueFilename);

		} catch (IOException e) {
			log.error("Failed to upload file to S3", e);
			throw new FileUploadException("Failed to upload file to S3", e);
		}
	}

	private void validateFile(MultipartFile file) {
		if (file.getSize() > maxFileSize) {
			throw new FileUploadException("File size exceeds the maximum limit of " + maxFileSize + " bytes.");
		}
		if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
			throw new FileUploadException("Invalid file type. Allowed types are: " + ALLOWED_FILE_TYPES);
		}
	}

	private String getFileExtension(String filename) {
		if (filename != null && filename.contains(".")) {
			return filename.substring(filename.lastIndexOf("."));
		}
		return "";
	}
}
package com.tourverse.backend.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties(AwsS3Config.S3Properties.class)
public class AwsS3Config {

	private final S3Properties s3Properties;

	public AwsS3Config(S3Properties s3Properties) {
		this.s3Properties = s3Properties;
	}
	
	@Bean
	S3Client s3Client() {
		return S3Client.builder().region(Region.of(s3Properties.getRegion()))
				.credentialsProvider(s3Properties.getAccessKey() != null && !s3Properties.getAccessKey().isBlank()
						? StaticCredentialsProvider.create(AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey()))
						: DefaultCredentialsProvider.create())
				.build();
	}

	@ConfigurationProperties(prefix = "cloud.aws.s3")
	public static class S3Properties {
		private String accessKey;
		private String secretKey;
		private String region;
		private String bucketName;

		public String getAccessKey() {
			return accessKey;
		}

		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}

		public String getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getBucketName() {
			return bucketName;
		}

		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}

	}
}

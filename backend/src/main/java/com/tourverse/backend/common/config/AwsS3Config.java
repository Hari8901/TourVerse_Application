
package com.tourverse.backend.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties(AwsS3Config.S3Properties.class)
@RequiredArgsConstructor
public class AwsS3Config {

	private final S3Properties s3Properties;

	@Bean
	S3Client s3Client() {
		return S3Client.builder().region(Region.of(s3Properties.getRegion()))
				.credentialsProvider(StaticCredentialsProvider
						.create(AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey())))
				.build();
	}

	@ConfigurationProperties(prefix = "cloud.aws.s3")
	@Validated
	@Data
	public static class S3Properties {
		@NotBlank(message = "Access key should not be blank")
		private String accessKey;
		@NotBlank(message = "Secret key should not be blank")
		private String secretKey;
		@NotBlank(message = "Region should not be blank")
		private String region;
		@NotBlank(message = "Bucket name should not be blank")
		private String bucketName;

	}
}

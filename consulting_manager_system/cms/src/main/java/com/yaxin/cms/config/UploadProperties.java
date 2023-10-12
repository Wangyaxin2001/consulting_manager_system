package com.yaxin.cms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "upload.oss")
public class UploadProperties {

	/**
	 * OSS Access key
	 */
	private String accessKey;

	/**
	 * OSS Secret key
	 */
	private String secretKey;

	/**
	 * bucketName
	 */
	private String bucket;

	/**
	 * url地址,用于拼接 文件上传成功后回显的url
	 */
	private String baseUrl;
}

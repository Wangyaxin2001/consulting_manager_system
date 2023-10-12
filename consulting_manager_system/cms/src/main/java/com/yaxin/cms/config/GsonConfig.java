package com.yaxin.cms.config;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author horry
 * @Description 创建Gson对象
 * @date 2023/8/25-15:22
 */
@Configuration
public class GsonConfig {

	@Bean
	@ConditionalOnMissingBean
	public Gson gson() {
		return new Gson();
	}
}

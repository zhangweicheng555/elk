package com.kuandeng.kelk.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置类:
 * 
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@SuppressWarnings("unchecked")
	@Bean
	public Docket testApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("kts")
				.genericModelSubstitutes(DeferredResult.class).useDefaultResponseMessages(false).forCodeGeneration(true)
				.pathMapping("/")// base，最终调用接口后会和paths拼接在一起
				.select()
//				.paths(Predicates.or(PathSelectors.regex("/task.*"), PathSelectors.regex("/service/.*"),
//						PathSelectors.regex("/activiti/.*")))// 过滤的接口
				.build().apiInfo(metaApiInfo());
		;
		return docket;
	}

	private ApiInfo metaApiInfo() {
		StringVendorExtension sve = new StringVendorExtension("KD", "宽凳（北京）科技有限公司");
		Collection<VendorExtension> list = new ArrayList<VendorExtension>();
		list.add(sve);
		ApiInfo apiInfo = new ApiInfo("任务相关接口", // 大标题
				"任务相关接口.", // 小标题
				"1.0", // 版本
				"http://kts.kuandeng.com", new Contact("leipeng", "", "leipeng@kuandeng.com"), "宽凳（北京）科技有限公司", // 链接显示文字
				"http://kts.kuandeng.com", // 网站链接
				list);

		return apiInfo;
	}

}
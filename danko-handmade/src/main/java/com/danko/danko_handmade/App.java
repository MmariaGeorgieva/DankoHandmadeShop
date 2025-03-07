package com.danko.danko_handmade;

import com.danko.danko_handmade.config.CloudflareR2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CloudflareR2Config.class)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}

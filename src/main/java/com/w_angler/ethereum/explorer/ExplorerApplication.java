package com.w_angler.ethereum.explorer;

import com.w_angler.ethereum.explorer.prop.EthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({EthProperties.class})
public class ExplorerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExplorerApplication.class, args);
	}
}

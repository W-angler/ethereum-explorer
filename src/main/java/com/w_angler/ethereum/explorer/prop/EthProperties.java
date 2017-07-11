package com.w_angler.ethereum.explorer.prop;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix="eth")
@Data
public class EthProperties {
	private List<String> peers=new ArrayList<>();
}

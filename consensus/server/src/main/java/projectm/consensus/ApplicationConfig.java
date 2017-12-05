package projectm.consensus;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class ApplicationConfig {

	@Bean
	public Docket apis() {
		ApiInfo apiInfo = new ApiInfoBuilder()//
				.title("Project M consensus APIs")//
				.description("Project consensus APIs")//
				.version("1.0")//
				.build();
		return new Docket(DocumentationType.SWAGGER_2)//
				.groupName("project-m-consensus")//
				.apiInfo(apiInfo)//
				.select()//
				.paths(PathSelectors.ant("/api/**"))//
				.build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneral(Exception exc) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@Value("${projectm.server.ip}")
	private String ip;
	@Value("${server.port}")
	private int port;
	@Value("${projectm.consensus.nodes}")
	private String nodes;

	/** Not include self. */
	public List<NodeAddress> cluster() {
		List<NodeAddress> list = new ArrayList<NodeAddress>();
		for (String node : nodes.split(",")) {
			String[] s = node.split(":");
			if (ip.equals(s[0]) && port == Integer.parseInt(s[1])) {
				continue;
			}
			NodeAddress addr = new NodeAddress(s[0], Integer.parseInt(s[1]));
			list.add(addr);
		}
		return list;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
}

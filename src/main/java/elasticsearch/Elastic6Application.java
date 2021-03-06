package elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication/*(exclude = ElasticsearchAutoConfiguration.class)*/
@EnableElasticsearchRepositories
public class Elastic6Application {

	public static void main(String[] args) {
		SpringApplication.run(Elastic6Application.class, args);
	}
}
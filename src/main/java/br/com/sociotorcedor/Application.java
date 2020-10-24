package br.com.sociotorcedor;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import br.com.sociotorcedor.infrastructure.client.stream.ChannelStreams;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableRabbit
@EnableBinding(ChannelStreams.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
 
		@Bean
		public ModelMapper modelMapper() {
			return new ModelMapper();
		}
}

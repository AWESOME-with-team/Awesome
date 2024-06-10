	package com.be.whereu;

	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


	@EnableJpaAuditing
	@SpringBootApplication
	public class WhereUApplication {

		public static void main(String[] args) {
			SpringApplication.run(WhereUApplication.class, args);
		}

	}

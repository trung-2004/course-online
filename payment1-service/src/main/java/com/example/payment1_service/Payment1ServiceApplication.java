package com.example.payment1_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class Payment1ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Payment1ServiceApplication.class, args);
	}



	@Bean
	NewTopic notification() {
		// topic name, partition numbers, replication number(= số lượng broker server)
		return new NewTopic("topicItemOnline", 2, (short) 1);
	}

	@Bean
	NewTopic notification1() {
		// topic name, partition numbers, replication number(= số lượng broker server)
		return new NewTopic("notification", 2, (short) 1);
	}

	@Bean
	NewTopic paymentStatus() {
		// topic name, partition numbers, replication number(= số lượng broker server)
		return new NewTopic("orderStatus1", 2, (short) 1);
	}

	@Bean
	NewTopic testUserStatus() {
		// topic name, partition numbers, replication number(= số lượng broker server)
		return new NewTopic("testUser", 2, (short) 1);
	}
}

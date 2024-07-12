package com.example.course_service;

import com.example.course_service.repository.CourseRepository;
import com.example.course_service.schedule.ThreadCourse;
import com.example.course_service.schedule.ThreadCourseRelated;
import com.example.course_service.service.impl.RedisCacheService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class CourseServiceApplication {

	@Autowired
	private RedisCacheService redisCacheService;

	@Autowired
	private CourseRepository courseRepository;

	public static void main(String[] args) {
		SpringApplication.run(CourseServiceApplication.class, args);
	}

//	@Bean
//	NewTopic notification() {
//		// topic name, partition numbers, replication number(= số lượng broker server)
//		return new NewTopic("notification", 2, (short) 1);
//	}

	@Bean
	JsonMessageConverter converter() {
		return new JsonMessageConverter();
	}

	@Bean
	public Integer init(){
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		// cache course
		for (int i = 0; i < 3; i++) {
			ThreadCourse threadCourse = new ThreadCourse(redisCacheService, courseRepository);
			executorService.execute(threadCourse);
		}

		for (int i = 0; i < 3; i++) {
			ThreadCourseRelated threadCourseRelated = new ThreadCourseRelated(redisCacheService, courseRepository);
			executorService.execute(threadCourseRelated);
		}
		return 1;
	}
}

package com.example.course_service.service.impl;

import com.example.course_service.dto.response.course.CourseDetail;
import com.example.course_service.entity.Course;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisCacheService {
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

//    public CourseDetail getValue(String key) {
//        return (CourseDetail)redisTemplate.opsForValue().get(key);
//    }

    public <T> T getValue(String key, Class<T> tClass) {
        try {
            Object object = redisTemplate.opsForValue().get(key);
            if (object == null) {
                return null;
            }
            return objectMapper.convertValue(object, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving object from Redis", e);
        }
    }

    public void setValue(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteValue(String key){
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimout(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key,timeout, timeUnit);
    }

    public void setValueWithTimeout(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key,timeout, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkExistsKey(String key){
        boolean check = false;
        try {
            check = redisTemplate.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }
    public void lPushAll(String key, List<String> value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }
    public void lPushAllCourse(String key, List<CourseDetail> value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public boolean checkEmptyList(String key) {
        return redisTemplate.opsForList().size(key)!=0;
    }

    public List<CourseDetail> getListCourse(String key) {
        try {
            Object obj = redisTemplate.opsForList().rightPop(key);
            if (obj instanceof List) {
                List<Object> list = (List<Object>) obj;
                List<CourseDetail> products = list.stream()
                        .map(item -> objectMapper.convertValue(item, CourseDetail.class))
                        .collect(Collectors.toList());
                return products;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<CourseDetail> getListCourseRelated(String key) {
        try {
            Object obj = redisTemplate.opsForList().rightPop(key);
            if (obj instanceof List) {
                List<Object> list = (List<Object>) obj;
                List<CourseDetail> products = list.stream()
                        .map(item -> objectMapper.convertValue(item, CourseDetail.class))
                        .collect(Collectors.toList());
                return products;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

package com.example.course_service.schedule;

import com.example.course_service.dto.response.course.CourseDetail;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.service.impl.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.course_service.schedule.CacheCourse.keyCourseDetails;
import static com.example.course_service.schedule.CacheCourse.keyQueueCourse;

@RequiredArgsConstructor
@Slf4j
public class ThreadCourse implements Runnable{
    private final RedisCacheService redisCacheService;
    private final CourseRepository courseRepository;

    @Override
    public void run() {
        try {
            while (true) {
                if (redisCacheService.checkExistsKey(keyQueueCourse)) {
                    log.info("Có course trong queue");
                    List<CourseDetail> list = redisCacheService.getListCourse(keyQueueCourse);
                        if (list != null && !list.isEmpty()){
                            for (CourseDetail courseDetail : list){
                                redisCacheService.setValue(keyCourseDetails+courseDetail.getId(), courseDetail);
                            }
                        }
                } else {
                    Thread.sleep(100);
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

//        try {
//            Thread.sleep(2000);
//            log.info("Start thread" + Thread.currentThread().getId());
//            while (redisCacheService.checkEmptyList(keyQueueCourse)){
//                List<CourseDetail> list = redisCacheService.getListCourse(keyQueueCourse);
//                if (list != null && !list.isEmpty()){
//                    for (CourseDetail courseDetail : list){
//                        redisCacheService.setValue(keyCourseDetails+courseDetail.getId(), courseDetail);
//                    }
//                }
//            }
//            log.info("End thread" + Thread.currentThread().getId());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

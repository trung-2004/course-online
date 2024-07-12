package com.example.course_service.schedule;

import com.example.course_service.dto.response.course.CourseDetail;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.service.impl.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.example.course_service.schedule.CacheCourse.keyCourserelated;
import static com.example.course_service.schedule.CacheCourse.keyQueueCourseRelated;

@RequiredArgsConstructor
@Slf4j
public class ThreadCourseRelated implements Runnable{
    private final RedisCacheService redisCacheService;
    private final CourseRepository courseRepository;

    @Override
    public void run() {
        try {
            while (true) {
                if (redisCacheService.checkExistsKey(keyQueueCourseRelated)) {
                    log.info("Có course trong queue related");
                    List<CourseDetail> list = redisCacheService.getListCourse(keyQueueCourseRelated);
                    if (list != null && !list.isEmpty()){
                        for (CourseDetail courseDetail : list){
                            List<String> strings = courseRepository.findTop4IdsByCategoryIdAndNotCurrentCourseId(courseDetail.getCategoryId(), courseDetail.getId(), PageRequest.of(0, 4));
                            if (strings != null && !strings.isEmpty()){
                                redisCacheService.setValue(keyCourserelated+courseDetail.getId(), strings.toString());
                            }
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

package com.example.test_service.service.impl;

import com.example.test_service.dto.ApiResponse;
import com.example.test_service.dto.request.*;
import com.example.test_service.dto.response.*;
import com.example.test_service.entity.*;
import com.example.test_service.exception.AppException;
import com.example.test_service.exception.ErrorCode;
import com.example.test_service.mapper.QuestionTestMapper;
import com.example.test_service.mapper.TestMapper;
import com.example.test_service.repository.*;
import com.example.test_service.repository.httpclient.CourseClient;
import com.example.test_service.repository.httpclient.PaymentClient;
import com.example.test_service.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TestUserRepository testUserRepository;
    private final QuestionTestRepository questionTestRepository;
    private final AnswerStudentRepository answerStudentRepository;
    private final QuestionTestUserRepository questionTestUserRepository;
    private final ExcelUploadService excelUploadService;
    private final AnswerRepository answerRepository;
    private final PaymentClient paymentClient;
    private final CourseClient courseClient;
    private final TestMapper testMapper;
    private final QuestionTestMapper questionTestMapper;


    @Override
    public TestDetail getdetailTest(String id, String userId) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOTFOUND));

        ApiResponse<Boolean> checkBuyCourse = paymentClient.checkBuyCourse(test.getCourseId());
        if (!checkBuyCourse.getResult()) throw new AppException(ErrorCode.NOTFOUND);
//        // check course_user

        TestUser testUser = testUserRepository.findByTestAndUserIdAndStatusAndStatusStudy(test, userId, true, true);
        if (testUser != null) throw new AppException(ErrorCode.NOTFOUND);

        // get 10 question
        List<QuestionTestDTO> questionTestDTOS = new ArrayList<QuestionTestDTO>();

        // get question easy
        List<QuestionTestDTO> questionTestDTOSEasy = questionTestRepository
                .findRandomQuestionsByTopicIdAndLevel(test.getTopicId(), 0, 5).stream().map(questionTestMapper::toQuestionTestDTO).collect(Collectors.toList());
        if (questionTestDTOSEasy.size() < 5) throw new AppException(ErrorCode.QUESTION_EASY_NOT_ENOUGH);

        // get question medium
        List<QuestionTestDTO> questionTestDTOSMedium = questionTestRepository
                .findRandomQuestionsByTopicIdAndLevel(test.getTopicId(), 1, 5).stream().map(questionTestMapper::toQuestionTestDTO).collect(Collectors.toList());
        if (questionTestDTOSMedium.size() < 5) throw new AppException(ErrorCode.QUESTION_MEDIUM_NOT_ENOUGH);

        // get question hard
        List<QuestionTestDTO> questionTestDTOSHard = questionTestRepository
                .findRandomQuestionsByTopicIdAndLevel(test.getTopicId(), 2, 5).stream().map(questionTestMapper::toQuestionTestDTO).collect(Collectors.toList());
        if (questionTestDTOSHard.size() < 5) throw new AppException(ErrorCode.QUESTION_HARD_NOT_ENOUGH);

        questionTestDTOS.addAll(questionTestDTOSEasy);
        questionTestDTOS.addAll(questionTestDTOSMedium);
        questionTestDTOS.addAll(questionTestDTOSHard);

        TestDetail testDetail = TestDetail.builder()
                .id(test.getId())
                .title(test.getTitle())
                .type(test.getType())
                .totalQuestion(test.getTotalQuestion())
                .time(test.getTime())
                .description(test.getDescription())
                .questionTestDTOS(questionTestDTOS)
                .createdDate(test.getCreatedDate())
                .modifiedBy(test.getModifiedBy())
                .createdBy(test.getCreatedBy())
                .modifiedDate(test.getModifiedDate())
                .build();

        return testDetail;
    }

    @Transactional
    @Override
    public String submitTest(String id, String userId, SubmitTest submitTest) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOTFOUND));

        if (submitTest.getCreateAnswerStudents().size() != test.getTotalQuestion()) throw new AppException(ErrorCode.NOTFOUND);

        TestUser testUserExisting = testUserRepository.findByTestAndUserIdAndStatusAndStatusStudy(test, userId, true, true);
        if (testUserExisting != null) throw new AppException(ErrorCode.NOTFOUND);

        // Kiểm tra người dùng đã hoàn thành bài kiểm tra hôm nay chưa
        LocalDate today = LocalDate.now();
        ZonedDateTime startOfDay = today.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);

        List<TestUser> attemptsToday = testUserRepository.findAttemptsByUserAndDate(
                test, userId,
                Timestamp.from(startOfDay.toInstant()),
                Timestamp.from(endOfDay.toInstant())
        );

        if (!attemptsToday.isEmpty()) {
            throw new AppException(ErrorCode.TEST_USER_HAS_ALREADY_ATTEMPTED_TODAY);
        }


        ApiResponse<Boolean> checkBuyCourse = paymentClient.checkBuyCourse(test.getCourseId());
        if (!checkBuyCourse.getResult()) throw new AppException(ErrorCode.NOTFOUND);
        // check course_user

        TestUser testUser = TestUser.builder()
                .userId(userId)
                .time(submitTest.getTime())
                .test(test)
                .statusStudy(true)
                .build();
        testUserRepository.save(testUser);

//        // Lấy danh sách câu hỏi từ các phiên test
        List<QuestionTest> questionTestList = new ArrayList<>();
        double totalScore = 0;
        double score;
        score = test.getTotalMark() / test.getTotalQuestion();

        for (CreateAnswerStudent createAnswerStudent : submitTest.getCreateAnswerStudents()) {
            QuestionTest questionTest = questionTestRepository.findById(createAnswerStudent.getQuestionId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOTFOUND));
            Answer answerStudent = answerRepository.findById(createAnswerStudent.getAnswerId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOTFOUND));

            Answer answerQuestioncorrect = answerRepository.findByQuestionTestAndStatus(questionTest, true);
            if (answerQuestioncorrect == null) throw new AppException(ErrorCode.NOTFOUND);

            AnswerStudent answerStudent1 = AnswerStudent.builder()
                    .answer(answerStudent)
                    .questionTest(questionTest)
                    .testUser(testUser)
                    .content(answerStudent.getContent())
                    .build();

            QuestionTestUser questionTestUser = QuestionTestUser.builder()
                    .questionTest(questionTest)
                    .testUser(testUser)
                    .build();

            if (answerStudent.equals(answerQuestioncorrect)){
                totalScore+=score;
                questionTestUser.setCorrect(true);
                answerStudent1.setCorrect(true);
                answerStudentRepository.save(answerStudent1);
                questionTestUserRepository.save(questionTestUser);
            } else  {
                questionTestUser.setCorrect(false);
                answerStudent1.setCorrect(false);
                answerStudentRepository.save(answerStudent1);
                questionTestUserRepository.save(questionTestUser);
            }
        }
        score = roundToNearestHalf(score);

        testUser.setScore(totalScore);
        if (totalScore >= test.getPastMark()){
            testUser.setStatus(true);
        } else {
            testUser.setStatus(false);
        }

        testUser.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        testUser.setCreatedBy("Demo");
        testUser.setModifiedBy("Demo");
        testUser.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        testUserRepository.save(testUser);

        return testUser.getId();
    }

    @Override
    public TestUserDTO getresultTest(String id, String userId) {
        TestUser testUser = testUserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
        if (!testUser.getUserId().equals(userId)) throw new AppException(ErrorCode.NOTFOUND);

        TestUserDTO testUserDTO = TestUserDTO.builder()
                .id(testUser.getId())
                .score(testUser.getScore())
                .time(testUser.getTime())
                .status(testUser.isStatus())
                .createdBy(testUser.getCreatedBy())
                .createdDate(testUser.getCreatedDate())
                .modifiedBy(testUser.getModifiedBy())
                .modifiedDate(testUser.getModifiedDate())
                .build();

        return testUserDTO;
    }

    @Transactional
    @Override
    public TestDTO saveTest(String userId, CreateTest createTest) {
//        if(excelUploadService.isValidExcelFile(createTest.getFile())){
//            try {
//                // 1 topic chỉ có 1 bài test
//                Test test = testRepository.findByTopicId(createTest.getTopicId());
//                if (test != null) throw new AppException(ErrorCode.NOTFOUND);
//
//                ApiResponse<TopicResponse> topicResponse = courseClient.getTopic(createTest.getTopicId());
//                if (topicResponse.getResult() == null) throw new AppException(ErrorCode.NOTFOUND);
//                Test testInput = Test.builder()
//                        .title(createTest.getTitle())
//                        .description(createTest.getDescription())
//                        .type(0)
//                        .time(createTest.getTime())
//                        .totalQuestion(0)
//                        .pastMark(createTest.getPastMark())
//                        .totalMark(createTest.getTotalMark())
//                        .topicId(createTest.getTopicId())
//                        .courseId(topicResponse.getResult().getCourseId())
//                        .createdBy("Demo")
//                        .modifiedBy("Demo")
//                        .createdDate(new Timestamp(System.currentTimeMillis()))
//                        .modifiedDate(new Timestamp(System.currentTimeMillis()))
//                        .build();
//                testRepository.save(testInput);
//
//                List<QuestionTest> questionTestInputList = excelUploadService.getCustomersDataFromExcel(createTest.getFile().getInputStream(), testInput);
//                this.questionTestRepository.saveAll(questionTestInputList);
//                testInput.setTotalQuestion(questionTestInputList.size());
//                testRepository.save(testInput);
//                return testMapper.toTestDTO(testInput);
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//                throw new IllegalArgumentException("The file is not a valid excel file");
//            }
//        } else {
//            throw new AppException(ErrorCode.NOTFOUND);
//        }
        return null;
    }

    @Override
    public TestDTO editTest(String userId, EditTest editTest) {
        Test test = testRepository.findById(editTest.getId()).orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
        Test testExisting = testRepository.findByTopicId(editTest.getTopicId());
        if (testExisting != null && !testExisting.getId().equals(test.getId())) throw new AppException(ErrorCode.NOTFOUND);
        ApiResponse<TopicResponse> topicResponse = courseClient.getTopic(editTest.getTopicId());
        if (topicResponse.getResult() == null) throw new AppException(ErrorCode.NOTFOUND);
        test.setTitle(editTest.getTitle());
        test.setDescription(editTest.getDescription());
        test.setTopicId(editTest.getTopicId());
        test.setCourseId(topicResponse.getResult().getCourseId());
        test.setType(editTest.getType());
        test.setTime(editTest.getTime());
        test.setPastMark(editTest.getPastMark());
        test.setTotalMark(editTest.getTotalMark());
        test.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        testRepository.save(test);
        return testMapper.toTestDTO(test);
    }

    @Override
    public TestDTOResponse getByTopic(String topicId, String userId) {
        Test test = testRepository.findByTopicId(topicId);
        if (test == null) return null;
        TestUser testUser = testUserRepository.findByTestAndUserIdAndStatusAndStatusStudy(test, userId, true, true);
        TestDTOResponse testDTOResponse = TestDTOResponse.builder()
                .id(test.getId())
                .title(test.getTitle())
                .time(test.getTime())
                .type(test.getType())
                .totalQuestion(test.getTotalQuestion())
                .description(test.getDescription())
                .createdBy(test.getCreatedBy())
                .createdDate(test.getCreatedDate())
                .modifiedBy(test.getModifiedBy())
                .modifiedDate(test.getModifiedDate())
                .build();
        if (testUser == null) {
            testDTOResponse.setStatus(false);
        } else {
            testDTOResponse.setStatus(true);
        }
        return testDTOResponse;
    }

    @Override
    public Boolean check(List<String> topicIds, String userId) {
        for (String topicId : topicIds) {
            // Tìm bài thi theo topicId
            Test test = testRepository.findByTopicId(topicId);
            if (test == null) return false;

            // Tìm xem người dùng đã hoàn thành bài thi hay chưa
            TestUser testUser = testUserRepository.findByTestAndUserIdAndStatusAndStatusStudy(test, userId, true, true);
            if (testUser == null) {
                return false; // Nếu có bất kỳ bài thi nào chưa hoàn thành, trả về false
            }
        }
        return true; // Nếu người dùng hoàn thành tất cả các bài thi, trả về true
    }

    @Transactional
    @KafkaListener(id = "testUserGroup", topics = "testUser")
    public void listen(TestUserRequest testUserRequest) {
        try {
            log.info("Received: {}");
            for (String topicId : testUserRequest.getTopicId()) {
                Test test = testRepository.findByTopicId(topicId);
                if (test == null) continue;
                List<TestUser> testUserList = testUserRepository.findAllByTestAndUserId(test, testUserRequest.getUserId());
                if (testUserList.isEmpty()) continue;
                for (TestUser testUser : testUserList) {
                    testUser.setStatusStudy(false);
                    testUserRepository.save(testUser);
                }
            }

        } catch (Exception e) {
            log.error("Error processing itemOnlineUser: {}", e);
            // Optionally, you can rethrow the exception or handle it accordingly
            // throw e;
        }
    }

    public static double roundToNearestHalf(double number) {
        // Lấy phần thập phân của số
        double fractionalPart = number - (int) number;

        // Làm tròn phần thập phân
        if (fractionalPart < 0.25) {
            return Math.floor(number); // Làm tròn xuống
        } else if (fractionalPart >= 0.75) {
            return Math.ceil(number); // Làm tròn lên
        } else {
            return Math.floor(number) + 0.5; // Làm tròn về 0.5
        }
    }

    private AnswerStudent findAnswerForQuestion(List<AnswerStudent> studentAnswers, QuestionTest questionTestOnline) {
        for (AnswerStudent answer : studentAnswers) {
            if (answer.getQuestionTest().equals(questionTestOnline)) {
                return answer;
            }
        }
        return null;
    }
}

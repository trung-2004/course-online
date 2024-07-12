package com.example.test_service.service;

import com.example.test_service.dto.request.CreateTest;
import com.example.test_service.dto.request.EditTest;
import com.example.test_service.dto.request.SubmitTest;
import com.example.test_service.dto.response.TestDTO;
import com.example.test_service.dto.response.TestDTOResponse;
import com.example.test_service.dto.response.TestDetail;
import com.example.test_service.dto.response.TestUserDTO;

import java.util.List;

public interface TestService {
    TestDetail getdetailTest(String id, String userId);

    String submitTest(String id, String userId, SubmitTest submitTest);

    TestUserDTO getresultTest(String id, String userId);

    TestDTO saveTest(String userId, CreateTest createTest);

    TestDTO editTest(String userId, EditTest editTest);

    TestDTOResponse getByTopic(String topicId, String userId);

    Boolean check(List<String> topicIds, String userId);
}

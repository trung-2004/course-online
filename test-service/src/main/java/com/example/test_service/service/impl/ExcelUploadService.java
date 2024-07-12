package com.example.test_service.service.impl;

import com.example.test_service.entity.QuestionTest;
import com.example.test_service.entity.Test;
import com.example.test_service.exception.AppException;
import com.example.test_service.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExcelUploadService {
//    private final SessionRepository sessionRepository;
//    private final TestSessionRepository testInputSessionRepository;
//    //private final
//
//    public boolean isValidExcelFile(MultipartFile file){
//        if (file != null) {
//            return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
//        }
//        return false;
//    }
//    public List<QuestionTest> getCustomersDataFromExcel(InputStream inputStream, Test testInput){
//        List<QuestionTest> questionTestInputList = new ArrayList<>();
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            XSSFSheet sheet = workbook.getSheet("TestInput");// name sheet
//            int rowIndex =0;
//            int orderTop = 0;
//            for (Row row : sheet){
//                if (rowIndex ==0){
//                    rowIndex++;
//                    continue;
//                }
//                Iterator<Cell> cellIterator = row.iterator();
//                //int cellIndex = 0;
//                int totalQuestion = 0;
//                QuestionTest questionTestInput = new QuestionTest();
//                for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
//                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                    switch (cellIndex){
//                        case 0 -> questionTestInput.setTitle(cell.getStringCellValue().trim());
//                        case 1 -> {
//
//                            if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
//                                break;
//                            } else {
//                                String id;
//                                if (cell.getCellType() == CellType.STRING) {
//                                    id = cell.getStringCellValue();
//                                } else if (cell.getCellType() == CellType.NUMERIC) {
//                                    id = String.valueOf((int) cell.getNumericCellValue());
//                                } else {
//                                    throw new AppException(ErrorCode.INVALID_KEY);
//                                }
//
//                                Session session = sessionRepository.findById(id)
//                                        .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
//                                TestSession testInputSessionExiting = testInputSessionRepository.findByTestAndSession(testInput, session);
//
//                                if (testInputSessionExiting == null) {
//                                    totalQuestion = 1;
//                                    TestSession testInputSession = TestSession.builder()
//                                            .session(session)
//                                            .test(testInput)
//                                            .orderTop(orderTop)
//                                            .totalQuestion(totalQuestion)
//                                            .createdBy("Demo")
//                                            .modifiedBy("Demo")
//                                            .createdDate(new Timestamp(System.currentTimeMillis()))
//                                            .modifiedDate(new Timestamp(System.currentTimeMillis()))
//                                            .build();
//                                    testInputSessionRepository.save(testInputSession);
//                                    questionTestInput.setTestSession(testInputSession);
//                                    orderTop += 1;
//                                } else {
//                                    questionTestInput.setTestSession(testInputSessionExiting);
//                                    testInputSessionExiting.setTotalQuestion(testInputSessionExiting.getTotalQuestion() + 1);
//                                }
//                            }
//                        }
//                        case 2 -> //questionTestInput.setAudiomp3(cell.getStringCellValue());
//                        {
//                            if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
//                                questionTestInput.setAudiomp3(" ");
//                            } else {
//                                switch (cell.getCellType()) {
//                                    case STRING:
//                                        questionTestInput.setAudiomp3(cell.getStringCellValue());
//                                        break;
//                                    case NUMERIC:
//                                        questionTestInput.setAudiomp3(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                        break;
//                                    default:
//                                        questionTestInput.setAudiomp3(" ");
//                                        break;
//                                }
//                            }
//                        }
//                        case 3 -> //questionTestInput.setImage(cell.getStringCellValue());
//                        {
//                            if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
//                                questionTestInput.setImage(" ");
//                            } else {
//                                switch (cell.getCellType()) {
//                                    case STRING:
//                                        questionTestInput.setImage(cell.getStringCellValue());
//                                        break;
//                                    case NUMERIC:
//                                        questionTestInput.setImage(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                        break;
//                                    default:
//                                        questionTestInput.setImage(" ");
//                                        break;
//                                }
//                            }
//                        }
//
//                        case 4 -> //questionTestInput.setParagraph(cell.getStringCellValue());
//                        {
//                            if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
//                                questionTestInput.setParagraph(" ");
//                            } else {
//                                switch (cell.getCellType()) {
//                                    case STRING:
//                                        questionTestInput.setParagraph(cell.getStringCellValue());
//                                        break;
//                                    case NUMERIC:
//                                        questionTestInput.setParagraph(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                        break;
//                                    default:
//                                        questionTestInput.setParagraph(" ");
//                                        break;
//                                }
//                            }
//                        }
//                        case 5 ->
//                        {
//                            switch (cell.getCellType()) {
//                                case STRING:
//                                    questionTestInput.setOption1(cell.getStringCellValue());
//                                    break;
//                                case NUMERIC:
//                                    questionTestInput.setOption1(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                    break;
//                                default:
//                                    questionTestInput.setOption1(null);
//                                    break;
//                            }
//                        }
//                        case 6 ->
//                        {
//                            {
//                                switch (cell.getCellType()) {
//                                    case STRING:
//                                        questionTestInput.setOption2(cell.getStringCellValue());
//                                        break;
//                                    case NUMERIC:
//                                        questionTestInput.setOption2(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                        break;
//                                    default:
//                                        questionTestInput.setOption2(null);
//                                        break;
//                                }
//                            }
//                        }
//                        case 7 ->
//                        {
//                            {
//                                switch (cell.getCellType()) {
//                                    case STRING:
//                                        questionTestInput.setOption3(cell.getStringCellValue());
//                                        break;
//                                    case NUMERIC:
//                                        questionTestInput.setOption3(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                        break;
//                                    default:
//                                        questionTestInput.setOption3(null);
//                                        break;
//                                }
//                            }
//                        }
//                        case 8 ->
//                        {
//                            {
//                                switch (cell.getCellType()) {
//                                    case STRING:
//                                        questionTestInput.setOption4(cell.getStringCellValue());
//                                        break;
//                                    case NUMERIC:
//                                        questionTestInput.setOption4(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                        break;
//                                    default:
//                                        questionTestInput.setOption4(null);
//                                        break;
//                                }
//                            }
//                        }
//                        case 9 ->
//                        {
//                            switch (cell.getCellType()) {
//                                case STRING:
//                                    questionTestInput.setCorrectanswer(cell.getStringCellValue());
//                                    break;
//                                case NUMERIC:
//                                    questionTestInput.setCorrectanswer(NumberToTextConverter.toText(cell.getNumericCellValue()));
//                                    break;
//                                default:
//                                    questionTestInput.setCorrectanswer(null);
//                                    break;
//                            }
//                        }
//                        case 10 -> questionTestInput.setType((int) cell.getNumericCellValue());
//                        case 11 -> questionTestInput.setPart((int) cell.getNumericCellValue());
//                        case 12 -> //questionTestInput.setOrderTop((int) cell.getNumericCellValue());
//                        {
//                            switch (cell.getCellType()) {
//                                case STRING:
//                                    questionTestInput.setOrderTop(Integer.valueOf(cell.getStringCellValue()));
//                                    break;
//                                case NUMERIC:
//                                    questionTestInput.setOrderTop(Integer.valueOf(NumberToTextConverter.toText(cell.getNumericCellValue())));
//                                    break;
//                                default:
//                                    questionTestInput.setOrderTop(0);
//                                    break;
//                            }
//                        }
//                        default -> {
//                        }
//                    }
//                }
//                questionTestInput.setCreatedBy("Demo");
//                questionTestInput.setModifiedBy("Demo");
//                questionTestInput.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//                questionTestInput.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//                questionTestInputList.add(questionTestInput);
//            }
//            workbook.close();
//        } catch (IOException e) {
//            e.getStackTrace();
//        }
//
//        return questionTestInputList;
//    }
}

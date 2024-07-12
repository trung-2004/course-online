package com.example.course_service.service.impl;

import com.example.course_service.entity.Certificate;
//import com.itextpdf.kernel.colors.Color;
//import com.itextpdf.kernel.colors.DeviceRgb;
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Service
public class PdfService {

    public byte[] generatePdf(Certificate certificate) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        // Step 1: Create PdfWriter
//        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
//
//        // Step 2: Create PdfDocument
//        PdfDocument pdfDoc = new PdfDocument(writer);
//
//        // Step 3: Create Document
//        Document document = new Document(pdfDoc);
//
//        // Step 4: Define Colors
//        DeviceRgb darkBlue = new DeviceRgb(25, 25, 112);
//        DeviceRgb black = new DeviceRgb(0, 0, 0);
//
//        // Step 5: Add content with specified colors
//        Paragraph title = new Paragraph("Certificate of Completion")
//                .setFontColor(darkBlue)
//                .setFontSize(24)
//                .setTextAlignment(TextAlignment.CENTER);
//        document.add(title);
//
//        Paragraph subTitle = new Paragraph("This is to certify that")
//                .setFontColor(black)
//                .setFontSize(12)
//                .setTextAlignment(TextAlignment.LEFT);
//        document.add(subTitle);
//
//        Paragraph userName = new Paragraph("User Name: " + certificate.getFullName())
//                .setFontColor(black)
//                .setFontSize(12)
//                .setTextAlignment(TextAlignment.LEFT);
//        document.add(userName);
//
//        Paragraph completionText = new Paragraph("Has successfully completed the course")
//                .setFontColor(black)
//                .setFontSize(12)
//                .setTextAlignment(TextAlignment.LEFT);
//        document.add(completionText);
//
//        Paragraph courseName = new Paragraph("Course: " + certificate.getCourse().getName())
//                .setFontColor(black)
//                .setFontSize(12)
//                .setTextAlignment(TextAlignment.LEFT);
//        document.add(courseName);
//
//        Paragraph issuedDate = new Paragraph("Date Issued: " + certificate.getIssuedDate())
//                .setFontColor(black)
//                .setFontSize(12)
//                .setTextAlignment(TextAlignment.RIGHT);
//        document.add(issuedDate);
//
//        // Step 6: Close the Document
//        document.close();
//
//        return byteArrayOutputStream.toByteArray();
        return null;
    }
}

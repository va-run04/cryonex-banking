package com.cryonex.customer.service;

import com.cryonex.customer.dto.response.AuditResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Pageable;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.cryonex.customer.exception.BusinessValidationException;
import java.io.ByteArrayOutputStream;

@Service
public class AuditService {

    private final CustomerAuditRepository auditRepository;
    private final CustomerRepository customerRepository;

    public AuditService(CustomerAuditRepository auditRepository,
                        CustomerRepository customerRepository) {
        this.auditRepository = auditRepository;
        this.customerRepository = customerRepository;
    }

    // 1) GET AUDIT HISTORY
    public List<AuditResponseDto> getAuditHistory(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("AUD_001", "Customer not found."));

        List<CustomerAudit> audits = auditRepository.findByCustomerOrderByCreatedDateDesc(customer);

        List<AuditResponseDto> responseList = new ArrayList<>();

        for (CustomerAudit audit : audits) {
            AuditResponseDto dto = new AuditResponseDto();
            dto.setAuditId(audit.getAuditId());
            dto.setAction(audit.getAction());
            dto.setPerformedBy(audit.getPerformedBy());
            dto.setOldValue(audit.getOldValue());
            dto.setNewValue(audit.getNewValue());
            dto.setCreatedDate(audit.getCreatedDate());
            responseList.add(dto);
        }

        return responseList;
    }

    // 2) GET SINGLE AUDIT RECORD
    public AuditResponseDto getAuditRecord(String customerId, String auditId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("AUD_001", "Customer not found."));

        CustomerAudit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new ResourceNotFoundException("AUD_002", "Audit record not found."));

        AuditResponseDto response = new AuditResponseDto();
        response.setAuditId(audit.getAuditId());
        response.setAction(audit.getAction());
        response.setPerformedBy(audit.getPerformedBy());
        response.setOldValue(audit.getOldValue());
        response.setNewValue(audit.getNewValue());
        response.setCreatedDate(audit.getCreatedDate());

        return response;
    }
    // 3) SEARCH AUDIT
    public Page<AuditResponseDto> searchAudit(String customerId, String action, String performedBy,
                                              LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("AUD_001", "Customer not found."));

        Specification<CustomerAudit> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("customer"), customer));

            if (action != null) {
                predicates.add(criteriaBuilder.equal(root.get("action"), action));
            }
            if (performedBy != null) {
                predicates.add(criteriaBuilder.equal(root.get("performedBy"), performedBy));
            }
            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate));
            }
            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<CustomerAudit> auditPage = auditRepository.findAll(spec, pageable);

        return auditPage.map(audit -> {
            AuditResponseDto dto = new AuditResponseDto();
            dto.setAuditId(audit.getAuditId());
            dto.setAction(audit.getAction());
            dto.setPerformedBy(audit.getPerformedBy());
            dto.setOldValue(audit.getOldValue());
            dto.setNewValue(audit.getNewValue());
            dto.setCreatedDate(audit.getCreatedDate());
            return dto;
        });
    }

    // 4) EXPORT AUDIT REPORT
    public byte[] exportAuditReport(String customerId, String format) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("AUD_001", "Customer not found."));

        List<CustomerAudit> audits = auditRepository.findByCustomerOrderByCreatedDateDesc(customer);

        if ("PDF".equalsIgnoreCase(format)) {
            return generatePdfReport(customer, audits);
        } else if ("EXCEL".equalsIgnoreCase(format)) {
            return generateExcelReport(customer, audits);
        } else {
            throw new BusinessValidationException("AUD_003", "Invalid export format. Use PDF or EXCEL.");
        }
    }

    private byte[] generatePdfReport(Customer customer, List<CustomerAudit> audits) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, com.itextpdf.kernel.geom.PageSize.A4.rotate());

            document.add(new Paragraph("Audit Report for Customer: " + customer.getCustomerId()));
            document.add(new Paragraph(" "));

            float[] columnWidths = {2, 3, 2, 4, 3};
            Table table = new Table(columnWidths);
            table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            table.addHeaderCell("Audit ID");
            table.addHeaderCell("Action");
            table.addHeaderCell("Performed By");
            table.addHeaderCell("Details");
            table.addHeaderCell("Date");

            for (CustomerAudit audit : audits) {
                table.addCell(audit.getAuditId());
                table.addCell(audit.getAction());
                table.addCell(audit.getPerformedBy() != null ? audit.getPerformedBy() : "-");
                table.addCell(audit.getNewValue() != null ? audit.getNewValue() : "-");
                table.addCell(audit.getCreatedDate().toString());
            }

            document.add(table);
            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report: " + e.getMessage(), e);
        }
    }

    private byte[] generateExcelReport(Customer customer, List<CustomerAudit> audits) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Audit Report");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Audit ID");
            headerRow.createCell(1).setCellValue("Action");
            headerRow.createCell(2).setCellValue("Performed By");
            headerRow.createCell(3).setCellValue("Details");
            headerRow.createCell(4).setCellValue("Date");

            int rowNum = 1;
            for (CustomerAudit audit : audits) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(audit.getAuditId());
                row.createCell(1).setCellValue(audit.getAction());
                row.createCell(2).setCellValue(audit.getPerformedBy() != null ? audit.getPerformedBy() : "-");
                row.createCell(3).setCellValue(audit.getNewValue() != null ? audit.getNewValue() : "-");
                row.createCell(4).setCellValue(audit.getCreatedDate().toString());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel report: " + e.getMessage(), e);
        }
    }






}
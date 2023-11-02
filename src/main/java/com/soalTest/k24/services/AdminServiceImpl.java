package com.soalTest.k24.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.soalTest.k24.entities.Admin;
import com.soalTest.k24.entities.Member;
import com.soalTest.k24.repositories.AdminRepository;
import com.soalTest.k24.repositories.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Admin createAdmin(Admin admin) {
        admin.setId(UUID.randomUUID().toString());
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    public Admin findById(String id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Admin updateAdmin(Admin admin) {
        Admin existingAdmin = adminRepository.findById(admin.getId()).orElse(null);
        if (existingAdmin != null) {
            existingAdmin.setName(admin.getName());
            existingAdmin.setPassword(admin.getPassword());

        }
        return adminRepository.save(existingAdmin);
    }

    public String deleteAdmin(String id) {
        adminRepository.deleteById(id);
        return "product removed !! " + id;
    }

    public void exportMembersToExcel(HttpServletResponse response, List<Member> members) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Courses Info");
            HSSFRow row = sheet.createRow(0);

            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Password");
            row.createCell(2).setCellValue("Gender");
            row.createCell(2).setCellValue("Birth");
            row.createCell(2).setCellValue("Phone Number");
            row.createCell(2).setCellValue("Email");
            row.createCell(2).setCellValue("No KTP");

            int rowNum = 1;
            for (Member member : members) {
                HSSFRow dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(member.getId());
                dataRow.createCell(1).setCellValue(member.getName());
                dataRow.createCell(2).setCellValue(member.getPassword());
                dataRow.createCell(2).setCellValue(member.getGender());
                dataRow.createCell(2).setCellValue(member.getBirth());
                dataRow.createCell(2).setCellValue(member.getPhone());
                dataRow.createCell(2).setCellValue(member.getEmail());
                dataRow.createCell(2).setCellValue(member.getKtp());
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=members.xlsx");
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ByteArrayInputStream exportMembersToPDF(List<Member> members) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);

            Font headFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 12);

            PdfPCell cell = new PdfPCell(new Paragraph("Members Report", headFont));
            cell.setColspan(8);
            cell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            cell.setPadding(10);
            table.addCell(cell);

            table.addCell(new PdfPCell(new Paragraph("ID", cellFont)));
            table.addCell(new PdfPCell(new Paragraph("Name", cellFont)));
            table.addCell(new PdfPCell(new Paragraph("Password", cellFont)));
            table.addCell(new PdfPCell(new Paragraph("Gender", cellFont)));
            table.addCell(new PdfPCell(new Paragraph("Phone Number", cellFont)));
            table.addCell(new PdfPCell(new Paragraph("Email", cellFont)));
            table.addCell(new PdfPCell(new Paragraph("Date of Birth", cellFont)));
            table.addCell(new PdfPCell(new Paragraph("No KTP", cellFont)));

            for (Member member : members) {
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(member.getId()), cellFont)));
                table.addCell(new PdfPCell(new Paragraph(member.getName(), cellFont)));
                table.addCell(new PdfPCell(new Paragraph(member.getPassword(), cellFont)));
                table.addCell(new PdfPCell(new Paragraph(member.getGender(), cellFont)));
                table.addCell(new PdfPCell(new Paragraph(member.getPhone(), cellFont)));
                table.addCell(new PdfPCell(new Paragraph(member.getEmail(), cellFont)));
                table.addCell(new PdfPCell(new Paragraph(member.getBirth().toString(), cellFont)));
                table.addCell(new PdfPCell(new Paragraph(member.getKtp(), cellFont)));
            }

            document.add(table);
            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }


}

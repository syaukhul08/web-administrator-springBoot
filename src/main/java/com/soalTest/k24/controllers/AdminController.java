package com.soalTest.k24.controllers;

import com.soalTest.k24.entities.Admin;
import com.soalTest.k24.entities.Member;
import com.soalTest.k24.services.AdminServiceImpl;
import com.soalTest.k24.services.MemberServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private  AdminServiceImpl adminService;

    @Autowired
    private MemberServiceImpl memberService;

    @PostMapping("/add")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin add = adminService.createAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(add);
    }

    @GetMapping
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("admin/index.html");
        List<Member> result = memberService.getAllMembers();
        view.addObject("memberList", result);
        return view;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable("id") String id){
        Member member = memberService.findById(id);
        if (member == null){
            return new ModelAndView("redirect:/admin");
        }
        ModelAndView view = new ModelAndView("admin/detail.html");
        view.addObject("data",member);
        return view;
    }

    @GetMapping
    public List<Admin> getAllAdmin() {
        return adminService.getAllAdmin();
    }

    @GetMapping("/{id}")
    public Admin getAdmin(@PathVariable String id) {
        return adminService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable String id, @RequestBody Admin admin) {
        Admin existingAdmin = adminService.findById(id);
        if (existingAdmin == null) {
            return ResponseEntity.notFound().build();
        }
        admin.setId(String.valueOf(id));
        adminService.updateAdmin(admin);
        return ResponseEntity.ok("Member updated");
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable String id) {
        return adminService.deleteAdmin(id);
    }

    @GetMapping("/export/excel")
    public void exportMembersToExcel(HttpServletResponse response) {
        List<Member> members = memberService.getAllMembers();
        adminService.exportMembersToExcel(response, members);
    }

    @GetMapping("/export/pdf")
    public void exportMembersToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=members.pdf");

        List<Member> members = memberService.getAllMembers();

        ByteArrayInputStream bis = adminService.exportMembersToPDF(members);
        IOUtils.copy(bis, response.getOutputStream());
    }
}

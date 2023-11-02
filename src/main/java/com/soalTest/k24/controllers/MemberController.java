package com.soalTest.k24.controllers;

import com.soalTest.k24.entities.Member;
import com.soalTest.k24.services.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberServiceImpl memberService;

    @PostMapping("/add")
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member add = memberService.createMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(add);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable String id) {
        return memberService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMember(@PathVariable String id, @RequestBody Member member) {
        Member existingMember = memberService.findById(id);
        if (existingMember == null) {
            return ResponseEntity.notFound().build();
        }
        member.setId(String.valueOf(id));
        memberService.updateMember(member);
        return ResponseEntity.ok("Member updated");
    }

    @DeleteMapping("/delete/{id}")
    public String deleteMember(@PathVariable String id) {
        return memberService.deleteMember(id);
    }


    @PostMapping("/{id}/uploadPhoto")
    public ResponseEntity<String> uploadPhoto(@PathVariable String id, @RequestParam("photo") MultipartFile photoFile) {
        try {
            memberService.uploadPhoto(id, photoFile);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }
}

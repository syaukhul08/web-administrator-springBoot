package com.soalTest.k24.services;

import com.soalTest.k24.entities.Admin;
import com.soalTest.k24.entities.Member;
import com.soalTest.k24.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MemberServiceImpl {

    @Autowired
    private MemberRepository memberRepository;

    public Member createMember(Member member) {
        member.setId(UUID.randomUUID().toString());
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member findById(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    public Member updateMember(Member member) {
        Member existingMember = memberRepository.findById(member.getId()).orElse(null);
        if (existingMember != null) {
            existingMember.setName(member.getName());
            existingMember.setPhone(member.getPhone());
            existingMember.setBirth(member.getBirth());
            existingMember.setEmail(member.getEmail());
            existingMember.setGender(member.getGender());
            existingMember.setKtp(member.getKtp());
        }
        return memberRepository.save(existingMember);
    }
    public String deleteMember(String id) {
        memberRepository.deleteById(id);
        return "product removed !! " + id;
    }

    public Member uploadPhoto(String memberId, MultipartFile photoFile) throws IOException {
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member != null) {
            long fileSizeInBytes = photoFile.getSize();
            if (fileSizeInBytes <= DataSize.ofMegabytes(1).toBytes()) {
                member.setPhoto(photoFile.getBytes());
                return memberRepository.save(member);
            } else {
                throw new IllegalArgumentException("File size exceeded 1 MB");
            }
        }
        return null;
    }
}

package com.example.demo.controller;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;
	
	@GetMapping("/members/{id}")
	public String findMember(@PathVariable("id") Member member) {
		//Member member = memberRepository.findById(id).get();
		return member.getUsername();
	}
	
	@GetMapping("/members")
	public Page<Member> list(Pageable pageable) {
		return memberRepository.findAll(pageable);
	}
	
	@PostConstruct
	public void init() {
		//memberRepository.save(new Member("userA"));
		for(int i=0; i<100; i++) {
			//memberRepository.save(new Member("userA"));
			memberRepository.save(new Member("user" + i, i));
		}	
	}
}

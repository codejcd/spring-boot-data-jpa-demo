package com.example.demo.controller;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MemberDto;
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
	public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
		PageRequest pageRequest = PageRequest.of(1, 2);
		return memberRepository.findAll(pageable).map(MemberDto::new);
		// Page<Member> page = memberRepository.findAll(pageable).map;
		//Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
		//return map;
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

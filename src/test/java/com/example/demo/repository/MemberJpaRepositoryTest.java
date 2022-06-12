package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Member;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberJpaRepositoryTest {

	@Autowired
	MemberJpaRepository memberJpaRepository;
	
	@Test
	public void testMember() {
		Member member = new Member("Jack");
		Member savedMember = memberJpaRepository.save(member);
		Member findMember = memberJpaRepository.find(savedMember.getId());
		
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member); // 동일한 트랜잭션 내에서는 JPA 영속성 컨텍스트는 동일하다는 결과 (1차캐시)
	}
	
}

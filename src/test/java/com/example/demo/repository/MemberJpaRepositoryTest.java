package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
	
	@Test
	public void findByUsernameAndAgeGreaterThan() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);
		
		List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
		
		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
	}
	
	@Test
	public void paging() {
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 10));
		memberJpaRepository.save(new Member("member3", 10));
		memberJpaRepository.save(new Member("member4", 10));
		memberJpaRepository.save(new Member("member5", 10));
		
		
		int age = 10;
		int offset = 0;
		int limit = 3;
		
		List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
		long totalCount = memberJpaRepository.totalCount(age);
		
		assertThat(members.size()).isEqualTo(3);
		assertThat(totalCount).isEqualTo(5);
	}
	
	@Test
	public void bulkUpdate() {
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 20));
		memberJpaRepository.save(new Member("member3", 30));
		memberJpaRepository.save(new Member("member4", 31));
		memberJpaRepository.save(new Member("member5", 50));
		
		int resultCount = memberJpaRepository.bulkAgePlus(20);
		
		assertThat(resultCount).isEqualTo(4);
	}
}

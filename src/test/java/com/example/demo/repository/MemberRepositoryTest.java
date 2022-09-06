package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Team;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	TeamJpaRepository teamRepository;
	
	@PersistenceContext
	EntityManager em;
	
	//@Test
	public void testMember() {
		Member member = new Member("Jack");
		Member savedMember = memberRepository.save(member);
		Member findMember = memberRepository.findById(savedMember.getId()).get();
		
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		// 동일한 트랜잭션 내에서는 JPA 영속성 컨텍스트는 동일하다는 결과 (1차캐시)
		assertThat(findMember).isEqualTo(member); 
	}
	
	//@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);
		
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);
		
		long count = memberRepository.count();
		assertThat(count).isEqualTo(2);
		
		memberRepository.delete(member1);
		memberRepository.delete(member2);
		
		long deletedCount = memberRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}
	
	//@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
		
		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		
	}
	
	//@Test
	public void findByUser() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<Member> result = memberRepository.findUser("AAA", 10);
		assertThat(result.get(0)).isEqualTo(m1);
		
		
	}
	
	//@Test
	public void findUsernameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<String> usernameList = memberRepository.findUsernameList();
		assertThat(usernameList.get(0)).isEqualTo(m1.getUsername());
		
	}
	
	
	//@Test 
	public void findMemberDto() {
		
		Team team = new Team("teamA");
		teamRepository.save(team);
		
		Member m1 = new Member("AAA", 10);
		m1.setTeam(team);
		memberRepository.save(m1);
		
		List<MemberDto> memberDto = memberRepository.findMemberDto();
		for(MemberDto dto : memberDto) {
			System.out.println("dto = " + dto);
		}
	}
	
	//@Test 
	public void findByNames() {
		
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<String> paramList = new ArrayList<>();
		paramList.add("AAA");
		paramList.add("BBB");
		
		List<Member> result = memberRepository.findByNames(paramList);
		for(Member m : result) {
			System.out.println("member = " + m);
		}
	}
	
	//@Test 
	public void returnType() {
		
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<Member> memberList = memberRepository.findListByUsername("AAA");
		assertThat(memberList.get(0).getUsername()).isEqualTo(m1.getUsername());
		
		Member member =  memberRepository.findMemberByUsername("AAA");
		assertThat(member.getUsername()).isEqualTo(m1.getUsername());
		
		Optional<Member> optional = memberRepository.findOptionalByUsername("AAA");
		assertThat(optional.isPresent()).isEqualTo(m1.getUsername());
	}
	
	@Test
	public void paging() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));
		
		int age = 10;
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
		
		Page<Member> page = memberRepository.findByAge(age, pageRequest);
		
		Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
		
		List<Member> content = page.getContent();
		long totalEle = page.getTotalElements();
		
		assertThat(content.size()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}
	
	@Test
	public void bulkUpdate() {
		// 벌크 연산 시 DB에 바로 업데이트 하지만 영속성 컨텍스트에는 반영이 안되므로 수동 업데이트해야함.
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 20));
		memberRepository.save(new Member("member3", 30));
		memberRepository.save(new Member("member4", 31));
		memberRepository.save(new Member("member5", 50));
		
		int resultCount = memberRepository.bulkAgePlus(20);
		//em.flush();
		//em.clear();
		
		List<Member> result = memberRepository.findByUsername("member5");
		 
		Member member5 = result.get(0);
		System.out.println("member5 = " + member5);
		
		assertThat(resultCount).isEqualTo(4);
	}
	
	@Test
	public void findMemberLazy() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		
		teamRepository.save(teamA);
		teamRepository.save(teamB);
		
		Member m1 = new Member("member1", 10, teamA);
		Member m2 = new Member("member2", 10, teamA);
		
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		em.flush();
		em.clear();
		
		List<Member> members = memberRepository.findAll(); // fetch join 이 안된경우 N+1 문제가 생긴다.
		//List<Member> members = memberRepository.findMemberFetchJoin(); // fetch join을 짜는 것이 부담되는 경우 entity graph 옵션을 사용.
		// JPQL과 entity graph 옵션을 같이 사용도 가능하다.
		
		for(Member member : members) {
			System.out.println("member = " + member.getUsername());
			System.out.println("member,teamClass = " + member.getTeam().getClass());
			System.out.println("member.team = " + member.getTeam().getName()); 
		}
		
	}
	
	@Test
	public void queryHint() {
		Member m1 = new Member("member1", 10); 
		memberRepository.save(m1);
		em.flush();
		em.clear();
		
		//Member findMember = memberRepository.findById(m1.getId()).get(); // 객체가 2개가 관리가된다.
		Member findMember = memberRepository.findReadOnlyByUsername("member1"); // 성능 최적화.
		findMember.setUsername("member2");
		
		em.flush();
		
	}
	
	@Test
	public void lock() {
		Member m1 = new Member("member1", 10); 
		memberRepository.save(m1);
		em.flush();
		em.clear();
		
		List<Member> result = memberRepository.findLockByUsername("member1");
		
	}
	
	@Test
	public void callCustom() {
		List<Member> result = memberRepository.fidMemberCustom();
	}
		
}

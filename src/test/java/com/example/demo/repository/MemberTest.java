package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Member;
import com.example.demo.entity.Team;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberTest {

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	public void testEntity() {
		Team teamA = new Team("TeamA");
		Team teamB = new Team("TeamB");
		
		em.persist(teamA);
		em.persist(teamB);
		
		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member1", 20, teamA);
		Member member3 = new Member("member1", 30, teamB);
		Member member4 = new Member("member1", 40, teamB);

		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
		
		em.flush();
		em.clear();
		
	    List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
	    
	    for (Member member : members) {
	    	System.out.println("member = " + member);
	    	System.out.println("-> member.team = " + member.getTeam());
	    }
		
	}
	
	@Test
	public void JpaEventBaseEntity() throws Exception {
		
		//given
		Member member = new Member("member1");
		memberRepository.save(member); //@PrePersist
		
		Thread.sleep(100);
		member.setUsername("member2");
		
		em.flush(); // @preUpdate
		em.clear();
		
		//when
		Member findMember = memberRepository.findById(member.getId()).get();
		
		//then
		//System.out.println("findMember.getCreateDate = " + findMember.getCreateDate());
		//System.out.println("findMember.getUodateDate = " + findMember.getUpdateDate());
		
		System.out.println("findMember.getCreateDate = " + findMember.getCreatedDate());
		System.out.println("findMember.getUpdateDate = " + findMember.getLastModifiedDate());
		System.out.println("findMember.getCreateBy = " + findMember.getCreatedBy());
		System.out.println("findMember.getModifiedBy = " + findMember.getLastModifedBy());
	
		
	}
	
}

package com.example.demo.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository, JpaSpecificationExecutor<Member> {
	
	public List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
	
	//@Query(name = "Member.findByUsername")
	List<Member> findByUsername(@Param("username") String username);
	
	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String userName, @Param("age") int age);
	
	@Query("select m.username from Member m")
	List<String> findUsernameList();
	
	@Query("select new com.example.demo.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();
	
	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") List<String> names);
	
	List<Member> findListByUsername(String username); // 컬렉션은 값이 없어도 null을 반환하지는 않는다.
	Member findMemberByUsername(String username);
	Optional<Member> findOptionalByUsername(String username);
	
	@Query(value = "select m from Member m left join m.team t",
			countQuery = "select count(m) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);
	
	@Modifying(clearAutomatically = true) // 없을 경우 select 호출. clear 대신 옵션사용
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);
	
	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();
	
	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();
	
	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();
	
	@EntityGraph(attributePaths = {"team"})
	List<Member> findEntityGraphByUsername(@Param("username") String username);
	
	@QueryHints(value = @QueryHint(name="org.hibernate.readOnly", value="true"))
	Member findReadOnlyByUsername(String username);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE) // JPA 거를 편하게 쓸수있음
	List<Member> findLockByUsername(String username);
}

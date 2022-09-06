package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
//@NamedQuery(name="Member.findByUsername", query="select m from Member m where m.username = :username")
// namedquery 는 실무 사용은 잘 하지 않는다.
public class Member extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name="member_id")
	private Long id;
	private String username;
	private int age;
	
	@ManyToOne(fetch = FetchType.LAZY) // 최적화를 위해서는 왠만한 즉시 로딩보다는 LAZY 로 세팅. 아닐경우 매우 복잡한 쿼리가 생성될수있음.
	@JoinColumn(name="team_id")
	private Team team;
	
	public Member(String username) {
		this.username = username;
	}
	
	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}
	
	public Member(String username, int age, Team team) {
		this.username = username;
		this.age = age;
		this.team = team;
		
		if(team != null) {
			changeTeam(team);
		}
	}
	
	
	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}

}

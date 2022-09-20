package com.example.demo.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.example.demo.entity.Member;
import com.example.demo.entity.Team;

public class MemberSpec {

	public static Specification<Member> teamName(final String teamName) {
		return new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			
				if(!StringUtils.hasText(teamName)) {
					return null;
				}
				
				Join<Member, Team> t = root.join("team", javax.persistence.criteria.JoinType.INNER);
				return criteriaBuilder.equal(t.get("name"), teamName);
			}
		};
	}
	
	public static Specification<Member> username(final String username) {
		return (Specification<Member>)(root, query, builder) -> 
			builder.equal(root.get("username"), username);
	}
}

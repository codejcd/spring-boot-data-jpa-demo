package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.example.demo.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements CustomMemberRepository {
	
	private final EntityManager em;
	
	@Override
	public List<Member> fidMemberCustom() {
		return em.createQuery("select m from Member m").getResultList();
	}

}

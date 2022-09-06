package com.example.demo.dto;

import lombok.Data;

@Data // entity에는 왠만하면 쓰면 안된다.
public class MemberDto {
	
	private Long id;
	private String username;
	private String teamName;
	
	public MemberDto(Long id, String username, String teamName) {
		this.id = id;
		this.username = username;
		this.teamName = teamName;
	}
}

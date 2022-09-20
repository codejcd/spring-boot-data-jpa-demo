package com.example.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.example.demo.SpringbootDataJpaDemoApplication;
import com.example.demo.entity.Item;

@SpringBootTest
@ContextConfiguration
public class ItemRepositoryTest {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Test
	public void save() {
		Item item = new Item("A");
		itemRepository.save(item);
	}
}

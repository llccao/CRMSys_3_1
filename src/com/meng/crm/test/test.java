package com.meng.crm.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.meng.crm.entity.Customer;
import com.meng.crm.entity.CustomerDrain;
import com.meng.crm.entity.SalesChance;
import com.meng.crm.orm.Page;
import com.meng.crm.service.CustomerDrainService;
import com.meng.crm.service.CustomerService;
import com.meng.crm.service.SalesChanceService;

@Service
public class test {

	private ApplicationContext ioc = null;
	
	private SalesChanceService service;
	
	private CustomerService customerService;
	
	private CustomerDrainService customerDrainService;
	
	@Before
	public void init() {
		ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		service = ioc.getBean(SalesChanceService.class);
		customerService = ioc.getBean(CustomerService.class);
		customerDrainService =ioc.getBean(CustomerDrainService.class);
	}
	
	@Test
	public void testGetById() {
		//SalesChance chance = service.getById(162);
		
		CustomerDrain customerDrain =customerDrainService.getDrainById(204);
		System.out.println(customerDrain.getDelay());
		//Page<Customer> page = customerService.findAll(1, 4,null);
		
		//System.out.println(page.getTotalElements());
		
		//System.out.println(chance.getDesignee());
	}

}

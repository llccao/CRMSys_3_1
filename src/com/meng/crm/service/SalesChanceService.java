package com.meng.crm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.meng.crm.dao.ContactMapper;
import com.meng.crm.dao.CustomerMapper;
import com.meng.crm.dao.SalesChanceMapper;
import com.meng.crm.entity.Contact;
import com.meng.crm.entity.Customer;
import com.meng.crm.entity.SalesChance;
import com.meng.crm.entity.User;
import com.meng.crm.orm.Page;
import com.meng.crm.orm.PropertyFilter;
import com.meng.crm.orm.PropertyFilter.MatchType;
import com.meng.crm.utils.ReflectionUtils;

@Transactional(readOnly=true,
			   isolation=Isolation.DEFAULT,
			   propagation=Propagation.REQUIRES_NEW,
			   rollbackFor=Exception.class)
@Service
public class SalesChanceService {
	
	@Autowired
	private SalesChanceMapper salesChanceMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private ContactMapper contactMapper;
	
	/**
	 * 	开发客户成功
	 *  将 sales_chances表中对应ID的那条记录的stauts 字段的值修改为 3
	 *	向 customers 和 contacts 数据表中各插入一条记录。
	 *	Cusomers 数据表中插入 3 个字段：name，no（随机字符串） 和 state（正常）
	 *	向 contacts 数据表也插入 3 个字段：name、tel、customer_id
	 *	将上面三个操作放在同一方法中受同一个事务管理
	 *
	 *	@author lsj
	 */
	@Transactional
	public void finish(int id) {
		SalesChance salesChance = salesChanceMapper.getById(id);
		
		//1.将 sales_chances表中对应ID的那条记录的stauts 字段的值修改为 3
		salesChanceMapper.updateStatusById(3,id);
		
		//2.Customers 数据表中插入 3 个字段：name，no（随机字符串） 和 state（正常）
		Customer customer = new Customer();
		customer.setNo(UUID.randomUUID().toString());
		customer.setName(salesChance.getCustName());
		customer.setState("正常");
		customerMapper.insertPart(customer);
		
		//3.向 contacts 数据表也插入 3 个字段：name、tel、customer_id
		Contact contact = new Contact();
		contact.setName(salesChance.getContact());
		contact.setTel(salesChance.getContactTel());
		contact.setCustomer(customer);
		contactMapper.insertPart(contact);
	}
	
	@Transactional
	public void updatePart(SalesChance chance) {
		chance.setStatus(2);
		salesChanceMapper.updatePart(chance);
	}
	
	public List<User> getUsers() {
		
		return salesChanceMapper.getUsers();
	}
	
	@Transactional
	public void delete(Integer id) {
		salesChanceMapper.delete(id);
	}

	/**
	 * 根据chanceId修改chance的状态
	 * @param id
	 * @author lsj
	 *//*
	@Transactional
	public void updateStatusById(Integer status, Integer id){
		
	}*/
	
	@Transactional
	public void update(SalesChance chance) {
		chance.setStatus(2);
		salesChanceMapper.update(chance);
	}
	
	public SalesChance getById(Integer id) {
		return salesChanceMapper.getById(id);
	}
	
	@Transactional
	public void save(SalesChance salesChance) {
//		Long designeeId = salesChance.getDesignee().getId();
			// 说明是录入的销售机会
			salesChance.setStatus(1);
		salesChanceMapper.insert(salesChance);
		
	}
	
	/**
	 * 待查询条件的分页,要将params里面的参数分解出来,传到dao中进行查询
	 * 1.将params 里面的参数分解成  PropertyFilter 类型 然后组装成一个map当做入参传进dao层的方法中
	 */
	public Page<SalesChance> getPage(int pageNo,int pageSize, Map<String, Object> params) {
		
		Page<SalesChance> page = new Page<SalesChance>();
		
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		
		// 将参数转成propertyFilter 类型
		List<PropertyFilter> filters = PropertyFilter.parseParamsToFilters(params);
		// 将filters 分解成 可以放到map中的能够传入入参的map
		Map<String,Object> myBatisMap = parseFiltersToMyBatisMap(filters);
		
		
		int fromIndex = (page.getPageNo() -1 ) * page.getPageSize() + 1;
		int endIndex = fromIndex + page.getPageSize();
		
		// status 不能写死,因为后面分派的时候还要用到.
		myBatisMap.put("status", 1);
		myBatisMap.put("fromIndex", fromIndex);
		myBatisMap.put("endIndex", endIndex);
		
		long totalElements = salesChanceMapper.getElements(myBatisMap);
		
		page.setTotalElements(totalElements);
		
		List<SalesChance> content = salesChanceMapper.getContent(myBatisMap);
		page.setContent(content);
		
		return page;
	}

	private Map<String, Object> parseFiltersToMyBatisMap(
			List<PropertyFilter> filters) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		for (PropertyFilter filter : filters) {
			
			MatchType matchType = filter.getMatchType();
			String propertyName = filter.getPropertyName();
			Class propertyType = filter.getPropertyType();
			Object propertyValue = filter.getPropertyValue();
			
			propertyValue = ReflectionUtils.convertValue(propertyValue, propertyType);
			
			switch(matchType) {
			case LIKE:
				propertyValue = "%" + propertyValue + "%";
			}
			map.put(propertyName, propertyValue);
		}
		
		return map;
	}

}

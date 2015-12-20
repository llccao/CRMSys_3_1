package com.meng.crm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meng.crm.dao.CustomerDrainMapper;
import com.meng.crm.entity.CustomerDrain;
import com.meng.crm.orm.Page;
import com.meng.crm.orm.PropertyFilter;
import com.meng.crm.orm.PropertyFilter.MatchType;
import com.meng.crm.utils.ReflectionUtils;

@Service
public class CustomerDrainService {

	@Autowired
	private CustomerDrainMapper customerDrainMapper;
	
	@Transactional
	public void callCustomerDrainCheckProcedure(){
		customerDrainMapper.callCheckDrainProcedure();
	}
	
	@Transactional
	public Page<CustomerDrain> findAll(int pageNo, int pageSize,
			Map<String, Object> params) {
		Page<CustomerDrain> page = new Page<CustomerDrain>();

		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		// 将参数转成propertyFilter 类型
		List<PropertyFilter> filters = PropertyFilter
				.parseParamsToFilters(params);
		// 将filters 分解成 可以放到map中的能够传入入参的map
		Map<String, Object> myBatisMap = parseFiltersToMyBatisMap(filters);

		int fromIndex = (page.getPageNo() - 1) * page.getPageSize() + 1;
		int endIndex = fromIndex + page.getPageSize();

		myBatisMap.put("fromIndex", fromIndex);
		myBatisMap.put("endIndex", endIndex);

		long totalElements = customerDrainMapper.getElements(myBatisMap);

		page.setTotalElements(totalElements);

		List<CustomerDrain> content = customerDrainMapper.getContent(myBatisMap);
		page.setContent(content);

		return page;

	}

	private Map<String, Object> parseFiltersToMyBatisMap(
			List<PropertyFilter> filters) {

		Map<String, Object> map = new HashMap<String, Object>();

		for (PropertyFilter filter : filters) {

			MatchType matchType = filter.getMatchType();
			String propertyName = filter.getPropertyName();
			Class propertyType = filter.getPropertyType();
			Object propertyValue = filter.getPropertyValue();

			propertyValue = ReflectionUtils.convertValue(propertyValue,
					propertyType);

			switch (matchType) {
			case LIKE:
				propertyValue = "%" + propertyValue + "%";
			}
			map.put(propertyName, propertyValue);
		}

		return map;
	}

	@Transactional
	public CustomerDrain getDrainById(Integer id) {
		// TODO Auto-generated method stub
		return customerDrainMapper.getDrainById(id);
	}

	
	@Transactional
	public void updateCustomerDrain(CustomerDrain drain) {
		customerDrainMapper.updateCustomerDrain(drain);
	}
	

}

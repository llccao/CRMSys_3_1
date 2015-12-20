package com.meng.crm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.meng.crm.dao.SalesChanceMapper;
import com.meng.crm.dao.SalesPlanMapper;
import com.meng.crm.entity.SalesChance;
import com.meng.crm.entity.SalesPlan;
import com.meng.crm.orm.Page;
import com.meng.crm.orm.PropertyFilter;
import com.meng.crm.orm.PropertyFilter.MatchType;
import com.meng.crm.utils.ReflectionUtils;

@Transactional(readOnly=true,
			   isolation=Isolation.DEFAULT,
			   propagation=Propagation.REQUIRES_NEW,
			   rollbackFor=Exception.class)
@Service
public class SalesPlanService {

	@Autowired
	private SalesPlanMapper salesPlanMapper;
	
	@Autowired
	private SalesChanceMapper salesChanceMapper;
	
	// 处于正在执行中的销售机会
	public Page<SalesChance> getAll(int pageNo , int pageSize,Map<String, Object> params) {
		
		Page<SalesChance> page = new Page<SalesChance>();
		
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		
		// 将参数转成propertyFilter 类型
		List<PropertyFilter> filters = PropertyFilter.parseParamsToFilters(params);
		// 将filters 分解成 可以放到map中的能够传入入参的map
		Map<String,Object> myBatisMap = parseFiltersToMyBatisMap(filters);
		
		
		int fromIndex = (page.getPageNo() -1 ) * page.getPageSize() + 1;
		int endIndex = fromIndex + page.getPageSize();
		
		// status 不能写死,因为后面分派的时候还要用到,要显示的是status不是1的.
		myBatisMap.put("status", 2);
		myBatisMap.put("fromIndex", fromIndex);
		myBatisMap.put("endIndex", endIndex);
		
		long totalElements = salesPlanMapper.getElements(myBatisMap);
		
		page.setTotalElements(totalElements);
		
		List<SalesChance> content = salesPlanMapper.getContent(myBatisMap);
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
	
	//停止计划
	@Transactional
	public void stop(Integer id) {
		
		salesPlanMapper.stop(id);
	}
	
	@Transactional
	public List<SalesPlan> getSalesPlanById(Integer id) {
		
		return salesPlanMapper.getSalesPlanById(id);
	}
	
}

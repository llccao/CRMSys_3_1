package com.meng.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.meng.crm.entity.SalesChance;
import com.meng.crm.entity.SalesPlan;

public interface SalesPlanMapper {
	// 获取总记录数
	public long getElements(Map<String, Object> myBatisMap);

	// 获取当前页面的 content
	public List<SalesChance> getContent(Map<String, Object> myBatisMap);
	
	//停止计划
	@Update("update sales_chances "
			+ "set status=4"
			+ "where id=#{id}")
	public void stop(Integer id);
	
	//查询计划
	@Select("select id,plan_date as \"date\" ,todo,plan_result as \"result\" from SALES_PLAN where chance_id=#{id}")
	public List<SalesPlan> getSalesPlanById(Integer id);

}

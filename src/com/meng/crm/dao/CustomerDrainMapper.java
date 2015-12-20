package com.meng.crm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Update;

import com.meng.crm.entity.CustomerDrain;

public interface CustomerDrainMapper {

	@Update("{call check_drain}")
	public void callCheckDrainProcedure();
	
	// 获取总记录数
	public long getElements(Map<String, Object> myBatisMap);

	// 获取当前页面的 content
	public List<CustomerDrain> getContent(Map<String, Object> myBatisMap);

	public CustomerDrain getDrainById(Integer id);

	/*@Update("update customer_drains set delay=#{delay} where id=#{id}")
	public void updateDelay(String delays,Integer id);

	@Update("update customer_drains set last_order_date =#{date} ,delay=#{delay} where id=#{id}")
	public void updateReason(Date date, String reason,Integer id);
*/
	
	public void updateCustomerDrain(CustomerDrain drain);

	
	

}

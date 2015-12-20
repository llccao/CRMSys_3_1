package com.meng.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.meng.crm.entity.Customer;
import com.meng.crm.entity.Dict;
import com.meng.crm.orm.Page;

public interface CustomerMapper {

	/**
	 * 客户开发成功时插入进customer的关联的一些属性字段 插入 3 个字段：name，no（随机字符串） 和 state（正常）
	 * 此处因为得用插入之后的主键 所以该用配置文件的方式
	 * 
	 * @author lsj
	 */
	void insertPart(Customer customer);
	
	// 获取总记录数
	public long getElements(Map<String, Object> myBatisMap);

	// 获取当前页面的 content
	public List<Customer> getContent(Map<String, Object> myBatisMap);
	
	//select item from dicts t where id >=23 and id<=30
	@Select("select id,item from dicts  where id in (23,24,25,26,27)")
	List<Dict> findLevels();
	
	@Select("select id, item from dicts  where id in (18,19,20,21,22,28)")
	List<Dict> findRegions();
}

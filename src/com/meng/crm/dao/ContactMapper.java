package com.meng.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.meng.crm.entity.Contact;

public interface ContactMapper {
	
	/**
	 * 开发客户成功之后向contact表中插入3个字段
	 * @param contact
	 */
	@SelectKey(before=true,keyProperty="id",keyColumn="id", statement="select crm_seq.nextval from dual",resultType=Long.class)
	@Insert("insert into contacts(id,name,tel,customer_id) values(#{id},#{name,jdbcType=VARCHAR},#{tel,jdbcType=VARCHAR},#{customer.id,jdbcType=NUMERIC})")
	void insertPart(Contact contact);
	
}

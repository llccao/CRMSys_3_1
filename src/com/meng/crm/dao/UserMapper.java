package com.meng.crm.dao;

import org.apache.ibatis.annotations.Select;

import com.meng.crm.entity.User;

public interface UserMapper {
	
	@Select("select id,name,password,enabled from users where name = #{name}")
	public User getByName(String name);
	
}

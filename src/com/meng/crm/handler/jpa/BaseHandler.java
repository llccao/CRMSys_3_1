package com.meng.crm.handler.jpa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.util.WebUtils;

import com.meng.crm.service.jpa.BaseService;

public class BaseHandler<T, PK extends Serializable> {

	@Autowired
	private BaseService<T, PK> service;

	public void saveAndFlush(T t) {
		 service.saveAndFlush(t);
	}

	public T findone(PK id) {
		// TODO Auto-generated method stub
		return service.findone(id);
	}

	public List<T> findAll(){
		return service.findAll();
	}
	public void delete(PK id) {
		service.delete(id);
	}
	
	public String list(String pageNoStr, HttpServletRequest request) {
		// 1. 得到一 search_ 开头的请求参数的 Map.
		// key: 去除了前缀的请求参数的名字,value: 输入的值
		Map<String, Object> params = WebUtils.getParametersStartingWith(
				request, "search_");

		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {
		}

		// 2. 调用 Service 方法, 得到 Page 对象
		Page<T> page = service.getPage(pageNo - 1, 5, params);

		// 3. 把 Page 对象放入到 request 中
		request.setAttribute("page", page);

		// 4. 把 1 的 Map 在反序列化为一个查询的字符串, 传回到页面上.
		String queryString = encodeParameterStringWithPrefix(params, "search_");
		request.setAttribute("searchParams", queryString);

		return null;
	}

	public static String encodeParameterStringWithPrefix(
			Map<String, Object> params, String prefix) {
		if ((params == null) || (params.size() == 0)) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append(entry.getKey())
					.append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}

}
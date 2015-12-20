package com.meng.crm.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.meng.crm.entity.Contact;
import com.meng.crm.entity.Customer;
import com.meng.crm.entity.Dict;
import com.meng.crm.orm.Page;
import com.meng.crm.service.CustomerService;

@RequestMapping(value = "/customer")
@Controller
public class CustomerHandler {

	@Autowired
	CustomerService customerService;

	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "pageNo", required = false) String pageNoStr,
			Map<String, Object> map, HttpServletRequest request) {

		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {
		}

		int pageSize = 4;
		// 获取请求参数中的"search_"为前缀的字符串,去除了前缀
		Map<String, Object> params = WebUtils.getParametersStartingWith(
				request, "filter_");

		Page<Customer> page = customerService.findAll(pageNo, pageSize, params);
		//List<Customer> content = page.getContent();

		List<Dict> levels = customerService.findLevels();
		List<Dict> regions = customerService.findRegions();
		map.put("levels", levels);
		map.put("regions", regions);
		map.put("page", page);

		// 将请求参数中的字符串转换查询条件,传呼页面,防止翻页是将查询条件弄丢
		String queryString = parserParamsToQueryString(params);

		map.put("queryString", queryString);
		return "customer/list";
	}

	private String parserParamsToQueryString(Map<String, Object> params) {

		if (params != null && params.size() > 0) {
			StringBuilder queryString = new StringBuilder();

			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				queryString.append("filter_").append(key).append("=")
						.append(value).append("&");
			}
			return queryString.replace(queryString.length() - 1,
					queryString.length(), "").toString();
		}

		return "";
	}
}

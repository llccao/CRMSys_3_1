package com.meng.crm.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.meng.crm.entity.CustomerDrain;
import com.meng.crm.orm.Page;
import com.meng.crm.service.CustomerDrainService;

@RequestMapping("/drain")
@Controller
public class CustomerDrainHandler {

	@Autowired
	CustomerDrainService customerDrainService;

	/*@ModelAttribute
	public void getCustomerDrain(@RequestParam(value="id",required=false) Integer id,
			Map<String,Object> map) {
		if(id != null) {
			CustomerDrain drain = customerDrainService.getDrainById(id);
			map.put("drain", drain);
		}
	}*/
	
	//添加流失原因
	@RequestMapping(value="/confirm/{id}",method=RequestMethod.POST)
	public String updateReason(@PathVariable("id")Integer id,@Param("reason")String reason){
		CustomerDrain drain = customerDrainService.getDrainById(id);
		drain.setReason(reason);
		drain.setStatus("流失");
		drain.setDrainDate(new Date());
		customerDrainService.updateCustomerDrain(drain);
		
		return "redirect:/drain/list";
	}
	//更改暂缓措施
	@RequestMapping(value="/delay/{id}",method=RequestMethod.POST)
	public String updateDelay(@PathVariable("id")Integer id,@Param("delay")String delay){
		CustomerDrain drain = customerDrainService.getDrainById(id);
		
		String delays=drain.getDelay()+"`"+delay;
		drain.setDelay(delays);
		customerDrainService.updateCustomerDrain(drain);	
		
		return "redirect:/drain/delay/"+id;
	}
	
	
	//去追加暂缓措施的页面
	@RequestMapping("/delay/{id}")
	public String delay(@PathVariable("id")Integer id,Map<String,Object> map){
		CustomerDrain drain = customerDrainService.getDrainById(id);
		map.put("drain", drain);
		return "drain/delay";
	}
	//去添加流失原因的页面
	@RequestMapping("/confirm/{id}")
	public String confirm(@PathVariable("id")Integer id,Map<String,Object> map){
		CustomerDrain drain = customerDrainService.getDrainById(id);
		map.put("drain", drain);
		return "drain/confirm";
	}
	
	//带查询条件的分页
	@RequestMapping("/list")
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

		Page<CustomerDrain> page = customerDrainService.findAll(pageNo,
				pageSize, params);
		// List<Customer> content = page.getContent();
		map.put("page", page);

		// 将请求参数中的字符串转换查询条件,传呼页面,防止翻页是将查询条件弄丢
		String queryString = parserParamsToQueryString(params);

		map.put("queryString", queryString);
		
		return "drain/list";
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

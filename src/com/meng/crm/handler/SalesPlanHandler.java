package com.meng.crm.handler;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.meng.crm.entity.SalesChance;
import com.meng.crm.entity.SalesPlan;
import com.meng.crm.orm.Page;
import com.meng.crm.service.SalesChanceService;
import com.meng.crm.service.SalesPlanService;

@RequestMapping("/plan")
@Controller
public class SalesPlanHandler {
	
	@Autowired
	private SalesPlanService salesPlanService;
	@Autowired
	private SalesChanceService salesChanceService;

	
	/**
	 * 开发成功或失败后,最后一个td要显示的就要改成查看这个销售机会的具体信息,
	 * 此方法就用于将某个销售机会的具体信息返回到jsp
	 * @ms
	 */
	
	@RequestMapping("/detail")
	public String detail(@RequestParam(value="id") Integer id, Map<String,Object> map) {
		
		SalesChance chance = salesChanceService.getById(id);
		
		map.put("chance", chance);
		
		return "plan/detail";
	}
	
	
	@RequestMapping(value="/make",method=RequestMethod.GET)
	public String makePlan(@RequestParam("id") String idStr,Map<String,Object> map){
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) { }
		
		map.put("chance", salesChanceService.getById(id));
		
		return "plan/make";
	}
	@RequestMapping(value="/execution",method=RequestMethod.GET)
	public String toExecution(@RequestParam("id") String idStr,Map<String,Object> map){
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) { }
		
		map.put("chance", salesChanceService.getById(id));
		
		return "plan/execute";
	}
	
	@RequestMapping("/make")
	public String toMake(@RequestParam("id") String idStr,Map<String,Object> map) {
		
		int id = 0;
		
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) { }
		
		map.put("chance", salesChanceService.getById(id));
		
		return "plan/make";
	}
	
	@RequestMapping("/chance/list")
	public String list(@RequestParam(value="pageNo",required=false) String pageNoStr,
			Map<String,Object> map,HttpServletRequest request) {
		
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) { }
		
		int pageSize = 4;
		// 获取请求参数中的"filter_"为前缀的字符串,去除了前缀
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "filter_");
		
		Page<SalesChance> page = salesPlanService.getAll(pageNo, pageSize, params);
		map.put("page", page);
		
		// 将请求参数中的字符串转换查询条件,传呼页面,防止翻页是将查询条件弄丢
		String queryString = parserParamsToQueryString(params);
		
		map.put("queryString", queryString);
		
		return "plan/list";
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
	
	//停止计划
	@RequestMapping(value="/stop/{id}",method=RequestMethod.PUT)
	public String stop(@PathVariable("id")Integer id, RedirectAttributes attributes) {
		salesPlanService.stop(id);
		attributes.addFlashAttribute("message", "已经终止");
		return "redirect:/plan/chance/list";
	}
	//查询页面
	@RequestMapping("/detail/{id}")
	public String selectSalesPlan(@PathVariable("id")Integer id,
			Map<String,Object> map,HttpServletRequest request) {
		
		SalesChance chance =salesChanceService.getById(id);
		List<SalesPlan> list= salesPlanService.getSalesPlanById(id);
		
		map.put("chance", chance);
		map.put("list",list );
		
		return "plan/detail";
	}
}

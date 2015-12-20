package com.meng.crm.handler.jpa;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.meng.crm.entity.Dict;
import com.meng.crm.service.jpa.DictService;

@Controller
@RequestMapping("/dict")
public class DictHandler extends BaseHandler<Dict, Long> {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", required = false, defaultValue = "1") String pageNoStr,
			HttpServletRequest request) {
		super.list(pageNoStr, request);
		
		return "dict/list";
	}

	@RequestMapping(value ="/create/{id}", method=RequestMethod.GET)
	public String input(@PathVariable("id")Integer id, Map<String, Object> map) {
			map.put("dict", super.findone(Long.valueOf(id)));
			return "dict/input";
	}
	@RequestMapping(value ="/create", method=RequestMethod.GET)
	public String input2(Map<String, Object> map) {
		
			map.put("dict", new Dict());
			return "dict/input";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String inputDict(Dict dict) {

		super.saveAndFlush(dict);

		return "redirect:/dict/list";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id")Integer id,
			RedirectAttributes attributes) {
		super.delete(Long.valueOf(id));
		attributes.addFlashAttribute("message", "删除成功!");
		return "redirect:/dict/list";
	}

}

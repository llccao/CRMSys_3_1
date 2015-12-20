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

import com.meng.crm.entity.Product;

@Controller
@RequestMapping("/product")
public class ProductHandler extends BaseHandler<Product, Long> {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", required = false, defaultValue = "1") String pageNoStr,
			HttpServletRequest request) {
		super.list(pageNoStr, request);

		return "product/list";
	}

	@RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
	public String input(@PathVariable("id") Integer id, Map<String, Object> map) {
		map.put("product", super.findone(Long.valueOf(id)));
		return "product/input";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String input2(Map<String, Object> map) {

		map.put("product", new Product());
		return "product/input";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String inputproduct(Product product) {

		super.saveAndFlush(product);

		return "redirect:/product/list";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id,
			RedirectAttributes attributes) {
		try {
			super.delete(Long.valueOf(id));
		} catch (Exception e) {
			attributes.addFlashAttribute("message", "还有库存！不能删除!");
			return "redirect:/product/list";
		}
		
		attributes.addFlashAttribute("message", "删除成功!");
		return "redirect:/product/list";
	}

}

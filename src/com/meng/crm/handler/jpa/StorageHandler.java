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

import com.meng.crm.entity.Storage;
import com.meng.crm.service.jpa.ProductService;

@Controller
@RequestMapping("/storage")
public class StorageHandler extends BaseHandler<Storage, Long> {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", required = false, defaultValue = "1") String pageNoStr,
			HttpServletRequest request) {
		super.list(pageNoStr, request);

		return "storage/list";
	}

	@RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
	public String input(@PathVariable("id") Long id, Map<String, Object> map) {
		map.put("storage", super.findone(Long.valueOf(id)));

		return "storage/input";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String input2(Map<String, Object> map) {

		map.put("products", productService.findAll());
		map.put("storage", new Storage());
		return "storage/input";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String inputstorage(@RequestParam("stockCount") Integer stockCount,
			Storage storage) {

		int count = storage.getStockCount();
		count += stockCount;
		storage.setStockCount(count);
		super.saveAndFlush(storage);

		return "redirect:/storage/list";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id,
			RedirectAttributes attributes) {
		super.delete(Long.valueOf(id));
		attributes.addFlashAttribute("message", "删除成功!");
		return "redirect:/storage/list";
	}

}

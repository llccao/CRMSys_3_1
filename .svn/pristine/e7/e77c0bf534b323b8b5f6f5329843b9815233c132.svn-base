package com.meng.crm.orm;

import java.util.List;

public class Page<T> {
	
	private int pageSize = 2;
	private int pageNo = 1;
	
	private int totalPage;
	private long totalElements;
	
	List<T> content;

	public boolean isHasPrev() {
		return (pageNo > 1);
	}
	
	public boolean isHasNext() {
		return (pageNo < totalPage);
	}
	
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	
	public long getTotalElements() {
		return totalElements;
	}
	
	public int getPageSize() {
		
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		
		return pageNo;
	}

	public void setPageNo(int pageNo) {

		if(pageNo < 1) {
			pageNo = 1;
		}
		
		this.pageNo = pageNo;
	}

	public int getTotalPage() {
		
		totalPage = (int) (getTotalElements() % getPageSize() == 0 ? getTotalElements() / getPageSize():(getTotalElements() / getPageSize() + 1));
		
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		
		if(pageNo > totalPage) {
			pageNo = totalPage;
		}
		
		this.totalPage = totalPage;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	
	
	
}

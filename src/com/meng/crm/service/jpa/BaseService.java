package com.meng.crm.service.jpa;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.meng.crm.dao.jpa.BaseRepository;
import com.meng.crm.entity.Dict;
import com.meng.crm.orm.PropertyFilter;
import com.meng.crm.orm.PropertyFilter.MatchType;
import com.meng.crm.utils.ReflectionUtils;

public class BaseService<T, PK extends Serializable> {

	@Autowired
	private BaseRepository<T, PK> repository;

	public void saveAndFlush(T t) {
		repository.saveAndFlush(t);
	}

	public List<T> findAll(){
		return repository.findAll();
	}
	
	public T findone(PK id) {
		// TODO Auto-generated method stub
		return repository.findOne(id);
	}

	public void delete(PK id) {
		 repository.delete(id);
	}
	
	
	
	public Page<T> getPage(int pageNo, int pageSize, Map<String, Object> params) {
		//1. 创建 PageRequest 对象
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		
		//2. 把传入的 params 转为 PropertyFilter 的集合
		List<PropertyFilter> filters = PropertyFilter.parseParamsToFilters(params);
		
		//3. 把 PropertyFilter 的集合转为 Specification 对象
		Specification<T> specification = parsePropertyFilterToSpecification(filters);
		
		//4. 调用方法返回 Page.
		return repository.findAll(specification, pageable);
	}
	
	public Specification<T> parsePropertyFilterToSpecification(
			final List<PropertyFilter> filters) {
		//Specification: SpringData 中封装了查询条件的接口. 
		Specification<T> specification = new Specification<T>() {
			//Predicate: JPA Criteria 查询中的查询条件
			//Root: 实际查询的实体类, 可以导航到要查询的属性上.
			//CriteriaBuilder: JPA Criteria 查询的静态工厂类. 可以从中获取到 JPA Criteria 查询的很多实例
			@Override
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate result = null;
				
				if(filters != null && filters.size() > 0){
					List<Predicate> predicates = new ArrayList<>();
					
					for(PropertyFilter filter: filters){
						Predicate predicate = null;
						
						//可能是级联属性: 例如: Customer 的 manager.name 属性. 
						String propertyName = filter.getPropertyName();
						//得到 JPA 中的 Expression. 以备后用
						String [] propertyNames = propertyName.split("\\.");
						Path expression = root.get(propertyNames[0]);
						if(propertyNames.length > 1){
							for(int i = 1; i < propertyNames.length; i++){
								expression = expression.get(propertyNames[i]);
							}
						}
						
						//把输入的值转为实际的目标类型. 多见于字符串转 Date 类型. 
						Object propertyVal = filter.getPropertyValue();
						Class propertyType = filter.getPropertyType();
						propertyVal = ReflectionUtils.convertValue(propertyVal, propertyType);
						
						//根据匹配的类型, 来创建查询调节键
						MatchType matchType = filter.getMatchType();
						switch (matchType) {
						case EQ:
							predicate = builder.equal(expression, propertyVal);
							break;
						case GE:
							predicate = builder.ge(expression, (Number)propertyVal);
							break;
						case GT:
							predicate = builder.gt(expression, (Number)propertyVal);
							break;
						case LE:
							predicate = builder.le(expression, (Number)propertyVal);
							break;
						case LT:
							predicate = builder.lt(expression, (Number)propertyVal);
							break;
						case LIKE:
							predicate = builder.like(expression, "%" + propertyVal + "%");
							break;
						case ISNULL:
							predicate = builder.isNull(expression);
						default:
							break;
						}
						predicates.add(predicate);
					}
					
					return builder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				
				return result;
			}
		};
		
		return specification;
	}
}

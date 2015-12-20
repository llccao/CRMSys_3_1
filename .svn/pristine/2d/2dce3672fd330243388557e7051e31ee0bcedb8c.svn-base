package com.meng.crm.orm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("rawtypes")
public class PropertyFilter {
	
	public enum MatchType {
		EQ , GE , GT , LE , LT , LIKE , ISNULL ;
	}
	
	public enum PropertyType {
		
		S(String.class) ,I(Integer.class) ,L(Long.class) , D(Date.class);

		private Class propertyType;
		
		private PropertyType(Class propertyType ) {
			this.propertyType = propertyType;
		}
		
		public Class getPropertyType() {
			return propertyType;
		}
	}

	private String propertyName;
	private Object propertyValue;
	
	private MatchType matchType;
	private Class propertyType;
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Object getPropertyValue() {
		return propertyValue;
	}
	
	public MatchType getMatchType() {
		return matchType;
	}
	
	public Class getPropertyType() {
		return propertyType;
	}

	public PropertyFilter(String propertyName, Object propertyValue,
			MatchType matchType, Class propertyType) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.matchType = matchType;
		this.propertyType = propertyType;
	}
	
	public static List<PropertyFilter> parseParamsToFilters (Map<String ,Object> params) {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		if(params != null && params.size() > 0) {
			
			for(Map.Entry<String,Object> entry:params.entrySet()) {
				
				String key = entry.getKey();// LIKES_loginName
				Object propertyValue = entry.getValue();
				
				// 拆分key获取具体的类型
				String str1 = StringUtils.substringBefore(key, "_"); //LIKES
				// LIKE
				String matchTypeCode = str1.substring(0, str1.length() - 1);
				// S
				String propertyTypeCode = str1.substring(str1.length() - 1);
				
				// 将其转换成对应的枚举类型
				MatchType matchType = Enum.valueOf(MatchType.class, matchTypeCode);
				Class propertyType = Enum.valueOf(PropertyType.class, propertyTypeCode).getPropertyType();
				
				// 获取真正的属性名 loginName
				String propertyName = StringUtils.substringAfter(key, "_");
				
				PropertyFilter filter = 
						new PropertyFilter(propertyName, propertyValue, matchType, propertyType); 
				
				filters.add(filter);
			}
		}
		return filters;
	}
}

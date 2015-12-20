<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>客户基本信息管理</title>
</head>
<body>

	<div class="page_title">客户基本信息管理</div>
	<div class="button_bar">
		<button class="common_button" onclick="document.forms[0].submit();">查询</button>
	</div>
	
	<form action="${ctp }/customer/list" method="POST">
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>客户名称</th>
				<td>
					<input type="text" name="filter_LIKES_NAME"/>
				</td>
				<th>地区</th>
				<td>
					<select name="filter_EQS_region">
						<option value="">全部</option>
						<c:forEach items="${regions }" var="Dict">
							<option value="${Dict.item }">${Dict.item  }</option>
						</c:forEach>
					</select>
				</td>
				<th>&nbsp;</th>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<th>客户经理</th>
				<td><input type="text" name="filter_LIKES_manager.name" /></td>
				
				<th>客户等级</th>
				<td>
					<select name="filter_EQS_level">
						<option value="">全部</option>
						<c:forEach items="${levels }" var="level">
							<option value="${level.item}">${level.item}</option>
						</c:forEach>
					</select>
				</td>
				
				<th>状态</th>
				<td>
					<select name="filter_EQS_state">
						<option value="">全部</option>
						<option value="正常">正常</option>
						<option value="流失">流失</option>
						<option value="删除">删除</option>					
					</select>
				</td>
			</tr>
		</table>
		
		<!-- 列表数据 -->
		<br />
		
		<c:if test="${page != null && page.totalElements > 0 }">
			<table class="data_list_table" border="0" cellPadding="3"
				cellSpacing="0">
				<tr>
					<th>客户编号</th>
					<th>客户名称</th>
					<th>地区</th>
					<th>客户经理</th>
					<th>客户等级</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				
				<c:forEach var="customer" items="${page.content }">
					<tr>
						<td class="list_data_text">${customer.no }&nbsp;</td>
						<td class="list_data_ltext">${customer.name }&nbsp;</td>
						<td class="list_data_text">${customer.region }&nbsp;</td>
						<td class="list_data_text">${customer.manager.name }&nbsp;</td>
						<td class="list_data_text">${customer.level }&nbsp;</td>
						<td class="list_data_text">${customer.state}&nbsp;</td>
						<td class="list_data_op">
							<img onclick="window.location.href='${ctp}/customer/create?id=${customer.id}'"
								title="编辑" src="${ctp }/static/images/bt_edit.gif" class="op_button" alt="" /> 
							<img onclick="window.location.href='${ctp}/contact/list?customerid=${customer.id }'"
								title="联系人" src="${ctp }/static/images/bt_linkman.gif" class="op_button" alt="联系人信息" /> 
							<img onclick="window.location.href='${ctp}/activity/list?customerid=${customer.id }'"
								title="交往记录" src="${ctp }/static/images/bt_acti.gif" class="op_button" alt="交往记录" /> 
							<img onclick="window.location.href='${ctp}/order/list?customerid=${customer.id }'"
								title="历史订单" src="${ctp }/static/images/bt_orders.gif" class="op_button" alt="历史订单" /> 
								<c:if test="${customer.state != '删除' }">
									<img onclick="window.location.href='${ctp}/customer/delete?id=${customer.id}'" 
									title="删除" src="${ctp }/static/images/bt_del.gif" class="op_button" alt="删除" />
								</c:if>
							</td>					
					</tr>
				</c:forEach>
			</table>
			<div style="text-align:right; padding:6px 6px 0 0;">
				共 ${page.totalElements } 条记录 
				&nbsp;&nbsp;
				当前第 ${page.pageNo } 页/共 ${page.totalPage } 页
				&nbsp;&nbsp;
					<c:if test="${page.hasPrev }">
						<a href='${ctp }/customer/list?pageNo=1&${queryString }'>首页</a>
						&nbsp;&nbsp;
						<a href='${ctp }/customer/list?pageNo=${page.pageNo - 1}&${queryString }'>上一页</a>
						&nbsp;&nbsp;
					</c:if>
					
					<c:if test="${page.hasNext }">
						<a href='${ctp }/customer/list?pageNo=${page.pageNo + 1}&${queryString }'>下一页</a>
						&nbsp;&nbsp;
						<a href='${ctp }/customer/list?pageNo=${page.totalPage}&${queryString }'>末页</a>
						&nbsp;&nbsp;
					</c:if>
				转到 <input type="text" id="pageNo" size='1'/> 页
				&nbsp;&nbsp;
			
			</div>

<script type="text/javascript" src="${ctp}/static/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript">

	$(function(){
		
		$("#pageNo").change(function(){
			
			var pageNo = $(this).val();
			var reg = /^\d+$/;
			
			if(!reg.test(pageNo)){
				$(this).val("");
				alert("输入的页码不合法");
				return;
			}
			
			var pageNo2 = parseInt(pageNo);
			if(pageNo2 < 1 || pageNo2 > parseInt("${page.totalPage}")){
				$(this).val("");
				alert("输入的页码不合法");
				return;
			}
			
			//查询条件需要放入到 class='condition' 的隐藏域中.  
			window.location.href = "list?pageNo=" + pageNo2 + "&${queryString }";
			
		});
	})
</script>
		</c:if>
		<c:if test="${page == null || page.totalElements == 0 }">
			没有任何数据
		</c:if>
	</form>
	
</body>
</html>

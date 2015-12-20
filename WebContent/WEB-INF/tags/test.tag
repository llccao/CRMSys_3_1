<%@ tag import="java.util.Date"%>
<%@ tag language="java" pageEncoding="UTF-8"%>

<!-- 设置标签体的类型 -->
<%@ tag body-content="scriptless" %>

<!-- 设置标签属性 -->
<!--  
rtexprvalue: rt: runtime, expr: expression, value. 即是否可以接受 EL 表达式的值
-->
<%@ attribute name="count" required="true" rtexprvalue="true" type="java.lang.Integer" %>

Hello World, Time: <%= new Date() %>
<br><br>

<!-- 使用标签的方式输出标签体的内容 -->
Tag Body Content: <jsp:doBody></jsp:doBody>
<br><br>


<%
	for(int i = 0; i < count; i++){
%>
	<!-- 调用 SimpleTagSupport 继承的方法来输出标签体的内容 -->
	Tag Body Content: <% getJspBody().invoke(out);  %><br>
<%		
	}
%>
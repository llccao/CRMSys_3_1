<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 2.说一下struts2-hibernate-Spring 的工作流程？
·
1). Struts2 负责接受请求、渲染结果. 
2). Spring 的 IOC 容器管理各个组件: 整合 Struts2，Hibernate 和 其他组件，AOP 完成声明式事务
3). Hibernate 提供 DAO 操作.

SpringMVC、Struts2
Spring
JDBC(dbutils, Spring JdbcTemplate)、Hibernate、JPA\SpringData、myBatis

------------------------------------------------------------

3. Struts2 面试问题: 

1). 简述 Struts2 的工作流程: 

①. 请求发送给 StrutsPrepareAndExecuteFilter
②. StrutsPrepareAndExecuteFilter 判定该请求是否是一个 Struts2 请求
③. 若该请求是一个 Struts2 请求，则 StrutsPrepareAndExecuteFilter 把请求的处理交给 ActionProxy
④. ActionProxy 创建一个 ActionInvocation 的实例，并进行初始化
⑤. ActionInvocation 实例在调用 Action 的过程前后，涉及到相关拦截器（Intercepter）的调用。
⑥. Action 执行完毕，ActionInvocation 负责根据 struts.xml 中的配置找到对应的返回结果。调用结果的 execute 方法，渲染结果。
⑦. 执行各个拦截器 invocation.invoke() 之后的代码
⑧. 把结果发送到客户端

2). Struts2 拦截器 和 过滤器 的区别：

①、过滤器依赖于Servlet容器，而拦截器不依赖于Servlet容器。
②、Struts2 拦截器只能对 Action 请求起作用，而过滤器则可以对几乎所有请求起作用。
③、拦截器可以访问 Action 上下文(ActionContext)、值栈里的对象(ValueStack)，而过滤器不能. 
④、在 Action 的生命周期中，拦截器可以多次调用，而过滤器只能在容器初始化时被调用一次。

3). 为什么要使用 Struts2 & Struts2 的优点：

①. 基于 MVC 架构，框架结构清晰。
②. 使用 OGNL: OGNL 可以快捷的访问值栈中的数据、调用值栈中对象的方法
③. 拦截器: Struts2 的拦截器是一个 Action 级别的 AOP, Struts2 中的许多特性都是通过拦截器来实现的,
 例如异常处理，文件上传，验证等。拦截器是可配置与重用的
④. 多种表现层技术. 如：JSP、FreeMarker、Velocity 等

4). Struts2 如何访问 HttpServletRequest、HttpSession、ServletContext 三个域对象 ?

①. 与 Servlet API 解耦的访问方式

	> 通过 ActionContext 访问域对象对应的 Map 对象
	> 通过实现 Aware 接口使 Struts2 注入对应的 Map 对象

②.  与 Servlet API 耦合的访问方式
	> 通过 ServletActionContext 直接获取 Servlet API 对象
	> 通过实现 ServletXxxAware 接口的方式使 Struts2 注入对应的对象

5). Struts2 中的默认包 struts-default 有什么作用？

①. struts-default 包是 struts2 内置的，它定义了 struts2 内部的众多拦截器和 Result 类型，
        而 Struts2 很多核心的功能都是通过这些内置的拦截器实现，
        如：从请求中把请求参数封装到action、文件上传和数据验证等等都是通过拦截器实现的。
        当包继承了struts-default包才能使用struts2为我们提供的这些功能。   
②. struts-default 包是在 struts-default.xml 中定义，struts-default.xml 也是 Struts2 默认配置文件。 
   Struts2 每次都会自动加载 struts-default.xml文件。  
③. 通常每个包都应该继承 struts-default 包。 

6). 说出 struts2 中至少 5 个的默认拦截器

exception；fileUpload；i18n；modelDriven；params；prepare；token；tokenSession；validation 等 

7). 谈谈 ValueStack：

①. ValueStack 贯穿整个 Action 的生命周期，保存在 request 域中，所以 ValueStack 和 request 的生命周期一样. 
       当 Struts2 接受一个请求时，会迅速创建 ActionContext，ValueStack，Action. 
       然后把 Action 存放进 ValueStack，所以 Action 的实例变量可以被 OGNL 访问。 
       请求来的时候，Action、ValueStack 的生命开始；请求结束，Action、ValueStack的生命结束  
②. 值栈是多实例的，因为Action 是多例的(和 Servlet 不一样，Servelt 是单例的)，
        而每个 Action 都有一个对应的值栈，Action 对象默认保存在栈顶；  
③. ValueStack 本质上就是一个 ArrayList(查看源代码得到)；  
④. 使用 OGNL 访问值栈的内容时，不需要#号，而访问 request、session、application、attr 时，需要加#号；  
⑤. Struts2 重写了 request 的 getAttribute 方法，所以可以使用 EL 直接访问值栈中的内容

8). ActionContext、ServletContext、pageContext的区别 ？

①. ActionContext Struts2 的 API：是当前的 Action 的上下文环境
②. ServletContext 和 PageContext 是 Servlet 的 API

9). Struts2 有哪几种结果类型 ?

参看 struts-default.xml 中的相关配置：dispatcher、chain、redirect 等.

10). 拦截器的生命周期与工作过程 ?

①. 每个拦截器都是需要实现 Interceptor 接口  
	> init()：在拦截器被创建后立即被调用, 它在拦截器的生命周期内只被调用一次. 可以在该方法中对相关资源进行必要的初始化；  
	> intercept(ActionInvocation invocation)：每拦截一个动作请求，该方法就会被调用一次；  
	> destroy：该方法将在拦截器被销毁之前被调用, 它在拦截器的生命周期内也只被调用一次；  

11). 如何在 Struts2 中使用 Ajax 功能 ?

①. JSON plugin
②. DOJO plugin  
③. DWR plugin
④. 使用 Stream 结果类型. 

------------------------------------------------------------

4. Hibernate 面试问题：

1). Hibernate 的检索方式有哪些 ?

① 导航对象图检索  
② OID检索  
③ HQL检索  
④ QBC检索  
⑤ 本地SQL检索

2). 在 Hibernate 中 Java 对象的状态有哪些 ？
①. 临时状态（transient）：不处于 Session 的缓存中。OID 为 null 或等于 id 的 unsaved-value 属性值
②. 持久化状态（persistent）：加入到 Session 的缓存中。
③. 游离状态（detached）：已经被持久化，但不再处于 Session 的缓存中。

3). Session的清理和清空有什么区别？
清理缓存调用的是 session.flush() 方法. 而清空调用的是 session.clear() 方法.
Session 清理缓存是指按照缓存中对象的状态的变化来同步更新数据库，但不清空缓存；清空是把 Session 的缓存置空, 但不同步更新数据库；

4). load()和get()的区别  

①：如果数据库中，没有 OID 指定的对象。通过 get方法加载，则返回的是一个null；通过load加载，则返回一个代理对象，
如果后面代码如果调用对象的某个属性会抛出异常：org.hibernate.ObjectNotFoundException； 
②：load 支持延迟加载，get 不支持延迟加载。 

5). hibernate 优缺点

①. 优点:
	> 对 JDBC 访问数据库的代码做了封装，简化了数据访问层繁琐的重复性代码
	> 映射的灵活性, 它支持各种关系数据库, 从一对一到多对多的各种复杂关系. 
	> 非侵入性、移植性会好
	> 缓存机制: 提供一级缓存和二级缓存

②. 缺点:
	> 无法对 SQL 进行优化
	> 框架中使用 ORM 原则, 导致配置过于复杂	
	> 执行效率和原生的 JDBC 相比偏差: 特别是在批量数据处理的时候
	> 不支持批量修改、删除

6). 描述使用 Hibernate 进行大批量更新的经验.

直接使用 hibernate API 进行批量更新和批量删除都不推荐

	> 执行 UPDATE 操作不会把该对象纳入到 Session 缓存中! 但无法对更新提供定制的批量操作
	> 把所有的对象都纳入到一级缓存中, 逐个取出, 逐个更新. 若对象很多, 会使一级缓存溢出!

而直接通过 JDBC API 执行相关的 SQl 语句或调用相关的存储过程是最佳的方式

7). Hibernate 的 OpenSessionInView 问题

①. 用于解决懒加载异常, 主要功能就是把 Hibernate Session 和一个请求的线程绑定在一起, 直到页面完整输出, 
这样就可以保证页面读取数据的时候 Session 一直是开启的状态, 如果去获取延迟加载对象也不会报错。
②. 问题: 如果在业务处理阶段大批量处理数据, 有可能导致一级缓存里的对象占用内存过多导致内存溢出, 另外一个是连接问题:  
Session 和数据库 Connection 是绑定在一起的, 如果业务处理缓慢也会导致数据库连接得不到及时的释放, 造成连接池连接不够. 所以在并发量
较大的项目中不建议使用此种方式, 可以考虑使用迫切左外连接 (LEFT OUTER JOIN FETCH) 或手工对关联的对象进行初始化. 
③. 配置 Filter 的时候要放在 Struts2 过滤器的前面, 因为它要页面完全显示完后再退出.
 
8). Hibernate 中 getCurrentSession() 和 openSession() 的区别 ? 

①. getCurrentSession() 它会先查看当前线程中是否绑定了 Session, 如果有则直接返回, 如果没有再创建. 
而openSession() 则是直接 new 一个新的 Session 并返回。
②. 使用 ThreadLocal 来实现线程 Session 的隔离。
③. getCurrentSession() 在事务提交的时候会自动关闭 Session, 而 openSession() 需要手动关闭.

9). Hibernate 批量操作

①. 批量新增: 一次新增大批量对象, 避免一级缓存内存溢出, 可以定时 flush() 之后再 clear() 掉, 
②. 批量更新: 可以通过 hql 来批量更新
③. 使用无状态 Session 接口, 它不会实现一二级缓存查询缓存交互, 查询后的对象立即处于游离态, 
通过 update 或者 delete 可以操作并同步到数据库中
④. 使用原生的 SQL 或存储过程

10). 如何调用原生 SQL ?

调用 Session 的 doWork() 方法.

------------------------------------------------------------

5. Spring 面试题

1). 开发中主要使用 Spring 的什么技术 ?

①. IOC 容器管理各层的组件
②. 使用 AOP 配置声明式事务
③. 整合其他框架.

2). 简述 AOP 和 IOC 概念

AOP: Aspect Oriented Program, 面向(方面)切面的编程;
     Filter(过滤器)也是一种 AOP. 
     AOP 是一种新的方法论, 是对传统 OOP(Object-Oriented Programming, 面向对象编程) 的补充.
     AOP 的主要编程对象是切面(aspect), 而切面模块化横切关注点.
            举例通过事务说明. 
     
IOC: Invert Of Control, 控制反转. 也成为 DI(依赖注入)
            其思想是反转资源获取的方向. 传统的资源查找方式要求组件向容器发起请求查找资源. 
            作为回应, 容器适时的返回资源. 而应用了 IOC 之后, 则是容器主动地将资源推送给它所管理的组件, 
            组件所要做的仅是选择一种合适的方式来接受资源. 这种行为也被称为查找的被动形式

3). 在 Spring 中如何配置 Bean ?

Bean 的配置方式: 通过全类名（反射）、通过工厂方法（静态工厂方法 & 实例工厂方法）、FactoryBean

4). IOC 容器对 Bean 的生命周期:
①. 通过构造器或工厂方法创建 Bean 实例
②. 为 Bean 的属性设置值和对其他 Bean 的引用
③. 将 Bean 实例传递给 Bean 后置处理器的 postProcessBeforeInitialization 方法
④. 调用 Bean 的初始化方法(init-method)
⑤. 将 Bean 实例传递给 Bean 后置处理器的 postProcessAfterInitialization方法
⑦. Bean 可以使用了
⑧. 当容器关闭时, 调用 Bean 的销毁方法(destroy-method)

5). Spring 如何整合 Struts2 ?

整合 Struts2, 即由 IOC 容器管理 Struts2 的 Action:

	> 安装 Spring 插件: 把 struts2-spring-plugin-2.2.1.jar 复制到当前 WEB 应用的 WEB-INF/lib 目录下
	> 在 Spring 的配置文件中配置 Struts2 的 Action 实例
	> 在 Struts 配置文件中配置 action, 但其 class 属性不再指向该 Action 的实现类, 而是指向 Spring 容器中 Action 实例的 ID

6). Spring 如何整合 Hibernate

整合 Hibernate, 即由 IOC 容器生成 SessionFactory 对象, 并使用 Spring 的声明式事务

	> 利用 LocalSessionFactoryBean 工厂 Bean, 声明一个使用 XML 映射文件的 SessionFactory 实例.
	> 利用 HibernateTransactionManager 配置 Hibernate 的事务管理器

------------------------------------------------------------

6. Spring MVC 面试题

1). Spring MVC 比较 Struts2

①. Spring MVC 的入口是 Servlet, 而 Struts2 是 Filter
②. Spring MVC 会稍微比 Struts2 快些. Spring MVC 是基于方法设计, 而 Sturts2 是基于类, 每次发一次请求都会实例一个 Action.
③. Spring MVC 使用更加简洁, 开发效率Spring MVC确实比 struts2 高: 支持 JSR303, 处理 ajax 的请求更方便
④. Struts2 的 OGNL 表达式使页面的开发效率相比 Spring MVC 更高些.  

2). Spring MVC 的运行流程

①. 在整个 Spring MVC 框架中， DispatcherServlet 处于核心位置，负责协调和组织不同组件以完成请求处理并返回响应的工作
②. SpringMVC 处理请求过程：
	> 若一个请求匹配 DispatcherServlet 的请求映射路径(在 web.xml 中指定), 
	WEB 容器将该请求转交给 DispatcherServlet 处理
	> DispatcherServlet 接收到请求后, 将根据请求信息(包括 URL、HTTP 方法、请求头、请求参数、Cookie 等)及 
	HandlerMapping 的配置找到处理请求的处理器(Handler). 可将 HandlerMapping 看成路由控制器，将 Handler 看成目标主机。
	> 当 DispatcherServlet 根据 HandlerMapping 得到对应当前请求的 Handler 后，通过 HandlerAdapter 
	对 Handler 进行封装，再以统一的适配器接口调用 Handler。
	> 处理器完成业务逻辑的处理后将返回一个 ModelAndView 给 DispatcherServlet, 
	ModelAndView 包含了视图逻辑名和模型数据信息
	> DispatcherServlet 借助 ViewResoler 完成逻辑视图名到真实视图对象的解析
	> 得到真实视图对象 View 后, DispatcherServlet 使用这个 View 对 ModelAndView 中的模型数据进行视图渲染 

------------------------------------------------------------

如何在 Spring Data 自定义方法

7. Shiro 面试题: 

1). 比较 SpringSecurity 和 Shiro

	相比 Spring Security, Shiro 在保持强大功能的同时, 使用简单性和灵活性

	> SpringSecurity: 即使是一个一个简单的请求，最少得经过它的 8 个Filter
	> SpringSecurity 必须在 Spring 的环境下使用
	> 初学 Spring Security, 曲线还是较大, 需要深入学习其源码和框架, 配置起来也较费力.
	
2). Shiro 的优点

	> 简单的身份认证, 支持多种数据源
	> 对角色的简单的授权, 支持细粒度的授权(方法级)
	> 支持缓存，以提升应用程序的性能；
	> 内置的基于 POJO 企业会话管理, 适用于 Web 以及非 Web 的环境
	> 非常简单的加密 API
	> 不跟任何的框架或者容器捆绑, 可以独立运行	

3). 简述 Shiro 的核心组件

	Shiro 架构 3 个核心组件:
	
	> Subject: 正与系统进行交互的人, 或某一个第三方服务. 
	所有 Subject 实例都被绑定到（且这是必须的）一个SecurityManager 上。
	> SecurityManager: Shiro 架构的心脏, 用来协调内部各安全组件, 管理内部组件实例, 并通过它来提供安全管理的各种服务. 
	当 Shiro 与一个 Subject 进行交互时, 实质上是幕后的 SecurityManager 处理所有繁重的 Subject 安全操作。
	> Realms: 本质上是一个特定安全的 DAO. 当配置 Shiro 时, 必须指定至少一个 Realm 用来进行身份验证和/或授权. 
	Shiro 提供了多种可用的 Realms 来获取安全相关的数据. 如关系数据库(JDBC), INI 及属性文件等. 
	可以定义自己 Realm 实现来代表自定义的数据源。
	
4). 认证过程:

①. 应用程序代码调用 Subject.login 方法，传递创建好的包含终端用户的 Principals(身份)和 
Credentials(凭证)的 AuthenticationToken 实例

②. Subject 实例: 通常为 DelegatingSubject(或子类)委托应用程序的 SecurityManager 通过调用 
securityManager.login(token) 开始真正的验证。

③. SubjectManager 接收 token，调用内部的 Authenticator 实例调用 authenticator.authenticate(token).
Authenticator 通常是一个 ModularRealmAuthenticator 实例, 支持在身份验证中协调一个或多个Realm 实例

④. 如果应用程序中配置了一个以上的 Realm, ModularRealmAuthenticator 实例将利用配置好的 
AuthenticationStrategy 来启动 Multi-Realm 认证尝试. 在Realms 被身份验证调用之前, 期间和以后, 
AuthenticationStrategy 被调用使其能够对每个Realm 的结果作出反应.

⑤. 每个配置的 Realm 用来帮助看它是否支持提交的 AuthenticationToken. 
如果支持, 那么支持 Realm 的 getAuthenticationInfo 方法将会伴随着提交的 token 被调用. 
getAuthenticationInfo 方法有效地代表一个特定 Realm 的单一的身份验证尝试。

⑥. 此时可以提供 AuthenticatingRealm 的 doGetAuthenticationInfo 方法实现自定义的登录. 
	
5). 授权过程:

①. 应用程序或框架代码调用任何 Subject 的 hasRole*, checkRole*, isPermitted*,或者checkPermission*方法的变体, 
传递任何所需的权限

②. Subject 的实例—通常是 DelegatingSubject(或子类), 调用securityManager 的对应的方法. 

③. SecurityManager 调用 org.apache.shiro.authz.Authorizer 接口的对应方法. 
默认情况下，authorizer 实例是一个 ModularRealmAuthorizer 实例, 它支持协调任何授权操作过程中的一个或多个Realm 实例

④. 每个配置好的 Realm 被检查是否实现了相同的 Authorizer 接口. 
如果是, Realm 各自的 hasRole*, checkRole*,isPermitted*，或 checkPermission* 方法将被调用。

6). 如何自实现认证:

Shiro 的认证过程由 Realm 执行, SecurityManager 会调用 org.apache.shiro.realm.Realm 的 
getAuthenticationInfo(AuthenticationToken token) 方法. 
实际开发中, 通常提供 org.apache.shiro.realm.AuthenticatingRealm 的实现类, 
并在该实现类中提供 doGetAuthenticationInfo(AuthenticationToken token)方法的具体实现

7). 如何实现自实现授权:

实际开发中, 通常提供  org.apache.shiro.realm.AuthorizingRealm 的实现类, 
并提供 doGetAuthorizationInfo(PrincipalCollection principals) 方法的具体实现

6). 如何配置在 Spring 中配置使用 Shiro

①. 在 web.xml 中配置 Shiro 的 Filter
②. 在 Spring 的配置文件中配置  Shiro:
	> 配置自定义 Realm：实现自定义认证和授权
	> 配置 Shiro 实体类使用的缓存策略
	> 配置 SecurityManager
	> 配置保证 Shiro 内部 Bean 声明周期都得到执行的 Lifecycle Bean 后置处理器
	> 配置AOP 式方法级权限检查 

	> 配置 Shiro Filter

------------------------------------------------------------

8.   

①. 数据库集群
一般来说 MySQL 是最常用的, 可能最初是一个 mysql 主机, 当数据增加到 100 万以上, 
MySQL的效能急剧下降. 常用的优化措施是 M-S(主-从) 方式进行同步复制, 将查询和操作和分别在不同的服务器上进行操作. 
②. HTML 静态化: 效率最高、消耗最小的就是纯静态化的 html 页面, 所以要尽可能使的应用中的页面采用静态页面来实现. 
③. 做缓存集群、负载均衡、分布式存储。

------------------------------------------------------------

1. 项目相关：

1). 通常会问简历中写的项目，简历中可以写 3-4 个项目. 发项目模版. 

2). 具体问题：

①. 简述下你的项目.

I.   整体介绍，大致说明下项目所实现的功能、多少人、用了多少时间、什么技术、什么数据库

CRM 项目 - 客户关系管理系统。
3 个人做了 1 年。
SpringMVC、Spring、Shiro、Oracle

II.  介绍自己实现了哪些功能模块

主要负责 "系统权限管理" 和 "营销管理" 模块.

②. 项目有什么亮点 ? 

I.   全程注解
II.  SpringMVC 对比 Struts2 使用注解开发更便捷。而且在处理 Aajx， 文件上传、下载也更方便
III. 使用 Shiro 处理权限部分。相对于之前使用的 SpringSecurity 更容易上手。且配置也方便。而且支持方法级别的权限。
IV.  使用 Spring Data 做数据库层，提高了开发效率。

③. 项目中遇到困难，怎么办 ?

I.  如果是业务问题：问同事 -> 问项目经理, 由项目经理解决 -> 有时候也直接和产品经理沟通.

II. 技术问题：自己解决：Debug 测试, 查看部分源代码 -> 百度、Google -> 问技术较好的同事 -> 项目经理 -> 把问题记录下来，有时间整理

④. 项目用到了哪些技术 ? 或者说用到了哪些框架 ?

SpringMVC、Spring、JPA & SpringDATA、Shiro、Quartz、jQuery

⑤. 在该项目中有哪些提高或思考(不仅是技术上可能也包括团队上) ?

I.   新技术的引入：第一次使用 SpringMVC 和 Shiro。相比 Struts2 和 SpringSecurity 更轻量级，使用也更顺手。
            虽然开始时不是很适应。所以不能排斥新技术
II.  感觉时间安排的还不是很好。后期项目周期比较急，所以在和产品团队的沟通上有些问题。（因为需求总会发生修改）

⑥. 你在项目中的角色 ? 或者说你都做了什么 ?

I. 研发、测试、新技术问题的汇总和解决。

------------------------------------------------------------ -->
</body>
</html>
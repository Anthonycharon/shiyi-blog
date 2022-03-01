/*
 Navicat Premium Data Transfer

 Source Server         : 42.192.150.44_3306
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 42.192.150.44:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 21/02/2022 10:48:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_admin_log
-- ----------------------------
DROP TABLE IF EXISTS `b_admin_log`;
CREATE TABLE `b_admin_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求接口',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `operation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作名称',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `spend_time` bigint(20) NULL DEFAULT NULL COMMENT '请求接口耗时',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `params_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `class_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类地址',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 887 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_admin_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_article
-- ----------------------------
DROP TABLE IF EXISTS `b_article`;
CREATE TABLE `b_article`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类id',
  `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章封面地址',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章简介',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容 （最多两百字）',
  `content_md` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容md版',
  `is_secret` int(2) NULL DEFAULT 0 COMMENT '是否是私密文章 0 否 1是',
  `is_stick` int(2) NULL DEFAULT 0 COMMENT '是否置顶 0否 1是',
  `is_publish` int(10) NULL DEFAULT 0 COMMENT '是否发布 0：下架 1：发布',
  `is_original` int(10) NULL DEFAULT NULL COMMENT '是否原创  0：转载 1:原创',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转载地址',
  `quantity` bigint(6) NULL DEFAULT 0 COMMENT '文章阅读量',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '说明',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'seo关键词',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_article
-- ----------------------------
INSERT INTO `b_article` VALUES (5, 7, 17, 'gateway之动态路由', '\r\nhttp://r6aox1myz.hd-bkt.clouddn.com/1639990056972.jpg', '项目需求要根据一个请求动态生成gateway的路由，从而提供访问', '<h4><a id=\"DynamicRouteServiceImpl_ApplicationEventPublisherAware_1\"></a>创建DynamicRouteServiceImpl文件 并实现ApplicationEventPublisherAware。</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-meta\">@Slf4j</span>\n<span class=\"hljs-meta\">@Service</span>\n<span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">class</span> <span class=\"hljs-title class_\">DynamicRouteServiceImpl</span> <span class=\"hljs-keyword\">implements</span> <span class=\"hljs-title class_\">ApplicationEventPublisherAware</span> {\n\n    <span class=\"hljs-meta\">@Autowired</span>\n    <span class=\"hljs-keyword\">private</span> RouteDefinitionWriter routeDefinitionWriter;\n\n    <span class=\"hljs-keyword\">private</span> ApplicationEventPublisher publisher;\n\n    <span class=\"hljs-comment\">/**\n     * 添加路由实体类\n     *\n     * <span class=\"hljs-doctag\">@param</span> definition\n     * <span class=\"hljs-doctag\">@return</span>\n     */</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-type\">boolean</span> <span class=\"hljs-title function_\">add</span><span class=\"hljs-params\">(RouteDefinition definition)</span> {\n        routeDefinitionWriter.save(Mono.just(definition)).subscribe();\n        <span class=\"hljs-built_in\">this</span>.publisher.publishEvent(<span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">RefreshRoutesEvent</span>(<span class=\"hljs-built_in\">this</span>));\n        <span class=\"hljs-keyword\">return</span> <span class=\"hljs-literal\">true</span>;\n    }\n\n    <span class=\"hljs-comment\">/**\n     * <span class=\"hljs-doctag\">@param</span> definition 路由实体类\n     * <span class=\"hljs-doctag\">@return</span>\n     */</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-type\">boolean</span> <span class=\"hljs-title function_\">update</span><span class=\"hljs-params\">(RouteDefinition definition)</span> {\n        <span class=\"hljs-keyword\">try</span> {\n            routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();\n        } <span class=\"hljs-keyword\">catch</span> (Exception e) {\n            log.error(<span class=\"hljs-string\">&quot;update 失败。没有找到对应的路由ID :{}&quot;</span>, definition.getId());\n        }\n        routeDefinitionWriter.save(Mono.just(definition)).subscribe();\n        <span class=\"hljs-built_in\">this</span>.publisher.publishEvent(<span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">RefreshRoutesEvent</span>(<span class=\"hljs-built_in\">this</span>));\n        <span class=\"hljs-keyword\">return</span> <span class=\"hljs-literal\">true</span>;\n    }\n\n    <span class=\"hljs-comment\">/**\n     * serviceId\n     *\n     * <span class=\"hljs-doctag\">@param</span> id\n     * <span class=\"hljs-doctag\">@return</span>\n     */</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-type\">boolean</span> <span class=\"hljs-title function_\">del</span><span class=\"hljs-params\">(String id)</span> {\n        routeDefinitionWriter.delete(Mono.just(id)).subscribe();\n        <span class=\"hljs-built_in\">this</span>.publisher.publishEvent(<span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">RefreshRoutesEvent</span>(<span class=\"hljs-built_in\">this</span>));\n        <span class=\"hljs-keyword\">return</span> <span class=\"hljs-literal\">true</span>;\n    }\n\n    <span class=\"hljs-meta\">@Override</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">void</span> <span class=\"hljs-title function_\">setApplicationEventPublisher</span><span class=\"hljs-params\">(ApplicationEventPublisher applicationEventPublisher)</span> {\n        <span class=\"hljs-built_in\">this</span>.publisher = applicationEventPublisher;\n    }\n}\n\n</code></div></pre>\n<h4><a id=\"RefreshRouteServiceApplicationEventPublisherAware_59\"></a>新建RefreshRouteService文件并实现ApplicationEventPublisherAware</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-meta\">@Component</span>\n<span class=\"hljs-meta\">@Slf4j</span>\n<span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">class</span> <span class=\"hljs-title class_\">RefreshRouteService</span> <span class=\"hljs-keyword\">implements</span> <span class=\"hljs-title class_\">ApplicationEventPublisherAware</span> {\n\n    <span class=\"hljs-keyword\">private</span> ApplicationEventPublisher publisher;\n\n    <span class=\"hljs-meta\">@Override</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">void</span> <span class=\"hljs-title function_\">setApplicationEventPublisher</span><span class=\"hljs-params\">(ApplicationEventPublisher publisher)</span> {\n        <span class=\"hljs-built_in\">this</span>.publisher = publisher;\n    }\n\n    <span class=\"hljs-comment\">/**\n     * 刷新路由表\n     */</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">void</span> <span class=\"hljs-title function_\">refreshRoutes</span><span class=\"hljs-params\">()</span> {\n        publisher.publishEvent(<span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">RefreshRoutesEvent</span>(<span class=\"hljs-built_in\">this</span>));\n    }\n}\n\n</code></div></pre>\n<h4><a id=\"GatewayRoutesController_82\"></a>新建GatewayRoutesController</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-meta\">@RestController</span>\n<span class=\"hljs-meta\">@RequestMapping(&quot;/gateway&quot;)</span>\n\n<span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">class</span> <span class=\"hljs-title class_\">GatewayRoutesController</span> {\n\n    <span class=\"hljs-meta\">@Autowired</span>\n    <span class=\"hljs-keyword\">private</span> RefreshRouteService refreshRouteService;\n\n    <span class=\"hljs-meta\">@Autowired</span>\n    <span class=\"hljs-keyword\">private</span> DynamicRouteServiceImpl dynamicRouteService;\n\n    <span class=\"hljs-meta\">@GetMapping(&quot;/refreshRoutes&quot;)</span>\n    <span class=\"hljs-keyword\">public</span> CommonResult <span class=\"hljs-title function_\">refreshRoutes</span><span class=\"hljs-params\">()</span> {\n        refreshRouteService.refreshRoutes();\n        <span class=\"hljs-keyword\">return</span> CommonResult.builder().message(<span class=\"hljs-string\">&quot;成功&quot;</span>).build();\n    }\n\n    <span class=\"hljs-comment\">/**\n     * <span class=\"hljs-doctag\">@param</span>\n     * <span class=\"hljs-doctag\">@return</span>\n     */</span>\n    <span class=\"hljs-meta\">@RequestMapping(value = &quot;routes/add&quot;, method = RequestMethod.POST)</span>\n    <span class=\"hljs-keyword\">public</span> CommonResult <span class=\"hljs-title function_\">add</span><span class=\"hljs-params\">(<span class=\"hljs-meta\">@RequestBody</span> RouteDefinition routeDefinition)</span> {\n        <span class=\"hljs-type\">boolean</span> <span class=\"hljs-variable\">flag</span> <span class=\"hljs-operator\">=</span> dynamicRouteService.add(routeDefinition);\n        <span class=\"hljs-keyword\">if</span> (flag) <span class=\"hljs-keyword\">return</span> CommonResult.builder().message(<span class=\"hljs-string\">&quot;成功&quot;</span>).build();\n        <span class=\"hljs-keyword\">return</span> CommonResult.builder().message(<span class=\"hljs-string\">&quot;失败&quot;</span>).build();\n    }\n\n    <span class=\"hljs-comment\">/**\n     * <span class=\"hljs-doctag\">@param</span> definition\n     * <span class=\"hljs-doctag\">@return</span>\n     */</span>\n    <span class=\"hljs-meta\">@RequestMapping(value = &quot;routes/update&quot;, method = RequestMethod.POST)</span>\n    <span class=\"hljs-keyword\">public</span> CommonResult <span class=\"hljs-title function_\">update</span><span class=\"hljs-params\">(<span class=\"hljs-meta\">@RequestBody</span> RouteDefinition definition)</span> {\n        <span class=\"hljs-type\">boolean</span> <span class=\"hljs-variable\">flag</span> <span class=\"hljs-operator\">=</span> dynamicRouteService.update(definition);\n        <span class=\"hljs-keyword\">if</span> (flag) <span class=\"hljs-keyword\">return</span> CommonResult.builder().message(<span class=\"hljs-string\">&quot;成功&quot;</span>).build();\n        <span class=\"hljs-keyword\">return</span> CommonResult.builder().message(<span class=\"hljs-string\">&quot;失败&quot;</span>).build();\n    }\n\n    <span class=\"hljs-comment\">/**\n     * <span class=\"hljs-doctag\">@param</span> serviceId\n     * <span class=\"hljs-doctag\">@return</span>\n     */</span>\n    <span class=\"hljs-meta\">@RequestMapping(value = &quot;routes/del&quot;, method = RequestMethod.POST)</span>\n    <span class=\"hljs-keyword\">public</span> CommonResult <span class=\"hljs-title function_\">update</span><span class=\"hljs-params\">(<span class=\"hljs-meta\">@RequestParam(&quot;serviceId&quot;)</span> String serviceId)</span> {\n        <span class=\"hljs-type\">boolean</span> <span class=\"hljs-variable\">flag</span> <span class=\"hljs-operator\">=</span> dynamicRouteService.del(serviceId);\n        <span class=\"hljs-keyword\">if</span> (flag) <span class=\"hljs-keyword\">return</span> CommonResult.builder().message(<span class=\"hljs-string\">&quot;成功&quot;</span>).build();\n        <span class=\"hljs-keyword\">return</span> CommonResult.builder().message(<span class=\"hljs-string\">&quot;失败&quot;</span>).build();\n    }\n\n}\n\n</code></div></pre>\n<h4><a id=\"routesadd__137\"></a>当需要添加路由时只需执行routes/add接口 传入相对应的参数格式即可</h4>\n', '\n#### 创建DynamicRouteServiceImpl文件 并实现ApplicationEventPublisherAware。\n```java\n@Slf4j\n@Service\npublic class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {\n\n    @Autowired\n    private RouteDefinitionWriter routeDefinitionWriter;\n\n    private ApplicationEventPublisher publisher;\n\n    /**\n     * 添加路由实体类\n     *\n     * @param definition\n     * @return\n     */\n    public boolean add(RouteDefinition definition) {\n        routeDefinitionWriter.save(Mono.just(definition)).subscribe();\n        this.publisher.publishEvent(new RefreshRoutesEvent(this));\n        return true;\n    }\n\n    /**\n     * @param definition 路由实体类\n     * @return\n     */\n    public boolean update(RouteDefinition definition) {\n        try {\n            routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();\n        } catch (Exception e) {\n            log.error(\"update 失败。没有找到对应的路由ID :{}\", definition.getId());\n        }\n        routeDefinitionWriter.save(Mono.just(definition)).subscribe();\n        this.publisher.publishEvent(new RefreshRoutesEvent(this));\n        return true;\n    }\n\n    /**\n     * serviceId\n     *\n     * @param id\n     * @return\n     */\n    public boolean del(String id) {\n        routeDefinitionWriter.delete(Mono.just(id)).subscribe();\n        this.publisher.publishEvent(new RefreshRoutesEvent(this));\n        return true;\n    }\n\n    @Override\n    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {\n        this.publisher = applicationEventPublisher;\n    }\n}\n\n```\n\n#### 新建RefreshRouteService文件并实现ApplicationEventPublisherAware\n\n```java\n@Component\n@Slf4j\npublic class RefreshRouteService implements ApplicationEventPublisherAware {\n\n    private ApplicationEventPublisher publisher;\n\n    @Override\n    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {\n        this.publisher = publisher;\n    }\n\n    /**\n     * 刷新路由表\n     */\n    public void refreshRoutes() {\n        publisher.publishEvent(new RefreshRoutesEvent(this));\n    }\n}\n\n```\n#### 新建GatewayRoutesController\n```java\n@RestController\n@RequestMapping(\"/gateway\")\n\npublic class GatewayRoutesController {\n\n    @Autowired\n    private RefreshRouteService refreshRouteService;\n\n    @Autowired\n    private DynamicRouteServiceImpl dynamicRouteService;\n\n    @GetMapping(\"/refreshRoutes\")\n    public CommonResult refreshRoutes() {\n        refreshRouteService.refreshRoutes();\n        return CommonResult.builder().message(\"成功\").build();\n    }\n\n    /**\n     * @param\n     * @return\n     */\n    @RequestMapping(value = \"routes/add\", method = RequestMethod.POST)\n    public CommonResult add(@RequestBody RouteDefinition routeDefinition) {\n        boolean flag = dynamicRouteService.add(routeDefinition);\n        if (flag) return CommonResult.builder().message(\"成功\").build();\n        return CommonResult.builder().message(\"失败\").build();\n    }\n\n    /**\n     * @param definition\n     * @return\n     */\n    @RequestMapping(value = \"routes/update\", method = RequestMethod.POST)\n    public CommonResult update(@RequestBody RouteDefinition definition) {\n        boolean flag = dynamicRouteService.update(definition);\n        if (flag) return CommonResult.builder().message(\"成功\").build();\n        return CommonResult.builder().message(\"失败\").build();\n    }\n\n    /**\n     * @param serviceId\n     * @return\n     */\n    @RequestMapping(value = \"routes/del\", method = RequestMethod.POST)\n    public CommonResult update(@RequestParam(\"serviceId\") String serviceId) {\n        boolean flag = dynamicRouteService.del(serviceId);\n        if (flag) return CommonResult.builder().message(\"成功\").build();\n        return CommonResult.builder().message(\"失败\").build();\n    }\n\n}\n\n```\n#### 当需要添加路由时只需执行routes/add接口 传入相对应的参数格式即可\n', 1, 0, 1, 1, NULL, 152, '', '2021-09-01 10:35:13', 'gateway,动态路由,springboot,springcloud', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (9, 7, 18, 'springboot操作elasticsearch实现基本增删改查', '\r\nhttp://r6aox1myz.hd-bkt.clouddn.com/1639990013089.jpg', '基于elasticsearch7.14版本使用官方的restHighLevelClient实现操作', '<h4><a id=\"1_0\"></a>1、添加依赖</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\">&lt;dependency&gt;\n            &lt;groupId&gt;org.elasticsearch.client&lt;/groupId&gt;\n            &lt;artifactId&gt;elasticsearch-rest-client&lt;/artifactId&gt;\n            &lt;version&gt;<span class=\"hljs-number\">7.6</span><span class=\"hljs-number\">.2</span>&lt;/version&gt;\n        &lt;/dependency&gt;\n        &lt;dependency&gt;\n            &lt;groupId&gt;org.elasticsearch.client&lt;/groupId&gt;\n            &lt;artifactId&gt;elasticsearch-rest-high-level-client&lt;/artifactId&gt;\n            &lt;version&gt;<span class=\"hljs-number\">7.14</span><span class=\"hljs-number\">.1</span>&lt;/version&gt;\n            &lt;exclusions&gt;\n                &lt;exclusion&gt;\n                    &lt;artifactId&gt;elasticsearch-rest-client&lt;/artifactId&gt;\n                    &lt;groupId&gt;org.elasticsearch.client&lt;/groupId&gt;\n                &lt;/exclusion&gt;\n            &lt;/exclusions&gt;\n        &lt;/dependency&gt;\n        &lt;dependency&gt;\n            &lt;groupId&gt;org.elasticsearch&lt;/groupId&gt;\n            &lt;artifactId&gt;elasticsearch&lt;/artifactId&gt;\n            &lt;version&gt;<span class=\"hljs-number\">7.14</span><span class=\"hljs-number\">.1</span>&lt;/version&gt;\n        &lt;/dependency&gt;\n</code></div></pre>\n<h4><a id=\"2ElasticsearchConfiges_24\"></a>2、新建ElasticsearchConfig文件配置es</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-meta\">@Configuration</span>\n<span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">class</span> <span class=\"hljs-title class_\">ElasticsearchConfig</span> {\n\n    <span class=\"hljs-meta\">@Value(&quot;${searchServer.elasticsearch.hostlist}&quot;)</span>\n    <span class=\"hljs-keyword\">private</span> String hostlist;\n\n    <span class=\"hljs-meta\">@Bean</span>\n    <span class=\"hljs-keyword\">public</span> RestHighLevelClient <span class=\"hljs-title function_\">restHighLevelClient</span><span class=\"hljs-params\">()</span>{\n        <span class=\"hljs-comment\">//解析hostlist配置信息</span>\n        String[] split = hostlist.split(<span class=\"hljs-string\">&quot;,&quot;</span>);\n        <span class=\"hljs-comment\">//创建HttpHost数组，其中存放es主机和端口的配置信息</span>\n        HttpHost[] httpHostArray = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HttpHost</span>[split.length];\n        <span class=\"hljs-keyword\">for</span>(<span class=\"hljs-type\">int</span> i=<span class=\"hljs-number\">0</span>;i&lt;split.length;i++){\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">item</span> <span class=\"hljs-operator\">=</span> split[i];\n            httpHostArray[i] = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HttpHost</span>(item.split(<span class=\"hljs-string\">&quot;:&quot;</span>)[<span class=\"hljs-number\">0</span>], Integer.parseInt(item.split(<span class=\"hljs-string\">&quot;:&quot;</span>)[<span class=\"hljs-number\">1</span>]), <span class=\"hljs-string\">&quot;http&quot;</span>);\n        }\n        <span class=\"hljs-comment\">//创建RestHighLevelClient客户端</span>\n        <span class=\"hljs-keyword\">return</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">RestHighLevelClient</span>(RestClient.builder(httpHostArray));\n    }\n\n    <span class=\"hljs-comment\">//项目主要使用RestHighLevelClient，对于低级的客户端暂时不用</span>\n    <span class=\"hljs-meta\">@Bean</span>\n    <span class=\"hljs-keyword\">public</span> RestClient <span class=\"hljs-title function_\">restClient</span><span class=\"hljs-params\">()</span>{\n        <span class=\"hljs-comment\">//解析hostlist配置信息</span>\n        String[] split = hostlist.split(<span class=\"hljs-string\">&quot;,&quot;</span>);\n        <span class=\"hljs-comment\">//创建HttpHost数组，其中存放es主机和端口的配置信息</span>\n        HttpHost[] httpHostArray = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HttpHost</span>[split.length];\n        <span class=\"hljs-keyword\">for</span>(<span class=\"hljs-type\">int</span> i=<span class=\"hljs-number\">0</span>;i&lt;split.length;i++){\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">item</span> <span class=\"hljs-operator\">=</span> split[i];\n            httpHostArray[i] = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HttpHost</span>(item.split(<span class=\"hljs-string\">&quot;:&quot;</span>)[<span class=\"hljs-number\">0</span>], Integer.parseInt(item.split(<span class=\"hljs-string\">&quot;:&quot;</span>)[<span class=\"hljs-number\">1</span>]), <span class=\"hljs-string\">&quot;http&quot;</span>);\n        }\n        <span class=\"hljs-keyword\">return</span> RestClient.builder(httpHostArray).build();\n    }\n}\n</code></div></pre>\n<p>yml文件配置</p>\n<pre><div class=\"hljs\"><code class=\"lang-yml\"><span class=\"hljs-attr\">searchServer:</span>\n  <span class=\"hljs-attr\">elasticsearch:</span>\n    <span class=\"hljs-attr\">hostlist:</span> <span class=\"hljs-string\">${eshostlist:localhost:9200}</span> <span class=\"hljs-comment\">#多个结点中间用逗号分隔</span>\n</code></div></pre>\n<h4><a id=\"3httplocalhost9200_67\"></a>3、配置完后启动项目访问http://localhost:9200/</h4>\n<p>接下来就是操作es了<br />\n1、分页查询</p>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-keyword\">public</span> Map&lt;String,Object&gt; <span class=\"hljs-title function_\">selectEventPage</span><span class=\"hljs-params\">(String index,<span class=\"hljs-type\">int</span> from,<span class=\"hljs-type\">int</span> size,String value,Integer id)</span> {\n        Map&lt;String,Object&gt; map = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HashMap</span>&lt;&gt;();\n        <span class=\"hljs-comment\">//设置ES索引和类型</span>\n        <span class=\"hljs-type\">SearchRequest</span> <span class=\"hljs-variable\">searchRequest</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">SearchRequest</span>(index);\n\n        <span class=\"hljs-comment\">/**\n         * 分页参数\n         * 第一页为0\n         */</span>\n        <span class=\"hljs-type\">SearchSourceBuilder</span> <span class=\"hljs-variable\">searchSourceBuilder</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">SearchSourceBuilder</span>();\n        searchSourceBuilder.from(from*size);\n        searchSourceBuilder.size(size);\n\n        <span class=\"hljs-comment\">//最大的查询条件</span>\n        <span class=\"hljs-type\">BoolQueryBuilder</span> <span class=\"hljs-variable\">boolQueryBuilder</span> <span class=\"hljs-operator\">=</span> QueryBuilders.boolQuery();\n        boolQueryBuilder.must(QueryBuilders.termQuery(<span class=\"hljs-string\">&quot;deleteFlag&quot;</span>,<span class=\"hljs-number\">0</span>));\n\n        <span class=\"hljs-comment\">/**\n         * 查询条件\n         */</span>\n        <span class=\"hljs-keyword\">if</span> (StringUtils.isNotBlank(value)){\n            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(value,<span class=\"hljs-string\">&quot;acticleSimple&quot;</span>,<span class=\"hljs-string\">&quot;articleName&quot;</span> ).\n                    analyzer(<span class=\"hljs-string\">&quot;ik_max_word&quot;</span>).operator(Operator.OR));\n\n        }\n        <span class=\"hljs-keyword\">if</span> (<span class=\"hljs-literal\">null</span> != id) boolQueryBuilder.must(QueryBuilders.termQuery(<span class=\"hljs-string\">&quot;tagId&quot;</span>,id));\n\n        <span class=\"hljs-comment\">//设置高亮字段</span>\n        <span class=\"hljs-type\">HighlightBuilder</span> <span class=\"hljs-variable\">highlightBuilder</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HighlightBuilder</span>();\n        highlightBuilder.preTags(<span class=\"hljs-string\">&quot;&lt;span style=\\&quot;color:red\\&quot;&gt;&quot;</span>);\n        highlightBuilder.postTags(<span class=\"hljs-string\">&quot;&lt;/span&gt;&quot;</span>);\n        <span class=\"hljs-comment\">//高亮字段</span>\n        highlightBuilder.field(<span class=\"hljs-string\">&quot;articleName&quot;</span>).field(<span class=\"hljs-string\">&quot;acticleSimple&quot;</span>);\n        searchSourceBuilder.highlighter(highlightBuilder);\n        <span class=\"hljs-comment\">//将searchSourceBuilder放入searchRequest中</span>\n        searchRequest.source(searchSourceBuilder);\n        <span class=\"hljs-comment\">//搜索  排序</span>\n        searchSourceBuilder.query(boolQueryBuilder).sort(<span class=\"hljs-string\">&quot;isStick&quot;</span>, SortOrder.ASC).sort(<span class=\"hljs-string\">&quot;createTime&quot;</span>,SortOrder.ASC);\n        <span class=\"hljs-keyword\">try</span> {\n            <span class=\"hljs-type\">SearchResponse</span> <span class=\"hljs-variable\">searchResponse</span> <span class=\"hljs-operator\">=</span> restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);\n            <span class=\"hljs-comment\">//搜索结果</span>\n            <span class=\"hljs-type\">SearchHits</span> <span class=\"hljs-variable\">searchHits</span> <span class=\"hljs-operator\">=</span> searchResponse.getHits();\n            <span class=\"hljs-comment\">//计算总数量</span>\n            <span class=\"hljs-type\">long</span> <span class=\"hljs-variable\">totalCount</span> <span class=\"hljs-operator\">=</span> searchResponse.getHits().getTotalHits().value;\n            map.put(<span class=\"hljs-string\">&quot;totalCount&quot;</span>,totalCount);\n            <span class=\"hljs-comment\">//得到总页数</span>\n            <span class=\"hljs-type\">int</span> <span class=\"hljs-variable\">page</span> <span class=\"hljs-operator\">=</span> (<span class=\"hljs-type\">int</span>) Math.ceil((<span class=\"hljs-type\">float</span>) totalCount / size);\n            map.put(<span class=\"hljs-string\">&quot;totalPage&quot;</span>,page);\n            map.put(<span class=\"hljs-string\">&quot;current&quot;</span>,from);\n            List&lt;BlogArticle&gt; eventEsResultList = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">ArrayList</span>&lt;&gt;();\n            <span class=\"hljs-keyword\">for</span> (SearchHit searchHit : searchHits) {\n                <span class=\"hljs-type\">Map</span> <span class=\"hljs-variable\">eventMap</span> <span class=\"hljs-operator\">=</span> searchHit.getSourceAsMap();\n                <span class=\"hljs-type\">BlogArticle</span> <span class=\"hljs-variable\">article</span> <span class=\"hljs-operator\">=</span> JSON.parseObject(JSON.toJSONString(eventMap), BlogArticle.class);\n                Map&lt;String, HighlightField&gt; highlightFields = searchHit.getHighlightFields();\n                <span class=\"hljs-comment\">//高亮字段</span>\n                <span class=\"hljs-type\">HighlightField</span> <span class=\"hljs-variable\">highlight_articleName</span> <span class=\"hljs-operator\">=</span> highlightFields.get(<span class=\"hljs-string\">&quot;articleName&quot;</span>);\n                <span class=\"hljs-keyword\">if</span>(highlight_articleName!=<span class=\"hljs-literal\">null</span>){\n                    <span class=\"hljs-comment\">// 为title串值增加自定义的高亮标签</span>\n                    Text[] titleTexts = highlight_articleName.fragments();\n                    <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">articleName</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-string\">&quot;&quot;</span>;\n                    <span class=\"hljs-keyword\">for</span> (Text text : titleTexts) {\n                        articleName += text;\n                    }\n                    <span class=\"hljs-comment\">//将追加了高亮标签的串值重新填充到对应的对象</span>\n                    article.setArticleName(articleName);\n                }\n\n                <span class=\"hljs-type\">HighlightField</span> <span class=\"hljs-variable\">highlight_acticleSimple</span> <span class=\"hljs-operator\">=</span> highlightFields.get(<span class=\"hljs-string\">&quot;acticleSimple&quot;</span>);\n                <span class=\"hljs-keyword\">if</span>(highlight_acticleSimple!=<span class=\"hljs-literal\">null</span>){\n                    Text[] titleTexts = highlight_acticleSimple.fragments();\n                    <span class=\"hljs-comment\">// 为title串值增加自定义的高亮标签</span>\n                    <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">acticleSimple</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-string\">&quot;&quot;</span>;\n                    <span class=\"hljs-keyword\">for</span> (Text text : titleTexts) {\n                        acticleSimple += text;\n                    }\n                    <span class=\"hljs-comment\">//将追加了高亮标签的串值重新填充到对应的对象</span>\n                    article.setActicleSimple(acticleSimple);\n                }\n                <span class=\"hljs-keyword\">try</span> {\n                    eventEsResultList.add(article);\n                } <span class=\"hljs-keyword\">catch</span> (Exception e) {\n                    e.printStackTrace();\n                }\n            }\n            map.put(<span class=\"hljs-string\">&quot;data&quot;</span>,eventEsResultList);\n        } <span class=\"hljs-keyword\">catch</span> (IOException e) {\n            e.printStackTrace();\n        }\n        <span class=\"hljs-keyword\">return</span> map;\n    }\n</code></div></pre>\n<hr />\n', '#### 1、添加依赖\n```java\n<dependency>\n            <groupId>org.elasticsearch.client</groupId>\n            <artifactId>elasticsearch-rest-client</artifactId>\n            <version>7.6.2</version>\n        </dependency>\n        <dependency>\n            <groupId>org.elasticsearch.client</groupId>\n            <artifactId>elasticsearch-rest-high-level-client</artifactId>\n            <version>7.14.1</version>\n            <exclusions>\n                <exclusion>\n                    <artifactId>elasticsearch-rest-client</artifactId>\n                    <groupId>org.elasticsearch.client</groupId>\n                </exclusion>\n            </exclusions>\n        </dependency>\n        <dependency>\n            <groupId>org.elasticsearch</groupId>\n            <artifactId>elasticsearch</artifactId>\n            <version>7.14.1</version>\n        </dependency>\n```\n#### 2、新建ElasticsearchConfig文件配置es\n```java\n@Configuration\npublic class ElasticsearchConfig {\n\n    @Value(\"${searchServer.elasticsearch.hostlist}\")\n    private String hostlist;\n\n    @Bean\n    public RestHighLevelClient restHighLevelClient(){\n        //解析hostlist配置信息\n        String[] split = hostlist.split(\",\");\n        //创建HttpHost数组，其中存放es主机和端口的配置信息\n        HttpHost[] httpHostArray = new HttpHost[split.length];\n        for(int i=0;i<split.length;i++){\n            String item = split[i];\n            httpHostArray[i] = new HttpHost(item.split(\":\")[0], Integer.parseInt(item.split(\":\")[1]), \"http\");\n        }\n        //创建RestHighLevelClient客户端\n        return new RestHighLevelClient(RestClient.builder(httpHostArray));\n    }\n\n    //项目主要使用RestHighLevelClient，对于低级的客户端暂时不用\n    @Bean\n    public RestClient restClient(){\n        //解析hostlist配置信息\n        String[] split = hostlist.split(\",\");\n        //创建HttpHost数组，其中存放es主机和端口的配置信息\n        HttpHost[] httpHostArray = new HttpHost[split.length];\n        for(int i=0;i<split.length;i++){\n            String item = split[i];\n            httpHostArray[i] = new HttpHost(item.split(\":\")[0], Integer.parseInt(item.split(\":\")[1]), \"http\");\n        }\n        return RestClient.builder(httpHostArray).build();\n    }\n}\n```\nyml文件配置\n```yml\nsearchServer:\n  elasticsearch:\n    hostlist: ${eshostlist:localhost:9200} #多个结点中间用逗号分隔\n```\n#### 3、配置完后启动项目访问http://localhost:9200/\n\n接下来就是操作es了\n1、分页查询\n```java\npublic Map<String,Object> selectEventPage(String index,int from,int size,String value,Integer id) {\n        Map<String,Object> map = new HashMap<>();\n        //设置ES索引和类型\n        SearchRequest searchRequest = new SearchRequest(index);\n\n        /**\n         * 分页参数\n         * 第一页为0\n         */\n        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();\n        searchSourceBuilder.from(from*size);\n        searchSourceBuilder.size(size);\n\n        //最大的查询条件\n        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();\n        boolQueryBuilder.must(QueryBuilders.termQuery(\"deleteFlag\",0));\n\n        /**\n         * 查询条件\n         */\n        if (StringUtils.isNotBlank(value)){\n            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(value,\"acticleSimple\",\"articleName\" ).\n                    analyzer(\"ik_max_word\").operator(Operator.OR));\n\n        }\n        if (null != id) boolQueryBuilder.must(QueryBuilders.termQuery(\"tagId\",id));\n\n        //设置高亮字段\n        HighlightBuilder highlightBuilder = new HighlightBuilder();\n        highlightBuilder.preTags(\"<span style=\\\"color:red\\\">\");\n        highlightBuilder.postTags(\"</span>\");\n        //高亮字段\n        highlightBuilder.field(\"articleName\").field(\"acticleSimple\");\n        searchSourceBuilder.highlighter(highlightBuilder);\n        //将searchSourceBuilder放入searchRequest中\n        searchRequest.source(searchSourceBuilder);\n        //搜索  排序\n        searchSourceBuilder.query(boolQueryBuilder).sort(\"isStick\", SortOrder.ASC).sort(\"createTime\",SortOrder.ASC);\n        try {\n            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);\n            //搜索结果\n            SearchHits searchHits = searchResponse.getHits();\n            //计算总数量\n            long totalCount = searchResponse.getHits().getTotalHits().value;\n            map.put(\"totalCount\",totalCount);\n            //得到总页数\n            int page = (int) Math.ceil((float) totalCount / size);\n            map.put(\"totalPage\",page);\n            map.put(\"current\",from);\n            List<BlogArticle> eventEsResultList = new ArrayList<>();\n            for (SearchHit searchHit : searchHits) {\n                Map eventMap = searchHit.getSourceAsMap();\n                BlogArticle article = JSON.parseObject(JSON.toJSONString(eventMap), BlogArticle.class);\n                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();\n                //高亮字段\n                HighlightField highlight_articleName = highlightFields.get(\"articleName\");\n                if(highlight_articleName!=null){\n                    // 为title串值增加自定义的高亮标签\n                    Text[] titleTexts = highlight_articleName.fragments();\n                    String articleName = \"\";\n                    for (Text text : titleTexts) {\n                        articleName += text;\n                    }\n                    //将追加了高亮标签的串值重新填充到对应的对象\n                    article.setArticleName(articleName);\n                }\n\n                HighlightField highlight_acticleSimple = highlightFields.get(\"acticleSimple\");\n                if(highlight_acticleSimple!=null){\n                    Text[] titleTexts = highlight_acticleSimple.fragments();\n                    // 为title串值增加自定义的高亮标签\n                    String acticleSimple = \"\";\n                    for (Text text : titleTexts) {\n                        acticleSimple += text;\n                    }\n                    //将追加了高亮标签的串值重新填充到对应的对象\n                    article.setActicleSimple(acticleSimple);\n                }\n                try {\n                    eventEsResultList.add(article);\n                } catch (Exception e) {\n                    e.printStackTrace();\n                }\n            }\n            map.put(\"data\",eventEsResultList);\n        } catch (IOException e) {\n            e.printStackTrace();\n        }\n        return map;\n    }\n```\n----------------------------------------------------\n', 0, 0, 1, 1, NULL, 23, '', '2021-09-09 15:03:07', 'springboot,elasticsearch,sprinbcloud,java,restHighLevelClient', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (14, 7, 12, '关于博客', '\r\nhttp://r6aox1myz.hd-bkt.clouddn.com/1642655697136.jpg', '关于博客的一些介绍', '<h4><a id=\"_0\"></a>博客介绍</h4>\n<h4><a id=\"1_1\"></a>1、博客使用到的技术</h4>\n<ol>\n<li>后端：springboot，mysql，es搜索引擎，以及redis</li>\n<li>前端：vue</li>\n</ol>\n<h4><a id=\"2_5\"></a>2、首页登录</h4>\n<p>本博客支持<a href=\"https://connect.qq.com/index.html\" target=\"_blank\">QQ</a>、<a href=\"https://gitee.com/api/v5/oauth_doc#/\" target=\"_blank\">gitee</a>、<a href=\"https://open.weibo.com/?bottomnav=1&amp;wvr=6\" target=\"_blank\">微博</a>登录，后续还会支持账号密码和邮箱登录</p>\n<h4><a id=\"3_7\"></a>3、图片的存储</h4>\n<p>项目中图片存储采用的是<a href=\"https://www.qiniu.com/products/kodo\" target=\"_blank\">七牛云</a>平台的对象存储，因为只有七牛云每个月会有免费的一个额度，其他的都是需要收费的。</p>\n<h4><a id=\"4_9\"></a>4、首页的搜索</h4>\n<p>首页的搜索采用的是es的搜索，eslinux启动的话会占用比较高的一个内存，所以博主介意如需要使用es搜索的购买服务器时资金允许的情况下尽可能购买内存比较多的服务器。然后项目搜索的话主要是围绕文章的标题和内容进行的分词搜索。</p>\n<blockquote>\n<p>上面的话都是一些首页的介绍，原作者只是提供了一个模板，并没有提供后台管理的一个功能，所以我就自己写了一个后台管理，主要借用的是比较火的一个<a href=\"https://gitee.com/panjiachen/vue-admin-template\" target=\"_blank\">vue-admin-template</a>项目模板来进行的二次开发，由于时间有限，所以后台管理开发的功能并不是很多，但是也能进行一个简单的博客管理了。</p>\n</blockquote>\n<h3><a id=\"_14\"></a>后台管理</h3>\n<blockquote>\n<p><a href=\"http://www.shiyit.com/admin\" target=\"_blank\">后台地址</a><br />\n演示账号：test,密码：test</p>\n</blockquote>\n<h4><a id=\"1_17\"></a>1、登录界面</h4>\n<p><img src=\"http://img.shiyit.com/1639989154384.jpg\" alt=\"login.jpg\" /></p>\n<h4><a id=\"2_19\"></a>2、后台首页</h4>\n<p><img src=\"http://img.shiyit.com/1639989162006.png\" alt=\"adminhome1.png\" /><br />\n<img src=\"http://img.shiyit.com/1639989170907.png\" alt=\"adminhome2.png\" /></p>\n<h4><a id=\"3_22\"></a>3、文章管理</h4>\n<ol>\n<li>列表页<br />\n<img src=\"http://img.shiyit.com/1639989186471.png\" alt=\"文章列表.png\" /></li>\n<li>文章添加<br />\n<img src=\"http://img.shiyit.com/1639989202706.png\" alt=\"文章添加.png\" /><br />\n编辑器使用的是mavon-editor编辑器，如不会使用的可以点击下面的链接查看<br />\n<a href=\"https://blog.csdn.net/qq_43681948/article/details/101531303\" target=\"_blank\">Vue markdown编辑器</a></li>\n</ol>\n<h4><a id=\"4_29\"></a>4、标签管理</h4>\n<p><img src=\"http://img.shiyit.com/1639989247680.png\" alt=\"标签列表.png\" /></p>\n<h4><a id=\"5_31\"></a>5、日志管理</h4>\n<ol>\n<li>针对用户访问以及管理员操作和异常信息都做了一个日志的记录</li>\n</ol>\n<p><img src=\"http://img.shiyit.com/1639989291127.png\" alt=\"日志管理.png\" /></p>\n<h4><a id=\"6_35\"></a>6、系统管理</h4>\n<p><img src=\"http://img.shiyit.com/1639989383160.png\" alt=\"系统配置.png\" /></p>\n<h4><a id=\"7_37\"></a>7、监控中心</h4>\n<ol>\n<li>监控中心主要有服务器的监控和定时任务，定时任务支持自动添加修改删除功能，改了时间无需修改代码<br />\n<img src=\"http://img.shiyit.com/1639989456867.png\" alt=\"定时任务.png\" /></li>\n</ol>\n<h4><a id=\"_40\"></a>结尾</h4>\n<p>上述只是项目的一些基础的功能图片，小伙伴们可以自行登录去查看，后台管理所看到的菜单功能都已实现，还在着手后续的一些功能开发。后台管理全由我自己根据模板改造而来，毕竟我只会业余的前端技术，所以有所缺陷也属正常。如有比较有意思的功能欢迎各位小伙伴给我留言，作者觉得有意思的话就会着手开发。如本项目小伙伴觉得可以，希望能够码云star一下，万分感谢！！</p>\n<blockquote>\n<p>码云地址：<a href=\"https://gitee.com/quequnlong/vue-admin-blog\" target=\"_blank\">点我进入</a></p>\n</blockquote>\n<h1><a id=\"bye_43\"></a>bye</h1>\n', '#### 博客介绍\n#### 1、博客使用到的技术\n1. 后端：springboot，mysql，es搜索引擎，以及redis\n2. 前端：vue\n\n#### 2、首页登录\n本博客支持[QQ](https://connect.qq.com/index.html)、[gitee](https://gitee.com/api/v5/oauth_doc#/)、[微博](https://open.weibo.com/?bottomnav=1&wvr=6)登录，后续还会支持账号密码和邮箱登录\n#### 3、图片的存储\n项目中图片存储采用的是[七牛云](https://www.qiniu.com/products/kodo)平台的对象存储，因为只有七牛云每个月会有免费的一个额度，其他的都是需要收费的。\n#### 4、首页的搜索\n首页的搜索采用的是es的搜索，eslinux启动的话会占用比较高的一个内存，所以博主介意如需要使用es搜索的购买服务器时资金允许的情况下尽可能购买内存比较多的服务器。然后项目搜索的话主要是围绕文章的标题和内容进行的分词搜索。\n\n> 上面的话都是一些首页的介绍，原作者只是提供了一个模板，并没有提供后台管理的一个功能，所以我就自己写了一个后台管理，主要借用的是比较火的一个[vue-admin-template](https://gitee.com/panjiachen/vue-admin-template)项目模板来进行的二次开发，由于时间有限，所以后台管理开发的功能并不是很多，但是也能进行一个简单的博客管理了。\n\n###  后台管理\n> [后台地址](http://www.shiyit.com/admin)\n演示账号：test,密码：test\n#### 1、登录界面\n![login.jpg](http://img.shiyit.com/1639989154384.jpg)\n#### 2、后台首页\n![adminhome1.png](http://img.shiyit.com/1639989162006.png)\n![adminhome2.png](http://img.shiyit.com/1639989170907.png)\n#### 3、文章管理\n1. 列表页\n![文章列表.png](http://img.shiyit.com/1639989186471.png)\n2. 文章添加\n![文章添加.png](http://img.shiyit.com/1639989202706.png)\n编辑器使用的是mavon-editor编辑器，如不会使用的可以点击下面的链接查看\n[Vue markdown编辑器](https://blog.csdn.net/qq_43681948/article/details/101531303)\n#### 4、标签管理\n![标签列表.png](http://img.shiyit.com/1639989247680.png)\n#### 5、日志管理\n1. 针对用户访问以及管理员操作和异常信息都做了一个日志的记录\n\n![日志管理.png](http://img.shiyit.com/1639989291127.png)\n#### 6、系统管理\n![系统配置.png](http://img.shiyit.com/1639989383160.png)\n#### 7、监控中心\n1. 监控中心主要有服务器的监控和定时任务，定时任务支持自动添加修改删除功能，改了时间无需修改代码\n![定时任务.png](http://img.shiyit.com/1639989456867.png)\n#### 结尾\n上述只是项目的一些基础的功能图片，小伙伴们可以自行登录去查看，后台管理所看到的菜单功能都已实现，还在着手后续的一些功能开发。后台管理全由我自己根据模板改造而来，毕竟我只会业余的前端技术，所以有所缺陷也属正常。如有比较有意思的功能欢迎各位小伙伴给我留言，作者觉得有意思的话就会着手开发。如本项目小伙伴觉得可以，希望能够码云star一下，万分感谢！！\n> 码云地址：[点我进入](https://gitee.com/quequnlong/vue-admin-blog)\n# bye\n\n', 0, 1, 1, 1, NULL, 1598, '', '2021-10-15 09:57:22', 'blog,isblog,博客', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (87, 7, 19, 'springboot使用webmagic抓取文章', 'https://i.picsum.photos/id/1006/3000/2000.jpg?hmac=x83pQQ7LW1UTo8HxBcIWuRIVeN_uCg0cG6keXvNvM8g', 'springboot使用webmagic抓取文章', '<h4><a id=\"_0\"></a>引入依赖</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">   <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">dependency</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">groupId</span>&gt;</span>us.codecraft<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">groupId</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">artifactId</span>&gt;</span>webmagic-core<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">artifactId</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">version</span>&gt;</span>0.7.3<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">version</span>&gt;</span>\n        <span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">dependency</span>&gt;</span>\n        <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">dependency</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">groupId</span>&gt;</span>us.codecraft<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">groupId</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">artifactId</span>&gt;</span>webmagic-extension<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">artifactId</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">version</span>&gt;</span>0.7.3<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">version</span>&gt;</span>\n        <span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">dependency</span>&gt;</span>\n</code></div></pre>\n<h4><a id=\"PageProcessor_13\"></a>创建抓取类实现PageProcessor</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-comment\">/**\n * <span class=\"hljs-doctag\">@author</span> blue\n * <span class=\"hljs-doctag\">@date</span> 2021/12/22\n * <span class=\"hljs-doctag\">@apiNote</span>\n */</span>\n<span class=\"hljs-meta\">@Component</span>\n<span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">class</span> <span class=\"hljs-title class_\">CSDNPageProcessor</span> <span class=\"hljs-keyword\">implements</span> <span class=\"hljs-title class_\">PageProcessor</span> {\n\n    <span class=\"hljs-keyword\">private</span> <span class=\"hljs-type\">Site</span> <span class=\"hljs-variable\">site</span> <span class=\"hljs-operator\">=</span> Site.me().setRetryTimes(<span class=\"hljs-number\">1</span>).setSleepTime(<span class=\"hljs-number\">1000</span>);\n\n    <span class=\"hljs-meta\">@Override</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">void</span> <span class=\"hljs-title function_\">process</span><span class=\"hljs-params\">(Page page)</span> {\n	<span class=\"hljs-comment\">//此处HTML标签 自行在文章页面右键检查元素获取</span>\n        page.putField(<span class=\"hljs-string\">&quot;title&quot;</span>, page.getHtml().xpath(<span class=\"hljs-string\">&quot;//h1[@class=&#x27;title-article&#x27;]/text()&quot;</span>).toString());\n        page.putField(<span class=\"hljs-string\">&quot;tags&quot;</span>, page.getHtml().xpath(<span class=\"hljs-string\">&quot;//a[@class=&#x27;tag-link&#x27;]/text()&quot;</span>).all());\n        page.putField(<span class=\"hljs-string\">&quot;content&quot;</span>, page.getHtml().xpath(<span class=\"hljs-string\">&quot;//div[@class=&#x27;article_content clearfix&#x27;]&quot;</span>).toString()\n        +<span class=\"hljs-string\">&quot;&lt;blockquote&gt;\\n&quot;</span> +\n                <span class=\"hljs-string\">&quot;&lt;p&gt;该文章转载博客:&quot;</span>+page.getUrl()+<span class=\"hljs-string\">&quot;&lt;/p&gt;\\n&quot;</span> +\n                <span class=\"hljs-string\">&quot;&lt;/blockquote&gt;\\n&quot;</span>);\n        <span class=\"hljs-comment\">//page.putField(SysConf.AVATAR,SysConf.ARTICLE_IMG);</span>\n        <span class=\"hljs-keyword\">if</span> (page.getResultItems().get(SqlConf.TITLE) == <span class=\"hljs-literal\">null</span>) {\n            <span class=\"hljs-comment\">// 如果是列表页，跳过此页，pipeline不进行后续处理</span>\n            page.setSkip(<span class=\"hljs-literal\">true</span>);\n        }\n    }\n\n    <span class=\"hljs-meta\">@Override</span>\n    <span class=\"hljs-keyword\">public</span> Site <span class=\"hljs-title function_\">getSite</span><span class=\"hljs-params\">()</span> {\n        <span class=\"hljs-keyword\">return</span> site;\n    }\n}\n</code></div></pre>\n<h4><a id=\"Pipeline_47\"></a>创建数据处理类实现Pipeline</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-comment\">/**\n * <span class=\"hljs-doctag\">@author</span> blue\n * <span class=\"hljs-doctag\">@date</span> 2021/12/22\n * <span class=\"hljs-doctag\">@apiNote</span>\n */</span>\n<span class=\"hljs-meta\">@Component</span>\n<span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">class</span> <span class=\"hljs-title class_\">BlogPipeline</span> <span class=\"hljs-keyword\">implements</span> <span class=\"hljs-title class_\">Pipeline</span> {\n\n    <span class=\"hljs-keyword\">private</span> <span class=\"hljs-keyword\">static</span> <span class=\"hljs-keyword\">final</span> <span class=\"hljs-type\">Logger</span> <span class=\"hljs-variable\">LOGGER</span> <span class=\"hljs-operator\">=</span> LoggerFactory.getLogger(BlogPipeline.class);\n\n    <span class=\"hljs-meta\">@Autowired</span>\n    <span class=\"hljs-keyword\">private</span> ArticleMapper articleMapper;\n    <span class=\"hljs-meta\">@Autowired</span>\n    <span class=\"hljs-keyword\">private</span> TagsMapper tagsMapper;\n    <span class=\"hljs-meta\">@Autowired</span>\n    <span class=\"hljs-keyword\">private</span> RestTemplate restTemplate;\n\n    <span class=\"hljs-meta\">@Override</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">void</span> <span class=\"hljs-title function_\">process</span><span class=\"hljs-params\">(ResultItems resultItems, Task task)</span> {\n         <span class=\"hljs-type\">Object</span> <span class=\"hljs-variable\">title</span> <span class=\"hljs-operator\">=</span> resultItems.get(<span class=\"hljs-string\">&quot;title&quot;</span>);\n        List&lt;Object&gt; tagList = resultItems.get(<span class=\"hljs-string\">&quot;tags&quot;</span>);\n        <span class=\"hljs-type\">Object</span> <span class=\"hljs-variable\">content</span> <span class=\"hljs-operator\">=</span> resultItems.get(<span class=\"hljs-string\">&quot;content&quot;</span>);\n        <span class=\"hljs-keyword\">if</span> (StringUtils.isNotBlank(content.toString())){\n            <span class=\"hljs-comment\">//爬取的是HTML内容，需要转成MD格式的内容</span>\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">newContent</span> <span class=\"hljs-operator\">=</span> content.toString().replaceAll(<span class=\"hljs-string\">&quot;&lt;code&gt;&quot;</span>, <span class=\"hljs-string\">&quot;&lt;code class=\\&quot;lang-java\\&quot;&gt;&quot;</span>);\n            <span class=\"hljs-type\">MutableDataSet</span> <span class=\"hljs-variable\">options</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">MutableDataSet</span>();\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">markdown</span> <span class=\"hljs-operator\">=</span> FlexmarkHtmlConverter.builder(options).build().convert(newContent)\n                    .replace(<span class=\"hljs-string\">&quot;lang-java&quot;</span>,<span class=\"hljs-string\">&quot;java&quot;</span>);\n\n            <span class=\"hljs-comment\">//文章封面图片 由https://picsum.photos该网站随机获取</span>\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">url</span> <span class=\"hljs-operator\">=</span> MessageFormat.format(<span class=\"hljs-string\">&quot;https://picsum.photos/id/{0}/info&quot;</span>, RanDomUtil.generationOneNumber(<span class=\"hljs-number\">1000</span>));\n            <span class=\"hljs-type\">Map</span> <span class=\"hljs-variable\">map</span> <span class=\"hljs-operator\">=</span> restTemplate.getForObject(url, Map.class);\n\n            <span class=\"hljs-type\">Date</span> <span class=\"hljs-variable\">nowDate</span> <span class=\"hljs-operator\">=</span> DateUtils.getNowDate();\n            <span class=\"hljs-type\">BlogArticle</span> <span class=\"hljs-variable\">entity</span> <span class=\"hljs-operator\">=</span> BlogArticle.builder().userId(SysConf.ADMIN_USER_ID).contentMd(markdown)\n                    .categoryId(SysConf.OTHER_CATEGORY_ID).isOriginal(YesOrNoEnum.NO.getCode())\n                    .title(title.toString()).avatar(map.get(<span class=\"hljs-string\">&quot;download_url&quot;</span>).toString()).content(newContent)\n                    .createTime(nowDate).updateTime(nowDate).build();\n\n            articleMapper.insert(entity);\n            <span class=\"hljs-comment\">//为该文章添加标签</span>\n            List&lt;Long&gt; tagsId = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">ArrayList</span>&lt;&gt;();\n            tagList.forEach(item -&gt;{\n                <span class=\"hljs-type\">Tags</span> <span class=\"hljs-variable\">result</span> <span class=\"hljs-operator\">=</span> tagsMapper.selectOne(<span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">QueryWrapper</span>&lt;Tags&gt;().eq(SqlConf.NAME, item.toString()));\n                <span class=\"hljs-keyword\">if</span> (result == <span class=\"hljs-literal\">null</span>){\n                    result = Tags.builder().name(item.toString()).build();\n                    tagsMapper.insert(result);\n                }\n                tagsId.add(result.getId());\n            });\n            tagsMapper.saveArticleToTags(entity.getId(),tagsId);\n\n        }\n\n    }\n}\n</code></div></pre>\n<h4><a id=\"_106\"></a>调用</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"><span class=\"hljs-type\">Spider</span>  <span class=\"hljs-variable\">spider</span> <span class=\"hljs-operator\">=</span> Spider.create(<span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">CSDNPageProcessor</span>()).addUrl(url);\n        spider.addPipeline(blogPipeline).thread(<span class=\"hljs-number\">5</span>).run();\n</code></div></pre>\n<blockquote>\n<p>以上展示是全部代码，如爬取CSDN博客的文章可直接复制，修改数据处理类的代码即可，有时间的会讲的更加清楚一点</p>\n</blockquote>\n', '#### 引入依赖\n```xml\n   <dependency>\n            <groupId>us.codecraft</groupId>\n            <artifactId>webmagic-core</artifactId>\n            <version>0.7.3</version>\n        </dependency>\n        <dependency>\n            <groupId>us.codecraft</groupId>\n            <artifactId>webmagic-extension</artifactId>\n            <version>0.7.3</version>\n        </dependency>\n```\n#### 创建抓取类实现PageProcessor\n```java\n/**\n * @author blue\n * @date 2021/12/22\n * @apiNote\n */\n@Component\npublic class CSDNPageProcessor implements PageProcessor {\n\n    private Site site = Site.me().setRetryTimes(1).setSleepTime(1000);\n\n    @Override\n    public void process(Page page) {\n	//此处HTML标签 自行在文章页面右键检查元素获取\n        page.putField(\"title\", page.getHtml().xpath(\"//h1[@class=\'title-article\']/text()\").toString());\n        page.putField(\"tags\", page.getHtml().xpath(\"//a[@class=\'tag-link\']/text()\").all());\n        page.putField(\"content\", page.getHtml().xpath(\"//div[@class=\'article_content clearfix\']\").toString()\n        +\"<blockquote>\\n\" +\n                \"<p>该文章转载博客:\"+page.getUrl()+\"</p>\\n\" +\n                \"</blockquote>\\n\");\n        //page.putField(SysConf.AVATAR,SysConf.ARTICLE_IMG);\n        if (page.getResultItems().get(SqlConf.TITLE) == null) {\n            // 如果是列表页，跳过此页，pipeline不进行后续处理\n            page.setSkip(true);\n        }\n    }\n\n    @Override\n    public Site getSite() {\n        return site;\n    }\n}\n```\n#### 创建数据处理类实现Pipeline\n```java\n/**\n * @author blue\n * @date 2021/12/22\n * @apiNote\n */\n@Component\npublic class BlogPipeline implements Pipeline {\n\n    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPipeline.class);\n\n    @Autowired\n    private ArticleMapper articleMapper;\n    @Autowired\n    private TagsMapper tagsMapper;\n    @Autowired\n    private RestTemplate restTemplate;\n\n    @Override\n    public void process(ResultItems resultItems, Task task) {\n         Object title = resultItems.get(\"title\");\n        List<Object> tagList = resultItems.get(\"tags\");\n        Object content = resultItems.get(\"content\");\n        if (StringUtils.isNotBlank(content.toString())){\n            //爬取的是HTML内容，需要转成MD格式的内容\n            String newContent = content.toString().replaceAll(\"<code>\", \"<code class=\\\"lang-java\\\">\");\n            MutableDataSet options = new MutableDataSet();\n            String markdown = FlexmarkHtmlConverter.builder(options).build().convert(newContent)\n                    .replace(\"lang-java\",\"java\");\n\n            //文章封面图片 由https://picsum.photos该网站随机获取\n            String url = MessageFormat.format(\"https://picsum.photos/id/{0}/info\", RanDomUtil.generationOneNumber(1000));\n            Map map = restTemplate.getForObject(url, Map.class);\n\n            Date nowDate = DateUtils.getNowDate();\n            BlogArticle entity = BlogArticle.builder().userId(SysConf.ADMIN_USER_ID).contentMd(markdown)\n                    .categoryId(SysConf.OTHER_CATEGORY_ID).isOriginal(YesOrNoEnum.NO.getCode())\n                    .title(title.toString()).avatar(map.get(\"download_url\").toString()).content(newContent)\n                    .createTime(nowDate).updateTime(nowDate).build();\n\n            articleMapper.insert(entity);\n            //为该文章添加标签\n            List<Long> tagsId = new ArrayList<>();\n            tagList.forEach(item ->{\n                Tags result = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(SqlConf.NAME, item.toString()));\n                if (result == null){\n                    result = Tags.builder().name(item.toString()).build();\n                    tagsMapper.insert(result);\n                }\n                tagsId.add(result.getId());\n            });\n            tagsMapper.saveArticleToTags(entity.getId(),tagsId);\n\n        }\n\n    }\n}\n```\n#### 调用\n```java\nSpider  spider = Spider.create(new CSDNPageProcessor()).addUrl(url);\n        spider.addPipeline(blogPipeline).thread(5).run();\n```\n\n> 以上展示是全部代码，如爬取CSDN博客的文章可直接复制，修改数据处理类的代码即可，有时间的会讲的更加清楚一点', 0, 0, 1, 1, NULL, 21, '', '2022-01-07 17:14:46', '', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (89, 7, 17, '微信公众号关键词自动回复', '\r\nhttp://r6aox1myz.hd-bkt.clouddn.com/1641893735342.jpg', '基于springboot的微信公众号关键词自动回复功能', '<p>大家好，我是拾壹</p>\n<h2><a id=\"_2\"></a>前言</h2>\n<p>博客今天上线一个文章私密的功能，该功能需要通过关注公众号来获取验证码，通过正确的验证码才能来查阅文章，具体效果如下图。<br />\n<img src=\"http://img.shiyit.com/1641892107383.jpg\" alt=\"16418920121.jpg\" /></p>\n<p><img src=\"http://img.shiyit.com/1644808396724.png\" alt=\"image.png\" /></p>\n<p>我感觉还蛮有意思的就决定把这个写出来，供大家去使用，下面来讲讲我的具体实现。</p>\n<h4><a id=\"1_11\"></a>1.首先需要自行前往微信公众平台创建公众号(博主已经有公众号就不在进行讲解这一步了)</h4>\n<h4><a id=\"2_13\"></a>2.在公众号平台选择基础配置进行服务器的配置，如下图</h4>\n<p><img src=\"http://img.shiyit.com/1641892470689.jpg\" alt=\"16418924261.jpg\" /></p>\n<ol>\n<li>url：填写你后端的接口地址，get请求</li>\n<li>token：自己随便定义一个</li>\n<li>EncodingAESKey：可以自己选择也可以随机生成</li>\n<li>加解密方式：根据自己的实际情况来选择</li>\n</ol>\n<h4><a id=\"3controller_20\"></a>3.controller层代码</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"> <span class=\"hljs-meta\">@ApiOperation(&quot;微信公众号服务器配置校验token&quot;)</span>\n    <span class=\"hljs-meta\">@RequestMapping(value = &quot;/test&quot;,method = RequestMethod.GET)</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">void</span> <span class=\"hljs-title function_\">checkToken</span><span class=\"hljs-params\">(HttpServletRequest request, HttpServletResponse response)</span> {\n        <span class=\"hljs-comment\">//token验证代码段</span>\n        <span class=\"hljs-keyword\">try</span> {\n            log.info(<span class=\"hljs-string\">&quot;请求已到达，开始校验token&quot;</span>);\n            <span class=\"hljs-keyword\">if</span> (StringUtils.isNotBlank(request.getParameter(<span class=\"hljs-string\">&quot;signature&quot;</span>))) {\n                <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">signature</span> <span class=\"hljs-operator\">=</span> request.getParameter(<span class=\"hljs-string\">&quot;signature&quot;</span>);\n                <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">timestamp</span> <span class=\"hljs-operator\">=</span> request.getParameter(<span class=\"hljs-string\">&quot;timestamp&quot;</span>);\n                <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">nonce</span> <span class=\"hljs-operator\">=</span> request.getParameter(<span class=\"hljs-string\">&quot;nonce&quot;</span>);\n                <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">echostr</span> <span class=\"hljs-operator\">=</span> request.getParameter(<span class=\"hljs-string\">&quot;echostr&quot;</span>);\n                log.info(<span class=\"hljs-string\">&quot;signature[{}], timestamp[{}], nonce[{}], echostr[{}]&quot;</span>, signature, timestamp, nonce, echostr);\n                <span class=\"hljs-keyword\">if</span> (WeChatUtil.checkSignature(signature, timestamp, nonce)) {\n                    log.info(<span class=\"hljs-string\">&quot;数据源为微信后台，将echostr[{}]返回！&quot;</span>, echostr);\n                    <span class=\"hljs-type\">BufferedOutputStream</span> <span class=\"hljs-variable\">out</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">BufferedOutputStream</span>(response.getOutputStream());\n                    out.write(echostr.getBytes());\n                    out.flush();\n                    out.close();\n                }\n            }\n        } <span class=\"hljs-keyword\">catch</span> (IOException e) {\n            log.error(<span class=\"hljs-string\">&quot;校验出错&quot;</span>);\n            e.printStackTrace();\n        }\n    }\n\n</code></div></pre>\n<h4><a id=\"4WeChatUtiltokentoken_49\"></a>4.WeChatUtil代码,需要注意此类里面的token需要与公众号平台填写的token一致</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"> <span class=\"hljs-keyword\">private</span> <span class=\"hljs-keyword\">static</span> <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">token</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-string\">&quot;demo123456&quot;</span>;\n\n    <span class=\"hljs-keyword\">private</span> <span class=\"hljs-keyword\">static</span> <span class=\"hljs-keyword\">final</span> <span class=\"hljs-type\">Logger</span> <span class=\"hljs-variable\">LOGGER</span> <span class=\"hljs-operator\">=</span> LoggerFactory.getLogger(WeChatUtil.class);\n\n    <span class=\"hljs-comment\">/**\n     * 校验签名\n     * <span class=\"hljs-doctag\">@param</span> signature 签名\n     * <span class=\"hljs-doctag\">@param</span> timestamp 时间戳\n     * <span class=\"hljs-doctag\">@param</span> nonce 随机数\n     * <span class=\"hljs-doctag\">@return</span> 布尔值\n     */</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">static</span> <span class=\"hljs-type\">boolean</span> <span class=\"hljs-title function_\">checkSignature</span><span class=\"hljs-params\">(String signature,String timestamp,String nonce)</span>{\n        <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">checktext</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-literal\">null</span>;\n        <span class=\"hljs-keyword\">if</span> (<span class=\"hljs-literal\">null</span> != signature) {\n            <span class=\"hljs-comment\">//对ToKen,timestamp,nonce 按字典排序</span>\n            String[] paramArr = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">String</span>[]{token,timestamp,nonce};\n            Arrays.sort(paramArr);\n            <span class=\"hljs-comment\">//将排序后的结果拼成一个字符串</span>\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">content</span> <span class=\"hljs-operator\">=</span> paramArr[<span class=\"hljs-number\">0</span>].concat(paramArr[<span class=\"hljs-number\">1</span>]).concat(paramArr[<span class=\"hljs-number\">2</span>]);\n\n            <span class=\"hljs-keyword\">try</span> {\n                <span class=\"hljs-type\">MessageDigest</span> <span class=\"hljs-variable\">md</span> <span class=\"hljs-operator\">=</span> MessageDigest.getInstance(<span class=\"hljs-string\">&quot;SHA-1&quot;</span>);\n                <span class=\"hljs-comment\">//对接后的字符串进行sha1加密</span>\n                <span class=\"hljs-type\">byte</span>[] digest = md.digest(content.getBytes());\n                checktext = byteToStr(digest);\n            } <span class=\"hljs-keyword\">catch</span> (NoSuchAlgorithmException e){\n                e.printStackTrace();\n            }\n        }\n        <span class=\"hljs-comment\">//将加密后的字符串与signature进行对比</span>\n        <span class=\"hljs-keyword\">return</span> checktext !=<span class=\"hljs-literal\">null</span> ? checktext.equals(signature.toUpperCase()) : <span class=\"hljs-literal\">false</span>;\n    }\n\n    <span class=\"hljs-comment\">/**\n     * 将字节数组转化我16进制字符串\n     * <span class=\"hljs-doctag\">@param</span> byteArrays 字符数组\n     * <span class=\"hljs-doctag\">@return</span> 字符串\n     */</span>\n    <span class=\"hljs-keyword\">private</span> <span class=\"hljs-keyword\">static</span> String <span class=\"hljs-title function_\">byteToStr</span><span class=\"hljs-params\">(<span class=\"hljs-type\">byte</span>[] byteArrays)</span>{\n        <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">str</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-string\">&quot;&quot;</span>;\n        <span class=\"hljs-keyword\">for</span> (<span class=\"hljs-type\">int</span> <span class=\"hljs-variable\">i</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-number\">0</span>; i &lt; byteArrays.length; i++) {\n            str += byteToHexStr(byteArrays[i]);\n        }\n        <span class=\"hljs-keyword\">return</span> str;\n    }\n\n    <span class=\"hljs-comment\">/**\n     *  将字节转化为十六进制字符串\n     * <span class=\"hljs-doctag\">@param</span> myByte 字节\n     * <span class=\"hljs-doctag\">@return</span> 字符串\n     */</span>\n    <span class=\"hljs-keyword\">private</span> <span class=\"hljs-keyword\">static</span> String <span class=\"hljs-title function_\">byteToHexStr</span><span class=\"hljs-params\">(<span class=\"hljs-type\">byte</span> myByte)</span> {\n        <span class=\"hljs-type\">char</span>[] Digit = {<span class=\"hljs-string\">&#x27;0&#x27;</span>,<span class=\"hljs-string\">&#x27;1&#x27;</span>,<span class=\"hljs-string\">&#x27;2&#x27;</span>,<span class=\"hljs-string\">&#x27;3&#x27;</span>,<span class=\"hljs-string\">&#x27;4&#x27;</span>,<span class=\"hljs-string\">&#x27;5&#x27;</span>,<span class=\"hljs-string\">&#x27;6&#x27;</span>,<span class=\"hljs-string\">&#x27;7&#x27;</span>,<span class=\"hljs-string\">&#x27;8&#x27;</span>,<span class=\"hljs-string\">&#x27;9&#x27;</span>,<span class=\"hljs-string\">&#x27;A&#x27;</span>,<span class=\"hljs-string\">&#x27;B&#x27;</span>,<span class=\"hljs-string\">&#x27;C&#x27;</span>,<span class=\"hljs-string\">&#x27;D&#x27;</span>,<span class=\"hljs-string\">&#x27;E&#x27;</span>,<span class=\"hljs-string\">&#x27;F&#x27;</span>};\n        <span class=\"hljs-type\">char</span>[] tampArr = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">char</span>[<span class=\"hljs-number\">2</span>];\n        tampArr[<span class=\"hljs-number\">0</span>] = Digit[(myByte &gt;&gt;&gt; <span class=\"hljs-number\">4</span>) &amp; <span class=\"hljs-number\">0X0F</span>];\n        tampArr[<span class=\"hljs-number\">1</span>] = Digit[myByte &amp; <span class=\"hljs-number\">0X0F</span>];\n        <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">str</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">String</span>(tampArr);\n        <span class=\"hljs-keyword\">return</span> str;\n    }\n</code></div></pre>\n<p>需要注意在公众号平台提交服务器配置时这些代码需要部署上线，否则会token失效。以上代码可直接复制，经过博主测试不会存在token失效问题。下面讲一下如何通过关键词来实现自动回复功能</p>\n<h4><a id=\"1pomxml_113\"></a>1.pom.xml引入依赖</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">     <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">dependency</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">groupId</span>&gt;</span>org.dom4j<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">groupId</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">artifactId</span>&gt;</span>dom4j<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">artifactId</span>&gt;</span>\n            <span class=\"hljs-tag\">&lt;<span class=\"hljs-name\">version</span>&gt;</span>2.1.3<span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">version</span>&gt;</span>\n        <span class=\"hljs-tag\">&lt;/<span class=\"hljs-name\">dependency</span>&gt;</span>\n</code></div></pre>\n<h4><a id=\"2controller_123\"></a>2.同样的是controller层添加如下代码</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\">    <span class=\"hljs-meta\">@ApiOperation(&quot;处理微信服务器的消息转发&quot;)</span>\n    <span class=\"hljs-meta\">@PostMapping(value = &quot;test&quot;)</span>\n    <span class=\"hljs-keyword\">public</span> String  <span class=\"hljs-title function_\">wechat</span><span class=\"hljs-params\">(HttpServletRequest request)</span> <span class=\"hljs-keyword\">throws</span> Exception {\n        <span class=\"hljs-comment\">// 调用parseXml方法解析请求消息</span>\n        Map&lt;String,String&gt; requestMap = WeChatUtil.parseXml(request);\n        <span class=\"hljs-comment\">// 消息类型</span>\n        <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">msgType</span> <span class=\"hljs-operator\">=</span> requestMap.get(<span class=\"hljs-string\">&quot;MsgType&quot;</span>);\n        <span class=\"hljs-comment\">// xml格式的消息数据</span>\n        <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">respXml</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-literal\">null</span>;\n        <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">mes</span> <span class=\"hljs-operator\">=</span> requestMap.get(<span class=\"hljs-string\">&quot;Content&quot;</span>);\n        <span class=\"hljs-comment\">// 文本消息</span>\n        <span class=\"hljs-keyword\">if</span> (<span class=\"hljs-string\">&quot;text&quot;</span>.equals(msgType) &amp;&amp; <span class=\"hljs-string\">&quot;验证码&quot;</span>.equals(mes)) {\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">code</span> <span class=\"hljs-operator\">=</span> RanDomUtil.generationNumber(<span class=\"hljs-number\">6</span>);\n            respXml=WeChatUtil.sendTextMsg(requestMap,code);\n            redisCache.setCacheObject(RedisConstants.WECHAT_CODE+code,code,<span class=\"hljs-number\">30</span>, TimeUnit.MINUTES);\n        }\n        <span class=\"hljs-keyword\">return</span> respXml;\n    }\n\n</code></div></pre>\n<blockquote>\n<p>此处需要注意 此接口和前面校验token的接口请求方式不同，这里是post请求，前面校验token是get请求，这里的接口地址不能乱写，需要和公众号平台填写的url相同，也就是前面校验token和这里接收请求的接口一致，只是请求方式不同</p>\n</blockquote>\n<h4><a id=\"3WeChatUtil_146\"></a>3.WeChatUtil添加如下代码</h4>\n<pre><div class=\"hljs\"><code class=\"lang-java\"> <span class=\"hljs-comment\">/**\n     * 解析微信发来的请求(xml)\n     *\n     * <span class=\"hljs-doctag\">@param</span> request\n     * <span class=\"hljs-doctag\">@return</span>\n     * <span class=\"hljs-doctag\">@throws</span> Exception\n     */</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">static</span> Map&lt;String,String&gt; <span class=\"hljs-title function_\">parseXml</span><span class=\"hljs-params\">(HttpServletRequest request)</span> <span class=\"hljs-keyword\">throws</span> Exception {\n        LOGGER.info(<span class=\"hljs-string\">&quot;请求已到达，开始解析参数...&quot;</span>);\n\n        <span class=\"hljs-comment\">// 将解析结果存储在HashMap中</span>\n        Map&lt;String,String&gt; map = <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HashMap</span>&lt;&gt;();\n\n        <span class=\"hljs-comment\">// 从request中取得输入流</span>\n        <span class=\"hljs-type\">InputStream</span> <span class=\"hljs-variable\">inputStream</span> <span class=\"hljs-operator\">=</span> request.getInputStream();\n        <span class=\"hljs-comment\">// 读取输入流</span>\n        <span class=\"hljs-type\">SAXReader</span> <span class=\"hljs-variable\">reader</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">SAXReader</span>();\n        <span class=\"hljs-type\">Document</span> <span class=\"hljs-variable\">document</span> <span class=\"hljs-operator\">=</span> reader.read(inputStream);\n        <span class=\"hljs-comment\">// 得到xml根元素</span>\n        <span class=\"hljs-type\">Element</span> <span class=\"hljs-variable\">root</span> <span class=\"hljs-operator\">=</span> document.getRootElement();\n        <span class=\"hljs-comment\">// 得到根元素的所有子节点</span>\n        List&lt;Element&gt; elementList = root.elements();\n        <span class=\"hljs-comment\">// 遍历所有子节点</span>\n        <span class=\"hljs-keyword\">for</span> (Element e : elementList) {\n            map.put(e.getName(), e.getText());\n        }\n\n        <span class=\"hljs-comment\">// 释放资源</span>\n        inputStream.close();\n        LOGGER.info(<span class=\"hljs-string\">&quot;参数解析完成。{}&quot;</span>,map);\n        <span class=\"hljs-keyword\">return</span> map;\n    }\n    <span class=\"hljs-comment\">/**\n     * 回复文本消息\n     * <span class=\"hljs-doctag\">@param</span> requestMap\n     * <span class=\"hljs-doctag\">@param</span> content\n     * <span class=\"hljs-doctag\">@return</span>\n     */</span>\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">static</span> String <span class=\"hljs-title function_\">sendTextMsg</span><span class=\"hljs-params\">(Map&lt;String,String&gt; requestMap,String content)</span>{\n\n        Map&lt;String,Object&gt; map= <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">HashMap</span>&lt;&gt;();\n        map.put(<span class=\"hljs-string\">&quot;ToUserName&quot;</span>, requestMap.get(<span class=\"hljs-string\">&quot;FromUserName&quot;</span>));\n        map.put(<span class=\"hljs-string\">&quot;FromUserName&quot;</span>,  requestMap.get(<span class=\"hljs-string\">&quot;ToUserName&quot;</span>));\n        map.put(<span class=\"hljs-string\">&quot;MsgType&quot;</span>, <span class=\"hljs-string\">&quot;text&quot;</span>);\n        map.put(<span class=\"hljs-string\">&quot;CreateTime&quot;</span>, <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">Date</span>().getTime());\n        map.put(<span class=\"hljs-string\">&quot;Content&quot;</span>, content);\n        <span class=\"hljs-keyword\">return</span>  mapToXML(map);\n    }\n    <span class=\"hljs-keyword\">public</span> <span class=\"hljs-keyword\">static</span> String <span class=\"hljs-title function_\">mapToXML</span><span class=\"hljs-params\">(Map map)</span> {\n        <span class=\"hljs-type\">StringBuffer</span> <span class=\"hljs-variable\">sb</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">new</span> <span class=\"hljs-title class_\">StringBuffer</span>();\n        sb.append(<span class=\"hljs-string\">&quot;&lt;xml&gt;&quot;</span>);\n        mapToXML2(map, sb);\n        sb.append(<span class=\"hljs-string\">&quot;&lt;/xml&gt;&quot;</span>);\n        LOGGER.info(<span class=\"hljs-string\">&quot;请求完成,返回参数：{}&quot;</span>,sb);\n        <span class=\"hljs-keyword\">try</span> {\n            <span class=\"hljs-keyword\">return</span> sb.toString();\n        } <span class=\"hljs-keyword\">catch</span> (Exception e) {\n        }\n        <span class=\"hljs-keyword\">return</span> <span class=\"hljs-literal\">null</span>;\n    }\n    <span class=\"hljs-keyword\">private</span> <span class=\"hljs-keyword\">static</span> <span class=\"hljs-keyword\">void</span> <span class=\"hljs-title function_\">mapToXML2</span><span class=\"hljs-params\">(Map map, StringBuffer sb)</span> {\n        <span class=\"hljs-type\">Set</span> <span class=\"hljs-variable\">set</span> <span class=\"hljs-operator\">=</span> map.keySet();\n        <span class=\"hljs-keyword\">for</span> (<span class=\"hljs-type\">Iterator</span> <span class=\"hljs-variable\">it</span> <span class=\"hljs-operator\">=</span> set.iterator(); it.hasNext();) {\n            <span class=\"hljs-type\">String</span> <span class=\"hljs-variable\">key</span> <span class=\"hljs-operator\">=</span> (String) it.next();\n            <span class=\"hljs-type\">Object</span> <span class=\"hljs-variable\">value</span> <span class=\"hljs-operator\">=</span> map.get(key);\n            <span class=\"hljs-keyword\">if</span> (<span class=\"hljs-literal\">null</span> == value)\n                value = <span class=\"hljs-string\">&quot;&quot;</span>;\n            <span class=\"hljs-keyword\">if</span> (value.getClass().getName().equals(<span class=\"hljs-string\">&quot;java.util.ArrayList&quot;</span>)) {\n                <span class=\"hljs-type\">ArrayList</span> <span class=\"hljs-variable\">list</span> <span class=\"hljs-operator\">=</span> (ArrayList) map.get(key);\n                sb.append(<span class=\"hljs-string\">&quot;&lt;&quot;</span> + key + <span class=\"hljs-string\">&quot;&gt;&quot;</span>);\n                <span class=\"hljs-keyword\">for</span> (<span class=\"hljs-type\">int</span> <span class=\"hljs-variable\">i</span> <span class=\"hljs-operator\">=</span> <span class=\"hljs-number\">0</span>; i &lt; list.size(); i++) {\n                    <span class=\"hljs-type\">HashMap</span> <span class=\"hljs-variable\">hm</span> <span class=\"hljs-operator\">=</span> (HashMap) list.get(i);\n                    mapToXML2(hm, sb);\n                }\n                sb.append(<span class=\"hljs-string\">&quot;&lt;/&quot;</span> + key + <span class=\"hljs-string\">&quot;&gt;&quot;</span>);\n\n            } <span class=\"hljs-keyword\">else</span> {\n                <span class=\"hljs-keyword\">if</span> (value <span class=\"hljs-keyword\">instanceof</span> HashMap) {\n                    sb.append(<span class=\"hljs-string\">&quot;&lt;&quot;</span> + key + <span class=\"hljs-string\">&quot;&gt;&quot;</span>);\n                    mapToXML2((HashMap) value, sb);\n                    sb.append(<span class=\"hljs-string\">&quot;&lt;/&quot;</span> + key + <span class=\"hljs-string\">&quot;&gt;&quot;</span>);\n                } <span class=\"hljs-keyword\">else</span> {\n                    sb.append(<span class=\"hljs-string\">&quot;&lt;&quot;</span> + key + <span class=\"hljs-string\">&quot;&gt;&lt;![CDATA[&quot;</span> + value + <span class=\"hljs-string\">&quot;]]&gt;&lt;/&quot;</span> + key + <span class=\"hljs-string\">&quot;&gt;&quot;</span>);\n                }\n\n            }\n\n        }\n    }\n</code></div></pre>\n<p>以上只是讲解了自动回复文本类型的功能，其他类型功能以后在进行讲解或可自行百度<br />\n最后献上完成的示例图<br />\n<img src=\"http://img.shiyit.com/1641893184104.jpg\" alt=\"16418931691.jpg\" /></p>\n<h2><a id=\"Bye_242\"></a>Bye</h2>\n', '大家好，我是拾壹\n\n## 前言\n博客今天上线一个文章私密的功能，该功能需要通过关注公众号来获取验证码，通过正确的验证码才能来查阅文章，具体效果如下图。\n![16418920121.jpg](http://img.shiyit.com/1641892107383.jpg)\n\n![image.png](http://img.shiyit.com/1644808396724.png)\n\n我感觉还蛮有意思的就决定把这个写出来，供大家去使用，下面来讲讲我的具体实现。\n\n\n#### 1.首先需要自行前往微信公众平台创建公众号(博主已经有公众号就不在进行讲解这一步了)\n\n#### 2.在公众号平台选择基础配置进行服务器的配置，如下图\n![16418924261.jpg](http://img.shiyit.com/1641892470689.jpg)\n1. url：填写你后端的接口地址，get请求\n2. token：自己随便定义一个\n3. EncodingAESKey：可以自己选择也可以随机生成\n4. 加解密方式：根据自己的实际情况来选择\n\n#### 3.controller层代码\n```java\n @ApiOperation(\"微信公众号服务器配置校验token\")\n    @RequestMapping(value = \"/test\",method = RequestMethod.GET)\n    public void checkToken(HttpServletRequest request, HttpServletResponse response) {\n        //token验证代码段\n        try {\n            log.info(\"请求已到达，开始校验token\");\n            if (StringUtils.isNotBlank(request.getParameter(\"signature\"))) {\n                String signature = request.getParameter(\"signature\");\n                String timestamp = request.getParameter(\"timestamp\");\n                String nonce = request.getParameter(\"nonce\");\n                String echostr = request.getParameter(\"echostr\");\n                log.info(\"signature[{}], timestamp[{}], nonce[{}], echostr[{}]\", signature, timestamp, nonce, echostr);\n                if (WeChatUtil.checkSignature(signature, timestamp, nonce)) {\n                    log.info(\"数据源为微信后台，将echostr[{}]返回！\", echostr);\n                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());\n                    out.write(echostr.getBytes());\n                    out.flush();\n                    out.close();\n                }\n            }\n        } catch (IOException e) {\n            log.error(\"校验出错\");\n            e.printStackTrace();\n        }\n    }\n\n```\n#### 4.WeChatUtil代码,需要注意此类里面的token需要与公众号平台填写的token一致\n```java\n private static String token = \"demo123456\";\n\n    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatUtil.class);\n\n    /**\n     * 校验签名\n     * @param signature 签名\n     * @param timestamp 时间戳\n     * @param nonce 随机数\n     * @return 布尔值\n     */\n    public static boolean checkSignature(String signature,String timestamp,String nonce){\n        String checktext = null;\n        if (null != signature) {\n            //对ToKen,timestamp,nonce 按字典排序\n            String[] paramArr = new String[]{token,timestamp,nonce};\n            Arrays.sort(paramArr);\n            //将排序后的结果拼成一个字符串\n            String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);\n\n            try {\n                MessageDigest md = MessageDigest.getInstance(\"SHA-1\");\n                //对接后的字符串进行sha1加密\n                byte[] digest = md.digest(content.getBytes());\n                checktext = byteToStr(digest);\n            } catch (NoSuchAlgorithmException e){\n                e.printStackTrace();\n            }\n        }\n        //将加密后的字符串与signature进行对比\n        return checktext !=null ? checktext.equals(signature.toUpperCase()) : false;\n    }\n\n    /**\n     * 将字节数组转化我16进制字符串\n     * @param byteArrays 字符数组\n     * @return 字符串\n     */\n    private static String byteToStr(byte[] byteArrays){\n        String str = \"\";\n        for (int i = 0; i < byteArrays.length; i++) {\n            str += byteToHexStr(byteArrays[i]);\n        }\n        return str;\n    }\n\n    /**\n     *  将字节转化为十六进制字符串\n     * @param myByte 字节\n     * @return 字符串\n     */\n    private static String byteToHexStr(byte myByte) {\n        char[] Digit = {\'0\',\'1\',\'2\',\'3\',\'4\',\'5\',\'6\',\'7\',\'8\',\'9\',\'A\',\'B\',\'C\',\'D\',\'E\',\'F\'};\n        char[] tampArr = new char[2];\n        tampArr[0] = Digit[(myByte >>> 4) & 0X0F];\n        tampArr[1] = Digit[myByte & 0X0F];\n        String str = new String(tampArr);\n        return str;\n    }\n```\n需要注意在公众号平台提交服务器配置时这些代码需要部署上线，否则会token失效。以上代码可直接复制，经过博主测试不会存在token失效问题。下面讲一下如何通过关键词来实现自动回复功能\n\n#### 1.pom.xml引入依赖\n```xml\n     <dependency>\n            <groupId>org.dom4j</groupId>\n            <artifactId>dom4j</artifactId>\n            <version>2.1.3</version>\n        </dependency>\n```\n\n\n#### 2.同样的是controller层添加如下代码\n```java\n    @ApiOperation(\"处理微信服务器的消息转发\")\n    @PostMapping(value = \"test\")\n    public String  wechat(HttpServletRequest request) throws Exception {\n        // 调用parseXml方法解析请求消息\n        Map<String,String> requestMap = WeChatUtil.parseXml(request);\n        // 消息类型\n        String msgType = requestMap.get(\"MsgType\");\n        // xml格式的消息数据\n        String respXml = null;\n        String mes = requestMap.get(\"Content\");\n        // 文本消息\n        if (\"text\".equals(msgType) && \"验证码\".equals(mes)) {\n            String code = RanDomUtil.generationNumber(6);\n            respXml=WeChatUtil.sendTextMsg(requestMap,code);\n            redisCache.setCacheObject(RedisConstants.WECHAT_CODE+code,code,30, TimeUnit.MINUTES);\n        }\n        return respXml;\n    }\n\n```\n> 此处需要注意 此接口和前面校验token的接口请求方式不同，这里是post请求，前面校验token是get请求，这里的接口地址不能乱写，需要和公众号平台填写的url相同，也就是前面校验token和这里接收请求的接口一致，只是请求方式不同\n#### 3.WeChatUtil添加如下代码\n```java\n /**\n     * 解析微信发来的请求(xml)\n     *\n     * @param request\n     * @return\n     * @throws Exception\n     */\n    public static Map<String,String> parseXml(HttpServletRequest request) throws Exception {\n        LOGGER.info(\"请求已到达，开始解析参数...\");\n\n        // 将解析结果存储在HashMap中\n        Map<String,String> map = new HashMap<>();\n\n        // 从request中取得输入流\n        InputStream inputStream = request.getInputStream();\n        // 读取输入流\n        SAXReader reader = new SAXReader();\n        Document document = reader.read(inputStream);\n        // 得到xml根元素\n        Element root = document.getRootElement();\n        // 得到根元素的所有子节点\n        List<Element> elementList = root.elements();\n        // 遍历所有子节点\n        for (Element e : elementList) {\n            map.put(e.getName(), e.getText());\n        }\n\n        // 释放资源\n        inputStream.close();\n        LOGGER.info(\"参数解析完成。{}\",map);\n        return map;\n    }\n    /**\n     * 回复文本消息\n     * @param requestMap\n     * @param content\n     * @return\n     */\n    public static String sendTextMsg(Map<String,String> requestMap,String content){\n\n        Map<String,Object> map= new HashMap<>();\n        map.put(\"ToUserName\", requestMap.get(\"FromUserName\"));\n        map.put(\"FromUserName\",  requestMap.get(\"ToUserName\"));\n        map.put(\"MsgType\", \"text\");\n        map.put(\"CreateTime\", new Date().getTime());\n        map.put(\"Content\", content);\n        return  mapToXML(map);\n    }\n    public static String mapToXML(Map map) {\n        StringBuffer sb = new StringBuffer();\n        sb.append(\"<xml>\");\n        mapToXML2(map, sb);\n        sb.append(\"</xml>\");\n        LOGGER.info(\"请求完成,返回参数：{}\",sb);\n        try {\n            return sb.toString();\n        } catch (Exception e) {\n        }\n        return null;\n    }\n    private static void mapToXML2(Map map, StringBuffer sb) {\n        Set set = map.keySet();\n        for (Iterator it = set.iterator(); it.hasNext();) {\n            String key = (String) it.next();\n            Object value = map.get(key);\n            if (null == value)\n                value = \"\";\n            if (value.getClass().getName().equals(\"java.util.ArrayList\")) {\n                ArrayList list = (ArrayList) map.get(key);\n                sb.append(\"<\" + key + \">\");\n                for (int i = 0; i < list.size(); i++) {\n                    HashMap hm = (HashMap) list.get(i);\n                    mapToXML2(hm, sb);\n                }\n                sb.append(\"</\" + key + \">\");\n\n            } else {\n                if (value instanceof HashMap) {\n                    sb.append(\"<\" + key + \">\");\n                    mapToXML2((HashMap) value, sb);\n                    sb.append(\"</\" + key + \">\");\n                } else {\n                    sb.append(\"<\" + key + \"><![CDATA[\" + value + \"]]></\" + key + \">\");\n                }\n\n            }\n\n        }\n    }\n```\n以上只是讲解了自动回复文本类型的功能，其他类型功能以后在进行讲解或可自行百度\n最后献上完成的示例图\n![16418931691.jpg](http://img.shiyit.com/1641893184104.jpg)\n\n## Bye', 0, 1, 1, 1, NULL, 51, '', '2022-01-11 17:27:11', '', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (100, 7, 20, 'springboot项目banner文件制作', 'https://i.picsum.photos/id/50/4608/3072.jpg?hmac=E6WgCk6MBOyuRjW4bypT6y-tFXyWQfC_LjIBYPUspxE', 'springboot项目banner文件制作', '<p>大家好，我是拾壹</p>\n<h4><a id=\"_1\"></a>前言</h4>\n<p>今天决定给博客项目加一个banner启动的文件，然后就找到了这么一个可以自定义文字生成springboot启动的banner网站,如下图</p>\n<p><img src=\"http://img.shiyit.com/1642144155567.png\" alt=\"image.png\" /></p>\n<p>简单实用，只需在输入框输入想要生成的文字即可，通过font下拉框可以选择多种款式</p>\n<h4><a id=\"_8\"></a>样例图</h4>\n<p><img src=\"http://img.shiyit.com/1642144340475.png\" alt=\"image.png\" /></p>\n<p>另外附上我自己使用的banner文件：</p>\n<pre><div class=\"hljs\"><code class=\"lang-txt\">${AnsiColor.GREEN}\n .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.\n| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |\n| |     _____    | || |    _______   | || |   ______     | || |   _____      | || |     ____     | || |    ______    | |\n| |    |_   _|   | || |   /  ___  |  | || |  |_   _ \\    | || |  |_   _|     | || |   .&#x27;    `.   | || |  .&#x27; ___  |   | |\n| |      | |     | || |  |  (__ \\_|  | || |    | |_) |   | || |    | |       | || |  /  .--.  \\  | || | / .&#x27;   \\_|   | |\n| |      | |     | || |   &#x27;.___`-.   | || |    |  __&#x27;.   | || |    | |   _   | || |  | |    | |  | || | | |    ____  | |\n| |     _| |_    | || |  |`\\____) |  | || |   _| |__) |  | || |   _| |__/ |  | || |  \\  `--&#x27;  /  | || | \\ `.___]  _| | |\n| |    |_____|   | || |  |_______.&#x27;  | || |  |_______/   | || |  |________|  | || |   `.____.&#x27;   | || |  `._____.&#x27;   | |\n| |              | || |              | || |              | || |              | || |              | || |              | |\n| &#x27;--------------&#x27; || &#x27;--------------&#x27; || &#x27;--------------&#x27; || &#x27;--------------&#x27; || &#x27;--------------&#x27; || &#x27;--------------&#x27; |\n &#x27;----------------&#x27;  &#x27;----------------&#x27;  &#x27;----------------&#x27;  &#x27;----------------&#x27;  &#x27;----------------&#x27;  &#x27;----------------&#x27;\n${AnsiColor.YELLOW}\nGitee: https://gitee.com/quequnlong/vue-admin-blog\nGitee: https://gitee.com/quequnlong/isblog-server\nGitee: https://gitee.com/quequnlong/blog-vue\n${AnsiColor.BLUE}\n</code></div></pre>\n<p>文件一些变量的描述：</p>\n<table>\n<thead>\n<tr>\n<th>变量</th>\n<th>描述</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td>${application.version}</td>\n<td>MANIFEST.MF中声明的应用版本号，例如Implementation-Version: 1.0会打印1.0</td>\n</tr>\n<tr>\n<td>${application.formatted-version}</td>\n<td>MANIFEST.MF中声明的被格式化后的应用版本号（被括号包裹且以v作为前缀），用于显示，例如(v1.0)</td>\n</tr>\n<tr>\n<td>${spring-boot.version}</td>\n<td>当前Spring Boot的版本号，例如1.4.1.RELEASE</td>\n</tr>\n<tr>\n<td>${AnsiColor.NAME}）</td>\n<td>NAME代表一种ANSI编码</td>\n</tr>\n</tbody>\n</table>\n<p>如有不懂可以私聊博主,生成banner的网站地址：http://patorjk.com/software/taag/#p=display&amp;f=Graffiti&amp;t=Type%20Something%20</p>\n<h4><a id=\"Bye_43\"></a>Bye</h4>\n', '大家好，我是拾壹\n#### 前言\n今天决定给博客项目加一个banner启动的文件，然后就找到了这么一个可以自定义文字生成springboot启动的banner网站,如下图\n\n![image.png](http://img.shiyit.com/1642144155567.png)\n\n简单实用，只需在输入框输入想要生成的文字即可，通过font下拉框可以选择多种款式\n\n#### 样例图\n\n![image.png](http://img.shiyit.com/1642144340475.png)\n\n另外附上我自己使用的banner文件：\n```txt\n${AnsiColor.GREEN}\n .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.\n| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |\n| |     _____    | || |    _______   | || |   ______     | || |   _____      | || |     ____     | || |    ______    | |\n| |    |_   _|   | || |   /  ___  |  | || |  |_   _ \\    | || |  |_   _|     | || |   .\'    `.   | || |  .\' ___  |   | |\n| |      | |     | || |  |  (__ \\_|  | || |    | |_) |   | || |    | |       | || |  /  .--.  \\  | || | / .\'   \\_|   | |\n| |      | |     | || |   \'.___`-.   | || |    |  __\'.   | || |    | |   _   | || |  | |    | |  | || | | |    ____  | |\n| |     _| |_    | || |  |`\\____) |  | || |   _| |__) |  | || |   _| |__/ |  | || |  \\  `--\'  /  | || | \\ `.___]  _| | |\n| |    |_____|   | || |  |_______.\'  | || |  |_______/   | || |  |________|  | || |   `.____.\'   | || |  `._____.\'   | |\n| |              | || |              | || |              | || |              | || |              | || |              | |\n| \'--------------\' || \'--------------\' || \'--------------\' || \'--------------\' || \'--------------\' || \'--------------\' |\n \'----------------\'  \'----------------\'  \'----------------\'  \'----------------\'  \'----------------\'  \'----------------\'\n${AnsiColor.YELLOW}\nGitee: https://gitee.com/quequnlong/vue-admin-blog\nGitee: https://gitee.com/quequnlong/isblog-server\nGitee: https://gitee.com/quequnlong/blog-vue\n${AnsiColor.BLUE}\n```\n文件一些变量的描述：\n\n|变量|描述|\n|-|-|-|\n|${application.version}|MANIFEST.MF中声明的应用版本号，例如Implementation-Version: 1.0会打印1.0|\n|${application.formatted-version}|MANIFEST.MF中声明的被格式化后的应用版本号（被括号包裹且以v作为前缀），用于显示，例如(v1.0)|\n|${spring-boot.version}|当前Spring Boot的版本号，例如1.4.1.RELEASE|\n|${AnsiColor.NAME}）|NAME代表一种ANSI编码|\n\n如有不懂可以私聊博主,生成banner的网站地址：http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20\n\n#### Bye', 0, 0, 1, 1, NULL, 13, '', '2022-01-14 15:19:20', '', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (101, NULL, 18, 'linux使用docker安装elasticsearch', '\r\nhttp://r6aox1myz.hd-bkt.clouddn.com/1643085445090.jpg', 'linux使用docker安装elasticsearch', '<p>大家好，我是拾壹</p>\n<h4><a id=\"_2\"></a>前言</h4>\n<p>原来服务器带不动elasticsearch，所以一直没有使用到，最近换了一个2核4G的服务器，完全可以带的动elasticsearch了，所以就着手安装elasticsearch，当然安装又是只能找度娘了，每次百度找这个总是要找好几篇才能安装成功，所以这次安装完就决定写文章以后就不用在去找别人的了。我也不废话 ，下面进入正题。</p>\n<h4><a id=\"1elasticsearch_5\"></a>1.拉取elasticsearch镜像</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">sudo docker pull elasticsearch:7.6.2\n</code></div></pre>\n<h4><a id=\"2_9\"></a>2.创建持久化文件</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">sudo mkdir -p /myfile/elasticsearch/config\nsudo mkdir -p /myfile/elasticsearch/data\n</code></div></pre>\n<h4><a id=\"3_14\"></a>3.配置详情</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">echo &quot;http.host: 0.0.0.0&quot; &gt;&gt; /myfile/elasticsearch/config/elasticsearch.yml\n</code></div></pre>\n<h4><a id=\"4_18\"></a>4.设置权限</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">sudo chmod -R 777 /myfile/elasticsearch/\n</code></div></pre>\n<h4><a id=\"5_22\"></a>5.设置虚拟内存</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">sudo sysctl -w vm.max_map_count=262144\n</code></div></pre>\n<h4><a id=\"6elasticsearch_27\"></a>6.启动elasticsearch</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\"> sudo docker run --name elasticsearch -p 9200:9200\\\n -e &quot;discovery.type=single-node&quot; \\\n -e ES_JAVA_OPTS=&quot;-Xms84m -Xmx512m&quot; \\\n -v/myfile/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \\\n -v /myfile/elasticsearch/data:/usr/share/elasticsearch/data \\\n -v /myfile/elasticsearch/plugins:/usr/share/elasticsearch/plugins \\\n -d elasticsearch:7.6.2\n</code></div></pre>\n<p>-p 端口映射<br />\n-e discovery.type=single-node 单点模式启动<br />\n-e ES_JAVA_OPTS=&quot;-Xms84m -Xmx512m&quot;：设置启动占用的内存范围,可以适当把512调小，本人是256也可以跑起来<br />\n-v 目录挂载<br />\n-d 后台运行</p>\n<h4><a id=\"_44\"></a>看到如下图片则说明启动成功</h4>\n<p><img src=\"https://img-blog.csdnimg.cn/20200729101102288.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MzQ4Njg2Mw==,size_16,color_FFFFFF,t_70\" alt /></p>\n<blockquote>\n<p>至此安装就就结束了，希望本篇博文可以帮到各位，既然安装了elasticsearch肯定就会需要同步数据到elasticsearch的啦，我自己使用的是logstash来进行同步的，以后我会写篇文章来讲解怎么同步</p>\n</blockquote>\n<h4><a id=\"Bye_50\"></a>Bye</h4>\n', '大家好，我是拾壹\n\n#### 前言\n原来服务器带不动elasticsearch，所以一直没有使用到，最近换了一个2核4G的服务器，完全可以带的动elasticsearch了，所以就着手安装elasticsearch，当然安装又是只能找度娘了，每次百度找这个总是要找好几篇才能安装成功，所以这次安装完就决定写文章以后就不用在去找别人的了。我也不废话 ，下面进入正题。\n\n#### 1.拉取elasticsearch镜像\n```xml\nsudo docker pull elasticsearch:7.6.2\n```\n#### 2.创建持久化文件\n```xml\nsudo mkdir -p /myfile/elasticsearch/config\nsudo mkdir -p /myfile/elasticsearch/data\n```\n#### 3.配置详情\n```xml\necho \"http.host: 0.0.0.0\" >> /myfile/elasticsearch/config/elasticsearch.yml\n```\n#### 4.设置权限\n```xml\nsudo chmod -R 777 /myfile/elasticsearch/\n```\n#### 5.设置虚拟内存\n```xml\nsudo sysctl -w vm.max_map_count=262144\n```\n\n#### 6.启动elasticsearch\n```xml\n sudo docker run --name elasticsearch -p 9200:9200\\\n -e \"discovery.type=single-node\" \\\n -e ES_JAVA_OPTS=\"-Xms84m -Xmx512m\" \\\n -v/myfile/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \\\n -v /myfile/elasticsearch/data:/usr/share/elasticsearch/data \\\n -v /myfile/elasticsearch/plugins:/usr/share/elasticsearch/plugins \\\n -d elasticsearch:7.6.2\n```\n \n -p 端口映射\n -e discovery.type=single-node 单点模式启动\n -e ES_JAVA_OPTS=\"-Xms84m -Xmx512m\"：设置启动占用的内存范围,可以适当把512调小，本人是256也可以跑起来\n -v 目录挂载\n -d 后台运行\n\n#### 看到如下图片则说明启动成功\n\n![](https://img-blog.csdnimg.cn/20200729101102288.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MzQ4Njg2Mw==,size_16,color_FFFFFF,t_70)\n\n> 至此安装就就结束了，希望本篇博文可以帮到各位，既然安装了elasticsearch肯定就会需要同步数据到elasticsearch的啦，我自己使用的是logstash来进行同步的，以后我会写篇文章来讲解怎么同步\n\n#### Bye\n\n', 0, 0, 1, 1, NULL, 7, '', '2022-01-25 11:57:48', '', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (102, NULL, 17, 'linux使用docker安装redis', '\r\nhttp://r6aox1myz.hd-bkt.clouddn.com/1643092063302.jpg', 'linux使用docker安装redis', '<p>大家好，我是拾壹</p>\n<h4><a id=\"_2\"></a>前言</h4>\n<p>大家在项目开发中redis是必不可少的，很多人都会使用redis，但是安装就可能需要百度了，我自己也是一到每次需要安装redis的时候就要开始百度了，但如果每次都是复制粘贴的话我感觉是记不住的。所以为了自己能够时刻复习就决定写一篇文章来记录安装过程。</p>\n<h4><a id=\"1redis_5\"></a>1.搜索redis版本</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">docker search redis\n</code></div></pre>\n<h4><a id=\"2_9\"></a>2.拉取镜像</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">docker pull redis\n</code></div></pre>\n<p>该命令是拉取最新版的redis，如需安装指定版本的就根据上面命令查出来的结果，选择一个版本替换其中的redis即可，</p>\n<h4><a id=\"3_15\"></a>3.新建挂载文件夹（路径自己定即可）</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">mkdir -p /root/docker/redis/data\nmkdir -p /root/docker/redis/conf\n</code></div></pre>\n<h4><a id=\"4confredisconfvim_redisconf_20\"></a>4.进入上一步创建的conf文件夹中，创建文件redis.conf，可以使用vim redis.conf也可使用可视化软件创建，并将下面内容写入</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">#bind 127.0.0.1 //允许远程连接\nprotected-mode no \nappendonly yes \nrequirepass 123456 \n</code></div></pre>\n<ul>\n<li>protected-mode：配置保护</li>\n<li>appendonly：持久化</li>\n<li>requirepass：密码</li>\n</ul>\n<h4><a id=\"5_30\"></a>5.启动容器</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">docker run --name redis -p 6379:6379 -v /root/docker/redis/data:/data -v /root/docker/redis/conf/redis.conf:/etc/redis/redis.conf -d redis redis-server /etc/redis/redis.conf --appendonly yes\n</code></div></pre>\n<p>输入docker ps查看容器是否运行成功，如果没有，输入“docker logs -f myredis” ，查询容器运行日志，就知道哪里出错了</p>\n<h4><a id=\"6redis_35\"></a>6.进入redis</h4>\n<pre><div class=\"hljs\"><code class=\"lang-xml\">docker exec -it myredis redis-cli\n</code></div></pre>\n<blockquote>\n<p>如以上都没问题就可以使用可视化工具连接redis了</p>\n</blockquote>\n<h4><a id=\"Bye_41\"></a>Bye</h4>\n', '大家好，我是拾壹\n\n#### 前言\n大家在项目开发中redis是必不可少的，很多人都会使用redis，但是安装就可能需要百度了，我自己也是一到每次需要安装redis的时候就要开始百度了，但如果每次都是复制粘贴的话我感觉是记不住的。所以为了自己能够时刻复习就决定写一篇文章来记录安装过程。\n\n#### 1.搜索redis版本\n```xml\ndocker search redis\n```\n#### 2.拉取镜像\n```xml\ndocker pull redis\n```\n该命令是拉取最新版的redis，如需安装指定版本的就根据上面命令查出来的结果，选择一个版本替换其中的redis即可，\n\n#### 3.新建挂载文件夹（路径自己定即可）\n```xml\nmkdir -p /root/docker/redis/data\nmkdir -p /root/docker/redis/conf\n```\n#### 4.进入上一步创建的conf文件夹中，创建文件redis.conf，可以使用vim redis.conf也可使用可视化软件创建，并将下面内容写入\n```xml\n#bind 127.0.0.1 //允许远程连接\nprotected-mode no \nappendonly yes \nrequirepass 123456 \n```\n- protected-mode：配置保护\n- appendonly：持久化\n- requirepass：密码\n#### 5.启动容器\n```xml\ndocker run --name redis -p 6379:6379 -v /root/docker/redis/data:/data -v /root/docker/redis/conf/redis.conf:/etc/redis/redis.conf -d redis redis-server /etc/redis/redis.conf --appendonly yes\n```\n输入docker ps查看容器是否运行成功，如果没有，输入“docker logs -f myredis” ，查询容器运行日志，就知道哪里出错了\n#### 6.进入redis\n```xml\ndocker exec -it myredis redis-cli\n```\n> 如以上都没问题就可以使用可视化工具连接redis了\n\n#### Bye\n', 0, 0, 1, 1, NULL, 6, '', '2022-01-25 14:26:24', '', '2022-02-21 04:00:01');
INSERT INTO `b_article` VALUES (110, NULL, 28, 'mysql实现按天、月、季、年统计', 'https://picsum.photos/id/791/5760/3840', 'mysql实现按天、月、季、年统计', '<p>大家好，我是拾壹</p>\n<h4><a id=\"_1\"></a>前言</h4>\n<p>最近在开发中碰到一个需要按天周月季年的统计查询需求，一开始脑袋发热的就是去写代码获取这个阶段的时间点，然而写着写着就代码乱起来了，于是我就百度找mysql函数，功夫不负有心人，找了关键的函数词，为了巩固我的sql知识，于是便写下该文章。</p>\n<h4><a id=\"_4\"></a>按天统计</h4>\n<pre><div class=\"hljs\"><code class=\"lang-sql\"><span class=\"hljs-keyword\">SELECT</span> DATE_FORMAT(statistics_time,<span class=\"hljs-string\">&#x27;%Y-%m-%d&#x27;</span>) <span class=\"hljs-keyword\">as</span> statistics_time,<span class=\"hljs-built_in\">count</span>(id) \n<span class=\"hljs-keyword\">FROM</span> st_fault_day <span class=\"hljs-keyword\">WHERE</span> <span class=\"hljs-keyword\">month</span>(statistics_time) <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">month</span>(curdate()) \n<span class=\"hljs-keyword\">GROUP</span> <span class=\"hljs-keyword\">BY</span> statistics_time\n</code></div></pre>\n<p>输出结果：<br />\n<img src=\"http://img.shiyit.com/1645170369478.png\" alt=\"image.png\" /></p>\n<blockquote>\n<p>以上的我加了条件只统计当前月的，month函数为返回当前时间的月份</p>\n</blockquote>\n<h4><a id=\"_14\"></a>按月统计</h4>\n<pre><div class=\"hljs\"><code class=\"lang-sql\"><span class=\"hljs-keyword\">select</span> DATE_FORMAT(statistics_time,<span class=\"hljs-string\">&#x27;%Y-%m&#x27;</span>) <span class=\"hljs-keyword\">as</span> statistics_time, <span class=\"hljs-built_in\">count</span>(<span class=\"hljs-operator\">*</span>) \n<span class=\"hljs-keyword\">from</span> st_fault_day <span class=\"hljs-keyword\">where</span> <span class=\"hljs-keyword\">year</span>(statistics_time) <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">year</span>(curdate()) \n<span class=\"hljs-keyword\">GROUP</span> <span class=\"hljs-keyword\">BY</span> DATE_FORMAT(statistics_time,<span class=\"hljs-string\">&#x27;%Y-%m&#x27;</span>) <span class=\"hljs-keyword\">ORDER</span> <span class=\"hljs-keyword\">BY</span> statistics_time <span class=\"hljs-keyword\">asc</span>\n</code></div></pre>\n<p>输出结果：<br />\n<img src=\"http://img.shiyit.com/1645170719063.png\" alt=\"image.png\" /></p>\n<blockquote>\n<p>同样这里我也加了条件 只统计今年的，year函数返回当前时间的年份</p>\n</blockquote>\n<h4><a id=\"_24\"></a>按季度统计</h4>\n<pre><div class=\"hljs\"><code class=\"lang-sql\"><span class=\"hljs-keyword\">SELECT</span> QUARTER(statistics_time) statistics_time,<span class=\"hljs-built_in\">count</span>(<span class=\"hljs-operator\">*</span>)\n <span class=\"hljs-keyword\">FROM</span> st_fault_day <span class=\"hljs-keyword\">WHERE</span>  <span class=\"hljs-keyword\">year</span>(statistics_time) <span class=\"hljs-operator\">=</span> <span class=\"hljs-keyword\">year</span>(curdate( ))\n<span class=\"hljs-keyword\">GROUP</span> <span class=\"hljs-keyword\">BY</span> \n    QUARTER(statistics_time)\n<span class=\"hljs-keyword\">ORDER</span> <span class=\"hljs-keyword\">BY</span> statistics_time <span class=\"hljs-keyword\">DESC</span>;\n</code></div></pre>\n<p>输出结果：<br />\n<img src=\"http://img.shiyit.com/1645170892988.png\" alt=\"image.png\" /></p>\n<blockquote>\n<p>其中statistics_time代表的第几个季度，1则代表今年的第一季度</p>\n</blockquote>\n<h4><a id=\"_36\"></a>按年统计</h4>\n<pre><div class=\"hljs\"><code class=\"lang-sql\"><span class=\"hljs-keyword\">SELECT</span> DATE_FORMAT(statistics_time,<span class=\"hljs-string\">&#x27;%Y&#x27;</span>) <span class=\"hljs-keyword\">as</span> statistics_time,<span class=\"hljs-built_in\">count</span>(<span class=\"hljs-operator\">*</span>)\n<span class=\"hljs-keyword\">FROM</span> st_fault_day\n<span class=\"hljs-keyword\">GROUP</span> <span class=\"hljs-keyword\">BY</span> DATE_FORMAT(statistics_time,<span class=\"hljs-string\">&#x27;%Y&#x27;</span>)\n<span class=\"hljs-keyword\">ORDER</span> <span class=\"hljs-keyword\">BY</span> statistics_time <span class=\"hljs-keyword\">asc</span>;\n</code></div></pre>\n<p>输出结果:<br />\n<img src=\"http://img.shiyit.com/1645171023175.png\" alt=\"image.png\" /></p>\n<h4><a id=\"_46\"></a>按自然周统计</h4>\n<pre><div class=\"hljs\"><code class=\"lang-sql\"><span class=\"hljs-keyword\">SELECT</span> DATE_FORMAT(statistics_time,<span class=\"hljs-string\">&#x27;%y年%u周&#x27;</span>) <span class=\"hljs-keyword\">as</span> w,<span class=\"hljs-built_in\">count</span>(<span class=\"hljs-operator\">*</span>)\n<span class=\"hljs-keyword\">FROM</span> st_fault_day\n<span class=\"hljs-keyword\">GROUP</span> <span class=\"hljs-keyword\">BY</span> DATE_FORMAT(statistics_time,<span class=\"hljs-string\">&#x27;%y%u&#x27;</span>)\n<span class=\"hljs-keyword\">ORDER</span> <span class=\"hljs-keyword\">BY</span> w <span class=\"hljs-keyword\">asc</span>;\n</code></div></pre>\n<p>输出结果:<br />\n<img src=\"http://img.shiyit.com/1645171141559.png\" alt=\"image.png\" /></p>\n<blockquote>\n<p>这个统计是今年的第多少个周，并不是我想要的当月的周统计，但还是写出来供参考</p>\n</blockquote>\n<h3><a id=\"Bye_57\"></a>Bye</h3>\n', '大家好，我是拾壹\n#### 前言\n最近在开发中碰到一个需要按天周月季年的统计查询需求，一开始脑袋发热的就是去写代码获取这个阶段的时间点，然而写着写着就代码乱起来了，于是我就百度找mysql函数，功夫不负有心人，找了关键的函数词，为了巩固我的sql知识，于是便写下该文章。\n\n#### 按天统计\n```sql\nSELECT DATE_FORMAT(statistics_time,\'%Y-%m-%d\') as statistics_time,count(id) \nFROM st_fault_day WHERE month(statistics_time) = month(curdate()) \nGROUP BY statistics_time\n```\n输出结果：\n![image.png](http://img.shiyit.com/1645170369478.png)\n> 以上的我加了条件只统计当前月的，month函数为返回当前时间的月份\n\n#### 按月统计\n```sql\nselect DATE_FORMAT(statistics_time,\'%Y-%m\') as statistics_time, count(*) \nfrom st_fault_day where year(statistics_time) = year(curdate()) \nGROUP BY DATE_FORMAT(statistics_time,\'%Y-%m\') ORDER BY statistics_time asc\n```\n输出结果：\n![image.png](http://img.shiyit.com/1645170719063.png)\n> 同样这里我也加了条件 只统计今年的，year函数返回当前时间的年份\n\n#### 按季度统计\n```sql\nSELECT QUARTER(statistics_time) statistics_time,count(*)\n FROM st_fault_day WHERE  year(statistics_time) = year(curdate( ))\nGROUP BY \n    QUARTER(statistics_time)\nORDER BY statistics_time DESC;\n```\n输出结果：\n![image.png](http://img.shiyit.com/1645170892988.png)\n> 其中statistics_time代表的第几个季度，1则代表今年的第一季度\n\n#### 按年统计\n```sql\nSELECT DATE_FORMAT(statistics_time,\'%Y\') as statistics_time,count(*)\nFROM st_fault_day\nGROUP BY DATE_FORMAT(statistics_time,\'%Y\')\nORDER BY statistics_time asc;\n```\n输出结果:\n![image.png](http://img.shiyit.com/1645171023175.png)\n\n#### 按自然周统计\n```sql\nSELECT DATE_FORMAT(statistics_time,\'%y年%u周\') as w,count(*)\nFROM st_fault_day\nGROUP BY DATE_FORMAT(statistics_time,\'%y%u\')\nORDER BY w asc;\n```\n输出结果:\n![image.png](http://img.shiyit.com/1645171141559.png)\n> 这个统计是今年的第多少个周，并不是我想要的当月的周统计，但还是写出来供参考\n\n### Bye', 0, 0, 1, 1, NULL, 9, '', '2022-02-18 16:01:08', '', '2022-02-21 04:00:01');

-- ----------------------------
-- Table structure for b_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `b_article_tag`;
CREATE TABLE `b_article_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_tag_1`(`article_id`) USING BTREE,
  INDEX `fk_article_tag_2`(`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 637 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_article_tag
-- ----------------------------
INSERT INTO `b_article_tag` VALUES (504, 9, 2);
INSERT INTO `b_article_tag` VALUES (505, 9, 1);
INSERT INTO `b_article_tag` VALUES (579, 87, 1);
INSERT INTO `b_article_tag` VALUES (580, 87, 14);
INSERT INTO `b_article_tag` VALUES (586, 5, 13);
INSERT INTO `b_article_tag` VALUES (587, 5, 1);
INSERT INTO `b_article_tag` VALUES (595, 101, 2);
INSERT INTO `b_article_tag` VALUES (596, 101, 18);
INSERT INTO `b_article_tag` VALUES (599, 102, 18);
INSERT INTO `b_article_tag` VALUES (600, 102, 17);
INSERT INTO `b_article_tag` VALUES (624, 14, 10);
INSERT INTO `b_article_tag` VALUES (625, 14, 1);
INSERT INTO `b_article_tag` VALUES (626, 100, 1);
INSERT INTO `b_article_tag` VALUES (628, 89, 1);
INSERT INTO `b_article_tag` VALUES (636, 110, 31);

-- ----------------------------
-- Table structure for b_category
-- ----------------------------
DROP TABLE IF EXISTS `b_category`;
CREATE TABLE `b_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `click_volume` int(10) NULL DEFAULT 0,
  `sort` int(11) NOT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_name`(`name`) USING BTREE COMMENT '博客分类名称'
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_category
-- ----------------------------
INSERT INTO `b_category` VALUES (12, '项目介绍', 0, 8, '2021-12-29 10:21:40', '2022-01-20 17:50:32');
INSERT INTO `b_category` VALUES (13, '生活随笔', 0, 4, '2021-12-29 10:22:09', '2021-12-29 10:23:40');
INSERT INTO `b_category` VALUES (16, '转载', 0, 3, '2021-12-29 10:41:45', NULL);
INSERT INTO `b_category` VALUES (17, '后端框架', 0, 9, '2021-12-29 14:00:49', '2022-01-21 10:23:18');
INSERT INTO `b_category` VALUES (18, '搜索框架', 0, 5, '2021-12-29 14:01:09', NULL);
INSERT INTO `b_category` VALUES (19, '爬虫', 0, 1, '2022-01-07 17:08:57', '2022-01-21 10:23:01');
INSERT INTO `b_category` VALUES (20, '小工具', 0, 7, '2022-01-14 15:05:58', '2022-01-20 17:46:18');
INSERT INTO `b_category` VALUES (28, '数据库', 0, 0, '2022-02-18 16:01:07', '2022-02-18 08:01:06');

-- ----------------------------
-- Table structure for b_comment
-- ----------------------------
DROP TABLE IF EXISTS `b_comment`;
CREATE TABLE `b_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '评论人ID',
  `article_id` bigint(20) NOT NULL COMMENT '文章id',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `reply_user_id` bigint(20) NULL DEFAULT NULL COMMENT '回复人id',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_comment
-- ----------------------------
INSERT INTO `b_comment` VALUES (13, 24, 14, '测试', NULL, NULL, '2022-01-06 09:37:47');

-- ----------------------------
-- Table structure for b_dict
-- ----------------------------
DROP TABLE IF EXISTS `b_dict`;
CREATE TABLE `b_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
  `is_publish` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否发布(1:是，0:否)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dict
-- ----------------------------
INSERT INTO `b_dict` VALUES (1, '用户性别', 'sys_user_sex', '1', '用户性别', '2021-11-19 09:24:23', '2021-11-27 22:03:54', 0);
INSERT INTO `b_dict` VALUES (2, '发布状态', 'sys_publish_status', '1', '发布状态列表', '2021-11-19 17:12:51', '2021-11-19 17:12:51', 0);
INSERT INTO `b_dict` VALUES (3, '搜索模式', 'sys_search_model', '1', '搜索模式：SQL搜索、全文检索', '2021-11-26 08:57:47', '2021-11-26 08:57:47', 2);
INSERT INTO `b_dict` VALUES (4, '系统是否', 'sys_yes_no', '1', '系统是否列表', '2021-11-26 14:03:12', '2021-11-26 14:03:12', 2);
INSERT INTO `b_dict` VALUES (5, '系统开关', 'sys_normal_disable', '1', '系统开关列表', '2021-11-26 15:16:43', '2021-11-26 15:16:43', 3);
INSERT INTO `b_dict` VALUES (6, '博客登录方式', 'sys_login_method', '1', '博客登录方式 账号密码、QQ、微博', '2021-11-27 13:52:38', '2021-11-27 13:52:38', 0);
INSERT INTO `b_dict` VALUES (7, '定时任务分组', 'sys_job_group', '1', '定时任务分组列表', '2021-12-10 08:53:30', '2021-12-10 08:53:30', 2);
INSERT INTO `b_dict` VALUES (8, '任务状态', 'sys_job_status', '1', '任务状态列表', '2021-12-10 09:01:10', '2021-12-10 09:01:10', 2);
INSERT INTO `b_dict` VALUES (9, '任务执行策略', 'sys_job_misfire', '1', '任务执行策略列表', '2021-12-10 11:11:48', '2021-12-10 11:12:04', 2);

-- ----------------------------
-- Table structure for b_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `b_dict_data`;
CREATE TABLE `b_dict_data`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_id` bigint(10) NOT NULL COMMENT '字典类型id',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标签',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典键值',
  `style` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回显样式',
  `is_default` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否默认（1是 0否）',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  `is_publish` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否发布(1:是，0:否)',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dict_data
-- ----------------------------
INSERT INTO `b_dict_data` VALUES (1, 2, '发布', '1', 'success', '1', 1, '1', NULL);
INSERT INTO `b_dict_data` VALUES (2, 2, '下架', '0', 'danger', '0', 0, '1', NULL);
INSERT INTO `b_dict_data` VALUES (3, 4, '是', '1', 'success', '1', 1, '1', '系统是否 是');
INSERT INTO `b_dict_data` VALUES (4, 4, '否', '0', 'warning', '0', 0, '1', '系统是否 否');
INSERT INTO `b_dict_data` VALUES (5, 5, '开启', '1', 'success', '1', 1, '1', '系统开关 开启');
INSERT INTO `b_dict_data` VALUES (6, 5, '关闭', '0', 'warning', '0', 2, '1', '系统开关 关闭');
INSERT INTO `b_dict_data` VALUES (7, 3, 'ES搜素', '1', 'success', '0', 1, '1', '搜索模式：开启ElasticSearch全文检索');
INSERT INTO `b_dict_data` VALUES (8, 3, 'SQL搜索', '0', 'warning', '1', 2, '1', '搜索模式：SQL搜索');
INSERT INTO `b_dict_data` VALUES (9, 6, '账号', '0', 'primary', '0', 1, '1', '账号密码登录');
INSERT INTO `b_dict_data` VALUES (10, 6, 'QQ', '2', 'success', '1', 2, '1', 'QQ登录');
INSERT INTO `b_dict_data` VALUES (11, 6, '微博', '3', 'danger', '0', 3, '1', '微博登录');
INSERT INTO `b_dict_data` VALUES (12, 1, '男', '1', '', '1', 1, '1', '性别 男');
INSERT INTO `b_dict_data` VALUES (13, 1, '女', '0', '', '1', 0, '1', '性别 女');
INSERT INTO `b_dict_data` VALUES (14, 7, '默认', 'DEFAULT', 'primary', '1', 1, '1', '默认分组');
INSERT INTO `b_dict_data` VALUES (15, 7, '系统', 'SYSTEM', 'warning', '0', 2, '1', '系统分组');
INSERT INTO `b_dict_data` VALUES (16, 8, '正常', '0', 'primary', '0', 1, '1', '正常状态');
INSERT INTO `b_dict_data` VALUES (17, 8, '暂停', '1', 'danger', '1', 2, '1', '暂停状态');
INSERT INTO `b_dict_data` VALUES (18, 9, '默认策略', '0', '', '1', 1, '1', '默认策略');
INSERT INTO `b_dict_data` VALUES (19, 9, '立即执行', '1', '', '0', 2, '1', '立即执行');
INSERT INTO `b_dict_data` VALUES (20, 9, '执行一次', '2', '', '0', 3, '1', '执行一次');
INSERT INTO `b_dict_data` VALUES (21, 9, '放弃执行', '3', '', '0', 4, '1', '放弃执行');
INSERT INTO `b_dict_data` VALUES (22, 6, '码云', '4', 'danger', '0', 4, '1', 'gitee登录');

-- ----------------------------
-- Table structure for b_exception_log
-- ----------------------------
DROP TABLE IF EXISTS `b_exception_log`;
CREATE TABLE `b_exception_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `operation` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `params` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `exception_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常对象json格式',
  `exception_message` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常简单信息,等同于e.getMessage',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_exception_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_feed_back
-- ----------------------------
DROP TABLE IF EXISTS `b_feed_back`;
CREATE TABLE `b_feed_back`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细内容',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '添加时间',
  `type` int(1) NOT NULL COMMENT '反馈类型 1:需求 2：缺陷',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_feed_back
-- ----------------------------

-- ----------------------------
-- Table structure for b_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `b_friend_link`;
CREATE TABLE `b_friend_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站名称',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站地址',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站头像地址',
  `info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网站描述',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT 'ENUM-状态:\"0-待审核\",\"1-通过\"',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '友情链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_friend_link
-- ----------------------------
INSERT INTO `b_friend_link` VALUES (1, '拾壹博客', 'http://www.shiyit.com', 'http://img.shiyit.com/1644807308537.jpg', '一个It技术的探索者\n\n\n', '', 6, 2, '2021-08-27 11:53:31', '2021-12-20 17:28:16');

-- ----------------------------
-- Table structure for b_job
-- ----------------------------
DROP TABLE IF EXISTS `b_job`;
CREATE TABLE `b_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_job
-- ----------------------------
INSERT INTO `b_job` VALUES (1, '自动更新文章阅读数', 'DEFAULT', 'blogTask.updateReadQuantity', '0 0 4 * * ?', '3', '1', '0', 'admin', '2021-12-08 17:16:40', '', NULL, '');
INSERT INTO `b_job` VALUES (2, '系统默认（无参）', 'DEFAULT', 'blogTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2021-12-09 09:09:21', '', NULL, '');
INSERT INTO `b_job` VALUES (3, '系统默认（有参）', 'DEFAULT', 'blogTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2021-12-09 09:09:21', '', NULL, '');
INSERT INTO `b_job` VALUES (6, '定时修改标签的点击量', 'DEFAULT', 'blogTask.autoTagsClickVolume', '0 0 3 * * ?', '0', '1', '0', 'quequnlong', '2021-12-17 15:37:20', '', NULL, '1');
INSERT INTO `b_job` VALUES (7, '定时删除当天验证通过的ip', 'DEFAULT', 'blogTask.removeCode', '0 30 23 * * ?', '0', '1', '0', 'blue', '2022-01-11 16:39:42', '', NULL, '1');

-- ----------------------------
-- Table structure for b_job_log
-- ----------------------------
DROP TABLE IF EXISTS `b_job_log`;
CREATE TABLE `b_job_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `stop_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 295 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_menu
-- ----------------------------
DROP TABLE IF EXISTS `b_menu`;
CREATE TABLE `b_menu`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级资源ID',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源编码',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `level` int(11) NULL DEFAULT NULL COMMENT '资源级别',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源图标',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型 menu、button',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重定向地址',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `hidden` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 263 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-权限资源表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_menu
-- ----------------------------
INSERT INTO `b_menu` VALUES (1, '0', '/system', 'Layout', '系统管理', 0, 5, 'el-icon-setting', 'menu', '', '2019-03-28 18:51:08', '2021-12-17 15:26:06', '/system/menu', 'system', '1');
INSERT INTO `b_menu` VALUES (2, '1', '/role', '/system/role', '角色管理', 1, 2, 'el-icon-user-solid', 'menu', '', '2019-03-30 14:00:03', '2021-11-16 15:40:42', '', 'role', '1');
INSERT INTO `b_menu` VALUES (3, '2', '/system/role/list', NULL, '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, '', NULL, '0');
INSERT INTO `b_menu` VALUES (4, '2', '/system/role/queryUserRole', NULL, '获取当前登录用户所拥有的权限', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-27 12:46:57', '', NULL, '0');
INSERT INTO `b_menu` VALUES (5, '2', '/system/role/update', '/system/role/update', '修改', 2, NULL, NULL, 'btn', '', '2021-09-24 15:57:33', '2021-11-11 18:09:44', '', NULL, '0');
INSERT INTO `b_menu` VALUES (6, '2', '/system/role/remove', '/system/role/update', '删除', 2, NULL, NULL, 'btn', '', '2021-09-27 11:33:32', '2021-11-11 18:09:36', '', NULL, '0');
INSERT INTO `b_menu` VALUES (7, '2', '/system/role/create', NULL, '添加', 2, 1, NULL, 'btn', NULL, '2021-11-13 21:14:07', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (8, '1', '/menu', '/system/menu', '菜单管理', 1, 5, 'el-icon-menu', 'menu', NULL, NULL, '2021-11-18 11:26:00', '', 'menu', '1');
INSERT INTO `b_menu` VALUES (9, '8', '/system/menu/getMenuTree', NULL, '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (10, '8', '/system/menu/create', NULL, '添加', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (11, '8', '/system/menu/update', '/system/menu/update', '修改', 2, 2, NULL, 'btn', NULL, '2021-11-11 16:56:34', '2021-11-11 18:10:09', NULL, '/system/menu/update', '0');
INSERT INTO `b_menu` VALUES (12, '8', '/system/menu/getMenuList', NULL, '获取所有的url', 2, 6, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (13, '8', '/system/menu/remove', '/system/menu/remove', '删除', 2, NULL, NULL, 'btn', '', '2021-09-27 11:45:33', '2021-11-11 18:10:03', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (14, '1', '/user', '/system/user', '用户管理', 1, 1, 'el-icon-user', 'menu', NULL, NULL, '2021-11-16 12:01:51', NULL, 'user', '1');
INSERT INTO `b_menu` VALUES (15, '14', '/system/user/list', '', '列表', 2, NULL, NULL, 'btn', '', '2021-09-27 15:33:19', '2021-11-11 18:10:22', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (16, '14', '/system/user/remove', '', '删除', 2, NULL, NULL, 'btn', '', '2021-09-27 16:36:42', '2021-11-11 18:10:27', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (17, '14', '/system/user/create', '', '添加', 2, NULL, NULL, 'btn', NULL, '2021-09-27 16:36:54', '2021-11-11 18:10:30', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (18, '14', '/system/user/update', '', '修改', 2, NULL, NULL, 'btn', '', '2021-09-27 16:59:38', '2021-11-11 18:10:34', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (19, '14', '/system/user/info', '', '详情', 2, NULL, NULL, 'btn', NULL, '2021-09-27 16:59:50', '2021-11-11 18:10:37', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (20, '14', '/system/user/getUserMenu', '', '获取用户权限', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:10:40', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (21, '14', '/system/user/updatePassword', '', '修改密码', 2, NULL, NULL, 'btn', NULL, '2021-11-09 17:23:58', '2021-11-11 18:10:51', NULL, '/system/user/update_password', '0');
INSERT INTO `b_menu` VALUES (22, '14', '/generateCode', NULL, '生成用户邀请码', 2, 5, NULL, 'btn', NULL, '2021-08-13 09:20:25', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (23, '14', '/system/user/logout', '', '退出登录', 2, NULL, NULL, 'btn', '', '2021-09-26 10:21:27', '2021-11-11 18:10:46', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (24, '1', '/api', '/system/api', '接口管理', 1, 4, 'el-icon-edit', 'menu', NULL, '2021-11-12 09:26:01', '2021-11-20 13:58:15', NULL, 'api', '1');
INSERT INTO `b_menu` VALUES (25, '24', '/system/menu/getMenuApi', '', '列表', 2, 1, '1', 'btn', NULL, '2021-11-12 10:55:11', NULL, NULL, '', '0');
INSERT INTO `b_menu` VALUES (26, '0', '/articles', 'Layout', '文章管理', 0, 1, 'el-icon-document-copy', 'menu', NULL, NULL, '2021-11-16 15:45:14', '/articles/index', '', '1');
INSERT INTO `b_menu` VALUES (27, '26', 'index', '/articles/index', '文章管理', 1, 1, 'el-icon-notebook-2', 'menu', NULL, NULL, '2021-11-16 15:41:57', '/articles/index', 'Articles', '1');
INSERT INTO `b_menu` VALUES (28, '27', '/system/article/list', '', '列表', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:11:17', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (29, '27', '/system/article/delete', '', '删除', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:11:21', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (30, '27', '/system/article/update', '', '修改', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:11:25', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (31, '27', '/system/article/add', '', '添加', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:11:32', NULL, '2', '0');
INSERT INTO `b_menu` VALUES (32, '27', '/system/article/info', '', '详情', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:11:35', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (33, '27', '/system/article/baiduSeo', '', 'SEO', 2, NULL, NULL, 'btn', '', '2021-10-15 10:38:19', '2021-11-11 18:11:41', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (35, '26', '/tags', '/articles/tags/index', '标签管理', 1, 2, 'el-icon-collection-tag', 'menu', NULL, NULL, '2021-11-18 11:25:18', NULL, 'Tags', '1');
INSERT INTO `b_menu` VALUES (36, '35', '/system/tags/list', NULL, '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (37, '35', '/system/tags/add', '', '新增', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:11:54', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (38, '35', '/system/tags/info', '', '详情', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:11:58', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (39, '35', '/system/tags/update', '', '修改', 2, NULL, NULL, 'btn', NULL, NULL, '2021-11-11 18:12:08', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (40, '35', '/system/tags/remove', '/sys/tags/remove', '删除', 2, NULL, NULL, 'btn', NULL, '2021-11-10 17:34:38', '2021-11-11 18:12:01', NULL, '/sys/tags/remove', '0');
INSERT INTO `b_menu` VALUES (41, '0', '/site', 'Layout', '网站管理', 0, 2, 'el-icon-guide', 'menu', NULL, NULL, '2021-11-16 15:48:41', '/friendLink/index', '', '1');
INSERT INTO `b_menu` VALUES (47, '245', '/messages', '/news/message', '留言管理', 1, 2, 'el-icon-chat-dot-round', 'menu', NULL, NULL, '2021-11-16 15:43:46', '/message/index', '/message', '1');
INSERT INTO `b_menu` VALUES (48, '47', '/system/message/list', '', '列表', 2, NULL, NULL, 'btn', NULL, '2021-09-26 11:50:33', '2021-11-11 18:12:56', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (49, '47', '/system/message/remove', NULL, '删除', 2, 1, NULL, NULL, NULL, NULL, '2021-11-15 15:36:28', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (50, '47', 'test', NULL, '回复', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (51, '41', 'friendLink', '/site/friendLink/index', '友情链接', 1, 3, 'el-icon-link', 'menu', NULL, NULL, '2021-11-16 15:43:55', NULL, 'friendLink', '1');
INSERT INTO `b_menu` VALUES (52, '51', '/system/friend/list', NULL, '列表', 2, NULL, NULL, 'btn', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (53, '51', '/system/friend/create', NULL, '添加', 2, 1, NULL, 'btn', NULL, '2021-11-12 16:52:26', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (54, '51', '/system/friend/update', NULL, '修改', 2, 1, NULL, 'btn', NULL, '2021-11-12 16:52:08', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (55, '51', '/system/friend/remove', NULL, '删除', 2, 1, NULL, 'btn', NULL, '2021-11-14 12:18:00', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (56, '0', '/logs', 'Layout', '日志管理', 0, 4, 'el-icon-document', 'menu', NULL, NULL, '2021-12-31 14:46:11', NULL, NULL, '1');
INSERT INTO `b_menu` VALUES (57, '56', 'index', '/logs/userLog/index', '用户日志', 1, 1, 'el-icon-coordinate', 'menu', NULL, NULL, '2021-11-17 10:02:31', NULL, 'userLogs', '1');
INSERT INTO `b_menu` VALUES (58, '57', '/system/userLog/list', '', '列表', 2, NULL, '', 'btn', NULL, NULL, '2021-11-11 18:13:47', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (59, '56', '/adminLog', '/logs/adminLog', '操作日志', 1, 2, 'el-icon-magic-stick', 'menu', NULL, '2021-11-10 17:49:02', '2021-11-17 10:02:41', NULL, 'adminLog', '1');
INSERT INTO `b_menu` VALUES (60, '59', '/system/adminLog/list', '/sys/adminLog/query_log', '列表', 2, NULL, NULL, 'btn', NULL, '2021-11-10 17:49:27', '2021-11-11 18:13:54', NULL, '/zwblog/adminLog', '0');
INSERT INTO `b_menu` VALUES (61, '56', 'exceptionLog', '/logs/exceptionLog', '异常日志', 1, 3, 'el-icon-cpu', 'menu', NULL, '2021-11-11 10:57:42', '2021-11-17 10:02:47', NULL, 'exceptionLog', '1');
INSERT INTO `b_menu` VALUES (62, '61', '/system/exceptionLog/list', '/sys/exceptionLog/query_log', '列表', 2, NULL, NULL, 'btn', NULL, '2021-11-11 11:05:47', '2021-11-11 18:13:59', NULL, '/sys/exceptionLog/query_log', '0');
INSERT INTO `b_menu` VALUES (63, '0', '/other', 'Layout', '其他', 0, 1, 'el-icon-more-outline', 'menu', NULL, '2021-11-12 09:29:15', NULL, NULL, 'other', '0');
INSERT INTO `b_menu` VALUES (64, '63', '/image', '/image', '图片管理', 1, 1, 'el-icon-picture-outline', 'menu', NULL, '2021-11-12 09:31:24', '2021-11-16 15:47:05', NULL, '/image', '0');
INSERT INTO `b_menu` VALUES (65, '64', '/file/delBatchFile', '', '删除', 2, NULL, NULL, 'btn', '', '2021-09-27 11:53:16', '2021-11-11 18:10:55', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (66, '63', '/home', '', '首页', 1, NULL, 'el-icon-s-home', 'menu', '', '2021-10-16 15:46:06', '2021-11-12 09:30:39', NULL, 'home', '0');
INSERT INTO `b_menu` VALUES (67, '66', '/system/home/lineCount', '', '顶部统计信息', 2, NULL, NULL, 'btn', NULL, '2021-10-16 15:46:56', '2021-11-27 11:51:56', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (164, '0', '/listener', 'Layout', '监控中心', 0, 6, 'el-icon-monitor', 'menu', NULL, '2021-11-16 11:48:04', NULL, NULL, 'listener', '1');
INSERT INTO `b_menu` VALUES (165, '164', '/server', '/listener/server', '服务监控', 1, 1, 'el-icon-light-rain', 'menu', NULL, '2021-11-16 11:49:16', '2021-12-10 08:47:17', NULL, 'server', '1');
INSERT INTO `b_menu` VALUES (166, '165', '/system/home/systemInfo', NULL, '查看', 2, 1, NULL, 'btn', NULL, '2021-11-16 11:50:03', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (169, '41', '/dict', '/site/dict/index', '字典管理', 1, 1, 'el-icon-heavy-rain', 'menu', NULL, '2021-11-25 17:37:43', '2021-12-10 15:28:52', NULL, 'dict', '1');
INSERT INTO `b_menu` VALUES (170, '169', '/system/dict/list', NULL, '列表', 2, 1, NULL, 'btn', NULL, '2021-11-25 17:38:04', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (171, '41', '/dictData', '/site/dict/data', '字典数据', 1, 2, 'el-icon-sunset', 'menu', NULL, '2021-11-25 17:53:25', '2021-12-11 20:11:50', NULL, 'dictData', '0');
INSERT INTO `b_menu` VALUES (172, '171', '/system/dictData/list', NULL, '列表', 2, 1, NULL, 'btn', NULL, '2021-11-25 17:53:52', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (173, '169', '/system/dict/add', NULL, '添加', 2, 1, NULL, 'btn', NULL, '2021-11-26 08:57:12', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (174, '169', '/system/dict/update', NULL, '修改', 2, 2, NULL, 'btn', NULL, '2021-11-26 08:57:29', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (175, '171', '/system/dictData/getDataByDictType', NULL, '类型集合字典数据', 2, 2, NULL, 'btn', NULL, '2021-11-26 09:55:27', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (176, '169', '/system/dict/delete', NULL, '删除', 2, 3, NULL, 'btn', NULL, '2021-11-26 11:22:21', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (177, '169', '/system/dict/deleteBatch', NULL, '批量删除', 2, NULL, '4', 'btn', NULL, '2021-11-26 11:22:37', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (178, '171', '/system/dictData/add', NULL, '添加', 2, 1, NULL, 'btn', NULL, '2021-11-26 14:06:04', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (179, '171', '/system/dictData/update', NULL, '修改', 2, 2, NULL, 'btn', NULL, '2021-11-26 14:06:18', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (180, '171', '/system/dictData/delete', NULL, '删除', 2, 3, NULL, 'btn', NULL, '2021-11-26 14:06:31', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (181, '171', '/system/dictData/deleteBatch', NULL, '批量删除', 2, 4, NULL, 'btn', NULL, '2021-11-26 14:06:46', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (182, '1', 'systemconfig', '/system/systemconfig/index', '系统配置', 1, 2, 'el-icon-setting', 'menu', NULL, '2021-11-26 15:06:11', '2021-11-27 12:53:08', NULL, 'systemconfig', '1');
INSERT INTO `b_menu` VALUES (183, '182', '/system/config/getConfig', NULL, '查询', 2, 1, NULL, 'btn', NULL, '2021-11-26 15:06:39', '2021-11-26 15:45:36', NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (184, '182', '/system/config/update', NULL, '修改', 2, 2, NULL, 'btn', NULL, '2021-11-26 15:55:47', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (185, '2', '/system/role/queryRoleId', NULL, '获取该角色所拥有的权限', 2, 1, NULL, 'btn', NULL, '2021-11-27 12:47:27', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (186, '41', 'webConfig', '/site/webConfig/index', '网站配置', 1, 3, 'el-icon-setting', 'menu', NULL, '2021-11-27 13:48:02', NULL, NULL, 'webConfig', '1');
INSERT INTO `b_menu` VALUES (187, '186', '/system/webConfig/list', NULL, '查询', 2, 1, NULL, 'btn', NULL, '2021-11-27 13:48:33', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (188, '186', '/system/webConfig/update', NULL, '修改', 2, 1, NULL, 'btn', NULL, '2021-11-27 14:12:42', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (189, '35', '/system/tags/deleteBatch', '', '批量删除', 2, 1, NULL, 'btn', '', '2021-11-28 12:44:48', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (191, '164', '/quartz', '/listener/quartz', '定时任务', 1, 2, 'el-icon-coordinate', 'menu', '', '2021-12-10 08:46:08', NULL, NULL, 'quartz', '1');
INSERT INTO `b_menu` VALUES (192, '191', '/system/job/list', '', '列表', 2, 1, NULL, 'btn', '', '2021-12-10 08:47:52', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (193, '191', '/system/job/add', '', '添加', 2, 2, NULL, 'btn', '', '2021-12-10 08:48:13', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (194, '191', '/system/job/update', '', '修改', 2, 3, NULL, 'btn', '', '2021-12-10 08:48:27', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (195, '191', '/system/job/delete', '', '删除', 2, 4, NULL, 'btn', '', '2021-12-10 08:48:45', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (196, '191', '/system/job/run', '', '立即执行', 2, 5, NULL, 'btn', '', '2021-12-10 08:52:15', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (197, '191', '/system/job/change', '', '修改状态', 2, 6, NULL, 'btn', '', '2021-12-10 08:52:42', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (198, '191', '/system/job/info', '', '详情', 2, 7, NULL, 'btn', '', '2021-12-10 10:09:27', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (199, '164', '/jobLog', '/listener/quartz/log', '任务日志', 1, 3, 'el-icon-help', 'menu', '', '2021-12-10 11:45:00', NULL, NULL, 'log', '0');
INSERT INTO `b_menu` VALUES (200, '199', '/system/jobLog/list', '', '列表', 2, 1, NULL, 'btn', '', '2021-12-10 11:45:23', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (201, '199', '/system/jobLog/deleteBatch', '', '批量删除', 2, 2, NULL, 'btn', '', '2021-12-10 13:50:17', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (202, '199', '/system/jobLog/clean', '', '清空', 2, 3, NULL, 'btn', '', '2021-12-10 13:50:28', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (203, '191', '/system/job/deleteBatch', '', '批量删除', 2, 8, NULL, 'btn', '', '2021-12-10 14:23:21', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (215, '27', '/system/article/reptile', '', '爬虫', 2, 6, NULL, 'btn', '', '2021-12-24 09:00:15', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (216, '35', '/system/tags/top', '', '标签置顶', 2, 5, NULL, 'btn', '', '2021-12-24 09:00:36', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (217, '41', '/pages', '/site/page', '页面管理', 1, 3, 'el-icon-document-remove', 'menu', '', '2021-12-27 11:47:36', NULL, NULL, '/pages', '1');
INSERT INTO `b_menu` VALUES (218, '217', '/system/page/list', '', '列表', 2, 1, NULL, 'btn', '', '2021-12-27 11:48:06', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (219, '217', '/system/page/add', '', '新增', 2, 2, NULL, 'btn', '', '2021-12-27 11:48:30', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (220, '217', '/system/page/update', '', '修改', 2, 3, NULL, 'btn', '', '2021-12-27 11:48:47', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (221, '217', '/system/page/delete', '', '删除', 2, 4, NULL, 'btn', '', '2021-12-27 11:49:01', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (223, '26', '/category', '/articles/category/index', '分类管理', 1, 3, 'el-icon-files', 'menu', '', '2021-12-29 10:05:12', '2021-12-29 10:08:05', NULL, '/category', '1');
INSERT INTO `b_menu` VALUES (224, '223', '/system/category/list', '', '列表', 2, 1, NULL, 'btn', '', '2021-12-29 10:05:38', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (225, '223', '/system/category/info', '', '详情', 2, 2, NULL, 'btn', '', '2021-12-29 10:05:58', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (226, '223', '/system/category/add', '', '新增', 2, 3, NULL, 'btn', '', '2021-12-29 10:06:18', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (227, '223', '/system/category/update', '', '修改', 2, 4, NULL, 'btn', '', '2021-12-29 10:06:33', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (228, '223', '/system/category/deleteBatch', '', '批量删除', 2, 5, NULL, 'btn', '', '2021-12-29 10:06:47', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (229, '223', '/system/category/top', '', '置顶', 2, 6, NULL, 'btn', '', '2021-12-29 10:07:06', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (230, '223', '/system/category/delete', '', '删除', 2, 7, NULL, 'btn', '', '2021-12-29 10:27:55', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (231, '249', '/albums', '/site/album/album', '相册列表', 1, 1, 'el-icon-camera', 'menu', '', '2021-12-30 16:29:09', '2022-01-06 15:00:26', NULL, '/album', '1');
INSERT INTO `b_menu` VALUES (232, '231', '/system/album/list', '', '列表', 2, 1, NULL, 'btn', '', '2021-12-30 16:31:50', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (233, '231', '/system/album/info', '', '详情', 2, 2, NULL, 'btn', '', '2021-12-30 16:32:15', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (234, '231', '/system/album/add', '', '新增', 2, 3, NULL, 'btn', '', '2021-12-30 16:32:33', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (235, '231', '/system/album/update', '', '修改', 2, 4, NULL, 'btn', '', '2021-12-30 16:42:49', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (236, '231', '/system/album/delete', '', '删除', 2, 5, NULL, 'btn', '', '2021-12-30 16:43:13', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (237, '249', '/photos', '/site/album/photo', '照片管理', 1, 2, 'el-icon-camera', 'menu', '', '2021-12-31 08:49:19', NULL, NULL, '/photo', '0');
INSERT INTO `b_menu` VALUES (238, '237', '/system/photo/list', '', '列表', 2, 1, NULL, 'btn', '', '2021-12-31 08:49:42', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (239, '237', '/system/photo/info', '', '详情', 2, 2, NULL, 'btn', '', '2021-12-31 08:50:10', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (241, '237', '/system/photo/add', '', '新增', 2, 3, NULL, 'btn', '', '2021-12-31 08:50:38', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (242, '237', '/system/photo/update', '', '修改', 2, 4, NULL, 'btn', '', '2021-12-31 08:50:52', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (243, '237', '/system/photo/deleteBatch', '', '批量删除', 2, 5, NULL, 'btn', '', '2021-12-31 08:51:08', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (244, '237', '/system/photo/movePhoto', '', '移动照片', 2, 5, NULL, 'btn', '', '2021-12-31 08:51:08', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (245, '0', '/news', 'Layout', '消息管理', 0, 3, 'el-icon-bell', 'menu', '', '2021-12-31 14:21:26', NULL, NULL, '/new', '1');
INSERT INTO `b_menu` VALUES (246, '47', '/system/message/passBatch', '', '批量通过', 2, 3, NULL, 'btn', '', '2021-12-31 14:35:29', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (247, '47', '/system/message/deleteBatch', '', '批量删除', 2, 4, NULL, 'btn', '', '2021-12-31 14:35:47', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (248, '66', '/web/comment/addComment', '', '发表评论', 2, 5, NULL, 'btn', '', '2022-01-06 09:27:46', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (249, '0', '/album', 'Layout', '相册管理', 0, 3, 'el-icon-picture-outline', 'menu', '', '2021-12-31 14:21:26', '2022-01-06 15:00:44', NULL, '/album', '1');
INSERT INTO `b_menu` VALUES (250, '57', '/system/userLog/delete', '', '删除', 2, 2, NULL, 'btn', '', '2022-01-06 15:41:01', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (251, '59', '/system/adminLog/delete', '', '删除', 2, 2, NULL, 'btn', '', '2022-01-06 15:41:27', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (252, '61', '/system/exceptionLog/delete', '', '删除', 2, 2, NULL, 'btn', '', '2022-01-06 15:41:49', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (253, '27', '/system/article/deleteBatch', '', '批量删除', 2, 6, NULL, 'btn', '', '2022-01-06 18:00:24', NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (254, '51', '/system/friend/top', '', '置顶', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (256, '245', '/feedbacks', '/news/feedback', '反馈管理', 1, 2, 'el-icon-warning-outline', 'menu', '', NULL, NULL, NULL, '/feedback', '1');
INSERT INTO `b_menu` VALUES (257, '256', '/system/feedback/list', '', '列表', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (258, '256', '/system/feedback/deleteBatch', '', '批量删除', 2, 2, NULL, 'btn', '批量删除反馈', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (260, '64', '/file/upload', '', '上传图片', 2, 1, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (261, '66', '/system/home/init', '', '首页各种统计信息', 2, 3, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');
INSERT INTO `b_menu` VALUES (262, '27', '/system/article/pubOrShelf', '', '发布或下架文章', 2, 4, NULL, 'btn', '', NULL, NULL, NULL, NULL, '0');

-- ----------------------------
-- Table structure for b_message
-- ----------------------------
DROP TABLE IF EXISTS `b_message`;
CREATE TABLE `b_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time` tinyint(10) NULL DEFAULT NULL,
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 0:审核  1：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_message
-- ----------------------------
INSERT INTO `b_message` VALUES (24, '测试', '2021-12-31 14:01:15', '我真的很好啊i', 'https://tva2.sinaimg.cn/crop.0.0.720.720.50/006gY6m1jw8exkv40jxvqj30k00k0gmp.jpg?KID=imgbed,tva&Expires=1640940521&ssig=IiFQU%2FO%2BAC', '113.246.243.81', '中国-湖南省-长沙市', 8, 1);
INSERT INTO `b_message` VALUES (25, '测试测试', '2021-12-31 14:02:51', '我真的很好啊i', 'https://tva2.sinaimg.cn/crop.0.0.720.720.50/006gY6m1jw8exkv40jxvqj30k00k0gmp.jpg?KID=imgbed,tva&Expires=1640940521&ssig=IiFQU%2FO%2BAC', '113.246.243.81', '中国-湖南省-长沙市', 8, 1);
INSERT INTO `b_message` VALUES (27, '测试123', '2021-12-31 14:56:53', '游客', 'http://img.shiyit.com/touristAvatar.png', '113.246.243.81', '中国-湖南省-长沙市', 8, 1);
INSERT INTO `b_message` VALUES (28, 'ceshi ', '2022-01-05 11:42:14', '游客', 'http://img.shiyit.com/touristAvatar.png', '182.135.162.57', '中国-四川省-资阳市', 8, 1);
INSERT INTO `b_message` VALUES (29, '测试', '2022-01-05 13:01:40', '我真的很好啊i', 'https://tva2.sinaimg.cn/crop.0.0.720.720.1024/006gY6m1jw8exkv40jxvqj30k00k0gmp.jpg?KID=imgbed,tva&Expires=1641365320&ssig=g6q0dhwbts', '220.202.225.132', '中国-湖南省-长沙市', 9, 1);
INSERT INTO `b_message` VALUES (30, '1', '2022-01-12 10:25:49', '游客', 'http://img.shiyit.com/touristAvatar.png', '123.144.63.191', '中国-重庆市-重庆市', 8, 1);
INSERT INTO `b_message` VALUES (32, '111', '2022-02-14 11:47:16', '游客', 'http://img.shiyit.com/touristAvatar.png', '116.169.13.45', '中国-四川省-成都市', 7, 1);

-- ----------------------------
-- Table structure for b_page
-- ----------------------------
DROP TABLE IF EXISTS `b_page`;
CREATE TABLE `b_page`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `page_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `page_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `page_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_page
-- ----------------------------
INSERT INTO `b_page` VALUES (1, '首页', 'home', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481190129.jpg', '2021-08-07 10:32:36', '2022-01-18 12:46:32');
INSERT INTO `b_page` VALUES (2, '归档', 'archive', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481212604.jpg', '2021-08-07 10:32:36', '2022-01-18 12:46:59');
INSERT INTO `b_page` VALUES (3, '分类', 'category', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481235438.jpg', '2021-08-07 10:32:36', '2022-01-18 12:47:17');
INSERT INTO `b_page` VALUES (4, '标签', 'tag', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481247590.png', '2021-08-07 10:32:36', '2022-01-18 12:47:29');
INSERT INTO `b_page` VALUES (5, '相册', 'album', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481260439.png', '2021-08-07 10:32:36', '2022-01-18 12:47:42');
INSERT INTO `b_page` VALUES (6, '友链', 'link', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481271507.jpg', '2021-08-07 10:32:36', '2022-01-18 12:47:53');
INSERT INTO `b_page` VALUES (7, '关于', 'about', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481282043.jpg', '2021-08-07 10:32:36', '2022-01-18 12:48:04');
INSERT INTO `b_page` VALUES (8, '留言', 'message', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481294001.png', '2021-08-07 10:32:36', '2022-01-18 12:48:15');
INSERT INTO `b_page` VALUES (9, '个人中心', 'user', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481301788.jpeg', '2021-08-07 10:32:36', '2022-01-18 12:48:23');
INSERT INTO `b_page` VALUES (10, '文章列表', 'articleList', 'http://r6aox1myz.hd-bkt.clouddn.com/1642481333013.jpg', '2021-08-10 15:36:19', '2022-01-18 12:48:54');

-- ----------------------------
-- Table structure for b_photo
-- ----------------------------
DROP TABLE IF EXISTS `b_photo`;
CREATE TABLE `b_photo`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `album_id` int(11) NOT NULL COMMENT '相册id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '照片名',
  `info` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '照片描述',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '照片地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '照片' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_photo
-- ----------------------------
INSERT INTO `b_photo` VALUES (1, 1, '图片一', '图片二', 'https://www.static.talkxj.com/articles/f2a62f4f273d06084cc918bf61123aae.jpg', '2021-12-29 17:26:15', '2021-12-29 17:26:17');
INSERT INTO `b_photo` VALUES (2, 1, '图片二', '图片二', 'https://www.static.talkxj.com/articles/57e40faa1d4b8055bab5cdc246778788.jpg', '2021-12-29 17:26:47', '2021-12-29 17:26:50');
INSERT INTO `b_photo` VALUES (3, 1, '图片三', '图片三', 'https://www.static.talkxj.com/articles/961c61a370cce5c0a2b233532bfa4139.png', '2021-12-29 17:26:47', '2021-12-29 17:26:50');
INSERT INTO `b_photo` VALUES (4, 1, '图片四', '图片四', 'https://www.static.talkxj.com/articles/002b90aefe681c846588df9a9a14fc47.jpg', '2021-12-29 17:26:47', '2021-12-29 17:26:50');
INSERT INTO `b_photo` VALUES (5, 1, '图片五', '图片五', 'https://www.static.talkxj.com/articles/f21704d1596d904d78cffd36293573a5.png', '2021-12-29 17:26:47', '2021-12-29 17:26:50');
INSERT INTO `b_photo` VALUES (6, 1, '图片六', '图片六', 'https://www.static.talkxj.com/articles/30225075a8dde449d44e77dc1917bc20.jpg', '2021-12-29 17:26:47', '2021-12-29 17:26:50');

-- ----------------------------
-- Table structure for b_photo_album
-- ----------------------------
DROP TABLE IF EXISTS `b_photo_album`;
CREATE TABLE `b_photo_album`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册名',
  `info` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册描述',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '相册封面',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态值 0公开 1私密',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '相册' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_photo_album
-- ----------------------------
INSERT INTO `b_photo_album` VALUES (1, '精选壁纸', '壁纸', 'https://www.static.talkxj.com/photos/f426814dd3748727443775b3e588f68f.png', 0, '2021-12-29 16:54:47', '2021-12-30 17:31:45');

-- ----------------------------
-- Table structure for b_role
-- ----------------------------
DROP TABLE IF EXISTS `b_role`;
CREATE TABLE `b_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-角色表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_role
-- ----------------------------
INSERT INTO `b_role` VALUES (1, 'admin', '管理员', '系统管理员', '2019-03-28 15:51:56', '2022-01-06 18:03:34');
INSERT INTO `b_role` VALUES (2, 'user', '用户', '用户', '2021-12-27 07:01:39', '2021-12-27 07:01:39');
INSERT INTO `b_role` VALUES (5, 'shiyiblog', '演示', '演示账号', '2021-11-14 12:23:25', '2022-01-06 18:03:43');

-- ----------------------------
-- Table structure for b_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `b_role_menu`;
CREATE TABLE `b_role_menu`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(10) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(10) NULL DEFAULT NULL COMMENT '菜单ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_id`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9627 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理 - 角色-权限资源关联表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_role_menu
-- ----------------------------
INSERT INTO `b_role_menu` VALUES (9193, 5, 1, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9194, 5, 2, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9195, 5, 3, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9196, 5, 4, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9197, 5, 5, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9198, 5, 6, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9199, 5, 7, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9200, 5, 185, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9201, 5, 8, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9202, 5, 9, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9203, 5, 10, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9204, 5, 11, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9205, 5, 12, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9206, 5, 13, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9207, 5, 14, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9208, 5, 15, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9209, 5, 16, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9210, 5, 17, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9211, 5, 18, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9212, 5, 19, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9213, 5, 20, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9214, 5, 21, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9215, 5, 22, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9216, 5, 23, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9217, 5, 24, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9218, 5, 25, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9219, 5, 182, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9220, 5, 183, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9221, 5, 184, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9222, 5, 26, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9223, 5, 27, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9224, 5, 28, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9225, 5, 29, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9226, 5, 30, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9227, 5, 31, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9228, 5, 32, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9229, 5, 33, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9230, 5, 215, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9231, 5, 253, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9232, 5, 35, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9233, 5, 36, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9234, 5, 37, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9235, 5, 38, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9236, 5, 39, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9237, 5, 40, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9238, 5, 189, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9239, 5, 216, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9240, 5, 223, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9241, 5, 224, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9242, 5, 225, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9243, 5, 226, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9244, 5, 227, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9245, 5, 228, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9246, 5, 229, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9247, 5, 230, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9248, 5, 41, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9249, 5, 51, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9250, 5, 52, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9251, 5, 53, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9252, 5, 54, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9253, 5, 55, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9254, 5, 254, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9255, 5, 169, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9256, 5, 170, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9257, 5, 173, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9258, 5, 174, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9259, 5, 176, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9260, 5, 177, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9261, 5, 171, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9262, 5, 172, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9263, 5, 175, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9264, 5, 178, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9265, 5, 179, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9266, 5, 180, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9267, 5, 181, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9268, 5, 186, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9269, 5, 187, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9270, 5, 188, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9271, 5, 217, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9272, 5, 218, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9273, 5, 219, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9274, 5, 220, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9275, 5, 221, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9276, 5, 56, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9277, 5, 57, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9278, 5, 58, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9279, 5, 250, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9280, 5, 59, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9281, 5, 60, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9282, 5, 251, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9283, 5, 61, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9284, 5, 62, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9285, 5, 252, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9286, 5, 64, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9287, 5, 65, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9288, 5, 67, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9289, 5, 68, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9290, 5, 167, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9291, 5, 168, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9292, 5, 190, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9293, 5, 255, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9294, 5, 164, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9295, 5, 165, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9296, 5, 166, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9297, 5, 191, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9298, 5, 192, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9299, 5, 193, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9300, 5, 194, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9301, 5, 195, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9302, 5, 196, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9303, 5, 197, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9304, 5, 198, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9305, 5, 203, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9306, 5, 199, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9307, 5, 200, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9308, 5, 201, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9309, 5, 202, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9310, 5, 245, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9311, 5, 47, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9312, 5, 48, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9313, 5, 49, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9314, 5, 50, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9315, 5, 246, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9316, 5, 247, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9317, 5, 256, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9318, 5, 257, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9319, 5, 258, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9320, 5, 249, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9321, 5, 231, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9322, 5, 232, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9323, 5, 233, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9324, 5, 234, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9325, 5, 235, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9326, 5, 236, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9327, 5, 237, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9328, 5, 238, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9329, 5, 239, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9330, 5, 241, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9331, 5, 242, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9332, 5, 243, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9333, 5, 244, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9334, 5, 63, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9335, 5, 66, '2022-01-14 05:32:01', '2022-01-14 05:32:01');
INSERT INTO `b_role_menu` VALUES (9481, 1, 1, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9482, 1, 2, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9483, 1, 3, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9484, 1, 4, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9485, 1, 5, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9486, 1, 6, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9487, 1, 7, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9488, 1, 185, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9489, 1, 8, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9490, 1, 9, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9491, 1, 10, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9492, 1, 11, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9493, 1, 12, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9494, 1, 13, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9495, 1, 14, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9496, 1, 15, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9497, 1, 16, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9498, 1, 17, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9499, 1, 18, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9500, 1, 19, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9501, 1, 20, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9502, 1, 21, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9503, 1, 22, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9504, 1, 23, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9505, 1, 24, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9506, 1, 25, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9507, 1, 182, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9508, 1, 183, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9509, 1, 184, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9510, 1, 26, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9511, 1, 27, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9512, 1, 28, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9513, 1, 29, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9514, 1, 30, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9515, 1, 31, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9516, 1, 32, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9517, 1, 33, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9518, 1, 215, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9519, 1, 253, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9520, 1, 35, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9521, 1, 36, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9522, 1, 37, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9523, 1, 38, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9524, 1, 39, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9525, 1, 40, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9526, 1, 189, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9527, 1, 216, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9528, 1, 223, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9529, 1, 224, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9530, 1, 225, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9531, 1, 226, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9532, 1, 227, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9533, 1, 228, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9534, 1, 229, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9535, 1, 230, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9536, 1, 41, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9537, 1, 51, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9538, 1, 52, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9539, 1, 53, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9540, 1, 54, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9541, 1, 55, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9542, 1, 254, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9543, 1, 169, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9544, 1, 170, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9545, 1, 173, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9546, 1, 174, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9547, 1, 176, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9548, 1, 177, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9549, 1, 171, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9550, 1, 172, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9551, 1, 175, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9552, 1, 178, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9553, 1, 179, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9554, 1, 180, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9555, 1, 181, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9556, 1, 186, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9557, 1, 187, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9558, 1, 188, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9559, 1, 217, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9560, 1, 218, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9561, 1, 219, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9562, 1, 220, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9563, 1, 221, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9564, 1, 56, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9565, 1, 57, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9566, 1, 58, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9567, 1, 250, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9568, 1, 59, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9569, 1, 60, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9570, 1, 251, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9571, 1, 61, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9572, 1, 62, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9573, 1, 252, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9574, 1, 63, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9575, 1, 64, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9576, 1, 65, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9577, 1, 66, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9578, 1, 67, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9579, 1, 68, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9580, 1, 167, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9581, 1, 168, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9582, 1, 190, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9583, 1, 222, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9584, 1, 248, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9585, 1, 255, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9586, 1, 259, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9587, 1, 164, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9588, 1, 165, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9589, 1, 166, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9590, 1, 191, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9591, 1, 192, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9592, 1, 193, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9593, 1, 194, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9594, 1, 195, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9595, 1, 196, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9596, 1, 197, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9597, 1, 198, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9598, 1, 203, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9599, 1, 199, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9600, 1, 200, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9601, 1, 201, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9602, 1, 202, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9603, 1, 245, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9604, 1, 47, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9605, 1, 48, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9606, 1, 49, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9607, 1, 50, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9608, 1, 246, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9609, 1, 247, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9610, 1, 256, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9611, 1, 257, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9612, 1, 258, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9613, 1, 249, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9614, 1, 231, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9615, 1, 232, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9616, 1, 233, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9617, 1, 234, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9618, 1, 235, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9619, 1, 236, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9620, 1, 237, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9621, 1, 238, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9622, 1, 239, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9623, 1, 241, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9624, 1, 242, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9625, 1, 243, '2022-01-14 08:55:41', '2022-01-14 08:55:41');
INSERT INTO `b_role_menu` VALUES (9626, 1, 244, '2022-01-14 08:55:41', '2022-01-14 08:55:41');

-- ----------------------------
-- Table structure for b_system_config
-- ----------------------------
DROP TABLE IF EXISTS `b_system_config`;
CREATE TABLE `b_system_config`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `qi_niu_access_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云公钥',
  `qi_niu_secret_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云私钥',
  `qi_niu_area` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)',
  `qi_niu_bucket` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云上传空间',
  `qi_niu_picture_base_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '七牛云域名前缀：http://images.moguit.cn',
  `upload_qi_niu` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件上传七牛云(0:否， 1:是)',
  `open_email_activate` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开启注册用户邮件激活(0:否， 1:是)',
  `start_email_notification` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否开启邮件通知(0:否， 1:是)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `open_dashboard_notification` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否开启仪表盘通知(0:否， 1:是)',
  `dashboard_notification_md` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '仪表盘通知【用于首次登录弹框】MD',
  `dashboard_notification` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '仪表盘通知【用于首次登录弹框】',
  `search_model` int(1) NOT NULL DEFAULT 0 COMMENT '搜索模式【0:SQL搜索 、1：全文检索】',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_system_config
-- ----------------------------
INSERT INTO `b_system_config` VALUES (2, '', '', NULL, '', '', '1', '1', '1', '2021-11-26 15:41:36', '2021-11-26 15:41:36', '1', '欢迎来到Isblog博客项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~\n\n项目源码：[点我传送](https://gitee.com/quequnlong/vue-admin-blog)，项目官网：[点我传送](http://www.isblog.com.cn)\n\n项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈\n\n最低配置：1核2G 需要开启虚拟内存\n\n推荐配置：2核4G 【狂欢特惠】\n\n服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话\n\n欢迎朋友能够给予一些支持，非常感谢~（ps.. 小伙伴赞赏的时候可以备注一下下~）\n|支付宝|微信|\n|-|-|-|\n|![支付宝](http://img.isblog.com.cn/alipay.png)|![微信](http://img.isblog.com.cn/wxpay.png)|\n', '<p>欢迎来到Isblog博客项目，开源项目离不开大家的支持，希望小伙伴能随手点赞一下，你的点赞就是我维护的动力~</p>\n<p>项目源码：<a href=\"https://gitee.com/quequnlong/vue-admin-blog\" target=\"_blank\">点我传送</a>，项目官网：<a href=\"http://www.isblog.com.cn\" target=\"_blank\">点我传送</a></p>\n<p>项目还在开发阶段，如有不善的地方欢迎各位小伙伴多多反馈</p>\n<p>最低配置：1核2G 需要开启虚拟内存</p>\n<p>推荐配置：2核4G 【狂欢特惠】</p>\n<p>服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话</p>\n<p>欢迎朋友能够给予一些支持，非常感谢~（ps… 小伙伴赞赏的时候可以备注一下下~）</p>\n<table>\n<thead>\n<tr>\n<th>支付宝</th>\n<th>微信</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td><img src=\"http://img.isblog.com.cn/alipay.png\" alt=\"支付宝\" /></td>\n<td><img src=\"http://img.isblog.com.cn/wxpay.png\" alt=\"微信\" /></td>\n</tr>\n</tbody>\n</table>\n', 1);

-- ----------------------------
-- Table structure for b_tags
-- ----------------------------
DROP TABLE IF EXISTS `b_tags`;
CREATE TABLE `b_tags`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签名称',
  `click_volume` int(10) NULL DEFAULT 0,
  `sort` int(11) NOT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tag_name`(`name`) USING BTREE COMMENT '博客标签名称'
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_tags
-- ----------------------------
INSERT INTO `b_tags` VALUES (1, 'springboot', 0, 16, '2021-11-12 14:43:27', '2022-01-24 09:30:25');
INSERT INTO `b_tags` VALUES (2, 'elasticsearch', 0, 10, '2021-11-12 14:43:27', NULL);
INSERT INTO `b_tags` VALUES (10, 'blog', 0, 17, '2021-11-12 14:43:27', '2022-01-24 09:31:06');
INSERT INTO `b_tags` VALUES (12, 'vue', 0, 1, '2021-12-29 14:01:50', '2021-12-29 14:01:50');
INSERT INTO `b_tags` VALUES (13, 'springcloud', 0, 1, '2021-12-29 14:02:32', '2021-12-29 14:02:32');
INSERT INTO `b_tags` VALUES (14, 'webmagic', 0, 2, '2022-01-07 17:09:12', '2022-01-07 17:09:12');
INSERT INTO `b_tags` VALUES (15, 'markdown', 0, 0, '2022-01-14 06:04:17', '2022-01-14 06:04:17');
INSERT INTO `b_tags` VALUES (17, 'redis', 0, 3, '2022-01-25 14:09:03', '2022-01-25 06:09:02');
INSERT INTO `b_tags` VALUES (18, 'linux', 0, 4, '2022-01-25 14:09:17', '2022-01-25 06:09:16');
INSERT INTO `b_tags` VALUES (29, 'IDEA', 0, 0, '2022-02-15 15:12:49', '2022-02-15 07:12:48');
INSERT INTO `b_tags` VALUES (30, 'CamelCase ', 0, 0, '2022-02-15 15:12:49', '2022-02-15 07:12:48');
INSERT INTO `b_tags` VALUES (31, 'mysql', 0, 0, '2022-02-18 16:01:07', '2022-02-18 08:01:06');

-- ----------------------------
-- Table structure for b_user
-- ----------------------------
DROP TABLE IF EXISTS `b_user`;
CREATE TABLE `b_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(10) NULL DEFAULT 0 COMMENT '状态 0:禁用 1:正常',
  `login_type` int(10) NULL DEFAULT NULL COMMENT '登录方式',
  `user_auth_id` bigint(10) NULL DEFAULT NULL COMMENT '用户详情id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `ip_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `last_login_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-用户基础信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_user
-- ----------------------------
INSERT INTO `b_user` VALUES (1, 'test', '$2a$10$Qu489q2YndVDnQ85PAjNl.spaRQkSgMdLq6Epvh/2KW0Y1TCk.J0G', '演示账号', 'http://img.shiyit.com/1639990238271.jpg', '2021-11-14 12:35:03', '2022-02-19 21:53:31', 1, 0, NULL, 5, '60.255.33.67', '中国-四川省-巴中市', '2022-02-19 21:53:31');

-- ----------------------------
-- Table structure for b_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `b_user_auth`;
CREATE TABLE `b_user_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱号',
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户简介',
  `web_site` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人网站',
  `is_disable` int(1) NOT NULL DEFAULT 1 COMMENT '是否禁用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_user_auth
-- ----------------------------

-- ----------------------------
-- Table structure for b_user_log
-- ----------------------------
DROP TABLE IF EXISTS `b_user_log`;
CREATE TABLE `b_user_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作用户ID',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地址',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作日志',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作模块',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作结果',
  `access_os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `client_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6133 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_user_role
-- ----------------------------
DROP TABLE IF EXISTS `b_user_role`;
CREATE TABLE `b_user_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(10) NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` int(10) NULL DEFAULT NULL COMMENT '用户ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理 - 用户角色关联表 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_user_role
-- ----------------------------
INSERT INTO `b_user_role` VALUES (12, 1, 1, '2019-08-21 10:49:41', '2019-08-21 10:49:41');

-- ----------------------------
-- Table structure for b_web_config
-- ----------------------------
DROP TABLE IF EXISTS `b_web_config`;
CREATE TABLE `b_web_config`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'logo(文件UID)',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '网站名称',
  `summary` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '介绍',
  `keyword` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关键字',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者',
  `record_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备案号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `web_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站地址',
  `ali_pay` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝收款码FileId',
  `weixin_pay` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信收款码FileId',
  `github` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'gitee地址',
  `qq_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `show_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）',
  `login_type_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）',
  `open_comment` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `open_admiration` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启赞赏(0:否， 1:是)',
  `tourist_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '游客头像',
  `bulletin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告',
  `author_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者简介',
  `author_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者头像',
  `about_me` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关于我',
  `is_music_player` int(10) NULL DEFAULT 0 COMMENT '是否开启音乐播放器',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '网站配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_web_config
-- ----------------------------
INSERT INTO `b_web_config` VALUES (1, 'site-log.ico', '拾壹博客', '机会向来都是自己争取的。', '拾壹,拾壹博客', '拾壹', '湘ICP备2022002110号-1', '2021-11-27 13:43:16', '2022-01-20 13:30:44', 'http://www.shiyit.com', '1639990067114.png', '1639990061772.png', 'https://github.com/quequnlong', 'https://gitee.com/quequnlong', '1248954763', '1248954763@qq.com', '1,2,3,4', '1,4,3', '1', 1, 'touristAvatar.png', '个人开源博客后台管理项目码云地址:https://gitee.com/quequnlong/vue-admin-blog', '永远都是一个学者。', '1644807308537.jpg', '> 特别鸣谢博客作者：[gitee仓库](https://gitee.com/feng_meiyu/blog)\n#### 前端用的vue，后端用的springboot\n#### 后台管理gitee地址：https://gitee.com/quequnlong/vue-admin-blog\n', 1);

SET FOREIGN_KEY_CHECKS = 1;

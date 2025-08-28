# Keepa选品工具系统

基于RuoYi-Vue-Plus框架开发的亚马逊卖家选品工具，通过集成Keepa API实现商品数据采集、分析和筛选。

## 系统架构

### 技术栈
- **后端框架**: Spring Boot 3.4.7 + RuoYi-Vue-Plus
- **数据库**: MySQL 8.0+
- **缓存**: Redis 6.0+
- **消息队列**: 基于SnailJob任务调度
- **API集成**: Keepa Java SDK
- **前端框架**: Vue 2.x + Element UI
- **权限管理**: Sa-Token
- **数据访问**: MyBatis-Plus

### 模块结构
```
ruoyi-keepa-selections/
├── src/main/java/org/dromara/keepa/
│   ├── config/             # 配置类
│   │   └── KeepaApiConfig.java
│   ├── controller/         # 控制器
│   │   ├── KsSelectionBatchController.java
│   │   └── KsAmazonDomainController.java
│   ├── domain/            # 实体类
│   │   ├── KsSelectionBatch.java
│   │   ├── KsAmazonDomain.java
│   │   ├── KsProductAsin.java
│   │   ├── KsProductDetail.java
│   │   └── KsSelectionResult.java
│   ├── mapper/            # 数据访问层
│   │   ├── KsSelectionBatchMapper.java
│   │   └── KsProductAsinMapper.java
│   └── service/           # 业务层
│       ├── IKsSelectionBatchService.java
│       ├── IKeepaApiService.java
│       └── impl/
└── src/main/resources/
    ├── mapper/            # MyBatis XML映射
    └── application.yml    # 配置文件
```

## 核心功能

### 1. 选品批次管理
- 创建选品批次，指定亚马逊站点和类目
- 批次状态跟踪（初始→抓取ASIN→抓取详情→分析→完成）
- 进度监控和错误处理

### 2. ASIN数据抓取
- 根据类目获取前5000排名商品的ASIN列表
- 支持根目录最多50万个ASIN的抓取
- 异步处理，避免阻塞主线程

### 3. 商品详情采集
- 批量抓取商品详情数据（每次100个ASIN）
- 包含商品标题、品牌、价格历史、销量排名等
- Token限制处理（每分钟250个Token恢复）
- 自动重试和错误处理机制

### 4. 智能选品分析
- 多维度数据分析（价格趋势、排名波动、评论增长等）
- 竞争强度评估
- 盈利潜力预测
- 综合选品评分（0-100分）

### 5. 多维度筛选
- 价格区间筛选
- 销量排名筛选
- 评分和评论数筛选
- 竞争对手数量筛选
- 推荐等级筛选

## 数据库设计

### 核心表结构
1. **ks_selection_batch** - 选品批次表
2. **ks_amazon_domain** - 亚马逊站点表
3. **ks_product_asin** - 商品ASIN表
4. **ks_product_detail** - 商品详情表
5. **ks_product_price_history** - 价格历史表
6. **ks_product_rating_history** - 评分历史表
7. **ks_selection_result** - 选品结果表
8. **ks_api_token_usage** - API使用记录表
9. **ks_task_queue** - 任务队列表

## API接口

### 选品批次管理
```
GET    /keepa/batch/list          # 查询批次列表
GET    /keepa/batch/{id}          # 查询批次详情
POST   /keepa/batch              # 新增批次
PUT    /keepa/batch              # 修改批次
DELETE /keepa/batch/{ids}        # 删除批次
POST   /keepa/batch/create       # 创建批次
POST   /keepa/batch/{id}/fetch-asin    # 开始抓取ASIN
POST   /keepa/batch/{id}/fetch-detail  # 开始抓取详情
POST   /keepa/batch/{id}/analyze       # 开始分析
GET    /keepa/batch/{id}/progress      # 获取进度
```

### 亚马逊站点管理
```
GET /keepa/domain/list    # 获取可用站点
GET /keepa/domain/all     # 获取所有站点
```

## 配置说明

### Keepa API配置
```yaml
keepa:
  api:
    key: "your_keepa_api_key"           # API密钥
    tokens-per-minute: 250              # 每分钟Token恢复数量
    max-batch-size: 100                 # 单次请求最大ASIN数量
    request-delay-ms: 1000              # 请求间隔毫秒数
    timeout-ms: 60000                   # 请求超时时间
    max-retry-count: 3                  # 最大重试次数
    enable-rate-limit: true             # 是否启用限流
```

### 异步任务配置
```yaml
spring:
  task:
    execution:
      pool:
        core-size: 5                    # 核心线程数
        max-size: 20                    # 最大线程数
        queue-capacity: 100             # 队列容量
        thread-name-prefix: "keepa-async-"
```

## 部署指南

### 1. 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 2. 数据库初始化
```sql
-- 执行SQL脚本
source script/sql/ks_keepa_selections.sql;
```

### 3. 配置修改
```yaml
# application-dev.yml
spring:
  datasource:
    dynamic:
      datasource:
        master:
          url: jdbc:mysql://your_host:3306/keepa_selections
          username: your_username
          password: your_password

keepa:
  api:
    key: ${KEEPA_API_KEY:your_keepa_api_key}
```

### 4. 启动应用
```bash
mvn clean package
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

### 5. 访问系统
- 后端API: http://localhost:8080
- 前端界面: http://localhost:80（需要单独部署前端）

## 权限配置

在RuoYi系统中配置以下权限：

```
keepa:batch:list      # 查看批次列表
keepa:batch:query     # 查看批次详情
keepa:batch:add       # 新增批次
keepa:batch:edit      # 修改批次
keepa:batch:remove    # 删除批次
keepa:batch:export    # 导出批次
keepa:batch:create    # 创建批次
keepa:batch:fetchAsin # 抓取ASIN
keepa:batch:fetchDetail # 抓取详情
keepa:batch:analyze   # 分析数据
keepa:domain:list     # 查看站点列表
```

## 使用流程

### 1. 创建选品批次
1. 登录系统，进入选品批次管理页面
2. 点击"新增"按钮，填写批次信息
3. 选择亚马逊站点（美国、德国、英国等）
4. 输入类目ID（可多个，逗号分隔）
5. 设置最大ASIN数量（默认5000）
6. 保存批次

### 2. 抓取ASIN列表
1. 在批次列表中找到创建的批次
2. 点击"抓取ASIN"按钮
3. 系统将异步抓取指定类目的商品ASIN
4. 可在批次详情中查看进度

### 3. 抓取商品详情
1. 等待ASIN抓取完成后，点击"抓取详情"
2. 系统将批量抓取每个ASIN的详细信息
3. 包含价格历史、销量排名、评论数据等
4. 根据Token限制自动控制请求频率

### 4. 分析选品数据
1. 商品详情抓取完成后，点击"分析数据"
2. 系统将对每个商品进行多维度分析
3. 计算选品评分和推荐等级
4. 生成可筛选的选品结果列表

### 5. 筛选选品结果
1. 进入选品结果页面
2. 使用多种筛选条件进行筛选
3. 按评分、价格、排名等排序
4. 导出满足条件的商品列表

## 注意事项

1. **API限制**: 严格遵守Keepa API的Token限制，避免超频使用
2. **数据量**: 大批量数据抓取需要较长时间，建议合理设置批次大小
3. **错误处理**: 网络异常或API错误会自动重试，超过最大重试次数将标记失败
4. **数据更新**: 商品数据会实时变化，建议定期重新抓取和分析
5. **存储空间**: 历史数据占用存储空间较大，建议定期清理过期数据

## 扩展功能

### 计划中的功能
- [ ] 类目搜索功能
- [ ] 商品关键词分析
- [ ] 竞品监控
- [ ] 价格预警
- [ ] 数据可视化图表
- [ ] 批量导出功能
- [ ] 自定义分析指标

### 性能优化
- [ ] 数据库分表分库
- [ ] Redis缓存优化
- [ ] 异步任务优化
- [ ] API并发控制

## 技术支持

如有问题或建议，请通过以下方式联系：
- 项目地址: https://github.com/your-org/keepa-selections
- 技术文档: 详见各模块README
- 问题反馈: 通过GitHub Issues提交

---

**免责声明**: 本工具仅供学习和研究使用，使用者需要遵守Keepa API的使用条款，对使用本工具产生的任何后果自行承担责任。

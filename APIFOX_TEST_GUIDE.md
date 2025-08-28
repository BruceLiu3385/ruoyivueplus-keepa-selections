# Keepa选品模块 ApiFox 测试指南

## 🎯 概述

本文档为您介绍如何使用ApiFox测试RuoYi-Vue-Plus框架中的Keepa选品模块API接口。

## 📋 SpringDoc API文档分组

项目已配置SpringDoc分组，启动项目后可以在以下地址访问API文档：

- **主文档地址**: `http://localhost:8080/doc.html`
- **Keepa选品模块分组**: 选择 `6.Keepa选品模块` 分组

## 🔧 配置说明

### Keepa API 配置
```yaml
# 在 application.yml 中的配置
keepa:
  api:
    key: cqqkcu49mds3vp4u4v8om3qtmmfl4l63038r1httn5tun9tu155u39k4kpkbsj2k
    tokens-per-minute: 250
    timeout-seconds: 30
    max-retries: 3
    batch-size: 100
    base-url: https://api.keepa.com
    cache-enabled: true
    cache-expire-hours: 24
    monitoring-enabled: true
```

### SpringDoc 配置
```yaml
springdoc:
  group-configs:
    - group: 6.Keepa选品模块
      packages-to-scan: org.dromara.keepa
```

## 🚀 API接口清单

### 1. 选品批次管理 (`/keepa/batch`)

| 接口 | 方法 | 描述 |
|-----|------|------|
| `/keepa/batch/list` | GET | 查询选品批次列表 |
| `/keepa/batch` | POST | 新增选品批次 |
| `/keepa/batch` | PUT | 修改选品批次 |
| `/keepa/batch/{ids}` | DELETE | 删除选品批次 |
| `/keepa/batch/{batchId}` | GET | 获取选品批次详细信息 |
| `/keepa/batch/export` | POST | 导出选品批次列表 |

### 2. 亚马逊域名管理 (`/keepa/domain`)

| 接口 | 方法 | 描述 |
|-----|------|------|
| `/keepa/domain/list` | GET | 获取可用站点列表 |
| `/keepa/domain/all` | GET | 获取所有站点列表 |

### 3. Keepa API接口 (`/keepa/api`)

| 接口 | 方法 | 描述 |
|-----|------|------|
| `/keepa/api/categories/{domainCode}` | GET | 获取类目信息 |
| `/keepa/api/products/search` | GET | 搜索商品ASIN |
| `/keepa/api/products/bestsellers` | GET | 获取畅销商品 |
| `/keepa/api/products/details` | POST | 获取商品详情 |
| `/keepa/api/token/status` | GET | 检查Token状态 |
| `/keepa/api/validate` | GET | 验证API密钥 |
| `/keepa/api/domain/{domainCode}` | GET | 获取域名信息 |

## 📝 ApiFox测试示例

### 1. 验证API密钥
```http
GET /keepa/api/validate
Authorization: Bearer {your-token}
```

### 2. 获取美国站点类目信息
```http
GET /keepa/api/categories/1
Authorization: Bearer {your-token}
```

### 3. 搜索商品ASIN
```http
GET /keepa/api/products/search?domainCode=1&categoryId=541966
Authorization: Bearer {your-token}
```

### 4. 获取商品详情
```http
POST /keepa/api/products/details
Authorization: Bearer {your-token}
Content-Type: application/json

[
  "B08N5WRWNW",
  "B07ZPKN6YR",
  "B08BCBVBZM"
]
```

### 5. 创建选品批次
```http
POST /keepa/batch
Authorization: Bearer {your-token}
Content-Type: application/json

{
  "batchName": "测试批次001",
  "amazonDomain": "1",
  "categoryId": "541966",
  "categoryName": "Electronics",
  "remark": "ApiFox测试创建的批次"
}
```

### 6. 查询选品批次列表
```http
GET /keepa/batch/list?pageNum=1&pageSize=10
Authorization: Bearer {your-token}
```

## 🔐 权限说明

所有接口都需要相应的权限验证：

| 权限代码 | 说明 |
|----------|------|
| `keepa:batch:list` | 查看批次列表 |
| `keepa:batch:add` | 新增批次 |
| `keepa:batch:edit` | 编辑批次 |
| `keepa:batch:remove` | 删除批次 |
| `keepa:domain:list` | 查看域名列表 |
| `keepa:api:category` | 获取类目信息 |
| `keepa:api:search` | 搜索商品 |
| `keepa:api:details` | 获取商品详情 |
| `keepa:api:token` | 检查Token状态 |
| `keepa:api:validate` | 验证API |

## 💡 测试建议

1. **优先验证API密钥**: 首先调用 `/keepa/api/validate` 确保API密钥有效
2. **检查Token状态**: 使用 `/keepa/api/token/status` 监控剩余Token数量
3. **小批量测试**: 商品详情查询建议每次不超过10个ASIN
4. **错误处理**: 注意观察API返回的错误信息和状态码
5. **频率控制**: 遵守Keepa API的频率限制（250 tokens/分钟）

## 🛠️ 常见问题

### Q: API返回403错误？
A: 检查权限配置，确保用户拥有相应的权限代码。

### Q: Keepa API调用失败？
A: 
1. 验证API密钥是否正确
2. 检查Token是否用完
3. 确认网络连接正常

### Q: 如何获取域名编码？
A: 调用 `/keepa/domain/all` 获取所有可用的域名及其编码。

### Q: 批次创建失败？
A: 检查必填字段是否完整，amazonDomain是否为有效的域名编码。

## 📞 联系支持

如果在测试过程中遇到问题，请检查：
1. 项目是否正常启动
2. 数据库连接是否正常
3. Keepa API配置是否正确
4. 用户权限是否充足

---

**注意**: 本指南基于开发环境配置，生产环境请相应调整域名和端口。

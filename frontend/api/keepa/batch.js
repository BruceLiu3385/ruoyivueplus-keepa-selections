import request from '@/utils/request'

// 查询选品批次列表
export function listBatch(query) {
  return request({
    url: '/keepa/batch/list',
    method: 'get',
    params: query
  })
}

// 查询选品批次详细
export function getBatch(id) {
  return request({
    url: '/keepa/batch/' + id,
    method: 'get'
  })
}

// 新增选品批次
export function addBatch(data) {
  return request({
    url: '/keepa/batch',
    method: 'post',
    data: data
  })
}

// 修改选品批次
export function updateBatch(data) {
  return request({
    url: '/keepa/batch',
    method: 'put',
    data: data
  })
}

// 删除选品批次
export function delBatch(id) {
  return request({
    url: '/keepa/batch/' + id,
    method: 'delete'
  })
}

// 导出选品批次
export function exportBatch(query) {
  return request({
    url: '/keepa/batch/export',
    method: 'post',
    params: query
  })
}

// 创建选品批次
export function createBatch(data) {
  return request({
    url: '/keepa/batch/create',
    method: 'post',
    data: data
  })
}

// 开始抓取ASIN列表
export function startFetchAsin(id) {
  return request({
    url: '/keepa/batch/' + id + '/fetch-asin',
    method: 'post'
  })
}

// 开始抓取商品详情
export function startFetchDetail(id) {
  return request({
    url: '/keepa/batch/' + id + '/fetch-detail',
    method: 'post'
  })
}

// 开始分析商品数据
export function startAnalyze(id) {
  return request({
    url: '/keepa/batch/' + id + '/analyze',
    method: 'post'
  })
}

// 获取批次进度
export function getBatchProgress(id) {
  return request({
    url: '/keepa/batch/' + id + '/progress',
    method: 'get'
  })
}

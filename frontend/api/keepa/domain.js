import request from '@/utils/request'

// 查询可用的亚马逊站点列表
export function listDomain() {
  return request({
    url: '/keepa/domain/list',
    method: 'get'
  })
}

// 查询所有亚马逊站点列表（包括停用的）
export function listAllDomain() {
  return request({
    url: '/keepa/domain/all',
    method: 'get'
  })
}

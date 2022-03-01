import request from '@/utils/request'

export function init() {
  return request({
    url: '/system/home/init',
    method: 'get',
    params:{}
  })
}
export function lineCount() {
  return request({
    url: '/system/home/lineCount',
    method: 'get',
    params:{}
  })
}
export function systemInfo() {
  return request({
    url: '/system/home/systemInfo',
    method: 'get',
    params:{}
  })
}

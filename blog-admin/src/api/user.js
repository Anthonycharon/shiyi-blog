import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}
export function getInfo() {
  return request({
    url: '/system/user/getCurrentUserInfo',
    method: 'post',
    params: {}
  })
}
export function logout() {
  return request({
    url: '/system/user/logout',
    method: 'post'
  })
}
export function captchaImage() {
  return request({
    url: '/captchaImage',
    method: 'get',
    params:{}
  })
}
export function fetchUser(params) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params:params
  })
}
export function remove(data) {
  return request({
    url: '/system/user/remove',
    method: 'delete',
    data
  })
}
export function create(data) {
  return request({
    url: '/system/user/create',
    method: 'post',
    data
  })
}
export function update(data) {
  return request({
    url: '/system/user/update',
    method: 'post',
    data
  })
}
export function info(id) {
  return request({
    url: '/system/user/info',
    method: 'get',
    params:{id:id}
  })
}
export function getMenuTree() {
  return request({
    url: '/system/user/getUserMenu',
    method: 'post',
    params:{}
  })
}
export function editPassword(data) {
  return request({
    url: '/system/user/updatePassword',
    method: 'post',
    data
  })
}

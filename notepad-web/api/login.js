import {http} from '@/api/service.js'
import {encryptData} from '@/utils/encryption/utils.js'

/**
 * 登录
 * @param {Object} params - 登录参数
 */
export const login = (params) => {
  return http.post('/login', encryptData(params),{custom :{ auth: false }})
}

/**
 * 临时登录
 * @param {Object} params - 登录参数
 */
export const temporaryLogin = () => {
  return http.post('/temporary/login',{},{custom :{ auth: false }})
}

/**
 * 注册
 * @param {Object} params - 注册参数
 */
export const register = (params) => {
  return http.put('/register', encryptData(params),{custom :{ auth: false }})
}

/**
 * 忘记密码
 * @param {Object} params - 忘记密码
 */
export const forgetPassword = (params) => {
  return http.post('/forget/password', encryptData(params),{custom :{ auth: false }})
}

/**
 * 退出
 * @param {Object} params - 忘记密码
 */
export const logout = (params) => {
  return http.post('/logout',{custom :{ auth: false }})
}
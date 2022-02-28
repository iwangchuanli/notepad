import {http} from '@/api/service.js'
import {encryptData} from '@/utils/encryption/utils.js'


/**
 * 根据手机号发送邮箱验证码
 * @param {Object} params - 手机号
 */
export const sendCode = (params) => {
  return http.post('/email/phone', params,{custom :{ auth: false }})
}

/**
 * 反馈
 * @param {Object} params - 反馈
 */
export const feedback = (params) => {
  return http.post('/email/feedback', params)
}
import {http} from '@/api/service.js'
import {encryptData} from '@/utils/encryption/utils.js'

/**
 * 修改密码
 * @param {Object} params - 修改密码  
 */
export const userUpdatePassword = (params) => {
  return http.post('/user/update/password', encryptData(params))
}
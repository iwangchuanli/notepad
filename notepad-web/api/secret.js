import {http} from '@/api/service.js'
import {encryptData} from '@/utils/encryption/utils.js'

// 密钥接口

/**
 * 查询分页信息
 * @param {Object} params - 查询参数  
 */
export const getPage = (params) => {
  return http.post('/secret/page', params)
}

/**
 * 保存信息
 * @param {Object} params - 保存参数  
 */
export const saveData = (params) => {
  return http.put('/secret/save', encryptData(params))
}

/**
 * 删除信息
 * @param {Object} params - id 
 */
export const deleteData = (params) => {
  return http.delete('/secret/delete', encryptData(params))
}

/**
 * 获取详细信息
 * @param {Object} params - id  
 */
export const getInfo = (params) => {
  return http.post('/secret/info',encryptData(params))
}

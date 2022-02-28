import {http} from '@/api/service.js'

/**
 * 获取最新版本号
 * @param {Object} params - 参数  
 */
export const versionApp = (params) => {
  return http.post('/version/app', params)
}
import {http} from '@/api/service.js'

/**
 * 查询分页信息
 * @param {Object} params - 查询参数  
 */
export const getPage = (params) => {
  return http.post('/bill/page', params)
}

/**
 * 删除信息
 * @param {Object} params - id查询参数  
 */
export const deleteData = (params) => {
  return http.delete('/bill/delete', params)
}

/**
 * 获取详细信息
 * @param {Object} params - id查询参数  
 */
export const getInfo = (params) => {
  return http.post('/bill/info', params)
}

/**
 * 保存或更新
 * @param {Object} params - 编辑参数  
 */
export const saveData = (params) => {
  return http.put('/bill/save', params)
}

/**
 * 根据时间查询收入总价格，支出总价格 借款总价格
 * @param {Object} params - id查询参数  
 */
export const getTimeData = (params) => {
  return http.post('/bill/getTimeData', params)
}

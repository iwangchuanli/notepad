import {http} from '@/api/service.js'

/**
 * 查询最新的一条数据
 * @param {Object} params - 查询参数  
 */
export const getLastData = () => {
  return http.get('/record/lastData')
}

/**
 * 查询分页信息
 * @param {Object} params - 查询参数  
 */
export const getPage = (params) => {
  return http.post('/record/page', params)
}

/**
 * 删除信息
 * @param {Object} params - 查询参数  
 */
export const deleteData = (params) => {
  return http.delete('/record/delete', params)
}

/**
 * 查询详细信息
 * @param {Object} params - 查询参数  
 */
export const getInfo = (params) => {
  return http.post('/record/getInfo', params)
}

/**
 * 保存信息
 * @param {Object} params - 查询参数  
 */
export const saveData = (params) => {
  return http.put('/record/save', params)
}

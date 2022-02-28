import {http} from '@/api/service.js'

/**
 * 上传文件
 * @param {Object} params - 文件地址
 */
export const uploadImage = (params) => {
  return http.upload('/upload/image',{filePath:params,name: 'image',custom :{ auth: true }})
}
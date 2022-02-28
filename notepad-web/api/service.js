import Request from '@/utils/luch-request/index.js'
import {decryptData} from '@/utils/encryption/utils.js'
import $store from '@/store/index.js'

// 自己后台地址
export const baseUrl='http://localhost:8080'

const http = new Request()
http.setConfig((config) => { /* 设置全局配置 */
  config.baseURL = baseUrl
  config.header = {
    ...config.header,
  }
  config.custom = {
	 // 是否传token
    auth: true,
	// 是否使用loading
    loading: false 
  }
  return config
})


http.interceptors.request.use((config) => { /* 请求之前拦截器。可以使用async await 做异步操作 */
  config.header = {
    ...config.header,
  }
	/**
  	* custom {Object} - 自定义参数
  	*/
	if (config.custom.auth) {
		config.header.token =  $store.state.token
		if (!config.header.token) { // 如果token不存在，return Promise.reject(config) 会取消本次请求
			uni.$u.route({url:'/pages/login/login',type:'redirectTo'});
			return Promise.reject(config)
		}
	}
  return config
}, (config) => {
  return Promise.reject(config)
})


http.interceptors.response.use(async (response) => { /* 请求之后拦截器。可以使用async await 做异步操作  */
	if (response.statusCode != 200) { // 服务端返回的状态码不等于200，则reject()
		return Promise.reject(response)
	}
	let code = parseInt(response.data.code);
	let result=null;
	if(code) {
		result=response.data;
	}else{
		// AES解密
		result=JSON.parse(decryptData(response.data));
		code=result.code;
	}
	if (code == 5000||code == 1100||1120==code) {
		uni.$u.toast(response.data.message);
		return Promise.reject(response)
	}else if(1300==code||1310==code){
		// 退出登录操作
		$store.commit('logout')
		uni.$u.route({url:'/pages/login/login',type:'redirectTo'});
		return Promise.reject(response)
	}else if(1320==code){
		$store.commit('setToken',response.data.message)
		return Promise.reject(response)
	}
	return result;
}, (response) => { // 请求错误做点什么。可以使用async await 做异步操作
  return Promise.reject(response)
})

export {
  http
}

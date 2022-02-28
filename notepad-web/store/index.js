import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		token: '',
		userInfo: {},
		loginStatus:false,
		isTemporary:false,
		version: "1.0.5",
	},
	mutations: {
		//更新state数据
		setStateAttr(state, param){
			if(param instanceof Array){
				for(let item of param){
					state[item.key] = item.val;
				}
			}else{
				state[param.key] = param.val;
			}
		},
		// 初始化登录状态
		initUser(state){
			let userInfo = uni.getStorageSync('userInfo')
			if (userInfo) {
				userInfo = JSON.parse(userInfo)
				state.userInfo = userInfo
				state.token = uni.getStorageSync('token')
				state.loginStatus = true
				state.isTemporary=uni.getStorageSync('isTemporary')
			}
			
		},
		// 登录
		login(state,data){
			state.token=data.token
			state.userInfo = data.userInfo
			state.loginStatus = true
			state.isTemporary=data.isTemporary==2?false:true;
			// 持久化存储
			uni.setStorageSync('userInfo',JSON.stringify(data.userInfo))
			uni.setStorageSync('token',data.token)
			uni.setStorageSync('isTemporary',data.isTemporary==2?false:true)
		},
		// 退出登录
		logout(state){
			state.userInfo = {}
			state.loginStatus = false
			state.token = null
			uni.removeStorageSync('userInfo')
			uni.removeStorageSync('token')
			uni.removeStorageSync('isTemporary')
		},
		setToken(state,token){
			state.token=token;
			uni.setStorageSync('token',token)
		}
	}
}) 


export default store

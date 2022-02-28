<template>
	<view class="w-100">
		<view>
			<view class="pl-2 mb-5">
				<view class="flex just-content-between align-center">
					<u-avatar src="/static/icon/default-avatar.png" shape="square" size="100" class="u-avatar"/>
					<view class="u-avatar" @click="openUser">
						<view :class="isLogin?'u-line-1':'p-1 border'" hover-class="bg-hover-light"> 
							{{isLogin ? userInfo.nickname?userInfo.nickname:userInfo.mobile : '登录/注册'}} 
						</view>
						<view v-if="isLogin">欢迎使用</view>
					</view>
				</view>
			</view>
			<view class="n-p" v-for="(item,index) in list" :key="index" hover-class="bg-hover-light" @click="onClick(item)">
				<template v-if="item.isTemporary">
					<view class="mr-2 ml-2">
						<u-icon :name="item.icon" size="20"></u-icon>
					</view>
					<view class="p-right">
						<view class="font">{{item.name}}</view>
					</view>
				</template>
			</view>
			<view class="mt-5 p-1" v-if="isLogin">
				<u-button type="error" text="退出" plain icon="close-circle" throttleTime="5"  @click="logout"></u-button>
			</view>
		</view>
		<!-- 反馈弹框 -->
		<u-modal :show="modalShow" title="反馈" @confirm="sendMessage" closeOnClickOverlay @close="modalShow=false">
			<view class="slot-content">
				<view>
					<u--input placeholder="请输入您的邮箱" prefixIcon="email" v-model="feedback.email"></u--input>
				</view>
				<view class="mt-1">
					<u--textarea v-model="feedback.message" placeholder="请输入您反馈的内容"></u--textarea>
				</view>
			</view>
		</u-modal>
		<!-- 反馈弹框 -->
		<u-modal :show="updatePasswordShow" title="修改密码" @confirm="updatePasswordClick" closeOnClickOverlay @close="updatePasswordShow=false">
			<view class="slot-content">
				<view>
					<input-box ref="oldPassword" v-model="updatePassword.oldPassword" type="password" :inputValue="updatePassword.oldPassword"
					  :verification="['isNull']" :verificationTip="['原密码不能为空','']" placeholder="请输入原密码"/>
				</view>
				<view>
					<input-box ref="newPassword" v-model="updatePassword.newPassword" type="password" :inputValue="updatePassword.newPassword"
					  :verification="['isNull']" :verificationTip="['新密码不能为空','']" placeholder="请输入新密码"/>
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script>
	import {logout} from '@/api/login.js';
	import {userUpdatePassword} from '@/api/user.js';
	import {feedback} from '@/api/email.js';
	// #ifdef APP-PLUS
	import APPUpdate, { getCurrentNo } from '@/uni_modules/zhouWei-APPUpdate/js_sdk/appUpdate';
	// #endif
	export default {
		data() {
			return {
				modalShow:false,
				updatePasswordShow:false,
				feedback:{
					email:'',
					message:''
				},
				updatePassword:{
					oldPassword:'',
					newPassword:''
				},
				list: [{
					name: '修改密码',
					id: 0,
					icon: 'edit-pen-fill',
					isTemporary: true
				},{
					name: '反馈',
					id: 1,
					icon: 'email-fill',
					isTemporary: true
				},{
					name: '检查更新',
					id: 3,
					icon: 'download',
					isTemporary: true
				}, {
					name: '关于',
					id: 2,
					icon: 'setting-fill',
					isTemporary: true
				}],
			}
		},
		computed:{
			isLogin(){
				return this.$store.state.loginStatus;
			},
			userInfo(){
				return this.$store.state.userInfo;
			},
		},
		created() {
			this.list[0].isTemporary=!this.$store.state.isTemporary;
			// #ifndef APP-PLUS
			 this.list[2].isTemporary=false
			// #endif
		},
		methods: {
			//发送信息
			sendMessage(){
				if(""==this.feedback.email|| ""==this.feedback.message){
					uni.$u.toast('邮箱/内容不能为空');
				}
				if(!uni.$u.test.email(this.feedback.email)){
					uni.$u.toast('邮箱格式不正确');
				}
				//发送信息
				feedback(this.feedback).then(res=>{
					this.modalShow=false;
					uni.$u.toast('感谢您的反馈');
				})
			},
			openUser(){
				if(this.isLogin){
					return;
					//uni.$u.route('/pages/user/details',{id:this.userInfo.id})
				}else{
					uni.$u.route({url:'/pages/login/login',type:'redirectTo'});
				}
			},
			onClick(item) {
				switch(item.id){
					case 0:
						this.updatePasswordShow=true;
						break;
					case 1:
						this.modalShow=true
						break;
					case 2:
						uni.$u.route('/pages/about/about');
						break;
					case 3:
						this.onAPPUpdate();
						break;
				}
			},
			//修改密码
			updatePasswordClick(){
				if(!this.$refs.oldPassword.getValue() || !this.$refs.newPassword.getValue()){
					uni.$u.toast('原密码/新密码不能为空');
					return;
				}
				userUpdatePassword(this.updatePassword).then(res=>{
					if(2000==res.code){
						this.updatePasswordShow=false;
						uni.$u.toast('密码修改成功');
						this.$store.commit('logout')
						uni.$u.route({url:'/pages/login/login',type:'redirectTo'});
					}else{
						uni.$u.toast(res.message);
					}
				})
			},
			//退出
			logout(){
				logout().then(res=>{
					// 退出登录操作
					this.$store.commit('logout')
					uni.$u.route({url:'/pages/login/login',type:'redirectTo'});
				})
			},
			onAPPUpdate(){
				// #ifdef APP-PLUS
				APPUpdate(true);
				// #endif
			},
		}
	}
</script>

<style>
	.u-avatar{
		/* #ifdef H5 */
		margin-top: 40rpx;
		/* #endif */
		margin-right: 20rpx;
	}
	.n-p {
		display: flex;
		align-items: center;
	}
	.p-right {
		height:60rpx;
		display: flex;
		align-items: center;
	}
</style>

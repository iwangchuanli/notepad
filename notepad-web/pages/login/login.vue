<template>
	<view class="login">
		<view class="login-top bg-active">
			<view class="desc">
				<view>欢迎您</view>
				<text>Welcome!</text>
			</view>
		</view>
		<view class="login-type-content">
			<image class="login-bg" src="/static/login-bg.png" :style="{height: tabCurrentIndex === 1 ? '135vw' : '95vw'}"></image>
			<view class="main">
				<view class="nav-bar">
					<template v-if="tabCurrentIndex==2">
						<view class="h3 text-active nav-bar-item-active">
							忘记密码
						</view>
					</template>
					<template v-else>
						<view class="nav-bar-item" v-for="(item, index) in typeList" :key="index"
								:class="tabCurrentIndex === index ? 'nav-bar-item-active text-active' : ''"
								@tap="tabClick(index)">
							{{ item.text}}
						</view>
					</template>
				</view>
				<!-- 登录 -->
				<block v-if="tabCurrentIndex === 0">
					<view class="login-type-form">
						<view class="pb-2">
							 <u--input type="number" placeholder="请输入手机号码"
							    border="bottom" v-model="loginParams.mobile" maxlength="11" fontSize="16" ></u--input>
						</view>
						<view>
							<input-box ref="loginPassword" v-model="loginParams.password" type="password"
							  :verification="['isNull']" :verificationTip="['密码不能为空','']" placeholder="请输入密码"/>
						</view>
					</view>
					<view class="text-right mr-3" @click="tabCurrentIndex = 2">
						忘记密码？
					</view>
					<!-- 底部协议 -->
					<view class="flex align-center justify-center" @tap="handleProtocolPopupShow">
						<radio :checked="appAgreementDefaultSelect" style="transform:scale(0.7)"/>
						<view>
							<text>登录表示同意</text>
							<text class="text-primary">《服务协议、隐私政策》</text>
						</view>
						
					</view>
					<u-button text="登录" :loading="btnLoading" :disabled="btnLoading" class="confirm-btn bg-active" @tap="toLogin" throttleTime="5"></u-button>
				</block>
				
				<!-- 注册 -->
				<block v-if="tabCurrentIndex === 1">
					<u--form :model="registerParams" ref="register" style="width: 80%;margin: 0 auto;" labelWidth="65">
						<u-form-item label="昵称" prop="nickname" >
							<u--input placeholder="请输入昵称" border="bottom"
							 v-model="registerParams.nickname" fontSize="16"></u--input>
						</u-form-item>
						<u-form-item label="手机号码" prop="mobile">
							<u--input type="number" placeholder="请输入手机号码" border="bottom"
							    v-model="registerParams.mobile" maxlength="11" fontSize="16"></u--input>
						</u-form-item>
						<u-form-item label="邮箱" prop="email">
							<u--input placeholder="请输入邮箱" border="bottom"
							 v-model="registerParams.email" fontSize="16"></u--input>
						</u-form-item>
						<u-form-item label="密码" prop="password">
							<input-box ref="password" v-model="registerParams.password" type="password"
							  :verification="['isNull']" :verificationTip="['密码不能为空','']" placeholder="请输入密码"/>
						</u-form-item>
						<u-form-item label="确认密码" prop="passwordRepetition">
							<input-box ref="password" v-model="registerParams.passwordRepetition" type="password"
							  :verification="['isNull']" :verificationTip="['确认密码不能为空','']" placeholder="请输入确认密码"/>
						</u-form-item>
					</u--form>
					<u-button text="注册" :loading="btnLoading" :disabled="btnLoading" 
					class="confirm-btn bg-active" @tap="toRegister" throttleTime="5"></u-button>
				</block>
				
				<!-- 忘记密码 -->
				<block v-if="tabCurrentIndex === 2">
					<u--form :model="forgetParams" ref="forget" style="width: 80%;margin: 0 auto;" labelWidth="65">
						<u-form-item label="手机号码" prop="mobile">
							<u--input type="number" placeholder="请输入手机号码"
							   border="bottom" v-model="forgetParams.mobile" maxlength="11" fontSize="16"></u--input>
						</u-form-item>
						<u-form-item label="密码" prop="password">
							<input-box ref="password" v-model="forgetParams.password" type="password"
							  :verification="['isNull']" :verificationTip="['新密码不能为空','']" placeholder="请输入新密码"/>
						</u-form-item>
						<u-form-item label="验证码" prop="code">
							<u--input placeholder="请输入验证码" border="bottom"
							 v-model="forgetParams.code" fontSize="16" >
							</u--input>
							<u-button  shape="circle" @click="getCode"
							:disabled="readonly" :customStyle="{width:'85px'}">
							{{codeText}}</u-button>
						</u-form-item>
					</u--form>
					<view class="flex just-content-between align-center p-5">
						<u-button size="large" shape="circle" class="mr-1" @click="tabCurrentIndex=0">
							<u-icon name="arrow-leftward" size="20"></u-icon>返回
						</u-button>
						<u-button size="large" shape="circle" text="确定" :loading="btnLoading" :disabled="btnLoading"
						class="ml-1" @tap="forgetPassword" throttleTime="5"></u-button>
					</view>
				</block>
			</view>
		</view>
		<!-- 底部协议 -->
		<view class="footer" v-if="tabCurrentIndex==0">
			<view class="flex justify-center">
				<u-divider style="width: 450rpx;" text="其他登录方式"></u-divider>
			</view>
		   <u-grid :border="false" col="4" align="center"  @click="clickGrid">
			  <u-grid-item v-for="(item,index) in loginTypeList" :key="index" :customStyle="{width:130+'rpx'}">
				<u--image :src="item.icon" width="35" height="35"/>
				<text>{{item.text}}</text>
			  </u-grid-item>
		  </u-grid>
		</view>
	
		<!--协议popup-->
		<protocol-popup v-if="isProtocolPopupShow" ref="mapState" @popupState="popupState"
		    protocolPath="/pages/doc/protocol/protocol"></protocol-popup>
	</view>
</template>
<script>
	import {login,temporaryLogin,register,forgetPassword} from '@/api/login.js';
	import {sendCode} from '@/api/email.js';
	var clear;
	export default {
		data() {
			return {
				loginParams: {
					mobile: '',
					password: ''
				},
				registerParams: {
					nickname: '',
					mobile: '',
					email:'',
					password: '',
					passwordRepetition: '',
				},
				forgetParams:{
					mobile:'',
					password:'',
					code:''
				},
				isProtocolPopupShow: false, // 控制协议popup显示
				appAgreementDefaultSelect:true,
				btnLoading: false,
				tabCurrentIndex: 0,
				codeText:'验证码',
				readonly: false,
				typeList: [
					{ text: '登录'},
					{ text: '注册'}
				],
				loginTypeList: [
					{ text: '临时', icon: '/static/icon/temporary_login.png' },
					{ text: '微信', icon: '/static/icon/wechat.png' },
					{ text: '微博', icon: '/static/icon/weibo.png' },
					{ text: '支付宝', icon: '/static/icon/alipay.png' }
				],
				rules:{
					nickname:{
							required: true,
							message: '请输入昵称',
							trigger: ['blur', 'change']
					},
					mobile:[{
								type: 'number',
								required: true,
								min:11,
								message: '请输入手机号',
								trigger: ['blur', 'change']
					},{
						validator: (rule, value, callback) => {
							return uni.$u.test.mobile(value);
						},
						message: '手机号码不正确',
						trigger: ['change','blur'],
					}],
					email:[{
							type: 'email',
							required: true,
							message: '请输入邮箱',
							trigger: ['blur', 'change']
						},{
						validator: (rule, value, callback) => {
							return uni.$u.test.email(value);
						},
						message: '邮箱格式不正确',
						trigger: ['change','blur'],
					}],
					password: {	
							required: true,
							message: '请输入密码',
							trigger: ['blur', 'change']
					},
					passwordRepetition:[{	
							required: true,
							message: '请输入确认密码',
							trigger: ['blur', 'change']
					},{
						validator: (rule, value, callback) => {
							return value == this.registerParams.password;
						},
						message: '两次密码不一致',
						trigger: ['change','blur'],
					}],
					code:{
						required: true,
						message: '请输入验证码',
						trigger: ['blur', 'change']
					}
				},
			};
		},
		onLoad(options) {
			// 如果不是第一次进来 就不需要强制阅读协议
			if (!uni.getStorageSync('notFirstTimeLogin')) {
				this.appAgreementDefaultSelect = false;
			} else {
				this.appAgreementDefaultSelect = true;
			}
		},
		methods: {
			// 提交表单
			async toLogin() {
				if (!this.appAgreementDefaultSelect) {
					uni.$u.toast('请勾选并阅读协议')
					return;
				}
				if(!this.loginParams.mobile|| !this.$refs.loginPassword.getValue()){
					uni.$u.toast('请输入手机号和密码')
					return;
				}
				this.btnLoading=true;
				login(this.loginParams).then(res=>{
					this.btnLoading=false;
					if(2000==res.code){
						this.$store.commit('login', res.data);
						uni.$u.route({url: 'pages/index/index',type:'reLaunch'})
					}else{
						uni.$u.toast(res.message)
					}
				});
			},
			// 切换登录/注册
			tabClick(index) {
				this.tabCurrentIndex = index;
			},
			// 注册账号
			async toRegister() {
				// this.$refs.register.setRules(this.rules);
				this.$refs.register.validate().then(res => {
					this.btnLoading=true;
					register(this.registerParams).then(res=>{
						this.btnLoading=false;
						if(2000!=res.code){
							uni.$u.toast(res.message)
						}else{
							this.tabCurrentIndex=0;
						}
					})
				})
			},
			getCode() {
				if (this.readonly) {
					uni.$u.toast('验证码以发送到您的邮箱');
					return;
				}
				if (!this.forgetParams.mobile) {
					uni.$u.toast('请输入手机号');
					return;
				}
				if (!uni.$u.test.mobile(this.forgetParams.mobile)) {
					uni.$u.toast('请输入正确的手机号');
					return;
				}
				sendCode({'phone':this.forgetParams.mobile}).then(res=>{
					if(2000==res.code){
						// 这里此提示会被this.start()方法中的提示覆盖
						uni.$u.toast('验证码以发送到您的邮箱');
						// 通知验证码组件内部开始倒计时
						this.getCodeState();
					}else{
						uni.$u.toast('验证码发送失败');
					}
				})
			},
			//验证码按钮文字状态
			getCodeState() {
				const _this = this;
				this.readonly = true;
				this.codeText = '60S';
				var s = 60;
				clear = setInterval(() => {
					s--;
					_this.codeText = s + 'S';
					if (s <= 0) {
						clearInterval(clear);
						_this.codeText = '验证码';
						_this.readonly = false;
					}
				}, 1000);
			},
			codeChange(text) {
				this.tips = text;
			},
			handleProtocolPopupShow(){
				this.isProtocolPopupShow = true;
			},
			// 监听是否同意协议
			popupState(e) {
				if (e) {
					this.appAgreementDefaultSelect = true;
					uni.setStorageSync('notFirstTimeLogin', true);
					this.isProtocolPopupShow = false;
				} else {
					this.appAgreementDefaultSelect = false;
					this.isProtocolPopupShow = false;
				}
			},
			// 忘记密码
			forgetPassword(){
				// this.$refs.forget.setRules(this.rules);
				this.$refs.forget.validate().then(res => {
					this.btnLoading=true;
					forgetPassword(this.forgetParams).then(res=>{
						this.btnLoading=false;
						if(2000!=res.code){
							uni.$u.toast(res.message)
						}else{
							this.tabCurrentIndex=0;
						}
					})
				})
			},
			//临时登录
			clickGrid(index){
				if (!this.appAgreementDefaultSelect) {
					uni.showToast({ title: '请勾选并阅读协议' });
					return;
				}
				if(0==index){
					temporaryLogin().then(res=>{
						if(2000==res.code){
							this.$store.commit('login', res.data);
							uni.$u.route({url: 'pages/index/index',type:'reLaunch'})
						}
					});
				}else{
					uni.$u.toast(this.loginTypeList[index].text+"暂未开通")
				}
			}
		}
	};
</script>
<style lang="scss" scoped>
	.login {
		width: 100%;
		position: relative;
		.bg-active {
		  background-color: $app-navbar-backgroundColor;
		  color: #fff;
		}
		.text-active{
		  color: $app-navbar-backgroundColor;
		}
		.login-top {
			height: 360rpx;
			position: relative;
			.desc {
				position: absolute;
				top: 100rpx;
				left: 40rpx;
				font-size: 48rpx;
			}
		}
		.login-type-content {
			position: relative;
			top: -70rpx;
			.login-bg {
				width: 94vw;
				height: 94vw;
				margin: 0 3vw;
			}
			.main {
				width: 100%;
				position: absolute;
				top: 0;
				
				.nav-bar {
					display: flex;
					height: 100rpx;
					justify-content: center;
					align-items: center;
					position: relative;
					z-index: 10;
					.nav-bar-item {
						flex: 1;
						display: flex;
						height: 100%;
						line-height: 96rpx;
						font-size: 32rpx;
						display: flex;
						margin: 0 120rpx;
						justify-content: center;
					}
					.nav-bar-item-active {
						border-bottom: 5rpx solid;
					}
				}
				.login-type-form {
					width: 80%;
					margin: 50rpx auto;
					
				}
				.confirm-btn {
				  margin-top: 40upx;
				  width: 80%;
					height: 80rpx;
					line-height: 80rpx;
				}
			}
		}
	}
	// 密码input
	.uni-input-placeholder{
		color: rgb(192, 196, 204);
	}
	.input-box-all{
		padding: 0 10px;
		border-bottom: 1px solid #dadbde;
	}
	.footer {
	  margin-top: -37px;
	}
</style>

<template>
	<view class="w-100">
		<my-paging ref="paging" v-model="dataList" @query="queryList">
			<view slot="top">
				<tickler-navbar title="账号" rightIcon="edit-pen" @leftClick="loginMenuShow=true"
				 @rightClick="clickAdd"></tickler-navbar>
			</view>
			<!-- 数据 -->
			<u-swipe-action>
				<u-swipe-action-item :show="item.show" :index="index" :name="index" @click="clickSwipe" :options="options"
							v-for="(item, index) in dataList" :key="item.id">
					<view class="flex justify-center u-border-bottom">
						<view class="w-95 font flex align-center text-ellipsis" style="height: 100rpx;">
							{{ item.desc }}
						</view>
					</view>
				</u-swipe-action-item>
			</u-swipe-action>
		</my-paging>
		<!-- 添加 -->
		<u-popup :show="saveShow" mode="center" :closeOnClickOverlay="false" :round="14" closeable @close="saveShow=false"
		:customStyle='{"width":"95%"}'>
			<view class="px-3 py-1">
				<u--form :model="upModel" ref="upForm" :labelStyle='{"width":"100%"}'>
					<u-form-item label="账号" labelWidth="55" leftIcon="account">
						<u--input border="bottom" v-model="upModel.account" focus placeholder="请输入账号" />
					</u-form-item>
					<u-form-item label="密码" prop="password" required>
						<input-box ref="password" v-model="upModel.password" type="password" :inputValue="upModel.password"
						  :verification="['isNull']" :verificationTip="['密码不能为空','']" placeholder="请输入密码"/>
					</u-form-item>
					<u-form-item label="平台URL">
						<u--input border="bottom" placeholder="是哪个平台" v-model="upModel.refererUrl"/>
					</u-form-item>
					<u-form-item label="简介" prop="desc" required>
						<u--textarea border="bottom" maxlength="120" autoHeight v-model="upModel.desc" placeholder="请填写简介" ></u--textarea>
					</u-form-item>
				</u--form>
				<view class="mt-1 flex justify-center align-center">
					<u-button @click="submit">保存</u-button>
				</view>
			</view>
		</u-popup>
		
		<!-- 登录菜单 -->
		<u-popup :show="loginMenuShow" @close="loginMenuShow=false"  safeAreaInsetTop mode="left"
		:customStyle='{"width":"500rpx","margin-top":"50rpx","border-radius": "8rpx","height":"100%"}'>
			<login-popup></login-popup>
		</u-popup>
	</view>
</template>

<script>
	import { getPage,saveData,deleteData,getInfo } from '@/api/secret.js';
	export default {
		data() {
			return {
				dataList: [],
				saveShow:false,
				loginMenuShow:false,
				upModel:{},
				rules:{
					password:[
						{
							required: true,
							message: '请输入密码',
							trigger: ['blur', 'change']
					}],
					desc: [
						{	
							required: true,
							message: '请填写简介',
							trigger: ['blur', 'change']
						},
						{
							min: 2,
							max: 120,
							message: '简介最少2个字,最大120个字',
							trigger: ['blur', 'change']
						}
					]
				},
				options:[{
							text: '查看',
							style: { backgroundColor: '#007aff' }
						},
						{
							text: '删除',
							style: { backgroundColor: '#dd524d' }
						}],
			}
		},
		methods: {
			queryList(pageNo, pageSize) {
				const params = {
					pageNumber: pageNo,
					pageSize: pageSize
				}
				getPage(params).then(res=>{
					this.$refs.paging.complete(res.data.rows);
				})
			},
			submit(){
				this.$refs.password.getValue()
				this.$refs.upForm.setRules(this.rules);
				this.$refs.upForm.validate().then(res => {
					saveData(this.upModel).then(res=>{
						this.saveShow=false
						this.$refs.paging.reload()
					})
				})	
			},
			clickAdd(){
				this.saveShow=true
				this.upModel={}
			},
			clickSwipe(rsp) {
				// 0==查看  1删除
				const params={id:this.dataList[rsp.name].id}
				if(rsp.index == 1) {
					//删除
					deleteData(params).then(res=>{
						this.dataList.splice(rsp.name, 1);
					})
				} else {
					//查看
					this.dataList[rsp.name].show = false;
					getInfo(params).then(res=>{
						this.upModel=res.data;
						this.saveShow=true;
					})
				}
			},
		},
	}
</script>
<style scoped lang="scss">
	.uni-input-placeholder{
		color: rgb(192, 196, 204);
	}
	.input-box-all{
		padding: 0 10px;
		border-bottom: 1px solid #dadbde;
	}
</style>

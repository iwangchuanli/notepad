<template>
	<view>
		<tickler-navbar title="记录" rightIcon="list-dot" @leftClick="loginMenuShow=true" 
		 @rightClick="openListShow=true"></tickler-navbar>
		<view class="mx-2">
			<u--form :model="form">
				<u-form-item prop="title" borderBottom label="标题">
					<u--input border="none" v-model="form.title" placeholder="请输入标题" placeholderStyle='color:#949596'/>
				</u-form-item>
				<u-form-item prop="createTime" label="时间" borderBottom>
					<datetime-picker ref="datetimePicker"
					    fields="hour"
						start="2022-01-00 00:00:00"
						end="2125-12-12 24:59:59"
						:defaultValue="form.createTime"
					    @change="saveTime"
						placeholder="请选择时间"
					></datetime-picker>
				</u-form-item>
				<u-form-item prop="details">
					<male-editor :uploadFile="upFile" :html="form.details" :fromItemHeight="125" @input="editorChange"></male-editor>
				</u-form-item>
			</u--form>
		</view>
		
		<!-- 所有记录 -->
		<u-popup :show="openListShow" safeAreaInsetTop mode="right" @close="openListShow=false"
		 :customStyle='{"width":"500rpx","margin-top":"60rpx","border-radius": "8rpx","height":"100%"}'>
			<my-paging ref="paging" v-model="dataList" @query="queryList" style="position:absolute;padding-top: 8rpx;">
				<u-swipe-action>
					<u-swipe-action-item :show="item.show" :index="index" :name="index" @click="click" :options="options"
								v-for="(item, index) in dataList" :key="item.id" class="border-bottom">
						<view class="ml-1">
							<view>{{item.createTime | formatTime}}</view>
							<view >
								<text >{{ item.title }}</text>
							</view>
						</view>
					</u-swipe-action-item>
				</u-swipe-action>
			</my-paging>
			<view style="position: fixed;bottom: 0;width: 500rpx">
			<u-button @click="popup_but" text="添加"></u-button>
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
	import { getLastData,getPage,deleteData,getInfo,saveData } from '@/api/record.js';
	import { formatTime } from '@/utils/util.js';
	import {uploadImage} from '@/api/file.js'
	import {baseUrl} from '@/api/service.js'
	export default {
		data() {
			return {
				openListShow:false,
				pickerShow:false,
				loginMenuShow:false,
				dataList:[],
				details:'',
				options: [{
					text: '查看',
					style: { backgroundColor: '#007aff' }
				},
				{
					text: '删除',
					style: { backgroundColor: '#dd524d' }
				}],
				form: {},
				oldForm:'',
			}
		},
		filters:{
			formatTime(time){
				return formatTime(time,'yyyy-M-d h时 D');
			},
		},
		onShow() {
			// 加载最后一条信息
			getLastData().then(res=>{
				this.form=res.data;
				this.oldForm=JSON.stringify(this.form);
			})
		},
		onHide() {
			if(!this.$store.state.isTemporary){
				//保存逻辑
				this.form.details=this.details;
				if(this.oldForm != JSON.stringify(this.form)&&null!=this.form.createTime){
					saveData(this.form).then(res=>{
						this.oldForm=JSON.stringify(this.form);
						uni.$u.toast('保存成功')
					})
				} 
			}
		},
		methods: {
			queryList(pageNo, pageSize) {
				const params = {
					pageNumber: pageNo,
					pageSize: pageSize
				}
				getPage(params).then(res=>{
					if(2000==res.code){
						this.$refs.paging.complete(res.data.rows);
					}
				})
			},
			click(rsp) {
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
					this.openListShow=false;
					this.form={}
					getInfo(params).then(res=>{
						this.form=res.data;
						this.oldForm=JSON.stringify(this.form);
					})
				}
			},
			popup_but(){
				this.form={}
				this.$refs.datetimePicker.setDateStr(null);
				this.openListShow=false
			},
			saveTime(e){
				this.form.createTime=e.f3;
			},
			upFile(callback) {
				uni.chooseImage({
					count: 5,
					sizeType: ['original', 'compressed'],
					sourceType: ['album', 'camera'],
					success: async(res) => {
						let tempFilePaths = res.tempFilePaths;
						let imageArr =[];
						for (let temp of tempFilePaths) {
							// 图片上传服务器
							uploadImage(temp).then(rsp=>{
								if(2000==rsp.code){
									imageArr.push({src: baseUrl+rsp.data.path,alt:rsp.data.name})
								}
							})
						}
						callback(imageArr)
					}
				});
			},
			editorChange(e){
				this.details=e;
			}
		}
	}
</script>

<style lang="scss">

</style>

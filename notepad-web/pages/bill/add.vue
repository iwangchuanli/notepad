<template>
	<view class="w-100">
		<tickler-navbar :isShowRightIcon="false" :isShowLeft="false" rightText="保存" leftIconColor="#fff"
		@rightClick="save"></tickler-navbar>
		 <view class="flex justify-center">
		 	<u--form :model="dataInfo" ref="uForm" labelWidth="70" style="width: 95%;">
		 		<u-form-item label="用  途 :" prop="purpose" borderBottom required>
		 			<u--input v-model="dataInfo.purpose" border="none"></u--input>
		 		</u-form-item>
		 		<u-form-item label="金  额 :" prop="money" borderBottom required>
		 			<u--input  type="digit" v-model="dataInfo.money" border="none"></u--input>
		 		</u-form-item>
		 		<u-form-item label="类  型" prop="type" borderBottom required>
		 			<u-radio-group v-model="dataInfo.type">
		 				<u-radio :customStyle="{marginRight: '16px'}"
		 					v-for="(item, index) in typeList"
		 					:key="index" :label="item.val" :name="item.key">
		 				</u-radio>
		 			</u-radio-group>
		 		</u-form-item>
		 		<template v-if="3==dataInfo.type">
		 			<u-form-item label="借款姓名:" prop="borrowerName" borderBottom>
		 				<u--input v-model="dataInfo.borrowerName" border="none"></u--input>
		 			</u-form-item>
		 			<u-form-item label="是否还完" prop="isPayOff" borderBottom>
		 				<u-radio-group v-model="dataInfo.isPayOff">
		 					<u-radio :customStyle="{marginRight: '16px'}"
		 						v-for="(item, index) in isPayOffList"
		 						:key="index" :label="item.val" :name="item.key">
		 					</u-radio>
		 				</u-radio-group>
		 			</u-form-item>
		 		</template>
		 		<u-form-item label="支付方式" prop="payType" borderBottom required>
		 			<u-radio-group v-model="dataInfo.payType" class="flex-wrap">
		 				<u-radio :customStyle="{marginRight: '16px',paddingBottom: '3px'}"
		 					v-for="(item, index) in payList"
		 					:key="index" :label="item.val" :name="item.key">
		 				</u-radio>
		 			</u-radio-group>
		 		</u-form-item>
		 		<u-form-item label="记录时间:" prop="updateTime" borderBottom required>
		 			<datetime-picker ref="datetimePicker"
		 				fields="day"
		 				start="2022-01-00 00:00:00"
		 				end="2125-12-12 24:59:59"
		 				:defaultValue="dataInfo.updateTime"
		 				@change="saveTime"
		 				placeholder="请选择时间"
		 			></datetime-picker>
		 		</u-form-item>
		 		<u-form-item>
					<male-editor :uploadFile="upFile" placeholder="描述" :fromItemHeight="300"
					@input="editorChange"></male-editor>
		 		</u-form-item>
		 	</u--form>
		 </view>
	</view>
</template>

<script>
	import {saveData } from '@/api/bill.js';
	import {uploadImage} from '@/api/file.js'
	import {baseUrl} from '@/api/service.js'
	export default {
		data() {
			return {
				dataInfo:{},
				parentId:0,
				details:'',
				typeList:[
					{key: 1, val:'收入'},
					{key: 2, val:'支出'},
					{key: 3, val:'借款'}
				],
				payList:[
					{key: 1, val:'支付宝'},
					{key: 2, val:'微信'},
					{key: 3, val:'银行卡'},
					{key: 4, val:'信用卡'},
				],
				isPayOffList:[{
					key:1 , val:'是'
				},{
					key:2 , val:'否'
				}],
				rules:{
					purpose:[{
						required: true,
						message: '请输入用途',
						trigger: ['blur', 'change']
					},{
							min: 2,
							max: 250,
							message: '最少2个字,最大250个字',
							trigger: ['blur', 'change']
					}],
					money:[{
						type: "number",
						required: true,
						message: '请输入金额',
						trigger: ['blur', 'change']
					},{
						validator: (rule, value, callback) => {
							return uni.$u.test.amount(value);
						},
						message: '可以是整数,最多两位小数',
						trigger: ['change','blur'],
					}],
					type:{
						type: "number",
						required: true,
						message: '请选类型',
						trigger: ['blur', 'change']
					},
					payType:{
						type: "number",
						required: true,
						message: '请选支付方式',
						trigger: ['blur', 'change']
					},
					updateTime:{
						required: true,
						message: '请选择记录时间',
						trigger: ['blur', 'change']
					}
				}
			}
		},
		onLoad(e) {
			this.parentId=e.pId;
		},
		methods: {
			saveTime(e){
				this.dataInfo.updateTime=e.f3;
			},
			save(){
				if(!this.dataInfo.purpose){
					uni.$u.toast('用途不能为空');
					return;
				}
				this.$refs.uForm.setRules(this.rules);
				this.$refs.uForm.validate().then(res => {
					this.dataInfo.details=this.details;
					this.dataInfo.parentId=this.parentId;
					saveData(this.dataInfo).then(rsp=>{
						if(2000==rsp.code){
							uni.$u.toast('保存成功')
							uni.$emit('refreshData');
							uni.navigateBack()
						}
					})
				})
			},
			upFile(callback) {
				uni.chooseImage({
					count: 3,
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

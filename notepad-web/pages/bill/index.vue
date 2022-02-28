<template>
	<view>
		<my-paging ref="paging" @query="queryList" @onRefresh="onRefresh">
			<view slot="top">
				<!-- 自定义导航栏 -->
				<tickler-navbar title="账单" rightIcon="edit-pen" @leftClick="loginMenuShow=true"
				 @rightClick="toSavePage"></tickler-navbar>
				<!-- tab -->
				<v-tabs v-model="tabIndex" height="90rpx"  :scroll="false" fontSize="30rpx" :tabs="listDataTab" @change="changeTab" ></v-tabs>
				<u-line class="u-line" :hair-line="false"></u-line>
				<template v-if="tabIndex==0">
					<bill-title :startTime="startTime" :endTime="endTime" :earningMoneyTotal="earningMoneyTotal"
						:expenseMoneyTotal="expenseMoneyTotal" @pickerVisableShow='pickerVisable=true'>
					</bill-title>
					<u-gap height="10" bg-color="#F1F2F6" class="w-100"></u-gap>
				</template>
			</view> 
			<!-- 数据 -->
			<view v-for="(item, index) in dataList" :key="index" class="flex flex-column align-center">
				<bill-body :data="item" :tabIndex="tabIndex" @clickBtn="clickBtn"></bill-body>
				<template v-if="index!=dataList.length-1">
					<u-gap height="10" bg-color="#F1F2F6" class="w-100"></u-gap>
				</template>
			</view>
		</my-paging>
		<term-picker :visable.sync="pickerVisable" timeLimit themeColor="#10BE9D" @confirm="confirmPicker"></term-picker>
		
		<!-- 登录菜单 -->
		<u-popup :show="loginMenuShow" @close="loginMenuShow=false"  safeAreaInsetTop mode="left"
		:customStyle='{"width":"500rpx","margin-top":"50rpx","border-radius": "8rpx","height":"100%"}'>
			<login-popup></login-popup>
		</u-popup>
	</view>
</template>
<script>
	import { getPage,getTimeData,deleteData } from '@/api/bill.js';
	export default {
		data() {
			return {
				dataList: [],
				dataTotal:0,
				tabIndex:0,
				loginMenuShow:false,
				listDataTab:['全部','收入','支出','借款'],
				startTime:'',
				endTime:'',
				// 收入总价格
				earningMoneyTotal: '0',
				// 支出总价格
				expenseMoneyTotal: "0",
				pickerVisable:false,
			}
		},
		onLoad() {
			const now= new Date();
			const year = now.getFullYear();
			let month=(now.getMonth()+1)< 10 ?'0'+(now.getMonth()+1):now.getMonth()+1;
			this.startTime=year+"-"+month;
			this.endTime=year+"-"+month;
			this.getTimeData();
			uni.$on('refreshData',() => {
					this.getTimeData();
					this.onRefresh()
					this.$refs.paging.reload();
				})
		},
		methods: {
			//根据时间获取总支出 总收入
			getTimeData(){
				const params = {
					startTime:this.startTime,
					endTime:this.endTime
				}
				getTimeData(params).then(res=>{
					let data=res.data;
					this.earningMoneyTotal=data.earningMoneyTotal;
					this.expenseMoneyTotal=data.expenseMoneyTotal;
					this.borrowMoneyTotal=data.borrowMoneyTotal;
				})
			},
			//分页查询
			queryList(pageNo, pageSize) {
				const params = {
					pageNumber: pageNo,
					pageSize: pageSize,
					tabNum:this.tabIndex,
				}
				if(0==this.tabIndex){
					params.startTime=this.startTime,
					params.endTime=this.endTime
				}
				getPage(params).then(res=>{
					if(0==res.data.total){
						this.$refs.paging.complete();
						return;
					}
					let resultData= res.data.rows;
					resultData.forEach(e=>{
						this.dataTotal+=e.detail.length;
					})
					let isLastShow=false;
					if(this.dataTotal<res.data.total){
						isLastShow=true;
					}
					
					if(!this.dataList.length){
						this.dataList=resultData;
					}else{
						let oldData= this.encapsulatingData(this.dataList,resultData);
						
						this.dataList=oldData.concat(resultData)
					}
					this.$refs.paging.completeByNoMore([1],isLastShow);
				})
			},
			changeTab(index){
				this.tabIndex = index;
				this.dataTotal=0;
				this.dataList=[];
				this.$refs.paging.reload();
			},
			onRefresh(){
				this.dataList=[];
				this.dataTotal=0;
			},
			//封装数据
			encapsulatingData(oldArr,newtArr){
				let lastItem= oldArr[oldArr.length-1];
				let newItem= newtArr[0];
				if(lastItem.updateTime==newItem.updateTime){
					let result={};
					result.updateTime=lastItem.updateTime;
					result.expenseMoney=!lastItem.expenseMoney?newItem.expenseMoney:lastItem.expenseMoney;
					result.earningMoney=!lastItem.earningMoney?newItem.earningMoney:lastItem.earningMoney;
					result.borrowMoney=!lastItem.borrowMoney?newItem.borrowMoney:lastItem.borrowMoney;
					result.detail=lastItem.detail.concat(newItem.detail);
					oldArr.pop();
					oldArr.push(result);
					newtArr.shift();
				}
				return oldArr;
			},
			confirmPicker(date){
				this.startTime=date[0];
				this.endTime=date[1];
				this.dataTotal=0;
				this.dataList=[];
				this.getTimeData()
				this.$refs.paging.reload();
			},
			clickBtn(index,id){
				// 0查看 1添加 2编辑 3删除
				if(0==index){
					//查看
					uni.$u.route('/pages/bill/details',{id:id})
				}else if(1==index){
					//添加
					uni.$u.route('/pages/bill/add',{pId:id})
				}else if(2== index){
					//编辑
					uni.$u.route('/pages/bill/update',{id:id})
				}else{
					deleteData({id:id}).then(res=>{
						this.dataTotal=0;
						this.dataList=[];
						this.getTimeData()
						this.$refs.paging.reload();
					})
				}
			},
			toSavePage(){
				//添加
				uni.$u.route('/pages/bill/add',{pId:0})
			}
		}
	}
</script>
<style lang="scss" scoped>
</style>
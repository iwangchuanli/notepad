<template>
	<view class="flex justify-center">
		<view class="w-90">
			<view style="height: 200rpx;" class="flex justify-center align-center flex-column">
				<text class="h2" :class="dataInfo.type==1?'text-success':dataInfo.type==2?'text-danger':'text-warning'">
					{{dataInfo.type==1?'+':dataInfo.type==2?'-':''}}{{dataInfo.money}}
				</text>
				<text class="text-light-muted">
					{{!dataInfo.borrowerName?'':dataInfo.borrowerName+"-"}}{{dataInfo.purpose}}
				</text>
			</view>
			<template v-if="dataInfo.borrowerName">
				<view  style="height: 80rpx;" class="flex justify-between align-center border-bottom">
					<text class="text-light-muted">借款姓名</text>
					<text>{{dataInfo.borrowerName}}</text>
				</view>
			</template>
			<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
				<text class="text-light-muted">用途</text>
				<text>{{dataInfo.purpose}}</text>
			</view>
			
			<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
				<text class="text-light-muted">类型</text>
				<text>{{dataInfo.type | getType}}</text>
			</view>
			
			<template v-if="dataInfo.isPayOff">
				<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
					<text class="text-light-muted">是否还完</text>
					<text>{{dataInfo.isPayOff==1?'已还完':'未还完'}}</text>
				</view>
			</template>
			
			<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
				<text class="text-light-muted">支付方式</text>
				<text>{{dataInfo.payType | getPayType}}</text>
			</view>
			
			<view style="height: 80rpx;" class="flex border-bottom justify-between align-center">
				<text class="text-light-muted">记录时间</text>
				<text>{{dataInfo.updateTime}}</text>
			</view>
			<template v-if="dataInfo.details">
				<view :style="{height:(dataInfo.details.length>200?'400rpx':'auto')}">
					<text class="text-light-muted">描述:</text>
					<u-parse class="border-bottom" :content="dataInfo.details" scrollTable selectable></u-parse>
				</view>
			</template>	
			<template v-if="dataInfo.childData">
				<u-card :full='true' v-for="(item,index) in dataInfo.childData" :key="index" :show-head='false' :show-foot='false'>
					<view slot="body">
						<view  style="height: 80rpx;" class="flex justify-between align-center border-bottom">
							<text class="text-light-muted">金额</text>
							<text>{{item.money}}</text>
						</view>
						<template v-if="item.borrowerName">
							<view  style="height: 80rpx;" class="flex justify-between align-center border-bottom">
								<text class="text-light-muted">借款姓名</text>
								<text>{{item.borrowerName}}</text>
							</view>
						</template>
						<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
							<text class="text-light-muted">用途</text>
							<text>{{item.purpose}}</text>
						</view>
						
						<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
							<text class="text-light-muted">类型</text>
							<text>{{item.type | getType}}</text>
						</view>
						
						<template v-if="item.isPayOff">
							<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
								<text class="text-light-muted">是否还完</text>
								<text>{{item.isPayOff==1?'已还完':'未还完'}}</text>
							</view>
						</template>
						
						<view style="height: 80rpx;" class="flex justify-between align-center border-bottom">
							<text class="text-light-muted">支付方式</text>
							<text>{{item.payType | getPayType}}</text>
						</view>
						
						<view style="height: 80rpx;" class="flex border-bottom justify-between align-center">
							<text class="text-light-muted">记录时间</text>
							<text>{{item.updateTime}}</text>
						</view>
						<template v-if="item.details">
							<view style="height: auto">
								<text class="text-light-muted">描述:</text>
								<u-parse :content="item.details" scrollTable selectable></u-parse>
							</view>
						</template>	
					</view>
				</u-card>
			</template>	
		</view>
	</view>
</template>

<script>
	import {getInfo } from '@/api/bill.js';
	export default {
		data() {
			return {
				dataInfo:{}
			};
		},
		filters:{
			getPayType(val){
				switch(val){
					case 1: return '支付宝';
					case 2: return '微信';
					case 3: return '银行卡';
					case 4: return '信用卡';
				}
			},
			getType(val){
				switch(val){
					case 1: return '收入';
					case 2: return '支出';
					case 3: return '借款';
				}
			}
		},
		onLoad(e) {
			this.getInfo(e.id)
		},
		methods:{
			getInfo(id){
				getInfo({id:id}).then(res=>{
					this.dataInfo=res.data
				})
			}
			
		}
		
	}
</script>

<style lang="scss">

</style>

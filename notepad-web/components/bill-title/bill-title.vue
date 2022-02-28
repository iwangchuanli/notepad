<template>
	<view>
		<view class="flex pl-4 mt-1">
			<view class="flex-1">
				<view class="text-light-muted" v-if="startTime.split('-')[0]==endTime.split('-')[0]">
					{{startTime.split('-')[0]}} 年
				</view>
				<view v-else>
					{{startTime.split('-')[0]}} 年-{{endTime.split('-')[0]}} 年
				</view>
			</view>
			<view class="flex-1 text-center">
				<view class="text-light-muted">收入</view>
			</view>
			<view class="flex-1 text-center">
				<view class="text-light-muted">支出</view>
			</view>
		</view>
		<view class="flex align-center pl-4 pb-2">
			<view class="flex-1" @tap="pickerVisableShow">
				<u-icon name="arrow-down-fill" top="8" style="margin-left: 50rpx;" color="#323232" size="30"></u-icon>
				<view class="flex align-center" style="width: 234rpx;">
					<text class="h2" v-if="startTime.split('-')[1]==endTime.split('-')[1]">
						{{startTime.split('-')[1]}}
					</text>
					<text class="h3" v-else>
						{{startTime.split('-')[1]}}-{{endTime.split('-')[1]}}
					</text>
					<text>月</text>
					<u-line class="u-line" margin="0rpx 40rpx" length="50" direction="col" color="#0c0c0c"></u-line>
				</view>
			</view>
			<view class="flex-1 mt-5 h3 text-center">{{earningMoneyTotal | numFilter}}</view>
			<view class="flex-1 mt-5 h3 text-center">{{expenseMoneyTotal | numFilter}}</view>
		</view>
	</view>
</template>

<script>
	export default {
		name:"bill-title",
		props: {
			startTime:{
				type: String,
				default: '',
				},
			endTime:{
				type: String,
				default: '',
				},
			earningMoneyTotal:{
				type: String,
				default: "0.0",
				},
			expenseMoneyTotal:{
				type: String,
				default: "0.0",
				}
		},
		filters:{
			numFilter (value) {
				let val= parseInt(value);
				if (val!=value) {
					return parseFloat(value).toFixed(2);
				  } else {
					return val;
				  }
			}
		},
		data() {
			return {
				
			};
		},
		methods:{
			pickerVisableShow(){
				this.$emit('pickerVisableShow')
			}
		}
	}
</script>

<style lang="scss">

</style>

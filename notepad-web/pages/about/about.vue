<template>
	<view class="container">
		<view class="flex align-center justify-center flex-column" style="height: 300rpx;">
			<u--image src="/static/logo.png" width="64" height="64"></u--image>
			<view style="height: 40px;" class="flex align-center">
				<text>Version {{version}}</text>
			</view>
		</view>
		<view class="content-box">
			<view class="content">项目可能存在缺陷或者bug，如果您在使用过程中发现问题或者有更好的建议，可反馈给我。</view>
			
		</view>

	</view>
</template>

<script>
	import { mapState } from 'vuex'
	export default {
		computed: mapState(['version']),
		data() {
			return {

			}
		},
		methods: {
		}
	}
</script>

<style>
	.container {
		padding: 0 50rpx 40rpx 50rpx;
		box-sizing: border-box;
	}
	.content-box {
		width: 100%;
		padding: 30rpx;
		box-sizing: border-box;
		background-color: #fff;
		border-radius: 10rpx;
		box-shadow: 0 0 10rpx #eee;
	}
	.content {
		color: #333;
		font-size: 30rpx;
		line-height: 44rpx;
		padding: 10rpx 0;
		text-align: justify;
	}
	.content-text {
		color: #5677fc !important;
	}
	.footer {
		display: flex;
		align-items: center;
		padding-top: 30rpx;
	}
	.footer-text {
		font-size: 24rpx;
		color: #999;
		padding-left: 20rpx;
		width: 300rpx;
	}
</style>

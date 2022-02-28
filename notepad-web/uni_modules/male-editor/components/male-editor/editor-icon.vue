<template>
	<view class="content">
		<view class="icon" :style="{color: color, fontSize: fontSize}" 
		:title="title" :data-name="dataName" :data-value="dataValue" 
		v-html="type" @click="toclick" :data-label="title"></view>
	</view>
</template>

<script>
	export default {
		props: {
			type: {
				type: String,
				default: '&#xe644;'
			},
			color: {
				type: String,
				default: '#666666'
			},
			fontSize: {
				type: String,
				default: '34rpx'
			},
			title:{
				type: String,
				default: ''
			},
			dataName:{
				type: String,
				default: ''
			},
			dataValue:{
				type: String,
				default: ''
			}
		},
		methods: {
			toclick(e) {
				this.$emit('click',e);
			}
		}
	}
</script>

<style scoped>
	.content{
		display: flex;
		align-items: center;
		justify-content: center;
	}
	@font-face {
		font-family: 'jin';
		/** 阿里巴巴矢量图标库的字体库地址，可以替换自己的字体库地址 **/
		src: url('https://at.alicdn.com/t/font_3182258_d9fuo7p4two.ttf?t=1644894502608') format('truetype');
	}

	.icon {
		font-family: jin !important;
		font-size: 34rpx;
	}

</style>

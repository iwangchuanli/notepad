<template>
	<view class="w-100">
		<u-navbar :title="title" border :leftIconColor="leftIconColor" :autoBack="!isShowLeft" placeholder bgColor='#39CCCC' :titleStyle='{color: "#fff" }'>
			<view slot="left" @click="leftClick" v-if="isShowLeft">
				<u--image src="/static/navbar/tiger.png" shape="circle" width="30" height="30"/>
				<!-- <u-icon :name="leftIcon" color="#ffffff" size="30"></u-icon> -->
			</view>
			<view slot="right" @click="rightClick" v-if="isShowRight">
				<template v-if="!isTemporary">
					<template v-if="isShowRightIcon">
						<u-icon :name="rightIcon" color="#ffffff" size="30"></u-icon>
					</template>
					<template v-else>
						<view class="font text-white">{{rightText}}</view>
					</template>
				</template>
			</view>
		</u-navbar>
	</view>
</template>

<script>
	export default {
		name:"tickler-navbar",
		props: {
			title: {
				type: String,
				default: ''
			},
			rightIcon: {
				type: String,
				default: ''
			},
			rightText: {
				type: String,
				default: ''
			},
			leftIcon: {
				type: String,
				default: 'more-dot-fill'
			},
			isShowLeft:{
				type:Boolean,
				default: true
			},
			isShowRight:{
				type:Boolean,
				default: true
			},
			leftIconColor:{
				type:String,
				default: '#303133'
			},
			isShowRightIcon:{
				type:Boolean,
				default:true
			},
		},
		data() {
			return {
				
			};
		},
		computed:{
			isTemporary(){
				return this.$store.state.isTemporary;
			}
		},
		methods:{
			leftClick(){
				this.$emit('leftClick')
			},
			rightClick(){
				this.$emit('rightClick')
			},
		}
	}
</script>

<style>

</style>

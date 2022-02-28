<template>
	<z-paging ref="paging" v-model="list" fixed auto-show-back-to-top refresher-threshold="160rpx" @query="queryList" @onRefresh="onRefresh">
		<!-- 这里插入一个view到z-paging中，并且这个view会被z-paging标记为top固定在顶部 -->
		<view slot="top">
			<!-- 这里接收页面传进来的slot，这样相当于将页面传进来的slot传给z-paging的slot="top"了 -->
			<slot name="top"></slot>
		</view>

		<!-- 这里插入一个view到z-paging中，并且这个view会被z-paging标记为bottom固定在底部 -->
		<view slot="bottom">
			<!-- 这里接收页面传进来的slot，这样相当于将页面传进来的slot传给z-paging的slot="bottom"了 -->
			<slot name="bottom"></slot>
		</view>

		<custom-back-to-top slot="backToTop" ref="backToTop" :current="current" :total="total"></custom-back-to-top>
		<!-- 这里通过slot自定义了下拉刷新view和没有更多数据view，页面那边就不用再写下面两行了 -->
		<!-- 自定义下拉刷新view(如果use-custom-refresher为true且不设置下面的slot="refresher"，此时不用获取refresherStatus，会自动使用z-paging自带的下拉刷新view) -->
		<custom-refresher slot="refresher" slot-scope="{refresherStatus}" :status="refresherStatus"></custom-refresher>
		
		<!-- 自定义没有更多数据view -->
		<!-- <custom-nomore slot="loadingMoreNoMore"></custom-nomore> -->

		<!-- 这里接收页面传进来的普通slot，如列表数据等 -->
		<slot />
	</z-paging>
</template>

<script>
	import customBackToTop from '@/components/my-paging/custom-back-to-top.vue';
	import customNomore from '@/components/my-paging/custom-nomore.vue';
	import customRefresher from '@/components/my-paging/custom-refresher.vue';
	export default {
		components: {
		    customBackToTop,
			customNomore,
			customRefresher
		},
		name: "my-paging",
		data() {
			return {
				list: [],
				current:0
			};
		},
		props: {
			//用于接收父组件v-model所绑定的list的值
			value: {
				type: Array,
				default: function() {
					return [];
				}
			},
			// 一共有多少页可以加载
			total: {
				type: Number,
				default: function() {
					return 0;
				},
			},
		},
		watch: {
			//监听页面v-mode传过来的值，同时传给z-paging
			value(newVal) {
				this.list = newVal;
			},
			//监听z-paging给当前组件的值，同时传给页面
			list(newVal) {
				//通过emit input修改页面中v-model绑定的值
				this.$emit('input', newVal);
			}
		},
		methods: {
			scroll() {
				if (this.$refs.backToTop) {
					this.$refs.backToTop.isScroll();
				}
			},
			//监听z-paging的@query事件，通过emit传递给页面
			queryList(pageNo, pageSize) {
				this.current=pageNo
				this.$emit('query', pageNo, pageSize);
			},
			onRefresh(){
				this.$emit('onRefresh');
			},
			//接收页面传递过来的reload事件，传给z-paging
			reload(data) {
				this.$refs.paging.reload(data);
			},
			//接收页面传递过来的complete事件，传给z-paging
			complete(data) {
				this.$refs.paging.complete(data);
			},
			//【自行判断是否有更多数据】请求结束(成功或者失败)调用此方法，将请求的结果传递给z-paging处理，第一个参数为请求结果数组，第二个参数为是否有更
			completeByNoMore(data, nomore, success = true){
				this.$refs.paging.completeByNoMore(data,nomore,success);
			},
			/*
			//如果是使用页面滚动，则需要添加以下两行，注意页面那边要引入mixins，与使用页面滚动示例写法相同。
			//接收页面传递过来的updatePageScrollTop事件，传给z-paging
			updatePageScrollTop(data){
				this.$refs.paging.updatePageScrollTop(data);
			},
			//接收页面传递过来的doLoadMore事件，传给z-paging
			doLoadMore(){
				this.$refs.paging.doLoadMore();
			}
			*/
		}
	}
</script>

<style>

</style>

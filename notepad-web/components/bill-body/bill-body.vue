<template>
	<view class="uni-swipe-action">
		<view class="flex justify-between align-center px-2" style="height: 100rpx;">
			<view class="font-md"> {{data.updateTime | formatTime}}</view>
			<view class="text-secondary text-right" style="font-size: 27rpx;">
				<view v-if="data.earningMoney">收入￥{{data.earningMoney|numFilter}}</view>
				<view v-if="data.expenseMoney">支出￥{{data.expenseMoney|numFilter}}</view>
				<view v-if="data.borrowMoney">借款￥{{data.borrowMoney|numFilter}}</view>
			</view>
		</view>
		<view class="w-100">
			<block v-for="(it,i) of data.detail" :key="i">
				<view class="border-bottom">
					<view class="uni-swipe-action__container" @touchstart="touchStart" @touchmove="touchMove" @touchend="touchEnd"
					 @touchcancel="touchEnd" :style="{'transform':messageIndex == i ? transformX : 'translateX(0px)','-webkit-transform':messageIndex == i ? transformX : 'translateX(0px)'}" :data-index="i">
						<view class="uni-swipe-action__content">
							<view class="flex justify-between align-center px-3" style="height: 120rpx;">
								<view class="font">
									<view>
										{{!it.borrowerName?'':it.borrowerName+"-"}}{{it.purpose}}
									</view>
									<text v-if="3==it.type" class="text-light-muted">
										是否还完 : {{it.isPayOff==1?'是':"否"}}
									</text>
									<text v-else class="text-light-muted">
										支付方式 : {{it.payType | getPayType}}
									</text>
								</view>
								<view>
									<text :class="it.type==1?'text-success':it.type==2?'text-danger':'text-warning'">
										{{it.type==1?'+':it.type==2?'-':''}}{{it.money|numFilter}}
									</text>
								</view>
							</view>
						</view>
						<template v-if="0==tabIndex">
							<view class="uni-swipe-action__btn-group" :id="elId">
								<view v-for="(item,index) in options.slice(1, options.length)" :key="index" class="uni-swipe-action--btn" :style="{backgroundColor: item.style && item.style.backgroundColor ? item.style.backgroundColor : '#C7C6CD',color: item.style && item.style.color ? item.style.color : '#FFFFFF',fontSize: item.style && item.style.fontSize ? item.style.fontSize : '28upx'}"
								 @click="bindClickBtn(item,it)">
									{{item.text }}
								</view>
							</view>
						</template>
						<template v-else>
							<view class="uni-swipe-action__btn-group" :id="elId">
								<view v-for="(item,index) in 3!=it.type?options.slice(1, options.length):options" :key="index" class="uni-swipe-action--btn" :style="{backgroundColor: item.style && item.style.backgroundColor ? item.style.backgroundColor : '#C7C6CD',color: item.style && item.style.color ? item.style.color : '#FFFFFF',fontSize: item.style && item.style.fontSize ? item.style.fontSize : '28upx'}"
								 @click="bindClickBtn(item,it)">
									{{item.text }}
								</view>
							</view>
						</template>
					</view>
				</view>
			</block>
		</view>
	</view>
</template>
<script>
	import { formatMdDTime } from '@/utils/util.js';
	export default {
		name: 'bill-body',
		props: {
			data:{
				type:Object
			},
			tabIndex:{
				type: Number,
				default: 0,
				},
		},
		data() {
			const elId = `Uni_${Math.ceil(Math.random() * 10e5).toString(36)}`
			return {
				elId: elId,
				transformX: 'translateX(0px)',
				messageIndex: -1,
				options:[{
						text: '添加',
						index:1,
						style: {
							backgroundColor: '#5CADAD'
						}
					},{
						text: '查看',
						index:0,
						style: {
							backgroundColor: '#007bff'
						}
					},{
						text: '编辑',
						index:2,
						style: {
							backgroundColor: '#28a745'
						}
					}, {
						text: '删除',
						index: 3,
						style: {
							backgroundColor: '#dd524d'
						}
					}]
			}
		},
		filters:{
			formatTime(time){
				return formatMdDTime(time);
			},
			getPayType(val){
				switch(val){
					case 1: return '支付宝';
					case 2: return '微信';
					case 3: return '银行卡';
					case 4: return '信用卡';
				}
			},
			numFilter (value) {
				let val= parseInt(value);
				if (val!=value) {
					return parseFloat(value).toFixed(2);
				  } else {
					return val;
				  }
			}
		},
		created() {
			this.direction = ''
			this.startX = 0
			this.startY = 0
			this.btnGroupWidth = 0
			this.isMoving = false
		},
		mounted() {
			this.getSize()
		},
		onReady() {
			this.getSize()
		},
		methods: {
			getSize() {
				uni.createSelectorQuery().in(this).select(`#${this.elId}`).boundingClientRect().exec((ret) => {
					this.btnGroupWidth = ret[0].width;
				});
			},
			bindClickBtn(item, data) {
				this.messageIndex = -1;
				this.$emit('clickBtn', item.index, data.id);
			},
			touchStart(event) {
				if(event.currentTarget.dataset.disabled === true){
					return;
				}
				this.startX = event.touches[0].pageX;
				this.startY = event.touches[0].pageY;
			},
			touchMove(event) {
				if (this.direction === 'Y' || event.currentTarget.dataset.disabled === true) {
					this.direction = '';
					return;
				}
				var moveY = event.touches[0].pageY - this.startY,
						moveX = event.touches[0].pageX - this.startX;
				if (!this.isMoving && Math.abs(moveY) > Math.abs(moveX) || Math.abs(moveY) > 100 || Math.abs(moveX) < 50) { //纵向滑动//参数100与50可调节侧滑灵敏度
					this.direction = 'Y';
					return;
				}
				this.direction = moveX > 0 ? 'right' : 'left';
				this.messageIndex = moveX < 0 ? event.currentTarget.dataset.index : -1;
				this.isMoving = true;
			},
			touchEnd(event) {
				this.isMoving = false;
				if (this.direction !== 'right' && this.direction !== 'left') {
					this.direction = '';
					return;
				}
				if (this.direction == 'right') {
					this.messageIndex = -1;
				} 
				this.endMove(event)
			},
			endMove(event) {
				if (this.direction === 'Y' || event.currentTarget.dataset.disabled === true) {
					this.direction = '';
					return;
				}
				if (this.messageIndex !== -1) {
					this.transformX = `translateX(${-this.btnGroupWidth}px)`;
					this.$emit('opened');
				} else {
					this.transformX = 'translateX(0px)';
					this.$emit('closed');
				}
				this.direction = '';
			},
		}
	}
</script>

<style lang="scss">
	.uni-swipe-action {
		width: 100%;
		overflow: hidden;
		&__container {
			background-color: #FFFFFF;
			width: 200%;
			display: flex;
			flex-direction: row;
			flex-wrap: wrap;
			transition: transform 350ms cubic-bezier(.165, .84, .44, 1);
		}
		&__content {
			width: 50%;
		}
		&__btn-group {
			display: flex;
			flex-direction: row;
		}
		&--btn {
			padding: 0 32upx;
			color: #FFFFFF;
			background-color: #C7C6CD;
			font-size: 28upx;
			display: inline-flex;
			text-align: center;
			flex-direction: row;
			align-items: center;
		}
	}
</style>
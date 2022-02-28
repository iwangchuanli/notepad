<template>
	<view class="container">
		<view class="text">
			用户在使用我们提供的网络服务前有义务仔细阅读本协议。用户在此不可撤销地承诺，若其使用，将视为用户同意并接受本协议全部条款的约束，此后用户无权以未阅读本协议或对本协议有任何误解为由，主张本协议无效或要求撤销本协议。
		</view>
		<view class="text">
			第一条、保护用户个人信息是一项基本原则，我们将会采取合理的措施保护用户的个人信息。除法律法规规定的情形外，未经用户许可我们不会向第三方公开、透漏个人信息。APP对相关信息采用专业加密存储与传输方式，保障用户个人信息安全，如果您选择同意使用APP软件， 即表示您认可并接受APP服务条款及其可能随时更新的内容。
		</view>
		<view class="text">
			第二条、我们将会使用您的以下功能：麦克风、喇叭、WIFI网络、蜂窝通信网络、手机基站数据、SD卡、短信控制、通话权限、蓝牙管理，如果您禁止APP使用以上相关服务和功能，您将自行承担不能获得或享用APP相应服务的后果。
		</view>
		<view class="text">
			第三条、在您使用我们的产品或服务的过程中，我们可能：需要您提供个人信息，如姓名、电子邮件地址、电话号码、联系地址等以及注册或申请服务时需要 的其它类似个人信息；您对我们的产品和服务使用即表明您同意我们对这些信息的收集和合理使用。您可以自行选择拒绝、放弃使用相关产品或服务。
		</view>
		<view class="text">
			第四条、由于您的自身行为或不可抗力等情形，导致上述可能涉及您隐私或您认为是私人信息的内容发生被泄露、批漏，或被第三方获取、使用、转让等情形的，均由您自行承担不利后果，我们对此不承担任何责任。
		</view>
		<view class="text">
			第五条、我们拥有对上述条款的最终解释权。
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {};
	}
};
</script>

<style lang="scss">
.container {
	width: 100%;
	padding: $uni-spacing-row-lg;
	box-sizing: border-box;
	padding-bottom: 48rpx;
	.text {
		font-size: $uni-font-size-sm;
		text-indent: 2em;
		color: $uni-text-color;
		padding-bottom: $uni-spacing-col-sm;
		text-align: justify;
	}
}
</style>

<template>
	<view class="container">
		<view class='wrapper'>
			<!--  -->
			<view class="editor-wrapper">
				<editor id="editor" class="ql-container" :placeholder="placeholder" 
				showImgSize showImgToolbar showImgResize @statuschange="onStatusChange" 
				:read-only="readOnly" @ready="onEditorReady" @input="editorChange">
				</editor>
			</view>
			<!-- 操作工具 -->
			<!--  -->
			<view v-if="autoHideToolbar" :style="'position:absolute;bottom:' + (isIOS ? keyboardHeight : 0) + 'px'"> 
				<view class="flex align-center justify-center">
					<editorIcon class="single" type="&#xe8ba;" font-size="44rpx" title="插入图片" @click="selectImage"/>
					<editorIcon class="single" type="&#xe6ec;" font-size="44rpx" title="修改文字样式" @click="showMore('1')" :color="showTextMoreTool ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xec88;" font-size="44rpx" title="对齐方式" @click="showMore('2')" :color="showAlignMoreTool ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xe64c;" font-size="44rpx" title="排列" @click="showMore('3')" :color="showListMoreTool ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xe617;" font-size="44rpx" title="撤销" @click="undo" />
					<editorIcon class="single" type="&#xe65d;" font-size="44rpx" title="重做" @click="redo"/>
					<editorIcon class="single" type="&#xe946;" font-size="44rpx" title="清空" @click="clear"/>
				</view>
				<!-- 文字相关操作 -->
				<view class="flex align-center justify-center flex-wrap" @tap="format" v-if="showTextMoreTool" :style="'position:absolute;bottom:' + 50 + 'px'">
					<editorIcon class="single" type="&#xec83;" font-size="44rpx" title="加粗" dataName="bold" :color="formats.bold ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xec85;" font-size="44rpx" title="斜体" dataName="italic" :color="formats.italic ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xe72c;" font-size="44rpx" title="文字颜色" dataName="color" :color="formats.color ? formats.color : '#666666'"  @click="openColor"/>
					<picker :range="headerlist" @change="formatsChange" @tap.stop="formatsChange" data-name="header">
						<editorIcon type="&#xe609;" font-size="44rpx" title="文字标题" :color="formats.header ? activeColor : '#666666'">
						</editorIcon>
					</picker>
					<editorIcon class="single" type="&#xe60e;" font-size="44rpx" title="分割线" :color="divider ? activeColor : '#666666'" @tap="insertDivider"/>
					<editorIcon class="single" type="&#xe710;" font-size="44rpx" title="下划线" dataName="underline" :color="formats.underline ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xe645;" font-size="44rpx" title="删除线" dataName="strike" :color="formats.strike ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xe678;" font-size="44rpx" title="文字背景色" dataName="backgroundColor" :color="formats.backgroundColor ? formats.backgroundColor : '#666666'" @click="openColor"/>
					<editorIcon class="single" type="&#xe655;" font-size="44rpx" title="清除样式" @click="removeFormat" />
					<picker range-key="name" :range="fontSizelist" data-name="fontSize" @change="formatsChange" @tap.stop="formatsChange">
						<editorIcon class="single" type="&#xe621;"  font-size="44rpx" title="文字大小"
						:color="formats.fontSize ? activeColor : '#666666'"></editorIcon>
					</picker>
				</view>
				<!-- 对齐相关操作 -->
				<view class="w-100 flex align-center justify-center flex-wrap" @tap="format" v-if="showAlignMoreTool" :style="'position:absolute;bottom:' + 50 + 'px'">
					<editorIcon class="single" type="&#xec86;" font-size="44rpx" title="左对齐" dataName="align" dataValue="left" :color="formats.align== 'left' ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xec80;" font-size="44rpx" title="居中对齐" dataName="align" dataValue="center" :color="formats.align=='center' ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xec82;" font-size="44rpx" title="右对齐" dataName="align"  dataValue="right" :color="formats.align=='right' ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xec88;" font-size="44rpx" title="左右对齐" dataName="align"  dataValue="justify" :color="formats.align=='justify' ? factiveColor : '#666666'"/>
				</view>
				<!-- 排列相关操作 -->
				<view class="w-100 flex align-center justify-center flex-wrap" @tap="format" v-if="showListMoreTool" :style="'position:absolute;bottom:' + 50 + 'px'">
					<editorIcon class="single" type="&#xe63e;" font-size="44rpx" title="无序排列" dataName="list" dataValue="bullet" :color="formats.list === 'bullet' ? activeColor : '#666666'"/>
					<editorIcon class="single" type="&#xe64c;" font-size="44rpx" title="有序排列" dataName="list" dataValue="ordered" :color="formats.list === 'ordered' ? activeColor : '#666666'"/>
				</view>
			</view>
		</view>
		
		<male-color-picker ref="colorPicker" :hexcolor="hexcolor" @confirm="getColor"></male-color-picker>
	</view>

</template>

<script>
	import editorIcon from './editor-icon.vue';
	export default {
		components: {
			editorIcon
		},
		props: {
			html: {
				type: String
			},
			placeholder: {
				type: String,
				default: '开始输入...'
			},
			uploadFile: {
				type: Function
			},
			fromItemHeight:{
				type: Number,
				default: 0
			},
		},
		data() {
			return {
				showTextMoreTool:false,
				showAlignMoreTool:false,
				showListMoreTool:false,
				activeColor: '#F56C6C',
				autoHideToolbar:true,
				divider:false,
				editorHeight: 0,
				keyboardHeight:0,
				isIOS: false,
				fontSizelist: [{
					code: "",
					name: "默认"
				}, {
					code: "x-small",
					name: "1"
				}, {
					code: "small",
					name: "2"
				}, {
					code: "medium",
					name: "3"
				}, {
					code: "large",
					name: "4"
				}, {
					code: "x-large",
					name: "5"
				}, {
					code: "xx-large",
					name: "6"
				}, {
					code: "-webkit-xxx-large",
					name: "7"
				}],
				headerlist: ['默认', 'H1', 'H2', 'H3', 'H4', 'H5', 'H6'],
				headerindex: 0,
				colorPickerName: '',
				hexcolor: "#0000ff",
				readOnly: false,
				formats: {},
			}
		},
		watch: {
			html: function(newvar){
				if (this.editorCtx) {
				    this.editorCtx.setContents({
				        html: newvar
				    });
				}
			}
		},
		mounted: function() {
		    const platform = uni.getSystemInfoSync().platform;
		    this.isIOS = platform === 'ios';
		    
		    let keyboardHeight = 0;
		    this.updatePosition(0);
		    uni.onKeyboardHeightChange(res => {
		        if (res.height === keyboardHeight) return;
		        const duration = res.height > 0 ? res.duration * 1000 : 0;
		        keyboardHeight = res.height;
		        setTimeout(() => {
		            uni.pageScrollTo({
		                scrollTop: 0,
		                success: () => {
							this.autoHideToolbar=!this.autoHideToolbar;
		                    this.updatePosition(keyboardHeight);
		                    this.editorCtx && this.editorCtx.scrollIntoView();
		                }
		            });
		        }, duration);
		    });
		},
		methods: {
			updatePosition(keyboardHeight) {
			    const { windowHeight, windowWidth, platform } = uni.getSystemInfoSync();
			    const rpx = windowWidth / 750;
			    let titleHeight = 0;
			    //#ifdef H5
			    titleHeight = 44+50+this.fromItemHeight-20; //H5标题栏高度+tabber+两个fromitem
			    //#endif
				// #ifdef APP-PLUS
				let fromItemHeight=this.isIOS?this.fromItemHeight:this.fromItemHeight+10;
				titleHeight = 45+50+fromItemHeight; //app标题栏高度+tabber+两个fromitem
				// #endif
			    let bodyHeight = windowHeight - titleHeight;
			    this.keyboardHeight = keyboardHeight;
			    this.editorHeight = keyboardHeight > 0 ? bodyHeight - keyboardHeight : bodyHeight ;
			},
			openColor(e) {
				let dataset = e.target.dataset
				this.colorPickerName = dataset.name;
				this.hexcolor = dataset.value;
				this.$refs.colorPicker.open();
			},
			getColor(e) {
				let msg = '';
				switch (this.colorPickerName) {
					case 'backgroundColor':
						if (e.hex.toUpperCase() == '#FFFFFF') {
							e.hex = '';
						}
						msg = '背景色';
						break;
					case 'color':
						msg = '颜色';
						break;
				}
				this.setformat(this.colorPickerName, e.hex, msg + e.hex);
			},
			formatsChange(e) {
				if (e.type == 'click') { //不让上层触发点击事件
					return false;
				}
				let value = e.detail.value;
				let name = e.target.dataset.name
				if (name == 'header') {
					this.headerindex = value;
					if (value == 0) {
						value = null;
					}
				} else if (name == 'fontSize') {
					value = this.fontSizelist[value].code;
				} else if (name == 'size') {
					value = value > 0 ? value : 1;
				}
				this.setformat(name, value)
				return false;
			},
			editorChange(e){
				this.$emit('input', e.detail.html);
			},
			readOnlyChange() {
				this.readOnly = !this.readOnly
			},
			onEditorReady() {
				uni.createSelectorQuery().in(this).select('#editor').context(res => {
				        this.editorCtx = res.context;
				        if (this.html) {
				            this.editorCtx.setContents({
				                html: this.html
				            });
				        }
				    })
				    .exec();
			},
			undo() {
				this.editorCtx.undo()
				this.toast('撤销');
			},
			redo() {
				this.editorCtx.redo()
				this.toast('重做');
			},
			toast(label) {
			    uni.showToast({
			        duration: 600,
			        icon: 'none',
			        title: label
			    });
			},
			format(e) {
				let { name, value } = e.target.dataset
				if (!name) return
				this.editorCtx.format(name, value)

			},
			setformat(name, value, msg) {
				this.editorCtx.format(name, value);
				this.toast(msg);
			},
			onStatusChange(e) {
				const formats = e.detail
				this.formats = formats
			},
			insertDivider() {
				this.editorCtx.insertDivider()
				this.toast('插入分割线');
				this.divider=!this.divider;
			},
			clear() {
				this.editorCtx.clear()
				this.toast('清空');
			},
			removeFormat() {
				this.editorCtx.removeFormat()
				this.toast('清除格式');
			},
			selectImage() {
				this.uploadFile(data => {
					if(uni.$u.test.object(data)){
						this.editorCtx.insertImage(data);
					}else{
						data.forEach(e=>{
							this.editorCtx.insertImage({
							    src: data.url,
							    alt: data.alt
							});
						})
					}
				});
			},
			showMore(val){
				switch(val){
					case "1":
					this.showTextMoreTool=!this.showTextMoreTool;
					this.showAlignMoreTool=false;
					this.showListMoreTool=false;
					break;
					case "2":
					this.showAlignMoreTool=!this.showAlignMoreTool;
					this.showTextMoreTool=false;
					this.showListMoreTool=false;
					break;
					case "3":
					 this.showListMoreTool =!this.showListMoreTool
					 this.showAlignMoreTool=false;
					 this.showTextMoreTool=false;
					break;
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		padding-right: 0px;
		padding-left: 0px; 
		.wrapper {
			width: 100%;
			.editor-wrapper {
				width: 100%;
				background: #fff;
				.ql-container {
					box-sizing: border-box;
					width: 100%;
					min-height: 45vh;
					height: 100%;
					font-size: 16px;
					line-height: 1.5;
				}
			}
		}
	}
	.ql-active {
		color: #F56C6C;
	}

	.single {
		height: 80rpx;
		font-size: 32rpx;
		padding: 0 30rpx;
		display: flex;
		align-items: center;
		line-height: 80rpx;
		flex-direction: row;
		color: #666;
	}
</style>

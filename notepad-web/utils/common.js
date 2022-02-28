/*以下代码仅供参考，如有错误还请各位大哥大姐指导修正*/

//简单时间格式化
//使用方法 供新手参考 :
// 在vue文件中 import { dateFormat } from '@/js_sdk/common.js'
//let date = new Date();
// dateFormat("YYYY-mm-dd HH:mm",date)
export const dateFormat = (fmt, obj) => {
	var date;
	if (obj instanceof Date) {
	  date = obj;
	} else {
	  date = new Date(obj);
	}
	let ret;
	const opt = {
		"Y+": date.getFullYear().toString(), // 年
		"m+": (date.getMonth() + 1).toString(), // 月
		"d+": date.getDate().toString(), // 日
		"H+": date.getHours().toString(), // 时
		"M+": date.getMinutes().toString(), // 分
		"S+": date.getSeconds().toString() // 秒
		// 有其他格式化字符需求可以继续添加，必须转化成字符串
	};
	for (let k in opt) {
		ret = new RegExp("(" + k + ")").exec(fmt);
		if (ret) {
			fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
		};
	};
	return fmt;
}

// 数据深拷贝
//使用方法 供新手参考 :
// 在vue文件中 import { cloneObj } from '@/js_sdk/common.js'
// let dataList = cloneObj(data)
export const cloneObj = (obj) => {
	let newobj = obj.constructor === Array ? [] : {};
	if (typeof obj !== 'object') {
		return;
	}
	for (let i in obj) {
		newobj[i] = typeof obj[i] === 'object' ? cloneObj(obj[i]) : obj[i];
	}
	return newobj
};

// 常用正则判断
//使用方法 供新手参考 :
// 在vue文件中 import { regExpObj } from '@/js_sdk/common.js'
// let valueReg = regExpObj.regExpZh(value)
export const regExpObj = {
	//匹配有没有中文
	regExpZh: (str) => {
		return RegExp(/[\u4e00-\u9fa5]+/).test(str)
	},
	//只允许中文
	onlyregExpZh: (str) => {
		return RegExp(/^[\u4e00-\u9fa5]+$/).test(str)
	},
	//只允许中文，英文字母，数字
	regExpZhEnNum: (str) => {
		return RegExp(/^[\u4e00-\u9fa5A-Za-z0-9]+$/).test(str)
	},
	//匹配身份证
	regExpIDCard: (str) => {
		return RegExp(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/).test(str);
	},
	//匹配手机号
	regExpPhone: (str) => {
		return RegExp(/^1[3456789]\d{9}$/).test(str);
	},
	//匹配邮箱格式
	regExpEmail: (str) => {
		return RegExp(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test(str);
	},
	//匹配域名
	regExpRealmName: (str) => {
		return RegExp(/[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\/.?/).test(str)
	},
	//匹配InternetURL
	regExpInternetURL: (str) => {
		return RegExp(/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/).test(str)
	},
	//匹配密码（密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)）
	regExpPwd: (str) => {
		return RegExp(/^[a-zA-Z]\w{5,17}$/).test(str)
	},
	//日期格式  2019-01-12
	regExpDate: (str) => {
		return RegExp(/^\d{4}-\d{1,2}-\d{1,2}/).test(str)
	},
	//匹配空白行
	regExpWhiteLine: (str) => {
		return RegExp(/\n\s*\r/).test(str)
	},
	//匹配正数、负数、和小数
	regExpNumberFloat: (str) => {
		return RegExp(/^(\-|\+)?\d+(\.\d+)?$/).test(str)
	},
	//匹配零和非零开头的数字
	regExpZeroNumber: (str) => {
		return RegExp(/^(0|[1-9][0-9]*)$/).test(str)
	},
	//匹配数字
	regExpNumber: (str) => {
		return RegExp(/^[0-9]*$/).test(str)
	},
	//长度为8-20的所有字符
	regExpAllByte: (str) => {
		return RegExp(/^.{8,20}$/).test(str)
	}
}

//判断枚举对象取值
// 在vue文件中 import { enumeration } from '@/js_sdk/common.js'
// let valueStatus = enumeration(enumObj,0);
export const enumeration = (enumObj, index) => {
	//enumObj ====> 枚举对象 例如：{0:"不通过",1:"通过",2:"待审批"};
	//index ====> 查询属性值或属性名
	for (let i in enumObj) {
		if (i == index) {
			return enumObj[i];
		} else if (enumObj[i] == index) {
			return i;
		}
	}
}

//本地缓存
export const dataStroage = {
	//设置离线缓存
	setStroage: (key, data) => {
		uni.setStorageSync(key, data);
	},
	//获取离线缓存
	getStroage: (key) => {
		let data = uni.getStorageSync(key);
		return data;
	},
	//移除某个离线缓存
	removeStroage: (key) => {
		uni.removeStorageSync(key);
	},
	//移除所有离线缓存
	closeAllStroage: () => {
		uni.clearStorageSync();
	}
}

// 原生Js获取URL传递的参数
export const getUrlParam = (name) => {
	let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	let r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;
}

//大于固定字符串显示省略号
export const handleStr = (str,length) => {
	let handle_str;
	str.length <= length ? handle_str = str : handle_str = str.slice(0, length) + "...";
	return handle_str
}

/* 倒计时
  endtime 结束时间
  nowtime 开始时间 默认值时当前时间
*/
export const countDown = (endtime,nowtime) => {
  let day = 0,
    hour = 0,
    minute = 0,
    second = 0, //时间默认值
    newtime = nowtime || new Date().valueOf()/1000,
    times = endtime - newtime;
  if (times > 0) {
    day = Math.floor(times / (60 * 60 * 24));
    hour = Math.floor(times / (60 * 60)) - (day * 24);
    minute = Math.floor(times / 60) - (day * 24 * 60) - (hour * 60);
    second = Math.floor(times) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
  }
  day <= 9 ? day = '0' + day : null;
  hour <= 9 ? hour = '0' + hour : null;
  minute <= 9 ? minute = '0' + minute : null;
  second <= 9 ? second = '0' + second : null;
  return {day,hour,minute,second}
}

/* 判断数据类型 */
export const isDataType = {
	isArray:(obj) => {
	  return (typeof obj == 'object') && obj.constructor == Array;
	},
	isObject:(obj) => {
	  return Object.prototype.toString.call(obj) === "[object Object]";
	},
	isEmptyObject:(obj) => {
	  let t;
	  for (t in obj) return !1;
	  return !0
	}
}
// 数组排序
export const arraySort = (arryObj)=> {
	//此为升序，若需降序请自行修改代码if条件大于改为小于
	let tmp;
	for(let i = 0;i<arryObj.length;i++){
		for(let j = 0;j<arryObj.length;j++){
			if(arryObj[j]>arryObj[j+1]){
				tmp = arryObj[j];
				arryObj[j] = arryObj[j+1];
				arryObj[j+1] = tmp;
			}
		}
	}
	return arryObj;
}

// 数组去重
export const arrayRemoval = (arrayObj) => {
	let tmpArray = [];
	for(let i = 0;i<arrayObj.length;i++){
		if(tmpArray.indexOf(arrayObj[i]) == -1){
			tmpArray.push(arrayObj[i]);
		}
	}
	return tmpArray;
}

//数组筛选过滤
export const arrayFilter = (arrayObj,keyValue) => {
	let tmpArray = [];
	for(let i = 0;i<arrayObj.length;i++){
		if(arrayObj[i].toString().indexOf(keyValue) != -1){
			tmpArray.push(arrayObj[i]);
		}
	}
	return tmpArray;
}
//数组对象指定属性过滤
export const arrayObjFilter = (arrayObj,keyAttributeArray,keyValue) => {
	// 在vue文件中 import { enumeration } from '@/js_sdk/common.js'
	// 使用列子：
	// let arry = [{
	// 	name:"dsa",
	// 	key:"152"
	// 	},{
	// 		name:"ooo",
	// 		key:"sdf"
	// 	},{
	// 		name:123,
	// 		key:"wewe"
	// 	},{
	// 		name:"loe",
	// 		key:"uuwe"
	// 	}];
	//arrayObjFilter(arry,[""name","key"],"s");
	let tmpArray = [];
	for(let i = 0;i<arrayObj.length;i++){
		for(let j in arrayObj[i]){
			for(let l = 0;l<keyAttributeArray.length;l++){
				if(j == keyAttributeArray[l]){
					if(arrayObj[i][j].toString().indexOf(keyValue) != -1){
						tmpArray.push(arrayObj[i]);
					}
				}
			}
		}
	}
	return tmpArray;
	
}

// 金钱格式化,三位逗号
export const formatMoney = (num)=>{
	let newNum = num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	return newNum;
}

// 生成一定范围内随机数
export const RandomNum = (min, max) => {
	let num = Math.floor(Math.random() * (max - min + 1)) + min;
	return num;
};

// 数组交集
export const similarity = (arr1, arr2) => arr1.filter(v => arr2.includes(v));


//数组中某元素出现的次数
/**
 * @param { array } arr
 * @param {*} value
 */
export const countOccurrences = (arr, value)=> {
    return arr.reduce((a, v) => v === value ? a + 1 : a + 0, 0);
}

// 字符串去除空格
/**
 * @param { string } str 待处理字符串
 * @param  { number } status 去除空格状态 1-所有空格  2-前后空格  3-前空格 4-后空格 默认为1
 */
export const removeBlank = (str, status = 1)=> {
    if (status && status !== 1 && status !== 2 && status !== 3 && status !== 4) return;
    switch (status) {
        case 1:
            return str.replace(/\s/g, "");
        case 2:
            return str.replace(/(^\s)|(\s*$)/g, "");
        case 3:
            return str.replace(/(^\s)/g, "");
        case 4:
            return str.replace(/(\s$)/g, "");
        default:
            return str;
    }
}

//字体大小写转换
/**
 * @param { string } str 待转换的字符串
 * @param { number } type 1-全大写 2-全小写 3-首字母大写 其他-不转换
 */

export const turnCase = (str, type)=> {
    switch (type) {
        case 1:
            return str.toUpperCase();
        case 2:
            return str.toLowerCase();
        case 3:
            return str[0].toUpperCase() + str.substr(1).toLowerCase();
        default:
            return str;
    }
}

// 数字超过规定大小加上加号“+”，如数字超过99显示99+
/**
 * @param { number } val 输入的数字
 * @param { number } maxNum 数字规定界限
 */
export const numberDefine = (val, maxNum) =>{
    val = val ? val-0 :0;
    if (val > maxNum ) {
        return `${maxNum}+`
    }else{
        return val;
    }
};


//函数防抖方法
/**
 * @param { function } func
 * @param { number } wait 延迟执行毫秒数
 * @param { boolean } immediate  true 表立即执行，false 表非立即执行
 */
export const debounce = (func,wait,immediate)=> {
    let timeout;
    return function () {
        let context = this;
        let args = arguments;

        if (timeout) clearTimeout(timeout);
        if (immediate) {
            let callNow = !timeout;
            timeout = setTimeout(() => {
                timeout = null;
            }, wait);
            if (callNow) func.apply(context, args)
        }
        else {
            timeout = setTimeout(() => {
                func.apply(context, args)
            }, wait);
        }
    }
}

//函数节流方法
/**
 * @param { function } func 函数
 * @param { number } wait 延迟执行毫秒数
 * @param { number } type 1 表时间戳版，2 表定时器版
 */
export const throttle = (func, wait ,type)=> {
    let previous, timeout;
    if(type===1){
        previous = 0;
    }else if(type===2){
        timeout = null;
    }
    return function() {
        let context = this;
        let args = arguments;
        if(type===1){
            let now = Date.now();

            if (now - previous > wait) {
                func.apply(context, args);
                previous = now;
            }
        }else if(type===2){
            if (!timeout) {
                timeout = setTimeout(() => {
                    timeout = null;
                    func.apply(context, args)
                }, wait)
            }
        }

    }
}


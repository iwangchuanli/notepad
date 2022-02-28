
export const formatMdDTime = (obj) => {
	return formatTime(obj,'M月d日 D')
}

/**
 * 格式化时间
 * @param {*} obj 
 * @param {*} format 
 * @example utilscore.formatTime(new Date(),'yyyy-M-d h:m:s D') // => 2019-05-10 17:37:24 星期六
 */
export const formatTime = (obj, format) => {
    if (format) {
      var date;
	  obj = obj.replace(/-/g, '/')
      if (obj instanceof Date) {
        date = obj;
      } else {
        date = new Date(obj);
      }
	  
      var dayNames = [ '周日','周一', '周二', '周三', '周四', '周五', '周六',]

      var o = {
        'M+': date.getMonth() < 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1), // 月份
        'd+': date.getDate() < 10 ? "0" + date.getDate() : date.getDate(), // 日
        'h+': date.getHours(), // 小时
        'm+': date.getMinutes(), // 分
        's+': date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds(), // 秒
        'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
        'S+': date.getMilliseconds(), // 毫秒
        'D+': dayNames[date.getDay()], //星期
      };

      if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (`${date.getFullYear()}`).substr(4 - RegExp.$1.length));
      for (var k in o) {
        if (new RegExp(`(${k})`).test(format)) {
          format = format.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : ((`
00${o[k]}`).substr((`${o[k]}`).length)));
        }
      }
      return format;
    } else {
      let date = new Date(obj)
      let _year = date.getFullYear(),
        _month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : '0' + (date.getMonth() + 1),
        _date = date.getDate(),
        _hour = date.getHours(),
        _minute = date.getMinutes(),
        _second = date.getSeconds()
      return _year + '-' + _month + '-' + _date + ' ' + _hour + ':' + _minute + ':' + _second
    }
  }


  /**
   * @param  {s} 秒数
   * @return {String} 字符串
   * @example utilscore.formatHMS(3610) // -> 1h0m10s
   */
  export const formatHMS = (s) => {
    var str = ''
    if (s > 3600) {
      str = Math.floor(s / 3600) + 'h' + Math.floor(s % 3600 / 60) + 'm' + s % 60 + 's'
    } else if (s > 60) {
      str = Math.floor(s / 60) + 'm' + s % 60 + 's'
    } else {
      str = s % 60 + 's'
    }
    return str
  }
  /**
   * toast
   */
  export const msg = (title = '', param={}) => {
  	if(!title) return;
  	uni.showToast({
  		title,
  		duration: param.duration || 1500,
  		mask: param.mask || false,
  		icon: param.icon || 'none'
  	});
  }
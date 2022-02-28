import { versionApp } from '@/api/version.js';
const platform = uni.getSystemInfoSync().platform;
export default {
    getServerNo: (version, isPrompt = false, callback) => {
        let httpData = {
			appId: 'tickler',
            version: version.versionName
        };
        if (platform == "android") {
            httpData.type = 1101;
        } else {
            httpData.type = 1102;
        }
		versionApp(httpData).then(res=>{
			if(2000==res.code){
				if(res.data.isUpdate){
					callback && callback(res.data);
				}else if(isPrompt){
					uni.$u.toast("暂无新版本");
				}
			}
        });
    },
    appUpdateColor: "007aff",
    appUpdateIcon: ''
}
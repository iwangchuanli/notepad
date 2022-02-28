import JSEncrypt from '@/utils/encryption/jsencrypt/jsencrypt';
import CryptoJS from '@/utils/encryption/crypto-js/crypto-js';
const PRIVATE_KEY="MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAIoKG2+Wd42mHgwsdX57E99OmcDBClXOyBtVx+rUF8CqU90gRyLlVjSnQxFrkUyIIyUtNcc5ubIct6eYypyran6rYnPoPw/7bVn/jY3Ymx0GlM7GoasFuZO4LI03wlb/Vrj7OGQASpAgNYJ+BmhVCeE862gIWGBHjVm9nG/T3INnAgMBAAECgYEAgRtS7dtdli54jApJGjMjMwjg5VenwvnUTzy77vbQAHNOXNODtS+qoTSPlgt1qo7p0Cuo0N08wUn6u9Y8KqxovRD4bJH7tuGX7BeQb1HAWPiA0y+cxdnQLVWK7pVCSeH8s+53hKOruo176AfL5vDgbkpkuL4yFzYzNW7WNaG64wECQQC/t9LW74HLGyHfjXdSkvIhMahWXx7gbEjMT6BpidjZVLi6JZhihB22uYnnO6ZzQtJ+5BJYOUsvDkYnuLFkR76/AkEAuFLETo2Y5EgBQ+YhvaFZPAQlO97kH90zPfRLwrkyr+dQVCMWWEcFuAqdoIgjyqA8mszZ60gW3YPFulOWYCSNWQJBAKeOA9HffwNAkkkr/TYIwV7rZGEgPv8LBFCz4tF6LTA8EDp38wdeyg1ReMnD40RnCUrnD3VVlXTpt7yteTPYVEcCQQChZ3iXAM8dNyBEgMSS9TLapxajsq44aM5yNfYRcXK9LeTdiEwMyURswH7/D7enu2DWfQNmUB4Vo0oFuyQ21EshAkEAvEe4HOXxDW9lMZeUJSe8VLet1QCBZsWkeyX/eiqz4NkKh+iwThyLHQ+1yhG+4WtSw4ANF2pX9gH8xRcPulqRPg==";
const PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnC6hyC/bbrAQAd0R9c9RxTP2VNAe/y/PqufhyhrvYyTGx3h/eZ/29CBIlE2IqdWlIHDbtf1f3Uv9NeV19518gRl1X6fS3jDNQHhbB60OEVxyn5OFKLz0bq+/rSo84lfcaX3W2plBdv4xTl4xsOhUkDJbbFwQ/HpfYoFSgN/U+6wIDAQAB";
 
 
 //解密 数据
export const decryptData = (data) => {
	let aseKey= rsaDecrypt(PRIVATE_KEY,data.encryptedKey)
	return aesDecrypt(data.responseData,aseKey)
}
 
 //加密 数据
export const encryptData = (params) => {
	const key= initAesKey(16);
	const encrypted= rsaEncrypt(key,PUBLIC_KEY)
	const requestData= aesEncrypt(JSON.stringify(params),key)
	return {"requestData":requestData,"encryptedKey":encrypted}
} 
 
//解密  
export const aesDecrypt=(data,publicKey)=>{  
	var key  = CryptoJS.enc.Utf8.parse(publicKey);
	var decrypt = CryptoJS.AES.decrypt(data, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
	return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}

//加密
export const aesEncrypt=(word,publicKey)=>{ 
	var key  = CryptoJS.enc.Utf8.parse(publicKey);
	var srcs = CryptoJS.enc.Utf8.parse(word);
	var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
	return encrypted.toString();
}

/**
 * rsa加密（需要先加载jsencrypt.js文件）, 加密最大长度116位，足够应付这里的aes密匙加密传输
 */
export const rsaEncrypt = (word, publicKey) => {
	const encrypt = new JSEncrypt();
	encrypt.setPublicKey(publicKey);
	return encrypt.encrypt(word);
}

/**
 * rsa解密（需要先加载jsencrypt.js文件）, 加密最大长度116位，足够应付这里的aes密匙加密传输
 */
export const rsaDecrypt = (privateKey, data) => {
	const encrypt =new JSEncrypt();
	encrypt.setPrivateKey(privateKey);
	return encrypt.decrypt(data);
}

/**
 * 生成aes密匙
 * 生成规则：md5(当前时间戳 + 随机字符串)
 */
export const initAesKey = (len) => {
	len = len || 32;
	var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'; /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
	var maxPos = $chars.length;
	var pwd = '';
	for (let i = 0; i < len; i++) {
		pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
	}
	return pwd;
}

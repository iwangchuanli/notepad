import Vue from 'vue'
import App from './App'
import store from './store'

Vue.prototype.$store = store

App.mpType = 'app'

// main.js，注意要在use方法之后执行
import uView from '@/uni_modules/uview-ui'
Vue.use(uView)
Vue.config.productionTip = false

const app = new Vue({
    ...App
})
app.$mount()

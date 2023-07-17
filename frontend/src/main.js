import './assets/css/main.css'
import router from './router'
import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import request from "@/utils/request";
import {QuillEditor} from "@vueup/vue-quill";
import '@vueup/vue-quill/dist/vue-quill.snow.css'

const app = createApp(App)
app.use(router)
app.component('QuillEditor',QuillEditor)
app.use(ElementPlus,{size:"mini"})
app.config.globalProperties.$http = request
app.mount('#app')

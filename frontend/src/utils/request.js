import axios from 'axios'

// 初始化

export const request = axios.create({
    baseURL: 'http://175.178.76.218:9090',
    timeout: 5000
})

// 添加请求拦截器
request.interceptors.request.use(config => {
    // config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`
    return config
}, error => {
    return Promise.reject(error)
})

// 添加响应拦截器

request.interceptors.response.use(response => {
    let res = response.data
    // 返回文件
    if(response.config.responseType === 'blob'){
        return res
    }
    if(typeof res === 'string'){
        res = res ? JSON.parse(res) : res
    }
    return res
},error => {
    return Promise.reject(error)
})

export default request

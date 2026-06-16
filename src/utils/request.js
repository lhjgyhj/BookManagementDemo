import axios from 'axios'
import router from "@/router";
import Cookies from "js-cookie";

const request = axios.create({
    baseURL: "http://localhost:2222/api",
    timeout: 5000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(
    (config) => {
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
        const token = (Cookies.get('admin') ? JSON.parse(Cookies.get('admin')).token : '') ||
            (Cookies.get('user') ? JSON.parse(Cookies.get('user')).token : '');
        console.log("发送请求的token：", token);
        if (token) config.headers['token'] = token; // 有token就设置

        return config
    },
    (error) => {
        return Promise.reject(error)
    }
);

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    (response) => {
        let res/*res实际包含code,data,msg*/ = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res//如果返回结果是String，进行json解析
        }
        {
            if (res.code === '401'){
                if (router.currentRoute.path !== '/login') {//判断当前是否已在登录页，避免重复跳转
                router.push('/login');
            }
            }
        }
        return res;
    },
    error=>{
        console.log('err' + error)
        return Promise.reject(error)
    }
)

export default request;
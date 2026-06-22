import request from '@/utils/request'

export const captchaApi = () => request.get('/auth/captcha') as Promise<{ captchaId: string; image: string }>
export const loginApi = (data: { username: string; password: string; captchaId: string; captchaCode: string }) => request.post('/auth/login', data) as Promise<any>
export const userInfoApi = () => request.get('/auth/userinfo') as Promise<any>
export const logoutApi = () => request.post('/auth/logout')

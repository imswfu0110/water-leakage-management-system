import request from '@/utils/request'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  token: string
  userId: number
  username: string
  nickname: string
}

export function loginApi(data: LoginParams) {
  return request.post<LoginResult, LoginResult>('/auth/login', data)
}

export function pingAuthApi() {
  return request.get<string>('/auth/ping')
}
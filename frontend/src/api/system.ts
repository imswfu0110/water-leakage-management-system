import request from '@/utils/request'

export const getConfig = () => request.get('/system/config') as Promise<any>
export const updateConfig = (data: any) => request.put('/system/config', data)
export const uploadLogo = (file: File) => { const data = new FormData(); data.append('file', file); return request.post('/system/config/logo', data) as Promise<any> }

export const getMenuTree = () => request.get('/system/menu/tree') as Promise<any[]>
export const getUserMenus = () => request.get('/system/menu/user') as Promise<any[]>
export const createMenu = (data: any) => request.post('/system/menu', data)
export const updateMenu = (id: number, data: any) => request.put(`/system/menu/${id}`, data)
export const deleteMenu = (id: number) => request.delete(`/system/menu/${id}`)

export const getRolePage = (params: any) => request.get('/system/role/page', { params }) as Promise<any>
export const getRoleList = () => request.get('/system/role/list') as Promise<any[]>
export const createRole = (data: any) => request.post('/system/role', data)
export const updateRole = (id: number, data: any) => request.put(`/system/role/${id}`, data)
export const deleteRole = (id: number) => request.delete(`/system/role/${id}`)
export const getRoleMenus = (id: number) => request.get(`/system/role/${id}/menus`) as Promise<number[]>
export const saveRoleMenus = (id: number, menuIds: number[]) => request.put(`/system/role/${id}/menus`, { menuIds })

export const getUserPage = (params: any) => request.get('/system/user/page', { params }) as Promise<any>
export const createUser = (data: any) => request.post('/system/user', data)
export const updateUser = (id: number, data: any) => request.put(`/system/user/${id}`, data)
export const deleteUser = (id: number) => request.delete(`/system/user/${id}`)
export const resetPassword = (id: number, password: string) => request.put(`/system/user/${id}/password`, { password })

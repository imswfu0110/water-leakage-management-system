import request from '@/utils/request'
export const getOverview = () => request.get('/dashboard/overview') as Promise<any>
export const getLeakageTrend = () => request.get('/dashboard/leakage-trend') as Promise<any[]>
export const getAreaRanking = () => request.get('/dashboard/area-ranking') as Promise<any[]>

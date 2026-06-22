import { reactive } from 'vue'
import { getConfig, getUserMenus } from '@/api/system'
import { userInfoApi } from '@/api/auth'

export interface MenuItem { id: number; menuName: string; menuType: string; path?: string; icon?: string; children?: MenuItem[] }

export const appStore = reactive({
  systemName: '宁波东海供水管网漏损控制管理系统',
  systemLogo: '',
  user: null as any,
  menus: [] as MenuItem[],
  ready: false,
  async load() {
    const [config, user, menus] = await Promise.all([getConfig(), userInfoApi(), getUserMenus()])
    this.systemName = config.systemName || this.systemName
    this.systemLogo = config.systemLogo || ''
    this.user = user
    this.menus = menus.filter((item: MenuItem) => item.menuType !== 'BUTTON')
    this.ready = true
  },
  hasPath(path: string) {
    const walk = (items: MenuItem[]): boolean => items.some(i => i.path === path || walk(i.children || []))
    return path === '/dashboard' || walk(this.menus)
  }
})

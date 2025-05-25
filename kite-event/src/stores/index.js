// pinia的独立维护
import { createPinia } from 'pinia'
import persist from 'pinia-plugin-persistedstate'
// 创建pinia实例
const pinia = createPinia()
// 注册插件

pinia.use(persist)
// 导出
export default pinia

// 接受user模块的所有方法按需要导出
export * from './modules/user'

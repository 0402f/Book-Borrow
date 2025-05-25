import { defineConfig } from "vite";
import uni from "@dcloudio/vite-plugin-uni";
// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 3001,
  },
  plugins: [uni()],
  css: {
    preprocessorOptions: {
      scss: {
        // 配置忽略废弃警告
        quietDeps: true,
        // 可选：全局引入变量
        // additionalData: `@import "@/styles/variables.scss";`
      },
    },
  },
});

import {
	createSSRApp
} from "vue";
import App from "./App.vue";
import pinia from './store';
// 不再全局导入 wot-design-uni

export function createApp() {
	const app = createSSRApp(App);
	app.use(pinia);
	return {
		app,
	};
}

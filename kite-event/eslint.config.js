import js from '@eslint/js'
import pluginVue from 'eslint-plugin-vue'
import globals from 'globals'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'

export default [
  {
    'vue/multi-word-component-names': [
      'error',
      {
        ignores: ['Login', 'Home'] // 允许的单字组件名
      }
    ]
  },
  {
    name: 'app/files-to-lint',
    files: ['**/*.{js,mjs,jsx,vue}']
  },

  {
    name: 'app/files-to-ignore',
    ignores: ['**/dist/**', '**/dist-ssr/**', '**/coverage/**']
  },

  {
    languageOptions: {
      globals: {
        ...globals.browser
      }
    }
  },
  // 忽略特定文件的 ESLint 检查

  {
    rules: {
      // 💡 添加未定义变量错误提示
      'no-undef': 'error', // 启用未定义变量的错误提示
      camelcase: 'off', // 关闭驼峰命名规则
      'vue/multi-word-component-names': 'off', // 确保此规则已禁用

      // 💡 添加未使用变量的错误提示
      'no-unused-vars': [
        'error',
        {
          vars: 'all',
          args: 'after-used',
          ignoreRestSiblings: true
        }
      ] // 启用未使用变量的错误提示
    }
  },

  // 禁用与 Prettier 冲突的 ESLint 规则

  skipFormatting, // 确保这里没有拼写错误

  js.configs.recommended,
  ...pluginVue.configs['flat/essential']
]

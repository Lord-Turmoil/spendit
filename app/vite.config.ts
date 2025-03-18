import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vuetify from 'vite-plugin-vuetify';
import * as path from 'path';

// https://vite.dev/config/
export default defineConfig({
    root: '.',
    build: {
        outDir: './dist',
        minify: true,
        emptyOutDir: true
    },
    plugins: [vue(), vuetify({ autoImport: true })],
    resolve: {
        alias: [{ find: '~', replacement: path.resolve(__dirname, 'src') }]
    }
});

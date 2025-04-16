// Vuetify
import { createVuetify } from 'vuetify';
import 'vuetify/styles';
import '@mdi/font/css/materialdesignicons.css';
import { zhHans } from 'vuetify/locale';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

const vuetify = createVuetify({
    components,
    directives,
    locale: {
        locale: 'zhHans',
        messages: { zhHans }
    }
});

// Vue
import { createApp } from 'vue';
import { LONG_STALL, stall } from '~/utils/stall';
import App from './App.vue';
import router from '~/extensions/router';

function mountApp() {
    createApp(App).use(vuetify).use(router).mount('#app');
}

// Startup
import { engine } from '~/engine/engine';

function showErrorPage(error: Error) {
    const app = document.getElementById('app');
    document.body.removeChild(app);

    // get complete error frames
    const stack = error.stack;

    document.getElementById('error-message').innerHTML = error.message;
    document.getElementById('error-stack').innerHTML = stack;
    document.getElementById('error').style.display = 'block';
}

function hideSplash() {
    const splash = document.getElementById('splash');
    const app = document.getElementById('app');
    splash.style.display = 'none';
    if (app !== null) {
        app.style.display = 'block';
    }
}

async function startup() {
    // ensure engine is prepared before mounting the app
    if ((import.meta.env.VITE_FAST_BOOT || 'true') === 'true') {
        console.log('Fast boot enabled');
        await engine.init();
    } else {
        await stall(engine.init(), LONG_STALL);
    }
    mountApp();
}

window.onload = function () {
    startup()
        .catch((error) => {
            showErrorPage(error);
        })
        .finally(() => {
            hideSplash();
            window.onload = null;
        });
};

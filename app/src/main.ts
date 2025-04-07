import { createApp, resolveTransitionHooks } from 'vue';
import { zhHans } from 'vuetify/locale';
import { engine } from '~/engine/engine';

import App from './App.vue';

// Vuetify
import '@mdi/font/css/materialdesignicons.css';
import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

// custom components
import router from '~/extensions/router';

const vuetify = createVuetify({
    components,
    directives,
    locale: {
        locale: 'zhHans',
        messages: { zhHans }
    }
});

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
    await engine.init();
    createApp(App).use(vuetify).use(router).mount('#app');
}

async function launch() {
    await startup()
        .catch((err) => {
            showErrorPage(err);
        })
        .finally(() => {
            hideSplash();
        });
}

window.onload = function () {
    launch().then(() => {
        window.onload = null;
    });
};

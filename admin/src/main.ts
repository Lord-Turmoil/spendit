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
import App from './App.vue';
import router from '~/extensions/router';

createApp(App).use(vuetify).use(router).mount('#app');

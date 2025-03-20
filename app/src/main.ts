import { createApp } from 'vue';
import { zhHans } from 'vuetify/locale';

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

createApp(App).use(vuetify).use(router).mount('#app');

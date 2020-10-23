import Vue from 'vue'
import App from './App.vue'
import VueResource from 'vue-resource'

// Recursos globales
Vue.use(VueResource);

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')

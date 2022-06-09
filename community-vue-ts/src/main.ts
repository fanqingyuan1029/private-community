import { createApp } from "vue";
import {
  create,
  NAffix,
  NAlert,
  NButton,
  NCard,
  NCarousel,
  NConfigProvider,
  NDescriptions,
  NDescriptionsItem,
  NDivider,
  NGradientText,
  NIcon,
  NImage,
  NInput,
  NLayout,
  NLayoutContent,
  NLayoutFooter,
  NLayoutHeader,
  NMessageProvider,
  NModal,
  NNotificationProvider,
  NPopconfirm,
  NPopover,
  NRadio,
  NRadioGroup,
  NSpace,
  NSpin,
  NTable,
} from "naive-ui";
import router from "./router";
import App from "./App.vue";
import "normalize.css/normalize.css";
import { createPinia } from "pinia";

const naive = create({
  components: [
    NButton,
    NSpace,
    NLayout,
    NLayoutHeader,
    NLayoutContent,
    NLayoutFooter,
    NGradientText,
    NPopover,
    NAffix,
    NInput,
    NAlert,
    NCard,
    NPopconfirm,
    NConfigProvider,
    NSpin,
    NRadioGroup,
    NRadio,
    NIcon,
    NDivider,
    NMessageProvider,
    NCarousel,
    NTable,
    NModal,
    NDescriptions,
    NDescriptionsItem,
    NImage,
    NNotificationProvider
  ],
});
const pinia = createPinia();
const app = createApp(App);
app.use(pinia);
app.use(naive);
app.use(router);
app.mount("#app");

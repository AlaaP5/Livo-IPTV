import 'vuetify/styles'
import { createVuetify, type ThemeDefinition } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { aliases, mdi } from "vuetify/iconsets/mdi";
import "@mdi/font/css/materialdesignicons.css";


const customTheme: ThemeDefinition = {
  dark: false,
  colors: {
    primary: "#2b539e",
    secondary: "#8da0a1",
    accent: "#82B1FF",
    error: "#FF5252",
    info: "#2196F3",
    success: "#4CAF50",
    warning: "#FB8C00",
    background: "#F5F5F5",
    surface: "#FFFFFF",
    primaryLight: "#2E7D84",
    primaryDark: "#0D3A3E",
    secondaryLight: "#E8C766",
    secondaryDark: "#B8962E",
  },
};

export default createVuetify({
  components,
  directives,
  icons: {
    defaultSet: "mdi",
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    defaultTheme: "customTheme",
    themes: {
      customTheme,
    },
  },
  defaults: {
    VBtn: {
      rounded: "md",
      elevation: 0,
    },
    VCard: {
      rounded: "lg",
      elevation: 1,
    },
    global: {
      style: {
        fontFamily: 'Inter, system-ui, -apple-system, BlinkMacSystemFont, sans-serif',
      },
    },
    VTextField: {
      variant: "outlined",
      density: "comfortable",
    },
  },
});

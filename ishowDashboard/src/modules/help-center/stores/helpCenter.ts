import { defineStore } from "pinia";
import { ref } from "vue";

export const useHelpCenterStore = defineStore("helpCenter", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

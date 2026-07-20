import { defineStore } from "pinia";
import { ref } from "vue";

export const useAdStore = defineStore("ad", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

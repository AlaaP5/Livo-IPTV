import { defineStore } from "pinia";
import { ref } from "vue";

export const useChannelStore = defineStore("channel", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

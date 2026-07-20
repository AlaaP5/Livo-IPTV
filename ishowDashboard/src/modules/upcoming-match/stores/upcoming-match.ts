import { defineStore } from "pinia";
import { ref } from "vue";

export const useUpcomingMatchStore = defineStore("upcoming-match", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

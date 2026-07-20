import { defineStore } from "pinia";
import { ref } from "vue";

export const useTeamStore = defineStore("team", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

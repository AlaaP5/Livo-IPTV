import { defineStore } from "pinia";
import { ref } from "vue";

export const useComplaintStore = defineStore("complaint", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

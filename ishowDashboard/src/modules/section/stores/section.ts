import { defineStore } from "pinia";
import { ref } from "vue";

export const useSectionStore = defineStore("section", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

import { defineStore } from "pinia";
import { ref } from "vue";

export const useActorStore = defineStore("actor", () => {
  
  const loading = ref(false);

  function setLoading(value: boolean) {
    loading.value = value;
  }

  return {
    loading,
    setLoading
  };
});

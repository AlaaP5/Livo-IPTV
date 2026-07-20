<script setup lang="ts">
import { computed } from "vue";
import type { IActorResponse } from "../types/actor-response";
import { useActorStore } from "../stores/actor";
import actorService from "../services/actorService";



interface Props {
  modelValue: boolean;
  actorItem: IActorResponse | null;
}

const actorStore = useActorStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() =>
  (props.actorItem?.active) ? "0" : "1"
);

const actionLabel = computed(() =>
  (props.actorItem?.active) ? "Deactivate" : "Activate"
);

async function confirm() {
  if (!props.actorItem) return;

  await actorService.activateActor(props.actorItem.id, {
    active: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Actor`">

      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this actor?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="actorStore.loading" @click="confirm">
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

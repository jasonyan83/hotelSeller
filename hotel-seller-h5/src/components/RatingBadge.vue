<template>
  <span class="rating-badge" v-if="rating">{{ ratingText }}</span>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  rating: { type: Number, default: 0 },
})
const ratingText = computed(() => props.rating?.toFixed(2))
</script>

<style lang="scss" scoped>
.rating-badge {
  display: inline-block;
  background: $color-primary;
  color: $color-white;
  font-size: 11px;
  font-weight: 600;
  padding: 1px 5px;
  border-radius: $radius-sm;
  line-height: 1.4;
}
</style>

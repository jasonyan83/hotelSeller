<template>
  <div class="price-display" v-if="!degraded || displayMode !== 'NO_PRICE'">
    <span v-if="displayMode === 'APPROX_REF'" class="price-prefix">约</span>
    <span class="price-symbol">¥</span>
    <span class="price-value" :class="sizeClass">{{ formattedPrice }}</span>
    <span v-if="showSuffix" class="price-suffix">起</span>
    <span v-if="degraded" class="ref-tag">参考价</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  price: { type: [Number, String], default: 0 },
  size: { type: String, default: 'md' }, // sm, md, lg
  showSuffix: { type: Boolean, default: true },
  degraded: { type: Boolean, default: false },
  displayMode: { type: String, default: null }, // NORMAL_REF / APPROX_REF / NO_PRICE
})

const formattedPrice = computed(() => {
  if (!props.price) return '--'
  return Math.round(Number(props.price))
})

const sizeClass = computed(() => `price-value--${props.size}`)
</script>

<style lang="scss" scoped>
.price-display {
  display: inline-flex;
  align-items: baseline;
  gap: 1px;
}
.price-prefix {
  font-size: 12px;
  color: $color-primary;
}
.price-symbol {
  font-size: 12px;
  color: $color-primary;
  font-weight: 600;
}
.price-value {
  color: $color-primary;
  font-weight: 700;
  font-family: DINAlternate, -apple-system, sans-serif;
  &--sm { font-size: 12px; }
  &--md { font-size: 17px; }
  &--lg { font-size: 18px; }
}
.price-suffix {
  font-size: 12px;
  color: $color-primary;
  font-weight: 400;
}
.ref-tag {
  font-size: 10px;
  color: $color-gray-500;
  background: $color-gray-200;
  border-radius: 2px;
  padding: 1px 4px;
  margin-left: 4px;
  white-space: nowrap;
}
</style>

<template>
  <div class="refresh-price-btn-wrapper">
    <button class="refresh-price-btn" @click="$emit('refresh')" :disabled="loading">
      <van-loading v-if="loading" size="16" color="#D4513B" />
      <span v-else>查看实时价格</span>
    </button>
  </div>
</template>

<script setup>
defineProps({
  loading: { type: Boolean, default: false },
})
defineEmits(['refresh'])
</script>

<style lang="scss" scoped>
.refresh-price-btn-wrapper {
  display: flex;
  justify-content: center;
  padding: 24px 16px;
}
.refresh-price-btn {
  background: $color-white;
  border: 1px solid $color-primary;
  color: $color-primary;
  font-size: 14px;
  font-weight: 500;
  border-radius: 22px;
  padding: 10px 24px;
  min-width: 160px;
  cursor: pointer;
  transition: all $transition-fast;
  &:active {
    background: $color-primary-light;
  }
  &:disabled {
    opacity: 0.6;
  }
}
</style>

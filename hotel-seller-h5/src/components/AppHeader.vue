<template>
  <div class="app-header" :class="{ 'app-header--transparent': transparent }">
    <div class="app-header__left" @click="onBack">
      <van-icon name="arrow-left" size="20" />
    </div>
    <div class="app-header__title">
      <slot>{{ title }}</slot>
    </div>
    <div class="app-header__right">
      <slot name="right" />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const props = defineProps({
  title: { type: String, default: '' },
  transparent: { type: Boolean, default: false },
})
const emit = defineEmits(['back'])

function onBack() {
  emit('back')
  router.back()
}
</script>

<style lang="scss" scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  height: 44px;
  padding: 0 $space-lg;
  background: $color-white;
  @include hairline-bottom;

  &--transparent {
    background: transparent;
    &::after { display: none; }
  }

  &__left {
    width: 40px;
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  &__title {
    flex: 1;
    text-align: center;
    font-size: 16px;
    font-weight: 500;
    color: $color-gray-900;
    @include ellipsis;
  }
  &__right {
    width: 40px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 12px;
  }
}
</style>

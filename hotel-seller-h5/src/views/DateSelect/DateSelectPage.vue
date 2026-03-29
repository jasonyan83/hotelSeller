<template>
  <div class="date-page">
    <AppHeader title="选择日期" />
    <div class="date-hint">请选择入住和离店日期</div>
    <van-calendar
      v-model:show="show"
      type="range"
      :show-title="false"
      :poppable="false"
      :min-date="minDate"
      :max-date="maxDate"
      :style="{ height: '500px' }"
      color="#D4513B"
      @confirm="onConfirm"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useSearchStore } from '@/stores/searchStore'
import AppHeader from '@/components/AppHeader.vue'

const router = useRouter()
const searchStore = useSearchStore()
const show = ref(true)
const minDate = new Date()
const maxDate = new Date(Date.now() + 180 * 24 * 3600 * 1000)

function onConfirm(dates) {
  const [start, end] = dates
  searchStore.setDates(start, end)
  router.back()
}
</script>

<style lang="scss" scoped>
.date-page {
  min-height: 100vh;
  background: $color-white;
}
.date-hint {
  padding: $space-md $space-lg;
  font-size: 13px;
  color: $color-gray-500;
  text-align: center;
}
</style>

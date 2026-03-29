<template>
  <div class="dest-page">
    <AppHeader title="选择目的地" />
    <div class="search-box">
      <van-icon name="search" size="16" color="#999" />
      <input v-model="keyword" placeholder="搜索城市、区域" class="search-input" />
    </div>
    <div class="city-section">
      <div class="city-section__title">热门城市</div>
      <div class="city-grid">
        <div
          v-for="city in cities"
          :key="city.code"
          class="city-item"
          :class="{ 'city-item--active': searchStore.destination.code === city.code }"
          @click="selectCity(city)"
        >
          {{ city.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSearchStore } from '@/stores/searchStore'
import { hotCities } from '@/mock/data'
import AppHeader from '@/components/AppHeader.vue'

const router = useRouter()
const searchStore = useSearchStore()
const keyword = ref('')
const cities = ref(hotCities)

function selectCity(city) {
  searchStore.setDestination(city)
  router.back()
}
</script>

<style lang="scss" scoped>
.dest-page {
  min-height: 100vh;
  background: $color-white;
}
.search-box {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: $space-md $space-lg;
  padding: 8px 12px;
  background: $color-gray-100;
  border-radius: 20px;
}
.search-input {
  flex: 1;
  border: none;
  background: none;
  font-size: 14px;
  outline: none;
  &::placeholder { color: $color-gray-500; }
}
.city-section {
  padding: $space-lg;
  &__title {
    font-size: 14px;
    font-weight: 600;
    color: $color-gray-900;
    margin-bottom: $space-md;
  }
}
.city-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
.city-item {
  @include flex-center;
  height: 36px;
  background: $color-gray-100;
  border-radius: $radius-md;
  font-size: 13px;
  color: $color-gray-700;
  cursor: pointer;
  border: 1px solid transparent;
  &--active {
    color: $color-primary;
    background: $color-primary-light;
    border-color: $color-primary;
  }
}
</style>

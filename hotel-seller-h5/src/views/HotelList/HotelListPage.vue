<template>
  <div class="hotel-list-page">
    <!-- 顶栏 -->
    <div class="list-header">
      <van-icon name="arrow-left" size="20" @click="$router.back()" />
      <div class="list-header__dates">
        <span>住{{ searchStore.checkInDisplay }}</span>
        <span class="list-header__sep">-</span>
        <span>离{{ searchStore.checkOutDisplay }}</span>
      </div>
      <div class="list-header__search" @click="$router.push('/keyword-select')">
        <van-icon name="search" size="16" color="#999" />
        <span>商圈/酒店/地址</span>
      </div>
    </div>

    <!-- 筛选标签行 -->
    <div class="filter-chips hide-scrollbar">
      <span
        v-for="chip in filterChips"
        :key="chip.label"
        class="filter-chip"
        :class="{ 'filter-chip--active': chip.active, 'filter-chip--solid': chip.solid }"
        @click="chip.active = !chip.active"
      >
        {{ chip.label }}
      </span>
    </div>

    <!-- 酒店列表 -->
    <div class="hotel-list" v-if="hotelStore.hotelList.length">
      <div
        v-for="hotel in hotelStore.hotelList"
        :key="hotel.hotelId"
        class="hotel-card"
        @click="goDetail(hotel)"
      >
        <div class="hotel-card__thumb">
          <img :src="hotel.imageUrl" :alt="hotel.hotelName" />
          <RatingBadge v-if="hotel.rating >= 4.3" :rating="hotel.rating" class="hotel-card__badge" />
        </div>
        <div class="hotel-card__content">
          <div class="hotel-card__name">{{ hotel.hotelName }}</div>
          <div v-if="hotel.hotelNameEn" class="hotel-card__name-en">{{ hotel.hotelNameEn }}</div>
          <div class="hotel-card__location">{{ hotel.shortAddress }}</div>
          <div class="hotel-card__price-area" v-if="!hotel.degraded || hotel.displayMode !== 'NO_PRICE'">
            <div class="hotel-card__points" v-if="hotel.priceInfo.pointsPrice">
              <span class="points-val">{{ hotel.priceInfo.pointsPrice }}</span>
              <span class="points-unit"> 积分 × {{ searchStore.nightCount * 20 }}晚</span>
            </div>
            <PriceDisplay
              :price="hotel.priceInfo.startingPrice"
              size="sm"
              :degraded="hotel.degraded"
              :displayMode="hotel.displayMode"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 加载态 -->
    <div class="loading-more" v-if="hotelStore.isLoading">
      <van-loading size="20" color="#D4513B">加载中...</van-loading>
    </div>

    <!-- 热门地标 -->
    <div class="landmark-section" v-if="landmarks.length">
      <div class="landmark-title">── 热门地标 ──</div>
      <div class="landmark-tags hide-scrollbar">
        <span v-for="lm in landmarks" :key="lm" class="landmark-tag">{{ lm }}</span>
      </div>
    </div>

    <!-- 底部排序栏 -->
    <div class="sort-bar">
      <div
        v-for="item in sortItems"
        :key="item.value"
        class="sort-item"
        :class="{ 'sort-item--active': hotelStore.sortType === item.value }"
        @click="hotelStore.sortType = item.value"
      >
        <van-icon v-if="item.icon" :name="item.icon" size="16" />
        <span>{{ item.label }}</span>
      </div>
    </div>

    <!-- 酒店数量浮窗 -->
    <transition name="fade">
      <div v-if="showTotalToast" class="total-toast">
        {{ searchStore.destination.name }}共{{ hotelStore.totalCount }}家酒店
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSearchStore } from '@/stores/searchStore'
import { useHotelStore } from '@/stores/hotelStore'
import { mockAPI } from '@/mock/data'
import PriceDisplay from '@/components/PriceDisplay.vue'
import RatingBadge from '@/components/RatingBadge.vue'

const router = useRouter()
const searchStore = useSearchStore()
const hotelStore = useHotelStore()

const showTotalToast = ref(false)
const landmarks = ref([])

const filterChips = reactive([
  { label: '精选酒店', active: true, solid: true },
  { label: '迪士尼', active: false },
  { label: '4.7分以上', active: false },
  { label: '高端酒店', active: false },
  { label: '含早', active: false },
  { label: '免费取消', active: false },
])

const sortItems = [
  { label: '东航推荐', value: 'RECOMMEND', icon: '' },
  { label: '位置区域', value: 'DISTANCE', icon: '' },
  { label: '价格/评级', value: 'PRICE_ASC', icon: '' },
  { label: '筛选', value: 'FILTER', icon: 'filter-o' },
]

onMounted(async () => {
  await hotelStore.searchHotels({
    destinationCode: searchStore.destination.code,
    destinationName: searchStore.destination.name,
    checkInDate: searchStore.checkInDate,
    checkOutDate: searchStore.checkOutDate,
  })

  // 显示总数浮窗
  showTotalToast.value = true
  setTimeout(() => { showTotalToast.value = false }, 2000)

  // 加载热门地标
  const lmRes = await mockAPI.getHotLandmarks(searchStore.destination.code)
  landmarks.value = lmRes.data
})

function goDetail(hotel) {
  router.push(`/hotels/${hotel.hotelId}`)
}
</script>

<style lang="scss" scoped>
.hotel-list-page {
  min-height: 100vh;
  background: $color-white;
  padding-bottom: 56px;
}

// 顶栏
.list-header {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 44px;
  padding: 0 $space-lg;
  background: $color-white;
  position: sticky;
  top: 0;
  z-index: 100;
  @include hairline-bottom;

  &__dates {
    font-size: 12px;
    color: $color-gray-700;
    white-space: nowrap;
  }
  &__sep { margin: 0 2px; }
  &__search {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 4px;
    height: 30px;
    background: $color-gray-100;
    border-radius: 15px;
    padding: 0 10px;
    font-size: 13px;
    color: $color-gray-500;
  }
}

// 筛选chips
.filter-chips {
  display: flex;
  gap: 8px;
  padding: $space-sm $space-lg;
  overflow-x: auto;
  white-space: nowrap;
  background: $color-white;
}
.filter-chip {
  flex-shrink: 0;
  padding: 4px 10px;
  border-radius: $radius-chip;
  font-size: 12px;
  color: $color-gray-700;
  background: $color-gray-100;
  border: 1px solid transparent;
  cursor: pointer;
  transition: all $transition-fast;
  &--active {
    color: $color-primary;
    background: $color-primary-light;
    border-color: $color-primary;
  }
  &--solid.filter-chip--active {
    color: $color-white;
    background: $color-primary;
    border-color: $color-primary;
  }
}

// 酒店卡片
.hotel-card {
  display: flex;
  gap: 10px;
  padding: $space-md $space-lg;
  @include hairline-bottom($color-gray-200);
  cursor: pointer;
  transition: background $transition-fast;
  &:active { background: $color-gray-100; }

  &__thumb {
    position: relative;
    width: 110px;
    height: 80px;
    flex-shrink: 0;
    border-radius: $radius-md;
    overflow: hidden;
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  &__badge {
    position: absolute;
    right: 4px;
    top: 4px;
  }
  &__content {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  &__name {
    font-size: 15px;
    font-weight: 600;
    color: $color-gray-900;
    @include ellipsis;
  }
  &__name-en {
    font-size: 11px;
    color: $color-gray-500;
    @include ellipsis;
  }
  &__location {
    font-size: 11px;
    color: $color-gray-500;
    margin-top: 2px;
  }
  &__price-area {
    margin-top: auto;
    text-align: right;
  }
  &__points {
    font-size: 11px;
    .points-val {
      font-size: 14px;
      font-weight: 600;
      color: $color-gray-900;
    }
    .points-unit {
      color: $color-gray-500;
    }
  }
}

// 加载
.loading-more {
  @include flex-center;
  padding: $space-xl;
}

// 热门地标
.landmark-section {
  padding: $space-xl $space-lg;
  .landmark-title {
    text-align: center;
    font-size: 12px;
    color: $color-gray-500;
    margin-bottom: $space-md;
  }
}
.landmark-tags {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  white-space: nowrap;
}
.landmark-tag {
  flex-shrink: 0;
  padding: 6px 14px;
  background: $color-gray-100;
  border: 1px solid $color-gray-300;
  border-radius: $radius-chip;
  font-size: 12px;
  color: $color-gray-700;
}

// 底部排序栏
.sort-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 48px;
  background: $color-white;
  display: flex;
  align-items: center;
  justify-content: space-around;
  @include hairline-top;
  z-index: 100;
  @include safe-area-bottom;
}
.sort-item {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 13px;
  color: $color-gray-700;
  cursor: pointer;
  &--active {
    color: $color-primary;
    font-weight: 500;
  }
}

// 酒店数量浮窗
.total-toast {
  position: fixed;
  bottom: 60px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.7);
  color: $color-white;
  font-size: 13px;
  padding: 8px 20px;
  border-radius: 20px;
  z-index: 200;
  white-space: nowrap;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>

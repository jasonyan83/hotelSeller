<template>
  <div class="home-page">
    <!-- 顶栏 -->
    <div class="home-header">
      <van-icon name="arrow-left" size="20" color="#1A1A1A" />
      <span class="home-header__title">酒店频道</span>
      <van-icon name="wap-home-o" size="20" color="#1A1A1A" />
    </div>

    <!-- Banner -->
    <div class="banner-area">
      <div class="banner-content">
        <div class="banner-title">热门目的地酒店 ✈</div>
        <div class="banner-subtitle">精选好价</div>
      </div>
    </div>

    <!-- 搜索卡片 -->
    <div class="search-card">
      <div class="search-card__header">
        <span class="search-card__label">酒店</span>
        <span class="search-card__tag">可积分抵扣</span>
      </div>

      <!-- 目的地 -->
      <div class="search-row" @click="$router.push('/destination')">
        <span class="search-row__value">{{ searchStore.destination.name }}</span>
        <van-icon name="arrow" size="14" color="#999" />
      </div>

      <!-- 日期 -->
      <div class="search-row search-row--date" @click="$router.push('/date-select')">
        <div class="date-item">
          <span class="date-value">{{ searchStore.checkInDisplay }}</span>
          <span class="date-label">入住</span>
        </div>
        <span class="night-badge">共{{ searchStore.nightCount }}晚</span>
        <div class="date-item date-item--right">
          <span class="date-label">离店</span>
          <span class="date-value">{{ searchStore.checkOutDisplay }}</span>
        </div>
      </div>

      <!-- 入住信息 -->
      <div class="search-row">
        <span class="search-row__value search-row__value--sm">{{ searchStore.guestSummary }}</span>
        <span class="search-row__hint">价格/评级</span>
      </div>

      <!-- 关键字 -->
      <div class="search-row" @click="$router.push('/keyword-select')">
        <span class="search-row__placeholder">关键字/位置/品牌/等</span>
      </div>

      <!-- 查询按钮 -->
      <button class="search-btn" @click="handleSearch">查 询</button>
    </div>

    <!-- 供应商合作提示 -->
    <div class="partner-banner">
      <span><b>Booking.com</b></span>
      <span>侠客</span>
      <span class="dot">🟢</span>
      <span>更多选择...</span>
    </div>

    <!-- 城市 Tab -->
    <div class="city-tabs hide-scrollbar">
      <div
        v-for="city in cities"
        :key="city.code"
        class="city-tab"
        :class="{ 'city-tab--active': activeCity === city.code }"
        @click="switchCity(city)"
      >
        {{ city.name }}
      </div>
    </div>

    <!-- 酒店网格 -->
    <div class="hotel-grid">
      <div
        v-for="hotel in hotelStore.hotelList"
        :key="hotel.hotelId"
        class="hotel-grid-card"
        @click="goDetail(hotel)"
      >
        <div class="hotel-grid-card__img-wrap">
          <img :src="hotel.imageUrl" :alt="hotel.hotelName" class="hotel-grid-card__img" />
          <RatingBadge v-if="hotel.rating" :rating="hotel.rating" class="hotel-grid-card__rating" />
        </div>
        <div class="hotel-grid-card__info">
          <div class="hotel-grid-card__name">{{ hotel.hotelName }}</div>
          <StarRating :count="hotel.starCount" />
          <div class="hotel-grid-card__price" v-if="!hotel.degraded || hotel.displayMode !== 'NO_PRICE'">
            <PriceDisplay
              :price="hotel.priceInfo.startingPrice"
              size="sm"
              :degraded="hotel.degraded"
              :displayMode="hotel.displayMode"
            />
          </div>
          <div class="hotel-grid-card__points" v-if="hotel.priceInfo.pointsPrice && (!hotel.degraded || hotel.displayMode !== 'NO_PRICE')">
            <span class="points-price">
              <span class="yen">¥</span>20+积分 {{ hotel.priceInfo.pointsPrice }} 起
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部 TabBar -->
    <div class="bottom-tabbar">
      <div class="tab-item">
        <van-icon name="orders-o" size="20" />
        <span>我的订单</span>
      </div>
      <div class="tab-item">
        <van-icon name="clock-o" size="20" />
        <span>历史浏览</span>
      </div>
      <div class="tab-item">
        <van-icon name="like-o" size="20" />
        <span>我的收藏</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSearchStore } from '@/stores/searchStore'
import { useHotelStore } from '@/stores/hotelStore'
import { hotCities } from '@/mock/data'
import PriceDisplay from '@/components/PriceDisplay.vue'
import StarRating from '@/components/StarRating.vue'
import RatingBadge from '@/components/RatingBadge.vue'

const router = useRouter()
const searchStore = useSearchStore()
const hotelStore = useHotelStore()

const cities = ref(hotCities)
const activeCity = ref('SHA')

onMounted(() => {
  searchStore.restoreFromHistory()
  loadHotels()
})

async function loadHotels() {
  await hotelStore.searchHotels({
    destinationCode: activeCity.value,
    destinationName: searchStore.destination.name,
    checkInDate: searchStore.checkInDate,
    checkOutDate: searchStore.checkOutDate,
  })
}

function switchCity(city) {
  activeCity.value = city.code
  searchStore.setDestination(city)
  loadHotels()
}

function handleSearch() {
  searchStore.saveSearchState()
  router.push('/hotels')
}

function goDetail(hotel) {
  router.push(`/hotels/${hotel.hotelId}`)
}
</script>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  background: $color-bg;
  padding-bottom: 56px;
}

// 顶栏
.home-header {
  @include flex-between;
  height: 44px;
  padding: 0 $space-lg;
  background: $color-white;
  &__title {
    font-size: 15px;
    font-weight: 500;
    color: $color-gray-900;
  }
}

// Banner
.banner-area {
  background: linear-gradient(135deg, #F9EEEC 0%, #FCE8DC 100%);
  padding: 20px $space-lg 40px;
  .banner-content { }
  .banner-title {
    font-size: 18px;
    font-weight: 700;
    color: #B8412E;
  }
  .banner-subtitle {
    font-size: 14px;
    color: $color-orange;
    margin-top: 4px;
  }
}

// 搜索卡片
.search-card {
  background: $color-white;
  border-radius: $radius-xl;
  margin: -16px $space-lg 0;
  padding: $space-lg;
  position: relative;
  z-index: 10;
  box-shadow: $shadow-card;

  &__header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: $space-md;
  }
  &__label {
    font-size: 16px;
    font-weight: 600;
    color: $color-gray-900;
  }
  &__tag {
    font-size: 11px;
    color: $color-orange;
    background: $color-orange-light;
    padding: 2px 8px;
    border-radius: $radius-sm;
  }
}

.search-row {
  padding: $space-md 0;
  @include hairline-bottom($color-gray-200);
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;

  &--date {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  &__value {
    font-size: 16px;
    font-weight: 500;
    color: $color-gray-900;
    &--sm { font-size: 14px; }
  }
  &__hint {
    font-size: 14px;
    color: $color-gray-500;
  }
  &__placeholder {
    font-size: 14px;
    color: $color-gray-500;
  }
}

.date-item {
  display: flex;
  align-items: center;
  gap: 6px;
  &--right { flex-direction: row-reverse; }
}
.date-value {
  font-size: 15px;
  font-weight: 500;
  color: $color-gray-900;
}
.date-label {
  font-size: 12px;
  color: $color-gray-500;
}
.night-badge {
  font-size: 11px;
  color: $color-gray-700;
  background: $color-gray-200;
  padding: 2px 8px;
  border-radius: 10px;
}

.search-btn {
  @include btn-primary;
  width: 100%;
  margin-top: $space-lg;
  font-size: 16px;
}

// 供应商合作
.partner-banner {
  padding: $space-md $space-lg;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: $color-gray-500;
  b { color: $color-gray-700; }
  .dot { font-size: 8px; }
}

// 城市Tab
.city-tabs {
  display: flex;
  gap: 4px;
  padding: 0 $space-lg;
  overflow-x: auto;
  white-space: nowrap;
}
.city-tab {
  padding: 8px 14px;
  font-size: 14px;
  color: $color-gray-700;
  cursor: pointer;
  flex-shrink: 0;
  border-bottom: 2px solid transparent;
  transition: all $transition-fast;
  &--active {
    color: $color-primary;
    font-weight: 500;
    border-bottom-color: $color-primary;
  }
}

// 酒店网格
.hotel-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: $space-sm;
  padding: $space-md $space-lg;
}

.hotel-grid-card {
  background: $color-white;
  border-radius: $radius-lg;
  overflow: hidden;
  cursor: pointer;
  transition: transform $transition-fast;
  &:active { transform: scale(0.98); }

  &__img-wrap {
    position: relative;
    width: 100%;
    padding-top: 66.7%; // 3:2
    overflow: hidden;
  }
  &__img {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  &__rating {
    position: absolute;
    right: 6px;
    bottom: 6px;
  }
  &__info {
    padding: $space-sm;
  }
  &__name {
    font-size: 13px;
    font-weight: 500;
    color: $color-gray-900;
    @include ellipsis;
    margin-bottom: 2px;
  }
  &__price {
    margin-top: 4px;
  }
  &__points {
    margin-top: 2px;
    .points-price {
      font-size: 11px;
      color: $color-gray-500;
      .yen { color: $color-primary; font-size: 12px; }
    }
  }
}

// 底部TabBar
.bottom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: $color-white;
  display: flex;
  align-items: center;
  justify-content: space-around;
  @include hairline-top;
  z-index: 100;
  @include safe-area-bottom;

  .tab-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 2px;
    font-size: 10px;
    color: $color-gray-500;
    cursor: pointer;
  }
}
</style>

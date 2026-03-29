<template>
  <div class="hotel-detail-page" v-if="hotel">
    <!-- Hero 图片 -->
    <div class="hero-image">
      <img :src="hotel.heroImage" alt="" class="hero-img" />
      <div class="hero-nav">
        <div class="hero-btn" @click="$router.back()">
          <van-icon name="arrow-left" size="20" color="#fff" />
        </div>
        <div class="hero-right">
          <div class="hero-btn"><van-icon name="like-o" size="20" color="#fff" /></div>
          <div class="hero-btn"><van-icon name="share-o" size="20" color="#fff" /></div>
        </div>
      </div>
      <div class="hero-count">{{ hotel.imageCount }}张</div>
    </div>

    <!-- 酒店信息 -->
    <div class="hotel-info-section">
      <div class="hotel-name">{{ hotel.hotelName }}</div>
      <StarRating :count="hotel.starCount" />
      <div class="hotel-meta">
        <span class="hotel-meta__text">{{ hotel.openYear }}年开业，{{ hotel.renovateYear }}年装修</span>
        <span class="hotel-meta__link">详情/设施 ></span>
      </div>
    </div>

    <!-- 地址 -->
    <div class="address-section">
      <div class="address-text">{{ hotel.fullAddress }}</div>
      <div class="address-map">
        <van-icon name="location-o" size="14" color="#D4513B" />
        <span>地图</span>
      </div>
    </div>

    <!-- 内宾提示 -->
    <div class="guest-notice">
      <span>部分价格仅适用于持中国大陆身份证的居民!</span>
    </div>

    <!-- 降级提示条 -->
    <DegradedNoticeBar v-if="hotel.degraded && hotel.displayMode !== 'NO_PRICE'" />

    <div class="section-gap" />

    <!-- 入住信息栏 -->
    <div class="checkin-bar">
      <div class="checkin-item">
        <div class="checkin-label">入住日期</div>
        <div class="checkin-date">{{ searchStore.checkInDisplay }}</div>
        <div class="checkin-weekday">{{ searchStore.checkInWeekday }}</div>
      </div>
      <div class="checkin-night">
        <span class="night-badge">共{{ searchStore.nightCount }}晚</span>
      </div>
      <div class="checkin-item checkin-item--right">
        <div class="checkin-label">离店日期</div>
        <div class="checkin-date">{{ searchStore.checkOutDisplay }}</div>
        <div class="checkin-weekday">{{ searchStore.checkOutWeekday }}</div>
      </div>
      <div class="checkin-guest">{{ searchStore.adultCount }}人/{{ searchStore.roomCount }}间</div>
    </div>

    <div class="section-gap" />

    <!-- 房型区域 -->
    <div class="room-section" v-if="!hotel.degraded || hotel.displayMode !== 'NO_PRICE'">
      <div
        v-for="room in hotelStore.displayRooms"
        :key="room.physicalRoomId"
        class="room-group"
      >
        <!-- 物理房型卡片 -->
        <div class="physical-room" @click="hotelStore.toggleRoom(room.physicalRoomId)">
          <div class="physical-room__thumb">
            <img :src="room.roomImages?.[0] || hotel.imageUrl" alt="" />
          </div>
          <div class="physical-room__info">
            <div class="physical-room__name">{{ room.roomName }}</div>
            <div class="physical-room__meta">
              {{ room.area }}m² · {{ room.bedType }}
            </div>
          </div>
          <div class="physical-room__right">
            <PriceDisplay
              :price="room.startingPrice"
              size="md"
              :degraded="hotel.degraded"
              :displayMode="hotel.displayMode"
            />
            <van-icon
              :name="hotelStore.expandedRoomId === room.physicalRoomId ? 'arrow-up' : 'arrow-down'"
              size="16" color="#999"
            />
          </div>
        </div>

        <!-- 展开的产品房型 -->
        <transition name="slide">
          <div v-if="hotelStore.expandedRoomId === room.physicalRoomId" class="sell-room-list">
            <div
              v-for="sell in room.sellRooms"
              :key="sell.sellRoomId"
              class="sell-room-item"
            >
              <div class="sell-room__header">
                <span class="sell-room__name">{{ sell.sellRoomName }}</span>
                <span v-if="sell.supplierTag" class="sell-room__supplier-tag">{{ sell.supplierTag }}</span>
              </div>
              <div class="sell-room__tags">
                <span
                  v-for="tag in sell.tags"
                  :key="tag"
                  class="feature-tag"
                  :class="tag === '酒店优享' ? 'feature-tag--green' : 'feature-tag--orange'"
                >{{ tag }}</span>
              </div>
              <div class="sell-room__detail">
                <span class="detail-text">
                  · {{ sell.mealInfo?.summary || '无早' }}
                  <template v-if="sell.cancelPolicy">
                    · {{ sell.cancelPolicy.label }}
                    <template v-if="sell.cancelPolicy.description">
                      ({{ sell.cancelPolicy.description.slice(0, 20) }})
                    </template>
                  </template>
                </span>
              </div>
              <div class="sell-room__footer">
                <div class="sell-room__price-area">
                  <van-icon name="info-o" size="13" color="#999" />
                  <PriceDisplay
                    :price="sell.priceDetail.averagePrice"
                    size="lg"
                    :showSuffix="false"
                    :degraded="hotel.degraded"
                    :displayMode="hotel.displayMode"
                  />
                </div>
                <button class="sell-room__book-btn" @click.stop="handleBook(room, sell)">
                  {{ hotel.degraded ? '查看实时价格' : '预订' }}
                </button>
              </div>
            </div>
          </div>
        </transition>
      </div>

      <!-- 查看更多房型 -->
      <div v-if="hotelStore.hasMoreRooms" class="more-rooms" @click="hotelStore.showAllRoomTypes()">
        查看其他{{ hotelStore.hiddenRoomCount }}个房型
      </div>
    </div>

    <!-- NO_PRICE 模式 -->
    <div class="room-section" v-else>
      <RefreshPriceButton :loading="refreshing" @refresh="handleRefreshPrice" />
    </div>
  </div>

  <!-- 加载态 -->
  <div v-else class="loading-page">
    <van-loading size="32" color="#D4513B" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useSearchStore } from '@/stores/searchStore'
import { useHotelStore } from '@/stores/hotelStore'
import { useBookingStore } from '@/stores/bookingStore'
import PriceDisplay from '@/components/PriceDisplay.vue'
import StarRating from '@/components/StarRating.vue'
import DegradedNoticeBar from '@/components/degraded/DegradedNoticeBar.vue'
import RefreshPriceButton from '@/components/degraded/RefreshPriceButton.vue'

const route = useRoute()
const router = useRouter()
const searchStore = useSearchStore()
const hotelStore = useHotelStore()
const bookingStore = useBookingStore()

const hotel = ref(null)
const refreshing = ref(false)

onMounted(async () => {
  await hotelStore.fetchHotelDetail(route.params.hotelId)
  hotel.value = hotelStore.currentHotel
})

function handleBook(room, sellRoom) {
  bookingStore.setBookingInfo(hotel.value, room, sellRoom)
  router.push('/booking')
}

async function handleRefreshPrice() {
  refreshing.value = true
  try {
    await hotelStore.fetchHotelDetail(route.params.hotelId)
    hotel.value = hotelStore.currentHotel
  } finally {
    refreshing.value = false
  }
}
</script>

<style lang="scss" scoped>
.hotel-detail-page {
  min-height: 100vh;
  background: $color-bg;
  padding-bottom: 20px;
}

.loading-page {
  @include flex-center;
  min-height: 100vh;
}

// Hero 图片
.hero-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  .hero-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}
.hero-nav {
  position: absolute;
  top: 10px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  padding: 0 $space-lg;
}
.hero-right {
  display: flex;
  gap: 8px;
}
.hero-btn {
  width: 32px;
  height: 32px;
  @include flex-center;
  background: rgba(0, 0, 0, 0.3);
  border-radius: $radius-circle;
  cursor: pointer;
}
.hero-count {
  position: absolute;
  right: 10px;
  bottom: 10px;
  background: rgba(0, 0, 0, 0.5);
  color: $color-white;
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
}

// 酒店信息
.hotel-info-section {
  background: $color-white;
  padding: $space-lg;
}
.hotel-name {
  font-size: 18px;
  font-weight: 700;
  color: $color-gray-900;
  margin-bottom: 4px;
}
.hotel-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 6px;
  &__text {
    font-size: 13px;
    color: $color-gray-500;
  }
  &__link {
    font-size: 13px;
    color: $color-primary;
    cursor: pointer;
  }
}

// 地址
.address-section {
  background: $color-white;
  padding: 0 $space-lg $space-lg;
  display: flex;
  justify-content: space-between;
  align-items: center;
  @include hairline-top($color-gray-200);
  padding-top: $space-md;
}
.address-text {
  font-size: 13px;
  font-weight: 500;
  color: $color-gray-900;
  flex: 1;
  @include ellipsis;
}
.address-map {
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: 12px;
  color: $color-primary;
  cursor: pointer;
  flex-shrink: 0;
  margin-left: 12px;
}

// 内宾提示
.guest-notice {
  margin: 8px 12px;
  padding: 10px 14px;
  background: $color-primary-light;
  border: 1px solid rgba($color-primary, 0.3);
  border-radius: $radius-lg;
  font-size: 12px;
  color: $color-primary;
  text-align: center;
}

.section-gap {
  height: 8px;
  background: $color-bg;
}

// 入住信息栏
.checkin-bar {
  background: $color-white;
  padding: $space-lg;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
}
.checkin-item {
  text-align: center;
  &--right { text-align: center; }
}
.checkin-label {
  font-size: 12px;
  color: $color-gray-500;
}
.checkin-date {
  font-size: 20px;
  font-weight: 700;
  color: $color-primary;
  margin: 2px 0;
}
.checkin-weekday {
  font-size: 11px;
  color: $color-gray-500;
}
.checkin-night {
  .night-badge {
    font-size: 11px;
    color: $color-gray-700;
    background: $color-gray-200;
    padding: 3px 10px;
    border-radius: 12px;
  }
}
.checkin-guest {
  position: absolute;
  right: $space-lg;
  bottom: $space-lg;
  font-size: 16px;
  font-weight: 500;
  color: $color-gray-900;
}

// 房型区域
.room-section {
  background: $color-white;
}

.physical-room {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: $space-md $space-lg;
  cursor: pointer;
  @include hairline-bottom($color-gray-200);

  &__thumb {
    width: 90px;
    height: 65px;
    border-radius: $radius-md;
    overflow: hidden;
    flex-shrink: 0;
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  &__info {
    flex: 1;
    min-width: 0;
  }
  &__name {
    font-size: 15px;
    font-weight: 600;
    color: $color-gray-900;
  }
  &__meta {
    font-size: 12px;
    color: $color-gray-500;
    margin-top: 4px;
  }
  &__right {
    text-align: right;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 4px;
  }
}

// 产品房型
.sell-room-list {
  padding-left: 116px;
}
.sell-room-item {
  padding: $space-md $space-lg $space-md 0;
  @include hairline-bottom($color-gray-200);
}
.sell-room__header {
  display: flex;
  align-items: center;
  gap: 6px;
}
.sell-room__name {
  font-size: 14px;
  font-weight: 500;
  color: $color-gray-900;
}
.sell-room__supplier-tag {
  font-size: 10px;
  color: $color-primary;
  background: $color-primary-light;
  padding: 1px 4px;
  border-radius: 2px;
}
.sell-room__tags {
  display: flex;
  gap: 4px;
  margin-top: 4px;
  flex-wrap: wrap;
}
.feature-tag {
  font-size: 10px;
  padding: 1px 6px;
  border-radius: 2px;
  &--green {
    background: $color-green-light;
    color: $color-green;
  }
  &--orange {
    background: $color-orange-light;
    color: $color-orange;
  }
}
.sell-room__detail {
  margin-top: 4px;
  .detail-text {
    font-size: 12px;
    color: $color-gray-500;
  }
}
.sell-room__footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}
.sell-room__price-area {
  display: flex;
  align-items: center;
  gap: 2px;
}
.sell-room__book-btn {
  background: $color-primary;
  color: $color-white;
  border: none;
  border-radius: 16px;
  padding: 6px 20px;
  font-size: 13px;
  cursor: pointer;
  white-space: nowrap;
  &:active {
    background: $color-primary-dark;
  }
}

.more-rooms {
  padding: $space-lg;
  text-align: center;
  font-size: 14px;
  color: $color-primary;
  cursor: pointer;
}

// 展开动画
.slide-enter-active, .slide-leave-active {
  transition: all 0.3s ease;
  overflow: hidden;
}
.slide-enter-from, .slide-leave-to {
  opacity: 0;
  max-height: 0;
}
.slide-enter-to, .slide-leave-from {
  opacity: 1;
  max-height: 1000px;
}
</style>

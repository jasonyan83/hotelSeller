<template>
  <div class="booking-page">
    <!-- 顶栏 -->
    <AppHeader title="信息填写" />

    <!-- 变价提示 -->
    <div v-if="bookingStore.priceChanged" class="price-change-notice">
      <van-icon name="info-o" size="14" />
      <span>{{ bookingStore.priceChangeMessage }}</span>
    </div>

    <!-- 行程信息卡片 -->
    <div class="trip-card">
      <div class="trip-dates">
        <div class="trip-date-item">
          <span class="trip-date-val">{{ searchStore.checkInDisplay }}</span>
          <span class="trip-date-label">入住</span>
        </div>
        <div class="trip-night">共{{ searchStore.nightCount }}晚</div>
        <div class="trip-date-item">
          <span class="trip-date-val">{{ searchStore.checkOutDisplay }}</span>
          <span class="trip-date-label">离店</span>
        </div>
      </div>
      <div class="trip-hotel">{{ hotel?.hotelName }}</div>
      <div class="trip-room">{{ sellRoom?.sellRoomName }}</div>
      <div class="trip-policy" v-if="sellRoom?.cancelPolicy">
        <span>{{ sellRoom.cancelPolicy.label }}</span>
        <template v-if="sellRoom.mealInfo">
          <span class="trip-sep">|</span>
          <span>{{ sellRoom.mealInfo.summary }}</span>
        </template>
      </div>
      <div class="trip-notice">
        <span class="trip-notice__label">订房须知:</span>
        <span class="trip-notice__link">查看详情 ></span>
      </div>
      <div class="trip-notice__list">
        <div class="notice-item">· 请注意，您订的订单从{{ searchStore.checkInDisplay }}开始</div>
        <div class="notice-item">· {{ sellRoom?.cancelPolicy?.description || '预订后不可取消' }}</div>
        <div class="notice-item">· 如订单在支付后无法确认，将全额退款</div>
      </div>
    </div>

    <div class="section-gap" />

    <!-- 入住人 -->
    <div class="section">
      <div class="section-header">
        <span class="section-title">选择入住人</span>
        <span class="section-hint">为确保入住，请核对信息</span>
      </div>
      <div class="guest-info">
        <span>房间数 {{ searchStore.roomCount }}间房 每间最多住2人</span>
        <van-icon name="contact" size="18" color="#D4513B" />
      </div>

      <div v-for="(guest, idx) in bookingStore.guests" :key="idx" class="guest-group">
        <div class="guest-label">
          入住人{{ idx + 1 }}
          <van-icon
            v-if="idx > 0"
            name="cross" size="14" color="#999"
            @click="bookingStore.removeGuest(idx)"
          />
        </div>
        <div class="guest-inputs">
          <input v-model="guest.lastName" placeholder="入住人(姓)" class="guest-input" />
          <input v-model="guest.firstName" placeholder="入住人(名)" class="guest-input" />
          <input v-model="guest.lastNamePinyin" placeholder="入住人(姓拼音)" class="guest-input" />
          <input v-model="guest.firstNamePinyin" placeholder="入住人(名拼音)" class="guest-input" />
        </div>
      </div>
      <div class="add-guest" @click="bookingStore.addGuest()">
        <van-icon name="plus" size="14" /> 添加入住人
      </div>
    </div>

    <div class="section-gap" />

    <!-- 更多需求 -->
    <div class="section">
      <div class="section-header" @click="showPreference = !showPreference">
        <span class="section-title">更多需求</span>
        <van-icon :name="showPreference ? 'arrow-up' : 'arrow-down'" size="16" color="#999" />
      </div>
      <div v-if="showPreference" class="preference-tags">
        <span
          v-for="tag in preferenceOptions"
          :key="tag"
          class="pref-tag"
          :class="{ 'pref-tag--active': bookingStore.preferences.includes(tag) }"
          @click="togglePreference(tag)"
        >{{ tag }}</span>
      </div>
    </div>

    <div class="section-gap" />

    <!-- 备注 -->
    <div class="section">
      <div class="section-title">备注</div>
      <textarea
        v-model="bookingStore.remark"
        class="remark-input"
        placeholder="您的需求我们会传达给酒店，但不保证满足"
        rows="3"
      />
    </div>

    <div class="section-gap" />

    <!-- 联系人 -->
    <div class="section">
      <div class="contact-row">
        <label>联系人</label>
        <div class="contact-inputs">
          <input v-model="bookingStore.contactLastName" placeholder="联系人(姓)" />
          <input v-model="bookingStore.contactFirstName" placeholder="联系人(名)" />
        </div>
      </div>
      <div class="contact-row">
        <label>手机</label>
        <div class="contact-inputs">
          <span class="phone-prefix">{{ bookingStore.phonePrefix }}</span>
          <input v-model="bookingStore.phone" placeholder="请输入手机号" type="tel" />
        </div>
      </div>
      <div class="contact-row">
        <label>邮箱</label>
        <input v-model="bookingStore.email" placeholder="请输入联系人邮箱" class="full-input" />
      </div>
    </div>

    <div class="section-gap" />

    <!-- 优惠 -->
    <div class="section">
      <div class="promo-row">
        <span>优惠券</span>
        <span class="promo-hint">暂无可用优惠券 ></span>
      </div>
      <div class="promo-row">
        <span>酒店红包</span>
        <span class="promo-hint">暂无可用酒店红包 ></span>
      </div>
      <div class="promo-row">
        <div>
          <span>积分抵扣</span>
          <span class="promo-sub">(账户剩余0积分)</span>
        </div>
        <van-switch v-model="bookingStore.usePoints" size="20" active-color="#D4513B" />
      </div>
    </div>

    <div class="section-gap" />

    <!-- 出行类型 -->
    <div class="section">
      <div class="trip-type-row">
        <span class="section-title">出行类型</span>
        <div class="trip-types">
          <span
            class="trip-type-btn"
            :class="{ 'trip-type-btn--active': bookingStore.tripType === '休闲游' }"
            @click="bookingStore.tripType = '休闲游'"
          >休闲游</span>
          <span
            class="trip-type-btn"
            :class="{ 'trip-type-btn--active': bookingStore.tripType === '差旅' }"
            @click="bookingStore.tripType = '差旅'"
          >差旅</span>
        </div>
      </div>
    </div>

    <!-- 协议 -->
    <div class="agreement-section">
      <div class="points-notice">订酒店不再进行消费返积分</div>
      <label class="agreement-check">
        <input type="checkbox" v-model="bookingStore.agreedToTerms" />
        <span>已阅读并同意<a>酒店预订须知</a></span>
      </label>
    </div>

    <!-- 底部支付栏 -->
    <div class="booking-footer">
      <div class="footer-price">
        <span class="footer-label">实付:</span>
        <span class="footer-symbol">¥</span>
        <span class="footer-amount">{{ bookingStore.totalPrice }}</span>
        <span class="footer-detail">明细 ></span>
      </div>
      <button
        class="pay-btn"
        :disabled="!bookingStore.agreedToTerms"
        @click="handlePay"
      >
        立即支付
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useSearchStore } from '@/stores/searchStore'
import { useBookingStore } from '@/stores/bookingStore'
import AppHeader from '@/components/AppHeader.vue'
import { showToast } from 'vant'

const searchStore = useSearchStore()
const bookingStore = useBookingStore()

const showPreference = ref(false)
const preferenceOptions = ['安静', '无烟', '高楼层', '远离电梯', '加床']

const hotel = computed(() => bookingStore.selectedHotel)
const sellRoom = computed(() => bookingStore.selectedSellRoom)

onMounted(async () => {
  await bookingStore.verifyPrice()
})

function togglePreference(tag) {
  const idx = bookingStore.preferences.indexOf(tag)
  if (idx >= 0) bookingStore.preferences.splice(idx, 1)
  else bookingStore.preferences.push(tag)
}

function handlePay() {
  if (!bookingStore.agreedToTerms) {
    showToast('请先阅读并同意预订须知')
    return
  }
  showToast('预订功能开发中...')
}
</script>

<style lang="scss" scoped>
.booking-page {
  min-height: 100vh;
  background: $color-bg;
  padding-bottom: 70px;
}

.section-gap {
  height: 8px;
  background: $color-bg;
}

.section {
  background: $color-white;
  padding: $space-lg;
}
.section-header {
  @include flex-between;
  margin-bottom: $space-md;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: $color-gray-900;
}
.section-hint {
  font-size: 12px;
  color: $color-gray-500;
}

// 变价提示
.price-change-notice {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px $space-lg;
  background: #FFF3CD;
  font-size: 12px;
  color: #856404;
}

// 行程卡片
.trip-card {
  margin: 0 $space-lg;
  margin-top: $space-md;
  padding: 14px;
  border: 1px solid $color-gray-300;
  border-radius: $radius-lg;
}
.trip-dates {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: $space-md;
}
.trip-date-item {
  text-align: center;
}
.trip-date-val {
  font-size: 16px;
  font-weight: 600;
  color: $color-gray-900;
}
.trip-date-label {
  font-size: 12px;
  color: $color-gray-500;
  display: block;
}
.trip-night {
  font-size: 12px;
  color: $color-gray-700;
  background: $color-gray-200;
  padding: 2px 8px;
  border-radius: 10px;
}
.trip-hotel {
  font-size: 15px;
  font-weight: 600;
  color: $color-primary;
  margin-bottom: 4px;
}
.trip-room {
  font-size: 13px;
  color: $color-gray-700;
  margin-bottom: 6px;
}
.trip-policy {
  font-size: 12px;
  color: $color-gray-900;
  margin-bottom: 8px;
  .trip-sep {
    margin: 0 6px;
    color: $color-gray-300;
  }
}
.trip-notice {
  @include flex-between;
  margin-top: 8px;
  &__label {
    font-size: 13px;
    color: $color-gray-700;
  }
  &__link {
    font-size: 13px;
    color: $color-primary;
    cursor: pointer;
  }
  &__list {
    margin-top: 6px;
  }
}
.notice-item {
  font-size: 12px;
  color: $color-gray-700;
  line-height: 1.8;
  padding-left: 4px;
}

// 入住人
.guest-info {
  @include flex-between;
  font-size: 13px;
  color: $color-gray-700;
  margin-bottom: $space-md;
}
.guest-group {
  margin-bottom: $space-md;
}
.guest-label {
  font-size: 13px;
  color: $color-gray-700;
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.guest-inputs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}
.guest-input {
  width: 100%;
  height: 36px;
  border: 1px solid $color-gray-300;
  border-radius: $radius-md;
  padding: 0 10px;
  font-size: 13px;
  color: $color-gray-900;
  outline: none;
  &::placeholder { color: $color-gray-500; }
  &:focus { border-color: $color-primary; }
}
.add-guest {
  @include flex-center;
  gap: 4px;
  font-size: 14px;
  color: $color-primary;
  padding: $space-md 0 0;
  cursor: pointer;
}

// 偏好标签
.preference-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.pref-tag {
  padding: 6px 14px;
  border: 1px solid $color-gray-300;
  border-radius: 16px;
  font-size: 13px;
  color: $color-gray-700;
  cursor: pointer;
  transition: all $transition-fast;
  &--active {
    border-color: $color-primary;
    color: $color-primary;
    background: $color-primary-light;
  }
}

// 备注
.remark-input {
  width: 100%;
  border: 1px solid $color-gray-300;
  border-radius: $radius-md;
  padding: 10px;
  font-size: 12px;
  color: $color-gray-900;
  resize: none;
  outline: none;
  margin-top: 8px;
  &::placeholder { color: $color-gray-500; }
  &:focus { border-color: $color-primary; }
}

// 联系人
.contact-row {
  display: flex;
  align-items: center;
  padding: $space-sm 0;
  @include hairline-bottom($color-gray-200);
  label {
    width: 56px;
    font-size: 14px;
    color: $color-gray-900;
    flex-shrink: 0;
  }
  .contact-inputs {
    flex: 1;
    display: flex;
    gap: 8px;
    input {
      flex: 1;
      height: 36px;
      border: none;
      font-size: 14px;
      color: $color-gray-900;
      outline: none;
      &::placeholder { color: $color-gray-500; }
    }
  }
  .phone-prefix {
    font-size: 14px;
    color: $color-gray-900;
    line-height: 36px;
  }
  .full-input {
    flex: 1;
    height: 36px;
    border: none;
    font-size: 14px;
    color: $color-gray-900;
    outline: none;
    &::placeholder { color: $color-gray-500; }
  }
}

// 优惠
.promo-row {
  @include flex-between;
  padding: $space-md 0;
  @include hairline-bottom($color-gray-200);
  font-size: 14px;
  color: $color-gray-900;
  &:last-child::after { display: none; }
}
.promo-hint {
  font-size: 13px;
  color: $color-gray-500;
}
.promo-sub {
  font-size: 12px;
  color: $color-gray-500;
}

// 出行类型
.trip-type-row {
  @include flex-between;
}
.trip-types {
  display: flex;
  gap: 8px;
}
.trip-type-btn {
  padding: 6px 16px;
  border: 1px solid $color-gray-300;
  border-radius: $radius-chip;
  font-size: 13px;
  color: $color-gray-700;
  cursor: pointer;
  &--active {
    border-color: $color-primary;
    color: $color-primary;
    background: $color-primary-light;
  }
}

// 协议
.agreement-section {
  padding: $space-lg;
}
.points-notice {
  font-size: 12px;
  color: $color-primary;
  margin-bottom: 8px;
}
.agreement-check {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: $color-gray-700;
  cursor: pointer;
  input { accent-color: $color-primary; }
  a { color: $color-primary; }
}

// 底部支付栏
.booking-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: $color-white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 $space-lg;
  @include hairline-top;
  @include safe-area-bottom;
  z-index: 100;
}
.footer-price {
  display: flex;
  align-items: baseline;
  gap: 2px;
}
.footer-label {
  font-size: 12px;
  color: $color-gray-500;
}
.footer-symbol {
  font-size: 14px;
  color: $color-primary;
  font-weight: 600;
}
.footer-amount {
  font-size: 18px;
  font-weight: 700;
  color: $color-primary;
  font-family: DINAlternate, sans-serif;
}
.footer-detail {
  font-size: 12px;
  color: $color-gray-500;
  margin-left: 8px;
  cursor: pointer;
}
.pay-btn {
  @include btn-primary;
  padding: 10px 28px;
  font-size: 15px;
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}
</style>

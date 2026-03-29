<template>
  <div class="degraded-notice-bar">
    <van-icon name="info-o" size="14" />
    <span>当前价格为参考价，实际以预订时为准</span>
  </div>
</template>

<style lang="scss" scoped>
.degraded-notice-bar {
  display: flex;
  align-items: center;
  gap: 6px;
  background: #FFF7E6;
  border: 1px solid #FFE0B2;
  color: $color-orange;
  font-size: 12px;
  padding: 8px 14px;
  border-radius: $radius-lg;
  margin: 8px 12px;
  text-align: center;
  justify-content: center;
}
</style>

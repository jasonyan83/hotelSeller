<template>
  <span class="promotion-tag" :class="typeClass">{{ tag }}</span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: { type: String, default: 'DISCOUNT' },
  tag: { type: String, default: '减' },
})

const typeClass = computed(() => {
  const map = {
    DISCOUNT: 'tag--red',
    CASHBACK: 'tag--red',
    STAY_N_NIGHTS: 'tag--orange',
    EARLY_BOOKING: 'tag--orange',
    GIFT_PACK: 'tag--green',
    FREE_UPGRADE: 'tag--green',
    OTHER: 'tag--gray',
  }
  return map[props.type] || 'tag--red'
})
</script>

<style lang="scss" scoped>
.promotion-tag {
  display: inline-block;
  font-size: 10px;
  font-weight: 500;
  padding: 1px 4px;
  border-radius: 2px;
  line-height: 1.4;
  vertical-align: middle;
}
.tag--red {
  background: $color-primary-light;
  color: $color-primary;
  border: 1px solid rgba($color-primary, 0.3);
}
.tag--orange {
  background: $color-orange-light;
  color: $color-orange;
}
.tag--green {
  background: $color-green-light;
  color: $color-green;
}
.tag--gray {
  background: $color-gray-200;
  color: $color-gray-700;
}
</style>

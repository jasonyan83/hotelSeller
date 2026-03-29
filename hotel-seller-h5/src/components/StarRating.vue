<template>
  <span class="star-rating">
    <span class="star-diamonds">
      <span v-for="n in count" :key="n" class="diamond">◆</span>
    </span>
  </span>
</template>

<script setup>
defineProps({
  count: { type: Number, default: 3 },
})
</script>

<style lang="scss" scoped>
.star-rating {
  display: inline-flex;
  align-items: center;
}
.diamond {
  color: $color-gold;
  font-size: 10px;
  letter-spacing: -1px;
}
</style>

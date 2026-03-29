<template>
  <router-view v-slot="{ Component }">
    <keep-alive :include="keepAliveList">
      <component :is="Component" />
    </keep-alive>
  </router-view>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const keepAliveList = computed(() => {
  return router.getRoutes()
    .filter(r => r.meta?.keepAlive)
    .map(r => r.name)
})
</script>

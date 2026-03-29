<template>
  <div class="keyword-page">
    <div class="keyword-header">
      <van-icon name="arrow-left" size="20" @click="$router.back()" />
      <div class="keyword-search">
        <van-icon name="search" size="16" color="#999" />
        <input v-model="keyword" placeholder="关键字/位置/品牌/酒店名" class="keyword-input" @input="onInput" />
      </div>
    </div>
    <div class="suggest-list" v-if="suggestions.length">
      <div
        v-for="item in suggestions"
        :key="item.id"
        class="suggest-item"
        @click="selectSuggest(item)"
      >
        <div class="suggest-name">{{ item.name }}</div>
        <div class="suggest-parent">{{ item.parentName }}</div>
      </div>
    </div>
    <div class="hot-keywords" v-else>
      <div class="hot-title">热门搜索</div>
      <div class="hot-tags">
        <span v-for="kw in hotKeywords" :key="kw" class="hot-tag" @click="keyword = kw; onInput()">{{ kw }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useSearchStore } from '@/stores/searchStore'
import { mockAPI } from '@/mock/data'

const router = useRouter()
const searchStore = useSearchStore()

const keyword = ref('')
const suggestions = ref([])
const hotKeywords = ref(['迪士尼', '外滩', '虹桥', '和平饭店', '陆家嘴', '南京路'])

let timer = null
function onInput() {
  clearTimeout(timer)
  timer = setTimeout(async () => {
    if (!keyword.value.trim()) {
      suggestions.value = []
      return
    }
    const res = await mockAPI.suggest(keyword.value)
    suggestions.value = res.data
  }, 300)
}

function selectSuggest(item) {
  searchStore.keyword = item.name
  searchStore.keywordType = item.type
  searchStore.keywordRefId = item.id
  router.back()
}
</script>

<style lang="scss" scoped>
.keyword-page {
  min-height: 100vh;
  background: $color-white;
}
.keyword-header {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 44px;
  padding: 0 $space-lg;
  @include hairline-bottom;
}
.keyword-search {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 6px;
  height: 32px;
  background: $color-gray-100;
  border-radius: 16px;
  padding: 0 12px;
}
.keyword-input {
  flex: 1;
  border: none;
  background: none;
  font-size: 14px;
  outline: none;
  &::placeholder { color: $color-gray-500; }
}
.suggest-list { padding: 0 $space-lg; }
.suggest-item {
  padding: $space-md 0;
  @include hairline-bottom($color-gray-200);
  cursor: pointer;
}
.suggest-name {
  font-size: 14px;
  color: $color-gray-900;
}
.suggest-parent {
  font-size: 12px;
  color: $color-gray-500;
  margin-top: 2px;
}
.hot-keywords {
  padding: $space-lg;
}
.hot-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: $space-md;
}
.hot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.hot-tag {
  padding: 6px 14px;
  background: $color-gray-100;
  border-radius: $radius-chip;
  font-size: 13px;
  color: $color-gray-700;
  cursor: pointer;
}
</style>

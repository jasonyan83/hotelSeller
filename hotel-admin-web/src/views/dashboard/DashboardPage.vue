<template>
  <div class="page-container">
    <div class="page-header">
      <h2>控制台</h2>
      <span style="color: #909399; font-size: 14px">欢迎回来，管理员</span>
    </div>

    <!-- 统计卡片 -->
    <div class="stat-cards">
      <div class="stat-card">
        <div class="stat-card__info">
          <div class="label">供应商总数</div>
          <div class="value">{{ stats.supplierTotal || 0 }}</div>
        </div>
        <div class="stat-card__icon primary">
          <el-icon><OfficeBuilding /></el-icon>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-card__info">
          <div class="label">在线供应商</div>
          <div class="value">{{ stats.supplierOnline || 0 }}</div>
        </div>
        <div class="stat-card__icon success">
          <el-icon><CircleCheck /></el-icon>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-card__info">
          <div class="label">今日搜索量</div>
          <div class="value">{{ formatNumber(stats.todaySearchCount) }}</div>
        </div>
        <div class="stat-card__icon warning">
          <el-icon><Search /></el-icon>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-card__info">
          <div class="label">今日预订量</div>
          <div class="value">{{ stats.todayBookingCount || 0 }}</div>
        </div>
        <div class="stat-card__icon danger">
          <el-icon><Ticket /></el-icon>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <el-row :gutter="24">
      <el-col :span="16">
        <div class="page-card">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
            <h3 style="font-size: 16px; font-weight: 600">搜索趋势</h3>
            <el-radio-group v-model="trendDays" size="small" @change="loadTrend">
              <el-radio-button :value="7">近7天</el-radio-button>
              <el-radio-button :value="14">近14天</el-radio-button>
              <el-radio-button :value="30">近30天</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="trendChartRef" style="height: 360px"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="page-card">
          <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px">供应商报价占比</h3>
          <div ref="pieChartRef" style="height: 360px"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 快速操作 -->
    <div class="page-card" style="margin-top: 24px">
      <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px">快速操作</h3>
      <el-row :gutter="16">
        <el-col :span="6">
          <el-button style="width: 100%; height: 72px" @click="$router.push('/suppliers/add')">
            <div style="text-align: center">
              <el-icon size="24"><Plus /></el-icon>
              <div style="margin-top: 4px">新增供应商</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button style="width: 100%; height: 72px" @click="$router.push('/suppliers')">
            <div style="text-align: center">
              <el-icon size="24"><OfficeBuilding /></el-icon>
              <div style="margin-top: 4px">供应商管理</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button style="width: 100%; height: 72px" @click="$router.push('/recommended')">
            <div style="text-align: center">
              <el-icon size="24"><Star /></el-icon>
              <div style="margin-top: 4px">推荐酒店</div>
            </div>
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button style="width: 100%; height: 72px" @click="$router.push('/logs')">
            <div style="text-align: center">
              <el-icon size="24"><Document /></el-icon>
              <div style="margin-top: 4px">操作日志</div>
            </div>
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'

// Mock数据 (后端未启动时使用)
const stats = ref({
  supplierTotal: 8,
  supplierOnline: 6,
  supplierOffline: 2,
  recommendedHotels: 24,
  priceStrategies: 15,
  todaySearchCount: 12580,
  todayBookingCount: 345,
  cacheHitRate: 0.87,
})

const trendDays = ref(7)
const trendChartRef = ref(null)
const pieChartRef = ref(null)
let trendChart = null
let pieChart = null

const formatNumber = (num) => {
  if (!num) return '0'
  return num.toLocaleString()
}

const loadTrend = () => {
  const days = trendDays.value
  const dates = []
  const searchData = []
  const bookingData = []

  for (let i = days - 1; i >= 0; i--) {
    const d = new Date()
    d.setDate(d.getDate() - i)
    dates.push(`${d.getMonth() + 1}/${d.getDate()}`)
    searchData.push(Math.floor(8000 + Math.random() * 5000))
    bookingData.push(Math.floor(200 + Math.random() * 200))
  }

  if (trendChart) {
    trendChart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['搜索量', '预订量'], top: 0 },
      grid: { left: 50, right: 20, top: 40, bottom: 30 },
      xAxis: { type: 'category', data: dates },
      yAxis: [
        { type: 'value', name: '搜索量' },
        { type: 'value', name: '预订量' },
      ],
      series: [
        {
          name: '搜索量',
          type: 'line',
          smooth: true,
          data: searchData,
          itemStyle: { color: '#409eff' },
          areaStyle: { color: 'rgba(64,158,255,0.1)' },
        },
        {
          name: '预订量',
          type: 'bar',
          yAxisIndex: 1,
          data: bookingData,
          itemStyle: { color: '#67c23a' },
          barWidth: '40%',
        },
      ],
    })
  }
}

const loadPie = () => {
  const data = [
    { name: 'Booking.com', value: 35 },
    { name: 'Expedia', value: 25 },
    { name: '携程分销', value: 15 },
    { name: '美团', value: 12 },
    { name: '同程艺龙', value: 8 },
    { name: 'Agoda', value: 5 },
  ]

  if (pieChart) {
    pieChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c}%' },
      legend: { orient: 'vertical', right: 10, top: 'center' },
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
          data,
        },
      ],
    })
  }
}

onMounted(async () => {
  await nextTick()

  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value)
    loadTrend()
  }
  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value)
    loadPie()
  }

  window.addEventListener('resize', () => {
    trendChart?.resize()
    pieChart?.resize()
  })
})
</script>

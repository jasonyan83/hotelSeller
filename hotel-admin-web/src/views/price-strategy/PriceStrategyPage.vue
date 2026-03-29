<template>
  <div class="page-container">
    <div class="page-header">
      <h2>价格策略 - {{ supplierName }}</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>

    <!-- 全局策略 -->
    <div class="page-card" style="margin-bottom: 24px">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
        <h3 style="font-size: 16px; font-weight: 600">全局价格策略</h3>
        <el-button size="small" @click="editGlobalVisible = true">编辑</el-button>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="加价比例">{{ globalStrategy.markupRate }}%</el-descriptions-item>
        <el-descriptions-item label="加价金额">¥{{ globalStrategy.markupAmount }}/间夜</el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 12px; padding: 12px; background: #f5f7fa; border-radius: 4px; font-size: 13px; color: #909399">
        <strong>计算公式：</strong>售卖价 = 底价 × (1 + 加价比例/100) + 加价金额
      </div>
    </div>

    <!-- 特殊策略列表 -->
    <div class="page-card">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
        <h3 style="font-size: 16px; font-weight: 600">特殊价格策略</h3>
        <el-button type="primary" size="small" @click="openSpecialDialog(null)">
          <el-icon><Plus /></el-icon> 新增特殊策略
        </el-button>
      </div>

      <el-table :data="specialStrategies" stripe style="width: 100%">
        <el-table-column prop="strategyNo" label="策略编号" width="160" />
        <el-table-column label="适用渠道" width="200">
          <template #default="{ row }">
            <el-tag v-for="ch in parseJSON(row.channel)" :key="ch" size="small" style="margin-right: 4px">{{ ch }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="渠道配置" min-width="300">
          <template #default="{ row }">
            <div v-for="(cfg, idx) in parseJSON(row.channelConfigs)" :key="idx" style="font-size: 13px">
              <strong>{{ cfg.channel }}:</strong> 加价比例 {{ cfg.markupRate }}%, 加价金额 ¥{{ cfg.markupAmount }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="updatedTime" label="更新时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openSpecialDialog(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="deleteSpecial(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 全局策略编辑弹窗 -->
    <el-dialog v-model="editGlobalVisible" title="编辑全局价格策略" width="500px">
      <el-form label-width="120px">
        <el-form-item label="加价比例(%)">
          <el-input-number v-model="editGlobal.markupRate" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="加价金额(元)">
          <el-input-number v-model="editGlobal.markupAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editGlobalVisible = false">取消</el-button>
        <el-button type="primary" @click="saveGlobal">确定</el-button>
      </template>
    </el-dialog>

    <!-- 特殊策略编辑弹窗 -->
    <el-dialog v-model="specialDialogVisible" :title="specialForm.id ? '编辑特殊策略' : '新增特殊策略'" width="640px">
      <el-form label-width="120px">
        <el-form-item label="策略编号">
          <el-input v-model="specialForm.strategyNo" placeholder="如: SP_APP_2026" />
        </el-form-item>
        <el-form-item label="适用渠道">
          <el-checkbox-group v-model="specialForm.channels">
            <el-checkbox value="APP">APP</el-checkbox>
            <el-checkbox value="H5">H5</el-checkbox>
            <el-checkbox value="WEB">WEB</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-divider content-position="left">渠道加价配置</el-divider>

        <div v-for="ch in specialForm.channels" :key="ch" style="display: flex; align-items: center; gap: 12px; margin-bottom: 12px">
          <el-tag>{{ ch }}</el-tag>
          <span style="font-size: 13px">加价比例:</span>
          <el-input-number
            v-model="specialForm.configs[ch].markupRate"
            :min="0" :max="100" :precision="2" size="small" style="width: 120px"
          />
          <span style="font-size: 13px">%  加价金额:</span>
          <el-input-number
            v-model="specialForm.configs[ch].markupAmount"
            :min="0" :precision="2" size="small" style="width: 120px"
          />
          <span style="font-size: 13px">元</span>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="specialDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSpecial">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const supplierId = route.params.id
const supplierName = ref('Booking.com')

const globalStrategy = ref({ markupRate: 5.0, markupAmount: 10.0 })
const editGlobalVisible = ref(false)
const editGlobal = reactive({ markupRate: 5.0, markupAmount: 10.0 })

const specialStrategies = ref([])
const specialDialogVisible = ref(false)
const specialForm = reactive({
  id: null,
  strategyNo: '',
  channels: ['APP', 'H5'],
  configs: {
    APP: { markupRate: 0, markupAmount: 0 },
    H5: { markupRate: 0, markupAmount: 0 },
    WEB: { markupRate: 0, markupAmount: 0 },
  },
})

const parseJSON = (str) => {
  try {
    if (Array.isArray(str)) return str
    return JSON.parse(str || '[]')
  } catch {
    return []
  }
}

const saveGlobal = () => {
  Object.assign(globalStrategy.value, editGlobal)
  editGlobalVisible.value = false
  ElMessage.success('全局策略保存成功')
}

const openSpecialDialog = (row) => {
  if (row) {
    specialForm.id = row.id
    specialForm.strategyNo = row.strategyNo
    specialForm.channels = parseJSON(row.channel)
    const cfgs = parseJSON(row.channelConfigs)
    cfgs.forEach(c => {
      if (specialForm.configs[c.channel]) {
        specialForm.configs[c.channel] = { markupRate: c.markupRate, markupAmount: c.markupAmount }
      }
    })
  } else {
    specialForm.id = null
    specialForm.strategyNo = ''
    specialForm.channels = ['APP', 'H5']
    Object.keys(specialForm.configs).forEach(k => {
      specialForm.configs[k] = { markupRate: 0, markupAmount: 0 }
    })
  }
  specialDialogVisible.value = true
}

const saveSpecial = () => {
  const channelConfigs = specialForm.channels.map(ch => ({
    channel: ch,
    markupRate: specialForm.configs[ch]?.markupRate || 0,
    markupAmount: specialForm.configs[ch]?.markupAmount || 0,
  }))

  if (specialForm.id) {
    const idx = specialStrategies.value.findIndex(s => s.id === specialForm.id)
    if (idx >= 0) {
      specialStrategies.value[idx] = {
        ...specialStrategies.value[idx],
        strategyNo: specialForm.strategyNo,
        channel: JSON.stringify(specialForm.channels),
        channelConfigs: JSON.stringify(channelConfigs),
      }
    }
    ElMessage.success('编辑成功')
  } else {
    specialStrategies.value.push({
      id: Date.now(),
      strategyNo: specialForm.strategyNo,
      channel: JSON.stringify(specialForm.channels),
      channelConfigs: JSON.stringify(channelConfigs),
      updatedTime: new Date().toLocaleString(),
    })
    ElMessage.success('新增成功')
  }
  specialDialogVisible.value = false
}

const deleteSpecial = (row) => {
  ElMessageBox.confirm(`确定删除策略「${row.strategyNo}」吗？`, '确认删除', { type: 'warning' })
    .then(() => {
      specialStrategies.value = specialStrategies.value.filter(s => s.id !== row.id)
      ElMessage.success('删除成功')
    }).catch(() => {})
}

// 确保新增渠道时有默认配置
watch(() => specialForm.channels, (newChs) => {
  newChs.forEach(ch => {
    if (!specialForm.configs[ch]) {
      specialForm.configs[ch] = { markupRate: 0, markupAmount: 0 }
    }
  })
})

onMounted(() => {
  // Mock
  editGlobal.markupRate = globalStrategy.value.markupRate
  editGlobal.markupAmount = globalStrategy.value.markupAmount

  specialStrategies.value = [
    {
      id: 1,
      strategyNo: 'SP_APP_2026Q1',
      channel: JSON.stringify(['APP', 'H5']),
      channelConfigs: JSON.stringify([
        { channel: 'APP', markupRate: 3.0, markupAmount: 5 },
        { channel: 'H5', markupRate: 2.5, markupAmount: 8 },
      ]),
      updatedTime: '2026-03-28 10:00:00',
    },
    {
      id: 2,
      strategyNo: 'SP_WEB_2026Q1',
      channel: JSON.stringify(['WEB']),
      channelConfigs: JSON.stringify([
        { channel: 'WEB', markupRate: 4.0, markupAmount: 0 },
      ]),
      updatedTime: '2026-03-27 15:30:00',
    },
  ]
})
</script>

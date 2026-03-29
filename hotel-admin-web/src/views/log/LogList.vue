<template>
  <div class="page-container">
    <div class="page-header">
      <h2>操作日志</h2>
    </div>

    <div class="page-card">
      <!-- 筛选栏 -->
      <div class="filter-bar">
        <el-input
          v-model="filters.keyword"
          placeholder="搜索供应商名称/操作人"
          clearable
          style="width: 240px"
          @keyup.enter="loadData"
        >
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-select v-model="filters.operateType" placeholder="操作类型" clearable style="width: 140px">
          <el-option label="新增" value="CREATE" />
          <el-option label="编辑" value="EDIT" />
          <el-option label="删除" value="DELETE" />
          <el-option label="上线" value="ONLINE" />
          <el-option label="下线" value="OFFLINE" />
        </el-select>
        <el-date-picker
          v-model="filters.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 280px"
        />
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetFilters">重置</el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="operateTime" label="操作时间" width="180" sortable />
        <el-table-column prop="supplierName" label="供应商" width="160" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column label="操作类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small" :type="getTypeColor(row.operateType)">
              {{ getTypeLabel(row.operateType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operateTarget" label="操作对象" width="140" />
        <el-table-column prop="operateContent" label="操作内容" min-width="280" show-overflow-tooltip />
      </el-table>

      <!-- 分页 -->
      <div style="display: flex; justify-content: flex-end; margin-top: 16px">
        <el-pagination
          v-model:current-page="pagination.pageNo"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

const loading = ref(false)
const tableData = ref([])
const filters = reactive({ keyword: '', operateType: null, dateRange: null })
const pagination = reactive({ pageNo: 1, pageSize: 20, total: 0 })

const mockLogs = [
  { id: 1, operateTime: '2026-03-29 10:30:00', supplierName: 'Booking.com', operator: 'admin', operateType: 'EDIT', operateTarget: '供应商', operateContent: '编辑供应商信息: 修改结算币种为USD' },
  { id: 2, operateTime: '2026-03-29 09:15:00', supplierName: 'Expedia集团', operator: 'admin', operateType: 'EDIT', operateTarget: '全局价格策略', operateContent: '设置全局策略: 加价比例=5.0%, 加价金额=10.00' },
  { id: 3, operateTime: '2026-03-28 16:45:00', supplierName: '携程分销', operator: 'admin', operateType: 'ONLINE', operateTarget: '供应商状态', operateContent: '上线供应商: 携程分销' },
  { id: 4, operateTime: '2026-03-28 14:20:00', supplierName: '同程艺龙', operator: 'admin', operateType: 'OFFLINE', operateTarget: '供应商状态', operateContent: '下线供应商: 同程艺龙' },
  { id: 5, operateTime: '2026-03-28 11:00:00', supplierName: 'Booking.com', operator: 'admin', operateType: 'CREATE', operateTarget: '特殊价格策略', operateContent: '新增特殊策略: SP_APP_2026Q1' },
  { id: 6, operateTime: '2026-03-27 10:30:00', supplierName: '美团酒店', operator: 'admin', operateType: 'EDIT', operateTarget: '缓存策略', operateContent: '设置缓存策略: 参与列表页=1, 详情页报价源=CACHE_FIRST' },
  { id: 7, operateTime: '2026-03-27 09:00:00', supplierName: 'Agoda', operator: 'admin', operateType: 'CREATE', operateTarget: '供应商', operateContent: '新增供应商: Agoda' },
  { id: 8, operateTime: '2026-03-26 16:30:00', supplierName: '华住会', operator: 'admin', operateType: 'CREATE', operateTarget: '供应商', operateContent: '新增供应商: 华住会' },
  { id: 9, operateTime: '2026-03-26 14:00:00', supplierName: 'Booking.com', operator: 'admin', operateType: 'DELETE', operateTarget: '特殊价格策略', operateContent: '删除特殊策略: SP_WEB_OLD' },
  { id: 10, operateTime: '2026-03-25 11:00:00', supplierName: '锦江酒店', operator: 'admin', operateType: 'CREATE', operateTarget: '供应商', operateContent: '新增供应商: 锦江酒店' },
]

const getTypeColor = (type) => {
  const map = { CREATE: 'success', EDIT: '', DELETE: 'danger', ONLINE: 'success', OFFLINE: 'warning' }
  return map[type] || 'info'
}

const getTypeLabel = (type) => {
  const map = { CREATE: '新增', EDIT: '编辑', DELETE: '删除', ONLINE: '上线', OFFLINE: '下线' }
  return map[type] || type
}

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockLogs]
    if (filters.keyword) {
      const kw = filters.keyword.toLowerCase()
      data = data.filter(l =>
        l.supplierName.toLowerCase().includes(kw) ||
        l.operator.toLowerCase().includes(kw)
      )
    }
    if (filters.operateType) {
      data = data.filter(l => l.operateType === filters.operateType)
    }
    pagination.total = data.length
    const start = (pagination.pageNo - 1) * pagination.pageSize
    tableData.value = data.slice(start, start + pagination.pageSize)
    loading.value = false
  }, 200)
}

const resetFilters = () => {
  filters.keyword = ''
  filters.operateType = null
  filters.dateRange = null
  pagination.pageNo = 1
  loadData()
}

onMounted(() => loadData())
</script>

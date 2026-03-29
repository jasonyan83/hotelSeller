<template>
  <div class="page-container">
    <div class="page-header">
      <h2>供应商管理</h2>
      <el-button type="primary" @click="$router.push('/suppliers/add')">
        <el-icon><Plus /></el-icon> 新增供应商
      </el-button>
    </div>

    <!-- 筛选栏 -->
    <div class="page-card">
      <div class="filter-bar">
        <el-input
          v-model="filters.keyword"
          placeholder="搜索供应商名称/编号"
          clearable
          style="width: 240px"
          @keyup.enter="loadData"
        >
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-select v-model="filters.status" placeholder="状态" clearable style="width: 120px">
          <el-option label="在线" :value="1" />
          <el-option label="离线" :value="0" />
        </el-select>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetFilters">重置</el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="supplierCode" label="供应商编号" width="130" />
        <el-table-column prop="supplierName" label="供应商名称" min-width="160" />
        <el-table-column prop="brandName" label="品牌名" width="120" />
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="报价类型" width="110">
          <template #default="{ row }">
            {{ row.quoteType === 'BASE_PRICE' ? '底价' : '售价' }}
          </template>
        </el-table-column>
        <el-table-column label="数据来源" width="100">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.dataSource }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="platform" label="对接平台" width="120" />
        <el-table-column label="更新时间" width="170">
          <template #default="{ row }">
            {{ row.updatedTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button link type="primary" size="small" @click="viewDetail(row)">详情</el-button>
              <el-button link type="primary" size="small" @click="editSupplier(row)">编辑</el-button>
              <el-button link type="primary" size="small" @click="goPriceStrategy(row)">价格策略</el-button>
              <el-button
                link
                :type="row.status === 1 ? 'danger' : 'success'"
                size="small"
                @click="toggleStatus(row)"
              >
                {{ row.status === 1 ? '下线' : '上线' }}
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="display: flex; justify-content: flex-end; margin-top: 16px">
        <el-pagination
          v-model:current-page="pagination.pageNo"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const filters = reactive({ keyword: '', status: null })
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

// Mock数据(后端未启动时)
const mockSuppliers = [
  { id: 1, supplierCode: 'BOOKING', supplierName: 'Booking.com', brandName: 'Booking', status: 1, quoteType: 'BASE_PRICE', dataSource: 'API', platform: 'CHANGLINK', updatedTime: '2026-03-28 10:30:00' },
  { id: 2, supplierCode: 'EXPEDIA', supplierName: 'Expedia集团', brandName: 'Expedia', status: 1, quoteType: 'SELL_PRICE', dataSource: 'API', platform: 'CHANGLINK', updatedTime: '2026-03-28 09:15:00' },
  { id: 3, supplierCode: 'CTRIP_DS', supplierName: '携程分销', brandName: '携程', status: 1, quoteType: 'BASE_PRICE', dataSource: 'API', platform: 'DIRECT', updatedTime: '2026-03-27 14:20:00' },
  { id: 4, supplierCode: 'MEITUAN', supplierName: '美团酒店', brandName: '美团', status: 1, quoteType: 'SELL_PRICE', dataSource: 'API', platform: 'DIRECT', updatedTime: '2026-03-27 11:00:00' },
  { id: 5, supplierCode: 'TONGCHENG', supplierName: '同程艺龙', brandName: '同程', status: 0, quoteType: 'BASE_PRICE', dataSource: 'API', platform: 'CHANGLINK', updatedTime: '2026-03-26 16:45:00' },
  { id: 6, supplierCode: 'AGODA', supplierName: 'Agoda', brandName: 'Agoda', status: 1, quoteType: 'BASE_PRICE', dataSource: 'API', platform: 'CHANGLINK', updatedTime: '2026-03-26 10:30:00' },
  { id: 7, supplierCode: 'HUAZHU', supplierName: '华住会', brandName: '华住', status: 0, quoteType: 'SELL_PRICE', dataSource: 'EBOOKING', platform: 'DIRECT', updatedTime: '2026-03-25 09:00:00' },
  { id: 8, supplierCode: 'JINJIANG', supplierName: '锦江酒店', brandName: '锦江', status: 1, quoteType: 'BASE_PRICE', dataSource: 'API', platform: 'DIRECT', updatedTime: '2026-03-25 08:30:00' },
]

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockSuppliers]
    if (filters.keyword) {
      const kw = filters.keyword.toLowerCase()
      data = data.filter(s => s.supplierName.toLowerCase().includes(kw) || s.supplierCode.toLowerCase().includes(kw))
    }
    if (filters.status !== null && filters.status !== '') {
      data = data.filter(s => s.status === filters.status)
    }
    pagination.total = data.length
    const start = (pagination.pageNo - 1) * pagination.pageSize
    tableData.value = data.slice(start, start + pagination.pageSize)
    loading.value = false
  }, 300)
}

const resetFilters = () => {
  filters.keyword = ''
  filters.status = null
  pagination.pageNo = 1
  loadData()
}

const viewDetail = (row) => {
  router.push(`/suppliers/${row.id}/detail`)
}

const editSupplier = (row) => {
  router.push(`/suppliers/${row.id}/edit`)
}

const goPriceStrategy = (row) => {
  router.push(`/suppliers/${row.id}/price-strategy`)
}

const toggleStatus = (row) => {
  const action = row.status === 1 ? '下线' : '上线'
  ElMessageBox.confirm(`确定要${action}供应商「${row.supplierName}」吗？`, '确认操作', {
    type: 'warning',
  }).then(() => {
    row.status = row.status === 1 ? 0 : 1
    ElMessage.success(`${action}成功`)
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

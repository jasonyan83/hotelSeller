<template>
  <div class="page-container">
    <div class="page-header">
      <h2>推荐酒店管理</h2>
      <el-button type="primary" @click="addDialogVisible = true">
        <el-icon><Plus /></el-icon> 添加推荐
      </el-button>
    </div>

    <div class="page-card">
      <!-- 筛选 -->
      <div class="filter-bar">
        <el-select v-model="filters.destinationCode" placeholder="选择目的地" clearable style="width: 200px" @change="loadData">
          <el-option label="上海" value="SH" />
          <el-option label="北京" value="BJ" />
          <el-option label="广州" value="GZ" />
          <el-option label="深圳" value="SZ" />
          <el-option label="杭州" value="HZ" />
          <el-option label="三亚" value="SY" />
        </el-select>
        <el-button @click="loadData">刷新</el-button>
        <el-button type="danger" :disabled="selectedIds.length === 0" @click="batchDelete">
          批量删除 ({{ selectedIds.length }})
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table
        :data="tableData"
        stripe
        style="width: 100%"
        @selection-change="handleSelection"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
        <el-table-column prop="destinationCode" label="目的地编码" width="120" />
        <el-table-column prop="destinationName" label="目的地名称" width="140" />
        <el-table-column prop="destinationType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.destinationType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="hotelId" label="酒店ID" width="100" />
        <el-table-column prop="hotelName" label="酒店名称" min-width="240" />
        <el-table-column prop="updatedTime" label="更新时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row, $index }">
            <el-button link type="primary" size="small" :disabled="$index === 0" @click="moveUp($index)">上移</el-button>
            <el-button link type="primary" size="small" :disabled="$index === tableData.length - 1" @click="moveDown($index)">下移</el-button>
            <el-button link type="danger" size="small" @click="deleteOne(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="display: flex; justify-content: flex-end; margin-top: 16px">
        <el-pagination
          v-model:current-page="pagination.pageNo"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <!-- 添加推荐弹窗 -->
    <el-dialog v-model="addDialogVisible" title="添加推荐酒店" width="560px">
      <el-form label-width="100px" :model="addForm">
        <el-form-item label="目的地编码">
          <el-select v-model="addForm.destinationCode" style="width: 100%" @change="onDestChange">
            <el-option label="上海" value="SH" />
            <el-option label="北京" value="BJ" />
            <el-option label="广州" value="GZ" />
          </el-select>
        </el-form-item>
        <el-form-item label="目的地名称">
          <el-input v-model="addForm.destinationName" />
        </el-form-item>
        <el-form-item label="目的地类型">
          <el-select v-model="addForm.destinationType" style="width: 100%">
            <el-option label="城市" value="CITY" />
            <el-option label="商圈" value="DISTRICT" />
            <el-option label="城市标签" value="CITY_TAG" />
          </el-select>
        </el-form-item>
        <el-form-item label="酒店ID">
          <el-input v-model="addForm.hotelId" placeholder="输入酒店ID" />
        </el-form-item>
        <el-form-item label="酒店名称">
          <el-input v-model="addForm.hotelName" placeholder="输入酒店名称" />
        </el-form-item>
        <el-form-item label="排序序号">
          <el-input-number v-model="addForm.sortOrder" :min="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doAdd">确定添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const selectedIds = ref([])
const filters = reactive({ destinationCode: '' })
const pagination = reactive({ pageNo: 1, pageSize: 20, total: 0 })
const addDialogVisible = ref(false)
const addForm = reactive({
  destinationCode: 'SH',
  destinationName: '上海',
  destinationType: 'CITY',
  hotelId: '',
  hotelName: '',
  sortOrder: 1,
})

const mockData = [
  { id: 1, sortOrder: 1, destinationCode: 'SH', destinationName: '上海', destinationType: 'CITY', hotelId: 'H001', hotelName: '上海外滩华尔道夫酒店', updatedTime: '2026-03-28 10:00:00' },
  { id: 2, sortOrder: 2, destinationCode: 'SH', destinationName: '上海', destinationType: 'CITY', hotelId: 'H002', hotelName: '上海浦东丽思卡尔顿酒店', updatedTime: '2026-03-28 09:00:00' },
  { id: 3, sortOrder: 3, destinationCode: 'SH', destinationName: '上海', destinationType: 'CITY', hotelId: 'H003', hotelName: '上海静安香格里拉大酒店', updatedTime: '2026-03-27 14:00:00' },
  { id: 4, sortOrder: 4, destinationCode: 'SH', destinationName: '上海', destinationType: 'CITY', hotelId: 'H004', hotelName: '上海新天地安达仕酒店', updatedTime: '2026-03-27 10:00:00' },
  { id: 5, sortOrder: 1, destinationCode: 'BJ', destinationName: '北京', destinationType: 'CITY', hotelId: 'H101', hotelName: '北京王府井文华东方酒店', updatedTime: '2026-03-26 16:00:00' },
  { id: 6, sortOrder: 2, destinationCode: 'BJ', destinationName: '北京', destinationType: 'CITY', hotelId: 'H102', hotelName: '北京国贸大酒店', updatedTime: '2026-03-26 15:00:00' },
]

const loadData = () => {
  loading.value = true
  setTimeout(() => {
    let data = [...mockData]
    if (filters.destinationCode) {
      data = data.filter(d => d.destinationCode === filters.destinationCode)
    }
    data.sort((a, b) => a.sortOrder - b.sortOrder)
    pagination.total = data.length
    const start = (pagination.pageNo - 1) * pagination.pageSize
    tableData.value = data.slice(start, start + pagination.pageSize)
    loading.value = false
  }, 200)
}

const handleSelection = (rows) => {
  selectedIds.value = rows.map(r => r.id)
}

const moveUp = (index) => {
  if (index > 0) {
    const temp = tableData.value[index].sortOrder
    tableData.value[index].sortOrder = tableData.value[index - 1].sortOrder
    tableData.value[index - 1].sortOrder = temp
    ;[tableData.value[index], tableData.value[index - 1]] = [tableData.value[index - 1], tableData.value[index]]
    ElMessage.success('排序已更新')
  }
}

const moveDown = (index) => {
  if (index < tableData.value.length - 1) {
    const temp = tableData.value[index].sortOrder
    tableData.value[index].sortOrder = tableData.value[index + 1].sortOrder
    tableData.value[index + 1].sortOrder = temp
    ;[tableData.value[index], tableData.value[index + 1]] = [tableData.value[index + 1], tableData.value[index]]
    ElMessage.success('排序已更新')
  }
}

const deleteOne = (row) => {
  ElMessageBox.confirm(`确定删除推荐酒店「${row.hotelName}」吗？`, '确认删除', { type: 'warning' })
    .then(() => {
      const idx = mockData.findIndex(d => d.id === row.id)
      if (idx >= 0) mockData.splice(idx, 1)
      loadData()
      ElMessage.success('删除成功')
    }).catch(() => {})
}

const batchDelete = () => {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条推荐吗？`, '确认删除', { type: 'warning' })
    .then(() => {
      selectedIds.value.forEach(id => {
        const idx = mockData.findIndex(d => d.id === id)
        if (idx >= 0) mockData.splice(idx, 1)
      })
      selectedIds.value = []
      loadData()
      ElMessage.success('批量删除成功')
    }).catch(() => {})
}

const onDestChange = (val) => {
  const nameMap = { SH: '上海', BJ: '北京', GZ: '广州' }
  addForm.destinationName = nameMap[val] || ''
}

const doAdd = () => {
  if (!addForm.hotelId || !addForm.hotelName) {
    ElMessage.warning('请填写酒店ID和名称')
    return
  }
  mockData.push({
    id: Date.now(),
    ...addForm,
    updatedTime: new Date().toLocaleString(),
  })
  addDialogVisible.value = false
  loadData()
  ElMessage.success('添加成功')
}

onMounted(() => loadData())
</script>

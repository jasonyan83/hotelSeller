<template>
  <div class="page-container">
    <div class="page-header">
      <h2>供应商详情</h2>
      <div>
        <el-button type="primary" @click="$router.push(`/suppliers/${supplierId}/edit`)">编辑</el-button>
        <el-button @click="$router.push(`/suppliers/${supplierId}/price-strategy`)">价格策略</el-button>
        <el-button @click="$router.back()">返回</el-button>
      </div>
    </div>

    <!-- 基本信息 -->
    <div class="page-card" style="margin-bottom: 24px">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
        <h3 style="font-size: 16px; font-weight: 600">基本信息</h3>
        <el-tag :type="supplier.status === 1 ? 'success' : 'danger'" size="large">
          {{ supplier.status === 1 ? '在线' : '离线' }}
        </el-tag>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="供应商编号">{{ supplier.supplierCode }}</el-descriptions-item>
        <el-descriptions-item label="供应商名称">{{ supplier.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="品牌名">{{ supplier.brandName }}</el-descriptions-item>
        <el-descriptions-item label="上架渠道">
          <el-tag v-for="ch in parseJSON(supplier.channels)" :key="ch" size="small" style="margin-right: 4px">{{ ch }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报价模式">{{ supplier.quoteMode }}</el-descriptions-item>
        <el-descriptions-item label="报价类型">{{ supplier.quoteType === 'BASE_PRICE' ? '底价' : '售价' }}</el-descriptions-item>
        <el-descriptions-item label="开通业务">
          <el-tag v-for="bt in parseJSON(supplier.businessTypes)" :key="bt" size="small" type="info" style="margin-right: 4px">{{ bt }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="数据来源">{{ supplier.dataSource }}</el-descriptions-item>
        <el-descriptions-item label="对接平台">{{ supplier.platform }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 结算信息 -->
    <div class="page-card" style="margin-bottom: 24px">
      <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px">结算信息</h3>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="结算币种">{{ supplier.settlementCurrency }}</el-descriptions-item>
        <el-descriptions-item label="结算周期">{{ supplier.settlementCycle }}</el-descriptions-item>
        <el-descriptions-item label="预付结算方式">{{ supplier.prepaySettleMode }}</el-descriptions-item>
        <el-descriptions-item label="预付佣金比例">{{ supplier.prepayCommissionRate }}%</el-descriptions-item>
        <el-descriptions-item label="现付结算方式">{{ supplier.payatSettleMode }}</el-descriptions-item>
        <el-descriptions-item label="现付佣金比例">{{ supplier.payatCommissionRate }}%</el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 联系信息 -->
    <div class="page-card" style="margin-bottom: 24px">
      <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px">联系信息</h3>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="我方商务负责人">{{ contactInfo.businessContact }}</el-descriptions-item>
        <el-descriptions-item label="我方商务电话">{{ contactInfo.businessPhone }}</el-descriptions-item>
        <el-descriptions-item label="对方联系人">{{ contactInfo.supplierContact }}</el-descriptions-item>
        <el-descriptions-item label="对方联系电话">{{ contactInfo.supplierPhone }}</el-descriptions-item>
        <el-descriptions-item label="对方邮箱" :span="2">{{ contactInfo.supplierEmail }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 缓存策略 -->
    <div class="page-card" style="margin-bottom: 24px">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
        <h3 style="font-size: 16px; font-weight: 600">缓存策略</h3>
        <el-button size="small" @click="editCacheVisible = true">编辑</el-button>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="参与列表页报价">
          <el-tag :type="cacheStrategy.participateListPrice === 1 ? 'success' : 'info'" size="small">
            {{ cacheStrategy.participateListPrice === 1 ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="详情页报价源">{{ cacheStrategy.detailPriceSource }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 操作日志 -->
    <div class="page-card">
      <h3 style="font-size: 16px; font-weight: 600; margin-bottom: 16px">操作日志</h3>
      <el-table :data="logs" stripe style="width: 100%" max-height="300">
        <el-table-column prop="operateTime" label="操作时间" width="180" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="operateType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag size="small" :type="getLogTypeColor(row.operateType)">{{ row.operateType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operateTarget" label="操作对象" width="140" />
        <el-table-column prop="operateContent" label="操作内容" min-width="200" />
      </el-table>
    </div>

    <!-- 缓存策略编辑弹窗 -->
    <el-dialog v-model="editCacheVisible" title="编辑缓存策略" width="480px">
      <el-form label-width="140px">
        <el-form-item label="参与列表页报价">
          <el-switch v-model="editCache.participateListPrice" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="详情页报价源">
          <el-radio-group v-model="editCache.detailPriceSource">
            <el-radio value="CACHE_FIRST">缓存优先</el-radio>
            <el-radio value="REALTIME">实时</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editCacheVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCacheStrategy">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const supplierId = route.params.id

const supplier = ref({})
const contactInfo = ref({})
const cacheStrategy = ref({ participateListPrice: 1, detailPriceSource: 'CACHE_FIRST' })
const logs = ref([])
const editCacheVisible = ref(false)
const editCache = reactive({ participateListPrice: 1, detailPriceSource: 'CACHE_FIRST' })

const parseJSON = (str) => {
  try {
    if (Array.isArray(str)) return str
    return JSON.parse(str || '[]')
  } catch {
    return []
  }
}

const getLogTypeColor = (type) => {
  const map = { CREATE: 'success', EDIT: 'primary', DELETE: 'danger', ONLINE: 'success', OFFLINE: 'warning' }
  return map[type] || 'info'
}

const saveCacheStrategy = () => {
  Object.assign(cacheStrategy.value, editCache)
  editCacheVisible.value = false
  ElMessage.success('缓存策略保存成功')
}

onMounted(() => {
  // Mock数据
  supplier.value = {
    supplierCode: 'BOOKING',
    supplierName: 'Booking.com',
    brandName: 'Booking',
    status: 1,
    channels: ['APP', 'H5', 'WEB'],
    quoteMode: 'DOMESTIC',
    businessTypes: ['PREPAY', 'PAY_AT_HOTEL'],
    quoteType: 'BASE_PRICE',
    settlementCurrency: 'USD',
    settlementCycle: 'MONTHLY',
    prepaySettleMode: 'FIXED_PRE_DEDUCT',
    prepayCommissionRate: 15,
    payatSettleMode: 'DYNAMIC',
    payatCommissionRate: 12,
    dataSource: 'API',
    platform: 'CHANGLINK',
  }
  contactInfo.value = {
    businessContact: '张三',
    businessPhone: '13800138000',
    supplierContact: 'John',
    supplierPhone: '+1-555-1234',
    supplierEmail: 'john@booking.com',
  }
  logs.value = [
    { operateTime: '2026-03-28 10:30:00', operator: 'admin', operateType: 'EDIT', operateTarget: '供应商', operateContent: '编辑供应商信息' },
    { operateTime: '2026-03-27 14:20:00', operator: 'admin', operateType: 'ONLINE', operateTarget: '供应商状态', operateContent: '上线供应商: Booking.com' },
    { operateTime: '2026-03-26 09:00:00', operator: 'admin', operateType: 'CREATE', operateTarget: '供应商', operateContent: '新增供应商: Booking.com' },
  ]
})
</script>

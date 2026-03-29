<template>
  <div class="page-container">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑供应商' : '新增供应商' }}</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>

    <div class="page-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="140px" style="max-width: 800px">
        <!-- 基本信息 -->
        <h3 style="margin-bottom: 20px; color: #303133; font-size: 16px">基本信息</h3>

        <el-form-item label="供应商编号" prop="supplierCode">
          <el-input v-model="form.supplierCode" placeholder="如: BOOKING" :disabled="isEdit" />
        </el-form-item>

        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="form.supplierName" placeholder="如: Booking.com" />
        </el-form-item>

        <el-form-item label="品牌名">
          <el-input v-model="form.brandName" placeholder="品牌名称" />
        </el-form-item>

        <el-form-item label="上架渠道">
          <el-checkbox-group v-model="form.channels">
            <el-checkbox value="APP">APP</el-checkbox>
            <el-checkbox value="H5">H5</el-checkbox>
            <el-checkbox value="WEB">WEB</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="报价模式">
          <el-select v-model="form.quoteMode" style="width: 100%">
            <el-option label="国内" value="DOMESTIC" />
            <el-option label="国际" value="INTERNATIONAL" />
          </el-select>
        </el-form-item>

        <el-form-item label="开通业务">
          <el-checkbox-group v-model="form.businessTypes">
            <el-checkbox value="PREPAY">预付</el-checkbox>
            <el-checkbox value="PAY_AT_HOTEL">现付</el-checkbox>
            <el-checkbox value="GUARANTEE">担保</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="报价类型" prop="quoteType">
          <el-radio-group v-model="form.quoteType">
            <el-radio value="BASE_PRICE">底价</el-radio>
            <el-radio value="SELL_PRICE">售价</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 结算信息 -->
        <el-divider />
        <h3 style="margin-bottom: 20px; color: #303133; font-size: 16px">结算信息</h3>

        <el-form-item label="结算币种">
          <el-select v-model="form.settlementCurrency" style="width: 100%">
            <el-option label="人民币 (CNY)" value="CNY" />
            <el-option label="美元 (USD)" value="USD" />
            <el-option label="欧元 (EUR)" value="EUR" />
          </el-select>
        </el-form-item>

        <el-form-item label="结算周期">
          <el-select v-model="form.settlementCycle" style="width: 100%">
            <el-option label="日结" value="DAILY" />
            <el-option label="周结" value="WEEKLY" />
            <el-option label="月结" value="MONTHLY" />
          </el-select>
        </el-form-item>

        <el-form-item label="预付结算方式">
          <el-input v-model="form.prepaySettleMode" placeholder="如: FIXED_PRE_DEDUCT" />
        </el-form-item>

        <el-form-item label="预付佣金比例(%)">
          <el-input-number v-model="form.prepayCommissionRate" :min="0" :max="100" :precision="2" />
        </el-form-item>

        <el-form-item label="现付结算方式">
          <el-input v-model="form.payatSettleMode" placeholder="如: DYNAMIC" />
        </el-form-item>

        <el-form-item label="现付佣金比例(%)">
          <el-input-number v-model="form.payatCommissionRate" :min="0" :max="100" :precision="2" />
        </el-form-item>

        <!-- 技术对接 -->
        <el-divider />
        <h3 style="margin-bottom: 20px; color: #303133; font-size: 16px">技术对接</h3>

        <el-form-item label="数据来源">
          <el-radio-group v-model="form.dataSource">
            <el-radio value="API">API</el-radio>
            <el-radio value="EBOOKING">EBooking</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="对接平台">
          <el-input v-model="form.platform" placeholder="如: CHANGLINK" />
        </el-form-item>

        <el-form-item label="IP白名单">
          <el-input v-model="form.ipWhitelist" type="textarea" :rows="2" placeholder="多个IP用逗号分隔" />
        </el-form-item>

        <!-- 联系人信息 -->
        <el-divider />
        <h3 style="margin-bottom: 20px; color: #303133; font-size: 16px">联系信息</h3>

        <el-form-item label="我方商务负责人">
          <el-input v-model="contact.businessContact" placeholder="姓名" />
        </el-form-item>

        <el-form-item label="我方商务电话">
          <el-input v-model="contact.businessPhone" placeholder="电话" />
        </el-form-item>

        <el-form-item label="对方联系人">
          <el-input v-model="contact.supplierContact" placeholder="姓名" />
        </el-form-item>

        <el-form-item label="对方联系电话">
          <el-input v-model="contact.supplierPhone" placeholder="电话" />
        </el-form-item>

        <el-form-item label="对方邮箱">
          <el-input v-model="contact.supplierEmail" placeholder="邮箱" />
        </el-form-item>

        <!-- 提交 -->
        <el-divider />
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEdit ? '保存修改' : '提交新增' }}
          </el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  supplierCode: '',
  supplierName: '',
  brandName: '',
  channels: ['APP', 'H5', 'WEB'],
  quoteMode: 'DOMESTIC',
  businessTypes: ['PREPAY'],
  quoteType: 'BASE_PRICE',
  settlementCurrency: 'CNY',
  settlementCycle: 'MONTHLY',
  prepaySettleMode: '',
  prepayCommissionRate: 0,
  payatSettleMode: '',
  payatCommissionRate: 0,
  dataSource: 'API',
  platform: '',
  ipWhitelist: '',
})

const contact = reactive({
  businessContact: '',
  businessPhone: '',
  supplierContact: '',
  supplierPhone: '',
  supplierEmail: '',
})

const rules = {
  supplierCode: [{ required: true, message: '请输入供应商编号', trigger: 'blur' }],
  supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
  quoteType: [{ required: true, message: '请选择报价类型', trigger: 'change' }],
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    // Mock提交
    setTimeout(() => {
      submitting.value = false
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      router.push('/suppliers')
    }, 500)
  } catch (e) {
    // 表单验证失败
  }
}

onMounted(() => {
  if (isEdit.value) {
    // Mock加载编辑数据
    Object.assign(form, {
      supplierCode: 'BOOKING',
      supplierName: 'Booking.com',
      brandName: 'Booking',
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
      ipWhitelist: '10.0.0.1,10.0.0.2',
    })
    Object.assign(contact, {
      businessContact: '张三',
      businessPhone: '13800138000',
      supplierContact: 'John',
      supplierPhone: '+1-555-1234',
      supplierEmail: 'john@booking.com',
    })
  }
})
</script>

import request from '@/utils/request'

// ============ 供应商管理 ============

/** 分页查询供应商列表 */
export function listSuppliers(params) {
  return request.get('/suppliers', { params })
}

/** 查询供应商详情 */
export function getSupplierDetail(id) {
  return request.get(`/suppliers/${id}`)
}

/** 新增供应商 */
export function addSupplier(data) {
  return request.post('/suppliers', data)
}

/** 编辑供应商 */
export function updateSupplier(id, data) {
  return request.put(`/suppliers/${id}`, data)
}

/** 上下线供应商 */
export function updateSupplierStatus(id, data) {
  return request.put(`/suppliers/${id}/status`, data)
}

// ============ 价格策略 ============

/** 查询全局价格策略 */
export function getGlobalStrategy(supplierId) {
  return request.get(`/suppliers/${supplierId}/global-strategy`)
}

/** 设置全局价格策略 */
export function saveGlobalStrategy(supplierId, data) {
  return request.put(`/suppliers/${supplierId}/global-strategy`, data)
}

/** 查询特殊价格策略列表 */
export function listSpecialStrategies(supplierId) {
  return request.get(`/suppliers/${supplierId}/price-strategies`)
}

/** 添加特殊价格策略 */
export function addSpecialStrategy(supplierId, data) {
  return request.post(`/suppliers/${supplierId}/price-strategies`, data)
}

/** 编辑特殊价格策略 */
export function updateSpecialStrategy(strategyId, data) {
  return request.put(`/price-strategies/${strategyId}`, data)
}

/** 删除特殊价格策略 */
export function deleteSpecialStrategy(strategyId) {
  return request.delete(`/price-strategies/${strategyId}`)
}

// ============ 缓存策略 ============

/** 查询缓存策略 */
export function getCacheStrategy(supplierId) {
  return request.get(`/suppliers/${supplierId}/cache-strategy`)
}

/** 设置缓存策略 */
export function saveCacheStrategy(supplierId, data) {
  return request.put(`/suppliers/${supplierId}/cache-strategy`, data)
}

// ============ 推荐酒店 ============

/** 分页查询推荐酒店 */
export function listRecommendations(params) {
  return request.get('/recommendations', { params })
}

/** 添加推荐酒店 */
export function addRecommendation(data) {
  return request.post('/recommendations', data)
}

/** 批量删除推荐酒店 */
export function deleteRecommendations(ids) {
  return request.delete('/recommendations', { data: { ids } })
}

/** 调整推荐排序 */
export function updateRecommendSort(data) {
  return request.put('/recommendations/sort', data)
}

// ============ 操作日志 ============

/** 查询供应商操作日志 */
export function listSupplierLogs(supplierId, params) {
  return request.get(`/suppliers/${supplierId}/logs`, { params })
}

/** 查询全部操作日志 */
export function listAllLogs(params) {
  return request.get('/logs', { params })
}

// ============ 数据统计 ============

/** 统计概览 */
export function getStatsOverview() {
  return request.get('/stats/overview')
}

/** 搜索趋势 */
export function getSearchTrend(days = 7) {
  return request.get('/stats/search-trend', { params: { days } })
}

/** 供应商报价占比 */
export function getSupplierRatio() {
  return request.get('/stats/supplier-ratio')
}

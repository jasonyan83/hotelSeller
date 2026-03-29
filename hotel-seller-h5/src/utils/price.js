import dayjs from 'dayjs'

/**
 * 格式化价格
 */
export function formatPrice(price, options = {}) {
  const { showSymbol = true, decimal = 0 } = options
  if (price == null || isNaN(price)) return '--'
  const formatted = Number(price).toFixed(decimal).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  return showSymbol ? `¥${formatted}` : formatted
}

/**
 * 格式化积分
 */
export function formatPoints(points) {
  if (!points) return '0'
  return points.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

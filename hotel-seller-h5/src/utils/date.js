import dayjs from 'dayjs'

/**
 * 格式化日期显示 MM-DD
 */
export function formatDate(date, format = 'MM-DD') {
  if (!date) return ''
  return dayjs(date).format(format)
}

/**
 * 格式化星期几
 */
export function formatWeekday(date) {
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return weekdays[dayjs(date).day()]
}

/**
 * 计算两日期间隔天数
 */
export function diffDays(start, end) {
  return dayjs(end).diff(dayjs(start), 'day')
}

/**
 * 获取默认入住/离店日期
 */
export function getDefaultDates(isDomestic = true) {
  if (isDomestic) {
    return {
      checkIn: dayjs().add(1, 'day'),
      checkOut: dayjs().add(2, 'day'),
    }
  }
  return {
    checkIn: dayjs().add(15, 'day'),
    checkOut: dayjs().add(16, 'day'),
  }
}

import { defineStore } from 'pinia'
import dayjs from 'dayjs'
import { getDefaultDates } from '@/utils/date'
import { getFromStorage, setToStorage } from '@/utils/storage'

export const useSearchStore = defineStore('search', {
  state: () => {
    const defaults = getDefaultDates(true)
    return {
      destination: {
        code: 'SHA',
        name: '上海',
        nameEn: 'Shanghai',
        type: 'CITY',
      },
      checkInDate: defaults.checkIn.format('YYYY-MM-DD'),
      checkOutDate: defaults.checkOut.format('YYYY-MM-DD'),
      nightCount: 1,
      roomCount: 1,
      adultCount: 2,
      childCount: 0,
      childAges: [],
      keyword: '',
      keywordType: '',
      keywordRefId: '',
      starLevels: [],
      priceRange: { min: null, max: null },
      searchHistory: [],
      destHistory: [],
    }
  },

  getters: {
    isDomestic: (state) => state.destination.type !== 'INTERNATIONAL',
    checkInDisplay: (state) => dayjs(state.checkInDate).format('MM-DD'),
    checkOutDisplay: (state) => dayjs(state.checkOutDate).format('MM-DD'),
    checkInWeekday: (state) => {
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
      return weekdays[dayjs(state.checkInDate).day()]
    },
    checkOutWeekday: (state) => {
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
      return weekdays[dayjs(state.checkOutDate).day()]
    },
    guestSummary: (state) => {
      let text = `${state.roomCount}间, ${state.adultCount}成人`
      if (state.childCount > 0) text += ` ${state.childCount}儿童`
      else text += ` 0儿童/每间`
      return text
    }
  },

  actions: {
    setDestination(dest) {
      this.destination = dest
      this.keyword = ''
      this.starLevels = []
      this.priceRange = { min: null, max: null }
      this.addDestHistory(dest)
    },
    setDates(checkIn, checkOut) {
      this.checkInDate = dayjs(checkIn).format('YYYY-MM-DD')
      this.checkOutDate = dayjs(checkOut).format('YYYY-MM-DD')
      this.nightCount = dayjs(checkOut).diff(dayjs(checkIn), 'day')
    },
    restoreFromHistory() {
      const history = getFromStorage('lastSearch')
      if (!history) return
      if (dayjs(history.checkInDate).isBefore(dayjs(), 'day')) {
        const d = getDefaultDates(true)
        this.checkInDate = d.checkIn.format('YYYY-MM-DD')
        this.checkOutDate = d.checkOut.format('YYYY-MM-DD')
        this.nightCount = 1
      } else {
        this.checkInDate = history.checkInDate
        this.checkOutDate = history.checkOutDate
        this.nightCount = dayjs(history.checkOutDate).diff(dayjs(history.checkInDate), 'day')
      }
      if (history.destination) this.destination = history.destination
    },
    saveSearchState() {
      setToStorage('lastSearch', {
        destination: this.destination,
        checkInDate: this.checkInDate,
        checkOutDate: this.checkOutDate,
        keyword: this.keyword,
      })
    },
    addDestHistory(dest) {
      this.destHistory = [dest, ...this.destHistory.filter(d => d.code !== dest.code)].slice(0, 8)
      setToStorage('destHistory', this.destHistory)
    }
  }
})

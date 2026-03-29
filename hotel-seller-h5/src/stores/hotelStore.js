import { defineStore } from 'pinia'
import { mockAPI } from '@/mock/data'

export const useHotelStore = defineStore('hotel', {
  state: () => ({
    // 列表页
    hotelList: [],
    totalCount: 0,
    isLoading: false,
    hasMore: true,
    currentPage: 1,
    sortType: 'RECOMMEND',
    filters: {
      brands: [],
      facilities: [],
      districts: [],
      poiId: '',
    },
    // 详情页
    currentHotel: null,
    physicalRooms: [],
    expandedRoomId: null,
    showAllRooms: false,
    detailLoading: false,
  }),

  getters: {
    displayRooms: (state) => {
      if (state.showAllRooms) return state.physicalRooms
      return state.physicalRooms.slice(0, 8)
    },
    hasMoreRooms: (state) => state.physicalRooms.length > 8 && !state.showAllRooms,
    hiddenRoomCount: (state) => Math.max(0, state.physicalRooms.length - 8),
  },

  actions: {
    async searchHotels(params, isLoadMore = false) {
      this.isLoading = true
      try {
        const page = isLoadMore ? this.currentPage + 1 : 1
        const res = await mockAPI.searchHotels({
          ...params,
          pageNo: page,
          pageSize: 20,
        })
        if (isLoadMore) {
          this.hotelList.push(...res.data.hotels)
        } else {
          this.hotelList = res.data.hotels
        }
        this.totalCount = res.data.totalCount
        this.currentPage = page
        this.hasMore = this.hotelList.length < this.totalCount
      } finally {
        this.isLoading = false
      }
    },

    async fetchHotelDetail(hotelId) {
      this.detailLoading = true
      try {
        const res = await mockAPI.getHotelPrices(hotelId)
        this.currentHotel = res.data
        this.physicalRooms = res.data.physicalRooms || []
        // 默认展开第一个房型
        if (this.physicalRooms.length > 0) {
          this.expandedRoomId = this.physicalRooms[0].physicalRoomId
        }
      } finally {
        this.detailLoading = false
      }
    },

    toggleRoom(roomId) {
      this.expandedRoomId = this.expandedRoomId === roomId ? null : roomId
    },

    showAllRoomTypes() {
      this.showAllRooms = true
    },

    resetList() {
      this.hotelList = []
      this.totalCount = 0
      this.currentPage = 1
      this.hasMore = true
    }
  }
})

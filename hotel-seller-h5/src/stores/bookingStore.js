import { defineStore } from 'pinia'
import { mockAPI } from '@/mock/data'

export const useBookingStore = defineStore('booking', {
  state: () => ({
    selectedHotel: null,
    selectedRoom: null,
    selectedSellRoom: null,
    verifyResult: null,
    priceChanged: false,
    priceChangeMessage: '',
    // 表单
    guests: [{ lastName: '', firstName: '', lastNamePinyin: '', firstNamePinyin: '' }],
    contactLastName: '',
    contactFirstName: '',
    phone: '',
    phonePrefix: '+86',
    email: '',
    arrivalTime: '14:00',
    preferences: [],
    remark: '',
    invoiceInfo: null,
    couponId: null,
    usePoints: false,
    pointsAmount: 0,
    tripType: '',
    agreedToTerms: false,
  }),

  getters: {
    totalPrice: (state) => {
      if (state.verifyResult?.newTotalPrice) return state.verifyResult.newTotalPrice
      return state.selectedSellRoom?.priceDetail?.totalPrice || 0
    },
    averagePrice: (state) => {
      if (state.verifyResult?.newAveragePrice) return state.verifyResult.newAveragePrice
      return state.selectedSellRoom?.priceDetail?.averagePrice || 0
    },
  },

  actions: {
    setBookingInfo(hotel, room, sellRoom) {
      this.selectedHotel = hotel
      this.selectedRoom = room
      this.selectedSellRoom = sellRoom
      this.verifyResult = null
      this.priceChanged = false
    },

    async verifyPrice() {
      if (!this.selectedSellRoom) return
      const res = await mockAPI.verifyPrice({
        hotelId: this.selectedHotel?.hotelId,
        sellRoomId: this.selectedSellRoom.sellRoomId,
        originalTotalPrice: this.selectedSellRoom.priceDetail.totalPrice,
        originalAveragePrice: this.selectedSellRoom.priceDetail.averagePrice,
      })
      this.verifyResult = res.data
      this.priceChanged = res.data.priceChanged
      this.priceChangeMessage = res.data.message || ''
    },

    addGuest() {
      this.guests.push({ lastName: '', firstName: '', lastNamePinyin: '', firstNamePinyin: '' })
    },

    removeGuest(index) {
      if (this.guests.length > 1) {
        this.guests.splice(index, 1)
      }
    },

    resetForm() {
      this.guests = [{ lastName: '', firstName: '', lastNamePinyin: '', firstNamePinyin: '' }]
      this.contactLastName = ''
      this.contactFirstName = ''
      this.phone = ''
      this.email = ''
      this.arrivalTime = '14:00'
      this.preferences = []
      this.remark = ''
      this.agreedToTerms = false
    }
  }
})

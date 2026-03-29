// Mock 酒店数据，匹配设计文档和 UI 截图
const hotelImages = [
  'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=400&h=300&fit=crop',
]

const heroImages = [
  'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&h=400&fit=crop',
  'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=800&h=400&fit=crop',
  'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800&h=400&fit=crop',
]

export const hotCities = [
  { code: 'SHA', name: '上海', nameEn: 'Shanghai', isHot: true },
  { code: 'BJS', name: '北京', nameEn: 'Beijing', isHot: true },
  { code: 'KMG', name: '昆明', nameEn: 'Kunming', isHot: true },
  { code: 'SYX', name: '三亚', nameEn: 'Sanya', isHot: true },
  { code: 'SIA', name: '西安', nameEn: "Xi'an", isHot: true },
  { code: 'HGH', name: '杭州', nameEn: 'Hangzhou', isHot: true },
]

export const mockHotels = [
  {
    hotelId: 'H10001',
    hotelName: '上海龙柏饭店',
    hotelNameEn: 'Shanghai Longbai Hotel',
    starLevel: 'COMFORT',
    starCount: 3,
    rating: 4.55,
    reviewCount: 326,
    shortAddress: '近虹桥国际机场',
    fullAddress: '上海市闵行区黄桦路18号',
    latitude: 31.1857,
    longitude: 121.3753,
    distance: 2.1,
    distanceLabel: '距虹桥机场2.1公里',
    imageUrl: hotelImages[0],
    imageCount: 83,
    heroImage: heroImages[0],
    facilities: ['WIFI', 'PARKING', 'POOL'],
    promotionTags: ['DISCOUNT'],
    priceInfo: {
      startingPrice: 385,
      originalPrice: 420,
      currency: 'CNY',
      hasDiscount: true,
      discountAmount: 35,
      pointsPrice: 14600,
    },
    status: 'AVAILABLE',
    degraded: false,
    displayMode: null,
    openYear: 2005,
    renovateYear: 2020,
  },
  {
    hotelId: 'H10002',
    hotelName: '上海华凯华美达广场酒店',
    hotelNameEn: 'Ramada Plaza Shanghai Huakai',
    starLevel: 'HIGH',
    starCount: 5,
    rating: 4.35,
    reviewCount: 512,
    shortAddress: '近上海站',
    fullAddress: '上海市奉贤区月华路9号',
    latitude: 30.9178,
    longitude: 121.4740,
    distance: 5.3,
    distanceLabel: '距上海站5.3公里',
    imageUrl: hotelImages[1],
    imageCount: 126,
    heroImage: heroImages[1],
    facilities: ['WIFI', 'PARKING', 'GYM', 'RESTAURANT'],
    promotionTags: ['CASHBACK', 'DISCOUNT'],
    priceInfo: {
      startingPrice: 598,
      originalPrice: 680,
      currency: 'CNY',
      hasDiscount: true,
      discountAmount: 82,
      pointsPrice: 22720,
    },
    status: 'AVAILABLE',
    degraded: false,
    displayMode: null,
    openYear: 2007,
    renovateYear: 2021,
  },
  {
    hotelId: 'H10003',
    hotelName: '上海和平饭店',
    hotelNameEn: 'Fairmont Peace Hotel',
    starLevel: 'LUXURY',
    starCount: 5,
    rating: 4.70,
    reviewCount: 1024,
    shortAddress: '南京东路20号',
    fullAddress: '上海市黄浦区南京东路20号',
    latitude: 31.2414,
    longitude: 121.4900,
    distance: 0.5,
    distanceLabel: '距南京路步行街0.5公里',
    imageUrl: hotelImages[2],
    imageCount: 210,
    heroImage: heroImages[2],
    facilities: ['WIFI', 'PARKING', 'POOL', 'GYM', 'SPA', 'RESTAURANT'],
    promotionTags: ['EARLY_BOOKING'],
    priceInfo: {
      startingPrice: 3382,
      originalPrice: 3600,
      currency: 'CNY',
      hasDiscount: true,
      discountAmount: 218,
      pointsPrice: 128480,
    },
    status: 'AVAILABLE',
    degraded: false,
    displayMode: null,
    openYear: 1929,
    renovateYear: 2010,
  },
  {
    hotelId: 'H10004',
    hotelName: '上海外滩W酒店',
    hotelNameEn: 'W Shanghai - The Bund',
    starLevel: 'LUXURY',
    starCount: 5,
    rating: 4.62,
    reviewCount: 876,
    shortAddress: '外白渡桥北侧',
    fullAddress: '上海市虹口区东大名路66号',
    latitude: 31.2480,
    longitude: 121.4960,
    distance: 1.0,
    distanceLabel: '距外滩1.0公里',
    imageUrl: hotelImages[3],
    imageCount: 195,
    heroImage: heroImages[0],
    facilities: ['WIFI', 'PARKING', 'POOL', 'GYM', 'SPA', 'BAR'],
    promotionTags: ['DISCOUNT', 'GIFT_PACK'],
    priceInfo: {
      startingPrice: 2180,
      originalPrice: 2400,
      currency: 'CNY',
      hasDiscount: true,
      discountAmount: 220,
      pointsPrice: 82800,
    },
    status: 'AVAILABLE',
    degraded: false,
    displayMode: null,
    openYear: 2010,
    renovateYear: 2023,
  },
  {
    hotelId: 'H10005',
    hotelName: '上海迪士尼乐园酒店',
    hotelNameEn: 'Shanghai Disneyland Hotel',
    starLevel: 'LUXURY',
    starCount: 5,
    rating: 4.80,
    reviewCount: 2048,
    shortAddress: '迪士尼度假区内',
    fullAddress: '上海市浦东新区申迪北路799号',
    latitude: 31.1440,
    longitude: 121.6590,
    distance: 0.3,
    distanceLabel: '距迪士尼0.3公里',
    imageUrl: hotelImages[4],
    imageCount: 302,
    heroImage: heroImages[1],
    facilities: ['WIFI', 'PARKING', 'POOL', 'GYM', 'SPA', 'RESTAURANT', 'KIDS'],
    promotionTags: ['STAY_N_NIGHTS'],
    priceInfo: {
      startingPrice: 2688,
      originalPrice: 2888,
      currency: 'CNY',
      hasDiscount: true,
      discountAmount: 200,
      pointsPrice: 102200,
    },
    status: 'AVAILABLE',
    degraded: false,
    displayMode: null,
    openYear: 2016,
    renovateYear: 2022,
  },
  {
    hotelId: 'H10006',
    hotelName: '上海浦东丽思卡尔顿酒店',
    hotelNameEn: 'The Ritz-Carlton Shanghai, Pudong',
    starLevel: 'LUXURY',
    starCount: 5,
    rating: 4.75,
    reviewCount: 986,
    shortAddress: '陆家嘴世纪大道',
    fullAddress: '上海市浦东新区世纪大道8号',
    latitude: 31.2357,
    longitude: 121.5124,
    distance: 2.0,
    distanceLabel: '距陆家嘴2.0公里',
    imageUrl: hotelImages[5],
    imageCount: 180,
    heroImage: heroImages[2],
    facilities: ['WIFI', 'PARKING', 'POOL', 'GYM', 'SPA', 'RESTAURANT', 'BAR'],
    promotionTags: ['CASHBACK'],
    priceInfo: {
      startingPrice: 2980,
      originalPrice: 3200,
      currency: 'CNY',
      hasDiscount: true,
      discountAmount: 220,
      pointsPrice: 113200,
    },
    status: 'AVAILABLE',
    degraded: false,
    displayMode: null,
    openYear: 2012,
    renovateYear: 2021,
  },
  // 降级数据示例
  {
    hotelId: 'H10007',
    hotelName: '上海虹桥锦江大酒店',
    hotelNameEn: 'Jin Jiang Hotel Shanghai',
    starLevel: 'HIGH',
    starCount: 4,
    rating: 4.20,
    reviewCount: 256,
    shortAddress: '虹桥商务区',
    fullAddress: '上海市长宁区虹桥路888号',
    latitude: 31.1960,
    longitude: 121.4080,
    distance: 3.2,
    distanceLabel: '距虹桥商务区3.2公里',
    imageUrl: hotelImages[6],
    imageCount: 68,
    heroImage: heroImages[0],
    facilities: ['WIFI', 'PARKING', 'RESTAURANT'],
    promotionTags: [],
    priceInfo: {
      startingPrice: 520,
      originalPrice: 520,
      currency: 'CNY',
      hasDiscount: false,
      discountAmount: 0,
      pointsPrice: 19760,
    },
    status: 'AVAILABLE',
    degraded: true,
    displayMode: 'NORMAL_REF',
    openYear: 2003,
    renovateYear: 2018,
  },
  {
    hotelId: 'H10008',
    hotelName: '上海静安洲际酒店',
    hotelNameEn: 'InterContinental Shanghai Jing An',
    starLevel: 'LUXURY',
    starCount: 5,
    rating: 4.50,
    reviewCount: 768,
    shortAddress: '静安寺商圈',
    fullAddress: '上海市静安区华山路585号',
    latitude: 31.2227,
    longitude: 121.4435,
    distance: 1.5,
    distanceLabel: '距静安寺1.5公里',
    imageUrl: hotelImages[7],
    imageCount: 145,
    heroImage: heroImages[1],
    facilities: ['WIFI', 'PARKING', 'POOL', 'GYM', 'SPA'],
    promotionTags: ['DISCOUNT'],
    priceInfo: {
      startingPrice: 1580,
      originalPrice: 1800,
      currency: 'CNY',
      hasDiscount: true,
      discountAmount: 220,
      pointsPrice: 60000,
    },
    status: 'AVAILABLE',
    degraded: true,
    displayMode: 'APPROX_REF',
    openYear: 2008,
    renovateYear: 2019,
  },
]

// 物理房型 + 产品房型
export const mockRoomTypes = {
  H10002: [
    {
      physicalRoomId: 'PR001',
      roomName: '豪华大床房',
      roomImages: [hotelImages[1], hotelImages[2]],
      area: 38,
      bedType: '1张双人床(1.8米)',
      maxOccupancy: 2,
      hasWifi: true,
      hasWindow: true,
      isSmokeFree: true,
      floor: '8-15层',
      isMapped: true,
      startingPrice: 598,
      sellRooms: [
        {
          sellRoomId: 'SR001',
          sellRoomName: '大床',
          supplierTag: '商旅',
          supplierId: 'SUP001',
          supplierName: 'Booking.com',
          paymentType: 'PREPAY',
          guestType: 'DOMESTIC',
          mealInfo: { summary: '含早', breakfastCount: 2 },
          priceDetail: {
            averagePrice: 598,
            totalPrice: 1196,
            currency: 'CNY',
            dailyPrices: [
              { date: '2026-04-01', sellingPrice: 598 },
              { date: '2026-04-02', sellingPrice: 598 },
            ]
          },
          promotions: [
            { type: 'DISCOUNT', tag: '减', title: '立减82元' },
          ],
          cancelPolicy: {
            cancelType: 'FREE_CANCEL',
            label: '限时取消',
            description: '2026年04月05日 0点前可免费取消'
          },
          tags: ['酒店优享', '支持积分抵扣'],
          availability: 'IMMEDIATELY',
          instantConfirm: true,
          remainingRooms: 5,
          status: 'AVAILABLE'
        },
        {
          sellRoomId: 'SR002',
          sellRoomName: '大床',
          supplierTag: '直营保障',
          supplierId: 'SUP002',
          supplierName: '侠客',
          paymentType: 'PREPAY',
          guestType: 'DOMESTIC',
          mealInfo: { summary: '含早', breakfastCount: 2 },
          priceDetail: {
            averagePrice: 630,
            totalPrice: 1260,
            currency: 'CNY',
            dailyPrices: [
              { date: '2026-04-01', sellingPrice: 630 },
              { date: '2026-04-02', sellingPrice: 630 },
            ]
          },
          promotions: [],
          cancelPolicy: {
            cancelType: 'FREE_CANCEL',
            label: '限时取消',
            description: '2026年04月02日 0点前可免费取消'
          },
          tags: ['酒店优享', '支持积分抵扣'],
          availability: 'IMMEDIATELY',
          instantConfirm: true,
          remainingRooms: 3,
          status: 'AVAILABLE'
        }
      ]
    },
    {
      physicalRoomId: 'PR002',
      roomName: '豪华双床房',
      roomImages: [hotelImages[3], hotelImages[4]],
      area: 42,
      bedType: '2张单人床(1.2米)',
      maxOccupancy: 2,
      hasWifi: true,
      hasWindow: true,
      isSmokeFree: true,
      floor: '8-15层',
      isMapped: true,
      startingPrice: 658,
      sellRooms: [
        {
          sellRoomId: 'SR003',
          sellRoomName: '双床',
          supplierTag: '商旅',
          supplierId: 'SUP001',
          supplierName: 'Booking.com',
          paymentType: 'PREPAY',
          guestType: 'DOMESTIC',
          mealInfo: { summary: '含早', breakfastCount: 2 },
          priceDetail: {
            averagePrice: 658,
            totalPrice: 1316,
            currency: 'CNY',
            dailyPrices: [
              { date: '2026-04-01', sellingPrice: 658 },
              { date: '2026-04-02', sellingPrice: 658 },
            ]
          },
          promotions: [
            { type: 'CASHBACK', tag: '返', title: '返现50元' },
          ],
          cancelPolicy: {
            cancelType: 'NON_REFUNDABLE',
            label: '不可取消',
            description: '预订后不可取消/变更'
          },
          tags: ['酒店优享'],
          availability: 'IMMEDIATELY',
          instantConfirm: true,
          remainingRooms: 2,
          status: 'AVAILABLE'
        }
      ]
    },
    {
      physicalRoomId: 'PR003',
      roomName: '行政套房',
      roomImages: [hotelImages[5], hotelImages[6]],
      area: 65,
      bedType: '1张特大床(2.0米)',
      maxOccupancy: 3,
      hasWifi: true,
      hasWindow: true,
      isSmokeFree: true,
      floor: '18-22层',
      isMapped: true,
      startingPrice: 1280,
      sellRooms: [
        {
          sellRoomId: 'SR004',
          sellRoomName: '行政套房-含双早',
          supplierTag: '商旅',
          supplierId: 'SUP001',
          supplierName: 'Booking.com',
          paymentType: 'PREPAY',
          guestType: 'ALL',
          mealInfo: { summary: '双早', breakfastCount: 2 },
          priceDetail: {
            averagePrice: 1280,
            totalPrice: 2560,
            currency: 'CNY',
            dailyPrices: [
              { date: '2026-04-01', sellingPrice: 1280 },
              { date: '2026-04-02', sellingPrice: 1280 },
            ]
          },
          promotions: [
            { type: 'GIFT_PACK', tag: '惠', title: '含行政酒廊礼遇' },
          ],
          cancelPolicy: {
            cancelType: 'FREE_CANCEL',
            label: '可免费取消',
            description: '2026年03月31日 18:00前可免费取消'
          },
          tags: ['酒店优享', '支持积分抵扣'],
          availability: 'IMMEDIATELY',
          instantConfirm: true,
          remainingRooms: 1,
          status: 'AVAILABLE'
        }
      ]
    },
  ]
}

// 模拟延迟
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms))

// Mock API
export const mockAPI = {
  // 搜索酒店列表
  async searchHotels(params) {
    await delay(800)
    const { pageNo = 1, pageSize = 20 } = params
    const start = (pageNo - 1) * pageSize
    const list = mockHotels.slice(start, start + pageSize)
    return {
      code: 200,
      data: {
        totalCount: mockHotels.length,
        destinationName: params.destinationName || '上海',
        hotels: list,
      }
    }
  },

  // 酒店详情报价
  async getHotelPrices(hotelId) {
    await delay(600)
    const hotel = mockHotels.find(h => h.hotelId === hotelId) || mockHotels[1]
    const rooms = mockRoomTypes[hotelId] || mockRoomTypes['H10002']
    return {
      code: 200,
      data: {
        ...hotel,
        physicalRooms: rooms,
      }
    }
  },

  // 验价
  async verifyPrice(params) {
    await delay(500)
    const priceChanged = Math.random() > 0.7
    const diff = priceChanged ? Math.floor(Math.random() * 30) - 10 : 0
    return {
      code: 200,
      data: {
        available: true,
        priceChanged,
        priceChangeDirection: diff > 0 ? 'UP' : 'DOWN',
        averagePriceDiff: Math.abs(diff),
        newTotalPrice: params.originalTotalPrice + diff * 2,
        newAveragePrice: params.originalAveragePrice + diff,
        originalTotalPrice: params.originalTotalPrice,
        message: priceChanged
          ? `房价每晚${diff > 0 ? '上涨' : '下降'}了¥${Math.abs(diff)}，已为您更新价格。`
          : null,
      }
    }
  },

  // Suggest
  async suggest(keyword) {
    await delay(200)
    const results = [
      { type: 'HOTEL', id: 'H10001', name: '上海龙柏饭店', parentName: '上海, 上海市' },
      { type: 'HOTEL', id: 'H10002', name: '上海华凯华美达广场酒店', parentName: '上海, 奉贤区' },
      { type: 'BRAND', id: 'B001', name: '华美达', parentName: '温德姆集团' },
    ].filter(r => r.name.includes(keyword))
    return { code: 200, data: results }
  },

  // 热门目的地
  async getHotDestinations() {
    await delay(200)
    return { code: 200, data: hotCities }
  },

  // 热门地标
  async getHotLandmarks(cityCode) {
    await delay(200)
    const landmarks = {
      SHA: ['南京路步行街', '上海迪士尼', '外滩', '陆家嘴', '中山公园', '虹桥机场'],
      BJS: ['天安门', '故宫', '国贸CBD', '三里屯', '王府井'],
    }
    return { code: 200, data: landmarks[cityCode] || landmarks.SHA }
  }
}

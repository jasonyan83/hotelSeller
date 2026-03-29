const STORAGE_PREFIX = 'hotel_seller_'

export function getFromStorage(key) {
  try {
    const raw = localStorage.getItem(STORAGE_PREFIX + key)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function setToStorage(key, value) {
  try {
    localStorage.setItem(STORAGE_PREFIX + key, JSON.stringify(value))
  } catch {
    // storage full or unavailable
  }
}

export function removeFromStorage(key) {
  localStorage.removeItem(STORAGE_PREFIX + key)
}

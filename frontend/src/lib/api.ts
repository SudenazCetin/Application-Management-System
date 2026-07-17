import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use((config) => {
  const requestUrl = config.url ?? ''
  const isAuthRequest = requestUrl.includes('/api/auth/')
  const token = localStorage.getItem('ams_token')

  if (token && !isAuthRequest) {
    config.headers.Authorization = `Bearer ${token}`
  } else if (isAuthRequest && config.headers?.Authorization) {
    delete config.headers.Authorization
  }

  return config
})

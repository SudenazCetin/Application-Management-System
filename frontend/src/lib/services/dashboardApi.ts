import { api } from '../api'
import type { DashboardStats } from '../../types'

export async function getDashboardStats() {
  const { data } = await api.get<DashboardStats>('/api/dashboard')
  return data
}

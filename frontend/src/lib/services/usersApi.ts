import { api } from '../api'
import type { UserItem } from '../../types'

export type UserPayload = Omit<UserItem, 'id'>

export async function getUsers() {
  const { data } = await api.get<UserItem[]>('/api/users')
  return data
}

export async function createUser(payload: UserPayload) {
  const { data } = await api.post<UserItem>('/api/users', payload)
  return data
}

export async function updateUser(id: number, payload: UserPayload) {
  const { data } = await api.put<UserItem>(`/api/users/${id}`, payload)
  return data
}

export async function deleteUser(id: number) {
  await api.delete(`/api/users/${id}`)
}

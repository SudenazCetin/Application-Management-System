import { api } from '../api'
import type { FormTypeItem } from '../../types'

export type FormTypePayload = Omit<FormTypeItem, 'id'>

export async function getFormTypes() {
  const { data } = await api.get<FormTypeItem[]>('/api/form-types')
  return data
}

export async function createFormType(payload: FormTypePayload) {
  const { data } = await api.post<FormTypeItem>('/api/form-types', payload)
  return data
}

export async function updateFormType(id: number, payload: FormTypePayload) {
  const { data } = await api.put<FormTypeItem>(`/api/form-types/${id}`, payload)
  return data
}

export async function deleteFormType(id: number) {
  await api.delete(`/api/form-types/${id}`)
}

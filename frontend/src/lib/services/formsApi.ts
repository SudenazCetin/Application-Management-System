import { api } from '../api'
import type { ApplicationFormItem } from '../../types'

interface ApplicationFormsPageResponse {
  content: ApplicationFormItem[]
}

export interface ApplicationFormPayload {
  title: string
  description: string
  status: ApplicationFormItem['status']
  applicationDate: string
  userId: number
  formTypeId: number
}

export async function getApplicationForms() {
  const { data } = await api.get<ApplicationFormsPageResponse>('/api/forms', {
    params: {
      page: 0,
      size: 1000,
      sort: 'applicationDate,desc',
    },
  })

  return data.content
}

export async function createApplicationForm(payload: ApplicationFormPayload) {
  const { data } = await api.post<ApplicationFormItem>('/api/forms', payload)
  return data
}

export async function updateApplicationForm(id: number, payload: ApplicationFormPayload) {
  const { data } = await api.put<ApplicationFormItem>(`/api/forms/${id}`, payload)
  return data
}

export async function deleteApplicationForm(id: number) {
  await api.delete(`/api/forms/${id}`)
}

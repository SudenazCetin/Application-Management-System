import { api } from '../api'
import type { AttachmentItem } from '../../types'

export interface AttachmentPayload {
  originalName: string
  storedFileName: string
  filePath: string
  fileType: string
  fileSize: number
  uploadDate: string
  applicationFormId: number
}

export async function getAttachments() {
  const { data } = await api.get<AttachmentItem[]>('/api/attachments')
  return data
}

export async function createAttachment(payload: AttachmentPayload) {
  const { data } = await api.post<AttachmentItem>('/api/attachments', payload)
  return data
}

export async function updateAttachment(id: number, payload: AttachmentPayload) {
  const { data } = await api.put<AttachmentItem>(`/api/attachments/${id}`, payload)
  return data
}

export async function deleteAttachment(id: number) {
  await api.delete(`/api/attachments/${id}`)
}

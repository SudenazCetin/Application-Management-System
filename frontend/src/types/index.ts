export type Role = 'ADMIN' | 'PERSONNEL'

export interface AuthUser {
  email: string
  role: Role
  token: string
  fullName?: string
}

export interface ApiListState<T> {
  items: T[]
  loading: boolean
  error: string | null
}

export interface UserItem {
  id: number
  name: string
  surname: string
  email: string
  role: Role
}

export interface FormTypeItem {
  id: number
  name: string
  description: string
}

export interface ApplicationFormItem {
  id: number
  title: string
  description: string
  status: 'NEW' | 'IN_REVIEW' | 'APPROVED' | 'REJECTED' | 'CANCELLED'
  applicationDate: string
  userId: number
  formTypeId: number
}

export interface AttachmentItem {
  id: number
  fileName: string
  filePath: string
  fileType: string
  uploadedDate: string
  applicationFormId: number
}

export interface DashboardStats {
  totalApplications: number
  pendingApplications: number
  approvedApplications: number
  rejectedApplications: number
  todayApplications: number
  recentApplications: ApplicationFormItem[]
}

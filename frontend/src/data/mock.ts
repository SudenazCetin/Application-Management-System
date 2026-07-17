import type { ApplicationFormItem, AttachmentItem, FormTypeItem, UserItem } from '../types'

export const usersMock: UserItem[] = [
  { id: 1, name: 'Aylin', surname: 'Demir', email: 'aylin@company.com', role: 'ADMIN' },
  { id: 2, name: 'Mert', surname: 'Kaya', email: 'mert@company.com', role: 'PERSONNEL' },
  { id: 3, name: 'Zeynep', surname: 'Aras', email: 'zeynep@company.com', role: 'PERSONNEL' },
]

export const formTypesMock: FormTypeItem[] = [
  { id: 1, name: 'Leave Request', description: 'Annual and special leave workflows' },
  { id: 2, name: 'Expense Claim', description: 'Travel and operation expense approvals' },
]

export const formsMock: ApplicationFormItem[] = [
  {
    id: 101,
    title: 'Q3 Budget Form',
    description: 'Budget increase proposal for marketing ops',
    status: 'IN_REVIEW',
    applicationDate: '2026-07-15T10:00:00',
    userId: 1,
    formTypeId: 2,
  },
  {
    id: 102,
    title: 'Laptop Procurement',
    description: 'Replacement request for engineering team',
    status: 'APPROVED',
    applicationDate: '2026-07-16T14:20:00',
    userId: 2,
    formTypeId: 1,
  },
]

export const attachmentsMock: AttachmentItem[] = [
  {
    id: 900,
    fileName: 'budget-justification.pdf',
    filePath: '/uploads/budget-justification.pdf',
    fileType: 'application/pdf',
    uploadedDate: '2026-07-16T10:10:00',
    applicationFormId: 101,
  },
  {
    id: 901,
    fileName: 'quote-vendor.xlsx',
    filePath: '/uploads/quote-vendor.xlsx',
    fileType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    uploadedDate: '2026-07-16T10:12:00',
    applicationFormId: 102,
  },
]

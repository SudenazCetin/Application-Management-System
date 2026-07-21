import { useEffect, useMemo, useState, type FormEvent } from 'react'
import { ConfirmDialog } from '../../components/ui/ConfirmDialog'
import { DataTable } from '../../components/ui/DataTable'
import { Modal } from '../../components/ui/Modal'
import { PageHeader } from '../../components/ui/PageHeader'
import { Pagination } from '../../components/ui/Pagination'
import { Button } from '../../components/ui/Button'
import { Input } from '../../components/ui/Input'
import { EmptyState } from '../../components/states/EmptyState'
import { ErrorState } from '../../components/states/ErrorState'
import { LoadingScreen } from '../../components/states/LoadingScreen'
import { getErrorMessage } from '../../lib/httpError'
import {
  createApplicationForm,
  deleteApplicationForm,
  getApplicationForms,
  updateApplicationForm,
  type ApplicationFormPayload,
} from '../../lib/services/formsApi'
import { toast } from 'sonner'
import type { ApplicationFormItem } from '../../types'

const PAGE_SIZE = 5

// Backend'deki ALLOWED_TRANSITIONS kurallariyla birebir eslesir (ApplicationFormServiceImpl).
// APPROVED, REJECTED ve CANCELLED nihai (terminal) durumlardir; bu durumlara ulasan
// bir basvuru bir daha degistirilemez.
const ALLOWED_TRANSITIONS: Record<ApplicationFormItem['status'], ApplicationFormItem['status'][]> = {
  NEW: ['NEW', 'IN_REVIEW', 'CANCELLED'],
  IN_REVIEW: ['IN_REVIEW', 'APPROVED', 'REJECTED', 'CANCELLED'],
  APPROVED: ['APPROVED'],
  REJECTED: ['REJECTED'],
  CANCELLED: ['CANCELLED'],
}

const STATUS_LABELS: Record<ApplicationFormItem['status'], string> = {
  NEW: 'Yeni',
  IN_REVIEW: 'Incelemede',
  APPROVED: 'Onaylandi',
  REJECTED: 'Reddedildi',
  CANCELLED: 'Iptal Edildi',
}

const STATUS_STYLES: Record<ApplicationFormItem['status'], string> = {
  NEW: 'bg-blue-500/15 text-blue-300',
  IN_REVIEW: 'bg-amber-500/15 text-amber-300',
  APPROVED: 'bg-emerald-500/15 text-emerald-300',
  REJECTED: 'bg-red-500/15 text-red-300',
  CANCELLED: 'bg-white/10 text-app-muted',
}

const TERMINAL_STATUSES: ApplicationFormItem['status'][] = ['APPROVED', 'REJECTED', 'CANCELLED']

export function ApplicationFormsPage() {
  const [items, setItems] = useState<ApplicationFormItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [page, setPage] = useState(1)
  const [openModal, setOpenModal] = useState(false)
  const [openConfirm, setOpenConfirm] = useState(false)
  const [editingItem, setEditingItem] = useState<ApplicationFormItem | null>(null)
  const [selectedItem, setSelectedItem] = useState<ApplicationFormItem | null>(null)
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [status, setStatus] = useState<ApplicationFormItem['status']>('NEW')
  const [applicationDate, setApplicationDate] = useState(new Date().toISOString().slice(0, 16))
  const [userId, setUserId] = useState('')
  const [formTypeId, setFormTypeId] = useState('')

  async function loadForms() {
    setLoading(true)
    setError(null)

    try {
      const forms = await getApplicationForms()
      setItems(forms)
    } catch (err) {
      setError(getErrorMessage(err, 'Application forms could not be loaded'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadForms()
  }, [])

  function openCreateModal() {
    setEditingItem(null)
    setTitle('')
    setDescription('')
    setStatus('NEW')
    setApplicationDate(new Date().toISOString().slice(0, 16))
    setUserId('')
    setFormTypeId('')
    setOpenModal(true)
  }

  function openEditModal(item: ApplicationFormItem) {
    setEditingItem(item)
    setTitle(item.title)
    setDescription(item.description)
    setStatus(item.status)
    setApplicationDate(item.applicationDate.slice(0, 16))
    setUserId(String(item.userId))
    setFormTypeId(String(item.formTypeId))
    setOpenModal(true)
  }

  async function submitForm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const payload: ApplicationFormPayload = {
      title,
      description,
      status,
      applicationDate: new Date(applicationDate).toISOString(),
      userId: Number(userId),
      formTypeId: Number(formTypeId),
    }

    try {
      if (editingItem) {
        const updated = await updateApplicationForm(editingItem.id, payload)
        setItems((prev) => prev.map((item) => (item.id === updated.id ? updated : item)))
        toast.success('Application form updated')
      } else {
        const created = await createApplicationForm(payload)
        setItems((prev) => [created, ...prev])
        toast.success('Application form created')
      }

      setOpenModal(false)
    } catch (err) {
      toast.error(getErrorMessage(err, 'Application form could not be saved'))
    }
  }

  async function confirmDelete() {
    if (!selectedItem) {
      return
    }

    try {
      await deleteApplicationForm(selectedItem.id)
      setItems((prev) => prev.filter((item) => item.id !== selectedItem.id))
      setOpenConfirm(false)
      setSelectedItem(null)
      toast.success('Application form deleted')
    } catch (err) {
      toast.error(getErrorMessage(err, 'Application form could not be deleted'))
    }
  }

  const totalPages = Math.max(1, Math.ceil(items.length / PAGE_SIZE))
  const paged = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE
    return items.slice(start, start + PAGE_SIZE)
  }, [items, page])

  const isEditingFinalized = Boolean(editingItem && TERMINAL_STATUSES.includes(editingItem.status))
  const allowedStatusOptions = editingItem ? ALLOWED_TRANSITIONS[editingItem.status] : (['NEW'] as ApplicationFormItem['status'][])

  const columns = [
    { key: 'title', header: 'Title', render: (item: ApplicationFormItem) => item.title },
    {
      key: 'status',
      header: 'Status',
      render: (item: ApplicationFormItem) => (
        <span className={`rounded-full px-2 py-1 text-xs font-medium ${STATUS_STYLES[item.status]}`}>
          {STATUS_LABELS[item.status]}
        </span>
      ),
    },
    {
      key: 'date',
      header: 'Application Date',
      render: (item: ApplicationFormItem) => new Date(item.applicationDate).toLocaleDateString(),
    },
    { key: 'userId', header: 'User ID', render: (item: ApplicationFormItem) => item.userId },
    { key: 'formTypeId', header: 'Form Type ID', render: (item: ApplicationFormItem) => item.formTypeId },
    {
      key: 'actions',
      header: 'Actions',
      render: (item: ApplicationFormItem) => (
        <div className="flex gap-2">
          <Button
            type="button"
            variant="ghost"
            className="px-3 py-1 text-xs"
            disabled={TERMINAL_STATUSES.includes(item.status)}
            title={TERMINAL_STATUSES.includes(item.status) ? 'Sonuclanmis basvurular duzenlenemez' : undefined}
            onClick={() => openEditModal(item)}
          >
            Edit
          </Button>
          <Button
            type="button"
            variant="danger"
            className="px-3 py-1 text-xs"
            onClick={() => {
              setSelectedItem(item)
              setOpenConfirm(true)
            }}
          >
            Delete
          </Button>
        </div>
      ),
    },
  ]

  if (loading) {
    return <LoadingScreen message="Loading application forms..." />
  }

  if (error) {
    return <ErrorState title="Forms load failed" description={error} onRetry={() => void loadForms()} />
  }

  return (
    <section className="space-y-4">
      <PageHeader
        title="Application Forms"
        subtitle="Track and process all operational forms"
        actions={<Button onClick={openCreateModal}>Create Form</Button>}
      />

      {items.length === 0 ? (
        <EmptyState title="No forms yet" description="Create your first application form." />
      ) : (
        <DataTable columns={columns} data={paged} />
      )}
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />

      <Modal
        open={openModal}
        title={editingItem ? 'Update Application Form' : 'Create Application Form'}
        onClose={() => setOpenModal(false)}
      >
        <form className="space-y-3" onSubmit={submitForm}>
          <Input label="Title" value={title} onChange={(event) => setTitle(event.target.value)} required />
          <Input
            label="Description"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
            required
          />
          <label className="flex flex-col gap-1 text-sm">
            <span className="text-app-muted">Status</span>
            <select
              className="rounded-lg border border-white/10 bg-white/[0.04] px-3 py-2 disabled:cursor-not-allowed disabled:opacity-60"
              value={status}
              disabled={!editingItem || isEditingFinalized}
              onChange={(event) => setStatus(event.target.value as ApplicationFormItem['status'])}
            >
              {allowedStatusOptions.map((option) => (
                <option key={option} value={option}>
                  {STATUS_LABELS[option]}
                </option>
              ))}
            </select>
            {isEditingFinalized ? (
              <span className="text-xs text-app-danger">
                Bu basvuru {STATUS_LABELS[editingItem!.status]} durumunda sonuclanmistir ve degistirilemez.
              </span>
            ) : null}
          </label>
          <Input
            label="Application Date"
            type="datetime-local"
            value={applicationDate}
            onChange={(event) => setApplicationDate(event.target.value)}
            disabled={isEditingFinalized}
            required
          />
          <Input
            label="User ID"
            value={userId}
            onChange={(event) => setUserId(event.target.value)}
            disabled={isEditingFinalized}
            required
          />
          <Input
            label="Form Type ID"
            value={formTypeId}
            onChange={(event) => setFormTypeId(event.target.value)}
            disabled={isEditingFinalized}
            required
          />
          <div className="flex justify-end">
            <Button type="submit" disabled={isEditingFinalized}>
              {editingItem ? 'Update' : 'Save'}
            </Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={openConfirm}
        title="Delete form"
        description={`This action will remove ${selectedItem?.title ?? 'selected form'}.`}
        onClose={() => setOpenConfirm(false)}
        onConfirm={() => void confirmDelete()}
      />
    </section>
  )
}

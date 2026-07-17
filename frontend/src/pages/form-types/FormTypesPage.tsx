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
  createFormType,
  deleteFormType,
  getFormTypes,
  updateFormType,
  type FormTypePayload,
} from '../../lib/services/formTypesApi'
import { toast } from 'sonner'
import type { FormTypeItem } from '../../types'

const PAGE_SIZE = 5

export function FormTypesPage() {
  const [items, setItems] = useState<FormTypeItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [page, setPage] = useState(1)
  const [openModal, setOpenModal] = useState(false)
  const [openConfirm, setOpenConfirm] = useState(false)
  const [editingItem, setEditingItem] = useState<FormTypeItem | null>(null)
  const [selectedItem, setSelectedItem] = useState<FormTypeItem | null>(null)
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')

  async function loadFormTypes() {
    setLoading(true)
    setError(null)

    try {
      const data = await getFormTypes()
      setItems(data)
    } catch (err) {
      setError(getErrorMessage(err, 'Form type list could not be loaded'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadFormTypes()
  }, [])

  function openCreateModal() {
    setEditingItem(null)
    setName('')
    setDescription('')
    setOpenModal(true)
  }

  function openEditModal(item: FormTypeItem) {
    setEditingItem(item)
    setName(item.name)
    setDescription(item.description)
    setOpenModal(true)
  }

  async function submitForm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const payload: FormTypePayload = { name, description }

    try {
      if (editingItem) {
        const updated = await updateFormType(editingItem.id, payload)
        setItems((prev) => prev.map((item) => (item.id === updated.id ? updated : item)))
        toast.success('Form type updated')
      } else {
        const created = await createFormType(payload)
        setItems((prev) => [created, ...prev])
        toast.success('Form type created')
      }

      setOpenModal(false)
    } catch (err) {
      toast.error(getErrorMessage(err, 'Form type could not be saved'))
    }
  }

  async function confirmDelete() {
    if (!selectedItem) {
      return
    }

    try {
      await deleteFormType(selectedItem.id)
      setItems((prev) => prev.filter((item) => item.id !== selectedItem.id))
      setOpenConfirm(false)
      setSelectedItem(null)
      toast.success('Form type deleted')
    } catch (err) {
      toast.error(getErrorMessage(err, 'Form type could not be deleted'))
    }
  }

  const totalPages = Math.max(1, Math.ceil(items.length / PAGE_SIZE))
  const paged = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE
    return items.slice(start, start + PAGE_SIZE)
  }, [items, page])

  const columns = [
    { key: 'name', header: 'Type', render: (item: FormTypeItem) => item.name },
    { key: 'description', header: 'Description', render: (item: FormTypeItem) => item.description },
    {
      key: 'actions',
      header: 'Actions',
      render: (item: FormTypeItem) => (
        <div className="flex gap-2">
          <Button type="button" variant="ghost" className="px-3 py-1 text-xs" onClick={() => openEditModal(item)}>
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
    return <LoadingScreen message="Loading form types..." />
  }

  if (error) {
    return <ErrorState title="Form types load failed" description={error} onRetry={() => void loadFormTypes()} />
  }

  return (
    <section className="space-y-4">
      <PageHeader
        title="Form Types"
        subtitle="Create and maintain reusable form categories"
        actions={<Button onClick={openCreateModal}>Create Type</Button>}
      />

      {items.length === 0 ? (
        <EmptyState title="No form type yet" description="Add your first form category." />
      ) : (
        <DataTable columns={columns} data={paged} />
      )}
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />

      <Modal open={openModal} title={editingItem ? 'Update Form Type' : 'Create Form Type'} onClose={() => setOpenModal(false)}>
        <form className="space-y-3" onSubmit={submitForm}>
          <Input label="Type Name" value={name} onChange={(event) => setName(event.target.value)} required />
          <Input label="Description" value={description} onChange={(event) => setDescription(event.target.value)} required />
          <div className="flex justify-end">
            <Button type="submit">
              {editingItem ? 'Update' : 'Save'}
            </Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={openConfirm}
        title="Delete form type"
        description={`This action will remove ${selectedItem?.name ?? 'selected form type'}.`}
        onClose={() => setOpenConfirm(false)}
        onConfirm={() => void confirmDelete()}
      />
    </section>
  )
}

import { useEffect, useMemo, useState, type FormEvent } from 'react'
import { FileUpload } from '../../components/forms/FileUpload'
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
  createAttachment,
  deleteAttachment,
  getAttachments,
  updateAttachment,
  type AttachmentPayload,
} from '../../lib/services/attachmentsApi'
import { toast } from 'sonner'
import type { AttachmentItem } from '../../types'

const PAGE_SIZE = 5

export function AttachmentsPage() {
  const [items, setItems] = useState<AttachmentItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [page, setPage] = useState(1)
  const [openModal, setOpenModal] = useState(false)
  const [openConfirm, setOpenConfirm] = useState(false)
  const [editingItem, setEditingItem] = useState<AttachmentItem | null>(null)
  const [selectedItem, setSelectedItem] = useState<AttachmentItem | null>(null)
  const [selectedFileName, setSelectedFileName] = useState('No file selected')
  const [storedFileName, setStoredFileName] = useState('')
  const [filePath, setFilePath] = useState('')
  const [fileType, setFileType] = useState('')
  const [fileSize, setFileSize] = useState('')
  const [uploadDate, setUploadDate] = useState(new Date().toISOString().slice(0, 16))
  const [applicationFormId, setApplicationFormId] = useState('')

  async function loadAttachments() {
    setLoading(true)
    setError(null)

    try {
      const data = await getAttachments()
      setItems(data)
    } catch (err) {
      setError(getErrorMessage(err, 'Attachments could not be loaded'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadAttachments()
  }, [])

  function openCreateModal() {
    setEditingItem(null)
    setSelectedFileName('No file selected')
    setStoredFileName('')
    setFilePath('')
    setFileType('')
    setFileSize('')
    setUploadDate(new Date().toISOString().slice(0, 16))
    setApplicationFormId('')
    setOpenModal(true)
  }

  function openEditModal(item: AttachmentItem) {
    setEditingItem(item)
    setSelectedFileName(item.originalName)
    setStoredFileName(item.storedFileName)
    setFilePath(item.filePath)
    setFileType(item.fileType)
    setFileSize(String(item.fileSize))
    setUploadDate(item.uploadDate.slice(0, 16))
    setApplicationFormId(String(item.applicationFormId))
    setOpenModal(true)
  }

  async function submitForm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const payload: AttachmentPayload = {
      originalName: selectedFileName,
      storedFileName: storedFileName || `${Date.now()}-${selectedFileName}`,
      filePath,
      fileType,
      fileSize: Number(fileSize),
      uploadDate: new Date(uploadDate).toISOString(),
      applicationFormId: Number(applicationFormId),
    }

    try {
      if (editingItem) {
        const updated = await updateAttachment(editingItem.id, payload)
        setItems((prev) => prev.map((item) => (item.id === updated.id ? updated : item)))
        toast.success('Attachment updated')
      } else {
        const created = await createAttachment(payload)
        setItems((prev) => [created, ...prev])
        toast.success('Attachment created')
      }

      setOpenModal(false)
    } catch (err) {
      toast.error(getErrorMessage(err, 'Attachment could not be saved'))
    }
  }

  async function confirmDelete() {
    if (!selectedItem) {
      return
    }

    try {
      await deleteAttachment(selectedItem.id)
      setItems((prev) => prev.filter((item) => item.id !== selectedItem.id))
      setOpenConfirm(false)
      setSelectedItem(null)
      toast.success('Attachment deleted')
    } catch (err) {
      toast.error(getErrorMessage(err, 'Attachment could not be deleted'))
    }
  }

  const totalPages = Math.max(1, Math.ceil(items.length / PAGE_SIZE))
  const paged = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE
    return items.slice(start, start + PAGE_SIZE)
  }, [items, page])

  const columns = [
    { key: 'name', header: 'Original Name', render: (item: AttachmentItem) => item.originalName },
    { key: 'storedFileName', header: 'Stored Name', render: (item: AttachmentItem) => item.storedFileName },
    { key: 'type', header: 'Type', render: (item: AttachmentItem) => item.fileType },
    {
      key: 'fileSize',
      header: 'Size',
      render: (item: AttachmentItem) => `${Math.round(item.fileSize / 1024)} KB`,
    },
    {
      key: 'uploadDate',
      header: 'Uploaded',
      render: (item: AttachmentItem) => new Date(item.uploadDate).toLocaleDateString(),
    },
    {
      key: 'applicationFormId',
      header: 'Form ID',
      render: (item: AttachmentItem) => item.applicationFormId,
    },
    {
      key: 'actions',
      header: 'Actions',
      render: (item: AttachmentItem) => (
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
    return <LoadingScreen message="Loading attachments..." />
  }

  if (error) {
    return <ErrorState title="Attachments load failed" description={error} onRetry={() => void loadAttachments()} />
  }

  return (
    <section className="space-y-4">
      <PageHeader
        title="Attachments"
        subtitle="Securely upload and track evidence files"
        actions={<Button onClick={openCreateModal}>Upload</Button>}
      />

      {items.length === 0 ? (
        <EmptyState title="No attachments yet" description="Upload your first evidence file metadata." />
      ) : (
        <DataTable columns={columns} data={paged} />
      )}
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />

      <Modal open={openModal} title={editingItem ? 'Update Attachment' : 'Upload Attachment'} onClose={() => setOpenModal(false)}>
        <form className="space-y-3" onSubmit={submitForm}>
          <FileUpload
            onChange={(file) => {
              setSelectedFileName(file?.name ?? 'No file selected')
              setStoredFileName(file ? `${Date.now()}-${file.name}` : '')
              setFileSize(file ? String(file.size) : '')
            }}
          />
          <Input label="Original Name" value={selectedFileName} onChange={(event) => setSelectedFileName(event.target.value)} required />
          <Input label="Stored File Name" value={storedFileName} onChange={(event) => setStoredFileName(event.target.value)} required />
          <Input label="File Path" value={filePath} onChange={(event) => setFilePath(event.target.value)} required />
          <Input label="File Type" value={fileType} onChange={(event) => setFileType(event.target.value)} required />
          <Input label="File Size (bytes)" value={fileSize} onChange={(event) => setFileSize(event.target.value)} required />
          <Input
            label="Upload Date"
            type="datetime-local"
            value={uploadDate}
            onChange={(event) => setUploadDate(event.target.value)}
            required
          />
          <Input
            label="Application Form ID"
            value={applicationFormId}
            onChange={(event) => setApplicationFormId(event.target.value)}
            required
          />
          <p className="text-sm text-app-muted">{selectedFileName}</p>
          <div className="flex justify-end">
            <Button type="submit">
              {editingItem ? 'Update' : 'Save'}
            </Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={openConfirm}
        title="Delete attachment"
        description={`This action will remove ${selectedItem?.originalName ?? 'selected attachment'}.`}
        onClose={() => setOpenConfirm(false)}
        onConfirm={() => void confirmDelete()}
      />
    </section>
  )
}

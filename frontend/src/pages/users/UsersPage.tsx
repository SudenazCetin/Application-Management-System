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
import { createUser, deleteUser, getUsers, updateUser, type UserPayload } from '../../lib/services/usersApi'
import { toast } from 'sonner'
import type { UserItem } from '../../types'

const PAGE_SIZE = 5

export function UsersPage() {
  const [users, setUsers] = useState<UserItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [page, setPage] = useState(1)
  const [openModal, setOpenModal] = useState(false)
  const [openConfirm, setOpenConfirm] = useState(false)
  const [editingItem, setEditingItem] = useState<UserItem | null>(null)
  const [selectedItem, setSelectedItem] = useState<UserItem | null>(null)
  const [name, setName] = useState('')
  const [surname, setSurname] = useState('')
  const [email, setEmail] = useState('')
  const [role, setRole] = useState<UserItem['role']>('PERSONNEL')

  async function loadUsers() {
    setLoading(true)
    setError(null)

    try {
      const data = await getUsers()
      setUsers(data)
    } catch (err) {
      setError(getErrorMessage(err, 'Users list could not be loaded'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadUsers()
  }, [])

  function openCreateModal() {
    setEditingItem(null)
    setName('')
    setSurname('')
    setEmail('')
    setRole('PERSONNEL')
    setOpenModal(true)
  }

  function openEditModal(item: UserItem) {
    setEditingItem(item)
    setName(item.name)
    setSurname(item.surname)
    setEmail(item.email)
    setRole(item.role)
    setOpenModal(true)
  }

  async function submitForm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const payload: UserPayload = { name, surname, email, role }

    try {
      if (editingItem) {
        const updated = await updateUser(editingItem.id, payload)
        setUsers((prev) => prev.map((item) => (item.id === updated.id ? updated : item)))
        toast.success('User updated')
      } else {
        const created = await createUser(payload)
        setUsers((prev) => [created, ...prev])
        toast.success('User created')
      }

      setOpenModal(false)
    } catch (err) {
      toast.error(getErrorMessage(err, 'User could not be saved'))
    }
  }

  async function confirmDelete() {
    if (!selectedItem) {
      return
    }

    try {
      await deleteUser(selectedItem.id)
      setUsers((prev) => prev.filter((item) => item.id !== selectedItem.id))
      setOpenConfirm(false)
      setSelectedItem(null)
      toast.success('User deleted')
    } catch (err) {
      toast.error(getErrorMessage(err, 'User could not be deleted'))
    }
  }

  const totalPages = Math.max(1, Math.ceil(users.length / PAGE_SIZE))
  const paged = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE
    return users.slice(start, start + PAGE_SIZE)
  }, [page, users])

  const columns = [
    { key: 'name', header: 'Name', render: (item: UserItem) => `${item.name} ${item.surname}` },
    { key: 'email', header: 'Email', render: (item: UserItem) => item.email },
    {
      key: 'role',
      header: 'Role',
      render: (item: UserItem) => <span className="rounded-md bg-white/10 px-2 py-1 text-xs">{item.role}</span>,
    },
    {
      key: 'actions',
      header: 'Actions',
      render: (item: UserItem) => (
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
    return <LoadingScreen message="Loading users..." />
  }

  if (error) {
    return <ErrorState title="Users load failed" description={error} onRetry={() => void loadUsers()} />
  }

  return (
    <section className="space-y-4">
      <PageHeader
        title="Users"
        subtitle="Administrative user management and role assignment"
        actions={<Button onClick={openCreateModal}>Create User</Button>}
      />

      {users.length === 0 ? (
        <EmptyState title="No users yet" description="Create your first workspace user." />
      ) : (
        <DataTable columns={columns} data={paged} />
      )}
      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />

      <Modal open={openModal} title={editingItem ? 'Update User' : 'Create User'} onClose={() => setOpenModal(false)}>
        <form className="space-y-3" onSubmit={submitForm}>
          <Input label="Name" value={name} onChange={(event) => setName(event.target.value)} required />
          <Input label="Surname" value={surname} onChange={(event) => setSurname(event.target.value)} required />
          <Input label="Email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} required />
          <label className="flex flex-col gap-1 text-sm">
            <span className="text-app-muted">Role</span>
            <select
              className="rounded-lg border border-white/10 bg-white/[0.04] px-3 py-2"
              value={role}
              onChange={(event) => setRole(event.target.value as UserItem['role'])}
            >
              <option value="PERSONNEL">PERSONNEL</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </label>
          <div className="flex justify-end">
            <Button type="submit">
              {editingItem ? 'Update' : 'Save'}
            </Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={openConfirm}
        title="Delete user"
        description={`This action will remove ${selectedItem?.name ?? 'selected user'}.`}
        onClose={() => setOpenConfirm(false)}
        onConfirm={() => void confirmDelete()}
      />
    </section>
  )
}

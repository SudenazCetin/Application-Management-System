import { Modal } from './Modal'
import { Button } from './Button'

interface ConfirmDialogProps {
  open: boolean
  title: string
  description: string
  onConfirm: () => void
  onClose: () => void
}

export function ConfirmDialog({ open, title, description, onConfirm, onClose }: ConfirmDialogProps) {
  return (
    <Modal open={open} title={title} onClose={onClose}>
      <p className="mb-5 text-sm text-app-muted">{description}</p>
      <div className="flex justify-end gap-2">
        <Button type="button" variant="ghost" onClick={onClose}>
          Cancel
        </Button>
        <Button type="button" variant="danger" onClick={onConfirm}>
          Confirm
        </Button>
      </div>
    </Modal>
  )
}

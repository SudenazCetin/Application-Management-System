import type { PropsWithChildren } from 'react'
import { AnimatePresence, motion } from 'framer-motion'

interface ModalProps extends PropsWithChildren {
  open: boolean
  title: string
  onClose: () => void
}

export function Modal({ open, title, onClose, children }: ModalProps) {
  return (
    <AnimatePresence>
      {open ? (
        <motion.div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}>
          <motion.div
            className="glass-card w-full max-w-lg"
            initial={{ opacity: 0, y: 20, scale: 0.98 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 20, scale: 0.98 }}
          >
            <div className="mb-4 flex items-center justify-between">
              <h3 className="font-display text-xl font-semibold">{title}</h3>
              <button className="text-app-muted hover:text-app-text" onClick={onClose} type="button">
                Close
              </button>
            </div>
            {children}
          </motion.div>
        </motion.div>
      ) : null}
    </AnimatePresence>
  )
}

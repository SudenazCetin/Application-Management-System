import { motion } from 'framer-motion'

export function LoadingScreen({ message = 'Loading workspace...' }: { message?: string }) {
  return (
    <div className="flex min-h-[40vh] flex-col items-center justify-center gap-4">
      <motion.div
        className="h-12 w-12 rounded-full border-2 border-app-primary/20 border-t-app-primary"
        animate={{ rotate: 360 }}
        transition={{ repeat: Number.POSITIVE_INFINITY, duration: 1, ease: 'linear' }}
      />
      <p className="text-app-muted">{message}</p>
    </div>
  )
}

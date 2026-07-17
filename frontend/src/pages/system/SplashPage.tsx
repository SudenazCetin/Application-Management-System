import { motion } from 'framer-motion'
import { ShieldCheck } from 'lucide-react'
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'

export function SplashPage() {
  const navigate = useNavigate()
  const { isAuthenticated } = useAuth()

  useEffect(() => {
    const timer = setTimeout(() => {
      navigate(isAuthenticated ? '/dashboard' : '/login', { replace: true })
    }, 1300)

    return () => clearTimeout(timer)
  }, [isAuthenticated, navigate])

  return (
    <div className="flex min-h-screen items-center justify-center p-6">
      <motion.div
        initial={{ opacity: 0, scale: 0.94 }}
        animate={{ opacity: 1, scale: 1 }}
        className="glass-card flex w-full max-w-lg flex-col items-center gap-4 py-14 text-center"
      >
        <div className="rounded-full bg-app-primary/20 p-4">
          <ShieldCheck className="h-10 w-10 text-app-primary" />
        </div>
        <h1 className="heading-xl">Application Management System</h1>
        <p className="text-app-muted">Preparing secure workspace...</p>
      </motion.div>
    </div>
  )
}

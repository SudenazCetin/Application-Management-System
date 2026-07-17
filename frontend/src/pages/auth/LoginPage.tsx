import { motion } from 'framer-motion'
import { Loader2, Lock } from 'lucide-react'
import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { Button } from '../../components/ui/Button'
import { Input } from '../../components/ui/Input'
import { useAuth } from '../../hooks/useAuth'

export function LoginPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const { login } = useAuth()

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  async function onSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      await login(email, password)
      const from = (location.state as { from?: string } | null)?.from ?? '/dashboard'
      navigate(from, { replace: true })
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Login failed'
      setError(message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center p-6">
      <motion.form
        onSubmit={onSubmit}
        initial={{ opacity: 0, y: 16 }}
        animate={{ opacity: 1, y: 0 }}
        className="glass-card w-full max-w-md space-y-4"
      >
        <div className="space-y-2 text-center">
          <div className="mx-auto w-fit rounded-full bg-app-primary/20 p-3 text-app-primary">
            <Lock className="h-6 w-6" />
          </div>
          <h1 className="heading-lg">Welcome Back</h1>
          <p className="text-sm text-app-muted">Sign in to access your workspace.</p>
        </div>

        <Input label="Email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} required />
        <Input label="Password" type="password" value={password} onChange={(event) => setPassword(event.target.value)} required />

        {error ? <p className="text-sm text-app-danger">{error}</p> : null}

        <Button className="w-full" disabled={loading} type="submit">
          {loading ? <Loader2 className="h-4 w-4 animate-spin" /> : 'Login'}
        </Button>

        <p className="text-center text-sm text-app-muted">
          No account?{' '}
          <Link to="/register" className="text-app-primary hover:underline">
            Register
          </Link>
        </p>
      </motion.form>
    </div>
  )
}

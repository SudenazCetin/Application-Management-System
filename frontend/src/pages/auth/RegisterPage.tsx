import { motion } from 'framer-motion'
import { Loader2, UserPlus } from 'lucide-react'
import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Button } from '../../components/ui/Button'
import { Input } from '../../components/ui/Input'
import { useAuth } from '../../hooks/useAuth'

export function RegisterPage() {
  const navigate = useNavigate()
  const { register } = useAuth()

  const [name, setName] = useState('')
  const [surname, setSurname] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  async function onSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      await register({ name, surname, email, password })
      navigate('/login', { replace: true })
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Register failed'
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
            <UserPlus className="h-6 w-6" />
          </div>
          <h1 className="heading-lg">Create your account</h1>
          <p className="text-sm text-app-muted">Start managing applications in one secure hub.</p>
        </div>

        <Input label="Name" value={name} onChange={(event) => setName(event.target.value)} required />
        <Input label="Surname" value={surname} onChange={(event) => setSurname(event.target.value)} required />
        <Input label="Email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} required />
        <Input
          label="Password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          minLength={8}
          required
        />
        <p className="-mt-2 text-xs text-app-muted">Sifre en az 8 karakter olmalidir.</p>

        {error ? <p className="text-sm text-app-danger">{error}</p> : null}

        <Button className="w-full" disabled={loading} type="submit">
          {loading ? <Loader2 className="h-4 w-4 animate-spin" /> : 'Register'}
        </Button>

        <p className="text-center text-sm text-app-muted">
          Already have an account?{' '}
          <Link to="/login" className="text-app-primary hover:underline">
            Login
          </Link>
        </p>
      </motion.form>
    </div>
  )
}

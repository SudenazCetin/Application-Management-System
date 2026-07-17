import { ShieldAlert } from 'lucide-react'
import { Link } from 'react-router-dom'

export function UnauthorizedPage() {
  return (
    <div className="flex min-h-screen items-center justify-center p-6">
      <div className="glass-card w-full max-w-md space-y-4 text-center">
        <ShieldAlert className="mx-auto h-10 w-10 text-app-warning" />
        <h1 className="heading-lg">403 - Unauthorized</h1>
        <p className="text-sm text-app-muted">Bu kaynak icin gerekli role sahip degilsiniz.</p>
        <Link className="inline-block rounded-lg bg-app-primary px-4 py-2 text-sm font-semibold" to="/dashboard">
          Back to dashboard
        </Link>
      </div>
    </div>
  )
}

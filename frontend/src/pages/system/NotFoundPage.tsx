import { Compass } from 'lucide-react'
import { Link } from 'react-router-dom'

export function NotFoundPage() {
  return (
    <div className="flex min-h-screen items-center justify-center p-6">
      <div className="glass-card w-full max-w-md space-y-4 text-center">
        <Compass className="mx-auto h-10 w-10 text-app-primary" />
        <h1 className="heading-lg">404 - Page not found</h1>
        <p className="text-sm text-app-muted">Aradiginiz sayfa bulunamadi veya tasinmis olabilir.</p>
        <Link className="inline-block rounded-lg bg-app-primary px-4 py-2 text-sm font-semibold" to="/dashboard">
          Go dashboard
        </Link>
      </div>
    </div>
  )
}

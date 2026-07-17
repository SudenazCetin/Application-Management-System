import { Card } from '../../components/ui/Card'
import { PageHeader } from '../../components/ui/PageHeader'
import { useAuth } from '../../hooks/useAuth'

export function ProfilePage() {
  const { user } = useAuth()

  return (
    <section className="space-y-4">
      <PageHeader title="Profile" subtitle="Identity and access details" />
      <Card className="space-y-3">
        <div>
          <p className="text-xs text-app-muted">Email</p>
          <p className="font-medium">{user?.email}</p>
        </div>
        <div>
          <p className="text-xs text-app-muted">Role</p>
          <p className="font-medium">{user?.role}</p>
        </div>
        <div>
          <p className="text-xs text-app-muted">Security</p>
          <p className="text-sm text-app-muted">JWT authentication and role-based authorization are enabled.</p>
        </div>
      </Card>
    </section>
  )
}

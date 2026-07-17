import { useEffect, useState } from 'react'
import { AlertOctagon, BarChart3, CalendarClock, CheckCircle2, Clock3, ListChecks } from 'lucide-react'
import { Card } from '../../components/ui/Card'
import { DataTable } from '../../components/ui/DataTable'
import { PageHeader } from '../../components/ui/PageHeader'
import { StatCard } from '../../components/ui/StatCard'
import { EmptyState } from '../../components/states/EmptyState'
import { ErrorState } from '../../components/states/ErrorState'
import { LoadingScreen } from '../../components/states/LoadingScreen'
import { getErrorMessage } from '../../lib/httpError'
import { getDashboardStats } from '../../lib/services/dashboardApi'
import type { ApplicationFormItem, DashboardStats } from '../../types'

const STATUS_LABELS: Record<ApplicationFormItem['status'], string> = {
  NEW: 'Yeni',
  IN_REVIEW: 'Incelemede',
  APPROVED: 'Onaylandi',
  REJECTED: 'Reddedildi',
  CANCELLED: 'Iptal Edildi',
}

const STATUS_STYLES: Record<ApplicationFormItem['status'], string> = {
  NEW: 'bg-blue-500/15 text-blue-300',
  IN_REVIEW: 'bg-amber-500/15 text-amber-300',
  APPROVED: 'bg-emerald-500/15 text-emerald-300',
  REJECTED: 'bg-red-500/15 text-red-300',
  CANCELLED: 'bg-white/10 text-app-muted',
}

export function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  async function loadStats() {
    setLoading(true)
    setError(null)

    try {
      const data = await getDashboardStats()
      setStats(data)
    } catch (err) {
      setError(getErrorMessage(err, 'Dashboard istatistikleri yuklenemedi'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void loadStats()
  }, [])

  if (loading) {
    return <LoadingScreen message="Dashboard yukleniyor..." />
  }

  if (error || !stats) {
    return <ErrorState title="Dashboard yuklenemedi" description={error ?? 'Bilinmeyen hata'} onRetry={() => void loadStats()} />
  }

  const columns = [
    { key: 'title', header: 'Baslik', render: (item: ApplicationFormItem) => item.title },
    {
      key: 'status',
      header: 'Durum',
      render: (item: ApplicationFormItem) => (
        <span className={`rounded-full px-2 py-1 text-xs font-medium ${STATUS_STYLES[item.status]}`}>
          {STATUS_LABELS[item.status]}
        </span>
      ),
    },
    {
      key: 'date',
      header: 'Basvuru Tarihi',
      render: (item: ApplicationFormItem) => new Date(item.applicationDate).toLocaleString(),
    },
  ]

  return (
    <section className="space-y-4">
      <PageHeader
        title="Dashboard"
        subtitle="Platform genelindeki basvuru istatistiklerine genel bakis"
      />

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
        <StatCard
          title="Toplam Basvuru"
          value={String(stats.totalApplications)}
          icon={<ListChecks className="h-4 w-4" />}
          trend="Sistemdeki tum basvurular"
        />
        <StatCard
          title="Bekleyen Basvuru"
          value={String(stats.pendingApplications)}
          icon={<Clock3 className="h-4 w-4" />}
          trend="NEW + IN_REVIEW"
        />
        <StatCard
          title="Onaylanan Basvuru"
          value={String(stats.approvedApplications)}
          icon={<CheckCircle2 className="h-4 w-4" />}
          trend="APPROVED"
        />
        <StatCard
          title="Reddedilen Basvuru"
          value={String(stats.rejectedApplications)}
          icon={<AlertOctagon className="h-4 w-4" />}
          trend="REJECTED"
        />
        <StatCard
          title="Bugunku Basvurular"
          value={String(stats.todayApplications)}
          icon={<CalendarClock className="h-4 w-4" />}
          trend={new Date().toLocaleDateString()}
        />
        <StatCard
          title="Onay Orani"
          value={
            stats.totalApplications > 0
              ? `${Math.round((stats.approvedApplications / stats.totalApplications) * 100)}%`
              : '0%'
          }
          icon={<BarChart3 className="h-4 w-4" />}
          trend="Onaylanan / Toplam"
        />
      </div>

      <Card>
        <h2 className="mb-3 font-display text-xl">Son Olusturulan 10 Basvuru</h2>
        {stats.recentApplications.length === 0 ? (
          <EmptyState title="Henuz basvuru yok" description="Yeni bir basvuru olusturuldugunda burada listelenecek." />
        ) : (
          <DataTable columns={columns} data={stats.recentApplications} />
        )}
      </Card>
    </section>
  )
}

import { EmptyState } from '../../components/states/EmptyState'
import { PageHeader } from '../../components/ui/PageHeader'

export function EmptyStatePage() {
  return (
    <section className="space-y-4">
      <PageHeader title="Empty State" subtitle="Fallback UI for empty datasets" />
      <EmptyState title="No Records Yet" description="Yeni kayit olusturuldugunda bu liste otomatik olarak dolacaktir." />
    </section>
  )
}

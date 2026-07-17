import { ErrorState } from '../../components/states/ErrorState'
import { PageHeader } from '../../components/ui/PageHeader'

export function ErrorStatePage() {
  return (
    <section className="space-y-4">
      <PageHeader title="Error State" subtitle="Reusable error handling surface" />
      <ErrorState
        title="Request Failed"
        description="Sunucuya baglanirken bir hata olustu. Lutfen tekrar deneyin."
      />
    </section>
  )
}

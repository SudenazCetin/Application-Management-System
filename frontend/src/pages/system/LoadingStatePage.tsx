import { LoadingScreen } from '../../components/states/LoadingScreen'
import { PageHeader } from '../../components/ui/PageHeader'

export function LoadingStatePage() {
  return (
    <section className="space-y-4">
      <PageHeader title="Loading State" subtitle="Reusable loading visualization for async operations" />
      <LoadingScreen message="Fetching enterprise data..." />
    </section>
  )
}

import { Inbox } from 'lucide-react'

export function EmptyState({ title, description }: { title: string; description: string }) {
  return (
    <div className="glass-card flex flex-col items-center gap-3 py-12 text-center">
      <Inbox className="h-10 w-10 text-app-muted" />
      <h3 className="font-display text-xl">{title}</h3>
      <p className="max-w-md text-sm text-app-muted">{description}</p>
    </div>
  )
}

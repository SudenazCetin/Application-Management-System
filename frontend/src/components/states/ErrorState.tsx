import { AlertTriangle } from 'lucide-react'
import { Button } from '../ui/Button'

interface ErrorStateProps {
  title: string
  description: string
  onRetry?: () => void
}

export function ErrorState({ title, description, onRetry }: ErrorStateProps) {
  return (
    <div className="glass-card flex flex-col items-center gap-3 py-12 text-center">
      <AlertTriangle className="h-10 w-10 text-app-danger" />
      <h3 className="font-display text-xl">{title}</h3>
      <p className="max-w-md text-sm text-app-muted">{description}</p>
      {onRetry ? (
        <Button type="button" onClick={onRetry}>
          Try Again
        </Button>
      ) : null}
    </div>
  )
}

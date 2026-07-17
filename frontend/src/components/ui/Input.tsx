import type { InputHTMLAttributes } from 'react'
import { cn } from '../../lib/cn'

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string
  error?: string
}

export function Input({ label, className, error, ...props }: InputProps) {
  return (
    <label className="flex flex-col gap-1 text-sm">
      <span className="text-app-muted">{label}</span>
      <input
        className={cn(
          'w-full rounded-lg border border-white/10 bg-white/[0.04] px-3 py-2 text-app-text placeholder:text-app-muted/70 focus:border-app-primary focus:outline-none focus:ring-2 focus:ring-app-primary/30',
          className,
        )}
        {...props}
      />
      {error ? <span className="text-xs text-app-danger">{error}</span> : null}
    </label>
  )
}

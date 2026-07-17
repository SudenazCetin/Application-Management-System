import type { PropsWithChildren } from 'react'
import { cn } from '../../lib/cn'

interface CardProps extends PropsWithChildren {
  className?: string
}

export function Card({ children, className }: CardProps) {
  return <div className={cn('glass-card p-5', className)}>{children}</div>
}

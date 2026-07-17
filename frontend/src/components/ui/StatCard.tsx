import { motion } from 'framer-motion'
import type { ReactNode } from 'react'
import { Card } from './Card'

interface StatCardProps {
  title: string
  value: string
  icon: ReactNode
  trend: string
}

export function StatCard({ title, value, icon, trend }: StatCardProps) {
  return (
    <motion.div initial={{ opacity: 0, y: 14 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.35 }}>
      <Card className="flex flex-col gap-3">
        <div className="flex items-center justify-between text-app-muted">
          <span className="text-sm">{title}</span>
          <span>{icon}</span>
        </div>
        <p className="font-display text-3xl font-semibold">{value}</p>
        <p className="text-sm text-app-success">{trend}</p>
      </Card>
    </motion.div>
  )
}

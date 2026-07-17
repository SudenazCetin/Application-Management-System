import type { ReactNode } from 'react'
import { cn } from '../../lib/cn'

interface Column<T> {
  key: string
  header: string
  render: (item: T) => ReactNode
}

interface DataTableProps<T> {
  columns: Column<T>[]
  data: T[]
  className?: string
}

export function DataTable<T>({ columns, data, className }: DataTableProps<T>) {
  return (
    <div className={cn('overflow-x-auto rounded-xl border border-white/10', className)}>
      <table className="min-w-full text-left text-sm">
        <thead className="bg-white/[0.04] text-app-muted">
          <tr>
            {columns.map((column) => (
              <th key={column.key} className="px-4 py-3 font-medium">
                {column.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((item, index) => (
            <tr key={index} className="border-t border-white/5 hover:bg-white/[0.02]">
              {columns.map((column) => (
                <td key={column.key} className="px-4 py-3 text-app-text">
                  {column.render(item)}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

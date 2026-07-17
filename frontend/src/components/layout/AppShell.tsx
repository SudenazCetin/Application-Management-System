import type { PropsWithChildren } from 'react'
import { Sidebar } from './Sidebar'
import { TopNavbar } from './TopNavbar'

export function AppShell({ children }: PropsWithChildren) {
  return (
    <div className="mx-auto flex max-w-[1600px] gap-4 p-4">
      <Sidebar />
      <main className="min-h-screen flex-1">
        <TopNavbar />
        <div className="space-y-4">{children}</div>
      </main>
    </div>
  )
}

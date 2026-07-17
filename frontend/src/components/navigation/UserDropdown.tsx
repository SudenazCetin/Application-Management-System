import { ChevronDown, LogOut, ShieldCheck } from 'lucide-react'
import { useState } from 'react'
import { useAuth } from '../../hooks/useAuth'

export function UserDropdown() {
  const [open, setOpen] = useState(false)
  const { user, logout } = useAuth()

  return (
    <div className="relative">
      <button
        type="button"
        onClick={() => setOpen((prev) => !prev)}
        className="glass-panel flex items-center gap-2 rounded-lg px-3 py-2"
      >
        <div className="flex h-7 w-7 items-center justify-center rounded-full bg-app-primary/25 text-xs font-semibold">
          {user?.email?.[0]?.toUpperCase() ?? 'U'}
        </div>
        <span className="hidden text-sm text-app-muted md:block">{user?.role ?? 'PERSONNEL'}</span>
        <ChevronDown className="h-4 w-4 text-app-muted" />
      </button>
      {open ? (
        <div className="glass-card absolute right-0 z-20 mt-2 w-56 p-2">
          <p className="px-3 py-2 text-xs text-app-muted">{user?.email}</p>
          <div className="my-1 h-px bg-white/10" />
          <button
            type="button"
            className="flex w-full items-center gap-2 rounded-md px-3 py-2 text-left text-sm hover:bg-white/5"
          >
            <ShieldCheck className="h-4 w-4" />
            Security profile
          </button>
          <button
            type="button"
            className="flex w-full items-center gap-2 rounded-md px-3 py-2 text-left text-sm text-app-danger hover:bg-red-500/10"
            onClick={logout}
          >
            <LogOut className="h-4 w-4" />
            Logout
          </button>
        </div>
      ) : null}
    </div>
  )
}

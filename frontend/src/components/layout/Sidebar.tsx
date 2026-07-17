import { motion } from 'framer-motion'
import { Link, useLocation } from 'react-router-dom'
import { navItems, securityBadge } from '../../data/navigation'
import { useAuth } from '../../hooks/useAuth'
import { cn } from '../../lib/cn'

export function Sidebar() {
  const { user } = useAuth()
  const location = useLocation()

  const filtered = navItems.filter((item) => (user ? item.roles.includes(user.role) : false))
  const SecurityIcon = securityBadge.icon

  return (
    <aside className="glass-card hidden h-[calc(100vh-2rem)] w-72 flex-col gap-4 p-4 lg:flex">
      <div>
        <p className="font-display text-xl">Application Management</p>
        <p className="text-sm text-app-muted">Enterprise Workspace</p>
      </div>
      <nav className="flex flex-1 flex-col gap-1">
        {filtered.map((item) => {
          const Icon = item.icon
          const active = location.pathname.startsWith(item.path)

          return (
            <Link
              key={item.path}
              to={item.path}
              className={cn(
                'relative flex items-center gap-3 rounded-lg px-3 py-2 text-sm transition',
                active ? 'bg-app-primary/20 text-white' : 'text-app-muted hover:bg-white/[0.04] hover:text-app-text',
              )}
            >
              {active ? (
                <motion.span
                  className="absolute left-0 top-1/2 h-6 w-1 -translate-y-1/2 rounded-r bg-app-primary"
                  layoutId="active-nav"
                />
              ) : null}
              <Icon className="h-4 w-4" />
              {item.label}
            </Link>
          )
        })}
      </nav>
      <div className="glass-panel rounded-lg p-3 text-xs text-app-muted">
        <div className="mb-1 inline-flex items-center gap-2 text-app-text">
          <SecurityIcon className="h-4 w-4 text-app-success" />
          {securityBadge.title}
        </div>
        Role-aware menus and route guards are active.
      </div>
    </aside>
  )
}

import { Menu } from 'lucide-react'
import { useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { navItems } from '../../data/navigation'
import { useAuth } from '../../hooks/useAuth'
import { Breadcrumbs } from '../navigation/Breadcrumbs'
import { NotificationMenu } from '../navigation/NotificationMenu'
import { SearchBar } from '../navigation/SearchBar'
import { UserDropdown } from '../navigation/UserDropdown'

export function TopNavbar() {
  const [query, setQuery] = useState('')
  const [mobileMenu, setMobileMenu] = useState(false)
  const { user } = useAuth()
  const location = useLocation()

  const mobileItems = navItems.filter((item) => (user ? item.roles.includes(user.role) : false))

  return (
    <header className="glass-card sticky top-4 z-10 mb-4 flex flex-col gap-4 p-4">
      <div className="flex items-center justify-between gap-3">
        <button
          type="button"
          className="glass-panel rounded-lg p-2 text-app-muted lg:hidden"
          onClick={() => setMobileMenu((prev) => !prev)}
          aria-label="Toggle navigation"
        >
          <Menu className="h-5 w-5" />
        </button>
        <SearchBar value={query} onChange={setQuery} />
        <div className="flex items-center gap-2">
          <NotificationMenu />
          <UserDropdown />
        </div>
      </div>
      <Breadcrumbs />
      {mobileMenu ? (
        <nav className="grid grid-cols-2 gap-2 lg:hidden">
          {mobileItems.map((item) => {
            const Icon = item.icon
            const active = location.pathname.startsWith(item.path)

            return (
              <Link
                key={item.path}
                to={item.path}
                onClick={() => setMobileMenu(false)}
                className={`glass-panel flex items-center gap-2 rounded-lg px-3 py-2 text-sm ${active ? 'text-app-text' : 'text-app-muted'}`}
              >
                <Icon className="h-4 w-4" />
                {item.label}
              </Link>
            )
          })}
        </nav>
      ) : null}
    </header>
  )
}

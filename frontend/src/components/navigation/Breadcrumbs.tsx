import { ChevronRight } from 'lucide-react'
import { Link, useLocation } from 'react-router-dom'

function prettyLabel(value: string) {
  return value
    .replace(/-/g, ' ')
    .replace(/\b\w/g, (char) => char.toUpperCase())
}

export function Breadcrumbs() {
  const location = useLocation()
  const segments = location.pathname.split('/').filter(Boolean)

  if (segments.length === 0) {
    return null
  }

  return (
    <nav className="flex items-center gap-1 text-xs text-app-muted" aria-label="Breadcrumb">
      <Link to="/dashboard" className="hover:text-app-text">
        Home
      </Link>
      {segments.map((segment, index) => {
        const path = `/${segments.slice(0, index + 1).join('/')}`
        const last = index === segments.length - 1

        return (
          <span key={segment} className="inline-flex items-center gap-1">
            <ChevronRight className="h-3 w-3" />
            {last ? <span className="text-app-text">{prettyLabel(segment)}</span> : <Link to={path}>{prettyLabel(segment)}</Link>}
          </span>
        )
      })}
    </nav>
  )
}

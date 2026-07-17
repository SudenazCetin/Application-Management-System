import { Bell } from 'lucide-react'

export function NotificationMenu() {
  return (
    <button
      className="glass-panel relative rounded-lg p-2 text-app-muted transition hover:text-app-text"
      type="button"
      aria-label="Notifications"
    >
      <Bell className="h-5 w-5" />
      <span className="absolute -right-1 -top-1 h-2.5 w-2.5 rounded-full bg-app-warning" />
    </button>
  )
}

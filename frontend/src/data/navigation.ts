import {
  ClipboardList,
  FileType2,
  Home,
  Paperclip,
  Shield,
  UserCircle2,
  Users,
  type LucideIcon,
} from 'lucide-react'
import type { Role } from '../types'

export interface NavItem {
  label: string
  path: string
  icon: LucideIcon
  roles: Role[]
}

export const navItems: NavItem[] = [
  { label: 'Dashboard', path: '/dashboard', icon: Home, roles: ['ADMIN', 'PERSONNEL'] },
  { label: 'Users', path: '/users', icon: Users, roles: ['ADMIN'] },
  { label: 'Form Types', path: '/form-types', icon: FileType2, roles: ['ADMIN'] },
  { label: 'Application Forms', path: '/forms', icon: ClipboardList, roles: ['ADMIN', 'PERSONNEL'] },
  { label: 'Attachments', path: '/attachments', icon: Paperclip, roles: ['ADMIN', 'PERSONNEL'] },
  { label: 'Profile', path: '/profile', icon: UserCircle2, roles: ['ADMIN', 'PERSONNEL'] },
]

export const securityBadge = {
  title: 'JWT + RBAC Active',
  icon: Shield,
}

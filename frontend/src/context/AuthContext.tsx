import { createContext } from 'react'
import type { AuthUser, Role } from '../types'

export interface AuthContextValue {
  user: AuthUser | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  register: (payload: {
    name: string
    surname: string
    email: string
    password: string
  }) => Promise<void>
  logout: () => void
  updateRole: (role: Role) => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)

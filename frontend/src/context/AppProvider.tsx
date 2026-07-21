import { useEffect, useMemo, useState, type PropsWithChildren } from 'react'
import axios from 'axios'
import { toast, Toaster } from 'sonner'
import { AuthContext } from './AuthContext'
import { api } from '../lib/api'
import { decodeToken } from '../lib/jwt'
import type { AuthUser, Role } from '../types'
import { getErrorMessage } from '../lib/httpError'

const TOKEN_KEY = 'ams_token'

function getInitialUser(): AuthUser | null {
  const token = localStorage.getItem(TOKEN_KEY)

  if (!token) {
    return null
  }

  const decoded = decodeToken(token)

  if (!decoded || !decoded.email) {
    localStorage.removeItem(TOKEN_KEY)
    return null
  }

  return {
    email: decoded.email,
    role: decoded.role,
    token,
  }
}

async function inferRoleFromBackend() {
  try {
    await api.get('/api/users')
    return 'ADMIN' as Role
  } catch (error) {
    if (axios.isAxiosError(error) && error.response?.status === 403) {
      return 'PERSONNEL' as Role
    }

    throw error
  }
}

export function AppProvider({ children }: PropsWithChildren) {
  const [user, setUser] = useState<AuthUser | null>(() => getInitialUser())

  useEffect(() => {
    async function syncRole() {
      if (!user?.token) {
        return
      }

      try {
        const role = await inferRoleFromBackend()
        setUser((prev) => {
          if (!prev) {
            return prev
          }

          return {
            ...prev,
            role,
          }
        })
      } catch {
        // Bu adimda role belirlenemezse mevcut role bilgisi korunur.
      }
    }

    void syncRole()
  }, [user?.token])

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: Boolean(user?.token),
      updateRole: (role: Role) => {
        setUser((prev) => {
          if (!prev) {
            return prev
          }

          return { ...prev, role }
        })
      },
      login: async (email: string, password: string) => {
        try {
          const { data } = await api.post<{ success: boolean; message: string; token: string | null }>('/api/auth/login', {
            email,
            password,
          })

          if (!data.success || !data.token) {
            throw new Error(data.message || 'Login failed')
          }

          const decoded = decodeToken(data.token)

          if (!decoded) {
            throw new Error('Token could not be parsed')
          }

          localStorage.setItem(TOKEN_KEY, data.token)

          const role = await inferRoleFromBackend()

          const nextUser: AuthUser = {
            email: decoded.email || email,
            role,
            token: data.token,
          }

          setUser(nextUser)
          toast.success('Giris basarili')
        } catch (error) {
          throw new Error(getErrorMessage(error, 'Login failed'))
        }
      },
      register: async (payload: {
        name: string
        surname: string
        email: string
        password: string
      }) => {
        try {
          const { data } = await api.post<{ success: boolean; message: string }>('/api/auth/register', payload)

          if (!data.success) {
            throw new Error(data.message || 'Kayit islemi basarisiz')
          }

          toast.success('Kayit basariyla tamamlandi')
        } catch (error) {
          throw new Error(getErrorMessage(error, 'Kayit islemi basarisiz'))
        }
      },
      logout: () => {
        localStorage.removeItem(TOKEN_KEY)
        setUser(null)
        toast.info('Oturum kapatildi')
      },
    }),
    [user],
  )

  return (
    <AuthContext.Provider value={value}>
      {children}
      <Toaster position="top-right" richColors closeButton />
    </AuthContext.Provider>
  )
}

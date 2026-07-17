import type { Role } from '../types'

interface JwtPayload {
  sub?: string
  role?: string
  roles?: string[]
  authorities?: string[]
}

function parseRoleFromClaims(payload: JwtPayload): Role {
  if (payload.role === 'ADMIN' || payload.role === 'PERSONNEL') {
    return payload.role
  }

  const roleCandidate = payload.roles?.[0] ?? payload.authorities?.[0] ?? ''

  if (roleCandidate.includes('ADMIN')) {
    return 'ADMIN'
  }

  return 'PERSONNEL'
}

export function decodeToken(token: string): { email: string; role: Role } | null {
  try {
    const encoded = token.split('.')[1]

    if (!encoded) {
      return null
    }

    const normalized = encoded.replace(/-/g, '+').replace(/_/g, '/')
    const payload = JSON.parse(atob(normalized)) as JwtPayload

    return {
      email: payload.sub ?? '',
      role: parseRoleFromClaims(payload),
    }
  } catch {
    return null
  }
}

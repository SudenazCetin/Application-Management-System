import { AnimatePresence, motion } from 'framer-motion'
import { BrowserRouter, Navigate, Route, Routes, useLocation } from 'react-router-dom'
import { ProtectedRoute } from './ProtectedRoute'
import { RoleGuard } from './RoleGuard'
import { AppLayout } from './AppLayout'
import { LoginPage } from '../pages/auth/LoginPage'
import { RegisterPage } from '../pages/auth/RegisterPage'
import { DashboardPage } from '../pages/dashboard/DashboardPage'
import { UsersPage } from '../pages/users/UsersPage'
import { FormTypesPage } from '../pages/form-types/FormTypesPage'
import { ApplicationFormsPage } from '../pages/forms/ApplicationFormsPage'
import { AttachmentsPage } from '../pages/attachments/AttachmentsPage'
import { ProfilePage } from '../pages/profile/ProfilePage'
import { SplashPage } from '../pages/system/SplashPage'
import { UnauthorizedPage } from '../pages/system/UnauthorizedPage'
import { NotFoundPage } from '../pages/system/NotFoundPage'
import { LoadingStatePage } from '../pages/system/LoadingStatePage'
import { EmptyStatePage } from '../pages/system/EmptyStatePage'
import { ErrorStatePage } from '../pages/system/ErrorStatePage'

function AnimatedRoutes() {
  const location = useLocation()

  return (
    <AnimatePresence mode="wait">
      <motion.div
        key={location.pathname}
        initial={{ opacity: 0, y: 8 }}
        animate={{ opacity: 1, y: 0 }}
        exit={{ opacity: 0, y: -8 }}
        transition={{ duration: 0.2 }}
      >
        <Routes location={location}>
          <Route path="/" element={<Navigate to="/splash" replace />} />
          <Route path="/splash" element={<SplashPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/unauthorized" element={<UnauthorizedPage />} />

          <Route element={<ProtectedRoute />}>
            <Route element={<AppLayout />}>
              <Route path="/dashboard" element={<DashboardPage />} />

              <Route element={<RoleGuard allow={['ADMIN']} />}>
                <Route path="/users" element={<UsersPage />} />
                <Route path="/form-types" element={<FormTypesPage />} />
              </Route>

              <Route element={<RoleGuard allow={['ADMIN', 'PERSONNEL']} />}>
                <Route path="/forms" element={<ApplicationFormsPage />} />
                <Route path="/attachments" element={<AttachmentsPage />} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/system/loading" element={<LoadingStatePage />} />
                <Route path="/system/empty" element={<EmptyStatePage />} />
                <Route path="/system/error" element={<ErrorStatePage />} />
              </Route>
            </Route>
          </Route>

          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </motion.div>
    </AnimatePresence>
  )
}

export function AppRouter() {
  return (
    <BrowserRouter>
      <AnimatedRoutes />
    </BrowserRouter>
  )
}

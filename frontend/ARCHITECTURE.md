# Application Management System Frontend Architecture

## 1) UI/UX Architecture

### Visual System
- Theme: dark enterprise glassmorphism
- Background: `#0B0F19`
- Card: `#171C28`
- Primary: `#4F7CFF`
- Success: `#22C55E`
- Danger: `#EF4444`
- Warning: `#F59E0B`
- Text: `#FFFFFF`
- Secondary Text: `#A1A1AA`

### Layout
- Desktop: left Sidebar + top Navbar + content area
- Tablet: collapsible mobile navigation in Navbar
- Mobile: stacked cards/tables, single-column forms

### Navigation
- Public routes: splash, login, register
- Protected routes: dashboard and all business pages
- Role guards:
  - ADMIN: users, form types
  - ADMIN + PERSONNEL: forms, attachments, profile

### Reusable UX States
- Splash / loading
- Generic loading state
- Empty state
- Error state
- 403 and 404 pages

### Motion and Feedback
- Page transitions with Framer Motion
- Sidebar active indicator animation
- Modal and overlay transitions
- Toast notifications via Sonner

## 2) Folder Structure

```text
frontend/
  src/
    app/
    components/
      forms/
      layout/
      navigation/
      states/
      ui/
    context/
    data/
    hooks/
    lib/
    pages/
      attachments/
      auth/
      dashboard/
      form-types/
      forms/
      profile/
      system/
      users/
    routes/
    types/
```

## 3) Page Implementation Summary

1. Splash / Loading: animated branded entry and redirect by auth state
2. Login: JWT login form, validation, toast feedback
3. Register: secure account creation form and redirect to login
4. Dashboard: premium summary cards and executive overview panel
5. Users: ADMIN table + pagination + modal + confirm dialog
6. Form Types: ADMIN management table + modal + confirm dialog
7. Application Forms: role-based list and create/remove UX
8. Attachments: authenticated list + file upload component
9. Profile: role and security profile details
10. 403 Unauthorized: role-denied page
11. 404 Not Found: unmatched route fallback
12. Loading Screen: reusable async loading page
13. Empty State: reusable no-data page
14. Error State: reusable failure page

## 4) API Integration Notes

- Axios base URL from environment: `VITE_API_BASE_URL`
- Default fallback: `http://localhost:8080`
- JWT token persisted in `localStorage` as `ams_token`
- Request interceptor adds `Authorization: Bearer <token>` automatically

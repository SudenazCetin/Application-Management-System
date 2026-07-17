import { Search } from 'lucide-react'

interface SearchBarProps {
  value: string
  onChange: (value: string) => void
}

export function SearchBar({ value, onChange }: SearchBarProps) {
  return (
    <label className="glass-panel flex w-full max-w-md items-center gap-2 rounded-lg px-3 py-2">
      <Search className="h-4 w-4 text-app-muted" />
      <input
        className="w-full bg-transparent text-sm text-app-text placeholder:text-app-muted/70 focus:outline-none"
        placeholder="Search records, users, forms..."
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </label>
  )
}

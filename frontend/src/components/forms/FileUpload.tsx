import { UploadCloud } from 'lucide-react'

interface FileUploadProps {
  onChange: (file: File | null) => void
}

export function FileUpload({ onChange }: FileUploadProps) {
  return (
    <label className="glass-panel flex cursor-pointer items-center justify-center gap-2 rounded-xl p-6 text-app-muted transition hover:border-app-primary/60">
      <UploadCloud className="h-5 w-5" />
      <span>Drag & drop or choose file</span>
      <input
        className="hidden"
        type="file"
        onChange={(event) => onChange(event.target.files?.[0] ?? null)}
      />
    </label>
  )
}

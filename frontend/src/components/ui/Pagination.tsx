interface PaginationProps {
  page: number
  totalPages: number
  onPageChange: (page: number) => void
}

export function Pagination({ page, totalPages, onPageChange }: PaginationProps) {
  return (
    <div className="flex items-center justify-end gap-2">
      <button
        className="rounded-md border border-white/15 px-3 py-1.5 text-sm text-app-muted disabled:opacity-40"
        disabled={page === 1}
        onClick={() => onPageChange(page - 1)}
        type="button"
      >
        Previous
      </button>
      <span className="text-sm text-app-muted">
        {page} / {totalPages}
      </span>
      <button
        className="rounded-md border border-white/15 px-3 py-1.5 text-sm text-app-muted disabled:opacity-40"
        disabled={page >= totalPages}
        onClick={() => onPageChange(page + 1)}
        type="button"
      >
        Next
      </button>
    </div>
  )
}

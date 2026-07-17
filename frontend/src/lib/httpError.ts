import axios from 'axios'

export function getErrorMessage(error: unknown, fallback: string) {
  if (axios.isAxiosError(error)) {
    const responseData = error.response?.data as
      | { message?: string; detail?: string; errors?: Array<{ defaultMessage?: string }> }
      | undefined

    const validationMessage = responseData?.errors?.[0]?.defaultMessage

    return responseData?.message ?? responseData?.detail ?? validationMessage ?? fallback
  }

  if (error instanceof Error) {
    return error.message
  }

  return fallback
}

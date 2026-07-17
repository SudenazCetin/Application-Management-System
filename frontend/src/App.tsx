import { AppProvider } from './context/AppProvider'
import { AppRouter } from './routes/AppRouter'

function App() {
  return (
    <AppProvider>
      <AppRouter />
    </AppProvider>
  )
}

export default App

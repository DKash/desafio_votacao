/* c8 ignore file */
import ReactDOM from 'react-dom/client';
import App from './App';
import { VotingProvider } from './contexts/VotingContext';
import './css/reset.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <VotingProvider>
    <App />
  </VotingProvider>
);
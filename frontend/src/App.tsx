import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Navbar } from './Navbar';
import { HeroSearch } from './HeroSearch';
import { Login } from './Login';
import { Register } from './Register';
import { BookRide } from './BookRide';

function LandingPage() {
  return (
    <div className="landing-page">
      <HeroSearch />
    </div>
  );
}

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/book-ride" element={<BookRide />} />
      </Routes>
    </Router>
  );
}

export default App;

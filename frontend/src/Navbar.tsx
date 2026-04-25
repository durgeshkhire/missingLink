import { useState, useEffect } from 'react';
import { Menu, X, Moon, Sun, User } from 'lucide-react';
import { FaMix } from "react-icons/fa6";
import { Link, useLocation, useNavigate } from 'react-router-dom';

export const Navbar = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isDark, setIsDark] = useState(false);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useState('User');
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    const name = localStorage.getItem('userName');
    setIsLoggedIn(!!token);
    if (name) setUserName(name);
  }, [location.pathname]);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleTheme = () => {
    const newIsDark = !isDark;
    setIsDark(newIsDark);
    if (newIsDark) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  };

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userName');
    setIsLoggedIn(false);
    setIsDropdownOpen(false);
    navigate('/');
  };

  return (
    <nav className="navbar" style={{
      boxShadow: isScrolled ? '0 4px 20px rgba(0,0,0,0.05)' : 'none'
    }}>
      <div className="container">
        <Link to="/" className="navbar-brand">
          <FaMix size={32} />
          <span>MissingLink</span>
        </Link>

        {/* Mobile Toggle Button */}
        <button 
          className="mobile-menu-btn" 
          onClick={toggleMobileMenu}
          aria-label="Toggle menu"
        >
          {isMobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
        </button>

        {/* Desktop & Mobile Menu */}
        <div className={`navbar-nav ${isMobileMenuOpen ? 'active' : ''}`}>
          <Link to="/book-ride" className="nav-link" onClick={() => setIsMobileMenuOpen(false)}>Book Ride</Link>
          <a href="#request" className="nav-link" onClick={() => setIsMobileMenuOpen(false)}>Request Ride</a>
          <Link to="/book-ride" className="nav-link" onClick={() => setIsMobileMenuOpen(false)}>Create Ride</Link>
          
          <button 
            onClick={toggleTheme} 
            className="theme-toggle-btn"
            aria-label="Toggle theme"
          >
            {isDark ? <Sun size={20} /> : <Moon size={20} />}
          </button>
          
          {isLoggedIn ? (
            <div className="profile-menu" style={{ position: 'relative' }}>
              <button 
                className="btn-outline" 
                style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }} 
                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
              >
                <div style={{ backgroundColor: 'var(--primary)', color: 'var(--primary-foreground)', borderRadius: '50%', width: '24px', height: '24px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.8rem' }}>
                  <User size={14} />
                </div>
                <span style={{ fontWeight: 600 }}>Profile • {userName}</span>
              </button>

              {isDropdownOpen && (
                <div className="dropdown-menu">
                  <Link to="/profile" className="dropdown-item" onClick={() => setIsDropdownOpen(false)}>My Profile</Link>
                  <Link to="/my-rides" className="dropdown-item" onClick={() => setIsDropdownOpen(false)}>My Rides</Link>
                  <div className="dropdown-divider"></div>
                  <button className="dropdown-item text-destructive" onClick={handleLogout}>Logout</button>
                </div>
              )}
            </div>
          ) : (
            <Link to="/login" className="btn-primary" style={{ textDecoration: 'none' }} onClick={() => setIsMobileMenuOpen(false)}>Login</Link>
          )}
        </div>
      </div>
    </nav>
  );
};

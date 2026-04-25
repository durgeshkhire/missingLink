import React, { useState } from 'react';
import { MapPin, Calendar, Users, Search, ArrowRightLeft } from 'lucide-react';

export const HeroSearch = () => {
  const [origin, setOrigin] = useState('Pune');
  const [destination, setDestination] = useState('Mumbai');
  const [date, setDate] = useState('');
  const [seats, setSeats] = useState('1');

  const handleSwap = () => {
    const temp = origin;
    setOrigin(destination);
    setDestination(temp);
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    console.log({ origin, destination, date, seats });
    // Proceed with search logic
  };

  return (
    <section className="hero">
      <div className="hero-bg-shapes">
        <div className="shape shape-1"></div>
        <div className="shape shape-2"></div>
      </div>
      
      <div className="hero-content">
        <h1 className="hero-title">Your Ride, Your Way.</h1>
        <p className="hero-subtitle">
          Experience premium intercity travel with MissingLink. 
          Book comfortable, reliable, and safe rides anytime.
        </p>
        
        <form className="search-widget" onSubmit={handleSearch}>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            {/* First Row: Locations */}
            <div className="search-row">
              <div className="form-group">
                <label htmlFor="origin">
                  <MapPin size={16} />
                  Origin
                </label>
                <input 
                  type="text" 
                  id="origin"
                  className="form-control" 
                  placeholder="City, Airport, etc."
                  value={origin}
                  onChange={(e) => setOrigin(e.target.value)}
                  required
                />
              </div>

              <div className="swap-icon-container" onClick={handleSwap} title="Swap locations">
                <ArrowRightLeft size={18} />
              </div>

              <div className="form-group">
                <label htmlFor="destination">
                  <MapPin size={16} />
                  Destination
                </label>
                <input 
                  type="text" 
                  id="destination"
                  className="form-control" 
                  placeholder="City, Airport, etc."
                  value={destination}
                  onChange={(e) => setDestination(e.target.value)}
                  required
                />
              </div>
            </div>
            
            {/* Second Row: Date & Seats */}
            <div className="search-row">
              <div className="form-group">
                <label htmlFor="date">
                  <Calendar size={16} />
                  Date
                </label>
                <input 
                  type="date" 
                  id="date"
                  className="form-control" 
                  value={date}
                  onChange={(e) => setDate(e.target.value)}
                  required
                />
              </div>

              <div className="form-group" style={{ minWidth: '100px', flex: '0.5' }}>
                <label htmlFor="seats">
                  <Users size={16} />
                  Seats
                </label>
                <select 
                  id="seats"
                  className="form-control"
                  value={seats}
                  onChange={(e) => setSeats(e.target.value)}
                >
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4+</option>
                </select>
              </div>
            </div>
          </div>
          
          <div style={{ display: 'flex', justifyContent: 'center', marginTop: '0.5rem' }}>
            <button type="submit" className="btn-primary search-btn" style={{ width: '100%', maxWidth: '300px' }}>
              <Search size={20} />
              Find Cabs
            </button>
          </div>
        </form>
      </div>
    </section>
  );
};

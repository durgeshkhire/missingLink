import { useState } from 'react';
import { FormEvent } from 'react';

export const BookRide = () => {
  const [formData, setFormData] = useState({
    originCity: 'Pune',
    destinationCity: 'Mumbai',
    originAddress: 'Shivajinagar Bus Stand',
    destinationAddress: 'Dadar Station',
    departureTime: '2026-05-01T08:00',
    totalSeats: 3,
    pricePerSeat: 350,
    description: 'AC car, no smoking',
    instantBooking: false
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { id, value, type } = e.target;
    // @ts-ignore
    const checked = e.target.checked;
    setFormData(prev => ({
      ...prev,
      [id]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    // Parse numeric fields for the payload
    const payload = {
      ...formData,
      totalSeats: Number(formData.totalSeats),
      pricePerSeat: Number(formData.pricePerSeat),
      departureTime: formData.departureTime.includes(':00') ? formData.departureTime : `${formData.departureTime}:00` // Formatting matching the prompt
    };
    console.log('BookRide Payload:', payload);
  };

  return (
    <div className="auth-page" style={{ padding: '100px 1.5rem 3rem 1.5rem' }}>
      <div className="hero-bg-shapes">
        <div className="shape shape-1"></div>
        <div className="shape shape-2"></div>
      </div>
      <div className="auth-card" style={{ maxWidth: '800px', width: '100%' }}>
        <h1 className="auth-title">Publish a Ride</h1>
        <p className="auth-subtitle">Offer a ride to passengers and share your journey.</p>
        
        <form className="auth-form" onSubmit={handleSubmit} style={{ textAlign: 'left' }}>
          
          <div className="search-row" style={{ display: 'flex', gap: '1.5rem', marginBottom: '1.25rem' }}>
            <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
              <label htmlFor="originCity">Origin City</label>
              <input 
                type="text" 
                id="originCity" 
                className="form-control" 
                value={formData.originCity}
                onChange={handleChange}
                required 
              />
            </div>
            <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
              <label htmlFor="destinationCity">Destination City</label>
              <input 
                type="text" 
                id="destinationCity" 
                className="form-control" 
                value={formData.destinationCity}
                onChange={handleChange}
                required 
              />
            </div>
          </div>

          <div className="search-row" style={{ display: 'flex', gap: '1.5rem', marginBottom: '1.25rem' }}>
            <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
              <label htmlFor="originAddress">Origin Address / Landmark</label>
              <input 
                type="text" 
                id="originAddress" 
                className="form-control" 
                value={formData.originAddress}
                onChange={handleChange}
                required 
              />
            </div>
            <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
              <label htmlFor="destinationAddress">Destination Address / Landmark</label>
              <input 
                type="text" 
                id="destinationAddress" 
                className="form-control" 
                value={formData.destinationAddress}
                onChange={handleChange}
                required 
              />
            </div>
          </div>

          <div className="search-row" style={{ display: 'flex', gap: '1.5rem', marginBottom: '1.25rem' }}>
            <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
              <label htmlFor="departureTime">Departure Time</label>
              <input 
                type="datetime-local" 
                id="departureTime" 
                className="form-control" 
                value={formData.departureTime}
                onChange={handleChange}
                required 
              />
            </div>
            <div className="form-group" style={{ flex: 0.5, marginBottom: 0 }}>
              <label htmlFor="totalSeats">Total Seats</label>
              <input 
                type="number" 
                id="totalSeats" 
                className="form-control" 
                min="1"
                value={formData.totalSeats}
                onChange={handleChange}
                required 
              />
            </div>
            <div className="form-group" style={{ flex: 0.5, marginBottom: 0 }}>
              <label htmlFor="pricePerSeat">Price / Seat (₹)</label>
              <input 
                type="number" 
                id="pricePerSeat" 
                className="form-control" 
                min="0"
                value={formData.pricePerSeat}
                onChange={handleChange}
                required 
              />
            </div>
          </div>

          <div className="form-group" style={{ marginBottom: '1.25rem' }}>
            <label htmlFor="description">Wait/Ride Description</label>
            <textarea 
              id="description" 
              className="form-control" 
              rows={3}
              value={formData.description}
              onChange={handleChange}
              style={{ resize: 'vertical' }}
            ></textarea>
          </div>

          <div className="form-group" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.5rem', padding: '1.25rem', backgroundColor: 'var(--card)', border: '1px solid var(--border)', borderRadius: 'var(--radius)' }}>
            <div>
              <label htmlFor="instantBooking" style={{ margin: 0, fontWeight: 600, display: 'block', fontSize: '1rem', color: 'var(--foreground)' }}>
                Instant Booking
              </label>
              <span style={{ fontSize: '0.85rem', color: 'var(--muted-foreground)' }}>Auto-approve passengers without review</span>
            </div>
            <label className="toggle-switch">
              <input 
                type="checkbox" 
                id="instantBooking" 
                checked={formData.instantBooking}
                onChange={handleChange}
              />
              <span className="toggle-slider"></span>
            </label>
          </div>

          <div style={{ display: 'flex', justifyContent: 'center' }}>
            <button type="submit" className="btn-primary auth-btn" style={{ maxWidth: '300px' }}>
              Publish Ride
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

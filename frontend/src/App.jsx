
import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [services, setServices] = useState([])
  const [name, setName] = useState('')
  const [url, setUrl] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [error, setError] = useState('')
  useEffect(() => {
    const fetchServices = () => {
      fetch('http://localhost:8080/api/services')
          .then(response => {
            if (!response.ok) {
              throw new Error('Failed to fetch services')
            }
            return response.json()
          })
          .then(data => {
            setServices(data)
          })
          .catch(error => {
            console.error('Error fetching services:', error)
          })
    }

    fetchServices()

    const interval = setInterval(fetchServices, 5000)

    return () => clearInterval(interval)
  }, [])
  const addService = async () => {

    setError('')
    try {
      const response = await fetch('http://localhost:8080/api/services', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, url }),
      })

      if (!response.ok) {
        const errorData = await response.json()
        console.log('Backend error response:', JSON.stringify(errorData, null, 2))
        throw new Error(
            errorData.detail || errorData.message || errorData.error || 'Failed to add service'
        )
      }

      const newService = await response.json()

      setServices([...services, newService])
      setName('')
      setUrl('')
      return true
    } catch (err) {
      console.error('Error adding service:', err)
      setError(err.message)
      return false
    }
  }
  const deleteService = async (id) => {
    try {
      const response = await fetch(
          `http://localhost:8080/api/services/${id}`,
          {
            method: 'DELETE',
          }
      )

      if (!response.ok) {
        throw new Error('Failed to delete service')
      }

      setServices(services.filter(service => service.id !== id))
    } catch (error) {
      console.error('Error deleting service:', error)
    }
  }
  const updateService = async (id, updatedName, updatedUrl) => {
    try {
      const response = await fetch(
          `http://localhost:8080/api/services/${id}`,
          {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              name: updatedName,
              url: updatedUrl,
            }),
          }
      )

      if (!response.ok) {
        const errorData = await response.json()

        console.log('Backend error response:', JSON.stringify(errorData, null, 2))

        throw new Error(
            errorData.detail || errorData.message || errorData.error || 'Failed to update service'
        )
      }

      const updatedService = await response.json()

      setServices(services.map(service =>
          service.id === id ? updatedService : service
      ))
      return true

    } catch (error) {
      console.error('Error updating service:', error)
      setError(error.message)
      return false
    }
  }
  const startEditing = (service) => {
    setEditingId(service.id)
    setName(service.name)
    setUrl(service.url)

    document.getElementById('add-service-form').style.display = 'block'
  }
  return (
      <div className="app">
        <header className="header">
          <div>
            <h1>Service Monitor</h1>
            <p>Monitor and manage your services</p>
          </div>
          <button
              className="add-button"
              onClick={() => document.getElementById('add-service-form').style.display = 'block'}
          >
            + Add service
          </button>
        </header>

        <main>
          <div id="add-service-form" className="form-container">
            <h2>Add new service</h2>{error && (
              <p className="error-message">{error}</p>
          )}

            <input
                type="text"
                placeholder="Service name"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />

            <input
                type="text"
                placeholder="Service URL (https://example.com)"
                value={url}
                onChange={(e) => setUrl(e.target.value)}
            />

            <button
                onClick={async () => {
                  let success

                  if (editingId !== null) {
                    success = await updateService(editingId, name, url)
                  } else {
                    success = await addService()
                  }

                  if (success) {
                    setEditingId(null)
                    setName('')
                    setUrl('')
                    setError('')
                    document.getElementById('add-service-form').style.display = 'none'
                  }
                }}
            >
              Save service
            </button>
          </div>
          <section className="stats">
            <div className="stat-card">
              <span>Total services</span>
              <h2>{services.length}</h2>
            </div>

            <div className="stat-card">
              <span>Services UP</span>
              <h2>{services.filter(service => service.status === 'UP').length}</h2>
            </div>

            <div className="stat-card">
              <span>Services DOWN</span>
              <h2>{services.filter(service => service.status === 'DOWN').length}</h2>
            </div>
          </section>

          <section className="service-section">
            <h2>Monitored services</h2>

            <table>
              <thead>
              <tr>
                <th>Name</th>
                <th>URL</th>
                <th>Status</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tbody>
              {services.length === 0 ? (
                  <tr>
                    <td colSpan="5">No services added yet.</td>
                  </tr>
              ) : (
                  services.map((service) => (
                      <tr key={service.id}>
                        <td>{service.name}</td>
                        <td>{service.url}</td>
                        <td>
  <span className={`status-badge ${service.status.toLowerCase()}`}>
    {service.status}
  </span>
                        </td>
                        <td>{service.createdAt}</td><td>
                        <button
                            className="edit-button"
                            onClick={() => startEditing(service)}
                        >
                          Edit
                        </button>

                        <button
                            className="delete-button"
                            onClick={() => deleteService(service.id)}
                        >
                          Delete
                        </button>
                      </td>
                      </tr>
                  ))
              )}
              </tbody>
            </table>
          </section>
        </main>
      </div>
  )
}

export default App

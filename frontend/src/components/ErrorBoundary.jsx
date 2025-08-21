import React from 'react';

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null, errorInfo: null };
  }

  static getDerivedStateFromError() {
    // Update state so the next render will show the fallback UI
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    // Log error details
    console.error('ErrorBoundary caught an error:', error, errorInfo);
    this.setState({
      error: error,
      errorInfo: errorInfo
    });
  }

  render() {
    if (this.state.hasError) {
      // Custom error UI
      return (
        <div className="error-boundary-container">
          <div className="container py-5">
            <div className="row justify-content-center">
              <div className="col-md-8 col-lg-6">
                <div className="card">
                  <div className="card-body text-center">
                    <div className="mb-4">
                      <i className="bi bi-exclamation-triangle text-danger" style={{ fontSize: '4rem' }}></i>
                    </div>
                    <h2 className="card-title text-danger mb-3">Oops! Something went wrong</h2>
                    <p className="card-text text-muted mb-4">
                      We're sorry, but something unexpected happened. Please try refreshing the page.
                    </p>
                    <div className="d-grid gap-2">
                      <button 
                        className="btn btn-primary"
                        onClick={() => window.location.reload()}
                      >
                        <i className="bi bi-arrow-clockwise me-2"></i>
                        Refresh Page
                      </button>
                      <button 
                        className="btn btn-outline-secondary"
                        onClick={() => window.location.href = '/'}
                      >
                        <i className="bi bi-house me-2"></i>
                        Go Home
                      </button>
                    </div>
                    
                    {/* Show error details in development */}
                    {import.meta.env.DEV && (
                      <details className="mt-4 text-start">
                        <summary className="text-danger cursor-pointer">
                          <small>Error Details (Development)</small>
                        </summary>
                        <pre className="mt-2 p-3 bg-light rounded small">
                          {this.state.error && this.state.error.toString()}
                          <br />
                          {this.state.errorInfo && this.state.errorInfo.componentStack}
                        </pre>
                      </details>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      );
    }

    // No error, render children normally
    return this.props.children;
  }
}

export default ErrorBoundary;

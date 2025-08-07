import React from 'react';

const Contact = () => {
  return (
    <div className="container py-5">
      <h1 className="text-center mb-5">Contact Us</h1>
      <div className="row">
        <div className="col-lg-8 mx-auto">
          <form>
            <div className="row g-3">
              <div className="col-md-6">
                <label className="form-label">First Name</label>
                <input type="text" className="form-control" />
              </div>
              <div className="col-md-6">
                <label className="form-label">Last Name</label>
                <input type="text" className="form-control" />
              </div>
              <div className="col-12">
                <label className="form-label">Email</label>
                <input type="email" className="form-control" />
              </div>
              <div className="col-12">
                <label className="form-label">Subject</label>
                <input type="text" className="form-control" />
              </div>
              <div className="col-12">
                <label className="form-label">Message</label>
                <textarea className="form-control" rows="5"></textarea>
              </div>
              <div className="col-12 text-center">
                <button type="submit" className="btn btn-primary">Send Message</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Contact;

import React from 'react';
import './FooterComponent.css';
import Logo from '../assets/images/logo/TourVerseLogo.png';
import footerBackground from '../assets/images/footer/footer_bg.jpg';

function FooterComponent() {
  return (
    <footer>
      <div
        className="footer-area footer-padding footer-bg pt-3"
        style={{ backgroundImage: `url(${footerBackground})` }}
      >
        <div className="container">
          <div className="row d-flex justify-content-between">
            <div className="col-lg-4 col-md-6 col-12 mb-lg-0">
              <div className="single-footer-caption">
                <div className="footer-logo text-center">
                  <a href="/">
                    <img src={Logo} alt="TourVerse" style={{ height: '110px', width: '130px', marginTop: '-25px', padding: '0' }} />
                  </a>
                </div>
                <div className='text-center'>
                  <div className="row align-items-center pb-3">
                    <div >
                      <div className="footer-social">
                        <a href="#"><i className="bi bi-twitter"></i></a>
                        <a href="#"><i className="bi bi-facebook"></i></a>
                        <a href="#" style={{ marginRight: "5px" }}><i className="bi bi-globe"></i></a>
                      </div>
                    </div>
                    <div >
                      <div className="footer-copy-right mt-3">
                        <p>
                          &copy; {new Date().getFullYear()} TourVerse. All rights reserved</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="col-xl-2 col-lg-3 col-md-3 col-sm-5">
              <div className="single-footer-caption">
                <div className="footer-tittle">
                  <h4>Quick Links</h4>
                  <ul className="list-unstyled">
                    <li><a href="#">About</a></li>
                    <li><a href="#">Offers & Discounts</a></li>
                    <li><a href="#">Get Coupon</a></li>
                    <li><a href="#">Contact Us</a></li>
                  </ul>
                </div>
              </div>
            </div>
            <div className="col-xl-3 col-lg-3 col-md-5 col-sm-7">
              <div className="single-footer-caption">
                <div className="footer-tittle">
                  <h4>Support</h4>
                  <ul className="list-unstyled">
                    <li><a href="#">Frequently Asked Questions</a></li>
                    <li><a href="#">Terms & Conditions</a></li>
                    <li><a href="#">Privacy Policy</a></li>
                    <li><a href="#">Report a Payment Issue</a></li>
                  </ul>
                </div>
              </div>
            </div>

          </div>
          {/* Footer bottom */}

        </div>
      </div>
    </footer>
  );
}

export default FooterComponent;
import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './HeaderComponent.css'; // custom CSS

function HeaderComponent() {
    return (
        <>
            <nav className="navbar navbar-expand-lg text-light custom-navbar sticky-top" style={{ backgroundColor: "#90cbeeff" }}>
                <div className="container-fluid">
                    <div className="container collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0 help">
                            <li className="nav-item">
                                <a className="nav-link text-light header-link" href="mailto:needhelp07@tourverse.com">
                                    <i className="bi bi-envelope-fill me-1"></i>
                                    needhelp07@tourverse.com
                                </a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link text-light header-link" href="tel:7666736126">
                                    <i className="bi bi-telephone-fill me-1"></i>
                                    7666736126
                                </a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link text-light header-link" href="#">
                                    <i className="bi bi-geo-alt-fill me-1"></i>
                                    Maharashtra
                                </a>
                            </li>
                        </ul>
                        <div className="d-flex gap-2 social">
                            <a href="#" className="nav-link text-light  header-link">
                                <i className="bi bi-facebook"></i>
                            </a>
                            <a href="#" className="nav-link text-light header-link">
                                <i className="bi bi-twitter"></i>
                            </a>
                            <a href="#" className="nav-link text-light header-link" style={{ marginRight: "5px" }}>
                                <i className="bi bi-instagram" ></i>
                            </a>
                        </div>
                    </div>
                </div>
            </nav>
        </>
    )
}

export default HeaderComponent;

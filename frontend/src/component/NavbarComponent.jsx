import React from 'react'
import Logo from '../assets/images/logo/TourVerseLogo.png';
import { Link } from 'react-router-dom';

function NavbarComponent() {
    return (
        <>
            <style>
                {`
            .menu a:hover {
                color: orange;
            }
            `}
            </style>

            <nav className="navbar navbar-expand-lg sticky-top" style={{ height: "80px", backgroundColor: "#84d2d2ff" }}>
                <div className="container">
                    <a className="navbar-brand" href="/">
                        <img src={Logo} alt="TourVerse" height={100} />
                    </a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav mx-auto mb-2 mb-lg-0 gap-4 menu" style={{ fontSize: "1.2rem", fontWeight: "500" }}>
                            <li className="nav-item">
                                <a className="nav-link" aria-current="page" href="/">Explore</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/about">About</a>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/register">Register</Link>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/login">Login</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/contact">Contact</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </>
    )
}

export default NavbarComponent
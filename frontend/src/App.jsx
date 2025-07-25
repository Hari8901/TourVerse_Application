import React from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import HeaderComponent from './component/HeaderComponent';
import NavbarComponent from './component/NavbarComponent';
import HomePage from './pages/HomePage';
import AboutPage from './pages/AboutPage';
import ContactPage from './pages/ContactPage';
import LoginUserPage from './pages/LoginUserPage';
import RegisterUserPage from './pages/RegisterUserPage';
import FooterComponent from './component/FooterComponent';
import './App.css'

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/register"
          element={<RegisterUserPage />}
        />
        <Route
          path="/login"
          element={<LoginUserPage />}
        />
        <Route
          path="*"
          element={
            <>
              <HeaderComponent />
              <NavbarComponent />
              <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/about" element={<AboutPage />} />
                <Route path="/contact" element={<ContactPage />} />
              </Routes>
              <FooterComponent />
            </>
          }
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App

// Test script to check profile functionality
console.log('Testing Profile Page functionality...');

// Test if main components are loading
setTimeout(() => {
    console.log('Checking for common console errors...');
    
    // Check if React is loaded
    if (typeof React !== 'undefined') {
        console.log('✅ React is loaded');
    } else {
        console.log('❌ React not found');
    }
    
    // Check if profile page elements exist
    const profileElements = document.querySelectorAll('[class*="profile"], [class*="Profile"]');
    if (profileElements.length > 0) {
        console.log('✅ Profile elements found:', profileElements.length);
    } else {
        console.log('❌ No profile elements found');
    }
    
    // Check for error elements
    const errorElements = document.querySelectorAll('.text-danger, .is-invalid, .alert-danger');
    if (errorElements.length > 0) {
        console.log('⚠️ Error elements found:', errorElements.length);
        errorElements.forEach((el, index) => {
            console.log(`Error ${index + 1}:`, el.textContent.trim());
        });
    } else {
        console.log('✅ No error elements found');
    }
    
    // Check localStorage
    const user = localStorage.getItem('user');
    const token = localStorage.getItem('jwt_token');
    
    console.log('User in localStorage:', user ? 'Found' : 'Not found');
    console.log('Token in localStorage:', token ? 'Found' : 'Not found');
    
}, 2000);

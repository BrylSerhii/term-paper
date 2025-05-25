document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const loginBtn = document.getElementById('login-btn');
    const registerBtn = document.getElementById('register-btn');
    const getStartedBtn = document.getElementById('get-started-btn');
    const learnMoreBtn = document.getElementById('learn-more-btn');
    const loginModal = document.getElementById('login-modal');
    const registerModal = document.getElementById('register-modal');
    const closeModalButtons = document.querySelectorAll('.close-modal');
    const switchToRegister = document.getElementById('switch-to-register');
    const switchToLogin = document.getElementById('switch-to-login');
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    // Initialize the application
    console.log('Study Platform frontend initialized');

    // Check for session expired message
    function checkSessionStatus() {
        const urlParams = new URLSearchParams(window.location.search);
        const sessionStatus = urlParams.get('session');

        if (sessionStatus === 'expired') {
            alert('Your session has expired. Please log in again.');
            // Remove the query parameter from the URL without reloading the page
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }

    // Check session status on page load
    checkSessionStatus();

    // Utility function to make authenticated API requests
    function fetchWithAuth(url, options = {}) {
        const token = localStorage.getItem('token');
        if (!token) {
            return fetch(url, options);
        }

        // Clone the options to avoid modifying the original object
        const authOptions = { ...options };

        // Initialize headers if they don't exist
        authOptions.headers = authOptions.headers || {};

        // Add Authorization header with the token
        authOptions.headers = {
            ...authOptions.headers,
            'Authorization': `Bearer ${token}`
        };

        return fetch(url, authOptions);
    }

    // Function to open modal
    function openModal(modal) {
        modal.classList.add('active');
    }

    // Function to close modal
    function closeModal(modal) {
        modal.classList.remove('active');
    }

    // Function to close all modals
    function closeAllModals() {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(modal => {
            closeModal(modal);
        });
    }

    // Event Listeners for opening modals
    loginBtn.addEventListener('click', function(e) {
        e.preventDefault();
        openModal(loginModal);
    });

    registerBtn.addEventListener('click', function(e) {
        e.preventDefault();
        openModal(registerModal);
    });

    getStartedBtn.addEventListener('click', function() {
        openModal(registerModal);
    });

    learnMoreBtn.addEventListener('click', function() {
        // Scroll to features section
        document.querySelector('.features').scrollIntoView({ behavior: 'smooth' });
    });

    // Event Listeners for closing modals
    closeModalButtons.forEach(button => {
        button.addEventListener('click', function() {
            closeAllModals();
        });
    });

    // Event Listeners for switching between modals
    switchToRegister.addEventListener('click', function(e) {
        e.preventDefault();
        closeModal(loginModal);
        openModal(registerModal);
    });

    switchToLogin.addEventListener('click', function(e) {
        e.preventDefault();
        closeModal(registerModal);
        openModal(loginModal);
    });

    // Close modal when clicking outside
    window.addEventListener('click', function(e) {
        if (e.target.classList.contains('modal')) {
            closeAllModals();
        }
    });

    // Handle login form submission
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const username = document.getElementById('login-username').value;
        const password = document.getElementById('login-password').value;

        // Create request payload
        const payload = {
            username: username,
            password: password
        };

        // Send login request
        fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                // Show error message
                alert(data.message || 'Login failed. Please check your credentials.');
            } else {
                // Store token in localStorage
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', data.username);

                // Close modal
                closeAllModals();

                // Update UI for logged in user
                updateUIForLoggedInUser(data.username);

                // Show success message
                alert('Login successful!');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred during login. Please try again.');
        });
    });

    // Handle register form submission
    registerForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const username = document.getElementById('register-username').value;
        const email = document.getElementById('register-email').value;
        const password = document.getElementById('register-password').value;

        // Validate password
        if (password.length < 6) {
            alert('Password must be at least 6 characters.');
            return;
        }

        if (!/^[a-zA-Z0-9]+$/.test(password)) {
            alert('Password must contain only English letters and digits.');
            return;
        }

        // Create request payload
        const payload = {
            username: username,
            email: email,
            password: password
        };

        // Send register request
        fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                // Show error message
                alert(data.message || 'Registration failed. Please try again.');
            } else if (data.emailVerificationRequired) {
                // Close modal
                closeAllModals();

                // Show verification message
                alert(data.message || 'Registration successful! Please check your email to verify your account before logging in.');
            } else {
                // Store token in localStorage (for backward compatibility)
                if (data.token) {
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('username', data.username);

                    // Update UI for logged in user
                    updateUIForLoggedInUser(data.username);
                }

                // Close modal
                closeAllModals();

                // Show success message
                alert('Registration successful!');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred during registration. Please try again.');
        });
    });

    // Function to update UI for logged in user
    function updateUIForLoggedInUser(username) {
        // Hide login and register buttons
        loginBtn.style.display = 'none';
        registerBtn.style.display = 'none';

        // Create user info and logout button
        const navList = document.querySelector('.nav-list');

        // Create dashboard link
        const dashboardItem = document.createElement('li');
        const dashboardLink = document.createElement('a');
        dashboardLink.href = 'dashboard.html';
        dashboardLink.className = 'nav-link';
        dashboardLink.textContent = 'Dashboard';
        dashboardItem.appendChild(dashboardLink);

        // Create user info element
        const userInfoItem = document.createElement('li');
        const userInfoLink = document.createElement('a');
        userInfoLink.href = 'dashboard.html';
        userInfoLink.className = 'nav-link';
        userInfoLink.textContent = `Welcome, ${username}`;
        userInfoItem.appendChild(userInfoLink);

        // Create logout button
        const logoutItem = document.createElement('li');
        const logoutLink = document.createElement('a');
        logoutLink.href = '#';
        logoutLink.className = 'nav-link';
        logoutLink.textContent = 'Logout';
        logoutLink.addEventListener('click', function(e) {
            e.preventDefault();

            // Clear localStorage
            localStorage.removeItem('token');
            localStorage.removeItem('username');

            // Reload page to reset UI
            window.location.reload();
        });
        logoutItem.appendChild(logoutLink);

        // Add elements to nav
        navList.appendChild(dashboardItem);
        navList.appendChild(userInfoItem);
        navList.appendChild(logoutItem);
    }

    // Function to check if user is authenticated
    function checkAuthentication() {
        const token = localStorage.getItem('token');
        const username = localStorage.getItem('username');

        if (!token || !username) {
            return;
        }

        // Verify token is valid by making an authenticated request
        fetchWithAuth('/api/auth/validate-token')
            .then(response => {
                if (response.ok) {
                    return response.json().then(data => {
                        if (data.valid) {
                            // Token is valid, update UI
                            updateUIForLoggedInUser(data.username);
                            console.log('User authenticated:', data.username);
                        } else {
                            // Token is invalid, clear localStorage
                            localStorage.removeItem('token');
                            localStorage.removeItem('username');
                            console.log('Token validation failed');
                        }
                    });
                } else {
                    // Token is invalid, clear localStorage
                    localStorage.removeItem('token');
                    localStorage.removeItem('username');
                    throw new Error('Invalid token');
                }
            })
            .catch(error => {
                console.error('Authentication check failed:', error);
            });
    }

    // Check if user is already logged in
    checkAuthentication();

    // Function to check API health
    function checkApiHealth() {
        fetchWithAuth('/api/health')
            .then(response => {
                if (!response.ok) {
                    throw new Error('API health check failed');
                }
                return response.json();
            })
            .then(data => {
                console.log('API Health Status:', data.status);
                if (data.status === 'UP') {
                    console.log('API is running:', data.message);
                }
            })
            .catch(error => {
                console.error('API Health Check Error:', error);
            });
    }

    // Check API health on page load
    checkApiHealth();
});

document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const logoutBtn = document.getElementById('logout-btn');
    const userDetails = document.getElementById('user-details');

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

    // Function to check if user is authenticated
    function checkAuthentication() {
        const token = localStorage.getItem('token');
        
        if (!token) {
            // Redirect to login page if no token
            window.location.href = 'index.html';
            return;
        }

        // Verify token is valid by making an authenticated request
        fetchWithAuth('/api/auth/validate-token')
            .then(response => {
                if (response.ok) {
                    return response.json().then(data => {
                        if (data.valid) {
                            // Token is valid, display user information
                            displayUserInfo(data);
                        } else {
                            // Token is invalid, redirect to login
                            redirectToLogin();
                        }
                    });
                } else {
                    // Token is invalid, redirect to login
                    redirectToLogin();
                }
            })
            .catch(error => {
                console.error('Authentication check failed:', error);
                redirectToLogin();
            });
    }

    // Function to display user information
    function displayUserInfo(userData) {
        // Create user info HTML
        const userInfoHTML = `
            <p><strong>Username:</strong> ${userData.username}</p>
            <p><strong>Email:</strong> ${userData.email}</p>
            <p><strong>Role:</strong> ${userData.role}</p>
        `;
        
        // Update user details element
        userDetails.innerHTML = userInfoHTML;
        
        // Update page title with username
        document.querySelector('.dashboard-title').textContent = `Welcome to Your Dashboard, ${userData.username}`;
    }

    // Function to redirect to login page
    function redirectToLogin() {
        // Clear localStorage
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        
        // Redirect to login page
        window.location.href = 'index.html';
        
        // Add a query parameter to show a message
        window.location.href = 'index.html?session=expired';
    }

    // Handle logout button click
    logoutBtn.addEventListener('click', function(e) {
        e.preventDefault();
        
        // Clear localStorage
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        
        // Redirect to home page
        window.location.href = 'index.html';
    });

    // Check authentication when page loads
    checkAuthentication();
});
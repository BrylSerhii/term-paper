document.addEventListener('DOMContentLoaded', function() {
    // Initialize the application
    console.log('Study Platform frontend initialized');

    // Event listeners for login and register buttons
    document.getElementById('login-button').addEventListener('click', function() {
        console.log('Login button clicked');
        alert('Login functionality will be implemented in the future.');
        // Show login form or navigate to login page
    });

    document.getElementById('register-button').addEventListener('click', function() {
        console.log('Register button clicked');
        // Navigate to registration page
        window.location.href = '/register.html';
    });

    // Function to check API health
    function checkApiHealth() {
        fetch('/api/health')
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

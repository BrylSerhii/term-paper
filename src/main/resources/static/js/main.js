document.addEventListener('DOMContentLoaded', function() {
    // Initialize the application
    console.log('Study Platform frontend initialized');

    // Event listeners for navigation
    document.getElementById('home-link').addEventListener('click', function(e) {
        e.preventDefault();
        console.log('Home link clicked');
        // Navigate to home page
    });

    document.getElementById('courses-link').addEventListener('click', function(e) {
        e.preventDefault();
        console.log('Courses link clicked');
        // Navigate to courses page or fetch courses
        fetchCourses();
    });

    document.getElementById('login-link').addEventListener('click', function(e) {
        e.preventDefault();
        console.log('Login link clicked');
        // Show login form or navigate to login page
    });

    document.getElementById('register-link').addEventListener('click', function(e) {
        e.preventDefault();
        console.log('Register link clicked');
        // Show registration form or navigate to registration page
    });

    document.getElementById('explore-courses').addEventListener('click', function() {
        console.log('Explore courses button clicked');
        // Scroll to courses section or navigate to courses page
        document.getElementById('featured-courses').scrollIntoView({ behavior: 'smooth' });
        fetchCourses();
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

    // Function to fetch courses from the API
    function fetchCourses() {
        const coursesContainer = document.getElementById('courses-container');
        coursesContainer.innerHTML = '<div class="course-card"><h3>Loading courses...</h3></div>';

        // First check if the API is available
        fetch('/api/health')
            .then(response => {
                if (!response.ok) {
                    throw new Error('API is not available');
                }
                return response.json();
            })
            .then(healthData => {
                // If API is up, try to fetch courses
                if (healthData.status === 'UP') {
                    return fetch('/api/courses')
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Courses endpoint not available');
                            }
                            return response.json();
                        });
                } else {
                    throw new Error('API is not ready');
                }
            })
            .then(data => {
                displayCourses(data);
            })
            .catch(error => {
                console.error('Error fetching courses:', error);
                coursesContainer.innerHTML = `
                    <div class="course-card">
                        <h3>Using sample data</h3>
                        <p>The API is still in development. Showing sample courses.</p>
                    </div>
                `;

                // For demonstration purposes, show sample courses when API fails
                setTimeout(() => {
                    displaySampleCourses();
                }, 1000);
            });
    }

    // Function to display courses in the UI
    function displayCourses(courses) {
        const coursesContainer = document.getElementById('courses-container');

        if (courses.length === 0) {
            coursesContainer.innerHTML = `
                <div class="course-card">
                    <h3>No courses available</h3>
                    <p>Check back later for new courses.</p>
                </div>
            `;
            return;
        }

        let coursesHTML = '';
        courses.forEach(course => {
            coursesHTML += `
                <div class="course-card">
                    <h3>${course.title}</h3>
                    <p>${course.description.substring(0, 100)}...</p>
                    <p>Created by: ${course.createdBy.username}</p>
                    <button class="enroll-button" data-course-id="${course.id}">Enroll Now</button>
                </div>
            `;
        });

        coursesContainer.innerHTML = coursesHTML;

        // Add event listeners to enroll buttons
        document.querySelectorAll('.enroll-button').forEach(button => {
            button.addEventListener('click', function() {
                const courseId = this.getAttribute('data-course-id');
                enrollInCourse(courseId);
            });
        });
    }

    // Function to display sample courses when API is not available
    function displaySampleCourses() {
        const sampleCourses = [
            {
                id: 1,
                title: 'Introduction to Programming',
                description: 'Learn the basics of programming with this comprehensive course designed for beginners.',
                createdBy: { username: 'admin' }
            },
            {
                id: 2,
                title: 'Web Development Fundamentals',
                description: 'Master HTML, CSS, and JavaScript to build modern and responsive websites.',
                createdBy: { username: 'admin' }
            },
            {
                id: 3,
                title: 'Data Science Essentials',
                description: 'Explore data analysis, visualization, and machine learning techniques.',
                createdBy: { username: 'admin' }
            }
        ];

        displayCourses(sampleCourses);
    }

    // Function to handle course enrollment
    function enrollInCourse(courseId) {
        console.log(`Enrolling in course with ID: ${courseId}`);
        // In a real application, this would make an API call to enroll the user
        alert(`You have successfully enrolled in the course!`);
    }

    // Check API health on page load
    checkApiHealth();

    // Load sample courses on initial page load (for demonstration)
    setTimeout(() => {
        displaySampleCourses();
    }, 1000);
});

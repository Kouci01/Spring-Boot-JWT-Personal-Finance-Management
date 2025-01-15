// Handle login form submission
document.getElementById('login-form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // You can replace this with your login logic
    console.log('Login attempted with:', email, password);

    const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({email, password})
    });

    const data = await response.json();
    sessionStorage.setItem("jwt", data.token);
    if (response.ok) {
        alert('Login successful');
        window.location.href = "index.html";
    } else {
        alert('Failed to login, please check you email and password');
    }
});

// Google Login functionality
async function loginWithGoogle() {
    window.location.href = "http://localhost:8080/oauth2/authorization/google"
}
//
// // // Initialize Google Sign-In (you can replace this with real OAuth2.0 logic)
// // function initGoogleSignIn() {
// //     gapi.load('auth2', function() {
// //         gapi.auth2.init({
// //             client_id: 'YOUR_GOOGLE_CLIENT_ID'
// //         });
// //     });
// // }
// //
// // initGoogleSignIn();

// Handle register form submission
document.getElementById('register-form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const name = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    // Basic password check
    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }

    console.log('Registering:', email, password);
    // You can replace this with your registration logic
    const response = await fetch("http://localhost:8080/auth/signup", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({name, email, password})
        // mode: "cors"
    });

    if (response.ok) {
        alert('Registration successful');
        window.location.href = "login.html";
    } else {
        alert('Registration failed, email already used');
    }

});
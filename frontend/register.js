const registerForm = document.getElementById("registerForm");

if(registerForm){
    registerForm.addEventListener("submit", function(e){
        e.preventDefault();

        const fullName = document.querySelector('input[placeholder="Full Name"]').value;
        const rollNo = document.querySelector('input[placeholder="Roll no / Employee ID"]').value;
        const email = document.querySelector('input[placeholder="Email Address"]').value;
        const password = document.getElementById("regPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        if(password !== confirmPassword){
            alert("Passwords do not match!");
            return;
        }

        const userData = {
            fullName,
            rollNo,
            email,
            password
        };

        localStorage.setItem("registeredUser", JSON.stringify(userData));

        alert("Registration Successful ✅");

        window.location.href = "login.html";
    });
}
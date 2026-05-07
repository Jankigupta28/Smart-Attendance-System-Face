let registerType = "user";
/* SWITCH USER / ADMIN */
function switchLogin(type) {

    registerType = type;

    const buttons = document.querySelectorAll(".tab-btn");

    buttons.forEach(btn =>
        btn.classList.remove("active")
    );

    if (type === "user") {
        buttons[0].classList.add("active");
    }
    else {
        buttons[1].classList.add("active");
    }
}
const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const fullName = document.querySelector('input[placeholder="Full Name"]').value;
        const rollNo = document.querySelector('input[placeholder="Roll no / Employee ID"]').value;
        const email = document.querySelector('input[placeholder="Email Address"]').value;
        const password = document.getElementById("regPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        if (password !== confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        let users =
            JSON.parse(localStorage.getItem("registeredUsers")) || [];

        const alreadyExists = users.some(user =>
            user.email === email || user.rollNo === rollNo
        );

        if (alreadyExists) {
            alert("User already registered!");
          
        return;
        }

        const userData = {
            fullName,
            rollNo,
            email,
            password,
            role: registerType
        };

        users.push(userData);

        localStorage.setItem(
            "registeredUsers",
            JSON.stringify(users)
        );

        localStorage.setItem(
            "currentUser",
            JSON.stringify(userData)
        );

        alert("Registration Successful ✅");

        if (registerType === "user") {
            window.location.href = "face-register.html";
        }
        else {
            window.location.href = "login.html";
        }
    });
}
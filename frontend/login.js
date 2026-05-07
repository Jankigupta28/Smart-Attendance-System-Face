let loginType = "user";

/* SWITCH LOGIN ROLE */
function switchLogin(type) {
    loginType = type;
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
/* LOGIN FORM */
const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        /* EMPTY CHECK */
        if (email === "" || password === "") {
            alert("Please fill all fields!");
            return;
        }

        let users = JSON.parse(localStorage.getItem("registeredUsers")) || [];
        /* FIND USER */
        const validUser = users.find(user =>
            user.email === email &&
            user.password === password &&
            user.role === loginType
        );
        /* INVALID */
        if (!validUser) {
            alert("Invalid Credentials or Wrong Role!");
            return;
        }
        /* FACE CHECK */
        if (!validUser.faceRegistered && validUser.role === "user") {
            alert("Please complete face registration first!");
            return;
        }
        /* SAVE CURRENT USER */
        localStorage.setItem(
            "currentUser",
            JSON.stringify(validUser)
        );

        /* ROLE BASED LOGIN */
        if (validUser.role === "admin") {
            alert("Admin Login Successful ✅")
            window.location.href = "admin-dashboard.html";
        }
        else {
            alert("Student Login Successful ✅");
            window.location.href = "dashboard.html";
        }
    });
}
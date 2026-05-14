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
  fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email: email, password: password })
})

.then(res => {
    if (!res.ok) {
        return res.text().then(text => { throw new Error(text) });
    }
    return res.json();
})

.then(data => {
    if (data.status === "Login success") {
        // Use 'userId' consistently
      
        localStorage.setItem("userId", data.userId);
        localStorage.setItem("teacherName", data.name);  // ⭐ ADD THIS
        
        console.log("Saved userId:", localStorage.getItem("userId"));
        if (data.role === "TEACHER") {
            window.location.href = "admin-dashboard.html";
        } else {
            window.location.href = "dashboard.html";
        }
    } else {
        alert("Login failed: " + (data.message || "Invalid credentials"));
    }
})
.catch(err => {
    console.error("Login Error:", err);
    alert("Login error: " + err.message);
});
    });
}
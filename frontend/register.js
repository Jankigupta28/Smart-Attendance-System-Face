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

        if (registerType === "user") {
            fetch("http://localhost:8080/students", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    enrollmentNumber: rollNo,
                    name: fullName,
                    email: email,
                    password: password,
                    department: "CS",
                    semester: 1
                })
            })
                // .then(res => res.text())
                .then(res => {
                    if (!res.ok) {
                        throw new Error("✅ Account already exists!\nPlease login instead of registering again 😊");
                    }
                    return res.text();
                })

                .then(() => {
                    localStorage.setItem("userId", rollNo);
                    alert("Registration Successful ✅");
                    window.location.href = "face-register.html";
                })
                .catch (err => {
                alert(err.message);
            });
}
else {
    fetch("http://localhost:8080/teacher", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            teacherId: rollNo,
            name: fullName,
            email: email,
            password: password,
            department: "CS"
        })
    })
        // .then(res => res.text())
        .then(res => {
            if (!res.ok) {
                throw new Error("✅ Account already exists!\nPlease login instead of registering again 😊");
            }
            return res.text();
        })

        .then(() => {
            localStorage.setItem("userId", rollNo);
            alert("Registration Successful ✅");
            window.location.href = "login.html";
        })
        //         .catch(err => console.error("Error:", err));
        // }
        .catch(err => {
             alert(err.message);
        });
    }

    });
}



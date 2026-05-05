let loginType = "user";

function switchLogin(type){
    loginType = type;

    const buttons = document.querySelectorAll(".tab-btn");

    buttons.forEach(btn => btn.classList.remove("active"));

    if(type === "user"){
        buttons[0].classList.add("active");
    }else{
        buttons[1].classList.add("active");
    }
}

document.getElementById("loginForm").addEventListener("submit", function(e){
    e.preventDefault();

    if(loginType === "user"){
        alert("User Login Successful");
        window.location.href = "dashboard.html";
    }else{
        alert("Admin Login Successful");
        window.location.href = "admin-dashboard.html";
    }
});
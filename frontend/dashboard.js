window.addEventListener("load", () => {

    const status = localStorage.getItem("attendanceStatus");
    const time = localStorage.getItem("checkInTime");
    const date = localStorage.getItem("attendanceDate");

    const history = JSON.parse(localStorage.getItem("attendanceHistory")) || [];

    document.getElementById("attendanceStatus").innerText =
        status || "Absent";

    document.getElementById("checkInTime").innerText =
        time || "--:--";

    document.getElementById("attendanceDate").innerText =
        date || "--/--/----";

    document.getElementById("totalAttendance").innerText =
        history.length;

    const totalWorkingDays = 30;

    const percent = ((history.length / totalWorkingDays) * 100).toFixed(1);

    document.getElementById("attendancePercent").innerText =
        percent + "%";
});

function logout(){
    alert("Logged Out");
    window.location.href = "index.html";
}
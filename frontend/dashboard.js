window.addEventListener("load", () => {

    const currentUser = JSON.parse(localStorage.getItem("currentUser"));
    /* USER NAME */
    if (currentUser) {
        document.getElementById("welcomeName").innerText =
            currentUser.fullName + " 👋";

        document.getElementById("userNameTop").innerText =
            currentUser.fullName;
    }

    // const status = localStorage.getItem("attendanceStatus");
    // const time = localStorage.getItem("checkInTime");
    // const date = localStorage.getItem("attendanceDate");

    // const history = JSON.parse(localStorage.getItem("attendanceHistory")) || [];

    // document.getElementById("attendanceStatus").innerText =
    //     status || "Absent";

    // document.getElementById("checkInTime").innerText =
    //     time || "--:--";

    // document.getElementById("attendanceDate").innerText =
    //     date || "--/--/----";


    /* ALL ATTENDANCE */
    const allHistory = JSON.parse(localStorage.getItem("attendanceHistory")) || [];

    /* CURRENT USER HISTORY ONLY */
    const userHistory = allHistory.filter(record =>
        record.roll === currentUser.rollNo
    );

    /* TODAY RECORD */
    const today = new Date().toLocaleDateString();
    const todayRecord = userHistory.find(record =>
        record.date === today
    );

    /* STATUS */
    document.getElementById("attendanceStatus").innerText =
        todayRecord
            ? todayRecord.status
            : "Absent";

    /* TIME */
    document.getElementById("checkInTime").innerText =
        todayRecord
            ? todayRecord.time
            : "--:--";

    /* DATE */
    document.getElementById("attendanceDate").innerText =
        todayRecord
            ? todayRecord.date
            : "--/--/----";

    /* TOTAL ATTENDANCE */
    document.getElementById("totalAttendance").innerText =
        history.length;

    /* PERCENTAGE */
    const totalWorkingDays = 30;
    const percent = ((history.length / totalWorkingDays) * 100).toFixed(1);
    document.getElementById("attendancePercent").innerText =
        percent + "%";
});

function logout() {
    localStorage.removeItem("currentUser");
    alert("Logged Out");
    window.location.href = "login.html";
}
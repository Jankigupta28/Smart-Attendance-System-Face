const adminTableBody = document.getElementById("adminTableBody");

const allAttendance =
    JSON.parse(localStorage.getItem("attendanceHistory")) || [];

const registeredUsers =
    JSON.parse(localStorage.getItem("registeredUsers")) || [];

const today = new Date().toLocaleDateString();



/* ===== FILTER ONLY TODAY'S ATTENDANCE ===== */
const todayAttendance = allAttendance.filter(record =>
    record.date === today
);



/* ===== RENDER TABLE ===== */
function renderTable(data) {

    adminTableBody.innerHTML = "";

    if (data.length === 0) {
        adminTableBody.innerHTML = `
            <tr>
                <td colspan="5">No Attendance Marked Today</td>
            </tr>
        `;
        return;
    }

    data.slice().reverse().forEach(student => {

        adminTableBody.innerHTML += `
            <tr>
                <td>${student.name}</td>
                <td>${student.roll}</td>
                <td>${student.date}</td>
                <td>${student.time}</td>
                <td class="${student.status.toLowerCase()}">
                    ${student.status}
                </td>
            </tr>
        `;
    });
}

renderTable(todayAttendance);



/* ===== SUMMARY CARDS ===== */
document.getElementById("totalStudents").innerText =
    registeredUsers.length;

document.getElementById("presentToday").innerText =
    todayAttendance.length;

document.getElementById("absentToday").innerText =
    registeredUsers.length - todayAttendance.length;



const rate =
    registeredUsers.length > 0
        ? ((todayAttendance.length / registeredUsers.length) * 100).toFixed(1)
        : 0;

document.getElementById("attendanceRate").innerText =
    rate + "%";



/* ===== FILTER FUNCTION ===== */
function filterRecords() {

    const search =
        document.getElementById("searchStudent").value.toLowerCase();

    const status =
        document.getElementById("statusFilter").value;

    const filtered = todayAttendance.filter(student => {

        const matchName =
            student.name.toLowerCase().includes(search);

        const matchStatus =
            status === "all" || student.status === status;

        return matchName && matchStatus;
    });

    renderTable(filtered);
}



/* ===== LOGOUT ===== */
function logout() {
    localStorage.removeItem("currentUser");
    window.location.href = "login.html";
}
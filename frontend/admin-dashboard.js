const adminTableBody = document.getElementById("adminTableBody");
const today = new Date().toISOString().slice(0, 10);
let allAttendance = [];
let allStudents = [];

window.addEventListener("load", () => {
    const teacherId = localStorage.getItem("userId");
   if (!teacherId) {
        window.location.href = "login.html";
        return;
    } 

fetch(`http://localhost:8080/teacher/teacher/${teacherId}`)
    .then(res => res.json())
    .then(teacher => {
        document.getElementById("teacherName").innerText = teacher.name;
        document.getElementById("welcomeTeacherName").innerText = teacher.name;
    });
   

    // TOTAL STUDENTS
    fetch("http://localhost:8080/students")
        .then(res => res.json())
        .then(students => {
            allStudents = students;
            document.getElementById("totalStudents").innerText = students.length;
        });

    // ALL ATTENDANCE
    fetch("http://localhost:8080/attendance/all")
        .then(res => res.json())
        .then(data => {
            allAttendance = data;

            const todayAttendance = data.filter(att =>
                att.timeStamp.startsWith(today)
            );

            document.getElementById("presentToday").innerText = todayAttendance.length;
            document.getElementById("absentToday").innerText = allStudents.length - todayAttendance.length;

            const rate = allStudents.length > 0
                ? ((todayAttendance.length / allStudents.length) * 100).toFixed(1)
                : 0;
            document.getElementById("attendanceRate").innerText = rate + "%";

            renderTable(todayAttendance);
        });
});

function renderTable(data) {
    adminTableBody.innerHTML = "";
    if (data.length === 0) {
        adminTableBody.innerHTML = `
            <tr>
                <td colspan="5">No Attendance Records Found</td>
            </tr>
        `;
        return;
    }

    data.slice().reverse().forEach(att => {
        const date = new Date(att.timeStamp);
        adminTableBody.innerHTML += `
            <tr>
                <td>${att.student.name}</td>
                <td>${att.student.enrollmentNumber}</td>
                <td>${date.toLocaleDateString()}</td>
                <td>${date.toLocaleTimeString()}</td>
                <td class="present">${att.status}</td>
            </tr>
        `;
    });
}

function filterRecords() {
    const search = document.getElementById("searchStudent").value.toLowerCase();
    const status = document.getElementById("statusFilter").value;
    const dateFilter = document.getElementById("filterDate").value;

    const filtered = allAttendance.filter(att => {
        const matchName = att.student.name.toLowerCase().includes(search);
        const matchStatus = status === "all" || att.status === status;
        const matchDate = !dateFilter || att.timeStamp.startsWith(dateFilter);
        return matchName && matchStatus && matchDate;
    });

    renderTable(filtered);
}

function logout() {
    localStorage.removeItem("userId");
    window.location.href = "login.html";
}
function startSession() {
    const teacherId = localStorage.getItem("userId");
    const courseId = parseInt(document.getElementById("courseSelect").value);
    const radius = 100;

    navigator.geolocation.getCurrentPosition(pos => {
        fetch("http://localhost:8080/class/session/start", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                teacherId: teacherId,
                courseId: courseId,
                centerLat: pos.coords.latitude,
                centerLng: pos.coords.longitude,
                radius: radius
            })
        })
        .then(res => res.text())
        .then(data => {
            alert("Session Started ✅");
            document.getElementById("sessionStatus").innerText = "Session Active ✅";
        });
    });
}

function endSession() {
    const teacherId = localStorage.getItem("userId");
    const courseId = document.getElementById("courseSelect").value;

    fetch(`http://localhost:8080/class/session/end?teacherId=${teacherId}&courseId=${courseId}`, {
        method: "POST"
    })
    .then(res => res.text())
    .then(data => {
        alert("Session Ended ❌");
        document.getElementById("sessionStatus").innerText = "No Active Session";
    });
}
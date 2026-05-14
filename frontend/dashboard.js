window.addEventListener("load", () => {

   const enrollmentNumber = localStorage.getItem("userId");
console.log("UserId from localStorage:", enrollmentNumber);

    console.log("Enrollment:", enrollmentNumber);

    if (!enrollmentNumber) {
        window.location.href = "login.html";
        return;
    }

    // STUDENT DATA
    fetch(`http://localhost:8080/student/${enrollmentNumber}`)
        .then(async res => {

            if (!res.ok) {
                throw new Error("Student not found");
            }

            return res.json();
        })
        .then(student => {

            document.getElementById("welcomeName").innerHTML =
                "Welcome, " + student.name + " 👋";

            document.getElementById("userNameTop").innerText =
                student.name;

        })
        .catch(err => {
            console.error("Student Fetch Error:", err);
        });

    // ATTENDANCE DATA
    fetch(`http://localhost:8080/attendance/student/${enrollmentNumber}`)
        .then(async res => {

            if (!res.ok) {
                throw new Error("Attendance not found");
            }

            return res.json();
        })
        .then(data => {

            const today = new Date().toISOString().slice(0, 10);

            const todayRecord = data.find(att =>
                att.timeStamp.startsWith(today)
            );

            document.getElementById("attendanceStatus").innerHTML = 
                todayRecord ? "Present ✅" : "Absent ❌";

            document.getElementById("checkInTime").innerText =
                todayRecord
                    ? new Date(todayRecord.timeStamp).toLocaleTimeString()
                    : "--:--";

            document.getElementById("attendanceDate").innerText = today;

            document.getElementById("totalAttendance").innerText =
                data.length;

            const totalWorkingDays = 30;

            const percent =
                ((data.length / totalWorkingDays) * 100).toFixed(1);

            document.getElementById("attendancePercent").innerText =
                percent + "%";

        })
        .catch(err => {
            console.error("Attendance Fetch Error:", err);
        });

});

function logout() {
    localStorage.removeItem("userId");
    alert("Logged Out");
    window.location.href = "login.html";
}
const historyTableBody = document.getElementById("historyTableBody");

if (historyTableBody) {
    const enrollmentNumber = localStorage.getItem("userId");

    if (!enrollmentNumber) {
        window.location.href = "login.html";
    }

    fetch(`http://localhost:8080/attendance/student/${enrollmentNumber}`)
        .then(res => res.json())
        .then(data => {
            if (data.length === 0) {
                historyTableBody.innerHTML = `
                    <tr>
                        <td colspan="3" class="no-record">
                            No Attendance Records Found
                        </td>
                    </tr>
                `;
            } else {
                data.slice().reverse().forEach(record => {
                    const date = new Date(record.timeStamp);
                    historyTableBody.innerHTML += `
                        <tr>
                            <td>${date.toLocaleDateString()}</td>
                            <td>${date.toLocaleTimeString()}</td>
                            <td class="present">Present ✅</td>
                        </tr>
                    `;
                });
            }
        })
        .catch(err => {
            console.error("History Error:", err);
        });
}
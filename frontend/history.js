 const historyTableBody = document.getElementById("historyTableBody");

if (historyTableBody) {

    // const history = JSON.parse(localStorage.getItem("attendanceHistory")) || [];
    const currentUser =
        JSON.parse(localStorage.getItem("currentUser"));

    /* ALL ATTENDANCE */
    const allHistory =
        JSON.parse(localStorage.getItem("attendanceHistory")) || [];

    /* ONLY CURRENT USER HISTORY */
    const userHistory = allHistory.filter(record =>
        record.roll === currentUser.rollNo
    );

    /* NO RECORD */
    if (history.length === 0) {
        historyTableBody.innerHTML = `
            <tr>
                <td colspan="3" class="no-record">
                    No Attendance Records Found
                </td>
            </tr>
        `;
    } else {
        userHistory.slice().reverse().forEach(record => {
            historyTableBody.innerHTML += `
                <tr>
                    <td>${record.date}</td>
                    <td>${record.time}</td>
                    <td class="present">${record.status}</td>
                </tr>
            `;
        });
    }
}
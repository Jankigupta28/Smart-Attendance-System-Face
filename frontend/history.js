const historyTableBody = document.getElementById("historyTableBody");

if(historyTableBody){

    const history = JSON.parse(localStorage.getItem("attendanceHistory")) || [];

    if(history.length === 0){

        historyTableBody.innerHTML = `
            <tr>
                <td colspan="3" class="no-record">
                    No Attendance Records Found
                </td>
            </tr>
        `;

    } else {

        history.slice().reverse().forEach(record => {
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
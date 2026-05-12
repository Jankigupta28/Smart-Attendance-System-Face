/* ALL ATTENDANCE DATA */
let attendanceData = JSON.parse(localStorage.getItem("attendanceHistory")) || [];
let filteredData = attendanceData;

/* LOAD TABLE */
renderTable(filteredData);
updateSummary(filteredData);

/* TABLE FUNCTION */
function renderTable(data) {
    const previewBody = document.getElementById("previewBody");
    previewBody.innerHTML = "";
    if (data.length === 0) {
        previewBody.innerHTML = `
            <tr>
                <td colspan="5" class="no-record">
                    No Records Found
                </td>
            </tr>
        `;
        return;
    }

    data.slice().reverse().forEach(record => {
        previewBody.innerHTML += `
            <tr>
                <td>${record.name}</td>
                <td>${record.roll}</td>
                <td>${record.date}</td>
                <td>${record.time}</td>
                <td class="${record.status.toLowerCase()}">
                    ${record.status}
                </td>
            </tr>
        `;
    });
}


/* SUMMARY */
function updateSummary(data) {
    document.getElementById("totalRecords").innerText = data.length;
    const present = data.filter(r => r.status === "Present").length;
    const absent = data.filter(r => r.status === "Absent").length;
    document.getElementById("presentCount").innerText = present;
    document.getElementById("absentCount").innerText = absent;
}

/* FILTER DATE */
function filterByDate() {
    const selectedDate = document.getElementById("reportDate").value;
    if (selectedDate === "") {
        filteredData = attendanceData;
        renderTable(filteredData);
        updateSummary(filteredData);
        return;
    }

    const formattedDate = new Date(selectedDate).toLocaleDateString();
    filteredData = attendanceData.filter(record =>
        record.date === formattedDate
    );
    renderTable(filteredData);
    updateSummary(filteredData);
}

/* EXPORT CSV */
function exportCSV() {
    if (filteredData.length === 0) {
        alert("No Records To Export");
        return;
    }
    let csv = `Name,Roll No,Date,Time,Status
`;

    filteredData.forEach(record => {
        csv +=
            `${record.name},
             ${record.roll},
             ${record.date},
             ${record.time},
             ${record.status}
`;
    });
    const blob = new Blob([csv], {
            type: "text/csv"
        });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "attendance-report.csv";
    link.click();
    alert("Report Exported Successfully ✅");
}

/* BACK */
function goBack() {

    window.location.href =
        "admin-dashboard.html";
}
let attendanceData = [];
let filteredData = [];

/* LOAD ALL ATTENDANCE */
window.onload = function () {

    fetch("http://localhost:8080/attendance/all")
        .then(res => res.json())
        .then(data => {

            attendanceData = data;
            filteredData = data;

            renderTable(filteredData);
            updateSummary(filteredData);
        })
        .catch(err => {
            console.error("Error:", err);
        });
};

/* TABLE */
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
    data.slice().reverse().forEach(att => {
        const dateObj = new Date(att.timeStamp);
        previewBody.innerHTML += `
            <tr>
                <td>${att.student.name}</td>
                <td>${att.student.enrollmentNumber}</td>
                <td>${dateObj.toLocaleDateString()}</td>
                <td>${dateObj.toLocaleTimeString()}</td>
                <td class="${att.status.toLowerCase()}">
                    ${att.status}
                </td>
            </tr>
        `;
    });
}

/* SUMMARY */
function updateSummary(data) {
    document.getElementById("totalRecords").innerText = data.length;
    const present = data.filter(a => a.status === "PRESENT").length;
    const absent = data.filter(a => a.status === "ABSENT").length;
    document.getElementById("presentCount").innerText = present;
    document.getElementById("absentCount").innerText = absent;
}

/* FILTER DATE */
function filterByDate() {
    const selectedDate =
        document.getElementById("reportDate").value;
    if (!selectedDate) {
        filteredData = attendanceData;
        renderTable(filteredData);
        updateSummary(filteredData);
        return;
    }

    fetch(`http://localhost:8080/attendance/date?date=${selectedDate}`)
        .then(res => res.json())
        .then(data => {
            filteredData = data;
            renderTable(filteredData);
            updateSummary(filteredData);
        });
}

/* EXPORT CSV */
function exportCSV() {
    if (filteredData.length === 0) {
        alert("No Records To Export");
        return;
    }
    let csv =
        `Name,Enrollment Number,Date,Time,Status
`;
    filteredData.forEach(att => {
        const dateObj = new Date(att.timeStamp);
        csv +=
            `${att.student.name},
            ${att.student.enrollmentNumber},
            ${dateObj.toLocaleDateString()},
            ${dateObj.toLocaleTimeString()},
            ${att.status}
`;
    });
    const blob = new Blob([csv], {
        type: "text/csv"
    });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "attendance-report.csv";
    link.click();
    alert("CSV Exported Successfully ✅");
}

/* EXPORT PDF */
function exportPDF() {
    const teacherId = localStorage.getItem("userId");
    window.open(
        `http://localhost:8080/report/teacher/${teacherId}`,
        "_blank"
    );
}

/* BACK */
function goBack() {
    window.location.href =
        "admin-dashboard.html";
}
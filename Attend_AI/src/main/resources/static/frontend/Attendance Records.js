/* CURRENT ADMIN */
const currentUser =
    JSON.parse(localStorage.getItem("currentUser"));
if (currentUser) {
    document.getElementById("teacherName").innerText =
        currentUser.fullName;
}

/* GET ATTENDANCE DATA */
let attendanceData =
    JSON.parse(localStorage.getItem("attendanceHistory")) || [];
const adminTableBody =
    document.getElementById("adminTableBody");


/* RENDER TABLE */
function renderTable(data) {
    adminTableBody.innerHTML = "";

    /* NO RECORD */
    if (data.length === 0) {
        adminTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="no-record">
                    No Attendance Records Found
                </td>
            </tr>
        `;
        return;
    }

    /* TABLE DATA */
    data.slice().reverse().forEach(record => {
        adminTableBody.innerHTML += `
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


/* INITIAL LOAD */
renderTable(attendanceData);

/* SUMMARY COUNTS */
function updateSummary(data) {
    document.getElementById("totalRecords").innerText =
        data.length;
    const presentCount =
        data.filter(r => r.status === "Present").length;
    const absentCount =
        data.filter(r => r.status === "Absent").length;
    document.getElementById("presentCount").innerText =
        presentCount;
    document.getElementById("absentCount").innerText =
        absentCount;
    const percent = data.length
        ? ((presentCount / data.length) * 100).toFixed(1)
        : 0;
    document.getElementById("attendancePercent").innerText =
        percent + "%";
}

/* INITIAL SUMMARY */
updateSummary(attendanceData);


/* FILTER RECORDS */
function filterRecords() {
    const searchValue =
        document.getElementById("searchStudent")
            .value
            .toLowerCase();
    const selectedDate =
        document.getElementById("filterDate").value;
    const selectedStatus =
        document.getElementById("statusFilter").value;
    let filteredData = attendanceData.filter(record => {
        /* NAME SEARCH */
        const matchName =
            record.name.toLowerCase()
                .includes(searchValue);

                /* DATE FILTER */
        let matchDate = true;
        if (selectedDate) {
            const formattedDate =
                new Date(selectedDate).toLocaleDateString();
            matchDate =
                record.date === formattedDate;
        }

        /* STATUS FILTER */
        const matchStatus =
            selectedStatus === "all" ||
            record.status === selectedStatus;
        return matchName && matchDate && matchStatus;
    });

    renderTable(filteredData);
    updateSummary(filteredData);
}

/* LOGOUT */
function logout() {

    localStorage.removeItem("currentUser");

    alert("Logged Out Successfully");

    window.location.href = "login.html";
}
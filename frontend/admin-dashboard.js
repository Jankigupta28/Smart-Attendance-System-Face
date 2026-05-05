const adminTableBody = document.getElementById("adminTableBody");

let attendanceData =
    JSON.parse(localStorage.getItem("attendanceHistory")) || [];

function renderTable(data){

    adminTableBody.innerHTML = "";

    if(data.length === 0){
        adminTableBody.innerHTML = `
            <tr>
                <td colspan="5">No Attendance Records Found</td>
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

renderTable(attendanceData);

function filterRecords(){

    const search =
        document.getElementById("searchStudent").value.toLowerCase();

    const status =
        document.getElementById("statusFilter").value;

    let filtered = attendanceData.filter(student => {

        const matchName =
            student.name.toLowerCase().includes(search);

        const matchStatus =
            status === "all" || student.status === status;

        return matchName && matchStatus;
    });

    renderTable(filtered);
}

function logout(){
    window.location.href = "login.html";
}
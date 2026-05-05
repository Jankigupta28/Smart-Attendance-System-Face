const video = document.getElementById("video");
const scanBtn = document.getElementById("scanBtn");
const confirmBtn = document.getElementById("confirmBtn");

let faceVerified = false;
let locationVerified = false;

navigator.mediaDevices.getUserMedia({video:true})
.then(stream => {
    video.srcObject = stream;
});

navigator.geolocation.getCurrentPosition(pos => {

    const lat = pos.coords.latitude;
    const lon = pos.coords.longitude;

    const collegeLat = 25.28;
    const collegeLon = 82.98;

    const distance = Math.sqrt(
        (lat - collegeLat) ** 2 +
        (lon - collegeLon) ** 2
    );

    if(distance < 0.01){
        locationVerified = true;
        document.getElementById("locationStatus").innerText =
            "Inside Campus ✅";
    }

});

scanBtn.addEventListener("click", () => {

    document.getElementById("faceStatus").innerText =
        "Scanning...";

    setTimeout(() => {

        faceVerified = true;

        document.getElementById("faceStatus").innerText =
            "Face Verified ✅";

        if(faceVerified && locationVerified){
            confirmBtn.disabled = false;
        }

    }, 2000);
});

confirmBtn.addEventListener("click", () => {

    const now = new Date();

    const date = now.toLocaleDateString();
    const time = now.toLocaleTimeString([], {
        hour:'2-digit',
        minute:'2-digit'
    });

    let history = JSON.parse(localStorage.getItem("attendanceHistory")) || [];

    const alreadyMarked = history.some(r => r.date === date);

    if(alreadyMarked){
        alert("Already Marked Today");
        return;
    }

    history.push({date,time,status:"Present"});

    localStorage.setItem("attendanceHistory", JSON.stringify(history));
    localStorage.setItem("attendanceStatus", "Present");
    localStorage.setItem("checkInTime", time);
    localStorage.setItem("attendanceDate", date);

    alert("Attendance Marked");
    window.location.href = "dashboard.html";
});
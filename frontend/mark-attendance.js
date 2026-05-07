const video = document.getElementById("video");
const scanBtn = document.getElementById("scanBtn");
const confirmBtn = document.getElementById("confirmBtn");

let faceVerified = false;
let locationVerified = false;

/* START CAMERA */
navigator.mediaDevices.getUserMedia({ video: true })
    .then(stream => {
        video.srcObject = stream;
    })
    .catch(() => {
        alert("Camera Access Denied");
    });

/* LOCATION CHECK */
navigator.geolocation.getCurrentPosition(pos => {

    const lat = pos.coords.latitude;
    const lon = pos.coords.longitude;

    /* Dummy College Location */
    const collegeLat = 25.28;
    const collegeLon = 82.98;

    const distance = Math.sqrt(
        (lat - collegeLat) ** 2 +
        (lon - collegeLon) ** 2
    );

    if (distance < 0.01) {
        locationVerified = true;
        document.getElementById("locationStatus").innerText =
            "Inside Campus ✅";
    }
    else {
        document.getElementById("locationStatus").innerText =
            "Outside Campus ❌";
    }
});

/* FACE SCAN */
scanBtn.addEventListener("click", () => {
    document.getElementById("faceStatus").innerText =
        "Scanning Face...";
    setTimeout(() => {
        faceVerified = true;
        document.getElementById("faceStatus").innerText =
            "Face Verified ✅";

        /* ENABLE BUTTON */
        if (faceVerified && locationVerified) {
            confirmBtn.disabled = false;
        }
    }, 2000);
});

/* MARK ATTENDANCE */
confirmBtn.addEventListener("click", () => {
    const now = new Date();
    const date = now.toLocaleDateString();
    const time = now.toLocaleTimeString([], {
        hour: '2-digit',
        minute: '2-digit'
    });

    /* CURRENT USER */
    const currentUser = JSON.parse(localStorage.getItem("currentUser")) || {
        fullName: "Unknown",
        rollNo: "N/A"
    };

    let history = JSON.parse(localStorage.getItem("attendanceHistory")) || [];

    /* ONE ATTENDANCE PER DAY */
    const alreadyMarked = history.some(record =>
        record.date === date &&
        record.roll === currentUser.rollNo
    );
    if (alreadyMarked) {
        alert("Attendance Already Marked Today ❌");
        return;
    }

    /* SAVE ATTENDANCE */
    history.push({
        name: currentUser.fullName,
        roll: currentUser.rollNo,
        date: date,
        time: time,
        status: "Present"
    });

    localStorage.setItem("attendanceHistory", JSON.stringify(history));
    localStorage.setItem("attendanceStatus", "Present");
    localStorage.setItem("checkInTime", time);
    localStorage.setItem("attendanceDate", date);

    alert("Attendance Marked Successfully ✅");

    window.location.href = "dashboard.html";
});
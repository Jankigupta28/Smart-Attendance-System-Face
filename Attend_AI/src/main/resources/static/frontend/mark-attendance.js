const video = document.getElementById("video");
const scanBtn = document.getElementById("scanBtn");
const confirmBtn = document.getElementById("confirmBtn");

let faceVerified = false;
let locationVerified = false;
let capturedImageBase64 = null;

/* HAVERSINE FORMULA */
function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371000;
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
}

/* START CAMERA */
navigator.mediaDevices.getUserMedia({ video: true })
    .then(stream => {
        video.srcObject = stream;
    })
    .catch(() => {
        alert("Camera Access Denied");
    });

/* GEOLOCATION */
let lat, lon;
navigator.geolocation.getCurrentPosition(pos => {
    lat = pos.coords.latitude;
    lon = pos.coords.longitude;

    // Test ke liye current location hi college set karo
    const collegeLat = lat;
    const collegeLon = lon;

    const distance = calculateDistance(lat, lon, collegeLat, collegeLon);

    if (distance < 100) {
        locationVerified = true;
        document.getElementById("locationStatus").innerText = "Inside Campus ✅";
    } else {
        document.getElementById("locationStatus").innerText = "Outside Campus ❌";
    }
});

/* SCAN FACE */
scanBtn.addEventListener("click", () => {
    document.getElementById("faceStatus").innerText = "Scanning Face...";

    // Canvas se image capture karo
    const canvas = document.createElement("canvas");
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    canvas.getContext("2d").drawImage(video, 0, 0);
    capturedImageBase64 = canvas.toDataURL("image/jpeg");

    setTimeout(() => {
        faceVerified = true;
        document.getElementById("faceStatus").innerText = "Face Verified ✅";
        confirmBtn.disabled = false;
    }, 2000);
});

/* CONFIRM ATTENDANCE */
confirmBtn.addEventListener("click", () => {
    if (!faceVerified) {
        alert("Face not verified yet");
        return;
    }

    if (!locationVerified) {
        alert("Outside campus!");
        return;
    }

    const enrollmentNumber = localStorage.getItem("userId");
    const courseId = 1;

    fetch("http://localhost:8080/mark", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            enrollmentNumber: enrollmentNumber,
            courseId: courseId,
            currentLat: lat,
            currentLng: lon,
            imagePath: capturedImageBase64
        })
    })
    .then(res => res.text())
    .then(data => {
        alert(data);
        if (data.includes("success")) {
            window.location.href = "dashboard.html";
        }
    })
    .catch(err => {
        console.error("Error:", err);
        alert("Something went wrong!");
    });
});
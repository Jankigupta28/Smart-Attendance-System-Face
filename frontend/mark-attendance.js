const video = document.getElementById("video");
const scanBtn = document.getElementById("scanBtn");
const confirmBtn = document.getElementById("confirmBtn");

let faceVerified = false;
let lat, lon;
let capturedImageBase64 = "";

// 1. Camera Start
navigator.mediaDevices.getUserMedia({ video: true })
    .then(s => { video.srcObject = s; })
    .catch(e => alert("Camera error: " + e));

// 2. GPS Location (Attendance ke liye zaroori hai)
navigator.geolocation.getCurrentPosition(p => {
    lat = p.coords.latitude;
    lon = p.coords.longitude;
    document.getElementById("locationStatus").innerText = "Location Captured ✅";
}, () => alert("Please enable GPS/Location"));

// 3. Step 1: Face Scan (Flask AI se verify karega)
scanBtn.addEventListener("click", async () => {
    const userId = localStorage.getItem("userId");
    if (!userId) return alert("User ID missing! Please login again.");

    try {
        // DB se student ka face data mangwao
        const userRes = await fetch(`http://localhost:8080/student/${userId}`);
        const userData = await userRes.json();

        // Photo capture karo
        const canvas = document.createElement("canvas");
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        canvas.getContext("2d").drawImage(video, 0, 0);
        capturedImageBase64 = canvas.toDataURL("image/jpeg");

        // Flask ko verification ke liye bhejo
        const flaskRes = await fetch("http://localhost:5000/verify-face", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                imagePath: capturedImageBase64,
                savedEncoding: userData.faceEncoding
            })
        });

        const result = await flaskRes.json();
        if (result.status === "success" && result.match === true) {
            faceVerified = true;
            confirmBtn.disabled = false;
            document.getElementById("faceStatus").innerText = "Face Verified ✅";
            alert("Face Matched! Now click Mark Attendance.");
        } else {
            alert("Face Match Failed!");
        }
    } catch (err) {
        alert("Verification Error: " + err.message);
    }
});

// 4. Step 2: Final Attendance (Spring Boot Controller ko data bhejega)
confirmBtn.addEventListener("click", async () => {
    if (!faceVerified) return;

    // Ye data aapke Java 'MarkAttendanceDTO' se exact match hona chahiye
    const attendanceDTO = {
        enrollmentNumber: localStorage.getItem("userId"),
        currentLat: parseFloat(lat),
        currentLng: parseFloat(lon),
        imagePath: capturedImageBase64 // Java controller isi image ko recognize karega
    };

    try {
        const response = await fetch("http://localhost:8080/attendance/mark-active", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(attendanceDTO)
        });

        const msg = await response.text();

        if (response.ok) {
            alert("✅ Success: " + msg);
            window.location.href = "dashboard.html";
        } else {
            // Yahan wahi error dikhayega jo Spring Boot se aayega (e.g. "Outside campus")
            alert("❌ Error: " + msg);
        }
    } catch (err) {
        alert("Server Error: Check if Spring Boot is running.");
    }
});
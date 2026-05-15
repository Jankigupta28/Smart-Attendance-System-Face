const video = document.getElementById("video");
const captureBtn = document.getElementById("captureBtn");
const saveFaceBtn = document.getElementById("saveFaceBtn");
const sampleNumber = document.getElementById("sampleNumber");
const progressFill = document.getElementById("progressFill");
const canvas = document.getElementById("canvas");
const MAX_SAMPLES = 5;

let faceSamples = [];
let detectionInterval = null;

// ── Load Models & Start Camera ────────────────────────────
async function init() {
    try {
        console.log("Starting Initialization...");
        // Loading Models from CDN
       const MODEL_URL = "https://cdn.jsdelivr.net/gh/justadudewhohacks/face-api.js@master/weights";
        await Promise.all([
            faceapi.nets.tinyFaceDetector.loadFromUri(MODEL_URL),
            faceapi.nets.faceLandmark68TinyNet.loadFromUri(MODEL_URL),
        ]);
        console.log("Models loaded ✓");

        // Request Camera - Simplified constraints for better compatibility
        const stream = await navigator.mediaDevices.getUserMedia({
            video: { width: 640, height: 480 }
        });
        video.srcObject = stream;

        video.onloadedmetadata = () => {
            video.play();
            console.log("Video playing ✓");
            startFaceDetection();
        };

    } catch (err) {
        console.error("Init error:", err);
        if (err.name === "NotAllowedError") {
            alert("Permission Denied: Please click the LOCK icon in the URL bar and enable the Camera.");
        } else {
            alert("Error: " + err.message);
        }
    }
}

// ── Live Face Detection ─────────────────
function startFaceDetection() {
    const overlay = document.createElement("canvas");
    overlay.style.position = "absolute";
    overlay.style.top = video.offsetTop + "px";
    overlay.style.left = video.offsetLeft + "px";
    overlay.style.pointerEvents = "none";
    video.parentElement.appendChild(overlay);

    detectionInterval = setInterval(async () => {
        if (video.paused || video.ended) return;

        const detection = await faceapi
            .detectSingleFace(video, new faceapi.TinyFaceDetectorOptions())
            .withFaceLandmarks(true);

        overlay.width = video.videoWidth;
        overlay.height = video.videoHeight;
        const ctx = overlay.getContext("2d");
        ctx.clearRect(0, 0, overlay.width, overlay.height);

        if (detection) {
            const box = detection.detection.box;
            ctx.strokeStyle = "#00ff00";
            ctx.lineWidth = 3;
            ctx.strokeRect(box.x, box.y, box.width, box.height);
            captureBtn.disabled = false;
        } else {
            captureBtn.disabled = true;
        }
    }, 200);
}

// ── Capture Logic ───────────────────────────────────────────────
captureBtn.addEventListener("click", async () => {
    if (faceSamples.length >= MAX_SAMPLES) return;

    const offscreen = document.createElement("canvas");
    offscreen.width = video.videoWidth;
    offscreen.height = video.videoHeight;
    offscreen.getContext("2d").drawImage(video, 0, 0);

    const base64Image = offscreen.toDataURL("image/jpeg", 0.7);
    faceSamples.push(base64Image);

    const count = faceSamples.length;
    sampleNumber.innerText = count;
    if (progressFill) progressFill.style.width = (count / MAX_SAMPLES * 100) + "%";

    if (count >= MAX_SAMPLES) {
        captureBtn.disabled = true;
        captureBtn.innerText = "All Samples Captured ✓";
    }
});
// ── Save Face Data (Flask + Spring Boot Handshake) ─────────────
saveFaceBtn.addEventListener("click", async () => {
    if (faceSamples.length < MAX_SAMPLES) {
        alert("Please capture at least 5 face samples before proceeding.");
        return;
    }

    // LocalStorage se student id nikalo
    const enrollmentNumber = localStorage.getItem("userId");
    if (!enrollmentNumber) {
        alert("User ID missing! Please register again from the form.");
        window.location.href = "register.html";
        return;
    }

    try {
        saveFaceBtn.innerText = "Processing AI Models...";
        saveFaceBtn.disabled = true;

        // Step 1: Python (Flask) ko photo bhejo encoding nikalne ke liye
        const flaskRes = await fetch("http://localhost:5000/get_encoding", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ imagePath: faceSamples[0] }) // Pehla sample bhej rahe hain
        });

        const flaskData = await flaskRes.json();

        if (flaskData.status === "success") {
            // Step 2: Jo face data mila, use Java (Spring Boot) ke through Database me daalo
            const springRes = await fetch(`http://localhost:8080/students/${enrollmentNumber}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    faceEncoding: JSON.stringify(flaskData.encoding)
                })
            });

            if (springRes.ok) {
                alert("Face Profile Registered Successfully! ✅");
                localStorage.removeItem("userId"); // Clean up

                // Ye raha aapka Login Page Redirect
                window.location.href = "login.html";
            } else {
                alert("Database Update Failed! Check Spring Boot.");
                saveFaceBtn.disabled = false;
                saveFaceBtn.innerText = "Save Face Data";
            }
        } else {
            alert("AI Error: Face not detected clearly. Please try again.");
            saveFaceBtn.disabled = false;
            saveFaceBtn.innerText = "Save Face Data";
        }
    } catch (err) {
        console.error("System Error: ", err);
        alert("Server connection failed! Ensure Flask (5000) and Spring Boot (8080) are running.");
        saveFaceBtn.disabled = false;
        saveFaceBtn.innerText = "Save Face Data";
    }
});

// CRITICAL: Function call karna mat bhoolna!
init();
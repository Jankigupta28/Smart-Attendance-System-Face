const video = document.getElementById("video");
const captureBtn = document.getElementById("captureBtn");
const saveFaceBtn = document.getElementById("saveFaceBtn");
const sampleNumber = document.getElementById("sampleNumber");
const progressFill = document.getElementById("progressFill");
const canvas = document.getElementById("canvas");

let sampleCount = 0;
let faceSamples = [];
/* START CAMERA */
navigator.mediaDevices.getUserMedia({ video: true })
    .then(stream => {
        video.srcObject = stream;
    });
/* CAPTURE FACE */
captureBtn.addEventListener("click", () => {

    if (sampleCount >= 5) return;

    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    const ctx = canvas.getContext("2d");
    ctx.drawImage(video, 0, 0);

    faceSamples.push(canvas.toDataURL("image/jpeg", 0.3));
  
    sampleCount++;
    sampleNumber.innerText = sampleCount;
    progressFill.style.width = `${sampleCount * 20}%`;

    if (sampleCount === 5) {
        captureBtn.disabled = true;
        captureBtn.innerText = "Completed ✅";
    }
});
// ── Save Face Data (Flask + Spring Boot Handshake) ─────────────
saveFaceBtn.addEventListener("click", async () => {
    if (faceSamples.length < MAX_SAMPLES) {
        alert("Please capture at least 5 face samples before proceeding.");
        return;
    }
    const enrollmentNumber = localStorage.getItem("userId");


fetch("http://localhost:8080/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
        enrollmentNumber: enrollmentNumber,
        imagePath: faceSamples[0].split(',')[1]
    })
})
.then(res => res.text())
.then(data => {
    alert("Face Registered Successfully");
    window.location.href = "login.html";
});
});

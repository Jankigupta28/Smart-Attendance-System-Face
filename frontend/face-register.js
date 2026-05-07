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
    const imageData = canvas.toDataURL("image/png");
    faceSamples.push(imageData);
    sampleCount++;
    sampleNumber.innerText = sampleCount;
    progressFill.style.width = `${sampleCount * 20}%`;
    if (sampleCount === 5) {
        captureBtn.disabled = true;
        captureBtn.innerText = "Completed ✅";
    }
});
/* SAVE FACE */
saveFaceBtn.addEventListener("click", () => {
    if (sampleCount < 5) {
        alert("Capture 5 Samples First");
        return;
    }
    const currentUser = JSON.parse(localStorage.getItem("currentUser"));
    let users = JSON.parse(localStorage.getItem("registeredUsers")) || [];
    users = users.map(user => {
        if (user.email === currentUser.email) {
            return {
                ...user,
                faceRegistered: true,
                faceSamples: faceSamples
            };
        }
        return user;
    });
    localStorage.setItem(
        "registeredUsers",
        JSON.stringify(users)
    );

    alert("Face Registered Successfully");

    window.location.href = "login.html";
});
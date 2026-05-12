from flask import Flask, request, jsonify
from flask_cors import CORS
import face_recognition
import os, json, base64

app = Flask(__name__)
CORS(app)

DB_FILE = "students_faces.json"

@app.route('/register', methods=['POST'])
def register():
    try:
        data = request.json
        enrollment = data.get('enrollmentNumber')
        img_b64 = data.get('imagePath').split(",")[-1]

        # Image ko temporary save karo check karne ke liye
        with open("temp.jpg", "wb") as f:
            f.write(base64.b64decode(img_b64))

        image = face_recognition.load_image_file("temp.jpg")
        encodings = face_recognition.face_encodings(image)

        if not encodings:
            return jsonify({"status": "fail", "message": "Face not detected"}), 400

        # Database (JSON file) load karo
        db = {}
        if os.path.exists(DB_FILE):
            with open(DB_FILE, "r") as f:
                db = json.load(f)

        # Enrollment number ke against face data save karo
        db[enrollment] = encodings[0].tolist()
        with open(DB_FILE, "w") as f:
            json.dump(db, f)

        return jsonify({"status": "success"})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == '__main__':

    app.run(port=5000, debug=True)
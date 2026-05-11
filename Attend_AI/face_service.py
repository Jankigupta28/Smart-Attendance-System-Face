from flask import Flask, request, jsonify
import cv2
import numpy as np
import os
import hashlib
import base64
import tempfile
import json

app = Flask(__name__)

STUDENTS_FILE = "students.json"

# Load existing students
if os.path.exists(STUDENTS_FILE):
    with open(STUDENTS_FILE, "r") as f:
        students = json.load(f)
else:
    students = {}

def get_face_hash(image_path):
    img = cv2.imread(image_path)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)

    if len(faces) == 0:
        return None

    x, y, w, h = faces[0]
    face = gray[y:y+h, x:x+w]
    face_resized = cv2.resize(face, (100, 100))
    face_hash = hashlib.md5(face_resized.tobytes()).hexdigest()
    return face_hash

@app.route('/register', methods=['POST'])
def register():
    data = request.json
    enrollment_number = data['enrollmentNumber']
    img_base64 = data['imagePath']

    if ',' in img_base64:
        img_base64 = img_base64.split(',')[1]

    img_data = base64.b64decode(img_base64)
    with tempfile.NamedTemporaryFile(delete=False, suffix='.png') as temp:
        temp.write(img_data)
        img_path = temp.name

    face_hash = get_face_hash(img_path)
    if face_hash is None:
        return jsonify({'error': 'No face found'}), 400

    students[enrollment_number] = face_hash

    # File mein save karo
    with open(STUDENTS_FILE, "w") as f:
        json.dump(students, f)

    return jsonify({'status': 'success', 'enrollmentNumber': enrollment_number})

@app.route('/recognize', methods=['POST'])
def recognize():
    data = request.json
    img_base64 = data['imagePath']

    if ',' in img_base64:
        img_base64 = img_base64.split(',')[1]

    img_data = base64.b64decode(img_base64)
    with tempfile.NamedTemporaryFile(delete=False, suffix='.png') as temp:
        temp.write(img_data)
        img_path = temp.name

    face_hash = get_face_hash(img_path)
    if face_hash is None:
        return jsonify({'enrollmentNumber': None, 'error': 'No face'}), 400

    for enrollment_number, known_hash in students.items():
        if face_hash == known_hash:
            return jsonify({'enrollmentNumber': enrollment_number})

    return jsonify({'enrollmentNumber': None})

if __name__ == '__main__':
    app.run(port=5000)
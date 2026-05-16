import sys
import os
import uuid
import base64
import json

# ==========================================================
# 1. ENVIRONMENT & LIBRARY FIXES (Python 3.13 Support)
# ==========================================================
site_packages_path = r'C:\Users\HP\AppData\Local\Programs\Python\Python313\Lib\site-packages'
if site_packages_path not in sys.path:
    sys.path.append(site_packages_path)

os.environ['FACE_RECOGNITION_MODELS'] = os.path.join(site_packages_path, 'face_recognition_models')

# Fixing pkg_resources for Python 3.13
try:
    import pkg_resources
except ImportError:
    import pip._vendor.pkg_resources as pkg_resources
    sys.modules['pkg_resources'] = pkg_resources

try:
    import face_recognition
    from flask import Flask, request, jsonify, make_response
    from flask_cors import CORS
    print("✅ SUCCESS: All Libraries Loaded Successfully!")
except Exception as e:
    print(f"❌ Initialization Error: {e}")

# ==========================================================
# 2. FLASK SERVER INITIALIZATION
# ==========================================================
app = Flask(__name__)
CORS(app) # Enables connection between port 63342 and 5000

# ==========================================================
# 3. ROUTE: FACE REGISTRATION (Used in face-register.js)
# ==========================================================
@app.route('/get_encoding', methods=['POST', 'OPTIONS'])
def get_encoding():
    if request.method == 'OPTIONS':
        return make_response("", 200)

    temp_filename = f"reg_{uuid.uuid4()}.jpg"
    try:
        data = request.json
        # Extract base64 image data
        img_raw = data.get('imagePath').split(",")[-1]

        with open(temp_filename, "wb") as f:
            f.write(base64.b64decode(img_raw))

        image = face_recognition.load_image_file(temp_filename)
        encodings = face_recognition.face_encodings(image)

        if not encodings:
            return jsonify({"status": "fail", "message": "No face detected in the captured photo."}), 400

        # Return the first face encoding as a list
        return jsonify({
            "status": "success",
            "encoding": encodings[0].tolist()
        })
    except Exception as e:
        print(f"Registration Error: {str(e)}")
        return jsonify({"status": "error", "message": str(e)}), 500
    finally:
        if os.path.exists(temp_filename):
            os.remove(temp_filename)

# ==========================================================
# 4. ROUTE: FACE VERIFICATION (Used in mark-attendance.js)
# ==========================================================
@app.route('/verify-face', methods=['POST', 'OPTIONS'])
def verify_face():
    if request.method == 'OPTIONS':
        return make_response("", 200)

    temp_filename = f"v_{uuid.uuid4()}.jpg"
    try:
        data = request.json
        img_raw = data.get('imagePath').split(",")[-1]
        saved_enc_raw = data.get('savedEncoding')

        # Handle encoding if it's sent as a string
        if isinstance(saved_enc_raw, str):
            saved_enc = json.loads(saved_enc_raw)
        else:
            saved_enc = saved_enc_raw

        with open(temp_filename, "wb") as f:
            f.write(base64.b64decode(img_raw))

        img = face_recognition.load_image_file(temp_filename)
        current_encs = face_recognition.face_encodings(img)

        if not current_encs:
            return jsonify({"status": "fail", "message": "No face detected during verification."}), 400

        # Match face against saved encoding
        match = face_recognition.compare_faces([saved_enc], current_encs[0], tolerance=0.6)

        return jsonify({
            "status": "success",
            "match": bool(match[0])
        })
    except Exception as e:
        print(f"Verification Error: {str(e)}")
        return jsonify({"status": "error", "message": str(e)}), 500
    finally:
        if os.path.exists(temp_filename):
            os.remove(temp_filename)

# ==========================================================
# 5. ROUTE: SPRING BOOT RECOGNIZE (Used in Java Controller)
# ==========================================================
@app.route('/recognize', methods=['POST'])
def recognize():
    try:
        data = request.json
        enrollment = data.get("enrollmentNumber")
        print(f"Recognize request received for Enrollment: {enrollment}")

        return jsonify({
            "status": "success",
            "enrollmentNumber": enrollment
        })
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

# ==========================================================
# 6. RUN SERVER
# ==========================================================
if __name__ == '__main__':
    print("🚀 Flask Server Running on http://127.0.0.1:5000")
    print("--- Ready for Registration & Attendance ---")
    app.run(host='0.0.0.0', port=5000, debug=False)
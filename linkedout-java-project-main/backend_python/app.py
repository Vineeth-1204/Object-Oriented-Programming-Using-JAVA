# app.py - Single file Flask backend for LinkedOut (NO photo support)

import secrets
from flask import Flask, request, jsonify
from flask_cors import CORS
from sqlalchemy import create_engine, Column, String, Text
from sqlalchemy.orm import declarative_base, sessionmaker, Session

# -------------------------
# Config
# -------------------------
DB_FILE = "linkedout.db"
DATABASE_URL = f"sqlite:///{DB_FILE}"

# -------------------------
# Database
# -------------------------
engine = create_engine(DATABASE_URL, connect_args={"check_same_thread": False})
SessionLocal = sessionmaker(bind=engine, autocommit=False, autoflush=False)
Base = declarative_base()


class User(Base):
    __tablename__ = "users"
    id = Column(String(32), primary_key=True, index=True)
    name = Column(String(200), nullable=False)
    education = Column(String(500))
    experience = Column(Text)
    skills = Column(Text)
    password = Column(String(255), nullable=False)


Base.metadata.create_all(bind=engine)


def get_db() -> Session:
    return SessionLocal()


def gen_unique_id():
    return "LKO" + secrets.token_hex(3).upper()


# -------------------------
# Flask App
# -------------------------
app = Flask(__name__)
CORS(app)


@app.post("/create_user")
def create_user():
    db = get_db()

    name = request.form.get("name")
    education = request.form.get("education")
    experience = request.form.get("experience")
    skills = request.form.get("skills")
    password = request.form.get("password")

    if not name or not password:
        return jsonify({"error": "name and password required"}), 400

    uid = gen_unique_id()

    user = User(
        id=uid,
        name=name,
        education=education,
        experience=experience,
        skills=skills,
        password=password,
    )

    db.add(user)
    db.commit()
    return jsonify({"id": uid})


@app.get("/get_user/<user_id>")
def get_user(user_id):
    db = get_db()
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        return jsonify({"error": "Not found"}), 404

    return jsonify({
        "id": user.id,
        "name": user.name,
        "education": user.education,
        "experience": user.experience,
        "skills": user.skills,
    })


@app.post("/edit_user")
def edit_user():
    db = get_db()
    data = request.json

    uid = data.get("id")
    password = data.get("password")

    user = db.query(User).filter(User.id == uid).first()
    if not user:
        return jsonify({"error": "Not found"}), 404

    if user.password != password:
        return jsonify({"error": "Invalid password"}), 401

    if data.get("name") is not None:
        user.name = data["name"]
    if data.get("education") is not None:
        user.education = data["education"]
    if data.get("experience") is not None:
        user.experience = data["experience"]
    if data.get("skills") is not None:
        user.skills = data["skills"]

    db.add(user)
    db.commit()
    return jsonify({"status": "ok"})


@app.delete("/delete_user/<user_id>")
def delete_user(user_id):
    db = get_db()
    password = request.args.get("password", "")

    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        return jsonify({"error": "Not found"}), 404

    if user.password != password:
        return jsonify({"error": "Invalid password"}), 401

    db.delete(user)
    db.commit()
    return jsonify({"status": "deleted"})


# -------------------------
# Run
# -------------------------
if __name__ == "__main__":
    app.run(debug=True, port=8000)

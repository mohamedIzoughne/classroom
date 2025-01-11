normally we should have:

## Teacher
- name
- email
- password(will be shashed)
- image

## Student
- name
- email
- phoneNumber
- gender
- date of birth
class id


## Class
- name
- filiere
- description: optional


## module/subject
- name
- class refrences class table
- description: optional

## sessions
- name
- subject refrences subject table
- room/salle refrences room table
- day
- hours
- status: boolean // wether the teacher closed the finished the session or not

## reclamation
- name
- date
- execuse
- session name: refrences session
- day refrences day table


## salles/rooms
- name

## Days
- name
- id, serial number
- date

## presence
- id SERIAL PRIMARY KEY,
- student_id INT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
- day_id INT NOT NULL REFERENCES days(id) ON DELETE CASCADE,
- status BOOLEAN NOT NULL, -- TRUE for present, FALSE for absent
- session_id
- week number

Plan before Monday:
today: matieres et classes + consult student + some of dashboard + login + reclamations
tomorrow: sessions + some of dashboard + attendance
insert into registry_t (
    registry_id,
    firstname,
    lastname,
    dateofbirth)
    values (
    'f49ea237-d2ee-48c8-872f-767c8e3fc473',
    'John',
    'Doe',
    '2024-11-17');

insert into student_t (
    student_id,
    registrationnumber,
    email,
    password)
    values (
    '6d5385a5-c21e-49c1-b1c0-293d042a2dfe',
    '123456',
    'johndoe@domain.com',
    'password');

insert into course_t (
    course_id,
    name,
    description
    )
    values (
    '766988cd-b7e8-459e-8d94-8d74f6042691',
    'OOP',
    'Object Oriented Programming'
    );

insert into registration_t(
    registration_id,
    student,
    course,
    registrationdate)
    values (
    '4bb6d5a8-3516-45c9-8f9c-5b06257a30dc',
    '6d5385a5-c21e-49c1-b1c0-293d042a2dfe',
    '766988cd-b7e8-459e-8d94-8d74f6042691',
    '2024-11-17'
);
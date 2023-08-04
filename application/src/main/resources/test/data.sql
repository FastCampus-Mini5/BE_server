insert into user_tb
(id, email, password, username, role, hire_date, profile_image, created_date, updated_date)
values (1, 'kjoJiIPBz6f5YQXgKxpurQ==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '88kd02Gzscm5encDICfxJA==', 'USER', date('2019-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (2, 'VynYMl7zNOhDiqMVFC6ODg==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', 'Eg4iD3nDL3l+g28tUHHnIg==', 'USER', date('2019-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (3, '9ef2PAWiR6rG9yeTqXyflg==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '5A/8pW1DNtMaZnmlWiBcag==', 'USER', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (4, 'DTPa6fWGFf+WCxYSfyAcmA==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', 'AkiNZoWj2C/WhSwVTcVf4A==', 'USER', date('2023-03-17'), '/image/default.png',
        date('2023-03-17'), date('2023-03-17')),
       (5, '5HgZNdfxLiIBNlMYdx5E8A==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '/X/ItYtSw53opmqgyOem9g==', 'USER', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (6, '5JWFP8E3hDvU2V8fvyd/Vw==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '+VtFqZ1lpfUj3w1/GzGLOw==', 'USER', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17'));

insert into vacation_tb
(id, user_id, created_date, start_date, end_date, reason, status, approval_date)
values (1, 1, date('2023-07-27'), date('2023-07-27'), date('2023-07-27'), '휴가', 'PENDING', null),
       (2, 2, date('2023-07-27'), date('2023-08-01'), date('2023-08-01'), '병가', 'APPROVE', null),
       (3, 3, date('2023-07-27'), date('2023-08-06'), date('2023-08-06'), '휴가', 'REJECT', null),
       (4, 4, date('2023-07-27'), date('2023-08-08'), date('2023-08-08'), '휴가', 'PENDING', null),
       (5, 5, date('2023-07-27'), date('2023-07-30'), date('2023-07-30'), '병가', 'APPROVE', null),
       (6, 6, date('2023-07-27'), date('2023-08-07'), date('2023-08-07'), '휴가', 'REJECT', null);

insert into vacation_info_tb
(id, user_id, remain_vacation, used_vacation)
values (1, 1, 5, 2),
       (2, 2, 6, 3),
       (3, 3, 7, 4),
       (4, 4, 8, 5),
       (5, 5, 9, 6),
       (6, 6, 10, 7);

insert into on_duty_tb
(id, user_id, created_date, updated_date, duty_date, status, approval_date)
values (1, 1, date('2023-07-27'), date('2023-07-27'), date('2023-07-27'), 'PENDING', null),
       (2, 2, date('2023-07-27'), date('2023-08-01'), date('2023-08-01'), 'APPROVE', null),
       (3, 3, date('2023-07-27'), date('2023-08-06'), date('2023-08-06'), 'REJECT', null),
       (4, 4, date('2023-07-27'), date('2023-08-08'), date('2023-08-08'), 'PENDING', null),
       (5, 5, date('2023-07-27'), date('2023-07-30'), date('2023-07-30'), 'APPROVE', null),
       (6, 6, date('2023-07-27'), date('2023-08-07'), date('2023-08-07'), 'REJECT', null);

insert into log_tb
(id, request_ip, sign_in_date, user_id)
values (1, '127.0.0.1', date('2023-07-26'), 1),
       (2, '127.0.0.1', date('2023-07-27'), 1),
       (3, '127.0.0.1', date('2023-07-28'), 1),
       (4, '127.0.0.2', date('2023-07-26'), 2),
       (5, '127.0.0.3', date('2023-07-27'), 3),
       (6, '127.0.0.4', date('2023-07-27'), 4);

insert into sign_up_tb
(id, email, password, username, hire_date, created_date)
values (1, 'k3zB3mbHhxsqb0jvPiYE9A==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '88kd02Gzscm5encDICfxJA==', date('2023-07-27'), date('2023-07-27')),
       (2, 'Glp8+lCvkG2/NW5+NIcUgg==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', 'Eg4iD3nDL3l+g28tUHHnIg==', date('2023-08-01'), date('2023-08-01')),
       (3, 'ePnPG780PCAyn3WpkChwGQ==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '5A/8pW1DNtMaZnmlWiBcag==', date('2023-08-06'), date('2023-08-06')),
       (4, 'YTdARKcFYOKmP9rexFG87g==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', 'AkiNZoWj2C/WhSwVTcVf4A==', date('2023-08-08'), date('2023-08-08')),
       (5, '08AddoLhJyEmYONIQGFmWw==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '/X/ItYtSw53opmqgyOem9g==', date('2023-07-30'), date('2023-07-30')),
       (6, 'AoJHavkGWBmssmfQvePZLQ==', '$2a$10$fIlcyUbp4JxhQa2xUQIh5emZpfbycmqTAkN4im4df7Kj4bzxzKDmm', '+VtFqZ1lpfUj3w1/GzGLOw==', date('2023-08-07'), date('2023-08-07'));

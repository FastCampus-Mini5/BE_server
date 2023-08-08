insert into user_tb
(id, email, password, username, role, hire_date, profile_image, created_date, updated_date)
values (1, 'kjoJiIPBz6f5YQXgKxpurQ==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'qZD2MFWVmHViKSeDLx+Jlg==', 'USER', date('2019-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (2, 'VynYMl7zNOhDiqMVFC6ODg==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'Xo/mZuQhqRk2IEl9JLxQPg==', 'USER', date('2019-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (3, '9ef2PAWiR6rG9yeTqXyflg==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', '9707LSJzSYUes8mE7vQIcQ==', 'USER', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (4, 'DTPa6fWGFf+WCxYSfyAcmA==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'SrO8yQpIcPNdGE9X+VobHw==', 'USER', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (5, '5HgZNdfxLiIBNlMYdx5E8A==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', '5qLY5JRdi7qElLVRImMNsg==', 'USER', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (6, '5JWFP8E3hDvU2V8fvyd/Vw==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'L2fsVi6sO5oRMru0PdMaBQ==', 'USER', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (7, 'k3zB3mbHhxsqb0jvPiYE9A==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'BZXtt5b+4KoBmwLIRFbdxA==', 'USER', date('2022-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (8, 'Glp8+lCvkG2/NW5+NIcUgg==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'bZhLMBTjNpqqOX6VEtbhSA==', 'USER', date('2023-01-01'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (9, 'ePnPG780PCAyn3WpkChwGQ==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'ZHIVPsqfQ5s1O2s3L/JR6A==', 'USER', date('2023-05-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (10, 'YTdARKcFYOKmP9rexFG87g==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'q+ggvsgjm7waV879YZKJYg==', 'USER', date('2023-08-06'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (80, '4e763MpUxEh8+91H69EnRw==', '$2a$10$zbxTMhR86pzcIA7Hb0.aludfUUzu9ModAlHzNaJZUkaIaXx0iRJHK', '4e763MpUxEh8+91H69EnRw==', 'ADMIN', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (100, 'vYmi29ELW93OC2X8IvZeEcYSH75XSiou3NU+1JYquqI=', '$2a$10$v8FySam6mvG18prce/3Vu.4.pNPIcbRYObbuSGfeC41yQ4PyHVy0u', '4e763MpUxEh8+91H69EnRw==', 'ADMIN', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17')),
       (200, 'wL0RQKcssRtFW0nUSRMJcJ4GCZ+Mu19OIh0T/d54fWs=', '$2a$10$WvBthERMv4stQe4aU0IZNOFKoBc08.TEP1LdY59X8Bk8el3LPWQ7a', 'bhyOYqovPimR7IBuX74dfA==', 'ADMIN', date('2023-03-17'), '/image/default.png', date('2023-03-17'),
        date('2023-03-17'));

insert into vacation_tb
(id, user_id, created_date, start_date, end_date, reason, status, approval_date)
values (1, 1, date('2023-08-07'), date('2023-08-28'), date('2023-08-29'), '휴가', 'PENDING', null),
       (2, 2, date('2023-08-10'), date('2023-08-25'), date('2023-08-25'), '연차', 'PENDING', null),
       (3, 3, date('2023-08-10'), date('2023-08-24'), date('2023-08-25'), '휴가', 'PENDING', null),
       (4, 4, date('2023-08-07'), date('2023-08-14'), date('2023-08-14'), '연차', 'PENDING', null),
       (5, 5, date('2023-08-10'), date('2023-08-21'), date('2023-08-21'), '기타사항', 'PENDING', null),
       (6, 6, date('2023-06-29'), date('2023-07-03'), date('2023-07-05'), '휴가', 'APPROVE', date('2023-06-30')),
       (7, 7, date('2023-07-24'), date('2023-07-28'), date('2023-07-28'), '연차', 'APPROVE', date('2023-07-25')),
       (8, 8, date('2023-07-07'), date('2023-07-13'), date('2023-07-14'), '휴가', 'APPROVE', date('2023-07-07')),
       (9, 9, date('2023-07-27'), date('2023-08-07'), date('2023-08-07'), '연차', 'APPROVE', date('2023-07-28')),
       (10, 10, date('2023-07-27'), date('2023-08-14'), date('2023-08-16'), '기타사항', 'APPROVE', date('2023-08-01'));

insert into vacation_info_tb
(id, user_id, remain_vacation, used_vacation)
values (1, 1, 5, 2),
       (2, 2, 6, 3),
       (3, 3, 7, 4),
       (4, 4, 8, 5),
       (5, 5, 9, 6),
       (6, 6, 14, 1),
       (7, 7, 8, 2),
       (8, 8, 9, 3),
       (9, 9, 6, 4),
       (10, 10, 9, 6);

insert into on_duty_tb
(id, user_id, created_date, updated_date, duty_date, status, approval_date)
values (1, 1, date('2023-07-27'), date('2023-08-27'), date('2023-08-27'), 'PENDING', null),
       (2, 2, date('2023-08-01'), date('2023-08-25'), date('2023-08-25'), 'PENDING', null),
       (3, 3, date('2023-08-06'), date('2023-08-06'), date('2023-08-06'), 'PENDING', null),
       (4, 4, date('2023-08-08'), date('2023-08-08'), date('2023-08-08'), 'PENDING', null),
       (5, 5, date('2023-07-30'), date('2023-07-30'), date('2023-07-30'), 'PENDING', null),
       (6, 6, date('2023-07-05'), date('2023-07-07'), date('2023-07-07'), 'APPROVE', date('2023-08-08')),
       (7, 7, date('2023-07-07'), date('2023-07-15'), date('2023-07-15'), 'APPROVE', date('2023-08-08')),
       (8, 8, date('2023-08-07'), date('2023-08-13'), date('2023-08-13'), 'APPROVE', date('2023-08-08')),
       (9, 9, date('2023-07-01'), date('2023-07-04'), date('2023-07-04'), 'APPROVE', date('2023-08-08')),
       (10, 10, date('2023-07-17'), date('2023-07-30'), date('2023-07-30'), 'APPROVE', date('2023-08-08'));

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
values (1, '08AddoLhJyEmYONIQGFmWw==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', '6Yuhi3kPGsYbZaC0jQnBBQ==', date('2023-07-27'), date('2023-07-27')),
       (2, 'AoJHavkGWBmssmfQvePZLQ==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'kL73OkEV2DwMAqollpd4Ww==', date('2023-07-27'), date('2023-07-27')),
       (3, 'DdMRuO/NaAGPKRMpV8alaw==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'yB7PhYcQJT2VX4H8JkA4Mg==', date('2023-07-27'), date('2023-07-27')),
       (4, 'gUggOtJlO4IAZg8RbRL5gg==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', '1+VDIyhBiW03eFHkWd0vaA==', date('2023-07-27'), date('2023-07-27')),
       (5, 'fzT1m0xg0T2EtmKOrA2RWA==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', '9Q9vrVEnqAZrJDRn762EoQ==', date('2023-07-27'), date('2023-07-27')),
       (6, 'Wvku86fEs+oAp74fmlwE1Q==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'ykLY7jQCVy96QAys5RVJaQ==', date('2023-07-27'), date('2023-07-27')),
       (7, 'jLj+a71rB+VO01jrzI4Rdw==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'Mu0GzYrlDm+GwsNiOcOEbA==', date('2023-07-27'), date('2023-07-27')),
       (8, 'oZiaCArK/627uiBcm2FXKA==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'OHhj4xSHIViCf/MSMi1SYA==', date('2023-07-27'), date('2023-07-27')),
       (9, 'fSOImPr1HLN/aquCaF+whQ==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'YgG5SEaVM4fwyeYLzT0Gqg==', date('2023-07-27'), date('2023-07-27')),
       (10, 'RF4F4Mm1CW4OMDwLnc9dxA==', '$2a$10$YI4Mh0gwbeKw12Wjo0/OPOgNFNg9h2GZRmtbzWcCgaBOIJqOSnur.', 'sNz9EkGmtef/fKDgATjtjw==', date('2023-07-27'), date('2023-07-27'));
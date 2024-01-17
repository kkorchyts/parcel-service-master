INSERT INTO parcel_service.parcels (tracking_number, user_id, length, width, height, weight, destination)
VALUES ('TRACK94673182', '1', 25.3, 10.5, 5.5, 23.4, '1421 Oak Avenue, Boulder, CO 80302, USA'),
       ('TRACK50892473', '2', 12.8, 6.7, 5.1, 2.9, '12 Smith Street, Auckland Central, Auckland 1010, New Zealand'),
       ('TRACK16985723', '3', 7.2, 3.6, 1.9, 0.5, '47 Rue des Acacias, 75017 Paris, France'),
       ('TRACK74321850', '4', 18.6, 10.2, 6.5, 3.8, '1025 South Olive Street, Los Angeles, CA 90015, USA');

INSERT INTO parcel_service.tracking (id, tracking_status, timestamp, tracking_number)
VALUES (1, 'REGISTERED', '2023-05-01T06:23:10Z', 'TRACK94673182'),
       (2, 'IN_PROGRESS', '2023-05-01T10:45:35Z', 'TRACK94673182'),
       (3, 'REGISTERED', '2023-04-30T08:05:45Z', 'TRACK50892473'),
       (4, 'REGISTERED', '2023-04-29T10:05:13Z', 'TRACK16985723'),
       (5, 'IN_PROGRESS', '2023-04-30T12:24:45Z', 'TRACK16985723'),
       (6, 'DELIVERED', '2023-05-01T13:47:35Z', 'TRACK16985723'),
       (7, 'REGISTERED', '2023-05-01T08:23:14Z', 'TRACK74321850'),
       (8, 'CANCELLED', '2023-05-01T10:01:07Z', 'TRACK74321850');

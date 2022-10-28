#!/usr/bin/env bash
set -e
export POSTGRES_USER=postgres
export PGPASSWORD=admin

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL

    CREATE TABLE public.toll_company(
       company_id INT PRIMARY KEY     NOT NULL,
       company_name           TEXT    NOT NULL
    );
    CREATE TABLE public.toll_booth(
       toll_booth_id INT PRIMARY KEY     NOT NULL,
       company_id INT references toll_company,
       booth_lane_no  INT NOT NULL
    );
    CREATE TABLE public.toll_pass(
       pass_id INT PRIMARY KEY     NOT NULL,
       vehicle_type VARCHAR(3) NOT NULL,
       toll_company_id INT references toll_company,
       rate INT,
       pass_date DATE,
       pass_type  VARCHAR(7) NOT NULL
    );
    CREATE TABLE public.vehicle(
      vehicle_id serial PRIMARY KEY,
      registration_no VARCHAR(10) NOT NULL,
      vehicle_type VARCHAR(3)
    );
    CREATE TABLE public.toll_ticket(
       ticket_id serial PRIMARY KEY,
       ticket_date DATE,
       ticket_expiry_date DATE,
       toll_booth_id INT references toll_booth,
       vehicle_id INT references vehicle,
       pass_id  INT references toll_pass
    );

    INSERT INTO public.toll_company VALUES (1, 'TNP');
    INSERT INTO public.toll_company VALUES (2, 'TNG');

    INSERT INTO public.toll_booth VALUES (1, 1, 1);
    INSERT INTO public.toll_booth VALUES (2, 1, 2);
    INSERT INTO public.toll_booth VALUES (3, 1, 3);
    INSERT INTO public.toll_booth VALUES (4, 1, 1);
    INSERT INTO public.toll_booth VALUES (5, 1, 2);
    INSERT INTO public.toll_booth VALUES (6, 1, 3);
    INSERT INTO public.toll_booth VALUES (7, 2, 1);
    INSERT INTO public.toll_booth VALUES (8, 2, 1);
    INSERT INTO public.toll_booth VALUES (9, 2, 1);


    INSERT INTO public.toll_pass VALUES (1, '2W', 1, 20, '2022-10-26', 'SINGLE');
    INSERT INTO public.toll_pass VALUES (2, '2W', 1, 20, '2022-10-26', 'RETURN');
    INSERT INTO public.toll_pass VALUES (3, '2W', 1, 20, '2022-10-26', 'WEEKLY');
    INSERT INTO public.toll_pass VALUES (4, '4W', 1, 50, '2022-10-26', 'SINGLE');
    INSERT INTO public.toll_pass VALUES (5, '4W', 1, 50, '2022-10-26', 'RETURN');
    INSERT INTO public.toll_pass VALUES (6, '4W', 1, 50, '2022-10-26', 'WEEKLY');

    INSERT INTO public.toll_pass VALUES (7, '2W', 2, 10, '2022-10-26', 'SINGLE');
    INSERT INTO public.toll_pass VALUES (8, '2W', 2, 10, '2022-10-26', 'RETURN');
    INSERT INTO public.toll_pass VALUES (9, '2W', 2, 10, '2022-10-26', 'WEEKLY');
    INSERT INTO public.toll_pass VALUES (10, '4W', 2, 30, '2022-10-26', 'SINGLE');
    INSERT INTO public.toll_pass VALUES (11, '4W', 2, 30, '2022-10-26', 'RETURN');
    INSERT INTO public.toll_pass VALUES (12, '4W', 2, 30, '2022-10-26', 'WEEKLY');
EOSQL
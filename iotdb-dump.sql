--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: equipment_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.equipment_type AS ENUM (
    'Compressor',
    'Turbine',
    'Pump'
);


ALTER TYPE public.equipment_type OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admins (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL
);


ALTER TABLE public.admins OWNER TO postgres;

--
-- Name: admin_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.admin_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.admin_id_seq OWNER TO postgres;

--
-- Name: admin_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.admin_id_seq OWNED BY public.admins.id;


--
-- Name: equipments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipments (
    id integer NOT NULL,
    location character varying(255),
    type public.equipment_type NOT NULL,
    equipment_id character varying(15)
);


ALTER TABLE public.equipments OWNER TO postgres;

--
-- Name: equipments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipments_id_seq OWNER TO postgres;

--
-- Name: equipments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipments_id_seq OWNED BY public.equipments.id;


--
-- Name: faultyrecords; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.faultyrecords (
    id integer NOT NULL,
    "time" timestamp without time zone NOT NULL,
    equipment_id character varying(15)
);


ALTER TABLE public.faultyrecords OWNER TO postgres;

--
-- Name: faultyrecords_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.faultyrecords_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.faultyrecords_id_seq OWNER TO postgres;

--
-- Name: faultyrecords_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.faultyrecords_id_seq OWNED BY public.faultyrecords.id;


--
-- Name: maintainrecords; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.maintainrecords (
    id integer NOT NULL,
    "time" timestamp without time zone NOT NULL,
    operator_name character varying(15),
    equipment_id character varying(15)
);


ALTER TABLE public.maintainrecords OWNER TO postgres;

--
-- Name: maintainrecords_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.maintainrecords_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.maintainrecords_id_seq OWNER TO postgres;

--
-- Name: maintainrecords_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.maintainrecords_id_seq OWNED BY public.maintainrecords.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    location character varying(150)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: admins id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins ALTER COLUMN id SET DEFAULT nextval('public.admin_id_seq'::regclass);


--
-- Name: equipments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments ALTER COLUMN id SET DEFAULT nextval('public.equipments_id_seq'::regclass);


--
-- Name: faultyrecords id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.faultyrecords ALTER COLUMN id SET DEFAULT nextval('public.faultyrecords_id_seq'::regclass);


--
-- Name: maintainrecords id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainrecords ALTER COLUMN id SET DEFAULT nextval('public.maintainrecords_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: admins; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.admins (id, username, password) FROM stdin;
1	admin	671f17e380ad1f5782913eef7be1476ef7b31b0e29950d45f49a62b683a7e820
\.


--
-- Data for Name: equipments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equipments (id, location, type, equipment_id) FROM stdin;
2	Atlanta	Turbine	sensor_0
3	Chicago	Compressor	sensor_1
4	San Francisco	Turbine	sensor_2
5	Atlanta	Pump	sensor_3
6	New York	Pump	sensor_4
7	Houston	Compressor	sensor_5
8	Houston	Pump	sensor_6
9	Atlanta	Compressor	sensor_7
10	New York	Compressor	sensor_8
11	Houston	Compressor	sensor_9
12	San Francisco	Turbine	sensor_10
13	New York	Turbine	sensor_11
14	Atlanta	Compressor	sensor_12
15	New York	Compressor	sensor_13
16	Chicago	Pump	sensor_14
17	Atlanta	Turbine	sensor_15
18	Atlanta	Compressor	sensor_16
19	Chicago	Turbine	sensor_17
20	Atlanta	Pump	sensor_18
21	Chicago	Compressor	sensor_19
22	Houston	Pump	sensor_20
23	Houston	Turbine	sensor_21
24	New York	Pump	sensor_22
25	San Francisco	Pump	sensor_23
26	San Francisco	Pump	sensor_24
27	Atlanta	Compressor	sensor_25
28	New York	Compressor	sensor_26
29	San Francisco	Turbine	sensor_27
30	Houston	Turbine	sensor_28
31	Houston	Compressor	sensor_29
32	Atlanta	Compressor	sensor_30
33	Chicago	Turbine	sensor_31
34	New York	Compressor	sensor_32
35	Chicago	Compressor	sensor_33
36	San Francisco	Compressor	sensor_34
37	Chicago	Compressor	sensor_35
38	New York	Compressor	sensor_36
39	Atlanta	Pump	sensor_37
40	Atlanta	Turbine	sensor_38
41	San Francisco	Compressor	sensor_39
42	Houston	Pump	sensor_40
43	San Francisco	Turbine	sensor_41
44	Atlanta	Pump	sensor_42
45	Atlanta	Turbine	sensor_43
46	Atlanta	Turbine	sensor_44
47	Houston	Compressor	sensor_45
48	Chicago	Turbine	sensor_46
49	New York	Turbine	sensor_47
50	New York	Pump	sensor_48
51	Houston	Compressor	sensor_49
52	Atlanta	Compressor	sensor_50
53	Houston	Turbine	sensor_51
54	New York	Turbine	sensor_52
55	Chicago	Compressor	sensor_53
56	New York	Compressor	sensor_54
57	Houston	Compressor	sensor_55
58	New York	Turbine	sensor_56
59	Houston	Compressor	sensor_57
60	Houston	Pump	sensor_58
61	San Francisco	Pump	sensor_59
62	Atlanta	Compressor	sensor_60
63	Atlanta	Compressor	sensor_61
64	Chicago	Turbine	sensor_62
65	Chicago	Compressor	sensor_63
66	San Francisco	Compressor	sensor_64
67	Atlanta	Compressor	sensor_65
68	Houston	Compressor	sensor_66
69	Chicago	Turbine	sensor_67
70	San Francisco	Compressor	sensor_68
71	San Francisco	Pump	sensor_69
72	New York	Turbine	sensor_70
73	Chicago	Pump	sensor_71
74	Houston	Compressor	sensor_72
75	San Francisco	Compressor	sensor_73
76	Houston	Pump	sensor_74
77	Chicago	Turbine	sensor_75
78	Atlanta	Compressor	sensor_76
79	San Francisco	Turbine	sensor_77
80	Atlanta	Turbine	sensor_78
81	New York	Pump	sensor_79
82	Chicago	Compressor	sensor_80
83	Chicago	Turbine	sensor_81
84	Chicago	Turbine	sensor_82
85	San Francisco	Compressor	sensor_83
86	San Francisco	Compressor	sensor_84
87	Atlanta	Turbine	sensor_85
88	San Francisco	Compressor	sensor_86
89	Atlanta	Pump	sensor_87
90	New York	Pump	sensor_88
91	San Francisco	Compressor	sensor_89
92	New York	Turbine	sensor_90
93	San Francisco	Turbine	sensor_91
94	San Francisco	Pump	sensor_92
95	New York	Pump	sensor_93
96	Houston	Pump	sensor_94
97	Chicago	Pump	sensor_95
98	San Francisco	Compressor	sensor_96
99	Houston	Turbine	sensor_97
100	Chicago	Pump	sensor_98
101	Chicago	Pump	sensor_99
\.


--
-- Data for Name: faultyrecords; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.faultyrecords (id, "time", equipment_id) FROM stdin;
1	2025-04-11 12:38:36.557511	sensor_2
2	2025-04-11 12:38:48.003397	sensor_39
3	2025-04-12 12:51:30.753131	sensor_14
4	2025-04-12 12:51:33.541315	sensor_23
5	2025-04-12 12:51:37.557562	sensor_36
6	2025-04-12 12:52:38.577814	sensor_16
7	2025-04-12 12:52:38.885536	sensor_17
8	2025-04-12 12:52:45.37498	sensor_38
9	2025-04-12 12:52:49.698624	sensor_52
10	2025-04-12 12:52:53.105671	sensor_63
\.


--
-- Data for Name: maintainrecords; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.maintainrecords (id, "time", operator_name, equipment_id) FROM stdin;
1	2025-04-12 12:51:33	kkk	sensor_0
7	2025-04-05 12:51:33	kk	sensor_2
2	2025-04-09 12:51:33	kkk	sensor_1
4	2025-04-06 12:51:33	kkk	sensor_2
8	2025-04-08 12:51:33	kk	sensor_1
6	2025-04-10 12:51:33	kkk	sensor_1
3	2025-04-11 12:51:33	kkk	sensor_2
5	2025-04-07 12:51:33	kkk	sensor_2
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, password, location) FROM stdin;
7	add	123	Chicago
9	addeeee	671f17e380ad1f5782913eef7be1476ef7b31b0e29950d45f49a62b683a7e820	Chicago
13	kkk	671f17e380ad1f5782913eef7be1476ef7b31b0e29950d45f49a62b683a7e820	Chicago
11	kk	caa74459209155bfe2b02ec0c9c687199fe6c5a41c96199f9148d821bd660e0b	Chicago222
\.


--
-- Name: admin_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.admin_id_seq', 1, true);


--
-- Name: equipments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.equipments_id_seq', 101, true);


--
-- Name: faultyrecords_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.faultyrecords_id_seq', 10, true);


--
-- Name: maintainrecords_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.maintainrecords_id_seq', 8, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 13, true);


--
-- Name: admins admin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT admin_pkey PRIMARY KEY (id);


--
-- Name: admins admin_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT admin_username_key UNIQUE (username);


--
-- Name: equipments equipments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments
    ADD CONSTRAINT equipments_pkey PRIMARY KEY (id);


--
-- Name: faultyrecords faultyrecords_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.faultyrecords
    ADD CONSTRAINT faultyrecords_pkey PRIMARY KEY (id);


--
-- Name: maintainrecords maintainrecords_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainrecords
    ADD CONSTRAINT maintainrecords_pkey PRIMARY KEY (id);


--
-- Name: equipments unique_equipment_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments
    ADD CONSTRAINT unique_equipment_id UNIQUE (equipment_id);


--
-- Name: users unique_username; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT unique_username UNIQUE (username);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: faultyrecords fk_faultyrecords_equipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.faultyrecords
    ADD CONSTRAINT fk_faultyrecords_equipment FOREIGN KEY (equipment_id) REFERENCES public.equipments(equipment_id) ON DELETE CASCADE;


--
-- Name: maintainrecords maintainrecords___fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainrecords
    ADD CONSTRAINT maintainrecords___fk FOREIGN KEY (operator_name) REFERENCES public.users(username);


--
-- Name: maintainrecords maintainrecords___fk_2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainrecords
    ADD CONSTRAINT maintainrecords___fk_2 FOREIGN KEY (equipment_id) REFERENCES public.equipments(equipment_id);


--
-- PostgreSQL database dump complete
--


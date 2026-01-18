-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.
CREATE TABLE public.users (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  created_at timestamp with time zone,
  date_of_birth character varying,
  email character varying NOT NULL UNIQUE,
  role character varying,
  supabase_id character varying NOT NULL UNIQUE,
  updated_at timestamp with time zone,
  user_name character varying,
  CONSTRAINT users_pkey PRIMARY KEY (id)
);
CREATE TABLE public.posts (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  content text NOT NULL,
  created_at timestamp with time zone,
  metadata jsonb,
  updated_at timestamp with time zone,
  user_id bigint NOT NULL,
  author_id uuid NOT NULL,
  CONSTRAINT posts_pkey PRIMARY KEY (id),
  CONSTRAINT fk5lidm6cqbc7u4xhqpxm898qme FOREIGN KEY (user_id) REFERENCES public.users(id)
);
CREATE TABLE public.comments (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  created_at timestamp with time zone,
  message text NOT NULL,
  post_id bigint NOT NULL,
  user_id bigint NOT NULL,
  author_id uuid NOT NULL,
  content text NOT NULL,
  CONSTRAINT comments_pkey PRIMARY KEY (id),
  CONSTRAINT fk8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES public.users(id),
  CONSTRAINT fkh4c7lvsc298whoyd4w9ta25cr FOREIGN KEY (post_id) REFERENCES public.posts(id)
);
CREATE TABLE public.messages (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  content text NOT NULL,
  created_at timestamp with time zone,
  to_user_email character varying NOT NULL,
  room_id character varying,
  sender_id bigint NOT NULL,
  CONSTRAINT messages_pkey PRIMARY KEY (id),
  CONSTRAINT fk4ui4nnwntodh6wjvck53dbk9m FOREIGN KEY (sender_id) REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS tags
(
    id bigserial NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by bigserial NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by bigserial NOT NULL,
    tag_name character varying(255) ,
    CONSTRAINT tags_pkey PRIMARY KEY (id),
    CONSTRAINT tag_name UNIQUE (tag_name)
);
CREATE TABLE IF NOT EXISTS employer
(
    id bigserial NOT NULL ,
    created_at timestamp(6) without time zone NOT NULL,
    created_by bigserial NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by bigserial NOT NULL,
    company_size integer,
    description character varying(255),
    email character varying(255) ,
    industry character varying(255),
    is_active boolean,
    location character varying(255) ,
    name character varying(255) ,
    website character varying(255) ,
    CONSTRAINT employer_pkey PRIMARY KEY (id)

);
CREATE TABLE IF NOT EXISTS candidate
(
    id bigserial NOT NULL ,
    created_at timestamp(6) without time zone NOT NULL,
    created_by bigserial NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by bigserial NOT NULL,
    email character varying(255)  ,
    experience_summary character varying(255) ,
    is_active boolean,
    name character varying(255)  ,
    phone bigserial,
    resume_url character varying(255) ,
    CONSTRAINT candidate_pkey PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS candidate_skill
(
    id bigserial NOT NULL ,
    skill_name character varying(255) ,
    candidate_id bigserial,
    CONSTRAINT candidate_skill_pkey PRIMARY KEY (id),
    CONSTRAINT Candidate_Id_FK FOREIGN KEY (candidate_id)
        REFERENCES candidate (id) 
      
);
CREATE TABLE IF NOT EXISTS candidate_tags
(
    id bigserial NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by bigserial NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by bigserial NOT NULL,
    candidate_id bigserial,
    tag_id bigserial NOT NULL,
    CONSTRAINT candidate_tags_pkey PRIMARY KEY (id),
    CONSTRAINT Tag_Id_Fk FOREIGN KEY (tag_id)
        REFERENCES tags (id) ,
    CONSTRAINT Candidate_Id_FK FOREIGN KEY (candidate_id)
        REFERENCES candidate (id)       
);

CREATE TABLE IF NOT EXISTS job
(
    id bigserial NOT NULL ,
    category character varying(255) ,
    description character varying(255),
    employment_type character varying(255),
    expiry_date date,
    is_active boolean,
    max_salary integer,
    min_salary integer,
    posted_date date,
    required_skills character varying(255)[] ,
    title character varying(255) ,
    employer_id bigserial NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by bigserial NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by bigserial NOT NULL,
    tag_id bigserial,
    CONSTRAINT job_pkey PRIMARY KEY (id),
    CONSTRAINT Tag_Id_FK FOREIGN KEY (tag_id)
        REFERENCES tags (id),
    CONSTRAINT Employer_Id_FK FOREIGN KEY (employer_id)
        REFERENCES employer (id) 
);

CREATE TABLE IF NOT EXISTS application
(
    id bigserial NOT NULL ,
    created_at timestamp(6) without time zone NOT NULL,
    created_by bigserial NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by bigserial NOT NULL,
    application_status character varying(255),
    is_applied boolean,
    submitted_date date,
    candidate_id bigserial NOT NULL,
    job_id bigserial NOT NULL,
    CONSTRAINT application_pkey PRIMARY KEY (id),
    CONSTRAINT Candidate_Id_FK FOREIGN KEY (candidate_id)
        REFERENCES candidate (id) ,
    CONSTRAINT Job_Id_FK FOREIGN KEY (job_id)
        REFERENCES job (id)
);
CREATE TABLE IF NOT EXISTS interview
(
    id bigserial NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by bigserial NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by bigserial NOT NULL,
    interview_date timestamp(6) without time zone,
    interview_status character varying(255) ,
    interview_type character varying(255),
    application_id bigserial NOT NULL,
    CONSTRAINT interview_pkey PRIMARY KEY (id),
    CONSTRAINT Application_Id_FK FOREIGN KEY (application_id)
        REFERENCES application (id) 
);


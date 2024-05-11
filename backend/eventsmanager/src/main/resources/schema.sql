CREATE TABLE IF NOT EXISTS Events (
    id SERIAL PRIMARY KEY,
    title varchar(250) NOT NULL,
    startTime timestamp NOT NULL,
    location varchar(100) NOT NULL,
    info varchar(1000)
);

CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    id_code VARCHAR(50) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    info TEXT CHECK (length(info) <= 1500)
);

CREATE TABLE IF NOT EXISTS companies (
    id SERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    company_code VARCHAR(50) NOT NULL,
    participant_count INTEGER,
    payment_method VARCHAR(255) NOT NULL,
    info TEXT NOT NULL CHECK (length(info) <= 5000)
);


CREATE TABLE IF NOT EXISTS event_participants (
    event_id INTEGER REFERENCES events(id),
    participant_id INTEGER,  
    participant_type VARCHAR(10) CHECK (participant_type IN ('customer', 'company')), 
    PRIMARY KEY (event_id, participant_id, participant_type)
);
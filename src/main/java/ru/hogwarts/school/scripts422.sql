CREATE TABLE cars
(
    car_name   TEXT PRIMARY KEY,
    model_name TEXT NOT NULL,
    cost       INTEGER CHECK ( cost > 0 )
);

CREATE TABLE drivers
(
    name        TEXT                            NOT NULL,
    age         INTEGER CHECK (age > 17)        NOT NULL,
    certificate BOOLEAN DEFAULT true            NOT NULL,
    car_name    TEXT REFERENCES cars (car_name) NOT NULL
);

INSERT INTO cars (car_name, model_name, cost)
VALUES ('Toyota', 'Camry', 3000000);
INSERT INTO drivers (name, age, certificate, car_name)
VALUES ('Valentin', 25, true, 'Toyota');
INSERT INTO drivers (name, age, certificate, car_name)
VALUES ('Igor', 34, true, 'Toyota');
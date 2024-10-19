CREATE TABLE product
(
    id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name     VARCHAR(255)     NOT NULL,
    type     VARCHAR(100)     NOT NULL,
    value    DOUBLE PRECISION NOT NULL,
    quantity INTEGER          NOT NULL
);
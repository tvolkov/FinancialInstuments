DROP TABLE IF EXISTS INSTRUMENT_PRICE_MODIFIER;
CREATE MEMORY TEMPORARY TABLE IF NOT EXISTS INSTRUMENT_PRICE_MODIFIER(ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL, NAME CHAR NOT NULL, MULTIPLIER DOUBLE);
INSERT INTO INSTRUMENT_PRICE_MODIFIER VALUES (1, 'INSTRUMENT1', 1.23);
INSERT INTO INSTRUMENT_PRICE_MODIFIER VALUES (2, 'INSTRUMENT2', 4.56);
INSERT INTO INSTRUMENT_PRICE_MODIFIER VALUES (3, 'INSTRUMENT3', 7.89);
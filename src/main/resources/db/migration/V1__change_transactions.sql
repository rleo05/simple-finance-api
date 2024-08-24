ALTER TABLE transactions_tb
DROP CONSTRAINT fkb4gsrhr0vdtxtqvsjt7vihhox,
ADD CONSTRAINT fkb4gsrhr0vdtxtqvsjt7vihhox FOREIGN KEY (sender)
REFERENCES accounts_tb (id) ON DELETE SET NULL;

ALTER TABLE transactions_tb
DROP CONSTRAINT fkjmyvcjgimnrib2x356eqst7ac,
ADD CONSTRAINT fkjmyvcjgimnrib2x356eqst7ac FOREIGN KEY (receiver)
REFERENCES accounts_tb (id) ON DELETE SET NULL;

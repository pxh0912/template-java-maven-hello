CREATE TABLE Products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_code TEXT,
    product_name TEXT,
    manufacturer TEXT,
    production_date DATE,
    model TEXT,
    purchase_price REAL,
    retail_price REAL,
    quantity INTEGER
);

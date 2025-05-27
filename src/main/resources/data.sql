-- 브랜드 데이터 삽입
INSERT INTO brand (name) VALUES 
('A'), ('B'), ('C'), ('D'), ('E'), ('F'), ('G'), ('H'), ('I');

-- 상품 데이터 삽입 (브랜드 A)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'A 상의', 11200, 1),
('OUTERWEAR', 'A 아우터', 5500, 1),
('PANTS', 'A 바지', 4200, 1),
('SNEAKERS', 'A 스니커즈', 9000, 1),
('BAGS', 'A 가방', 2000, 1),
('HATS', 'A 모자', 1700, 1),
('SOCKS', 'A 양말', 1800, 1),
('ACCESSORIES', 'A 액세서리', 2300, 1);

-- 상품 데이터 삽입 (브랜드 B)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'B 상의', 10500, 2),
('OUTERWEAR', 'B 아우터', 5900, 2),
('PANTS', 'B 바지', 3800, 2),
('SNEAKERS', 'B 스니커즈', 9100, 2),
('BAGS', 'B 가방', 2100, 2),
('HATS', 'B 모자', 2000, 2),
('SOCKS', 'B 양말', 2000, 2),
('ACCESSORIES', 'B 액세서리', 2200, 2);

-- 상품 데이터 삽입 (브랜드 C)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'C 상의', 10000, 3),
('OUTERWEAR', 'C 아우터', 6200, 3),
('PANTS', 'C 바지', 3300, 3),
('SNEAKERS', 'C 스니커즈', 9200, 3),
('BAGS', 'C 가방', 2200, 3),
('HATS', 'C 모자', 1900, 3),
('SOCKS', 'C 양말', 2200, 3),
('ACCESSORIES', 'C 액세서리', 2100, 3);

-- 상품 데이터 삽입 (브랜드 D)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'D 상의', 10100, 4),
('OUTERWEAR', 'D 아우터', 5100, 4),
('PANTS', 'D 바지', 3000, 4),
('SNEAKERS', 'D 스니커즈', 9500, 4),
('BAGS', 'D 가방', 2500, 4),
('HATS', 'D 모자', 1500, 4),
('SOCKS', 'D 양말', 2400, 4),
('ACCESSORIES', 'D 액세서리', 2000, 4);

-- 상품 데이터 삽입 (브랜드 E)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'E 상의', 10700, 5),
('OUTERWEAR', 'E 아우터', 5000, 5),
('PANTS', 'E 바지', 3800, 5),
('SNEAKERS', 'E 스니커즈', 9900, 5),
('BAGS', 'E 가방', 2300, 5),
('HATS', 'E 모자', 1800, 5),
('SOCKS', 'E 양말', 2100, 5),
('ACCESSORIES', 'E 액세서리', 2100, 5);

-- 상품 데이터 삽입 (브랜드 F)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'F 상의', 11200, 6),
('OUTERWEAR', 'F 아우터', 7200, 6),
('PANTS', 'F 바지', 4000, 6),
('SNEAKERS', 'F 스니커즈', 9300, 6),
('BAGS', 'F 가방', 2100, 6),
('HATS', 'F 모자', 1600, 6),
('SOCKS', 'F 양말', 2300, 6),
('ACCESSORIES', 'F 액세서리', 1900, 6);

-- 상품 데이터 삽입 (브랜드 G)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'G 상의', 10500, 7),
('OUTERWEAR', 'G 아우터', 5800, 7),
('PANTS', 'G 바지', 3900, 7),
('SNEAKERS', 'G 스니커즈', 9000, 7),
('BAGS', 'G 가방', 2200, 7),
('HATS', 'G 모자', 1700, 7),
('SOCKS', 'G 양말', 2100, 7),
('ACCESSORIES', 'G 액세서리', 2000, 7);

-- 상품 데이터 삽입 (브랜드 H)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'H 상의', 10800, 8),
('OUTERWEAR', 'H 아우터', 6300, 8),
('PANTS', 'H 바지', 3100, 8),
('SNEAKERS', 'H 스니커즈', 9700, 8),
('BAGS', 'H 가방', 2100, 8),
('HATS', 'H 모자', 1600, 8),
('SOCKS', 'H 양말', 2000, 8),
('ACCESSORIES', 'H 액세서리', 2000, 8);

-- 상품 데이터 삽입 (브랜드 I)
INSERT INTO product (category, name, price, brand_id) VALUES 
('TOPS', 'I 상의', 11400, 9),
('OUTERWEAR', 'I 아우터', 6700, 9),
('PANTS', 'I 바지', 3200, 9),
('SNEAKERS', 'I 스니커즈', 9500, 9),
('BAGS', 'I 가방', 2400, 9),
('HATS', 'I 모자', 1700, 9),
('SOCKS', 'I 양말', 1700, 9),
('ACCESSORIES', 'I 액세서리', 2400, 9); 
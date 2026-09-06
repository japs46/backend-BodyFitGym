-- ============================================================
-- USUARIO ADMINISTRADOR POR DEFECTO (solo para desarrollo local)
-- ON CONFLICT DO NOTHING garantiza idempotencia entre reinicios.
-- Password en texto plano: Admin123$
-- ============================================================
INSERT INTO gym_user (document, name, last_name, user_name, password, status, role, registration_date)
VALUES (
    '0000000000',
    'Administrador',
    'BodyFitGym',
    'admin',
    '$2y$10$V1D7YdTVzFLtbwQI4p/e2uMdkICetGH0iKbgO5SDN5GbsUu1yW2bW',
    'Activo',
    'ADMINISTRADOR',
    CURRENT_DATE
)
ON CONFLICT (user_name) DO NOTHING;

-- Inserir registros de vídeos

INSERT INTO TB_VIDEOS (id, user_id, file_name, status, error_message, created_at, updated_at) VALUES
-- Video 1: Vídeo com sucesso (UPLOADED)
('d22ac0c5-3c5d-493e-bba4-95b559c4b835', 'user123', 'video1.mp4', 'UPLOADED', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Video 2: Vídeo em processamento (PROCESSING)
('6b3d4d3a-6b99-4bc4-87d1-6e0e6c8859a4', 'user456', 'video2.mp4', 'PROCESSING', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Video 3: Vídeo processado com sucesso (PROCESSED)
('d836f4e9-12a2-4f76-bd9a-0e2cdb98b11e', 'user789', 'video3.mp4', 'PROCESSED', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Video 4: Vídeo falhou ao processar (FAILED)
('f7a1a4c6-7e71-44f7-bba9-845cb5e1cf76', 'user123', 'video4.mp4', 'FAILED', 'Erro no processamento do vídeo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Video 5: Vídeo com sucesso (UPLOADED)
('a91232e5-e56f-4e07-b441-2db7b74db00d', 'user456', 'video5.mp4', 'UPLOADED', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

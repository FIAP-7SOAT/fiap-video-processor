-- Table: TB_VIDEOS
CREATE TABLE IF NOT EXISTS TB_VIDEOS (
    id UUID NOT NULL DEFAULT gen_random_uuid(), -- Gera UUID automaticamente (PostgreSQL)
    user_id VARCHAR(255) NOT NULL, -- Relacionado ao usuário que fez o upload
    file_name VARCHAR(255) NOT NULL, -- Nome do arquivo de vídeo
    status VARCHAR(50) NOT NULL, -- Status do processamento (UPLOADED, PROCESSING, PROCESSED, FAILED)
    error_message TEXT, -- Mensagem de erro, se falhar
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Última atualização
    CONSTRAINT tb_videos_pkey PRIMARY KEY (id)
);

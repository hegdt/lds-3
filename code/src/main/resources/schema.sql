-- Tabela Usuario
CREATE TABLE usuario (
                         id UUID PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL
);

-- Tabela Instituicao
CREATE TABLE instituicao (
                             id UUID PRIMARY KEY,
                             nome VARCHAR(255) NOT NULL,
                             cnpj VARCHAR(20) NOT NULL UNIQUE,
                             endereco TEXT NOT NULL
);

-- Tabela Aluno
CREATE TABLE aluno (
                       id UUID PRIMARY KEY,
                       cpf VARCHAR(14) NOT NULL UNIQUE,
                       rg VARCHAR(20) NOT NULL,
                       endereco TEXT NOT NULL,
                       curso VARCHAR(255) NOT NULL,
                       instituicao_id UUID NOT NULL,
                       saldo INTEGER NOT NULL DEFAULT 0,
                       CONSTRAINT fk_aluno_usuario FOREIGN KEY (id) REFERENCES usuario (id),
                       CONSTRAINT fk_aluno_instituicao FOREIGN KEY (instituicao_id) REFERENCES instituicao (id)
);

-- Tabela Professor
CREATE TABLE professor (
                           id UUID PRIMARY KEY,
                           cpf VARCHAR(14) NOT NULL UNIQUE,
                           departamento VARCHAR(255) NOT NULL,
                           instituicao_id UUID NOT NULL,
                           saldo INTEGER NOT NULL DEFAULT 0,
                           CONSTRAINT fk_professor_usuario FOREIGN KEY (id) REFERENCES usuario (id),
                           CONSTRAINT fk_professor_instituicao FOREIGN KEY (instituicao_id) REFERENCES instituicao (id)
);

-- Tabela Empresa
CREATE TABLE empresa (
                         id UUID PRIMARY KEY,
                         cnpj VARCHAR(20) NOT NULL UNIQUE,
                         nome_fantasia VARCHAR(255) NOT NULL,
                         CONSTRAINT fk_empresa_usuario FOREIGN KEY (id) REFERENCES usuario (id)
);

-- Tabela Vantagem
CREATE TABLE vantagem (
                          id UUID PRIMARY KEY,
                          titulo VARCHAR(255) NOT NULL,
                          descricao TEXT NOT NULL,
                          custo INTEGER NOT NULL,
                          foto TEXT,
                          empresa_id UUID NOT NULL,
                          CONSTRAINT fk_vantagem_empresa FOREIGN KEY (empresa_id) REFERENCES empresa (id)
);

-- Tabela Transacao
CREATE TABLE transacao (
                           id UUID PRIMARY KEY,
                           data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           tipo VARCHAR(50) NOT NULL,
                           quantidade INTEGER NOT NULL,
                           remetente_id UUID NOT NULL,
                           destinatario_id UUID NOT NULL,
                           mensagem TEXT NOT NULL,
                           CONSTRAINT fk_transacao_remetente FOREIGN KEY (remetente_id) REFERENCES usuario (id),
                           CONSTRAINT fk_transacao_destinatario FOREIGN KEY (destinatario_id) REFERENCES usuario (id)
);

-- Tabela Cupom
CREATE TABLE cupom (
                       id UUID PRIMARY KEY,
                       codigo VARCHAR(100) NOT NULL UNIQUE,
                       vantagem_id UUID NOT NULL,
                       aluno_id UUID NOT NULL,
                       data_geracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_cupom_vantagem FOREIGN KEY (vantagem_id) REFERENCES vantagem (id),
                       CONSTRAINT fk_cupom_aluno FOREIGN KEY (aluno_id) REFERENCES aluno (id)
);
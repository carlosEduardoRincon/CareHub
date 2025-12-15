# CareHub

Plataforma modular para gestão de agendamentos, notificações e histórico clínico. O projeto é composto por múltiplos serviços independentes, banco de dados relacional e um barramento de eventos para comunicação assíncrona.

## Arquitetura do Projeto

- Estilo: microserviços, comunicação assíncrona via eventos.
- Serviços principais:
  - Scheduling: gestão de agendas, pacientes e profissionais (APIs REST).
  - Notification: envio de notificações (e-mail/SMTP) reativas a eventos.
  - History: captura e consulta do histórico (GraphQL).
- Infraestrutura:
  - Banco de dados relacional (PostgreSQL).
  - Barramento de eventos (Apache Kafka).
  - Migrações de esquema (Flyway).
- Diagrama: consulte docs/diagrams/architecture.mermaid para uma visão de alto nível do tráfego entre serviços, DB, Kafka e SMTP.

Fluxo típico:
- Scheduling processa operações de domínio (por exemplo, criação/alteração de agendas) e publica eventos.
- Notification consome eventos e dispara e-mails transacionais (confirmações, lembretes, etc.).
- History consome eventos e persiste o histórico, expondo consultas via GraphQL.

Portas padrão:
- Notification: 8081
- Scheduling: 8082
- History: 8083
- PostgreSQL: 5432
- Kafka: 9092

## Tecnologias do Projeto

- Java 21 (LTS)
- Spring Boot (Web, Security, Data JPA, Validation)
- Spring for Apache Kafka
- PostgreSQL
- Flyway (migrações)
- GraphQL (no serviço History)
- OpenAPI/Swagger Codegen (no serviço Scheduling)
- Maven (build/gestão de dependências)
- Docker e Docker Compose (orquestração)

## Responsabilidade dos Módulos

- Scheduling:
  - Expõe APIs REST para gerenciamento de agendas, pacientes e profissionais.
  - Validação de dados e regras de negócio.
  - Publica eventos no Kafka após operações de domínio.
  - Geração de interfaces a partir de contratos (OpenAPI) para manter APIs consistentes.

- Notification:
  - Consome eventos do Kafka e envia notificações por e-mail via SMTP.
  - Integra com o banco de dados quando necessário (persistência/controle).
  
- History:
  - Consome eventos e persiste um trilho de auditoria/histórico.
  - Fornece consultas via GraphQL para relatórios e rastreabilidade.
  - Integra com banco de dados e Kafka.

## Diferentes Tipos de Usuários

- Paciente: visualiza e gerencia seus compromissos.
- Médico: gerencia agendas, confirmações e ajustes relacionados ao atendimento.
- Enfermeiro: apoia organização de agendas e prepara o atendimento.
- Administrador: governança do sistema, auditoria, parametrizações e acesso amplo conforme políticas.

Observações:
- Autenticação e autorização baseadas em tokens (JWT). As permissões são definidas por perfis/roles.
- As políticas de acesso podem variar por serviço conforme as regras de segurança configuradas.

## Descrição das Funcionalidades

- Gestão de agendas:
  - CRUD de agendas/horários, associação com profissionais e pacientes.
  - Publicação de eventos em alterações relevantes.
- Gestão de pacientes e profissionais:
  - Cadastro, atualização e consulta (orientado por contratos definidos).
- Notificações:
  - Envio de e-mails transacionais em eventos (criação/alteração).
- Histórico e auditoria:
  - Registro de eventos e mudanças.
  - Consulta via GraphQL para relatórios e rastreabilidade.
- Infra e confiabilidade:
  - Migrações automatizadas de banco (Flyway).
  - Comunicação assíncrona via Kafka para desacoplamento e resiliência.

## Como Rodar o Projeto

Você pode executar via Docker Compose (recomendado para desenvolvimento).

### Docker Compose

Pré-requisitos:
- Docker
- Docker Compose

Passos:
1. Crie um arquivo `.env` na raiz com as variáveis necessárias (veja “Configurações necessárias” abaixo).
2. Suba a stack:
   - docker compose up -d --build
3. Aguarde os healthchecks dos serviços (DB/Kafka/serviços de aplicação).
4. Acesse:
   - Scheduling: http://localhost:8082
   - Notification: http://localhost:8081
   - History (GraphQL): http://localhost:8083

Dicas:
- Use docker compose logs -f <serviço> para acompanhar logs.
- Para atualizar após mudanças de código, rode novamente com --build.

Observação:
- Migrações de banco (Flyway) são aplicadas automaticamente no startup dos serviços.

## Configurações Necessárias

Defina as variáveis de ambiente conforme seu ambiente. Não compartilhe segredos reais.

Comuns aos serviços:
- SPRING_PROFILES_ACTIVE: perfil de execução (ex.: local ou docker)
- SPRING_DATASOURCE_URL: JDBC do PostgreSQL (ex.: jdbc:postgresql://<host>:5432/<db>)
- SPRING_DATASOURCE_USERNAME: usuário do banco
- SPRING_DATASOURCE_PASSWORD: senha do banco
- SPRING_KAFKA_BOOTSTRAP_SERVERS: endereço do Kafka (ex.: localhost:9092 ou kafka:9092)
- JWT_SECRET: segredo utilizado para assinatura/validação de tokens (quando aplicável)

Específicos do Notification (e-mail SMTP):
- MAIL_HOST: host do servidor SMTP (ex.: smtp.seuprovedor.com)
- MAIL_PORT: porta SMTP (ex.: 587 para TLS)
- MAIL_USERNAME: usuário/conta SMTP
- MAIL_PASSWORD: senha/chave de app do provedor
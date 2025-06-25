# API CityTour

Uma API REST para aplicativo de turismo da cidade, permitindo que os usuários descubram, avaliem e gerenciem pontos turísticos.

## 🚀 Deploy

A API está disponível em: https://outside-wilona-jgmotta2-02732f12.koyeb.app/

## 📋 Funcionalidades

### 🔐 Autenticação de Usuário
- **POST** `/auth/signup` - Cria uma nova conta de usuário
- **POST** `/auth/signin` - Autentica um usuário e retorna um token JWT

### 👤 Gerenciamento de Usuário
- **GET** `/ws/users/me` - Retorna o perfil do usuário autenticado
- **PUT** `/ws/users/me` - Atualiza o perfil do usuário autenticado
- **PUT** `/ws/users/me/password` - Atualiza a senha do usuário autenticado

### 📍 Gerenciamento de Pontos Turísticos
- **GET** `/ws/points` - Retorna uma lista paginada de todos os pontos turísticos
- **GET** `/ws/points/{id}` - Busca um ponto turístico por ID
- **GET** `/ws/points/search` - Busca por pontos turísticos com base em um termo de pesquisa
- **POST** `/ws/points` - Adiciona um novo ponto turístico
- **PUT** `/ws/points/{id}` - Atualiza um ponto turístico existente
- **DELETE** `/ws/points/{id}` - Deleta um ponto turístico

### ⭐ Avaliações de Pontos Turísticos
- **POST** `/ws/points/{placeId}/reviews` - Adiciona uma avaliação a um ponto turístico
- **GET** `/ws/points/{placeId}/reviews` - Obtém todas as avaliações de um ponto turístico

### 🏆 Rankings e Listas
- **GET** `/ws/points/top-rated` - Retorna os pontos turísticos com as melhores avaliações
- **GET** `/ws/points/most-visited` - Retorna os pontos turísticos mais visitados

## 🛠️ Tecnologias

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- JPA/Hibernate

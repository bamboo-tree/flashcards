# F L @ $ # ( @ R D &

## What is it?

This is a simple flashcards REST API written in Sprinboot. It allows to CRUD users, decks and cards.

**The main features are:**

* Layered service architecture: entity, repository, service, controller
* Data isolation using DTOs and ModelMapper
* OpenAPI documentation with swagger
* Data validation with jakarta
* Relations between entities
* JPA repositories
* Services with bussines logic
* REST controllers
* Global exceptions handler

## REST API endpoints:

**Cards:**
```
   GET: /api/cards                  - find all cards
   GET: /api/cards/id               - find card by id
  POST: /api/cards                  - create new card
   PUT: /api/cards/id               - update card by id
DELETE: /api/cards/id               - delete card by id
```

**Decks:**
```
   GET: /api/decks                  - find all decks
   GET: /api/decks/id               - find deck by id
  POST: /api/decks                  - create new deck
   PUT: /api/decks/id               - update deck by id
DELETE: /api/decks/id               - delete deck by id
   PUT: /api/decks/deckId/cardId    - add card to deck by id
DELETE: /api/decks/deckId/cardId    - delete card from deck by id
```

**Users:**
```
   GET: /api/user                  - find all users
   GET: /api/user/id               - find user by id
  POST: /api/user                  - create new user
   PUT: /api/user/id               - update user by id
DELETE: /api/user/id               - delete user by id
   PUT: /api/user/userId/deckId    - add deck to user by id
DELETE: /api/user/userId/deckId    - delete deck from user by id
```

## Maintenance

In order to make life easier some mandatory features were included:

* Unit tests with Mockito
* Swagger documentation
* Slf4j logging
* Method documentation using standard annotations

`[INFO] Tests run: 83, Failures: 0, Errors: 0, Skipped: 0`

## How to run it?

I assume you have `mvn` installed, if not install it.

**Clone this repository**

`git clone git@github.com:bamboo-tree/flashcards.git`

**Go to `backend` directory**

`cd ./flashcards/backend`

**Start springboot application**

`mvn spring-boot:run`

**Run tests**

`mvn test`

## Why?

Because I wanted to make some small springboot project. I think it's pretty cool and overengineered
considering its functionalities. It's funny that writting tests was more exhausting than the rest of
this application.

*No, it was not AI generated.*

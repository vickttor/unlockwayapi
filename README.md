# Unlockway API

RESTFull API built with Spring Boot and Maven.
This repository keeps the source code of the unlockway's api.

### Running with Docker

```shell
docker build -t unlockway_api_image .
```

- `docker build`: build the image from the DockerFile in the root directory
- `-t unlockway_api_image`: We're applying a tag name to the image. This helps us to not specify the image ID every time, instead, we use the given name to the image.
- `.`: The dot at the end of the command specify that the DockerFile is in the actual directory.

```shell
docker run -d -p 8080:8080 --name unlockway_api unlockway_api_image
```

- `docker run`: Create, execute and logs the container
- `-d`: Detached from the console to not block the terminal
- `-p 8080:8080`: Port mapping. We're saying to the container that our localhost:8080 must match with the container's 8080 port.
- `--name unlockway_api`: We're naming the container
- `unlockway_api_image`: We're saying the docker use our image created previously.

---

# Endpoints

#### api/users/authenticate **(POST)**

PAYLOAD:

```json
{
	"email": "teste@teste.com"
	"password": "123456@!"
}
```

RESPONSE:

```json
{
  "status": 200,
  "data": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "fistName": "John",
    "lastName": "Doe",
    "weight": 75.34,
    "height": 1.8,
    "goal": "getMass, mantainHealth",
    "bioType": "hectomorfo",
    "email": "teste@teste.com",
    "password": "123456@!",
    "token": "tahsk192kashj09123781287zb12893au1293791@628109"
  }
}
```

#### api/users/register **(POST)**

PAYLOAD:

```json
{
  "fistName": "John",
  "lastName": "Doe",
  "weight": 75.34,
  "height": 1.8,
  "goal": "getMass, mantainHealth",
  "bioType": "hectomorfo",
  "email": "teste@teste.com",
  "password": "123456@"
}
```

RESPONSE:

```json
{
  "status": 201,
  "data": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "fistName": "John",
    "lastName": "Doe",
    "weight": 75.34,
    "height": 1.8,
    "goal": "getMass, mantainHealth",
    "bioType": "hectomorfo",
    "email": "teste@teste.com",
    "password": "123456@!",
    "token": "tahsk192kashj09123781287zb12893au1293791@628109"
  }
}
```

#### api/notifications?userId={userID} **(GET)**

PARAMS:

- userId

RESPONSE:

```json
{
  "status": 200,
  "data": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "title": "Hora do Almoço",
    "description": "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
    "read": true
  }
}
```

## Home

#### api/analysis?userId={userId} **(GET)**

PARAMS:

- userId

RESPONSE:

```json
{
  "status": 200,
  "data": {
    "meals": 0,
    "routines": 0,
    "notifications": 32,
    "weekCalories": [2805, 1032, 2100, 165, 4999, null, null]
  }
}
```

#### api/routineInUsage?userId={userId} **(GET)**

PARAMS:

- userId

RESPONSE:

```json
{
  "status": 200,
  "data": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "meals": [
      {
        "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "name": "Arroz com abóbora",
        "notifyAt": "2023-09-28T02:30:17.021Z",
        "description": "Lorem Ipsum is simply dummy text of the printing and",
        "category": 1,
        "createdAt": "2023-09-28T02:30:17.021Z",
        "updatedAt": "2023-09-28T02:30:28.448Z"
      }
    ],
    "inUsage": true,
    "weekRepetitions": {
      "sunday": true,
      "monday": true,
      "tuesday": true,
      "wednesday": true,
      "thursday": true,
      "friday": false,
      "saturday": false
    },
    "totalCaloriesInTheDay": 2890,
    "createdAt": "2023-09-28T02:30:17.021Z",
    "updatedAt": "2023-09-28T02:30:28.448Z"
  }
}
```

## Food

#### api/ingredients **(GET)**

RESPONSE:

```json
{
  "status": 200,
  "data": [
    {
      "idFood": "3ef7f51a-79b4-4d12-b7f0-c6e9d848cc28",
      "name": "Canela",
      "description": "A canela é uma especiaria amplamente utilizada em todo o mundo, conhecida por seu sabor doce e aroma reconhecível. Ela é obtida da casca interna da árvore da canela, que pertence ao gênero Cinnamomum. Existem várias espécies de árvores de canela, mas as duas variedades mais conhecidas são a canela-do-ceilão (Cinnamomum verum ou Cinnamomum zeylanicum) e a canela-cássia (Cinnamomum cassia)",
      "measure": 2,
      "calories": 150.5,
      "proteins": 10.2,
      "water": 75.0,
      "minerals": 5.3,
      "vitamins": ["A", "E", "C"],
      "fats": 8.7
    }
  ]
}
```

## Meals

#### api/meals?userId={userId} **(GET)**

PARAMS:

- userId

RESPONSE:

```json
{
  "status": 200,
  "data": [
    {
      "idMeal": "550e8400-e29b-41d4-a716-446655440000",
      "photo": "https://azure.storage/file19283912319123.png",
      "name": "Delicious Meal",
      "description": "A meal that is both healthy and delicious.",
      "category": 1,
      "preparationMethod": "Bake for 30 minutes at 350 degrees.",
      "ingredients": [
        {
          "idFood": "550e8400-e29b-41d4-a716-446655440000",
          "name": "Chicken",
          "measure": 1,
          "amount": 1
        }
      ],
      "totalCalories": 500.0,
      "isEnable": true,
      "createdAt": "2023-10-01T00:00:00.000Z",
      "updatedAt": "2023-10-01T00:00:00.000Z"
    }
  ]
}
```

#### api/meals **(POST)**

PAYLOAD:

```json
{
  "status": 200,
  "data": {
    "name": "Sample Name",
    "photo": "https://azure.storage/file19283912319123.png",
    "description": "Sample Description",
    "category": "Sample Category",
    "preparationMethod": "Sample Preparation Method",
    "ingredients": [
      {
        "idFood": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "amount": 200.0
      }
    ]
  }
}
```

RESPONSE:

```json
{
  "status": 201,
  "data": {
    "idFood": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "name": "Sample Name",
    "photo": "https://azure.storage/file19283912319123.png",
    "description": "Sample Description",
    "category": 1,
    "preparationMethod": "Sample Preparation Method",
    "ingredients": [
      {
        "idFood": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "measure": 1,
        "amount": 200.0
      }
    ],
    "totalCalories": 807.46,
    "isEnable": true,
    "createdAt": "2023-10-01T00:00:00.000Z",
    "updatedAt": "2023-10-01T00:00:00.000Z"
  }
}
```

#### api/meals?id={idMeal} **(PUT)**

PAYLOAD:

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "name": "Sample Name",
  "photo": "https://azure.storage/file19283912319123.png",
  "description": "Sample Description",
  "category": "Sample Category",
  "preparationMethod": "Sample Preparation Method",
  "ingredients": [
    {
      "idFood": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "amount": 200.0
    }
  ]
}
```

RESPONSE:

```json
{
  "status": 200,
  "message": "Meal Updated"
}
```

#### api/meals/filter?category=1 **(GET)**

PARAMS:

- category: Enum

RESPONSE:

```json
{
  "status": 200,
  "data": [
    {
      "idMeal": "550e8400-e29b-41d4-a716-446655440000",
      "photo": "https://azure.storage/file19283912319123.png",
      "name": "Delicious Meal",
      "description": "A meal that is both healthy and delicious.",
      "category": 1,
      "preparationMethod": "Bake for 30 minutes at 350 degrees.",
      "ingredients": [
        {
          "idFood": "550e8400-e29b-41d4-a716-446655440000",
          "name": "Chicken",
          "measure": 1,
          "amount": 1
        }
      ],
      "totalCalories": 500.0,
      "isEnable": true,
      "createdAt": "2023-10-01T00:00:00.000Z",
      "updatedAt": "2023-10-01T00:00:00.000Z"
    }
  ]
}
```

## Routines

#### api/routines?userId={userId} **(GET)**

PARAMS:

- userId

RESPONSE:

```json
{
  "status": 200,
  "data": [
    {
      "idRoutine": "550e8400-e29b-41d4-a716-446655440000",
      "name": "My Weekly Routine",
      "inUsage": true,
      "meals": [
        {
          "idMeal": "550e8400-e29b-41d4-a716-446655440000",
          "notifyAt": "2023-10-01T00:00:00.000Z",
          "photo": "https://azure.storage/file19283912319123.png",
          "name": "Delicious Meal",
          "description": "A meal that is both healthy and delicious.",
          "category": 1,
          "totalCalories": 500.0
        }
      ],
      "weekRepetitions": [true, true, true, true, true, false, false],
      "totalCaloriesInTheDay": 500.0,
      "isEnable": true,
      "createdAt": "2023-10-01T00:00:00.000Z",
      "updatedAt": "2023-10-01T00:00:00.000Z"
    }
  ]
}
```

#### api/routines **(POST)**

PAYLOAD:

```json
{
  "name": "My Weekly Routine",
  "inUsage": false,
  "meals": [
    {
      "idMeal": "550e8400-e29b-41d4-a716-446655440000",
      "notifyAt": "2023-10-01T00:00:00.000Z"
    }
  ],
  "weekRepetitions": [true, true, true, true, true, false, false]
}
```

RESPONSE:

```json
{
  "status": 201,
  "data": {
    "idRoutine": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Delicious Meal",
    "inUsage": false,
    "meals": [
      {
        "idMeal": "550e8400-e29b-41d4-a716-446655440000",
        "notifyAt": "2023-10-01T00:00:00.000Z",
        "ingested": false,
        "photo": "https://azure.storage/file19283912319123.png",
        "name": "Delicious Meal",
        "description": "A meal that is both healthy and delicious.",
        "category": 1,
        "totalCalories": 500.0
      }
    ],
    "weekRepetitions": [true, true, true, true, true, false, false],
    "totalCaloriesInTheDay": 500.0,
    "isEnable": true,
    "createdAt": "2023-10-01T00:00:00.000Z",
    "updatedAt": "2023-10-01T00:00:00.000Z"
  }
}
```

#### api/routines?id={idRoutine} **(PUT)**

PAYLOAD:

```json
{
  "idRoutine": "550e8400-e29b-41d4-a716-446655440000",
  "name": "My Weekly Routine",
  "inUsage": false,
  "meals": [
    {
      "idMeal": "550e8400-e29b-41d4-a716-446655440000",
      "notifyAt": "2023-10-01T00:00:00.000Z"
    }
  ],
  "weekRepetitions": [true, true, true, true, true, false, false]
}
```

RESPONSE:

```json
{
  "status": 200,
  "message": "Routine Updated"
}
```

#### api/routines/filter?userId={userId} **(GET)**

PARAMS:

- days

RESPONSE:

```json
{
  "status": 200,
  "data": [
    {
      "idRoutine": "550e8400-e29b-41d4-a716-446655440000",
      "name": "My Weekly Routine",
      "inUsage": true,
      "meals": [
        {
          "idMeal": "550e8400-e29b-41d4-a716-446655440000",
          "notifyAt": "2023-10-01T00:00:00.000Z",
          "ingested": true,
          "photo": "https://azure.storage/file19283912319123.png",
          "name": "Delicious Meal",
          "description": "A meal that is both healthy and delicious.",
          "category": 1,
          "totalCalories": 500.0
        }
      ],
      "weekRepetitions": [true, true, true, true, true, false, false],
      "totalCaloriesInTheDay": 500.0,
      "isEnable": true,
      "createdAt": "2023-10-01T00:00:00.000Z",
      "updatedAt": "2023-10-01T00:00:00.000Z"
    }
  ]
}
```

## History

#### api/history?userId={userID} **(GET)**

RESPONSE:

```json
{
  "status": 200,
  "data": [
    {
      "idHistory": "880e8400-e29b-41d4-a716-446655440000",
      "idRoutine": "550e8400-e29b-41d4-a716-446655440000",
      "name": "My Weekly Routine",
      "date": "2023-10-01T00:00:00.000Z",
      "totalCaloriesInTheDay": 830.0,
      "meals": [
        {
          "idMeal": "550e8400-e29b-41d4-a716-446655440000",
          "notifyAt": "2023-10-01T00:00:00.000Z",
          "ingested": true,
          "photo": "https://azure.storage/file19283912319123.png",
          "name": "Delicious Meal",
          "description": "A meal that is both healthy and delicious.",
          "category": 1,
          "totalCalories": 500.0
        }
      ],
      "weekRepetitions": [true, true, true, true, true, false, false]
    }
  ]
}
```

#### api/history/filter?userId={userId} **(GET)**

RESPONSE:

```json
{
  "status": 200,
  "data": [
    {
      "idHistory": "880e8400-e29b-41d4-a716-446655440000",
      "idRoutine": "550e8400-e29b-41d4-a716-446655440000",
      "name": "My Weekly Routine",
      "date": "2023-10-01T00:00:00.000Z",
      "totalCaloriesInTheDay": 830.0,
      "meals": [
        {
          "idMeal": "550e8400-e29b-41d4-a716-446655440000",
          "notifyAt": "2023-10-01T00:00:00.000Z",
          "ingested": true,
          "photo": "https://azure.storage/file19283912319123.png",
          "name": "Delicious Meal",
          "description": "A meal that is both healthy and delicious.",
          "category": 1,
          "totalCalories": 500.0
        }
      ],
      "weekRepetitions": [true, true, true, true, true, false, false]
    }
  ]
}
```

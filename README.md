# API documentation

## GET /health

Description: returns a confirmation that the server does work.

## POST /signin

Parameters:
- **username**: username (obviously). Cannot be omitted.
- **password**: password (obviously). Cannot be omitted.

Response: returns 200 if username and password are correct, and Authorization header with token. Token is valid for 10 days.

**Request example**
```
POST /signin HTTP/1.1

{
  "username": "admin",
  "password": "admin"
}
```

**Response example**
```
HTTP/1.1 200 OK
Authorization: long_token_should_be_here
OtherHeaders: values...
```

## GET /helloworld

Description: test endpoint

## GET /helloworld_protected

Description: test secured endpoint. Returns response only with valid token. Authorization should look like: Word "Bearer", than whitespace and token (See example).
Response: returns 200 and {"hello": "world"}. If token is invalid - 403.

**Request example**
```
GET /helloworld_protected HTTP/1.1
Authorization: Bearer long_token_should_be_here
```

**Response example**
```
HTTP/1.1 200 OK
SomeHeaders: values...

{
  "hello": "world"
}
```

## POST /users

Description: user registration

Parameters:
- **username**: mandatory, must be unique
- **password**: mandatory, must be unique
- **email**: must be unique
- **firstName**: optional
- **lastName**: optional
- **birthday**: optional (as epoch timestamp)

Response: returns 201 if everything is fine and user object. Returns 400 if something wrong, expleneation in "Message" header.

Username rules:
1. Only contains alphanumeric characters, underscore and dot.
2. Underscore and dot can't be at the end or start of a username (e.g _username / username_ / .username / username.).
3. Underscore and dot can't be next to each other (e.g user_.name).
4. Underscore or dot can't be used multiple times in a row (e.g user__name / user..name).
5. Number of characters must be between 8 to 20.

**Request example**
```
POST /users HTTP/1.1

{
  "username": "newUser",
  "password": "new password",
  "firstName": "NAME"
}
```

**Response example**
```
HTTP/1.1 201 CREATED

{
    "userId": "some-unique-id",
    "username": "newUser",
    "firstName": "NAME",
    "lastName": null,
    "birthday": null,
    "universityId": null
}
```

## HEAD /usernames/{username}

Parameter:
- **username**: mandatory.

Description: check if username is availabale.
Returns: 204 (NO_CONTENT) if available, otherwise 409 (CONFLICT)

**Request example**
```
HEAD /usernames/admin HTTP/1.1
```

**Response example**
```
HTTP/1.1 409 CONFLICT
```

## HEAD /emails/{email}

Parameter:
- **email**: mandatory.

Description: check if email is availabale.
Returns: 204 (NO_CONTENT) if available, otherwise 409 (CONFLICT)

**Request example**
```
HEAD /emails/email@mail.com HTTP/1.1
```

**Response example**
```
HTTP/1.1 240 NO_CONTENT
```

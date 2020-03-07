# API documentation

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

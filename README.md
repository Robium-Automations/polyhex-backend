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
Content-Type: application/json

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
Content-Type: application/json

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

Description: check if email is available.
Returns: 204 (NO_CONTENT) if available, otherwise 409 (CONFLICT)

**Request example**
```
HEAD /emails/email@mail.com HTTP/1.1
```

**Response example**
```
HTTP/1.1 204 NO_CONTENT
```

## POST /faculties

Description: create new faculty.

Requires: 
- moderator role
- token

Parameter: 
- **facultyName**: mandatory.

Returns:
- 200 if everything is fine
- 403 if user is not moderator
- 400 if something else is wrong, with message error

**Request example**
```
POST /faculties HTTP/1.1
Content-Type: application/json
Authorization: Bearer long_token_should_be_here

{
    "facultyName": "New Faculty"
}
```

**Response example**
```
HTTP/1.1 200 OK

{
    "facultyId": "uuid",
    "facultyName": "New Faculty",
    "universityId": "uuid" (user's universityId)
}
```

## GET /universities/{universityId}/faculties

Description: returns faculty list of the given university in alphabetical order

Requires:
- token

Returns:
- 200 and list of faculties

Parameters: 
- **universityId**: mandatory.
- **offset**: optional, default=0
- **limit**: optional, default=10

**Request example**
```
GET /universities/university_uuid/faculties?offet=5&limit=10 HTTP/1.1
Authorization: Bearer long_token_should_be_here
```

**Response example**
```
HTTP/1.1 200 OK
offset: 5
limit: 3

[
    {
        "facultyId": "uuid1",
         "facultyName": "faculty name 1",
         "universityId": "university_uuid"
    },
    {
        "facultyId": "uuid2",
         "facultyName": "faculty name 2",
         "universityId": "university_uuid"
    },
    {
        "facultyId": "uuid3",
         "facultyName": "faculty name 3",
         "universityId": "university_uuid"
    }
]
```



## GET /faculties/{facultyId}

Description: returns faculty

Requires:
- token

Returns:
- 200 and faculties
- 204 if not found

Parameters: 
- **facultyId**: mandatory

**Request example**
```
GET /faculties/faculty_uuid HTTP/1.1
Authorization: Bearer long_token_should_be_here
```

**Response example**
```
HTTP/1.1 200 OK

{
    "facultyId": uuid1",
    "facultyName": "faculty name",
    "universityId": "university_uuid"
}
```

## POST /groups

Description: create new group

Requires: 
- moderator role
- token

Returns:
- 200 if everything is fine and group object
- 403 if not moderator

Parameters: 
- **groupName**: mandatory, string
- **subjectId**: mandatory, subject uuid
- **semesterId**: mandatory, semester uuid

**Request example**
```
POST /groups HTTP/1.1
Content-Type: application/json
Authorization: Bearer long_token_should_be_here

{
    "groupName": "New Faculty",
    "subjectId": "New Faculty",
    "semesterId": "New Faculty"
}
```

**Response example**
```
HTTP/1.1 200 OK

{
    "facultyId": uuid,
    "facultyName": "New Faculty",
    "universityId": uuid (user's universityId)
}
```

## PUT /groups/{groupId}

Description: manage group - join or leave a group, moderators can also remove other users from the group

Requires: 
- token

Returns:
- 200 if everything is fine and group object
- 403 if not moderator(if removing other users)
- 400 if not enough info

Parameters:
- **action**: mandatory, one of the following - join, leave, remove
- **userId**: mandatory if action=remove, otherwise not

**Request example**
```
PUT /groups/group_id HTTP/1.1
Content-Type: application/json
Authorization: Bearer long_token_should_be_here

{
    "action": "join"
}
```

**Response example**
```
HTTP/1.1 200 OK
```

## POST /semesters

Description: create new semester

Requires: 
- moderator role
- token

Returns:
- 200 if everything is fine and semester object
- 403 if not moderator
- 400 if not enough info

Parameters: 
- **semesterName**: mandatory, string
- **semesterDescription**: optional, string
- **startDate**: optional, epoch timestamp in milliseconds. For example 22.03.1997 17:05 GMT = 1584896700000
- **endDate**: optional, epoch timestamp in milliseconds. For example 22.03.1997 17:05 GMT = 1584896700000

**Request example**
```
POST /semesters HTTP/1.1
Content-Type: application/json
Authorization: Bearer long_token_should_be_here

{
    "semesterName": "WS 2019/2020",
    "semesterDescription": "Here should be a description."
}
```

**Response example**
```
HTTP/1.1 200 OK

{
    "semesterId": "semester_id",
    "semesterName": "WS 2019/2020",
    "semesterDescription": "Here should be a description.",
    "startDate": null
    "endDate": null
    "universityId": "university_id"
}
```

## GET /universities/{universityId}/semesters

Description: get semesters

Requires: 
- token

Returns:
- 200 and list of semester objects, of emptyList
- 200 and empty list if universityId is incorrect

Parameters: 
- **universityId**: mandatory.
- **offset**: optional, default=0
- **limit**: optional, default=10

**Request example**
```
GET /universities/university_uuid/semesters HTTP/1.1
Authorization: Bearer long_token_should_be_here
```

**Response example**
```
HTTP/1.1 200 OK
offset: 0
limit: 10

[
    {
        "semesterId": "semester_id1",
        "semesterName": "WS 2019/2020",
        "semesterDescription": "Here should be a description.",
        "startDate": null
        "endDate": null
        "universityId": "university_id"
    },
    {
        "semesterId": "semester_id2",
        "semesterName": "SS 2019",
        "semesterDescription": "Another description.",
        "startDate": null
        "endDate": null
        "universityId": "university_id"
    },
    {
        "semesterId": "semester_id3",
        "semesterName": "SS 2020",
        "semesterDescription": null,
        "startDate": null
        "endDate": null
        "universityId": "university_id"
    },    
]
```

## PUT /semesters/{semesterId}

Description: update semester info. There should be sent the whole object (without id), if some parameters are not set, they become null.

Requires: 
- moderator role
- token

Returns:
- 200 if everything is fine and semester object
- 403 if not moderator
- 400 if shit occurs

Parameters: 
- **semesterName**: mandatory, string
- **semesterDescription**: optional, string
- **startDate**: optional, epoch timestamp in milliseconds. For example 22.03.1997 17:05 GMT = 1584896700000
- **endDate**: optional, epoch timestamp in milliseconds. For example 22.03.1997 17:05 GMT = 1584896700000

**Request example**
```
PUT /semesters/semester_id HTTP/1.1
Content-Type: application/json
Authorization: Bearer long_token_should_be_here

{
    "semesterName": "WS 2020/2021",
    "semesterDescription": "Awesome description."
}
```

**Response example**
```
HTTP/1.1 200 OK

{
    "semesterId": "semester_id",
    "semesterName": "WS 2020/2021",
    "semesterDescription": "Awesome description."
    "startDate": null
    "endDate": null
    "universityId": "university_id"
}
```


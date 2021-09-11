# enjoy-hq-challenge
Simple rest api to authenticate users and store/query documents. [Original challenege](doc/enjoyhq_clojure_backend_coding_challenge)

Some details reguarding the api.
* Date fields are of these two formats 
	* `2023-12-10T15:52:00Z`
	* `2023-12-10`
* In POST requests always include the headers
	* `Content-Type: application/json`
## Contents
* [Usage](#usage)
* [Authentication](#authentication)
* [Testing Data](#testing-data)
* [Possible improvements](#possible-improvements)

## Usage
Run mysql docker container. It should be up on `//localhost:3307/enjoy_hq_dev`

> cd docker  
> docker-compose up

Run either the prebuilt UBERJAR or by LEININGEN. Server will be up on port `3000`
> java -jar ./releases/enjoy-hq-challenge-0.1.0-SNAPSHOT-standalone.jar     
> lein run

You can checkout the swagger documentation on  
http://localhost:3000/api-docs/index.html

Or import the postman collection from [doc/enjoy-hq-challenge.postman_collection.json](doc/enjoy-hq-challenge.postman_collection.json)

## Authentication
The authentication is JWT token based authentication with expiritaion duration (15min).  

Create a user
```shell
curl --location --request POST 'http://localhost:3000/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username" : "tommy",
    "password" : "hanks"
}'
```

Authenticate with the user
```shell
curl --location --request POST 'http://localhost:3000/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username" : "tommy",
    "password" : "hanks"
}'
# => { "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbW15IiwiZXhwIjoxNjMxMzExNDM1fQ._a30flhEGIXgr8i4CbC_VP9aessBFZIgaRmP61gP3rs" }
```

Now you can call the protected endpoints by sending the recieved token in header as 
> Authorization=Token eyJhbGciO....61gP3rs
```shell
curl --location --request GET 'http://localhost:3000/_index/2' \
--header 'Authorization: Token eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbW15IiwiZXhwIjoxNjMxMzExNDM1fQ._a30flhEGIXgr8i4CbC_VP9aessBFZIgaRmP61gP3rs' \
--data-raw ''
```

## Testing Data
There is already some testing data in the db consisting of some documents for the following user.  
```json
{
	"username" : "fruitman",
	"password" : "fruitman"
}
```
Login in with the user and start testing queries.
```shell
curl --location --request POST 'http://localhost:3000/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username" : "fruitman",
    "password" : "fruitman"
}'
```

## Possible improvements
* Using lifecycle and dependency management library like Component or Integrant.
* Defining protocols for the database access layer for further decoupling.
* Feature/handler tests
* Bcrypt hashing for storing passwords.

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

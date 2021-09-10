# EnjoyHQ Clojure Developer Coding Challenge

Thank you for your interest in EnjoyHQ. This coding challenge consists of two
parts. We don't like puzzles and tricky questions, so we based all tasks on
real-life problems we had to solve when building EnjoyHQ.

## General rules

The language of choice, of course, is Clojure. You can use any supporting software, as
long as you provide clear instructions on how to check your examples. If it's helpful, here's what we use internally

- Leiningen for Clojure project management (dependencies, uberjar building)
- we have open sourced basic building blocks for our services, you can find them here: https://github.com/nomnom-insights
- we use Docker Compose for orchestrating backing services (Postgres, Elasticsearch etc)


You're free to use any tools of course.

Once your solution is complete, please make sure to:

- ensure that your code works with case-sensitive file systems
- document how to get everything running, bonus points if you're using Docker
- zip the whole project directory, including `.git`, we're curious about your
  commit history
- email it to us

If you have doubts or something is not clear - do not hesitate to ask!

## Part 1: Search engine API

In this part, the goal is to build a simple search engine, which uses JSON as
message serialization format and HTTP as a transport protocol. How users will
authenticate is up to you. The only requirement is that two users should not
be able to request or find documents they do not own

The documents have to at least support the following schema:


```

id: string
content: text
title: text
created_at: timestamp
updated_at: timestamp
```

You can add additional fields if required by your implementation.

### Authentication

Unless noted all endpoints require authentication.

### Endpoints


The API should support the following endpoints. Means of authentication have been omitted.


### `GET /health-check`

No authentication required.

Example:

```shell
curl localhost:3000/health-check
# => {"message" : "up"}
```

### `POST /_index`

Add a document to index, documents have to comply with the mentioned schema

Returns an object with status field and id of the document. Note: documents can
be upserted, meaning that if a request with the same id is made the content will
be updated.

Example:

```shell
curl -X POST  localhost:3000/_index \
  -d '{ "content" : "I really like bananas", "created_at" : "2021-03-21T03:23:32Z"}'

# => { "status" : "ack", "id" : "<some id>" }
```

### `POST /_search`

Searches through the index. Query payload supports multiple filters at a time and optional ordering parameter:

```edn
{
 :filters [;; for text fields
           {:field <field name>
            :filter_type <exact_match | starts_with | substring>
            :match <text to match>}
           ;; for timestamp fields
           {:field <field name>
            :from <start date>
            :to <end date (optional)>}
           ]

 :order_by <created_at | updated_at >
 }

```



Returns an object with total field indicating number of matched documents and
hits - an array of matching documents.

Example:

```shell
curl -X POST  localhost:3000/_search \
    -d '{ "filters" : [ { "field" : "content", "match" : "banana", "filter_type": "substring" }], "order_by": "updated_at"}'
# =>
{
  "total" : 1 , "hits" : [
    { "content" : "I really like bananas", "id" : "abc", "created_at" : "2018-05-21T12:33.45Z", "updated_at" : "2021-03-04T12:32:45Z" }
  ]
}



curl -X POST  localhost:3000/_search \
    -d '{ "filters" : [ { "field" :"created_at", "start": "2010-01-01"} }], "order_by": "updated_at"}'
# =>
{
  "total" : 1 , "hits" : [
    { "content" : "I really like bananas", "id" : "abc", "created_at" : "2018-05-21T12:33.45Z", "updated_at" : "2021-03-04T12:32:45Z" }
  ]
}


curl -X POST  localhost:3000/_search \
    -d '{ "filters" : [ { "field" :"created_at", "start": "2020-01-01"}, {"field": "content", "filter_type" : "substring", "match" : "ban" }], "order_by": "updated_at"}'

# =>
{
  "total" : 1 , "hits" : [
    { "content" : "I really like bananas", "id" : "abc", "created_at" : "2018-05-21T12:33.45Z", "updated_at" : "2021-03-04T12:32:45Z" }
  ]
}

curl -X POST  localhost:3000/_search \
    -d '{ "filters" : [ { "field" :"created_at", "start": "2020-01-01"}, {"field": "content", "filter_type" : "exact_match", "match" : "airplane" }], "order_by": "updated_at"}'


# =>

{
  "total" : 0, "hits" : []
}

```

### `GET /_search/:id`

Fetches document with given ID.
Example:

```shell
curl localhost:3000/_search/abc
# => { "content" : "I really like bananas",
       "id" : "abc",
       "created_at" : "2018-05-21T12:33Z" }
```

### `DELETE /_search/:id`

Deletes a document from index.

```shelll
curl -X DELETE localhost:3000/_search/abc
# => { "status" : "ack" }
```

## Part 2: Persistence

Add persistence layer to the API so that the data can survive
search service restarts.
Choice of storage engine is up to you.

---

Good luck!

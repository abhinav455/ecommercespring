each entity is a table in jpa,
each instance is a row in jpa and we have repository that
has all instances/objects of that entity

jpa entities are objects that represent persistent data in rdbms
jpa relies on java bean 

default constructor is needed



lazy vs eager fetch, lazy fetch of address and only user fetch
eager address and user all fetched

on startup we dont store entities, on findAll()
only it queries db and fetches all users and puts in memory and returns data 
and removes the users




---------------------------------------


Q)repository jpa pattern update, it pulls rows and creates entites from it and stores in memory. what if there all million rows? wont the memry get filled and program crash?

good question. NO — not always.

it depends on HOW you query.

1) normal .save() or .findById() → returns ONE row

that’s fine. no memory problem.

2) .findAll() → THIS is the dangerous one

If you do:

List<User> allUsers = userRepository.findAll();


and your user table has 1 million rows → yes, this will try to load 1 million objects into memory → bad → can cause OOM.

So don’t do findAll() blindly in production.

What is the correct way?

Use pagination:

PageRequest pageRequest = PageRequest.of(0, 50);

Page<User> page = userRepository.findAll(pageRequest);


You get only 50 rows.


Spring Data JPA does NOT keep ALL objects you fetched in memory forever.
After the repository returns, and transaction is closed, persistence context flushes.

But still during query execution, if you ask for 1M rows, RAM will explode.

Rule:
large datasets → always use pagination or streaming

Example streaming:
@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
Stream<User> streamAll();


Then you iterate stream, memory stays small.

----------------------------------























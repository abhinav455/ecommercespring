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


controller always uses UserRequest and UserResponse, never User directly
thus no model in controller

inject
1)make final and constructor to get the object
2) or @Autowired spring
3) lombok @RequiredArgsConstructor


-----------------------------------

return users.stream()
.filter(...)
.findFirst()
.map(u -> { u.setName("x"); return true; })
.orElse(false);
flow:
case 1: element found in stream
findFirst() returns Optional<User> with a value

then .map(...) is executed → mapping returns Optional<Boolean> with value true

then .orElse(false) → since Optional already has value → returns that value (true)

result = true
orElse(false) is not executed, it just checks "do I have a value? yes → return it."

case 2: no element found in stream (empty)
findFirst() returns Optional.empty

map is not executed (map is skipped because Optional is empty)

then .orElse(false) → Optional has no value → so it returns false

result = false

You can rewrite mentally like:
java
Copy code
Optional<Boolean> opt = users.findFirst().map(...);
if(opt.hasValue) {
return opt.value;
} else {
return false;
}


--------------------------------------

 - can add @Transactional to whole service class or to a method inside it


ChatGPT said:

@Transactional is a Spring annotation that says:

run this method inside a database transaction

what does that mean?

A DB transaction is like doing multiple DB operations as one unit.

Example without transaction:

update A
update B
update C
--> if error in C → A and B are already committed. data becomes inconsistent.


With transaction:

BEGIN
update A
update B
update C
COMMIT


if C fails:

ROLLBACK → revert everything


so DB stays consistent.

what @Transactional does in Spring:

opens a transaction at method start

all DB calls inside run under same transaction

if method ends normally → commit

if exception occurs → rollback

Where to use it?

put on service layer:

@Service
public class UserService {
@Transactional
public void updateUserAddress(...) {
userRepo.save(...)
addressRepo.save(...)
// both are atomic now
}
}

-------------------------------


BigDecimal totalPrice = cartItems.stream()
  .map(CartItem::getPrice)
  .reduce(BigDecimal.ZERO, BigDecimal::add);





















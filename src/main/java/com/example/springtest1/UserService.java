package com.example.springtest1;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public List<User> fetchAllUsers(){
        return userList;
    }

    public Optional<User> fetchUser(Long id){
        return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public void addUser(User user){
        user.setId(nextId++);
        userList.add(user);
    }

    public boolean updateUser(Long id, User updatedUser){
        return userList.stream().filter(user -> user.getId().equals(id))
                .findFirst().map(existingUser-> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    return true;
                }).orElse(false);
    }


}








/*

orElse is on the Optional (the return of map) — not on findFirst.

breakdown:

Step by step:

findFirst() returns:

Optional<User>


Then you do:

.map(existingUser -> { ...; return true; })


map will convert the Optional<User> into:

Optional<Boolean>


So the type is now Optional<Boolean>.

Then:

.orElse(false)


is called on that Optional<Boolean>.

Simple mental model
stream.findFirst()     -> Optional<T>
.map(x -> do stuff)    -> Optional<R>
.orElse(default)       -> R


So final value is either:

result of your map block ( true )

or false if nothing was found
 */
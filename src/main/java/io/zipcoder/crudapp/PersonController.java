package io.zipcoder.crudapp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    PersonRepository repository;

    @Autowired
    public PersonController(PersonRepository repository){
        this.repository = repository;
    };

    @PostMapping
    ResponseEntity<Person> CreatePerson(@RequestBody Person p){
        return new ResponseEntity<>(repository.save(p), HttpStatus.CREATED );
    }

    @GetMapping("/{id}")
    ResponseEntity<Person> getPerson(@PathVariable int id){
        Optional<Person> p = repository.findById(id);
        return p.map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    List<Person> getPersonList(){
        List<Person> l = new ArrayList<>();
        for (Person p :repository.findAll())
            l.add(p);
        return l;
    }

    @PutMapping("/{id}")
    ResponseEntity<Person> updatePerson(@RequestBody Person p){
        Optional<Person> old = repository.findById(p.id);
        //Person old = repository.findById(p.id).get();
        repository.save(p);
        return new ResponseEntity<>(p, old.isPresent() ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePerson(@PathVariable int id){
        repository.findById(id).ifPresent( x -> repository.delete(x));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }
}

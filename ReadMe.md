# camelCase - by BertKoor

This is a simple demo project for [Apache Camel](https://camel.apache.org) 
used in a [Spring Boot](https://spring.io/projects/spring-boot) application. 

It's attached to a talk I did on this subject which is posted on [YouTube](https://youtu.be/2N_zC9X70F8).
I gave that talk earlier to colleagues of a big Dutch bank I work for, using the service my team had built as an example.
But since no one outside that bank has access to that project, I had to come up with another public example project.

The links to reference naterial are at the bottom of this article.

## The 'use case' of camelCase

The business case here is all about camels.
The real animals - smelly, moody, with one or two humps you would find in or near the desert.
Where it's hazardous to cross a desert with a car, it's slow but safe to do so using camels.

Imagine we all have camels instead of cars. 
Ofcourse then there are companies renting out camels, selling and buying them.
And imagine the government mandated that each camel carries a license plate (or have a RFID chip) 
for identification purposes.

The core problem this project is solving:

> What is a camel worth?

### Existing Components

Since Apache Camel is about Enterprise Integration Patterns, _connecting anything with everything_ ,
a couple of services and resources already exist:

 * The __CVS__ (Camel Valuation Service) is already developed. 
   It takes some depersonalized input values of a camel such as the weight, age, number of humps, 
   physical condition and customer rating to calculate an estimation of the camel's market value.
 * The __CR__ (Camel Registry) is a datastore which contains details like the weight, age and number of humps.
 * The __CHS__ (Camel Health Service) holds the medical history and periodical medical check results of any registered camel.
   Based on the health records it can return the PHS (physical historical state) of a camel, 
   expressed in a number from one (very bad) to ten (very good).
 * Camels that are rented out might get star ratings from customers, 
   expressing their customer happiness in using that particular camel.
   The average rating can be retrieved from the __CCRS__ (Camel Customer Rating Service)
   and is expressed in a number between one (bad) and five (good) in a precision of halve stars.

### Camel License ID's

For security and privacy reasons the CR, CHR and CCRS cannot be accessed directly with the camel's license ID.
Instead an __internal ID__ is used. There exists a top secret algorithm to convert the public license ID to the internal ID.
Experts from the government's IT department guarantee this algorithm cannot be reversed.

The public license ID of a camel has exactly six identifying characters.
It contains at least one digit and at least one letter.
It may be formatted for readability with hyphens, dots or spaces.

The private internal ID of a camel is just a number. 

So it is easy to tell whether it's a public license ID or private internal ID, these cannot get confused.

### Summary

 * To get the value of a camel, you need to call the CVS.
 * Data needed for the CVS call can be retrieved from the CR, CHS and CCRS.
 * The CR, CHS and CCRS need the private internal ID, 
   which can be constructed by a top secret algorithm from the public license ID.

## Technical Details

If you'd like to study how the project is constructed from the bottom up,
I'd advise to look at the [commit history](https://github.com/BertKoor/camelCase/commits/master).

## References

Here are all the links from the presentation :

 * https://camel.apache.org
   is the main website of Apache Camel
 * https://mvnrepository.com/artifact/org.apache.camel/camel-core 
   lists publication dates of camel-core.jar, showing the version history and pace of development.
 * https://camel.apache.org/components/latest/eips/enterprise-integration-patterns.html
   lists common Enterprise Integration Patterns and ways how to implement them with Apache Camel
 * https://camel.apache.org/components/latest/
   is the main source for documentation on components developed for Apache Camel
 * Getting Started:
   1. https://www.manning.com/books/camel-in-action-second-edition
      is the recommended book to read
   1. https://ordina-jtech.github.io/camel-workshop/index.html
      is the documentation page of the Camel workshop created by Ivo Woltring and Edwin Derksen.
      To do the workshop, clone repo https://github.com/Ordina-JTech/camel-workshop
      and check out a branch for each exersize.
   1. https://www.baeldung.com/apache-camel-spring-boot
      is another article explaining the setup of a simple project using Spring Boot and Camel
 * https://start.spring.io
   is the Spring Initializr that generates a project structure for you.
   I would suggest to add dependencies Spring Web, Apache Camel, and optionally Spring Boot Actuator.
   
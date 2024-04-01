# strategy-pattern-java

## Purpose

Java project to exemplify the implementation of the [GoF Strategy pattern](https://www.gofpattern.com/behavioral/patterns/strategy-pattern.php#google_vignette), which would prevent us from the infinite if-else blocks spread throughout our algorithm every time certain steps are specific to one or another case:

```
  ...
  if (case1) {
    do something specific to case2
  } else {
    do another thing for all other cases
  }
  ...
  if (case2) {
    do something specific to case2
  }
```

This practice leads to all special cases being highly sensitive to changes done on any other case, especially hurtful if such cases have legal implications or belong to different customers.

## Solution

This example simulates a Pharma service that has different flows depending 
on the seriousness of the drugs that are being purchased, all of them 
sharing some basic logic, but having some differences that should not
be mixed with each other.

### Important classes

- `PurchaseServiceStrategyFinder`. This is the class that selects the strategy;
  although the simplest, it's the core of the Strategy pattern. No other
  class in the algorithm should ever need to check which flow is running
  ever again.
  
  This is only an idea, you could implement the strategy selector based
  on Spring context's getBean("stratgyId") if you find a way to match
  strategy IDs and Spring bean names, or any other way, but don't allow
  creativity to get in the way of people reading your code.

- `PharmaPurchaseService`. This interface is the second in importance,
    the pattern wouldn't make sense if there wasn't a single interface
    the main program was dealing with, without knowing the specific
    underlying implementation based on request/system conditions.

- Strategy implementations.
  - `BasePurchaseService`. DRY principle means you should reuse code
    instead of copying/pasting it. This class contains the steps
    that are common to all of the different implementations of the
    strategy so they can be reused by composition.
  - `TemplatePurchaseService`. An additional behavioral design pattern
    that could commonly be implemented together with **Strategy Pattern**
    is the **Template Method**. This allows to prevent copying
    the same flow over and over again. In this example, only two
    classes extend this class in order to visualize the pros and cons
    of implementing the Template Method, which might include:
    - Template Method. Reuses but complicates the flows linearity
        and could branch it.
    - Replicating the flow. Keeps clarity at risk of desynchronizing
       the flows if further common steps have to be added to all of
       them and duplicates testing effort.
  - `UnprescriptedPurchaseService`. First Strategy implementation that
    represents the simplest flow.
  - `PrescriptedPurchaseService`. Second Strategy implementation in
    which the hypothetical developers realized the flow was going
    to be too similar to the one above, hence implemented Template
    Method by extending `TemplatePurchaseService`. Previous class
    wasn't altered with Template Method on purpose.
  - `DangerousPurchaseService`. Third Strategy implementation
    that reuses much of the flow from `PrescriptedPurchaseService`
    by also extending the Template Method pattern.

## TO-DO:
- Implement: `BasePurchaseService`.
- Implement unit tests
- Provide curl commands for manual tests in the README file
- Possibly showcases how project [Lombok](https://projectlombok.org/) stable and 
experimental could help to implement the Strategy pattern
even when dealing with third party libraries closed for 
extension even when we need most of their implementation
in our Strategy.
  - And also, Java records don't have Lombok builder to create
    copies of an object, useful when updating drug inventory.

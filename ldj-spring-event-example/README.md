# Spring Event example

This is an example application for LivingDocumentation Java that uses Spring application events.
It exists to demonstrate the example Spring renderer.

In the example, a fictional sandwich delivery service receives a single order for 6 sandwiches; in response, the app "updates the inventory", "dispatches sandwiches", and "generates" then "sends an invoice".
None of this actually happens - instead, the different Spring beans just log what they would have done had they been implemented.

There is no user interface.

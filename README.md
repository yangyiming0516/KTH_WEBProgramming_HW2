# KTH_WEBProgramming_HW2

Task we meet:
We did half of the tasks using bottom up method and the rest using top down method. This
web service fully meet the requirements.
There are 3 services, which are User, Booking and Query.
1. Service User is built for new users to sign up and old users to login.
2. Service Query has two services, service getroute could give all routes when user put
in the departure city and the destination. It allows 1 transfer. Service getdate, when
the user’s itinerary(flight, date) is determined, it would give back the price and
amount of available seats.
3. Service Booking can let user book the flight tickets, get the reference number and
booking number, allows the put in of credit card number.
Requirements we meet:
A. We have implement the service using top down fashion and the rest in the bottom-up
fashion.
B. We have tested on the Netbeans that every service works well independently.
C. The SOAP message we extended is shown below.
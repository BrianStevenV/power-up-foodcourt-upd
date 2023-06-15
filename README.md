This microservice manages all the services related to the restaurant, the information and service of restaurants, dishes, orders, data collection and actions of the user type employee with the restaurant.
An administrator can register a restaurant in the system, for this he must log in previously to obtain the access and consume this endpoint, only the administrator type users can create restaurants and assign it to the owner type users.
ENDPOINT: /foodCourt/restaurant/

example of the request: 

```JSON
{
  "name": "examplereadme",
  "direction": "exampleread",
  "phone": "3116805083",
  "urlLogotype": "example",
  "nit": "6642",
  "idOwner": "272945367"
}
```
will return in a success message :
```JSON
{
  "message": "Restaurant created successfully."
}
```

An owner, can enter dishes to his restaurant, for that he must register the name of his restaurant and the category of the dish, then he must enter the data of the dish:
ENDPOINT: /foodCourt/plate/{nameRestaurant}/categoryPlate}

```JSON
{
  "name": "PlateExample",
  "description": "example",
  "price": 500,
  "urlImage": "plateExample"
}
```
then it will return a success message.
If the restaurant is different, then it will return an error message, because a restaurant owner can only add dishes to his own restaurant:
```JSON
{
  "message":"The owner belongs other restaurant."
}

```
The owner is the only one who can update the information of the dishes of his restaurant, for this you need to be logged in and have the restaurant registered to his person to make the change:
ENDPOINT: /foodCourt/plate/
```JSON
{
  "id": 567,
  "price": 20,
  "description": "ploye"
}
```

If the owner tries to update information he will get the following message:

```JSON
{
  "message":"The owner belongs other restaurant."
}
```
An owner can change the status of the plate of his restaurant, if it is enabled or disabled but he can only change the status of plates of his own restaurant.
ENDPOINT: /foodCourt/plate/plate/status/{enabled}
```JSON
{
  TRUE
  "name": "pepo",
  "idRestaurant": 35
}
```

A client can interact with the application by means of queries and orders, firstly, it allows the client endpoints to query the restaurants and dishes that are available, these will be returned by means of a pagination where filters can be applied or not to it.
ENDPOINT: /foodCourt/pagination/restaurant

```JSON
{
  "sizePage": 5
  "name": "-> sorting profile"
}
```
and will return:

```JSON
{
  "message": "LIST OF RESTAURANTS FOUND IN THE SYSTEM."
}
```
ENDPOINT: /foodCourt/pagination/plate
```JSON
{
"restaurantName":"example",
"sizePage":6 
"sortBy": "-> sort profile",
"category" :"-> optional filter"
}
```

A customer when he has navigated through our application, can place an order, for this he registers the name of the restaurant and can place all the orders of the available dishes that are in that restaurant, if the customer orders a dish from another restaurant the application will not allow to register the order, if he enters incorrect values neither and if he enters a dish that is not available it will also return an exception message:
ENDPOINT: /foodCourt/order/
```JSON
{
  "nameRestaurant": "example",
  "plateOrderRequestDtoList": [
    {
      "idPlate": 33,
      "amountPlate": 3
    }
  ]
}
```


```JSON.
{
  "message":"Order created successfully."
}

```
If you violate any of the above validations, then you may get one of the following validations in response:
```JSON

{
  "message":"The plate is not available."
}
```

```JSON
{
  "message":"Plate belong other restaurant or Plate not found, you can only order food from the same restaurant."
}

```
A customer can only have one order active in the system, if the customer wants to place another order having one active, then he will have the following information: 
```JSON
{
  "message":"Customer already has an order, it must first be fulfilled in order to assign another."
}

```

An employee can interact with the application through actions that have an impact on the management of the restaurant service, below we will see some important actions:
ENDPOINT: /foodCourt/orders/employee
```JSON
{
  "idRestaurant": 43,
  "idOrder": [
    32
  ]
}
```
and will have for successful response: 
```JSON
{
  "message": "Employee successfully assigned."
}
```
If, in this case, a wrong value is registered, you will get the following error:
```JSON
Order, Restaurant and Status order not found, Please, the order must belong to the same restaurant and pending status
```
An employee can be assigned to one or several orders, for that reason, the application receives a list of order ids.
When an employee takes an order, the order status changes from pending to preparation automatically, the employee just by being logged in and using the application assigns the employee's id to the order without the need to pass his information directly through the order.
Then, an employee can change the order status from in preparation to ready, when this happens, the system communicates with another microservice which receives an external api that allows sending by notification a message to the user letting him know that the order is ready and delivers the verification of the order to have more security when delivering orders.
To deploy all this service, you only need to enter the order id:
ENDPOINT: /foodCourt/orders/action/ready/{id}
```JSON
56
```

example of successful response: 
```JSON
"message": "The order is ready, the order code is: 8454261904810968685. Do not share it with anyone."
```	
finally when the customer receives his order, the employee can change the status of the order to delivered, he enters the order verification number along with the order id: 
ENDPOINT: /foodCourt/orders/action/delivered/
```JSON
32
8454261904810968685
```
will return the following information: 
```JSON
{
  "message": "The Order was delivered."
}
```
If the employee enters a wrong value, you will get the following information: 
```JSON
Order, Status or verification code is wrong.
```
Finally, an action that a customer user can take, if the status of the order is pending, then he can cancel the order:
ENDPOINT: /foodCourt/orders/action/cancel/{id}
```JSON
56
```
will return the following message:
```JSON
{
  "message": "The order is cancelled."
}
```
If the customer tries to cancel an order that is in preparation, then it will return the following information:
```JSON
{
  "error": "Sorry, The order is in preparation and is not possible to cancel it."
}
```
To accompany all this interaction of the employee, the employee can paginate and see in an orderly manner the orders that are in your system, for this you need to be logged in, enter the idRestaurant, the different states of the orders by which you want to filter and the number of pages for which you want the action: 
```JSON
56
PENDING
8
```

The system is very user friendly, it was developed thinking in the user experience, in this way in the project you will find that there are data that the endpoints ask for that are of knowledge of each type of user to make the experience more enriching, but in these cases, the backend system makes all the managements to translate those data in data that allows the correct management of them, as for example the normalization of the database which allows the correct management of them, the backend system does all the steps to translate that data into data that allows the correct management of them, such as the normalization of the database which meets the recommended criteria by assigning id to PKS and not assigning other values, you can also see data that are backend type as the ids, which are data that belong more to the system that to the knowledge of the user, to put in evidence this the backend was realized in this practical way, with everything, the message that is tried to deliver is that the logic keeps the coherence with the data for its management and the normalization of them in the communication with other microservices and with database which is the recommended practice, that is the justification of the style of the program having shared a different approach and attending this and taking advantage of the opportunity of the exercise, I realized it in this way.

The system also has more exceptions, but I would like to present them and talk more about them in the presentation, however, to know a little of the project I give this introduction.

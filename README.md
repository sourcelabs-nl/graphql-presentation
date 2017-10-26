# graphql-presentation

Slides are in the docs folder:

* https://github.com/sourcelabs-nl/graphql-presentation/tree/master/docs

Code samples:

* https://github.com/sourcelabs-nl/graphql-presentation/tree/master/src/main/java/nl/sourcelabs/graphql

## Running the Spring Boot demo application

Clone this git repository on your local machine. And run the application using the Spring Boot Maven plugin.

On Mac/Unix

```bash
./mvnw spring-boot:run
```

On Windows

```bash
mvnw spring-boot:run
```
Once it is started, browse to: http://localhost:8080/graphiql

### Query

Now you can try to query the model (control+space will help you). An example query to retrieve all orders:

```graphql
query {
  orders {
    id
    totalPrice
    orderItems {
      quantity
      product {
        title
        price
        imageUrl
      }
      shipment {
        status
      }
    }
  }
}
```

### Query with errors element in the response

To generate a response that includes an errors element you can include the exception field inside orderItems. This demonstrates that the order information is returned but the OrderItem field names exception is null.

```graphql
query {
  orders {
    id
    totalPrice
    orderItems {
      exception
      quantity
      product {
        title
        price
        imageUrl
      }
      shipment {
        status
      }      
    }
  }
}
```

### Mutation

You can place an order by executing the following mutation query:

```graphql
mutation CreateOrder($order: OrderInput) {
  createOrder(order: $order) {
    id
    totalPrice
    orderItems {
      quantity
      product {
        title
        price
        imageUrl
      }
    }
  }
}
```

Using this query variable:

```json
{
  "order": {
    "orderItems": [
      {
        "productId": "123",
        "quantity": 1
      },
      {
        "productId": "234",
        "quantity": 1
      }
    ]
  }
}
```

## Questions?

If you have any questions or maybe you want to request a presentation or workshop, then please send a mail to: info@sourcelabs.nl



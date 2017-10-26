# graphql-presentation

Slides are in the docs folder:

* https://github.com/sourcelabs-nl/graphql-presentation/tree/master/docs

Code samples:

* https://github.com/sourcelabs-nl/graphql-presentation/tree/master/src/main/java/nl/sourcelabs/graphql

## Running the Spring boot example

Open the file: 8_SpringBootExample.kt and run the main function.

* Browse to: http://localhost:8080/graphiql

Now you can try to query the model (control+space will help you). An example query to retrieve all orders:

```graphql
query {
  orders {
    id
    totalPrice
    orderItems {
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

You can place an order by executing the following mutation query:

```graphql
mutation CreateOrder($order: OrderInput) {
  createOrder(order: $order) {
    id
    totalPrice
    orderItems {
      product {
        price
        title
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

If you have any questions, send a mail to: info@sourcelabs.nl



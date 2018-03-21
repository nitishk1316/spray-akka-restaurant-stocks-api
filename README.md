# Restaurant Stocks API Service

## How to run the service
Clone the repository:
```
> git clone https://github.com/nitishkumarsingh13/spray-akka-restaurant-stocks-api.git
```

Get to the `spray-akka-restaurant-stocks-api` folder:
```
> cd spray-akka-restaurant-stocks-api
```

Run the service:
```
> sbt run
```

The service runs on port 5000 by default.

## Usage

### Create a stock
```
curl -v -H "Content-Type: application/json" \
     -X POST http://localhost:5000/stocks \
     -d '{"id": "1", "name": "Milk", "price": "10!", "quantity": "50"}'
```

### Get all stocks
```
curl -v http://localhost:5000/stocks
```

### Get a stock by id
```
curl -v http://localhost:5000/stocks/1
```

### Update stock by id
```
curl -v -H "Content-Type: application/json" \
     -X PUT http://localhost:5000/stocks/1 \
     -d '{"name": "Milk", "price": "10!", "quantity": "100"}'
```

### Delete a quiz by id
```
curl -v -X DELETE http://localhost:5000/stocks/1
```

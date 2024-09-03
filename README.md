# 🌍 Currency Exchange API Documentation

This API provides CRUD (Create, Read, Update) operations for managing currencies and exchange rates. It supports operations like listing currencies, retrieving specific currency details, adding new currencies, managing exchange rates, and performing currency conversions.

## 📝 API Endpoints

### 1. 💱 Currencies

#### GET `/currencies`

**Description:**  
Retrieve a list of all available currencies.

**Response:**

- **`200 OK`**: Successful response with a list of currencies.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Response:**

```json
[
    {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

#### GET `/currency/{code}`

**Description:**  
Retrieve details of a specific currency by its code.

**Parameters:**

- `code`: The currency code (e.g., USD, EUR).

**Response:**

- **`200 OK`**: Successful response with currency details.
- **`400 Bad Request`**: Currency code is missing in the request.
- **`404 Not Found`**: Currency not found.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Response:**

```json
{
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

#### POST `/currencies`

**Description:**  
Add a new currency to the database.

**Request Body:**  
Form fields in `x-www-form-urlencoded` format.

- `FullName` (string): The name of the currency.
- `Code` (string): The currency code.
- `Sign` (string): The currency symbol.

**Response:**

- **`201 Created`**: Successful response with the newly created currency details.
- **`400 Bad Request`**: Missing required form field.
- **`409 Conflict`**: Currency with the same code already exists.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Response:**

```json
{
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```
### 2.📈 Exchange Rates

#### GET `/exchangeRates`

**Description:**  
Retrieve a list of all available exchange rates.

**Response:**

- **`200 OK`**: Successful response with a list of exchange rates.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Response:**

```json
[
    {
        "id": 0,
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.99
    }
]
```

#### GET `/exchangeRate/{baseCurrencyCode}{targetCurrencyCode}`

**Description:**  
Retrieve the exchange rate for a specific currency pair.

**Parameters:**

- `baseCurrencyCode`: The base currency code (e.g., USD).
- `targetCurrencyCode`: The target currency code (e.g., EUR).

**Response:**

- **`200 OK`**: Successful response with the exchange rate details.
- **`400 Bad Request`**: Currency codes are missing in the request.
- **`404 Not Found`**: Exchange rate for the specified currency pair not found.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Response:**

```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

#### POST `/exchangeRates`

**Description:**  
Add a new exchange rate to the database.

**Request Body:**  
Form fields in `x-www-form-urlencoded` format.

- `baseCurrencyCode` (string): The base currency code.
- `targetCurrencyCode` (string): The target currency code.
- `rate` (number): The exchange rate.

**Response:**

- **`201 Created`**: Successful response with the newly created exchange rate details.
- **`400 Bad Request`**: Missing required form field.
- **`409 Conflict`**: Exchange rate for the specified currency pair already exists.
- **`404 Not Found`**: One or both currencies do not exist in the database.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Response:**

```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

#### PATCH `/exchangeRate/{baseCurrencyCode}{targetCurrencyCode}`

**Description:**  
Update an existing exchange rate in the database.

**Parameters:**

- `baseCurrencyCode`: The base currency code (e.g., USD).
- `targetCurrencyCode`: The target currency code (e.g., EUR).

**Request Body:**  
Form fields in `x-www-form-urlencoded` format.

- `rate` (number): The new exchange rate.

**Response:**

- **`200 OK`**: Successful response with the updated exchange rate details.
- **`400 Bad Request`**: Missing required form field.
- **`404 Not Found`**: Exchange rate for the specified currency pair not found in the database.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Response:**

```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 1.02
}
```
### 3.🔄 Currency Exchange

#### GET `/exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT`

**Description:**  
Calculate the conversion of a specified amount from one currency to another.

**Parameters:**

- `from`: The base currency code (e.g., USD).
- `to`: The target currency code (e.g., EUR).
- `amount`: The amount to be converted.

**Response:**

- **`200 OK`**: Successful response with the conversion details.
- **`400 Bad Request`**: Required query parameters are missing.
- **`404 Not Found`**: Exchange rate for the specified currency pair not found.
- **`500 Internal Server Error`**: The database is unavailable or another server error occurred.

**Example Request:**

GET /exchange?from=USD&to=AUD&amount=10

**Example Response:**

```json
{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 2,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00,
    "convertedAmount": 14.50
}
```

### 4.⚠️ Error Responses 
In the case of an error, the response will include a message field explaining the error. 

**Example:**

```json
{
    "message": "Currency not found"
}
```

The message field will vary depending on the specific error that occurred.

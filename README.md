# Currency Exchange (Spring Boot + H2)

Features:
- CRUD APIs for `currencies_exchange_rate` table
- H2 console available at `/h2-console`

1. The request and response body log all API be called and call out external APIs:
   - Inbound: LoggingFilter with `OncePerRequestFilter`
   - Outbound: Use `ExchangeFilterFunction` in WebClientConfig
2. swagger-ui
   - Swagger UI available at `/swagger-ui.html`
3. i18n design:

    Accept language in Header:
   - `en`
   - `zh-TW`
4. Design patterns in this project:
   - Single Responsibility
   - DI & IoC
   - Singleton
   - Proxy (`@Transactional`)
   - Facade
5. Able to run on Docker:
```
docker build -t currency-exchange .
docker run -p 8080:8080 currency-exchange
```
6. Error handling

    Use `@RestControllerAdvice` to hanlde Global Exception
7. Encryption and decryption technology `(enhancement in future)`

   I'm so sorry because I don't have enough time to implement this feature, I describe the solution as follow:

   - Step 1: Server generates RSA key pair (`private key` and `public key`)
   - Step 2: Client will sign request with `private key`
   - Step 3: Service verify the request with `public key`

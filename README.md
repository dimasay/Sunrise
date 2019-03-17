**1. Create mySQL database**

**2. Create environment variables:**

- jdbcUrl - database url(ex: jdbc:mysql://127.0.0.1:3306/sunrise?serverTimezone=UTC)
- jdbcUser - database username
- jdbcPassword - database username password

**3. Add collection of queries to postman from file "Sunrise.postman_collection.json" from root**

**4. For succesful testing you should add to database city: Odessa(already created query in postman "Put city")**

`Manual`

_(при использовании коллекции запросов для postman)_

- You can add any cities from Ukraine.
- City name, Название города, latitude, longitude - required parameters.
- With GET query "supported cities" you can see list of supported cities.
- WIth Get queries "Get sunrise", "Get sunset", "Get sunset and sunrise" you can get all information what you need.
- The query is supported for several cities at once, it is enough to enter the names of cities separated by commas for the city parameter:

city=Kyiv, Odessa, Lvov

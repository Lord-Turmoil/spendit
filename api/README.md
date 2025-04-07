# Spendit API

Spendit backend API server.

## Environment Variables

You need to set up a MySQL database and configure the following environment variables:

- MYSQL_USERNAME
- MYSQL_PASSWORD
- MYSQL_HOST
- MYSQL_PORT
- MYSQL_DATABASE (optional)

Then, specify the domain of the front end app and the domain for Cookies.

- APP_DOMAIN
- COOKIES_DOMAIN

Finally, your 32 character JWT secret key.

- JWT_SECRET

https://www.amitph.com/spring-boot-data-jpa-mysql/
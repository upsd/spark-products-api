# Product API written in Spark Java
An example project utilising [Spark](http://sparkjava.com/) to create an API.

## Features
We have the following features:
* Get all products
* Add a new product

This means it is not a full CRUD application.

Those features translate into the following routes:
* GET `/products`
* POST `/products`

Check out the [tests](./src/test) and the [production code](./src/main) to see how it works.

## Adding more features
Also, check out the 
[branch implementing more endpoints](https://github.com/upsd/spark-products-api/tree/adding_features) for an example of 
adding more features to the basic API.
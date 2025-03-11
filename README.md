# ecommerce-product-service
This has code base for product-service of my ecommerce website, built as part of my capstone project.

### APIs supported
1. **GET /products/id**: To get details of the product with given id.
2. **GET /products**: To get list of all products.
3. **POST /products**: To create a new product.
4. **PUT /products/id**: To update the given product with that id.
5. **DELETE /products/id**: To delete that product.

### Spring profiles
1. **default**: It is selected by default in Spring boot application. This application, in the default mode, uses mysql databse with its repositories and services. This profile is the most used profile.
2. **fakestoremode**: this is the mode in which the application instead of using its own database, uses fakestore api to mock repository and database. 
3. _How to switch profiles?_ Nothing special needs to be done if one wants to run in default mode. But if one wants to run in fakestoremode, then active profile can be set externally in intellij or using command line. In intellij, go to run configuration, edit it and add "fakestoremode" under active profiles section. Delete this entry, if one wants to revert back to default mode. 
# Description

Fly recipe server implements a spring boot application that:
  * Scrapes the Fly Fish Food YouTube channel utilizing calls to the YouTube public API in order to create fly tying recipes.  
  * Creates a CRUD repository for managing fly recipes and users utilizing an in-memory database that also saves to disk.
  * Creates a REST API that can be called to:
    * Manage recipes:
      * Get All Recipes
      * Get all "complete" recipes
      * Get a count of all Recipes
      * Get a count of all "complete" recipes
    * Manage Users/ User Information
      * Add a new user
      * Get a user's favorite recipes
      * Get a user's owned ingredients
      * Update a user's favorite recipes
      * Udate a user's owned ingredients
# Front End Repository
https://github.com/mschabow/fly-recipe-react-frontend

# Live Front End Application
https://main.d1gzc3kx2t95ke.amplifyapp.com

# power-up-foodcourt

Queridos mentores, reciban un cordial saludo!

Este proyecto de estudio tiene su propio enfoque, pensando en la experiencia de usuario y que esta se va a llevar a cabo en Swagger, decidi tener en cuenta esto y hacer el programa muy interactivo,
de esta forma, el aplicativo pide datos concretos, datos que el usuario sabe y conoce y no datos que esta fuera de su conocimiento, es por eso que en vez de preguntar al usuario por informacion el cual
no conoce (por ejemplo Primary Key) decidi mas bien preguntar al usuario por nombres y otro tipo de informacion que es mas intuitiva y que el usuario verdaderamente tiene conocimiento, este enfoque no significa que las 
relaciones esten establecidas de esta misma forma, pues el sistema respeta la normalizacion y aunque al usuario le pida datos interactivos, el sistema en su funcionamiento interno se encarga de obtener
informacion correspondiente para poder llevar la correcta relacion y funcionamiento en el sistema, de esta manera hay una separacion entre la experiencia del usuario y el funcionamiento interno, tengo en cuenta que el cliente
interactua con el frontend y este tiene un equipo de desarrolladores que es de su entera ocupacion el cual se encarga por completo de esta relacion no solo cumpliendo los estandares respecto al cliente
sino tambien con la aplicacion por lo que decidi tomar este cuidado.

Este microservicio se encarga de las consultas que se hagan con la base de datos de la plazoleta y la logica referente a esta. Consume a la api de usuario-microservicio para validar informacion de los usuarios.

El puerto del microservicio es el 8091 y recuerde modificar los datos de acceso a la base de datos.

URL swagger: http://localhost:8091/swagger-ui/index.html

## EndPoints

## /foodCourt/restaurant/
-Peticion para crear un restaurante, disponible solo para usuario administrador

- El nombre del restaurante puede tener numeros, sin embargo no puede ser solo numerico.
- El nit debe ser solo numerico y es un campo unico.
- El numero de telefono puede tener un '+' al inicio y debe ser solo numerico. Debe contener entre 6 y 13 caracteres
- El url del Logo debe ser una url valida.
- El id del propietario es un Long y debe estar previamente registrado en la base de datos de usuarios.
Ejemplo de la petición:

```JSON
{
  "name": "Bash",
  "direction": "Java Street",
  "phone": "3192621119",
  "urlLogotype": "string",
  "nit": "407765",
  "idOwner": 3
}
```

## /foodCourt/plate/{nameRestaurant}/{categoryPlate}
Ejemplo de la petición:
- Peticion para crear un plato

```JSON
{
  "name": "Meat",
  "description": "Good Food",
  "price": 30.0,
  "urlImage": "http>//image.org"
}
```

## /foodCourt/plate/
- El propietario puede modificar la informacion de los platos de su restaurante
Ejemplo de la petición:
```JSON
{
  "id": 7,
  "price": 800.0,
  "description": "a"
}
```
## /foodCourt/plate/status/{enabled}
- El propietario puede cambiar el estado del plato
Ejemplo de la petición:
```JSON
{
  "name": "Apple",
  "idRestaurant": 15
}
```
## /foodCourt/pagination/restaurant
- disponible para usuario Cliente

- Debe de rellenar los parametros para poder llevar a cabo la operacion
- Obtendra los restaurantes de la plazoleta filtrados

## /foodCourt/pagination/plate
- disponible para usuario Cliente

- Debe de rellenar los parametros para poder llevar a cabo la operacion
- Obtendra los platos de la plazoleta filtrados, tendra dos opciones, puede filtrar los platos por restaurante y puede tener en cuenta la categoria del mismo

El proyecto aun esta en construccion y aun se esta puliendo algunos detalles, por lo que pido paciencia en historias de usuario, muchas gracias por la comprension!

Por motivos de retraso no pude hacer una rama para cada historia, pero a partir de las historias 11 en adelante las tendran, de esta manera busco aplicar el concepto y el requerimiento ya que no me fue posible
desde el principio aplicarlo y al verme atrasado decidi hacer el speed up de esta manera, tambien no tenia el conocimiento de que se debia de hacer asi, pero ya que me percate lo tendre en cuenta.

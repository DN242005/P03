# Bitácora de Contribución - Integrante 2

Rol: Autor de Factory Method y Observer / Revisor
Pull Request: PR pendiente

A mí me tocó trabajar en cómo creamos los objetos y cómo avisamos de los cambios. Implementé el patrón Factory Method creando la clase VentaFactory. La idea aquí fue quitarle esa responsabilidad a la clase principal y asegurar que nadie pueda instanciar una venta sin sus detalles (protegiendo las invariantes). También agregué la interfaz VentaObserver basándome en el patrón Observer. Así, cuando confirmamos una venta, el sistema avisa automáticamente a los observadores sin que nuestro dominio tenga que conocer detalles técnicos de bases de datos o interfaces.

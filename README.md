# Utilizando Java

| Servidor | WebFlux    | Threads Virtuais|
|----------|------------|-----------------|
| Tomcat   | Não        | Não             |
| Tomcat   | Não        | Sim             |
| Netty    | Sim        | Não             |
| Netty    | Sim        | Sim             |

# Utilizando Kotlin

| Servidor | WebFlux    | Coroutines | Threads Virtuais |
|----------|------------|------------|------------------|
| Tomcat   | Não        | Não        | Não              |
| Tomcat   | Não        | Não        | Sim              |
| Netty    | Sim        | Não        | Não              |
| Netty    | Sim        | Não        | Sim              |
| Netty    | Não        | Sim        | Sim              |
| Netty    | Não        | Sim        | Não              |
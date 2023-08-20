# sse-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Experimental SSE With Quarkus using EventBus.

### SSE

This project aims to experiment the implementation of an SSE web service with quarkus.

The service must make it possible to subscribe to a resource and then receive data as soon as they are available.

During the subscription, the service must return a quantity of data sent before the subscription.

### EventBus

It also sets up the decoupled exchange of data between components. For this it uses Vert.x EventBus.

The exchange takes place on a (bus) address with typed data.

### Thank

inspired by https://github.com/jimbogithub/sse and many other!
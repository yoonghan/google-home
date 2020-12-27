## Locker project connecting Raspberry Pi

### Usage
1. To compile code, run `sbt compile`
2. To run execute `sbt run`
3. To run in console, do`sbt console` and it will start a Dotty REPL.
4. To package `sbt pack`, the executable file is located in target/pack folder

### Configuration
1. Configure `application.conf` in resources.

```
com.walcron.twice {
  kafka {
    brokers = "my-app"
    username = "local"
    password = "INFO"
    topic = ""
    frequency-in-miliseconds = 1000
  }
}
akka {
  logger-startup-timeout = 30s
}
```

### Setup
1. Create a karaf setting or using cloudkarafka.
2. Make sure service.walcron.com is working and running.

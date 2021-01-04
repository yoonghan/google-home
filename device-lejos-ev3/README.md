# A Lego Device

Lego will be a receiving device for anything broadcasted by google home (need to be proxied though).

## Installation

1. Get eclipse.
2. Setup eclipse with Lejos.
3. Make sure it is using java 8.
4. From eclipse, import project.
5. Turn on Bluetooth for connection.
6. Create a PAN connection so that 10.0.1.1 is the Bluetooth IP address to connect to.
7. Right-click on Main.java and let it run on EV3 device

*NOTE:* For PAN connection, upload at least twice. The first time normally fails with connection timeout. 

## Testing

1. Right-click on TriggerClient.java and run as Java Appliction. This will run Large motor on Port A and B, while a medium motor on Port C.

## Windows PAN (Bluetooth to EV3)

1. Pair bluetooth
2. Open Control Panel\Network and Internet\Network Connections
3. Click on "Bluetooth Network Connection" and look for "View Bluetooth network devices" in the menu.
4. Right-click on EV3, Choose Connect Using -> Access Point.

### Commands
1. Connection is via PAN of bluetooth. But possible too via TCP/IP
Command is broken to:

```
AX:L:036000002000900
String.format("AX:L:%04d%04d%04d%03d",-360,0,2000,900)

(Motor port 1,Motor port 2):(Motor Type):(4-rotation motor 1)(4-rotation motor 2)(4-acceleration)(3-speed)
```

Motor port = A to D for ports, X as none. E.g. AX = port a for single motor only.

Motor type = U is unregulated motor(see below), L is Regulated Large motor, M is Regulated Medium motor

rotation1 = supports from -999 to 9999 for regulated motors.

rotation2 = supports from -999 to 9999 for regulated motors.

acceleration = support from 0000 to 6000 for regulated motors. Set to 0 to use default.

speed = support from 000 to 900 for regulated motors. set to 0 to use default.

```
AX:U:-05000100000000
String.format("AX:L:%04d%03d00000000",-50,1)

(Motor port 1,none):U:(4-power)(3-time in seconds)(8-dumped)
```

power = supports from -100 to 0100 for unregulated motors. - is backward, + is forward

time = supports from 0 to 999 in seconds

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
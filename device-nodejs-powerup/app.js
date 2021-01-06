var express = require('express');
const PoweredUP = require("node-poweredup");
const poweredUP = new PoweredUP.PoweredUP();

var app = express();
const serverHost = process.env.HOST || 'localhost'
const serverPort = process.env.PORT || 3001

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

let shouldRun = false;

app.get('/', (req, res) => {
  return res.send('Received a GET HTTP method');
});
 
app.post('/', (req, res) => {
  /*const message = {
    id,
    text: req.body.text,
    userId: req.me.id,
  };*/

  const message = {
    id: 1
  }
 
  return res.send(message);
});


poweredUP.on("discover", async (hub) => { // Wait to discover a Hub
    console.log(`Discovered ${hub.name}!`);
    await hub.connect(); // Connect to the Hub
    const motorA = await hub.waitForDeviceAtPort("A"); // Make sure a motor is plugged into port A
    const motorB = await hub.waitForDeviceAtPort("B"); // Make sure a motor is plugged into port B
    console.log("Connected");

    while (shouldRun) { 
        console.log("Running motor B at speed 50");
        motorB.rotateByDegrees(90); // Start a motor attached to port B to run a 3/4 speed (75) indefinitely
        console.log("Running motor A at speed 100 for 2 seconds");
        motorA.setPower(100); // Run a motor attached to port A for 2 seconds at maximum speed (100) then stop
        await hub.sleep(2000);
        motorA.brake();
        await hub.sleep(1000); // Do nothing for 1 second
        console.log("Running motor A at speed -30 for 1 second");
        motorA.setPower(-30); // Run a motor attached to port A for 2 seconds at 1/2 speed in reverse (-50) then stop
        await hub.sleep(2000);
        motorA.brake();
        shouldRun = false;
        await hub.sleep(1000); // Do nothing for 1 second
    }
});

poweredUP.scan(); // Start scanning for Hubs

app.listen(serverPort, function() {
   console.log(`Server listening at http://${serverHost}:${serverPort}`);
});

module.exports = app;

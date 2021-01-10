var express = require('express');
//const PoweredUP = require("node-poweredup");
//const poweredUP = new PoweredUP.PoweredUP();
const lego = require("./lego");

var app = express();
const serverHost = process.env.HOST || 'localhost'
const serverPort = process.env.PORT || 3001

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

let shouldRun = false;

app.get('/', (req, res) => {
  return res.send('Received a GET HTTP method');
});
 
app.post('/api/trigger', (req, res) => {
  console.log("Received")
  shouldRun = true
  return res.send({"status":"ok"});
});

app.post('/api/trigger/complete', (req, res) => {
  console.log("Received")
  shouldRun = true
  return res.status(204).send()
});

app.listen(serverPort, function() {
  /*poweredUP.on("discover", async (hub) => { // Wait to discover a Hub
    console.log(`Discovered ${hub.name}!`);
    await hub.connect(); // Connect to the Hub
    const motorA = await hub.waitForDeviceAtPort("A"); // Make sure a motor is plugged into port A
    const motorB = await hub.waitForDeviceAtPort("B"); // Make sure a motor is plugged into port B

    while(true) {
      if (shouldRun) {
        await lego.legoExec(motorA, motorB, hub)
        shouldRun = false;
        await hub.sleep(1000); // Do nothing for 1 second
      }
      else {
        await hub.sleep(5000);
      }
    }
  });

  poweredUP.scan(); // Start scanning for Hubs*/

  console.log(`Server listening at http://${serverHost}:${serverPort}, and run as ROOT!!`);
});


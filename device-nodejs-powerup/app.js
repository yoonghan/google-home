var express = require('express');

var app = express();
const serverHost = process.env.HOST || 'localhost'
const serverPort = process.env.PORT || 3001

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

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

app.listen(serverPort, function() {
   console.log(`Server listening at http://${serverHost}:${serverPort}`);
});

module.exports = app;

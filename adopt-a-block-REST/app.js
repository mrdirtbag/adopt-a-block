var restify = require('restify');
var MongoClient = require('mongodb').MongoClient;
var _ = require('underscore');

var mongoConn = process.env.MONGODB;

var server = restify.createServer();
server.use(restify.bodyParser());
server.use(
  function crossOrigin(req,res,next){
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    return next();
  }
);

// POST BATCH //////////////////////////////////
server.post('/ivCollection', function (req, res, next) {
  
  var payload = req.body;
  payload.submitted = new Date().getTime();
  
  console.log(payload);
  
  MongoClient.connect(mongoConn, function(err, db) {
    if(err) { return console.dir(err); }
    
    // delete the real time data (if any)    
    db.collection('realtime').remove({username: payload.username, hashID: payload.hashID}, function(){
      //nothing to do?
    });

    // insert batch data
    var collection = db.collection('ivCollection');
  
    collection.insert(payload, {w:1}, function(insertError, result) {
      if(insertError) { return console.dir(insertError);}
      
      res.send(201, "yup, I got it");
      return next();
    });
    

  });
});

// POST REALTIME //////
server.post('/realtime', function (req, res, next) {
  
  var payload = req.body;
  payload.submitted = new Date().getTime();
  
  console.log(payload);
  
  MongoClient.connect(mongoConn, function(err, db) {
    if(err) { return console.dir(err); }

    var collection = db.collection('realtime');
      
    collection.update({username: payload.username, hashID: payload.hashID}, payload, {upsert:true}, function(insertError, result) {
      if(insertError) { return console.dir(insertError);}
      
      res.send(201, "yup, I got the realtime data");
      return next();
    });
  });
});

// GET BATCH //////////////////////////
server.get('/everything', function (req, res, next) {
  MongoClient.connect(mongoConn, function(err, db) {
    if(err) throw err;

    var collection = db
      .collection('ivCollection')
      .find({})
      .limit(10)
      .toArray(function(err, docs) {
        var users = _(docs).map(function(user){
          user.secondsToComplete = (_.max(_.pluck(user.points, "epoch")) -  _.min(_.pluck(user.points, "epoch"))) / 1000;
          return user;
        });       
        res.send(200, {"users" : users});       
      });
  });  
  return next();
});

// GET realtime
server.get('/GETrealtime', function (req, res, next) {
  MongoClient.connect(mongoConn, function(err, db) {
    if(err) throw err;

    var collection = db
      .collection('realtime')
      .find({})
      .limit(10)
      .toArray(function(err, docs) {
        res.send(200, docs);       
      });
  });  
  return next();
});


server.listen(process.env.PORT || 5000, function() {
  console.log('%s listening at %s', server.name, server.url);
});

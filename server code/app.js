const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const compression = require('compression');
const path = require('path');
const logger = require('morgan');
const dotenv = require('dotenv');

process.env.APP_ROOT = __dirname;
dotenv.config();
dotenv.load();
const app = express();

app.use(logger('dev'));
app.use(cookieParser(process.env.SECRET));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

// compress all responses
app.use(compression());

//disable x-powered-by
app.disable('x-powered-by');

console.log("PRODUCTION ENVIRONMENT");

const routes = require("./app/routes");
app.use(express.static('uploads'));
app.use('/', routes);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handler
app.use(function (err, req, res, next) {
    console.log(err);
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'DEVELOPMENT' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.json({status: false, message: 'error'});
});

module.exports = app;
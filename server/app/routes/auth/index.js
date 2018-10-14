const Router = require("express").Router;
const route = Router();

route.post('/login', require('./functions/login'));
route.post('/sendOtp', require('./functions/sendOtp'));

module.exports = route;
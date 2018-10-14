const Router = require("express").Router;
const route = Router();
const jwt = require('jsonwebtoken');


route.use(function (req, res, next) {
    var token = req.body.token || req.query.token || req.headers['jwt-token'];
    if (!token) {
        return res.status(403).send({success: false, message: 'No token provided.'});
    }

    jwt.verify(token, 'superSecret', function (err, decoded) {
        if (err) {
            return res.json({success: false, message: 'Failed to authenticate token.'});
        }

        if(decoded.phone.toString() !== process.env.ADMIN_PHONE.toString()){
            return res.status(403).json({status: false, message: "access denied"});
        }

        req.user = decoded;
        next();
    });
});


/***
 * add a quiz
 * query = {quizName}
 */
route.get('/addQuiz', require('./functions/addQuiz'));

/***
 * add a quiz
 * query = {quizName}
 */
route.post('/addQuestion', require('./functions/addQuestions'));

/***
 * trigger a quiz after 20s and notify users
 * query = {quizId}
 */
route.get('/triggerQuiz', require('./functions/triggerQuiz'));


module.exports = route;
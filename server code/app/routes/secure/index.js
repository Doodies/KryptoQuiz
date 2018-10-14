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

        req.user = decoded;
        next();
    });
});

/***
 * submit firstName, lastName, email
 * body: quizId, userId, correctAns
 */
route.post('/submitDetails', require("./functions/submitDetails"));

/***
 * get user details
 */
route.get('/getDetails', require("./functions/getDetails"));

/***
 * Get quiz questions
 * params: quizId
 */
route.get('/getQuizQues', require("./functions/getQuiz"));

/***
 * submit quiz
 * body: quizId, userId, correctAns
 */
route.post('/submitQuiz', require("./functions/submitQuiz"));

/***
 * get leader board
 */
route.get('/leaderBoard', require("./functions/leaderBoard"));

module.exports = route;
const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");
const notifyQuiz = require('./notifyQuiz');
const calculateResult = require('./calculateResult');

const Quiz = models["quiz"];
const User = models["user"];
const Question = models["question"];
const User_quiz = models["user_quiz"];

module.exports = (req, res) => {
    let quizId = parseInt(req.query["quizId"]);
    if (!quizId) {
        return res.status(400).json({status: false, message: "bad request"})
    }

    Quiz.findById(quizId)
        .then(quiz => {
            let startOffsetSeconds = 5 * 1000;   // 5 seconds
            let quizTotalTimeSeconds = 100 * 1000;
            let endOffsetSeconds = 5 * 1000;

            let currentDT = new Date();
            let quizStartDT = new Date(currentDT.getTime() + startOffsetSeconds);
            let quizEndDT = new Date(quizStartDT.getTime() + quizTotalTimeSeconds + endOffsetSeconds);

            // update quiz timings
            quiz.update({
                start_time: quizStartDT,
                end_time: quizEndDT
            })
                .then(function (questions) {
                    // start a function to calculate final result
                    setTimeout(calculateResult, quizEndDT - quizStartDT, quizId);
                    console.log("results will be calculated after "+(quizEndDT - quizStartDT).toString());
                    // setTimeout(calculateResult, 1000, quizId);

                    // notify users
                    User.findAll({
                        where: {},
                        raw: true,
                    })
                        .then(users => {
                            let tokens = users.map(user => user['firebase_id']);
                            notifyQuiz("new Quiz", 'Quiz ' + quizId.toString() + ' is going to start at ' + quizStartDT.toLocaleString(),
                                {quizId: quizId, quizStartDT: startOffsetSeconds}, tokens);

                            return res.status(200).json({status: true, message: "successfully notified"})
                        })
                        .catch((err) => {
                            console.log(err);
                            return res.status(503).json({status: false, message: "error in database"})
                        });
                })
                .catch((err) => {
                    console.log(err);
                    return res.status(503).json({status: false, msg: "error in database"})
                });
        })
        .catch((err) => {
            console.log(err);
            return res.status(503).json({status: false, msg: "error in database"})
        });
};
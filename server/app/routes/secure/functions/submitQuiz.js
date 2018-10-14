const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");

const Quiz = models["quiz"];
const User = models["user"];
const Question = models["question"];
const User_quiz = models["user_quiz"];

module.exports = (req, res) => {
    let userId = parseInt(req.user["id"]);
    let quizId = parseInt(req.body["quizId"]);
    let correctCount = parseInt(req.body["correctCount"]);

    if (!quizId || !userId || !correctCount) {
        return res.status(400).json({status: false, message: "bad request"})
    }

    Quiz.findById(quizId)
        .then(quiz => {
            if (!quiz) {
                return res.status(400).json({status: true, message: "quiz not found"});
            }
            if ((new Date()) > quiz.end_time) {
                return res.status(400).json({status: true, message: "time limit exeeded for submission"});
            }

            User.findById(userId)
                .then(user => {
                    if (!user) {
                        return res.status(400).json({status: true, message: "user not found"});
                    }

                    quiz.addUser(user, {through: {correct_count: correctCount}})
                        .then(() => {
                            return res.status(200).json({status: true, message: "successfully submitted"});
                        })
                        .catch((err) => {
                            console.log(err);
                            return res.status(503).json({status: false, message: "error in database"})
                        });
                })
                .catch((err) => {
                    console.log(err);
                    return res.status(503).json({status: false, message: "error in database"})
                });
        })
        .catch((err) => {
            console.log(err);
            return res.status(503).json({status: false, message: "error in database"})
        });
};
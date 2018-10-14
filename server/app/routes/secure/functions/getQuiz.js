const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");

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
            // if (!quiz || (new Date()) < quiz.start_time || (new Date()) > quiz.end_time) {
            //     return res.status(400).json({status: true, message: "quiz not found or not started"});
            // }

            quiz.getQuestions()
                .then(function (questions) {
                    if (questions.length === 0) {
                        return res.status(400).json({status: true, message: "questions not found"});
                    }

                    return res.status(200).json({status: true, message: questions});
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
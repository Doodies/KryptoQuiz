const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");
const notify = require('./notifyQuiz');

const Quiz = models["quiz"];

module.exports = (req, res) => {
    let quizName = req.query["quizName"];
    if (!quizName) {
        return res.status(400).json({status: false, message: "bad request"})
    }

    Quiz.create({
        quiz_name: quizName,
        start_time: new Date(),
        end_time: new Date()
    })
        .then(quiz => {
            return res.status(200).json({status: true, message: "quiz added"});
        })
        .catch((err) => {
            console.log(err);
            return res.status(503).json({status: false, message: "error in database"})
        });
};
const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");

const Question = models["question"];

module.exports = (req, res) => {
    let ques = req.body['ques'];
    let question_image = req.body['question_image'];
    let o_one = req.body['o_one'];
    let o_two = req.body['o_two'];
    let o_three = req.body['o_three'];
    let o_four = req.body['o_four'];
    let ans = req.body['ans'];
    let quiz_id = req.body['quiz_id'];

    Question.create({
        ques: ques,
        question_image: question_image,
        o_one: o_one,
        o_two: o_two,
        o_three: o_three,
        o_four: o_four,
        ans: ans,
        quiz_id: quiz_id
    })
        .then(question => {
            return res.status(200).json({status: true, message: "question added"});
        })
        .catch((err) => {
            console.log(err);
            return res.status(503).json({status: false, message: "error in database"})
        });
};
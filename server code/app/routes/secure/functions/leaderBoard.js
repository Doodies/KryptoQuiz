const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");

const Quiz = models["quiz"];
const User = models["user"];
const Question = models["question"];
const User_quiz = models["user_quiz"];


module.exports = (req, res) => {
    let freq = req.query["freq"];
    if (!freq) {
        return res.status(400).json({status: false, message: "bad request"})
    }

    let whereObj = {};
    if (freq === "daily") {
        whereObj['created_at'] = {[Op.gt]: new Date(new Date() - 24 * 60 * 60 * 1000)}
    } else if (freq === "monthly") {
        whereObj['created_at'] = {[Op.gt]: new Date(new Date() - 30 * 24 * 60 * 60 * 1000)}
    }

    User_quiz.findAll({
        where: whereObj,
        group: ['user_quiz.user_id'],
        attributes: [[Sequelize.fn('SUM', Sequelize.col('tAmount')), 'total'], 'user_id'],
        order: [[Sequelize.col('total'), 'DESC']],
        raw: true,
        // include: [{model: User}]
    }).then(function (users) {
        console.log(users);
        return res.status(200).json({status: true, message: users});
    }).catch(function (error) {
        console.log(error)
    });
};
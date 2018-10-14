const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");
const User = models["user"];
const User_quiz = models["user_quiz"];
const blockchainService = require(process.env.APP_ROOT + '/app/services/blockchain.services.js');

module.exports = (req, res) => {
    User
        .findById(req.user['id'])
        .then(user => {
            if (!user) {
                return res.status(404).json({status: false, message: "user not found"});
            }

            let rawUser = user.get({plain: true});
            delete rawUser['otp'];

            User_quiz.findAll({
                where: {
                    user_id: rawUser.id,
                },
                raw: true
            })
                .then(objs => {
                    // setting the total games played
                    rawUser['gamesPlayed'] = objs.length;

                    let amountWon = 0;
                    for (let obj of objs) {
                        if (obj['tAmount'] > 0) {
                            amountWon += obj['tAmount'];
                        }
                    }
                    // setting total amount won
                    rawUser['amountWon'] = amountWon;

                    // setting total amount left
                    // use blockchain function
                    blockchainService.balanceOf(rawUser['public_key'])
                        .then(result => {
                            rawUser['balance'] = result;
                            return res.status(200).json({status: true, message: rawUser});
                        })
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
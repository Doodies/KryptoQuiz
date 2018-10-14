const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");
const notifyResults = require('./notifyResults');
const blockchainService = require(process.env.APP_ROOT + '/app/services/blockchain.services.js');


const Quiz = models["quiz"];
const User = models["user"];
const Question = models["question"];
const User_quiz = models["user_quiz"];


module.exports = (quizId) => {
    if (!quizId) {
        console.log("can't calculate result as no quiz id supplied!");
        return;
    }else{
        console.log("Calculating results!");
    }

    Quiz.findById(quizId)
        .then(quiz => {
            if (!quiz) {
                console.log("quiz not found");
                return;
            }
            console.log("quiz found");
            // if (quiz.end_time > new Date()) {
            //     console.log("quiz not ended");
            //     return;
            // }

            Quiz.findAll({
                where: {
                    id: quizId
                },
                include: [{
                    model: User,
                }],
                logging: false,
                raw: true
            })
                .then(objs => {
                    // group users
                    let groups = [[]/*0-4*/, []/*5*/, []/*6*/, []/*7*/, []/*8*/, []/*9*/, []/*10*/];
                    for (let obj of objs) {
                        let count = obj['users.user_quiz.correct_count'];
                        if (count >= 5) {
                            groups[count - 4].push(obj);
                        } else {
                            groups[0].push(obj);
                        }
                    }

                    // calculate total distribution amount
                    let distributionRatios = [0.05, 0.1, 0.15, 0.3, 0.4]
                    let totalNonZeros = 5;
                    for (let i = 0; i < 5; i++) {
                        if (groups[i + 2].length === 0) {
                            totalNonZeros--;
                            let divR = distributionRatios[i];
                            distributionRatios[i] = 0;
                            let dis = divR/totalNonZeros;
                            for(let j = 0; j<distributionRatios.length; j++){
                                if(distributionRatios[j] !== 0)
                                    distributionRatios[j] += dis;
                            }
                        }
                    }

                    let totalDistributionAmount = groups[0].length * 5;
                    let totalDistributionArray = [];
                    for(let i = 0; i<distributionRatios.length; i++){
                        totalDistributionArray.push(distributionRatios[i] * totalDistributionAmount)
                    }

                    // calculate how much a person of a group earned
                    let distributionArray = [-5, 0];
                    for (let i = 0; i < totalDistributionArray.length; i++) {
                        if (groups[i + 2].length !== 0) {
                            distributionArray.push(totalDistributionArray[i] / groups[i + 2].length)
                        } else {
                            distributionArray.push(totalDistributionArray[i])
                        }
                    }


                    // calculate how much each person earned
                    let transactionsArray = [];
                    for (let i = 0; i < groups.length; i++) {
                        let group = groups[i];
                        distributionArray[i] = Math.floor(distributionArray[i]);
                        let tAmount = distributionArray[i];

                        for (let obj of group) {
                            obj['tAmount'] = tAmount;
                            transactionsArray.push(obj);
                        }
                    }

                    // notify results to users
                    notifyResults(transactionsArray);

                    // add transaction to the user_quiz database table
                    for (let i = 0; i < groups.length; i++) {
                        let whereObj = {quiz_id: quizId};
                        if (i === 0) {
                            whereObj['correct_count'] = {[Op.lt]: 5}
                        } else {
                            whereObj['correct_count'] = i + 4;
                        }
                        User_quiz.update(
                            {tAmount: distributionArray[i]},
                            {where: whereObj}
                        )
                            .spread((affectedCount, affectedRows) => {
                            })
                    }

                    // Do real transaction on blockchain
                    for (let obj of transactionsArray) {
                        if (obj['tAmount'] === 0) {
                            continue;
                        }
                        if (obj['tAmount'] < 0) {
                            blockchainService.transfer(obj['users.public_key'], process.env.ADMIN_PUBLIC_KEY, -1 * obj['tAmount'])
                        } else {
                            blockchainService.transfer(process.env.ADMIN_PUBLIC_KEY, obj['users.public_key'], obj['tAmount'])
                        }
                    }

                    console.log("transactions queued");
                })
        })
        .catch((err) => {
            console.log(err);
        });
};
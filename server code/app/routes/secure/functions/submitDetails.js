const Sequelize = require("sequelize");
const Op = Sequelize.Op;
const models = require(process.env.APP_ROOT + "/app/db/models");
const User = models["user"];

module.exports = (req, res) => {
    let firstName = req.body['firstName'];
    let lastName = req.body['lastName'];
    let email = req.body['email'];

    User
        .findById(req.user['id'])
        .then(user => {
            if (!user) {
                return res.status(404).json({status: false, message: "user not found"});
            }
            user
                .update({
                    first_name : firstName,
                    last_name : lastName,
                    email : email
                })
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
};
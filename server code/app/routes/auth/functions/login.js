const sequelize = require('sequelize');
const models = require(process.env.APP_ROOT + "/app/db/models");
const User = models.user;
const jwt = require('jsonwebtoken');

//body = {phone, otp}
module.exports = (req, res) => {
    let phone = parseInt(req.body['phone']);
    let otp = parseInt(req.body['otp']);
    if (!phone || !otp) {
        return res.status(400).json({status: false, message: "bad request"});
    }

    User.findAll({
        where: {
            phone: phone,
            otp: otp
        },
        limit: 1,
        logging: false
    })
        .then(function (users) {
            if (users.length === 0) {
                return res.status(400).json({status: false, message: "invalid credentials"});
            }

            let user = users[0];

            const payload = {
                id: user.id,
                phone: user.phone
            };
            var token = jwt.sign(payload, 'superSecret', {
                expiresIn: 500000 // (minutes) expires after a lot of hours
            });

            // remove otp from database
            user
                .update({
                    otp: null
                })
                .then(() => {
                    // return the information including token as JSON
                    return res.status(200).json({
                        status: true,
                        message: 'Enjoy your token!',
                        token: token,
                        newUser: !user.email
                    });
                })
                .catch((err) => {
                    console.log(err);
                    return res.status(503).json({status: false, message: "error in database"})
                })
        })
        .catch(function (err) {
            console.log(err);
            return res.status(400).json({status: false, message: "error in database"});
        })
};

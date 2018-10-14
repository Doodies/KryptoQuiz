const models = require(process.env.APP_ROOT + "/app/db/models");
const User = models.user;
const randomString = require('randomstring');
const fs = require('fs');
const blockchainService =require(process.env.APP_ROOT + '/app/services/blockchain.services.js');


const accountSid = 'AC943a233dcea6f059a65c2ceb140eca41';
const authToken = '94342787f2aa0800f5ab906a81058d8c';
const twilioClient = require('twilio')(accountSid, authToken);

//body = {phone, firebaseId}
module.exports = (req, res) => {
    const phone = parseInt(req.body["phone"]);
    const firebaseId = req.body['firebaseId'];
    if (!phone || !firebaseId) {
        console.log(phone);
        console.log(firebaseId);
        return res.status(400).json({status: false, message: "bad request"});
    }

    User.findAll({
        where: {phone: phone},
        limit: 1
    })
        .then(users => {
            let otp = Math.floor(1000 + Math.random() * 9000);

            if (users.length === 0) {
                // create the user
                const keys = require('./keys');
                let publicKey = keys['keys'].shift();
                blockchainService.transfer(process.env.ADMIN_PUBLIC_KEY, publicKey, 50);

                fs.writeFile("./app/routes/auth/functions/keys.json", JSON.stringify(keys), function (err) {
                    if(err){
                        console.log(err);
                        return res.status(400).json({status: false, message: "some error occurred"});
                    }

                    User.create({
                        phone: phone,
                        otp: otp,
                        firebase_id: firebaseId,
                        public_key: publicKey
                    })
                        .then(user => {
                            // you can now access the newly created user via the variable user
                            send_otp_now(res, phone, otp, true);
                        })
                        .catch((err) => {
                            console.log(err);
                            return res.status(503).json({status: false, message: "error in database"})
                        });
                });
            } else {
                // update otp and firebase id in database
                let user = users[0];
                if (user.otp) {
                    otp = user.otp;
                }

                // add the otp
                user
                    .update({
                        otp: otp,
                        firebase_id : firebaseId
                    })
                    .then(() => {
                        send_otp_now(res, phone, otp, false);
                    })
                    .catch((err) => {
                        console.log(err);
                        return res.status(503).json({status: false, message: "error in database"})
                    });
            }
        })
        .catch((err) => {
            console.log(err);
            return res.status(503).json({status: false, message: "error in database"})
        });
};

function send_otp_now(res, phone, otp, isNew) {
    twilioClient.messages
        .create({
            from: '+15719897952',
            to: '+91' + phone.toString(),
            body: 'otp is ' + otp.toString()
        })
        .then(message => {
            console.log(message);
            return res.status(200).json({status: true, message: "otp sent", "newUser": isNew});
        })
        .catch(err => {
            console.log(err);
            return res.status(503).json({status: false, message: "error sending otp"});
        })
}
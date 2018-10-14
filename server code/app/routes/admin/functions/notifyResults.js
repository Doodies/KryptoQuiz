const FCM = require('fcm-node');
const serverKey = process.env.FIREBASE_SERVER_KEY;
const fcm = new FCM(serverKey);

module.exports = (objs) => {
    const message = {
        to: '',
        collapse_key: 'green',
        notification: {
            title: "Quiz Results",
            body: "",
        },
        data: {}
    };

    for (let obj of objs) {
        message['to'] = obj['users.firebase_id'];
        if (obj['tAmount'] < 0) {
            message['notification']['body'] = "You lost ";
        } else {
            message['notification']['body'] = "You earned ";
        }
        message['notification']['body'] = message['notification']['body'] + Math.abs(obj['tAmount']).toString() +
            " coins in quiz " + obj['id'].toString();

        message['data']['tAmount'] = obj['tAmount'];
        message['data']['quizId'] = obj['id'];

        fcm.send(message, function (err, response) {
            if (err) {
                console.log(err);
            } else {
                console.log("Successfully sent to id " + obj + " with response: ", response);
            }
        });
    }
};
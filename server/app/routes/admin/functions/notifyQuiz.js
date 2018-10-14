const FCM = require('fcm-node');
const serverKey = process.env.FIREBASE_SERVER_KEY;
const fcm = new FCM(serverKey);

module.exports = (title, body, data, tokens) => {
    const message = {
        to: '',
        collapse_key: 'green',
        notification: {
            title: title,
            body: body,
        },
        data: data
    };

    for (let token of tokens) {
        message['to'] = token;
        fcm.send(message, function (err, response) {
            if (err) {
                console.log(err);
            } else {
                console.log("Successfully sent to id " + token + " with response: ", response);
            }
        });
    }
};
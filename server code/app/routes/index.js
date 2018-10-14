const express = require("express");
const Router = express.Router;
const route = Router();
const blockchainService = require(process.env.APP_ROOT + '/app/services/blockchain.services.js');
const routes = {
    api: {
        admin: require("./admin"),
        auth: require("./auth"),
        secure: require("./secure"),
    },
};


route.use(function (req, res, next) {
    try {
        req.body = JSON.parse(Object.keys(req.body)[0]);
    } catch (err) {
        // req.body = req.body
    }
    next();
});

route.get('/api', function (req, res) {
    return res.json({status: true, message: 'hey you! go ahead :)'});
});
route.use('/api/auth', routes.api.auth);
route.use('/api/admin', routes.api.admin);
route.use('/api/secure', routes.api.secure);
route.use('/api/images', express.static(process.env.APP_ROOT + "/app/db/uploads/images"));

route.get('/api/checkBalance', function (req, res) {
    let retVal = {status: true, balance: 0};
    blockchainService.balanceOf(req.query['publicKey']).then(b => {
        retVal['balance'] = b;
        res.status(200).json(retVal);
    })

    // blockchainService.transfer(process.env.ADMIN_PUBLIC_KEY, "0x0b1c0105eF5D1934Ed3723750d7B65289547769C", 50);
    // blockchainService.transfer(process.env.ADMIN_PUBLIC_KEY, "0xee32d193be00cc30a9598b03753ed074ab3bd34c", 50);
    // blockchainService.transfer(process.env.ADMIN_PUBLIC_KEY, "0xd6783af856b80339ed0c64ae103cf435542a8304", 50);
    // blockchainService.transfer(process.env.ADMIN_PUBLIC_KEY, "0xdee8a32230735446f7bb0c3507b52c3aecadb5d1", 50);
});

module.exports = route;
let DappToken = artifacts.require("./DappToken.sol")
module.exports = (deployer) => {
    deployer.deploy(DappToken, 10000, 'qCoin', 5, 'QCX');
};
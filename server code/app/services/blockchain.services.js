const Web3 = require('web3');
var web3 = new Web3();
const Url = require('url');
const provider = new web3.providers.HttpProvider('http://localhost:7545', 0, process.env.ETHERBASE, process.env.PASSWORD);
var account;
var accounts;
// Step 1: Get a contract into my application
var json = require("../truffle/build/contracts/DappToken.json");
// Step 2: Turn that contract into an abstraction I can use
var contract = require("truffle-contract");

let DappToken;

var pendingTransactions = [];



module.exports = {

    init : function () {
        web3.setProvider(provider);
        DappToken = contract(json)
        DappToken.setProvider(provider)
        web3.eth.getAccounts((err,accs)=>{
            if(err!=null){
                console.log("There was an error in fetching accounts !!!")
            }
            else if(accs.length==0){
                console.log("Error!!")
            }
            else{
                accounts = accs;
                account = accounts[0]
                console.log("Main account for sending transactions is : " + account)
            }
        })
    },
    getAccounts: function () {
        return web3.eth.accounts;
    },
    balanceOf: function(addr){
        let meta;
        return DappToken.deployed().then((instance)=>{
            meta = instance;
            return meta.contract.balanceOf.call(addr)
        }).then((result)=>{
            return result.toNumber()
        }).catch(err=>{
            console.log("Error !!!!")
        })
    },
    // from sender to receiver
    transfer: function(SA, RA, value){
        // predefine value = 10
        // RA  = "0x5Cb24067a6bF014cAC706e255bA5bf997c550124";
        // SA ="0x82cb832780e7724e3cccac464b5077f31953e0f1";
        // value = 10;

        let meta;
        return DappToken.deployed().then((instance)=>{
            meta = instance;
            return meta.contract.transfer(RA,value,{from:SA})
        }).then((result)=>{
        }).catch(err=>{
            console.log("Error!!!")
        })
    },
};

// To check whether the transfer is success
/*

    if(balanceOf[owner] does not change after transfer){
        p<< "Not Success"
    }
    else{
        p<< "Sucess"
    }

 */
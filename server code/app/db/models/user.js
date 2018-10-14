'use strict';
module.exports = (sequelize, DataTypes) => {
    const user = sequelize.define('user', {
        phone: {
            type: DataTypes.BIGINT.UNSIGNED,
            unique: true,
            validate: {
                max: 9999999999,
                min: 1000000000,
                isInt: true
            }
        },
        otp: {
            type: DataTypes.INTEGER.UNSIGNED,
            allowNull: true
        },
        firebase_id: {
            type: DataTypes.STRING,
            allowNull: true,
            unique: true,
            validate: {
                notEmpty: true,
            }
        },
        public_key: {
            type: DataTypes.STRING,
            allowNull: true,
            unique: true,
            validate: {
                notEmpty: true,
            }
        },
        first_name: {
            type: DataTypes.STRING,
            allowNull: true,
        },
        last_name: {
            type: DataTypes.STRING,
            allowNull: true,
        },
        email: {
            type: DataTypes.STRING,
            allowNull: true,
            validate: {
                isEmail: true
            }
        },
    }, {
        underscored: true,
        indexes: [{
            unique: true,
            fields: ['phone']
        }]
    });

    user.associate = (models) => {
        user.belongsToMany(models.quiz, { through: models.user_quiz});
    };

    return user;
};
'use strict';

module.exports = (sequelize, DataTypes) => {
    const user_quiz = sequelize.define('user_quiz', {
        correct_count: {
            type: DataTypes.INTEGER,
            allowNull: false,
            validate: {
                notEmpty: true,
            }
        },
        tAmount: {
            type: DataTypes.FLOAT,
            allowNull: true
        },
    }, {
        underscored: true,
    });

    return user_quiz;
};
'use strict';
module.exports = (sequelize, DataTypes) => {
    const quiz = sequelize.define('quiz', {
        quiz_name: {
            type: DataTypes.STRING,
            allowNull: false,
            unique: true,
            validate: {
                notEmpty: true,
            }
        },
        start_time: {
            type: DataTypes.DATE,
            allowNull: false,
            validate: {
                isDate: true
            }
        },
        end_time: {
            type: DataTypes.DATE,
            allowNull: false,
            validate: {
                isDate: true
            }
        },
    }, {
        underscored: true,
        indexes: [{
            unique: true,
            fields: ['quiz_name']
        }]
    });

    quiz.associate = (models) => {
        quiz.hasMany(models.question, {as: 'Questions'});
        quiz.belongsToMany(models.user, { through: models.user_quiz});
    };

    return quiz;
};
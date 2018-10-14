'use strict';

module.exports = (sequelize, DataTypes) => {
    const question = sequelize.define('question', {
        ques: {
            type: DataTypes.STRING,
            allowNull: false,
            unique: true,
            validate: {
                notEmpty: true,
            }
        },
        question_image: {
            type: DataTypes.STRING,
            allowNull: true,
            defaultValue: null,
        },
        o_one: {
            type: DataTypes.STRING,
            allowNull: false,
            validate: {
                notEmpty: true,
            }
        },
        o_two: {
            type: DataTypes.STRING,
            allowNull: false,
            validate: {
                notEmpty: true,
            }
        },
        o_three: {
            type: DataTypes.STRING,
            allowNull: false,
            validate: {
                notEmpty: true,
            }
        },
        o_four: {
            type: DataTypes.STRING,
            allowNull: false,
            validate: {
                notEmpty: true,
            }
        },
        ans: {
            type: DataTypes.INTEGER,
            allowNull: false,
            validate: {
                notEmpty: true,
            }
        },
    }, {
        underscored: true,
    });

    question.associate = (models) => {
        question.belongsToMany(models.user, { through: models.user_quiz});
    };

    return question;
};
module.exports = {
  "parser": "babel-eslint",
  "parserOptions": {
    "ecmaVersion": 8,
    "ecmaFeatures": {
      "experimentalObjectRestSpread": true,
      "jsx": true,
      "modules": true
    },
    "sourceType": "module"
  },

  "plugins": [
    "prettier"
  ],
  "env": {
    "browser": true,
    "node": true,
    "jasmine": true
  },

  "extends": [
    "airbnb-base",
    "eslint:recommended",
    "prettier",
  ],

  "rules": {
    "semi": [2, "never"],
    "no-console": 0
  },


}

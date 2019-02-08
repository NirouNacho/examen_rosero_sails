/**
 * Pokemon.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {

    numeroPokemon:{
      type: 'number',
      columnType: 'long'
    },
    nombrePokemon: {
      type: 'string'
    },
    poderEspecialUno: {
      type: 'string'
    },
    poderEspecialDos: {
      type: 'string'
    },
    fechaCreacion: {
      type: 'string',
      columnType: 'date'
    },
    nivel: {
      type: 'number',
      columnType: 'int'
    },
    idEntrenador: {
      model: 'Entrenador'
    }

  },

};


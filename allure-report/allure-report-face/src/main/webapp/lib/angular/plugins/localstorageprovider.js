/**
 * loaded from https://github.com/Zmetser/localstorageservice
 */
(function ( angular ) {

  'use strict';

  var TableStorageProvider = function () {

    this.$get = function () {

      var Storage = function ( tableName ) {
        this.$$tableName = tableName;
        this.$$table = getFromLocalStorage( tableName );
      };

      Storage.prototype.addItem =
      Storage.prototype.setItem = function ( itemName, data ) {
        var item, arrayMode;

        arrayMode = angular.isUndefined(data);

        // Kick of array storage on first add.
        if ( arrayMode && isEmpty(this.$$table) ) {
          this.$$table = [];
        }
        else if ( arrayMode && !angular.isArray(this.$$table) ) {
          throw new Error('itemName should be specified in Object Storage mode.');
        }

        if ( arrayMode ) {
          item = itemName;
          this.$$table.push(item);
        }
        else {
          item = this.$$table[itemName] = data;
        }

        addToLocalStorage( this.$$tableName, this.$$table );

        return item;
      };

      Storage.prototype.getItem = function ( itemName ) {
        var item;

        if ( !this.$$table.hasOwnProperty(itemName) ) {
          return null;
        }

        item = this.$$table[itemName];

        return item;
      };

      Storage.prototype.removeItem = function ( itemName ) {
        var item;

        if ( angular.isArray(this.$$table) ){
          item = this._removeFromArray( itemName );
        }
        else {
          item = this._removeFromObject( itemName );
        }

        addToLocalStorage( this.$$tableName, this.$$table );

        return item;
      };

      Storage.prototype._removeFromArray = function ( item ) {
        var index, removedItem;

        if ( !angular.isNumber(item) ) {
          index = getIndex(this.$$table, item);
        }
        else {
          index = item;
        }

        if ( index <= this.$$table.length && index >= 0 ) {
          removedItem = this.$$table[index];

          this.$$table.splice(index, 1);

          return removedItem;
        }

        return null;
      };

      Storage.prototype._removeFromObject = function ( itemName ) {
        var item;

        if ( !this.$$table.hasOwnProperty(itemName) ) {
          return null;
        }

        item = this.$$table[itemName];

        delete this.$$table[itemName];

        return item;
      };

      Storage.prototype.truncate = function () {
        this.$$table = {};
        removeFromLocalStorage( this.$$tableName );
        this.$$tableName = '';
      };

      var $storage = function ( tableName ) {
        return new Storage( tableName );
      };

      return $storage;

    };

  };

  angular.module('localStorageModule', [])
    .provider('$storage', TableStorageProvider);

  // localStorage helpers
  function addToLocalStorage( key, value ) {
    if ( undefined === value ) {
      return false;
    }

    value = JSON.stringify(value);
    localStorage.setItem(key, value);

    return true;
  }

  function getFromLocalStorage( key ) {
    var item = localStorage.getItem(key);

    if ( !item ) {
      return {};
    }

    item = JSON.parse(item);

    return item;
  }

  function removeFromLocalStorage( key ) {
    localStorage.removeItem(key);
  }

  // common helpers
  function isEmpty( value ) {
    if ( angular.isObject(value) ) {
      for ( var key in value ) {
        if ( value.hasOwnProperty(key) ) {
          return false;
        }
      }
      return true;
    }
    return angular.isUndefined(value) || value === '' || value === null || value !== value;
  }

  function getIndex( array, item ) {
    if ( !angular.isArray(array) ) { return false; }
    if ( !angular.isObject(item) ) {
      return array.indexOf(item);
    }
    else {
      for (var i = 0; i < array.length; i++) {
        if (angular.equals(array[i], item)) {
          return i;
        }
      }
      return -1;
    }
  }

})( angular );

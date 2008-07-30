var $dhtml = true;
var $as3 = false;
var $js1 = true;
var $swf9 = false;
var $swf7 = false;
var $profile = false;
var $swf8 = false;
var $runtime = "dhtml";
var $svg = false;
var $as2 = false;
var $debug = true;
var $j2me = false;
var _Copyright = "Portions of this file are copyright (c) 2001-2007 by Laszlo Systems, Inc.  All rights reserved.";
Function.prototype.make = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#16.27 -*- */
function  () {
var $1_xtor;
$1_xtor = 
/* -*- file: compiler/LzRuntime.lzs#22.5 -*- */
function xtor () {

}
if (arguments.length > 0) {
$1_xtor.prototype = this.prototype;
var $2_o = new $1_xtor();
this.apply($2_o, arguments);
return $2_o
} else {
return new this()
}
}
$lzsc$temp._dbg_name = "Function.prototype.make";
return $lzsc$temp
})();
Object.make = Object;
Array.make = Array;
Function.make = Function;
String.make = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#57.15 -*- */
function  ($1_value) {
switch (arguments.length) {
case 0:
$1_value = "";

}
return new String($1_value)
}
$lzsc$temp._dbg_name = "String.make";
return $lzsc$temp
})();
Boolean.make = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#68.16 -*- */
function  ($1_value) {
return new Boolean($1_value)
}
$lzsc$temp._dbg_name = "Boolean.make";
return $lzsc$temp
})();
Number.make = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#75.15 -*- */
function  ($1_value) {
switch (arguments.length) {
case 0:
$1_value = +0;

}
return new Number($1_value)
}
$lzsc$temp._dbg_name = "Number.make";
return $lzsc$temp
})();
Date.make = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#86.13 -*- */
function  ($1_year, $2_month, $3_date, $4_hours, $5_minutes, $6_seconds, $7_ms) {
switch (arguments.length) {
case 2:
$3_date = 1;
case 3:
$4_hours = 0;
case 4:
$5_minutes = 0;
case 5:
$6_seconds = 0;
case 6:
$7_ms = 0;

}
return new Date($1_year, $2_month, $3_date, $4_hours, $5_minutes, $6_seconds, $7_ms)
}
$lzsc$temp._dbg_name = "Date.make";
return $lzsc$temp
})();
window._root = window;
var $modules = {}
$modules.runtime = this;
$modules.lz = $modules.runtime;
$modules.user = $modules.lz;
var global = $modules.user;
window.parent.$modules = $modules;
var _dbg_log_all_writes;
_dbg_log_all_writes = "console" in global && typeof console.log == "function";
var Debug;
var Debug = new Object();
Debug.backtraceStack = new Array();
Debug.backtraceStack.maxDepth = 100;
Debug.addText = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#206.19 -*- */
function  ($1_msg) {
try {
var $2_dw = window.parent.frames["LaszloDebugger"];
var $3_dwd = $2_dw.document;
var $4_span = $3_dwd.createElement("span");
$4_span.innerHTML = '<span style="white-space: pre">' + $1_msg.split("\n").join("<br />") + "</span>";
$3_dwd.body.appendChild($4_span);
$4_span.scrollIntoView(false)
}
catch (e) {
try {
if (print.length > 0) {
print($1_msg);
return
}
}
catch (e) {

}
}
}
$lzsc$temp._dbg_name = "Debug.addText";
return $lzsc$temp
})();
Debug.log = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#239.15 -*- */
function  ($1_msg) {
if ("console" in global && typeof console.log == "function") {
console.log("" + $1_msg)
}
}
$lzsc$temp._dbg_name = "Debug.log";
return $lzsc$temp
})();
Debug.__write = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#268.19 -*- */
function  ($1_msg) {
if (_dbg_log_all_writes) {
this.log($1_msg)
}
this.addText($1_msg)
}
$lzsc$temp._dbg_name = "Debug.__write";
return $lzsc$temp
})();
Debug.debug = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#280.17 -*- */
function  () {
this.__write("DEBUG: " + Array.prototype.slice.call(arguments, 0).join(" "))
}
$lzsc$temp._dbg_name = "Debug.debug";
return $lzsc$temp
})();
Debug.info = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#288.16 -*- */
function  () {
this.__write("INFO: " + Array.prototype.slice.call(arguments, 0).join(" "))
}
$lzsc$temp._dbg_name = "Debug.info";
return $lzsc$temp
})();
Debug.warn = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#296.16 -*- */
function  () {
this.__write("WARNING: " + Array.prototype.slice.call(arguments, 0).join(" "))
}
$lzsc$temp._dbg_name = "Debug.warn";
return $lzsc$temp
})();
Debug.error = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#304.17 -*- */
function  () {
this.__write("ERROR: " + Array.prototype.slice.call(arguments, 0).join(" "))
}
$lzsc$temp._dbg_name = "Debug.error";
return $lzsc$temp
})();

/* -*- file: compiler/LzRuntime.lzs#311.3 -*- */
function $reportSourceWarning ($1_filename, $2_lineNumber, $3_msg, $4_fatal) {
var $5_warning = ($4_fatal ? "ERROR:  " : "WARNING: ") + $1_filename + ":" + $2_lineNumber + ": " + $3_msg + "\n";
Debug.__write($5_warning)
}
Debug.debug("Source warnings enabled");
$reportSourceWarning("testing", 0, "Test source warning");

/* -*- file: compiler/LzRuntime.lzs#327.3 -*- */
function $reportUndefinedObjectProperty ($1_filename, $2_lineNumber, $3_propertyName) {
if (!arguments.callee._dbg_recursive_call) {
arguments.callee._dbg_recursive_call = true;
$reportSourceWarning($1_filename, $2_lineNumber, "undefined object does not have a property '" + $3_propertyName + "'", true);
arguments.callee._dbg_recursive_call = false
}
}
$reportUndefinedObjectProperty._dbg_recursive_call = false;

/* -*- file: compiler/LzRuntime.lzs#338.3 -*- */
function $reportUndefinedProperty ($1_filename, $2_lineNumber, $3_propertyName) {
if (!arguments.callee._dbg_recursive_call) {
arguments.callee._dbg_recursive_call = true;
$reportSourceWarning($1_filename, $2_lineNumber, "reference to undefined property '" + $3_propertyName + "'", false);
arguments.callee._dbg_recursive_call = false
}
}
$reportUndefinedProperty._dbg_recursive_call = false;

/* -*- file: compiler/LzRuntime.lzs#349.3 -*- */
function $reportUndefinedVariable ($1_filename, $2_lineNumber, $3_variableName) {
if (!arguments.callee._dbg_recursive_call) {
arguments.callee._dbg_recursive_call = true;
$reportSourceWarning($1_filename, $2_lineNumber, "reference to undefined variable '" + $3_variableName + "'", true);
arguments.callee._dbg_recursive_call = false
}
}
$reportUndefinedVariable._dbg_recursive_call = false;

/* -*- file: compiler/LzRuntime.lzs#360.3 -*- */
function $reportNotFunction ($1_filename, $2_lineNumber, $3_name, $4_value) {
if (!arguments.callee._dbg_recursive_call) {
arguments.callee._dbg_recursive_call = true;
var $5_msg = "call to non-function";
if (typeof $3_name == "string") {
$5_msg += " '" + $3_name + "'"
}
$5_msg += " (type '" + typeof $4_value + "')";
if (typeof $4_value == "undefined") {
$5_msg = "call to undefined function";
if (typeof $3_name == "string") {
$5_msg += " '" + $3_name + "'"
}
}
$reportSourceWarning($1_filename, $2_lineNumber, $5_msg, true);
arguments.callee._dbg_recursive_call = false
}
}
$reportNotFunction._dbg_recursive_call = false;

/* -*- file: compiler/LzRuntime.lzs#380.3 -*- */
function $reportUndefinedMethod ($1_filename, $2_lineNumber, $3_name, $4_value) {
if (!arguments.callee._dbg_recursive_call) {
arguments.callee._dbg_recursive_call = true;
var $5_msg = "call to non-method";
if (typeof $3_name == "string") {
$5_msg += " '" + $3_name + "'"
}
$5_msg += " (type '" + typeof $4_value + "')";
if (typeof $4_value == "undefined") {
$5_msg = "call to undefined method";
if (typeof $3_name == "string") {
$5_msg += " '" + $3_name + "'"
}
}
$reportSourceWarning($1_filename, $2_lineNumber, $5_msg, true);
arguments.callee._dbg_recursive_call = false
}
}
$reportUndefinedMethod._dbg_recursive_call = false;
Debug.evalCarefully = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#411.27 -*- */
function  ($1_fileName, $2_lineNumber, $3_closure, $4_context) {
try {
return $3_closure.call($4_context)
}
catch (e) {
$reportSourceWarning($1_fileName, $2_lineNumber, e)
}
}
$lzsc$temp._dbg_name = "Debug.evalCarefully";
return $lzsc$temp
})();
if (typeof window.addEventListener == "function") {
window.onerror = (function  () {
var $lzsc$temp = 
/* -*- file: compiler/LzRuntime.lzs#426.24 -*- */
function  ($1_errorString, $2_fileName, $3_lineNo) {
$reportSourceWarning($2_fileName, $3_lineNo, $1_errorString, true);
return false
}
$lzsc$temp._dbg_name = "window.onerror";
return $lzsc$temp
})()
}
var Instance = 
/* -*- file: compiler/Class.lzs#27.16 -*- */
function constructor () {
this.constructor = arguments.callee;
this.initialize.apply(this, arguments)
}
Instance.prototype.constructor = Instance;
Instance.prototype.__initialized = false;
Instance.classname = "Instance";
Instance.prototype.classname = "Object";
Instance._dbg_typename = "Class";
Instance._dbg_name = Instance.prototype._dbg_typename = "Instance";
(function  () {
var $lzsc$temp = 
/* -*- file: compiler/Class.lzs#52.2 -*- */
function  () {
var $1_addProperty = 
/* -*- file: compiler/Class.lzs#53.21 -*- */
function AddProperty ($1_name, $2_value) {
this[$1_name] = $2_value;
if ($2_value instanceof Function) {
var $3_xtor = this.constructor;
if ($2_value.hasOwnProperty("superclasses")) {
var $4_os = $2_value.superclasses;
var $5_found = false;
for (var $6_i = $4_os.length - 1;
$6_i >= 0;
$6_i--) {
if ($4_os[$6_i] === $3_xtor) {
$5_found = true;
break
}
}
if (!$5_found) {
$2_value.superclasses.push($3_xtor)
}
} else {
if ($2_value.hasOwnProperty("superclass") && $2_value.superclass !== $3_xtor) {
var $7_superclass = $2_value.superclass;
delete $2_value.superclass;
$2_value.superclasses = [$7_superclass, $3_xtor]
} else {
$2_value.superclass = $3_xtor
}
}
if (!$2_value._dbg_typename) {
$2_value._dbg_owner = this;
$2_value._dbg_typename = 
/* -*- file: compiler/Class.lzs#80.33 -*- */
function _dbg_typename () {
var $1_o = this._dbg_owner;
var $2_t = $1_o._dbg_typename;
if (!$1_o.hasOwnProperty("_dbg_typename")) {
$2_t += "#" + Debug.IDForObject($1_o)
}
return $2_t + " function"
}
}
}
}
$1_addProperty.call(Instance.prototype, "addProperty", $1_addProperty)
}
$lzsc$temp._dbg_name = "compiler/Class.lzs#52/2";
return $lzsc$temp
})()();
Instance.prototype.addProperty("nextMethod", 
/* -*- file: compiler/Class.lzs#114.46 -*- */
function nextMethod ($1_currentMethod, $2_nextMethodName) {
var $3_next;
if ($1_currentMethod.hasOwnProperty("superclass")) {
$3_next = $1_currentMethod.superclass.prototype[$2_nextMethodName]
} else {
var $4_superclasses = $1_currentMethod.superclasses;
for (var $5_i = $4_superclasses.length - 1;
$5_i >= 0;
$5_i--) {
if (this instanceof $4_superclasses[$5_i]) {
$3_next = $4_superclasses[$5_i].prototype[$2_nextMethodName];
break
}
}
}
if (!$3_next) {
Debug.error("No next method %s in %w", $2_nextMethodName, $1_currentMethod)
}
return $3_next
});
Instance.prototype.addProperty("initialize", 
/* -*- file: compiler/Class.lzs#151.46 -*- */
function initialize () {

});
Instance.initialize = 
/* -*- file: compiler/Class.lzs#168.23 -*- */
function initialize ($1_prototype) {
$1_prototype.__initialized = false
}
Instance.initialize._dbg_typename = "Instance static function";
Instance.make = 
/* -*- file: compiler/Class.lzs#188.17 -*- */
function make () {
var $1_xtor;
$1_xtor = 
/* -*- file: compiler/Class.lzs#200.7 -*- */
function xtor () {
this.constructor = constructor
}
Debug.info("`%w.%s` is deprecated.  Use `new %w` instead.", this, arguments.callee, this);
if (arguments.length > 0) {
var constructor = this;
$1_xtor.prototype = constructor.prototype;
var $2_o = new $1_xtor();
constructor.apply($2_o, arguments);
return $2_o
}
return new this()
}
Instance.make._dbg_typename = "Instance static function";
Instance.prototype.addProperty("validateClassStructure", 
/* -*- file: compiler/Class.lzs#215.60 -*- */
function validateClassStructure () {
var $1_prototype = this.constructor.prototype;
do{
var $2_constructor = $1_prototype.constructor;
if (!$1_prototype.__initialized) {
for (var $3_k in $1_prototype) {
if ($1_prototype.hasOwnProperty($3_k) && $3_k != "constructor") {
var $4_value = $1_prototype[$3_k];
if ($4_value instanceof Function) {
if ($4_value.hasOwnProperty("superclasses")) {
var $5_os = $4_value.superclasses;
var $6_found = false;
for (var $7_i = $5_os.length - 1;
$7_i >= 0;
$7_i--) {
if ($5_os[$7_i] === $2_constructor) {
$6_found = true;
break
}
}
if (!$6_found) {
$4_value.superclasses.push($2_constructor);
Debug.error("Invalid class structure [3+]: Use `addProperty` to create %w.%s", this, $3_k)
}
} else {
if ($4_value.hasOwnProperty("superclass")) {
var $8_o = $4_value.superclass;
if ($8_o !== $2_constructor) {
delete $4_value.superclass;
$4_value.superclasses = [$8_o, $2_constructor];
Debug.error("Invalid class structure [2]: Use `addProperty` to create %w.%s", this, $3_k)
}
} else {
$4_value.superclass = $2_constructor;
Debug.error("Invalid class structure [1]: Use `addProperty` to create %w.%s", this, $3_k)
}
}
}
}
}
$1_prototype.__initialized = true
}
$1_prototype = $2_constructor.prototype
} while ($2_constructor !== Instance)
});
var Class = {prototype: new Instance(), addProperty: 
/* -*- file: compiler/Class.lzs#268.16 -*- */
function addProperty ($1_name, $2_value) {
var $3_proto = this.prototype;
$3_proto.addProperty.apply($3_proto, arguments)
}, addStaticProperty: 
/* -*- file: compiler/Class.lzs#286.22 -*- */
function addStaticProperty ($1_name, $2_value) {
this[$1_name] = $2_value;
if ($2_value instanceof Function && !$2_value._dbg_typename) {
$2_value._dbg_owner = this;
$2_value._dbg_typename = 
/* -*- file: compiler/Class.lzs#301.31 -*- */
function _dbg_typename () {
return this._dbg_owner._dbg_name + " static function"
}
}
}, _dbg_name: "Class", allClasses: {Instance: Instance}, make: 
/* -*- file: compiler/Class.lzs#313.9 -*- */
function make ($1_classname, $2_traitsAndSuperclass, $3_instanceProperties, $4_staticProperties) {
var nc = 
/* -*- file: compiler/Class.lzs#315.14 -*- */
function constructor () {
this.constructor = arguments.callee;
this.validateClassStructure();
if (this.initialize !== Instance.prototype.initialize) {
this.initialize.apply(this, arguments)
}
}
nc.constructor = this;
nc.classname = $1_classname;
nc._dbg_typename = this._dbg_name;
nc._dbg_name = $1_classname;
this.addStaticProperty.call(nc, "addStaticProperty", this.addStaticProperty);
nc.addStaticProperty("addProperty", this.addProperty);
var superclass = null;
if ($2_traitsAndSuperclass instanceof Array) {
for (var $5_k = $2_traitsAndSuperclass.length - 1;
$5_k >= 0;
$5_k--) {
var $6_c = $2_traitsAndSuperclass[$5_k];
if ($6_c instanceof Function) {
if (superclass) {
Debug.error("Class.make: Multiple superclasses %s and %s for class %s", superclass, $6_c, $1_classname)
}
$2_traitsAndSuperclass.splice($5_k, 1);
superclass = $6_c
}
}
} else {
if ($2_traitsAndSuperclass instanceof Function) {
superclass = $2_traitsAndSuperclass;
$2_traitsAndSuperclass = null
} else {
if ($2_traitsAndSuperclass) {
Debug.error("Class.make: invalid superclass %w", $2_traitsAndSuperclass)
}
}
}
if (!superclass) {
superclass = Instance
}
nc.addStaticProperty("make", superclass.make);
var $7_xtor = 
/* -*- file: compiler/Class.lzs#368.18 -*- */
function prototype () {
this.constructor = superclass
}
$7_xtor.prototype = superclass.prototype;
var $8_prototype = new $7_xtor();
if ($2_traitsAndSuperclass instanceof Array) {
while ($2_traitsAndSuperclass.length) {
var $9_t = $2_traitsAndSuperclass.pop();
$8_prototype = $9_t.makeInterstitial($8_prototype, $2_traitsAndSuperclass.length > 0)
}
}
for (var $10_name in $4_staticProperties) {
nc.addStaticProperty($10_name, $4_staticProperties[$10_name])
}
nc.prototype = $8_prototype;
$8_prototype._dbg_typename = $1_classname;
for (var $11_p in $3_instanceProperties) {
nc.addProperty($11_p, $3_instanceProperties[$11_p])
}
(
/* -*- file: compiler/Class.lzs#397.6 -*- */
function classInit ($1_prototype, $2_constructor) {
if ($2_constructor !== Instance) {
arguments.callee($1_prototype, $2_constructor.prototype.constructor)
}
if ($2_constructor.hasOwnProperty("initialize")) {
$2_constructor.initialize.call(nc, $1_prototype)
}
})($8_prototype, nc);
this.allClasses[$1_classname] = nc;
return nc
}}
Class.addProperty._dbg_typename = "Class static function";
Class.addStaticProperty._dbg_typename = "Class static function";
Class.make._dbg_typename = "Class static function";
var Trait = {prototype: new Instance(), allTraits: {}, _dbg_typename: Class._dbg_name, _dbg_name: "Trait", addProperty: 
/* -*- file: compiler/Class.lzs#426.16 -*- */
function addProperty ($1_name, $2_value) {
this.prototype[$1_name] = $2_value;
var $3_impls = this.implementations;
for (var $4_mash in $3_impls) {
var $5_t = $3_impls[$4_mash];
$5_t.addProperty.apply($5_t, arguments)
}
if ($2_value instanceof Function && !$2_value._dbg_typename) {
$2_value._dbg_typename = this._dbg_name + " function"
}
}, addStaticProperty: 
/* -*- file: compiler/Class.lzs#441.22 -*- */
function addStaticProperty ($1_name, $2_value) {
this[$1_name] = $2_value;
if ($2_value instanceof Function && !$2_value._dbg_typename) {
$2_value._dbg_typename = this._dbg_name + " static function"
}
}, makeInterstitial: 
/* -*- file: compiler/Class.lzs#453.21 -*- */
function makeInterstitial ($1_superclassInstance, $2_sharable) {
var $3_impls = this.implementations;
var $4_mash = this.classname + "+" + $1_superclassInstance.constructor.classname;
if (false) {
if ($3_impls.hasOwnProperty($4_mash)) {
return $3_impls[$4_mash]
}
}
var $5_prototype = this.prototype;
for (var $6_name in $5_prototype) {
$1_superclassInstance.addProperty.call($1_superclassInstance, $6_name, $5_prototype[$6_name])
}
var $7_xtor = 
/* -*- file: compiler/Class.lzs#479.16 -*- */
function interstitial () {
this.constructor = arguments.callee
}
$7_xtor.prototype = $1_superclassInstance;
if (this.hasOwnProperty("initialize")) {
$7_xtor.initialize = this.initialize
}
$7_xtor.classname = $4_mash;
$7_xtor._dbg_typename = "Interstitial Constructor";
$7_xtor._dbg_name = $7_xtor.classname;
var $8_t = new $7_xtor();
$3_impls[$4_mash] = $8_t;
return $8_t
}, make: 
/* -*- file: compiler/Class.lzs#502.9 -*- */
function make ($1_classname, $2_superTrait, $3_instanceProperties, $4_staticProperties) {
var $5_nt = {constructor: this, classname: $1_classname, _dbg_typename: this._dbg_name, _dbg_name: $1_classname, prototype: $2_superTrait ? $2_superTrait.make() : new Object(), implementations: {}}
this.addStaticProperty.call($5_nt, "addStaticProperty", this.addStaticProperty);
$5_nt.addStaticProperty("addProperty", this.addProperty);
$5_nt.addStaticProperty("makeInterstitial", this.makeInterstitial);
for (var $6_p in $4_staticProperties) {
$5_nt.addStaticProperty($6_p, $4_staticProperties[$6_p])
}
for (var $6_p in $3_instanceProperties) {
$5_nt.addProperty($6_p, $3_instanceProperties[$6_p])
}
if ($5_nt.hasOwnProperty("initialize")) {
$5_nt.initialize($5_nt.prototype)
}
this.allTraits[$1_classname] = $5_nt;
return $5_nt
}}
Trait.addStaticProperty._dbg_typename = "Trait static function";
Trait.addProperty._dbg_typename = "Trait static function";
Trait.makeInterstitial._dbg_typename = "Trait static function";
Trait.make._dbg_typename = "Trait static function";
var LzMessage = Class.make("LzMessage", null, {message: "", appendInternal: 
/* -*- file: compiler/LzFormatter.lzs#27.3 -*- */
function appendInternal ($1_str, $2_obj) {
this.message += $1_str
}, append: 
/* -*- file: compiler/LzFormatter.lzs#31.3 -*- */
function append ($1_str) {
var $2_len = arguments.length;
for (var $3_i = 0;
$3_i < $2_len;
$3_i++) {
this.appendInternal(String(arguments[$3_i]))
}
}, toString: 
/* -*- file: compiler/LzFormatter.lzs#38.3 -*- */
function toString () {
return this.message
}, toHTML: 
/* -*- file: compiler/LzFormatter.lzs#42.3 -*- */
function toHTML () {
return this.toString()
}}, {initialize: 
/* -*- file: compiler/LzFormatter.lzs#23.10 -*- */
function initialize ($1_message) {
this.message = $1_message
}});
var LzFormatter = Trait.make("LzFormatter", null, {pad: 
/* -*- file: compiler/LzFormatter.lzs#65.3 -*- */
function pad ($1_value, $2_widthMin, $3_decMax, $4_pad, $5_sign, $6_radix, $7_force) {
switch (arguments.length) {
case 0:
$1_value = "";
case 1:
$2_widthMin = null;
case 2:
$3_decMax = null;
case 3:
$4_pad = " ";
case 4:
$5_sign = "-";
case 5:
$6_radix = 10;
case 6:
$7_force = false;

}
var $8_isNumber = typeof $1_value == "number";
if ($8_isNumber) {
if ($3_decMax != null) {
var $9_precision = Math.pow(10, -$3_decMax);
$1_value = Math.round($1_value / $9_precision) * $9_precision
}
$1_value = Number($1_value).toString($6_radix);
if ($5_sign != "-") {
if ($1_value.indexOf("-") != 0) {
if ($1_value != 0) {
$1_value = $5_sign + $1_value
} else {
$1_value = " " + $1_value
}
}
}
} else {
$1_value = "" + $1_value
}
var $10_strlen = $1_value.length;
if ($3_decMax != null) {
if ($8_isNumber) {
var $11_decimal = $1_value.lastIndexOf(".");
if ($11_decimal == -1) {
var $12_decimals = 0;
if ($7_force || $3_decMax > 0) {
$1_value += "."
}
} else {
var $12_decimals = $10_strlen - ($11_decimal + 1)
}
for (var $13_i = $12_decimals;
$13_i < $3_decMax;
$13_i++) {
$1_value += "0"
}
} else {
$1_value = $1_value.substring(0, $3_decMax)
}
}
$10_strlen = $1_value.length;
if (!$2_widthMin) {
$2_widthMin = 0
}
var $14_leftJustify = false;
if ($2_widthMin < 0) {
$2_widthMin = -$2_widthMin;
$14_leftJustify = true
}
if ($10_strlen >= $2_widthMin) {
return $1_value
}
if ($14_leftJustify) {
for (var $13_i = $10_strlen;
$13_i < $2_widthMin;
$13_i++) {
$1_value = $1_value + " "
}
} else {
$5_sign = null;
if ($4_pad != " ") {
if (" +-".indexOf($1_value.substring(0, 1)) >= 0) {
$5_sign = $1_value.substring(0, 1);
$1_value = $1_value.substring(1)
}
}
for (var $13_i = $10_strlen;
$13_i < $2_widthMin;
$13_i++) {
$1_value = $4_pad + $1_value
}
if ($5_sign != null) {
$1_value = $5_sign + $1_value
}
}
return $1_value
}, formatToString: 
/* -*- file: compiler/LzFormatter.lzs#187.3 -*- */
function formatToString (control, $1_args) {
var $8_getarg;
$8_getarg = 
/* -*- file: compiler/LzFormatter.lzs#211.5 -*- */
function getarg ($1_i) {
if ($1_i >= al) {
Debug.warn("%#0.48w: insufficient arguments", control);
return null
}
return arglist[$1_i]
}
var al = arguments.length;
if (!(typeof control == "string" || control instanceof String) || al > 1 != control.indexOf("%") >= 0) {
var $2_out = new LzMessage();
for (var $3_i = 0;
$3_i < al;
$3_i++) {
var $4_arg = arguments[$3_i];
var $5_sep = $3_i == al - 1 ? "\n" : " ";
$2_out.append($4_arg);
$2_out.appendInternal($5_sep)
}
return $2_out
}
if (al < 1) {
control = ""
}
var $6_ctrl = "" + control;
var $7_argno = 1;
var arglist = arguments;
var $9_base = 0;
var $10_limit = $6_ctrl.length;
var $11_start = 0;
var $12_end = 0;
var $2_out = new LzMessage();
while ($11_start < $10_limit) {
$12_end = $6_ctrl.indexOf("%");
if ($12_end == -1) {
$2_out.append($6_ctrl.substring($11_start, $10_limit));
break
}
$2_out.append($6_ctrl.substring($11_start, $12_end));
$9_base = $12_end;
$11_start = $12_end + 1;
$12_end = $12_end + 2;
var $13_sign = "-";
var $14_pad = " ";
var $15_alternate = false;
var $16_length = "";
var $17_precision = null;
var $18_directive = null;
while ($11_start < $10_limit && $18_directive == null) {
var $19_char = $6_ctrl.substring($11_start, $12_end);
$11_start = $12_end++;
switch ($19_char) {
case "-":
$16_length = $19_char;
break;
case "+":
case " ":
$13_sign = $19_char;
break;
case "#":
$15_alternate = true;
break;
case "0":
if ($16_length === "" && $17_precision === null) {
$14_pad = $19_char;
break
};
case "1":
case "2":
case "3":
case "4":
case "5":
case "6":
case "7":
case "8":
case "9":
if ($17_precision !== null) {
$17_precision += $19_char
} else {
$16_length += $19_char
}
break;
case "$":
$7_argno = $16_length;
$16_length = "";
break;
case "*":
if ($17_precision !== null) {
$17_precision = $8_getarg($7_argno);
$7_argno++
} else {
$16_length = $8_getarg($7_argno);
$7_argno++
}
break;
case ".":
$17_precision = "";
break;
case "h":
case "l":
break;
default:
$18_directive = $19_char;
break;

}
}
var $20_value = $8_getarg($7_argno);
var $21_decimals = null;
var $22_force = false;
if ($17_precision !== null) {
$21_decimals = 1 * $17_precision
} else {
switch ($18_directive) {
case "F":
case "E":
case "G":
case "f":
case "e":
case "g":
$21_decimals = 6;
$22_force = $15_alternate;
break;
case "O":
case "o":
if ($15_alternate && $20_value != 0) {
$2_out.append("0")
}
break;
case "X":
case "x":
if ($15_alternate && $20_value != 0) {
$2_out.append("0" + $18_directive)
}
break;

}
}
var $23_radix = 10;
switch ($18_directive) {
case "o":
case "O":
$23_radix = 8;
break;
case "x":
case "X":
$23_radix = 16;
break;

}
switch ($18_directive) {
case "U":
case "O":
case "X":
case "u":
case "o":
case "x":
if ($20_value < 0) {
$20_value = -$20_value;
var $24_wid = Math.abs(parseInt($16_length));
if (isNaN($24_wid)) {
$24_wid = Number($20_value).toString($23_radix).length
}
var $25_max = Math.pow($23_radix, $24_wid);
$20_value = $25_max - $20_value
};

}
switch ($18_directive) {
case "D":
case "U":
case "I":
case "O":
case "X":
case "F":
case "E":
case "G":
$20_value = Number($20_value);
$2_out.append(this.pad($20_value, $16_length, $21_decimals, $14_pad, $13_sign, $23_radix, $22_force).toUpperCase());
$7_argno++;
break;
case "c":
$20_value = String.fromCharCode($20_value);
case "w":
var $26_width = $21_decimals || Debug.printLength;
$2_out.appendInternal(this.pad(Debug.__String($20_value, true, $26_width, $15_alternate), $16_length, null, $14_pad, $13_sign, $23_radix, $22_force), $20_value);
$7_argno++;
break;
case "s":
var $27_str;
if ($20_value instanceof Function) {
$27_str = this.functionName($20_value);
if (!$27_str) {
$27_str = "function () {...}"
}
} else {
if (typeof $20_value == "number") {
$27_str = Number($20_value).toString($23_radix)
} else {
$27_str = "" + $20_value
}
}
$2_out.appendInternal(this.pad($27_str, $16_length, $21_decimals, $14_pad, $13_sign, $23_radix, $22_force), $20_value);
$7_argno++;
break;
case "d":
case "u":
case "i":
case "o":
case "x":
case "f":
case "e":
case "g":
$20_value = Number($20_value);
$2_out.append(this.pad($20_value, $16_length, $21_decimals, $14_pad, $13_sign, $23_radix, $22_force));
$7_argno++;
break;
case "%":
$2_out.append("%");
break;
default:
$2_out.append($6_ctrl.substring($9_base, $11_start));
break;

}
$6_ctrl = $6_ctrl.substring($11_start, $10_limit);
$9_base = 0, $10_limit = $6_ctrl.length;
$11_start = 0, $12_end = 0
}
if ($7_argno < al) {
Debug.warn("%#0.48w: excess arguments", control);
$2_out.appendInternal(" ");
for (;
$7_argno < al;
$7_argno++) {
var $4_arg = $8_getarg($7_argno);
var $5_sep = $7_argno == al - 1 ? "\n" : " ";
$2_out.append($4_arg);
$2_out.appendInternal($5_sep)
}
}
return $2_out
}}, null);
var __LzDebug = Debug;
Debug._dbg_name = "Debug";
global._dbg_name = "global";
Debug.printLength = 1024;
Debug.printPretty = true;
Debug.messageLevels = {ALL: 0, DEBUG: 1, INFO: 2, WARNING: 3, ERROR: 4, NONE: 5}
Debug.messageLevel = "ALL";
Debug.internalPropertyPrefixes = ["$", "__"];
Debug.showInternalProperties = false;
Debug.versionInfo = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#79.21 -*- */
function  () {
this.write(LzCanvas.versionInfoString())
}
$lzsc$temp._dbg_name = "Debug.versionInfo";
return $lzsc$temp
})();
Debug.inspect = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#95.17 -*- */
function  ($1_obj, $2_reserved) {
var $3_msg = this.inspectInternal($1_obj, $2_reserved);
this.freshLine();
this.addHTMLText($3_msg);
return $1_obj
}
$lzsc$temp._dbg_name = "Debug.inspect";
return $lzsc$temp
})();
Debug.inspect.printLength = 256;
Debug.__typeof = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#142.18 -*- */
function  ($1_thing) {
return typeof $1_thing
}
$lzsc$temp._dbg_name = "Debug.__typeof";
return $lzsc$temp
})();
Debug.functionName = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#151.22 -*- */
function  ($1_fn, $2_isGlobal) {
return String($1_fn)
}
$lzsc$temp._dbg_name = "Debug.functionName";
return $lzsc$temp
})();
Debug.__String = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#176.20 -*- */
function  ($1_thing, $2_pretty, $3_limit, $4_unique) {
return String($1_thing)
}
$lzsc$temp._dbg_name = "Debug.__String";
return $lzsc$temp
})();
Debug.makeObjectLink = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#183.24 -*- */
function  ($1_obj, $2_id) {
return $1_obj
}
$lzsc$temp._dbg_name = "Debug.makeObjectLink";
return $lzsc$temp
})();
Debug.inspectInternal = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#194.25 -*- */
function  ($1_obj, $2_showInternalProperties) {
return this.__String($1_obj)
}
$lzsc$temp._dbg_name = "Debug.inspectInternal";
return $lzsc$temp
})();
Debug.xmlEscape = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#204.19 -*- */
function  ($1_ts) {
if ($1_ts && (typeof $1_ts == "string" || $1_ts instanceof String)) {
var $2_outstr = "";
var $3_tlen = $1_ts.length;
for (var $4_i = 0;
$4_i < $3_tlen;
$4_i++) {
var $5_c = $1_ts.charAt($4_i);
if ($5_c == "<") {
$2_outstr += "&lt;"
} else {
if ($5_c == ">") {
$2_outstr += "&gt;"
} else {
if ($5_c == "&") {
$2_outstr += "&amp;"
} else {
$2_outstr += $5_c
}
}
}
}
return $2_outstr
} else {
return $1_ts
}
}
$lzsc$temp._dbg_name = "Debug.xmlEscape";
return $lzsc$temp
})();
Debug.internalProperty = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#231.26 -*- */
function  ($1_str) {
var $2_ipp = this.internalPropertyPrefixes;
for (var $3_key in $2_ipp) {
if ($1_str.indexOf($2_ipp[$3_key]) == 0) {
return true
}
}
return false
}
$lzsc$temp._dbg_name = "Debug.internalProperty";
return $lzsc$temp
})();
Debug.singleEscapeCharacters = {"\\b": "\b", "\\t": "\t", "\\n": "\n", "\\v": "\v", "\\f": "\f", "\\r": "\r", '\\"': '"', "\\'": "'", "\\\\": "\\"}
Debug.abbreviate = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#267.20 -*- */
function  ($1_s, $2_l) {
if (arguments.length < 2) {
$2_l = this.printLength
}
var $3_ellipsis = "...";
if ($1_s.length > $2_l - $3_ellipsis.length) {
$1_s = $1_s.substring(0, $2_l - $3_ellipsis.length) + $3_ellipsis
}
return $1_s
}
$lzsc$temp._dbg_name = "Debug.abbreviate";
return $lzsc$temp
})();
Debug.stringEscape = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#286.22 -*- */
function  ($1_s, $2_quoted) {
$1_s = $1_s.split("\\").join("\\\\");
var $3_np = this.singleEscapeCharacters;
var $4_skip = '"';
var $5_quote = "";
var $6_ignore = "'";
if ($2_quoted) {
$6_ignore = "";
var $7_singles = $1_s.split("'").length;
var $8_doubles = $1_s.split('"').length;
if ($7_singles > $8_doubles) {
$4_skip = "'";
$5_quote = '"'
} else {
$4_skip = '"';
$5_quote = "'"
}
}
for (var $9_rep in $3_np) {
var $10_ch = $3_np[$9_rep];
if ($10_ch != "\\" && $10_ch != $4_skip && $10_ch != $6_ignore) {
$1_s = $1_s.split($10_ch).join($9_rep)
}
}
return $5_quote + $1_s + $5_quote
}
$lzsc$temp._dbg_name = "Debug.stringEscape";
return $lzsc$temp
})();
Debug.objseq = 0;
Debug.id_to_object_table = [];
Debug.IDForObject = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#343.21 -*- */
function  ($1_obj, $2_force) {
var $3_id;
for ($3_id = 0;
$3_id < this.id_to_object_table.length;
$3_id++) {
if (this.id_to_object_table[$3_id] === $1_obj) {
return $3_id
}
}
if (!$2_force) {
if (!($1_obj && (typeof $1_obj == "object" || $1_obj instanceof Object))) {
return null
}
}
$3_id = this.objseq++;
this.id_to_object_table[$3_id] = $1_obj;
return $3_id
}
$lzsc$temp._dbg_name = "Debug.IDForObject";
return $lzsc$temp
})();
Debug.ObjectForID = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzDebug.lzs#367.21 -*- */
function  ($1_id) {
return this.id_to_object_table[$1_id]
}
$lzsc$temp._dbg_name = "Debug.ObjectForID";
return $lzsc$temp
})();
Debug.atFreshLine = true;
Debug.atPrompt = false;
Debug.freshLine = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzFormat.lzs#21.19 -*- */
function  () {
if (!this.atFreshLine) {
this.addText("\n");
this.atFreshLine = true
}
}
$lzsc$temp._dbg_name = "Debug.freshLine";
return $lzsc$temp
})();
Debug.freshPrompt = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzFormat.lzs#31.21 -*- */
function  () {
if (!this.atPrompt) {
this.freshLine();
this.addHTMLText('<span class="DEBUG">lzx&gt; </span>');
this.atPrompt = true
}
}
$lzsc$temp._dbg_name = "Debug.freshPrompt";
return $lzsc$temp
})();
Debug.__write = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzFormat.lzs#45.17 -*- */
function  ($1_msg) {
if (global["_dbg_log_all_writes"]) {
this.log($1_msg)
}
var $2_str = String($1_msg);
this.atFreshLine = $2_str.charAt($2_str.length - 1) == "\n";
if ($2_str.length) {
this.atPrompt = false
}
this.addText($1_msg)
}
$lzsc$temp._dbg_name = "Debug.__write";
return $lzsc$temp
})();
Debug.writeInternal = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzFormat.lzs#65.23 -*- */
function  ($1_objects) {
var $2_msg = this.formatToString.apply(this, arguments);
this.freshLine();
this.__write($2_msg)
}
$lzsc$temp._dbg_name = "Debug.writeInternal";
return $lzsc$temp
})();
Debug.write = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzFormat.lzs#94.15 -*- */
function  () {
return this.writeInternal.apply(this, arguments)
}
$lzsc$temp._dbg_name = "Debug.write";
return $lzsc$temp
})();
Debug.pad = LzFormatter.prototype.pad;
Debug.formatToString = LzFormatter.prototype.formatToString;
Debug.format = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzFormat.lzs#120.16 -*- */
function  ($1_control, $2_args) {
Debug.__write(this.formatToString.apply(this, arguments))
}
$lzsc$temp._dbg_name = "Debug.format";
return $lzsc$temp
})();
Debug.makeDebugWindow = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#19.25 -*- */
function  () {
var $1_module = $modules.lz;
var $2_idp = new RegExp("^[_$\\w\\d]+$");
for (var $3_name in $1_module) {
if ($3_name.match($2_idp)) {
try {
var $4_obj = $1_module[$3_name];
if ($4_obj instanceof Object && $4_obj.constructor && !$4_obj.hasOwnProperty("_dbg_name")) {
$4_obj._dbg_name = "#" + $3_name
}
}
catch (e) {

}
}
var $5_ptypes = {Array: Array, Boolean: Boolean, Date: Date, Function: Function, Number: Number, Object: Object, String: String}
for (var $6_n in $5_ptypes) {
var $7_p = $5_ptypes[$6_n];
try {
if (!Debug.functionName($7_p)) {
$7_p._dbg_name = $6_n
}
}
catch (e) {

}
}
}
}
$lzsc$temp._dbg_name = "Debug.makeDebugWindow";
return $lzsc$temp
})();
Debug.DebugWindow = null;
Debug.clear = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#65.15 -*- */
function  () {
var $1_dw = this.DebugWindow;
if (!$1_dw) {
$1_dw = this.DebugWindow = window.parent.frames["LaszloDebugger"]
}
$1_dw.document.body.innerHTML = ""
}
$lzsc$temp._dbg_name = "Debug.clear";
return $lzsc$temp
})();
Debug.addHTMLText = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#78.21 -*- */
function  ($1_str) {
var $2_dw = this.DebugWindow;
if (!$2_dw) {
$2_dw = this.DebugWindow = window.parent.frames["LaszloDebugger"]
}
var $3_dwd = $2_dw.document;
var $4_span = $3_dwd.createElement("span");
var $5_dwdb = $3_dwd.body;
$4_span.innerHTML = '<span style="white-space: pre">' + $1_str.split("\n").join("<br />") + "</span>";
$5_dwdb.appendChild($4_span);
this.atFreshLine = $1_str.charAt($1_str.length - 1) == "\n";
if ($1_str.length) {
this.atPrompt = false
}
$2_dw.scrollTo(0, $5_dwdb.scrollHeight)
}
$lzsc$temp._dbg_name = "Debug.addHTMLText";
return $lzsc$temp
})();
Debug.addText = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#101.17 -*- */
function  ($1_msg) {
var $2_str;
try {
if ($1_msg && $1_msg["toHTML"]) {
$2_str = $1_msg.toHTML()
} else {
$2_str = String($1_msg).toHTML()
}
}
catch (e) {
$2_str = $1_msg
}
try {
this.addHTMLText($2_str)
}
catch (e) {
try {
if (print.length > 0) {
print($2_str);
return
}
}
catch (e) {

}
}
}
$lzsc$temp._dbg_name = "Debug.addText";
return $lzsc$temp
})();
Debug.log = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#132.13 -*- */
function  ($1_msg) {
if ("console" in global && typeof console.log == "function") {
var $2_fn = "log";
if ($1_msg instanceof LzError) {
$2_fn = "error"
} else {
if ($1_msg instanceof LzWarning) {
$2_fn = "warn"
} else {
if ($1_msg instanceof LzInfo) {
$2_fn = "info"
} else {
if ($1_msg instanceof LzDebug) {
$2_fn = "debug"
}
}
}
}
if (typeof console[$2_fn] != "function") {
$2_fn = "log"
}
if ($1_msg instanceof LzMessage || $1_msg instanceof LzSourceMessage) {
console[$2_fn].apply(console, $1_msg.toArray())
} else {
console[$2_fn]("" + $1_msg)
}
}
}
$lzsc$temp._dbg_name = "Debug.log";
return $lzsc$temp
})();
Debug.displayResult = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#159.23 -*- */
function  ($1_result) {
if (typeof $1_result != "undefined") {
if ($1_result !== this.environment._) {
if (typeof this.environment.__ != "undefined") {
this.environment.___ = this.environment.__
}
if (typeof this.environment._ != "undefined") {
this.environment.__ = this.environment._
}
this.environment._ = $1_result
}
}
this.freshLine();
if (typeof $1_result != "undefined") {
this.format("%w", $1_result)
}
this.freshPrompt()
}
$lzsc$temp._dbg_name = "Debug.displayResult";
return $lzsc$temp
})();
Debug.displayObj = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#183.20 -*- */
function  ($1_id) {
var $2_obj = this.ObjectForID($1_id);
this.freshPrompt();
this.addHTMLText('<span class="DEBUG">' + this.formatToString("Debug.inspect(%0.48w)", $2_obj).toHTML() + "</span>\n");
this.displayResult(this.inspect($2_obj))
}
$lzsc$temp._dbg_name = "Debug.displayObj";
return $lzsc$temp
})();
Debug.environment = {}
Debug.doEval = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#203.16 -*- */
function  ($1_expr) {
this.freshPrompt();
this.addHTMLText('<span class="DEBUG">' + String($1_expr).toHTML() + "</span>\n");
try {
with (global) {
with (this.environment) {
var $2_value = eval($1_expr)
}
}
this.displayResult($2_value)
}
catch (e) {
Debug.error(e)
}
}
$lzsc$temp._dbg_name = "Debug.doEval";
return $lzsc$temp
})();
Debug.warnInternal = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#230.22 -*- */
function  ($1_xtor, $2_control, $3_args) {
var $4_sourceMessage = LzSourceMessage;
var $5_level = $4_sourceMessage.level;
if ($5_level > $4_sourceMessage.levelMax) {
return
}
try {
$4_sourceMessage.level = $5_level + 1;
var $6_msg = $1_xtor.format.apply($1_xtor, [null, null].concat(Array.prototype.slice.call(arguments, 1)));
var $7_mls = this.messageLevels;
if ($7_mls[$1_xtor.prototype.type] >= $7_mls[this.messageLevel]) {
this.freshLine();
this.__write($6_msg)
}
}
finally {
$4_sourceMessage.level = $5_level
}
return $6_msg
}
$lzsc$temp._dbg_name = "Debug.warnInternal";
return $lzsc$temp
})();
Debug.enableInspectMouseHandlers = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#255.36 -*- */
function  ($1_div, $2_enable) {
if ($2_enable) {
$1_div.prev_onclick = $1_div.onclick;
$1_div.style.prev_border = $1_div.style.border;
$1_div.style.prev_margin = $1_div.style.margin;
$1_div.style.border = "1px solid red";
$1_div.style.margin = "-1px";
$1_div.onclick = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#262.19 -*- */
function  ($1_e) {
Debug.write("view = ", this.owner.owner)
}
$lzsc$temp._dbg_name = "div.onclick";
return $lzsc$temp
})()
} else {
$1_div.onclick = $1_div.prev_onclick;
$1_div.style.border = $1_div.style.prev_border;
$1_div.style.margin = $1_div.style.prev_margin;
delete $1_div.prev_onclick;
delete $1_div.prev_margin;
delete $1_div.prev_border
}
}
$lzsc$temp._dbg_name = "Debug.enableInspectMouseHandlers";
return $lzsc$temp
})();
Debug.showDivs = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#280.18 -*- */
function  ($1_enable) {
if ($1_enable == null) {
$1_enable = true
}
Debug._showDivs(canvas, $1_enable)
}
$lzsc$temp._dbg_name = "Debug.showDivs";
return $lzsc$temp
})();
Debug._showDivs = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/kernel.js#288.19 -*- */
function  ($1_view, $2_enable) {
var $3_k = $1_view.sprite;
if ($3_k != null) {
var $4_div = $3_k.__LZdiv;
if ($4_div != null) {
this.enableInspectMouseHandlers($4_div, $2_enable)
}
}
for (var $5_i = 0;
$5_i < $1_view.subviews.length;
$5_i++) {
var $6_cv = $1_view.subviews[$5_i];
Debug._showDivs($6_cv, $2_enable)
}
}
$lzsc$temp._dbg_name = "Debug._showDivs";
return $lzsc$temp
})();
Debug.sourceWarningHistory = [];
var $reportSourceWarning = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzCompiler.lzs#19.28 -*- */
function  ($1_filename, $2_lineNumber, $3_msg, $4_fatal) {
var $5_warning;
if (LzWarning) {
var $6_what = $4_fatal ? LzError : LzWarning;
$5_warning = new $6_what($1_filename, $2_lineNumber, $3_msg)
} else {
$5_warning = ($4_fatal ? "ERROR: " : "WARNING: ") + $1_filename + ":" + $2_lineNumber + ": " + $3_msg + "\n"
}
var $7_warningString = $5_warning.toString();
if (Debug.sourceWarningHistory[$7_warningString]) {
return
}
Debug.sourceWarningHistory[$7_warningString] = true;
if (Debug.remoteDebug) {
if (Debug.inEvalRequest) {
Debug.xmlwarnings.push([$1_filename, $2_lineNumber, $3_msg])
} else {
Debug.sockWriteWarning($1_filename, $2_lineNumber, $3_msg)
}
} else {
Debug.freshLine();
Debug.__write($5_warning)
}
}
$lzsc$temp._dbg_name = "debugger/LzCompiler.lzs#19/28";
return $lzsc$temp
})();
var LzMessage = 
/* -*- file: debugger/LzMessage.lzs#26.17 -*- */
function LzMessage ($1_message) {
this.constructor = arguments.callee;
this.objects = [];
if (arguments.length > 0) {
this.appendInternal("" + $1_message, $1_message)
}
}
LzMessage.prototype = new String();
LzMessage.prototype.message = "";
LzMessage.prototype.length = 0;
LzMessage.prototype.toLowerCase = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#73.35 -*- */
function  () {
var $1_msg = new LzMessage(this.message.toLowerCase.apply(this, arguments));
$1_msg.objects = this.objects.concat();
return $1_msg
}
$lzsc$temp._dbg_name = "LzMessage.prototype.toLowerCase";
return $lzsc$temp
})();
LzMessage.prototype.toUpperCase = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#82.35 -*- */
function  () {
var $1_msg = new LzMessage(this.message.toUpperCase.apply(this, arguments));
$1_msg.objects = this.objects.concat();
return $1_msg
}
$lzsc$temp._dbg_name = "LzMessage.prototype.toUpperCase";
return $lzsc$temp
})();
LzMessage.prototype.toString = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#92.32 -*- */
function  ($1_radix) {
return this.message.toString($1_radix)
}
$lzsc$temp._dbg_name = "LzMessage.prototype.toString";
return $lzsc$temp
})();
LzMessage.prototype.valueOf = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#98.31 -*- */
function  () {
return this.message.valueOf()
}
$lzsc$temp._dbg_name = "LzMessage.prototype.valueOf";
return $lzsc$temp
})();
LzMessage.prototype.concat = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#103.30 -*- */
function  () {
var $1_msg = new LzMessage(this.message.concat.apply(this, arguments));
var $2_offset = 0;
for (var $3_i = 0;
$3_i < arguments.length;
$3_i++) {
var $4_arg = arguments[$3_i];
if ($4_arg instanceof LzMessage) {
var $5_ao = $4_arg.objects;
for (var $6_j = 0;
$6_j < $5_ao.length;
$6_j++) {
var $7_od = $5_ao[$6_j];
$1_msg.objects.push({id: $7_od.id, start: $7_od.start + $2_offset, end: $7_od.end + $2_offset})
}
}
$2_offset += String($4_arg).length
}
return $1_msg
}
$lzsc$temp._dbg_name = "LzMessage.prototype.concat";
return $lzsc$temp
})();
LzMessage.prototype.slice = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#124.29 -*- */
function  () {
return this.message.slice.apply(this, arguments)
}
$lzsc$temp._dbg_name = "LzMessage.prototype.slice";
return $lzsc$temp
})();
LzMessage.prototype.split = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#130.29 -*- */
function  () {
return this.message.split.apply(this, arguments)
}
$lzsc$temp._dbg_name = "LzMessage.prototype.split";
return $lzsc$temp
})();
LzMessage.prototype.substr = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#136.30 -*- */
function  () {
return this.message.substr.apply(this, arguments)
}
$lzsc$temp._dbg_name = "LzMessage.prototype.substr";
return $lzsc$temp
})();
LzMessage.prototype.substring = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#142.33 -*- */
function  () {
return this.message.substring.apply(this, arguments)
}
$lzsc$temp._dbg_name = "LzMessage.prototype.substring";
return $lzsc$temp
})();
LzMessage.prototype.appendInternal = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#154.38 -*- */
function  ($1_str, $2_obj) {
if (arguments.length < 2) {
var $3_id = null
} else {
var $3_id = Debug.IDForObject($2_obj)
}
if ($3_id == null) {
this.message += $1_str
} else {
var $4_start = this.message.length;
this.message += $1_str;
var $5_end = this.message.length;
this.objects.push({id: $3_id, start: $4_start, end: $5_end})
}
this.length = this.message.length
}
$lzsc$temp._dbg_name = "LzMessage.prototype.appendInternal";
return $lzsc$temp
})();
LzMessage.prototype.append = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#181.30 -*- */
function  ($1_args) {
var $2_len = arguments.length;
for (var $3_i = 0;
$3_i < $2_len;
$3_i++) {
var $4_arg = arguments[$3_i];
if ($4_arg instanceof Object || Debug.IDForObject($4_arg) != null) {
var $5_str = Debug.__String($4_arg, true, Infinity, true);
this.appendInternal($5_str, $4_arg)
} else {
this.appendInternal(String($4_arg))
}
}
}
$lzsc$temp._dbg_name = "LzMessage.prototype.append";
return $lzsc$temp
})();
String.prototype.toHTML = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#201.27 -*- */
function  () {
return Debug.xmlEscape(this)
}
$lzsc$temp._dbg_name = "String.prototype.toHTML";
return $lzsc$temp
})();
LzMessage.prototype.toArray = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#213.31 -*- */
function  ($1_linkMaker) {
switch (arguments.length) {
case 0:
$1_linkMaker = 
/* -*- file: debugger/LzMessage.lzs#216.17 -*- */
function linkMaker ($1_rep, $2_id) {
return Debug.ObjectForID($2_id)
}
break;

}
var $2_msg = this.message;
var $3_base = 0;
var $4_limit = $2_msg.length;
var $5_start = 0;
var $6_end = 0;
var $7_objs = this.objects;
var $8_id;
var $9_array = [];
var $10_len = $7_objs.length;
for (var $11_i = 0;
$11_i < $10_len;
$11_i++) {
var $12_annot = $7_objs[$11_i];
$5_start = $12_annot.start;
$6_end = $12_annot.end;
$8_id = $12_annot.id;
$9_array.push($2_msg.substring($3_base, $5_start).toHTML());
$9_array.push($1_linkMaker($2_msg.substring($5_start, $6_end).toHTML(), $8_id));
$3_base = $6_end
}
$9_array.push($2_msg.substring($3_base, $4_limit).toHTML());
return $9_array
}
$lzsc$temp._dbg_name = "LzMessage.prototype.toArray";
return $lzsc$temp
})();
LzMessage.prototype.toHTML = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#246.30 -*- */
function  () {
return this.toArray((function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#247.23 -*- */
function  () {
return Debug.makeObjectLink.apply(Debug, arguments)
}
$lzsc$temp._dbg_name = "debugger/LzMessage.lzs#247/23";
return $lzsc$temp
})()).join("")
}
$lzsc$temp._dbg_name = "LzMessage.prototype.toHTML";
return $lzsc$temp
})();

/* -*- file: debugger/LzMessage.lzs#264.1 -*- */
function LzSourceMessage ($1_file, $2_line, $3_message) {
switch (arguments.length) {
case 0:
$1_file = null;
case 1:
$2_line = null;
case 2:
$3_message = "";

}
if ("backtraceStack" in Debug) {
var $4_bts = Debug.backtraceStack;
var $5_btsl = $4_bts.length;
var $6_skip = 3;
for (var $7_i = $5_btsl - 1;
$7_i > $6_skip;
$7_i--) {
var $8_callee = $4_bts[$7_i].callee;
if ($8_callee === $reportSourceWarning || $8_callee === Debug.warnInternal) {
$6_skip = $5_btsl - $7_i + 3;
break
}
}
if (Debug.backtraceStack.length > $6_skip) {
this.backtrace = Debug.backtrace($6_skip);
if ($1_file == null && this.backtrace) {
var $9_top = this.backtrace.userStackFrame();
if ($9_top) {
$1_file = $9_top.filename();
$2_line = $9_top.lineno()
}
}
}
}
this.file = $1_file;
this.line = $2_line;
if ($3_message instanceof LzMessage) {
this.message = $3_message
} else {
this.message = new LzMessage($3_message)
}
}
LzSourceMessage.prototype.type = "";
LzSourceMessage.prototype.color = "#000000";
LzSourceMessage.level = 0;
LzSourceMessage.levelMax = 5;
LzSourceMessage.format = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#325.26 -*- */
function  ($1_file, $2_line, $3_control, $4_args) {
var $5_al = arguments.length;
switch ($5_al) {
case 0:
$1_file = null;
case 1:
$2_line = null;

}
var $6_message;
if ($5_al < 2) {
$6_message = ""
} else {
var $7_debug = Debug;
$6_message = $7_debug.formatToString.apply($7_debug, Array.prototype.slice.call(arguments, 2))
}
if ($1_file == null) {
for (var $8_i = 3;
$8_i < arguments.length;
$8_i++) {
var $9_arg = arguments[$8_i];
if ($9_arg instanceof lz.node && "_dbg_filename" in $9_arg) {
$1_file = $9_arg._dbg_filename;
$2_line = $9_arg._dbg_lineno
}
}
}
return new (this.prototype.constructor)($1_file, $2_line, $6_message)
}
$lzsc$temp._dbg_name = "LzSourceMessage.format";
return $lzsc$temp
})();
LzSourceMessage.prototype.locationString = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#357.44 -*- */
function  () {
var $1_str = this.type;
if (this.file) {
$1_str += " @";
$1_str += this.file;
if (this.line) {
$1_str += "#";
$1_str += this.line
}
}
$1_str += ": ";
return $1_str
}
$lzsc$temp._dbg_name = "LzSourceMessage.prototype.locationString";
return $lzsc$temp
})();
LzSourceMessage.prototype.toArray = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#374.37 -*- */
function  () {
var $1_array = [this.locationString()];
if (this.message instanceof LzMessage) {
return $1_array.concat(this.message.toArray())
}
return $1_array.concat("" + this.message)
}
$lzsc$temp._dbg_name = "LzSourceMessage.prototype.toArray";
return $lzsc$temp
})();
LzSourceMessage.prototype.toStringInternal = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#386.46 -*- */
function  ($1_conversion) {
return this.locationString() + this.message[$1_conversion]()
}
$lzsc$temp._dbg_name = "LzSourceMessage.prototype.toStringInternal";
return $lzsc$temp
})();
LzSourceMessage.prototype._dbg_name = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#393.39 -*- */
function  () {
return this.toStringInternal("toString")
}
$lzsc$temp._dbg_name = "LzSourceMessage.prototype._dbg_name";
return $lzsc$temp
})();
LzSourceMessage.prototype.toString = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#400.38 -*- */
function  () {
return this.toStringInternal("toString") + "\n"
}
$lzsc$temp._dbg_name = "LzSourceMessage.prototype.toString";
return $lzsc$temp
})();
LzSourceMessage.prototype.toHTML = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#409.36 -*- */
function  () {
var $1_id = Debug.IDForObject(this);
return Debug.makeObjectLink(this.toStringInternal("toHTML"), $1_id, this) + "\n"
}
$lzsc$temp._dbg_name = "LzSourceMessage.prototype.toHTML";
return $lzsc$temp
})();

/* -*- file: debugger/LzMessage.lzs#421.1 -*- */
function LzWarning ($1_file, $2_line, $3_message) {
LzSourceMessage.apply(this, arguments)
}
LzWarning.prototype = new LzSourceMessage();
LzWarning.prototype.constructor = LzWarning;
LzWarning.format = LzSourceMessage.format;
LzWarning.prototype.type = "WARNING";
LzWarning.prototype.color = "#ff9900";

/* -*- file: debugger/LzMessage.lzs#437.1 -*- */
function LzError ($1_file, $2_line, $3_message) {
LzSourceMessage.apply(this, arguments)
}
LzError.prototype = new LzSourceMessage();
LzError.prototype.constructor = LzError;
LzError.format = LzSourceMessage.format;
LzError.prototype.type = "ERROR";
LzError.prototype.color = "#ff0000";

/* -*- file: debugger/LzMessage.lzs#452.1 -*- */
function LzInfo ($1_file, $2_line, $3_message) {
LzSourceMessage.apply(this, arguments)
}
LzInfo.prototype = new LzSourceMessage();
LzInfo.prototype.constructor = LzInfo;
LzInfo.format = LzSourceMessage.format;
LzInfo.prototype.type = "INFO";
LzInfo.prototype.color = "#0066cc";

/* -*- file: debugger/LzMessage.lzs#467.1 -*- */
function LzDebug ($1_file, $2_line, $3_message) {
LzSourceMessage.apply(this, arguments)
}
LzDebug.prototype = new LzSourceMessage();
LzDebug.prototype.constructor = LzDebug;
LzDebug.format = LzSourceMessage.format;
LzDebug.prototype.type = "DEBUG";
LzDebug.prototype.color = "#00cc00";
Debug.warn = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#487.14 -*- */
function  ($1_control, $2_args) {
return this.warnInternal.apply(this, [LzWarning].concat(Array.prototype.slice.call(arguments, 0)))
}
$lzsc$temp._dbg_name = "Debug.warn";
return $lzsc$temp
})();
Debug.error = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#495.15 -*- */
function  ($1_control, $2_args) {
return this.warnInternal.apply(this, [LzError].concat(Array.prototype.slice.call(arguments, 0)))
}
$lzsc$temp._dbg_name = "Debug.error";
return $lzsc$temp
})();
Debug.info = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#504.14 -*- */
function  ($1_control, $2_args) {
return this.warnInternal.apply(this, [LzInfo].concat(Array.prototype.slice.call(arguments, 0)))
}
$lzsc$temp._dbg_name = "Debug.info";
return $lzsc$temp
})();
Debug.debug = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#513.15 -*- */
function  ($1_control, $2_args) {
return this.warnInternal.apply(this, [LzDebug].concat(Array.prototype.slice.call(arguments, 0)))
}
$lzsc$temp._dbg_name = "Debug.debug";
return $lzsc$temp
})();
Debug.deprecated = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMessage.lzs#526.20 -*- */
function  ($1_obj, $2_method, $3_replacement) {
Debug.info("%w.%s is deprecated.  Use %w.%s instead", $1_obj, $2_method, $1_obj, $3_replacement)
}
$lzsc$temp._dbg_name = "Debug.deprecated";
return $lzsc$temp
})();
Debug.objectClassPattern = new RegExp("^\\[object\\s+(\\w+)\\]$");
Debug.nativeClassPattern = new RegExp("^\\[(\\w+)\\]$");
Debug.__typeof = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#12.18 -*- */
function  ($1_thing) {
try {
var $2_n = typeof $1_thing;
if ($1_thing && ($2_n == "object" || $1_thing instanceof Object)) {
var $3_oc = $1_thing["constructor"];
var $4_user_name = null;
if ($1_thing["_dbg_typename"] && $1_thing._dbg_typename instanceof Function) {
try {
$4_user_name = $1_thing._dbg_typename()
}
catch (e) {

}
} else {
if (typeof $1_thing._dbg_typename == "string") {
$4_user_name = $1_thing._dbg_typename
}
}
if (typeof $4_user_name == "string" || $4_user_name instanceof String) {
$2_n = $4_user_name
} else {
if ($3_oc) {
var $5_ocn = this.functionName($3_oc, false);
if (!$5_ocn) {
var $6_ts = $1_thing.toString();
var $7_m = $6_ts.match(this.objectClassPattern);
if (!$7_m) {
$7_m = $6_ts.match(this.nativeClassPattern)
}
if ($7_m) {
$5_ocn = $7_m[1]
}
}
if ($5_ocn) {
if ($3_oc !== global[$5_ocn]) {
var $8_id = this.IDForObject($3_oc);
$5_ocn += "#" + $8_id
}
$2_n = $5_ocn
}
}
}
if ($3_oc instanceof Function && !($1_thing instanceof $3_oc)) {
if ($1_thing === $3_oc.prototype) {

} else {
$2_n = "\xBF" + $2_n + "?"
}
}
}
try {
if ($1_thing && typeof $1_thing.length == "number") {
$2_n += "(" + $1_thing.length + ")"
}
}
catch (e) {

}
}
catch (e) {
try {
$2_n = typeof $1_thing
}
catch (e) {
$2_n = "Error computing __typeof"
}
}
return $2_n
}
$lzsc$temp._dbg_name = "Debug.__typeof";
return $lzsc$temp
})();
Debug.functionNamePattern = new RegExp("function\\s+([^\\s(]+)[\\s(]");
Debug.functionName = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#95.22 -*- */
function  ($1_fn, $2_mustBeUnique) {
if ($1_fn && $1_fn instanceof Function) {
if ($1_fn.hasOwnProperty("_dbg_name")) {
var $3_n = $1_fn._dbg_name
} else {
var $4_fstring = $1_fn.toString();
var $5_m = $4_fstring.match(this.functionNamePattern);
if ($5_m) {
var $3_n = $5_m[1]
}
}
if ($3_n) {
if (!$2_mustBeUnique || $1_fn === global[$3_n]) {
return $3_n
}
}
}
return null
}
$lzsc$temp._dbg_name = "Debug.functionName";
return $lzsc$temp
})();
Debug.__String = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#120.18 -*- */
function  ($1_thing, $2_pretty, $3_limit, $4_unique) {
var nodeToString;
nodeToString = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#227.7 -*- */
function nodeToString ($1_node) {
var $2_tn = $1_node.nodeName || "";
var $3_path = $2_tn.toLowerCase();
if ($1_node.nodeType == Node.ELEMENT_NODE) {
var $4_id = $1_node.id;
var $5_cn = $1_node.className;
if ($4_id) {
$3_path += "#" + $4_id
} else {
if ($5_cn) {
var $6_more = $5_cn.indexOf(" ");
if ($6_more == -1) {
$6_more = $5_cn.length
}
$3_path += "." + $5_cn.substring(0, $6_more)
}
}
}
var $7_parent = $1_node.parentNode;
if ($7_parent) {
var $8_index;
var $9_count = 0;
for (var $10_sibling = $7_parent.firstChild;
$10_sibling;
$10_sibling = $10_sibling.nextSibling) {
if ($2_tn == $10_sibling.nodeName) {
$9_count++;
if ($8_index) {
break
}
}
if ($1_node === $10_sibling) {
$8_index = $9_count
}
}
if ($9_count > 1) {
$3_path += "[" + $8_index + "]"
}
try {
return nodeToString($7_parent) + "/" + $3_path
}
catch (e) {
return ".../" + $3_path
}
}
return $3_path
}
try {
switch (arguments.length) {
case 1:
$2_pretty = this.printPretty;
case 2:
$3_limit = this.printLength;
case 3:
$4_unique = !$2_pretty;

}
if ($3_limit <= 0) {
return ""
}
if ($1_thing === void 0) {
if ($2_pretty) {
return "(void 0)"
} else {
return "\xABundefined\xBB"
}
}
if ($1_thing === null) {
return "null"
}
var $5_t = typeof $1_thing;
var $6_debug_name = null;
var $7_s = "";
if ($1_thing instanceof Object) {
var $8_opl = this.printLength;
this.printLength = $3_limit < this.inspect.printLength ? $3_limit : this.inspect.printLength;
var $9_ac = Class.allClasses;
if ($1_thing.hasOwnProperty("_dbg_typename")) {
for (var $10_cn in $9_ac) {
if ($1_thing === $9_ac[$10_cn].prototype) {
$6_debug_name = ".prototype";
break
}
}
}
if ($6_debug_name) {

} else {
if ($1_thing["_dbg_name"] && $1_thing._dbg_name instanceof Function) {
try {
$6_debug_name = $1_thing._dbg_name()
}
catch (e) {

}
} else {
if ($1_thing["_dbg_name"] && (typeof $1_thing._dbg_name == "string" || $6_debug_name instanceof String)) {
$6_debug_name = $1_thing._dbg_name
}
}
}
this.printLength = $8_opl
}
if (typeof $6_debug_name == "string" || $6_debug_name instanceof String) {
if ($6_debug_name.charAt(0) != "#") {
$2_pretty = !$4_unique
}
$7_s = this.stringEscape($6_debug_name)
} else {
if ($5_t == "null" || $5_t == "number" || $5_t == "boolean") {
$2_pretty = true;
$7_s = String($1_thing)
} else {
if ($5_t == "string" || $1_thing instanceof String) {
if ($1_thing instanceof String && $1_thing.constructor !== String) {
$2_pretty = !$4_unique
}
$7_s = this.abbreviate($1_thing, $3_limit);
var $11_prc = $7_s === $1_thing;
if (!$11_prc) {
$2_pretty = !$4_unique
}
$7_s = this.stringEscape($7_s, true);
if ($5_t == "string" && $11_prc) {
return $7_s
}
this.IDForObject($1_thing, true)
} else {
if ($5_t == "function" || $1_thing instanceof Function) {
var $12_n = this.functionName($1_thing, $4_unique);
if ($12_n != null) {
$7_s = $12_n
} else {
$2_pretty = !$4_unique;
$7_s = this.functionName($1_thing, false);
if ($7_s == null) {
$7_s = ""
}
}
} else {
if (!isNaN(Number($1_thing["nodeType"]))) {
$7_s = nodeToString($1_thing)
} else {
if ($5_t == "object" || $1_thing instanceof Object) {
if (!$1_thing.constructor.prototype.isPrototypeOf || !($1_thing.constructor.prototype.isPrototypeOf instanceof Function) || !$1_thing.constructor.prototype.isPrototypeOf($1_thing)) {
$2_pretty = !$4_unique
}
if ($1_thing.constructor === Date || $1_thing.constructor === Boolean || $1_thing.constructor === Number) {
$7_s = $1_thing.toString();
if ($7_s == null) {
$7_s = ""
}
} else {
if ($1_thing instanceof String) {

} else {
if (typeof $1_thing.length == "number") {
if ($1_thing.constructor !== Array) {
$2_pretty = !$4_unique
}
var $13_ellip = true;
var $14_tl = $1_thing.length;
for (var e = 0;
e < $14_tl && $7_s.length < $3_limit;
e++) {
if (typeof $1_thing[e] == "undefined") {
if ($13_ellip) {
$7_s += "..., ";
$13_ellip = false
}
} else {
$13_ellip = true;
$7_s += String($1_thing[e]) + ", "
}
}
if ($7_s != "") {
$7_s = $7_s.substring(0, $7_s.length - 2)
}
$7_s = "[" + $7_s + "]"
} else {
if ($1_thing["toString"] && this.toString instanceof Function && $1_thing.toString !== Object.prototype.toString && typeof $1_thing.toString() != "undefined" && $1_thing.toString() != "undefined") {
$2_pretty = !$4_unique;
$7_s = String($1_thing)
} else {
if (!$1_thing instanceof Object || $1_thing.constructor !== Object) {
$2_pretty = !$4_unique
}
var $13_ellip = true;
for (var e in $1_thing) {
var $15_v = $1_thing[e];
var $16_tv = typeof $15_v;
var $17_dtv = this.__typeof($15_v);
if ((!($1_thing instanceof Object) || $1_thing.hasOwnProperty(e)) && $16_tv != "undefined" && $16_tv != "function" && "" + $15_v != "" && !this.internalProperty(e) && !this.internalProperty($17_dtv)) {
$13_ellip = true;
$7_s += "" + e + ": " + String($15_v) + ", "
} else {
if ($13_ellip) {
$7_s += "..., ";
$13_ellip = false
}
}
if ($7_s.length > $3_limit) {
break
}
}
if ($7_s != "") {
$7_s = $7_s.substring(0, $7_s.length - 2)
}
$7_s = "{" + $7_s + "}"
}
}
}
}
} else {
$2_pretty = !$4_unique;
$7_s = String($1_thing)
}
}
}
}
}
}
if ($2_pretty && $7_s != "" && $7_s.length < $3_limit) {
return $7_s
}
}
catch (e) {
try {
$7_s = String($1_thing)
}
catch (e) {
$7_s = "Error computing __String"
}
}
var $18_id = this.IDForObject($1_thing, $7_s.length >= $3_limit);
var $19_r = "\xAB";
$19_r += this.__typeof($1_thing);
if ($4_unique && $18_id != null) {
$19_r += "#" + $18_id
}
if ($7_s != "") {
var $20_room = $3_limit - $19_r.length - 6;
if ($20_room > 0) {
$19_r += "| ";
$19_r += this.abbreviate($7_s, $20_room)
}
}
$19_r += "\xBB";
return $19_r
}
$lzsc$temp._dbg_name = "Debug.__String";
return $lzsc$temp
})();
Debug.makeObjectLink = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#404.24 -*- */
function  ($1_rep, $2_id, $3_attrs) {
var $4_type = "INSPECT";
switch (arguments.length) {
case 1:
$2_id = this.IDForObject($1_rep);
case 2:
break;
case 3:
if ($3_attrs.type) {
$4_type = $3_attrs.type
};

}
if ($2_id != null) {
var $5_obj = this.ObjectForID($2_id);
var $6_tip = Debug.formatToString("Inspect %0.32#w", $5_obj);
return '<a class="' + $4_type + '" title="' + $6_tip + '" href="javascript:window.parent.$modules.lz.Debug.displayObj(' + $2_id + ')">' + $1_rep + "</a>"
}
return $1_rep
}
$lzsc$temp._dbg_name = "Debug.makeObjectLink";
return $lzsc$temp
})();
Debug.inspectInternal = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#427.25 -*- */
function  (obj, $1_showInternalProperties) {
var $2_si = typeof $1_showInternalProperties != "undefined" ? $1_showInternalProperties : this.showInternalProperties;
var $3_hasProto = obj && obj.hasOwnProperty;
var $4_opl = this.printLength;
try {
if (!(typeof obj == "object" || obj instanceof Object)) {
this.printLength = Infinity
} else {
this.printLength = this.inspect.printLength
}
var $5_name = this.xmlEscape(this.__String(obj, false));
if (!(obj instanceof Object)) {
return $5_name
}
this.printLength = this.inspect.printLength;
var $6_keys = [];
var $7_arraylen = typeof obj.length == "number" ? obj.length : null;
if ($2_si) {
for (var $8_p in {callee: true, length: true, constructor: true, prototype: true}) {
try {
if ($3_hasProto && obj.hasOwnProperty($8_p)) {
$6_keys.push($8_p)
}
}
catch (e) {

}
}
}
for (var key in obj) {
try {
if (!$3_hasProto || obj.hasOwnProperty(key) || (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#469.14 -*- */
function  () {
try {
return obj[key]
}
catch (e) {

}
}
$lzsc$temp._dbg_name = "debugger/platform/dhtml/LzDebug.js#469/14";
return $lzsc$temp
})()() !== (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#470.14 -*- */
function  () {
try {
return obj.constructor.prototype[key]
}
catch (e) {

}
}
$lzsc$temp._dbg_name = "debugger/platform/dhtml/LzDebug.js#470/14";
return $lzsc$temp
})()()) {
if ($7_arraylen && key >= 0 && key < $7_arraylen) {

} else {
if ($2_si || !this.internalProperty(key) && !this.internalProperty(this.__typeof(obj[key]))) {
$6_keys.push(key)
}
}
}
}
catch (e) {

}
}
$6_keys.sort((function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#485.15 -*- */
function  ($1_a, $2_b) {
var $3_al = $1_a.toLowerCase();
var $4_bl = $2_b.toLowerCase();
return ($3_al > $4_bl) - ($3_al < $4_bl)
}
$lzsc$temp._dbg_name = "debugger/platform/dhtml/LzDebug.js#485/15";
return $lzsc$temp
})());
var $9_description = "";
var $10_kl = $6_keys.length;
var $11_val;
var $12_wid = 0;
if (this.markGeneration > 0) {
for (var $13_i = 0;
$13_i < $10_kl;
$13_i++) {
var $14_kil = $6_keys[$13_i].length;
if ($14_kil > $12_wid) {
$12_wid = $14_kil
}
}
}
if ($7_arraylen) {
var $14_kil = ("" + $7_arraylen).length;
if ($14_kil > $12_wid) {
$12_wid = $14_kil
}
}
var $15_last;
for (var $13_i = 0;
$13_i < $10_kl;
$13_i++) {
var key = $6_keys[$13_i];
if (key != $15_last) {
$15_last = key;
$11_val = obj[key];
$9_description += "  " + this.computeSlotDescription(obj, key, $11_val, $12_wid) + "\n"
}
}
if ($7_arraylen && !(typeof obj == "string" || obj instanceof String)) {
for (var key = 0;
key < $7_arraylen;
key++) {
if (!$3_hasProto || obj.hasOwnProperty(key)) {
$11_val = obj[key];
if (typeof $11_val != "undefined") {
$9_description += "  " + this.computeSlotDescription(obj, key, $11_val, $12_wid) + "\n"
}
}
}
}
}
finally {
this.printLength = $4_opl
}
if (this.markGeneration > 0) {
var $16_leaked = this.annotation.leaked;
if (obj && obj instanceof Object && obj.hasOwnProperty && obj.hasOwnProperty instanceof Function && obj.hasOwnProperty($16_leaked) && obj[$16_leaked]) {
$5_name += " (\xA3" + obj[$16_leaked] + ")"
}
}
if ($9_description != "") {
$9_description = " {\n" + $9_description + "}"
}
return $5_name + $9_description
}
$lzsc$temp._dbg_name = "Debug.inspectInternal";
return $lzsc$temp
})();
Debug.computeSlotDescription = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzDebug.js#554.32 -*- */
function  ($1_obj, $2_key, $3_val, $4_wid) {
var $5_r = $2_key + ":";
$4_wid++;
try {
if (this.markGeneration > 0) {
var $6_annotation = this.annotation;
var $7_leaked = $6_annotation.leaked;
var $8_why = $6_annotation.why;
var $9_wf = "        ";
$4_wid += $9_wf.length;
if ($3_val && $3_val instanceof Object && $3_val.hasOwnProperty && $3_val.hasOwnProperty instanceof Function && $3_val.hasOwnProperty($7_leaked) && $3_val[$7_leaked] && (!$1_obj.hasOwnProperty($7_leaked) || $3_val[$8_why].indexOf($1_obj[$8_why]) == 0)) {
$5_r += this.pad(" (\xA3" + $3_val[$7_leaked] + ")", $9_wf.length)
} else {
$5_r += $9_wf
}
}
var $10_ostr = this.xmlEscape(this.__String($3_val));
var $11_id = this.IDForObject($3_val);
$5_r = this.pad($5_r, $4_wid);
$5_r += " " + this.makeObjectLink($10_ostr, $11_id)
}
catch (e) {
try {
$5_r += this.formatToString(" Error: %w computing description", e)
}
catch (e) {
$5_r += " Error computing description"
}
}
return $5_r
}
$lzsc$temp._dbg_name = "Debug.computeSlotDescription";
return $lzsc$temp
})();
Debug.stackOverflow = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#22.25 -*- */
function  () {
var $1_bs = this.backtraceStack;
try {
var $2_old = $1_bs.maxDepth;
$1_bs.maxDepth *= 1.25;
throw new Error(Debug.error("Stack overflow"))
}
finally {
$1_bs.maxDepth = $2_old
}
}
$lzsc$temp._dbg_name = "Debug.stackOverflow";
return $lzsc$temp
})();
var __LzStackFrame = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#53.22 -*- */
function  ($1_args) {
if ($1_args instanceof Array) {
this["this"] = $1_args["this"];
this["function"] = $1_args.callee;
this.__lineno = "lineno" in $1_args ? $1_args.lineno : this["function"]._dbg_lineno
}
this.arguments = $1_args
}
$lzsc$temp._dbg_name = "debugger/LzBacktrace.lzs#53/22";
return $lzsc$temp
})();
__LzStackFrame.prototype.isUserFrame = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#66.42 -*- */
function  () {
return this["function"]._dbg_filename.indexOf("lfc") != 0
}
$lzsc$temp._dbg_name = "__LzStackFrame.prototype.isUserFrame";
return $lzsc$temp
})();
__LzStackFrame.prototype.filename = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#74.39 -*- */
function  () {
return this["function"]._dbg_filename
}
$lzsc$temp._dbg_name = "__LzStackFrame.prototype.filename";
return $lzsc$temp
})();
__LzStackFrame.prototype.lineno = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#82.37 -*- */
function  () {
return this.__lineno
}
$lzsc$temp._dbg_name = "__LzStackFrame.prototype.lineno";
return $lzsc$temp
})();
__LzStackFrame.prototype._dbg_name = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#90.38 -*- */
function  () {
var $1_callee = this["function"];
var $2_filename = $1_callee._dbg_filename;
var $3_lineno = this.__lineno;
return Debug.formatToString("%0.64w @%s#%d", $1_callee, $2_filename, $3_lineno)
}
$lzsc$temp._dbg_name = "__LzStackFrame.prototype._dbg_name";
return $lzsc$temp
})();
__LzStackFrame.prototype._dbg_typename = "StackFrame";
var LzBacktrace = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#113.19 -*- */
function  ($1_skip) {
if (arguments.length < 1) {
$1_skip = 0
}
var $2_bs = Debug.backtraceStack;
var $3_l = $2_bs.length - $1_skip;
this.length = $3_l;
for (var $4_i = 0;
$4_i < $3_l;
$4_i++) {
var $5_fr = $2_bs[$4_i];
if (!$5_fr.hasOwnProperty("__LzStackFrame") || $5_fr["lineno"] != $5_fr.__LzStackFrame.__lineno) {
$5_fr.__LzStackFrame = new __LzStackFrame($5_fr)
}
this[$4_i] = $5_fr.__LzStackFrame
}
}
$lzsc$temp._dbg_name = "debugger/LzBacktrace.lzs#113/19";
return $lzsc$temp
})();
LzBacktrace.prototype.userStackFrame = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#142.42 -*- */
function  () {
for (var $1_i = this.length - 1;
$1_i >= 0;
$1_i--) {
var $2_fr = this[$1_i];
if ($2_fr.isUserFrame()) {
return $2_fr
}
}
}
$lzsc$temp._dbg_name = "LzBacktrace.prototype.userStackFrame";
return $lzsc$temp
})();
LzBacktrace.prototype.toStringInternal = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#159.42 -*- */
function  ($1_printer, $2_length) {
switch (arguments.length) {
case 0:
$1_printer = 
/* -*- file: debugger/LzBacktrace.lzs#162.17 -*- */
function printer ($1_o) {
return Debug.__String($1_o["function"])
};
case 1:
$2_length = Debug.printLength;

}
var $3_backtrace = "";
var $4_sep = " <- ";
for (var $5_i = this.length - 1;
$5_i >= 0 && $3_backtrace.length < $2_length;
$5_i--) {
$3_backtrace += $1_printer(this[$5_i]) + $4_sep
}
if ($3_backtrace != "") {
$3_backtrace = $3_backtrace.substring(0, $3_backtrace.length - $4_sep.length)
}
$3_backtrace = Debug.abbreviate($3_backtrace, $2_length);
return $3_backtrace
}
$lzsc$temp._dbg_name = "LzBacktrace.prototype.toStringInternal";
return $lzsc$temp
})();
LzBacktrace.prototype.toString = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#182.34 -*- */
function  () {
return this.toStringInternal()
}
$lzsc$temp._dbg_name = "LzBacktrace.prototype.toString";
return $lzsc$temp
})();
LzBacktrace.prototype._dbg_name = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#191.35 -*- */
function  () {
return this.toStringInternal((function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#192.32 -*- */
function  ($1_o) {
return Debug.functionName($1_o["function"], false)
}
$lzsc$temp._dbg_name = "debugger/LzBacktrace.lzs#192/32";
return $lzsc$temp
})(), 75)
}
$lzsc$temp._dbg_name = "LzBacktrace.prototype._dbg_name";
return $lzsc$temp
})();
LzBacktrace.prototype._dbg_typename = "Backtrace";
Debug.backtrace = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzBacktrace.lzs#210.19 -*- */
function  ($1_skip) {
if (arguments.length < 1) {
$1_skip = 1
}
if (Debug.backtraceStack.length > $1_skip) {
return new LzBacktrace($1_skip)
}
}
$lzsc$temp._dbg_name = "Debug.backtrace";
return $lzsc$temp
})();
var LzTrace = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzTrace.lzs#19.15 -*- */
function  ($1_file, $2_line, $3_message) {
LzSourceMessage.apply(this, arguments)
}
$lzsc$temp._dbg_name = "debugger/LzTrace.lzs#19/15";
return $lzsc$temp
})();
LzTrace.prototype = new LzSourceMessage();
LzTrace.prototype.constructor = LzTrace;
LzTrace.format = LzSourceMessage.format;
LzTrace.prototype.type = "TRACE";
LzTrace.prototype.color = "#00cc00";
Debug.traceMessage = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzTrace.lzs#46.22 -*- */
function  ($1_control, $2_args) {
return this.warnInternal.apply(this, [LzTrace].concat(Array.prototype.slice.call(arguments, 0)))
}
$lzsc$temp._dbg_name = "Debug.traceMessage";
return $lzsc$temp
})();
Debug.trace = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzTrace.lzs#68.15 -*- */
function  ($1_who, $2_what) {
if ($1_who[$2_what] instanceof Function) {
var f = $1_who[$2_what];
var $3_m = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzTrace.lzs#71.13 -*- */
function  () {
Debug.traceMessage("[%6.2f] %s.apply(%w, %w)", new Date().getTime() % 1000000, f, this, arguments);
var $1_r = f.apply(this, arguments);
Debug.traceMessage("[%6.2f] %s -> %w", new Date().getTime() % 1000000, f, $1_r);
return $1_r
}
$lzsc$temp._dbg_name = "debugger/LzTrace.lzs#71/13";
return $lzsc$temp
})();
$3_m._dbg_previous_definition = f;
$1_who[$2_what] = $3_m
} else {
Debug.error("%w.%s is not a function", $1_who, $2_what)
}
}
$lzsc$temp._dbg_name = "Debug.trace";
return $lzsc$temp
})();
Debug.untrace = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzTrace.lzs#96.17 -*- */
function  ($1_who, $2_what) {
if ($1_who[$2_what] instanceof Function) {
var $3_f = $1_who[$2_what];
var $4_p = $3_f["_dbg_previous_definition"];
if ($4_p) {
$1_who[$2_what] = $4_p
} else {
Debug.error("%w.%s is not being traced", $1_who, $2_what)
}
} else {
Debug.error("%w.%s is not a function", $1_who, $2_what)
}
}
$lzsc$temp._dbg_name = "Debug.untrace";
return $lzsc$temp
})();
var LzMonitor = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMonitor.lzs#19.17 -*- */
function  ($1_file, $2_line, $3_message) {
LzSourceMessage.apply(this, arguments)
}
$lzsc$temp._dbg_name = "debugger/LzMonitor.lzs#19/17";
return $lzsc$temp
})();
LzMonitor.prototype = new LzSourceMessage();
LzMonitor.prototype.constructor = LzMonitor;
LzMonitor.format = LzSourceMessage.format;
LzMonitor.prototype.type = "MONITOR";
LzTrace.prototype.color = "#00cc00";
Debug.monitorMessage = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/LzMonitor.lzs#44.24 -*- */
function  ($1_control, $2_args) {
return this.warnInternal.apply(this, [LzMonitor].concat(Array.prototype.slice.call(arguments, 0)))
}
$lzsc$temp._dbg_name = "Debug.monitorMessage";
return $lzsc$temp
})();
Debug.monitor = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzMonitor.js#20.17 -*- */
function  (who, what) {
var o = who[what];
var $1_s = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzMonitor.js#22.11 -*- */
function  ($1_n) {
var $2_c = arguments.callee.caller;
if (!$2_c && Debug.backtraceStack instanceof Array) {
var $3_bs = Debug.backtraceStack;
$2_c = $3_bs[$3_bs.length - 2].callee
}
Debug.monitorMessage("[%6.2f] %s: %w.%s: %w -> %w", new Date().getTime() % 1000000, $2_c || "(unknown)", who, what, o, $1_n);
return o = $1_n
}
$lzsc$temp._dbg_name = "debugger/platform/dhtml/LzMonitor.js#22/11";
return $lzsc$temp
})();
var $2_g = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzMonitor.js#36.11 -*- */
function  () {
return o
}
$lzsc$temp._dbg_name = "debugger/platform/dhtml/LzMonitor.js#36/11";
return $lzsc$temp
})();
try {
delete who[what];
who.__defineGetter__(what, $2_g);
who.__defineSetter__(what, $1_s);
with (who) {
if (eval(what) !== o) {
throw new Error("Debug.monitor: failed to install functional getter/setter")
}
}
}
catch (e) {
Debug.error("Debug.monitor: Can't monitor %s.%s", who, what);
delete who[what];
who[what] = o
}
}
$lzsc$temp._dbg_name = "Debug.monitor";
return $lzsc$temp
})();
Debug.unmonitor = (function  () {
var $lzsc$temp = 
/* -*- file: debugger/platform/dhtml/LzMonitor.js#69.19 -*- */
function  ($1_who, $2_what) {
var $3_o = $1_who[$2_what];
delete $1_who[$2_what];
$1_who[$2_what] = $3_o
}
$lzsc$temp._dbg_name = "Debug.unmonitor";
return $lzsc$temp
})();
var black = 0;
var green = 32768;
var silver = 12632256;
var lime = 65280;
var gray = 8421504;
var olive = 8421376;
var white = 16777215;
var yellow = 16776960;
var maroon = 8388608;
var navy = 128;
var red = 16711680;
var blue = 255;
var purple = 8388736;
var teal = 32896;
var fuchsia = 16711935;
var aqua = 65535;
var LzDeclaredEvent = {}
LzDeclaredEvent.sendEvent = (function  () {
var $lzsc$temp = 
/* -*- file: core/LzDefs.lzs#20.29 -*- */
function  () {

}
$lzsc$temp._dbg_name = "LzDeclaredEvent.sendEvent";
return $lzsc$temp
})();
LzDeclaredEvent.clearDelegates = (function  () {
var $lzsc$temp = 
/* -*- file: core/LzDefs.lzs#25.34 -*- */
function  () {

}
$lzsc$temp._dbg_name = "LzDeclaredEvent.clearDelegates";
return $lzsc$temp
})();
LzDeclaredEvent.removeDelegate = (function  () {
var $lzsc$temp = 
/* -*- file: core/LzDefs.lzs#30.34 -*- */
function  () {

}
$lzsc$temp._dbg_name = "LzDeclaredEvent.removeDelegate";
return $lzsc$temp
})();
LzDeclaredEvent.getDelegateCount = (function  () {
var $lzsc$temp = 
/* -*- file: core/LzDefs.lzs#35.36 -*- */
function  () {
return 0
}
$lzsc$temp._dbg_name = "LzDeclaredEvent.getDelegateCount";
return $lzsc$temp
})();
LzDeclaredEvent.toString = (function  () {
var $lzsc$temp = 
/* -*- file: core/LzDefs.lzs#40.28 -*- */
function  () {
return "LzDeclaredEvent"
}
$lzsc$temp._dbg_name = "LzDeclaredEvent.toString";
return $lzsc$temp
})();
LzDeclaredEvent.ready = false;

/* -*- file: core/LzDefs.lzs#52.1 -*- */
function DeclareEvent ($1_who, $2_what) {
$1_who[$2_what] = LzDeclaredEvent
}

/* -*- file: core/LzDefs.lzs#60.1 -*- */
function DeclareEvents ($1_who, $2_what) {
for (var $3_i in $2_what) {
$1_who[$2_what[$3_i]] = LzDeclaredEvent
}
}

/* -*- file: core/LzDefs.lzs#75.1 -*- */
function LzInheritedHash ($1_parent) {
var $2_xtor;
$2_xtor = 
/* -*- file: core/LzDefs.lzs#88.7 -*- */
function xtor () {

}
if ($1_parent) {
$2_xtor.prototype = $1_parent;
return new $2_xtor()
}
}
var ConstructorMap = {}
var lz = ConstructorMap;
var LzNode = Class.make("LzNode", null, {initialize: 
/* -*- file: core/LzNode.lzs#49.1 -*- */
function initialize ($1_parent, $2_attrs, $3_children, $4_instcall) {
this.__LZUID = "__U" + ++LzNode.__UIDs;
this.__LZdeferDelegates = true;
var $5_qpos = LzDelegate.__LZdelegatesQueue.length;
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
var $7_iargs;
if ($2_attrs && $2_attrs["$hasdefaultattrs"]) {
$7_iargs = $2_attrs
} else {
$7_iargs = new LzInheritedHash(this.defaultattrs);
if ($2_attrs) {
var $8_dattrs = this.defaultattrs;
var $9_dattr$refs = "$refs" in $8_dattrs && $8_dattrs.$refs;
var $10_cleanup = null;
for (var $11_k in $2_attrs) {
var $12_attrk = $2_attrs[$11_k];
if ($9_dattr$refs && $9_dattr$refs[$11_k]) {
if (!$10_cleanup) {
$10_cleanup = {}
}
$10_cleanup[$11_k] = true
}
if ($12_attrk instanceof Object) {
var $13_dattrk = $8_dattrs[$11_k];
if ($13_dattrk instanceof Object) {
if ($12_attrk instanceof Array) {
$7_iargs[$11_k] = $12_attrk.concat($13_dattrk);
continue
} else {
if (typeof $12_attrk == "object") {
var $14_tmp = new LzInheritedHash($13_dattrk);
for (var $15_j in $12_attrk) {
$14_tmp[$15_j] = $12_attrk[$15_j]
}
$7_iargs[$11_k] = $14_tmp;
continue
}
}
}
}
$7_iargs[$11_k] = $12_attrk
}
if ($10_cleanup) {
if (!$7_iargs.hasOwnProperty("$refs")) {
$7_iargs.$refs = new LzInheritedHash($9_dattr$refs)
}
var $16_ia = LzNode._ignoreAttribute;
for (var $11_k in $10_cleanup) {
$7_iargs.$refs[$11_k] = $16_ia
}
}
}
}
this._instanceAttrs = $7_iargs;
this._instanceChildren = $3_children;
var $17_maskedargs = new LzInheritedHash($7_iargs);
this.__LZisnew = !$4_instcall;
var $18_c = "classChildren" in this.constructor.prototype ? this.constructor.prototype.classChildren : null;
if ($18_c && $18_c.length) {
if (!("doneClassRoot" in $18_c && $18_c.doneClassRoot) && !this.$isstate) {
$18_c.doneClassRoot = true;
this.__LZassignClassRoot($18_c, 1)
}
if ($3_children && $3_children.length) {
$3_children = $18_c.concat($3_children)
} else {
$3_children = $18_c
}
} else {
if ($3_children && $3_children.length) {
$3_children = $3_children.concat()
}
}
this.construct($1_parent, $17_maskedargs);
this.__LZapplyArgs($17_maskedargs, true);
if (this.__LZdeleted) {
return
}
var $19_styleMap = this.$styles();
if ($19_styleMap) {
this.__LZstyleConstraints = this.__LZapplyStyleMap($19_styleMap, $2_attrs)
}
if (this.constructWithArgs) {
this.constructWithArgs($17_maskedargs)
}
delete this.__LZdeferDelegates;
if ($5_qpos != LzDelegate.__LZdelegatesQueue.length) {
LzDelegate.__LZdrainDelegatesQueue($5_qpos)
}
if (this.onconstruct.ready) {
this.onconstruct.sendEvent(this)
}
if ($3_children && $3_children.length) {
this.createChildren($3_children)
} else {
this.__LZinstantiationDone()
}
}, clonenumber: null, __LZlateinit: null, __LZpreventSubInit: null, __LZresolveDict: null, __LZsourceLocation: null, __LZUID: null, __LZdelegates: null, defaultattrs: {$hasdefaultattrs: true}, isinited: false, subnodes: null, datapath: null, initstage: null, $isstate: false, doneClassRoot: false, parent: null, children: null, cloneManager: null, name: null, id: null, defaultplacement: null, placement: null, $cfn: 0, __LZdeleted: false, immediateparent: null, dependencies: null, classroot: null, nodeLevel: null, sourceLocatorTable: {}, lookupSourceLocator: 
/* -*- file: core/LzNode.lzs#468.5 -*- */
function lookupSourceLocator ($1_sourceLocator) {
return this.sourceLocatorTable[$1_sourceLocator]
}, $styles: 
/* -*- file: core/LzNode.lzs#480.1 -*- */
function $styles () {
return null
}, __LZapplyStyleMap: 
/* -*- file: core/LzNode.lzs#498.1 -*- */
function __LZapplyStyleMap ($1_stylemap, $2_initialArgs) {
var $3_styleConstraints = {}
for (var $4_k in $1_stylemap) {
if ($2_initialArgs[$4_k] != null) {
continue
}
var $5_v = LzCSSStyle.getPropertyValueFor(this, $1_stylemap[$4_k]);
if (typeof $5_v == "string" && !isNaN($5_v)) {
Debug.warn("Invalid CSS value for %w.%s: `%#w`.  Use: `#%06x`.", this, $4_k, $5_v, Number($5_v));
$5_v = Number($5_v)
}
if (!($4_k in $2_initialArgs)) {
if ($5_v instanceof Function) {
if (this[$4_k] == null) {
$3_styleConstraints[$4_k] = $5_v
}
} else {
if ($5_v != null) {
if (!this.__LZdeleted) {
var $lzsc$1375156679 = this.setters;
if ($lzsc$1375156679 && $4_k in $lzsc$1375156679) {
this[$lzsc$1375156679[$4_k]]($5_v)
} else {
if ($lzsc$1375156679 == null) {
Debug.warn("null setters on", this, $4_k, $5_v)
}
this[$4_k] = $5_v;
var $lzsc$1728885037 = "on" + $4_k;
if ($lzsc$1728885037 in this) {
if (this[$lzsc$1728885037].ready) {
this[$lzsc$1728885037].sendEvent($5_v)
}
}
}
}
}
}
}
}
return $3_styleConstraints
}, __LZapplyStyleConstraints: 
/* -*- file: core/LzNode.lzs#546.1 -*- */
function __LZapplyStyleConstraints () {
var $1_styleConstraints = this.__LZstyleConstraints;
for (var $2_k in $1_styleConstraints) {
var $3_fn = $1_styleConstraints[$2_k];
var $4_v = $3_fn.call(this);
if (!this.__LZdeleted) {
var $lzsc$2021392094 = this.setters;
if ($lzsc$2021392094 && $2_k in $lzsc$2021392094) {
this[$lzsc$2021392094[$2_k]]($4_v)
} else {
if ($lzsc$2021392094 == null) {
Debug.warn("null setters on", this, $2_k, $4_v)
}
this[$2_k] = $4_v;
var $lzsc$1186915814 = "on" + $2_k;
if ($lzsc$1186915814 in this) {
if (this[$lzsc$1186915814].ready) {
this[$lzsc$1186915814].sendEvent($4_v)
}
}
}
}
}
}, construct: 
/* -*- file: core/LzNode.lzs#605.1 -*- */
function construct ($1_parent, $2_args) {
var $3_lp = $1_parent;
this.parent = $3_lp;
if ($3_lp) {
var $4_ip = $3_lp;
var $5_a = $2_args;
if (!("ignoreplacement" in $5_a)) {
var $6_thisplacement = "placement" in $5_a ? $5_a.placement : null;
if ($6_thisplacement == null) {
$6_thisplacement = $3_lp.defaultplacement
} else {
this.placement = $6_thisplacement
}
while ($6_thisplacement != null) {
if ($4_ip.determinePlacement == LzNode.prototype.determinePlacement) {
var $7_pp = $4_ip.searchSubnodes("name", $6_thisplacement);
if ($7_pp == null) {
$7_pp = $4_ip
}
} else {
var $7_pp = $4_ip.determinePlacement(this, $6_thisplacement, $2_args)
}
$6_thisplacement = $7_pp != $4_ip ? $7_pp.defaultplacement : null;
$4_ip = $7_pp
}
}
var $8_ip_subnodes = $4_ip.subnodes;
if ($8_ip_subnodes == null) {
$8_ip_subnodes = new Array();
$4_ip.subnodes = $8_ip_subnodes
}
$8_ip_subnodes[$8_ip_subnodes.length] = this;
var $9_nl = $4_ip.nodeLevel;
this.nodeLevel = $9_nl ? $9_nl + 1 : 1;
this.immediateparent = $4_ip
} else {
this.nodeLevel = 1
}
}, init: 
/* -*- file: core/LzNode.lzs#661.1 -*- */
function init () {
return
}, __LZinstantiationDone: 
/* -*- file: core/LzNode.lzs#673.1 -*- */
function __LZinstantiationDone () {
if (!this.immediateparent || this.immediateparent.isinited || this.initstage == "early" || this.__LZisnew && LzInstantiator.syncNew) {
this.__LZcallInit()
}
}, __LZsetPreventInit: 
/* -*- file: core/LzNode.lzs#685.1 -*- */
function __LZsetPreventInit () {
this.__LZpreventSubInit = []
}, __LZclearPreventInit: 
/* -*- file: core/LzNode.lzs#692.1 -*- */
function __LZclearPreventInit () {
var $1_lzp = this.__LZpreventSubInit;
delete this.__LZpreventSubInit;
var $2_l = $1_lzp.length;
for (var $3_i = 0;
$3_i < $2_l;
$3_i++) {
$1_lzp[$3_i].__LZcallInit()
}
}, __LZcallInit: 
/* -*- file: core/LzNode.lzs#705.1 -*- */
function __LZcallInit () {
if (this.parent && this.parent.__LZpreventSubInit) {
this.parent.__LZpreventSubInit.push(this);
return
}
this.isinited = true;
this.__LZresolveReferences();
if (this.__LZstyleConstraints) {
this.__LZapplyStyleConstraints()
}
var $1_sl = this.subnodes;
if ($1_sl) {
var $2_i = 0;
var $3_l = $1_sl.length;
while ($2_i < $3_l) {
var $4_s = $1_sl[$2_i++];
var $5_t = $1_sl[$2_i];
if ($4_s.isinited || $4_s.__LZlateinit) {
continue
}
$4_s.__LZcallInit();
if ($5_t != $1_sl[$2_i]) {
while ($2_i > 0) {
if ($5_t == $1_sl[--$2_i]) {
break
}
}
}
}
}
if (this.__LZsourceLocation) {
LzNode.sourceLocatorTable[this.__LZsourceLocation] = this
}
this.init();
if (this.oninit.ready) {
this.oninit.sendEvent(this)
}
if (this.datapath && this.datapath.__LZApplyDataOnInit) {
this.datapath.__LZApplyDataOnInit()
}
}, completeInstantiation: 
/* -*- file: core/LzNode.lzs#768.1 -*- */
function completeInstantiation () {
if (!this.isinited) {
var $1_myis = this.initstage;
this.initstage = "early";
if ($1_myis == "defer") {
LzInstantiator.createImmediate(this, this.__LZdeferredcarr)
} else {
LzInstantiator.completeTrickle(this)
}
}
}, __LZapplyArgs: 
/* -*- file: core/LzNode.lzs#796.1 -*- */
function __LZapplyArgs ($1_args, $2_constcall) {
var $3_oset = {}
var $4_hasset = [];
var $5_hasearly = null;
if ("$setters" in $1_args && $1_args.$setters) {
this.__LZsetSetters($1_args.$setters)
}
var $6_setrs = this.setters;
for (var $7_a in $1_args) {
if ($3_oset[$7_a] || $1_args[$7_a] === LzNode._ignoreAttribute) {
continue
}
$3_oset[$7_a] = true;
if ($6_setrs && $6_setrs[$7_a] == null) {
this.addProperty($7_a, $1_args[$7_a]);
if (!$2_constcall) {
var $8_evt = "on" + $7_a;
if ($8_evt in this) {
if (this[$8_evt].ready) {
this[$8_evt].sendEvent($1_args[$7_a])
}
}
}
} else {
if ($6_setrs && $6_setrs[$7_a] != -1) {
if (this.earlySetters && this.earlySetters[$7_a]) {
if ($5_hasearly == null) {
$5_hasearly = []
}
$5_hasearly[this.earlySetters[$7_a]] = $7_a
} else {
$4_hasset.push($7_a)
}
}
}
}
if ($5_hasearly) {
for (var $9_i = 1;
$9_i < $5_hasearly.length;
$9_i++) {
var $7_a = $5_hasearly[$9_i];
if (this[$6_setrs[$7_a]]) {
this[$6_setrs[$7_a]]($1_args[$7_a], $7_a)
}
}
}
while ($4_hasset.length) {
var $7_a = $4_hasset.pop();
this[$6_setrs[$7_a]]($1_args[$7_a], $7_a)
}
}, createChildren: 
/* -*- file: core/LzNode.lzs#861.1 -*- */
function createChildren ($1_carr) {
if (this.__LZdeleted) {
return
}
if ("defer" == this.initstage) {
this.__LZlateinit = true;
this.__LZdeferredcarr = $1_carr
} else {
if ("late" == this.initstage) {
this.__LZlateinit = true;
LzInstantiator.trickleInstantiate(this, $1_carr)
} else {
if (this.__LZisnew && LzInstantiator.syncNew || "immediate" == this.initstage) {
LzInstantiator.createImmediate(this, $1_carr)
} else {
LzInstantiator.requestInstantiation(this, $1_carr)
}
}
}
}, getAttribute: 
/* -*- file: core/LzNode.lzs#884.1 -*- */
function getAttribute ($1_prop) {
if (null == this.getters[$1_prop]) {
return this[$1_prop]
} else {
return this[this.getters[$1_prop]]()
}
}, setAttribute: 
/* -*- file: core/LzNode.lzs#908.1 -*- */
function setAttribute ($1_prop, $2_val, $3_ifchanged) {
if (this.__LZdeleted || $3_ifchanged && this[$1_prop] == $2_val) {
return
}
var $4_s = this.setters;
if ($4_s && $1_prop in $4_s) {
this[$4_s[$1_prop]]($2_val)
} else {
if ($4_s == null) {
Debug.warn("null setters on", this, $1_prop, $2_val)
}
this[$1_prop] = $2_val;
var $5_evt = "on" + $1_prop;
if ($5_evt in this) {
if (this[$5_evt].ready) {
this[$5_evt].sendEvent($2_val)
}
}
}
}, setProp: 
/* -*- file: core/LzNode.lzs#932.1 -*- */
function setProp ($1_prop, $2_val) {
Debug.deprecated(this, arguments.callee, this.setAttribute);
if (!this.__LZdeleted) {
var $lzsc$329267744 = this.setters;
if ($lzsc$329267744 && $1_prop in $lzsc$329267744) {
this[$lzsc$329267744[$1_prop]]($2_val)
} else {
if ($lzsc$329267744 == null) {
Debug.warn("null setters on", this, $1_prop, $2_val)
}
this[$1_prop] = $2_val;
var $lzsc$318856363 = "on" + $1_prop;
if ($lzsc$318856363 in this) {
if (this[$lzsc$318856363].ready) {
this[$lzsc$318856363].sendEvent($2_val)
}
}
}
}
}, getExpectedAttribute: 
/* -*- file: core/LzNode.lzs#957.1 -*- */
function getExpectedAttribute ($1_prop) {
var $2_e_prop = "e_" + $1_prop;
if (!this[$2_e_prop]) {
this[$2_e_prop] = {}
}
if (this[$2_e_prop].v == null) {
return this[$1_prop]
}
return this[$2_e_prop].v
}, setExpectedAttribute: 
/* -*- file: core/LzNode.lzs#969.1 -*- */
function setExpectedAttribute ($1_prop, $2_val) {
var $3_e_prop = "e_" + $1_prop;
if (!this[$3_e_prop]) {
this[$3_e_prop] = {}
}
this[$3_e_prop].v = $2_val
}, addToExpectedAttribute: 
/* -*- file: core/LzNode.lzs#980.1 -*- */
function addToExpectedAttribute ($1_prop, $2_val) {
var $3_e_prop = "e_" + $1_prop;
if (!this[$3_e_prop]) {
this[$3_e_prop] = {}
}
if (this[$3_e_prop].v == null) {
this[$3_e_prop].v = this[$1_prop]
}
this[$3_e_prop].v += $2_val
}, __LZincrementCounter: 
/* -*- file: core/LzNode.lzs#991.1 -*- */
function __LZincrementCounter ($1_prop) {
var $2_e_prop = "e_" + $1_prop;
var $3_tep = this[$2_e_prop];
if (!$3_tep) {
$3_tep = this[$2_e_prop] = {}
}
if (!$3_tep.c) {
$3_tep.c = 0
}
$3_tep.c += 1
}, makeChild: 
/* -*- file: core/LzNode.lzs#1024.1 -*- */
function makeChild ($1_e, $2_async) {
if (this.__LZdeleted) {
return
}
for (var $3_p = this;
$3_p != canvas;
$3_p = $3_p.immediateparent) {
if ($3_p == null) {
break
}
if ($3_p.__LZdeleted) {
Debug.error("%w.makeChild(%w, %w) when %w.__LZdeleted", this, $1_e, $2_async, $3_p)
}
}
var $4_x = ConstructorMap[$1_e.name];
if (!$4_x || !($4_x.make instanceof Function)) {
Debug.error("Class for tag %s has not been defined yet", $1_e.name)
}
var $5_w;
if ($4_x) {
$5_w = new $4_x(this, $1_e.attrs, "children" in $1_e ? $1_e.children : null, $2_async)
}
return $5_w
}, setters: {name: "setName", id: "setID", $events: "__LZsetEvents", $refs: "__LZstoreRefs", $delegates: "__LZstoreDelegates", options: "__LZsetOptions", placement: -1, datapath: "setDatapath", $setters: -1, $classrootdepth: "__LZsetClassRoot", $datapath: "__LZmakeDatapath"}, __LZsetClassRoot: 
/* -*- file: core/LzNode.lzs#1085.1 -*- */
function __LZsetClassRoot ($1_d) {
if (!$1_d) {
return
}
var $2_p = this.parent;
while (--$1_d > 0) {
$2_p = $2_p.parent
}
this.classroot = $2_p
}, __LZsetSetters: 
/* -*- file: core/LzNode.lzs#1098.1 -*- */
function __LZsetSetters ($1_o) {
for (var $2_s in $1_o) {
var $3_attrSet = "_anonSet" + $2_s;
this.__LZaddSetter($2_s, $3_attrSet);
this[$3_attrSet] = $1_o[$2_s]
}
}, __LZaddSetter: 
/* -*- file: core/LzNode.lzs#1109.1 -*- */
function __LZaddSetter ($1_key, $2_val) {
if (!this.hasOwnProperty("setters")) {
this.setters = new LzInheritedHash(this.setters)
}
if ("put" in this.setters) {
this.setters.put($1_key, $2_val)
} else {
this.setters[$1_key] = $2_val
}
}, dataBindAttribute: 
/* -*- file: core/LzNode.lzs#1132.1 -*- */
function dataBindAttribute ($1_attr, $2_path) {
if (!this.datapath) {
this.setDatapath(".")
}
if (!this.__LZdelegates) {
this.__LZdelegates = []
}
this.__LZdelegates.push(new LzDataAttrBind(this.datapath, $1_attr, $2_path))
}, __LZdelayedSetters: {$refs: "__LZresolveRefs"}, earlySetters: {name: 1, id: 2, $events: 3, $delegates: 4, $classrootdepth: 5, $datapath: 6}, getters: {}, __LZstoreDelegates: 
/* -*- file: core/LzNode.lzs#1166.1 -*- */
function __LZstoreDelegates ($1_delarr) {
var $2_resarray = [];
var $3_l = $1_delarr.length;
for (var $4_i = 0;
$4_i < $3_l;
$4_i += 3) {
if ($1_delarr[$4_i + 2]) {
$2_resarray.push($1_delarr[$4_i], $1_delarr[$4_i + 1], $1_delarr[$4_i + 2])
} else {
var $5_m = $1_delarr[$4_i + 1];
if (!this.__LZdelegates) {
this.__LZdelegates = []
}
this.__LZdelegates.push(new LzDelegate(this, $5_m, this, $1_delarr[$4_i]))
}
}
if ($2_resarray.length) {
this.__LZstoreAttr($2_resarray, "$delegates")
}
}, __LZstoreRefs: 
/* -*- file: core/LzNode.lzs#1195.1 -*- */
function __LZstoreRefs ($1_val, $2_prop) {
for (var $3_i in $1_val) {
if (!($3_i in this)) {
this[$3_i] = null
}
}
this.__LZstoreAttr($1_val, $2_prop)
}, __LZstoreAttr: 
/* -*- file: core/LzNode.lzs#1216.1 -*- */
function __LZstoreAttr ($1_val, $2_prop) {
if (this.__LZresolveDict == null) {
this.__LZresolveDict = {}
}
this.__LZresolveDict[$2_prop] = $1_val
}, __LZresolveReferences: 
/* -*- file: core/LzNode.lzs#1227.1 -*- */
function __LZresolveReferences () {
var $1_rdict = this.__LZresolveDict;
this.__LZresolveDict = null;
for (var $2_r in $1_rdict) {
if ($2_r == "$delegates") {
continue
}
this[this.__LZdelayedSetters[$2_r]]($1_rdict[$2_r])
}
if ($1_rdict && $1_rdict.$delegates) {
this.__LZsetDelegates($1_rdict.$delegates)
}
}, __LZevalPathExpr: 
/* -*- file: core/LzNode.lzs#1245.1 -*- */
function __LZevalPathExpr ($1_$lzsc$rp) {
with (global) {
with (this) {
return eval($1_$lzsc$rp)
}
}
}, __LZresolveRefs: 
/* -*- file: core/LzNode.lzs#1256.1 -*- */
function __LZresolveRefs ($1_refs) {
for (var $2_p in $1_refs) {
var $3_rp = $1_refs[$2_p];
var $4_pp;
if ("string" == typeof $3_rp) {
$3_rp = LzParsedPath.trim($3_rp);
var $5_qc = $3_rp.charAt(0);
if ($5_qc == "'" || $5_qc == '"') {
if ($5_qc != $3_rp.charAt($3_rp.length - 1)) {
Debug.warn("Bad quoting for $path %w in %w", $3_rp, this)
}
$4_pp = $3_rp.substring(1, $3_rp.length - 1)
} else {
$4_pp = this.__LZevalPathExpr($3_rp);
if ($4_pp == null) {
Debug.warn("No value for $path reference %w in %w", $3_rp, this)
}
}
this.dataBindAttribute($2_p, $4_pp)
} else {
if (!("dependencies" in $3_rp && $3_rp.dependencies)) {
if ($3_rp instanceof Function) {
$3_rp.call(this)
}
}
}
}
for (var $2_p in $1_refs) {
var $3_rp = $1_refs[$2_p];
if ($3_rp instanceof Function && "dependencies" in $3_rp) {
this.applyConstraint($2_p, $3_rp, $3_rp.dependencies.call(this))
}
}
}, __LZsetDelegates: 
/* -*- file: core/LzNode.lzs#1302.1 -*- */
function __LZsetDelegates ($1_delarr) {
if ($1_delarr.length && !this.__LZdelegates) {
this.__LZdelegates = []
}
var $2_l = $1_delarr.length;
for (var $3_i = 0;
$3_i < $2_l;
$3_i += 3) {
var $4_senderexp = $1_delarr[$3_i + 2];
var $5_sender = $4_senderexp != null ? $4_senderexp.call(this) : null;
if ($5_sender == null) {
$5_sender = this
}
var $6_meth = $1_delarr[$3_i + 1];
this.__LZdelegates.push(new LzDelegate(this, $6_meth, $5_sender, $1_delarr[$3_i]))
}
}, applyConstraint: 
/* -*- file: core/LzNode.lzs#1329.1 -*- */
function applyConstraint ($1_prop, $2_cfunc, $3_dep) {
var $4_l = $3_dep.length;
if ($4_l) {
if (!this.__LZdelegates) {
this.__LZdelegates = []
}
var $5_refF = "$cf" + this.$cfn++;
this[$5_refF] = $2_cfunc;
var $6_dp;
for (var $7_i = 0;
$7_i < $4_l;
$7_i += 2) {
var $8_d = new LzDelegate(this, $5_refF);
this.__LZdelegates.push($8_d);
$6_dp = $3_dep[$7_i];
if ($6_dp) {
$8_d.register($6_dp, "on" + $3_dep[$7_i + 1])
}
}
}
$2_cfunc.call(this)
}, releaseConstraint: 
/* -*- file: core/LzNode.lzs#1371.1 -*- */
function releaseConstraint ($1_prop) {
var $2_refF = "_SetCons" + $1_prop;
if ($2_refF in this) {
this[$2_refF].delegate.unregisterAll()
}
}, setName: 
/* -*- file: core/LzNode.lzs#1383.1 -*- */
function setName ($1_name) {
if (typeof $1_name == "string" && $1_name.length) {
if (this.parent && this.parent[$1_name] && this.parent[$1_name] !== this) {
Debug.warn("Redefining %w.%s from %w to %w", this.parent, $1_name, this.parent[$1_name], this)
}
if (this.parent) {
this.parent[$1_name] = this
}
if (this.immediateparent && this.immediateparent[$1_name] && this.immediateparent[$1_name] !== this) {
Debug.warn("Redefining %w.%s from %w to %w", this.immediateparent, $1_name, this.immediateparent[$1_name], this)
}
if (this.immediateparent) {
this.immediateparent[$1_name] = this
}
this.name = $1_name;
if (this.parent === canvas) {
if (global[$1_name] && global[$1_name] !== this) {
Debug.warn("Redefining #%s from %w to %w", $1_name, global[$1_name], this)
}
if (!this.hasOwnProperty("id")) {
this.id = $1_name
}
global[$1_name] = this
}
} else {
if ($1_name) {
Debug.error("Invalid name %w for %w", $1_name, this)
}
}
}, defaultSet: 
/* -*- file: core/LzNode.lzs#1432.1 -*- */
function defaultSet ($1_val, $2_prop) {
if ($1_val != null) {
this[$2_prop] = $1_val
}
}, setID: 
/* -*- file: core/LzNode.lzs#1447.1 -*- */
function setID ($1_id) {
if (typeof $1_id == "string" && $1_id.length) {
if (global[$1_id] && global[$1_id] !== this) {
Debug.warn("Redefining #%s from %w to %w", $1_id, global[$1_id], this)
}
this.id = $1_id;
global[$1_id] = this
} else {
if ($1_id) {
Debug.error("Invalid id %w for %w", $1_id, this)
}
}
}, setDatapath: 
/* -*- file: core/LzNode.lzs#1479.1 -*- */
function setDatapath ($1_dp) {
if (null != this.datapath && $1_dp != LzNode._ignoreAttribute) {
this.datapath.setXPath($1_dp)
} else {
new LzDatapath(this, {xpath: $1_dp})
}
}, setData: 
/* -*- file: core/LzNode.lzs#1494.3 -*- */
function setData ($1_data) {
this.data = $1_data;
var $2_dp = this.datapath != null ? this.datapath : new LzDatapath(this);
$2_dp.setPointer($1_data);
if (this.ondata.ready) {
this.ondata.sendEvent($1_data)
}
}, applyData: 
/* -*- file: core/LzNode.lzs#1512.1 -*- */
function applyData ($1_data) {

}, __LZsetEvents: 
/* -*- file: core/LzNode.lzs#1519.1 -*- */
function __LZsetEvents ($1_eventNames) {
var $2_n = $1_eventNames.length;
for (var $3_i = 0;
$3_i < $2_n;
$3_i++) {
var $4_ename = $1_eventNames[$3_i];
this[$4_ename] = LzDeclaredEvent
}
}, __LZsetDefaultHandler: 
/* -*- file: core/LzNode.lzs#1530.1 -*- */
function __LZsetDefaultHandler ($1_eName, $2_func) {
var $3_defHandleName = "_handle" + $1_eName;
this[$3_defHandleName] = $2_func;
var $4_d = new LzDelegate(this, $3_defHandleName, this, $1_eName);
if (!this.__LZhandlers) {
this.__LZhandlers = [$4_d]
} else {
this.__LZhandlers.push($4_d)
}
}, options: {}, __LZsetOptions: 
/* -*- file: core/LzNode.lzs#1561.3 -*- */
function __LZsetOptions ($1_hash) {
if (!this.hasOwnProperty("options")) {
this.options = new LzInheritedHash(this.options)
}
for (var $2_key in $1_hash) {
this.options[$2_key] = $1_hash[$2_key]
}
}, getOption: 
/* -*- file: core/LzNode.lzs#1582.1 -*- */
function getOption ($1_key) {
return this.options[$1_key]
}, setOption: 
/* -*- file: core/LzNode.lzs#1593.1 -*- */
function setOption ($1_key, $2_val) {
if (!this.hasOwnProperty("options")) {
this.options = new LzInheritedHash(this.options)
}
this.options[$1_key] = $2_val
}, determinePlacement: 
/* -*- file: core/LzNode.lzs#1625.1 -*- */
function determinePlacement ($1_aSub, $2_placement, $3_args) {
if ($2_placement == null) {
var $4_p = null
} else {
var $4_p = this.searchSubnodes("name", $2_placement)
}
return $4_p == null ? this : $4_p
}, searchImmediateSubnodes: 
/* -*- file: core/LzNode.lzs#1646.1 -*- */
function searchImmediateSubnodes ($1_prop, $2_val) {
var $3_s = this.subnodes;
if ($3_s == null) {
return null
}
for (var $4_i = $3_s.length - 1;
$4_i >= 0;
$4_i--) {
var $5_si = $3_s[$4_i];
if ($5_si[$1_prop] == $2_val) {
return $5_si
}
}
return null
}, searchSubnodes: 
/* -*- file: core/LzNode.lzs#1667.1 -*- */
function searchSubnodes ($1_prop, $2_val) {
var $3_nextS = this.subnodes ? this.subnodes.concat() : [];
while ($3_nextS.length > 0) {
var $4_s = $3_nextS;
$3_nextS = new Array();
for (var $5_i = $4_s.length - 1;
$5_i >= 0;
$5_i--) {
var $6_si = $4_s[$5_i];
if ($6_si[$1_prop] == $2_val) {
return $6_si
}
var $7_sis = $6_si.subnodes;
if ($7_sis) {
for (var $8_j = $7_sis.length - 1;
$8_j >= 0;
$8_j--) {
$3_nextS.push($7_sis[$8_j])
}
}
}
}
return null
}, searchParents: 
/* -*- file: core/LzNode.lzs#1697.1 -*- */
function searchParents ($1_prop) {
var $2_sview = this;
do{
$2_sview = $2_sview.immediateparent;
if ($2_sview[$1_prop] != null) {
return $2_sview
}
} while ($2_sview != canvas)
}, getUID: 
/* -*- file: core/LzNode.lzs#1716.1 -*- */
function getUID () {
return this.__LZUID
}, childOf: 
/* -*- file: core/LzNode.lzs#1729.1 -*- */
function childOf ($1_node) {
if ($1_node == null) {
return false
}
var $2_pv = this;
while ($2_pv.nodeLevel >= $1_node.nodeLevel) {
if ($2_pv == $1_node) {
return true
}
$2_pv = $2_pv.immediateparent
}
return false
}, destroy: 
/* -*- file: core/LzNode.lzs#1745.1 -*- */
function destroy ($1_recursiveCall) {
if (this.__LZdeleted == true) {
return
}
this.__LZdeleted = true;
if (this.ondestroy.ready) {
this.ondestroy.sendEvent(this)
}
this.__LZinstantiationDone = null;
this.setters = null;
if (this.subnodes != null) {
for (var $2_i = this.subnodes.length - 1;
$2_i >= 0;
$2_i--) {
this.subnodes[$2_i].destroy(true)
}
}
if (this.name != null) {
if (this.parent[this.name] == this) {
delete this.parent[this.name]
}
if (this.immediateparent[this.name] == this) {
delete this.immediateparent[this.name]
}
if (this.parent === canvas && global[this.name] === this) {
try {
delete global[this.name]
}
catch (e) {
global[this.name] = void 0
}
}
}
if (this.id != null) {
if (global[this.id] === this) {
try {
delete global[this.id]
}
catch (e) {
global[this.id] = void 0
}
}
}
if (this.__LZdelegates != null) {
for (var $2_i = this.__LZdelegates.length - 1;
$2_i >= 0;
$2_i--) {
this.__LZdelegates[$2_i].unregisterAll()
}
}
delete this.__LZdelegates;
if ("_events" in this && this._events != null) {
for (var $2_i = this._events.length - 1;
$2_i >= 0;
$2_i--) {
this._events[$2_i].clearDelegates()
}
}
delete this._events;
if (this.immediateparent && this.immediateparent.subnodes) {
for (var $2_i = this.immediateparent.subnodes.length - 1;
$2_i >= 0;
$2_i--) {
if (this.immediateparent.subnodes[$2_i] == this) {
this.immediateparent.subnodes.splice($2_i, 1);
break
}
}
}
delete this.data
}, deleteNode: 
/* -*- file: core/LzNode.lzs#1849.1 -*- */
function deleteNode ($1_recursiveCall) {
Debug.deprecated(this, arguments.callee, this.destroy);
this.destroy($1_recursiveCall)
}, animate: 
/* -*- file: core/LzNode.lzs#1879.1 -*- */
function animate ($1_prop, $2_to, $3_duration, $4_isRelative, $5_moreargs) {
if ($3_duration == 0) {
var $6_val = $4_isRelative ? this[$1_prop] + $2_to : $2_to;
if (!this.__LZdeleted) {
var $lzsc$1914731234 = this.setters;
if ($lzsc$1914731234 && $1_prop in $lzsc$1914731234) {
this[$lzsc$1914731234[$1_prop]]($6_val)
} else {
if ($lzsc$1914731234 == null) {
Debug.warn("null setters on", this, $1_prop, $6_val)
}
this[$1_prop] = $6_val;
var $lzsc$301073210 = "on" + $1_prop;
if ($lzsc$301073210 in this) {
if (this[$lzsc$301073210].ready) {
this[$lzsc$301073210].sendEvent($6_val)
}
}
}
}
return null
}
var $7_args = {attribute: $1_prop, to: $2_to, duration: $3_duration, start: true, relative: $4_isRelative, target: this}
for (var $8_p in $5_moreargs) {
$7_args[$8_p] = $5_moreargs[$8_p]
}
var $9_animator = new LzAnimator(null, $7_args);
return $9_animator
}, toString: 
/* -*- file: core/LzNode.lzs#1901.1 -*- */
function toString () {
return this.constructor.classname + " " + this.getDebugIdentification()
}, _dbg_name: 
/* -*- file: core/LzNode.lzs#1919.1 -*- */
function _dbg_name () {
if (typeof this.id == "string" && global[this.id] === this) {
return "#" + this.id
} else {
if (typeof this.name == "string" && this.parent[this.name] === this) {
return "." + this.name
} else {
if (this.toString !== LzNode.prototype.toString) {
return String(this)
} else {
return ""
}
}
}
}, getDebugIdentification: 
/* -*- file: core/LzNode.lzs#1940.1 -*- */
function getDebugIdentification () {
var $1_s = "";
if (this.name != null) {
$1_s += " name: " + this.name + " "
}
if (this.id != null) {
$1_s += " id: " + this.id + " "
}
return $1_s
}, __LZassignClassRoot: 
/* -*- file: core/LzNode.lzs#1961.1 -*- */
function __LZassignClassRoot ($1_arr, $2_depth) {
if ($1_arr != null) {
var $3_l = $1_arr.length;
for (var $4_i = 0;
$4_i < $3_l;
$4_i++) {
$1_arr[$4_i].attrs.$classrootdepth = $2_depth;
var $5_a = "children" in $1_arr[$4_i] ? $1_arr[$4_i].children : null;
if ($5_a && "length" in $5_a) {
var $6_cl = ConstructorMap[$1_arr[$4_i].name];
this.__LZassignClassRoot($5_a, "$isstate" in $6_cl.prototype && $6_cl.prototype.$isstate ? $2_depth : $2_depth + 1)
}
}
}
}, __LZmakeDatapath: 
/* -*- file: core/LzNode.lzs#1981.1 -*- */
function __LZmakeDatapath ($1_dpobj) {
if (!($1_dpobj instanceof Object)) {
Debug.debug("__LZmakeDatapath on non-object %w?", $1_dpobj)
}
this.makeChild($1_dpobj, true)
}}, {tagname: "node", initialize: 
/* -*- file: core/LzNode.lzs#224.8 -*- */
function initialize ($1_prototype) {
if (this.hasOwnProperty("tagname")) {
var $2_tagname = this.tagname;
if ($2_tagname) {
if (ConstructorMap[$2_tagname] !== this) {
if ($2_tagname in ConstructorMap) {
Debug.warn("Redefining tag %s from %w to %w", $2_tagname, ConstructorMap[$2_tagname], this)
}
ConstructorMap[$2_tagname] = this
}
this._dbg_name = $1_prototype._dbg_typename = "lz." + $2_tagname
}
}
for (var $3_hash in {setters: true, getters: true, defaultattrs: true, options: true, __LZdelayedSetters: true, earlySetters: true}) {
if (!$1_prototype.hasOwnProperty($3_hash)) {
$1_prototype[$3_hash] = new LzInheritedHash($1_prototype[$3_hash])
}
}
}, _ignoreAttribute: {toString: (function  () {
var $lzsc$temp = 
/* -*- file: core/LzNode.lzs#789.42 -*- */
function  () {
return "_ignoreAttribute"
}
$lzsc$temp._dbg_name = "core/LzNode.lzs#789/42";
return $lzsc$temp
})()}, __UIDs: 0});
(function  () {
var $lzsc$temp = function  () {
with (LzNode) {
with (LzNode.prototype) {
DeclareEvent(prototype, "oninit");
DeclareEvent(prototype, "onconstruct");
DeclareEvent(prototype, "ondata");
DeclareEvent(prototype, "onclonenumber");
DeclareEvent(prototype, "ondestroy");
prototype.getProp = getAttribute;
prototype._setProp = setProp
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzUserClass = Class.make("LzUserClass", LzNode, {initialize: 
/* -*- file: core/UserClass.lzs#77.1 -*- */
function initialize ($1_parent, $2_args) {
var $3_classobj = $2_args.initobj;
var $4_classname = $3_classobj.name;
var $5_attrs = $3_classobj.attrs;
var $6_extend = $2_args.parent;
var $7_sup;
if ($6_extend == null) {
$7_sup = LzView
} else {
$7_sup = ConstructorMap[$6_extend]
}
if (!($7_sup instanceof Function && ($7_sup === LzNode || $7_sup.prototype instanceof LzNode))) {
Debug.error("Invalid superclass %w for %s", $7_sup, $4_classname)
}
var $8_traitList = null;
if ("with" in $5_attrs) {
$8_traitList = $5_attrs["with"].split(" ").join("").split(",");
for (var $9_i = $8_traitList.length - 1;
$9_i >= 0;
$9_i--) {
var $10_n = $8_traitList[$9_i];
$8_traitList[$9_i] = global[$10_n];
if ($8_traitList[$9_i] == null || $8_traitList[$9_i].makeInterstitial == null) {
Debug.error("LzUserClass.initialize: skipping invalid trait %w for class %w", $10_n, $4_classname);
$8_traitList.splice($9_i, 1)
}
}
$8_traitList.push($7_sup)
}
var $11_newclass = this.userclass = Class.make($4_classname, $8_traitList ? $8_traitList : $7_sup, null, {tagname: $4_classname});
if (!($4_classname in global)) {
global[$4_classname] = $11_newclass
} else {
Debug.info("The global `%s` is already defined.  To dynamically create a <%s> element, you will have to use `%s`.", $4_classname, $4_classname, $11_newclass)
}
var $12_newproto = $11_newclass.prototype;
delete $5_attrs.name;
var $13_cc;
if ("classChildren" in $7_sup.prototype && $7_sup.prototype.classChildren.length) {
$13_cc = $7_sup.prototype.classChildren.concat()
} else {
$13_cc = []
}
if ("children" in $3_classobj && $3_classobj.children.length) {
$13_cc = $13_cc.concat($3_classobj.children)
}
if ("defaultplacement" in $5_attrs && $5_attrs.defaultplacement != null) {
$13_cc.push({name: "__LZUserClassPlacementObject", attrs: $5_attrs.defaultplacement});
delete $5_attrs.defaultplacement
}
if ($13_cc.length) {
$12_newproto.addProperty("classChildren", $13_cc)
}
var $14_haveone = false;
if ("$events" in $5_attrs) {
var $15_events = $5_attrs.$events;
var $4_classname = $12_newproto.classname;
var $16_elen = $15_events.length;
for (var $17_k = 0;
$17_k < $16_elen;
$17_k++) {
var $18_eventName = $15_events[$17_k];
$12_newproto.addProperty($18_eventName, LzDeclaredEvent)
}
delete $5_attrs.$events
}
var $19_customsetters = "$setters" in $5_attrs ? $5_attrs.$setters : null;
if ($19_customsetters) {
delete $5_attrs.$setters;
for (var $20_s in $19_customsetters) {
var $21_attrSet = "_anonSet" + $20_s;
$12_newproto.addProperty($21_attrSet, $19_customsetters[$20_s]);
$12_newproto.setters[$20_s] = $21_attrSet
}
}
var $22_dattrs = $12_newproto.defaultattrs;
if ($5_attrs) {
var $23_dattr$refs = "$refs" in $22_dattrs && $22_dattrs.$refs;
var $24_cleanup = null;
var $25_setters = $12_newproto.setters;
var $26_isstate = "$isstate" in $12_newproto && $12_newproto.$isstate;
for (var $17_k in $5_attrs) {
var $27_attrk = $5_attrs[$17_k];
if ($23_dattr$refs && $23_dattr$refs[$17_k]) {
if (!$24_cleanup) {
$24_cleanup = {}
}
$24_cleanup[$17_k] = true
}
if ($25_setters[$17_k] == null && !$26_isstate) {
if ($27_attrk instanceof Object && !($27_attrk instanceof Function)) {

}
$12_newproto.addProperty($17_k, $27_attrk);
continue
}
if ($27_attrk instanceof Object) {
var $28_dattrk = $22_dattrs[$17_k];
if ($28_dattrk instanceof Object) {
if ($27_attrk instanceof Array) {
$22_dattrs[$17_k] = $27_attrk.concat($28_dattrk);
continue
} else {
if (typeof $27_attrk == "object") {
var $29_tmp = new LzInheritedHash($28_dattrk);
for (var $30_j in $27_attrk) {
$29_tmp[$30_j] = $27_attrk[$30_j]
}
$22_dattrs[$17_k] = $29_tmp;
continue
}
}
}
}
$22_dattrs[$17_k] = $27_attrk
}
if ($24_cleanup) {
if (!$22_dattrs.hasOwnProperty("$refs")) {
$22_dattrs.$refs = new LzInheritedHash($23_dattr$refs)
}
var $31_ia = LzNode._ignoreAttribute;
for (var $17_k in $24_cleanup) {
$22_dattrs.$refs[$17_k] = $31_ia
}
}
}
}, _dbg_name: 
/* -*- file: core/UserClass.lzs#276.5 -*- */
function _dbg_name () {
return Debug.formatToString("for %s", this.userclass)
}}, {tagname: "class"});
(function  () {
var $lzsc$temp = function  () {
with (LzUserClass) {
with (LzUserClass.prototype) {

}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
ConstructorMap.__LZUserClassPlacementObject = 
/* -*- file: core/UserClass.lzs#288.47 -*- */
function __LZUserClassPlacementObject ($1_parent, $2_placement) {
$1_parent.defaultplacement = $2_placement
}
var LzDelegate = Class.make("LzDelegate", null, {initialize: 
/* -*- file: events/LaszloEvents.lzs#35.1 -*- */
function initialize ($1_context, $2_functionName, $3_eventSender, $4_eventName) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
this.c = $1_context;
if (typeof $2_functionName != "string") {
Debug.warn("LzDelegate functionName must be a string in %w", arguments.caller)
}
this.f = $2_functionName;
if ($3_eventSender != null) {
if (typeof $4_eventName != "string") {
Debug.warn("LzDelegate eventName must be a string in %w", arguments.caller)
}
this.register($3_eventSender, $4_eventName)
}
this.__delegateID = LzDelegate.__nextID++
}, c: null, f: null, lastevent: 0, enabled: true, event_called: false, execute: 
/* -*- file: events/LaszloEvents.lzs#85.1 -*- */
function execute ($1_sd) {
var $2_context = this.c;
if ($2_context) {
if ($2_context["__LZdeleted"]) {
return
}
return $2_context[this.f]($1_sd)
}
}, register: 
/* -*- file: events/LaszloEvents.lzs#108.1 -*- */
function register ($1_eventSender, $2_eventName) {
if (!$1_eventSender) {
Debug.error("No eventSender (%w) for %s", $1_eventSender, $2_eventName);
return
}
var $3_anEvent = $1_eventSender[$2_eventName];
if ($3_anEvent == LzDeclaredEvent || !$3_anEvent) {
$3_anEvent = new LzEvent($1_eventSender, $2_eventName, this)
} else {
$3_anEvent.addDelegate(this)
}
this[this.lastevent++] = $3_anEvent;
var $3_anEvent = $1_eventSender[$2_eventName];
if (!$3_anEvent.hasOwnProperty("_dbg_eventSender")) {
$3_anEvent._dbg_eventSender = $1_eventSender;
$3_anEvent._dbg_eventName = $2_eventName
}
}, unregisterAll: 
/* -*- file: events/LaszloEvents.lzs#149.1 -*- */
function unregisterAll () {
for (var $1_i = 0;
$1_i < this.lastevent;
$1_i++) {
this[$1_i].removeDelegate(this);
this[$1_i] = null
}
this.lastevent = 0
}, unregisterFrom: 
/* -*- file: events/LaszloEvents.lzs#162.1 -*- */
function unregisterFrom ($1_event) {
var $2_keep = [];
for (var $3_i = 0;
$3_i < this.lastevent;
$3_i++) {
var $4_ev = this[$3_i];
if ($4_ev == $1_event) {
$4_ev.removeDelegate(this)
} else {
$2_keep.push($4_ev)
}
this[$3_i] = null
}
this.lastevent = 0;
var $5_l = $2_keep.length;
for (var $3_i = 0;
$3_i < $5_l;
$3_i++) {
this[this.lastevent++] = $2_keep[$3_i]
}
}, disable: 
/* -*- file: events/LaszloEvents.lzs#184.1 -*- */
function disable () {
this.enabled = false
}, enable: 
/* -*- file: events/LaszloEvents.lzs#191.1 -*- */
function enable () {
this.enabled = true
}, toString: 
/* -*- file: events/LaszloEvents.lzs#225.1 -*- */
function toString () {
return "Delegate for " + this.c + " calls " + this.f + " " + this.__delegateID
}, _dbg_name: 
/* -*- file: events/LaszloEvents.lzs#234.1 -*- */
function _dbg_name () {
var $1_name = Debug.formatToString("%0.48w.%s()", this.c, this.f);
if (this[0]) {
$1_name += Debug.formatToString("/* handles %w%s */", this[0], this[1] ? " ..." : "")
}
return $1_name
}}, {__nextID: 1, __LZdelegatesQueue: [], __LZdrainDelegatesQueue: 
/* -*- file: events/LaszloEvents.lzs#205.10 -*- */
function __LZdrainDelegatesQueue ($1_pos) {
var $2_evq = this.__LZdelegatesQueue;
var $3_n = $2_evq.length;
var $4_i = $1_pos;
while ($4_i < $3_n) {
var $5_d = $2_evq[$4_i];
var $6_sd = $2_evq[$4_i + 1];
if ($5_d.c[$5_d.f]) {
$5_d.c[$5_d.f]($6_sd)
}
$4_i += 2
}
$2_evq.length = $1_pos
}});
var LzEvent = Class.make("LzEvent", null, {initialize: 
/* -*- file: events/LaszloEvents.lzs#343.1 -*- */
function initialize ($1_eventSender, $2_eventName, $3_d) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
var $4__evs = $1_eventSender._events;
if ($4__evs == null) {
$1_eventSender._events = [this]
} else {
$4__evs.push(this)
}
$1_eventSender[$2_eventName] = this;
if ($3_d) {
this.delegateList = [$3_d];
this.ready = true
} else {
this.delegateList = []
}
this._dbg_eventSender = $1_eventSender;
this._dbg_eventName = $2_eventName
}, locked: false, ready: false, addDelegate: 
/* -*- file: events/LaszloEvents.lzs#390.1 -*- */
function addDelegate ($1_d) {
this.ready = true;
this.delegateList.push($1_d)
}, sendEvent: 
/* -*- file: events/LaszloEvents.lzs#403.1 -*- */
function sendEvent ($1_sd) {
if (this.locked) {
return
}
var $2_dll = this.delegateList.length;
if ($2_dll == 0) {
return
}
this.locked = true;
var $4_calledDelegates = new Array();
var $5_d;
var $6_evq = LzDelegate.__LZdelegatesQueue;
for (var $7_i = $2_dll;
$7_i >= 0;
$7_i--) {
$5_d = this.delegateList[$7_i];
if ($5_d && !$5_d.event_called) {
$5_d.event_called = true;
$4_calledDelegates.push($5_d);
if ($5_d.enabled && $5_d.c) {
if ($5_d.c.__LZdeferDelegates) {
$6_evq.push($5_d, $1_sd)
} else {
if ($5_d.c[$5_d.f]) {
$5_d.c[$5_d.f]($1_sd)
}
}
}
}
}
while ($5_d = $4_calledDelegates.pop()) {
$5_d.event_called = false
}
this.locked = false
}, removeDelegate: 
/* -*- file: events/LaszloEvents.lzs#467.1 -*- */
function removeDelegate ($1_d) {
var $2_dll = this.delegateList.length;
for (var $3_i = 0;
$3_i < $2_dll;
$3_i++) {
if (this.delegateList[$3_i] == $1_d) {
this.delegateList.splice($3_i, 1);
break
}
}
if (this.delegateList.length == 0) {
this.ready = false
}
}, clearDelegates: 
/* -*- file: events/LaszloEvents.lzs#483.1 -*- */
function clearDelegates () {
while (this.delegateList.length) {
this.delegateList[0].unregisterFrom(this)
}
this.ready = false
}, getDelegateCount: 
/* -*- file: events/LaszloEvents.lzs#495.1 -*- */
function getDelegateCount () {
return this.delegateList.length
}, toString: 
/* -*- file: events/LaszloEvents.lzs#499.1 -*- */
function toString () {
return "LzEvent"
}, _dbg_name: 
/* -*- file: events/LaszloEvents.lzs#508.1 -*- */
function _dbg_name () {
return Debug.formatToString("%0.48w.%s", this._dbg_eventSender, this._dbg_eventName)
}}, null);
var LzIdleKernel = {__callbacks: [], addCallback: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzIdleKernel.lzs#19.19 -*- */
function  ($1_scope, $2_funcname) {
LzIdleKernel.__callbacks.push([$1_scope, $2_funcname])
}
$lzsc$temp._dbg_name = "kernel/LzIdleKernel.lzs#19/19";
return $lzsc$temp
})(), removeCallback: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzIdleKernel.lzs#22.22 -*- */
function  ($1_scope, $2_funcname) {
for (var $3_i = LzIdleKernel.__callbacks.length - 1;
$3_i >= 0;
$3_i--) {
if (LzIdleKernel.__callbacks[$3_i][0] == $1_scope && LzIdleKernel.__callbacks[$3_i][1] == $2_funcname) {
return LzIdleKernel.__callbacks.splice($3_i, 1)
}
}
}
$lzsc$temp._dbg_name = "kernel/LzIdleKernel.lzs#22/22";
return $lzsc$temp
})(), __update: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzIdleKernel.lzs#29.16 -*- */
function  () {
for (var $1_i = LzIdleKernel.__callbacks.length - 1;
$1_i >= 0;
$1_i--) {
var $2_s = LzIdleKernel.__callbacks[$1_i][0];
$2_s[LzIdleKernel.__callbacks[$1_i][1]](getTimer())
}
}
$lzsc$temp._dbg_name = "kernel/LzIdleKernel.lzs#29/16";
return $lzsc$temp
})()}
setInterval(LzIdleKernel.__update, 33);
var LzUtils = {callback: {__scopeid: 0, __scopes: [], getcallbackstr: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#20.26 -*- */
function  ($1_scope, $2_name) {
var $3_sc = LzUtils.callback.__scopeid++;
if ($1_scope.__callbacks == null) {
$1_scope.__callbacks = {sc: $3_sc}
} else {
$1_scope.__callbacks[$3_sc] = $3_sc
}
LzUtils.callback.__scopes[$3_sc] = $1_scope;
return "if (LzUtils.callback.__scopes[" + $3_sc + "]) LzUtils.callback.__scopes[" + $3_sc + "]." + $2_name + ".apply(LzUtils.callback.__scopes[" + $3_sc + "], [])"
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#20/26";
return $lzsc$temp
})(), getcallbackfunc: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#34.27 -*- */
function  ($1_scope, name, args) {
var sc = LzUtils.callback.__scopeid++;
if ($1_scope.__callbacks == null) {
$1_scope.__callbacks = {sc: sc}
} else {
$1_scope.__callbacks[sc] = sc
}
LzUtils.callback.__scopes[sc] = $1_scope;
return (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#42.20 -*- */
function  () {
var $1_s = LzUtils.callback.__scopes[sc];
if ($1_s) {
return $1_s[name].apply($1_s, args)
}
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#42/20";
return $lzsc$temp
})()
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#34/27";
return $lzsc$temp
})(), remove: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#47.18 -*- */
function  ($1_scope) {
if ($1_scope.__callbacks != null) {
for (var $2_i in $1_scope.__callbacks) {
var $3_sc = $1_scope.__callbacks[$2_i];
delete LzUtils.callback.__scopes[$3_sc]
}
delete $1_scope.__callbacks
}
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#47/18";
return $lzsc$temp
})()}, dectohex: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#58.16 -*- */
function  ($1_c, $2_p) {
if (typeof $1_c == "number") {
var $3_hex = $1_c.toString(16);
var $4_pad = ($2_p ? $2_p : 0) - $3_hex.length;
while ($4_pad > 0) {
$3_hex = "0" + $3_hex;
$4_pad--
}
return $3_hex
} else {
return $1_c
}
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#58/16";
return $lzsc$temp
})(), color: {hextoint: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#73.19 -*- */
function  ($1_value) {
if (typeof $1_value != "string") {
return $1_value
}
if ($1_value.charAt(0) == "#") {
var $2_n = parseInt($1_value.slice(1), 16);
switch (!isNaN($2_n) && $1_value.length - 1) {
case 3:
return (($2_n & 3840) << 8 | ($2_n & 240) << 4 | $2_n & 15) * 17;
case 6:
return $2_n;
default:
Debug.warn("invalid color: " + $1_value);

}
}
if (typeof eval($1_value) == "number") {
return eval($1_value)
}
Debug.warn("unknown color format: " + $1_value);
return 0
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#73/19";
return $lzsc$temp
})(), inttohex: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#96.20 -*- */
function  ($1_c) {
if (typeof $1_c == "string") {
$1_c = $1_c * 1
}
if (typeof $1_c == "number") {
var $2_hex = LzUtils.dectohex($1_c, 6);
$1_c = "#" + $2_hex
}
return $1_c
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#96/20";
return $lzsc$temp
})(), torgb: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/LzUtils.lzs#107.17 -*- */
function  ($1_s) {
if (typeof $1_s == "number") {
$1_s = this.inttohex($1_s)
}
if (typeof $1_s != "string") {
return $1_s
}
if ($1_s.length < 6) {
$1_s = "#" + $1_s.charAt(1) + $1_s.charAt(1) + $1_s.charAt(2) + $1_s.charAt(2) + $1_s.charAt(3) + $1_s.charAt(3) + ($1_s.length > 4 ? $1_s.charAt(4) + $1_s.charAt(4) : "")
}
return "rgb(" + parseInt($1_s.substring(1, 3), 16) + "," + parseInt($1_s.substring(3, 5), 16) + "," + parseInt($1_s.substring(5, 7), 16) + ($1_s.length > 7 ? "," + parseInt($1_s.substring(7, 9), 16) : "") + ")"
}
$lzsc$temp._dbg_name = "kernel/LzUtils.lzs#107/17";
return $lzsc$temp
})()}}
var LzLibraryCleanup = Class.make("LzLibraryCleanup", LzNode, {initialize: 
/* -*- file: kernel/LzLibraryCleanup.lzs#22.5 -*- */
function initialize ($1_owner, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
var $3_lib = LzLibrary.findLibrary($2_args.libname);
$3_lib.loading = false;
if ($3_lib.onload.ready) {
$3_lib.onload.sendEvent(true)
}
}}, {tagname: "__libraryloadercomplete"});
var LzResourceLibrary = {}
var getTimer = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LFC.js#19.20 -*- */
function  () {
return new Date().valueOf() - getTimer.startTime
}
$lzsc$temp._dbg_name = "kernel/dhtml/LFC.js#19/20";
return $lzsc$temp
})();
getTimer.startTime = new Date().valueOf();
global = window;
var LzPool = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzPool.js#13.14 -*- */
function  ($1_getter, $2_cacheHit, $3_destroyer, $4_owner) {
this.cache = {}
if (typeof $1_getter == "function") {
this.getter = $1_getter
}
if (typeof $2_cacheHit == "function") {
this.cacheHit = $2_cacheHit
}
if (typeof $3_destroyer == "function") {
this.destroyer = $3_destroyer
}
if ($4_owner) {
this.owner = $4_owner
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzPool.js#13/14";
return $lzsc$temp
})();
LzPool.prototype.cache = null;
LzPool.prototype.get = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzPool.js#25.24 -*- */
function  ($1_id) {
if (this.cache[$1_id] == null) {
this.cache[$1_id] = this.getter($1_id, Array.prototype.slice.apply(arguments, [1]))
} else {
if (this.cacheHit) {
this.cacheHit($1_id, this.cache[$1_id], Array.prototype.slice.apply(arguments, [1]))
}
}
if (this.owner) {
this.cache[$1_id].owner = this.owner
}
return this.cache[$1_id]
}
$lzsc$temp._dbg_name = "LzPool.prototype.get";
return $lzsc$temp
})();
LzPool.prototype.flush = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzPool.js#35.26 -*- */
function  ($1_id) {
if (this.destroyer) {
this.destroyer($1_id, this.cache[$1_id])
}
this.cache[$1_id] = null
}
$lzsc$temp._dbg_name = "LzPool.prototype.flush";
return $lzsc$temp
})();
LzPool.prototype.destroy = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzPool.js#41.28 -*- */
function  () {
for (var $1_id in this.cache) {
this.flush($1_id)
}
this.owner = null;
this.cache = null
}
$lzsc$temp._dbg_name = "LzPool.prototype.destroy";
return $lzsc$temp
})();
LzPool.prototype.getter = null;
LzPool.prototype.destroyer = null;
LzPool.prototype.cacheHit = null;
var LzKeyboardKernel = {__downKeysHash: {}, __keyboardEvent: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzKeyboardKernel.js#15.23 -*- */
function  ($1_e) {
if (!$1_e) {
$1_e = window.event
}
var $2_delta = {}
var $3_dirty = false;
var $4_k = $1_e["keyCode"];
var $5_dh = LzKeyboardKernel.__downKeysHash;
if ($4_k >= 0) {
var $6_s = String.fromCharCode($4_k).toLowerCase();
var $7_t = $1_e.type;
if ($7_t == "keyup") {
if ($5_dh[$6_s] != false) {
$2_delta[$6_s] = false;
$3_dirty = true
}
$5_dh[$6_s] = false
} else {
if ($7_t == "keydown") {
if ($5_dh[$6_s] != true) {
$2_delta[$6_s] = true;
$3_dirty = true
}
$5_dh[$6_s] = true
}
}
}
if ($5_dh["alt"] != $1_e["altKey"]) {
$2_delta["alt"] = $1_e["altKey"];
$3_dirty = true;
if (LzSprite.prototype.quirks["alt_key_sends_control"]) {
$2_delta["control"] = $2_delta["alt"]
}
}
if ($5_dh["control"] != $1_e["ctrlKey"]) {
$2_delta["control"] = $1_e["ctrlKey"];
$3_dirty = true
}
if ($5_dh["shift"] != $1_e["shiftKey"]) {
$2_delta["shift"] = $1_e["shiftKey"];
$3_dirty = true
}
$5_dh["alt"] = $1_e["altKey"];
$5_dh["control"] = $1_e["ctrlKey"];
$5_dh["shift"] = $1_e["shiftKey"];
if ($3_dirty && LzKeyboardKernel.__callback) {
LzKeyboardKernel.__scope[LzKeyboardKernel.__callback]($2_delta, $4_k, "on" + $7_t)
}
if ($4_k >= 0) {
if ($4_k == 9) {
$1_e.cancelBubble = true;
$1_e.returnValue = false;
return false
} else {
if (LzKeyboardKernel.__cancelKeys && ($4_k == 13 || $4_k == 0 || $4_k == 37 || $4_k == 38 || $4_k == 39 || $4_k == 40)) {
$1_e.cancelBubble = true;
$1_e.returnValue = false;
return false
}
}
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzKeyboardKernel.js#15/23";
return $lzsc$temp
})(), __mousewheelEvent: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzKeyboardKernel.js#75.25 -*- */
function  ($1_e) {
if (!$1_e) {
$1_e = window.event
}
var $2_delta = 0;
if ($1_e.wheelDelta) {
$2_delta = $1_e.wheelDelta / 120;
if (LzSprite.prototype.quirks["reverse_mouse_wheel"]) {
$2_delta = -$2_delta
}
} else {
if ($1_e.detail) {
$2_delta = -$1_e.detail / 3
}
}
if ($1_e.preventDefault) {
$1_e.preventDefault()
}
$1_e.returnValue = false;
if ($2_delta && LzKeyboardKernel.__mousewheelcallback) {
LzKeyboardKernel.__scope[LzKeyboardKernel.__mousewheelcallback]($2_delta)
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzKeyboardKernel.js#75/25";
return $lzsc$temp
})(), __callback: null, __mousewheelcallback: null, __scope: null, __cancelKeys: true, setCallback: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzKeyboardKernel.js#94.19 -*- */
function  ($1_scope, $2_keyboardcallback, $3_mousewheelcallback) {
this.__scope = $1_scope;
this.__callback = $2_keyboardcallback;
this.__mousewheelcallback = $3_mousewheelcallback;
if (lzOptions.dhtmlKeyboardControl != false) {
document.onkeydown = LzKeyboardKernel.__keyboardEvent;
document.onkeyup = LzKeyboardKernel.__keyboardEvent;
document.onkeypress = LzKeyboardKernel.__keyboardEvent;
if (window.addEventListener) {
window.addEventListener("DOMMouseScroll", LzKeyboardKernel.__mousewheelEvent, false)
}
document.onmousewheel = LzKeyboardKernel.__mousewheelEvent
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzKeyboardKernel.js#94/19";
return $lzsc$temp
})()}
var LzMouseKernel = {__lastMouseDown: null, __x: 0, __y: 0, owner: null, __showncontextmenu: null, __defaultcontextmenu: null, __mouseEvent: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzMouseKernel.js#21.20 -*- */
function  ($1_e) {
if (!$1_e) {
$1_e = window.event
}
var $2_eventname = "on" + $1_e.type;
if (window["LzKeyboardKernel"]) {
LzKeyboardKernel.__keyboardEvent($1_e)
}
if (window["LzInputTextSprite"] && $2_eventname != "onmousemove" && LzInputTextSprite.prototype.__lastshown != null) {
LzInputTextSprite.prototype.__hideIfNotFocused()
}
if ($2_eventname == "onmouseup" && LzMouseKernel.__lastMouseDown != null) {
LzMouseKernel.__lastMouseDown.__globalmouseup($1_e)
} else {
if ($2_eventname == "onmousemove") {
if ($1_e.pageX || $1_e.pageY) {
LzMouseKernel.__x = $1_e.pageX;
LzMouseKernel.__y = $1_e.pageY
} else {
if ($1_e.clientX || $1_e.clientY) {
LzMouseKernel.__x = $1_e.clientX;
LzMouseKernel.__y = $1_e.clientY
}
}
}
}
if (LzMouseKernel.__callback) {
if ($1_e.button == 2 && $2_eventname != "oncontextmenu") {
return
}
if ($2_eventname == "oncontextmenu") {
var $3_targ = $1_e.srcElement ? $1_e.srcElement : $1_e.target;
if ($3_targ && $3_targ.owner && $3_targ.owner.__contextmenu) {
$3_targ.owner.__contextmenu.__show();
return $3_targ.owner.__contextmenu.showbuiltins
} else {
if (LzMouseKernel.__defaultcontextmenu) {
LzMouseKernel.__defaultcontextmenu.__show();
return LzMouseKernel.__defaultcontextmenu.showbuiltins
}
}
} else {
return LzMouseKernel.__scope[LzMouseKernel.__callback]($2_eventname)
}
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzMouseKernel.js#21/20";
return $lzsc$temp
})(), __callback: null, __scope: null, setCallback: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzMouseKernel.js#57.19 -*- */
function  ($1_scope, $2_funcname) {
this.__scope = $1_scope;
this.__callback = $2_funcname;
document.onmousemove = LzMouseKernel.__mouseEvent;
document.onmousedown = LzMouseKernel.__mouseEvent;
document.onmouseup = LzMouseKernel.__mouseEvent;
document.oncontextmenu = LzMouseKernel.__mouseEvent
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzMouseKernel.js#57/19";
return $lzsc$temp
})(), __showhand: "pointer", showHandCursor: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzMouseKernel.js#72.22 -*- */
function  ($1_show) {
var $2_c = $1_show == true ? "pointer" : "default";
this.__showhand = $2_c;
LzMouseKernel.setCursorGlobal($2_c)
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzMouseKernel.js#72/22";
return $lzsc$temp
})(), setCursorGlobal: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzMouseKernel.js#83.23 -*- */
function  ($1_n) {
if (LzSprite.prototype.quirks.no_cursor_colresize) {
return
}
var $1_n = LzSprite.prototype.__defaultStyles.hyphenate($1_n);
LzSprite.prototype.__setCSSClassProperty(".lzclickdiv", "cursor", $1_n)
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzMouseKernel.js#83/23";
return $lzsc$temp
})(), restoreCursor: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzMouseKernel.js#97.21 -*- */
function  () {
if (LzSprite.prototype.quirks.no_cursor_colresize) {
return
}
if (LzMouseKernel.__amLocked) {
return
}
LzSprite.prototype.__setCSSClassProperty(".lzclickdiv", "cursor", LzMouseKernel.__showhand)
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzMouseKernel.js#97/21";
return $lzsc$temp
})(), lock: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzMouseKernel.js#109.12 -*- */
function  () {
LzMouseKernel.__amLocked = true
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzMouseKernel.js#109/12";
return $lzsc$temp
})(), unlock: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzMouseKernel.js#117.14 -*- */
function  () {
LzMouseKernel.__amLocked = false;
LzMouseKernel.restoreCursor()
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzMouseKernel.js#117/14";
return $lzsc$temp
})()}
var LzSprite = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#12.16 -*- */
function  ($1_owner, $2_isroot) {
if ($1_owner == null) {
return
}
if ($2_isroot) {
this.isroot = true;
LzSprite.__rootSprite = this;
var $3_div = document.createElement("div");
$3_div.className = "lzcanvasdiv";
var $4_p = Lz.__propcache;
var $5_root = $4_p.appenddiv;
if ($4_p.bgcolor) {
$3_div.style.backgroundColor = $4_p.bgcolor
}
if ($4_p.width) {
$3_div.style.width = $4_p.width
}
if ($4_p.height) {
$3_div.style.height = $4_p.height
}
if ($4_p.id) {
this._id = $4_p.id
}
if (this.quirks.canvas_div_cannot_be_clipped == false && $4_p.width && $4_p.width.indexOf("%") == -1 && $4_p.height && $4_p.height.indexOf("%") == -1) {
$3_div.style.clip = "rect(0px " + $4_p.width + " " + $4_p.height + " 0px)";
$3_div.style.overflow = "hidden"
}
$5_root.appendChild($3_div);
this.__LZdiv = $3_div;
if (this.quirks.fix_clickable) {
var $6_cdiv = document.createElement("div");
$6_cdiv.className = "lzcanvasclickdiv";
$5_root.appendChild($6_cdiv);
this.__LZclickdiv = $6_cdiv
}
} else {
this.__LZdiv = document.createElement("div");
this.__LZdiv.className = "lzdiv";
if (this.quirks.fix_clickable) {
this.__LZclickdiv = document.createElement("div");
this.__LZclickdiv.className = "lzdiv"
}
}
this.__LZdiv.owner = this;
if (this.quirks.fix_clickable) {
this.__LZclickdiv.owner = this
}
this.owner = $1_owner;
this.uid = LzSprite.prototype.uid++;
if (this.quirks.ie_leak_prevention) {
this.__sprites[this.uid] = this
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzSprite.js#12/16";
return $lzsc$temp
})();
LzSprite.prototype.__defaultStyles = {lzdiv: {position: "absolute"}, lzclickdiv: {position: "absolute"}, lzinputclickdiv: {position: "absolute"}, lzcanvasdiv: {position: "absolute"}, lzcanvasclickdiv: {zIndex: 100000, position: "absolute"}, lztext: {fontFamily: "Verdana,Vera,sans-serif", fontStyle: "normal", fontWeight: "normal", fontSize: "11px", whiteSpace: "normal", position: "absolute"}, lzswftext: {fontFamily: "Verdana,Vera,sans-serif", fontStyle: "normal", fontWeight: "normal", fontSize: "11px", whiteSpace: "normal", position: "absolute", paddingTop: "2px", paddingLeft: "2px"}, lzinputtext: {fontFamily: "Verdana,Vera,sans-serif", fontStyle: "normal", fontWeight: "normal", fontSize: "11px", width: "100%", height: "100%", borderWidth: 0, backgroundColor: "transparent"}, lzswfinputtext: {fontFamily: "Verdana,Vera,sans-serif", fontStyle: "normal", fontWeight: "normal", fontSize: "11px", width: "100%", height: "100%", borderWidth: 0, backgroundColor: "transparent", marginTop: "1px", marginLeft: "2px"}, lzswfinputtextmultiline: {fontFamily: "Verdana,Vera,sans-serif", fontStyle: "normal", fontWeight: "normal", fontSize: "11px", width: "100%", height: "100%", borderWidth: 0, backgroundColor: "transparent", marginTop: "2px", marginLeft: "2px"}, writeCSS: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#137.15 -*- */
function  () {
var $1_css = "";
for (var $2_classname in this) {
if ($2_classname == "writeCSS" || $2_classname == "hyphenate" || $2_classname == "__re") {
continue
}
$1_css += $2_classname.indexOf("#") == -1 ? "." : "";
$1_css += $2_classname + "{";
for (var $3_n in this[$2_classname]) {
var $4_v = this[$2_classname][$3_n];
$1_css += this.hyphenate($3_n) + ":" + $4_v + ";"
}
$1_css += "}"
}
document.write('<style type="text/css">' + $1_css + "</style>")
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzSprite.js#137/15";
return $lzsc$temp
})(), __re: new RegExp("[A-Z]"), hyphenate: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#154.16 -*- */
function  ($1_n) {
var $2_i = $1_n.search(this.__re);
if ($2_i != -1) {
var $3_c = $1_n.substring($2_i, $2_i + 1);
$1_n = $1_n.substring(0, $2_i) + "-" + $3_c.toLowerCase() + $1_n.substring($2_i + 1, $1_n.length)
}
return $1_n
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzSprite.js#154/16";
return $lzsc$temp
})()}
LzSprite.prototype.__defaultStyles["#lzcontextmenu a"] = {color: "#000", display: "block", textDecoration: "none"}
LzSprite.prototype.__defaultStyles["#lzcontextmenu a:hover"] = {color: "#FFF", backgroundColor: "#333"}
LzSprite.prototype.__defaultStyles["#lzcontextmenu"] = {position: "absolute", zIndex: 10000000, backgroundColor: "#CCC", border: "1px outset #999", padding: "4px", fontFamily: "Verdana,Vera,sans-serif", fontSize: "13px", margin: "2px", color: "#999"}
LzSprite.prototype.uid = 0;
LzSprite.prototype.quirks = {fix_clickable: true, fix_ie_background_height: false, fix_ie_clickable: false, ie_alpha_image_loader: false, ie_leak_prevention: false, invisible_parent_image_sizing_fix: false, emulate_flash_font_metrics: true, inner_html_strips_newlines: true, inner_html_no_entity_apos: false, css_hide_canvas_during_init: true, firefox_autocomplete_bug: false, hand_pointer_for_clickable: true, alt_key_sends_control: false, safari_textarea_subtract_scrollbar_height: false, safari_avoid_clip_position_input_text: false, reverse_mouse_wheel: false, no_cursor_colresize: false, safari_visibility_instead_of_display: false, preload_images_only_once: false, absolute_position_accounts_for_offset: false, canvas_div_cannot_be_clipped: false, inputtext_parents_cannot_contain_clip: false, minimize_opacity_changes: false, set_height_for_multiline_inputtext: false, ie_offset_position_by_2: false}
LzSprite.prototype.capabilities = {rotation: false, scalecanvastopercentage: true, opacity: true, colortransform: false, audio: false, accessibility: false, htmlinputtext: false, advancedfonts: false}
LzSprite.prototype.__updateQuirks = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#227.37 -*- */
function  () {
if (window["Lz"] && Lz.__BrowserDetect) {
Lz.__BrowserDetect.init();
if (this.quirks["inner_html_strips_newlines"] == true) {
LzSprite.prototype.inner_html_strips_newlines_re = RegExp("$", "mg")
}
if (Lz.__BrowserDetect.isIE) {
this.quirks["ie_alpha_image_loader"] = true;
this.quirks["ie_leak_prevention"] = true;
this.quirks["fix_ie_clickable"] = true;
this.quirks["fix_ie_background_height"] = true;
this.quirks["inner_html_no_entity_apos"] = true;
this.quirks["inputtext_parents_cannot_contain_clip"] = true;
this.quirks["minimize_opacity_changes"] = true;
this.quirks["set_height_for_multiline_inputtext"] = true;
if (Lz.__BrowserDetect.version > 6) {
this.quirks["ie_offset_position_by_2"] = true
}
} else {
if (Lz.__BrowserDetect.isSafari) {
this.quirks["invisible_parent_image_sizing_fix"] = true;
this.quirks["alt_key_sends_control"] = true;
this.quirks["safari_textarea_subtract_scrollbar_height"] = true;
this.quirks["safari_avoid_clip_position_input_text"] = true;
this.quirks["safari_visibility_instead_of_display"] = true;
this.quirks["absolute_position_accounts_for_offset"] = true;
this.quirks["canvas_div_cannot_be_clipped"] = true
} else {
if (Lz.__BrowserDetect.isOpera) {
this.quirks["invisible_parent_image_sizing_fix"] = true;
this.quirks["reverse_mouse_wheel"] = true;
this.quirks["no_cursor_colresize"] = true;
this.quirks["absolute_position_accounts_for_offset"] = true;
this.quirks["canvas_div_cannot_be_clipped"] = true
} else {
if (Lz.__BrowserDetect.isFirefox && Lz.__BrowserDetect.version < 2) {
this.quirks["firefox_autocomplete_bug"] = true
}
}
}
}
}
if (this.quirks["safari_avoid_clip_position_input_text"]) {
LzSprite.prototype.__defaultStyles.lzswfinputtext.marginTop = "-2px";
LzSprite.prototype.__defaultStyles.lzswfinputtext.marginLeft = "-2px";
LzSprite.prototype.__defaultStyles.lzswfinputtextmultiline.marginTop = "-2px";
LzSprite.prototype.__defaultStyles.lzswfinputtextmultiline.marginLeft = "-2px"
}
if (this.quirks["css_hide_canvas_during_init"]) {
if (this.quirks["safari_visibility_instead_of_display"]) {
LzSprite.prototype.__defaultStyles.lzcanvasdiv.visibility = "hidden"
} else {
LzSprite.prototype.__defaultStyles.lzcanvasdiv.display = "none"
}
LzSprite.prototype.__defaultStyles.lzcanvasclickdiv.display = "none"
}
if (this.quirks["hand_pointer_for_clickable"]) {
LzSprite.prototype.__defaultStyles.lzclickdiv.cursor = "pointer"
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__updateQuirks";
return $lzsc$temp
})();
LzSprite.prototype.__updateQuirks();
LzSprite.prototype.__defaultStyles.writeCSS();
LzSprite.prototype.__LZdiv = null;
LzSprite.prototype.__LZimg = null;
LzSprite.prototype.__LZclick = null;
LzSprite.prototype.x = null;
LzSprite.prototype.y = null;
LzSprite.prototype.opacity = null;
LzSprite.prototype.width = null;
LzSprite.prototype.height = null;
LzSprite.prototype.playing = false;
LzSprite.prototype.clickable = false;
LzSprite.prototype.frame = 1;
LzSprite.prototype.frames = null;
LzSprite.prototype.blankimage = lzOptions.ServerRoot + "/lps/includes/blank.gif";
LzSprite.prototype.resource = null;
LzSprite.prototype.source = null;
LzSprite.prototype.visible = null;
LzSprite.prototype.text = null;
LzSprite.prototype.clip = null;
LzSprite.prototype.stretches = null;
LzSprite.prototype.resourceWidth = null;
LzSprite.prototype.resourceHeight = null;
LzSprite.prototype.cursor = null;
LzSprite.prototype.init = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#354.27 -*- */
function  ($1_v) {
this.setVisible($1_v);
if (this.isroot) {
if (this.quirks["safari_visibility_instead_of_display"]) {
this.__LZdiv.style.visibility = "visible"
}
var $2_s = document.getElementById("lzsplash");
if ($2_s) {
LzSprite.prototype.__discardElement($2_s)
}
if (this._id) {
Lz[this._id]._ready()
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.init";
return $lzsc$temp
})();
LzSprite.prototype.__topZ = 1;
LzSprite.prototype.__parent = null;
LzSprite.prototype.__children = null;
LzSprite.prototype.addChildSprite = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#381.37 -*- */
function  ($1_sprite) {
if (this.stretches != null && this.__warnstretches != true) {
Debug.warn("Due to limitations in the DHTML runtime, stretches will only apply to resources in this view, and doesn't affect child views.");
this.__warnstretches = true
}
if (this.color != null && this.__warncolorcascade != true) {
Debug.warn("Due to limitations in the DHTML runtime, color will only apply to resources in this view, and doesn't affect child views.");
this.__warncolorcascade = true
}
$1_sprite.__parent = this;
if (this.__children) {
this.__children.push($1_sprite)
} else {
this.__children = [$1_sprite]
}
this.__LZdiv.appendChild($1_sprite.__LZdiv);
if (this.quirks.fix_clickable) {
this.__LZclickdiv.appendChild($1_sprite.__LZclickdiv)
}
$1_sprite.__setZ(++this.__topZ)
}
$lzsc$temp._dbg_name = "LzSprite.prototype.addChildSprite";
return $lzsc$temp
})();
LzSprite.prototype.setResource = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#409.34 -*- */
function  ($1_r) {
if (this.resource == $1_r) {
return
}
if ($1_r.indexOf("http:") == 0 || $1_r.indexOf("https:") == 0) {
this.skiponload = false;
this.setSource($1_r);
this.resource = $1_r;
return
}
this.resource = $1_r;
var $2_res = LzResourceLibrary[$1_r];
if (!$2_res) {
Debug.warn("Could not find resource", $1_r);
return
}
var $3_urls = $2_res.frames;
this.resourceWidth = $2_res.width;
this.resourceHeight = $2_res.height;
this.skiponload = true;
this.owner.setTotalFrames($3_urls.length);
var $4_url = $3_urls[0];
if ($4_url) {
this.baseurl = "";
if ($2_res.ptype) {
if ($2_res.ptype == "sr") {
this.baseurl = lzOptions.ServerRoot + "/"
}
}
this.frames = $3_urls;
this.__preloadFrames();
this.setSource($4_url, true)
} else {
this.setSource($1_r, true)
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setResource";
return $lzsc$temp
})();
LzSprite.prototype.CSSDimension = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#461.35 -*- */
function  ($1_value, $2_units) {
return Math.floor($1_value) + ($2_units ? $2_units : "px")
}
$lzsc$temp._dbg_name = "LzSprite.prototype.CSSDimension";
return $lzsc$temp
})();
LzSprite.prototype.loading = false;
LzSprite.prototype.setSource = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#466.32 -*- */
function  ($1_url, $2_indirect) {
if ($2_indirect != true) {
this.skiponload = false
}
this.loading = true;
this.source = $1_url;
if (!this.__ImgPool) {
this.__ImgPool = new LzPool(LzSprite.prototype.__getImage, LzSprite.prototype.__gotImage, LzSprite.prototype.__destroyImage, this)
}
var $3_im = this.__ImgPool.get($1_url);
if (this.__LZimg) {
this.__LZdiv.replaceChild($3_im, this.__LZimg);
this.__LZimg = $3_im
} else {
this.__LZimg = $3_im;
this.__LZdiv.appendChild(this.__LZimg)
}
if (this.stretches) {
this.__updateStretches()
}
if (this.clickable) {
this.setClickable(true)
}
if (this.quirks.ie_alpha_image_loader) {
this.__updateIEAlpha($3_im)
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setSource";
return $lzsc$temp
})();
if (LzSprite.prototype.quirks.ie_alpha_image_loader) {
LzSprite.prototype.__updateIEAlpha = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#501.42 -*- */
function  ($1_who) {
if ($1_who._hasax == true) {
return
}
var $2_w = this.resourceWidth;
var $3_h = this.resourceHeight;
if (this.stretches == "both") {
$2_w = "100%";
$3_h = "100%"
} else {
if (this.stretches == "width") {
$2_w = "100%"
} else {
if (this.stretches == "height") {
$3_h = "100%"
}
}
}
if ($2_w == null) {
$2_w = this.width == null ? "100%" : this.width
}
if ($3_h == null) {
$3_h = this.height == null ? "100%" : this.height
}
if ($2_w == null || $3_h == null) {
return
}
$1_who.style.width = $2_w;
$1_who.style.height = $3_h
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__updateIEAlpha";
return $lzsc$temp
})()
}
LzSprite.prototype.setFontName = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#527.34 -*- */
function  ($1_val, $2_prop) {
this.fontname = $1_val
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setFontName";
return $lzsc$temp
})();
LzSprite.prototype.setClickable = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#531.35 -*- */
function  ($1_c) {
$1_c = $1_c == true;
if (this.clickable == $1_c) {
return
}
if (this.__LZimg != null) {
if (this.__LZdiv._clickable) {
this.__setClickable(false, this.__LZdiv)
}
if (!this.__LZclick) {
if (this.quirks.fix_ie_clickable) {
this.__LZclick = document.createElement("img");
this.__LZclick.src = LzSprite.prototype.blankimage
} else {
this.__LZclick = document.createElement("div")
}
this.__LZclick.owner = this;
this.__LZclick.className = "lzclickdiv";
this.__LZclick.style.width = this.__LZdiv.style.width;
this.__LZclick.style.height = this.__LZdiv.style.height;
if (this.quirks.fix_clickable) {
this.__LZclickdiv.appendChild(this.__LZclick)
} else {
this.__LZdiv.appendChild(this.__LZclick)
}
}
this.__setClickable($1_c, this.__LZclick);
if (this.quirks.fix_clickable) {
if (this.quirks.fix_ie_clickable) {
this.__LZclickdiv.style.display = $1_c ? "" : "none";
this.__LZclick.style.display = $1_c ? "" : "none"
} else {
this.__LZclick.style.display = $1_c ? "block" : "none"
}
}
} else {
if (this.quirks.fix_clickable) {
if (!this.__LZclick) {
if (this.quirks.fix_ie_clickable) {
this.__LZclick = document.createElement("img");
this.__LZclick.src = LzSprite.prototype.blankimage
} else {
this.__LZclick = document.createElement("div")
}
this.__LZclick.owner = this;
this.__LZclick.className = "lzclickdiv";
this.__LZclick.style.width = this.__LZdiv.style.width;
this.__LZclick.style.height = this.__LZdiv.style.height;
this.__LZclickdiv.appendChild(this.__LZclick)
}
this.__setClickable($1_c, this.__LZclick);
if (this.quirks.fix_ie_clickable) {
this.__LZclick.style.display = $1_c ? "" : "none"
} else {
this.__LZclick.style.display = $1_c ? "block" : "none"
}
} else {
this.__setClickable($1_c, this.__LZdiv)
}
}
this.clickable = $1_c
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setClickable";
return $lzsc$temp
})();
LzSprite.prototype.__setClickable = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#599.37 -*- */
function  ($1_c, $2_who) {
if ($2_who._clickable == $1_c) {
return
}
$2_who._clickable = $1_c;
if ($1_c) {
$2_who.onclick = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#604.23 -*- */
function  ($1_e) {
this.owner.__mouseEvent($1_e);
return false
}
$lzsc$temp._dbg_name = "who.onclick";
return $lzsc$temp
})();
$2_who.onmouseover = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#605.27 -*- */
function  ($1_e) {
this.owner.__mouseEvent($1_e);
return false
}
$lzsc$temp._dbg_name = "who.onmouseover";
return $lzsc$temp
})();
$2_who.onmouseout = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#606.26 -*- */
function  ($1_e) {
this.owner.__mouseEvent($1_e);
return false
}
$lzsc$temp._dbg_name = "who.onmouseout";
return $lzsc$temp
})();
$2_who.onmousedown = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#608.27 -*- */
function  ($1_e) {
this.owner.__mouseEvent($1_e);
return false
}
$lzsc$temp._dbg_name = "who.onmousedown";
return $lzsc$temp
})();
$2_who.onmouseup = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#609.25 -*- */
function  ($1_e) {
this.owner.__mouseEvent($1_e);
return false
}
$lzsc$temp._dbg_name = "who.onmouseup";
return $lzsc$temp
})();
if (this.quirks.fix_ie_clickable) {
$2_who.ondrag = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#611.26 -*- */
function  ($1_e) {
return false
}
$lzsc$temp._dbg_name = "who.ondrag";
return $lzsc$temp
})()
}
} else {
$2_who.onclick = null;
$2_who.onmouseover = null;
$2_who.onmouseout = null;
$2_who.onmousedown = null;
$2_who.onmouseup = null;
if (this.quirks.fix_ie_clickable) {
$2_who.ondrag = null
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__setClickable";
return $lzsc$temp
})();
LzSprite.prototype.__mouseEvent = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#628.35 -*- */
function  ($1_e) {
if (!$1_e) {
$1_e = window.event
}
if (window["LzKeyboardKernel"] && window["LzKeyboardKernel"].__keyboardEvent) {
LzKeyboardKernel.__keyboardEvent($1_e)
}
var $2_skipevent = false;
var $3_eventname = "on" + $1_e.type;
if (window["LzInputTextSprite"] && $3_eventname == "onmouseover" && LzInputTextSprite.prototype.__lastshown != null) {
LzInputTextSprite.prototype.__hideIfNotFocused()
}
if ($3_eventname == "onmousedown") {
$1_e.cancelBubble = true;
this.__mousedown = true;
if (window["LzMouseKernel"]) {
LzMouseKernel.__lastMouseDown = this
}
} else {
if ($3_eventname == "onmouseup") {
$1_e.cancelBubble = false;
if (window["LzMouseKernel"] && LzMouseKernel.__lastMouseDown == this) {
this.__mousedown = false;
LzMouseKernel.__lastMouseDown = null
} else {
$2_skipevent = true
}
}
}
if ($2_skipevent == false && this.owner.mouseevent) {
LzModeManager.handleMouseButton(this.owner, $3_eventname)
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__mouseEvent";
return $lzsc$temp
})();
LzSprite.prototype.__globalmouseup = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#657.38 -*- */
function  ($1_e) {
if (this.__mousedown) {
this.__mouseEvent($1_e)
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__globalmouseup";
return $lzsc$temp
})();
LzSprite.prototype.setX = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#663.27 -*- */
function  ($1_x) {
if ($1_x == null || $1_x == this.x || isNaN($1_x)) {
return
}
this.__poscachedirty = true;
this.x = $1_x;
$1_x = this.CSSDimension($1_x);
if (this._x != $1_x) {
this._x = $1_x;
this.__LZdiv.style.left = $1_x;
if (this.quirks.fix_clickable) {
this.__LZclickdiv.style.left = $1_x
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setX";
return $lzsc$temp
})();
LzSprite.prototype.setWidth = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#677.31 -*- */
function  ($1_w) {
if ($1_w == null || $1_w < 0 || isNaN($1_w) || this.width == $1_w) {
return
}
this.width = $1_w;
$1_w = this.CSSDimension($1_w);
if (this._w != $1_w) {
this._w = $1_w;
this.__LZdiv.style.width = $1_w;
if (this.clip) {
this.__updateClip()
}
if (this.stretches) {
this.__updateStretches()
}
if (this.__LZclick) {
this.__LZclick.style.width = $1_w
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setWidth";
return $lzsc$temp
})();
LzSprite.prototype.setY = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#692.27 -*- */
function  ($1_y) {
if ($1_y == null || $1_y == this.y || isNaN($1_y)) {
return
}
this.__poscachedirty = true;
this.y = $1_y;
$1_y = this.CSSDimension($1_y);
if (this._y != $1_y) {
this._y = $1_y;
this.__LZdiv.style.top = $1_y;
if (this.quirks.fix_clickable) {
this.__LZclickdiv.style.top = $1_y
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setY";
return $lzsc$temp
})();
LzSprite.prototype.setHeight = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#707.32 -*- */
function  ($1_h) {
if ($1_h == null || $1_h < 0 || isNaN($1_h) || this.height == $1_h) {
return
}
this.height = $1_h;
$1_h = this.CSSDimension($1_h);
if (this._h != $1_h) {
this._h = $1_h;
this.__LZdiv.style.height = $1_h;
if (this.clip) {
this.__updateClip()
}
if (this.stretches) {
this.__updateStretches()
}
if (this.__LZclick) {
this.__LZclick.style.height = $1_h
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setHeight";
return $lzsc$temp
})();
LzSprite.prototype.setMaxLength = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#725.35 -*- */
function  ($1_v) {

}
$lzsc$temp._dbg_name = "LzSprite.prototype.setMaxLength";
return $lzsc$temp
})();
LzSprite.prototype.setPattern = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#732.33 -*- */
function  ($1_v) {

}
$lzsc$temp._dbg_name = "LzSprite.prototype.setPattern";
return $lzsc$temp
})();
LzSprite.prototype.__LZsetClickRegion = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#739.41 -*- */
function  ($1_cr) {
Debug.warn("click regions are not currently implemented in dhtml.")
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__LZsetClickRegion";
return $lzsc$temp
})();
LzSprite.prototype.setVisible = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#746.33 -*- */
function  ($1_v) {
if (this.visible == $1_v) {
return
}
this.visible = $1_v;
this.__LZdiv.style.display = $1_v ? "block" : "none";
if (this.quirks.fix_clickable) {
if (this.__LZclick && this.quirks.fix_ie_clickable) {
this.__LZclick.style.display = $1_v ? "" : "none"
} else {
this.__LZclickdiv.style.display = $1_v ? "block" : "none"
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setVisible";
return $lzsc$temp
})();
LzSprite.prototype.setColor = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#760.31 -*- */
function  ($1_c) {
if (this.color == $1_c) {
return
}
this.color = $1_c;
this.__LZdiv.style.color = LzUtils.color.inttohex($1_c)
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setColor";
return $lzsc$temp
})();
LzSprite.prototype.setBGColor = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#766.33 -*- */
function  ($1_c) {
if (this.bgcolor == $1_c) {
return
}
this.bgcolor = $1_c;
this.__LZdiv.style.backgroundColor = $1_c == null ? "transparent" : LzUtils.color.inttohex($1_c);
if (this.quirks.fix_ie_background_height) {
if (this.height != null && this.height < 2) {
this.setSource(LzSprite.prototype.blankimage, true)
} else {
if (!this._fontSize) {
this.__LZdiv.style.fontSize = "0px"
}
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setBGColor";
return $lzsc$temp
})();
LzSprite.prototype.setOpacity = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#780.33 -*- */
function  ($1_o) {
if (this.opacity == $1_o || $1_o < 0) {
return
}
this.opacity = $1_o;
if ($1_o < 0.0010) {
$1_o = 0
}
if (this.quirks.ie_alpha_image_loader) {
this.__LZdiv.style.filter = "alpha(opacity=" + $1_o * 100 + ")"
} else {
this.__LZdiv.style.opacity = $1_o
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setOpacity";
return $lzsc$temp
})();
LzSprite.prototype.play = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#792.27 -*- */
function  ($1_f) {
if (isNaN($1_f * 1) == false) {
this.__setFrame($1_f)
}
if (this.playing == true) {
return
}
if (this.frames && this.frames.length > 1) {
this.playing = true;
LzIdleKernel.addCallback(this, "__incrementFrame")
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.play";
return $lzsc$temp
})();
LzSprite.prototype.stop = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#805.27 -*- */
function  ($1_f) {
if (this.playing == true) {
this.playing = false;
LzIdleKernel.removeCallback(this, "__incrementFrame")
}
if (isNaN($1_f * 1) == false) {
this.__setFrame($1_f)
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.stop";
return $lzsc$temp
})();
LzSprite.prototype.__incrementFrame = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#820.39 -*- */
function  () {
this.frame++;
if (this.frames && this.frame > this.frames.length) {
this.frame = 1
}
this.__updateFrame()
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__incrementFrame";
return $lzsc$temp
})();
LzSprite.prototype.__updateFrame = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#832.36 -*- */
function  ($1_force) {
if (this.playing || $1_force) {
var $2_url = this.frames[this.frame - 1];
this.setSource($2_url, true)
}
if (this.owner.frame != this.frame - 1) {
this.owner.spriteAttribute("frame", this.frame)
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__updateFrame";
return $lzsc$temp
})();
if (LzSprite.prototype.quirks.preload_images_only_once) {
LzSprite.prototype.__preloadurls = {}
}
LzSprite.prototype.__preloadFrames = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#847.38 -*- */
function  () {
if (!this.__ImgPool) {
this.__ImgPool = new LzPool(LzSprite.prototype.__getImage, LzSprite.prototype.__gotImage, LzSprite.prototype.__destroyImage, this)
}
var $1_l = this.frames.length;
for (var $2_i = 0;
$2_i < $1_l;
$2_i++) {
var $3_src = this.frames[$2_i];
if (this.quirks.preload_images_only_once) {
if ($2_i > 0 && LzSprite.prototype.__preloadurls[$3_src]) {
continue
}
LzSprite.prototype.__preloadurls[$3_src] = true
}
var $4_im = this.__ImgPool.get($3_src, true);
if (this.quirks.ie_alpha_image_loader) {
this.__updateIEAlpha($4_im)
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__preloadFrames";
return $lzsc$temp
})();
LzSprite.prototype.__findParents = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#871.36 -*- */
function  ($1_prop) {
var $2_out = [];
var $3_sprite = this;
if ($3_sprite[$1_prop] != null) {
$2_out.push($3_sprite)
}
do{
$3_sprite = $3_sprite.__parent;
if ($3_sprite[$1_prop] != null) {
$2_out.push($3_sprite)
}
} while ($3_sprite != LzSprite.__rootSprite);
return $2_out
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__findParents";
return $lzsc$temp
})();
LzSprite.prototype.__imgonload = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#886.34 -*- */
function  ($1_i) {
if (this.loading != true) {
return
}
if (this.__imgtimoutid != null) {
clearTimeout(this.__imgtimoutid)
}
this.__imgtimoutid = null;
this.loading = false;
this.resourceWidth = $1_i.width;
this.resourceHeight = $1_i.height;
if (LzSprite.prototype.quirks.invisible_parent_image_sizing_fix && this.resourceWidth == 0) {
var $2_sprites = this.__findParents("visible");
if ($2_sprites.length > 0) {
var $3_vals = [];
var $4_l = $2_sprites.length;
for (var $5_n = 0;
$5_n < $4_l;
$5_n++) {
var $6_v = $2_sprites[$5_n];
$3_vals[$5_n] = $6_v.__LZdiv.style.display;
$6_v.__LZdiv.style.display = "block"
}
this.resourceWidth = $1_i.width;
this.resourceHeight = $1_i.height;
for (var $5_n = 0;
$5_n < $4_l;
$5_n++) {
var $6_v = $2_sprites[$5_n];
$6_v.__LZdiv.style.display = $3_vals[$5_n]
}
}
}
if (this.stretches) {
this.__updateStretches()
}
$1_i.__lastcondition = "__imgonload";
this.owner.resourceload({width: this.resourceWidth, height: this.resourceHeight, resource: this.resource, skiponload: this.skiponload})
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__imgonload";
return $lzsc$temp
})();
LzSprite.prototype.__imgonerror = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#925.35 -*- */
function  ($1_i) {
if (this.loading != true) {
return
}
if (this.__LZimg) {
this.__LZimg.__lastcondition = "__imgonerror"
}
if (this.__imgtimoutid != null) {
clearTimeout(this.__imgtimoutid)
}
this.__imgtimoutid = null;
this.loading = false;
this.resourceWidth = 1;
this.resourceHeight = 1;
if (this.stretches) {
this.__updateStretches()
}
this.owner.resourceloaderror({resource: this.resource})
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__imgonerror";
return $lzsc$temp
})();
LzSprite.prototype.__imgontimeout = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#940.37 -*- */
function  ($1_i) {
if (this.loading != true) {
return
}
if (this.__LZimg) {
this.__LZimg.__lastcondition = "__imgontimeout"
}
this.__imgtimoutid = null;
this.loading = false;
this.resourceWidth = 1;
this.resourceHeight = 1;
if (this.stretches) {
this.__updateStretches()
}
this.owner.resourceloadtimeout({resource: this.resource})
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__imgontimeout";
return $lzsc$temp
})();
LzSprite.prototype.__destroyImage = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#955.37 -*- */
function  ($1_url, $2_img) {
if ($2_img && $2_img.owner) {
LzUtils.callback.remove($2_img.owner)
}
if (LzSprite.prototype.quirks.ie_alpha_image_loader && $2_img.sizer) {
if ($2_img.sizer.tId) {
clearTimeout($2_img.sizer.tId)
}
LzSprite.prototype.__discardElement($2_img.sizer);
$2_img.sizer.onload = null;
$2_img.sizer.onloadforeal = null;
$2_img.sizer = null
}
if ($2_img) {
LzSprite.prototype.__discardElement($2_img)
}
$2_img = null;
if (LzSprite.prototype.quirks.preload_images_only_once) {
LzSprite.prototype.__preloadurls[$1_url] = null
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__destroyImage";
return $lzsc$temp
})();
LzSprite.prototype.__gotImage = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#974.33 -*- */
function  ($1_url, $2_obj) {
this.owner[$2_obj.__lastcondition]({width: this.owner.resourceWidth, height: this.owner.resourceHeight})
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__gotImage";
return $lzsc$temp
})();
LzSprite.prototype.__getImage = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#983.33 -*- */
function  ($1_url, $2_skiploader) {
if (this.owner.baseurl) {
$1_url = this.owner.baseurl + $1_url
}
if (LzSprite.prototype.quirks.ie_alpha_image_loader) {
var im = document.createElement("div");
im.style.overflow = "hidden";
if (this.owner && $2_skiploader + "" != "true") {
im.owner = this.owner;
if (!im.sizer) {
im.sizer = document.createElement("img")
}
im.sizer.onload = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#998.31 -*- */
function  () {
im.sizer.tId = setTimeout(this.onloadforeal, 1)
}
$lzsc$temp._dbg_name = "im.sizer.onload";
return $lzsc$temp
})();
im.sizer.onerror = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1002.32 -*- */
function  () {
im.owner.__imgonerror(im.sizer)
}
$lzsc$temp._dbg_name = "im.sizer.onerror";
return $lzsc$temp
})();
im.sizer.onloadforeal = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1005.37 -*- */
function  () {
im.owner.__imgonload(im.sizer)
}
$lzsc$temp._dbg_name = "im.sizer.onloadforeal";
return $lzsc$temp
})();
var $3_callback = LzUtils.callback.getcallbackstr(this.owner, "__imgontimeout");
this.owner.__imgtimoutid = setTimeout($3_callback, canvas.medialoadtimeout);
im.sizer.src = $1_url
}
if (this.owner.stretches) {
im.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + $1_url + "',sizingMethod='scale')"
} else {
im.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + $1_url + "')"
}
} else {
var im = document.createElement("img");
im.className = "lzdiv";
if (this.owner && $2_skiploader + "" != "true") {
im.owner = this.owner;
im.onload = LzUtils.callback.getcallbackfunc(this.owner, "__imgonload", [im]);
im.onerror = LzUtils.callback.getcallbackfunc(this.owner, "__imgonerror", [im]);
var $3_callback = LzUtils.callback.getcallbackstr(this.owner, "__imgontimeout");
this.owner.__imgtimoutid = setTimeout($3_callback, canvas.medialoadtimeout)
}
im.src = $1_url
}
if (im) {
im.__lastcondition = "__imgonload"
}
return im
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__getImage";
return $lzsc$temp
})();
LzSprite.prototype.setClip = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1035.30 -*- */
function  ($1_c) {
if (this.clip == $1_c) {
return
}
this.clip = $1_c;
this.__updateClip()
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setClip";
return $lzsc$temp
})();
LzSprite.prototype.__updateClip = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1045.35 -*- */
function  () {
if (this.clip && this.width != null && this.width >= 0 && this.height != null && this.height >= 0) {
var $1_s = "rect(0px " + this._w + " " + this._h + " 0px)";
this.__LZdiv.style.clip = $1_s;
if (this.quirks.fix_clickable) {
this.__LZclickdiv.style.clip = $1_s
}
} else {
if (this.__LZdiv.style.clip) {
this.__LZdiv.style.clip = "rect(auto auto auto auto)";
if (this.quirks.fix_clickable) {
this.__LZclickdiv.style.clip = "rect(auto auto auto auto)"
}
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__updateClip";
return $lzsc$temp
})();
LzSprite.prototype.stretchResource = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1070.38 -*- */
function  ($1_s) {
if (this.stretches == $1_s) {
return
}
this.stretches = $1_s;
this.__updateStretches()
}
$lzsc$temp._dbg_name = "LzSprite.prototype.stretchResource";
return $lzsc$temp
})();
LzSprite.prototype.__updateStretches = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1080.40 -*- */
function  () {
if (this.loading) {
return
}
if (this.__LZimg) {
if (this.stretches == "both") {
this.__LZimg.width = this.width;
this.__LZimg.height = this.height
} else {
if (this.stretches == "height") {
this.__LZimg.width = this.resourceWidth;
this.__LZimg.height = this.height
} else {
if (this.stretches == "width") {
this.__LZimg.width = this.width;
this.__LZimg.height = this.resourceHeight
} else {
this.__LZimg.width = this.resourceWidth;
this.__LZimg.height = this.resourceHeight
}
}
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__updateStretches";
return $lzsc$temp
})();
LzSprite.prototype.predestroy = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1099.33 -*- */
function  () {

}
$lzsc$temp._dbg_name = "LzSprite.prototype.predestroy";
return $lzsc$temp
})();
LzSprite.prototype.destroy = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1102.30 -*- */
function  ($1_recursive) {
if (this.destroyed == true) {
return
}
if ($1_recursive) {
if (this.__children) {
for (var $2_i = 0;
$2_i < this.__children.length;
$2_i++) {
this.__children[$2_i].destroy($1_recursive)
}
}
}
if (this.__ImgPool) {
this.__ImgPool.destroy()
}
if (this.__LZimg) {
this.__discardElement(this.__LZimg)
}
if (this.__LZclick) {
this.__setClickable(false, this.__LZclick);
this.__discardElement(this.__LZclick)
}
if (this.__LzInputDiv) {
this.__setTextEvents(false);
this.__discardElement(this.__LzInputDiv)
}
if (this.__LZdiv) {
this.__LZdiv.onselectstart = null;
this.__setClickable(false, this.__LZdiv);
this.__discardElement(this.__LZdiv)
}
if (this.__LZinputclickdiv) {
this.__LZinputclickdiv.onmousedown = null;
this.__discardElement(this.__LZinputclickdiv)
}
if (this.__LZclickdiv) {
this.__discardElement(this.__LZclickdiv)
}
if (this.__LZtextdiv) {
this.__discardElement(this.__LZtextdiv)
}
if (this.__LZcanvas) {
this.__discardElement(this.__LZcanvas)
}
this.__ImgPool = null;
this.destroyed = true
}
$lzsc$temp._dbg_name = "LzSprite.prototype.destroy";
return $lzsc$temp
})();
LzSprite.prototype.getMouse = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1149.31 -*- */
function  () {
var $1_p = this.__getPos();
if (this.isroot) {
return {x: LzMouseKernel.__x - $1_p.x, y: LzMouseKernel.__y - $1_p.y}
} else {
var $2_m = LzSprite.__rootSprite.getMouse();
return {x: $2_m.x - $1_p.x, y: $2_m.y - $1_p.y}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.getMouse";
return $lzsc$temp
})();
LzSprite.prototype.__poscache = null;
LzSprite.prototype.__poscachedirty = true;
LzSprite.prototype.__getPos = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1172.31 -*- */
function  () {
var $1_dirty = false;
var $2_p = this;
while ($2_p != this.__rootSprite) {
if ($2_p.__poscachedirty) {
$1_dirty = $2_p;
break
}
$2_p = $2_p.__parent
}
if ($1_dirty == false && this.__poscache) {
return this.__poscache
}
var $3_el = this.__LZdiv;
var $4_parent = null;
var $5_pos = {}
var $6_box;
if (Lz.__BrowserDetect.isIE) {
$6_box = $3_el.getBoundingClientRect();
var $7_scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
var $8_scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
if (this.quirks["ie_offset_position_by_2"]) {
$8_scrollLeft -= 2;
$7_scrollTop -= 2
}
return {x: $6_box.left + $8_scrollLeft, y: $6_box.top + $7_scrollTop}
} else {
if (document.getBoxObjectFor) {
$6_box = document.getBoxObjectFor($3_el);
$5_pos = {x: $6_box.x, y: $6_box.y}
} else {
$5_pos = {x: $3_el.offsetLeft, y: $3_el.offsetTop}
$4_parent = $3_el.offsetParent;
if ($4_parent != $3_el) {
while ($4_parent) {
$5_pos.x += $4_parent.offsetLeft;
$5_pos.y += $4_parent.offsetTop;
$4_parent = $4_parent.offsetParent
}
}
if (this.quirks.absolute_position_accounts_for_offset && this.hasOwnProperty("getStyle") && this.getStyle($3_el, "position") == "absolute") {
$5_pos.y -= document.body.offsetTop
}
}
}
if ($3_el.parentNode) {
$4_parent = $3_el.parentNode
} else {
$4_parent = null
}
while ($4_parent && $4_parent.tagName != "BODY" && $4_parent.tagName != "HTML") {
$5_pos.x -= $4_parent.scrollLeft;
$5_pos.y -= $4_parent.scrollTop;
if ($4_parent.parentNode) {
$4_parent = $4_parent.parentNode
} else {
$4_parent = null
}
}
var $2_p = this;
while ($2_p && $2_p != this.__rootSprite) {
if ($2_p.__parent) {
$2_p.__poscachedirty = false
}
$2_p = $2_p.__parent
}
this.__poscache = $5_pos;
return $5_pos
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__getPos";
return $lzsc$temp
})();
LzSprite.prototype.getWidth = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1247.31 -*- */
function  () {
var $1_w = this.__LZdiv.clientWidth;
return $1_w == 0 ? this.width : $1_w
}
$lzsc$temp._dbg_name = "LzSprite.prototype.getWidth";
return $lzsc$temp
})();
LzSprite.prototype.getHeight = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1253.32 -*- */
function  () {
var $1_h = this.__LZdiv.clientHeight;
return $1_h == 0 ? this.height : $1_h
}
$lzsc$temp._dbg_name = "LzSprite.prototype.getHeight";
return $lzsc$temp
})();
LzSprite.prototype.setCursor = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1264.32 -*- */
function  ($1_c) {
if (this.quirks.no_cursor_colresize) {
return
}
if ($1_c == this.cursor) {
return
}
if (this.clickable != true) {
this.setClickable(true)
}
this.cursor = $1_c;
var $1_c = LzSprite.prototype.__defaultStyles.hyphenate($1_c);
this.__LZclick.style.cursor = $1_c
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setCursor";
return $lzsc$temp
})();
LzSprite.prototype.setShowHandCursor = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1281.40 -*- */
function  ($1_s) {
if ($1_s == true) {
this.setCursor("pointer")
} else {
this.setCursor("default")
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setShowHandCursor";
return $lzsc$temp
})();
LzSprite.prototype.getMCRef = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1289.31 -*- */
function  () {
return this.__LZdiv
}
$lzsc$temp._dbg_name = "LzSprite.prototype.getMCRef";
return $lzsc$temp
})();
LzSprite.prototype.bringToFront = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1294.35 -*- */
function  () {
if (!this.__parent) {
Debug.warn("bringToFront with no parent");
return
}
if (this.__parent.__children.length < 2) {
return
}
this.__setZ(++this.__parent.__topZ)
}
$lzsc$temp._dbg_name = "LzSprite.prototype.bringToFront";
return $lzsc$temp
})();
LzSprite.prototype.__setZ = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1309.29 -*- */
function  ($1_z) {
this.__LZdiv.style.zIndex = $1_z;
if (this.quirks.fix_clickable) {
this.__LZclickdiv.style.zIndex = $1_z
}
this.__z = $1_z
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__setZ";
return $lzsc$temp
})();
LzSprite.prototype.__zCompare = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1320.33 -*- */
function  ($1_a, $2_b) {
if ($1_a.__z < $2_b.__z) {
return -1
}
if ($1_a.__z > $2_b.__z) {
return 1
}
return 0
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__zCompare";
return $lzsc$temp
})();
LzSprite.prototype.sendToBack = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1328.33 -*- */
function  () {
if (!this.__parent) {
Debug.warn("sendToBack with no parent");
return
}
var $1_c = this.__parent.__children;
if ($1_c.length < 2) {
return
}
$1_c.sort(LzSprite.prototype.__zCompare);
this.sendBehind($1_c[0])
}
$lzsc$temp._dbg_name = "LzSprite.prototype.sendToBack";
return $lzsc$temp
})();
LzSprite.prototype.sendBehind = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1343.33 -*- */
function  ($1_behindSprite) {
if (!$1_behindSprite) {
return
}
if (!this.__parent) {
Debug.warn("sendBehind with no parent");
return
}
var $2_c = this.__parent.__children;
if ($2_c.length < 2) {
return
}
$2_c.sort(LzSprite.prototype.__zCompare);
var $3_behindZ = false;
for (var $4_i = 0;
$4_i < $2_c.length;
$4_i++) {
var $5_s = $2_c[$4_i];
if ($5_s == $1_behindSprite) {
$3_behindZ = $1_behindSprite.__z
}
if ($3_behindZ != false) {
$5_s.__setZ(++$5_s.__z)
}
}
this.__setZ($3_behindZ)
}
$lzsc$temp._dbg_name = "LzSprite.prototype.sendBehind";
return $lzsc$temp
})();
LzSprite.prototype.sendInFrontOf = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1369.36 -*- */
function  ($1_frontSprite) {
if (!$1_frontSprite) {
return
}
if (!this.__parent) {
Debug.warn("sendInFrontOf with no parent");
return
}
var $2_c = this.__parent.__children;
if ($2_c.length < 2) {
return
}
$2_c.sort(LzSprite.prototype.__zCompare);
var $3_frontZ = false;
for (var $4_i = 0;
$4_i < $2_c.length;
$4_i++) {
var $5_s = $2_c[$4_i];
if ($3_frontZ != false) {
$5_s.__setZ(++$5_s.__z)
}
if ($5_s == $1_frontSprite) {
$3_frontZ = $1_frontSprite.__z + 1
}
}
this.__setZ($3_frontZ)
}
$lzsc$temp._dbg_name = "LzSprite.prototype.sendInFrontOf";
return $lzsc$temp
})();
LzSprite.prototype.__setFrame = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1399.33 -*- */
function  ($1_f) {
if (!this.frames || this.frame == $1_f) {
return
}
if ($1_f < 1) {
$1_f = 1
} else {
if ($1_f > this.frames.length) {
$1_f = this.frames.length
}
}
this.frame = $1_f;
this.__updateFrame(true)
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__setFrame";
return $lzsc$temp
})();
LzSprite.prototype.__discardElement = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1414.39 -*- */
function  ($1_element) {
if (LzSprite.prototype.quirks.ie_leak_prevention) {
if ($1_element.owner) {
$1_element.owner = null
}
var $2_garbageBin = document.getElementById("__LZIELeakGarbageBin");
if (!$2_garbageBin) {
$2_garbageBin = document.createElement("DIV");
$2_garbageBin.id = "__LZIELeakGarbageBin";
$2_garbageBin.style.display = "none";
document.body.appendChild($2_garbageBin)
}
$2_garbageBin.appendChild($1_element);
$2_garbageBin.innerHTML = ""
} else {
if ($1_element.parentNode) {
$1_element.parentNode.removeChild($1_element)
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__discardElement";
return $lzsc$temp
})();
LzSprite.prototype.getZ = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1436.27 -*- */
function  () {
return this.__z
}
$lzsc$temp._dbg_name = "LzSprite.prototype.getZ";
return $lzsc$temp
})();
LzSprite.prototype.updateResourceSize = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1440.41 -*- */
function  () {
this.owner.resourceload({width: this.resourceWidth, height: this.resourceHeight, resource: this.resource, skiponload: true})
}
$lzsc$temp._dbg_name = "LzSprite.prototype.updateResourceSize";
return $lzsc$temp
})();
LzSprite.prototype.unload = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1444.29 -*- */
function  () {
if (this.__ImgPool) {
this.__ImgPool.destroy();
this.__ImgPool = null
}
if (this.__LZimg) {
this.__discardElement(this.__LZimg)
}
this.__LZimg = null
}
$lzsc$temp._dbg_name = "LzSprite.prototype.unload";
return $lzsc$temp
})();
LzSprite.prototype.__setCSSClassProperty = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1456.44 -*- */
function  ($1_classname, $2_name, $3_value) {
var $4_rulename = document.all ? "rules" : "cssRules";
var $5_sheets = document.styleSheets;
var $6_sl = $5_sheets.length - 1;
for (var $7_i = $6_sl;
$7_i >= 0;
$7_i--) {
var $8_rules = $5_sheets[$7_i][$4_rulename];
var $9_rl = $8_rules.length - 1;
for (var $10_j = $9_rl;
$10_j >= 0;
$10_j--) {
if ($8_rules[$10_j].selectorText == $1_classname) {
$8_rules[$10_j].style[$2_name] = $3_value
}
}
}
}
$lzsc$temp._dbg_name = "LzSprite.prototype.__setCSSClassProperty";
return $lzsc$temp
})();
LzSprite.prototype.setDefaultContextMenu = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1476.44 -*- */
function  ($1_cmenu) {
LzMouseKernel.__defaultcontextmenu = $1_cmenu
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setDefaultContextMenu";
return $lzsc$temp
})();
LzSprite.prototype.setContextMenu = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1485.37 -*- */
function  ($1_cmenu) {
this.__contextmenu = $1_cmenu
}
$lzsc$temp._dbg_name = "LzSprite.prototype.setContextMenu";
return $lzsc$temp
})();
LzSprite.prototype.getContextMenu = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzSprite.js#1493.37 -*- */
function  () {
return this.__contextmenu
}
$lzsc$temp._dbg_name = "LzSprite.prototype.getContextMenu";
return $lzsc$temp
})();
if (LzSprite.prototype.quirks.ie_leak_prevention) {
LzSprite.prototype.__sprites = {}

/* -*- file: kernel/dhtml/LzSprite.js#1502.5 -*- */
function __cleanUpForIE () {
var $1_obj = LzTextSprite.prototype._sizedomcache;
var $2_f = LzSprite.prototype.__discardElement;
for (var $3_i in $1_obj) {
$2_f($1_obj[$3_i])
}
LzTextSprite.prototype._sizedomcache = null;
var $1_obj = LzSprite.prototype.__sprites;
for (var $3_i in $1_obj) {
$1_obj[$3_i].destroy();
$1_obj[$3_i] = null
}
LzSprite.prototype.__sprites = null;
document.onmousemove = null;
document.onmousedown = null;
document.onmouseup = null;
document.oncontextmenu = null;
document.onkeydown = null;
document.onkeyup = null;
document.onkeypress = null;
document.onmousewheel = null;
window.onresize = null;
window.onunload = null
}
window.onbeforeunload = __cleanUpForIE
}
var LzLibrary = Class.make("LzLibrary", LzNode, {construct: 
/* -*- file: kernel/dhtml/LzLibrary.js#25.1 -*- */
function construct ($1_parent, $2_args) {
this.stage = $2_args.stage;
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.sprite = new LzSprite(this, false, $2_args);
LzLibrary.libraries[$2_args.name] = this
}, init: 
/* -*- file: kernel/dhtml/LzLibrary.js#35.1 -*- */
function init () {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["init"] : this.nextMethod(arguments.callee, "init")).apply(this, arguments);
if (this.stage == "late") {
this.load()
}
}, loaded: false, loading: false, toString: 
/* -*- file: kernel/dhtml/LzLibrary.js#56.1 -*- */
function toString () {
return "Library " + this.href + " named " + this.name
}, load: 
/* -*- file: kernel/dhtml/LzLibrary.js#63.1 -*- */
function load () {
Lz.__dhtmlLoadLibrary(this.href)
}}, {tagname: "import", libraries: [], findLibrary: 
/* -*- file: kernel/dhtml/LzLibrary.js#48.8 -*- */
function findLibrary ($1_libname) {
return LzLibrary.libraries[$1_libname]
}, __LZsnippetLoaded: 
/* -*- file: kernel/dhtml/LzLibrary.js#71.8 -*- */
function __LZsnippetLoaded ($1_url) {
var $2_libname = null;
var $3_libs = LzLibrary.libraries;
for (var $4_l in $3_libs) {
if ($3_libs[$4_l].href == $1_url) {
$2_libname = $3_libs[$4_l].name;
break
}
}
if ($2_libname == null) {
Debug.error("could not find library with href", $1_url)
}
LzInstantiateView({attrs: {libname: $2_libname}, name: "__libraryloadercomplete"}, 1);
canvas.initDone()
}});
(function  () {
var $lzsc$temp = function  () {
with (LzLibrary) {
with (LzLibrary.prototype) {
DeclareEvent(prototype, "onload")
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzTextSprite = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#12.20 -*- */
function  ($1_owner) {
if ($1_owner == null) {
return
}
this.__LZdiv = document.createElement("div");
this.__LZdiv.className = "lzdiv";
this.__LZtextdiv = document.createElement("div");
this.__LZtextdiv.className = "lzdiv";
this.__LZdiv.appendChild(this.__LZtextdiv);
if (this.quirks.emulate_flash_font_metrics) {
this.__LZdiv.className = "lzswftext"
} else {
this.__LZdiv.className = "lztext"
}
this.__LZdiv.owner = this;
if (this.quirks.fix_clickable) {
this.__LZclickdiv = document.createElement("div");
this.__LZclickdiv.className = "lzdiv";
this.__LZclickdiv.owner = this
}
this.owner = $1_owner;
this.uid = LzSprite.prototype.uid++;
if (this.quirks.ie_leak_prevention) {
this.__sprites[this.uid] = this
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzTextSprite.js#12/20";
return $lzsc$temp
})();
LzTextSprite.prototype = new LzSprite(null);
LzTextSprite.prototype.__initTextProperties = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#40.47 -*- */
function  ($1_args) {
this.setFontName($1_args.font);
this.setFontStyle($1_args.fontstyle);
this.setFontSize($1_args.fontsize);
this.text = $1_args.text
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.__initTextProperties";
return $lzsc$temp
})();
LzTextSprite.prototype._fontStyle = "normal";
LzTextSprite.prototype._fontWeight = "normal";
LzTextSprite.prototype._fontSize = "11px";
LzTextSprite.prototype._fontFamily = "Verdana,Vera,sans-serif";
LzTextSprite.prototype._whiteSpace = "normal";
LzTextSprite.prototype.__wpadding = 4;
LzTextSprite.prototype.__hpadding = 4;
LzTextSprite.prototype.__sizecacheupperbound = 1000;
LzTextSprite.prototype.setFontSize = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#57.38 -*- */
function  ($1_fsize) {
if ($1_fsize == null || $1_fsize < 0) {
return
}
$1_fsize = this.CSSDimension($1_fsize);
if (this._fontSize != $1_fsize) {
this._fontSize = $1_fsize;
this.__LZdiv.style.fontSize = $1_fsize;
this._styledirty = true
}
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setFontSize";
return $lzsc$temp
})();
LzTextSprite.prototype.setFontStyle = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#68.39 -*- */
function  ($1_fstyle) {
var $2_fweight;
if ($1_fstyle == "plain") {
$2_fweight = "normal";
$1_fstyle = "normal"
} else {
if ($1_fstyle == "bold") {
$2_fweight = "bold";
$1_fstyle = "normal"
} else {
if ($1_fstyle == "italic") {
$2_fweight = "normal";
$1_fstyle = "italic"
} else {
if ($1_fstyle == "bold italic" || $1_fstyle == "bolditalic") {
$2_fweight = "bold";
$1_fstyle = "italic"
}
}
}
}
if ($2_fweight != this._fontWeight) {
this._fontWeight = $2_fweight;
this.__LZdiv.style.fontWeight = $2_fweight;
this._styledirty = true
}
if ($1_fstyle != this._fontStyle) {
this._fontStyle = $1_fstyle;
this.__LZdiv.style.fontStyle = $1_fstyle;
this._styledirty = true
}
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setFontStyle";
return $lzsc$temp
})();
LzTextSprite.prototype.setFontName = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#98.38 -*- */
function  ($1_fname) {
if ($1_fname != this._fontFamily) {
this._fontFamily = $1_fname;
this.__LZdiv.style.fontFamily = $1_fname;
this._styledirty = true
}
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setFontName";
return $lzsc$temp
})();
LzTextSprite.prototype.setTextColor = LzSprite.prototype.setColor;
LzTextSprite.prototype.setText = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#108.34 -*- */
function  ($1_t, $2_force) {
if ($1_t == "null") {
$1_t = ""
}
if ($2_force != true && this.text == $1_t) {
return
}
this.text = $1_t;
if (this.multiline && $1_t && $1_t.indexOf("\n") >= 0) {
if (this.quirks["inner_html_strips_newlines"]) {
$1_t = $1_t.replace(this.inner_html_strips_newlines_re, "<br />")
}
} else {
if (this._whiteSpace != "normal") {
this._whiteSpace = "normal";
this.__LZdiv.style.whiteSpace = "normal";
this._styledirty = true
}
}
if ($1_t && this.quirks["inner_html_no_entity_apos"]) {
$1_t = $1_t.replace(RegExp("&apos;", "mg"), "&#39;")
}
this.__LZtextdiv.innerHTML = $1_t;
this.fieldHeight = null
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setText";
return $lzsc$temp
})();
LzTextSprite.prototype.setMultiline = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#140.39 -*- */
function  ($1_m) {
$1_m = $1_m == true;
if (this.multiline == $1_m) {
return
}
this.multiline = $1_m;
if ($1_m) {
if (this._whiteSpace != "normal") {
this._whiteSpace = "normal";
this.__LZdiv.style.whiteSpace = "normal";
this._styledirty = true
}
this.__LZdiv.style.overflow = "visible"
} else {
if (this._whiteSpace != "nowrap") {
this._whiteSpace = "nowrap";
this.__LZdiv.style.whiteSpace = "nowrap";
this._styledirty = true
}
this.__LZdiv.style.overflow = "hidden"
}
this.setText(this.text, true)
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setMultiline";
return $lzsc$temp
})();
LzTextSprite.prototype.setPattern = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#165.37 -*- */
function  ($1_val) {
Debug.warn("setPattern not yet implemented for dhtml")
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setPattern";
return $lzsc$temp
})();
LzTextSprite.prototype.getTextWidth = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#172.39 -*- */
function  () {
if (this.text == null || this.text == "") {
return 0
}
return this.getTextSize(this.text, this.resize).width
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getTextWidth";
return $lzsc$temp
})();
LzTextSprite.prototype.getTextHeight = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#178.40 -*- */
function  () {
if (this.__LZdiv) {
return this.getHeight()
} else {
return 0
}
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getTextHeight";
return $lzsc$temp
})();
LzTextSprite.prototype.getTextfieldHeight = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#188.45 -*- */
function  () {
if (this._styledirty != true && this.fieldHeight != null) {
return this.fieldHeight
}
if (this.text == null || this.text == "") {
this.fieldHeight = this.getTextSize("Yq_gy").height;
return this.fieldHeight
}
if (this.multiline) {
var $1_oldheight = false;
if (this.height) {
$1_oldheight = this.__LZdiv.style.height;
this.__LZdiv.style.height = "auto"
}
var $2_h = this.__LZdiv.clientHeight;
if ($2_h == 0 || $2_h == null) {
$2_h = this.getTextSize(this.text).height;
if ($2_h > 0 && this.quirks.emulate_flash_font_metrics) {
$2_h += this.__hpadding
}
} else {
if ($2_h == 2) {
$2_h = this.getTextSize(this.text).height
}
if ($2_h > 0 && this.quirks.emulate_flash_font_metrics) {
$2_h += this.__hpadding
}
this.fieldHeight = $2_h
}
if (this.height) {
this.__LZdiv.style.height = $1_oldheight
}
} else {
var $2_h = this.getTextSize("Yq_gy").height;
if ($2_h != 0) {
this.fieldHeight = $2_h
}
}
return $2_h
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getTextfieldHeight";
return $lzsc$temp
})();
LzTextSprite.prototype._sizecache = {counter: 0}
if (LzSprite.prototype.quirks.ie_leak_prevention) {
LzTextSprite.prototype._sizedomcache = {}
}
LzTextSprite.prototype._styledirty = true;
LzTextSprite.prototype.getTextSize = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#237.38 -*- */
function  ($1_string, $2_ignorewidth) {
if (this._styledirty != true) {
var $3_style = this._stylecache
} else {
var $3_style = "position: absolute";
$3_style += ";visibility: hidden";
$3_style += ";top: 4000px";
$3_style += ";font-size: " + this._fontSize;
$3_style += ";font-style: " + this._fontStyle;
$3_style += ";font-weight: " + this._fontWeight;
$3_style += ";font-family: " + this._fontFamily;
if (this.multiline && $2_ignorewidth != true) {
if (this.width) {
$3_style += ";width: " + this.width + "px"
}
}
if (document.all && document.body.insertAdjacentHTML) {
if (this.__LzInputDiv != null) {
$3_style += ";white-space: pre"
} else {
$3_style += ";white-space: " + this._whiteSpace
}
} else {
if (document.getElementById && document.createElement) {
if (this.__LzInputDiv != null) {
$3_style += ";white-space: pre"
} else {
$3_style += ";white-space: " + this._whiteSpace
}
}
}
this._stylecache = $3_style;
this._styledirty = false
}
if (this._sizecache.counter > this.__sizecacheupperbound) {
this._sizecache = {counter: 0}
}
if (this._sizecache[$3_style] == null) {
this._sizecache[$3_style] = {}
}
var $4__textsizecache = this._sizecache[$3_style];
if (!$4__textsizecache[$1_string]) {
var $5_dim = {}
if (document.all && document.body.insertAdjacentHTML) {
if (this.multiline && $1_string && this.quirks["inner_html_strips_newlines"]) {
$1_string = $1_string.replace(this.inner_html_strips_newlines_re, "<br />")
}
var $6_tagname = "span";
var $7_mdiv = $4__textsizecache[$6_tagname];
if ($7_mdiv == null) {
var $8_html = "<" + $6_tagname + ' id="testSpan' + this._sizecache.counter + '"';
$8_html += ' style="' + $3_style + '">';
$8_html += $1_string;
$8_html += "</" + $6_tagname + ">";
document.body.insertAdjacentHTML("beforeEnd", $8_html);
$7_mdiv = document.all["testSpan" + this._sizecache.counter];
$4__textsizecache[$6_tagname] = $7_mdiv
}
} else {
if (document.getElementById && document.createElement) {
if (this.__LzInputDiv == null) {
if (this.multiline && $1_string && this.quirks["inner_html_strips_newlines"]) {
$1_string = $1_string.replace(this.inner_html_strips_newlines_re, "<br />")
}
}
var $6_tagname = this.multiline ? "div" : "span";
var $7_mdiv = $4__textsizecache[$6_tagname];
if ($7_mdiv == null) {
$7_mdiv = document.createElement($6_tagname);
Lz.__setAttr($7_mdiv, "style", $3_style);
document.body.appendChild($7_mdiv);
$4__textsizecache[$6_tagname] = $7_mdiv
}
}
}
if (this.quirks.ie_leak_prevention) {
LzTextSprite.prototype._sizedomcache[$6_tagname + $3_style] = $7_mdiv
}
$7_mdiv.innerHTML = $1_string;
$7_mdiv.style.display = "block";
$5_dim.width = $7_mdiv.offsetWidth;
$5_dim.height = $7_mdiv.offsetHeight;
$7_mdiv.style.display = "none";
if (this.quirks.emulate_flash_font_metrics) {
$5_dim.height = Math.floor($5_dim.height * 1.0000002) + (this.multiline ? 0 : this.__hpadding);
$5_dim.width = $5_dim.width + (this.multiline ? 0 : this.__wpadding);
if (this._whiteSpace == "normal") {
if (this.multiline) {
$5_dim.width += this.__wpadding
}
}
}
$4__textsizecache[$1_string] = $5_dim;
this._sizecache.counter++
}
return $4__textsizecache[$1_string]
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getTextSize";
return $lzsc$temp
})();
LzTextSprite.prototype.setSelectable = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#333.40 -*- */
function  ($1_s) {
if ($1_s) {
this.__LZdiv.onselectstart = null;
this.__LZdiv.style["MozUserSelect"] = "normal";
this.__LZdiv.style["KHTMLUserSelect"] = "normal";
this.__LZdiv.style["UserSelect"] = "normal"
} else {
this.__LZdiv.onselectstart = LzTextSprite.prototype.__cancelhandler;
this.__LZdiv.style["MozUserSelect"] = "none";
this.__LZdiv.style["KHTMLUserSelect"] = "none";
this.__LZdiv.style["UserSelect"] = "none"
}
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setSelectable";
return $lzsc$temp
})();
LzTextSprite.prototype.__cancelhandler = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#348.42 -*- */
function  () {
return false
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.__cancelhandler";
return $lzsc$temp
})();
LzTextSprite.prototype.setResize = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#352.36 -*- */
function  ($1_r) {
this.resize = $1_r == true
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setResize";
return $lzsc$temp
})();
LzTextSprite.prototype.setSelection = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#356.39 -*- */
function  ($1_start, $2_end) {
Debug.warn("LzTextSprite.setSelection is not implemented yet.")
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setSelection";
return $lzsc$temp
})();
LzTextSprite.prototype.getSelectionPosition = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#363.47 -*- */
function  () {
Debug.warn("LzTextSprite.getSelectionPosition is not implemented yet.")
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getSelectionPosition";
return $lzsc$temp
})();
LzTextSprite.prototype.getSelectionSize = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#370.43 -*- */
function  () {
Debug.warn("LzTextSprite.getSelectionSize is not implemented yet.")
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getSelectionSize";
return $lzsc$temp
})();
LzTextSprite.prototype.getScroll = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#377.36 -*- */
function  () {
Debug.warn("LzTextSprite.getScroll is not implemented yet.")
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getScroll";
return $lzsc$temp
})();
LzTextSprite.prototype.getMaxScroll = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#384.39 -*- */
function  () {
Debug.warn("LzTextSprite.getMaxScroll is not implemented yet.")
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.getMaxScroll";
return $lzsc$temp
})();
LzTextSprite.prototype.setScroll = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#391.36 -*- */
function  () {
Debug.warn("LzTextSprite.setScroll is not implemented yet.")
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setScroll";
return $lzsc$temp
})();
LzTextSprite.prototype.__setWidth = LzSprite.prototype.setWidth;
LzTextSprite.prototype.setWidth = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#399.35 -*- */
function  ($1_w) {
if ($1_w == null || $1_w < 0 || isNaN($1_w) || this.width == $1_w) {
return
}
var $2_wp = this.CSSDimension($1_w >= this.__wpadding ? $1_w - this.__wpadding : 0);
this.__LZtextdiv.style.width = $2_wp;
this.__LZtextdiv.style.clip = "rect(0px " + $2_wp + " " + this.CSSDimension(this.height >= this.__hpadding ? this.height - this.__hpadding : 0) + " 0px)";
this.__setWidth($1_w);
this._styledirty = true
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setWidth";
return $lzsc$temp
})();
LzTextSprite.prototype.__setHeight = LzSprite.prototype.setHeight;
LzTextSprite.prototype.setHeight = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#409.36 -*- */
function  ($1_h) {
if ($1_h == null || $1_h < 0 || isNaN($1_h) || this.height == $1_h) {
return
}
var $2_hp = this.CSSDimension($1_h >= this.__hpadding ? $1_h - this.__hpadding : 0);
this.__LZtextdiv.style.height = $2_hp;
this.__LZtextdiv.style.clip = "rect(0px " + this.CSSDimension(this.width >= this.__wpadding ? this.width - this.__wpadding : 0) + " " + $2_hp + " 0px)";
this.__setHeight($1_h);
if (this.multiline) {
this._styledirty = true
}
}
$lzsc$temp._dbg_name = "LzTextSprite.prototype.setHeight";
return $lzsc$temp
})();
document.onselectstart = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#419.26 -*- */
function  () {
return false
}
$lzsc$temp._dbg_name = "document.onselectstart";
return $lzsc$temp
})();
document.ondrag = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzTextSprite.js#420.19 -*- */
function  () {
return false
}
$lzsc$temp._dbg_name = "document.ondrag";
return $lzsc$temp
})();
var LzInputTextSprite = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#12.25 -*- */
function  ($1_owner) {
if ($1_owner == null) {
return
}
this.__LZdiv = document.createElement("div");
this.__LZdiv.className = "lzdiv";
this.__LZdiv.owner = this;
if (this.quirks.fix_clickable) {
this.__LZclickdiv = document.createElement("div");
this.__LZclickdiv.className = "lzdiv";
this.__LZclickdiv.owner = this
}
this.owner = $1_owner;
this.uid = LzSprite.prototype.uid++;
if (this.quirks.ie_leak_prevention) {
this.__sprites[this.uid] = this
}
this.__createInputText()
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzInputTextSprite.js#12/25";
return $lzsc$temp
})();
LzInputTextSprite.prototype = new LzTextSprite(null);
LzInputTextSprite.prototype.____hpadding = 2;
LzInputTextSprite.prototype.____wpadding = 2;
LzInputTextSprite.prototype.__createInputText = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#38.49 -*- */
function  ($1_t) {
if (this.__LzInputDiv) {
return
}
if (this.owner && this.owner.password) {
this.__LzInputDiv = document.createElement("input");
Lz.__setAttr(this.__LzInputDiv, "type", "password")
} else {
if (this.owner && this.owner.multiline) {
this.__LzInputDiv = document.createElement("textarea")
} else {
this.__LzInputDiv = document.createElement("input");
Lz.__setAttr(this.__LzInputDiv, "type", "text")
}
}
if (this.quirks.firefox_autocomplete_bug) {
Lz.__setAttr(this.__LzInputDiv, "autocomplete", "off")
}
this.__LzInputDiv.owner = this;
if (this.quirks.emulate_flash_font_metrics) {
if (this.owner && this.owner.multiline) {
this.__LzInputDiv.className = "lzswfinputtextmultiline"
} else {
this.____hpadding = 1;
this.__LzInputDiv.className = "lzswfinputtext"
}
} else {
this.__LzInputDiv.className = "lzinputtext"
}
if (this.owner) {
Lz.__setAttr(this.__LzInputDiv, "name", this.owner.name)
}
if ($1_t == null) {
$1_t = ""
}
Lz.__setAttr(this.__LzInputDiv, "value", $1_t);
if (this.quirks.fix_clickable) {
if (this.quirks.fix_ie_clickable) {
this.__LZinputclickdiv = document.createElement("img");
this.__LZinputclickdiv.src = LzSprite.prototype.blankimage
} else {
this.__LZinputclickdiv = document.createElement("div")
}
this.__LZinputclickdiv.className = "lzclickdiv";
this.__LZinputclickdiv.owner = this;
this.__LZinputclickdiv.onmouseover = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#78.46 -*- */
function  () {
this.owner.__show()
}
$lzsc$temp._dbg_name = "this.__LZinputclickdiv.onmouseover";
return $lzsc$temp
})();
this.__LZclickdiv.appendChild(this.__LZinputclickdiv)
}
this.__LZdiv.appendChild(this.__LzInputDiv);
this.__setTextEvents(true)
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.__createInputText";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__show = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#88.38 -*- */
function  () {
if (this.__shown == true || this.disabled == true) {
return
}
this.__hideIfNotFocused();
LzInputTextSprite.prototype.__lastshown = this;
this.__shown = true;
this.__LzInputDiv = this.__LZdiv.removeChild(this.__LzInputDiv);
if (this.quirks["inputtext_parents_cannot_contain_clip"]) {
var $1_sprites = this.__findParents("clip");
var $2_l = $1_sprites.length;
if ($2_l > 1) {
if (this._shownclipvals == null) {
this._shownclipvals = [];
this._shownclippedsprites = $1_sprites;
for (var $3_n = 0;
$3_n < $2_l;
$3_n++) {
var $4_v = $1_sprites[$3_n];
this._shownclipvals[$3_n] = $4_v.__LZclickdiv.style.clip;
$4_v.__LZclickdiv.style.clip = "rect(auto auto auto auto)"
}
}
}
}
if (this.quirks.fix_ie_clickable) {
this.__LZclickdiv.appendChild(this.__LzInputDiv);
this.__setCSSClassProperty(".lzclickdiv", "display", "none")
} else {
this.__LZinputclickdiv.appendChild(this.__LzInputDiv)
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.__show";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__hideIfNotFocused = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#122.50 -*- */
function  () {
if (LzInputTextSprite.prototype.__lastshown == null) {
return
}
if (LzInputTextSprite.prototype.__focusedSprite != LzInputTextSprite.prototype.__lastshown) {
LzInputTextSprite.prototype.__lastshown.__hide()
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.__hideIfNotFocused";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__hide = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#129.38 -*- */
function  () {
if (this.__shown != true || this.disabled == true) {
return
}
LzInputTextSprite.prototype.__lastshown = null;
this.__shown = false;
if (this.quirks["inputtext_parents_cannot_contain_clip"]) {
if (this._shownclipvals != null) {
for (var $1_n = 0;
$1_n < this._shownclipvals.length;
$1_n++) {
var $2_v = this._shownclippedsprites[$1_n];
$2_v.__LZclickdiv.style.clip = this._shownclipvals[$1_n]
}
this._shownclipvals = null;
this._shownclippedsprites = null
}
}
if (this.quirks.fix_ie_clickable) {
this.__setCSSClassProperty(".lzclickdiv", "display", "");
this.__LzInputDiv = this.__LZclickdiv.removeChild(this.__LzInputDiv)
} else {
this.__LzInputDiv = this.__LZinputclickdiv.removeChild(this.__LzInputDiv)
}
this.__LZdiv.appendChild(this.__LzInputDiv)
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.__hide";
return $lzsc$temp
})();
LzInputTextSprite.prototype.gotBlur = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#160.39 -*- */
function  () {
if (LzInputTextSprite.prototype.__focusedSprite != this) {
return
}
this.deselect()
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.gotBlur";
return $lzsc$temp
})();
LzInputTextSprite.prototype.gotFocus = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#166.40 -*- */
function  () {
if (LzInputTextSprite.prototype.__focusedSprite == this) {
return
}
this.select()
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.gotFocus";
return $lzsc$temp
})();
LzInputTextSprite.prototype.setText = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#172.39 -*- */
function  ($1_t) {
if ($1_t == null) {
return
}
this.text = $1_t;
this.__createInputText($1_t);
this.__LzInputDiv.value = $1_t
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setText";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__setTextEvents = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#179.47 -*- */
function  ($1_c) {
if ($1_c) {
this.__LzInputDiv.onblur = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#182.36 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onblur")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onblur";
return $lzsc$temp
})();
this.__LzInputDiv.onmousedown = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#183.41 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onmousedown")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onmousedown";
return $lzsc$temp
})();
this.__LzInputDiv.onfocus = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#184.37 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onfocus")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onfocus";
return $lzsc$temp
})();
this.__LzInputDiv.onclick = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#185.37 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onclick")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onclick";
return $lzsc$temp
})();
this.__LzInputDiv.onkeyup = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#186.37 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onkeyup")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onkeyup";
return $lzsc$temp
})();
this.__LzInputDiv.onkeydown = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#187.39 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onkeydown")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onkeydown";
return $lzsc$temp
})();
this.__LzInputDiv.onselect = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#188.38 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onselect")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onselect";
return $lzsc$temp
})();
this.__LzInputDiv.onchange = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#189.38 -*- */
function  ($1_e) {
this.owner.__textEvent($1_e, "onchange")
}
$lzsc$temp._dbg_name = "this.__LzInputDiv.onchange";
return $lzsc$temp
})()
} else {
this.__LzInputDiv.onblur = null;
this.__LzInputDiv.onmousedown = null;
this.__LzInputDiv.onfocus = null;
this.__LzInputDiv.onclick = null;
this.__LzInputDiv.onkeyup = null;
this.__LzInputDiv.onkeydown = null;
this.__LzInputDiv.onselect = null;
this.__LzInputDiv.onchange = null
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.__setTextEvents";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__textEvent = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#202.43 -*- */
function  ($1_e, $2_eventname) {
if (this.destroyed == true) {
return
}
var $3_keycode = $1_e ? $1_e.keyCode : event.keyCode;
if ($2_eventname == "onfocus" || $2_eventname == "onmousedown") {
LzInputTextSprite.prototype.__focusedSprite = this;
this.__show();
if ($2_eventname == "onfocus" && this._cancelfocus) {
this._cancelfocus = false;
return
}
if (window["LzKeyboardKernel"]) {
LzKeyboardKernel.__cancelKeys = false
}
} else {
if ($2_eventname == "onblur") {
if (window["LzKeyboardKernel"]) {
LzKeyboardKernel.__cancelKeys = true
}
if (LzInputTextSprite.prototype.__focusedSprite == this) {
LzInputTextSprite.prototype.__focusedSprite = null
}
this.__hide();
if (this._cancelblur) {
this._cancelblur = false;
return
}
}
}
if (this.owner) {
this.owner.inputtextevent($2_eventname, $3_keycode);
if ($2_eventname == "onkeydown" || $2_eventname == "onkeyup") {
var $4_v = this.__LzInputDiv.value;
if ($4_v != this.text) {
this.text = $4_v;
this.owner.inputtextevent("onchange", $4_v)
}
}
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.__textEvent";
return $lzsc$temp
})();
LzInputTextSprite.prototype.setEnabled = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#238.42 -*- */
function  ($1_val) {
this.disabled = !$1_val;
this.__LzInputDiv.disabled = this.disabled
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setEnabled";
return $lzsc$temp
})();
LzInputTextSprite.prototype.setMaxLength = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#243.44 -*- */
function  ($1_val) {
if ($1_val == null) {
return
}
this.__LzInputDiv.maxLength = $1_val
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setMaxLength";
return $lzsc$temp
})();
LzInputTextSprite.prototype.select = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#248.38 -*- */
function  ($1_start, $2_end) {
this._cancelblur = true;
this.__show();
this.__LzInputDiv.focus();
LzInputTextSprite.__lastfocus = this;
setTimeout("LzInputTextSprite.__lastfocus.__LzInputDiv.select()", 50);
if (window["LzKeyboardKernel"]) {
LzKeyboardKernel.__cancelKeys = false
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.select";
return $lzsc$temp
})();
LzInputTextSprite.prototype.setSelection = LzInputTextSprite.prototype.select;
LzInputTextSprite.prototype.deselect = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#261.40 -*- */
function  () {
this._cancelfocus = true;
this.__hide();
if (this.__LzInputDiv && this.__LzInputDiv.blur) {
this.__LzInputDiv.blur()
}
if (window["LzKeyboardKernel"]) {
LzKeyboardKernel.__cancelKeys = true
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.deselect";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__fontStyle = "normal";
LzInputTextSprite.prototype.__fontWeight = "normal";
LzInputTextSprite.prototype.__fontSize = "11px";
LzInputTextSprite.prototype.__fontFamily = "Verdana,Vera,sans-serif";
LzInputTextSprite.prototype.__setFontSize = LzTextSprite.prototype.setFontSize;
LzInputTextSprite.prototype.setFontSize = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#276.43 -*- */
function  ($1_fsize) {
this.__setFontSize($1_fsize);
if (this.__fontSize != this._fontSize) {
this.__fontSize = this._fontSize;
this.__LzInputDiv.style.fontSize = this._fontSize
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setFontSize";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__setFontStyle = LzTextSprite.prototype.setFontStyle;
LzInputTextSprite.prototype.setFontStyle = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#285.44 -*- */
function  ($1_fstyle) {
this.__setFontStyle($1_fstyle);
if (this.__fontStyle != this._fontStyle) {
this.__fontStyle = this._fontStyle;
this.__LzInputDiv.style.fontStyle = this._fontStyle
}
if (this.__fontWeight != this._fontWeight) {
this.__fontWeight = this._fontWeight;
this.__LzInputDiv.style.fontWeight = this._fontWeight
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setFontStyle";
return $lzsc$temp
})();
LzInputTextSprite.prototype.__setFontName = LzTextSprite.prototype.setFontName;
LzInputTextSprite.prototype.setFontName = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#298.43 -*- */
function  ($1_fname) {
this.__setFontName($1_fname);
if (this.__fontFamily != this._fontFamily) {
this.__fontFamily = this._fontFamily;
this.__LzInputDiv.style.fontFamily = this._fontFamily
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setFontName";
return $lzsc$temp
})();
LzInputTextSprite.prototype.setWidth = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#306.40 -*- */
function  ($1_w) {
if ($1_w == null || $1_w < 0 || isNaN($1_w) || this.width == $1_w) {
return
}
this.__setWidth($1_w - this.____wpadding);
if (this.quirks.fix_clickable) {
var $1_w = this.CSSDimension(this.width);
this.__LZclickdiv.style.width = $1_w;
this.__LZinputclickdiv.style.width = $1_w
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setWidth";
return $lzsc$temp
})();
LzInputTextSprite.prototype.setHeight = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#317.41 -*- */
function  ($1_h) {
if ($1_h == null || $1_h < 0 || isNaN($1_h) || this.height == $1_h) {
return
}
this.__setHeight($1_h);
if (this.quirks.fix_clickable) {
var $1_h = this.CSSDimension(this.height);
this.__LZclickdiv.style.height = $1_h;
this.__LZinputclickdiv.style.height = $1_h;
if (this.multiline && this.quirks.set_height_for_multiline_inputtext) {
this.__LzInputDiv.style.height = this.CSSDimension(this.height - this.____hpadding * 2)
}
}
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setHeight";
return $lzsc$temp
})();
LzInputTextSprite.prototype.setColor = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#332.40 -*- */
function  ($1_c) {
if (this.color == $1_c) {
return
}
this.color = $1_c;
this.__LzInputDiv.style.color = LzUtils.color.inttohex($1_c)
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.setColor";
return $lzsc$temp
})();
LzInputTextSprite.prototype.getText = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#338.39 -*- */
function  () {
return this.__LzInputDiv.value
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.getText";
return $lzsc$temp
})();
LzInputTextSprite.prototype.getTextfieldHeight = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzInputTextSprite.js#342.50 -*- */
function  () {
if (this.fieldHeight != null) {
return this.fieldHeight
}
if (this.text == null || this.text == "") {
var $1_testheight = true;
this.text = "YgjyZT;:"
}
if (this.multiline) {
var $2_oldheight = false;
if (this.height) {
$2_oldheight = this.__LzInputDiv.style.height;
this.__LzInputDiv.style.height = "auto"
}
var $3_h = this.__LzInputDiv.scrollHeight;
if ($3_h == 0 || $3_h == null) {
$3_h = this.getTextSize(this.text).height
} else {
if (this.quirks["safari_textarea_subtract_scrollbar_height"]) {
$3_h += 24
}
this.fieldHeight = $3_h
}
if (this.height) {
this.__LzInputDiv.style.height = $2_oldheight
}
} else {
var $3_h = this.getTextSize(this.text).height;
if ($3_h != 0) {
this.fieldHeight = $3_h
}
}
if (this.quirks.emulate_flash_font_metrics) {
$3_h += 4
}
if ($1_testheight) {
this.text = ""
}
return $3_h
}
$lzsc$temp._dbg_name = "LzInputTextSprite.prototype.getTextfieldHeight";
return $lzsc$temp
})();
var LzXMLParser = new Object();
LzXMLParser.parseXML = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzXMLParser.js#17.24 -*- */
function  ($1_str, $2_trimwhitespace, $3_nsprefix) {
var $4_parser = new DOMParser();
var $5_doc = $4_parser.parseFromString($1_str, "text/xml");
return $5_doc.childNodes[0]
}
$lzsc$temp._dbg_name = "LzXMLParser.parseXML";
return $lzsc$temp
})();
if (typeof DOMParser == "undefined") {
var DOMParser = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzXMLParser.js#33.20 -*- */
function  () {

}
$lzsc$temp._dbg_name = "kernel/dhtml/LzXMLParser.js#33/20";
return $lzsc$temp
})();
DOMParser.prototype.parseFromString = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzXMLParser.js#35.42 -*- */
function  ($1_str, $2_contentType) {
if (typeof window.ActiveXObject != "undefined") {
var $3_d = new ActiveXObject("MSXML.DomDocument");
$3_d.loadXML($1_str);
return $3_d
} else {
if (typeof XMLHttpRequest != "undefined") {
var $4_req = new XMLHttpRequest();
$4_req.open("GET", "data:" + ($2_contentType || "application/xml") + ";charset=utf-8," + encodeURIComponent($1_str), false);
if ($4_req.overrideMimeType) {
$4_req.overrideMimeType($2_contentType)
}
$4_req.send(null);
return $4_req.responseXML
}
}
}
$lzsc$temp._dbg_name = "DOMParser.prototype.parseFromString";
return $lzsc$temp
})()
}
var LzXMLTranslator = new Object();
LzXMLTranslator.copyXML = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzXMLTranslator.js#17.27 -*- */
function  ($1_xmldoc, $2_trimwhitespace, $3_nsprefix) {
var $4_lfcnode = LzXMLTranslator.copyBrowserXML($1_xmldoc, true, $2_trimwhitespace, $3_nsprefix);
return $4_lfcnode
}
$lzsc$temp._dbg_name = "LzXMLTranslator.copyXML";
return $lzsc$temp
})();
LzXMLTranslator.whitespacePat = new RegExp("^[\t\n\r ]*$");
LzXMLTranslator.stringTrimPat = new RegExp("(^[\t\n\r ]*|[\t\n\r ]*$)", "g");
LzXMLTranslator.slashPat = new RegExp("/", "g");
LzXMLTranslator.copyBrowserXML = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzXMLTranslator.js#27.34 -*- */
function  ($1_node, $2_ignorewhite, $3_trimwhite, $4_nsprefix) {
if (!$1_node) {
return $1_node
}
var $5_nv = $1_node.nodeValue;
var $6_lfcnode = null;
if ($1_node.nodeType == 3 || $1_node.nodeType == 4) {
if ($2_ignorewhite && LzXMLTranslator.whitespacePat.test($5_nv)) {
return null
}
if ($3_trimwhite) {
var $7_nvo = $5_nv;
$5_nv = $5_nv.replace(LzXMLTranslator.stringTrimPat, "")
}
$6_lfcnode = new LzDataText($5_nv);
return $6_lfcnode
} else {
if ($1_node.nodeType == 1 || $1_node.nodeType == 9) {
var $8_nattrs = $1_node.attributes;
var $9_cattrs = {}
if ($8_nattrs) {
for (var $10_k = 0;
$10_k < $8_nattrs.length;
$10_k++) {
var $11_attrnode = $8_nattrs.item($10_k);
if ($11_attrnode) {
var $12_attrname = $11_attrnode.name;
var $13_attrval = $11_attrnode.value;
var $14_nattrname = $12_attrname;
if (!$4_nsprefix) {
var $15_colpos = $12_attrname.indexOf(":");
if ($15_colpos >= 0) {
$14_nattrname = $12_attrname.substring($15_colpos + 1)
}
}
$9_cattrs[$14_nattrname] = $13_attrval
}
}
}
var $16_nname = $1_node.nodeName;
if ($16_nname && !$4_nsprefix) {
var $17_npos = $16_nname.indexOf(":");
if ($17_npos >= 0) {
$16_nname = $16_nname.substring($17_npos + 1)
}
}
$6_lfcnode = new LzDataElement($16_nname, $9_cattrs);
var $18_children = $1_node.childNodes;
var $19_newchildren = [];
for (var $20_i = 0;
$20_i < $18_children.length;
$20_i++) {
var $21_child = $18_children[$20_i];
var $22_lfcchild = LzXMLTranslator.copyBrowserXML($21_child, $2_ignorewhite, $3_trimwhite, $4_nsprefix);
if ($22_lfcchild != null) {
$19_newchildren.push($22_lfcchild)
}
}
$6_lfcnode.setChildNodes($19_newchildren);
return $6_lfcnode
} else {
return null
}
}
}
$lzsc$temp._dbg_name = "LzXMLTranslator.copyBrowserXML";
return $lzsc$temp
})();
var LzHTTPLoader = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#12.20 -*- */
function  ($1_owner, $2_proxied) {
this.owner = $1_owner;
this.options = {parsexml: true}
this.requestheaders = {}
this.requestmethod = LzHTTPLoader.GET_METHOD
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzHTTPLoader.js#12/20";
return $lzsc$temp
})();
LzHTTPLoader.prototype.loadSuccess = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#20.38 -*- */
function  ($1_loader, $2_data) {

}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.loadSuccess";
return $lzsc$temp
})();
LzHTTPLoader.prototype.loadError = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#21.38 -*- */
function  ($1_loader, $2_data) {

}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.loadError";
return $lzsc$temp
})();
LzHTTPLoader.prototype.loadTimeout = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#22.38 -*- */
function  ($1_loader, $2_data) {

}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.loadTimeout";
return $lzsc$temp
})();
LzHTTPLoader.prototype.getResponse = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#25.38 -*- */
function  () {
return this.responseText
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.getResponse";
return $lzsc$temp
})();
LzHTTPLoader.prototype.getResponseStatus = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#30.44 -*- */
function  () {

}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.getResponseStatus";
return $lzsc$temp
})();
LzHTTPLoader.prototype.getResponseHeaders = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#39.45 -*- */
function  () {
return this.req.getAllResponseHeaders()
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.getResponseHeaders";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setRequestHeaders = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#49.44 -*- */
function  ($1_obj) {
this.requestheaders = $1_obj
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setRequestHeaders";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setRequestHeader = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#58.43 -*- */
function  ($1_key, $2_val) {
this.requestheaders[$1_key] = $2_val
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setRequestHeader";
return $lzsc$temp
})();
LzHTTPLoader.prototype.abort = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#62.32 -*- */
function  () {
if (this.req) {
this.req.cancel()
}
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.abort";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setOption = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#70.36 -*- */
function  ($1_key, $2_val) {
this.options[$1_key] = $2_val
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setOption";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setProxied = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#76.37 -*- */
function  ($1_proxied) {
this.setOption("proxied", $1_proxied)
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setProxied";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setQueryParams = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#82.41 -*- */
function  ($1_qparams) {
this.queryparams = $1_qparams
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setQueryParams";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setQueryString = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#88.41 -*- */
function  ($1_qstring) {
this.querystring = $1_qstring
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setQueryString";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setQueueing = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#96.38 -*- */
function  ($1_queuing) {
this.setOption("queuing", $1_queuing)
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setQueueing";
return $lzsc$temp
})();
LzHTTPLoader.prototype.getResponseHeader = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#104.44 -*- */
function  ($1_key) {
return this.req.getResponseHeader($1_key)
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.getResponseHeader";
return $lzsc$temp
})();
LzHTTPLoader.GET_METHOD = "GET";
LzHTTPLoader.POST_METHOD = "POST";
LzHTTPLoader.PUT_METHOD = "PUT";
LzHTTPLoader.DELETE_METHOD = "DELETE";
LzHTTPLoader.prototype.open = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#115.31 -*- */
function  ($1_method, $2_url, $3_username, $4_password) {
this.req = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
this.requesturl = $2_url;
this.requestmethod = $1_method
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.open";
return $lzsc$temp
})();
LzHTTPLoader.prototype.makeProxiedURL = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#129.45 -*- */
function  ($1_url, $2_reqtype, $3_lzt, $4_headers) {
var $5_proxyurl = LzBrowser.getBaseURL();
var $6_qargs = {lzt: $3_lzt != null ? $3_lzt : "xmldata", reqtype: $2_reqtype, sendheaders: this.options.sendheaders, trimwhitespace: this.options.trimwhitespace, nsprefix: this.options.nsprefix, url: LzBrowser.toAbsoluteURL($1_url, this.secure), timeout: this.timeout, cache: this.options.cacheable, ccache: this.options.ccache}
var $7_hname;
var $8_headerString = "";
if ($4_headers != null) {
for ($7_hname in $4_headers) {
$8_headerString += $7_hname + ": " + $4_headers[$7_hname] + "\n"
}
}
if ($8_headerString != "") {
$6_qargs["headers"] = $8_headerString
}
if (!this.options.ccache) {
$6_qargs.__lzbc__ = new Date().getTime()
}
$5_proxyurl += "?";
var $9_sep = "";
for (var $10_key in $6_qargs) {
var $11_val = $6_qargs[$10_key];
if (typeof $11_val == "string") {
$11_val = encodeURIComponent($11_val);
$11_val = $11_val.replace(LzDataset.slashPat, "%2F")
}
$5_proxyurl += $9_sep + $10_key + "=" + $11_val;
$9_sep = "&"
}
return $5_proxyurl
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.makeProxiedURL";
return $lzsc$temp
})();
LzHTTPLoader.prototype.send = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#179.31 -*- */
function  ($1_content) {
this.loadXMLDoc(this.requestmethod, this.requesturl, this.requestheaders, $1_content, true, true)
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.send";
return $lzsc$temp
})();
LzHTTPLoader.activeRequests = [];
LzHTTPLoader.prototype.timeout = Infinity;
LzHTTPLoader.prototype.setTimeout = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#197.37 -*- */
function  ($1_timeout) {
this.timeout = $1_timeout
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setTimeout";
return $lzsc$temp
})();
LzHTTPLoader.prototype.setupTimeout = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#203.39 -*- */
function  ($1_obj, $2_duration) {
var $3_endtime = new Date().getTime() + $2_duration;
LzHTTPLoader.activeRequests.push($1_obj, $3_endtime);
setTimeout("LzHTTPLoader.__LZcheckXMLHTTPTimeouts()", $2_duration)
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.setupTimeout";
return $lzsc$temp
})();
LzHTTPLoader.prototype.removeTimeout = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#210.40 -*- */
function  ($1_target) {
var $2_activeReqs = LzHTTPLoader.activeRequests;
LzHTTPLoader.activeRequests = [];
for (var $3_i = 0;
$3_i < $2_activeReqs.length;
$3_i += 2) {
var $4_dset = $2_activeReqs[$3_i];
var $5_dstimeout = $2_activeReqs[$3_i + 1];
if ($4_dset != $1_target) {
LzHTTPLoader.activeRequests.push($4_dset, $5_dstimeout)
}
}
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.removeTimeout";
return $lzsc$temp
})();
LzHTTPLoader.__LZcheckXMLHTTPTimeouts = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#225.41 -*- */
function  () {
var $1_activeReqs = LzHTTPLoader.activeRequests;
LzHTTPLoader.activeRequests = [];
for (var $2_i = 0;
$2_i < $1_activeReqs.length;
$2_i += 2) {
var $3_loader = $1_activeReqs[$2_i];
var $4_dstimeout = $1_activeReqs[$2_i + 1];
var $5_now = new Date().getTime();
if ($5_now > $4_dstimeout) {
if ($3_loader.req) {
$3_loader.req.abort()
}
this.req = null;
$3_loader.loadTimeout($3_loader, null)
} else {
LzHTTPLoader.activeRequests.push($3_loader, $4_dstimeout)
}
}
}
$lzsc$temp._dbg_name = "LzHTTPLoader.__LZcheckXMLHTTPTimeouts";
return $lzsc$temp
})();
LzHTTPLoader.prototype.getElapsedTime = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#245.41 -*- */
function  () {
return new Date().getTime() - this.gstart
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.getElapsedTime";
return $lzsc$temp
})();
LzHTTPLoader.prototype.__setRequestHeaders = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#250.46 -*- */
function  ($1_xhr, $2_headers) {
if ($2_headers != null) {
for (var $3_key in $2_headers) {
var $4_val = $2_headers[$3_key];
$1_xhr.setRequestHeader($3_key, $4_val)
}
}
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.__setRequestHeaders";
return $lzsc$temp
})();
LzHTTPLoader.prototype.loadXMLDoc = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#264.37 -*- */
function  ($1_method, $2_url, $3_headers, $4_postbody, $5_ignorewhite, parsexml) {
if (this.req) {
var __pthis__ = this;
this.req.onreadystatechange = (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzHTTPLoader.js#268.39 -*- */
function  () {
if (__pthis__.req == null) {
return
}
if (__pthis__.req.readyState == 4) {
if (__pthis__.req.status == 200 || __pthis__.req.status == 304) {
var $1_elt = null;
var $2_xml = __pthis__.req.responseXML;
__pthis__.responseXML = $2_xml;
var $3_lzxdata = null;
if ($2_xml != null && parsexml) {
var $4_nodes = __pthis__.req.responseXML.childNodes;
for (var $5_i = 0;
$5_i < $4_nodes.length;
$5_i++) {
var $6_child = $4_nodes.item($5_i);
if ($6_child.nodeType == 1) {
$1_elt = $6_child;
break
}
}
$3_lzxdata = LzXMLTranslator.copyXML($1_elt, __pthis__.options.trimwhitespace, __pthis__.options.nsprefix)
}
__pthis__.responseText = __pthis__.req.responseText;
__pthis__.removeTimeout(__pthis__);
__pthis__.req = null;
__pthis__.loadSuccess(__pthis__, $3_lzxdata)
} else {
__pthis__.req = null;
__pthis__.loadError(__pthis__, null)
}
}
}
$lzsc$temp._dbg_name = "this.req.onreadystatechange";
return $lzsc$temp
})();
this.req.open($1_method, $2_url, true);
if ($1_method == "POST" && $3_headers["content-type"] == null) {
$3_headers["content-type"] = "application/x-www-form-urlencoded"
}
this.__setRequestHeaders(this.req, $3_headers);
this.req.send($4_postbody)
}
this.setupTimeout(this, this.timeout)
}
$lzsc$temp._dbg_name = "LzHTTPLoader.prototype.loadXMLDoc";
return $lzsc$temp
})();
var LzScreenKernel = {width: null, height: null, __resizeEvent: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzScreenKernel.js#16.21 -*- */
function  () {
if (window.top.innerHeight) {
var $1_sc = window.top.document.body;
LzScreenKernel.width = $1_sc.scrollWidth;
LzScreenKernel.height = $1_sc.scrollHeight
} else {
if (window.top.document.documentElement && window.top.document.documentElement.clientHeight) {
var $1_sc = window.top.document.documentElement;
LzScreenKernel.width = $1_sc.scrollWidth;
LzScreenKernel.height = $1_sc.scrollHeight
} else {
if (window.top.document.body) {
var $1_sc = window.top.document.body;
LzScreenKernel.width = window.top.document.body.scrollWidth;
LzScreenKernel.height = window.top.document.body.clientHeight
}
}
}
if (LzScreenKernel.__callback) {
LzScreenKernel.__scope[LzScreenKernel.__callback]({width: LzScreenKernel.width, height: LzScreenKernel.height})
}
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzScreenKernel.js#16/21";
return $lzsc$temp
})(), __init: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzScreenKernel.js#53.14 -*- */
function  () {
window.onresize = LzScreenKernel.__resizeEvent
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzScreenKernel.js#53/14";
return $lzsc$temp
})(), __callback: null, __scope: null, setCallback: (function  () {
var $lzsc$temp = 
/* -*- file: kernel/dhtml/LzScreenKernel.js#58.19 -*- */
function  ($1_scope, $2_funcname) {
this.__scope = $1_scope;
this.__callback = $2_funcname;
this.__init();
this.__resizeEvent()
}
$lzsc$temp._dbg_name = "kernel/dhtml/LzScreenKernel.js#58/19";
return $lzsc$temp
})()}
var LzContextMenu = Class.make("LzContextMenu", LzNode, {initialize: 
/* -*- file: kernel/dhtml/LzContextMenu.js#22.1 -*- */
function initialize ($1_del) {
this.__LZmousedowndel = new LzDelegate(this, "__hide");
this.items = [];
this.setDelegate($1_del)
}, showbuiltins: false, setDelegate: 
/* -*- file: kernel/dhtml/LzContextMenu.js#41.1 -*- */
function setDelegate ($1_delegate) {
this._delegate = $1_delegate
}, addItem: 
/* -*- file: kernel/dhtml/LzContextMenu.js#51.1 -*- */
function addItem ($1_item) {
this.items.push($1_item)
}, __show: 
/* -*- file: kernel/dhtml/LzContextMenu.js#56.1 -*- */
function __show () {
var $1_s = document.getElementById("lzcontextmenu");
if (!$1_s) {
$1_s = document.createElement("div");
Lz.__setAttr($1_s, "id", "lzcontextmenu");
Lz.__setAttr($1_s, "style", "display: none");
document.body.appendChild($1_s)
}
if (this.onmenuopen.ready) {
this.onmenuopen.sendEvent(this)
}
var $2_o = "";
for (var $3_i = 0;
$3_i < this.items.length;
$3_i++) {
var $4_v = this.items[$3_i].cmenuitem;
if ($4_v.visible != true) {
continue
}
if ($4_v.separatorBefore) {
$2_o += "<br/>"
}
if ($4_v.enabled) {
$2_o += '<a onmousedown="javascript:LzMouseKernel.__showncontextmenu.__select(' + $3_i + ');return false;"';
$2_o += ">" + $4_v.caption + "</a>"
} else {
$2_o += $4_v.caption
}
}
LzMouseKernel.__showncontextmenu = this;
$1_s.innerHTML = $2_o;
$1_s.style.left = LzMouseKernel.__x + "px";
$1_s.style.top = LzMouseKernel.__y + "px";
$1_s.style.display = "block";
this.__LZmousedowndel.register(LzGlobalMouse, "onmousedown");
if (this._delegate != null) {
this._delegate.execute(this)
}
}, __hide: 
/* -*- file: kernel/dhtml/LzContextMenu.js#91.1 -*- */
function __hide () {
var $1_s = document.getElementById("lzcontextmenu");
if (!$1_s) {
return
}
$1_s.style.display = "none";
this.__LZmousedowndel.unregisterAll()
}, __select: 
/* -*- file: kernel/dhtml/LzContextMenu.js#99.1 -*- */
function __select ($1_i) {
this.__hide();
if (this.items[$1_i]) {
this.items[$1_i].__select()
}
}, hideBuiltInItems: 
/* -*- file: kernel/dhtml/LzContextMenu.js#108.1 -*- */
function hideBuiltInItems () {
this.showbuiltins = false
}, clearItems: 
/* -*- file: kernel/dhtml/LzContextMenu.js#117.1 -*- */
function clearItems () {
this.items = []
}, getItems: 
/* -*- file: kernel/dhtml/LzContextMenu.js#126.1 -*- */
function getItems () {
return this.items
}, makeMenuItem: 
/* -*- file: kernel/dhtml/LzContextMenu.js#137.1 -*- */
function makeMenuItem ($1_title, $2_callback) {
var $3_item = new LzContextMenuItem($1_title, $2_callback);
return $3_item
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzContextMenu) {
with (LzContextMenu.prototype) {
DeclareEvent(prototype, "onmenuopen")
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzContextMenuItem = Class.make("LzContextMenuItem", LzNode, {initialize: 
/* -*- file: kernel/dhtml/LzContextMenu.js#155.1 -*- */
function initialize ($1_title, $2_del) {
this.cmenuitem = {visible: true, enabled: true, separatorBefore: false, caption: $1_title}
this.setDelegate($2_del)
}, setDelegate: 
/* -*- file: kernel/dhtml/LzContextMenu.js#170.1 -*- */
function setDelegate ($1_delegate) {
this._delegate = $1_delegate
}, __select: 
/* -*- file: kernel/dhtml/LzContextMenu.js#175.1 -*- */
function __select () {
if (this.onselect.ready) {
this.onselect.sendEvent(this)
}
if (this._delegate != null) {
if (this._delegate instanceof LzDelegate) {
this._delegate.execute(this)
} else {
if (typeof this._delegate == "function") {
this._delegate(this)
} else {
Debug.error("LzContextMenuItem.setDelegate must be passed a delegate", this, this._delegate)
}
}
}
}, setCaption: 
/* -*- file: kernel/dhtml/LzContextMenu.js#196.1 -*- */
function setCaption ($1_caption) {
this.cmenuitem.caption = $1_caption
}, setEnabled: 
/* -*- file: kernel/dhtml/LzContextMenu.js#205.1 -*- */
function setEnabled ($1_val) {
this.cmenuitem.enabled = $1_val
}, setSeparatorBefore: 
/* -*- file: kernel/dhtml/LzContextMenu.js#216.1 -*- */
function setSeparatorBefore ($1_val) {
this.cmenuitem.separatorBefore = $1_val
}, setVisible: 
/* -*- file: kernel/dhtml/LzContextMenu.js#226.1 -*- */
function setVisible ($1_val) {
this.cmenuitem.visible = $1_val
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzContextMenuItem) {
with (LzContextMenuItem.prototype) {
DeclareEvent(prototype, "onselect")
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzView = Class.make("LzView", LzNode, {DOUBLE_CLICK_TIME: 500, capabilities: LzSprite.prototype.capabilities, construct: 
/* -*- file: views/LaszloView.lzs#202.1 -*- */
function construct ($1_parent, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).call(this, $1_parent ? $1_parent : canvas, $2_args);
var $3_ip = this.immediateparent;
this.mask = $3_ip.mask;
this.__makeSprite($2_args);
if ("width" in $2_args || "$refs" in $2_args && "width" in $2_args.$refs && $2_args.$refs.width) {
this.hassetwidth = true;
this.__LZcheckwidth = false
}
if ("height" in $2_args || "$refs" in $2_args && "height" in $2_args.$refs && $2_args.$refs.height) {
this.hassetheight = true;
this.__LZcheckheight = false
}
var $4_r = null;
if ("resource" in $2_args && $2_args["resource"] != null) {
$4_r = $2_args.resource;
$2_args.resource = LzNode._ignoreAttribute
}
if ("clip" in $2_args && $2_args["clip"]) {
if (this.sprite) {
this.makeMasked()
}
}
if ($2_args["stretches"]) {
if (this.sprite) {
this.stretchResource($2_args.stretches)
}
}
if ($4_r != null) {
if (this.sprite) {
this.setResource($4_r)
}
}
}, spriteAttribute: 
/* -*- file: views/LaszloView.lzs#244.1 -*- */
function spriteAttribute ($1_attrname, $2_value) {
if (this[$1_attrname]) {
if (!this.__LZdeleted) {
var $lzsc$306123356 = this.setters;
if ($lzsc$306123356 && $1_attrname in $lzsc$306123356) {
this[$lzsc$306123356[$1_attrname]]($2_value)
} else {
if ($lzsc$306123356 == null) {
Debug.warn("null setters on", this, $1_attrname, $2_value)
}
this[$1_attrname] = $2_value;
var $lzsc$954434156 = "on" + $1_attrname;
if ($lzsc$954434156 in this) {
if (this[$lzsc$954434156].ready) {
this[$lzsc$954434156].sendEvent($2_value)
}
}
}
}
}
}, __makeSprite: 
/* -*- file: views/LaszloView.lzs#253.1 -*- */
function __makeSprite ($1_args) {
this.sprite = new LzSprite(this, false, $1_args)
}, init: 
/* -*- file: views/LaszloView.lzs#263.1 -*- */
function init () {
this.sprite.init(this.visible)
}, addSubview: 
/* -*- file: views/LaszloView.lzs#277.1 -*- */
function addSubview ($1_s) {
this.sprite.addChildSprite($1_s.sprite);
if ($1_s.addedToParent) {
return
}
if (this.subviews.length == 0) {
this.subviews = []
}
this.subviews.push($1_s);
$1_s.addedToParent = true;
if (this.__LZcheckwidth) {
this.__LZcheckwidthFunction($1_s)
}
if (this.__LZcheckheight) {
this.__LZcheckheightFunction($1_s)
}
if (this.onaddsubview.ready) {
this.onaddsubview.sendEvent($1_s)
}
}, __LZinstantiationDone: 
/* -*- file: views/LaszloView.lzs#306.1 -*- */
function __LZinstantiationDone () {
this.immediateparent.addSubview(this);
(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZinstantiationDone"] : this.nextMethod(arguments.callee, "__LZinstantiationDone")).apply(this, arguments)
}, mask: null, focusable: false, focustrap: null, fontstyle: null, fontsize: null, sprite: null, visible: true, visibility: "collapse", __LZvizO: true, __LZvizDat: true, __LZvizLoad: true, opacity: 1, bgcolor: null, x: 0, y: 0, rotation: 0, width: 0, height: 0, unstretchedwidth: 0, unstretchedheight: 0, subviews: [], __LZclickregion: "LzMouseEvents", xoffset: 0, yoffset: 0, __LZrsin: 0, __LZrcos: 1, __LZcaloffset: false, _xscale: 1, _yscale: 1, totalframes: 0, frame: 0, loadperc: 0, framesloadratio: 0, loadratio: 0, hassetheight: false, hassetwidth: false, __LZisView: true, addedToParent: null, checkPlayStatusDel: null, masked: false, pixellock: null, setButtonSize: null, clickable: false, showhandcursor: null, updatePlayDel: null, resource: null, resourcewidth: null, resourceheight: null, __LZbgColorO: null, __LZbgRef: null, __LZbuttonRef: null, __LZcheckwidth: true, __LZcheckheight: true, __LZhasoffset: null, __LZisBackgrounded: null, __LZmaskClip: null, __LZmovieClipRef: null, __LZoutlieheight: null, __LZoutliewidth: null, __LZsubUniqueNum: null, setLayout: 
/* -*- file: views/LaszloView.lzs#759.1 -*- */
function setLayout ($1_layoutobj) {
if (!this.isinited) {
this.__LZstoreAttr($1_layoutobj, "layout");
return
}
var $2_classname = $1_layoutobj["class"];
if ($2_classname == null) {
$2_classname = "simplelayout"
}
if (this.__LZlayout) {
this.__LZlayout.destroy()
}
if ($2_classname != "none") {
var $3_o = {}
for (var $4_i in $1_layoutobj) {
if ($4_i != "class") {
$3_o[$4_i] = $1_layoutobj[$4_i]
}
}
if ($2_classname == "null") {
this.__LZlayout = null;
return
}
this.__LZlayout = new (ConstructorMap[$2_classname])(this, $3_o)
}
}, setFontName: 
/* -*- file: views/LaszloView.lzs#803.1 -*- */
function setFontName ($1_val, $2_prop) {
this.fontname = $1_val;
if (!this.sprite) {
return
}
this.sprite.setFontName($1_val, $2_prop)
}, _setrescwidth: false, _setrescheight: false, searchSubviews: 
/* -*- file: views/LaszloView.lzs#838.1 -*- */
function searchSubviews ($1_prop, $2_val) {
var $3_nextS = this.subviews.concat();
while ($3_nextS.length > 0) {
var $4_s = $3_nextS;
$3_nextS = new Array();
for (var $5_i = $4_s.length - 1;
$5_i >= 0;
$5_i--) {
var $6_si = $4_s[$5_i];
if ($6_si[$1_prop] == $2_val) {
return $6_si
}
var $7_sis = $6_si.subviews;
for (var $8_j = $7_sis.length - 1;
$8_j >= 0;
$8_j--) {
$3_nextS.push($7_sis[$8_j])
}
}
}
return null
}, searchimmediateparents: 
/* -*- file: views/LaszloView.lzs#859.1 -*- */
function searchimmediateparents ($1_prop) {
Debug.deprecated(this, arguments.callee, this.searchParents);
return this.searchParents($1_prop)
}, layouts: null, releaseLayouts: 
/* -*- file: views/LaszloView.lzs#885.1 -*- */
function releaseLayouts () {
if (this.layouts) {
for (var $1_i = this.layouts.length - 1;
$1_i >= 0;
$1_i--) {
this.layouts[$1_i].releaseLayout()
}
}
}, setResource: 
/* -*- file: views/LaszloView.lzs#900.1 -*- */
function setResource ($1_resourceName) {
if ($1_resourceName == null || $1_resourceName == this._resource) {
return
}
if ($1_resourceName != "empty") {
this.sprite.setResource($1_resourceName)
}
this.__LZhaser = $1_resourceName == "empty";
this.resource = $1_resourceName;
if (this.onresource.ready) {
this.onresource.sendEvent($1_resourceName)
}
this._resource = this.resource;
return this.sprite.__LZmovieClipRef
}, resourceload: 
/* -*- file: views/LaszloView.lzs#934.1 -*- */
function resourceload ($1_i) {
if ("resource" in $1_i) {
this.resource = $1_i.resource;
if (this.onresource.ready) {
this.onresource.sendEvent($1_i.resource)
}
}
if (this.resourcewidth != $1_i.width) {
if ("width" in $1_i) {
this.resourcewidth = $1_i.width;
if (this.onresourcewidth.ready) {
this.onresourcewidth.sendEvent($1_i.width)
}
}
if (!this.hassetwidth && this.resourcewidth != this.width || this._setrescwidth && this.unstretchedwidth != this.resourcewidth) {
this.updateWidth(this.resourcewidth)
}
}
if (this.resourceheight != $1_i.height) {
if ("height" in $1_i) {
this.resourceheight = $1_i.height;
if (this.onresourceheight.ready) {
this.onresourceheight.sendEvent($1_i.height)
}
}
if (!this.hassetheight && this.resourceheight != this.height || this._setrescheight && this.unstretchedheight != this.resourceheight) {
this.updateHeight(this.resourceheight)
}
}
if ($1_i.skiponload != true) {
if (this.onload.ready) {
this.onload.sendEvent(this)
}
}
}, resourceloaderror: 
/* -*- file: views/LaszloView.lzs#975.1 -*- */
function resourceloaderror () {
this.resourcewidth = 0;
this.resourceheight = 0;
if (this.onresourcewidth.ready) {
this.onresourcewidth.sendEvent(0)
}
if (this.onresourceheight.ready) {
this.onresourceheight.sendEvent(0)
}
this.reevaluateSize();
if (this.onerror.ready) {
this.onerror.sendEvent()
}
}, resourceloadtimeout: 
/* -*- file: views/LaszloView.lzs#988.1 -*- */
function resourceloadtimeout () {
this.resourcewidth = 0;
this.resourceheight = 0;
if (this.onresourcewidth.ready) {
this.onresourcewidth.sendEvent(0)
}
if (this.onresourceheight.ready) {
this.onresourceheight.sendEvent(0)
}
this.reevaluateSize();
if (this.ontimeout.ready) {
this.ontimeout.sendEvent()
}
}, setTotalFrames: 
/* -*- file: views/LaszloView.lzs#1004.1 -*- */
function setTotalFrames ($1_n) {
if ($1_n != null && this.totalframes != $1_n) {
this.totalframes = $1_n;
if (this.ontotalframes.ready) {
this.ontotalframes.sendEvent(this.totalframes)
}
}
}, destroy: 
/* -*- file: views/LaszloView.lzs#1016.1 -*- */
function destroy ($1_recursiveCall) {
if (this.__LZdeleted) {
return
}
if (this.sprite) {
this.sprite.predestroy()
}
if (this.addedToParent) {
var $2_svs = this.immediateparent.subviews;
if ($2_svs != null) {
for (var $3_i = $2_svs.length - 1;
$3_i >= 0;
$3_i--) {
if ($2_svs[$3_i] == this) {
$2_svs.splice($3_i, 1);
break
}
}
}
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["destroy"] : this.nextMethod(arguments.callee, "destroy")).apply(this, arguments);
if ($1_recursiveCall == true) {
return
}
if (this.sprite) {
this.sprite.destroy($1_recursiveCall)
}
this.setVisibility("hidden");
if (this.addedToParent) {
if ("__LZoutliewidth" in this.immediateparent && this.immediateparent.__LZoutliewidth == this) {
this.immediateparent.__LZoutliewidth = null
}
if ("__LZoutlieheight" in this.immediateparent && this.immediateparent.__LZoutlieheight == this) {
this.immediateparent.__LZoutlieheight = null
}
if ("onremovesubview" in this.immediateparent) {
if (this.immediateparent.onremovesubview.ready) {
this.immediateparent.onremovesubview.sendEvent(this)
}
}
}
}, deleteView: 
/* -*- file: views/LaszloView.lzs#1058.1 -*- */
function deleteView ($1_recursiveCall) {
Debug.deprecated(this, arguments.callee, this.destroy);
this.destroy()
}, setVisible: 
/* -*- file: views/LaszloView.lzs#1090.1 -*- */
function setVisible ($1_amVisible) {
if (this._visible == $1_amVisible) {
return
}
this._visible = $1_amVisible;
if ($1_amVisible) {
var $2_v = "visible"
} else {
if ($1_amVisible == null) {
Debug.info("%w.%s(%w) is deprecated.  Perhaps you meant %w.%s(%s)?  If not, use %w.%s('collapse').", this, arguments.callee, $1_amVisible, this, arguments.callee, false, this, this.setVisibility);
var $2_v = "collapse"
} else {
var $2_v = "hidden"
}
}
this.visibility = $2_v;
if (this.onvisibility.ready) {
this.onvisibility.sendEvent(this.visibility)
}
this.__LZupdateShown()
}, setVisibility: 
/* -*- file: views/LaszloView.lzs#1125.1 -*- */
function setVisibility ($1_amVisible) {
if (this.visibility == $1_amVisible) {
return
}
this.visibility = $1_amVisible;
if (!($1_amVisible == "visible" || $1_amVisible == "hidden" || $1_amVisible == "collapse")) {
Debug.error("%w.%s called with unknown arg '%s' use 'visible', 'hidden', or 'collapse'.", this, arguments.callee, $1_amVisible)
}
if (this.onvisibility.ready) {
this.onvisibility.sendEvent($1_amVisible)
}
this.__LZupdateShown()
}, __LZupdateShown: 
/* -*- file: views/LaszloView.lzs#1142.1 -*- */
function __LZupdateShown () {
if (this.visibility == "collapse") {
var $1_shown = this.__LZvizO && this.__LZvizDat && this.__LZvizLoad
} else {
var $1_shown = this.visibility == "visible"
}
if ($1_shown != this.visible) {
this.visible = $1_shown;
if (this.sprite) {
this.sprite.setVisible($1_shown)
}
var $2_ip = this.immediateparent;
if ($2_ip && $2_ip.__LZcheckwidth) {
$2_ip.__LZcheckwidthFunction(this)
}
if ($2_ip && $2_ip.__LZcheckheight) {
$2_ip.__LZcheckheightFunction(this)
}
if (this.onvisible.ready) {
this.onvisible.sendEvent($1_shown)
}
}
}, setWidth: 
/* -*- file: views/LaszloView.lzs#1174.1 -*- */
function setWidth ($1_v) {
if (this._width != $1_v) {
this._width = $1_v;
this.sprite.setWidth($1_v);
if ($1_v == null) {
this.hassetwidth = false;
this.__LZcheckwidth = true;
if (this._setrescwidth) {
this.unstretchedwidth = null;
this._xscale = 1
}
this.reevaluateSize("width");
return
}
this.width = $1_v;
if (this.pixellock) {
$1_v = Math.floor($1_v)
}
if (this._setrescwidth) {
var $2_xscale = this.unstretchedwidth == 0 ? 100 : $1_v / this.unstretchedwidth;
this._xscale = $2_xscale
} else {
this.__LZcheckwidth = false
}
this.hassetwidth = true;
if (this.immediateparent.__LZcheckwidth) {
this.immediateparent.__LZcheckwidthFunction(this)
}
if (this.onwidth.ready) {
this.onwidth.sendEvent($1_v)
}
}
}, setHeight: 
/* -*- file: views/LaszloView.lzs#1223.1 -*- */
function setHeight ($1_v) {
if (this._height != $1_v) {
this._height = $1_v;
this.sprite.setHeight($1_v);
if ($1_v == null) {
this.hassetheight = false;
this.__LZcheckheight = true;
if (this._setrescheight) {
this.unstretchedheight = null;
this._yscale = 1
}
this.reevaluateSize("height");
return
}
this.height = $1_v;
if (this.pixellock) {
$1_v = Math.floor($1_v)
}
if (this._setrescheight) {
var $2_yscale = this.unstretchedheight == 0 ? 100 : $1_v / this.unstretchedheight;
this._yscale = $2_yscale
} else {
this.__LZcheckheight = false
}
this.hassetheight = true;
if (this.immediateparent.__LZcheckheight) {
this.immediateparent.__LZcheckheightFunction(this)
}
if (this.onheight.ready) {
this.onheight.sendEvent($1_v)
}
}
}, setOpacity: 
/* -*- file: views/LaszloView.lzs#1268.1 -*- */
function setOpacity ($1_v) {
if (this.capabilities.opacity) {
this.sprite.setOpacity($1_v)
} else {
this.__warnCapability("view.setOpacity()")
}
this.opacity = $1_v;
if (this.onopacity.ready) {
this.onopacity.sendEvent($1_v)
}
var $2_coviz = this.__LZvizO;
var $3_newoviz = $1_v != 0;
if ($2_coviz != $3_newoviz) {
this.__LZvizO = $3_newoviz;
this.__LZupdateShown()
}
}, setX: 
/* -*- file: views/LaszloView.lzs#1290.1 -*- */
function setX ($1_v, $2_force) {
if ($2_force || this._x != $1_v) {
this._x = $1_v;
this.x = $1_v;
if (this.__LZhasoffset) {
if (this.capabilities.rotation) {
$1_v -= this.xoffset * this.__LZrcos - this.yoffset * this.__LZrsin
} else {
$1_v -= this.xoffset
}
}
if (this.pixellock) {
$1_v = Math.floor($1_v)
}
this.sprite.setX($1_v);
if (this.immediateparent.__LZcheckwidth) {
this.immediateparent.__LZcheckwidthFunction(this)
}
if (this.onx.ready) {
this.onx.sendEvent(this.x)
}
}
}, setY: 
/* -*- file: views/LaszloView.lzs#1317.1 -*- */
function setY ($1_v, $2_force) {
if ($2_force || this._y != $1_v) {
this._y = $1_v;
this.y = $1_v;
if (this.__LZhasoffset) {
if (this.capabilities.rotation) {
$1_v -= this.xoffset * this.__LZrsin + this.yoffset * this.__LZrcos
} else {
$1_v -= this.yoffset
}
}
if (this.pixellock) {
$1_v = Math.floor($1_v)
}
this.sprite.setY($1_v);
if (this.immediateparent.__LZcheckheight) {
this.immediateparent.__LZcheckheightFunction(this)
}
if (this.ony.ready) {
this.ony.sendEvent(this.y)
}
}
}, setRotation: 
/* -*- file: views/LaszloView.lzs#1343.1 -*- */
function setRotation ($1_v) {
if (this.capabilities.rotation) {
this.sprite.setRotation($1_v)
} else {
this.__warnCapability("view.setRotation()")
}
this.rotation = $1_v;
var $2_rrad = Math.PI / 180 * this.rotation;
this.__LZrsin = Math.sin($2_rrad);
this.__LZrcos = Math.cos($2_rrad);
if (this.onrotation.ready) {
this.onrotation.sendEvent($1_v)
}
if (this.__LZhasoffset) {
this.setX(this.x, true);
this.setY(this.y, true)
}
if (this.immediateparent.__LZcheckwidth) {
this.immediateparent.__LZcheckwidthFunction(this)
}
if (this.immediateparent.__LZcheckheight) {
this.immediateparent.__LZcheckheightFunction(this)
}
}, setAlign: 
/* -*- file: views/LaszloView.lzs#1373.1 -*- */
function setAlign ($1_align) {
if ($1_align == "left") {
this.releaseConstraint("x");
if (!this.__LZdeleted) {
var $lzsc$1043468374 = this.setters;
if ($lzsc$1043468374 && "x" in $lzsc$1043468374) {
this[$lzsc$1043468374["x"]](0)
} else {
if ($lzsc$1043468374 == null) {
Debug.warn("null setters on", this, "x", 0)
}
this["x"] = 0;
if ("onx" in this) {
if (this["onx"].ready) {
this["onx"].sendEvent(0)
}
}
}
}
} else {
if ($1_align == "center") {
var $2_f = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#1378.17 -*- */
function  () {
var $lzsc$1708677949 = this.immediateparent.width / 2 - this.width / 2;
if (!this.__LZdeleted) {
var $lzsc$1985051652 = this.setters;
if ($lzsc$1985051652 && "x" in $lzsc$1985051652) {
this[$lzsc$1985051652["x"]]($lzsc$1708677949)
} else {
if ($lzsc$1985051652 == null) {
Debug.warn("null setters on", this, "x", $lzsc$1708677949)
}
this["x"] = $lzsc$1708677949;
if ("onx" in this) {
if (this["onx"].ready) {
this["onx"].sendEvent($lzsc$1708677949)
}
}
}
}
}
$lzsc$temp._dbg_name = "views/LaszloView.lzs#1378/17";
return $lzsc$temp
})();
this.setPosConstraint(this.immediateparent, $2_f, "width")
} else {
if ($1_align == "right") {
var $2_f = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#1384.17 -*- */
function  () {
var $lzsc$355602716 = this.immediateparent.width - this.width;
if (!this.__LZdeleted) {
var $lzsc$1425988466 = this.setters;
if ($lzsc$1425988466 && "x" in $lzsc$1425988466) {
this[$lzsc$1425988466["x"]]($lzsc$355602716)
} else {
if ($lzsc$1425988466 == null) {
Debug.warn("null setters on", this, "x", $lzsc$355602716)
}
this["x"] = $lzsc$355602716;
if ("onx" in this) {
if (this["onx"].ready) {
this["onx"].sendEvent($lzsc$355602716)
}
}
}
}
}
$lzsc$temp._dbg_name = "views/LaszloView.lzs#1384/17";
return $lzsc$temp
})();
this.setPosConstraint(this.immediateparent, $2_f, "width")
}
}
}
}, setXOffset: 
/* -*- file: views/LaszloView.lzs#1396.1 -*- */
function setXOffset ($1_o) {
this.__LZhasoffset = $1_o != 0;
this.xoffset = $1_o;
this.setX(this.x, true);
this.setY(this.y, true);
if (this.onxoffset.ready) {
this.onxoffset.sendEvent($1_o)
}
}, setYOffset: 
/* -*- file: views/LaszloView.lzs#1408.1 -*- */
function setYOffset ($1_o) {
this.__LZhasoffset = $1_o != 0;
this.yoffset = $1_o;
this.setX(this.x, true);
this.setY(this.y, true);
if (this.onyoffset.ready) {
this.onyoffset.sendEvent($1_o)
}
}, getBounds: 
/* -*- file: views/LaszloView.lzs#1437.1 -*- */
function getBounds () {
var $1_mtrix = [-this.xoffset, -this.yoffset, this.width - this.xoffset, -this.yoffset, -this.xoffset, this.height - this.yoffset, this.width - this.xoffset, this.height - this.yoffset, this.rotation, this.x, this.y];
var $2_i = $1_mtrix.length - 1;
while ($1_mtrix[$2_i] == this.__LZlastmtrix[$2_i]) {
if ($2_i-- == 0) {
return this.__LZstoredbounds
}
}
var $3_o = {}
for (var $2_i = 0;
$2_i < 8;
$2_i += 2) {
var $4_x = $1_mtrix[$2_i];
var $5_y = $1_mtrix[$2_i + 1];
var $6_cx = $4_x * this.__LZrcos - $5_y * this.__LZrsin;
var $7_cy = $4_x * this.__LZrsin + $5_y * this.__LZrcos;
if ($3_o.xoffset == null || $3_o.xoffset > $6_cx) {
$3_o.xoffset = $6_cx
}
if ($3_o.yoffset == null || $3_o.yoffset > $7_cy) {
$3_o.yoffset = $7_cy
}
if ($3_o.width == null || $3_o.width < $6_cx) {
$3_o.width = $6_cx
}
if ($3_o.height == null || $3_o.height < $7_cy) {
$3_o.height = $7_cy
}
}
$3_o.width -= $3_o.xoffset;
$3_o.height -= $3_o.yoffset;
$3_o.x = this.x + $3_o.xoffset;
$3_o.y = this.y + $3_o.yoffset;
this.__LZstoredbounds = $3_o;
this.__LZlastmtrix = $1_mtrix;
return $3_o
}, setValign: 
/* -*- file: views/LaszloView.lzs#1500.1 -*- */
function setValign ($1_valign) {
if ($1_valign == "top") {
this.releaseConstraint("y");
if (!this.__LZdeleted) {
var $lzsc$750708997 = this.setters;
if ($lzsc$750708997 && "y" in $lzsc$750708997) {
this[$lzsc$750708997["y"]](0)
} else {
if ($lzsc$750708997 == null) {
Debug.warn("null setters on", this, "y", 0)
}
this["y"] = 0;
if ("ony" in this) {
if (this["ony"].ready) {
this["ony"].sendEvent(0)
}
}
}
}
} else {
if ($1_valign == "middle") {
var $2_f = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#1505.17 -*- */
function  () {
var $lzsc$2069769545 = this.immediateparent.height / 2 - this.height / 2;
if (!this.__LZdeleted) {
var $lzsc$1312602778 = this.setters;
if ($lzsc$1312602778 && "y" in $lzsc$1312602778) {
this[$lzsc$1312602778["y"]]($lzsc$2069769545)
} else {
if ($lzsc$1312602778 == null) {
Debug.warn("null setters on", this, "y", $lzsc$2069769545)
}
this["y"] = $lzsc$2069769545;
if ("ony" in this) {
if (this["ony"].ready) {
this["ony"].sendEvent($lzsc$2069769545)
}
}
}
}
}
$lzsc$temp._dbg_name = "views/LaszloView.lzs#1505/17";
return $lzsc$temp
})();
this.setPosConstraint(this.immediateparent, $2_f, "height")
} else {
if ($1_valign == "bottom") {
var $2_f = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#1511.17 -*- */
function  () {
var $lzsc$279118378 = this.immediateparent.height - this.height;
if (!this.__LZdeleted) {
var $lzsc$101572877 = this.setters;
if ($lzsc$101572877 && "y" in $lzsc$101572877) {
this[$lzsc$101572877["y"]]($lzsc$279118378)
} else {
if ($lzsc$101572877 == null) {
Debug.warn("null setters on", this, "y", $lzsc$279118378)
}
this["y"] = $lzsc$279118378;
if ("ony" in this) {
if (this["ony"].ready) {
this["ony"].sendEvent($lzsc$279118378)
}
}
}
}
}
$lzsc$temp._dbg_name = "views/LaszloView.lzs#1511/17";
return $lzsc$temp
})();
this.setPosConstraint(this.immediateparent, $2_f, "height")
}
}
}
}, setPosConstraint: 
/* -*- file: views/LaszloView.lzs#1520.1 -*- */
function setPosConstraint ($1_v, $2_f, $3_widthorheight) {
var $4_d = [$1_v, $3_widthorheight, this, $3_widthorheight];
this.applyConstraint($3_widthorheight == "width" ? "x" : "y", $2_f, $4_d)
}, setColor: 
/* -*- file: views/LaszloView.lzs#1532.1 -*- */
function setColor ($1_c) {
this.sprite.setColor($1_c);
this.fgcolor = $1_c
}, getColor: 
/* -*- file: views/LaszloView.lzs#1543.1 -*- */
function getColor () {
return this.sprite.getColor()
}, setColorTransform: 
/* -*- file: views/LaszloView.lzs#1564.1 -*- */
function setColorTransform ($1_o) {
if (this.capabilities.colortransform) {
this.sprite.setColorTransform($1_o)
} else {
this.__warnCapability("view.setColorTransform()")
}
}, getColorTransform: 
/* -*- file: views/LaszloView.lzs#1588.1 -*- */
function getColorTransform () {
if (this.capabilities.colortransform) {
return this.sprite.getColorTransform()
} else {
this.__warnCapability("view.getColorTransform()")
}
}, getWidth: 
/* -*- file: views/LaszloView.lzs#1602.1 -*- */
function getWidth () {
return this.width
}, getHeight: 
/* -*- file: views/LaszloView.lzs#1611.1 -*- */
function getHeight () {
return this.height
}, __LZcheckSize: 
/* -*- file: views/LaszloView.lzs#1619.1 -*- */
function __LZcheckSize ($1_sview, $2_axis, $3_xory) {
if ($1_sview.addedToParent) {
if ($1_sview.__LZhasoffset || $1_sview.rotation != 0) {
var $4_bobj = $1_sview.getBounds()
} else {
var $4_bobj = $1_sview
}
var $5_ss = $4_bobj[$3_xory] + $4_bobj[$2_axis];
var $6_ts = this["_setresc" + $2_axis] ? this["unstretched" + $2_axis] : this[$2_axis];
if ($5_ss > $6_ts && $1_sview.visible) {
this["__LZoutlie" + $2_axis] = $1_sview;
if ($2_axis == "width") {
this.updateWidth($5_ss)
} else {
this.updateHeight($5_ss)
}
} else {
if (this["__LZoutlie" + $2_axis] == $1_sview && ($5_ss < $6_ts || !$1_sview.visible)) {
this.reevaluateSize($2_axis)
}
}
}
}, __LZcheckwidthFunction: 
/* -*- file: views/LaszloView.lzs#1663.1 -*- */
function __LZcheckwidthFunction ($1_sview) {
this.__LZcheckSize($1_sview, "width", "x")
}, __LZcheckheightFunction: 
/* -*- file: views/LaszloView.lzs#1669.1 -*- */
function __LZcheckheightFunction ($1_sview) {
this.__LZcheckSize($1_sview, "height", "y")
}, measureSize: 
/* -*- file: views/LaszloView.lzs#1679.1 -*- */
function measureSize ($1_axis) {
var $2_w = this["resource" + $1_axis];
for (var $3_i = this.subviews.length - 1;
$3_i >= 0;
$3_i--) {
var $4_sv = this.subviews[$3_i];
var $5_svs = $4_sv[$1_axis == "width" ? "x" : "y"] + $4_sv[$1_axis];
if ($4_sv.visible && $5_svs > $2_w) {
$2_w = $5_svs
}
}
return $2_w
}, measureWidth: 
/* -*- file: views/LaszloView.lzs#1697.1 -*- */
function measureWidth () {
return this.measureSize("width")
}, measureHeight: 
/* -*- file: views/LaszloView.lzs#1706.1 -*- */
function measureHeight () {
return this.measureSize("height")
}, updateSize: 
/* -*- file: views/LaszloView.lzs#1712.1 -*- */
function updateSize ($1_axis, $2_newsize) {
if ($1_axis == "width") {
this.updateWidth($2_newsize)
} else {
this.updateHeight($2_newsize)
}
}, updateWidth: 
/* -*- file: views/LaszloView.lzs#1720.1 -*- */
function updateWidth ($1_newsize) {
if (this._setrescwidth) {
this.unstretchedwidth = $1_newsize;
if (this.hassetwidth) {
var $2_scale = this.width / $1_newsize;
this._xscale = $2_scale
}
if (this.onunstretchedwidth.ready) {
this.onunstretchedwidth.sendEvent($1_newsize)
}
}
if (!this.hassetwidth) {
this.width = $1_newsize;
this.sprite.setWidth($1_newsize);
if (this.onwidth.ready) {
this.onwidth.sendEvent($1_newsize)
}
if (this.immediateparent.__LZcheckwidth) {
this.immediateparent.__LZcheckwidthFunction(this)
}
}
}, updateHeight: 
/* -*- file: views/LaszloView.lzs#1744.1 -*- */
function updateHeight ($1_newsize) {
if (this._setrescheight) {
this.unstretchedheight = $1_newsize;
if (this.hassetheight) {
var $2_scale = this.height / $1_newsize;
this._yscale = $2_scale
}
if (this.onunstretchedheight) {
if (this.onunstretchedheight.ready) {
this.onunstretchedheight.sendEvent($1_newsize)
}
}
}
if (!this.hassetheight) {
this.height = $1_newsize;
this.sprite.setHeight($1_newsize);
if (this.onheight.ready) {
this.onheight.sendEvent($1_newsize)
}
if (this.immediateparent.__LZcheckheight) {
this.immediateparent.__LZcheckheightFunction(this)
}
}
}, reevaluateSize: 
/* -*- file: views/LaszloView.lzs#1769.1 -*- */
function reevaluateSize ($1_ia) {
if ($1_ia == null) {
var $2_axis = "height";
this.reevaluateSize("width")
} else {
var $2_axis = $1_ia
}
if (this["hasset" + $2_axis] && !this["_setresc" + $2_axis]) {
return
}
var $3_o = this["_setresc" + $2_axis] ? this["unstretched" + $2_axis] : this[$2_axis];
var $4_w = this["resource" + $2_axis] || 0;
this["__LZoutlie" + $2_axis] = this;
for (var $5_i = this.subviews.length - 1;
$5_i >= 0;
$5_i--) {
var $6_sv = this.subviews[$5_i];
if ($6_sv.__LZhasoffset || $6_sv.rotation != 0) {
var $7_b = $6_sv.getBounds();
var $8_svs = $7_b[$2_axis == "width" ? "x" : "y"] + $7_b[$2_axis]
} else {
var $8_svs = $6_sv[$2_axis == "width" ? "x" : "y"] + $6_sv[$2_axis]
}
if ($6_sv.visible && $8_svs > $4_w) {
$4_w = $8_svs;
this["__LZoutlie" + $2_axis] = $6_sv
}
}
if ($3_o != $4_w) {
if ($2_axis == "width") {
this.updateWidth($4_w)
} else {
this.updateHeight($4_w)
}
}
}, updateResourceSize: 
/* -*- file: views/LaszloView.lzs#1816.1 -*- */
function updateResourceSize () {
this.sprite.updateResourceSize();
this.reevaluateSize()
}, setAttributeRelative: 
/* -*- file: views/LaszloView.lzs#1831.1 -*- */
function setAttributeRelative ($1_prop, $2_refView) {
var $3_tLink = this.getLinkage($2_refView);
var $4_val = $2_refView[$1_prop];
if ($1_prop == "x" || $1_prop == "y") {
$3_tLink.update($1_prop);
var $lzsc$1722633901 = ($4_val - $3_tLink.offset[$1_prop]) / $3_tLink.scale[$1_prop];
if (!this.__LZdeleted) {
var $lzsc$593113351 = this.setters;
if ($lzsc$593113351 && $1_prop in $lzsc$593113351) {
this[$lzsc$593113351[$1_prop]]($lzsc$1722633901)
} else {
if ($lzsc$593113351 == null) {
Debug.warn("null setters on", this, $1_prop, $lzsc$1722633901)
}
this[$1_prop] = $lzsc$1722633901;
var $lzsc$858434922 = "on" + $1_prop;
if ($lzsc$858434922 in this) {
if (this[$lzsc$858434922].ready) {
this[$lzsc$858434922].sendEvent($lzsc$1722633901)
}
}
}
}
} else {
if ($1_prop == "width" || $1_prop == "height") {
var $5_axis = $1_prop == "width" ? "x" : "y";
$3_tLink.update($5_axis);
var $lzsc$1195025434 = $4_val / $3_tLink.scale[$5_axis];
if (!this.__LZdeleted) {
var $lzsc$1178648316 = this.setters;
if ($lzsc$1178648316 && $1_prop in $lzsc$1178648316) {
this[$lzsc$1178648316[$1_prop]]($lzsc$1195025434)
} else {
if ($lzsc$1178648316 == null) {
Debug.warn("null setters on", this, $1_prop, $lzsc$1195025434)
}
this[$1_prop] = $lzsc$1195025434;
var $lzsc$776441215 = "on" + $1_prop;
if ($lzsc$776441215 in this) {
if (this[$lzsc$776441215].ready) {
this[$lzsc$776441215].sendEvent($lzsc$1195025434)
}
}
}
}
} else {

}
}
}, getAttributeRelative: 
/* -*- file: views/LaszloView.lzs#1905.1 -*- */
function getAttributeRelative ($1_prop, $2_refView) {
var $3_tLink = this.getLinkage($2_refView);
if ($1_prop == "x" || $1_prop == "y") {
$3_tLink.update($1_prop);
return $3_tLink.offset[$1_prop] + $3_tLink.scale[$1_prop] * this.getProp($1_prop)
} else {
if ($1_prop == "width" || $1_prop == "height") {
var $4_axis = $1_prop == "width" ? "x" : "y";
$3_tLink.update($4_axis);
return $3_tLink.scale[$4_axis] * this.getProp($1_prop)
} else {

}
}
}, __LZviewLinks: null, getLinkage: 
/* -*- file: views/LaszloView.lzs#1970.1 -*- */
function getLinkage ($1_refView) {
if (this.__LZviewLinks == null) {
this.__LZviewLinks = new Object()
}
var $2_uid = $1_refView.getUID();
if (this.__LZviewLinks[$2_uid] == null) {
this.__LZviewLinks[$2_uid] = new LzViewLinkage(this, $1_refView)
}
return this.__LZviewLinks[$2_uid]
}, mouseevent: 
/* -*- file: views/LaszloView.lzs#1989.1 -*- */
function mouseevent ($1_eventname) {
if (this[$1_eventname] && this[$1_eventname].ready) {
this[$1_eventname].sendEvent(this)
}
}, getMouse: 
/* -*- file: views/LaszloView.lzs#1998.1 -*- */
function getMouse ($1_xory) {
if (this.__movecounter != LzGlobalMouse.__movecounter) {
this.__movecounter = LzGlobalMouse.__movecounter;
this.__mousecache = this.sprite.getMouse($1_xory)
}
if ($1_xory == null) {
return this.__mousecache
}
return this.__mousecache[$1_xory]
}, containsPt: 
/* -*- file: views/LaszloView.lzs#2017.1 -*- */
function containsPt ($1_x, $2_y) {
return this.getAttribute("height") >= $2_y && $2_y >= 0 && (this.getAttribute("width") >= $1_x && $1_x >= 0)
}, bringToFront: 
/* -*- file: views/LaszloView.lzs#2025.1 -*- */
function bringToFront () {
if (!this.sprite) {
Debug.write("no sprite on ", this);
return
}
this.sprite.bringToFront()
}, getDepthList: 
/* -*- file: views/LaszloView.lzs#2043.1 -*- */
function getDepthList () {
var $1_o = [];
var $2_s = this.subviews;
for (var $3_i = 0;
$3_i < $2_s.length;
$3_i++) {
$1_o[$3_i] = $2_s[$3_i]
}
$1_o.sort(this.__zCompare);
return $1_o
}, __zCompare: 
/* -*- file: views/LaszloView.lzs#2061.1 -*- */
function __zCompare ($1_a, $2_b) {
var $3_az = $1_a.sprite.getZ();
var $4_bz = $2_b.sprite.getZ();
if ($3_az < $4_bz) {
return -1
}
if ($3_az > $4_bz) {
return 1
}
return 0
}, sendBehind: 
/* -*- file: views/LaszloView.lzs#2077.1 -*- */
function sendBehind ($1_v) {
return $1_v ? this.sprite.sendBehind($1_v.sprite) : false
}, sendInFrontOf: 
/* -*- file: views/LaszloView.lzs#2088.1 -*- */
function sendInFrontOf ($1_v) {
return $1_v ? this.sprite.sendInFrontOf($1_v.sprite) : false
}, sendToBack: 
/* -*- file: views/LaszloView.lzs#2096.1 -*- */
function sendToBack () {
this.sprite.sendToBack()
}, setResourceNumber: 
/* -*- file: views/LaszloView.lzs#2111.1 -*- */
function setResourceNumber ($1_n) {
this.__lzcheckframe = $1_n;
this.frame = $1_n;
this.stop($1_n);
if (this.onframe.ready) {
this.onframe.sendEvent($1_n)
}
}, stretchResource: 
/* -*- file: views/LaszloView.lzs#2130.1 -*- */
function stretchResource ($1_xory) {
this.sprite.stretchResource($1_xory);
if ($1_xory == null || $1_xory == "x" || $1_xory == "width" || $1_xory == "both") {
this._setrescwidth = true;
this.__LZcheckwidth = true;
this.reevaluateSize("width")
}
if ($1_xory == null || $1_xory == "y" || $1_xory == "height" || $1_xory == "both") {
this._setrescheight = true;
this.__LZcheckheight = true;
this.reevaluateSize("height")
}
}, setBGColor: 
/* -*- file: views/LaszloView.lzs#2150.1 -*- */
function setBGColor ($1_bgc) {
this.sprite.setBGColor($1_bgc);
if ($1_bgc != null) {
this.bgcolor = Number($1_bgc)
}
if (this.onbgcolor.ready) {
this.onbgcolor.sendEvent($1_bgc)
}
}, setSource: 
/* -*- file: views/LaszloView.lzs#2166.1 -*- */
function setSource ($1_source, $2_cache, $3_headers) {
this.sprite.setSource($1_source, $2_cache, $3_headers)
}, unload: 
/* -*- file: views/LaszloView.lzs#2173.1 -*- */
function unload () {
this.sprite.unload()
}, makeMasked: 
/* -*- file: views/LaszloView.lzs#2183.1 -*- */
function makeMasked () {
if (this.sprite) {
this.sprite.setClip(true)
}
this.masked = true;
this.mask = this
}, removeMask: 
/* -*- file: views/LaszloView.lzs#2190.1 -*- */
function removeMask () {
if (this.sprite) {
this.sprite.setClip(false)
}
this.masked = false;
this.mask = null
}, __LZsetClickRegion: 
/* -*- file: views/LaszloView.lzs#2201.1 -*- */
function __LZsetClickRegion ($1_cr) {
this.sprite.__LZsetClickRegion($1_cr)
}, setClickable: 
/* -*- file: views/LaszloView.lzs#2209.1 -*- */
function setClickable ($1_amclickable) {
this.sprite.setClickable($1_amclickable);
this.clickable = $1_amclickable;
if (this.onclickable.ready) {
this.onclickable.sendEvent($1_amclickable)
}
}, setCursor: 
/* -*- file: views/LaszloView.lzs#2222.1 -*- */
function setCursor ($1_cursor) {
this.sprite.setCursor($1_cursor)
}, setPlay: 
/* -*- file: views/LaszloView.lzs#2231.1 -*- */
function setPlay ($1_b) {
if ($1_b) {
this.play()
} else {
this.stop()
}
}, getMCRef: 
/* -*- file: views/LaszloView.lzs#2243.1 -*- */
function getMCRef () {
return this.sprite.getMCRef()
}, play: 
/* -*- file: views/LaszloView.lzs#2259.1 -*- */
function play ($1_f, $2_rel) {
this.sprite.play($1_f, $2_rel)
}, stop: 
/* -*- file: views/LaszloView.lzs#2269.1 -*- */
function stop ($1_f, $2_rel) {
this.sprite.stop($1_f, $2_rel)
}, setVolume: 
/* -*- file: views/LaszloView.lzs#2277.1 -*- */
function setVolume ($1_v) {
if (this.capabilities.audio) {
this.sprite.setVolume($1_v)
} else {
this.__warnCapability("view.setVolume()")
}
}, getVolume: 
/* -*- file: views/LaszloView.lzs#2289.1 -*- */
function getVolume () {
if (this.capabilities.audio) {
return this.sprite.getVolume()
} else {
this.__warnCapability("view.getVolume()")
}
}, setPan: 
/* -*- file: views/LaszloView.lzs#2301.1 -*- */
function setPan ($1_p) {
if (this.capabilities.audio) {
this.sprite.setPan($1_p)
} else {
this.__warnCapability("view.setPan()")
}
}, getPan: 
/* -*- file: views/LaszloView.lzs#2313.1 -*- */
function getPan () {
if (this.capabilities.audio) {
return this.sprite.getPan()
} else {
this.__warnCapability("view.getPan()")
}
}, getZ: 
/* -*- file: views/LaszloView.lzs#2325.1 -*- */
function getZ () {
return this.sprite.getZ()
}, seek: 
/* -*- file: views/LaszloView.lzs#2336.1 -*- */
function seek ($1_secs) {
var $2_m = this.getMCRef();
if ($2_m.isaudio == true) {
$2_m.seek($1_secs, this.playing)
} else {
var $3_f = $1_secs * canvas.framerate;
if (this.playing) {
this.play($3_f, true)
} else {
this.stop($3_f, true)
}
}
}, getCurrentTime: 
/* -*- file: views/LaszloView.lzs#2355.1 -*- */
function getCurrentTime () {
var $1_m = this.getMCRef();
if ($1_m.isaudio == true) {
return $1_m.getCurrentTime()
} else {
return this.frame / canvas.framerate
}
}, getTotalTime: 
/* -*- file: views/LaszloView.lzs#2372.1 -*- */
function getTotalTime () {
var $1_m = this.getMCRef();
if ($1_m.isaudio == true) {
return $1_m.getTotalTime()
} else {
return this.totalframes / canvas.framerate
}
}, getID3: 
/* -*- file: views/LaszloView.lzs#2386.1 -*- */
function getID3 () {
var $1_m = this.getMCRef();
if ($1_m.isaudio == true) {
return $1_m.getID3()
}
}, getPlayPerc: 
/* -*- file: views/LaszloView.lzs#2399.1 -*- */
function getPlayPerc () {
if (!this.__LZdidPPwarn) {
Debug.info("%w.%s is deprecated.  Use frame/totalframes attributes instead.", this, arguments.callee)
}
this.__LZdidPPwarn = true;
return this.frame / this.totalframes
}, setShowHandCursor: 
/* -*- file: views/LaszloView.lzs#2419.1 -*- */
function setShowHandCursor ($1_s) {
this.sprite.setShowHandCursor($1_s)
}, setAccessible: 
/* -*- file: views/LaszloView.lzs#2426.1 -*- */
function setAccessible ($1_accessible) {
if (this.capabilities.accessibility) {
this.sprite.setAccessible($1_accessible)
} else {
this.__warnCapability("view.setAccessible()")
}
}, setAAActive: 
/* -*- file: views/LaszloView.lzs#2438.1 -*- */
function setAAActive ($1_s, $2_mc) {
if (this.capabilities.accessibility) {
this.sprite.setAAActive($1_s, $2_mc)
} else {
this.__warnCapability("view.setAAActive()")
}
}, setAAName: 
/* -*- file: views/LaszloView.lzs#2451.1 -*- */
function setAAName ($1_s, $2_mc) {
if (this.capabilities.accessibility) {
this.sprite.setAAName($1_s, $2_mc)
} else {
this.__warnCapability("view.setAAName()")
}
}, setAADescription: 
/* -*- file: views/LaszloView.lzs#2463.1 -*- */
function setAADescription ($1_s, $2_mc) {
if (this.capabilities.accessibility) {
this.sprite.setAADescription($1_s, $2_mc)
} else {
this.__warnCapability("view.setAADescription()")
}
}, setAATabIndex: 
/* -*- file: views/LaszloView.lzs#2475.1 -*- */
function setAATabIndex ($1_s, $2_mc) {
if (this.capabilities.accessibility) {
this.sprite.setAATabIndex($1_s, $2_mc)
} else {
this.__warnCapability("view.setAATabIndex()")
}
}, setAASilent: 
/* -*- file: views/LaszloView.lzs#2488.1 -*- */
function setAASilent ($1_s, $2_mc) {
if (this.capabilities.accessibility) {
this.sprite.setAASilent($1_s, $2_mc)
} else {
this.__warnCapability("view.setAASilent()")
}
}, shouldYieldFocus: 
/* -*- file: views/LaszloView.lzs#2500.1 -*- */
function shouldYieldFocus () {
return true
}, blurring: false, setContextMenu: 
/* -*- file: views/LaszloView.lzs#2567.1 -*- */
function setContextMenu ($1_cmenu) {
this.sprite.setContextMenu($1_cmenu)
}, getContextMenu: 
/* -*- file: views/LaszloView.lzs#2577.1 -*- */
function getContextMenu () {
return this.sprite.getContextMenu()
}, __warnCapability: 
/* -*- file: views/LaszloView.lzs#2581.1 -*- */
function __warnCapability ($1_msg) {
Debug.warn("The %s runtime does not support %s", lzr, $1_msg)
}}, {tagname: "view", __LZproxypolicies: [], __LZcheckProxyPolicy: 
/* -*- file: views/LaszloView.lzs#2516.8 -*- */
function __LZcheckProxyPolicy ($1_url) {
var $2_pol = LzView.__LZproxypolicies;
for (var $3_i = $2_pol.length - 1;
$3_i >= 0;
$3_i--) {
var $4_resp = $2_pol[$3_i]($1_url);
if ($4_resp != null) {
return $4_resp
}
}
return canvas.proxied
}, addProxyPolicy: 
/* -*- file: views/LaszloView.lzs#2537.8 -*- */
function addProxyPolicy ($1_f) {
LzView.__LZproxypolicies.push($1_f)
}, removeProxyPolicy: 
/* -*- file: views/LaszloView.lzs#2549.8 -*- */
function removeProxyPolicy ($1_f) {
var $2_pol = LzView.__LZproxypolicies;
for (var $3_i = 0;
$3_i < $2_pol.length;
$3_i++) {
if ($2_pol[$3_i] == $1_f) {
LzView.__LZproxypolicies = $2_pol.splice($3_i, 1);
return true
}
}
return false
}});
(function  () {
var $lzsc$temp = function  () {
with (LzView) {
with (LzView.prototype) {
DeclareEvent(prototype, "onaddsubresource");
DeclareEvent(prototype, "onaddsubview");
DeclareEvent(prototype, "onbgcolor");
DeclareEvent(prototype, "onblur");
DeclareEvent(prototype, "onclick");
DeclareEvent(prototype, "onclickable");
DeclareEvent(prototype, "onfocus");
DeclareEvent(prototype, "onframe");
DeclareEvent(prototype, "onheight");
DeclareEvent(prototype, "onimload");
DeclareEvent(prototype, "onkeyup");
DeclareEvent(prototype, "onkeydown");
DeclareEvent(prototype, "onlastframe");
DeclareEvent(prototype, "onload");
DeclareEvent(prototype, "onloadperc");
DeclareEvent(prototype, "onerror");
DeclareEvent(prototype, "ontimeout");
DeclareEvent(prototype, "onmousedown");
DeclareEvent(prototype, "onmouseout");
DeclareEvent(prototype, "onmouseover");
DeclareEvent(prototype, "onmousetrackover");
DeclareEvent(prototype, "onmousetrackup");
DeclareEvent(prototype, "onmouseup");
DeclareEvent(prototype, "onopacity");
DeclareEvent(prototype, "onplay");
DeclareEvent(prototype, "onremovesubview");
DeclareEvent(prototype, "onresource");
DeclareEvent(prototype, "onresourceheight");
DeclareEvent(prototype, "onresourcewidth");
DeclareEvent(prototype, "onrotation");
DeclareEvent(prototype, "onstop");
DeclareEvent(prototype, "ontotalframes");
DeclareEvent(prototype, "onunstretchedheight");
DeclareEvent(prototype, "onunstretchedwidth");
DeclareEvent(prototype, "onvisible");
DeclareEvent(prototype, "onvisibility");
DeclareEvent(prototype, "onwidth");
DeclareEvent(prototype, "onx");
DeclareEvent(prototype, "onxoffset");
DeclareEvent(prototype, "ony");
DeclareEvent(prototype, "onyoffset");
DeclareEvent(prototype, "ondblclick");
setters.clip = -1;
setters.x = "setX";
setters.y = "setY";
setters.rotation = "setRotation";
setters.opacity = "setOpacity";
setters.alpha = "setOpacity";
setters.visible = "setVisible";
setters.visibility = "setVisibility";
setters.align = "setAlign";
setters.valign = "setValign";
setters.source = "setSource";
setters.bgcolor = "setBGColor";
setters.resource = "setResource";
setters.clickable = "setClickable";
setters.clickregion = "__LZsetClickRegion";
setters.cursor = "setCursor";
setters.fgcolor = "setColor";
setters.font = "setFontName";
setters.stretches = "stretchResource";
setters.play = "setPlay";
setters.showhandcursor = "setShowHandCursor";
setters.layout = "setLayout";
setters.aaactive = "setAAActive";
setters.aaname = "setAAName";
setters.aadescription = "setAADescription";
setters.aatabindex = "setAATabIndex";
setters.aasilent = "setAASilent";
__LZdelayedSetters.layout = "setLayout";
earlySetters.clickregion = 7;
earlySetters.stretches = 8;
setters.xoffset = "setXOffset";
setters.yoffset = "setYOffset";
setters.width = "setWidth";
setters.height = "setHeight";
setters.frame = "setResourceNumber";
LzView.prototype.__LZlastmtrix = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
prototype.getBounds.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#1488.36 -*- */
function  ($1_who, $2_self) {
return [$2_self, "rotation", $2_self, "x", $2_self, "y", $2_self, "width", $2_self, "height"]
}
$lzsc$temp._dbg_name = "prototype.getBounds.dependencies";
return $lzsc$temp
})();
prototype.setAttributeRelative.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#1855.47 -*- */
function  ($1_who, $2_self, $3_prop, $4_refView) {
var $5_tLink = $1_who.getLinkage($4_refView);
var $6_pass = 2;
var $7_d = [];
if ($3_prop == "width") {
var $8_ax = "x"
} else {
if ($3_prop == "height") {
var $8_ax = "y"
} else {
var $8_ax = $3_prop
}
}
var $9_sax = $8_ax == "x" ? "width" : "height";
while ($6_pass) {
if ($6_pass == 2) {
var $10_carr = $5_tLink.uplinkArray
} else {
var $10_carr = $5_tLink.downlinkArray
}
$6_pass--;
for (var $11_i = $10_carr.length - 1;
$11_i >= 0;
$11_i--) {
$7_d.push($10_carr[$11_i], $8_ax);
if ($7_d["_setresc" + $9_sax]) {
$7_d.push([$10_carr[$11_i], $9_sax])
}
}
}
return $7_d
}
$lzsc$temp._dbg_name = "prototype.setAttributeRelative.dependencies";
return $lzsc$temp
})();
prototype.getAttributeRelative.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#1921.47 -*- */
function  ($1_who, $2_self, $3_prop, $4_refView) {
var $5_tLink = $2_self.getLinkage($4_refView);
var $6_pass = 2;
var $7_d = [$2_self, $3_prop];
if ($3_prop == "width") {
var $8_ax = "x"
} else {
if ($3_prop == "height") {
var $8_ax = "y"
} else {
var $8_ax = $3_prop
}
}
var $9_sax = $8_ax == "x" ? "width" : "height";
while ($6_pass) {
if ($6_pass == 2) {
var $10_carr = $5_tLink.uplinkArray
} else {
var $10_carr = $5_tLink.downlinkArray
}
$6_pass--;
for (var $11_i = $10_carr.length - 1;
$11_i >= 0;
$11_i--) {
var $12_ci = $10_carr[$11_i];
$7_d.push($12_ci, $8_ax);
if ($12_ci["_setresc" + $9_sax]) {
$7_d.push($12_ci, $9_sax)
}
}
}
return $7_d
}
$lzsc$temp._dbg_name = "prototype.getAttributeRelative.dependencies";
return $lzsc$temp
})();
prototype.getMouse.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#2007.35 -*- */
function  () {
return [LzIdle, "idle"]
}
$lzsc$temp._dbg_name = "prototype.getMouse.dependencies";
return $lzsc$temp
})();
prototype.getCurrentTime.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#2364.41 -*- */
function  ($1_who, $2_self) {
return [$2_self, "frame"]
}
$lzsc$temp._dbg_name = "prototype.getCurrentTime.dependencies";
return $lzsc$temp
})();
prototype.getTotalTime.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#2394.39 -*- */
function  ($1_who, $2_self) {
return [$2_self, "load"]
}
$lzsc$temp._dbg_name = "prototype.getTotalTime.dependencies";
return $lzsc$temp
})();
prototype.getPlayPerc.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LaszloView.lzs#2410.38 -*- */
function  ($1_who, $2_self) {
return [$2_self, "frame"]
}
$lzsc$temp._dbg_name = "prototype.getPlayPerc.dependencies";
return $lzsc$temp
})()
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzViewLinkage = Class.make("LzViewLinkage", null, {initialize: 
/* -*- file: views/LzViewLinkage.lzs#41.3 -*- */
function initialize ($1_fromView, $2_toView) {
this.scale = new Object();
this.offset = new Object();
if ($1_fromView == $2_toView) {
return
}
this.uplinkArray = [];
var $3_pview = $1_fromView;
do{
$3_pview = $3_pview.immediateparent;
this.uplinkArray.push($3_pview)
} while ($3_pview != $2_toView && $3_pview != canvas);
this.downlinkArray = [];
if ($3_pview == $2_toView) {
return
}
var $3_pview = $2_toView;
do{
$3_pview = $3_pview.immediateparent;
this.downlinkArray.push($3_pview)
} while ($3_pview != canvas);
while (this.uplinkArray.length > 1 && this.downlinkArray[this.downlinkArray.length - 1] == this.uplinkArray[this.uplinkArray.length - 1] && this.downlinkArray[this.downlinkArray.length - 2] == this.uplinkArray[this.uplinkArray.length - 2]) {
this.downlinkArray.pop();
this.uplinkArray.pop()
}
}, update: 
/* -*- file: views/LzViewLinkage.lzs#81.3 -*- */
function update ($1_xory) {
var $2_tscale = 1;
var $3_toffset = 0;
var $4_scale = "_" + $1_xory + "scale";
if (this.uplinkArray) {
var $5_ual = this.uplinkArray.length;
for (var $6_i = 0;
$6_i < $5_ual;
$6_i++) {
var $7_a = this.uplinkArray[$6_i];
$2_tscale *= $7_a[$4_scale];
$3_toffset += $7_a[$1_xory] / $2_tscale
}
}
if (this.downlinkArray) {
for (var $6_i = this.downlinkArray.length - 1;
$6_i >= 0;
$6_i--) {
var $7_a = this.downlinkArray[$6_i];
$3_toffset -= $7_a[$1_xory] / $2_tscale;
$2_tscale /= $7_a[$4_scale]
}
}
this.scale[$1_xory] = $2_tscale;
this.offset[$1_xory] = $3_toffset
}, _dbg_name: 
/* -*- file: views/LzViewLinkage.lzs#112.5 -*- */
function _dbg_name () {
return "[" + this.scale.x + " 0 " + this.offset.x + " 0 " + this.scale.y + " " + this.offset.y + " 0 0 1]"
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzViewLinkage) {
with (LzViewLinkage.prototype) {

}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzText = Class.make("LzText", [LzFormatter, LzView], {scroll: 0, maxscroll: 0, hscroll: 0, maxhscroll: 0, getDefaultWidth: 
/* -*- file: views/LzText.lzs#111.1 -*- */
function getDefaultWidth () {
return 0
}, multiline: null, resize: true, text: null, colorstring: "#000000", init: 
/* -*- file: views/LzText.lzs#159.1 -*- */
function init () {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["init"] : this.nextMethod(arguments.callee, "init")).apply(this, arguments);
if (this.sizeToHeight) {
var $1_h = this.sprite.getTextfieldHeight();
if ($1_h > 0) {
this.setHeight($1_h)
}
}
}, construct: 
/* -*- file: views/LzText.lzs#175.1 -*- */
function construct ($1_parent, $2_args) {
this.password = "password" in $2_args && $2_args.password ? true : false;
this.multiline = "multiline" in $2_args ? $2_args.multiline : null;
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.sizeToHeight = false;
this.fontname = "font" in $2_args ? $2_args.font : this.searchParents("fontname").fontname;
this.fontsize = "fontsize" in $2_args ? $2_args.fontsize : this.searchParents("fontsize").fontsize;
this.fontstyle = "fontstyle" in $2_args ? $2_args.fontstyle : this.searchParents("fontstyle").fontstyle;
$2_args.font = this.fontname;
$2_args.fontsize = this.fontsize;
$2_args.fontstyle = this.fontstyle;
this.sprite.__initTextProperties($2_args);
$2_args.font = LzNode._ignoreAttribute;
$2_args.fontsize = LzNode._ignoreAttribute;
$2_args.fontstyle = LzNode._ignoreAttribute;
this.yscroll = 0;
this.xscroll = 0;
this.resize = "resize" in $2_args ? $2_args.resize == true : this.resize;
this.setResize(this.resize);
this.text = !("text" in $2_args) || $2_args.text == null ? "" : $2_args.text;
this.setMultiline(this.multiline);
this.sprite.setText(this.text);
if ($2_args.width == null) {
if (this.multiline) {
$2_args.width = this.parent.width
} else {
if (this.text != null && this.text != "" && this.text.length > 0) {
$2_args.width = this.getTextWidth()
} else {
$2_args.width = this.getDefaultWidth()
}
}
} else {
this.setResize(false)
}
if (!this.hassetheight) {
this.sizeToHeight = true
} else {
this.setHeight($2_args.height)
}
this.scrollheight = this.height;
if ("maxlength" in $2_args && $2_args.maxlength != null) {
this.setMaxLength($2_args.maxlength)
}
if ("pattern" in $2_args && $2_args.pattern != null) {
this.setPattern($2_args.pattern)
}
if (this.capabilities.advancedfonts) {
if ("antiAliasType" in $2_args && $2_args.antiAliasType != null) {
this.setAntiAliasType($2_args.antiAliasType)
} else {
this.setAntiAliasType("normal")
}
if ("gridFit" in $2_args && $2_args.gridFit != null) {
this.setGridFit($2_args.gridFit)
} else {
this.setGridFit("subpixel")
}
if ("sharpness" in $2_args && $2_args.sharpness != null) {
this.setSharpness($2_args.sharpness)
} else {
this.setSharpness(0)
}
if ("thickness" in $2_args && $2_args.thickness != null) {
this.setThickness($2_args.thickness)
} else {
this.setThickness(0)
}
if (!LzText.prototype.setters.antiAliasType) {
LzText.prototype.setters.antiAliasType = "setAntiAliasType";
LzText.prototype.defaultattrs.antiAliasType = "normal";
LzText.prototype.setters.gridFit = "setGridFit";
LzText.prototype.defaultattrs.gridfit = "subpixel";
LzText.prototype.setters.sharpness = "setSharpness";
LzText.prototype.defaultattrs.sharpness = 0;
LzText.prototype.setters.thickness = "setThickness";
LzText.prototype.defaultattrs.thickness = 0
}
}
}, __makeSprite: 
/* -*- file: views/LzText.lzs#305.1 -*- */
function __makeSprite ($1_args) {
this.sprite = new LzTextSprite(this, $1_args)
}, getMCRef: 
/* -*- file: views/LzText.lzs#315.1 -*- */
function getMCRef () {
return this.sprite.getMCRef()
}, setResize: 
/* -*- file: views/LzText.lzs#375.1 -*- */
function setResize ($1_val) {
this.sprite.setResize($1_val);
this.resize = $1_val
}, setWidth: 
/* -*- file: views/LzText.lzs#384.1 -*- */
function setWidth ($1_val) {
this.sprite.setWidth($1_val);
(arguments.callee.superclass ? arguments.callee.superclass.prototype["setWidth"] : this.nextMethod(arguments.callee, "setWidth")).apply(this, arguments);
if (this.sizeToHeight) {
var $2_h = this.sprite.getTextfieldHeight();
if ($2_h > 0) {
this.setHeight($2_h)
}
}
}, addText: 
/* -*- file: views/LzText.lzs#417.1 -*- */
function addText ($1_t) {
this.setText(this.getText() + $1_t)
}, clearText: 
/* -*- file: views/LzText.lzs#424.1 -*- */
function clearText () {
this.setText("")
}, setMaxLength: 
/* -*- file: views/LzText.lzs#432.1 -*- */
function setMaxLength ($1_val) {
if ($1_val == null || $1_val == "") {
return
}
this.sprite.setMaxLength($1_val);
this.maxlength = $1_val;
if (this.onmaxlength.ready) {
this.onmaxlength.sendEvent($1_val)
}
}, setPattern: 
/* -*- file: views/LzText.lzs#443.1 -*- */
function setPattern ($1_val) {
if ($1_val == null || $1_val == "") {
return
}
this.sprite.setPattern($1_val);
this.pattern = $1_val;
if (this.onpattern.ready) {
this.onpattern.sendEvent($1_val)
}
}, getTextWidth: 
/* -*- file: views/LzText.lzs#453.1 -*- */
function getTextWidth () {
return this.sprite.getTextWidth()
}, getTextHeight: 
/* -*- file: views/LzText.lzs#461.1 -*- */
function getTextHeight () {
return this.sprite.getTextHeight()
}, applyData: 
/* -*- file: views/LzText.lzs#471.1 -*- */
function applyData ($1_d) {
if (null == $1_d) {
this.clearText()
} else {
this.setText($1_d)
}
}, toString: 
/* -*- file: views/LzText.lzs#482.1 -*- */
function toString () {
return "LzText: " + this.getText()
}, _dbg_name: 
/* -*- file: views/LzText.lzs#491.1 -*- */
function _dbg_name () {
var $1_id = (arguments.callee.superclass ? arguments.callee.superclass.prototype["_dbg_name"] : this.nextMethod(arguments.callee, "_dbg_name")).call(this);
if ($1_id != this.toString()) {
return $1_id
} else {
return Debug.stringEscape(this.getText(), true)
}
}, setScroll: 
/* -*- file: views/LzText.lzs#504.1 -*- */
function setScroll ($1_h) {
this.sprite.setScroll($1_h)
}, getScroll: 
/* -*- file: views/LzText.lzs#511.1 -*- */
function getScroll () {
return this.sprite.getScroll()
}, getMaxScroll: 
/* -*- file: views/LzText.lzs#519.1 -*- */
function getMaxScroll () {
return this.sprite.getMaxScroll()
}, getBottomScroll: 
/* -*- file: views/LzText.lzs#527.1 -*- */
function getBottomScroll () {
return this.sprite.getBottomScroll()
}, setXScroll: 
/* -*- file: views/LzText.lzs#537.1 -*- */
function setXScroll ($1_n) {
this.sprite.setXScroll($1_n)
}, setYScroll: 
/* -*- file: views/LzText.lzs#547.1 -*- */
function setYScroll ($1_n) {
this.sprite.setYScroll($1_n)
}, annotateAAimg: 
/* -*- file: views/LzText.lzs#568.1 -*- */
function annotateAAimg ($1_txt) {
if (typeof $1_txt == "undefined") {
return
}
if ($1_txt.length == 0) {
return
}
var $2_ntxt = "";
var $3_start = 0;
var $4_end = 0;
var $5_i;
var $6_IMGSTART = "<img ";
while (true) {
$5_i = $1_txt.indexOf($6_IMGSTART, $3_start);
if ($5_i < 0) {
$2_ntxt += $1_txt.substring($3_start);
break
}
$2_ntxt += $1_txt.substring($3_start, $5_i + $6_IMGSTART.length);
$3_start = $5_i + $6_IMGSTART.length;
var $7_attrs = {}
$4_end = $3_start + this.parseImgAttributes($7_attrs, $1_txt.substring($3_start));
$2_ntxt += $1_txt.substring($3_start, $4_end + 1);
if ($7_attrs["alt"] != null) {
var $8_altval = $7_attrs["alt"];
$2_ntxt += "[image " + $8_altval + "]"
}
$3_start = $4_end + 1
}
return $2_ntxt
}, parseImgAttributes: 
/* -*- file: views/LzText.lzs#608.1 -*- */
function parseImgAttributes ($1_attrs, $2_str) {
var $3_i;
var $4_end = 0;
var $5_ATTNAME = "attrname";
var $6_ATTVAL = "attrval";
var $7_WHITESPACE = "whitespace";
var $8_WHITESPACE2 = "whitespace2";
var $9_mode = $7_WHITESPACE;
var $10_smax = $2_str.length;
var $11_attrname;
var $12_attrval;
var $13_delimiter;
for ($3_i = 0;
$3_i < $10_smax;
$3_i++) {
$4_end = $3_i;
var $14_c = $2_str.charAt($3_i);
if ($14_c == ">") {
break
}
if ($9_mode == $7_WHITESPACE) {
if ($14_c != " ") {
$9_mode = $5_ATTNAME;
$11_attrname = $14_c
}
} else {
if ($9_mode == $5_ATTNAME) {
if ($14_c == " " || $14_c == "=") {
$9_mode = $8_WHITESPACE2
} else {
$11_attrname += $14_c
}
} else {
if ($9_mode == $8_WHITESPACE2) {
if ($14_c == " " || $14_c == "=") {
continue
} else {
$9_mode = $6_ATTVAL;
$13_delimiter = $14_c;
$12_attrval = ""
}
} else {
if ($9_mode == $6_ATTVAL) {
if ($14_c != $13_delimiter) {
$12_attrval += $14_c
} else {
$9_mode = $7_WHITESPACE;
$1_attrs[$11_attrname] = $12_attrval
}
}
}
}
}
}
return $4_end
}, setText: 
/* -*- file: views/LzText.lzs#661.1 -*- */
function setText ($1_t, $2_force) {
$1_t += "";
if ($2_force != true && $1_t == this.text) {
return
}
if (this.visible) {
this.sprite.setVisible(this.visible)
}
this.sprite.setText($1_t);
this.text = $1_t;
if (this.width == 0 || this.resize && this.multiline == false) {
var $3_w = this.getTextWidth();
if ($3_w != this.width) {
this.setWidth($3_w)
}
}
if (this.sizeToHeight) {
var $4_h = this.sprite.getTextfieldHeight();
if ($4_h > 0) {
this.setHeight($4_h)
}
}
if (this.ontext.ready) {
this.ontext.sendEvent($1_t)
}
}, format: 
/* -*- file: views/LzText.lzs#701.1 -*- */
function format ($1_control, $2_args) {
this.setText(this.formatToString.apply(this, arguments))
}, updateMaxLines: 
/* -*- file: views/LzText.lzs#710.1 -*- */
function updateMaxLines () {
var $1_newlin = Math.floor(this.height / (this.font.height - 1));
if ($1_newlin != this.maxlines) {
this.maxlines = $1_newlin
}
}, getText: 
/* -*- file: views/LzText.lzs#744.1 -*- */
function getText () {
return this.text
}, escapeText: 
/* -*- file: views/LzText.lzs#764.1 -*- */
function escapeText ($1_ts) {
var $2_t = $1_ts == null ? this.text : $1_ts;
var $3_i;
for (var $4_ec in LzText.escapeChars) {
while ($2_t.indexOf($4_ec) > -1) {
$3_i = $2_t.indexOf($4_ec);
$2_t = $2_t.substring(0, $3_i) + LzText.escapeChars[$4_ec] + $2_t.substring($3_i + 1)
}
}
return $2_t
}, setSelectable: 
/* -*- file: views/LzText.lzs#786.1 -*- */
function setSelectable ($1_isSel) {
this.sprite.setSelectable($1_isSel)
}, setFontName: 
/* -*- file: views/LzText.lzs#795.1 -*- */
function setFontName ($1_fname) {
this.sprite.setFontName($1_fname);
this.fontname = $1_fname;
this.setText(this.getText(), true)
}, setFontSize: 
/* -*- file: views/LzText.lzs#805.1 -*- */
function setFontSize ($1_fsize) {
this.sprite.setFontSize($1_fsize);
this.fontsize = $1_fsize;
this.setText(this.getText(), true)
}, setFontStyle: 
/* -*- file: views/LzText.lzs#815.1 -*- */
function setFontStyle ($1_fstyle) {
this.sprite.setFontStyle($1_fstyle);
this.fontstyle = $1_fstyle
}, setMultiline: 
/* -*- file: views/LzText.lzs#827.1 -*- */
function setMultiline ($1_ml) {
this.sprite.setMultiline($1_ml);
this.multiline = $1_ml == true
}, setBorder: 
/* -*- file: views/LzText.lzs#837.1 -*- */
function setBorder ($1_onroff) {
this.sprite.setBorder($1_onroff)
}, setWordWrap: 
/* -*- file: views/LzText.lzs#845.1 -*- */
function setWordWrap ($1_wrap) {
this.sprite.setWordWrap($1_wrap)
}, setEmbedFonts: 
/* -*- file: views/LzText.lzs#853.1 -*- */
function setEmbedFonts ($1_onroff) {
this.sprite.setEmbedFonts($1_onroff)
}, setAntiAliasType: 
/* -*- file: views/LzText.lzs#862.1 -*- */
function setAntiAliasType ($1_aliasType) {
if (this.capabilities.advancedfonts) {
if ($1_aliasType == "normal" || $1_aliasType == "advanced") {
this.antiAliasType = $1_aliasType;
this.sprite.setAntiAliasType($1_aliasType)
} else {
Debug.warn("antiAliasType invalid, must be 'normal' or 'advanced', but you said '" + $1_aliasType + "'")
}
} else {
this.__warnCapability("text.setAntiAliasType()")
}
}, getAntiAliasType: 
/* -*- file: views/LzText.lzs#878.1 -*- */
function getAntiAliasType () {
if (this.capabilities.advancedfonts) {
return this.antiAliasType
} else {
this.__warnCapability("text.getAntiAliasType()")
}
}, setGridFit: 
/* -*- file: views/LzText.lzs#891.1 -*- */
function setGridFit ($1_gridFit) {
if (this.capabilities.advancedfonts) {
if ($1_gridFit == "none" || $1_gridFit == "pixel" || $1_gridFit == "subpixel") {
this.gridFit = $1_gridFit;
this.sprite.setGridFit($1_gridFit)
} else {
Debug.warn("gridFit invalid, must be 'none', 'pixel', or 'subpixel' but you said '" + $1_gridFit + "'")
}
} else {
this.__warnCapability("text.setGridFit()")
}
}, getGridFit: 
/* -*- file: views/LzText.lzs#907.1 -*- */
function getGridFit () {
if (this.capabilities.advancedfonts) {
return this.gridFit
} else {
this.__warnCapability("text.getGridFit()")
}
}, setSharpness: 
/* -*- file: views/LzText.lzs#921.1 -*- */
function setSharpness ($1_sharpness) {
if (this.capabilities.advancedfonts) {
if ($1_sharpness >= -400 && $1_sharpness <= 400) {
this.sharpness = $1_sharpness;
this.sprite.setSharpness($1_sharpness)
} else {
Debug.warn("sharpness out of range, must be -400 to 400")
}
} else {
this.__warnCapability("text.setSharpness()")
}
}, getSharpness: 
/* -*- file: views/LzText.lzs#937.1 -*- */
function getSharpness () {
if (this.capabilities.advancedfonts) {
return this.sharpness
} else {
this.__warnCapability("text.getSharpness()")
}
}, setThickness: 
/* -*- file: views/LzText.lzs#951.1 -*- */
function setThickness ($1_thickness) {
if (this.capabilities.advancedfonts) {
if ($1_thickness >= -200 && $1_thickness <= 200) {
this.thickness = $1_thickness;
this.sprite.setThickness($1_thickness)
} else {
Debug.warn("thickness out of range, must be -200 to 200")
}
} else {
this.__warnCapability("text.setThickness()")
}
}, getThickness: 
/* -*- file: views/LzText.lzs#967.1 -*- */
function getThickness () {
if (this.capabilities.advancedfonts) {
return this.thickness
} else {
this.__warnCapability("text.getThickness()")
}
}, setScroll: 
/* -*- file: views/LzText.lzs#990.1 -*- */
function setScroll ($1_s) {
this.sprite.setScroll($1_s)
}, setHScroll: 
/* -*- file: views/LzText.lzs#997.1 -*- */
function setHScroll ($1_s) {
this.sprite.setHScroll($1_s)
}, setSelection: 
/* -*- file: views/LzText.lzs#1012.1 -*- */
function setSelection ($1_start, $2_end) {
this.sprite.setSelection($1_start, $2_end)
}, getSelectionPosition: 
/* -*- file: views/LzText.lzs#1034.1 -*- */
function getSelectionPosition () {
return this.sprite.getSelectionPosition()
}, getSelectionSize: 
/* -*- file: views/LzText.lzs#1051.1 -*- */
function getSelectionSize () {
return this.sprite.getSelectionSize()
}}, {tagname: "text", escapeChars: {">": "&gt;", "<": "&lt;"}});
(function  () {
var $lzsc$temp = function  () {
with (LzText) {
with (LzText.prototype) {
DeclareEvent(prototype, "ontext");
DeclareEvent(prototype, "onmaxlength");
DeclareEvent(prototype, "onpattern");
DeclareEvent(prototype, "onscroll");
DeclareEvent(prototype, "onmaxscroll");
DeclareEvent(prototype, "onhscroll");
DeclareEvent(prototype, "onmaxhscroll");
defaultattrs.pixellock = true;
setters.text = "setText";
setters.resize = "setResize";
setters.multiline = -1;
setters.yscroll = "setYScroll";
setters.xscroll = "setXScroll";
setters.selectable = "setSelectable";
defaultattrs.selectable = false;
setters.maxlength = "setMaxLength";
setters.pattern = "setPattern";
defaultattrs.clip = true;
setters.font = "setFontName";
setters.fontsize = "setFontSize";
setters.fontstyle = "setFontStyle";
prototype.getTextWidth.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LzText.lzs#720.39 -*- */
function  ($1_who, $2_self) {
return [$2_self, "text"]
}
$lzsc$temp._dbg_name = "prototype.getTextWidth.dependencies";
return $lzsc$temp
})();
prototype.getTextHeight.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LzText.lzs#728.40 -*- */
function  ($1_who, $2_self) {
return [$2_self, "text"]
}
$lzsc$temp._dbg_name = "prototype.getTextHeight.dependencies";
return $lzsc$temp
})();
prototype.getMaxScroll.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LzText.lzs#735.39 -*- */
function  ($1_who, $2_self) {
return [$2_self, "maxscroll"]
}
$lzsc$temp._dbg_name = "prototype.getMaxScroll.dependencies";
return $lzsc$temp
})();
prototype.getText.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LzText.lzs#751.34 -*- */
function  ($1_who, $2_self) {
return [$2_self, "text"]
}
$lzsc$temp._dbg_name = "prototype.getText.dependencies";
return $lzsc$temp
})()
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzInputText = Class.make("LzInputText", LzText, {getDefaultWidth: 
/* -*- file: views/LzInputText.lzs#135.1 -*- */
function getDefaultWidth () {
return 100
}, construct: 
/* -*- file: views/LzInputText.lzs#146.1 -*- */
function construct ($1_parent, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this._onfocusDel = new LzDelegate(this, "_gotFocusEvent", this, "onfocus");
this._onblurDel = new LzDelegate(this, "_gotBlurEvent", this, "onblur")
}, __makeSprite: 
/* -*- file: views/LzInputText.lzs#159.1 -*- */
function __makeSprite ($1_args) {
this.sprite = new LzInputTextSprite(this, $1_args)
}, _gotFocusEvent: 
/* -*- file: views/LzInputText.lzs#164.1 -*- */
function _gotFocusEvent () {
this._focused = true;
this.sprite.gotFocus()
}, _gotBlurEvent: 
/* -*- file: views/LzInputText.lzs#170.1 -*- */
function _gotBlurEvent () {
this._focused = false;
this.sprite.gotBlur()
}, inputtextevent: 
/* -*- file: views/LzInputText.lzs#176.1 -*- */
function inputtextevent ($1_eventname, $2_value) {
if ($1_eventname == "onfocus" && this._focused) {
return
}
if ($1_eventname == "onblur" && !this._focused) {
return
}
if ($1_eventname == "onfocus" || $1_eventname == "onmousedown") {
this._focused = true;
if (LzFocus.getFocus() != this) {
var $3_tabdown = LzKeys.isKeyDown("tab");
LzFocus.setFocus(this, $3_tabdown);
return
}
} else {
if ($1_eventname == "onchange") {
if (this.multiline && this.sizeToHeight && this.height != this.sprite.getTextHeight()) {
this.setHeight(this.sprite.getTextfieldHeight())
}
if (this.ontext.ready) {
this.ontext.sendEvent($2_value)
}
return
} else {
if ($1_eventname == "onblur") {
this._focused = false
}
}
}
if (this[$1_eventname].ready) {
this[$1_eventname].sendEvent($2_value)
}
}, focusable: true, updateData: 
/* -*- file: views/LzInputText.lzs#211.1 -*- */
function updateData () {
return this.sprite.text
}, setEnabled: 
/* -*- file: views/LzInputText.lzs#220.1 -*- */
function setEnabled ($1_enabled) {
if (!this.__LZdeleted) {
var $lzsc$2104535181 = this.setters;
if ($lzsc$2104535181 && "focusable" in $lzsc$2104535181) {
this[$lzsc$2104535181["focusable"]]($1_enabled)
} else {
if ($lzsc$2104535181 == null) {
Debug.warn("null setters on", this, "focusable", $1_enabled)
}
this["focusable"] = $1_enabled;
if ("onfocusable" in this) {
if (this["onfocusable"].ready) {
this["onfocusable"].sendEvent($1_enabled)
}
}
}
}
this.enabled = $1_enabled;
this.sprite.setEnabled($1_enabled);
if (this.onenabled.ready) {
this.onenabled.sendEvent($1_enabled)
}
}, setHTML: 
/* -*- file: views/LzInputText.lzs#235.1 -*- */
function setHTML ($1_htmlp) {
if (this.capabilities["htmlinputtext"]) {
this.sprite.setHTML($1_htmlp)
} else {
this.__warnCapability("inputtext.setHTML()")
}
}, setText: 
/* -*- file: views/LzInputText.lzs#247.1 -*- */
function setText ($1_t) {
$1_t += "";
this.sprite.setText($1_t);
this.text = $1_t;
if (this.height < 9 || this.sizeToHeight) {
this.height = this.sprite.getTextfieldHeight();
if (this.onheight.ready) {
this.onheight.sendEvent()
}
if (this.height > 0) {
this.setHeight(this.height)
}
}
if (this.ontext.ready) {
this.ontext.sendEvent($1_t)
}
}, getText: 
/* -*- file: views/LzInputText.lzs#266.1 -*- */
function getText () {
return this.sprite.getText()
}}, {tagname: "inputtext"});
(function  () {
var $lzsc$temp = function  () {
with (LzInputText) {
with (LzInputText.prototype) {
DeclareEvent(prototype, "onenabled");
DeclareEvent(prototype, "onselect");
DeclareEvent(prototype, "ontext");
defaultattrs.selectable = true;
defaultattrs.enabled = true;
setters.enabled = "setEnabled";
prototype.getText.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: views/LzInputText.lzs#270.34 -*- */
function  ($1_who, $2_self) {
return [$2_self, "text"]
}
$lzsc$temp._dbg_name = "prototype.getText.dependencies";
return $lzsc$temp
})()
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzCanvas = Class.make("LzCanvas", LzView, {initialize: 
/* -*- file: views/LaszloCanvas.lzs#64.1 -*- */
function initialize ($1_args) {
this.hasdatapath = true;
this.datapath = {}
this.immediateparent = this;
this.__LZmovieClipRef = _root;
this.__LZsvdepth = 1;
this.__LZrightmenuclip = null;
if ("accessible" in $1_args) {
this.sprite.setAccessible($1_args.accessible)
}
this.mask = null;
this.viewLevel = 0;
this.resourcetable = {}
this.totalnodes = 0;
this.creatednodes = 0;
this.percentcreated = 0;
this.framerate = 30;
if (typeof $1_args.proxied == "undefined" || $1_args.proxied == null) {
var $2_lzproxied_query_arg = $1_args.__LZproxied == "true";
if (LzBrowser.getInitArg("lzproxied") != null) {
$2_lzproxied_query_arg = LzBrowser.getInitArg("lzproxied") == "true"
}
this.proxied = $2_lzproxied_query_arg
} else {
this.proxied = $1_args.proxied == true
}
delete $1_args.proxied;
this.sprite = new LzSprite(this, true);
this.__canvaswidthratio = null;
this.width = Number($1_args.width);
if (isNaN(this.width)) {
if ($1_args.width.charAt($1_args.width.length - 1) == "%") {
var $3_percent = Number($1_args.width.substr(0, $1_args.width.length - 1));
this.__canvaswidthratio = $3_percent / 100;
if (this.capabilities.scalecanvastopercentage != true) {
this.__canvaswidthratio = 1
}
} else {
Debug.warn("ignored bad value %#w for canvas width", $1_args.width);
this.width = 400
}
}
delete $1_args.width;
this.__canvasheightratio = null;
this.height = Number($1_args.height);
if (isNaN(this.height)) {
if ($1_args.height.charAt($1_args.height.length - 1) == "%") {
var $3_percent = Number($1_args.height.substr(0, $1_args.height.length - 1));
this.__canvasheightratio = $3_percent / 100;
if (this.capabilities.scalecanvastopercentage != true) {
this.__canvasheightratio = 1
}
} else {
Debug.warn("ignored bad value %#w for canvas height", $1_args.height);
this.height = 400
}
}
delete $1_args.height;
if (this.__canvasheightratio != null || this.__canvaswidthratio != null) {
LzScreenKernel.setCallback(this, "__windowResize")
}
this.bgcolor = $1_args.bgcolor;
delete $1_args.bgcolor;
this.lpsversion = $1_args.lpsversion + "." + this.__LZlfcversion;
delete $1_args.lpsversion;
this.__LZapplyArgs($1_args);
if (!("version" in this && this.version)) {
this.version = this.lpsversion
}
if (this.compareVersion(this.version) == -1) {
Debug.warn("Enabling old ondata behavior because app uses " + "version %w.", this.version);
this.__LZoldOnData = true
}
this.isinited = false;
this._lzinitialsubviews = [];
this.datasets = {}
global.canvas = this;
this.parent = this;
this.makeMasked();
this.__LZmouseupDel = new LzDelegate(this, "__LZmouseup", LzGlobalMouse, "onmouseup");
this.__LZmousedownDel = new LzDelegate(this, "__LZmousedown", LzGlobalMouse, "onmousedown");
this.__LZmousemoveDel = new LzDelegate(this, "__LZmousemove", LzGlobalMouse, "onmousemove");
LzPlatform.initCanvas(this)
}, __LZmouseup: 
/* -*- file: views/LaszloCanvas.lzs#197.1 -*- */
function __LZmouseup () {
if (this.onmouseup.ready) {
this.onmouseup.sendEvent()
}
}, __LZmousemove: 
/* -*- file: views/LaszloCanvas.lzs#205.1 -*- */
function __LZmousemove () {
if (this.onmousemove.ready) {
this.onmousemove.sendEvent()
}
}, __LZmousedown: 
/* -*- file: views/LaszloCanvas.lzs#213.1 -*- */
function __LZmousedown () {
if (this.onmousedown.ready) {
this.onmousedown.sendEvent()
}
}, __makeSprite: 
/* -*- file: views/LaszloCanvas.lzs#221.1 -*- */
function __makeSprite ($1_args) {
this.sprite = new LzSprite(this, true, $1_args);
Debug.write("making canvas sprite", this.sprite)
}, initdelay: 0, lpsversion: null, lpsrelease: null, version: null, __LZlfcversion: "0", nodeLevel: 0, proxied: true, dataloadtimeout: 30000, medialoadtimeout: 30000, percentcreated: null, datasets: null, proxied: null, compareVersion: 
/* -*- file: views/LaszloCanvas.lzs#318.1 -*- */
function compareVersion ($1_ver, $2_over) {
if ($2_over == null) {
$2_over = this.lpsversion
}
if ($1_ver == $2_over) {
return 0
}
var $3_ver1 = $1_ver.split(".");
var $4_ver2 = $2_over.split(".");
var $5_i = 0;
while ($5_i < $3_ver1.length || $5_i < $4_ver2.length) {
var $6_my = Number($3_ver1[$5_i]) || 0;
var $7_oth = Number($4_ver2[$5_i++]) || 0;
if ($6_my < $7_oth) {
return -1
} else {
if ($6_my > $7_oth) {
return 1
}
}
}
return 0
}, setResource: 
/* -*- file: views/LaszloCanvas.lzs#347.1 -*- */
function setResource () {
Object.error("You can't set a resource for the canvas.")
}, toString: 
/* -*- file: views/LaszloCanvas.lzs#355.1 -*- */
function toString () {
return "This is the canvas"
}, initDone: 
/* -*- file: views/LaszloCanvas.lzs#373.1 -*- */
function initDone () {
var $1_sva = new Array();
for (var $2_i = 0;
$2_i < this._lzinitialsubviews.length;
$2_i++) {
if ("initimmediate" in this._lzinitialsubviews[$2_i].attrs && this._lzinitialsubviews[$2_i].attrs.initimmediate) {
$1_sva.push(this._lzinitialsubviews[$2_i])
}
}
for (var $2_i = 0;
$2_i < this._lzinitialsubviews.length;
$2_i++) {
if (!("initimmediate" in this._lzinitialsubviews[$2_i].attrs && this._lzinitialsubviews[$2_i].attrs.initimmediate)) {
$1_sva.push(this._lzinitialsubviews[$2_i])
}
}
this._lzinitialsubviews = [];
LzInstantiator.requestInstantiation(this, $1_sva)
}, init: 
/* -*- file: views/LaszloCanvas.lzs#406.1 -*- */
function init () {

}, __LZinstantiationDone: 
/* -*- file: views/LaszloCanvas.lzs#412.1 -*- */
function __LZinstantiationDone () {
this.percentcreated = 1;
this.updatePercentCreated = null;
if (this.onpercentcreated.ready) {
this.onpercentcreated.sendEvent(this.percentcreated)
}
if (this.initdelay > 0) {
LzInstantiator.halt();
this.initokdel = new LzDelegate(this, "okToInit");
LzTimer.addTimer(this.initokdel, this.initdelay)
} else {
this.okToInit()
}
}, okToInit: 
/* -*- file: views/LaszloCanvas.lzs#429.1 -*- */
function okToInit () {
LzInstantiator.resume();
this.__LZcallInit()
}, updatePercentCreated: 
/* -*- file: views/LaszloCanvas.lzs#437.1 -*- */
function updatePercentCreated () {
this.percentcreated = Math.max(this.percentcreated, this.creatednodes / this.totalnodes);
this.percentcreated = Math.min(0.99, this.percentcreated);
if (this.onpercentcreated.ready) {
this.onpercentcreated.sendEvent(this.percentcreated)
}
}, initiatorAddNode: 
/* -*- file: views/LaszloCanvas.lzs#447.1 -*- */
function initiatorAddNode ($1_e, $2_n) {
this.totalnodes += $2_n;
this._lzinitialsubviews.push($1_e)
}, __LZcallInit: 
/* -*- file: views/LaszloCanvas.lzs#456.1 -*- */
function __LZcallInit ($1_an) {
if (this.isinited) {
return
}
this.isinited = true;
this.__LZresolveReferences();
var $2_sl = this.subnodes;
if ($2_sl) {
var $3_i = 0;
var $4_l = $2_sl.length;
while ($3_i < $4_l) {
var $5_s = $2_sl[$3_i++];
var $6_t = $2_sl[$3_i];
if ($5_s.isinited || $5_s.__LZlateinit) {
continue
}
$5_s.__LZcallInit();
if ($6_t != $2_sl[$3_i]) {
while ($3_i > 0) {
if ($6_t == $2_sl[--$3_i]) {
break
}
}
}
}
}
this.init();
this.sprite.init(true);
if (this.oninit.ready) {
this.oninit.sendEvent(this)
}
if (this.datapath && this.datapath.__LZApplyDataOnInit) {
this.datapath.__LZApplyDataOnInit()
}
}, setWidth: 
/* -*- file: views/LaszloCanvas.lzs#508.1 -*- */
function setWidth () {
Debug.error("setWidth cannot be called on the canvas.")
}, isProxied: 
/* -*- file: views/LaszloCanvas.lzs#517.1 -*- */
function isProxied () {
return this.proxied
}, setX: 
/* -*- file: views/LaszloCanvas.lzs#524.1 -*- */
function setX () {
Debug.error("setX cannot be called on the canvas.")
}, setHeight: 
/* -*- file: views/LaszloCanvas.lzs#533.1 -*- */
function setHeight () {
Debug.error("setHeight cannot be called on the canvas.")
}, setY: 
/* -*- file: views/LaszloCanvas.lzs#542.1 -*- */
function setY () {
Debug.error("setY cannot be called on the canvas.")
}, setDefaultContextMenu: 
/* -*- file: views/LaszloCanvas.lzs#553.1 -*- */
function setDefaultContextMenu ($1_cmenu) {
this.setContextMenu($1_cmenu);
this.sprite.setDefaultContextMenu($1_cmenu)
}, __windowResize: 
/* -*- file: views/LaszloCanvas.lzs#577.1 -*- */
function __windowResize ($1_size) {
if (this.__canvaswidthratio != null) {
this.width = Math.floor($1_size.width * this.__canvaswidthratio);
if (this.onwidth.ready) {
this.onwidth.sendEvent(this.width)
}
this.sprite.setWidth(this.width)
}
if (this.__canvasheightratio != null) {
this.height = Math.floor($1_size.height * this.__canvasheightratio);
if (this.onheight.ready) {
this.onheight.sendEvent(this.height)
}
this.sprite.setHeight(this.height)
}
}}, {tagname: "canvas", versionInfoString: 
/* -*- file: views/LaszloCanvas.lzs#562.8 -*- */
function versionInfoString () {
return "URL: " + LzBrowser.getLoadURL() + "\n" + "Version: " + canvas.lpsversion + "\n" + "Release: " + canvas.lpsrelease + "\n" + "Build: " + canvas.lpsbuild + "\n" + "Date: " + canvas.lpsbuilddate + "\n" + "Target: " + canvas.runtime + "\n" + "Runtime:" + LzBrowser.getVersion() + "\n"
}});
(function  () {
var $lzsc$temp = function  () {
with (LzCanvas) {
with (LzCanvas.prototype) {
DeclareEvent(prototype, "onpercentcreated");
DeclareEvent(prototype, "onmousedown");
DeclareEvent(prototype, "onmouseup");
DeclareEvent(prototype, "onmousemove");
prototype.__LZcheckwidth = null;
prototype.__LZcheckheight = null;
prototype.hassetwidth = true;
prototype.hassetheight = true
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzPlatform = new Object();
LzPlatform.initCanvas = (function  () {
var $lzsc$temp = 
/* -*- file: views/platform/LzPlatform.lzs#19.25 -*- */
function  ($1_canvasobj) {
LzPlatform.buildDefaultMenu($1_canvasobj)
}
$lzsc$temp._dbg_name = "LzPlatform.initCanvas";
return $lzsc$temp
})();
LzPlatform.__LZdefaultMenuItemHandler = (function  () {
var $lzsc$temp = 
/* -*- file: views/platform/LzPlatform.lzs#27.41 -*- */
function  ($1_obj, $2_item) {
LzBrowser.loadURL("http://www.openlaszlo.org", "lz_about")
}
$lzsc$temp._dbg_name = "LzPlatform.__LZdefaultMenuItemHandler";
return $lzsc$temp
})();
LzPlatform.__LZviewSourceMenuItemHandler = (function  () {
var $lzsc$temp = 
/* -*- file: views/platform/LzPlatform.lzs#36.44 -*- */
function  ($1_obj, $2_item) {
var $3_url = LzBrowser.getBaseURL();
if (canvas.proxied) {
$3_url = $3_url.toString() + "?lzt=source"
} else {
$3_url = $3_url.toString() + ".zip"
}
LzBrowser.loadURL($3_url, "lz_source")
}
$lzsc$temp._dbg_name = "LzPlatform.__LZviewSourceMenuItemHandler";
return $lzsc$temp
})();
LzPlatform.buildDefaultMenu = (function  () {
var $lzsc$temp = 
/* -*- file: views/platform/LzPlatform.lzs#53.31 -*- */
function  ($1_canvas) {
$1_canvas.__LZDefaultCanvasMenu = new LzContextMenu();
$1_canvas.__LZdefaultMenuItem = new LzContextMenuItem("About OpenLaszlo...", new LzDelegate(LzPlatform, "__LZdefaultMenuItemHandler"));
$1_canvas.__LZviewSourceMenuItem = new LzContextMenuItem("View Source", new LzDelegate(LzPlatform, "__LZviewSourceMenuItemHandler"));
$1_canvas.__LZDefaultCanvasMenu.hideBuiltInItems();
$1_canvas.__LZDefaultCanvasMenu.addItem($1_canvas.__LZdefaultMenuItem);
if ($1_canvas.proxied) {
$1_canvas.__LZDefaultCanvasMenu.addItem($1_canvas.__LZviewSourceMenuItem)
}
if ($1_canvas.__LZDefaultCanvasMenu) {
$1_canvas.setDefaultContextMenu($1_canvas.__LZDefaultCanvasMenu)
}
}
$lzsc$temp._dbg_name = "LzPlatform.buildDefaultMenu";
return $lzsc$temp
})();
var LzScript = Class.make("LzScript", LzNode, {initialize: 
/* -*- file: views/LzScript.lzs#71.1 -*- */
function initialize ($1_parent, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
$2_args.script()
}}, {tagname: "script"});
var LzAnimatorGroup = Class.make("LzAnimatorGroup", LzNode, {attribute: null, start: true, from: null, to: null, duration: null, indirect: false, relative: false, motion: "easeboth", repeat: null, paused: false, started: null, target: null, process: "sequential", target: null, paused: null, animatorProps: {attribute: true, from: true, duration: true, to: true, relative: true, target: true}, construct: 
/* -*- file: controllers/LzAnimatorGroup.lzs#214.1 -*- */
function construct ($1_parent, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
if (this.immediateparent instanceof LzAnimatorGroup) {
for (var $3_k in this.animatorProps) {
if ($2_args[$3_k] == null) {
$2_args[$3_k] = this.immediateparent[$3_k]
}
}
if (this.immediateparent.animators == null) {
this.immediateparent.animators = [this]
} else {
this.immediateparent.animators.push(this)
}
$2_args.start = LzNode._ignoreAttribute
} else {
this.target = this.immediateparent
}
if (!this.updateDel) {
this.updateDel = new LzDelegate(this, "update")
}
}, init: 
/* -*- file: controllers/LzAnimatorGroup.lzs#241.1 -*- */
function init () {
if (!this.target) {
this.target = this.immediateparent
}
if (this.started) {
this.doStart()
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["init"] : this.nextMethod(arguments.callee, "init")).apply(this, arguments)
}, setTarget: 
/* -*- file: controllers/LzAnimatorGroup.lzs#250.1 -*- */
function setTarget ($1_new_target) {
this.target = $1_new_target;
var $2_nodes = this.subnodes;
if ($2_nodes) {
for (var $3_i = 0;
$3_i < $2_nodes.length;
$3_i++) {
if ($2_nodes[$3_i] instanceof LzAnimatorGroup) {
$2_nodes[$3_i].setTarget($1_new_target)
}
}
}
if (this.ontarget.ready) {
this.ontarget.sendEvent($1_new_target)
}
}, setStart: 
/* -*- file: controllers/LzAnimatorGroup.lzs#267.1 -*- */
function setStart ($1_start) {
this.started = $1_start;
if (this.onstarted.ready) {
this.onstarted.sendEvent($1_start)
}
if (!this.isinited) {
return
}
if ($1_start) {
this.doStart()
} else {
this.stop()
}
}, doStart: 
/* -*- file: controllers/LzAnimatorGroup.lzs#286.1 -*- */
function doStart () {
if (this.isactive) {
return false
}
if (this.onstart.ready) {
this.onstart.sendEvent(new Date().getTime())
}
this.isactive = true;
this.crepeat = this.repeat;
this.prepareStart();
this.updateDel.register(LzIdle, "onidle");
return true
}, start: 
/* -*- file: controllers/LzAnimatorGroup.lzs#309.1 -*- */
function start () {
Debug.deprecated(this, arguments.callee, this.doStart);
this.doStart()
}, prepareStart: 
/* -*- file: controllers/LzAnimatorGroup.lzs#319.1 -*- */
function prepareStart () {
for (var $1_i = this.animators.length - 1;
$1_i >= 0;
$1_i--) {
this.animators[$1_i].notstarted = true
}
this.actAnim = this.animators.concat()
}, resetAnimator: 
/* -*- file: controllers/LzAnimatorGroup.lzs#330.1 -*- */
function resetAnimator () {
this.actAnim = this.animators.concat();
for (var $1_i = this.animators.length - 1;
$1_i >= 0;
$1_i--) {
this.animators[$1_i].needsrestart = true
}
}, update: 
/* -*- file: controllers/LzAnimatorGroup.lzs#354.1 -*- */
function update ($1_time) {
var $2_animend = this.actAnim.length - 1;
if ($2_animend > 0 && this.process == "sequential") {
$2_animend = 0
}
if (this.paused) {
return
}
for (var $3_i = $2_animend;
$3_i >= 0;
$3_i--) {
var $4_a = this.actAnim[$3_i];
if ($4_a.notstarted) {
$4_a.isactive = true;
$4_a.prepareStart();
$4_a.notstarted = false
} else {
if ($4_a.needsrestart) {
$4_a.resetAnimator();
$4_a.needsrestart = false
}
}
if ($4_a.update($1_time)) {
this.actAnim.splice($3_i, 1)
}
}
if (!this.actAnim.length) {
return this.checkRepeat()
}
return false
}, pause: 
/* -*- file: controllers/LzAnimatorGroup.lzs#394.1 -*- */
function pause ($1_dop) {
if ($1_dop == null) {
$1_dop = !this.paused
}
if (this.paused && !$1_dop) {
this.__LZaddToStartTime(new Date().getTime() - this.__LZpauseTime)
} else {
if (!this.paused && $1_dop) {
this.__LZpauseTime = new Date().getTime()
}
}
this.paused = $1_dop;
if (this.onpaused.ready) {
this.onpaused.sendEvent($1_dop)
}
}, __LZaddToStartTime: 
/* -*- file: controllers/LzAnimatorGroup.lzs#412.1 -*- */
function __LZaddToStartTime ($1_ptime) {
this.startTime += $1_ptime;
for (var $2_i = 0;
$2_i < this.actAnim.length;
$2_i++) {
this.actAnim[$2_i].__LZaddToStartTime($1_ptime)
}
}, stop: 
/* -*- file: controllers/LzAnimatorGroup.lzs#423.1 -*- */
function stop () {
if (this.actAnim) {
var $1_animend = this.actAnim.length - 1;
if ($1_animend > 0 && this.process == "sequential") {
$1_animend = 0
}
for (var $2_i = $1_animend;
$2_i >= 0;
$2_i--) {
this.actAnim[$2_i].stop()
}
}
this.__LZhalt()
}, __LZfinalizeAnim: 
/* -*- file: controllers/LzAnimatorGroup.lzs#442.1 -*- */
function __LZfinalizeAnim () {
this.__LZhalt()
}, __LZhalt: 
/* -*- file: controllers/LzAnimatorGroup.lzs#449.1 -*- */
function __LZhalt () {
this.isactive = false;
this.updateDel.unregisterAll();
if (this.onfinish.ready) {
this.onfinish.sendEvent(this)
}
if (this.onstop.ready) {
this.onstop.sendEvent(new Date().getTime())
}
}, checkRepeat: 
/* -*- file: controllers/LzAnimatorGroup.lzs#460.1 -*- */
function checkRepeat () {
if (this.crepeat == null || this.crepeat == 1) {
this.__LZfinalizeAnim();
return true
}
if (this.crepeat > 0) {
this.crepeat--;
if (this.onrepeat.ready) {
this.onrepeat.sendEvent(new Date().getTime())
}
}
this.resetAnimator()
}, destroy: 
/* -*- file: controllers/LzAnimatorGroup.lzs#477.1 -*- */
function destroy () {
this.stop();
this.updateDel.unregisterAll();
this.animators = null;
this.actAnim = null;
if (this.parent.animators && this.parent.animators.length) {
for (var $1_i = 0;
$1_i < this.parent.animators.length;
$1_i++) {
if (this.parent.animators[$1_i] == this) {
this.parent.animators.splice($1_i, 1);
break
}
}
for (var $1_i = 0;
$1_i < this.parent.actAnim.length;
$1_i++) {
if (this.parent.actAnim[$1_i] == this) {
this.parent.actAnim.splice($1_i, 1);
break
}
}
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["destroy"] : this.nextMethod(arguments.callee, "destroy")).apply(this, arguments)
}, toString: 
/* -*- file: controllers/LzAnimatorGroup.lzs#505.1 -*- */
function toString () {
return "GroupAnimator length = " + this.animators.length
}}, {tagname: "animatorgroup"});
(function  () {
var $lzsc$temp = function  () {
with (LzAnimatorGroup) {
with (LzAnimatorGroup.prototype) {
setters.start = "setStart";
defaultattrs.start = true;
setters.target = "setTarget";
setters.paused = "pause";
DeclareEvent(prototype, "ontarget");
DeclareEvent(prototype, "onduration");
DeclareEvent(prototype, "onstarted");
DeclareEvent(prototype, "onstart");
DeclareEvent(prototype, "onpaused");
DeclareEvent(prototype, "onfinish");
DeclareEvent(prototype, "onstop");
DeclareEvent(prototype, "onrepeat");
defaultattrs.ignoreplacement = true
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzAnimator = Class.make("LzAnimator", LzAnimatorGroup, {beginPoleDelta: 0.25, endPoleDelta: 0.25, attribute: null, construct: 
/* -*- file: controllers/LaszloAnimation.lzs#77.1 -*- */
function construct ($1_parent, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.primary_K = 1
}, setMotion: 
/* -*- file: controllers/LaszloAnimation.lzs#88.1 -*- */
function setMotion ($1_eparam) {
if ($1_eparam == "linear") {
this.calcNextValue = this.calcNextValueLinear
} else {
delete this.calcNextValue;
if ($1_eparam == "easeout") {
this.beginPoleDelta = 100
} else {
if ($1_eparam == "easein") {
this.endPoleDelta = 15
}
}
}
}, setTo: 
/* -*- file: controllers/LaszloAnimation.lzs#106.1 -*- */
function setTo ($1_eparam) {
this.origto = Number($1_eparam)
}, calcControlValues: 
/* -*- file: controllers/LaszloAnimation.lzs#117.1 -*- */
function calcControlValues ($1_cval) {
this.currentValue = $1_cval || 0;
var $2_dir = this.indirect ? -1 : 1;
if (this.currentValue < this.to) {
this.beginPole = this.currentValue - $2_dir * this.beginPoleDelta;
this.endPole = this.to + $2_dir * this.endPoleDelta
} else {
this.beginPole = this.currentValue + $2_dir * this.beginPoleDelta;
this.endPole = this.to - $2_dir * this.endPoleDelta
}
this.primary_K = 1;
var $3_kN = 1 * (this.beginPole - this.to) * (this.currentValue - this.endPole);
var $4_kD = 1 * (this.beginPole - this.currentValue) * (this.to - this.endPole);
if ($4_kD != 0) {
this.primary_K = Math.abs($3_kN / $4_kD)
}
}, doStart: 
/* -*- file: controllers/LaszloAnimation.lzs#163.1 -*- */
function doStart () {
if (this.isactive) {
return
}
this.isactive = true;
this.prepareStart();
this.updateDel.register(LzIdle, "onidle")
}, prepareStart: 
/* -*- file: controllers/LaszloAnimation.lzs#177.1 -*- */
function prepareStart () {
this.crepeat = this.repeat;
if (this.from != null) {
var $lzsc$1804830789 = this.target;
var $lzsc$743131184 = this.attribute;
var $lzsc$892564543 = Number(this.from);
if (!$lzsc$1804830789.__LZdeleted) {
var $lzsc$1238750329 = $lzsc$1804830789.setters;
if ($lzsc$1238750329 && $lzsc$743131184 in $lzsc$1238750329) {
$lzsc$1804830789[$lzsc$1238750329[$lzsc$743131184]]($lzsc$892564543)
} else {
if ($lzsc$1238750329 == null) {
Debug.warn("null setters on", $lzsc$1804830789, $lzsc$743131184, $lzsc$892564543)
}
$lzsc$1804830789[$lzsc$743131184] = $lzsc$892564543;
var $lzsc$604986472 = "on" + $lzsc$743131184;
if ($lzsc$604986472 in $lzsc$1804830789) {
if ($lzsc$1804830789[$lzsc$604986472].ready) {
$lzsc$1804830789[$lzsc$604986472].sendEvent($lzsc$892564543)
}
}
}
}
}
if (this.relative) {
this.to = this.origto
} else {
this.to = this.origto - this.target.getExpectedAttribute(this.attribute)
}
this.target.addToExpectedAttribute(this.attribute, this.to);
this.target.__LZincrementCounter(this.attribute);
this.currentValue = 0;
this.calcControlValues();
this.doBegin = true
}, resetAnimator: 
/* -*- file: controllers/LaszloAnimation.lzs#222.1 -*- */
function resetAnimator () {
if (this.from != null) {
var $lzsc$284244699 = this.target;
var $lzsc$662523455 = this.attribute;
var $lzsc$1682847134 = this.from;
if (!$lzsc$284244699.__LZdeleted) {
var $lzsc$694413440 = $lzsc$284244699.setters;
if ($lzsc$694413440 && $lzsc$662523455 in $lzsc$694413440) {
$lzsc$284244699[$lzsc$694413440[$lzsc$662523455]]($lzsc$1682847134)
} else {
if ($lzsc$694413440 == null) {
Debug.warn("null setters on", $lzsc$284244699, $lzsc$662523455, $lzsc$1682847134)
}
$lzsc$284244699[$lzsc$662523455] = $lzsc$1682847134;
var $lzsc$1787328158 = "on" + $lzsc$662523455;
if ($lzsc$1787328158 in $lzsc$284244699) {
if ($lzsc$284244699[$lzsc$1787328158].ready) {
$lzsc$284244699[$lzsc$1787328158].sendEvent($lzsc$1682847134)
}
}
}
}
var $1_d = this.from - this.target.getExpectedAttribute(this.attribute);
this.target.addToExpectedAttribute(this.attribute, $1_d)
}
if (!this.relative) {
this.to = this.origto - this.target.getExpectedAttribute(this.attribute);
this.calcControlValues()
}
this.target.addToExpectedAttribute(this.attribute, this.to);
this.target.__LZincrementCounter(this.attribute);
this.currentValue = 0;
this.doBegin = true
}, beginAnimator: 
/* -*- file: controllers/LaszloAnimation.lzs#257.1 -*- */
function beginAnimator ($1_time) {
this.startTime = $1_time;
this.lastIterationTime = $1_time;
if (this.onstart.ready) {
this.onstart.sendEvent($1_time)
}
this.doBegin = false
}, stop: 
/* -*- file: controllers/LaszloAnimation.lzs#274.1 -*- */
function stop () {
if (!this.isactive) {
return
}
var $1_e_prop = "e_" + this.attribute;
if (!this.target[$1_e_prop].c) {
this.target[$1_e_prop].c = 0
}
this.target[$1_e_prop].c--;
if (this.target[$1_e_prop].c <= 0) {
this.target[$1_e_prop].c = 0;
this.target[$1_e_prop].v = null
} else {
this.target[$1_e_prop].v -= this.to - this.currentValue
}
this.__LZhalt()
}, __LZfinalizeAnim: 
/* -*- file: controllers/LaszloAnimation.lzs#293.1 -*- */
function __LZfinalizeAnim () {
var $1_e_prop = "e_" + this.attribute;
if (!this.target[$1_e_prop].c) {
this.target[$1_e_prop].c = 0
}
this.target[$1_e_prop].c -= 1;
if (this.target[$1_e_prop].c <= 0) {
this.target[$1_e_prop].c = 0;
var $lzsc$107775541 = this.target;
var $lzsc$1205490207 = this.attribute;
var $lzsc$1557655535 = this.target[$1_e_prop].v;
if (!$lzsc$107775541.__LZdeleted) {
var $lzsc$1351096750 = $lzsc$107775541.setters;
if ($lzsc$1351096750 && $lzsc$1205490207 in $lzsc$1351096750) {
$lzsc$107775541[$lzsc$1351096750[$lzsc$1205490207]]($lzsc$1557655535)
} else {
if ($lzsc$1351096750 == null) {
Debug.warn("null setters on", $lzsc$107775541, $lzsc$1205490207, $lzsc$1557655535)
}
$lzsc$107775541[$lzsc$1205490207] = $lzsc$1557655535;
var $lzsc$983432406 = "on" + $lzsc$1205490207;
if ($lzsc$983432406 in $lzsc$107775541) {
if ($lzsc$107775541[$lzsc$983432406].ready) {
$lzsc$107775541[$lzsc$983432406].sendEvent($lzsc$1557655535)
}
}
}
}
this.target[$1_e_prop].v = null
}
this.__LZhalt()
}, calcNextValue: 
/* -*- file: controllers/LaszloAnimation.lzs#318.1 -*- */
function calcNextValue ($1_timeDifference) {
var $2_nextValue = this.currentValue;
var $3_aEndPole = this.endPole;
var $4_aBeginPole = this.beginPole;
var $5_K = Math.exp($1_timeDifference * 1 / this.duration * Math.log(this.primary_K));
if ($5_K != 1) {
var $6_aNumerator = $4_aBeginPole * $3_aEndPole * (1 - $5_K);
var $7_aDenominator = $3_aEndPole - $5_K * $4_aBeginPole;
if ($7_aDenominator != 0) {
$2_nextValue = $6_aNumerator / $7_aDenominator
}
}
return $2_nextValue
}, calcNextValueLinear: 
/* -*- file: controllers/LaszloAnimation.lzs#346.1 -*- */
function calcNextValueLinear ($1_timeDifference) {
var $2_elapsed = $1_timeDifference / this.duration;
return $2_elapsed * this.to
}, update: 
/* -*- file: controllers/LaszloAnimation.lzs#364.1 -*- */
function update ($1_time) {
var $2_animatorIsDone = false;
if (this.doBegin) {
this.beginAnimator($1_time)
} else {
if (!this.paused) {
var $3_aTotalTimeDifference = $1_time - this.startTime;
if ($3_aTotalTimeDifference < this.duration) {
this.setValue(this.calcNextValue($3_aTotalTimeDifference));
this.lastIterationTime = $1_time
} else {
this.setValue(this.to);
return this.checkRepeat()
}
}
}
}, setValue: 
/* -*- file: controllers/LaszloAnimation.lzs#394.1 -*- */
function setValue ($1_value) {
var $2_aDiff = $1_value - this.currentValue;
if (this.target.setAttribute) {
var $lzsc$2058330218 = this.target;
var $lzsc$1908645817 = this.attribute;
var $lzsc$1322830358 = this.target[this.attribute] + $2_aDiff;
if (!$lzsc$2058330218.__LZdeleted) {
var $lzsc$886224824 = $lzsc$2058330218.setters;
if ($lzsc$886224824 && $lzsc$1908645817 in $lzsc$886224824) {
$lzsc$2058330218[$lzsc$886224824[$lzsc$1908645817]]($lzsc$1322830358)
} else {
if ($lzsc$886224824 == null) {
Debug.warn("null setters on", $lzsc$2058330218, $lzsc$1908645817, $lzsc$1322830358)
}
$lzsc$2058330218[$lzsc$1908645817] = $lzsc$1322830358;
var $lzsc$2015058454 = "on" + $lzsc$1908645817;
if ($lzsc$2015058454 in $lzsc$2058330218) {
if ($lzsc$2058330218[$lzsc$2015058454].ready) {
$lzsc$2058330218[$lzsc$2015058454].sendEvent($lzsc$1322830358)
}
}
}
}
}
this.currentValue = $1_value
}, toString: 
/* -*- file: controllers/LaszloAnimation.lzs#409.1 -*- */
function toString () {
return "Animator for " + this.target + " attribute:" + this.attribute + " to:" + this.to
}}, {tagname: "animator"});
(function  () {
var $lzsc$temp = function  () {
with (LzAnimator) {
with (LzAnimator.prototype) {
setters.motion = "setMotion";
setters.to = "setTo"
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzLayout = Class.make("LzLayout", LzNode, {locked: 2, subviews: null, updateDelegate: null, delegates: null, construct: 
/* -*- file: controllers/LaszloLayout.lzs#102.1 -*- */
function construct ($1_view, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.subviews = new Array();
if (this.immediateparent.layouts == null) {
this.immediateparent.layouts = [this]
} else {
this.immediateparent.layouts.push(this)
}
this.updateDelegate = new LzDelegate(this, "update");
this.delegates = [this.updateDelegate];
if (this.immediateparent.isinited) {
this.__parentInit()
} else {
this.initDelegate = new LzDelegate(this, "__parentInit", this.immediateparent, "oninit");
this.delegates.push(this.initDelegate)
}
}, constructWithArgs: 
/* -*- file: controllers/LaszloLayout.lzs#132.1 -*- */
function constructWithArgs ($1_args) {
this.delegates.push(new LzDelegate(this, "gotNewSubview", this.immediateparent, "onaddsubview"));
this.delegates.push(new LzDelegate(this, "removeSubview", this.immediateparent, "onremovesubview"));
var $2_vsl = this.immediateparent.subviews.length;
for (var $3_i = 0;
$3_i < $2_vsl;
$3_i++) {
this.gotNewSubview(this.immediateparent.subviews[$3_i])
}
}, destroy: 
/* -*- file: controllers/LaszloLayout.lzs#152.1 -*- */
function destroy () {
this.releaseLayout();
(arguments.callee.superclass ? arguments.callee.superclass.prototype["destroy"] : this.nextMethod(arguments.callee, "destroy")).apply(this, arguments)
}, reset: 
/* -*- file: controllers/LaszloLayout.lzs#168.1 -*- */
function reset ($1_e) {
if (this.locked) {
return
}
this.update($1_e)
}, addSubview: 
/* -*- file: controllers/LaszloLayout.lzs#183.1 -*- */
function addSubview ($1_sd) {
if ($1_sd.getOption("layoutAfter")) {
this.__LZinsertAfter($1_sd, $1_sd.getOption("layoutAfter"))
} else {
this.subviews.push($1_sd)
}
}, gotNewSubview: 
/* -*- file: controllers/LaszloLayout.lzs#199.1 -*- */
function gotNewSubview ($1_sd) {
if (!$1_sd.getOption("ignorelayout")) {
this.addSubview($1_sd)
}
}, removeSubview: 
/* -*- file: controllers/LaszloLayout.lzs#211.1 -*- */
function removeSubview ($1_sd) {
for (var $2_i = this.subviews.length - 1;
$2_i >= 0;
$2_i--) {
if (this.subviews[$2_i] == $1_sd) {
this.subviews.splice($2_i, 1);
break
}
}
this.reset()
}, ignore: 
/* -*- file: controllers/LaszloLayout.lzs#229.1 -*- */
function ignore ($1_s) {
for (var $2_i = this.subviews.length - 1;
$2_i >= 0;
$2_i--) {
if (this.subviews[$2_i] == $1_s) {
this.subviews.splice($2_i, 1);
break
}
}
this.reset()
}, lock: 
/* -*- file: controllers/LaszloLayout.lzs#247.1 -*- */
function lock () {
this.locked = true
}, unlock: 
/* -*- file: controllers/LaszloLayout.lzs#256.1 -*- */
function unlock () {
this.locked = false;
this.reset()
}, __LZsetLocked: 
/* -*- file: controllers/LaszloLayout.lzs#266.1 -*- */
function __LZsetLocked ($1_locked) {
if (this.locked == $1_locked) {
return
}
if ($1_locked) {
this.lock()
} else {
this.unlock()
}
}, __parentInit: 
/* -*- file: controllers/LaszloLayout.lzs#278.1 -*- */
function __parentInit () {
if (this.locked == 2) {
if (this.isinited) {
this.unlock()
} else {
new LzDelegate(this, "unlock", this, "oninit")
}
}
}, releaseLayout: 
/* -*- file: controllers/LaszloLayout.lzs#292.1 -*- */
function releaseLayout () {
if (this.delegates) {
for (var $1_i = this.delegates.length - 1;
$1_i >= 0;
$1_i--) {
this.delegates[$1_i].unregisterAll()
}
}
if (this.immediateparent && this.immediateparent.layouts) {
for (var $1_i = this.immediateparent.layouts.length - 1;
$1_i >= 0;
$1_i--) {
if (this.immediateparent.layouts[$1_i] == this) {
this.immediateparent.layouts.splice($1_i, 1)
}
}
}
}, setLayoutOrder: 
/* -*- file: controllers/LaszloLayout.lzs#318.1 -*- */
function setLayoutOrder ($1_sub1, $2_sub2) {
for (var $3_i = this.subviews.length - 1;
$3_i >= 0;
$3_i--) {
if (this.subviews[$3_i] == $2_sub2) {
this.subviews.splice($3_i, 1);
break
}
}
if ($1_sub1 == "first") {
this.subviews.unshift($2_sub2)
} else {
if ($1_sub1 == "last") {
this.subviews.push($2_sub2)
} else {
for (var $3_i = this.subviews.length - 1;
$3_i >= 0;
$3_i--) {
if (this.subviews[$3_i] == $1_sub1) {
this.subviews.splice($3_i + 1, 0, $2_sub2);
break
}
}
}
}
this.reset();
return
}, swapSubviewOrder: 
/* -*- file: controllers/LaszloLayout.lzs#351.1 -*- */
function swapSubviewOrder ($1_sub1, $2_sub2) {
var $3_s1p = -1;
var $4_s2p = -1;
for (var $5_i = this.subviews.length - 1;
$5_i >= 0 && ($3_s1p < 0 || $4_s2p < 0);
$5_i--) {
if (this.subviews[$5_i] == $1_sub1) {
$3_s1p = $5_i
} else {
if (this.subviews[$5_i] == $2_sub2) {
$4_s2p = $5_i
}
}
}
this.subviews[$4_s2p] = $1_sub1;
this.subviews[$3_s1p] = $2_sub2;
this.reset();
return
}, __LZinsertAfter: 
/* -*- file: controllers/LaszloLayout.lzs#375.1 -*- */
function __LZinsertAfter ($1_newsub, $2_oldsub) {
for (var $3_i = this.subviews.length - 1;
$3_i >= 0;
$3_i--) {
if (this.subviews[$3_i] == $2_oldsub) {
this.subviews.splice($3_i, 0, $1_newsub)
}
}
}, toString: 
/* -*- file: controllers/LaszloLayout.lzs#402.1 -*- */
function toString () {
return "LzLayout for view " + this.immediateparent
}}, {tagname: "layout"});
(function  () {
var $lzsc$temp = function  () {
with (LzLayout) {
with (LzLayout.prototype) {
setters.locked = "__LZsetLocked"
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzFont = Class.make("LzFont", null, {style: null, name: null, height: null, ascent: null, descent: null, advancetable: null, lsbtable: null, rsbtable: null, initialize: 
/* -*- file: helpers/LzFont.lzs#365.1 -*- */
function initialize ($1_fontobject, $2_attrs, $3_style) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
this.name = $1_fontobject.name;
this.style = $3_style;
this.fontobject = $1_fontobject;
$1_fontobject[$3_style] = this;
for (var $4_k in $2_attrs) {
if ($4_k == "leading") {
continue
}
this[$4_k] = $2_attrs[$4_k]
}
this.height = this.ascent + this.descent;
this.advancetable[13] = this.advancetable[32];
this.advancetable[160] = 0
}, leading: 2, toString: 
/* -*- file: helpers/LzFont.lzs#391.1 -*- */
function toString () {
return "Font style " + this.style + " of name " + this.name
}}, null);
var LzSelectionManager = Class.make("LzSelectionManager", LzNode, {sel: null, selectedHash: null, selected: null, construct: 
/* -*- file: helpers/LzSelectionManager.lzs#121.1 -*- */
function construct ($1_view, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.toggle = $2_args.toggle == true;
this.sel = $2_args.selFuncName == null ? "setSelected" : $2_args.selFuncName;
this.selected = new Array();
this.selectedHash = new Object();
this.lastRange = null
}, select: 
/* -*- file: helpers/LzSelectionManager.lzs#138.1 -*- */
function select ($1_o) {
if (this.isSelected($1_o) && (this.toggle || this.isMultiSelect($1_o))) {
this.unselect($1_o);
return
}
if (this.selected.length > 0 && this.isRangeSelect($1_o)) {
var $2_s = this.lastRange != null ? this.lastRange : this.selected[0];
if ($2_s != $1_o) {
this.selectRange($2_s, $1_o)
}
return
}
if (!this.isMultiSelect($1_o)) {
this.clearSelection()
}
this.makeSelected($1_o)
}, isSelected: 
/* -*- file: helpers/LzSelectionManager.lzs#164.1 -*- */
function isSelected ($1_o) {
return this.selectedHash[$1_o.getUID()]
}, makeSelected: 
/* -*- file: helpers/LzSelectionManager.lzs#173.1 -*- */
function makeSelected ($1_s) {
if (this.selectedHash[$1_s.getUID()]) {
return
}
this.selectedHash[$1_s.getUID()] = true;
this.selected.push($1_s);
$1_s[this.sel](true)
}, unselect: 
/* -*- file: helpers/LzSelectionManager.lzs#187.1 -*- */
function unselect ($1_o) {
for (var $2_i = this.selected.length - 1;
$2_i >= 0;
$2_i--) {
if (this.selected[$2_i] == $1_o) {
this.selectedHash[$1_o.getUID()] = false;
$1_o[this.sel](false);
this.selected.splice($2_i, 1);
break
}
}
}, clearSelection: 
/* -*- file: helpers/LzSelectionManager.lzs#201.1 -*- */
function clearSelection () {
var $1_s;
while ($1_s = this.selected.pop()) {
$1_s[this.sel](false)
}
this.selected = new Array();
this.selectedHash = new Object();
this.lastRange = null
}, getSelection: 
/* -*- file: helpers/LzSelectionManager.lzs#215.1 -*- */
function getSelection () {
return this.selected
}, selectRange: 
/* -*- file: helpers/LzSelectionManager.lzs#224.1 -*- */
function selectRange ($1_s, $2_e) {
var $3_pview = $1_s.immediateparent;
var $4_svs = $3_pview.subviews;
var $5_st = null;
var $6_en = null;
for (var $7_i = 0;
$7_i < $4_svs.length;
$7_i++) {
if ($4_svs[$7_i] == $1_s) {
$5_st = $7_i
}
if ($4_svs[$7_i] == $2_e) {
$6_en = $7_i
}
if (null != $5_st && null != $6_en) {
break
}
}
var $8_dir = $5_st > $6_en ? -1 : 1;
this.clearSelection();
this.lastRange = $1_s;
for (var $7_i = $5_st;
$7_i != $6_en + $8_dir;
$7_i += $8_dir) {
this.makeSelected($3_pview.subviews[$7_i])
}
}, isMultiSelect: 
/* -*- file: helpers/LzSelectionManager.lzs#252.1 -*- */
function isMultiSelect () {
return LzKeys.isKeyDown("control")
}, isRangeSelect: 
/* -*- file: helpers/LzSelectionManager.lzs#262.1 -*- */
function isRangeSelect () {
return LzKeys.isKeyDown("shift")
}, toString: 
/* -*- file: helpers/LzSelectionManager.lzs#266.1 -*- */
function toString () {
return "LzSelectionManager"
}}, {tagname: "selectionmanager"});
var LzDataSelectionManager = Class.make("LzDataSelectionManager", LzSelectionManager, {makeSelected: 
/* -*- file: helpers/LzDataSelectionManager.lzs#32.1 -*- */
function makeSelected ($1_o) {
var $2_so = $1_o.datapath.p;
if (this.manager == null) {
this.manager = $1_o.cloneManager
}
if ($2_so.sel) {
return
}
$2_so.sel = true;
this.selected.push($2_so);
$1_o.datapath[this.sel](true);
if (this.manager == null) {
this.singleClone = $1_o
}
}, unselect: 
/* -*- file: helpers/LzDataSelectionManager.lzs#50.1 -*- */
function unselect ($1_o) {
if (this.manager == null) {
this.manager = $1_o.cloneManager
}
var $2_so = $1_o.datapath.p;
$2_so.sel = false;
for (var $3_i = this.selected.length - 1;
$3_i >= 0;
$3_i--) {
if (this.selected[$3_i] == $2_so) {
this.selected.splice($3_i, 1);
break
}
}
$1_o.datapath[this.sel](false);
if ($1_o == this.singleClone) {
this.singleClone = null
}
}, selectRange: 
/* -*- file: helpers/LzDataSelectionManager.lzs#73.1 -*- */
function selectRange ($1_s, $2_e) {
if (this.manager == null) {
this.manager = $2_e.cloneManager;
if (this.manager == null) {
Debug.write("selectRange failed, no clones");
return
}
}
var $3_nodes = this.manager.nodes;
var $4_st = -1;
var $5_en = -1;
var $6_i = 0;
var $7_ennode = $2_e.datapath.p;
while (($4_st == -1 || $5_en == -1) && $6_i < $3_nodes.length) {
if ($3_nodes[$6_i] == $1_s) {
$4_st = $6_i
}
if ($3_nodes[$6_i] == $7_ennode) {
$5_en = $6_i
}
$6_i++
}
var $8_dir = $4_st > $5_en ? -1 : 1;
this.clearSelection();
this.lastRange = $1_s;
if ($4_st == -1 || $5_en == -1) {
return
}
for (var $6_i = $4_st;
$6_i != $5_en + $8_dir;
$6_i += $8_dir) {
var $9_p = $3_nodes[$6_i];
$9_p.sel = true;
this.selected.push($9_p);
this.__LZsetSelected($9_p, true)
}
}, getSelection: 
/* -*- file: helpers/LzDataSelectionManager.lzs#128.1 -*- */
function getSelection () {
var $1_r = [];
for (var $2_i = 0;
$2_i < this.selected.length;
$2_i++) {
$1_r.push(new LzDatapointer(null, {pointer: this.selected[$2_i]}))
}
return $1_r
}, clearSelection: 
/* -*- file: helpers/LzDataSelectionManager.lzs#141.1 -*- */
function clearSelection () {
while (this.selected.length) {
var $1_p = this.selected.pop();
$1_p.sel = false;
this.__LZsetSelected($1_p, false)
}
this.lastRange = null
}, isSelected: 
/* -*- file: helpers/LzDataSelectionManager.lzs#155.1 -*- */
function isSelected ($1_o) {
if (this.manager == null) {
this.manager = $1_o.cloneManager
}
return $1_o.datapath.p.sel
}, __LZsetSelected: 
/* -*- file: helpers/LzDataSelectionManager.lzs#168.1 -*- */
function __LZsetSelected ($1_p, $2_val) {
if (this.manager != null) {
var $3_cl = this.manager.getCloneForNode($1_p, true);
if ($3_cl) {
$3_cl.datapath[this.sel]($2_val)
} else {
$1_p.sel = $2_val
}
} else {
if (!$2_val) {
if (this.singleClone != null && this.singleClone.datapath.p == $1_p) {
this.singleClone.datapath[this.sel](false);
this.singleClone = null;
return
}
}
$1_p.sel = $2_val
}
}}, {tagname: "dataselectionmanager"});
var LzCommand = Class.make("LzCommand", LzNode, {active: true, keys: null, setKeys: 
/* -*- file: helpers/LzCommand.lzs#66.1 -*- */
function setKeys ($1_k) {
this.keys = $1_k;
LzKeys.callOnKeyCombo(this, $1_k)
}, execute: 
/* -*- file: helpers/LzCommand.lzs#75.1 -*- */
function execute ($1_d) {
if (this.active) {
if (this.onselect.ready) {
this.onselect.sendEvent($1_d)
}
}
}, keysToString: 
/* -*- file: helpers/LzCommand.lzs#88.1 -*- */
function keysToString () {
var $1_DisplayKeys = {control: "Ctrl", shift: "Shift", alt: "Alt"}
var $2_s = "";
var $3_k = "";
var $4_l = this.keys.length;
for (var $5_i = 0;
$5_i < $4_l - 1;
$5_i++) {
$3_k = this.keys[$5_i];
if ($3_k == "Control") {
$3_k = "Ctrl"
}
$2_s = $2_s + $3_k + "+"
}
$3_k = this.keys[$5_i];
if ($3_k == "Control") {
$3_k = "Ctrl"
}
$2_s = $2_s + $3_k;
return $2_s
}}, {tagname: "command"});
(function  () {
var $lzsc$temp = function  () {
with (LzCommand) {
with (LzCommand.prototype) {
setters.key = "setKeys";
DeclareEvent(prototype, "onselect")
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzState = Class.make("LzState", LzNode, {asyncnew: false, subh: null, pooling: false, construct: 
/* -*- file: helpers/LzState.lzs#128.1 -*- */
function construct ($1_parent, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.heldArgs = {}
this.appliedChildren = [];
this.isapplied = false
}, createChildren: 
/* -*- file: helpers/LzState.lzs#138.1 -*- */
function createChildren ($1_carr) {
this.subh = $1_carr;
this.__LZinstantiationDone()
}, setApply: 
/* -*- file: helpers/LzState.lzs#147.1 -*- */
function setApply ($1_doapply) {
if (typeof $1_doapply == "function") {
this.addProperty("apply", $1_doapply);
return
}
if ($1_doapply) {
if (this.isinited) {
this.apply()
} else {
new LzDelegate(this, "apply", this, "oninit")
}
} else {
if (this.isinited) {
this.remove()
}
}
}, apply: 
/* -*- file: helpers/LzState.lzs#179.1 -*- */
function apply () {
if (this.isapplied) {
return
}
this.isapplied = true;
var $1_od = this.parent.__LZdelegates;
this.parent.__LZdelegates = null;
this.parent.__LZapplyArgs(this.heldArgs);
if (this.subh) {
var $2_shl = this.subh.length
}
this.parent.__LZsetPreventInit();
for (var $3_i = 0;
$3_i < $2_shl;
$3_i++) {
if (this.__LZpool && this.__LZpool[$3_i]) {
this.appliedChildren.push(this.__LZretach(this.__LZpool[$3_i]))
} else {
this.appliedChildren.push(this.parent.makeChild(this.subh[$3_i], this.asyncnew))
}
}
this.parent.__LZclearPreventInit();
this.parent.__LZresolveReferences();
this.__LZstatedelegates = this.parent.__LZdelegates;
this.parent.__LZdelegates = $1_od;
if (this.onapply.ready) {
this.onapply.sendEvent(this)
}
}, remove: 
/* -*- file: helpers/LzState.lzs#220.1 -*- */
function remove () {
if (!this.isapplied) {
return
}
if (this.onremove.ready) {
this.onremove.sendEvent(this)
}
this.isapplied = false;
if (this.__LZstatedelegates) {
for (var $1_i = 0;
$1_i < this.__LZstatedelegates.length;
$1_i++) {
this.__LZstatedelegates[$1_i].unregisterAll()
}
}
if (this.pooling && this.appliedChildren.length) {
this.__LZpool = []
}
for (var $1_i = 0;
$1_i < this.appliedChildren.length;
$1_i++) {
var $2_ac = this.appliedChildren[$1_i];
if (this.pooling) {
if ($2_ac instanceof LzView) {
this.__LZpool.push(this.__LZdetach($2_ac))
} else {
$2_ac.destroy();
this.__LZpool.push(null)
}
} else {
$2_ac.destroy()
}
}
this.appliedChildren = []
}, destroy: 
/* -*- file: helpers/LzState.lzs#261.1 -*- */
function destroy () {
this.pooling = false;
this.remove();
(arguments.callee.superclass ? arguments.callee.superclass.prototype["destroy"] : this.nextMethod(arguments.callee, "destroy")).apply(this, arguments)
}, __LZapplyArgs: 
/* -*- file: helpers/LzState.lzs#286.16 -*- */
function __LZapplyArgs ($1_args) {
var $2_ap = this.addProperty;
this.addProperty = (function  () {
var $lzsc$temp = 
/* -*- file: helpers/LzState.lzs#288.22 -*- */
function  ($1_prop, $2_val) {
this.heldArgs[$1_prop] = $2_val
}
$lzsc$temp._dbg_name = "this.addProperty";
return $lzsc$temp
})();
(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZapplyArgs"] : this.nextMethod(arguments.callee, "__LZapplyArgs")).call(this, $1_args);
this.addProperty = $2_ap
}, __LZstoreRefs: 
/* -*- file: helpers/LzState.lzs#301.16 -*- */
function __LZstoreRefs ($1_refs, $2_prop) {
var $3_parrefs = {}
var $4_myrefs = {}
for (var $5_p in $1_refs) {
if (LzState.staterefs[$5_p]) {
$4_myrefs[$5_p] = $1_refs[$5_p];
var $6_havemyrefs = true
} else {
$3_parrefs[$5_p] = $1_refs[$5_p];
var $7_haveparrefs = true
}
}
if ($6_havemyrefs) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZstoreRefs"] : this.nextMethod(arguments.callee, "__LZstoreRefs")).call(this, $4_myrefs, $2_prop)
}
if ($7_haveparrefs) {
this.heldArgs[$2_prop] = $3_parrefs
}
}, __LZstoreDelegates: 
/* -*- file: helpers/LzState.lzs#332.16 -*- */
function __LZstoreDelegates ($1_delarr) {
var $2_pardels = [];
var $3_mydels = [];
for (var $4_i = 0;
$4_i < $1_delarr.length;
$4_i += 3) {
if (LzState.stateevents[$1_delarr[$4_i]] && !$1_delarr[$4_i + 2]) {
var $5_arrtopush = $3_mydels;
var $6_mname = $1_delarr[$4_i + 1];
if (this.heldArgs[$6_mname]) {
this[$6_mname] = this.heldArgs[$6_mname];
delete this.heldArgs[$6_mname]
}
this.__LZaddSetter($6_mname, "__LZsetProperty")
} else {
var $5_arrtopush = $2_pardels
}
$5_arrtopush.push($1_delarr[$4_i], $1_delarr[$4_i + 1], $1_delarr[$4_i + 2])
}
if ($3_mydels.length) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZstoreDelegates"] : this.nextMethod(arguments.callee, "__LZstoreDelegates")).call(this, $3_mydels)
}
if ($2_pardels.length) {
this.heldArgs.$delegates = $2_pardels
}
}, __LZsetProperty: 
/* -*- file: helpers/LzState.lzs#377.1 -*- */
function __LZsetProperty ($1_prop, $2_propname) {
this[$2_propname] = $1_prop
}, __LZdetach: 
/* -*- file: helpers/LzState.lzs#384.1 -*- */
function __LZdetach ($1_aview) {
$1_aview.setVisible(false);
return $1_aview
}, __LZretach: 
/* -*- file: helpers/LzState.lzs#392.1 -*- */
function __LZretach ($1_aview) {
$1_aview.setVisible(true);
return $1_aview
}}, {tagname: "state", staterefs: {apply: true}, stateevents: {onremove: true, onapply: true}});
(function  () {
var $lzsc$temp = function  () {
with (LzState) {
with (LzState.prototype) {
DeclareEvent(prototype, "onapply");
DeclareEvent(prototype, "onremove");
setters.apply = "setApply";
setters.$setters = null;
prototype.$isstate = true;
setters.asyncnew = "__LZsetProperty";
setters.pooling = "__LZsetProperty";
setters.__LZsourceLocation = "__LZsetProperty"
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzDataNode = Trait.make("LzDataNode", null, {nodeType: null, getPreviousSibling: 
/* -*- file: data/LzDataNode.lzs#87.1 -*- */
function getPreviousSibling () {
if (!this.parentNode) {
return null
}
if (this.parentNode.__LZcoDirty) {
this.parentNode.__LZupdateCO()
}
return this.parentNode.childNodes[this.__LZo - 1]
}, getParent: 
/* -*- file: data/LzDataNode.lzs#97.1 -*- */
function getParent () {
return this.parentNode
}, getOffset: 
/* -*- file: data/LzDataNode.lzs#105.1 -*- */
function getOffset () {
if (!this.parentNode) {
return 0
}
if (this.parentNode.__LZcoDirty) {
this.parentNode.__LZupdateCO()
}
return this.__LZo
}, getNextSibling: 
/* -*- file: data/LzDataNode.lzs#123.1 -*- */
function getNextSibling () {
if (!this.parentNode) {
return null
}
if (this.parentNode.__LZcoDirty) {
this.parentNode.__LZupdateCO()
}
return this.parentNode.childNodes[this.__LZo + 1]
}, childOf: 
/* -*- file: data/LzDataNode.lzs#143.1 -*- */
function childOf ($1_el, $2_allowself) {
var $3_p = $2_allowself ? this : this.parentNode;
while ($3_p) {
if ($3_p == $1_el) {
return true
}
$3_p = $3_p.parentNode
}
return false
}, setOwnerDocument: 
/* -*- file: data/LzDataNode.lzs#157.1 -*- */
function setOwnerDocument ($1_ownerDoc) {
this.ownerDocument = $1_ownerDoc;
if (this.childNodes) {
for (var $2_i = 0;
$2_i < this.childNodes.length;
$2_i++) {
this.childNodes[$2_i].setOwnerDocument($1_ownerDoc)
}
}
if (this.onownerDocument && this.onownerDocument.ready) {
this.onownerDocument.sendEvent($1_ownerDoc)
}
}, __LZXMLescape: 
/* -*- file: data/LzDataNode.lzs#180.1 -*- */
function __LZXMLescape ($1_t) {
if (typeof $1_t != "string") {
return $1_t
}
var $2_olen = $1_t.length;
var $3_r = "";
for (var $4_i = 0;
$4_i < $2_olen;
$4_i++) {
var $5_code = $1_t.charCodeAt($4_i);
if ($5_code < 32) {
$3_r += "&#x" + LzUtils.dectohex($5_code, 0) + ";"
} else {
var $6_c = $1_t.charAt($4_i);
if (LzDataNode.__LZescapechars[$6_c] != null) {
$3_r += LzDataNode.__LZescapechars[$6_c]
} else {
$3_r += $6_c
}
}
}
return $3_r
}, __LZlockFromUpdate: 
/* -*- file: data/LzDataNode.lzs#206.1 -*- */
function __LZlockFromUpdate ($1_locker) {
this.ownerDocument.__LZdoLock($1_locker)
}, __LZunlockFromUpdate: 
/* -*- file: data/LzDataNode.lzs#213.1 -*- */
function __LZunlockFromUpdate ($1_locker) {
this.ownerDocument.__LZdoUnlock($1_locker)
}}, {ELEMENT_NODE: 1, TEXT_NODE: 3, DOCUMENT_NODE: 9, __LZescapechars: {"&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&apos;"}, stringToLzData: 
/* -*- file: data/LzDataNode.lzs#229.8 -*- */
function stringToLzData ($1_str, $2_trimwhitespace, $3_nsprefix) {
if ($1_str != null && $1_str != "") {
var $4_nativexml = LzXMLParser.parseXML($1_str, $2_trimwhitespace, $3_nsprefix);
var $5_lfcnode = LzXMLTranslator.copyXML($4_nativexml, $2_trimwhitespace, $3_nsprefix);
return $5_lfcnode
} else {
return null
}
}, whitespaceChars: {" ": true, "\r": true, "\n": true, "\t": true}, trim: 
/* -*- file: data/LzDataNode.lzs#249.8 -*- */
function trim ($1_str) {
var $2_whitech = LzDataNode.whitespaceChars;
var $3_len = $1_str.length;
var $4_sindex = 0;
var $5_eindex = $1_str.length - 1;
var $6_ch;
while ($4_sindex < $3_len) {
$6_ch = $1_str.charAt($4_sindex);
if ($2_whitech[$6_ch] != true) {
break
}
$4_sindex++
}
while ($5_eindex > $4_sindex) {
$6_ch = $1_str.charAt($5_eindex);
if ($2_whitech[$6_ch] != true) {
break
}
$5_eindex--
}
return $1_str.slice($4_sindex, $5_eindex + 1)
}});
(function  () {
var $lzsc$temp = function  () {
with (LzDataNode) {
with (LzDataNode.prototype) {
DeclareEvent(prototype, "onownerDocument");
prototype.getPreviousSibling.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzDataNode.lzs#115.45 -*- */
function  ($1_who, $2_self) {
return [this.parentNode, "childNodes", this, "parentNode"]
}
$lzsc$temp._dbg_name = "prototype.getPreviousSibling.dependencies";
return $lzsc$temp
})();
prototype.getNextSibling.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzDataNode.lzs#132.41 -*- */
function  ($1_who, $2_self) {
return [this.parentNode, "childNodes", this, "parentNode"]
}
$lzsc$temp._dbg_name = "prototype.getNextSibling.dependencies";
return $lzsc$temp
})()
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzDataElementTrait = Trait.make("LzDataElementTrait", null, {__LZo: -1, __LZcoDirty: true, __LZchangeQ: null, __LZlocker: null, insertBefore: 
/* -*- file: data/LzDataElement.lzs#57.1 -*- */
function insertBefore ($1_newChild, $2_refChild) {
this.__LZcoDirty = true;
for (var $3_i = 0;
$3_i < this.childNodes.length;
$3_i++) {
if (this.childNodes[$3_i] == $2_refChild) {
this.childNodes.splice($3_i, 0, $1_newChild);
$1_newChild.setOwnerDocument(this.ownerDocument);
$1_newChild.parentNode = this;
if ($1_newChild.onparentNode.ready) {
$1_newChild.onparentNode.sendEvent(this)
}
if (this.onchildNodes.ready) {
this.onchildNodes.sendEvent($1_newChild)
}
this.ownerDocument.handleDocumentChange("insertBefore", this, 0);
return $1_newChild
}
}
return null
}, replaceChild: 
/* -*- file: data/LzDataElement.lzs#85.1 -*- */
function replaceChild ($1_newChild, $2_oldChild) {
this.__LZcoDirty = true;
for (var $3_i = 0;
$3_i < this.childNodes.length;
$3_i++) {
if (this.childNodes[$3_i] == $2_oldChild) {
this.childNodes[$3_i] = $1_newChild;
$1_newChild.setOwnerDocument(this.ownerDocument);
$1_newChild.parentNode = this;
if ($1_newChild.onparentNode.ready) {
$1_newChild.onparentNode.sendEvent(this)
}
if (this.onchildNodes.ready) {
this.onchildNodes.sendEvent($1_newChild)
}
this.ownerDocument.handleDocumentChange("childNodes", this, 0, $1_newChild);
return $1_newChild
}
}
return null
}, removeChild: 
/* -*- file: data/LzDataElement.lzs#110.1 -*- */
function removeChild ($1_oldChild) {
var $2_reval = null;
this.__LZcoDirty = true;
for (var $3_i = 0;
$3_i < this.childNodes.length;
$3_i++) {
if (this.childNodes[$3_i] == $1_oldChild) {
this.childNodes.splice($3_i, 1);
if (this.onchildNodes.ready) {
this.onchildNodes.sendEvent($1_oldChild)
}
$2_reval = $1_oldChild;
this.ownerDocument.handleDocumentChange("removeChild", this, 0, $1_oldChild)
}
}
return $2_reval
}, appendChild: 
/* -*- file: data/LzDataElement.lzs#130.1 -*- */
function appendChild ($1_newChild) {
if (this.childNodes) {
this.childNodes.push($1_newChild)
} else {
this.childNodes = [$1_newChild]
}
$1_newChild.setOwnerDocument(this.ownerDocument);
$1_newChild.parentNode = this;
if ($1_newChild.onparentNode.ready) {
$1_newChild.onparentNode.sendEvent(this)
}
$1_newChild.__LZo = this.childNodes.length - 1;
if (this.onchildNodes.ready) {
this.onchildNodes.sendEvent($1_newChild)
}
this.ownerDocument.handleDocumentChange("appendChild", this, 0, $1_newChild);
return $1_newChild
}, hasChildNodes: 
/* -*- file: data/LzDataElement.lzs#154.1 -*- */
function hasChildNodes () {
return this.childNodes.length > 0
}, cloneNode: 
/* -*- file: data/LzDataElement.lzs#164.1 -*- */
function cloneNode ($1_deep) {
var $2_n = new LzDataElement(this.nodeName);
$2_n.setAttrs(this.attributes);
if ($1_deep) {
for (var $3_i = 0;
$3_i < this.childNodes.length;
$3_i++) {
$2_n.appendChild(this.childNodes[$3_i].cloneNode(true))
}
}
return $2_n
}, getAttr: 
/* -*- file: data/LzDataElement.lzs#181.1 -*- */
function getAttr ($1_name) {
if (this.attributes) {
return this.attributes[$1_name]
}
}, setAttr: 
/* -*- file: data/LzDataElement.lzs#197.1 -*- */
function setAttr ($1_name, $2_value) {
if (!this.attributes) {
this.attributes = {}
}
this.attributes[$1_name] = $2_value;
if (this.onattributes.ready) {
this.onattributes.sendEvent($1_name)
}
this.ownerDocument.handleDocumentChange("attributes", this, 1, {name: $1_name, value: $2_value, type: "set"});
return $2_value
}, removeAttr: 
/* -*- file: data/LzDataElement.lzs#211.1 -*- */
function removeAttr ($1_name) {
var $2_v = this.attributes[$1_name];
delete this.attributes[$1_name];
if (this.onattributes.ready) {
this.onattributes.sendEvent($1_name)
}
this.ownerDocument.handleDocumentChange("attributes", this, 1, {name: $1_name, value: $2_v, type: "remove"});
return $2_v
}, hasAttr: 
/* -*- file: data/LzDataElement.lzs#238.1 -*- */
function hasAttr ($1_name) {
return this.attributes[$1_name] != null
}, getFirstChild: 
/* -*- file: data/LzDataElement.lzs#246.1 -*- */
function getFirstChild () {
return this.childNodes[0]
}, getLastChild: 
/* -*- file: data/LzDataElement.lzs#260.1 -*- */
function getLastChild () {
return this.childNodes[this.childNodes.length - 1]
}, __LZupdateCO: 
/* -*- file: data/LzDataElement.lzs#274.1 -*- */
function __LZupdateCO () {
for (var $1_i = 0;
$1_i < this.childNodes.length;
$1_i++) {
this.childNodes[$1_i].__LZo = $1_i
}
this.__LZcoDirty = false
}, setAttrs: 
/* -*- file: data/LzDataElement.lzs#293.1 -*- */
function setAttrs ($1_attrs) {
var $2_a = {}
for (var $3_k in $1_attrs) {
$2_a[$3_k] = $1_attrs[$3_k]
}
this.attributes = $2_a;
if (this.onattributes.ready) {
this.onattributes.sendEvent($2_a)
}
if (this.ownerDocument) {
this.ownerDocument.handleDocumentChange("attributes", this, 1)
}
}, setChildNodes: 
/* -*- file: data/LzDataElement.lzs#309.1 -*- */
function setChildNodes ($1_children) {
this.childNodes = $1_children;
for (var $2_i = 0;
$2_i < $1_children.length;
$2_i++) {
var $3_c = $1_children[$2_i];
$3_c.setOwnerDocument(this.ownerDocument);
$3_c.parentNode = this;
if ($3_c.onparentNode) {
if ($3_c.onparentNode.ready) {
$3_c.onparentNode.sendEvent(this)
}
}
$3_c.__LZo = $2_i
}
this.__LZcoDirty = false;
if (this.onchildNodes) {
if (this.onchildNodes.ready) {
this.onchildNodes.sendEvent($1_children)
}
}
this.ownerDocument.handleDocumentChange("childNodes", this, 0)
}, setNodeName: 
/* -*- file: data/LzDataElement.lzs#331.1 -*- */
function setNodeName ($1_name) {
this.nodeName = $1_name;
if (this.onnodeName.ready) {
this.onnodeName.sendEvent($1_name)
}
if (this.parentNode) {
if (this.parentNode.onchildNodes.ready) {
this.parentNode.onchildNodes.sendEvent(this)
}
if (this.parentNode.onchildNode.ready) {
this.parentNode.onchildNode.sendEvent(this)
}
}
this.ownerDocument.handleDocumentChange("childNodeName", this.parentNode, 0);
this.ownerDocument.handleDocumentChange("nodeName", this, 1)
}, __LZgetText: 
/* -*- file: data/LzDataElement.lzs#348.1 -*- */
function __LZgetText () {
var $1_s = "";
for (var $2_i = 0;
$2_i < this.childNodes.length;
$2_i++) {
var $3_c = this.childNodes[$2_i];
if ($3_c.nodeType == LzDataNode.TEXT_NODE) {
$1_s += $3_c.data
}
}
return $1_s
}, getElementsByTagName: 
/* -*- file: data/LzDataElement.lzs#364.1 -*- */
function getElementsByTagName ($1_name) {
var $2_r = [];
for (var $3_i = 0;
$3_i < this.childNodes.length;
$3_i++) {
if (this.childNodes[$3_i].nodeName == $1_name) {
$2_r.push(this.childNodes[$3_i])
}
}
return $2_r
}, __LZlt: "<", __LZgt: ">", serialize: 
/* -*- file: data/LzDataElement.lzs#429.1 -*- */
function serialize () {
return this.serializeInternal(Infinity)
}, serializeInternal: 
/* -*- file: data/LzDataElement.lzs#437.1 -*- */
function serializeInternal ($1_len) {
if (arguments.length < 1) {
$1_len = Infinity
}
var $2_s = this.__LZlt + this.nodeName;
for (var $3_k in this.attributes) {
$2_s += " " + $3_k + '="' + this.__LZXMLescape(this.attributes[$3_k]) + '"';
if ($2_s.length > $1_len) {
break
}
}
if ($2_s.length <= $1_len && this.childNodes && this.childNodes.length) {
$2_s += this.__LZgt;
for (var $4_i = 0;
$4_i < this.childNodes.length;
$4_i++) {
$2_s += this.childNodes[$4_i].serialize($1_len);
if ($2_s.length > $1_len) {
break
}
}
$2_s += this.__LZlt + "/" + this.nodeName + this.__LZgt
} else {
$2_s += "/" + this.__LZgt
}
if ($2_s.length > $1_len) {
$2_s = Debug.abbreviate($2_s, $1_len)
}
return $2_s
}, _dbg_name: 
/* -*- file: data/LzDataElement.lzs#467.1 -*- */
function _dbg_name () {
return this.serializeInternal(Debug.printLength)
}, handleDocumentChange: 
/* -*- file: data/LzDataElement.lzs#479.1 -*- */
function handleDocumentChange ($1_what, $2_who, $3_type, $4_cobj) {
var $5_o = {who: $2_who, what: $1_what, type: $3_type}
if ($4_cobj) {
$5_o.cobj = $4_cobj
}
if (this.__LZchangeQ) {
this.__LZchangeQ.push($5_o)
} else {
if (this.onDocumentChange.ready) {
this.onDocumentChange.sendEvent($5_o)
}
}
}, toString: 
/* -*- file: data/LzDataElement.lzs#492.1 -*- */
function toString () {
var $1_r = this.serialize();
return $1_r
}, __LZdoLock: 
/* -*- file: data/LzDataElement.lzs#504.1 -*- */
function __LZdoLock ($1_locker) {
if (!this.__LZchangeQ) {
this.__LZchangeQ = [];
this.__LZlocker = $1_locker
}
}, __LZdoUnlock: 
/* -*- file: data/LzDataElement.lzs#515.1 -*- */
function __LZdoUnlock ($1_locker) {
if (this.__LZlocker != $1_locker) {
return
}
var $2_lzq = this.__LZchangeQ;
this.__LZchangeQ = null;
if ($2_lzq != null) {
for (var $3_i = 0;
$3_i < $2_lzq.length;
$3_i++) {
var $4_sendit = true;
var $5_tc = $2_lzq[$3_i];
for (var $6_j = 0;
$6_j < $3_i;
$6_j++) {
var $7_oc = $2_lzq[$6_j];
if ($5_tc.who == $7_oc.who && $5_tc.what == $7_oc.what && $5_tc.type == $7_oc.type) {
$4_sendit = false;
break
}
}
if ($4_sendit) {
this.handleDocumentChange($5_tc.what, $5_tc.who, $5_tc.type)
}
}
}
}}, {initialize: 
/* -*- file: data/LzDataElement.lzs#18.12 -*- */
function initialize ($1_prototype) {
if ($1_prototype.hasOwnProperty("setters")) {
$1_prototype.setters.attributes = "setAttrs";
$1_prototype.setters.childNodes = "setChildNodes";
$1_prototype.setters.nodeName = "setNodeName";
$1_prototype.setters.ownerDocument = "setOwnerDocument"
}
}, valueToElement: 
/* -*- file: data/LzDataElement.lzs#550.8 -*- */
function valueToElement ($1_o) {
var $2_n = new LzDataElement("element", {}, LzDataElementTrait.__LZv2E($1_o));
return $2_n
}, __LZv2E: 
/* -*- file: data/LzDataElement.lzs#560.8 -*- */
function __LZv2E ($1_o) {
var $2_type = typeof $1_o;
$2_type.toLowerCase();
var $3_c = [];
if ($2_type == "object") {
if ($1_o instanceof LzDataElement || $1_o instanceof LzDataText) {
$3_c[0] = $1_o
} else {
if ($1_o instanceof Date) {
$2_type = "date"
} else {
if ($1_o instanceof Array) {
$2_type = "array";
var $4_tag = $1_o.__LZtag != null ? $1_o.__LZtag : "item";
for (var $5_i = 0;
$5_i < $1_o.length;
$5_i++) {
var $6_tmpC = this.__LZv2E($1_o[$5_i]);
$3_c[$5_i] = new LzDataElement($4_tag, null, $6_tmpC)
}
} else {
$2_type = "struct";
var $5_i = 0;
for (var $7_k in $1_o) {
if ($7_k.indexOf("__LZ") == 0) {
continue
}
$3_c[$5_i++] = new LzDataElement($7_k, null, this.__LZv2E($1_o[$7_k]))
}
}
}
}
} else {
if ($1_o != null) {
$3_c[0] = new LzDataText($1_o)
}
}
if ($3_c.length == 0) {
$3_c = null
}
return $3_c
}});
(function  () {
var $lzsc$temp = function  () {
with (LzDataElementTrait) {
with (LzDataElementTrait.prototype) {
DeclareEvent(prototype, "onDocumentChange");
DeclareEvent(prototype, "onparentNode");
DeclareEvent(prototype, "onchildNode");
DeclareEvent(prototype, "onchildNodes");
DeclareEvent(prototype, "onattributes");
DeclareEvent(prototype, "onnodeName");
prototype.getAttr.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzDataElement.lzs#188.34 -*- */
function  ($1_who, $2_self) {
return [$2_self, "attributes"]
}
$lzsc$temp._dbg_name = "prototype.getAttr.dependencies";
return $lzsc$temp
})();
prototype.getFirstChild.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzDataElement.lzs#252.40 -*- */
function  ($1_who, $2_self) {
return [this, "childNodes"]
}
$lzsc$temp._dbg_name = "prototype.getFirstChild.dependencies";
return $lzsc$temp
})();
prototype.getLastChild.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzDataElement.lzs#266.39 -*- */
function  ($1_who, $2_self) {
return [this, "childNodes"]
}
$lzsc$temp._dbg_name = "prototype.getLastChild.dependencies";
return $lzsc$temp
})();
prototype.hasAttr.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzDataElement.lzs#285.34 -*- */
function  ($1_who, $2_self) {
return [$2_self, "attributes"]
}
$lzsc$temp._dbg_name = "prototype.hasAttr.dependencies";
return $lzsc$temp
})()
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzMiniNode = Class.make("LzMiniNode", null, {setAttribute: LzNode.prototype.setAttribute}, {initialize: 
/* -*- file: data/LzDataElement.lzs#613.10 -*- */
function initialize ($1_prototype) {
if (!$1_prototype.hasOwnProperty("setters")) {
$1_prototype["setters"] = new LzInheritedHash($1_prototype["setters"])
}
}});
var LzDataElement = Class.make("LzDataElement", [LzDataElementTrait, LzDataNode, LzMiniNode], {initialize: 
/* -*- file: data/LzDataElement.lzs#634.5 -*- */
function initialize ($1_name, $2_attributes, $3_children) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
this.nodeName = $1_name;
this.attributes = $2_attributes;
this.ownerDocument = this;
if ($3_children == null) {
this.setChildNodes([])
} else {
this.setChildNodes($3_children)
}
}, nodeName: null, nodeType: LzDataNode.ELEMENT_NODE, childNodes: null, attributes: null}, {makeNodeList: 
/* -*- file: data/LzDataElement.lzs#668.8 -*- */
function makeNodeList ($1_count, $2_name) {
var $3_a = [];
for (var $4_i = 0;
$4_i < $1_count;
$4_i++) {
$3_a[$4_i] = new LzDataElement($2_name, {}, null)
}
return $3_a
}, valueToElement: LzDataElementTrait.valueToElement});
var LzDataText = Class.make("LzDataText", [LzDataNode, LzMiniNode], {initialize: 
/* -*- file: data/LzDataText.lzs#24.1 -*- */
function initialize ($1_text) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
this.data = $1_text
}, nodeType: LzDataNode.TEXT_NODE, data: "", length: 0, setData: 
/* -*- file: data/LzDataText.lzs#55.1 -*- */
function setData ($1_newdata) {
this.data = $1_newdata;
if (this.ondata && this.ondata.ready) {
this.ondata.sendEvent($1_newdata)
}
if (this.ownerDocument) {
this.ownerDocument.handleDocumentChange("data", this, 1)
}
}, cloneNode: 
/* -*- file: data/LzDataText.lzs#68.1 -*- */
function cloneNode () {
var $1_n = new LzDataText(this.data);
return $1_n
}, serialize: 
/* -*- file: data/LzDataText.lzs#76.1 -*- */
function serialize () {
return this.__LZXMLescape(this.data)
}, toString: 
/* -*- file: data/LzDataText.lzs#83.1 -*- */
function toString () {
return this.data
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzDataText) {
with (LzDataText.prototype) {
DeclareEvent(LzDataText.prototype, "onDocumentChange");
DeclareEvent(LzDataText.prototype, "onparentNode");
DeclareEvent(LzDataText.prototype, "onchildNode");
DeclareEvent(LzDataText.prototype, "onchildNodes");
DeclareEvent(LzDataText.prototype, "onattributes");
DeclareEvent(LzDataText.prototype, "onnodeName");
setters.data = "setData"
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzDataRequest = Class.make("LzDataRequest", LzNode, {requestor: null, src: null, timeout: Infinity, status: null, rawdata: null, error: null, initialize: 
/* -*- file: data/LzDataRequest.lzs#48.5 -*- */
function initialize ($1_requestor) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
this.requestor = $1_requestor
}}, {tagname: "datarequest", SUCCESS: "success", TIMEOUT: "timeout", ERROR: "error", READY: "ready"});
(function  () {
var $lzsc$temp = function  () {
with (LzDataRequest) {
with (LzDataRequest.prototype) {
DeclareEvent(prototype, "onstatus")
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzDataProvider = Class.make("LzDataProvider", LzNode, {doRequest: 
/* -*- file: data/LzDataProvider.lzs#32.5 -*- */
function doRequest ($1_dreq) {

}}, {tagname: "dataprovider"});
var LzHTTPDataProvider = Class.make("LzHTTPDataProvider", LzDataProvider, {makeLoader: 
/* -*- file: data/LzHTTPDataProvider.lzs#29.5 -*- */
function makeLoader ($1_dreq) {
var $2_proxied = $1_dreq.proxied;
var $3_tloader = new LzHTTPLoader(this, $2_proxied);
$1_dreq.loader = $3_tloader;
$3_tloader.loadSuccess = this.loadSuccess;
$3_tloader.loadError = this.loadError;
$3_tloader.loadTimeout = this.loadTimeout;
$3_tloader.setProxied($2_proxied);
var $4_secure = "secure" in $1_dreq ? $1_dreq.secure : null;
if ($4_secure == null) {
if ($1_dreq.src.substring(0, 5) == "https") {
$4_secure = true
}
}
if ($4_secure) {
$3_tloader.baserequest = LzBrowser.getBaseURL($4_secure, $1_dreq.secureport)
}
$3_tloader.secure = $4_secure;
if ($4_secure) {
$3_tloader.secureport = $1_dreq.secureport
}
return $3_tloader
}, abortLoadForRequest: 
/* -*- file: data/LzHTTPDataProvider.lzs#71.5 -*- */
function abortLoadForRequest ($1_dreq) {
$1_dreq.loader.abort()
}, doRequest: 
/* -*- file: data/LzHTTPDataProvider.lzs#78.5 -*- */
function doRequest ($1_dreq) {
var $2_proxied = $1_dreq.proxied;
var $3_tloader = $1_dreq.loader;
if ($3_tloader == null || $1_dreq.multirequest == true || $1_dreq.queuerequests == true) {
$3_tloader = this.makeLoader($1_dreq)
}
$3_tloader.dataRequest = $1_dreq;
$3_tloader.setQueueing($1_dreq.queuerequests);
$3_tloader.setTimeout($1_dreq.timeout);
$3_tloader.setOption("cacheable", $1_dreq.cacheable == true);
$3_tloader.setOption("timeout", $1_dreq.timeout);
$3_tloader.setOption("trimwhitespace", $1_dreq.trimwhitespace == true);
$3_tloader.setOption("nsprefix", $1_dreq.nsprefix == true);
$3_tloader.setOption("sendheaders", $1_dreq.getresponseheaders == true);
if ($1_dreq.clientcacheable != null) {
$3_tloader.setOption("ccache", $1_dreq.clientcacheable)
}
var $4_headers = {}
var $5_headerparams = $1_dreq.requestheaders;
if ($5_headerparams != null) {
var $6_headernames = $5_headerparams.getNames();
for (var $7_i = 0;
$7_i < $6_headernames.length;
$7_i++) {
var $8_key = $6_headernames[$7_i];
var $9_val = $5_headerparams.getValue($8_key);
if ($2_proxied) {
$4_headers[$8_key] = $9_val
} else {
$3_tloader.setRequestHeader($8_key, $9_val)
}
}
}
var $10_qparams = $1_dreq.queryparams;
var $11_querystring = $1_dreq.querystring;
var $12_sep = "";
var $13_q = "?";
var $14_lzpostbody = $1_dreq.postbody;
if ($10_qparams != null) {
var $15_names = $10_qparams.getNames();
for (var $7_i in $15_names) {
var $16_name = $15_names[$7_i];
if (!$2_proxied && $16_name == "lzpostbody") {
$14_lzpostbody = $10_qparams.getValue($16_name)
} else {
$13_q += $12_sep + $16_name + "=" + encodeURIComponent($10_qparams.getValue($16_name));
$12_sep = "&"
}
}
}
if ($11_querystring != null && $11_querystring.length > 0) {
$13_q += $12_sep + $11_querystring
}
var $17_url = $1_dreq.src;
if ($13_q == "?") {

} else {
if ($17_url.indexOf("?") >= 0) {
$17_url = $17_url + "&" + $13_q.substring(1)
} else {
$17_url = $17_url + $13_q
}
}
if ($2_proxied) {
$17_url = $3_tloader.makeProxiedURL($17_url, $1_dreq.method, "xmldata", $4_headers)
} else {
if (!$1_dreq.clientcacheable) {
var $18_cachebreak = "__lzbc__=" + new Date().getTime();
if ($17_url.indexOf("?") >= 0) {
$17_url = $17_url + "&" + $18_cachebreak
} else {
$17_url = $17_url + "?" + $18_cachebreak
}
}
}
$1_dreq.status = "loading";
$3_tloader.open($2_proxied ? "POST" : $1_dreq.method, $17_url, null, null);
$3_tloader.send($14_lzpostbody)
}, loadSuccess: 
/* -*- file: data/LzHTTPDataProvider.lzs#189.5 -*- */
function loadSuccess ($1_loader, $2_data) {
var $3_dreq = $1_loader.dataRequest;
$3_dreq.status = LzDataRequest.SUCCESS;
$1_loader.owner.loadResponse($3_dreq, $2_data)
}, loadError: 
/* -*- file: data/LzHTTPDataProvider.lzs#195.5 -*- */
function loadError ($1_loader, $2_data) {
var $3_dreq = $1_loader.dataRequest;
$3_dreq.status = "error";
$1_loader.owner.loadResponse($3_dreq, $2_data)
}, loadTimeout: 
/* -*- file: data/LzHTTPDataProvider.lzs#201.5 -*- */
function loadTimeout ($1_loader, $2_data) {
var $3_dreq = $1_loader.dataRequest;
$3_dreq.status = LzDataRequest.TIMEOUT;
$1_loader.owner.loadResponse($3_dreq, $2_data)
}, setRequestError: 
/* -*- file: data/LzHTTPDataProvider.lzs#207.5 -*- */
function setRequestError ($1_dreq, $2_msg) {
$1_dreq.error = $2_msg;
$1_dreq.status = LzDataRequest.ERROR
}, loadResponse: 
/* -*- file: data/LzHTTPDataProvider.lzs#216.5 -*- */
function loadResponse ($1_dreq, $2_data) {
var $3_headers = new LzParam();
var $4_content = null;
if ($2_data == null) {
this.setRequestError($1_dreq, "client could not parse XML from server");
$1_dreq.onstatus.sendEvent($1_dreq);
return
}
var $5_proxied = $1_dreq.proxied;
if ($2_data.childNodes[0].nodeName == "error") {
this.setRequestError($1_dreq, $2_data.childNodes[0].attributes["msg"]);
$1_dreq.onstatus.sendEvent($1_dreq);
return
}
if ($5_proxied) {
var $6_hos = "childNodes" in $2_data.childNodes[1] ? $2_data.childNodes[1].childNodes : null;
if ($6_hos != null) {
for (var $7_i = 0;
$7_i < $6_hos.length;
$7_i++) {
var $8_h = $6_hos[$7_i];
if ($8_h.attributes) {
$3_headers.addValue($8_h.attributes.name, $8_h.attributes.value)
}
}
}
if ($2_data.childNodes[0].childNodes) {
$4_content = $2_data.childNodes[0].childNodes[0]
}
} else {
$4_content = $2_data
}
$1_dreq.xmldata = $4_content;
$1_dreq.responseheaders = $3_headers;
$1_dreq.rawdata = $1_dreq.loader.getResponse();
$1_dreq.onstatus.sendEvent($1_dreq)
}}, null);
var LzHTTPDataRequest = Class.make("LzHTTPDataRequest", LzDataRequest, {method: "GET", postbody: null, proxied: null, multirequest: false, queuerequests: false, queryparams: null, querystring: "", requestheaders: null, getresponsheaders: false, responseheaders: null, cacheable: false, clientcacheable: null, trimwhitespace: false, nsprefix: false, xmldata: null, loadtime: 0, secure: false, secureport: null, loader: null}, null);
var httpdataprovider = new LzHTTPDataProvider();
var defaultdataprovider = httpdataprovider;
var LzDataset = Class.make("LzDataset", [LzDataElementTrait, LzDataNode, LzNode], {dataprovider: defaultdataprovider, multirequest: false, dataRequest: null, dataRequestClass: LzHTTPDataRequest, dsloadDel: null, timeout: 60000, postbody: null, acceptencodings: false, params: null, nsprefix: false, getresponseheaders: false, querytype: "GET", trimwhitespace: false, nodeType: LzDataNode.DOCUMENT_NODE, cacheable: false, querystring: null, request: false, autorequest: false, headers: null, proxied: null, responseheaders: null, construct: 
/* -*- file: data/LzDataset.lzs#203.1 -*- */
function construct ($1_parent, $2_args) {
this.queuerequests = false;
this.oncanvas = $1_parent == canvas || $1_parent == null;
if (this._instanceAttrs["name"] == null) {
this._instanceAttrs["name"] = "localdata"
}
if ("port" in $2_args && $2_args.port && !("secureport" in $2_args && $2_args.secureport)) {
$2_args.secureport = $2_args.port
}
if ("querytype" in $2_args && $2_args.querytype) {
this.setQueryType($2_args.querytype)
}
if ("src" in $2_args && $2_args.src) {
this.src = $2_args.src
} else {
this.src = null
}
this.ownerDocument = this;
if ("timeout" in $2_args && $2_args.timeout) {
this.timeout = $2_args.timeout
} else {
this.timeout = canvas.dataloadtimeout
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments)
}, setName: 
/* -*- file: data/LzDataset.lzs#244.1 -*- */
function setName ($1_name) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["setName"] : this.nextMethod(arguments.callee, "setName")).apply(this, arguments);
this.nodeName = $1_name;
if (!("datasets" in canvas) || null == canvas.datasets) {
canvas.datasets = {}
}
if (this.oncanvas) {
if (global[$1_name] && global[$1_name] !== this) {
Debug.warn("Redefining #%s from %w to %w", $1_name, global[$1_name], this)
}
global[$1_name] = this;
canvas[$1_name] = this
} else {
$1_name = this.parent.getUID() + "." + $1_name
}
if (null != canvas.datasets[$1_name]) {
Debug.warn("A dataset already exists with the name '%s': %w", $1_name, canvas.datasets[$1_name])
}
canvas.datasets[$1_name] = this
}, destroy: 
/* -*- file: data/LzDataset.lzs#285.1 -*- */
function destroy ($1_recursiveCall) {
if (this.dsloadDel) {
this.dsloadDel.unregisterAll()
}
var $2_name = this.name;
if (this.oncanvas) {
if (canvas[$2_name] === this) {
delete canvas[$2_name]
}
if (global[$2_name] === this) {
delete global[$2_name]
}
} else {
$2_name = this.parent.getUID() + "." + $2_name
}
if (canvas.datasets[$2_name] === this) {
delete canvas.datasets[$2_name]
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["destroy"] : this.nextMethod(arguments.callee, "destroy")).apply(this, arguments)
}, getSrc: 
/* -*- file: data/LzDataset.lzs#312.1 -*- */
function getSrc () {
return this.src
}, getQueryString: 
/* -*- file: data/LzDataset.lzs#320.1 -*- */
function getQueryString () {
if (typeof this.querystring == "undefined") {
return ""
} else {
return this.querystring
}
}, getParams: 
/* -*- file: data/LzDataset.lzs#333.1 -*- */
function getParams () {
if (this.params == null) {
new LzParam(this, {name: "params"})
}
return this.params
}, getStatusCode: 
/* -*- file: data/LzDataset.lzs#347.1 -*- */
function getStatusCode () {
Debug.info("%s.%w is deprecated.  Use `onerror` and `ontimeout` events instead.", this, arguments.callee);
return 200
}, setData: 
/* -*- file: data/LzDataset.lzs#361.1 -*- */
function setData ($1_data, $2_headers) {
if ($1_data == null) {
return
}
if ($1_data instanceof Array) {
this.setChildNodes($1_data)
} else {
this.setChildNodes([$1_data])
}
this.data = $1_data;
if ("responseheaders" in this && this.responseheaders != null) {
this.responseheaders.destroy()
}
this.responseheaders = $2_headers;
if (this.ondata.ready) {
this.ondata.sendEvent(this)
}
}, gotError: 
/* -*- file: data/LzDataset.lzs#385.1 -*- */
function gotError ($1_e) {
this.errorstring = $1_e;
if (this.onerror.ready) {
this.onerror.sendEvent(this)
}
}, gotTimeout: 
/* -*- file: data/LzDataset.lzs#394.1 -*- */
function gotTimeout () {
if (this.ontimeout.ready) {
this.ontimeout.sendEvent(this)
}
}, getContext: 
/* -*- file: data/LzDataset.lzs#402.1 -*- */
function getContext () {
return this
}, getDataset: 
/* -*- file: data/LzDataset.lzs#410.1 -*- */
function getDataset () {
return this
}, getPointer: 
/* -*- file: data/LzDataset.lzs#418.1 -*- */
function getPointer () {
var $1_dp = new LzDatapointer(null);
$1_dp.p = this.getContext();
return $1_dp
}, setQueryString: 
/* -*- file: data/LzDataset.lzs#432.1 -*- */
function setQueryString ($1_s) {
this.params = null;
if (typeof $1_s == "object") {
if ($1_s instanceof LzParam) {
this.querystring = $1_s.toString()
} else {
var $2_p = new LzParam(this);
for (var $3_n in $1_s) {
$2_p.setValue($3_n, $1_s[$3_n], true)
}
this.querystring = $2_p.toString();
$2_p.destroy()
}
} else {
this.querystring = $1_s
}
if (this.autorequest) {
this.doRequest()
}
}, setSrc: 
/* -*- file: data/LzDataset.lzs#471.1 -*- */
function setSrc ($1_src) {
var $2_url = new LzURL($1_src);
this.querystring = $2_url.query;
$2_url.query = null;
this.src = $2_url.toString();
if (this.autorequest) {
this.doRequest()
}
}, setProxyRequests: 
/* -*- file: data/LzDataset.lzs#489.1 -*- */
function setProxyRequests ($1_val) {
if (typeof $1_val != "string") {
Debug.warn("arg '%w' to setProxyRequests must be a string with value 'inherit', 'true', or 'false'", $1_val)
} else {
this.proxied = $1_val
}
}, setRequest: 
/* -*- file: data/LzDataset.lzs#505.1 -*- */
function setRequest ($1_b) {
this.request = $1_b;
if (!$1_b) {
return
}
if (!this.isinited) {
this.reqOnInitDel = new LzDelegate(this, "doRequest", this, "oninit")
}
}, getLoadTime: 
/* -*- file: data/LzDataset.lzs#518.1 -*- */
function getLoadTime () {
Debug.info("%w.%s is deprecated.  Look at `%w.loadtime` object instead.", this, arguments.callee, this.dataRequest)
}, setQueryParam: 
/* -*- file: data/LzDataset.lzs#532.1 -*- */
function setQueryParam ($1_key, $2_val) {
this.querystring = null;
if (!this.params) {
new LzParam(this, {name: "params"})
}
this.params.setValue($1_key, $2_val);
if (this.autorequest) {
this.doRequest()
}
}, setQueryParams: 
/* -*- file: data/LzDataset.lzs#553.1 -*- */
function setQueryParams ($1_o) {
this.querystring = null;
if (!this.params) {
this.params = new LzParam(this, {name: "params"})
}
if ($1_o) {
this.params.addObject($1_o)
} else {
if ($1_o == null) {
this.params.remove()
}
}
if ($1_o && this.autorequest) {
this.doRequest()
}
}, isProxied: 
/* -*- file: data/LzDataset.lzs#579.1 -*- */
function isProxied () {
var $1_proxied_p = canvas.proxied;
if (this.proxied != null && this.proxied != "inherit") {
$1_proxied_p = this.proxied == "true"
}
if (this.proxied != null && this.proxied != "inherit") {
$1_proxied_p = this.proxied == "true"
}
return $1_proxied_p
}, doRequest: 
/* -*- file: data/LzDataset.lzs#597.1 -*- */
function doRequest () {
if (this.multirequest || this.dataRequest == null) {
this.dataRequest = new (this.dataRequestClass)(this)
}
var $1_dreq = this.dataRequest;
$1_dreq.src = this.src;
$1_dreq.timeout = this.timeout;
$1_dreq.status = $1_dreq.READY;
$1_dreq.method = this.querytype;
var $2_lzpostbody = null;
if (this.params) {
$2_lzpostbody = this.params.getValue("lzpostbody")
}
if ($2_lzpostbody != null) {
$1_dreq.postbody = $2_lzpostbody
}
$1_dreq.proxied = this.isProxied();
$1_dreq.queuerequests = this.queuerequests;
$1_dreq.queryparams = this.params;
$1_dreq.querystring = this.querystring;
$1_dreq.requestheaders = this.headers;
$1_dreq.getresponseheaders = this.getresponseheaders;
$1_dreq.secureport = this.secureport;
$1_dreq.cacheable = this.cacheable;
$1_dreq.clientcacheable = this.clientcacheable;
$1_dreq.trimwhitespace = this.trimwhitespace;
$1_dreq.nsprefix = this.nsprefix;
if (this.dsloadDel == null) {
this.dsloadDel = new LzDelegate(this, "handleDataResponse", $1_dreq, "onstatus")
} else {
this.dsloadDel.register($1_dreq, "onstatus")
}
this.dataprovider.doRequest($1_dreq)
}, handleDataResponse: 
/* -*- file: data/LzDataset.lzs#659.1 -*- */
function handleDataResponse ($1_datareq) {
if (this.dsloadDel != null) {
this.dsloadDel.unregisterFrom($1_datareq.onstatus)
}
if ($1_datareq.status == LzDataRequest.SUCCESS) {
this.setData($1_datareq.xmldata, $1_datareq.responseheaders)
} else {
if ($1_datareq.status == LzDataRequest.ERROR) {
this.gotError($1_datareq.error)
} else {
if ($1_datareq.status == LzDataRequest.TIMEOUT) {
this.gotTimeout()
}
}
}
}, setAutorequest: 
/* -*- file: data/LzDataset.lzs#683.1 -*- */
function setAutorequest ($1_b) {
this.autorequest = $1_b
}, getErrorString: 
/* -*- file: data/LzDataset.lzs#691.1 -*- */
function getErrorString () {
return this.errorstring
}, getRequestHeaderParams: 
/* -*- file: data/LzDataset.lzs#701.1 -*- */
function getRequestHeaderParams () {
return this.headers
}, clearRequestHeaderParams: 
/* -*- file: data/LzDataset.lzs#709.1 -*- */
function clearRequestHeaderParams () {
if (this.headers) {
this.headers.remove()
}
}, getResponseHeader: 
/* -*- file: data/LzDataset.lzs#724.1 -*- */
function getResponseHeader ($1_name) {
return this.dataRequest.responseheaders[$1_name]
}, setQueryType: 
/* -*- file: data/LzDataset.lzs#740.1 -*- */
function setQueryType ($1_reqtype) {
this.querytype = $1_reqtype.toUpperCase()
}, setPostBody: 
/* -*- file: data/LzDataset.lzs#748.1 -*- */
function setPostBody ($1_str) {
this.postbody = $1_str
}, abort: 
/* -*- file: data/LzDataset.lzs#755.1 -*- */
function abort () {
this.dataprovider.abortLoadForRequest(this.dataRequest)
}, getAllResponseHeaders: 
/* -*- file: data/LzDataset.lzs#765.1 -*- */
function getAllResponseHeaders () {
return this.responseheaders
}, setHeader: 
/* -*- file: data/LzDataset.lzs#774.1 -*- */
function setHeader ($1_k, $2_val) {
if (!this.headers) {
this.headers = new LzParam(this)
}
this.headers.setValue($1_k, $2_val)
}, setInitialData: 
/* -*- file: data/LzDataset.lzs#783.1 -*- */
function setInitialData ($1_d) {
if ($1_d != null) {
var $2_e = LzDataNode.stringToLzData($1_d, this.trimwhitespace, this.nsprefix);
this.setData($2_e.childNodes)
}
}, toString: 
/* -*- file: data/LzDataset.lzs#797.1 -*- */
function toString () {
return "LzDataset " + ":" + this.name
}}, {tagname: "dataset"});
(function  () {
var $lzsc$temp = function  () {
with (LzDataset) {
with (LzDataset.prototype) {
DeclareEvent(prototype, "ondata");
DeclareEvent(prototype, "onerror");
DeclareEvent(prototype, "ontimeout");
setters.data = "setData";
setters.src = "setSrc";
setters.autorequest = "setAutorequest";
setters.request = "setRequest";
setters.initialdata = "setInitialData"
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var __LzHttpDatasetPoolClass = Class.make("__LzHttpDatasetPoolClass", null, {_uid: 0, _p: [], get: 
/* -*- file: data/LzDataset.lzs#823.1 -*- */
function get ($1_dataDel, $2_errorDel, $3_timeoutDel, $4_acceptenc) {
var $5_d;
if (this._p.length > 0) {
$5_d = this._p[this._p.length - 1];
this._p.length = this._p.length - 1
} else {
$5_d = new LzDataset(null, {name: "LzHttpDatasetPool" + this._uid, type: "http", acceptencodings: $4_acceptenc ? $4_acceptenc : false});
this._uid++
}
if ($1_dataDel != null) {
$1_dataDel.register($5_d, "ondata")
}
if ($2_errorDel != null) {
$2_errorDel.register($5_d, "onerror")
}
if ($3_timeoutDel != null) {
$3_timeoutDel.register($5_d, "ontimeout")
}
return $5_d
}, recycle: 
/* -*- file: data/LzDataset.lzs#844.1 -*- */
function recycle ($1_d) {
$1_d.setQueryParams(null);
if ($1_d["ondata"]) {
$1_d.ondata.clearDelegates()
}
if ($1_d["ontimeout"]) {
$1_d.ontimeout.clearDelegates()
}
if ($1_d["onerror"]) {
$1_d.onerror.clearDelegates()
}
this._p[this._p.length] = $1_d
}}, null);
var LzHttpDatasetPool = new __LzHttpDatasetPoolClass();
var LzDatapointer = Class.make("LzDatapointer", LzNode, {p: null, data: null, context: null, __LZtracking: null, __LZtrackDel: null, rerunxpath: false, childOrSelf: 
/* -*- file: data/LzDatapointer.lzs#156.1 -*- */
function childOrSelf ($1_p) {
var $2_cp = this.p;
do{
if ($2_cp == $1_p) {
return true
}
$2_cp = $2_cp.$p
} while ($2_cp && $2_cp.$n <= this.p.$n)
}, gotError: 
/* -*- file: data/LzDatapointer.lzs#169.1 -*- */
function gotError ($1_msg) {
if (this.onerror.ready) {
this.onerror.sendEvent($1_msg)
}
}, gotTimeout: 
/* -*- file: data/LzDatapointer.lzs#177.1 -*- */
function gotTimeout ($1_msg) {
if (this.ontimeout.ready) {
this.ontimeout.sendEvent($1_msg)
}
}, getXPath: 
/* -*- file: data/LzDatapointer.lzs#193.1 -*- */
function getXPath ($1_p) {
if (!this.didGetXPathWarn) {
Debug.info("%w.%s is deprecated.  Use %w.%s or a $path{} constraint instead.", this, arguments.callee, this, this.xpathQuery)
}
this.didGetXPathWarn = true;
var $2_r = this.xpathQuery($1_p);
var $3_nr;
if ($2_r[0].nodeType) {
$3_nr = [];
for (var $4_i = 0;
$4_i < $2_r.length;
$4_i++) {
$3_nr[$4_i] = new LzDatapointer(null, {p: $2_r[$4_i]})
}
} else {
if ($2_r.nodeType) {
$3_nr = new LzDatapointer(null, {p: $2_r})
} else {
$3_nr = $2_r
}
}
return $3_nr
}, xpathQuery: 
/* -*- file: data/LzDatapointer.lzs#233.1 -*- */
function xpathQuery ($1_p) {
var $2_pp = this.parsePath($1_p);
var $3_nodes = this.__LZgetNodes($2_pp, $2_pp.context ? $2_pp.context : this.p);
if (!$3_nodes) {
return null
}
if ($2_pp.aggOperator != null) {
if ($2_pp.aggOperator == "last") {
return $3_nodes.length || this.__LZgetLast()
}
if ($2_pp.aggOperator == "position") {
if ($3_nodes.length) {
var $4_rarr = [];
for (var $5_i = 0;
$5_i < $3_nodes.length;
$5_i++) {
$4_rarr.push($5_i + 1)
}
return $4_rarr
} else {
return this.__LZgetPosition()
}
}
} else {
if ($2_pp.operator != null) {
if ($3_nodes.length) {
var $6_oarr = [];
for (var $5_i = 0;
$5_i < $3_nodes.length;
$5_i++) {
$6_oarr.push(this.__LZprocessOperator($3_nodes[$5_i], $2_pp))
}
return $6_oarr
} else {
return this.__LZprocessOperator($3_nodes, $2_pp)
}
} else {
return $3_nodes
}
}
}, setPointer: 
/* -*- file: data/LzDatapointer.lzs#287.1 -*- */
function setPointer ($1_p) {
if (!canvas.__LZoldOnData) {
this.setXPath(null);
if ($1_p != null) {
this.setDataContext($1_p.ownerDocument)
} else {
this.__LZsetTracking(null)
}
}
var $2_dc = this.data != $1_p;
var $3_pc = this.p != $1_p;
this.p = $1_p;
this.data = $1_p;
this.__LZsendUpdate($2_dc, $3_pc);
return $1_p != null
}, getDataset: 
/* -*- file: data/LzDatapointer.lzs#312.1 -*- */
function getDataset () {
if (this.p == null) {
if (this.context == this) {
return null
}
return this.context.getDataset()
}
return this.p.ownerDocument
}, setXPath: 
/* -*- file: data/LzDatapointer.lzs#334.1 -*- */
function setXPath ($1_p) {
var $2_hasxpath = $1_p;
if (!$2_hasxpath) {
this.xpath = null;
this.parsedPath = null;
if (this.p) {
this.__LZsetTracking(this.p.ownerDocument)
}
return
}
this.xpath = $1_p;
this.parsedPath = this.parsePath($1_p);
if (this.rerunxpath && this.parsedPath.hasDotDot && !this.parsedPath.context) {
this.__LZspecialDotDot = true
} else {
if (this.__LZdotdotCheckDel) {
this.__LZdotdotCheckDel.unregisterAll()
}
}
if (canvas.__LZoldOnData) {
if (this.parsedPath.context && !this.parsedPath.selectors.length && !this.rerunxpath) {
this.__LZspecialOndata = true
} else {
if (this.__LZspecialOndata) {
delete this.__LZspecialOndata
}
}
}
this.setDataContext(this.parsedPath.context);
return this.runXPath()
}, runXPath: 
/* -*- file: data/LzDatapointer.lzs#373.1 -*- */
function runXPath () {
if (!this.parsedPath) {
return
}
var $1_newc = null;
if (this.context && "getContext" in this.context) {
$1_newc = this.context.getContext()
}
if ($1_newc) {
var $2_n = this.__LZgetNodes(this.parsedPath, $1_newc, 0)
} else {
var $2_n = null
}
if (!$2_n) {
this.__LZHandleNoNodes();
return false
} else {
if ($2_n.length) {
this.__LZHandleMultiNodes($2_n);
return false
} else {
this.__LZHandleSingleNode($2_n);
return true
}
}
}, __LZsetupDotDot: 
/* -*- file: data/LzDatapointer.lzs#404.1 -*- */
function __LZsetupDotDot ($1_p) {
if (this.__LZlastdotdot != $1_p.ownerDocument) {
if (!this.__LZdotdotCheckDel) {
this.__LZdotdotCheckDel = new LzDelegate(this, "__LZcheckDotDot")
} else {
this.__LZdotdotCheckDel.unregisterAll()
}
this.__LZlastdotdot = $1_p.ownerDocument;
this.__LZdotdotCheckDel.register(this.__LZlastdotdot, "onDocumentChange")
}
}, __LZHandleSingleNode: 
/* -*- file: data/LzDatapointer.lzs#422.1 -*- */
function __LZHandleSingleNode ($1_n) {
if (this.__LZspecialDotDot) {
this.__LZsetupDotDot($1_n)
}
this.__LZupdateLocked = true;
this.__LZpchanged = $1_n != this.p;
this.p = $1_n;
this.__LZsetData();
this.__LZupdateLocked = false;
if (this.__LZspecialOndata) {
if ($1_n.childNodes.length) {
if (this.ondata && !this.__LZoldOndataWarn) {
Debug.info("Datapointer pointing to %w," + " relies on the ondata event from" + " a datapointer bound to the root node of a dataset." + " \n    This behavior is deprecated." + " Point the dataponter the first child " + " of the dataset, or use the dataset's ondata event.", this.context);
this.__LZoldOndataWarn = true
}
this.p = this.context;
if (this.ondata.ready) {
this.ondata.sendEvent(this.p)
}
}
return
}
this.__LZsendUpdate()
}, __LZHandleNoNodes: 
/* -*- file: data/LzDatapointer.lzs#462.1 -*- */
function __LZHandleNoNodes () {
var $1_pc = this.p != null;
var $2_dc = this.data != null;
this.p = null;
this.data = null;
this.__LZsendUpdate($2_dc, $1_pc)
}, __LZHandleMultiNodes: 
/* -*- file: data/LzDatapointer.lzs#473.1 -*- */
function __LZHandleMultiNodes ($1_n) {
Debug.error("%w matched %d nodes", this, $1_n.length);
this.__LZHandleNoNodes()
}, __LZsetData: 
/* -*- file: data/LzDatapointer.lzs#481.1 -*- */
function __LZsetData () {
if (this.parsedPath && this.parsedPath.aggOperator != null) {
if (this.parsedPath.aggOperator == "last") {
this.data = this.__LZgetLast();
this.__LZsendUpdate(true)
}
if (this.parsedPath.aggOperator == "position") {
this.data = this.__LZgetPosition();
this.__LZsendUpdate(true)
}
} else {
if (this.parsedPath && this.parsedPath.operator != null) {
this.__LZsimpleOperatorUpdate()
} else {
if (this.data != this.p) {
this.data = this.p;
this.__LZsendUpdate(true)
}
}
}
}, __LZgetLast: 
/* -*- file: data/LzDatapointer.lzs#501.1 -*- */
function __LZgetLast () {
if (this.context == this) {
return 1
}
return this.context.__LZgetLast() || 1
}, __LZgetPosition: 
/* -*- file: data/LzDatapointer.lzs#507.1 -*- */
function __LZgetPosition () {
if (this.context == this) {
return 1
}
return this.context.__LZgetPosition() || 1
}, __LZupdateLocked: false, __LZpchanged: false, __LZdchanged: false, __LZsendUpdate: 
/* -*- file: data/LzDatapointer.lzs#520.1 -*- */
function __LZsendUpdate ($1_upd, $2_upp) {
this.__LZdchanged = $1_upd || this.__LZdchanged;
this.__LZpchanged = $2_upp || this.__LZpchanged;
if (this.__LZupdateLocked) {
return false
}
if (this.__LZdchanged) {
if (this.ondata.ready) {
this.ondata.sendEvent(this.data)
}
this.__LZdchanged = false
}
if (this.__LZpchanged) {
if (this.onp.ready) {
this.onp.sendEvent(this.p)
}
this.__LZpchanged = false;
if (this.onDocumentChange.ready) {
this.onDocumentChange.sendEvent({who: this.p, type: 2, what: "context"})
}
}
return true
}, isValid: 
/* -*- file: data/LzDatapointer.lzs#549.1 -*- */
function isValid () {
return this.p != null
}, __LZsimpleOperatorUpdate: 
/* -*- file: data/LzDatapointer.lzs#554.1 -*- */
function __LZsimpleOperatorUpdate () {
var $1_ndat = this.__LZprocessOperator(this.p, this.parsedPath);
var $2_dchanged = false;
if (this.data != $1_ndat || this.parsedPath.operator == "attributes") {
this.data = $1_ndat;
$2_dchanged = true
}
this.__LZsendUpdate($2_dchanged)
}, ppcache: {}, parsePath: 
/* -*- file: data/LzDatapointer.lzs#578.1 -*- */
function parsePath ($1_pa) {
if ($1_pa instanceof LzDatapath) {
$1_pa = $1_pa.xpath
}
var $2_q = this.ppcache[$1_pa];
if ($2_q) {
var $3_l = $2_q["islocaldata"];
if ($3_l) {
$2_q.context = this.getLocalDataContext($3_l)
}
} else {
var $2_q = new LzParsedPath($1_pa, this);
this.ppcache[$1_pa] = $2_q
}
return $2_q
}, getLocalDataContext: 
/* -*- file: data/LzDatapointer.lzs#597.1 -*- */
function getLocalDataContext ($1_pp) {
var $2_n = this.parent;
if ($1_pp) {
var $3_a = $1_pp;
for (var $4_i = 0;
$4_i < $3_a.length;
$4_i++) {
if ($2_n) {
$2_n = $2_n[$3_a[$4_i]]
}
}
if ($2_n && $2_n instanceof LzDataset == false && $2_n["localdata"] instanceof LzDataset == true) {
$2_n = $2_n["localdata"]
}
}
if ($2_n != null && $2_n instanceof LzDataset) {
return $2_n
} else {
Debug.warn('local dataset "%w" not found in %w', $1_pp, this.parent)
}
}, pathSymbols: {"/": 1, "..": 2, "*": 3, ".": 4}, __LZgetNodes: 
/* -*- file: data/LzDatapointer.lzs#627.1 -*- */
function __LZgetNodes ($1_pathobj, $2_p, $3_startpoint) {
var $4_np;
if ($2_p == null) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return false
}
if ($1_pathobj.selectors != null) {
var $5_pathlen = $1_pathobj.selectors.length;
var $6_ignorenext = 0;
for (var $7_i = $3_startpoint ? $3_startpoint : 0;
$7_i < $5_pathlen;
$7_i++) {
var $8_cp = $1_pathobj.selectors[$7_i];
var $9_specialop = this.pathSymbols[$8_cp];
var $10_posnext = $1_pathobj.selectors[$7_i + 1];
if ($10_posnext && $10_posnext.pred == "range") {
var $11_range = $1_pathobj.selectors[++$7_i]
} else {
var $11_range = null
}
$4_np = null;
if (null != $8_cp.pred) {
if ($8_cp.pred == "hasattr") {
$2_p = $2_p.hasAttr($8_cp.attr) ? $2_p : false
} else {
if ($8_cp.pred == "attrval") {
if ($2_p.attributes != null) {
$2_p = $2_p.attributes[$8_cp.attr] == $8_cp.val ? $2_p : false
} else {
$2_p = false
}
}
}
} else {
if (!$9_specialop) {
$4_np = this.nodeByName($8_cp, $11_range, $2_p)
} else {
if ($9_specialop == 1) {
$2_p = $2_p.ownerDocument
} else {
if ($9_specialop == 2) {
$2_p = $2_p.parentNode
} else {
if ($9_specialop == 3) {
$4_np = [];
var $12_cnt = 0;
if ($2_p.childNodes) {
for (var $13_j = 0;
$13_j < $2_p.childNodes.length;
$13_j++) {
if ($2_p.childNodes[$13_j].nodeType == LzDataNode.ELEMENT_NODE) {
$12_cnt++;
if (!$11_range || $12_cnt >= $11_range[0]) {
$4_np.push($2_p.childNodes[$13_j])
}
if ($11_range && $12_cnt == $11_range[1]) {
break
}
}
}
}
}
}
}
}
}
if ($4_np != null) {
if ($4_np.length > 1) {
var $14_rval = [];
if ($7_i == $5_pathlen - 1) {
return $4_np
}
for (var $13_j = 0;
$13_j < $4_np.length;
$13_j++) {
var $15_r = this.__LZgetNodes($1_pathobj, $4_np[$13_j], $7_i + 1);
if ($15_r != null && $15_r.length > 0) {
for (var $16_n = 0;
$16_n < $15_r.length;
$16_n++) {
if ($15_r[$16_n]) {
$14_rval.push($15_r[$16_n])
}
}
} else {
if ($15_r) {
$14_rval.push($15_r)
}
}
}
if (!$14_rval.length) {
return null
} else {
if ($14_rval.length == 1) {
return $14_rval[0]
} else {
return $14_rval
}
}
} else {
$2_p = $4_np[0]
}
}
if (!$2_p) {
return false
}
}
}
return $2_p
}, getContext: 
/* -*- file: data/LzDatapointer.lzs#745.1 -*- */
function getContext () {
return this.p
}, nodeByName: 
/* -*- file: data/LzDatapointer.lzs#757.1 -*- */
function nodeByName ($1_name, $2_range, $3_p) {
var $4_o = [];
var $5_cnt = 0;
if (!$3_p) {
var $3_p = this.p;
if (!this.p) {
return null
}
}
if ($3_p.childNodes) {
for (var $6_i = 0;
$6_i < $3_p.childNodes.length;
$6_i++) {
if ($3_p.childNodes[$6_i].nodeName == $1_name) {
$5_cnt++;
if (!$2_range || $5_cnt >= $2_range[0]) {
$4_o.push($3_p.childNodes[$6_i])
}
if ($2_range && $5_cnt == $2_range[1]) {
break
}
}
}
}
return $4_o
}, __LZsetRerunXPath: 
/* -*- file: data/LzDatapointer.lzs#784.1 -*- */
function __LZsetRerunXPath ($1_rrx) {
this.rerunxpath = $1_rrx;
if (this.onrerunxpath.ready) {
this.onrerunxpath.sendEvent($1_rrx)
}
}, dupePointer: 
/* -*- file: data/LzDatapointer.lzs#797.1 -*- */
function dupePointer () {
var $1_dp = new LzDatapointer();
$1_dp.setFromPointer(this);
return $1_dp
}, __LZdoSelect: 
/* -*- file: data/LzDatapointer.lzs#804.1 -*- */
function __LZdoSelect ($1_selector, $2_amnt) {
$2_amnt = $2_amnt ? $2_amnt : 1;
var $3_np = this.p;
for (;
$3_np != null && $2_amnt > 0;
$2_amnt--) {
$3_np = $3_np[$1_selector]()
}
if ($3_np != null) {
this.setPointer($3_np);
return true
}
return false
}, selectNext: 
/* -*- file: data/LzDatapointer.lzs#824.1 -*- */
function selectNext ($1_amnt) {
return this.__LZdoSelect("getNextSibling", $1_amnt)
}, selectPrev: 
/* -*- file: data/LzDatapointer.lzs#835.1 -*- */
function selectPrev ($1_amnt) {
return this.__LZdoSelect("getPreviousSibling", $1_amnt)
}, selectChild: 
/* -*- file: data/LzDatapointer.lzs#846.1 -*- */
function selectChild ($1_amnt) {
return this.__LZdoSelect("getFirstChild", $1_amnt)
}, selectParent: 
/* -*- file: data/LzDatapointer.lzs#858.1 -*- */
function selectParent ($1_amnt) {
return this.__LZdoSelect("getParent", $1_amnt)
}, selectNextParent: 
/* -*- file: data/LzDatapointer.lzs#863.1 -*- */
function selectNextParent () {
var $1_op = this.p;
if (this.selectParent() && this.selectNext()) {
return true
} else {
this.setPointer($1_op);
return false
}
}, getNodeOffset: 
/* -*- file: data/LzDatapointer.lzs#879.1 -*- */
function getNodeOffset () {
Debug.info("%w.%s is deprecated.  Use XPath `position()` operator instead.", this, arguments.callee);
this.p.parentNode.__LZupdateCO();
return this.p.__LZo + 1
}, getNodeName: 
/* -*- file: data/LzDatapointer.lzs#892.1 -*- */
function getNodeName () {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
return this.p.nodeName
}, setNodeName: 
/* -*- file: data/LzDatapointer.lzs#906.1 -*- */
function setNodeName ($1_name) {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
this.p.setNodeName($1_name)
}, getNodeAttributes: 
/* -*- file: data/LzDatapointer.lzs#923.1 -*- */
function getNodeAttributes () {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
return this.p.attributes
}, getNodeAttribute: 
/* -*- file: data/LzDatapointer.lzs#938.1 -*- */
function getNodeAttribute ($1_name) {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
return this.p.attributes[$1_name]
}, setNodeAttribute: 
/* -*- file: data/LzDatapointer.lzs#954.1 -*- */
function setNodeAttribute ($1_name, $2_val) {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
this.p.setAttr($1_name, $2_val)
}, deleteNodeAttribute: 
/* -*- file: data/LzDatapointer.lzs#968.1 -*- */
function deleteNodeAttribute ($1_name) {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
this.p.removeAttr($1_name)
}, getNodeText: 
/* -*- file: data/LzDatapointer.lzs#985.1 -*- */
function getNodeText () {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
return this.p.__LZgetText()
}, getOtherNodeText: 
/* -*- file: data/LzDatapointer.lzs#1004.1 -*- */
function getOtherNodeText ($1_n) {
Debug.deprecated(this, arguments.callee, this.getNodeText);
var $2_s = "";
if ($1_n.c != null) {
var $3_nc = $1_n.c.length;
for (var $4_i = 0;
$4_i < $3_nc;
$4_i++) {
var $5_node = $1_n.c[$4_i];
if ($5_node.t != undefined) {
$2_s += $5_node.t
}
}
}
return $2_s
}, setNodeText: 
/* -*- file: data/LzDatapointer.lzs#1031.1 -*- */
function setNodeText ($1_val) {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
var $2_foundit = false;
for (var $3_i = 0;
$3_i < this.p.childNodes.length;
$3_i++) {
if (this.p.childNodes[$3_i].nodeType == LzDataNode.TEXT_NODE) {
this.p.childNodes[$3_i].setData($1_val);
$2_foundit = true;
break
}
}
if (!$2_foundit) {
this.p.appendChild(new LzDataText($1_val))
}
}, getNodeCount: 
/* -*- file: data/LzDatapointer.lzs#1059.1 -*- */
function getNodeCount () {
if (!this.p) {
return 0
}
return this.p.childNodes.length || 0
}, addNode: 
/* -*- file: data/LzDatapointer.lzs#1074.1 -*- */
function addNode ($1_name, $2_text, $3_attrs) {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
var $4_nn = new LzDataElement($1_name, $3_attrs);
if ($2_text != null) {
$4_nn.appendChild(new LzDataText($2_text))
}
this.p.appendChild($4_nn);
return $4_nn
}, deleteNode: 
/* -*- file: data/LzDatapointer.lzs#1098.1 -*- */
function deleteNode () {
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
var $1_op = this.p;
if (!this.rerunxpath) {
if (!this.selectNext()) {
this.__LZHandleNoNodes()
}
}
$1_op.parentNode.removeChild($1_op);
return $1_op
}, sendDataChange: 
/* -*- file: data/LzDatapointer.lzs#1122.1 -*- */
function sendDataChange ($1_chgpkg) {
this.getDataset().sendDataChange($1_chgpkg)
}, _openNode: "<", _closeNode: ">", _closeChar: "/", comparePointer: 
/* -*- file: data/LzDatapointer.lzs#1142.1 -*- */
function comparePointer ($1_ptr) {
return this.p == $1_ptr.p
}, addNodeFromPointer: 
/* -*- file: data/LzDatapointer.lzs#1152.1 -*- */
function addNodeFromPointer ($1_dp) {
if (!$1_dp.p) {
return
}
if (!this.p) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
var $2_n = $1_dp.p.cloneNode(true);
this.p.appendChild($2_n);
return new LzDatapointer(null, {pointer: $2_n})
}, setFromPointer: 
/* -*- file: data/LzDatapointer.lzs#1171.1 -*- */
function setFromPointer ($1_dp) {
this.setPointer($1_dp.p)
}, __LZprocessOperator: 
/* -*- file: data/LzDatapointer.lzs#1177.1 -*- */
function __LZprocessOperator ($1_p, $2_pp, $3_depends) {
if ($1_p == null) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
if ($2_pp.operatorArgs != null) {
return $1_p[$2_pp.operator]($2_pp.operatorArgs)
}
var $4_parts;
if ($2_pp.operator.indexOf("attributes.") == 0) {
$4_parts = ["attributes", $2_pp.operator.substr(11)]
} else {
$4_parts = $2_pp.operator.split(".")
}
var $5_val = $1_p;
for (var $6_i = 0;
$6_i < $4_parts.length;
$6_i++) {
var $7_pathElt = $4_parts[$6_i];
if ($5_val == null || !($7_pathElt in $5_val)) {
return
} else {
$5_val = $5_val[$7_pathElt]
}
}
return $5_val
}, makeRootNode: 
/* -*- file: data/LzDatapointer.lzs#1216.1 -*- */
function makeRootNode () {
return new LzDataElement("root")
}, finishRootNode: 
/* -*- file: data/LzDatapointer.lzs#1223.1 -*- */
function finishRootNode ($1_n) {
return $1_n.childNodes[0]
}, makeElementNode: 
/* -*- file: data/LzDatapointer.lzs#1229.1 -*- */
function makeElementNode ($1_attrs, $2_name, $3_par) {
var $4_tn = new LzDataElement($2_name, $1_attrs);
$3_par.appendChild($4_tn);
return $4_tn
}, makeTextNode: 
/* -*- file: data/LzDatapointer.lzs#1237.1 -*- */
function makeTextNode ($1_text, $2_par) {
var $3_tn = new LzDataText($1_text);
$2_par.appendChild($3_tn);
return $3_tn
}, serialize: 
/* -*- file: data/LzDatapointer.lzs#1267.1 -*- */
function serialize () {
if (this.p == null) {
Debug.info("%s: p is null in %s", arguments.callee, this);
return
}
return this.p.serialize()
}, _dbg_name: 
/* -*- file: data/LzDatapointer.lzs#1280.1 -*- */
function _dbg_name () {
return this.p._dbg_name()
}, setDataContext: 
/* -*- file: data/LzDatapointer.lzs#1287.1 -*- */
function setDataContext ($1_dc) {
if ($1_dc == null) {
this.context = this;
if (this.p) {
this.__LZsetTracking(this.p.ownerDocument)
}
} else {
if (this.context != $1_dc) {
this.context = $1_dc;
if (this.errorDel != null) {
this.errorDel.unregisterAll();
this.timeoutDel.unregisterAll()
}
this.__LZsetTracking($1_dc);
if (canvas.__LZoldOnData && !this.__LZspecialOndata) {
if (this.__LZoldDataDel) {
this.__LZoldDataDel.unregisterAll()
} else {
this.__LZoldDataDel = new LzDelegate(this, "__LZHandleDocChange")
}
this.__LZoldDataDel.register(this.context, "onDocumentChange")
}
var $2_hasxpath = this.xpath;
if ($2_hasxpath) {
if (this.errorDel == null) {
this.errorDel = new LzDelegate(this, "gotError");
this.timeoutDel = new LzDelegate(this, "gotTimeout")
}
this.errorDel.register($1_dc, "onerror");
this.timeoutDel.register($1_dc, "ontimeout")
}
}
}
}, __LZcheckChange: 
/* -*- file: data/LzDatapointer.lzs#1330.1 -*- */
function __LZcheckChange ($1_chgpkg) {
if (!this.rerunxpath) {
if (!this.p || $1_chgpkg.who == this.context) {
this.runXPath()
} else {
if (this.__LZneedsOpUpdate($1_chgpkg)) {
this.__LZsimpleOperatorUpdate()
}
}
} else {
if ($1_chgpkg.type == 2 || ($1_chgpkg.type == 0 || $1_chgpkg.type == 1 && this.parsedPath && this.parsedPath.hasOpSelector) && (this.parsedPath && this.parsedPath.hasDotDot || this.p == null || this.p.childOf($1_chgpkg.who))) {
this.runXPath();
return true
} else {
if (this.__LZneedsOpUpdate($1_chgpkg)) {
this.__LZsimpleOperatorUpdate()
}
}
return false
}
}, __LZneedsOpUpdate: 
/* -*- file: data/LzDatapointer.lzs#1360.1 -*- */
function __LZneedsOpUpdate ($1_chgpkg) {
return this.parsedPath && this.parsedPath.operator != null && (this.parsedPath.operator == "__LZgetText" ? $1_chgpkg.type == 0 && $1_chgpkg.who == this.p || $1_chgpkg.who.parentNode == this.p && $1_chgpkg.who.nodeType == LzDataNode.TEXT_NODE : $1_chgpkg.type == 1 && $1_chgpkg.who == this.p)
}, __LZHandleDocChange: 
/* -*- file: data/LzDatapointer.lzs#1385.1 -*- */
function __LZHandleDocChange ($1_chgpkg) {
var $2_who = $1_chgpkg.who;
if (!this.p) {
return false
}
var $3_meorbelowme = false;
var $4_track = $2_who;
var $5_i = 0;
var $6_sn = this.p;
do{
if ($4_track == $6_sn) {
$3_meorbelowme = true;
break
}
$4_track = $4_track.parentNode
} while ($4_track && $4_track != $2_who.ownerDocument);
if ($3_meorbelowme && this.ondata.ready) {
this.ondata.sendEvent(this.data)
}
return $3_meorbelowme
}, __LZcheckDotDot: 
/* -*- file: data/LzDatapointer.lzs#1405.1 -*- */
function __LZcheckDotDot ($1_chgpkg) {
var $2_who = $1_chgpkg.who;
if (($1_chgpkg.type == 0 || $1_chgpkg.type == 1 && this.parsedPath.hasOpSelector) && this.context.getContext().childOf($2_who)) {
this.runXPath()
}
}, destroy: 
/* -*- file: data/LzDatapointer.lzs#1424.1 -*- */
function destroy ($1_recur) {
this.__LZsetTracking(null);
if (this.errorDel) {
this.errorDel.unregisterAll()
}
if (this.timeoutDel) {
this.timeoutDel.unregisterAll()
}
if (this.__LZoldDataDel) {
this.__LZoldDataDel.unregisterAll()
}
if (this.__LZdotdotCheckDel) {
this.__LZdotdotCheckDel.unregisterAll()
}
delete this.p;
delete this.data;
delete this.__LZlastdotdot;
delete this.context;
delete this.__LZtracking;
(arguments.callee.superclass ? arguments.callee.superclass.prototype["destroy"] : this.nextMethod(arguments.callee, "destroy")).apply(this, arguments)
}, __LZsetTracking: 
/* -*- file: data/LzDatapointer.lzs#1454.1 -*- */
function __LZsetTracking ($1_who, $2_force) {
var $3_tracking = this.__LZtracking;
var $4_trackDel = this.__LZtrackDel;
if ($1_who) {
if ($3_tracking instanceof Array && $3_tracking.length > 1) {
Debug.error("%w.__LZtracking is %w, expecting an array of length 1", this, this.__LZtracking)
}
if ($3_tracking instanceof Array && $3_tracking.length == 1 && $3_tracking[0] === $1_who) {
return
}
if ($4_trackDel) {
$4_trackDel.unregisterAll()
}
var $5_hasxpath = $2_force || this.xpath;
if ($5_hasxpath) {
if (!$4_trackDel) {
this.__LZtrackDel = $4_trackDel = new LzDelegate(this, "__LZcheckChange")
}
this.__LZtracking = $3_tracking = [$1_who];
$4_trackDel.register($1_who, "onDocumentChange")
}
} else {
this.__LZtracking = [];
if ($4_trackDel) {
this.__LZtrackDel.unregisterAll()
}
}
}}, {tagname: "datapointer"});
(function  () {
var $lzsc$temp = function  () {
with (LzDatapointer) {
with (LzDatapointer.prototype) {
setters.xpath = "setXPath";
setters.context = "setDataContext";
setters.pointer = "setPointer";
setters.p = "setPointer";
prototype.defaultattrs = {}
prototype.defaultattrs.ignoreplacement = true;
setters.rerunxpath = "__LZsetRerunXPath";
DeclareEvent(prototype, "onp");
DeclareEvent(prototype, "onDocumentChange");
DeclareEvent(prototype, "ondata");
DeclareEvent(prototype, "onerror");
DeclareEvent(prototype, "ontimeout");
DeclareEvent(prototype, "onrerunxpath");
xpathQuery.dependencies = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzDatapointer.lzs#268.27 -*- */
function  ($1_who, $2_self, $3_p) {
if (this.parsePath) {
var $4_pp = this.parsePath($3_p);
return [$4_pp.hasDotDot ? $2_self.context.getContext().ownerDocument : $2_self, "DocumentChange"]
} else {
return [$2_self, "DocumentChange"]
}
}
$lzsc$temp._dbg_name = "xpathQuery.dependencies";
return $lzsc$temp
})();
getXPath.dependencies = xpathQuery.dependencies
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzParam = Class.make("LzParam", LzNode, {construct: 
/* -*- file: data/LzParam.lzs#53.1 -*- */
function construct ($1_parent, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.d = {}
}, parseQueryString: 
/* -*- file: data/LzParam.lzs#62.1 -*- */
function parseQueryString ($1_query) {
var $2_parameters = $1_query.split("&");
var $3_queries = {}
for (var $4_i in $2_parameters) {
var $5_key = $2_parameters[$4_i];
var $6_value = "";
var $7_n = $5_key.indexOf("=");
if ($7_n > 0) {
$6_value = unescape($5_key.substring($7_n + 1));
$5_key = $5_key.substring(0, $7_n)
}
$3_queries[$5_key] = $6_value
}
return $3_queries
}, createChildren: 
/* -*- file: data/LzParam.lzs#81.1 -*- */
function createChildren ($1_children) {

}, dlm: "&", sep: "=", addObject: 
/* -*- file: data/LzParam.lzs#93.1 -*- */
function addObject ($1_o) {
for (var $2_n in $1_o) {
this.setValue($2_n, $1_o[$2_n])
}
}, clone: 
/* -*- file: data/LzParam.lzs#103.1 -*- */
function clone ($1_o) {
var $1_o = new LzParam();
for (var $2_n in this.d) {
$1_o.d[$2_n] = this.d[$2_n].concat()
}
return $1_o
}, remove: 
/* -*- file: data/LzParam.lzs#117.1 -*- */
function remove ($1_name) {
if (null == $1_name) {
this.d = {}
} else {
if (null != this.d[$1_name]) {
var $2_i = this.findKey($1_name);
if ($2_i != null) {
this.d[$1_name].splice($2_i, 2)
}
}
}
}, setValue: 
/* -*- file: data/LzParam.lzs#135.1 -*- */
function setValue ($1_name, $2_val, $3_enc) {
if ($3_enc) {
$2_val = encodeURIComponent($2_val)
}
var $4_a = this.d[$1_name];
if ($4_a == null) {
this.d[$1_name] = [$1_name, $2_val]
} else {
var $5_i = this.findKey($1_name);
if (null != $5_i) {
$4_a[$5_i + 1] = $2_val
} else {
$4_a.push($1_name, $2_val)
}
}
}, addValue: 
/* -*- file: data/LzParam.lzs#160.1 -*- */
function addValue ($1_name, $2_val, $3_enc) {
if ($3_enc) {
$2_val = encodeURIComponent($2_val)
}
var $4_a = this.d[$1_name];
if ($4_a == null) {
this.d[$1_name] = [$1_name, $2_val]
} else {
$4_a.push($1_name, $2_val)
}
}, getValue: 
/* -*- file: data/LzParam.lzs#178.1 -*- */
function getValue ($1_name) {
var $2_i = this.findKey($1_name);
if (null != $2_i) {
return this.d[$1_name][$2_i + 1]
}
}, getValueNoCase: 
/* -*- file: data/LzParam.lzs#191.1 -*- */
function getValueNoCase ($1_name) {
var $2_a = this.d[$1_name];
if ($2_a.length) {
if ($2_a.length == 2) {
return $2_a[1]
} else {
var $3_r = [];
for (var $4_i = 1;
$4_i < $2_a.length;
$4_i += 2) {
$3_r.push($2_a[$4_i])
}
return $3_r
}
}
}, getNames: 
/* -*- file: data/LzParam.lzs#211.1 -*- */
function getNames () {
var $1_o = [];
for (var $2_n in this.d) {
var $3_a = this.d[$2_n];
for (var $4_i = 0;
$4_i < $3_a.length;
$4_i += 2) {
if (null != $3_a[$4_i]) {
$1_o.push($3_a[$4_i])
}
}
}
return $1_o
}, findKey: 
/* -*- file: data/LzParam.lzs#231.1 -*- */
function findKey ($1_name) {
var $2_a = this.d[$1_name];
if ($2_a != null) {
for (var $3_i = 0;
$3_i < $2_a.length;
$3_i += 2) {
if ($1_name == $2_a[$3_i]) {
return $3_i
}
}
}
}, setDelimiter: 
/* -*- file: data/LzParam.lzs#249.1 -*- */
function setDelimiter ($1_d) {
var $2_o = this.dlm;
this.dlm = $1_d;
return $2_o
}, setSeparator: 
/* -*- file: data/LzParam.lzs#263.1 -*- */
function setSeparator ($1_s) {
var $2_o = this.sep;
this.sep = $1_s;
return $2_o
}, toString: 
/* -*- file: data/LzParam.lzs#274.1 -*- */
function toString () {
return this.serialize()
}, serialize: 
/* -*- file: data/LzParam.lzs#285.1 -*- */
function serialize ($1_sep, $2_delim, $3_uriencode) {
var $1_sep = $1_sep == null ? this.sep : $1_sep;
var $4_dlm = $2_delim == null ? this.dlm : $2_delim;
var $5_o = "";
var $6_c = false;
for (var $7_mk in this.d) {
var $8_n = this.d[$7_mk];
if ($8_n != null) {
for (var $9_i = 0;
$9_i < $8_n.length;
$9_i += 2) {
if ($6_c) {
$5_o += $4_dlm
}
$5_o += $8_n[$9_i] + $1_sep;
$5_o += $3_uriencode ? encodeURIComponent($8_n[$9_i + 1]) : $8_n[$9_i + 1];
$6_c = true
}
}
}
return $5_o
}}, {tagname: "params"});
(function  () {
var $lzsc$temp = function  () {
with (LzParam) {
with (LzParam.prototype) {
setters.$hasdefaultattrs = -1
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
LzParam.prototype.toString = LzParam.prototype.serialize;
var LzParsedPath = Class.make("LzParsedPath", null, {initialize: 
/* -*- file: data/LzParsedPath.lzs#16.3 -*- */
function initialize ($1_pa, $2_node) {
this.path = $1_pa;
this.selectors = [];
var $3_nowarn = false;
var $4_sourceindex = $1_pa.indexOf(":/");
if ($4_sourceindex > -1) {
var $5_sourceparts = $1_pa.substring(0, $4_sourceindex).split(":");
if ($5_sourceparts.length > 1) {
var $6_dsrc = LzParsedPath.trim($5_sourceparts[0]);
var $7_dset = LzParsedPath.trim($5_sourceparts[1]);
if ($6_dsrc == "local") {
$3_nowarn = true;
this.islocaldata = $7_dset.split(".");
this.context = $2_node.getLocalDataContext(this.islocaldata)
} else {
this.context = canvas[$6_dsrc][$7_dset]
}
} else {
var $8_name = LzParsedPath.trim($5_sourceparts[0]);
if ($8_name == "new") {
this.context = new AnonDatasetGenerator(this)
} else {
this.context = canvas.datasets[$8_name]
}
}
if (this.context == null && $3_nowarn != true) {
Debug.error("couldn't find dataset for %w", $1_pa)
}
var $9_rest = $1_pa.substring($4_sourceindex + 2)
} else {
var $9_rest = $1_pa
}
var $10_nodes = [];
var $11_currnode = "";
var $12_instring = false;
var $13_escape = false;
for (var $14_i = 0;
$14_i < $9_rest.length;
$14_i++) {
var $15_c = $9_rest.charAt($14_i);
if ($15_c == "\\" && $13_escape == false) {
$13_escape = true;
continue
} else {
if ($13_escape == true) {
$13_escape = false;
$11_currnode += $15_c;
continue
} else {
if ($12_instring == false && $15_c == "/") {
$10_nodes.push($11_currnode);
$11_currnode = "";
continue
} else {
if ($15_c == "'") {
$12_instring = $12_instring ? false : true
}
}
}
}
$11_currnode += $15_c
}
$10_nodes.push($11_currnode);
if ($10_nodes != null) {
for (var $14_i = 0;
$14_i < $10_nodes.length;
$14_i++) {
var $16_cnode = LzParsedPath.trim($10_nodes[$14_i]);
if ($14_i == $10_nodes.length - 1) {
if ($16_cnode.charAt(0) == "@") {
this.hasAttrOper = true;
if ($16_cnode.charAt(1) == "*") {
this.operator = "attributes"
} else {
this.operator = "attributes." + $16_cnode.substring(1, $16_cnode.length)
}
continue
} else {
if ($16_cnode.charAt($16_cnode.length - 1) == ")") {
if ($16_cnode.indexOf("last") > -1) {
this.aggOperator = "last"
} else {
if ($16_cnode.indexOf("position") > -1) {
this.aggOperator = "position"
} else {
if ($16_cnode.indexOf("name") > -1) {
this.operator = "nodeName"
} else {
if ($16_cnode.indexOf("text") > -1) {
this.operator = "__LZgetText";
this.operatorArgs = 0
} else {
if ($16_cnode.indexOf("serialize") > -1) {
this.operator = "serialize";
this.operatorArgs = 0
} else {
this.gotError("Unknown operator: " + $16_cnode)
}
}
}
}
}
continue
} else {
if ($16_cnode == "") {
continue
}
}
}
}
var $17_preds = $16_cnode.split("[");
var $18_n = LzParsedPath.trim($17_preds[0]);
this.selectors.push($18_n == "" ? "/" : $18_n);
if ($18_n == "" || $18_n == "..") {
this.hasDotDot = true
}
if ($17_preds != null) {
for (var $19_j = 1;
$19_j < $17_preds.length;
$19_j++) {
var $20_pred = LzParsedPath.trim($17_preds[$19_j]);
$20_pred = $20_pred.substring(0, $20_pred.length - 1);
if (LzParsedPath.trim($20_pred).charAt(0) == "@") {
var $21_attrpred = $20_pred.split("=");
var $22_a;
var $23_tattr = $21_attrpred[0].substring(1);
if ($21_attrpred.length > 1) {
var $24_aval = LzParsedPath.trim($21_attrpred[1]);
$24_aval = $24_aval.substring(1, $24_aval.length - 1);
$22_a = {pred: "attrval", attr: LzParsedPath.trim($23_tattr), val: LzParsedPath.trim($24_aval)}
} else {
$22_a = {pred: "hasattr", attr: LzParsedPath.trim($23_tattr)}
}
this.selectors.push($22_a);
this.hasOpSelector = true
} else {
var $22_a = $20_pred.split("-");
$22_a[0] = LzParsedPath.trim($22_a[0]);
if ($22_a[0] == "") {
$22_a[0] = 1
}
if ($22_a[1] != null) {
$22_a[1] = LzParsedPath.trim($22_a[1])
}
if ($22_a[1] == "") {
$22_a[1] = Infinity
} else {
if ($22_a.length == 1) {
$22_a[1] = $22_a[0]
}
}
$22_a.pred = "range";
this.selectors.push($22_a)
}
}
}
}
}
}, operatorArgs: null, toString: 
/* -*- file: data/LzParsedPath.lzs#235.1 -*- */
function toString () {
return "Parsed path for path: " + this.path
}, debugWrite: 
/* -*- file: data/LzParsedPath.lzs#242.1 -*- */
function debugWrite () {
Debug.write(this);
Debug.write("  c:" + this.context + "|");
Debug.write("  n:" + this.selectors.join("|") + "|");
Debug.write("  d:" + this.operator + "|");
Debug.write("  ")
}}, {trim: 
/* -*- file: data/LzParsedPath.lzs#215.8 -*- */
function trim ($1_s) {
var $2_st = 0;
var $3_dotrim = false;
while ($1_s.charAt($2_st) == " ") {
$2_st++;
$3_dotrim = true
}
var $4_len = $1_s.length - $2_st;
while ($1_s.charAt($2_st + $4_len - 1) == " ") {
$4_len--;
$3_dotrim = true
}
return $3_dotrim ? $1_s.substr($2_st, $4_len) : $1_s
}});
var AnonDatasetGenerator = Class.make("AnonDatasetGenerator", null, {initialize: 
/* -*- file: data/LzParsedPath.lzs#263.1 -*- */
function initialize ($1_pp) {
this.pp = $1_pp
}, getContext: 
/* -*- file: data/LzParsedPath.lzs#270.1 -*- */
function getContext () {
var $1_d = new LzDataset();
var $2_dp = $1_d.getPointer();
if (this.pp.selectors != null) {
for (var $3_i = 0;
$3_i < this.pp.selectors.length;
$3_i++) {
if (this.pp.selectors[$3_i] == "/") {
continue
}
$2_dp.addNode(this.pp.selectors[$3_i]);
$2_dp.selectChild()
}
}
return $1_d
}, noncontext: true}, null);
var LzDatapath = Class.make("LzDatapath", LzDatapointer, {datacontrolsvisibility: true, __LZtakeDPSlot: true, pooling: null, replication: null, sortpath: null, sortorder: null, construct: 
/* -*- file: data/LzDatapath.lzs#147.1 -*- */
function construct ($1_v, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
if (!("data" in this.immediateparent)) {
this.immediateparent.data = null
}
if ($2_args.datacontrolsvisibility != null) {
this.datacontrolsvisibility = $2_args.datacontrolsvisibility
}
if (this.__LZtakeDPSlot) {
this.immediateparent.datapath = this;
var $3_pdp = null;
var $4_tarr = null;
if ("searchParents" in this.immediateparent) {
$3_pdp = this.immediateparent.searchParents("datapath").datapath;
$4_tarr = $3_pdp.__LZdepChildren
}
if ($4_tarr != null) {
$3_pdp.__LZdepChildren = [];
for (var $5_i = $4_tarr.length - 1;
$5_i >= 0;
$5_i--) {
var $6_c = $4_tarr[$5_i];
if ($6_c != this && !$6_c.$pathbinding && $6_c.immediateparent != this.immediateparent && $6_c.immediateparent.childOf(this.immediateparent)) {
$6_c.setDataContext(this, true)
} else {
$3_pdp.__LZdepChildren.push($6_c)
}
}
}
}
}, __LZHandleMultiNodes: 
/* -*- file: data/LzDatapath.lzs#193.1 -*- */
function __LZHandleMultiNodes ($1_nodes) {
var $2_clonetype;
switch (this.replication) {
case "lazy":
$2_clonetype = LzLazyReplicationManager;
break;
case "resize":
$2_clonetype = LzResizeReplicationManager;
break;
default:
$2_clonetype = LzReplicationManager;
break;

}
this.storednodes = $1_nodes;
var $3_rman = new $2_clonetype(this, this._instanceAttrs);
delete this.storednodes;
return $3_rman
}, setNodes: 
/* -*- file: data/LzDatapath.lzs#235.1 -*- */
function setNodes ($1_nodes) {
var $2_rman = this.__LZHandleMultiNodes($1_nodes);
if (!$2_rman) {
$2_rman = this
}
$2_rman.__LZsetTracking(null);
if ($1_nodes) {
for (var $3_i = 0;
$3_i < $1_nodes.length;
$3_i++) {
var $4_n = $1_nodes[$3_i];
var $5_own = $4_n.ownerDocument;
$2_rman.__LZsetTracking($5_own, true, $4_n != $5_own)
}
}
}, __LZsendUpdate: 
/* -*- file: data/LzDatapath.lzs#262.1 -*- */
function __LZsendUpdate ($1_upd, $2_upp) {
var $3_pchg = this.__LZpchanged;
if (!(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZsendUpdate"] : this.nextMethod(arguments.callee, "__LZsendUpdate")).apply(this, arguments)) {
return false
}
if (this.immediateparent.isinited) {
this.__LZApplyData($3_pchg)
} else {
this.__LZneedsUpdateAfterInit = true
}
return true
}, __LZApplyDataOnInit: 
/* -*- file: data/LzDatapath.lzs#281.1 -*- */
function __LZApplyDataOnInit () {
if (this.__LZneedsUpdateAfterInit) {
this.__LZApplyData();
delete this.__LZneedsUpdateAfterInit
}
}, __LZApplyData: 
/* -*- file: data/LzDatapath.lzs#291.1 -*- */
function __LZApplyData ($1_force) {
var $2_ip = this.immediateparent;
if (this.datacontrolsvisibility) {
this.immediateparent.__LZvizDat = this.p != null;
if ("__LZupdateShown" in this.immediateparent) {
this.immediateparent.__LZupdateShown()
}
}
var $3_cdat = $1_force || $2_ip.data != this.data || this.parsedPath && this.parsedPath.operator == "attributes";
this.data = this.data == null ? null : this.data;
$2_ip.data = this.data;
if ($3_cdat && $2_ip.ondata.ready) {
$2_ip.ondata.sendEvent(this.data)
}
if (this.parsedPath && (this.parsedPath.operator != null || this.parsedPath.aggOperator != null) && $3_cdat) {
if ($2_ip.applyData) {
$2_ip.applyData(this.data)
}
}
}, setDataContext: 
/* -*- file: data/LzDatapath.lzs#323.1 -*- */
function setDataContext ($1_p, $2_implicit) {
if ($1_p == null && this.immediateparent != null && "searchParents" in this.immediateparent) {
$1_p = this.immediateparent.searchParents("datapath").datapath;
$2_implicit = true
}
if ($1_p == this.context) {
return
}
if ($2_implicit) {
if ($1_p.__LZdepChildren == null) {
$1_p.__LZdepChildren = [this]
} else {
$1_p.__LZdepChildren.push(this)
}
} else {
var $3_dclist = null;
if (this.context) {
$3_dclist = this.context.__LZdepChildren
}
if ($3_dclist) {
for (var $4_i = 0;
$4_i < $3_dclist.length;
$4_i++) {
if ($3_dclist[$4_i] == this) {
$3_dclist.splice($4_i, 1);
break
}
}
}
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["setDataContext"] : this.nextMethod(arguments.callee, "setDataContext")).call(this, $1_p)
}, destroy: 
/* -*- file: data/LzDatapath.lzs#361.1 -*- */
function destroy ($1_recur) {
this.setName = null;
this.__LZupdateLocked = true;
if (this.context && !this.context.__LZdeleted && this.context.__LZdepChildren) {
var $2_dca = this.context.__LZdepChildren;
if ($2_dca != null) {
for (var $3_i = 0;
$3_i < $2_dca.length;
$3_i++) {
if ($2_dca[$3_i] == this) {
$2_dca.splice($3_i, 1);
break
}
}
}
}
if (!this.immediateparent.__LZdeleted) {
if (this.__LZdepChildren != null && this.__LZdepChildren.length) {
var $4_dnpar = this.immediateparent.searchParents("datapath").datapath;
for (var $3_i = 0;
$3_i < this.__LZdepChildren.length;
$3_i++) {
this.__LZdepChildren[$3_i].setDataContext($4_dnpar, true)
}
}
}
if (this.immediateparent.datapath == this) {
delete this.immediateparent.datapath
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["destroy"] : this.nextMethod(arguments.callee, "destroy")).apply(this, arguments)
}, updateData: 
/* -*- file: data/LzDatapath.lzs#423.1 -*- */
function updateData () {
if (!arguments[0] && this.p) {
this.p.__LZlockFromUpdate(this)
}
var $1_ppdo = this.parsedPath ? this.parsedPath.operator : null;
if (this.immediateparent.updateData && $1_ppdo != null) {
var $2_dat = this.immediateparent.updateData();
if ($1_ppdo == "nodeName") {
this.setNodeName($2_dat)
} else {
if ($1_ppdo == "__LZgetText") {
this.setNodeText($2_dat)
} else {
if ($1_ppdo == "attributes") {
this.p.setAttrs($2_dat)
} else {
this.setNodeAttribute($1_ppdo.substring(11), $2_dat)
}
}
}
}
if (this.__LZdepChildren != null) {
for (var $3_i = 0;
$3_i < this.__LZdepChildren.length;
$3_i++) {
this.__LZdepChildren[$3_i].updateData(true)
}
}
if (!arguments[0] && this.p) {
this.p.__LZunlockFromUpdate(this)
}
}, retrieveData: 
/* -*- file: data/LzDatapath.lzs#460.1 -*- */
function retrieveData () {
Debug.deprecated(this, arguments.callee, this.updateData);
return this.updateData()
}, __LZHandleDocChange: 
/* -*- file: data/LzDatapath.lzs#470.1 -*- */
function __LZHandleDocChange ($1_who) {
if ((arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZHandleDocChange"] : this.nextMethod(arguments.callee, "__LZHandleDocChange")).apply(this, arguments)) {
if (this.immediateparent.ondata.ready) {
this.immediateparent.ondata.sendEvent(this.data)
}
if (this.onDocumentChange.ready) {
this.onDocumentChange.sendEvent($1_who)
}
}
}, toString: 
/* -*- file: data/LzDatapath.lzs#481.1 -*- */
function toString () {
return "Datapath for " + this.immediateparent
}, _dpevents: ["ondata", "onerror", "ontimeout"], __LZcheckChange: 
/* -*- file: data/LzDatapath.lzs#497.1 -*- */
function __LZcheckChange ($1_chgpkg) {
if (!(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZcheckChange"] : this.nextMethod(arguments.callee, "__LZcheckChange")).apply(this, arguments)) {
if ($1_chgpkg.who.childOf(this.p, true) && this.onDocumentChange.ready) {
this.onDocumentChange.sendEvent($1_chgpkg)
}
}
}, __LZsetTracking: 
/* -*- file: data/LzDatapath.lzs#517.1 -*- */
function __LZsetTracking ($1_who, $2_additive, $3_needscheck) {
var $4_tracking;
var $5_trackDel;
if (!$1_who || !$2_additive) {
return (arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZsetTracking"] : this.nextMethod(arguments.callee, "__LZsetTracking")).call(this, $1_who, true)
}
$4_tracking = this.__LZtracking;
$5_trackDel = this.__LZtrackDel;
if (!($4_tracking instanceof Array)) {
Debug.error("%w.__LZtracking is %w, expecting an array", this, $4_tracking)
}
if ($3_needscheck) {
var $6_l = $4_tracking.length;
for (var $7_i = 0;
$7_i < $6_l;
$7_i++) {
if ($4_tracking[$7_i] === $1_who) {
return
}
}
}
if (!$5_trackDel) {
this.__LZtrackDel = $5_trackDel = new LzDelegate(this, "__LZcheckChange")
}
$4_tracking.push($1_who);
$5_trackDel.register($1_who, "onDocumentChange")
}, __LZisclone: false, __LZsetCloneManager: 
/* -*- file: data/LzDatapath.lzs#563.1 -*- */
function __LZsetCloneManager ($1_m) {
this.__LZisclone = true;
this.immediateparent.cloneManager = $1_m;
this.parsedPath = $1_m.parsedPath;
this.xpath = $1_m.xpath;
this.setDataContext($1_m)
}, setClonePointer: 
/* -*- file: data/LzDatapath.lzs#575.1 -*- */
function setClonePointer ($1_p) {
var $2_pc = this.p != $1_p;
this.p = $1_p;
if ($2_pc) {
if ($1_p && this.sel != $1_p.sel) {
this.sel = $1_p.sel || false;
this.immediateparent.setSelected(this.sel)
}
this.__LZpchanged = true;
this.__LZsetData()
}
}, setOrder: 
/* -*- file: data/LzDatapath.lzs#598.1 -*- */
function setOrder ($1_xpath, $2_comparator) {
if (this.__LZisclone) {
this.immediateparent.cloneManager.setOrder($1_xpath, $2_comparator)
} else {
this.sortpath = $1_xpath;
if ($2_comparator || $2_comparator != null) {
this.sortorder = $2_comparator
}
}
}, setComparator: 
/* -*- file: data/LzDatapath.lzs#619.1 -*- */
function setComparator ($1_comparator) {
if (this.__LZisclone) {
this.immediateparent.cloneManager.setComparator($1_comparator)
} else {
this.sortorder = $1_comparator
}
}, setSelected: 
/* -*- file: data/LzDatapath.lzs#631.1 -*- */
function setSelected ($1_torf) {
this.p.sel = $1_torf;
this.sel = $1_torf;
this.immediateparent.setSelected($1_torf)
}, __LZgetLast: 
/* -*- file: data/LzDatapath.lzs#640.1 -*- */
function __LZgetLast () {
if (this.__LZisclone) {
return this.context.nodes.length
} else {
if (this.p == this.context.getContext() && this.context.__LZgetLast) {
return this.context.__LZgetLast()
}
}
return 1
}, __LZgetPosition: 
/* -*- file: data/LzDatapath.lzs#654.1 -*- */
function __LZgetPosition () {
if (this.__LZisclone) {
return this.immediateparent.clonenumber + 1
} else {
if (this.p == this.context.getContext() && this.context.__LZgetPosition) {
return this.context.__LZgetPosition()
}
}
return 1
}}, {tagname: "datapath"});
(function  () {
var $lzsc$temp = function  () {
with (LzDatapath) {
with (LzDatapath.prototype) {
prototype.rerunxpath = true;
setters.__LZmanager = "__LZsetCloneManager"
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzReplicationManager = Class.make("LzReplicationManager", LzDatapath, {pooling: false, asyncnew: true, datacontrolsvisibility: false, __LZtakeDPSlot: false, visible: true, __LZpreventXPathUpdate: false, nodes: null, clones: null, __LZdataoffset: 0, construct: 
/* -*- file: data/LzReplicationManager.lzs#121.1 -*- */
function construct ($1_odp, $2_args) {
var $3_view = $1_odp.immediateparent;
this.datapath = this;
var $4_name = $3_view._instanceAttrs.name;
$2_args.name = $4_name;
delete $3_view.immediateparent[$4_name];
delete $3_view.parent[$4_name];
var $5_id = $3_view._instanceAttrs.id;
$2_args.id = $5_id;
if (global[$5_id] instanceof LzNode) {
global[$5_id] = null
}
$2_args.xpath = LzNode._ignoreAttribute;
if ($1_odp.sortpath != null) {
$2_args.sortpath = $1_odp.sortpath
}
if ($1_odp.sortorder != null || $1_odp.sortorder) {
$2_args.sortorder = $1_odp.sortorder
}
this.initialnodes = $1_odp.storednodes;
if ($1_odp.__LZspecialDotDot) {
this.__LZspecialDotDot = true;
if ($1_odp.__LZdotdotCheckDel) {
$1_odp.__LZdotdotCheckDel.unregisterAll()
}
delete $1_odp.__LZspecialDotDot
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).call(this, $3_view.parent, $2_args);
if ($3_view.parent != $3_view.immediateparent) {
$3_view.immediateparent[$2_args.name] = this
}
this.xpath = $1_odp.xpath;
this.parsedPath = $1_odp.parsedPath;
this.cloneClass = $3_view.constructor;
this.cloneParent = $3_view.parent;
this.cloneAttrs = new LzInheritedHash($3_view._instanceAttrs);
this.cloneAttrs.datapath = LzNode._ignoreAttribute;
this.cloneAttrs.$datapath = {name: "datapath"}
this.cloneAttrs.$datapath.attrs = {datacontrolsvisibility: $1_odp.datacontrolsvisibility, __LZmanager: this}
this.cloneAttrs.id = LzNode._ignoreAttribute;
this.cloneAttrs.name = LzNode._ignoreAttribute;
var $6_hadxpathconstraint = false;
if ($3_view._instanceAttrs.$refs && $3_view._instanceAttrs.$refs.datapath) {
this.cloneAttrs.$refs = new LzInheritedHash(this.cloneAttrs.$refs);
this.cloneAttrs.$refs.datapath = LzNode._ignoreAttribute;
var $7_cons = $3_view._instanceAttrs.$refs.datapath;
this._t = $7_cons.dependencies;
$6_hadxpathconstraint = true;
this.__LZpreventXPathUpdate = true;
this.applyConstraint("xpath", $7_cons, this._t());
this.__LZpreventXPathUpdate = false
} else {
if ($1_odp._instanceAttrs.$refs && $1_odp._instanceAttrs.$refs.xpath) {
var $7_cons = $1_odp._instanceAttrs.$refs.xpath;
this._t = $7_cons.dependencies;
$6_hadxpathconstraint = true;
this.__LZpreventXPathUpdate = true;
this.applyConstraint("xpath", $7_cons, this._t());
this.__LZpreventXPathUpdate = false
}
}
if (this.__LZsetCloneAttrs) {
this.__LZsetCloneAttrs()
}
if ($3_view._instanceChildren) {
this.cloneChildren = $3_view._instanceChildren.concat()
} else {
this.cloneChildren = []
}
this.visible = $1_odp.datacontrolsvisibility || (!$3_view.isinited && "visible" in $3_view._instanceAttrs ? $3_view._instanceAttrs.visible : $3_view.visible);
if ($2_args.pooling != null) {
this.pooling = $2_args.pooling
}
var $8_mycontext = $1_odp.context;
this.clones = [];
this.clonePool = [];
if (this.pooling) {
$1_odp.__LZsetCloneManager(this);
if ($6_hadxpathconstraint) {
$1_odp.setXPath = LzReplicationManager.__LZemptyFuntion
}
this.clones.push($3_view);
$3_view.immediateparent.addSubview($3_view)
} else {
this.destroyClone($3_view)
}
this.setDataContext($8_mycontext, $8_mycontext instanceof LzDatapointer)
}, constructWithArgs: 
/* -*- file: data/LzReplicationManager.lzs#281.1 -*- */
function constructWithArgs ($1_args) {
this.__LZHandleMultiNodes(this.initialnodes);
delete this.initialnodes;
if (this.visible == false) {
this.setVisible(false)
}
}, getNodeOffset: 
/* -*- file: data/LzReplicationManager.lzs#292.1 -*- */
function getNodeOffset ($1_p) {
if (this.nodes != null) {
var $2_l = this.nodes.length;
for (var $3_i = 0;
$3_i < $2_l;
$3_i++) {
if ($1_p == this.nodes[$3_i]) {
return $3_i
}
}
}
}, getCloneNumber: 
/* -*- file: data/LzReplicationManager.lzs#308.1 -*- */
function getCloneNumber ($1_n) {
return this.clones[$1_n]
}, __LZHandleNoNodes: 
/* -*- file: data/LzReplicationManager.lzs#315.1 -*- */
function __LZHandleNoNodes ($1_n) {
this.nodes = [];
while (this.clones.length) {
if (this.pooling) {
this.poolClone()
} else {
var $2_v = this.clones.pop();
this.destroyClone($2_v)
}
}
}, __LZHandleSingleNode: 
/* -*- file: data/LzReplicationManager.lzs#330.1 -*- */
function __LZHandleSingleNode ($1_n) {
this.__LZHandleMultiNodes([$1_n])
}, mergesort: 
/* -*- file: data/LzReplicationManager.lzs#337.1 -*- */
function mergesort ($1_arr, $2_l, $3_h) {
if ($2_l < $3_h) {
var $4_m = $2_l + Math.floor(($3_h - $2_l) / 2);
var $5_a = this.mergesort($1_arr, $2_l, $4_m);
var $6_b = this.mergesort($1_arr, $4_m + 1, $3_h)
} else {
if ($1_arr.length == 0) {
return []
} else {
return [$1_arr[$2_l]]
}
}
var $7_r = [];
var $8_ia = 0;
var $9_ib = 0;
var $10_al = $5_a.length;
var $11_bl = $6_b.length;
while ($8_ia < $10_al && $9_ib < $11_bl) {
if (this.orderf($6_b[$9_ib], $5_a[$8_ia]) == 1) {
$7_r.push($6_b[$9_ib++])
} else {
$7_r.push($5_a[$8_ia++])
}
}
while ($8_ia < $10_al) {
$7_r.push($5_a[$8_ia++])
}
while ($9_ib < $11_bl) {
$7_r.push($6_b[$9_ib++])
}
return $7_r
}, __LZHandleMultiNodes: 
/* -*- file: data/LzReplicationManager.lzs#371.1 -*- */
function __LZHandleMultiNodes ($1_n) {
var $2_layouts = this.parent && this.parent.layouts ? this.parent.layouts : [];
for (var $3_i in $2_layouts) {
$2_layouts[$3_i].lock()
}
this.hasdata = true;
var $4_lastnodes = this.nodes;
this.nodes = $1_n;
if (this.onnodes.ready) {
this.onnodes.sendEvent(this.nodes)
}
if (this.__LZspecialDotDot) {
this.__LZsetupDotDot($1_n[0])
}
if (this.orderpath != null) {
this.nodes = this.mergesort(this.nodes, 0, this.nodes.length - 1)
}
this.__LZadjustVisibleClones($4_lastnodes, true);
var $5_l = this.clones.length;
for (var $3_i = 0;
$3_i < $5_l;
$3_i++) {
var $6_cl = this.clones[$3_i];
var $7_iplusoffset = $3_i + this.__LZdataoffset;
$6_cl.clonenumber = $7_iplusoffset;
if (this.nodes) {
$6_cl.datapath.setClonePointer(this.nodes[$7_iplusoffset])
}
if ($6_cl.onclonenumber.ready) {
$6_cl.onclonenumber.sendEvent($7_iplusoffset)
}
}
if (this.onclones.ready) {
this.onclones.sendEvent(this.clones)
}
for (var $3_i in $2_layouts) {
$2_layouts[$3_i].unlock()
}
}, __LZadjustVisibleClones: 
/* -*- file: data/LzReplicationManager.lzs#409.1 -*- */
function __LZadjustVisibleClones ($1_lastnodes, $2_newnodes) {
var $3_stpt = this.__LZdiffArrays($1_lastnodes, this.nodes);
if (!this.pooling) {
while (this.clones.length > $3_stpt) {
var $4_v = this.clones.pop();
this.destroyClone($4_v)
}
}
LzInstantiator.enableDataReplicationQueuing();
while (this.nodes.length > this.clones.length) {
this.clones.push(this.getNewClone())
}
LzInstantiator.clearDataReplicationQueue();
while (this.nodes.length < this.clones.length) {
this.poolClone()
}
}, orderf: 
/* -*- file: data/LzReplicationManager.lzs#439.1 -*- */
function orderf ($1_a, $2_b) {
var $3_op = arguments.callee.op;
this.p = $1_a;
var $4_aa = this.xpathQuery($3_op);
this.p = $2_b;
var $5_bb = this.xpathQuery($3_op);
delete this.p;
if ($4_aa == null || $4_aa == "") {
$4_aa = "\n"
}
if ($5_bb == null || $5_bb == "") {
$5_bb = "\n"
}
return arguments.callee.comp($4_aa, $5_bb)
}, ascDict: 
/* -*- file: data/LzReplicationManager.lzs#463.1 -*- */
function ascDict ($1_a, $2_b) {
if ($1_a.toLowerCase() < $2_b.toLowerCase()) {
return 1
}
}, descDict: 
/* -*- file: data/LzReplicationManager.lzs#470.1 -*- */
function descDict ($1_a, $2_b) {
if ($1_a.toLowerCase() > $2_b.toLowerCase()) {
return 1
}
}, setOrder: 
/* -*- file: data/LzReplicationManager.lzs#483.1 -*- */
function setOrder ($1_xpath, $2_comparator) {
this.orderpath = null;
$2_comparator = $2_comparator == "sortpath" ? null : $2_comparator;
if ($2_comparator || $2_comparator != null || typeof this.orderf.comp != "function") {
this.setComparator($2_comparator)
}
this.orderpath = $1_xpath;
this.orderf.op = this.orderpath;
if (this.nodes.length) {
this.__LZHandleMultiNodes(this.nodes)
}
}, setComparator: 
/* -*- file: data/LzReplicationManager.lzs#518.1 -*- */
function setComparator ($1_comparator) {
if (typeof $1_comparator != "function") {
if ($1_comparator == "descending") {
$1_comparator = this.descDict
} else {
$1_comparator = this.ascDict
}
}
this.orderf.comp = $1_comparator;
if (this.orderpath != null && this.nodes.length) {
this.__LZHandleMultiNodes(this.nodes)
}
}, getNewClone: 
/* -*- file: data/LzReplicationManager.lzs#538.1 -*- */
function getNewClone ($1_forceNew) {
if (this.clonePool.length) {
var $2_v = this.reattachClone(this.clonePool.pop())
} else {
var $2_v = new (this.cloneClass)(this.cloneParent, this.cloneAttrs, this.cloneChildren, $1_forceNew == null ? this.asyncnew : !$1_forceNew)
}
if (this.visible == false) {
$2_v.setVisible(false)
}
return $2_v
}, poolClone: 
/* -*- file: data/LzReplicationManager.lzs#553.1 -*- */
function poolClone () {
var $1_v = this.clones.pop();
this.detachClone($1_v);
this.clonePool.push($1_v)
}, checkDependentContexts: null, handleModify: null, destroyClone: 
/* -*- file: data/LzReplicationManager.lzs#568.1 -*- */
function destroyClone ($1_v) {
$1_v.sprite.destroy(true);
$1_v.destroy()
}, setDatapath: 
/* -*- file: data/LzReplicationManager.lzs#580.1 -*- */
function setDatapath ($1_xp) {
this.setXPath($1_xp)
}, setXPath: 
/* -*- file: data/LzReplicationManager.lzs#587.1 -*- */
function setXPath ($1_xp) {
if (this.__LZpreventXPathUpdate) {
return
}
(arguments.callee.superclass ? arguments.callee.superclass.prototype["setXPath"] : this.nextMethod(arguments.callee, "setXPath")).apply(this, arguments)
}, handleDeletedNode: 
/* -*- file: data/LzReplicationManager.lzs#595.1 -*- */
function handleDeletedNode ($1_c) {
var $2_tclone = this.clones[$1_c];
if (this.pooling) {
this.detachClone($2_tclone);
this.clonePool.push($2_tclone)
} else {
this.destroyClone($2_tclone)
}
this.nodes.splice($1_c, 1);
this.clones.splice($1_c, 1)
}, getCloneForNode: 
/* -*- file: data/LzReplicationManager.lzs#612.1 -*- */
function getCloneForNode ($1_p) {
var $2_l = this.clones.length;
for (var $3_i = 0;
$3_i < $2_l;
$3_i++) {
if (this.clones[$3_i].datapath.p == $1_p) {
return this.clones[$3_i]
}
}
}, toString: 
/* -*- file: data/LzReplicationManager.lzs#624.1 -*- */
function toString () {
return "ReplicationManager in " + this.immediateparent
}, setVisible: 
/* -*- file: data/LzReplicationManager.lzs#632.1 -*- */
function setVisible ($1_vis) {
this.visible = $1_vis;
var $2_l = this.clones.length;
for (var $3_i = 0;
$3_i < $2_l;
$3_i++) {
this.clones[$3_i].setVisible($1_vis)
}
if (this.onvisible.ready) {
this.onvisible.sendEvent($1_vis)
}
}, __LZHandleDocChange: 
/* -*- file: data/LzReplicationManager.lzs#644.1 -*- */
function __LZHandleDocChange ($1_who) {
this.p = this.context.getContext();
(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZHandleDocChange"] : this.nextMethod(arguments.callee, "__LZHandleDocChange")).apply(this, arguments);
delete this.p
}, __LZcheckChange: 
/* -*- file: data/LzReplicationManager.lzs#654.1 -*- */
function __LZcheckChange ($1_chgpkg) {
this.p = this.nodes[0];
var $2_didrun = (arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZcheckChange"] : this.nextMethod(arguments.callee, "__LZcheckChange")).apply(this, arguments);
delete this.p;
if (!$2_didrun) {
var $3_who = $1_chgpkg.who;
var $4_l = this.clones.length;
for (var $5_i = 0;
$5_i < $4_l;
$5_i++) {
var $6_cl = this.clones[$5_i];
if ($6_cl.datapath.__LZneedsOpUpdate($1_chgpkg)) {
$6_cl.datapath.__LZsetData()
}
if ($3_who.childOf($6_cl.datapath.p, true)) {
if ($6_cl.datapath.onDocumentChange.ready) {
$6_cl.datapath.onDocumentChange.sendEvent($1_chgpkg)
}
}
}
}
}, __LZneedsOpUpdate: 
/* -*- file: data/LzReplicationManager.lzs#676.1 -*- */
function __LZneedsOpUpdate () {
return false
}, getContext: 
/* -*- file: data/LzReplicationManager.lzs#681.1 -*- */
function getContext ($1_chgpkg) {
return this.nodes[0]
}, detachClone: 
/* -*- file: data/LzReplicationManager.lzs#688.1 -*- */
function detachClone ($1_cl) {
if ($1_cl.isdetatchedclone) {
return
}
$1_cl.setVisible(false);
$1_cl.addedToParent = false;
var $2_svs = $1_cl.immediateparent.subviews;
for (var $3_i = $2_svs.length - 1;
$3_i >= 0;
$3_i--) {
if ($2_svs[$3_i] == $1_cl) {
$2_svs.splice($3_i, 1);
break
}
}
$1_cl.datapath.__LZtrackDel.unregisterAll();
if ($1_cl.immediateparent.onremovesubview.ready) {
$1_cl.immediateparent.onremovesubview.sendEvent($1_cl)
}
$1_cl.isdetatchedclone = true;
$1_cl.p = null
}, reattachClone: 
/* -*- file: data/LzReplicationManager.lzs#711.1 -*- */
function reattachClone ($1_cl) {
if (!$1_cl.isdetatchedclone) {
return $1_cl
}
$1_cl.immediateparent.addSubview($1_cl);
$1_cl.setVisible(this.visible);
$1_cl.isdetatchedclone = false;
return $1_cl
}, __LZdiffArrays: 
/* -*- file: data/LzReplicationManager.lzs#724.1 -*- */
function __LZdiffArrays ($1_a, $2_b) {
var $3_i = 0;
var $4_al = $1_a ? $1_a.length : 0;
var $5_bl = $2_b ? $2_b.length : 0;
while ($3_i < $4_al && $3_i < $5_bl) {
if ($1_a[$3_i] != $2_b[$3_i]) {
return $3_i
}
$3_i++
}
return $3_i
}, updateData: 
/* -*- file: data/LzReplicationManager.lzs#744.1 -*- */
function updateData ($1_a, $2_b) {
var $3_l = this.clones.length;
for (var $4_i = 0;
$4_i < $3_l;
$4_i++) {
this.clones[$4_i].datapath.updateData()
}
}}, {__LZemptyFuntion: 
/* -*- file: data/LzReplicationManager.lzs#274.8 -*- */
function __LZemptyFuntion () {
return
}});
(function  () {
var $lzsc$temp = function  () {
with (LzReplicationManager) {
with (LzReplicationManager.prototype) {
setters.sortpath = "setOrder";
setters.sortorder = "setComparator";
setters.datapath = "setXPath";
DeclareEvent(prototype, "onnodes");
DeclareEvent(prototype, "onclones");
DeclareEvent(prototype, "onvisible")
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzDatasource = Class.make("LzDatasource", LzNode, {proxied: null, loadSuccess: 
/* -*- file: data/LzDatasource.lzs#54.1 -*- */
function loadSuccess ($1_loader, $2_data) {
$1_loader.dataset.gotRawData($2_data)
}, loadError: 
/* -*- file: data/LzDatasource.lzs#55.1 -*- */
function loadError ($1_loader, $2_data) {
$1_loader.dataset.gotError($2_data)
}, loadTimeout: 
/* -*- file: data/LzDatasource.lzs#56.1 -*- */
function loadTimeout ($1_loader, $2_data) {
$1_loader.dataset.gotTimeout($2_data)
}, getLoaderForDataset: 
/* -*- file: data/LzDatasource.lzs#66.1 -*- */
function getLoaderForDataset ($1_dataset, $2_proxied) {
var $3_tloader = $1_dataset.getOption("dsloader");
if (!$3_tloader) {
$3_tloader = new LzHTTPLoader(this, $2_proxied, $1_dataset);
$1_dataset.setOption("dsloader", $3_tloader);
$3_tloader.setQueueing($1_dataset.queuerequests);
$3_tloader.loadSuccess = this.loadSuccess;
$3_tloader.loadError = this.loadError;
$3_tloader.loadTimeout = this.loadTimeout
}
if (typeof $1_dataset.timeout != "undefined" && $1_dataset.timeout != null) {
$3_tloader.setTimeout($1_dataset.timeout)
}
$3_tloader.setProxied($2_proxied);
var $4_secure = "secure" in $1_dataset ? $1_dataset.secure : null;
if ($4_secure == null) {
if (this.src.substring(0, 5) == "https") {
$4_secure = true
}
}
if ($4_secure) {
$3_tloader.baserequest = LzBrowser.getBaseURL($4_secure, $1_dataset.secureport)
}
$3_tloader.secure = $4_secure;
if ($4_secure) {
$3_tloader.secureport = $1_dataset.secureport
}
return $3_tloader
}, abortLoadForDataset: 
/* -*- file: data/LzDatasource.lzs#111.1 -*- */
function abortLoadForDataset ($1_forset) {
$1_forset.getOption("dsloader").abort()
}, getLoadTimeForDataset: 
/* -*- file: data/LzDatasource.lzs#120.1 -*- */
function getLoadTimeForDataset ($1_forset) {
return $1_forset.getOption("dsloader").getLastLoadtime()
}, toString: 
/* -*- file: data/LzDatasource.lzs#130.1 -*- */
function toString () {
return "LzDatasource '" + this.name + "'"
}}, null);
var LzHTTPDatasource = Class.make("LzHTTPDatasource", LzDatasource, {reqtype: "GET", isProxied: 
/* -*- file: data/LzHTTPDatasource.lzs#40.1 -*- */
function isProxied ($1_forset) {
var $2_proxied_p = canvas.proxied;
if (this.proxied != null && this.proxied != "inherit") {
$2_proxied_p = this.proxied == "true"
}
if ($1_forset.proxied != null && $1_forset.proxied != "inherit") {
$2_proxied_p = $1_forset.proxied == "true"
}
return $2_proxied_p
}, doRequest: 
/* -*- file: data/LzHTTPDatasource.lzs#57.1 -*- */
function doRequest ($1_forset) {
var $2_proxied = this.isProxied($1_forset);
var $3_tloader = this.getLoaderForDataset($1_forset, $2_proxied);
$3_tloader.setOption("cacheable", $1_forset.cacheable == true);
if ($1_forset.clientcacheable != null) {
$3_tloader.setOption("ccache", $1_forset.clientcacheable)
}
$3_tloader.setOption("timeout", $1_forset.timeout);
$3_tloader.setOption("trimwhitespace", $1_forset.trimwhitespace == true);
$3_tloader.setOption("nsprefix", $1_forset.nsprefix == true);
$3_tloader.setOption("sendheaders", $1_forset.getresponseheaders == true);
var $4_headers = {}
var $5_headerparams = $1_forset.getRequestHeaderParams();
if ($5_headerparams != null) {
var $6_headernames = $5_headerparams.getNames();
for (var $7_i = 0;
$7_i < $6_headernames.length;
$7_i++) {
var $8_key = $6_headernames[$7_i];
var $9_val = $5_headerparams.getValue($8_key);
if ($2_proxied) {
$4_headers[$8_key] = $9_val
} else {
$3_tloader.setRequestHeader($8_key, $9_val)
}
}
}
$1_forset.clearRequestHeaderParams();
var $10_qparams = $1_forset.getParams();
var $11_querystring = $1_forset.getQueryString();
var $12_sep = "";
var $13_q = "?";
var $14_lzpostbody = null;
var $15_names = $10_qparams.getNames();
for (var $7_i in $15_names) {
var $16_name = $15_names[$7_i];
if (!$2_proxied && $16_name == "lzpostbody") {
$14_lzpostbody = $10_qparams.getValue($16_name)
} else {
$13_q += $12_sep + $16_name + "=" + encodeURIComponent($10_qparams.getValue($16_name));
$12_sep = "&"
}
}
if ($11_querystring != null && $11_querystring.length > 0) {
if ($13_q.length > 0) {
$13_q += $12_sep + $11_querystring
} else {
$13_q = $11_querystring
}
}
var $17_url = this.src;
if ($13_q == "?") {

} else {
if ($17_url.indexOf("?") >= 0) {
$17_url = $17_url + "&" + $13_q
} else {
$17_url = $17_url + $13_q
}
}
if ($2_proxied) {
$17_url = $3_tloader.makeProxiedURL($17_url, this.reqtype, "xmldata", $4_headers)
} else {
if (!$1_forset.clientcacheable) {
var $18_cachebreak = "__lzbc__=" + new Date().getTime();
if ($17_url.indexOf("?") >= 0) {
$17_url = $17_url + "&" + $18_cachebreak
} else {
$17_url = $17_url + "?" + $18_cachebreak
}
}
}
$3_tloader.open($2_proxied ? "POST" : this.reqtype, $17_url, null, null);
$3_tloader.send($14_lzpostbody)
}, setQueryType: 
/* -*- file: data/LzHTTPDatasource.lzs#161.1 -*- */
function setQueryType ($1_d) {
this.reqtype = $1_d
}, processRawData: 
/* -*- file: data/LzHTTPDatasource.lzs#169.1 -*- */
function processRawData ($1_forset, $2_d) {
var $3_o = new LzParam($1_forset);
var $4_content = null;
if ($2_d == null) {
$1_forset.gotError("client could not parse XML from server");
return
}
$1_forset.rawtext = $2_d.rawtext;
var $5_proxied = this.isProxied($1_forset);
if ($2_d.childNodes[0].nodeName == "error") {
$1_forset.gotError($2_d.childNodes[0].attributes["msg"]);
return
}
if ($5_proxied) {
var $6_hos = "childNodes" in $2_d.childNodes[1] ? $2_d.childNodes[1].childNodes : null;
if ($6_hos != null) {
for (var $7_i = 0;
$7_i < $6_hos.length;
$7_i++) {
var $8_h = $6_hos[$7_i];
if ($8_h.attributes) {
$3_o.addValue($8_h.attributes.name, $8_h.attributes.value)
}
}
}
if ($2_d.childNodes[0].childNodes) {
$4_content = $2_d.childNodes[0].childNodes[0]
}
} else {
$4_content = $2_d
}
$1_forset.setData($4_content, $3_o)
}}, {tagname: "datasource"});
ConstructorMap.httpdatasource = LzHTTPDatasource;
var LzDataAttrBind = Class.make("LzDataAttrBind", LzDatapointer, {initialize: 
/* -*- file: data/LzDataAttrBind.lzs#14.1 -*- */
function initialize ($1_ndpath, $2_attr, $3_path) {
this.setAttr = $2_attr;
this.pathparent = $1_ndpath;
this.node = $1_ndpath.immediateparent;
this.setXPath($3_path);
if ($1_ndpath.__LZdepChildren == null) {
$1_ndpath.__LZdepChildren = [this]
} else {
$1_ndpath.__LZdepChildren.push(this)
}
}, $pathbinding: true, __LZsendUpdate: 
/* -*- file: data/LzDataAttrBind.lzs#31.1 -*- */
function __LZsendUpdate ($1_a, $2_b) {
var $3_pchg = this.__LZpchanged;
if (!(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZsendUpdate"] : this.nextMethod(arguments.callee, "__LZsendUpdate")).apply(this, arguments)) {
return
}
if ($3_pchg || this.node[this.setAttr] != this.data || this.parsedPath.operator == "attributes") {
var $lzsc$78507757 = this.node;
var $lzsc$542042680 = this.setAttr;
var $lzsc$362664518 = this.data == null ? null : this.data;
if (!$lzsc$78507757.__LZdeleted) {
var $lzsc$449097494 = $lzsc$78507757.setters;
if ($lzsc$449097494 && $lzsc$542042680 in $lzsc$449097494) {
$lzsc$78507757[$lzsc$449097494[$lzsc$542042680]]($lzsc$362664518)
} else {
if ($lzsc$449097494 == null) {
Debug.warn("null setters on", $lzsc$78507757, $lzsc$542042680, $lzsc$362664518)
}
$lzsc$78507757[$lzsc$542042680] = $lzsc$362664518;
var $lzsc$741507740 = "on" + $lzsc$542042680;
if ($lzsc$741507740 in $lzsc$78507757) {
if ($lzsc$78507757[$lzsc$741507740].ready) {
$lzsc$78507757[$lzsc$741507740].sendEvent($lzsc$362664518)
}
}
}
}
}
}, unregisterAll: 
/* -*- file: data/LzDataAttrBind.lzs#50.1 -*- */
function unregisterAll () {
var $1_dca = this.pathparent.__LZdepChildren;
if ($1_dca != null) {
for (var $2_i = 0;
$2_i < $1_dca.length;
$2_i++) {
if ($1_dca[$2_i] == this) {
$1_dca.splice($2_i, 1);
break
}
}
}
this.destroy()
}, setDataContext: 
/* -*- file: data/LzDataAttrBind.lzs#66.1 -*- */
function setDataContext ($1_dc) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["setDataContext"] : this.nextMethod(arguments.callee, "setDataContext")).call(this, $1_dc || this.pathparent)
}, updateData: 
/* -*- file: data/LzDataAttrBind.lzs#72.1 -*- */
function updateData () {
var $1_dat = this.node[this.setAttr];
if (this.data == $1_dat) {
return
}
var $2_ppdo = this.parsedPath.operator;
if ($2_ppdo != null) {
if ($2_ppdo == "nodeName") {
this.setNodeName($1_dat)
} else {
if ($2_ppdo == "__LZgetText") {
this.setNodeText($1_dat)
} else {
if ($2_ppdo == "attributes") {
this.p.setAttrs($1_dat)
} else {
this.setNodeAttribute($2_ppdo.substring(11), $1_dat)
}
}
}
}
}, toString: 
/* -*- file: data/LzDataAttrBind.lzs#91.1 -*- */
function toString () {
return "binder " + this.xpath
}, _dbg_name: 
/* -*- file: data/LzDataAttrBind.lzs#96.5 -*- */
function _dbg_name () {
return Debug.formatToString('%w.%s="$path{%w}"', this.node, this.setAttr, this.xpath)
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzDataAttrBind) {
with (LzDataAttrBind.prototype) {
prototype.rerunxpath = true
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzLazyReplicationManager = Class.make("LzLazyReplicationManager", LzReplicationManager, {axis: "y", spacing: 0, mask: null, construct: 
/* -*- file: data/LzLazyReplicationManager.lzs#64.1 -*- */
function construct ($1_odp, $2_args) {
if ($2_args.pooling != null) {
$2_args.pooling = true;
Debug.warn("Invalid pooling argument specified " + "with lazy replication in %w", this)
}
if ($2_args.axis != null) {
this.axis = $2_args.axis
}
this.sizeAxis = this.axis == "x" ? "width" : "height";
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.mask = $1_odp.immediateparent.immediateparent.mask;
var $3_cloneopt = {ignorelayout: true}
if (this.cloneAttrs.options != null) {
$3_cloneopt = new LzInheritedHash(this.cloneAttrs.options)
}
var $4_firstcl = this.clones[0];
$4_firstcl.setOption("ignorelayout", true);
var $5_layo = $4_firstcl.immediateparent.layouts;
if ($5_layo != null) {
for (var $6_i = 0;
$6_i < $5_layo.length;
$6_i++) {
$5_layo[$6_i].removeSubview($4_firstcl)
}
}
this.cloneAttrs.options = $3_cloneopt;
var $7_v = this.getNewClone(true);
this.cloneimmediateparent = $7_v.immediateparent;
if (this.initialnodes) {
$7_v.datapath.setClonePointer(this.initialnodes[1])
}
this.viewsize = $7_v[this.sizeAxis];
$7_v.datapath.setClonePointer(null);
this.clones.push($7_v);
if ($2_args.spacing == null) {
$2_args.spacing = 0
}
this.totalsize = this.viewsize + $2_args.spacing;
var $lzsc$880283019 = this.axis;
var $lzsc$251936743 = this.totalsize;
if (!$7_v.__LZdeleted) {
var $lzsc$1025163196 = $7_v.setters;
if ($lzsc$1025163196 && $lzsc$880283019 in $lzsc$1025163196) {
$7_v[$lzsc$1025163196[$lzsc$880283019]]($lzsc$251936743)
} else {
if ($lzsc$1025163196 == null) {
Debug.warn("null setters on", $7_v, $lzsc$880283019, $lzsc$251936743)
}
$7_v[$lzsc$880283019] = $lzsc$251936743;
var $lzsc$1653001912 = "on" + $lzsc$880283019;
if ($lzsc$1653001912 in $7_v) {
if ($7_v[$lzsc$1653001912].ready) {
$7_v[$lzsc$1653001912].sendEvent($lzsc$251936743)
}
}
}
}
this.__LZdataoffset = 0;
this.updateDel = new LzDelegate(this, "__LZadjustVisibleClones");
this.updateDel.register(this.cloneimmediateparent, "on" + this.axis);
this.updateDel.register(this.mask, "on" + this.sizeAxis)
}, viewsize: 0, totalsize: 0, __LZsetCloneAttrs: 
/* -*- file: data/LzLazyReplicationManager.lzs#137.1 -*- */
function __LZsetCloneAttrs () {
var $1_cloneopt = {ignorelayout: true}
if (this.cloneAttrs.options != null) {
$1_cloneopt = new LzInheritedHash(this.cloneAttrs.options)
}
this.cloneAttrs.options = $1_cloneopt
}, __LZHandleNoNodes: 
/* -*- file: data/LzLazyReplicationManager.lzs#148.1 -*- */
function __LZHandleNoNodes () {
this.__LZHandleMultiNodes([])
}, __LZadjustVisibleClones: 
/* -*- file: data/LzLazyReplicationManager.lzs#175.1 -*- */
function __LZadjustVisibleClones ($1_ln, $2_nn) {
var $3_nodelen = null;
if (this.nodes) {
$3_nodelen = this.nodes.length
}
if ($3_nodelen != null) {
if (this.__LZoldnodelen != this.nodes.length) {
var $lzsc$1887290208 = this.cloneimmediateparent;
var $lzsc$356738910 = this.sizeAxis;
var $lzsc$1899709359 = this.nodes.length * this.totalsize - this.spacing;
if (!$lzsc$1887290208.__LZdeleted) {
var $lzsc$1031182201 = $lzsc$1887290208.setters;
if ($lzsc$1031182201 && $lzsc$356738910 in $lzsc$1031182201) {
$lzsc$1887290208[$lzsc$1031182201[$lzsc$356738910]]($lzsc$1899709359)
} else {
if ($lzsc$1031182201 == null) {
Debug.warn("null setters on", $lzsc$1887290208, $lzsc$356738910, $lzsc$1899709359)
}
$lzsc$1887290208[$lzsc$356738910] = $lzsc$1899709359;
var $lzsc$687233609 = "on" + $lzsc$356738910;
if ($lzsc$687233609 in $lzsc$1887290208) {
if ($lzsc$1887290208[$lzsc$687233609].ready) {
$lzsc$1887290208[$lzsc$687233609].sendEvent($lzsc$1899709359)
}
}
}
}
this.__LZoldnodelen = this.nodes.length
}
}
if (!(this.mask && this.mask["hasset" + this.sizeAxis])) {
return
}
var $4_newstart = 0;
if (this.totalsize != 0) {
$4_newstart = Math.floor(-this.cloneimmediateparent[this.axis] / this.totalsize)
}
if (0 > $4_newstart) {
$4_newstart = 0
}
var $5_oldstart = 0;
var $6_oldlength = this.clones.length;
var $7_offset = $4_newstart - this.__LZdataoffset;
var $8_remainder = $4_newstart * this.totalsize + this.cloneimmediateparent[this.axis];
var $9_newlength = 0;
if (typeof $8_remainder == "number") {
$9_newlength = 1 + Math.floor((this.mask[this.sizeAxis] - $8_remainder) / this.totalsize)
}
if (this.nodes != null) {
if ($9_newlength + $4_newstart > this.nodes.length) {
$9_newlength = this.nodes.length - $4_newstart
}
}
if ($7_offset == 0 && $9_newlength == $6_oldlength) {
return
}
LzInstantiator.enableDataReplicationQueuing();
var $10_oldclones = this.clones;
this.clones = [];
for (var $11_i = 0;
$11_i < $9_newlength;
$11_i++) {
var $12_cl = false;
if ($11_i + $7_offset < 0) {
if ($9_newlength + $7_offset < $6_oldlength && $6_oldlength > 0) {
$12_cl = $10_oldclones[--$6_oldlength]
} else {
$12_cl = this.getNewClone()
}
} else {
if ($11_i + $7_offset >= $6_oldlength) {
if ($5_oldstart < $7_offset && $5_oldstart < $6_oldlength) {
$12_cl = $10_oldclones[$5_oldstart++]
} else {
$12_cl = this.getNewClone()
}
}
}
if ($12_cl) {
this.clones[$11_i] = $12_cl;
var $lzsc$2289595 = this.axis;
var $lzsc$899614868 = ($11_i + $4_newstart) * this.totalsize;
if (!$12_cl.__LZdeleted) {
var $lzsc$938862340 = $12_cl.setters;
if ($lzsc$938862340 && $lzsc$2289595 in $lzsc$938862340) {
$12_cl[$lzsc$938862340[$lzsc$2289595]]($lzsc$899614868)
} else {
if ($lzsc$938862340 == null) {
Debug.warn("null setters on", $12_cl, $lzsc$2289595, $lzsc$899614868)
}
$12_cl[$lzsc$2289595] = $lzsc$899614868;
var $lzsc$1594812365 = "on" + $lzsc$2289595;
if ($lzsc$1594812365 in $12_cl) {
if ($12_cl[$lzsc$1594812365].ready) {
$12_cl[$lzsc$1594812365].sendEvent($lzsc$899614868)
}
}
}
}
$12_cl.clonenumber = $4_newstart + $11_i;
if (this.nodes) {
$12_cl.datapath.setClonePointer(this.nodes[$4_newstart + $11_i])
}
if ($12_cl.onclonenumber.ready) {
$12_cl.onclonenumber.sendEvent($11_i)
}
} else {
this.clones[$11_i] = $10_oldclones[$11_i + $7_offset]
}
}
while ($5_oldstart < $7_offset && $5_oldstart < $6_oldlength) {
var $13_v = $10_oldclones[$5_oldstart++];
this.detachClone($13_v);
this.clonePool.push($13_v)
}
while ($6_oldlength > $9_newlength + $7_offset && $6_oldlength > 0) {
var $13_v = $10_oldclones[--$6_oldlength];
this.detachClone($13_v);
this.clonePool.push($13_v)
}
this.__LZdataoffset = $4_newstart;
LzInstantiator.clearDataReplicationQueue()
}, toString: 
/* -*- file: data/LzLazyReplicationManager.lzs#284.1 -*- */
function toString () {
return "Lazy clone manager in " + this.cloneimmediateparent
}, getCloneForNode: 
/* -*- file: data/LzLazyReplicationManager.lzs#291.1 -*- */
function getCloneForNode ($1_p, $2_dontmake) {
var $3_cl = (arguments.callee.superclass ? arguments.callee.superclass.prototype["getCloneForNode"] : this.nextMethod(arguments.callee, "getCloneForNode")).call(this, $1_p) || null;
if (!$3_cl && !$2_dontmake) {
$3_cl = this.getNewClone();
$3_cl.datapath.setClonePointer($1_p);
this.detachClone($3_cl);
this.clonePool.push($3_cl)
}
return $3_cl
}, getCloneNumber: 
/* -*- file: data/LzLazyReplicationManager.lzs#313.1 -*- */
function getCloneNumber ($1_n) {
return this.getCloneForNode(this.nodes[$1_n])
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzLazyReplicationManager) {
with (LzLazyReplicationManager.prototype) {
prototype.pooling = true
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzResizeReplicationManager = Class.make("LzResizeReplicationManager", LzLazyReplicationManager, {construct: 
/* -*- file: data/LzResizeReplicationManager.lzs#82.1 -*- */
function construct ($1_odp, $2_args) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["construct"] : this.nextMethod(arguments.callee, "construct")).apply(this, arguments);
this.datasizevar = "$" + this.getUID() + "track"
}, __LZsetCloneAttrs: 
/* -*- file: data/LzResizeReplicationManager.lzs#94.1 -*- */
function __LZsetCloneAttrs () {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["__LZsetCloneAttrs"] : this.nextMethod(arguments.callee, "__LZsetCloneAttrs")).apply(this, arguments);
this.cloneAttrs.setHeight = LzResizeReplicationManager.__LZResizeSetSize
}, getPositionByNode: 
/* -*- file: data/LzResizeReplicationManager.lzs#102.1 -*- */
function getPositionByNode ($1_n) {
var $2_pos = -this.spacing;
var $3_cnode;
if (this.nodes != null) {
for (var $4_i = 0;
$4_i < this.nodes.length;
$4_i++) {
$3_cnode = this.nodes[$4_i];
if ($1_n == this.nodes[$4_i]) {
return $2_pos + this.spacing
}
$2_pos += this.spacing + ($3_cnode[this.datasizevar] || this.viewsize)
}
}
}, __LZreleaseClone: 
/* -*- file: data/LzResizeReplicationManager.lzs#122.1 -*- */
function __LZreleaseClone ($1_v) {
this.detachClone($1_v);
this.clonePool.push($1_v)
}, __LZadjustVisibleClones: 
/* -*- file: data/LzResizeReplicationManager.lzs#135.1 -*- */
function __LZadjustVisibleClones ($1_ln, $2_nn) {
if (!this.mask["hasset" + this.sizeAxis]) {
return
}
if (this.__LZresizeupdating) {
return
}
this.__LZresizeupdating = true;
var $3_nl = this.nodes != null ? this.nodes.length : 0;
var $4_newstart = -this.cloneimmediateparent[this.axis];
var $4_newstart = 0 > $4_newstart ? 0 : Math.floor($4_newstart);
var $5_masksize = this.mask[this.sizeAxis];
var $6_newoffset = -1;
var $7_oldoffset = this.__LZdataoffset;
if ($2_nn) {
while (this.clones.length) {
this.poolClone()
}
var $8_oldclones = null;
var $9_ocl = 0
} else {
var $8_oldclones = this.clones;
var $9_ocl = $8_oldclones.length
}
this.clones = [];
var $10_cpos = -this.spacing;
var $11_inwindow = false;
var $12_newend = -1;
var $13_notfirst = true;
for (var $14_i = 0;
$14_i < $3_nl;
$14_i++) {
var $15_cnode = this.nodes[$14_i];
var $16_ds = $15_cnode[this.datasizevar];
var $17_csiz = $16_ds == null ? this.viewsize : $16_ds;
$10_cpos += this.spacing;
if (!$11_inwindow && $6_newoffset == -1 && $10_cpos - $4_newstart + $17_csiz >= 0) {
var $13_notfirst = $14_i != 0;
$11_inwindow = true;
var $18_newstartpos = $10_cpos;
$6_newoffset = $14_i;
var $19_firstkept = $14_i - $7_oldoffset;
$19_firstkept = $19_firstkept > $9_ocl ? $9_ocl : $19_firstkept;
if ($19_firstkept > 0) {
for (var $20_j = 0;
$20_j < $19_firstkept;
$20_j++) {
var $21_v = $8_oldclones[$20_j];
this.__LZreleaseClone($21_v)
}
}
} else {
if ($11_inwindow && $10_cpos - $4_newstart > $5_masksize) {
$11_inwindow = false;
$12_newend = $14_i - $6_newoffset;
var $22_lastkept = $14_i - $7_oldoffset;
$22_lastkept = $22_lastkept < 0 ? 0 : $22_lastkept;
if ($22_lastkept < $9_ocl) {
for (var $20_j = $22_lastkept;
$20_j < $9_ocl;
$20_j++) {
var $21_v = $8_oldclones[$20_j];
this.__LZreleaseClone($21_v)
}
}
}
}
if ($11_inwindow) {
if ($14_i >= $7_oldoffset && $14_i < $7_oldoffset + $9_ocl) {
var $23_cl = $8_oldclones[$14_i - $7_oldoffset]
} else {
var $23_cl = null
}
this.clones[$14_i - $6_newoffset] = $23_cl
}
$10_cpos += $17_csiz
}
var $24_clpos = $18_newstartpos;
if ($13_notfirst) {
$24_clpos += this.spacing
}
for (var $14_i = 0;
$14_i < this.clones.length;
$14_i++) {
var $15_cnode = this.nodes[$14_i + $6_newoffset];
var $23_cl = this.clones[$14_i];
if (!$23_cl) {
$23_cl = this.getNewClone();
$23_cl.clonenumber = $14_i + $6_newoffset;
$23_cl.datapath.setClonePointer($15_cnode);
if ($23_cl.onclonenumber.ready) {
$23_cl.onclonenumber.sendEvent($14_i + $6_newoffset)
}
this.clones[$14_i] = $23_cl
}
this.clones[$14_i] = $23_cl;
var $lzsc$1095227425 = this.axis;
if (!$23_cl.__LZdeleted) {
var $lzsc$820850690 = $23_cl.setters;
if ($lzsc$820850690 && $lzsc$1095227425 in $lzsc$820850690) {
$23_cl[$lzsc$820850690[$lzsc$1095227425]]($24_clpos)
} else {
if ($lzsc$820850690 == null) {
Debug.warn("null setters on", $23_cl, $lzsc$1095227425, $24_clpos)
}
$23_cl[$lzsc$1095227425] = $24_clpos;
var $lzsc$122302086 = "on" + $lzsc$1095227425;
if ($lzsc$122302086 in $23_cl) {
if ($23_cl[$lzsc$122302086].ready) {
$23_cl[$lzsc$122302086].sendEvent($24_clpos)
}
}
}
}
var $16_ds = $15_cnode[this.datasizevar];
var $17_csiz = $16_ds == null ? this.viewsize : $16_ds;
if ($23_cl[this.sizeAxis] != $17_csiz) {
var $lzsc$1021190519 = this.sizeAxis;
if (!($23_cl.__LZdeleted || true && $23_cl[$lzsc$1021190519] == $17_csiz)) {
var $lzsc$1111586911 = $23_cl.setters;
if ($lzsc$1111586911 && $lzsc$1021190519 in $lzsc$1111586911) {
$23_cl[$lzsc$1111586911[$lzsc$1021190519]]($17_csiz)
} else {
if ($lzsc$1111586911 == null) {
Debug.warn("null setters on", $23_cl, $lzsc$1021190519, $17_csiz)
}
$23_cl[$lzsc$1021190519] = $17_csiz;
var $lzsc$640556774 = "on" + $lzsc$1021190519;
if ($lzsc$640556774 in $23_cl) {
if ($23_cl[$lzsc$640556774].ready) {
$23_cl[$lzsc$640556774].sendEvent($17_csiz)
}
}
}
}
}
$24_clpos += $17_csiz + this.spacing
}
this.__LZdataoffset = $6_newoffset;
var $lzsc$107355882 = this.cloneimmediateparent;
var $lzsc$2128016868 = this.sizeAxis;
if (!$lzsc$107355882.__LZdeleted) {
var $lzsc$22744175 = $lzsc$107355882.setters;
if ($lzsc$22744175 && $lzsc$2128016868 in $lzsc$22744175) {
$lzsc$107355882[$lzsc$22744175[$lzsc$2128016868]]($10_cpos)
} else {
if ($lzsc$22744175 == null) {
Debug.warn("null setters on", $lzsc$107355882, $lzsc$2128016868, $10_cpos)
}
$lzsc$107355882[$lzsc$2128016868] = $10_cpos;
var $lzsc$963260180 = "on" + $lzsc$2128016868;
if ($lzsc$963260180 in $lzsc$107355882) {
if ($lzsc$107355882[$lzsc$963260180].ready) {
$lzsc$107355882[$lzsc$963260180].sendEvent($10_cpos)
}
}
}
}
this.__LZresizeupdating = false
}, __LZHandleCloneResize: 
/* -*- file: data/LzResizeReplicationManager.lzs#275.1 -*- */
function __LZHandleCloneResize ($1_cl, $2_s) {
var $3_osize = $1_cl.datapath.p[this.datasizevar] || this.viewsize;
if ($2_s != $3_osize) {
$1_cl.datapath.p[this.datasizevar] = $2_s;
this.__LZadjustVisibleClones()
}
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzResizeReplicationManager) {
with (LzResizeReplicationManager.prototype) {
prototype.pooling = false;
LzResizeReplicationManager.__LZResizeSetSize = (function  () {
var $lzsc$temp = 
/* -*- file: data/LzResizeReplicationManager.lzs#288.48 -*- */
function  ($1_h, $2_k) {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["setHeight"] : this.nextMethod(arguments.callee, "setHeight")).call(this, $1_h);
if ($2_k != true) {
this.cloneManager.__LZHandleCloneResize(this, $1_h)
}
}
$lzsc$temp._dbg_name = "LzResizeReplicationManager.__LZResizeSetSize";
return $lzsc$temp
})()
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzInstantiatorClass = Class.make("LzInstantiatorClass", null, {checkQDel: null, initialize: 
/* -*- file: services/LzInstantiator.lzs#22.1 -*- */
function initialize () {
this.checkQDel = new LzDelegate(this, "checkQ")
}, halted: false, isimmediate: false, isdatareplicating: false, istrickling: false, isUpdating: false, safe: true, timeout: 500, makeQ: [], trickleQ: [], tricklingQ: [], syncNew: true, trickletime: 10, setSafeInstantiation: 
/* -*- file: services/LzInstantiator.lzs#56.1 -*- */
function setSafeInstantiation ($1_isSafe) {
this.safe = $1_isSafe;
if (this.instanceQ.length) {
this.timeout = Infinity
}
}, requestInstantiation: 
/* -*- file: services/LzInstantiator.lzs#71.1 -*- */
function requestInstantiation ($1_v, $2_children) {
if (this.isimmediate) {
this.createImmediate($1_v, $2_children.concat())
} else {
var $3_c = this.newReverseArray($2_children);
if (this.isdatareplicating) {
this.datareplq.push($1_v, $3_c)
} else {
if (this.istrickling) {
this.tricklingQ.push($1_v, $3_c)
} else {
this.makeQ.push($1_v, $3_c);
this.checkUpdate()
}
}
}
}, enableDataReplicationQueuing: 
/* -*- file: services/LzInstantiator.lzs#90.1 -*- */
function enableDataReplicationQueuing () {
this.isdatareplicating = true;
this.datareplq = []
}, clearDataReplicationQueue: 
/* -*- file: services/LzInstantiator.lzs#97.1 -*- */
function clearDataReplicationQueue () {
this.isdatareplicating = false;
var $1_drq = this.datareplq;
for (var $2_i = $1_drq.length - 1;
$2_i > 0;
$2_i -= 2) {
this.makeQ.push($1_drq[$2_i - 1], $1_drq[$2_i])
}
this.checkUpdate()
}, newReverseArray: 
/* -*- file: services/LzInstantiator.lzs#110.1 -*- */
function newReverseArray ($1_arr) {
var $2_n = $1_arr.length;
var $3_a = Array($2_n);
var $4_i = 0;
var $5_j = $2_n - 1;
while ($4_i < $2_n) {
$3_a[$4_i] = $1_arr[$5_j];
$4_i++;
$5_j--
}
return $3_a
}, checkUpdate: 
/* -*- file: services/LzInstantiator.lzs#125.1 -*- */
function checkUpdate () {
if (!this.isUpdating && !this.halted) {
this.checkQDel.register(LzIdle, "onidle");
this.isUpdating = true
}
}, checkQ: 
/* -*- file: services/LzInstantiator.lzs#135.1 -*- */
function checkQ () {
if (!this.makeQ.length) {
if (!this.tricklingQ.length) {
if (!this.trickleQ.length) {
this.checkQDel.unregisterAll();
this.isUpdating = false;
return
} else {
var $1_p = this.trickleQ.shift();
var $2_c = this.trickleQ.shift();
this.tricklingQ.push($1_p, this.newReverseArray($2_c))
}
}
this.istrickling = true;
this.makeSomeViews(this.tricklingQ, this.trickletime);
this.istrickling = false
} else {
canvas.creatednodes += this.makeSomeViews(this.makeQ, this.timeout);
if (canvas.updatePercentCreated) {
canvas.updatePercentCreated()
}
}
}, makeSomeViews: 
/* -*- file: services/LzInstantiator.lzs#163.1 -*- */
function makeSomeViews ($1_cq, $2_otime) {
var $3_itime = new Date().getTime();
var $4_num = 0;
while (new Date().getTime() - $3_itime < $2_otime && $1_cq.length) {
var $5_l = $1_cq.length;
var $6_larr = $1_cq[$5_l - 1];
var $7_par = $1_cq[$5_l - 2];
var $8_parDone = false;
if ($7_par["__LZdeleted"]) {
$1_cq.length -= 2;
continue
}
while (new Date().getTime() - $3_itime < $2_otime) {
if ($5_l != $1_cq.length) {
break
}
if (!$6_larr.length) {
$8_parDone = true;
break
}
var $11_c = $6_larr.pop();
if ($11_c) {
$7_par.makeChild($11_c, true);
$4_num++
}
}
if ($8_parDone) {
$1_cq.length = $5_l - 2;
$7_par.__LZinstantiationDone()
}
}
return $4_num
}, trickleInstantiate: 
/* -*- file: services/LzInstantiator.lzs#222.1 -*- */
function trickleInstantiate ($1_v, $2_children) {
this.trickleQ.push($1_v, $2_children);
this.checkUpdate()
}, createImmediate: 
/* -*- file: services/LzInstantiator.lzs#230.1 -*- */
function createImmediate ($1_v, $2_children) {
var $3_c = this.newReverseArray($2_children);
var $4_wasimmediate = this.isimmediate;
this.isimmediate = true;
this.makeSomeViews([$1_v, $3_c], Infinity);
this.isimmediate = $4_wasimmediate
}, completeTrickle: 
/* -*- file: services/LzInstantiator.lzs#241.1 -*- */
function completeTrickle ($1_v) {
if (this.tricklingQ[0] == $1_v) {
var $2_wasimmediate = this.isimmediate;
this.isimmediate = true;
this.makeSomeViews(this.tricklingQ, Infinity);
this.isimmediate = $2_wasimmediate;
this.tricklingQ = []
} else {
var $3_tql = this.trickleQ.length;
for (var $4_i = 0;
$4_i < $3_tql;
$4_i += 2) {
if (this.trickleQ[$4_i] == $1_v) {
var $5_dchil = this.trickleQ[$4_i + 1];
this.trickleQ.splice($4_i, 2);
this.createImmediate($1_v, $5_dchil);
return
}
}
}
}, traceQ: 
/* -*- file: services/LzInstantiator.lzs#264.1 -*- */
function traceQ () {
var $1_mql = this.makeQ.length;
trace("****start trace");
for (var $2_i = 0;
$2_i < $1_mql;
$2_i += 2) {
var $3_s = "";
for (var $4_k = 0;
$4_k < this.makeQ[$2_i + 1].length;
$4_k++) {
$3_s += this.makeQ[$2_i + 1][$4_k].name + " |"
}
trace(this.makeQ[$2_i] + " : |" + $3_s + " >>> " + this.makeQ[$2_i].getUID())
}
trace("****trace done")
}, halt: 
/* -*- file: services/LzInstantiator.lzs#280.1 -*- */
function halt () {
this.isUpdating = false;
this.halted = true;
this.checkQDel.unregisterAll()
}, resume: 
/* -*- file: services/LzInstantiator.lzs#289.1 -*- */
function resume () {
this.halted = false;
this.checkUpdate()
}, drainQ: 
/* -*- file: services/LzInstantiator.lzs#300.1 -*- */
function drainQ ($1_limit) {
var $2_to = this.timeout;
var $3_tt = this.trickletime;
var $4_h = this.halted;
this.timeout = $1_limit;
this.trickletime = $1_limit;
this.halted = false;
this.isUpdating = true;
this.checkQ();
this.halted = $4_h;
this.timeout = $2_to;
this.trickletime = $3_tt;
return !this.isUpdating
}}, null);
var LzInstantiator = new LzInstantiatorClass();
var LzGlobalMouse = new Object();
DeclareEvent(LzGlobalMouse, "onmousemove");
DeclareEvent(LzGlobalMouse, "onmouseup");
DeclareEvent(LzGlobalMouse, "onmousedown");
LzGlobalMouse.__movecounter = 0;
LzGlobalMouse.__mouseEvent = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzGlobalMouse.lzs#84.30 -*- */
function  ($1_eventname, $2_view) {
if ($1_eventname == "onmousemove") {
LzGlobalMouse.__movecounter++
}
if (LzGlobalMouse[$1_eventname] && LzGlobalMouse[$1_eventname].ready) {
LzGlobalMouse[$1_eventname].sendEvent($2_view)
}
}
$lzsc$temp._dbg_name = "LzGlobalMouse.__mouseEvent";
return $lzsc$temp
})();
var LzModeManager = new Object();
DeclareEvent(LzModeManager, "onmode");
LzModeManager.onmode = null;
LzModeManager.__LZlastclick = null;
LzModeManager.willCall = false;
LzModeManager.eventsLocked = false;
LzModeManager.modeArray = new Array();
LzModeManager.toString = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#64.26 -*- */
function  () {
return "mode manager"
}
$lzsc$temp._dbg_name = "LzModeManager.toString";
return $lzsc$temp
})();
LzModeManager.makeModal = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#73.27 -*- */
function  ($1_view) {
this.modeArray.push($1_view);
if (this.onmode.ready) {
this.onmode.sendEvent($1_view)
}
var $2_f = LzFocus.getFocus();
if ($2_f && !$2_f.childOf($1_view)) {
LzFocus.clearFocus()
}
}
$lzsc$temp._dbg_name = "LzModeManager.makeModal";
return $lzsc$temp
})();
LzModeManager.release = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#86.25 -*- */
function  ($1_view) {
for (var $2_i = this.modeArray.length - 1;
$2_i >= 0;
$2_i--) {
if (this.modeArray[$2_i] == $1_view) {
this.modeArray.splice($2_i, this.modeArray.length - $2_i);
var $3_newmode = this.modeArray[$2_i - 1];
if (this.onmode.ready) {
this.onmode.sendEvent($3_newmode || null)
}
var $4_f = LzFocus.getFocus();
if ($3_newmode && $4_f && !$4_f.childOf($3_newmode)) {
LzFocus.clearFocus()
}
return
}
}
}
$lzsc$temp._dbg_name = "LzModeManager.release";
return $lzsc$temp
})();
LzModeManager.releaseAll = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#105.28 -*- */
function  () {
this.modeArray = new Array();
if (this.onmode.ready) {
this.onmode.sendEvent(null)
}
}
$lzsc$temp._dbg_name = "LzModeManager.releaseAll";
return $lzsc$temp
})();
LzModeManager.handleMouseEvent = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#118.33 -*- */
function  ($1_view, $2_eventStr) {
if ($2_eventStr == "onmouseup") {
LzTrack.__LZmouseup()
}
var $3_dosend = true;
var $4_isinputtext = false;
if ($1_view == null) {
$1_view = this.__findInputtextSelection()
}
LzGlobalMouse.__mouseEvent($2_eventStr, $1_view);
if (this.eventsLocked == true) {
return
}
var $5_i = this.modeArray.length - 1;
while ($3_dosend && $5_i >= 0) {
var $6_mView = this.modeArray[$5_i--];
if ($1_view && Debug && $1_view.childOf(Debug)) {
break
}
if ($1_view && $1_view.childOf($6_mView)) {
break
} else {
if ($6_mView) {
$3_dosend = $6_mView.passModeEvent($2_eventStr, $1_view)
}
}
}
if ($3_dosend) {
if ($2_eventStr == "onclick") {
if (this.__LZlastclick == $1_view && ("ondblclick" in $1_view && $1_view.ondblclick) && !$1_view.ondblclick.hasNoDelegates && getTimer() - this.__LZlastClickTime < $1_view.DOUBLE_CLICK_TIME) {
$2_eventStr = "ondblclick";
this.__LZlastclick = null
} else {
this.__LZlastclick = $1_view;
this.__LZlastClickTime = getTimer()
}
}
if ($1_view) {
$1_view.mouseevent($2_eventStr)
}
if ($2_eventStr == "onmousedown") {
LzFocus.__LZcheckFocusChange($1_view)
}
}
this["haveGlobal" + $2_eventStr] = false
}
$lzsc$temp._dbg_name = "LzModeManager.handleMouseEvent";
return $lzsc$temp
})();
LzModeManager.__LZallowFocus = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#190.31 -*- */
function  ($1_view) {
var $2_len = this.modeArray.length;
return $2_len == 0 || $1_view.childOf(this.modeArray[$2_len - 1])
}
$lzsc$temp._dbg_name = "LzModeManager.__LZallowFocus";
return $lzsc$temp
})();
LzModeManager.globalLockMouseEvents = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#198.39 -*- */
function  () {
this.eventsLocked = true
}
$lzsc$temp._dbg_name = "LzModeManager.globalLockMouseEvents";
return $lzsc$temp
})();
LzModeManager.globalUnlockMouseEvents = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#205.41 -*- */
function  () {
this.eventsLocked = false
}
$lzsc$temp._dbg_name = "LzModeManager.globalUnlockMouseEvents";
return $lzsc$temp
})();
LzModeManager.hasMode = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#215.25 -*- */
function  ($1_view) {
for (var $2_i = this.modeArray.length - 1;
$2_i >= 0;
$2_i--) {
if ($1_view == this.modeArray[$2_i]) {
return true
}
}
return false
}
$lzsc$temp._dbg_name = "LzModeManager.hasMode";
return $lzsc$temp
})();
LzModeManager.getModalView = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzModeManager.lzs#227.30 -*- */
function  () {
return this.modeArray[this.modeArray.length - 1] || null
}
$lzsc$temp._dbg_name = "LzModeManager.getModalView";
return $lzsc$temp
})();
LzMouseKernel.setCallback(LzModeManager, "rawMouseEvent");
var LzCursor = new Object();
LzCursor.showHandCursor = LzMouseKernel.showHandCursor;
LzCursor.setCursorGlobal = LzMouseKernel.setCursorGlobal;
LzCursor.lock = LzMouseKernel.lock;
LzCursor.unlock = LzMouseKernel.unlock;
LzCursor.restoreCursor = LzMouseKernel.restoreCursor;

/* -*- file: services/LzURL.lzs#29.1 -*- */
function LzURL ($1_url) {
this.protocol = null;
this.host = null;
this.port = null;
this.path = null;
this.file = null;
this.query = null;
this.fragment = null;
if ($1_url != null) {
this.parseURL($1_url)
}
}
LzURL.prototype.parseURL = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzURL.lzs#50.28 -*- */
function  ($1_url) {
if (this._parsed == $1_url) {
return
}
this._parsed = $1_url;
var $2_i0 = 0;
var $3_i1 = $1_url.indexOf(":");
var $4_iquery = $1_url.indexOf("?", $2_i0);
var $5_ifrag = $1_url.indexOf("#", $2_i0);
var $6_iopt = $1_url.length;
if ($5_ifrag != -1) {
$6_iopt = $5_ifrag
}
if ($4_iquery != -1) {
$6_iopt = $4_iquery
}
if ($3_i1 != -1) {
this.protocol = $1_url.substring($2_i0, $3_i1);
if ($1_url.substring($3_i1 + 1, $3_i1 + 3) == "//") {
$2_i0 = $3_i1 + 3;
$3_i1 = $1_url.indexOf("/", $2_i0);
if ($3_i1 == -1) {
$3_i1 = $6_iopt
}
var $7_hostPort = $1_url.substring($2_i0, $3_i1);
var $8_i = $7_hostPort.indexOf(":");
if ($8_i == -1) {
this.host = $7_hostPort;
this.port = null
} else {
this.host = $7_hostPort.substring(0, $8_i);
this.port = $7_hostPort.substring($8_i + 1)
}
} else {
$3_i1++
}
$2_i0 = $3_i1
}
$3_i1 = $6_iopt;
this._splitPath($1_url.substring($2_i0, $3_i1));
if ($5_ifrag != -1) {
this.fragment = $1_url.substring($5_ifrag + 1, $1_url.length)
} else {
$5_ifrag = $1_url.length
}
if ($4_iquery != -1) {
this.query = $1_url.substring($4_iquery + 1, $5_ifrag)
}
}
$lzsc$temp._dbg_name = "LzURL.prototype.parseURL";
return $lzsc$temp
})();
LzURL.prototype._splitPath = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzURL.lzs#134.30 -*- */
function  ($1_pathfile) {
if ($1_pathfile == "") {
return
}
var $2_ls = $1_pathfile.lastIndexOf("/");
if ($2_ls != -1) {
this.path = $1_pathfile.substring(0, $2_ls + 1);
this.file = $1_pathfile.substring($2_ls + 1, $1_pathfile.length);
if (this.file == "") {
this.file = null
}
return
}
this.path = null;
this.file = $1_pathfile
}
$lzsc$temp._dbg_name = "LzURL.prototype._splitPath";
return $lzsc$temp
})();
LzURL.prototype.dupe = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzURL.lzs#157.24 -*- */
function  () {
var $1_o = new LzURL();
$1_o.protocol = this.protocol;
$1_o.host = this.host;
$1_o.port = this.port;
$1_o.path = this.path;
$1_o.file = this.file;
$1_o.query = this.query;
$1_o.fragment = this.fragment;
return $1_o
}
$lzsc$temp._dbg_name = "LzURL.prototype.dupe";
return $lzsc$temp
})();
LzURL.prototype.toString = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzURL.lzs#174.28 -*- */
function  () {
var $1_out = "";
if (this.protocol != null) {
$1_out += this.protocol + ":";
if (this.host != null) {
$1_out += "//" + this.host;
if (null != this.port && LzBrowser.defaultPortNums[this.protocol] != this.port) {
$1_out += ":" + this.port
}
}
}
if (this.path != null) {
$1_out += this.path
}
if (null != this.file) {
$1_out += this.file
}
if (null != this.query) {
$1_out += "?" + this.query
}
if (null != this.fragment) {
$1_out += "#" + this.fragment
}
return $1_out
}
$lzsc$temp._dbg_name = "LzURL.prototype.toString";
return $lzsc$temp
})();
LzURL.merge = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzURL.lzs#212.15 -*- */
function  ($1_url, $2_defaults) {
var $3_m = $1_url.dupe();
if ($1_url.protocol == null) {
$3_m.protocol = $2_defaults.protocol
}
if ($1_url.host == null) {
$3_m.host = $2_defaults.host
}
if ($1_url.port == null) {
$3_m.port = $2_defaults.port
}
if ($1_url.path == null) {
$3_m.path = $2_defaults.path
}
if ($1_url.file == null) {
$3_m.file = $2_defaults.file
}
if ($1_url.query == null) {
$3_m.query = $2_defaults.query
}
if ($1_url.fragment == null) {
$3_m.fragment = $2_defaults.fragment
}
return $3_m
}
$lzsc$temp._dbg_name = "LzURL.merge";
return $lzsc$temp
})();
LzModeManager.rawMouseEvent = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzModeManager.js#12.31 -*- */
function  ($1_me) {
this.handleMouseEvent(null, $1_me)
}
$lzsc$temp._dbg_name = "LzModeManager.rawMouseEvent";
return $lzsc$temp
})();
LzModeManager.handleMouseButton = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzModeManager.js#16.35 -*- */
function  ($1_view, $2_eventName) {
this.handleMouseEvent($1_view, $2_eventName)
}
$lzsc$temp._dbg_name = "LzModeManager.handleMouseButton";
return $lzsc$temp
})();
LzModeManager.__findInputtextSelection = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzModeManager.js#25.42 -*- */
function  () {
if (LzInputTextSprite.prototype.__focusedSprite && LzInputTextSprite.prototype.__focusedSprite.owner) {
return LzInputTextSprite.prototype.__focusedSprite.owner
}
}
$lzsc$temp._dbg_name = "LzModeManager.__findInputtextSelection";
return $lzsc$temp
})();
var LzBrowser = new Object();
LzBrowser.postToLps = true;
LzBrowser.loadURL = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#23.21 -*- */
function  ($1_url, $2_target, $3_features) {
if ($2_target != null) {
window.open($1_url, $2_target, $3_features)
} else {
window.location = $1_url
}
}
$lzsc$temp._dbg_name = "LzBrowser.loadURL";
return $lzsc$temp
})();
LzBrowser.loadJS = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#40.19 -*- */
function  ($1_js, $2_target) {
this.loadURL("javascript:" + $1_js + ";void(0);", $2_target)
}
$lzsc$temp._dbg_name = "LzBrowser.loadJS";
return $lzsc$temp
})();
LzBrowser.callJS = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#50.19 -*- */
function  ($1_js) {
return eval($1_js)
}
$lzsc$temp._dbg_name = "LzBrowser.callJS";
return $lzsc$temp
})();
LzBrowser.getVersion = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#57.24 -*- */
function  () {
return navigator.userAgent
}
$lzsc$temp._dbg_name = "LzBrowser.getVersion";
return $lzsc$temp
})();
LzBrowser.getLoadURL = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#66.24 -*- */
function  () {
var $1_url = Lz.__propcache.url;
if (!$1_url) {
$1_url = new String(window.location)
}
var $2_colon = $1_url.indexOf(":");
var $3_slash = $1_url.indexOf("/");
if ($2_colon > -1) {
if ($1_url.indexOf("://") == $2_colon) {
return $1_url
} else {
if ($1_url.charAt($2_colon + 1) == "/") {
$1_url = $1_url.substring(0, $2_colon + 1) + "/" + $1_url.substring($2_colon + 1);
return $1_url
} else {
var $4_lzu = new LzURL(new String(window.location));
$1_url = $1_url.substring(0, $2_colon + 1) + "/" + $4_lzu.path + $1_url.substring($2_colon + 1);
return $1_url
}
}
} else {
if ($3_slash == 0) {
return $1_url
} else {
var $5_loc = new String(window.location);
var $6_lastslash = $5_loc.lastIndexOf("/");
$5_loc = $5_loc.substring(0, $6_lastslash + 1);
return $5_loc + $1_url
}
}
}
$lzsc$temp._dbg_name = "LzBrowser.getLoadURL";
return $lzsc$temp
})();
LzBrowser.getInitArg = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#109.24 -*- */
function  ($1_name) {
return global[$1_name]
}
$lzsc$temp._dbg_name = "LzBrowser.getInitArg";
return $lzsc$temp
})();
LzBrowser.defaultPortNums = {http: 80, https: 443}
LzBrowser.getBaseURL = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#119.24 -*- */
function  ($1_secure, $2_port) {
var $3_url = this.getLoadURLAsLzURL();
if ($1_secure) {
$3_url.protocol = "https"
}
if ($2_port) {
$3_url.port = $2_port
}
if ($1_secure && $2_port == null) {
$3_url.port = LzBrowser.defaultPortNums[$3_url.protocol]
}
delete $3_url.query;
return $3_url
}
$lzsc$temp._dbg_name = "LzBrowser.getBaseURL";
return $lzsc$temp
})();
LzBrowser.getLoadURLAsLzURL = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#146.31 -*- */
function  () {
if (!this.parsedloadurl) {
this.parsedloadurl = new LzURL(this.getLoadURL())
}
return this.parsedloadurl.dupe()
}
$lzsc$temp._dbg_name = "LzBrowser.getLoadURLAsLzURL";
return $lzsc$temp
})();
LzBrowser.toAbsoluteURL = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#160.27 -*- */
function  ($1_url, $2_secure) {
if ($1_url.indexOf("://") > -1 || $1_url.indexOf("/@WEBAPP@/") == 0 || $1_url.indexOf("file:") == 0) {
return $1_url
}
var $3_returl = "";
var $4_u = this.getLoadURLAsLzURL();
if ($1_url.indexOf(":") > -1) {
var $5_colon = $1_url.indexOf(":");
var $6_loadUrlIsSecure = $4_u.protocol == "https";
$4_u.protocol = $1_url.substring(0, $5_colon);
if ($2_secure || $6_loadUrlIsSecure) {
if ($4_u.protocol == "http") {
$4_u.protocol = "https"
}
}
var $7_path = $1_url.substring($5_colon + 1, $1_url.length);
if ($7_path.charAt(0) == "/") {
$4_u.path = $1_url.substring($5_colon + 1);
delete $4_u.file
} else {
$4_u.file = $1_url.substring($5_colon + 1)
}
$4_u.query = null
} else {
if ($1_url.charAt(0) == "/") {
$4_u.path = $1_url;
$4_u.file = null
} else {
$4_u.file = $1_url
}
$4_u.query = null
}
return $4_u.toString()
}
$lzsc$temp._dbg_name = "LzBrowser.toAbsoluteURL";
return $lzsc$temp
})();
LzBrowser.showMenu = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#242.22 -*- */
function  ($1_truefalse) {
fscommand("showmenu", $1_truefalse)
}
$lzsc$temp._dbg_name = "LzBrowser.showMenu";
return $lzsc$temp
})();
LzBrowser.xmlEscape = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#249.23 -*- */
function  ($1_str) {
return LzDataNode.prototype.__LZXMLescape($1_str)
}
$lzsc$temp._dbg_name = "LzBrowser.xmlEscape";
return $lzsc$temp
})();
LzBrowser.urlEscape = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#258.23 -*- */
function  ($1_str) {
return escape($1_str)
}
$lzsc$temp._dbg_name = "LzBrowser.urlEscape";
return $lzsc$temp
})();
LzBrowser.urlUnescape = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#266.25 -*- */
function  ($1_str) {
return unescape($1_str)
}
$lzsc$temp._dbg_name = "LzBrowser.urlUnescape";
return $lzsc$temp
})();
LzBrowser.usePost = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#272.21 -*- */
function  () {
return this.postToLps && this.supportsPost()
}
$lzsc$temp._dbg_name = "LzBrowser.usePost";
return $lzsc$temp
})();
LzBrowser.supportsPost = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#277.26 -*- */
function  () {
return true
}
$lzsc$temp._dbg_name = "LzBrowser.supportsPost";
return $lzsc$temp
})();
LzBrowser.setClipboard = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#287.26 -*- */
function  ($1_str) {
System.setClipboard($1_str)
}
$lzsc$temp._dbg_name = "LzBrowser.setClipboard";
return $lzsc$temp
})();
LzBrowser.isAAActive = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#297.24 -*- */
function  () {
var $1_a = Accessibility.isActive();
return $1_a
}
$lzsc$temp._dbg_name = "LzBrowser.isAAActive";
return $lzsc$temp
})();
LzBrowser.updateAccessibility = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzBrowser.js#307.33 -*- */
function  () {
Accessibility.updateProperties()
}
$lzsc$temp._dbg_name = "LzBrowser.updateAccessibility";
return $lzsc$temp
})();
var LzKeys = {}
LzKeys.downKeysHash = {}
DeclareEvent(LzKeys, "onkeydown");
DeclareEvent(LzKeys, "onkeyup");
DeclareEvent(LzKeys, "onmousewheeldelta");
LzKeys.mousewheeldelta = 0;
LzKeys.__keyboardEvent = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzKeys.js#31.26 -*- */
function  ($1_delta, $2_keycode) {
for (var $3_k in $1_delta) {
var $4_down = $1_delta[$3_k];
LzKeys.downKeysHash[$3_k] = $4_down;
if ($2_keycode) {
if ($4_down) {
if (LzKeys.onkeydown.ready) {
LzKeys.onkeydown.sendEvent($2_keycode)
}
} else {
if (LzKeys.onkeyup.ready) {
LzKeys.onkeyup.sendEvent($2_keycode)
}
}
}
}
}
$lzsc$temp._dbg_name = "LzKeys.__keyboardEvent";
return $lzsc$temp
})();
LzKeys.__mousewheelEvent = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzKeys.js#47.28 -*- */
function  ($1_delta) {
LzKeys.mousewheeldelta = $1_delta;
if (LzKeys.onmousewheeldelta.ready) {
LzKeys.onmousewheeldelta.sendEvent($1_delta)
}
}
$lzsc$temp._dbg_name = "LzKeys.__mousewheelEvent";
return $lzsc$temp
})();
LzKeys.isKeyDown = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzKeys.js#52.20 -*- */
function  ($1_k) {
$1_k = $1_k.toLowerCase();
return LzKeys.downKeysHash[$1_k] == true
}
$lzsc$temp._dbg_name = "LzKeys.isKeyDown";
return $lzsc$temp
})();
LzKeys.callOnKeyCombo = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzKeys.js#59.25 -*- */
function  ($1_who, $2_k) {
Debug.warn("LzKeys.callOnKeyCombo is currently unimplemented")
}
$lzsc$temp._dbg_name = "LzKeys.callOnKeyCombo";
return $lzsc$temp
})();
LzKeyboardKernel.setCallback(LzKeys, "__keyboardEvent", "__mousewheelEvent");
LzKeys.keyCodes = {a: 65, b: 66, c: 67, d: 68, e: 69, f: 70, g: 71, h: 72, i: 73, j: 74, k: 75, l: 76, m: 77, n: 78, o: 79, p: 80, q: 81, r: 82, s: 83, t: 84, u: 85, v: 86, w: 87, x: 88, y: 89, z: 90, numbpad0: 96, numbpad1: 97, numbpad2: 98, numbpad3: 99, numbpad4: 100, numbpad5: 101, numbpad6: 102, numbpad7: 103, numbpad8: 104, numbpad9: 105, multiply: 106, enter: 13, subtract: 109, decimal: 110, divide: 111, f1: 112, f2: 113, f3: 114, f4: 115, f5: 116, f6: 117, f7: 118, f8: 119, f9: 120, f10: 121, f11: 122, f12: 123, backspace: 8, tab: 9, clear: 12, enter: 13, shift: 16, control: 17, alt: 18, capslock: 20, esc: 27, spacebar: 32, pageup: 33, pagedown: 34, end: 35, home: 36, leftarrow: 37, uparrow: 38, rightarrow: 39, downarrow: 40, insert: 45, help: 47, numlock: 144}
LzKeys.keyCodes["add"] = 107;
LzKeys.keyCodes["delete"] = 46;
LzKeys.keyCodes["0"] = 48;
LzKeys.keyCodes["1"] = 49;
LzKeys.keyCodes["2"] = 50;
LzKeys.keyCodes["3"] = 51;
LzKeys.keyCodes["4"] = 52;
LzKeys.keyCodes["5"] = 53;
LzKeys.keyCodes["6"] = 54;
LzKeys.keyCodes["7"] = 55;
LzKeys.keyCodes["8"] = 56;
LzKeys.keyCodes["9"] = 57;
LzKeys.keyCodes[")"] = 48;
LzKeys.keyCodes["!"] = 49;
LzKeys.keyCodes["@"] = 50;
LzKeys.keyCodes["#"] = 51;
LzKeys.keyCodes["$"] = 52;
LzKeys.keyCodes["%"] = 53;
LzKeys.keyCodes["^"] = 54;
LzKeys.keyCodes["&"] = 55;
LzKeys.keyCodes["*"] = 56;
LzKeys.keyCodes["("] = 57;
LzKeys.keyCodes[";"] = 186;
LzKeys.keyCodes[":"] = 186;
LzKeys.keyCodes["="] = 187;
LzKeys.keyCodes["+"] = 187;
LzKeys.keyCodes["<"] = 188;
LzKeys.keyCodes[","] = 188;
LzKeys.keyCodes["-"] = 189;
LzKeys.keyCodes["_"] = 189;
LzKeys.keyCodes[">"] = 190;
LzKeys.keyCodes["."] = 190;
LzKeys.keyCodes["/"] = 191;
LzKeys.keyCodes["?"] = 191;
LzKeys.keyCodes["`"] = 192;
LzKeys.keyCodes["~"] = 192;
LzKeys.keyCodes["["] = 219;
LzKeys.keyCodes["{"] = 219;
LzKeys.keyCodes["\\"] = 220;
LzKeys.keyCodes["|"] = 220;
LzKeys.keyCodes["]"] = 221;
LzKeys.keyCodes["}"] = 221;
LzKeys.keyCodes['"'] = 222;
LzKeys.keyCodes["'"] = 222;
LzKeys.keyCodes["IME"] = 229;
var LzHistory = new Object();
LzHistory.isReady = true;
LzHistory.setHistory = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#37.24 -*- */
function  ($1_s) {
Lz.history.set($1_s)
}
$lzsc$temp._dbg_name = "LzHistory.setHistory";
return $lzsc$temp
})();
LzHistory.getPersist = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#45.24 -*- */
function  ($1_n) {

}
$lzsc$temp._dbg_name = "LzHistory.getPersist";
return $lzsc$temp
})();
LzHistory.offset = 0;
LzHistory.__lzdirty = false;
LzHistory.__lzhistq = [];
LzHistory.__lzcurrstate = {}
DeclareEvent(LzHistory, "onoffset");
LzHistory.receiveHistory = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#63.28 -*- */
function  ($1_o) {
$1_o *= 1;
if (!$1_o) {
$1_o = 0
}
if ($1_o > this.__lzhistq.length - 1) {
$1_o = this.__lzhistq.length
}
this.offset = $1_o;
if (this.onoffset.ready) {
this.onoffset.sendEvent($1_o)
}
var $2_h = this.__lzhistq[$1_o];
for (var $3_u in $2_h) {
var $1_o = $2_h[$3_u];
var $lzsc$83662664 = global[$1_o.c];
var $lzsc$213650249 = $1_o.n;
var $lzsc$899988162 = $1_o.v;
if (!$lzsc$83662664.__LZdeleted) {
var $lzsc$1919913339 = $lzsc$83662664.setters;
if ($lzsc$1919913339 && $lzsc$213650249 in $lzsc$1919913339) {
$lzsc$83662664[$lzsc$1919913339[$lzsc$213650249]]($lzsc$899988162)
} else {
if ($lzsc$1919913339 == null) {
Debug.warn("null setters on", $lzsc$83662664, $lzsc$213650249, $lzsc$899988162)
}
$lzsc$83662664[$lzsc$213650249] = $lzsc$899988162;
var $lzsc$1528566502 = "on" + $lzsc$213650249;
if ($lzsc$1528566502 in $lzsc$83662664) {
if ($lzsc$83662664[$lzsc$1528566502].ready) {
$lzsc$83662664[$lzsc$1528566502].sendEvent($lzsc$899988162)
}
}
}
}
}
}
$lzsc$temp._dbg_name = "LzHistory.receiveHistory";
return $lzsc$temp
})();
LzHistory.receiveEvent = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#82.26 -*- */
function  ($1_n, $2_v) {
canvas[$1_n] = $2_v;
if (canvas["on" + $1_n].ready) {
canvas["on" + $1_n].sendEvent($2_v)
}
}
$lzsc$temp._dbg_name = "LzHistory.receiveEvent";
return $lzsc$temp
})();
LzHistory.getCanvasAttribute = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#91.32 -*- */
function  ($1_n) {
return canvas[$1_n]
}
$lzsc$temp._dbg_name = "LzHistory.getCanvasAttribute";
return $lzsc$temp
})();
LzHistory.setCanvasAttribute = LzHistory.receiveEvent;
LzHistory.callMethod = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#103.24 -*- */
function  ($1_js) {
return eval($1_js)
}
$lzsc$temp._dbg_name = "LzHistory.callMethod";
return $lzsc$temp
})();
LzHistory.save = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#113.18 -*- */
function  ($1_who, $2_prop, $3_val) {
if ($3_val == null) {
$3_val = global[$1_who][$2_prop]
}
this.__lzcurrstate[$1_who] = {c: $1_who, n: $2_prop, v: $3_val}
this.__lzdirty = true
}
$lzsc$temp._dbg_name = "LzHistory.save";
return $lzsc$temp
})();
LzHistory.commit = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#127.20 -*- */
function  () {
if (!this.__lzdirty) {
return
}
this.__lzhistq[this.offset] = this.__lzcurrstate;
this.__lzhistq.length = this.offset + 1;
this.__lzcurrstate = {}
this.__lzdirty = false
}
$lzsc$temp._dbg_name = "LzHistory.commit";
return $lzsc$temp
})();
LzHistory.move = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#142.18 -*- */
function  ($1_by) {
this.commit();
if (!$1_by) {
$1_by = 1
}
var $2_o = this.offset + $1_by;
if (0 >= $2_o) {
$2_o = 0
}
if (this.__lzhistq.length >= $2_o) {
this.setHistory($2_o)
}
}
$lzsc$temp._dbg_name = "LzHistory.move";
return $lzsc$temp
})();
LzHistory.next = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#159.18 -*- */
function  () {
this.move(1)
}
$lzsc$temp._dbg_name = "LzHistory.next";
return $lzsc$temp
})();
LzHistory.prev = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#167.18 -*- */
function  () {
this.move(-1)
}
$lzsc$temp._dbg_name = "LzHistory.prev";
return $lzsc$temp
})();
LzHistory.clear = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#171.19 -*- */
function  () {
this.__lzhistq = [];
this.offset = 0;
if (this.onoffset.ready) {
this.onoffset.sendEvent(0)
}
}
$lzsc$temp._dbg_name = "LzHistory.clear";
return $lzsc$temp
})();
LzHistory.setPersist = (function  () {
var $lzsc$temp = 
/* -*- file: services/platform/dhtml/LzHistory.js#177.24 -*- */
function  () {
Debug.warn("History persistence is not implemented in DHTML.")
}
$lzsc$temp._dbg_name = "LzHistory.setPersist";
return $lzsc$temp
})();
Lz.__dhtmlhistoryready = true;

/* -*- file: services/LzCSSStyle.js#18.1 -*- */
function LzCSSStyleRule () {

}
LzCSSStyleRule.prototype.properties = null;
LzCSSStyleRule.prototype.selector = null;
LzCSSStyleRule.prototype.specificity = 0;
LzCSSStyleRule.prototype.getSpecificity = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#26.43 -*- */
function  () {
if (!this.specificity) {
if (this.parsed.type == LzCSSStyle._sel_compound) {
for (var $1_i = 0;
$1_i < this.parsed.length;
$1_i++) {
this.specificity += LzCSSStyle.getSelectorSpecificity(this.parsed[$1_i])
}
} else {
this.specificity = LzCSSStyle.getSelectorSpecificity(this.parsed)
}
}
return this.specificity
}
$lzsc$temp._dbg_name = "LzCSSStyleRule.prototype.getSpecificity";
return $lzsc$temp
})();
LzCSSStyleRule._getSimpleSelectorSpecificity = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#52.48 -*- */
function  ($1_selarr, $2_k) {
var $3_sel = $1_selarr[$2_k];
if ($2_k == "simpleselector") {
return 1
}
if ($3_sel.indexOf("#") >= 0) {
return 100
}
if ($3_sel.indexOf("[") >= 0) {
return 10
}
return 1
}
$lzsc$temp._dbg_name = "LzCSSStyleRule._getSimpleSelectorSpecificity";
return $lzsc$temp
})();
var LzCSSStyle = {}
LzCSSStyle.getComputedStyle = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#77.31 -*- */
function  ($1_node) {
var $2_csssd = new LzCSSStyleDeclaration();
$2_csssd.setNode($1_node);
return $2_csssd
}
$lzsc$temp._dbg_name = "LzCSSStyle.getComputedStyle";
return $lzsc$temp
})();
LzCSSStyle.__LZRuleCache = {}
LzCSSStyle.__LZPropertyCache = {}
LzCSSStyle.getPropertyValueFor = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#89.34 -*- */
function  ($1_initialnode, $2_pname) {
if (!$1_initialnode || !$2_pname) {
return
}
var $3_node = $1_initialnode;
while ($3_node != canvas) {
var $4_uid = $3_node.__LZUID;
var $5_val = this.__LZPropertyCache[$4_uid + $2_pname];
if ($5_val != null) {
return $5_val
}
var $6_rules = this.__LZRuleCache[$4_uid];
if (!$6_rules) {
$6_rules = new Array();
var $7_r;
for (var $8_i = this._rules.length - 1;
$8_i >= 0;
$8_i--) {
$7_r = this._rules[$8_i];
var $9_rp = $7_r.parsed;
if ($9_rp.type == this._sel_compound) {
var $10_curnode = $3_node;
var $11_sindex = $9_rp.length - 1;
var $12_firstone = true;
var $13_result = false;
while ($10_curnode != canvas) {
var $14_nrp = $9_rp[$11_sindex];
t = $14_nrp.type;
if (t == this._sel_star || t == this._sel_id && $14_nrp.id == $10_curnode.id || t == this._sel_tag && ($14_nrp.classname in lz && $10_curnode instanceof lz[$14_nrp.classname] || $14_nrp.classname in global && $10_curnode instanceof global[$14_nrp.classname]) || t == this._sel_attribute && $10_curnode[$14_nrp.attrname] == $14_nrp.attrvalue || t == this._sel_tagAndAttr && $10_curnode[$14_nrp.attrname] == $14_nrp.attrvalue && ($14_nrp.classname in lz && $10_curnode instanceof lz[$14_nrp.classname] || $14_nrp.classname in global && $10_curnode instanceof global[$14_nrp.classname]) || t == this._sel_compound && this._compoundSelectorApplies($14_nrp, $10_curnode, true)) {
if ($11_sindex-- == 0) {
$13_result = true;
break
}
} else {
if ($12_firstone) {
$13_result = false;
break
}
}
$10_curnode = $10_curnode.parent;
$12_firstone = false
}
if ($13_result) {
$6_rules.push($7_r)
}
} else {
if ($9_rp.type == this._sel_star || $9_rp.type == this._sel_id && $9_rp.id == $3_node.id || $9_rp.type == this._sel_tag && ($9_rp.classname in lz && $3_node instanceof lz[$9_rp.classname] || $9_rp.classname in global && $3_node instanceof global[$9_rp.classname]) || $9_rp.type == this._sel_attribute && $3_node[$9_rp.attrname] == $9_rp.attrvalue || $9_rp.type == this._sel_tagAndAttr && $3_node[$9_rp.attrname] == $9_rp.attrvalue && ($9_rp.classname in lz && $3_node instanceof lz[$9_rp.classname] || $9_rp.classname in global && $3_node instanceof global[$9_rp.classname])) {
$6_rules.push($7_r)
}
}
}
var $15_pprules = $3_node.name != null ? this._nameRules[$3_node.name] : null;
if ($15_pprules) {
for (var $8_i = $15_pprules.length - 1;
$8_i >= 0;
$8_i--) {
$7_r = $15_pprules[$8_i];
var $9_rp = $7_r.parsed;
if ($9_rp.type == this._sel_compound) {
var $10_curnode = $3_node;
var $11_sindex = $9_rp.length - 1;
var $12_firstone = true;
var $13_result = false;
while ($10_curnode != canvas) {
var $14_nrp = $9_rp[$11_sindex];
t = $14_nrp.type;
if (t == this._sel_star || t == this._sel_id && $14_nrp.id == $10_curnode.id || t == this._sel_tag && ($14_nrp.classname in lz && $10_curnode instanceof lz[$14_nrp.classname] || $14_nrp.classname in global && $10_curnode instanceof global[$14_nrp.classname]) || t == this._sel_attribute && $10_curnode[$14_nrp.attrname] == $14_nrp.attrvalue || t == this._sel_tagAndAttr && $10_curnode[$14_nrp.attrname] == $14_nrp.attrvalue && ($14_nrp.classname in lz && $10_curnode instanceof lz[$14_nrp.classname] || $14_nrp.classname in global && $10_curnode instanceof global[$14_nrp.classname]) || t == this._sel_compound && this._compoundSelectorApplies($14_nrp, $10_curnode, true)) {
if ($11_sindex-- == 0) {
$13_result = true;
break
}
} else {
if ($12_firstone) {
$13_result = false;
break
}
}
$10_curnode = $10_curnode.parent;
$12_firstone = false
}
if ($13_result) {
$6_rules.push($7_r)
}
} else {
if ($9_rp.type == this._sel_star || $9_rp.type == this._sel_id && $9_rp.id == $3_node.id || $9_rp.type == this._sel_tag && ($9_rp.classname in lz && $3_node instanceof lz[$9_rp.classname] || $9_rp.classname in global && $3_node instanceof global[$9_rp.classname]) || $9_rp.type == this._sel_attribute && $3_node[$9_rp.attrname] == $9_rp.attrvalue || $9_rp.type == this._sel_tagAndAttr && $3_node[$9_rp.attrname] == $9_rp.attrvalue && ($9_rp.classname in lz && $3_node instanceof lz[$9_rp.classname] || $9_rp.classname in global && $3_node instanceof global[$9_rp.classname])) {
$6_rules.push($7_r)
}
}
}
}
$6_rules.sort(this.__compareSpecificity);
this.__LZRuleCache[$4_uid] = $6_rules
}
var $16_l = $6_rules.length;
var $8_i = 0;
while ($8_i < $16_l) {
var $17_props = $6_rules[$8_i++].properties;
if ($2_pname in $17_props) {
$5_val = $17_props[$2_pname];
this.__LZPropertyCache[$4_uid + $2_pname] = $5_val;
break
}
}
if ($5_val != null) {
return $5_val
}
$3_node = $3_node.immediateparent
}
Debug.warn("No CSS value found for node %#w for property name %s", $1_initialnode, $2_pname)
}
$lzsc$temp._dbg_name = "LzCSSStyle.getPropertyValueFor";
return $lzsc$temp
})();
LzCSSStyle.getSelectorSpecificity = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#247.37 -*- */
function  ($1_parsedsel) {
switch ($1_parsedsel.type) {
case this._sel_tag:
case this._sel_star:
return 1;
case this._sel_id:
return 100;
case this._sel_attribute:
return 10;
case this._sel_tagAndAttr:
return 11;

}
}
$lzsc$temp._dbg_name = "LzCSSStyle.getSelectorSpecificity";
return $lzsc$temp
})();
LzCSSStyle.__compareSpecificity = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#274.35 -*- */
function  ($1_rA, $2_rB) {
if (!$1_rA.specificity) {
if (!$1_rA.specificity) {
if ($1_rA.parsed.type == LzCSSStyle._sel_compound) {
for (var $3_i = 0;
$3_i < $1_rA.parsed.length;
$3_i++) {
switch ($1_rA.parsed[$3_i].type) {
case LzCSSStyle._sel_tag:
case LzCSSStyle._sel_star:
$1_rA.specificity += 1;
break;
case LzCSSStyle._sel_id:
$1_rA.specificity += 100;
break;
case LzCSSStyle._sel_attribute:
$1_rA.specificity += 10;
break;
case LzCSSStyle._sel_tagAndAttr:
$1_rA.specificity += 11;
break;

}
}
} else {
switch ($1_rA.parsed.type) {
case LzCSSStyle._sel_tag:
case LzCSSStyle._sel_star:
$1_rA.specificity = 1;
break;
case LzCSSStyle._sel_id:
$1_rA.specificity = 100;
break;
case LzCSSStyle._sel_attribute:
$1_rA.specificity = 10;
break;
case LzCSSStyle._sel_tagAndAttr:
$1_rA.specificity = 11;
break;

}
}
}
}
if (!$2_rB.specificity) {
if (!$2_rB.specificity) {
if ($2_rB.parsed.type == LzCSSStyle._sel_compound) {
for (var $3_i = 0;
$3_i < $2_rB.parsed.length;
$3_i++) {
switch ($2_rB.parsed[$3_i].type) {
case LzCSSStyle._sel_tag:
case LzCSSStyle._sel_star:
$2_rB.specificity += 1;
break;
case LzCSSStyle._sel_id:
$2_rB.specificity += 100;
break;
case LzCSSStyle._sel_attribute:
$2_rB.specificity += 10;
break;
case LzCSSStyle._sel_tagAndAttr:
$2_rB.specificity += 11;
break;

}
}
} else {
switch ($2_rB.parsed.type) {
case LzCSSStyle._sel_tag:
case LzCSSStyle._sel_star:
$2_rB.specificity = 1;
break;
case LzCSSStyle._sel_id:
$2_rB.specificity = 100;
break;
case LzCSSStyle._sel_attribute:
$2_rB.specificity = 10;
break;
case LzCSSStyle._sel_tagAndAttr:
$2_rB.specificity = 11;
break;

}
}
}
}
var $4_specificityA = $1_rA.specificity;
var $5_specificityB = $2_rB.specificity;
if ($4_specificityA == $5_specificityB) {
if ($1_rA.parsed.type == LzCSSStyle._sel_compound && $2_rB.parsed.type == LzCSSStyle._sel_compound) {
for (var $3_i = 0;
$3_i < $1_rA.parsed.length;
$3_i++) {
if (!$1_rA.parsed[$3_i] || !$2_rB.parsed[$3_i]) {
break
}
if (!$1_rA.parsed[$3_i].classname || !$2_rB.parsed[$3_i].classname || $1_rA.parsed[$3_i].classname == $2_rB.parsed[$3_i].classname) {
continue
}
var $6_rac = lz[$1_rA.parsed[$3_i].classname];
var $7_rbc = lz[$2_rB.parsed[$3_i].classname];
return $6_rac && $7_rbc && "prototype" in $6_rac && $6_rac.prototype instanceof $7_rbc ? -1 : 1
}
}
if ($1_rA.parsed.classname && $2_rB.parsed.classname && $1_rA.parsed.classname != $2_rB.parsed.classname) {
var $6_rac = lz[$1_rA.parsed.classname];
var $7_rbc = lz[$2_rB.parsed.classname];
return $6_rac && $7_rbc && "prototype" in $6_rac && $6_rac.prototype instanceof $7_rbc ? -1 : 1
} else {
return $1_rA._lexorder < $2_rB._lexorder ? 1 : -1
}
}
return $4_specificityA < $5_specificityB ? 1 : -1
}
$lzsc$temp._dbg_name = "LzCSSStyle.__compareSpecificity";
return $lzsc$temp
})();
LzCSSStyle._printRuleArray = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#446.30 -*- */
function  ($1_arr) {
for (var $2_i = 0;
$2_i < $1_arr.length;
$2_i++) {
Debug.write($2_i, $1_arr[$2_i])
}
}
$lzsc$temp._dbg_name = "LzCSSStyle._printRuleArray";
return $lzsc$temp
})();
LzCSSStyle._compoundSelectorApplies = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#456.39 -*- */
function  ($1_rp, $2_node) {
var $3_curnode = $2_node;
var $4_sindex = $1_rp.length - 1;
var $5_firstone = true;
var $6_result = false;
while ($3_curnode != canvas) {
var $7_nrp = $1_rp[$4_sindex];
var $8_t = $7_nrp.type;
if ($8_t == this._sel_star || $8_t == this._sel_id && $7_nrp.id == $3_curnode.id || $8_t == this._sel_tag && ($7_nrp.classname in lz && $3_curnode instanceof lz[$7_nrp.classname] || $7_nrp.classname in global && $3_curnode instanceof global[$7_nrp.classname]) || $8_t == this._sel_attribute && $3_curnode[$7_nrp.attrname] == $7_nrp.attrvalue || $8_t == this._sel_tagAndAttr && $3_curnode[$7_nrp.attrname] == $7_nrp.attrvalue && ($7_nrp.classname in lz && $3_curnode instanceof lz[$7_nrp.classname] || $7_nrp.classname in global && $3_curnode instanceof global[$7_nrp.classname]) || $8_t == this._sel_compound && this._compoundSelectorApplies($7_nrp, $3_curnode, true)) {
if ($4_sindex-- == 0) {
$6_result = true;
break
}
} else {
if ($5_firstone) {
$6_result = false;
break
}
}
$3_curnode = $3_curnode.parent;
$5_firstone = false
}
return $6_result
}
$lzsc$temp._dbg_name = "LzCSSStyle._compoundSelectorApplies";
return $lzsc$temp
})();
LzCSSStyle._sel_unknown = 0;
LzCSSStyle._sel_star = 1;
LzCSSStyle._sel_id = 2;
LzCSSStyle._sel_tag = 3;
LzCSSStyle._sel_compound = 4;
LzCSSStyle._sel_attribute = 5;
LzCSSStyle._sel_tagAndAttr = 6;
LzCSSStyle._rules = new Array();
LzCSSStyle._nameRules = {}
LzCSSStyle._rulenum = 0;
LzCSSStyle._addRule = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#517.23 -*- */
function  ($1_r) {
$1_r._lexorder = this._rulenum++;
var $2_sel = $1_r.selector;
$1_r.parsed = null;
var $3_lastsel;
if ($2_sel instanceof Array) {
$1_r.parsed = [];
$1_r.parsed.type = this._sel_compound;
for (var $4_i = 0;
$4_i < $2_sel.length;
$4_i++) {
$1_r.parsed.push(this._parseSelector($2_sel[$4_i]))
}
$3_lastsel = $1_r.parsed[$1_r.parsed.length - 1]
} else {
$1_r.parsed = this._parseSelector($2_sel);
$3_lastsel = $1_r.parsed
}
if (($3_lastsel.type == this._sel_attribute || $3_lastsel.type == this._sel_tagAndAttr) && $3_lastsel.attrname == "name") {
var $5_aval = $3_lastsel.attrvalue;
if (!this._nameRules[$5_aval]) {
this._nameRules[$5_aval] = []
}
this._nameRules[$5_aval].push($1_r)
} else {
this._rules.push($1_r)
}
}
$lzsc$temp._dbg_name = "LzCSSStyle._addRule";
return $lzsc$temp
})();
LzCSSStyle._parseSelector = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#551.29 -*- */
function  ($1_sel) {
switch (typeof $1_sel) {
case "object":
if ($1_sel.simpleselector) {
$1_sel.type = this._sel_tagAndAttr;
$1_sel.classname = this._normalizeClassname($1_sel.simpleselector)
} else {
$1_sel.type = this._sel_attribute
}
return $1_sel;
break;
case "string":
return this._parseStringSelector($1_sel);
break;

}
}
$lzsc$temp._dbg_name = "LzCSSStyle._parseSelector";
return $lzsc$temp
})();
LzCSSStyle._parseStringSelector = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#568.35 -*- */
function  ($1_sel) {
var $2_parsed = {}
if ($1_sel == "*") {
$2_parsed.type = this._sel_star
} else {
var $3_index = $1_sel.indexOf("#");
if ($3_index >= 0) {
$2_parsed.id = $1_sel.substring($3_index + 1);
$2_parsed.type = this._sel_id
} else {
$2_parsed.type = this._sel_tag;
$2_parsed.classname = this._normalizeClassname($1_sel)
}
}
return $2_parsed
}
$lzsc$temp._dbg_name = "LzCSSStyle._parseStringSelector";
return $lzsc$temp
})();
LzCSSStyle._normalizeClassname = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#597.34 -*- */
function  ($1_cn) {
switch ($1_cn) {
case "view":
return "LzView";
case "animator":
return "LzAnimator";
case "animatorgroup":
return "LzAnimatorGroup";
case "canvas":
return "LzCanvas";
case "drawview":
return "LzDrawView";
case "inputtext":
return "LzInputText";
case "layout":
return "LzLayout";
case "node":
return "LzNode";
case "state":
return "LzState";
case "text":
return "LzText";
default:
return $1_cn;

}
}
$lzsc$temp._dbg_name = "LzCSSStyle._normalizeClassname";
return $lzsc$temp
})();

/* -*- file: services/LzCSSStyle.js#617.1 -*- */
function LzCSSStyleDeclaration () {

}
LzCSSStyleDeclaration.prototype._node = null;
LzCSSStyleDeclaration.prototype.getPropertyValue = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#622.52 -*- */
function  ($1_pname) {
return LzCSSStyle.getPropertyValueFor(this._node, $1_pname)
}
$lzsc$temp._dbg_name = "LzCSSStyleDeclaration.prototype.getPropertyValue";
return $lzsc$temp
})();
LzCSSStyleDeclaration.prototype.setNode = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyle.js#626.43 -*- */
function  ($1_node) {
this._node = $1_node
}
$lzsc$temp._dbg_name = "LzCSSStyleDeclaration.prototype.setNode";
return $lzsc$temp
})();

/* -*- file: services/LzStyleSheet.js#29.1 -*- */
function LzStyleSheet ($1_title, $2_href, $3_media, $4_sstype) {
this.type = $4_sstype;
this.disabled = false;
this.ownerNode = null;
this.parentStyleSheet = null;
this.href = $2_href;
this.title = $1_title;
this.media = $3_media
}

/* -*- file: services/LzCSSStyleSheet.js#28.1 -*- */
function LzCSSStyleSheet ($1_title, $2_href, $3_media, $4_sstype, $5_ownerRule, $6_cssRules) {
this.type = $4_sstype;
this.disabled = false;
this.ownerNode = null;
this.parentStyleSheet = null;
this.href = $2_href;
this.title = $1_title;
this.media = $3_media;
this.ownerRule = $5_ownerRule;
this.cssRules = $6_cssRules
}
LzCSSStyleSheet.prototype.insertRule = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzCSSStyleSheet.js#71.40 -*- */
function  ($1_rule, $2_index) {
if (!this.cssRules) {
this.cssRules = []
}
if ($2_index < 0) {
return null
}
if ($2_index < this.cssRules.length) {
this.cssRules.splice($2_index, 0, $1_rule);
return $2_index
}
if ($2_index == this.cssRules.length) {
return this.cssRules.push($1_rule) - 1
}
return null
}
$lzsc$temp._dbg_name = "LzCSSStyleSheet.prototype.insertRule";
return $lzsc$temp
})();
var LzTrackClass = Class.make("LzTrackClass", null, {__LZreg: new Object(), __LZactivegroups: null, __LZtrackDel: null, __LZoptimizeforaxis: "x", __LZmouseupDel: null, initialize: 
/* -*- file: services/LzTrack.lzs#125.1 -*- */
function initialize () {
(arguments.callee.superclass ? arguments.callee.superclass.prototype["initialize"] : this.nextMethod(arguments.callee, "initialize")).apply(this, arguments);
this.__LZtrackDel = new LzDelegate(this, "__LZtrack");
this.__LZmouseupDel = new LzDelegate(this, "__LZmouseup", LzGlobalMouse, "onmouseup")
}, register: 
/* -*- file: services/LzTrack.lzs#141.1 -*- */
function register ($1_v, $2_group) {
if ($1_v == null || $2_group == null) {
return
}
if (typeof this.__LZreg[$2_group] == "undefined") {
this.__LZreg[$2_group] = [];
this.__LZreg[$2_group].__LZlasthit = 0
}
this.__LZreg[$2_group].push($1_v);
if (!this.__LZdestroydel) {
this.__LZdestroydel = new LzDelegate(this, "__LZdestroyitem")
}
this.__LZdestroydel.register($1_v, "ondestroy")
}, unregister: 
/* -*- file: services/LzTrack.lzs#161.1 -*- */
function unregister ($1_v, $2_group) {
if ($1_v == null || $2_group == null) {
return
}
var $3_reglist = this.__LZreg[$2_group];
if ($3_reglist) {
for (var $4_i = 0;
$4_i < $3_reglist.length;
$4_i++) {
if ($3_reglist[$4_i] == $1_v) {
$3_reglist.splice($4_i, 1)
}
}
if ($3_reglist.length == 0) {
delete this.__LZreg[$2_group]
}
}
this.__LZdestroydel.unregisterFrom($1_v.ondestroy)
}, __LZdestroyitem: 
/* -*- file: services/LzTrack.lzs#183.1 -*- */
function __LZdestroyitem ($1_v) {
for (var $2_i in this.__LZreg) {
this.unregister($1_v, $2_i)
}
}, activate: 
/* -*- file: services/LzTrack.lzs#193.1 -*- */
function activate ($1_group) {
if (this.__LZactivegroups == null) {
this.__LZactivegroups = [];
this.__LZtrackDel.register(LzIdle, "onidle")
}
var $2_found = false;
for (var $3_i in this.__LZactivegroups) {
if (this.__LZactivegroups[$3_i] == this.__LZreg[$1_group]) {
$2_found = true
}
}
if (!$2_found) {
this.__LZactivegroups.push(this.__LZreg[$1_group])
}
}, deactivate: 
/* -*- file: services/LzTrack.lzs#212.1 -*- */
function deactivate ($1_group) {
for (var $2_i in this.__LZactivegroups) {
if (this.__LZactivegroups[$2_i] == this.__LZreg[$1_group]) {
this.__LZactivegroups.splice($2_i, 1)
}
}
if (this.__LZactivegroups == []) {
this.__LZtrackDel.unregisterAll()
}
if (typeof this.__LZreg[$1_group] != "undefined") {
this.__LZreg[$1_group].__LZlasthit = 0
}
}, __LZtopview: 
/* -*- file: services/LzTrack.lzs#228.1 -*- */
function __LZtopview ($1_a, $2_b) {
var $3_btemp = $2_b;
var $4_atemp = $1_a;
while ($4_atemp.nodeLevel < $3_btemp.nodeLevel) {
$3_btemp = $3_btemp.immediateparent;
if ($3_btemp == $1_a) {
return $2_b
}
}
while ($3_btemp.nodeLevel < $4_atemp.nodeLevel) {
$4_atemp = $4_atemp.immediateparent;
if ($4_atemp == $2_b) {
return $1_a
}
}
while ($4_atemp.immediateparent != $3_btemp.immediateparent) {
$4_atemp = $4_atemp.immediateparent;
$3_btemp = $3_btemp.immediateparent
}
if ($4_atemp.getZ() > $3_btemp.getZ()) {
return $1_a
} else {
return $2_b
}
}, __LZfindTopmost: 
/* -*- file: services/LzTrack.lzs#254.1 -*- */
function __LZfindTopmost ($1_vlist) {
var $2_top = $1_vlist[0];
for (var $3_i = 1;
$3_i < $1_vlist.length;
$3_i++) {
$2_top = this.__LZtopview($2_top, $1_vlist[$3_i])
}
return $2_top
}, __LZtrackgroup: 
/* -*- file: services/LzTrack.lzs#268.1 -*- */
function __LZtrackgroup ($1_group, $2_hitlist) {
for (var $3_i = 0;
$3_i < $1_group.length;
$3_i++) {
var $4_v = $1_group[$3_i];
if ($4_v.visible) {
var $5_vx = $4_v.getMouse("x");
if ($5_vx > 0 && $5_vx < $4_v.width) {
var $6_vy = $4_v.getMouse("y");
if ($6_vy > 0 && $6_vy < $4_v.height) {
$2_hitlist.push($4_v)
}
}
}
}
}, __LZtrack: 
/* -*- file: services/LzTrack.lzs#295.1 -*- */
function __LZtrack () {
var $1_found = false;
var $2_foundviews = [];
for (var $3_i in this.__LZactivegroups) {
var $4_hitlist = [];
var $5_thisgroup = this.__LZactivegroups[$3_i];
if ($5_thisgroup) {
this.__LZtrackgroup($5_thisgroup, $4_hitlist)
}
if (!$4_hitlist.length && $5_thisgroup && $5_thisgroup.__LZlasthit) {
if ($5_thisgroup.__LZlasthit.onmousetrackout && $5_thisgroup.__LZlasthit.onmousetrackout.ready) {
$5_thisgroup.__LZlasthit.onmousetrackout.sendEvent($5_thisgroup.__LZlasthit)
}
$5_thisgroup.__LZlasthit = 0
} else {
var $6_fd = this.__LZfindTopmost($4_hitlist);
if ($6_fd && $6_fd != $5_thisgroup.__LZlasthit) {
if ($5_thisgroup.__LZlasthit.onmousetrackout && $5_thisgroup.__LZlasthit.onmousetrackout.ready) {
$5_thisgroup.__LZlasthit.onmousetrackout.sendEvent($5_thisgroup.__LZlasthit)
}
$1_found = true;
$2_foundviews.push($6_fd);
$5_thisgroup.__LZlasthit = $6_fd
}
}
}
if ($1_found) {
for (var $3_i = 0;
$3_i < $2_foundviews.length;
$3_i++) {
var $7_v = $2_foundviews[$3_i];
if ($7_v.onmousetrackover.ready) {
$7_v.onmousetrackover.sendEvent($7_v)
}
}
}
}, __LZmouseup: 
/* -*- file: services/LzTrack.lzs#335.1 -*- */
function __LZmouseup () {
for (var $1_i in this.__LZactivegroups) {
var $2_thisgroup = this.__LZactivegroups[$1_i];
if ($2_thisgroup && $2_thisgroup.__LZlasthit.onmousetrackup && $2_thisgroup.__LZlasthit.onmousetrackup.ready) {
if (this["__LZlastmouseup"] == $2_thisgroup.__LZlasthit) {
this.__LZlastmouseup = null
} else {
$2_thisgroup.__LZlasthit.onmousetrackup.sendEvent(this.__LZlasthit);
this.__LZlastmouseup = $2_thisgroup.__LZlasthit
}
}
}
}}, null);
var LzTrack = new LzTrackClass();
var LzFocus = new Object();
DeclareEvent(LzFocus, "onfocus");
LzFocus.lastfocus = null;
LzFocus.csel = null;
LzFocus.upDel = new LzDelegate(LzFocus, "gotKeyUp", LzKeys, "onkeyup");
LzFocus.downDel = new LzDelegate(LzFocus, "gotKeyDown", LzKeys, "onkeydown");
LzFocus.focuswithkey = null;
LzFocus.__LZskipblur = false;
LzFocus.__LZsfnextfocus = -1;
LzFocus.__LZsfrunning = false;
LzFocus.gotKeyUp = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#58.20 -*- */
function  ($1_kC) {
if (this.csel && this.csel.onkeyup.ready) {
this.csel.onkeyup.sendEvent($1_kC)
}
}
$lzsc$temp._dbg_name = "LzFocus.gotKeyUp";
return $lzsc$temp
})();
LzFocus.gotKeyDown = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#66.22 -*- */
function  ($1_kC) {
if (this.csel && this.csel.onkeydown.ready) {
this.csel.onkeydown.sendEvent($1_kC)
}
if ($1_kC == LzKeys.keyCodes.tab) {
if (LzKeys.isKeyDown("shift")) {
this.prev()
} else {
this.next()
}
}
}
$lzsc$temp._dbg_name = "LzFocus.gotKeyDown";
return $lzsc$temp
})();
LzFocus.setNextKey = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#82.22 -*- */
function  ($1_k) {
Debug.error("Next key can no longer be set.")
}
$lzsc$temp._dbg_name = "LzFocus.setNextKey";
return $lzsc$temp
})();
LzFocus.setPrevKey = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#91.22 -*- */
function  ($1_k) {
Debug.error("Prev key can no longer be set.")
}
$lzsc$temp._dbg_name = "LzFocus.setPrevKey";
return $lzsc$temp
})();
LzFocus.__LZcheckFocusChange = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#100.32 -*- */
function  ($1_v) {
if ($1_v && $1_v.focusable) {
this.setFocus($1_v, false)
}
}
$lzsc$temp._dbg_name = "LzFocus.__LZcheckFocusChange";
return $lzsc$temp
})();
LzFocus.setFocus = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#122.20 -*- */
function  ($1_newsel) {
if (this.__LZsfrunning) {
this.__LZsfnextfocus = $1_newsel;
return
}
if (this.cseldest == $1_newsel) {
return
}
if (this.csel && this.csel.shouldYieldFocus && !this.csel.shouldYieldFocus()) {
return
}
var $2_prevsel = this.csel;
if ($2_prevsel) {
$2_prevsel.blurring = true
}
this.__LZsfnextfocus = -1;
this.__LZsfrunning = true;
if ($1_newsel && !$1_newsel.focusable) {
$1_newsel = this.getNext($1_newsel)
}
this.cseldest = $1_newsel;
if (arguments[1] != null) {
this.focuswithkey = arguments[1]
}
if (!this.__LZskipblur) {
this.__LZskipblur = true;
if (this.csel && this.csel.onblur.ready) {
this.csel.onblur.sendEvent($1_newsel)
}
if (this.__LZsfnextfocus != -1) {
this.__LZsfrunning = false;
this.setFocus(this.__LZsfnextfocus);
return
}
}
this.lastfocus = this.csel;
this.csel = $1_newsel;
this.__LZskipblur = false;
if ($1_newsel && $1_newsel.onfocus.ready) {
$1_newsel.onfocus.sendEvent($1_newsel)
}
if (this.__LZsfnextfocus != -1) {
this.__LZsfrunning = false;
this.setFocus(this.__LZsfnextfocus);
return
}
if (this.onfocus.ready) {
this.onfocus.sendEvent($1_newsel)
}
this.__LZsfrunning = false;
if (this.__LZsfnextfocus != -1) {
this.setFocus(this.__LZsfnextfocus);
return
}
if ($2_prevsel) {
$2_prevsel.blurring = false
}
}
$lzsc$temp._dbg_name = "LzFocus.setFocus";
return $lzsc$temp
})();
LzFocus.clearFocus = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#204.22 -*- */
function  () {
this.setFocus(null)
}
$lzsc$temp._dbg_name = "LzFocus.clearFocus";
return $lzsc$temp
})();
LzFocus.getFocus = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#212.20 -*- */
function  () {
return this.csel
}
$lzsc$temp._dbg_name = "LzFocus.getFocus";
return $lzsc$temp
})();
LzFocus.next = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#219.16 -*- */
function  () {
this.genMoveSelection(1)
}
$lzsc$temp._dbg_name = "LzFocus.next";
return $lzsc$temp
})();
LzFocus.getNext = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#229.19 -*- */
function  ($1_focusview) {
if (!$1_focusview) {
$1_focusview = this.csel
}
return this.moveSelSubview($1_focusview, 1)
}
$lzsc$temp._dbg_name = "LzFocus.getNext";
return $lzsc$temp
})();
LzFocus.getPrev = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#240.19 -*- */
function  ($1_focusview) {
if (!$1_focusview) {
$1_focusview = this.csel
}
return this.moveSelSubview($1_focusview, -1)
}
$lzsc$temp._dbg_name = "LzFocus.getPrev";
return $lzsc$temp
})();
LzFocus.prev = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#249.16 -*- */
function  () {
this.genMoveSelection(-1)
}
$lzsc$temp._dbg_name = "LzFocus.prev";
return $lzsc$temp
})();
LzFocus.genMoveSelection = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#256.28 -*- */
function  ($1_movedir) {
var $2_sel = this.csel;
var $3_check = $2_sel;
while ($2_sel && $3_check != canvas) {
if (!$3_check.visible) {
$2_sel = null
}
$3_check = $3_check.immediateparent
}
if ($2_sel == null) {
$2_sel = LzModeManager.getModalView()
}
var $4_v = null;
if ($2_sel && $2_sel["get" + ($1_movedir == 1 ? "Next" : "Prev") + "Selection"]) {
$4_v = $2_sel["get" + ($1_movedir == 1 ? "Next" : "Prev") + "Selection"]()
}
if ($4_v == null) {
$4_v = this.moveSelSubview($2_sel, $1_movedir)
}
if (!LzModeManager.__LZallowFocus($4_v)) {
return
}
this.setFocus($4_v, true)
}
$lzsc$temp._dbg_name = "LzFocus.genMoveSelection";
return $lzsc$temp
})();
LzFocus.accumulateSubviews = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#292.30 -*- */
function  ($1_accum, $2_v, $3_include, $4_top) {
if ($2_v == $3_include || $2_v.focusable && $2_v.visible) {
$1_accum.push($2_v)
}
if ($4_top || !$2_v.focustrap && $2_v.visible) {
for (var $5_i = 0;
$5_i < $2_v.subviews.length;
$5_i++) {
this.accumulateSubviews($1_accum, $2_v.subviews[$5_i], $3_include)
}
}
}
$lzsc$temp._dbg_name = "LzFocus.accumulateSubviews";
return $lzsc$temp
})();
LzFocus.moveSelSubview = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzFocus.lzs#311.26 -*- */
function  ($1_v, $2_mvdir) {
var $3_root = $1_v || canvas;
while (!$3_root.focustrap && $3_root.immediateparent && $3_root != $3_root.immediateparent) {
$3_root = $3_root.immediateparent
}
var $4_focusgroup = [];
this.accumulateSubviews($4_focusgroup, $3_root, $1_v, true);
var $5_index = -1;
for (var $6_i in $4_focusgroup) {
if ($4_focusgroup[$6_i] == $1_v) {
$5_index = Number($6_i);
break
}
}
if ($5_index == -1 && $2_mvdir == -1) {
$5_index = 0
}
$5_index = ($5_index + $2_mvdir + $4_focusgroup.length) % $4_focusgroup.length;
return $4_focusgroup[$5_index]
}
$lzsc$temp._dbg_name = "LzFocus.moveSelSubview";
return $lzsc$temp
})();
var LzIdleClass = Class.make("LzIdleClass", null, {coi: [], removeCOI: null, initialize: 
/* -*- file: services/LzIdle.lzs#41.1 -*- */
function initialize () {
this.removeCOI = new LzDelegate(this, "removeCallIdleDelegates")
}, callOnIdle: 
/* -*- file: services/LzIdle.lzs#51.1 -*- */
function callOnIdle ($1_d) {
this.coi.push($1_d);
if (!("regNext" in this && this.regNext)) {
this.regNext = true;
this.removeCOI.register(this, "onidle")
}
}, removeCallIdleDelegates: 
/* -*- file: services/LzIdle.lzs#62.1 -*- */
function removeCallIdleDelegates ($1_t) {
var $2_arr = this.coi;
this.coi = new Array();
for (var $3_i = 0;
$3_i < $2_arr.length;
$3_i++) {
$2_arr[$3_i].execute($1_t)
}
if (this.coi.length == 0) {
this.removeCOI.unregisterFrom(this.onidle);
this.regNext = false
}
}, toString: 
/* -*- file: services/LzIdle.lzs#81.1 -*- */
function toString () {
return "LzIdle"
}}, null);
(function  () {
var $lzsc$temp = function  () {
with (LzIdleClass) {
with (LzIdleClass.prototype) {
DeclareEvent(prototype, "onidle")
}
}
}
$lzsc$temp._dbg_name = "Compiler.substitute#0/1";
return $lzsc$temp
})()();
var LzIdle = new LzIdleClass();

/* -*- file: services/LzIdle.lzs#100.1 -*- */
function __idleupdate () {
var $1_oi = LzIdle.onidle;
if ($1_oi.ready) {
$1_oi.sendEvent(getTimer())
}
}
LzIdleKernel.addCallback(this, "__idleupdate");
var LzTimerClass = Class.make("LzTimerClass", null, {timerList: new Object(), addTimer: 
/* -*- file: services/LzTimer.lzs#69.1 -*- */
function addTimer ($1_d, $2_milisecs) {
var p = {"delegate": $1_d}
var $3_f = (function  () {
var $lzsc$temp = 
/* -*- file: services/LzTimer.lzs#71.13 -*- */
function  () {
LzTimer.removeTimerWithID(p.delegate, p.id);
p.delegate.execute(new Date().getTime())
}
$lzsc$temp._dbg_name = "services/LzTimer.lzs#71/13";
return $lzsc$temp
})();
var $4_id = setInterval($3_f, $2_milisecs);
if ($4_id instanceof Array) {
Debug.error("setInterval result type is unexpected; LzTimer will fail")
}
p.id = $4_id;
var $5_tle = this.timerList[$1_d];
if ($5_tle == null) {
this.timerList[$1_d] = $4_id
} else {
if (!($5_tle instanceof Array)) {
this.timerList[$1_d] = [$5_tle, $4_id]
} else {
$5_tle.push($4_id)
}
}
return $4_id
}, removeTimer: 
/* -*- file: services/LzTimer.lzs#107.1 -*- */
function removeTimer ($1_d) {
var $2_tle = this.timerList[$1_d];
var $3_id = null;
if ($2_tle != null) {
if ($2_tle instanceof Array) {
$3_id = $2_tle.shift();
clearInterval($3_id);
if ($2_tle.length == 0) {
delete this.timerList[$1_d]
}
} else {
$3_id = $2_tle;
clearInterval($3_id);
delete this.timerList[$1_d]
}
}
return $3_id
}, removeTimerWithID: 
/* -*- file: services/LzTimer.lzs#134.1 -*- */
function removeTimerWithID ($1_d, $2_id) {
var $3_tle = this.timerList[$1_d];
if ($3_tle != null) {
if ($3_tle instanceof Array) {
var $4_i = 0;
for ($4_i = 0;
$4_i < $3_tle.length;
$4_i++) {
var $5_id2 = $3_tle[$4_i];
if ($5_id2 == $2_id) {
clearInterval($2_id);
$3_tle.splice($4_i, 1);
break
}
}
if ($3_tle.length == 0) {
delete this.timerList[$1_d]
}
} else {
if ($3_tle == $2_id) {
clearInterval($2_id);
delete this.timerList[$1_d]
}
}
}
}, resetTimer: 
/* -*- file: services/LzTimer.lzs#166.1 -*- */
function resetTimer ($1_d, $2_milisecs) {
this.removeTimer($1_d);
return this.addTimer($1_d, $2_milisecs)
}, countTimers: 
/* -*- file: services/LzTimer.lzs#172.3 -*- */
function countTimers ($1_d) {
var $2_tle = this.timerList[$1_d];
if ($2_tle == null) {
return 0
} else {
if ($2_tle instanceof Array) {
return $2_tle.length
} else {
return 1
}
}
}}, null);
var LzTimer = new LzTimerClass();

/* -*- file: glue/LaszloInitiator.lzs#59.1 -*- */
function LzInstantiateView ($1_e, $2_tn) {
if ($1_e.name == "trait") {
new LzTrait($1_e);
return
}
if (typeof $2_tn == "undefined") {
$2_tn = 1
}
canvas.initiatorAddNode($1_e, $2_tn)
}

/* -*- file: glue/LaszloInitiator.lzs#76.1 -*- */
function lzAddLocalData ($1_name, $2_d, $3_trimwhitespace) {
return new LzDataset(canvas, {name: $1_name, initialdata: $2_d, trimwhitespace: $3_trimwhitespace})
}

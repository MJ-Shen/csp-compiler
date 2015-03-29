# csp-compiler

This is a compiler for clojure server page which transform csp to clj and load it;

## Installation

Add the following dependency to your project.clj file:

[csp-compiler "0.1.1"]

## Usage

Here is Demo
(ns hello-world.core
  (:use [csp-compiler.core :only [csp]]))
  (csp "PATH/XXX.csp");please put csp file under "resources" for war installing

## Dependencies
	[me.raynes/fs "1.4.6"](https://github.com/Raynes/fs)
	
## License

Copyright © 2015 MJ-Shen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

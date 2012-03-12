# `exceptional-clj`

`exceptional-clj` is a GetExceptional.com client for Clojure.

## Usage

Reporting an exception to GetExceptional.com is simple.  Simply call: `(exceptional-clj.core/handler exceptional-api-key exception)`.  

Sample usage:

	(try (/ 1 0)
		 (catch Exception e (exceptional-clj.core/handler api-key e)))

## License

Copyright (C) 2012 Newfound Research, LLC

Distributed under the Eclipse Public License, the same as Clojure.

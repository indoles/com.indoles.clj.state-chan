# state-chan

A Clojure library that implements a very simple state machine,
controlled by async channels. The basic idea is to send functions as
messages on a channel. The receiving end of the channel invokes those
functions. The functions take one arg, the current state, and return
the next state.

## Usage

(def m (init {}))
(respond-to m #(assoc %1 :key "value"))
(state m)
(stop m)


## License

Copyright © 2014 Indoles

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

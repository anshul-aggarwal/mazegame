# RMI Experiment

The rmiregistry is only needed for bootstraping process, nothing else. That is, to get the initial Tracker Remote Object, we will have to use the registry. 

Rest of the Players can be stored inside Tracker (as remote objects).

The most important thing to note is the "Stub". 

```
The Stub/Skeleton hides the communication details away from the developer. 
The Stub is the class that implements the remote interface. 
It serves as a client-side placeholder for the remote object. 
The stub communicates with the server-side skeleton. 
The skeleton is the stub's counterpart on server-side. Both communicate via the network. 
The skeleton actually knows the real remote objects delegates the stub's request to it and returns the response to the stub. 
You require both as they are the essential building blocks for RMI.
```

For eg:

```Obj a = new Obj();```
If I register object a with Tracker now, lets say ```tracker.add(a)```, this object will be copied and just kept in tracker. You wont be able to do anything on it.

However,
```Obj a = new Obj();
Obj stub = (Obj) UnicastRemoteObject.exportObject(a, port);
```
This creates a stub object *(It's still object a, but with additional details of how to communicate on the port provided)*. Now if do, ```tracker.add(stub)```, this stub will be registerd on tracker, along with the hidden code of how to communicate with this object on the port provided. Thus making communication super easy to implement.

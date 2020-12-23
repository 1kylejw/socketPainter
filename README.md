# socketPainter

---- From cover page for original coursework ----

●	Painter GUI
○	The Painter GUI was implemented almost exactly how it was described in the program overview. I added a rectangle, the color black and a clear button due to my family asking me to do so.

●	Painter Button / Interaction
○	The buttons were all implemented as described and do as you would expect them to do. The interaction with the canvas is smooth and works perfectly, no errors or exceptions thrown when running the canvas

●	Text Chat Window
○	The text chat utilizes 3 things: A JTextField, a button, and an array of labels. The labels store up to 4 messages. The way it works is that you type your message in the JTextField, press the button and the message is sent to the server as a class that stores the name of the person who sent the message and the message itself. All the clients then receive the message from the server and add it to the array of labels. The client then moves the text of each label to the label in front of it. The server also stores the current messages and loads them onto any new client connecting for the first time.

●	Basic Hub and Socket Connections
○	The hub creates a server socket, then accepts sockets from the connecting clients. Then the server creates an objectinputstream and objectoutputstream for the socket and forks off a thread for each socket to a handler class.

●	Threads to handle Sockets
○	The handler class then reads for objects from the client and checks the instance of each object, depending on the object the handler then runs a helper method to deal with each different object which sends the object back to each of the clients
■	If the instance is a primitive, it adds the primitive to an ArrayList and sends each primitive to the rest of the clients
■	The chat message adds the latest 4 messages to an array and sends the message to all the clients.

●	No Double Drawing
○	The program does not double draw.

●	Disconnects
○	Closes the sockets on exit on both the client and server end. No errors

●	Extra Credit
○	I implemented the mouse drag preview of the shape. I also implemented other things such as another shape, another color, and a clear button.

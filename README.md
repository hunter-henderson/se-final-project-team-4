# Team 4: UNO Implementation in Java
## CSCI 4490 Software Engineering
The final project for CSCI 4490 Software Engineering, which is an implementation of Uno using Java.

## SQL Set-Up Instructions
This program will access your MySQL tables through a user `student` with a password `hello`. Please ensure that you have this created, or change the information in `db.properties` accordingly.
Once you have the `db.properties` file set-up to your liking, launch your MySQL and run the `uno.sql` script located in the **Scripts** folder of this repository. This will create the required tables for the program to run.

## Run Instructions
Remember to set-up your Run Configuration and Debug Configuration, and to launch things in the following order:
1. Start your MySQL Server. In this implementation, we used xampp.
2. In the root folder, edit the `config.txt` file to contain the desired IP address of the server and the desired port with a space as a delimiter. So, if your desired IP is 192.168.0.82 and your desired port is 8300, the file contents would be `192.168.0.82 8300`. Be sure to save once you're done editing.
3. Run main class in GameGUI (or launch uno.bat in the `batFiles` folder).
4. You can choose to either **Host** or **Join** a game. If you have not created an account or inserted information into the corresponding SQL table, be sure to do so with the **Create Account** button. Note that you cannot create an account with the same username as another account.
5. If you are **Hosting** a game, repeat Step 4 in another client. If you are **Joining** a game, make sure that the Host's IP address and port is correctly listed in the `config.txt` file. The program will output the IP address and port it read from the config.txt file to the console.
6. Once you have joined a game and there are two or more players, have the host start it. Have fun!

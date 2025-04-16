import socket
import threading
import json
import logging  # pip install logging
import mysql.connector  # pip install mysql-connector-python
import os  # check if directory exists
import sys
import stat
import time
import ssl

HOST = '0.0.0.0'
PORT = 8000
FILE_DIRECTORY_PATH = './File_Storage/'
FILE_DIRECTORY_PATH_SHARED = '/Shared/'
FILE_DIRECTORY_PATH_OWNER = '/Owner/'
# context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="File_Server"
)
db = conn.cursor()

loggingPath = './Logging/LogFile.log'
logging.basicConfig(filename=loggingPath,
                    level=logging.DEBUG,
                    format='{"Time":"%(asctime)s", %(message)s}',
                    datefmt="%Y-%m-%d %H:%M:%S")


class FileServer:
    def __init__(self, host=HOST, port=PORT):
        self.host = HOST
        self.port = PORT
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def start(self):
        self.server_socket.bind((self.host, self.port))
        self.server_socket.listen()
        logging.info('"Action": "Server Started", "IP": "%s", "Status" : "Success", "Message": "Port = %s"' % (
            self.host, self.port))
        print(f"Server is listening on {self.host}:{self.port}")

        while True:
            # client_socket: socket object for this specific client
            # address: tuple containing (ip_address, port) of the client
            client_socket, address = self.server_socket.accept()
            print(f"Connected with {address}")
            logging.info('"Action": "Client Connected", "IP": "%s", "Status" : "Success"' % (
                address[0]))

            # Create and start a new thread to handle this client
            # This allows us to handle multiple clients simultaneously
            client_thread = threading.Thread(
                target=self.handle_client_request,
                args=(client_socket,)
            )
            client_thread.start()

    def handle_client_request(self, client_socket):
        # Get client's IP address
        client_address = client_socket.getpeername()[0]
        while True:
            try:
                recv_data = client_socket.recv(1024)
                message = json.loads(recv_data.decode())
                # print(f"Received message from {client_address}: {message}")
                request = message['request']
                match request:
                    case 'login':
                        self.handle_login(client_socket, message)
                    case 'register':
                        self.handle_register(client_socket, message)
                    case 'reset_password':
                        self.handle_reset_password(client_socket, message)
                    case 'logout':
                        self.handle_logout(client_socket, message)
                    case 'list_files':
                        self.handle_list_files(client_socket, message)
                    case 'upload_file':
                        self.handle_upload_file(client_socket, message)
                    case 'delete_file':
                        self.handle_delete_file(client_socket, message)
                    case 'get_file_data':
                        self.handle_get_file_data(client_socket, message)
                    case 'edit_file':
                        self.handle_edit_file(client_socket, message)
                    case 'share_file':
                        self.handle_share_file(client_socket, message)
                    case 'download_file':
                        self.handle_download_file(client_socket, message)
                    case 'get_logging':
                        self.handle_get_logging(client_socket, message)
                    case 'get_public_key':
                        self.handle_get_public_key(client_socket, message)
                    case 'getSalt':
                        self.getSalt(client_socket, message)
                    case 'disconnect':
                        break
                    case _:
                        print("Invalid request")

            except Exception as e:
                print(f"Error handling client request: {e}")
                break

        print(f"Connection with {client_address} closed")
        logging.info('"Action": "Client Disconnected", "IP": "%s", "Status" : "Success"' % (
            client_address))
        client_socket.close()

    def getSalt(self, client_socket, message):
        try:
            username = message['username']
            db.execute("SELECT salt FROM Users WHERE username = %s", (username))
            result = db.fetchone()
            if result:
                salt = result[0].decode('utf-8')
                response = {'status': True, 'salt': salt,
                            'username': username}
            else:
                response = {'status': False}
            client_socket.send(json.dumps(response).encode())
        except Exception as e:
            print(f"Error finding salt: {e}")

    def handle_login(self, client_socket, loginValue):
        client_address = client_socket.getpeername()[0]
        try:
            username = loginValue['username']
            password = loginValue['password']
            db.execute("SELECT username, role, lastLoginTime FROM Users WHERE username = %s AND password = %s",
                       (username, password))
            result = db.fetchone()
            if result:
                role = result[1]
                lastLoginTime = str(result[2])
                currentTime = time.strftime('%Y-%m-%d %H:%M:%S')

                db.execute("UPDATE Users SET lastLoginTime = %s WHERE username = %s",
                           (currentTime, username))
                response = {'status': True, 'role': role, 'lastLoginTime': lastLoginTime,
                            'username': username}
                if role == 'admin':
                    logging.info('"Action": "Admin Login", "IP": "%s", "Username": "%s", "Status": "Success"',
                                 client_address, username)
                else:
                    logging.info('"Action": "User Login", "IP": "%s", "Username": "%s", "Status": "Success"',
                                 client_address, username)
                conn.commit()
            else:
                response = {'status': False}
                logging.info('"Action": "Login", "IP": "%s", "Username": "%s", "Status": "Failed", "Message": "Invalid Username or Password"',
                             client_address, username)
            client_socket.send(json.dumps(response).encode())

        except Exception as e:
            print(f"Error handling client login: {e}")
            conn.rollback()

    def handle_register(self, client_socket, registerValue):
        client_address = client_socket.getpeername()[0]
        while True:
            try:
                username = registerValue['username']
                password = registerValue['password']
                salt = registerValue['salt']
                db.execute("SELECT username FROM Users WHERE username = %s",
                           (username,))
                result = db.fetchone()
                if result:
                    raise Exception("UsernameExists")

                else:
                    client_socket.send('UsernameAccept'.encode())

                    keyLength = int(client_socket.recv(1024).decode())

                    client_socket.send('Ready'.encode())

                    publicKey = b''
                    while len(publicKey) < keyLength:
                        buffer = client_socket.recv(1024)
                        publicKey += buffer

                    db.execute("INSERT INTO Users (username, password, salt, publicKey) VALUES (%s, %s, %s, %s)",
                               (username, password, salt, publicKey))

                    result = 'Success'
                    logging.info('"Action": "User Register", "IP": "%s", "Username": "%s", "Status" : "Success"' % (
                        client_address, username))
                    conn.commit()

                client_socket.send(result.encode())
                break

            except Exception as e:
                print(f"Error handling client register: {e}")
                client_socket.send(str(e).encode())
                logging.info('"Action": "User Register", "IP": "%s", "Status" : "Failed", "Message" : "%s"' % (
                    client_address, e))
                conn.rollback()
                break

    def handle_logout(self, client_socket, logoutValue):
        client_address = client_socket.getpeername()[0]
        username = logoutValue['username']
        try:
            logging.info('"Action": "User Logout", "IP": "%s", "Username": "%s", "Status" : "Success"' % (
                client_address, username))
            client_socket.send("Success".encode())
        except Exception as e:
            logging.info('"Action": "User Logout", "IP": "%s", "Username": "%s", "Status" : "Failed", "Message" : "%s"' % (
                client_address, username, e))
            print(f"Error handling client logout: {e}")

    def handle_list_files(self, client_socket, listValue):
        username = listValue['username']
        try:
            db.execute("SELECT fileName, fileOwner, filePath, fileModifyTime, fileUploadTime FROM Files WHERE fileOwner = %s",
                       (username,))  # get the owner files
            filesArray = db.fetchall()  # append to the filesArray

            db.execute("SELECT fileID FROM Permissions WHERE username = %s",
                       (username,))  # get the shared files
            sharedFilesFileID = db.fetchall()
            for file in sharedFilesFileID:  # get the shared files detail and append to the filesArray
                db.execute("SELECT fileName, fileOwner, filePath, fileModifyTime, fileUploadTime FROM Files WHERE fileID = %s",
                           (file[0],))
                filesArray.append(db.fetchone())

            files = []
            for file in filesArray:  # check the file exists in the os for each file
                # check path exists
                userFilePath = file[2]
                if file[1] != username:
                    userFilePath = FILE_DIRECTORY_PATH + username + \
                        FILE_DIRECTORY_PATH_SHARED + file[1] + '/' + file[0]

                # if file not exists in os, delete the record from db
                if not os.path.exists(userFilePath):
                    print(
                        f"User: {username}, File '{file[0]}' not found in {userFilePath}")
                    db.execute("SELECT fileID FROM Files WHERE fileName = %s AND fileOwner = %s",
                               (file[0], username))
                    fileID = db.fetchone()
                    if fileID:
                        db.execute("DELETE FROM Permissions WHERE fileID = %s",
                                   (fileID[0],))
                        db.execute("DELETE FROM Files WHERE fileID = %s",
                                   (fileID[0],))
                        conn.commit()
                    continue

                db.execute("SELECT FileID FROM Files WHERE fileName = %s AND fileOwner = %s",
                           (file[0], file[1]))
                fileID = db.fetchone()[0]
                db.execute("SELECT username FROM Permissions WHERE fileID = %s",
                           (fileID,))
                sharedUsers = db.fetchall()
                # convert to list ['ABC', 'DEF', 'GHI']
                sharedUsers = [user[0] for user in sharedUsers]
                # convert to string 'ABC, DEF, GHI'
                sharedUsers = ', '.join(sharedUsers)

                modified_Since_time = time.strftime(
                    "%Y-%m-%d %H:%M:%S", file[3].timetuple())
                upload_time = time.strftime(
                    "%Y-%m-%d %H:%M:%S", file[4].timetuple())
                file = list(file)
                file[2] = sharedUsers
                file[3] = modified_Since_time  # modified time
                file[4] = upload_time  # upload time

                files.append(file)

            files = json.dumps(files)
            client_socket.send(files.encode())

        except Exception as e:
            print(f"Error handling client list_files: {e}")

    def handle_upload_file(self, client_socket, fileValue):
        client_address = client_socket.getpeername()[0]
        username = fileValue['username']
        fileName = fileValue['filename']
        overwrite = fileValue['overwrite']
        file_size = fileValue['file_size']
        try:
            db.execute(
                "SELECT fileName FROM Files WHERE fileName = %s AND fileOwner = %s",
                (fileName, username))
            result = db.fetchone()
            if result and not overwrite:
                raise Exception("FileExists")
            elif not result or overwrite:
                client_socket.send("Ready".encode())

                filedata = b''
                while len(filedata) < file_size:
                    buffer = client_socket.recv(1024)
                    if not buffer:
                        break
                    filedata += buffer

                user_directory_path = FILE_DIRECTORY_PATH + username + FILE_DIRECTORY_PATH_OWNER
                if not os.path.exists(user_directory_path):
                    os.makedirs(user_directory_path)

                userFilePath = user_directory_path + fileName
                with open(userFilePath, "wb") as f:
                    f.write(filedata)

                db.execute("INSERT INTO Files (fileName, fileOwner, filePath, fileSize) VALUES (%s, %s, %s, %s)",
                           (fileName, username, userFilePath, file_size))

                conn.commit()

                db.execute("SELECT fileID FROM Files WHERE fileName = %s AND fileOwner = %s",
                           (fileName, username))
                fileID = db.fetchone()[0]
                logging.info('"Action": "File Upload", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success"' % (
                    client_address, username, fileName, fileID))

                client_socket.send("Success".encode())

        except Exception as e:
            print(f"Error handling client upload_file: {e}")
            logging.info('"Action": "File Upload", "IP": "%s", "Username": "%s", "Filename": "%s", "Status" : "Failed", "Message" : "%s"' % (
                client_address, username, fileName, e))
            client_socket.send(str(e).encode())

    def handle_delete_file(self, client_socket, fileValue):
        client_address = client_socket.getpeername()[0]
        fileName = fileValue['filename']
        permission = fileValue['permission']
        fileOwner = fileValue['fileOwner']
        username = fileValue['username']
        fileID = ''
        try:
            # get the fileID
            db.execute("SELECT fileID FROM Files WHERE fileName = %s AND fileOwner = %s",
                       (fileName, fileOwner))
            result = db.fetchone()
            fileID = result[0]
            if fileOwner != username and not permission:
                # File Permission Withdrawn
                try:
                    db.execute(
                        "DELETE FROM Permissions WHERE fileID = %s AND username = %s", (fileID, username))
                    userFilePath = FILE_DIRECTORY_PATH + username + \
                        FILE_DIRECTORY_PATH_SHARED + fileOwner + '/' + fileName
                    os.remove(userFilePath)
                    conn.commit()
                    logging.info('"Action": "File Permission Withdrawn", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success"' % (
                        client_address, username, fileName, fileID))

                    client_socket.send("Success".encode())
                    return
                except Exception as e:
                    print(
                        f"Error handling client File Permission Withdrawn : {e}")
                    logging.info('"Action": "File Permission Withdrawn", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Failed", "Message" : "%s"' % (
                        client_address, username, fileName, fileID, e))
                    conn.rollback()
                    client_socket.send("Error".encode())

            # Delete the shared file from the permissions table
            db.execute("DELETE FROM Permissions WHERE fileID = %s", (fileID,))
            db.execute("DELETE FROM Files WHERE fileID = %s", (fileID,))

            # Delete the file from the user file
            userFilePath = FILE_DIRECTORY_PATH + username + \
                FILE_DIRECTORY_PATH_OWNER + fileName
            os.remove(userFilePath)

            # Delete the file from the shared user file
            db.execute("SELECT username FROM Permissions WHERE fileID = %s",
                       (fileID,))
            sharedUsers = db.fetchall()
            for sharedUser in sharedUsers:
                sharedUser = sharedUser[0]
                userFilePath = FILE_DIRECTORY_PATH + sharedUser + \
                    FILE_DIRECTORY_PATH_SHARED + username + '/' + fileName
                os.remove(userFilePath)

            conn.commit()
            logging.info('"Action": "File Delete", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success"' % (
                client_address, username, fileName, fileID))

            client_socket.send("Success".encode())

        except Exception as e:
            print(f"Error handling client delete_file: {e}")
            logging.info('"Action": "File Delete", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Failed" , "Message" : "%s"' % (
                client_address, username, fileName, fileID, e))
            conn.rollback()

            client_socket.send(str(e).encode())

    def handle_download_file(self, client_socket, fileValue):
        client_address = client_socket.getpeername()[0]
        username = fileValue['username']
        fileName = fileValue['filename']
        fileID = ''
        try:
            # get the file size
            db.execute("SELECT fileSize, filePath, fileID FROM Files WHERE fileName = %s AND fileOwner = %s",
                       (fileName, username))
            result = db.fetchone()
            fileSize = result[0]
            filePath = result[1]
            fileID = result[2]
            if not result:
                client_socket.send("notFound".encode())
                return
            client_socket.send(str(fileSize).encode())

            # get start signal to send the file
            signal = client_socket.recv(1024).decode()
            if signal == "Ready":
                # send the file
                with open(filePath, "rb") as f:
                    filedata = f.read()
                    client_socket.sendall(filedata)

                logging.info('"Action": "File Download", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success"' % (
                    client_address, username, fileName, fileID))

        except Exception as e:
            logging.info('"Action": "File Download", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Failed", "Message" : "%s"' % (
                client_address, username, fileName, fileID, e))
            print(f"Error handling client download_file: {e}")

    def handle_get_file_data(self, client_socket, fileValue):
        client_address = client_socket.getpeername()[0]
        username = fileValue['username']
        fileName = fileValue['filename']
        fileOwner = fileValue['fileOwner']
        fileID = ''
        try:

            db.execute("SELECT fileID, filePath, fileSize FROM Files WHERE fileName = %s AND fileOwner = %s",
                       (fileName, fileOwner))
            result = db.fetchone()
            if not result:
                raise Exception("FileNotFound")
            fileID = result[0]
            filePath = result[1]
            fileSize = str(result[2])
            if fileOwner != username:
                filePath = FILE_DIRECTORY_PATH + username + \
                    FILE_DIRECTORY_PATH_SHARED + fileOwner + '/' + fileName
            if not os.path.exists(filePath):
                raise Exception("FileNotFound")

            client_socket.send(fileSize.encode())
            if fileSize == "0":
                return
            signal = client_socket.recv(1024).decode()
            print(f'signal: {signal}')
            if signal != "Ready":
                raise Exception("SignalNotReady")
            with open(filePath, "rb") as f:
                fileData = f.read()
                client_socket.sendall(fileData)
            logging.info('"Action": "File Get Data", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success"' % (
                client_address, username, fileName, fileID))

        except Exception as e:
            logging.info('"Action": "File Get Data", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Failed", "Message" : "%s"' % (
                client_address, username, fileName, fileID, e))
            print(f"Error handling client get_file_data: {e}")
            client_socket.send(str(e).encode())

    def handle_share_file(self, client_socket, fileValue):
        client_address = client_socket.getpeername()[0]
        fileOwner = fileValue['fileOwner']
        fileShareTo = fileValue['fileShareTo']
        filename = fileValue['filename']
        try:
            db.execute("SELECT username FROM Users WHERE username = %s",
                       (fileShareTo,))
            result = db.fetchone()
            if not result:
                raise Exception("UserNotFound")

            db.execute("SELECT fileID, filePath, fileSize FROM Files WHERE fileName = %s AND fileOwner = %s",
                       (filename, fileOwner))
            result = db.fetchone()
            fileID = result[0]
            filePath = result[1]
            fileSize = result[2]
            if not os.path.exists(filePath):
                raise Exception("FileNotFound")

            db.execute("SELECT fileID FROM Permissions WHERE fileID = %s AND username = %s",
                       (fileID, fileShareTo))
            result = db.fetchone()
            if result:
                db.execute("DELETE FROM Permissions WHERE fileID = %s AND username = %s",
                           (fileID, fileShareTo))
                userFilePath = FILE_DIRECTORY_PATH + fileShareTo + \
                    FILE_DIRECTORY_PATH_SHARED + fileOwner + '/' + filename
                os.remove(userFilePath)
                conn.commit()
                client_socket.send("Unshared".encode())
                logging.info('"Action": "File Unshare", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success", "Message" : "Unshared To \'%s\'"' % (
                    client_address, fileOwner, filename, fileID, fileShareTo))
                return

            client_socket.send(str(fileSize).encode())

            signal = client_socket.recv(1024).decode()
            if signal != "Ready":  # send the file
                raise Exception("SignalNotReady")

            # pass to owner to decrypt the file using owner's private key
            with open(filePath, "rb") as f:
                owner_encrypted_filedata = f.read()
                client_socket.sendall(owner_encrypted_filedata)

            # Get the encrypted file data from the owner,
            # which is encrypted using the share target's public key
            shared_encrypted_filedata = b''
            while len(shared_encrypted_filedata) < fileSize:
                buffer = client_socket.recv(1024)
                shared_encrypted_filedata += buffer

            # write the encrypted file data to the shared user's directory
            fileShareTo_directory_path = FILE_DIRECTORY_PATH + \
                fileShareTo + FILE_DIRECTORY_PATH_SHARED + fileOwner + '/'
            if not os.path.exists(fileShareTo_directory_path):
                os.makedirs(fileShareTo_directory_path)
            fileShareToFilePath = fileShareTo_directory_path + filename
            with open(fileShareToFilePath, "wb") as f:
                f.write(shared_encrypted_filedata)

            db.execute("INSERT INTO Permissions (fileID, username) VALUES (%s, %s)",
                       (fileID, fileShareTo))

            conn.commit()
            logging.info('"Action": "File Share", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success", "Message" : "Shared To \'%s\'"' % (
                client_address, fileOwner, filename, fileID, fileShareTo))
            client_socket.send("Shared".encode())

        except Exception as e:
            logging.info('"Action": "File Share", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Failed", "Message" : "Share To \'%s\', Error: %s"' % (
                client_address, fileOwner, filename, fileID, fileShareTo, e))
            print(f"Error handling client share_file: {e}")
            conn.rollback()
            client_socket.send(str(e).encode())

    def handle_edit_file(self, client_socket, fileValue):
        client_address = client_socket.getpeername()[0]
        username = fileValue['username']
        filename = fileValue['filename']
        newFilename = fileValue['newFilename']
        fileSize = fileValue['fileSize']
        fileSharedTo = fileValue['fileSharedTo']

        try:
            db.execute("SELECT fileID, filePath FROM Files WHERE fileName = %s AND fileOwner = %s",
                       (filename, username))
            result = db.fetchone()
            if not result:
                raise Exception("FileNotFound")
            fileID = result[0]
            filePath = result[1]
            if not os.path.exists(filePath):
                raise Exception("FileNotFound")
            db.execute("SELECT filename FROM Files WHERE fileOwner = %s",
                       (username,))
            files = db.fetchall()
            for file in files:
                if file[0] == newFilename and file[0] != filename:
                    raise Exception("FileNameExists")

            client_socket.send("Ready".encode())

            filedata = b''
            while len(filedata) < fileSize:
                buffer = client_socket.recv(1024)
                filedata += buffer

            user_directory_path = FILE_DIRECTORY_PATH + username + FILE_DIRECTORY_PATH_OWNER
            os.remove(filePath)
            newFilePath = user_directory_path + newFilename
            with open(newFilePath, "wb") as f:
                f.write(filedata)

            db.execute("UPDATE Files SET fileName = %s, filePath = %s, fileModifyTime = CURRENT_TIMESTAMP WHERE fileID = %s",
                       (newFilename, newFilePath, fileID))
            client_socket.send("SuccessOwner".encode())

            # update the file to the shared user's directory
            for user in fileSharedTo:
                if user == '':
                    continue
                encrypted_data = b''
                while len(encrypted_data) < fileSize:
                    buffer = client_socket.recv(1024)
                    encrypted_data += buffer

                user_directory_path = FILE_DIRECTORY_PATH + user + \
                    FILE_DIRECTORY_PATH_SHARED + username + '/'
                if not os.path.exists(user_directory_path):
                    os.makedirs(user_directory_path)
                userFilePath = user_directory_path + newFilename
                with open(userFilePath, "wb") as f:
                    f.write(encrypted_data)

            conn.commit()
            logging.info('"Action": "File Edit", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Success"' % (
                client_address, username, filename, fileID))
            client_socket.send("Success".encode())

        except Exception as e:
            logging.info('"Action": "File Edit", "IP": "%s", "Username": "%s", "Filename": "%s", "FileID" : "%s", "Status" : "Failed", "Message" : "%s"' % (
                client_address, username, filename, fileID, e))
            print(f"Error handling client edit_file: {e}")
            client_socket.send(str(e).encode())
            conn.rollback()

    def handle_reset_password(self, client_socket, resetValue):
        client_address = client_socket.getpeername()[0]
        username = resetValue['username']
        password = resetValue['password']
        try:
            db.execute(
                "UPDATE users SET password = %s WHERE username = %s", (password, username))
            result = db.fetchall()
            if result:
                response = {'status': True, 'username': username}
                logging.info('"Action": "Reset Password", "IP": "%s", "Username": "%s", "Status" : "Success"' % (
                    client_address, username))
            else:
                response = "username_not_found"
                logging.info('"Action": "Reset Password", "IP": "%s", "Username": "%s", "Status" : "Failed"' % (
                    client_address, username))
            client_socket.send(json.dumps(response).encode())
        except Exception as e:
            logging.info('"Action": "Reset Password", "IP": "%s", "Username": "%s", "Status" : "Failed"' % (
                client_address, username))
            print(f"Error handling client download_file: {e}")
        pass

    def handle_get_logging(self, client_socket, loggingValue):
        client_address = client_socket.getpeername()[0]
        username = loggingValue['username']
        try:
            with open(loggingPath, 'r') as f:
                logs = f.read()
                f.close()
            logging_length = len(logs)
            # send the length of the logging file
            client_socket.send(str(logging_length).encode())

            # get start signal to send the file
            signal = client_socket.recv(1024).decode()
            if signal != "Ready":
                return
            # send the file
            client_socket.send(logs.encode())

            logging.info('"Action": "Get Logging", "IP": "%s", "Username": "%s", "Status" : "Success"' % (
                client_address, username))
        except Exception as e:
            print(f"Error handling client get_logging: {e}")
            logging.info('"Action": "Get Logging", "IP": "%s", "Username": "%s", "Status" : "Failed" , "Message" : "%s"' % (
                client_address, username, e))
            client_socket.send("Error".encode())

    def handle_get_public_key(self, client_socket, publicKeyValue):
        client_address = client_socket.getpeername()[0]
        username = publicKeyValue['username']
        try:
            db.execute("SELECT publicKey FROM Users WHERE username = %s",
                       (username,))
            result = db.fetchone()
            if not result:
                raise Exception("UserNotFound")
            publicKey = result[0]
            publicKey_length = len(publicKey)
            # send the length of the public key
            client_socket.send(str(publicKey_length).encode())
            # get start signal to send the key
            signal = client_socket.recv(1024).decode()
            if signal != "Ready":
                return
            # send the key
            client_socket.send(publicKey)

            logging.info('"Action": "Get Public Key", "IP": "%s", "Username": "%s", "Status" : "Success"' % (
                client_address, username))
        except Exception as e:
            print(f"Error handling client get_public_key: {e}")
            logging.info('"Action": "Get Public Key", "IP": "%s", "Username": "%s", "Status" : "Failed", "Message" : "%s"' % (
                client_address, username, e))
            client_socket.send(str(e).encode())


if __name__ == "__main__":
    server = FileServer()
    server.start()

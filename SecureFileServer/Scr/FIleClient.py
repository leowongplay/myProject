import json
import threading
import socket
import tkinter as tk  # pip install tk
from tkinter import filedialog, messagebox, simpledialog, ttk, font
import base64
from base64 import b32encode
from Crypto import Random
from Crypto.PublicKey import RSA  # pip install pycryptodome
from Crypto.Hash import SHA
from Crypto.Cipher import PKCS1_v1_5 as PKCS1_cipher
import os
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad

from bcrypt import gensalt, hashpw  # pip install bcrypt
import pyotp
import qrcode  # pip install qrcode
from qrcode.image.pure import PyPNGImage
from PIL import Image, ImageTk  # pip install pillow
import ssl

hostname = socket.gethostname()
# get the IP address of the server, which is localhost in this case
IPAddr = socket.gethostbyname(hostname)
SERVER_HOST = IPAddr  # change this!
SERVER_PORT = 8000
KeyStoragePath = './Key_Storage/'
context = ssl.create_default_context()


class FileClient:
    def __init__(self, host, port):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect((host, port))

    def start(self):
        # Start the GUI event loop.
        self.LoginGUI()

    def getOTP(self, username):
        try:
            salt = self.giveSalt(username).encode('utf-8')
            transform = b32encode(salt)[:32]
            totp_url = pyotp.TOTP(transform).provisioning_uri(
                name=username + '@google.com', issuer_name='File Storage App')
            return totp_url
        except BrokenPipeError:
            self.login_status.config(
                text="Connection lost. Please try again.", fg="#FF6666")

    def verifyOTP(self, username, input):
        try:
            salt = self.giveSalt(username).encode('utf-8')
            transform = b32encode(salt)[:32]
            totp = pyotp.TOTP(transform)
            if (totp.verify(input)):
                messagebox.showinfo("Success", "Welcome")
                self.show_files()
            else:
                messagebox.showinfo(
                    "Fail", "The verification code is incorrect.\nPlease try again.")
                return
        except BrokenPipeError:
            self.login_status.config(
                text="Connection lost. Please try again.", fg="#FF6666")

    def giveSalt(self, username):
        try:
            getSalt = json.dumps(
                {'request': 'getSalt', 'username': (username,)}
            )
            self.socket.send(getSalt.encode())
            response = json.loads(self.socket.recv(1024).decode())
            if not response or response['status'] == False:
                self.login_status.config(
                    text="Wrong username or password.\nLogin failed. Try again.", fg="#FF6666")
                return
            salt = response.get('salt')
            return salt
        except BrokenPipeError:
            self.login_status.config(
                text="Connection lost. Please try again.", fg="#FF6666")

    def login(self, username, password):
        if not username or not password:
            self.login_status.config(
                text="Please enter a username and password", fg="#FF6666")
            return
        try:
            salt = self.giveSalt(username)
            password = self.encrypt_password(password, salt)

            loginValue = json.dumps(
                {'request': 'login', 'username': username, 'password': password})
            self.socket.send(loginValue.encode())

            message = json.loads(self.socket.recv(1024).decode())
            if not message or message['status'] == False:
                self.login_status.config(
                    text="Wrong username or password.\nLogin failed. Try again.", fg="#FF6666")
                return
            # Set default role to 'user' if not provided

            self.role = message.get('role', 'user')
            self.lastLoginTime = message.get('lastLoginTime', '')
            self.username = username
            self.OTP_UI()

            welcome_message = "Login successful, welcome " + self.role + " '" + username + "'"
            self.login_status.config(text=welcome_message, fg="green")
            self.showMessageBox("Login Successful", welcome_message)
            if self.baseLoginFrame.winfo_exists:  # if self.login_window exists, hide it
                self.baseLoginFrame.destroy()
            self.show_files()

        except BrokenPipeError:
            self.login_status.config(
                text="Connection lost. Please try again.", fg="#FF6666")

        except socket.error as e:
            self.login_status.config(
                text=f"Error handling client login: {str(e)}", fg="#FF6666")

        except tk.TclError as e:
            pass

    def register(self, username, password1, password2):
        if not username or not password1 or not password2:
            self.option_status.config(
                text="Please enter a username and both password", fg="#FF6666")
            return

        # username must be in between 5 and 20 characters long
        if len(username) < 5:
            self.option_status.config(
                text="Username must be at least 5 characters long", fg="#FF6666")
            return

        if len(username) > 20:
            self.option_status.config(
                text="Username must be at most 20 characters long", fg="#FF6666")
            return

        # password1 and password2 must match
        if password1 != password2:
            self.option_status.config(
                text="Passwords do not match", fg="#FF6666")
            return

        # password must be at least 12 characters long
        if len(password1) < 12:
            self.option_status.config(
                text="Password must be at least 12 characters long", fg="#FF6666")
            return

        # username must contain only letters, numbers, and underscores
        if not username.isalnum():
            self.option_status.config(
                text="Username must contain only letters, numbers, and underscores", fg="#FF6666")
            return
        try:
            salt = gensalt().decode('utf-8')
            encrypted_password = self.encrypt_password(password1, salt)
            registerValue = json.dumps(
                {'request': 'register', 'username': username, 'password': encrypted_password, 'salt': salt})
            self.socket.send(registerValue.encode())
            message = self.socket.recv(1024).decode()
            if message == "UsernameExists":
                self.option_status.config(
                    text="Username exists, try another username.", fg="#FF6666")
                return
            self.lastLoginTime = 'N/A'
            # generate RSA key pair and send public key to server
            self.publicKey = self.genRSAKeyPair(username)
            self.socket.send(str(len(self.publicKey)).encode())
            # server is ready to receive public key
            if self.socket.recv(1024).decode() == "Ready":
                # send public key to server to store in database
                self.socket.send(self.publicKey)
                if self.socket.recv(1024).decode() == "Success":
                    self.showMessageBox(
                        "Registration Successful", "Registration successful, welcome " + username)
                    self.username = username  # set the username
                    self.role = 'user'  # set the role to user by default
                    self.OTP_QR_UI()  # show the OTP QR code
                    if self.baseLoginFrame.winfo_exists:  # if self.login_window exists, hide it
                        self.baseLoginFrame.destroy()
                    self.show_files()

        except BrokenPipeError:
            self.option_status.config(
                text="Connection lost. Please try again.", fg="#FF6666")

        except socket.error as e:
            self.option_status.config(
                text=f"Error handling client registration: {str(e)}", fg="#FF6666")

    def reset_password(self, username, password1, password2, verifyCode):
        if not username or not password1 or not password2:
            self.option_status.config(
                text="Please enter a username and both password", fg="#FF6666")
            return

        # password must be at least 12 characters long
        if len(password1) < 12:
            self.option_status.config(
                text="Password must be at least 12 characters long", fg="#FF6666")
            return

        # password1 and password2 must match
        if password1 != password2:
            self.option_status.config(
                text="Passwords do not match", fg="#FF6666")
            return

        try:
            self.username = username
            salt = self.giveSalt(username)
            transform = b32encode(salt.encode('utf-8'))[:32]
            totp = pyotp.TOTP(transform)
            if (totp.verify(verifyCode)):
                encrypted_password = self.encrypt_password(password1, salt)
                resetValue = json.dumps(
                    {'request': 'reset_password', 'username': username, 'password': encrypted_password})
                self.socket.send(resetValue.encode())
                while True:
                    message = self.socket.recv(1024).decode()
                    if message == "username_not_found":
                        self.option_status.config(
                            text="Username not found, try another username.", fg="#FF6666")
                        return

                    self.showMessageBox(
                        "Password Reset Successful", "Password reset successful for " + username)
                    self.LoginGUI()
                    break
            else:
                self.option_status.config(
                    text="Verification Code Incorrect. Try Again.", fg="#FF6666")
        except BrokenPipeError:
            self.option_status.config(
                text="Connection lost. Please try again.", fg="#FF6666")

    def encrypt_password(self, password, salt):
        try:
            encrypted_password = hashpw(
                password.encode('utf-8'), salt.encode('utf-8'))
        except Exception as e:
            print(f"Error encrypting password:", e)
        return encrypted_password.decode('utf-8')

    def show_files(self):
        list_files = json.dumps(
            {'request': 'list_files', 'username': self.username})
        self.socket.send(list_files.encode())
        files = ''
        # receive the files from the server, if the buffer is less than 1024, break(we have received all the files)
        while True:
            buffer = self.socket.recv(1024).decode()
            files += buffer
            if len(buffer) < 1024:
                break

        self.showFilesGUI(files)

    def upload_file(self):
        file_path = filedialog.askopenfilename()  # open file dialog to select file
        if not file_path:
            self.showMessageBox("Error", "No file selected")
            return
        filename = file_path.split('/')[-1]  # get the filename only
        # check if the file is an executable file, if yes, show error message
        if filename.split('.')[-1] == 'exe':
            self.showMessageBox(
                "Error", "Cannot upload executable files")
            return
        try:
            with open(file_path, 'rb') as file:
                fileData = file.read()
            encrypted_data = self.encrypt_file(
                fileData, self.get_public_key(self.username))

            uploadValue = {
                'request': 'upload_file',
                'username': self.username,
                'overwrite': False,
                'filename': filename,
                'file_size': len(encrypted_data)
            }
            self.socket.send(json.dumps(uploadValue).encode())

            signal = self.socket.recv(1024).decode()
            # flag to continue upload if file already exists and user wants to overwrite
            continue_upload = False
            if signal == "FileExists":
                choice = self.showYesOrNoBox(
                    "Error", "File with name '" + filename + "' already exists.\n You need to delete the previous file to upload this file.\nDo you want to delete the previous file?")
                if not choice:
                    return
                # return true if file is deleted successfully
                if not self.delete_file([filename, self.username]):
                    return

                # set overwrite to True and upload the file again
                uploadValue['overwrite'] = True
                self.socket.send(json.dumps(uploadValue).encode())
                continue_upload = True

            if signal == "Ready" or continue_upload:
                if continue_upload:
                    continue_upload_signal = self.socket.recv(1024).decode()
                    if continue_upload_signal != "Ready":
                        self.showMessageBox(
                            "Error", "Error uploading file '" + filename + "'")
                        return

                # Send the file data to the server
                self.socket.sendall(encrypted_data)
                # Wait for the server to process the file
                returnMessage = self.socket.recv(1024).decode()
                if returnMessage == "Success":
                    self.showMessageBox(
                        "Success", "File '" + filename + "' uploaded successfully")
                else:
                    self.showMessageBox(
                        "Error", "Error uploading file '" + filename + "'")

        except Exception as e:
            print(f"Error uploading :", e)
            self.showMessageBox(
                "Error", "Error uploading file '" + filename + "'")

    def genRSAKeyPair(self, username):
        try:
            UserKeyStoragePath = KeyStoragePath + username + '/'
            if not os.path.exists(UserKeyStoragePath):
                os.makedirs(UserKeyStoragePath)
                # set the permission to read, write, execute for the user only
                os.chmod(UserKeyStoragePath, 0o700)
            else:
                return None
            random_generator = Random.new().read
            keyPair = RSA.generate(2048, random_generator)
            privateKey = keyPair.export_key()
            publicKey = keyPair.publickey().export_key()
            privateKey_path = UserKeyStoragePath + 'Private_Key.pem'
            with open(privateKey_path, 'wb') as file:
                file.write(privateKey)
                # set the permission to read only
                os.chmod(privateKey_path, 0o400)
            return publicKey

        except Exception as e:
            print(f"Error generating RSA key pair for user: ", e)

    def get_public_key(self, username):
        try:
            request = json.dumps(
                {'request': 'get_public_key', 'username': username})
            self.socket.send(request.encode())
            # get the response from the server, key length or UserNotFound
            response = self.socket.recv(1024).decode()
            if response == "UserNotFound":
                raise Exception("User not found")
            keyLength = int(response)
            self.socket.send("Ready".encode())
            publicKey = b''
            while len(publicKey) < keyLength:
                buffer = self.socket.recv(1024)
                publicKey += buffer
            return publicKey
        except Exception as e:
            return None

    def get_private_key(self):
        try:
            UserKeyStoragePath = KeyStoragePath + self.username + '/'
            with open(UserKeyStoragePath + 'Private_Key.pem', 'rb') as file:
                privateKey = file.read()
            return privateKey
        except Exception as e:
            print(f"Error getting private key:", e)

    def encrypt_file(self, fileData, key):
        encrypted_data = b''
        if not fileData:
            return encrypted_data
        try:
            # Generate a random AES key
            aesKey = Random.get_random_bytes(32)  # 256-bit key
            # create a cipher object using the random key with CBC mode
            aesCipher = AES.new(aesKey, AES.MODE_CBC)
            iv = aesCipher.iv

            # Add padding to the end of the file data to fit 16 bytes block size
            # Encrypt the file data with AES
            encrypted_file_data = aesCipher.encrypt(
                pad(fileData, AES.block_size))

            # Encrypt the AES key with RSA
            rsaKey = RSA.import_key(key)
            rsaCipher = PKCS1_cipher.new(rsaKey)
            encrypted_aes_key = rsaCipher.encrypt(aesKey)

            # Combine the encrypted AES key, IV, and encrypted file data
            encrypted_data = base64.b64encode(
                encrypted_aes_key + iv + encrypted_file_data)
        except Exception as e:
            print(f"Error encrypting file :", e)
        return encrypted_data

    def decrypt_file(self, encrypted_data, key):
        decrypted_data = b''
        if not encrypted_data:
            return decrypted_data
        try:
            encrypted_data = base64.b64decode(encrypted_data)

            # Extract the encrypted AES key, IV, and encrypted file data
            encrypted_aes_key = encrypted_data[:256]  # RSA 2048-bit key size
            iv = encrypted_data[256:272]  # AES block size is 16 bytes
            encrypted_file_data = encrypted_data[272:]

            # Decrypt the AES key with RSA
            rsaKey = RSA.import_key(key)
            rsaCipher = PKCS1_cipher.new(rsaKey)
            aesKey = rsaCipher.decrypt(encrypted_aes_key, sentinel=None)

            # Decrypt the file data with AES
            aesCipher = AES.new(aesKey, AES.MODE_CBC, iv)
            decrypted_data = unpad(aesCipher.decrypt(
                encrypted_file_data), AES.block_size)
        except ValueError as e:
            print(f"Error decrypting file: {e}")
        except Exception as e:
            print(f"Error decrypting file:", e)
        return decrypted_data

    def get_file_data(self, fileValues):
        if not fileValues:
            self.showErrorBox("Error", "No file selected")
            return
        filename = fileValues[0]
        fileOwner = fileValues[1]
        try:
            getRequestValue = {
                'request': 'get_file_data',
                'filename': filename,
                'username': self.username,
                'fileOwner': fileOwner
            }
            # send the request to the server
            self.socket.send(json.dumps(getRequestValue).encode())

            # Receive the file size from the server
            file_size = self.socket.recv(1024).decode()
            if file_size == "FileNotFound":
                self.showErrorBox(
                    "Error", "File '" + filename + "' not found")
                return
            elif file_size.isdigit():
                self.socket.send("Ready".encode())
                if file_size == "0":  # if the file size is 0, show error message
                    self.showMessageBox(
                        "Error", "File '" + filename + "' is empty")
                    return

                encrypted_data = b''
                while len(encrypted_data) < int(file_size):
                    encrypted_data += self.socket.recv(1024)

                private_key = self.get_private_key()  # get the private key of the user
                if not private_key:
                    self.showErrorBox("Error", "Error getting file data")
                    return
                decrypted_data = self.decrypt_file(  # decrypt the file data
                    encrypted_data, private_key)
                self.showFileDataGUI(filename, decrypted_data)

        except Exception as e:
            print(f"Error getting file:", e)

    def edit_file(self, fileValues):
        if not fileValues:
            self.showErrorBox("Error", "No file selected to edit")
            return
        fileOwner = fileValues[1]
        filename = fileValues[0]
        fileSharedTo = fileValues[2]
        if not fileValues[1] == self.username:
            self.showErrorBox(
                "Error", "You are not the owner of the file.\nNo permission to edit the file.")
            return
        self.showMessageBox(
            "Edit File", "Please select the new file to replace the existing file")

        # open file dialog to select file, to replace the existing file
        filePath = filedialog.askopenfilename()
        if not filePath:
            self.showMessageBox("Error", "No new file selected")
            return
        with open(filePath, 'rb') as file:
            fileData = file.read()
        encrypted_data = self.encrypt_file(
            fileData, self.get_public_key(self.username))

        # get the pk of the shared users
        fileSharedTo = fileSharedTo.split(',')
        public_keys = {}
        for user in fileSharedTo:
            if user != '':
                # get the public keys of the shared users
                public_keys[user] = self.get_public_key(user)
        # get the filename of the new file
        newFilename = filePath.split('/')[-1]
        request = {
            'request': 'edit_file',
            'username': self.username,
            'filename': filename,
            'newFilename': newFilename,
            'fileSize': len(encrypted_data),
            'fileSharedTo': fileSharedTo  # shared users
        }
        # send the request to the server
        self.socket.send(json.dumps(request).encode())
        signal = self.socket.recv(1024).decode()

        if signal == "FileNameExists":
            self.showErrorBox(
                "Error", "Detected filename change, but the new filename '" + newFilename + "' already exists.\nYou need to rename it to upload new version of this file.")
            return
        if signal == "Ready":
            self.socket.sendall(encrypted_data)
            if self.socket.recv(1024).decode() == "SuccessOwner":

                # send the file to shared users
                for user in fileSharedTo:
                    if user == '':
                        continue
                    encrypted_data = self.encrypt_file(
                        fileData, public_keys[user])
                    self.socket.sendall(encrypted_data)

                returnMessage = self.socket.recv(1024).decode()
                if returnMessage == "Success":
                    self.showMessageBox(
                        "Success", "File '" + filename + "' edited successfully")
                else:
                    self.showMessageBox(
                        "Error", "Error editing file '" + filename + "'")
        else:
            self.showMessageBox(
                "Error", "Error editing file '" + filename + "'")

    def delete_file(self, fileValues):
        '''
        return: True if file is deleted successfully, False otherwise
        '''
        if not fileValues:
            self.showErrorBox("Error", "No file selected")
            return False
        filename = fileValues[0]
        fileOwner = fileValues[1]

        try:
            if not self.showYesOrNoBox("Delete File", "Are you sure you want to delete the file '" + filename + "'?"):
                return False
            # Check if the user is the owner of the file, if not drop the permission to read
            deleteValue = {
                'request': 'delete_file',
                'permission': False,
                'filename': filename,
                'fileOwner': fileOwner,
                'username': self.username
            }
            if fileOwner != self.username:  # if the user is not the owner of the file, drop the permission to read
                choice = self.showYesOrNoBox(
                    "Error", "You don't have permission to delete this file, but you can still drop your permission to read.\n Continue?")
                if choice:
                    # send the request to the server
                    self.socket.send(json.dumps(deleteValue).encode())
                    message = self.socket.recv(1024).decode()
                    if message == "Success":
                        self.showMessageBox(
                            "Success", "File '" + filename + "'s permission dropped successfully")
                        return True
                    else:
                        self.showErrorBox(
                            "Error", 'Unable to drop permission to read for file ' + filename)
                return False

            else:  # if the user is the owner of the file, delete the file
                deleteValue['permission'] = True
                self.socket.send(json.dumps(deleteValue).encode())
                message = self.socket.recv(1024).decode()
                if message == "Success":
                    self.showMessageBox(
                        "Success", "File '" + filename + "' deleted successfully")
                    return True
                raise Exception()
            return False

        except Exception as e:
            print(f"Error deleting file '" + filename + "':", e)
            self.showMessageBox(
                "Error", "Error deleting file '" + filename + "'")
            return False

    def share_file(self, fileShareTo, fileValues):

        if not fileValues:
            self.showErrorBox("Error", "No file selected")
            return
        filename = fileValues[0]
        fileOwner = fileValues[1]

        if not fileShareTo:
            self.showErrorBox("Error", "No username entered")
            return
        if not filename or not fileOwner:
            self.showErrorBox("Error", "No file selected")
            return
        if fileShareTo == self.username:
            self.showErrorBox("Error", "You cannot share a file with yourself")
            return
        if fileOwner != self.username:
            self.showErrorBox("Error", "You are not the owner of the file")
            return
        share_user_public_key = self.get_public_key(
            fileShareTo)  # get the public key of the user
        if not share_user_public_key:
            self.showErrorBox("Error", "User '" + fileShareTo + "' not found")
            return
        request = json.dumps(
            {'request': 'share_file', 'fileOwner': fileOwner, 'filename': filename, 'fileShareTo': fileShareTo})
        self.socket.send(request.encode())  # send the request to the server
        message = self.socket.recv(1024).decode()
        match message:
            case "UserNotFound":
                self.showErrorBox("Error", "User '" +
                                  fileShareTo + "' not found")
            case "NotOwner":
                self.showErrorBox(
                    "Error", "You are not the owner of the file '" + filename + "'")
            case "FileNotFound":
                self.showErrorBox("Error", "File '" + filename + "' not found")
            case message if message.isdigit():  # if message can be cast to numbers, which is the file size
                self.socket.send("Ready".encode())
                fileDataLength = int(message)
                data_to_decrypt = b''
                while len(data_to_decrypt) < fileDataLength:
                    buffer = self.socket.recv(1024)
                    data_to_decrypt += buffer

                private_key = self.get_private_key()
                if not private_key:
                    self.showErrorBox(
                        "Error", "Error sharing file '" + filename + "'")
                    return

                fileData = self.decrypt_file(  # decrypt the file data with owner private key
                    data_to_decrypt, private_key)

                encrypted_data = self.encrypt_file(  # encrypt the file data with the shared user public key
                    fileData, share_user_public_key)

                self.socket.sendall(encrypted_data)

                if self.socket.recv(1024).decode() == "Shared":
                    self.showMessageBox("Success", "File '" + filename +
                                        "' shared with '" + fileShareTo + "' successfully.")
                else:
                    self.showErrorBox(
                        "Error", "Error when sharing file '" + filename + "'")
            case "Unshared":
                self.showMessageBox(
                    "Success", "File '" + filename + "' unshared with '" + fileShareTo + "' successfully.")
            case _:
                self.showErrorBox(
                    "Error", "Error sharing file '" + filename + "'")

    def download_file(self, fileValues):
        if not fileValues:
            self.showErrorBox("Error", "No file selected")
            return
        filename = fileValues[0]
        fileOwner = fileValues[1]
        try:
            if self.username != fileOwner:
                self.showErrorBox(
                    "Error", "You are not the owner of the file.\nNo permission to download the file.")
                return
            downloadValue = {
                'request': 'download_file',
                'filename': filename,
                'username': self.username
            }
            self.socket.send(json.dumps(downloadValue).encode())

            # Receive the file size from the server
            file_size = self.socket.recv(1024).decode()
            if file_size == "notFound":
                self.showErrorBox(
                    "Error", "File '" + filename + "' not found")
                return

            file_size = int(file_size)

            file_path = filedialog.asksaveasfilename(  # open save file dialog to save the file
                initialfile=filename)
            if not file_path:
                self.showMessageBox("Error", "No file path selected.")
                self.socket.send("Cancel".encode())
                return

            # Send the signal to start sent the file
            self.socket.send("Ready".encode())

            # Receive the file data from the server
            file_data = b''
            while len(file_data) < file_size:
                file_data += self.socket.recv(1024)
            decrypted_data = self.decrypt_file(  # decrypt the file data with the user private key
                file_data, self.get_private_key())

            with open(file_path, 'wb') as file:
                file.write(decrypted_data)
                file.close()
            self.showMessageBox(
                "Success", "File '" + filename + "' downloaded successfully")

        except Exception as e:
            print(f"Error downloading file '" + filename + "':", e)
            self.showMessageBox(
                "Error", "Error downloading file '" + filename + "'")

    def logout(self):
        if self.showYesOrNoBox("Logout", "Are you sure you want to logout?"):
            try:
                self.baseMainFrame.destroy()  # destroy the main frame
                if hasattr(self, 'loggingWindow') and self.loggingWindow.winfo_exists():
                    self.loggingWindow.destroy()
                    delattr(self, 'loggingWindow')
            except Exception as e:
                pass

            logout_message = json.dumps(
                {'request': 'logout', 'username': self.username})
            self.socket.send(logout_message.encode())
            message = self.socket.recv(1024).decode()
            if message == "Success":
                self.showMessageBox("Success", "Logged out successfully")
                self.start()
            else:
                self.showErrorBox("Error", "Error logging out")

    def get_logging(self):
        try:
            message = json.dumps(
                {'request': 'get_logging', 'username': self.username})
            self.socket.send(message.encode())

            # Receive the length of the loggings from the server
            logging_length = int(self.socket.recv(1024).decode())

            # Send the signal to start downloading the loggings
            self.socket.send("Ready".encode())

            loggingData = b''
            while len(loggingData) < logging_length:
                buffer = self.socket.recv(1024)
                loggingData += buffer

            loggingData = loggingData.decode().strip().split('\n')
            for log in loggingData:
                if not log:
                    loggingData.remove(log)
            loggingDataArray = [json.loads(data) for data in loggingData]
            self.showLoggingsGUI(loggingDataArray)
        except Exception as e:
            print(f"Error getting loggings:", e)
            self.showErrorBox("Error", "Error getting loggings")

    '''
    Following is GUI code.
    
    '''

    def genNewWindow(self, title, width, height):
        window = tk.Tk()
        window.title(title)
        window.minsize(width, height)
        window.resizable(width=True, height=True)
        # Center the window
        screen_width = window.winfo_screenwidth()
        screen_height = window.winfo_screenheight()
        position_top = int(screen_height / 2 - height / 2)
        position_right = int(screen_width / 2 - width / 2)
        window.geometry(
            f'{width}x{height}+{position_right}+{position_top}')
        return window

    def on_closing(self):  # close the window and close the socket connection
        if self.showYesOrNoBox("Exit", "Are you sure you want to exit?"):
            try:
                disconnectMessage = json.dumps(
                    {'request': 'disconnect', 'username': self.username})
                self.socket.send(disconnectMessage.encode())
                self.socket.close()
                if hasattr(self, 'loggingWindow') and self.loggingWindow.winfo_exists():
                    self.loggingWindow.destroy()
                if hasattr(self, 'fileDataWindow') and self.fileDataWindow.winfo_exists():
                    self.fileDataWindow.destroy()

            except Exception as e:
                pass
            finally:
                self.window.destroy()

    def LoginGUI(self):  # show the GUI for login
        try:
            if not hasattr(self, 'window') or self.window == None:
                self.window = self.genNewWindow(
                    'File Storage System', 900, 500)
            if hasattr(self, 'baseLoginOptionFrame') and self.baseLoginOptionFrame.winfo_exists():
                self.baseLoginOptionFrame.destroy()
            if hasattr(self, 'baseMainFrame') and self.baseMainFrame.winfo_exists():
                self.baseMainFrame.destroy()
            if hasattr(self, 'loggingWindow') and self.loggingWindow.winfo_exists():
                self.loggingWindow.destroy()
        except Exception as e:
            pass

        self.baseLoginFrame = tk.Frame(self.window)
        self.baseLoginFrame.pack(expand=True, fill='both')

        frame1 = tk.Frame(self.baseLoginFrame)
        frame2 = tk.Frame(self.baseLoginFrame)
        frame1.pack(expand=True, fill='both')
        frame2.pack(expand=True, fill='both')

        frame1.columnconfigure(0, weight=1)
        frame1.columnconfigure(1, weight=2)
        frame1.rowconfigure(0, weight=1)
        frame1.rowconfigure(1, weight=1)

        frame2.columnconfigure(0, weight=1)
        frame2.columnconfigure(1, weight=1)
        frame2.columnconfigure(2, weight=1)
        frame2.rowconfigure(0, weight=1)
        frame2.rowconfigure(1, weight=1)

        username_label = tk.Label(frame1, text="Username:")
        password_label = tk.Label(frame1, text="Password:")
        username_entry = tk.Entry(frame1)
        password_entry = tk.Entry(frame1, show="*")
        self.login_status = tk.Label(
            frame2, text="Please login with your username and password")

        register_button = tk.Button(frame2, text="Register",
                                    command=lambda: [self.showLoginOptionGUI('Register')])
        reset_password_button = tk.Button(frame2, text="Reset Password",
                                          command=lambda: [self.showLoginOptionGUI('Reset')])
        login_button = tk.Button(frame2, text="Login",
                                 command=lambda: self.login(username_entry.get(),
                                                            password_entry.get()))
        login_button.bind("<Return>", lambda event: self.login(username_entry.get(),
                                                               password_entry.get()))
        pad = 10
        username_label.grid(row=0, column=0, padx=pad,
                            pady=pad, sticky="WE")
        username_entry.grid(row=0, column=1, padx=pad,
                            pady=pad, sticky="WE")
        password_label.grid(row=1, column=0, padx=pad,
                            pady=pad, sticky="WE")
        password_entry.grid(row=1, column=1, padx=pad,
                            pady=pad, sticky="WE")

        register_button.grid(row=0, column=0, padx=pad + 20,
                             pady=pad + 20, sticky="WE")
        reset_password_button.grid(row=0, column=1,
                                   padx=pad + 20, pady=pad + 20, sticky="WE")
        login_button.grid(row=0, column=2,
                          padx=pad + 20, pady=pad + 20, sticky="WE")
        self.login_status.grid(row=1, column=0, columnspan=3,
                               padx=pad + 20, pady=pad + 20, sticky="NSWE")

        self.window.mainloop()

    def OTP_QR_UI(self):
        size = 250, 250
        if hasattr(self, 'baseLoginFrame') and self.baseLoginFrame.winfo_exists():
            self.baseLoginFrame.destroy()
        if hasattr(self, 'baseLoginOptionFrame') and self.baseLoginOptionFrame.winfo_exists():
            self.baseLoginOptionFrame.destroy()
        self.baseMainFrame = tk.Frame(self.window)
        self.baseMainFrame.pack(expand=True, fill='both')

        frame1 = tk.Frame(self.baseMainFrame)  # QR code
        frame1.pack(expand=True, fill='both')
        frame2 = tk.Frame(self.baseMainFrame)  # Input
        frame2.pack(expand=True, fill='both')

        frame1.columnconfigure(0, weight=1)
        frame2.columnconfigure(0, weight=1)
        frame2.columnconfigure(1, weight=1)
        frame2.columnconfigure(2, weight=1)
        frame2.rowconfigure(0, weight=1)
        frame2.rowconfigure(1, weight=1)

        img = qrcode.make(self.getOTP(self.username))
        with open('qr.png', 'wb') as qr:
            img.save(qr)

        image = Image.open('qr.png')
        image.thumbnail(size, Image.Resampling.LANCZOS)
        tkImage = ImageTk.PhotoImage(image)
        os.remove('qr.png')

        button = tk.Label(frame1, image=tkImage)
        button.grid(pady=20, padx=20, column=0)

        otpLabel = tk.Label(frame2, text="Verification Code:")
        otpEntry = tk.Entry(frame2)
        otpButton = tk.Button(frame2, text="Verify", command=lambda: [
                              self.verifyOTP(self.username, otpEntry.get())])
        otpHint = tk.Label(
            frame2, text="Please scan the QR code using Google Authenticator and input the verification code")

        otpLabel.grid(row=0, column=0)
        otpEntry.grid(row=0, column=1)
        otpButton.grid(row=0, column=2)
        otpHint.grid(row=1, columnspan=3)
        self.window.mainloop()

    def QR_UI(self):
        size = 250, 250
        try:
            if hasattr(self, 'baseMainFrame') and self.baseMainFrame.winfo_exists():
                self.baseMainFrame.destroy()
        except Exception as e:
            pass
        self.baseMainFrame = tk.Frame(self.window)
        self.baseMainFrame.pack(expand=True, fill='both')

        frame1 = tk.Frame(self.baseMainFrame)  # QR code
        frame1.pack(expand=True, fill='both')

        frame1.columnconfigure(0, weight=1)
        frame1.rowconfigure(0, weight=1)
        frame1.rowconfigure(1, weight=1)
        frame1.rowconfigure(2, weight=1)

        img = qrcode.make(self.getOTP(self.username))
        with open('qr.png', 'wb') as qr:
            img.save(qr)

        image = Image.open('qr.png')
        image.thumbnail(size, Image.Resampling.LANCZOS)
        tkImage = ImageTk.PhotoImage(image)
        os.remove('qr.png')

        hint = tk.Label(
            frame1, text="Please use Google Authenticator to scan this QR code.")
        hint.grid(pady=20, padx=20, row=0, column=0)

        QR_code = tk.Label(frame1, image=tkImage)
        QR_code.grid(pady=20, padx=20, row=1, column=0)

        cancel = tk.Button(frame1, text="Cancel",
                           command=self.show_files)
        cancel.grid(pady=20, padx=20, row=2, column=0)

        self.window.mainloop()

    def OTP_UI(self):
        try:
            if hasattr(self, 'baseLoginFrame') and self.baseLoginFrame.winfo_exists():
                self.baseLoginFrame.destroy()
            if hasattr(self, 'baseLoginOptionFrame') and self.baseLoginOptionFrame.winfo_exists():
                self.baseLoginOptionFrame.destroy()
        except Exception as e:
            pass

        self.baseMainFrame = tk.Frame(self.window)
        self.baseMainFrame.pack(expand=True, fill='both')

        frame1 = tk.Frame(self.baseMainFrame)
        frame1.pack(expand=True, fill='both')

        frame1.columnconfigure(0, weight=1)
        frame1.columnconfigure(1, weight=1)
        frame1.columnconfigure(2, weight=1)
        frame1.rowconfigure(0, weight=1)
        frame1.rowconfigure(1, weight=1)

        otpLabel = tk.Label(frame1, text="Verification Code:")
        otpEntry = tk.Entry(frame1)
        optButton = tk.Button(frame1, text="Verify", command=lambda: [
                              self.verifyOTP(self.username, otpEntry.get())])
        optHint = tk.Label(
            frame1, text="Welcome " + self.username + "! Please input the verification code from Google Authenticator")

        otpLabel.grid(row=1, column=0)
        otpEntry.grid(row=1, column=1)
        optButton.grid(row=1, column=2)
        optHint.grid(row=0, columnspan=3)

        self.window.mainloop()

    # this used for register and reset pwd
    def showLoginOptionGUI(self, option):
        if hasattr(self, 'baseLoginFrame') and self.baseLoginFrame.winfo_exists():
            self.baseLoginFrame.destroy()
        self.baseLoginOptionFrame = tk.Frame(self.window)
        self.baseLoginOptionFrame.pack(expand=True, fill='both')

        # Corrected frame reference
        frame1 = tk.Frame(self.baseLoginOptionFrame)
        # Corrected frame reference
        frame2 = tk.Frame(self.baseLoginOptionFrame)
        frame1.pack(expand=True, fill='both')
        frame2.pack(expand=True, fill='both')

        frame1.columnconfigure(0, weight=1)
        frame1.columnconfigure(1, weight=2)
        frame1.rowconfigure(0, weight=1)
        frame1.rowconfigure(1, weight=1)
        frame1.rowconfigure(2, weight=1)
        frame1.rowconfigure(3, weight=1)

        frame2.columnconfigure(0, weight=1)
        frame2.columnconfigure(1, weight=1)
        frame2.rowconfigure(0, weight=1)
        frame2.rowconfigure(1, weight=1)

        username_label = tk.Label(frame1, text="Username:")
        password_label = tk.Label(frame1, text="Password:")
        confirm_password_label = tk.Label(frame1, text="Confirm Password:")
        verifyCode_label = tk.Label(frame1, text="Verification Code:")
        username_entry = tk.Entry(frame1)
        password_entry = tk.Entry(frame1, show="*")
        confirm_password_entry = tk.Entry(frame1, show="*")
        verifyCode_entry = tk.Entry(frame1)

        verifyCode_label.grid(row=3, column=0, padx=10,
                              pady=10, sticky="WE")
        verifyCode_entry.grid(row=3, column=1, padx=10,
                              pady=10, sticky="WE")

        if option == 'Register':
            verifyCode_entry.grid_remove()
            verifyCode_label.grid_remove()
            self.option_status = tk.Label(
                frame2, text="Please register with your username and password")
            enter_button = tk.Button(frame2, text="Register",
                                     command=lambda: [self.register(username_entry.get(),
                                                                    password_entry.get(),
                                                                    confirm_password_entry.get())])
        elif option == 'Reset':
            self.display(verifyCode_label, verifyCode_entry)
            self.option_status = tk.Label(
                frame2, text="Please enter your username and new password")
            enter_button = tk.Button(frame2, text="Reset",
                                     command=lambda: [self.reset_password(username_entry.get(),
                                                                          password_entry.get(),
                                                                          confirm_password_entry.get(),
                                                                          verifyCode_entry.get())])
        cancel_button = tk.Button(frame2, text="Cancel",
                                  command=self.LoginGUI)

        pad = 10
        username_label.grid(row=0, column=0, padx=pad,
                            pady=pad, sticky="WE")
        username_entry.grid(row=0, column=1, padx=pad,
                            pady=pad, sticky="WE")
        password_label.grid(row=1, column=0, padx=pad,
                            pady=pad, sticky="WE")
        password_entry.grid(row=1, column=1, padx=pad,
                            pady=pad, sticky="WE")
        confirm_password_label.grid(row=2, column=0, padx=pad,
                                    pady=pad, sticky="WE")
        confirm_password_entry.grid(row=2, column=1, padx=pad,
                                    pady=pad, sticky="WE")
        cancel_button.grid(row=0, column=0, padx=pad + 20,
                           pady=pad + 20, sticky="WE")
        enter_button.grid(row=0, column=1,
                          padx=pad + 20, pady=pad + 20, sticky="WE")
        self.option_status.grid(row=1, column=0, columnspan=2,
                                padx=pad + 20, pady=pad + 20, sticky="NSWE")

        self.window.mainloop()

    # show the files of the user
    def showFilesGUI(self, files):

        # if self.baseLoginFrame exists, hide it
        if hasattr(self, 'baseMainFrame') and self.baseMainFrame.winfo_exists():
            self.baseMainFrame.destroy()
        if hasattr(self, 'baseLoginOptionFrame') and self.baseLoginOptionFrame.winfo_exists():
            self.baseLoginOptionFrame.destroy()

        self.baseMainFrame = tk.Frame(self.window)
        self.baseMainFrame.pack(expand=True, fill='both')
        self.baseMainFrame.columnconfigure(0, weight=1)
        self.baseMainFrame.rowconfigure(0, weight=3)
        self.baseMainFrame.rowconfigure(1, weight=1)
        self.baseMainFrame.rowconfigure(2, weight=1)

        # Frame 1 for the file list
        frame1 = tk.Frame(self.baseMainFrame)
        frame1.grid(row=0, column=0, padx=10, pady=10, sticky="NSWE")
        frame1.columnconfigure(0, weight=1)
        frame1.rowconfigure(0, weight=1)
        frame1.rowconfigure(1, weight=1)
        frame1.rowconfigure(2, weight=10)

        messageLabel = tk.Label(
            frame1, text="Here are the files "+self.username+" have:")
        lastLoginLabel = tk.Label(
            frame1, text="Welcome " + self.username + ", Last logged in time: " + self.lastLoginTime)
        columns = ('fileName', 'owner', 'Shared_To',
                   'modified_Since', 'fileUploadTime')
        tree = ttk.Treeview(frame1, columns=columns,
                            show="headings", selectmode='browse')

        tree.column(columns[0], anchor='center')
        tree.column(columns[1], anchor='center', width=100)
        tree.column(columns[2], anchor='center', width=100)
        tree.column(columns[3], anchor='center', width=100)
        tree.column(columns[4], anchor='center', width=100)

        tree.heading(columns[0], text='File Name')
        tree.heading(columns[1], text='Owner')
        tree.heading(columns[2], text='Shared To')
        tree.heading(columns[3], text='Modified Since')
        tree.heading(columns[4], text='File Upload Time')

        lastLoginLabel.grid(row=0, column=0, padx=10, pady=5, sticky="nsew")
        messageLabel.grid(row=1, column=0, padx=10, pady=5, sticky="nsew")
        tree.grid(row=2, column=0, padx=10, pady=10, sticky="nsew")

        # Frame 2 for the buttons
        frame2 = tk.Frame(self.baseMainFrame)
        frame2.grid(row=1, column=0, padx=10, pady=0, sticky="SWE")
        frame2.rowconfigure(0, weight=1)
        frame2.rowconfigure(1, weight=1)
        frame2.rowconfigure(2, weight=1)

        frame2.columnconfigure(0, weight=1)
        frame2.columnconfigure(1, weight=1)
        frame2.columnconfigure(2, weight=1)
        frame2.columnconfigure(3, weight=1)

        button_pad = 5
        # Add a button to upload a file
        upload_button = tk.Button(frame2, text="Upload File",
                                  command=lambda: [self.upload_file(), self.show_files()])
        upload_button.grid(row=0, column=0, padx=button_pad,
                           pady=button_pad, sticky="WE")

        # Add a button to show a file
        show_button = tk.Button(frame2, text="Show File",
                                command=lambda: [self.get_file_data(tree.item(tree.selection())['values'])])
        show_button.grid(row=0, column=1, padx=button_pad,
                         pady=button_pad, sticky="WE")

        # Add a button to edit a file
        edit_button = tk.Button(frame2, text="Edit File",
                                command=lambda: [self.edit_file(tree.item(tree.selection())['values']),
                                                 self.show_files()])
        edit_button.grid(row=0, column=2, padx=button_pad,
                         pady=button_pad, sticky="WE")

        # Add a button to download the file
        download_button = tk.Button(frame2, text="Download File",
                                    command=lambda: [self.download_file(tree.item(tree.selection())['values'])])
        download_button.grid(row=0, column=3, padx=button_pad,
                             pady=button_pad, sticky="WE")

        # Add a button to delete a file
        delete_button = tk.Button(frame2, text="Delete File",
                                  command=lambda: [self.delete_file(tree.item(tree.selection())['values']),
                                                   self.show_files()])
        delete_button.grid(row=1, column=0, padx=button_pad,
                           pady=button_pad, sticky="WE")

        # Add a button to share/unshare the file
        share_button = tk.Button(frame2, text="Share/Unshare File",
                                 command=lambda: [self.share_file(simpledialog.askstring(
                                     "Input", "Enter the username you want to share the file with:"),
                                     tree.item(tree.selection())['values']),
                                     self.show_files()])
        share_button.grid(row=1, column=1, padx=button_pad,
                          pady=button_pad, sticky="WE")

        # Add a button to show the QR code
        QR_button = tk.Button(frame2, text="QR Code", command=self.QR_UI)
        QR_button.grid(row=1, column=2, padx=button_pad,
                       pady=button_pad, sticky="WE")

        # If admins, add a button to view loggings
        if self.role == 'admin':
            get_logging_button = tk.Button(frame2, text="View Loggings",
                                           command=self.get_logging)
            get_logging_button.grid(row=1, column=3, padx=button_pad,
                                    pady=button_pad, sticky="WE")

        # Add a button to logout
        logout_button = tk.Button(frame2, text="Logout",
                                  command=self.logout)
        logout_button.grid(row=2, column=0, columnspan=2,
                           padx=button_pad, pady=button_pad, sticky="WE")

        # Add a button to refresh the file list
        refresh_button = tk.Button(frame2, text="Refresh",
                                   command=self.show_files)
        refresh_button.grid(row=2, column=2, columnspan=2,
                            padx=button_pad, pady=button_pad, sticky="WE")

        # Add the files to the tree
        if files == '[]':
            messageLabel.config(text="You don't have any files stored yet.")
        else:
            for file in json.loads(files):
                tree.insert('', 'end', values=file)
        self.window.protocol("WM_DELETE_WINDOW", self.on_closing)
        self.window.mainloop()

    # show the loggings, for admin only
    def showLoggingsGUI(self, loggings):
        self.loggingWindow = self.genNewWindow('Loggings', 1000, 400)

        self.loggingWindow.columnconfigure(0, weight=1)
        self.loggingWindow.rowconfigure(0, weight=1)

        # Create a treeview to display the loggings
        columns = ('Time', 'Action', 'IP', 'Username',
                   'Filename', 'FileID', 'Status', 'Message')
        tree = ttk.Treeview(self.loggingWindow, columns=columns,
                            show="headings", selectmode='browse',
                            yscrollcommand=True, xscrollcommand=True)
        for col in columns:
            tree.heading(col, text=col)
            tree.column(col, anchor='center', width=100)
            if col == 'Time' or col == 'Action':
                tree.column(col, width=150)

        tree.grid(row=0, column=0, padx=10, pady=10, sticky="nsew")

        for logging in loggings:
            tree.insert('', 'end', values=(
                logging.get('Time', ''),
                logging.get('Action', ''),
                logging.get('IP', ''),
                logging.get('Username', ''),
                logging.get('Filename', ''),
                logging.get('FileID', ''),
                logging.get('Status', ''),
                logging.get('Message', '')))

        self.loggingWindow.mainloop()

    # show the file data
    def showFileDataGUI(self, filename, fileData):

        self.fileDataWindow = self.genNewWindow('File Data', 600, 400)
        self.fileDataWindow.columnconfigure(0, weight=1)
        self.fileDataWindow.rowconfigure(0, weight=1)
        self.fileDataWindow.rowconfigure(1, weight=9)
        self.fileDataWindow.rowconfigure(2, weight=1)
        self.fileDataWindow.rowconfigure(3, weight=1)

        textLabel = tk.Label(self.fileDataWindow,
                             text="File '" + filename + "'s data:")
        warningLable = tk.Label(
            self.fileDataWindow, text="Warning: Some file extension may not be readable.")

        cancelled_button = tk.Button(self.fileDataWindow, text="Close",
                                     command=self.fileDataWindow.destroy)

        textLabel.grid(row=0, column=0, padx=10, pady=10, sticky="nsew")

        warningLable.grid(row=2, column=0, padx=10, pady=10, sticky="nsew")

        cancelled_button.grid(row=3, column=0, padx=10, pady=10, sticky="nsew")

        text = tk.Text(self.fileDataWindow, wrap='word')
        if filename.split('.')[-1] == 'txt':
            text.insert(tk.END, fileData.decode())
        else:
            text.insert(tk.END, fileData)
        text.config(state=tk.DISABLED)
        text.grid(row=1, column=0,
                  padx=10, pady=10, sticky="nsew")

        self.fileDataWindow.mainloop()

    def showMessageBox(self, title, message):
        messagebox.showinfo(title, message)

    def showErrorBox(self, title, message):
        messagebox.showerror(title, message)

    def showYesOrNoBox(self, title, message):
        return messagebox.askyesno(title, message)

    def remove(self, widget1, widget2):
        widget1.grid_remove()
        widget2.grid_remove()

    def display(self, widget1, widget2):
        widget1.grid(row=3, column=0, padx=10,
                     pady=10, sticky="WE")
        widget2.grid(row=3, column=1, padx=10,
                     pady=10, sticky="WE")


if __name__ == "__main__":

    # Create and start the client
    client = FileClient(SERVER_HOST, SERVER_PORT)
    client.start()

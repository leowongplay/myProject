import socket
import threading
import os
import datetime as dt

# Date format for the response, e.g.(Thu, 01 Jan 1970 00:00:00 GMT)
dateFormate = "%a, %d %b %Y %H:%M:%S GMT"
maxRequests = 100  # Maximum 100 requests
timeout = 15  # Maximum 15 seconds
acceptTxtTypes = {"html", "txt"}    # Acceptable text file types
acceptImageTypes = {"jpg", "jpeg", "png"}   # Acceptable image file types


def isPortHosting(hostName, port):
    # Check if the port is used or not, if used +1 and recheck, if not return and use it.
    so = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    while so.connect_ex((hostName, port)) == 0:
        port += 1
        so = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    so.close()
    return port


def writeLogFileRequest(addr, method, fileName, connect, ifModifiedSince):
    # Write the log file for the request
    try:
        clientHostname = socket.gethostbyaddr(addr[0])[0]
        clientIp = addr[0]
        logMess = "[Client request from (" + \
            clientHostname + ", " + clientIp + ")]"
        logMess += "[Connection: " + connect + "]"
        logMess += "[Method: " + method + "]"
        logMess += "[Request file name: " + fileName + "]"
        logMess += "[Date: " + accessTime + "]"
        if ifModifiedSince != "N/A":
            logMess += "[If-Modified-Since: " + ifModifiedSince + "]\n"
        else:
            logMess += "\n"
        f = open("log.txt", "a")
        f.write(logMess)
        f.close()
    except Exception:
        print("Error writing to log file.")


def writeLogFile(addr, fileName, header):
    # Write the log file for the response
    try:
        header = header.splitlines()
        clientHostname = socket.gethostbyaddr(addr[0])[0]
        clientIp = addr[0]
        logMess = "[Server response to (" + \
            clientHostname + ", " + clientIp + ")]"
        logMess += "[StatesCode: " + header[0] + "]"
        for i in range(1, len(header)):
            if header[i] != "":
                logMess += "[" + header[i] + "]"
        logMess += "\n"
        f = open("log.txt", "a")
        f.write(logMess)
        f.close()
    except Exception:
        print("Error writing to log file.")


def getTime(fileName):
    # Get the time of the file, if the file is not found return the current time
    try:
        if fileName != "N/A" and os.path.exists(fileName):
            return dt.datetime.strftime(dt.datetime.fromtimestamp(os.path.getmtime(fileName)), dateFormate)
        return dt.datetime.now().strftime(dateFormate)
    except Exception as e:
        return "N/A"


def strToDateTime(timeStr):
    # Convert the string time to datetime
    return dt.datetime.strptime(timeStr, dateFormate)


accessTime = getTime("N/A")


def headerGenerate(statusCode, path, fileLen, connectionPeriod):
    # Generate the header for the response

    header = ''
    # Status code
    # 200: OK
    # 404: File Not Found
    # 400: Bad Request
    # 304: Not Modified
    if statusCode == 200:
        header += 'HTTP/1.1 200 OK\n'
    elif statusCode == 404:
        header += 'HTTP/1.1 404 File Not Found\n'
    elif statusCode == 400:
        header += 'HTTP/1.1 400 Bad Request\n'
    elif statusCode == 304:
        header += 'HTTP/1.1 304 Not Modified\n'
    accessTime = getTime("N/A")
    header += "Date: " + accessTime + "\n"
    header += "Connection: " + connectionPeriod + "\n"
    if connectionPeriod != "close":
        header += "Keep-Alive: timeout=" + \
            str(timeout) + ", max=" + str(maxRequests) + "\n"

    if statusCode == 200 or statusCode == 304:
        header += "Last-Modified: " + getTime(path) + "\n"
        header += "Content-Length: " + str(fileLen) + "\n"
        temp = path.split(".")
        extension = temp[len(temp) - 1]
        if extension in acceptTxtTypes:
            header += "Content-Type: text/html\n\n"
        elif extension in acceptImageTypes:
            header += "Content-Type: image/" + extension + "\n\n"
        else:
            headerGenerate(400, path, fileContent, connectionPeriod)

    return header


def serverRun(conn, addr):
    # Run the server

    try:
        countRequest = 0
        conn.settimeout(timeout)
        while True:
            statusCode = 404
            if countRequest > maxRequests:  # Maximum 100 requests
                print("Maximum " + maxRequests + " requests , Connection to " +
                      str(addr) + " is closed.")
                conn.close()
                break
            request = conn.recv(4096).decode()  # Receive the request
            if not request:
                print("Connection from " + str(addr) + " closed.")
                conn.close()
                break
            if request != "":
                request = request.strip()  # Strip leading and trailing whitespace
                print("\nReceived request from " +
                      str(addr) + ": \n" + request + '\n')
                line = request.split('\n')
                fields = line[0].split(' ')
                requestMethod = fields[0]
                fileName = fields[1]
                fileContent = "N/A"
                connectionPeriod = "close"

                lines = request.splitlines()
                dictionData = {}

                for line in lines:  # Iterate over each line and split it into key-value pairs
                    if ":" in line:
                        # Split the line into key and value
                        key, value = line.split(":", 1)
                        key = key.strip()        # Strip leading whitespace
                        value = value.strip()
                        dictionData[key] = value        # Add to the dictionary

                if "Connection" in dictionData:
                    connectionPeriod = dictionData["Connection"]
                    if connectionPeriod != "close":
                        conn.settimeout(timeout)
                # Handling file name
                if fileName == "/":
                    fileName = "/index.html"  # Default file
                if not fileName.startswith("/"):
                    fileName = "/" + fileName
                ifModifiedSince = "N/A"

                if "If-Modified-Since" in dictionData:
                    ifModifiedSince = dictionData["If-Modified-Since"]
                # Write the request into the log file
                writeLogFileRequest(addr, requestMethod,
                                    fileName, connectionPeriod, ifModifiedSince)
                # Get the file path and extension
                path = "./srcFile" + fileName
                extension = fileName.split(".")[1]

                # Check if the request is valid
                if (requestMethod == "GET" or requestMethod == "HEAD") and\
                        (extension in acceptTxtTypes or extension in acceptImageTypes):
                    try:
                        # Open the file and get the content
                        if requestMethod == "GET":
                            if extension in acceptTxtTypes:  # Text file
                                file = open(path, "r")
                                fileContent = file.read()
                                header = headerGenerate(
                                    200, path, len(fileContent), connectionPeriod)
                                statusCode = 200
                            elif extension in acceptImageTypes:  # Image file
                                file = open(path, "rb")
                                fileContent = file.read()
                                header = headerGenerate(
                                    200, path, len(fileContent), connectionPeriod)
                                statusCode = 200
                            file.close()
                        else:  # HEAD request
                            file = open(path, "r")
                            header = headerGenerate(
                                200, path, len(fileContent), connectionPeriod)
                            statusCode = 200
                            file.close()

                        if ifModifiedSince != "N/A":  # Check if the file is modified
                            fileTime = getTime(path)
                            # If the file is not modified
                            if strToDateTime(ifModifiedSince) >= strToDateTime(fileTime):
                                header = headerGenerate(
                                    304, path, len(fileContent), connectionPeriod)
                                statusCode = 304
                    except FileNotFoundError:
                        header = headerGenerate(
                            404, path, len(fileContent), connectionPeriod)
                        statusCode = 404
                else:
                    header = headerGenerate(
                        400, path, len(fileContent), connectionPeriod)
                    statusCode = 400
                try:
                    # Send the header to the client

                    if requestMethod == "GET" and statusCode == 200:
                        if extension in acceptTxtTypes:
                            conn.sendall((header + fileContent).encode())
                        elif extension in acceptImageTypes:
                            conn.sendall(header.encode())
                            conn.sendall(fileContent)
                    else:
                        conn.sendall(header.encode())
                except BrokenPipeError:
                    print(
                        "Connection was closed by the client before the response could be sent.")

                # write the request into the log file
                writeLogFile(addr, fileName, header)
                # Close the connection if the connection period is close
                if connectionPeriod == "close":
                    print("Connection to: " + str(addr) + " is closed.")
                    conn.close()
                    break
                countRequest += 1
                print("Request completed, waiting for next request from: " + str(addr))
    except TimeoutError:  # Timeout error
        print("Timeout, connection to " + str(addr) + " is closed.")
        conn.close()

    except Exception as e:  # Other errors
        print("Error during processing the request.")
        print("Connection to " + str(addr) + " is closed.")
        conn.close()


# Set the host and port
host = "127.0.0.1"
port = 8080
try:
    port = isPortHosting(host, port)
    print("Server is running on host:port: " + host + ":" + str(port))
    print("Browser link: http://" + host + ":" + str(port) + "/")
except Exception:
    print("Port " + str(port) + " is unavailable.")
    exit(1)
try:
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    # Create socket object
    server_socket.bind((host, port))
    server_socket.listen(5)
    while True:
        # Accept the connection
        conn, addr = server_socket.accept()
        print("Received connection from " + str(addr))
        threading.Thread(target=serverRun, args=(
            conn, addr)).start()  # Start the server
except Exception:
    print("Error: Could not bind to port " + str(port))
    server_socket.close()
    exit(1)

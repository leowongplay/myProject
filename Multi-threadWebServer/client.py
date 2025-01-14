# Implements a simple HTTP client
import socket
# SERVER_HOST = '127.0.0.1'
# SERVER_PORT = 8080

SERVER_HOST = input('Enter the server host: ')
SERVER_PORT = int(input('Enter the server port: '))

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((SERVER_HOST, SERVER_PORT))
acceptTxtTypes = {"html", "txt"}
acceptImageTypes = {"jpg", "jpeg", "png"}
while True:
    try:
        print('Enter a request: ')
        lines = []
        while True:
            line = input()
            if line:
                lines.append(line)
            else:
                break
        request = "\n".join(lines)
        method = lines[0].split(" ")[0]
        filePath = lines[0].split(" ")[1]
        extension = "html"
        if "." in filePath:
            extension = filePath.split(".")[1]
        client_socket.send(request.encode())
        print('Server response:')
        response = "N/A"
        if extension in acceptImageTypes:
            response = client_socket.recv(4096)
            print(response)
            while response:
                print(response)
                response = client_socket.recv(4096)
        else:
            response = client_socket.recv(2048).decode()
            print(response)
        if not response:
            print('Connection closed.')
            break
    except:
        print('Client error: Connection had been closed.')
        break
client_socket.close()
